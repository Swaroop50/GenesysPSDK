//===============================================================================
//Genesys Platform SDK Samples
//===============================================================================

//===============================================================================
//Any authorized distribution of any copy of this code (including any related
//documentation) must reproduce the following restrictions, disclaimer and copyright
//notice:

//The Genesys name, trademarks and/or logo(s) of Genesys shall not be used to name
//(even as a part of another name), endorse and/or promote products derived from
//this code without prior written permission from Genesys Telecommunications
//Laboratories, Inc.

//The use, copy, and/or distribution of this code is subject to the terms of the Genesys
//Developer License Agreement.  This code shall not be used, copied, and/or
//distributed under any other license agreement.

//THIS CODE IS PROVIDED BY GENESYS TELECOMMUNICATIONS LABORATORIES, INC.
//("GENESYS") "AS IS" WITHOUT ANY WARRANTY OF ANY KIND. GENESYS HEREBY
//DISCLAIMS ALL EXPRESS, IMPLIED, OR STATUTORY CONDITIONS, REPRESENTATIONS AND
//WARRANTIES WITH RESPECT TO THIS CODE (OR ANY PART THEREOF), INCLUDING, BUT
//NOT LIMITED TO, IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
//PARTICULAR PURPOSE OR NON-INFRINGEMENT. GENESYS AND ITS SUPPLIERS SHALL
//NOT BE LIABLE FOR ANY DAMAGE SUFFERED AS A RESULT OF USING THIS CODE. IN NO
//EVENT SHALL GENESYS AND ITS SUPPLIERS BE LIABLE FOR ANY DIRECT, INDIRECT,
//CONSEQUENTIAL, ECONOMIC, INCIDENTAL, OR SPECIAL DAMAGES (INCLUDING, BUT
//NOT LIMITED TO, ANY LOST REVENUES OR PROFITS).

//Copyright (c) 2007 - 2019 Genesys Telecommunications Laboratories, Inc. All rights reserved.
//===============================================================================

package com.genesyslab.platform.samples.apptemplate;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Properties;
import java.util.concurrent.Future;

import javax.net.ssl.SSLContext;

import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Configurator;

import com.genesyslab.platform.applicationblocks.com.objects.CfgApplication;
import com.genesyslab.platform.apptemplate.configuration.ClientConfigurationHelper;
import com.genesyslab.platform.apptemplate.configuration.ConfigurationException;
import com.genesyslab.platform.apptemplate.configuration.GCOMApplicationConfiguration;
import com.genesyslab.platform.apptemplate.configuration.GConfigTlsPropertyReader;
import com.genesyslab.platform.apptemplate.configuration.IGApplicationConfiguration.IGAppConnConfiguration;
import com.genesyslab.platform.apptemplate.configuration.IGApplicationConfiguration.IGServerInfo;
import com.genesyslab.platform.commons.connection.configuration.ConnectionConfiguration;
import com.genesyslab.platform.commons.connection.tls.SSLExtendedOptions;
import com.genesyslab.platform.commons.connection.tls.TLSConfiguration;
import com.genesyslab.platform.commons.connection.tls.TLSConfigurationHelper;
import com.genesyslab.platform.commons.connection.tls.TLSConfigurationParser;
import com.genesyslab.platform.commons.log.Log;
import com.genesyslab.platform.commons.log.Log4J2LoggerFactoryImpl;
import com.genesyslab.platform.commons.protocol.ChannelOpenedEvent;
import com.genesyslab.platform.commons.protocol.ClientChannel;
import com.genesyslab.platform.commons.protocol.Endpoint;
import com.genesyslab.platform.configuration.protocol.exceptions.ConfRegistrationException;
import com.genesyslab.platform.configuration.protocol.types.CfgAppType;
import com.genesyslab.platform.standby.WSConfig;
import com.genesyslab.platform.standby.WSHandler;
import com.genesyslab.platform.standby.WarmStandby;
import com.genesyslab.platform.standby.events.WSDisconnectedEvent;
import com.genesyslab.platform.standby.events.WSOpenedEvent;
import com.genesyslab.platform.voice.protocol.TServerProtocol;

/**
 * The Application Template Application Block provides a way to read configuration options 
 * for applications and to configure Platform SDK protocols.
 * 
 * This samples shows:
 *   - How to create already configured Endpoint for client connection
 *   - How to create WarmStandby configuration
 * 
 * See http://docs.genesys.com/Documentation/PSDK/latest/Developer/UsingAppTemplateAB
 */
public class Main {

