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

package com.genesyslab.platform.samples.stat;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.Set;

import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Configurator;

import com.genesyslab.platform.commons.log.Log;
import com.genesyslab.platform.commons.log.Log4J2LoggerFactoryImpl;
import com.genesyslab.platform.commons.protocol.ClientChannel;
import com.genesyslab.platform.commons.protocol.Endpoint;
import com.genesyslab.platform.commons.protocol.Message;
import com.genesyslab.platform.commons.protocol.MessageHandler;
import com.genesyslab.platform.commons.threading.AsyncInvoker;
import com.genesyslab.platform.commons.threading.CompletionHandler;
import com.genesyslab.platform.reporting.protocol.StatServerProtocol;
import com.genesyslab.platform.reporting.protocol.statserver.DnActionsMask;
import com.genesyslab.platform.reporting.protocol.statserver.Notification;
import com.genesyslab.platform.reporting.protocol.statserver.NotificationMode;
import com.genesyslab.platform.reporting.protocol.statserver.StatisticCategory;
import com.genesyslab.platform.reporting.protocol.statserver.StatisticInterval;
import com.genesyslab.platform.reporting.protocol.statserver.StatisticMetricEx;
import com.genesyslab.platform.reporting.protocol.statserver.StatisticObject;
import com.genesyslab.platform.reporting.protocol.statserver.StatisticObjectType;
import com.genesyslab.platform.reporting.protocol.statserver.StatisticSubject;
import com.genesyslab.platform.reporting.protocol.statserver.events.EventInfo;
import com.genesyslab.platform.reporting.protocol.statserver.events.EventStatisticOpened;
import com.genesyslab.platform.reporting.protocol.statserver.requests.RequestCloseStatistic;
import com.genesyslab.platform.reporting.protocol.statserver.requests.RequestOpenStatisticEx;
import com.genesyslab.platform.standby.WSConfig;
import com.genesyslab.platform.standby.WSHandler;
import com.genesyslab.platform.standby.WarmStandby;
import com.genesyslab.platform.standby.events.WSAllTriedUnsuccessfullyEvent;
import com.genesyslab.platform.standby.events.WSDisconnectedEvent;
import com.genesyslab.platform.standby.events.WSOpenedEvent;
import com.genesyslab.platform.standby.events.WSTriedUnsuccessfullyEvent;

/**
 * Sample demonstrates MultiThreadInvoker that allows 
 * to handle incoming messages from StatServer and channel events in separate threads.
 * NOTE: MultiThreadInvoker can be used only if you don't relay on the order of incoming messages.
 */
public class StatPrinter extends WSHandler implements MessageHandler {

	private List<Integer> openedStats = Collections.synchronizedList(new ArrayList<Integer>());
	private WarmStandby wsEnabledClient = null;
	private static Properties appProps = null;

	public static void main(String[] args) {
		initLogger();
		appProps = getProperties();
		if(appProps==null) {
			System.out.println("Can't get \'application.properties\'. Check if file available in resources.");
			return;
		}
		
		StatPrinter printer = new StatPrinter();
		try {
			printer.openConnection();			
			Scanner scanner = new Scanner(System.in);
			scanner.nextLine();			
			scanner.close();			
			printer.closeStatistics();
		} catch (Throwable t) {
			t.printStackTrace();
		} finally {
			printer.closeConnection();
		}

	}

