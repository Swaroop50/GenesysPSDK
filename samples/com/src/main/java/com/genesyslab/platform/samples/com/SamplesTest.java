//===============================================================================
//Genesys Platform SDK Samples
//===============================================================================

//===============================================================================
// Any authorized distribution of any copy of this code (including any related
// documentation) must reproduce the following restrictions, disclaimer and copyright
// notice:

// The Genesys name, trademarks and/or logo(s) of Genesys shall not be used to name
// (even as a part of another name), endorse and/or promote products derived from
// this code without prior written permission from Genesys Telecommunications
// Laboratories, Inc.

// The use, copy, and/or distribution of this code is subject to the terms of the Genesys
// Developer License Agreement.  This code shall not be used, copied, and/or
// distributed under any other license agreement.

// THIS CODE IS PROVIDED BY GENESYS TELECOMMUNICATIONS LABORATORIES, INC.
// ("GENESYS") "AS IS" WITHOUT ANY WARRANTY OF ANY KIND. GENESYS HEREBY
// DISCLAIMS ALL EXPRESS, IMPLIED, OR STATUTORY CONDITIONS, REPRESENTATIONS AND
// WARRANTIES WITH RESPECT TO THIS CODE (OR ANY PART THEREOF), INCLUDING, BUT
// NOT LIMITED TO, IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
// PARTICULAR PURPOSE OR NON-INFRINGEMENT. GENESYS AND ITS SUPPLIERS SHALL
// NOT BE LIABLE FOR ANY DAMAGE SUFFERED AS A RESULT OF USING THIS CODE. IN NO
// EVENT SHALL GENESYS AND ITS SUPPLIERS BE LIABLE FOR ANY DIRECT, INDIRECT,
// CONSEQUENTIAL, ECONOMIC, INCIDENTAL, OR SPECIAL DAMAGES (INCLUDING, BUT
// NOT LIMITED TO, ANY LOST REVENUES OR PROFITS).

// Copyright (c) 2006 - 2019 Genesys Telecommunications Laboratories, Inc. All rights reserved.
//===============================================================================
package com.genesyslab.platform.samples.com;

import com.genesyslab.platform.applicationblocks.com.IConfService;
import com.genesyslab.platform.applicationblocks.com.objects.CfgApplication;
import com.genesyslab.platform.applicationblocks.com.objects.CfgPerson;
import com.genesyslab.platform.applicationblocks.com.ConfigException;
import com.genesyslab.platform.commons.log.Log;
import com.genesyslab.platform.commons.log.Log4J2LoggerFactoryImpl;
import com.genesyslab.platform.commons.protocol.ProtocolException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Configurator;


/**
 * See http://docs.genesys.com/Documentation/PSDK/latest/Developer/UsingtheCOMAB
 * 
 * Small test application to execute some of described samples in test mode
 * to ensure that provided code is working.
 *
 */
public class SamplesTest {

    /**
     * Small function to be executed to check that main Configuration Service operations
     * are working. Some kind of test.
     * Can be used for automatic tests with dependence on Genesys environment.
     *
     * @param args program commandline arguments
     * @throws ConfigException in case of any configuration service exception
     * @throws ProtocolException in case of any configuration protocol exception
     * @throws InterruptedException if process was interrupted
     */
    public static void main(final String[] args)
            throws ConfigException, InterruptedException, ProtocolException {
    	initLogger();
    	Properties properties = getProperties();
		if(properties==null) {
			System.out.println("Can't get \'application.properties\'. Check if file available in resources.");
			return;
		}

        String configServerHost;
        int    configServerPort;
        String configServerUser;
        String configServerPass;

        String tempAppName   = "AppName4Test"; // Unique name for temp app to be created,
                                               // changed and deleted.
        String tempAgentName = "AgentName4Test"; // Unique name for temp agent to be created,
                                                 // changed and deleted.

        System.out.println("ComJavaQuickStart started execution.");

        String someAppName = properties.getProperty("ConfServerClientName");
        if (someAppName == null || someAppName.equals("")) {
            someAppName = "default";
        }
        configServerHost = properties.getProperty("ConfServerHost");
        configServerPort = Integer.parseInt(properties.getProperty("ConfServerPort"));
        configServerUser = properties.getProperty("ConfServerUser");
        configServerPass = properties.getProperty("ConfServerPassword");


        IConfService service = InitializationSamples.initializeConfigService(
                tempAppName, configServerHost, configServerPort,
                configServerUser, configServerPass
        );

        ReadConfigurationSamples.readAndPrintCfgApplication(service, someAppName);

        ReadConfigurationSamples.readAllCfgApplicationsAsync(service);

        // Create application test (remove if it is already created before):
        DeleteConfObjectSamples.deleteApplication(service, tempAppName);
        CfgApplication app = CreateApplicationSamples.createSrvApplicationExt(service, tempAppName);
        // SAVE the application - the sample method does not save it:
        app.save();

        // Some tests with CfgPerson:
        // Delete person if already exists and then recreate:
        DeleteConfObjectSamples.deletePerson(service, tempAgentName);
        CfgPerson agent = AgentInformationSamples.createPersonRecord(service, tempAgentName);
        agent.save();
        AgentInformationSamples.readPersonPermissions(service, agent);
        AgentInformationSamples.modifyPersonPermissions(service, agent);
        agent.delete();

        EventsSubscriptionSamples.testEvents(service, tempAppName);

        DeleteConfObjectSamples.deleteApplication(service, tempAppName);

        // Closes protocol connection and release ConfService instance:
        InitializationSamples.uninitializeConfigService(service);

        System.out.println("ComJavaQuickStart finished execution.");
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