	public static void main(String[] args) {
		initLogger();
		Properties props = getProperties();
		if(props==null) {
			System.out.println("Can't get \'application.properties\'. Check if file available in resources.");
			return;
		}
		ConfigServiceHelper confService = null;

		try {
			//get our CfgApplication			
			confService = new ConfigServiceHelper(props);
			confService.openService();
			CfgApplication application = confService.getApplication(props.getProperty("application.name"));
			
			//create Application Template configuration holder.
			//Used by helpers to initialize Endpoints, Warm Standby, etc
			GCOMApplicationConfiguration appConfiguration = new GCOMApplicationConfiguration(application);
			
			//create Endpoint for connection to TServer.			
			Endpoint endppoint = createEndpoint(appConfiguration, CfgAppType.CFGTServer);
			if(endppoint!=null) {
				TServerProtocol protocol = new TServerProtocol(endppoint);
				testSingleConnect(protocol);
			}
			
			//create WarmStandby configuration to open connection using WarmStandby service.
			//If no backup server specified in CME, WarmStandby configuration will contain single endpoint.
			WSConfig wsconfig = createWSConfig(appConfiguration, CfgAppType.CFGTServer);
			if(wsconfig!=null) {
				TServerProtocol protocol = new TServerProtocol();
	            WarmStandby ws = new WarmStandby(protocol);
	            ws.setConfig(wsconfig);	            
	            testWSConnect(ws);
			}
			

		} catch (ConfRegistrationException ex) {
			System.out.println("Client registration failed: " + ex.getErrorDescription());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			confService.releaseConfigService();
		}

	}
	
	/**
	 * Demonstrates connection of the channel to the target Server with parameters (stored in Endpoint), 
	 * that were retrieved from CfgApplication.
	 */
	private static void testSingleConnect(ClientChannel channel)
			throws Exception {

		try {
			Endpoint epndpoint = channel.getEndpoint();
			URI uri = epndpoint != null ? epndpoint.getUri() : null;
			System.out.println("\nConnecting to server: " + uri);
			channel.open();
			System.out.println("Successfully connected!");
		} catch (Throwable t) {
			System.out.println("Failed to open connection, server unavailable.");
		} finally {
			if (channel != null) {
				channel.close();
				System.out.println("Disconnected.");
			}
		}

	}

	/**
	 * Demonstrates connection of the Warm Standby to the pair of target Servers with parameters (stored in WSConfig), 
	 * that were retrieved from CfgApplication.
	 */
	private static void testWSConnect(WarmStandby ws) {

		try {

			{
				System.out.println("\n\nInitiate open with WarmStandby");
				ws.openAsync(); // initiate open
				System.out.println("Connecting to server: "
						+ ws.getChannel().getEndpoint().getUri());
				ws.open(); // wait for open
				System.out.println("Successfully connected!");
				ws.closeAsync(); // initiate closing
				System.out.println("Disconnected.");
				ws.close(); // wait for close
			}

			{
				System.out.println("\n\nInitiate open with WarmStandby");
				Future<ChannelOpenedEvent> fOpen = ws.openAsync(); // initiate
																	// open
				System.out.println("Connecting to TServer: "
						+ ws.getChannel().getEndpoint().getUri());
				fOpen.get(); // wait for open
				System.out.println("Successfully connected!");
				ws.closeAsync(); // initiate closing
				System.out.println("Disconnected.");
				ws.close(); // wait for close
			}

			{
				System.out.println("\n\nInitiate open with WarmStandby");

				ws.setHandler(new WSHandler() {

					@Override
					public void onChannelOpened(WSOpenedEvent event) {
						super.onChannelOpened(event);
						System.out.println("Succesffully connected!");
						event.getWarmStandby().closeAsync(); // initiate close
					}

					@Override
					public void onChannelDisconnected(WSDisconnectedEvent event) {
						System.out.println("Disconnected.");
					}

				});

				ws.openAsync(); // initiate open
			}

		} catch (Throwable t) {
			t.printStackTrace();
			System.out.println("Failed to open connection, server unavailable.");
		}
	}
	