	//Creating Dynamic Statistics and subscribe for notifications.
	//See http://docs.genesys.com/Documentation/PSDK/latest/Developer/StatServer
	public void openStatistics() {		
		System.out.println(String.format("[ThreadId %d]: Subscribing for statistic notification....", getThreadId()));
		Set<String> ids = getObjectIds(appProps);
		openedStats.clear();
		try {
			for (String id : ids) {

				//Define statistic objects that have not already been defined in the Configuration Layer
				StatisticObject obj = StatisticObject.create(
						appProps.getProperty("statObj." + id + ".objectId"),
						StatisticObjectType.valueOf(appProps.getProperty("statObj." + id + ".type")),
						appProps.getProperty("statObj." + id + ".tenant"),
						appProps.getProperty("statObj." + id + ".tenantPassword"));

				StatisticMetricEx metric = StatisticMetricEx.create();
				metric.setMainMask(new DnActionsMask());
				metric.getMainMask().setAll();
				metric.setRelativeMask(new DnActionsMask());
				metric.getRelativeMask().clearAll();
				metric.setIntervalType(StatisticInterval.valueOf(appProps.getProperty("metric.intervalType")));
				metric.setIntervalLength(Integer.parseInt(appProps.getProperty("metric.intervalLength")));
				metric.setCategory(StatisticCategory.valueOf(appProps.getProperty("metric.category")));
				metric.setSubject(StatisticSubject.valueOf(appProps.getProperty("metric.subject")));

				Notification notification = Notification.create(
						NotificationMode.valueOf(appProps.getProperty("notification.mode")),
						Integer.parseInt(appProps.getProperty("notification.frequency")),
						Integer.parseInt(appProps.getProperty("notification.insensitivity")));

				RequestOpenStatisticEx request = RequestOpenStatisticEx.create(obj, metric, notification);

				ClientChannel channel = wsEnabledClient.getChannel();

				channel.requestAsync(request, this, new CompletionHandler<Message, StatPrinter>() {

					@Override
					public void completed(Message result, StatPrinter attachment) {
						if (result instanceof EventStatisticOpened) {
							Integer id = ((EventStatisticOpened) result).getReferenceId();
							openedStats.add(id);
							System.out.println(String.format("[ThreadId %d]: Statistic id=%d opened!", getThreadId(), id));
						} else {
							System.out.println(String.format("[ThreadId %d]: Failed to open statistic: %s", getThreadId(), result.toString()));
						}
					}

					@Override
					public void failed(Throwable exc, StatPrinter attachment) {
						exc.printStackTrace();
					}
				});
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}

	}

	public void closeStatistics() {
		WarmStandby ws = wsEnabledClient;
		if (ws != null) {
			ClientChannel channel = ws.getChannel();
			synchronized (openedStats) {
				try {
					for (Integer id : openedStats) {
						RequestCloseStatistic close = RequestCloseStatistic.create(id);
						System.out.println("Closing statistics " + id);
						channel.request(close);
						System.out.println("Done.");
					}
				} catch (Throwable t) {
					t.printStackTrace();
				}
				openedStats.clear();
			}
		}
	}

	public void openConnection() throws Exception {
		System.out.println("Opening channel...");
		

		// See http://docs.genesys.com/Documentation/PSDK/8.5.x/Developer/ConnectingtoaServer#t-1
		StatServerProtocol statServer = new StatServerProtocol();
		// By setting an MultiThreadInvoker, protocol events and callback can be run in separate GUI threads.
		statServer.setInvoker(new MultiThreadInvoker());

		// Protocol events are handled by setting MessageHandler.
		// See http://docs.genesys.com/Documentation/PSDK/latest/Developer/EventHandling#t-1
		statServer.setMessageHandler(this);

		//Enable Warm Standby for the client
		wsEnabledClient = new WarmStandby(statServer);
		Endpoint primaryEndpoint = new Endpoint("StatPrinter",
				appProps.getProperty("statserver.primary.host"), Integer.parseInt(appProps
						.getProperty("statserver.primary.port")));

		Endpoint backupEndpoint = new Endpoint("StatPrinter", appProps.getProperty("statserver.backup.host"),
				Integer.parseInt(appProps.getProperty("statserver.backup.port")));

		WSConfig wsConfig = new WSConfig();
		wsConfig.setEndpoints(primaryEndpoint, backupEndpoint);
		//time required for backup Server to step into primary state after former primary failure.
		wsConfig.setBackupDelay(5000);

		wsEnabledClient.setConfig(wsConfig);
		wsEnabledClient.setHandler(this);

		//tries to connect synchronously
		wsEnabledClient.open();
		//enables reconnection if connection is broken unexpectedly
		wsEnabledClient.autoRestore();
	}

	public void closeConnection() {
		System.out.println("Close channel.");
		WarmStandby ws = wsEnabledClient;
		if (ws != null) {
			ws.setHandler(null);
			try {
				ws.close();
			} catch (Throwable t) {
				t.printStackTrace();
			} finally {
				//Release custom invoker!
				AsyncInvoker invoker = ws.getChannel().getInvoker();
				if (invoker != null)
					invoker.dispose();
			}
		}
	}

	//Receiving Messages Asynchronously
	//See http://docs.genesys.com/Documentation/PSDK/latest/Developer/EventHandling
	@Override
	public void onMessage(Message message) {
		if (message instanceof EventInfo) {
			EventInfo info = (EventInfo) message;
			long threadId = getThreadId();
			System.out.println(String.format("[ThreadId %d]: statistic=%d intValue=%d stringValue=%s",
					threadId, info.getReferenceId(), info.getIntValue(), info.getStringValue()));
			try {
				//Doing something for a long time...
				Thread.sleep(1000);
			} catch (InterruptedException ex) {
			}
			;
		} else {
			System.out.println("Unexpected event: " + message);
		}
	}

	//Handle channel connection event 
	@Override
	public void onChannelOpened(WSOpenedEvent event) {
		System.out.println(String.format("[ThreadId %d]: Connected.", getThreadId()));
		openStatistics();
	}

	@Override
	public void onChannelDisconnected(WSDisconnectedEvent event) {
		System.out.println(String.format("[ThreadId %d]: Channel disconnected", getThreadId()));
	}

	@Override
	public void onEndpointTriedUnsuccessfully(WSTriedUnsuccessfullyEvent event) {
		System.out.println(String.format("[ThreadId %d]: Tried to connect to %s but failed.", getThreadId(),
				event.getEndpoint().toString()));
	}

	@Override
	public void onAllEndpointsTriedUnsuccessfully(WSAllTriedUnsuccessfullyEvent event) {
		System.out.println(String.format("[ThreadId %d]: All Endpoints have been tried unsuccessfully",
				getThreadId()));
	}

	private long getThreadId() {
		return Thread.currentThread().getId();
	}

	private static Properties getProperties() {		
		InputStream is = null;
		AppProperties props = null;
		try {
			is = Thread.currentThread().getContextClassLoader().getResourceAsStream("application.properties");
			if(is != null) {				
				props = new AppProperties();
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

	private static class AppProperties extends Properties {
		private static final long serialVersionUID = 1L;

		@Override
		public String getProperty(String key) {
			String value = super.getProperty(key);
			if (value != null)
				value = value.trim();
			return value;
		}
	}

	private static Set<String> getObjectIds(Properties props) {
		Set<String> ids = new HashSet<String>();
		for (Object k : props.keySet()) {
			if (k instanceof String) {
				String strKey = (String) k;
				if (strKey.startsWith("statObj") && strKey.length() > 9) {
					ids.add(strKey.substring(8, 9));
				}
			}
		}
		return ids;
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