	/**
	 * Creates fully configured Endpoint regarding settings, specified in Configuration Server.
	 * @param appConfiguration application which here client connection is defined
	 * @param serverType type of the client connection
	 * @return Endpoint
	 * @throws ConfigurationException
	 */
	private static Endpoint createEndpoint(GCOMApplicationConfiguration appConfiguration, CfgAppType serverType)
			throws ConfigurationException {

		IGAppConnConfiguration connConfig = appConfiguration.getAppServer(serverType);
		if (connConfig == null) {
			return null;
		}

		Endpoint targetEndpoint = null;

		GConfigTlsPropertyReader tlsReader = new GConfigTlsPropertyReader(appConfiguration, connConfig);
		boolean securedConn = Boolean.parseBoolean(tlsReader.getProperty("tls"));

		if (securedConn) {
			// TLS preparation section follows
			TLSConfiguration tlsConfiguration = TLSConfigurationParser.parseTlsConfiguration(
					new GConfigTlsPropertyReader(appConfiguration, connConfig), true);

			// TLS customization code goes here... // As an example, host name
			// verification is turned on IGApplicationConfiguration.IGServerInfo
			IGServerInfo targetServer = connConfig.getTargetServerConfiguration().getServerInfo();

			// Get TLS configuration objects for connection SSLContext
			SSLContext serverSSLContext = TLSConfigurationHelper.createSslContext(tlsConfiguration);
			SSLExtendedOptions serverSSLOptions = TLSConfigurationHelper
					.createSslExtendedOptions(tlsConfiguration);

			targetEndpoint = ClientConfigurationHelper.createEndpoint(appConfiguration, connConfig,
					connConfig.getTargetServerConfiguration(), securedConn, serverSSLContext,
					serverSSLOptions);
		} else {
			targetEndpoint = ClientConfigurationHelper.createEndpoint(appConfiguration, connConfig,
					connConfig.getTargetServerConfiguration());
		}
		
		if(targetEndpoint != null) {
			System.out.println(String.format("Resolved Endpoint for %s: \'%s\'",
					serverType.name(), targetEndpoint));

			ConnectionConfiguration cfg = targetEndpoint.getConfiguration();
			if (cfg != null) 
				System.out.println("\nEnpoint settings: \n" + cfg);			
		}
		else{
			System.out.println("Application has no client connections to " + serverType.name()
					+ " (see connections tab in CME)");	
		}
		
		return targetEndpoint;
	}
	
	/**
	 * Creates WarmStandby Configuration which is ready for connection to the pair of servers.
	 * Configuration includes:
	 *    - WarmStandby timeout parameters
	 *    - Target endpoints
	 *    - Connection parameters (tls, string encoding, etc) 
	 * @param appConfiguration application where client connection to WS pair is defined
	 * @param serverType type of the client connection
	 * @return WSConfig
	 * @throws ConfigurationException
	 */
	public static WSConfig createWSConfig(GCOMApplicationConfiguration appConfiguration, CfgAppType serverType) 
	        throws ConfigurationException {
	    
        IGAppConnConfiguration connConfig = appConfiguration.getAppServer(serverType);
        if (connConfig == null) {
			System.out.println("Application has no client connections to " + serverType.name()
					+ " (see connections tab in CME)");	    		
            return null;
        }
        
        WSConfig wsconfig = ClientConfigurationHelper.createWarmStandbyConfigEx(appConfiguration, connConfig);
        if(wsconfig != null) {
        	System.out.println("Got WarmStandby Configuration: \n" + wsconfig);	    
        }
	    
		return ClientConfigurationHelper.createWarmStandbyConfigEx(appConfiguration, connConfig);
	}


	private static Properties getProperties() {		
		InputStream is = null;
		Properties props = null;
		try {
			is = Thread.currentThread().getContextClassLoader().getResourceAsStream("application.properties");
			if(is != null) {				
				props = new Properties();
				props.load(is);
			}
		} catch (IOException ex) {
			ex.printStackTrace();
			props = null;
		}
		finally {
			if(is!=null) {
				try{
					is.close();
				}
				catch(Exception e) { }
			}
		}
		return props;
	}
	
	/**
	 * Enable PSDK logging by setting Logger factory and providing path to
	 * log4j2 configuration file. Alternately it is possible to enable logging
	 * with system properties:
	 * -Dcom.genesyslab.platform.commons.log.loggerFactory=com.genesyslab.platform.commons.log.Log4J2LoggerFactoryImpl
	 * -Dlog4j.configurationFile=path/to/log4j2.xml.
	 * 
	 *  See http://docs.genesys.com/Documentation/PSDK/latest/Developer/SettingUpLogging
	 */
	private static void initLogger() {
		ConfigurationSource source;
		InputStream is = null;
		try {			
			is = Thread.currentThread().getContextClassLoader().getResourceAsStream("log4j2.xml");	
			if(is==null) {
				System.out.println("Can't initialize log with \'log4j2.xml\'. Check if file available in resources.");
			}
			else {
				source = new ConfigurationSource(is);			
				Configurator.initialize(null, source);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			if (is != null) {
				try {
					is.close();
				}
				catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
		Log.setLoggerFactory(new Log4J2LoggerFactoryImpl());
	}
}
