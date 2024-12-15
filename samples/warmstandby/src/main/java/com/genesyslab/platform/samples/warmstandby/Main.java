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

package com.genesyslab.platform.samples.warmstandby;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Configurator;

import com.genesyslab.platform.commons.collections.KeyValueCollection;
import com.genesyslab.platform.commons.collections.processor.KeyValuePrinter;
import com.genesyslab.platform.commons.log.Log;
import com.genesyslab.platform.commons.log.Log4J2LoggerFactoryImpl;
import com.genesyslab.platform.commons.protocol.ClientChannel;
import com.genesyslab.platform.commons.protocol.Endpoint;
import com.genesyslab.platform.commons.protocol.Message;
import com.genesyslab.platform.commons.protocol.RegistrationException;
import com.genesyslab.platform.commons.protocol.WildcardEndpoint;
import com.genesyslab.platform.commons.threading.CompletionHandler;
import com.genesyslab.platform.openmedia.protocol.externalservice.request.Request3rdServer;
import com.genesyslab.platform.standby.WSConfig;
import com.genesyslab.platform.standby.WSHandler;
import com.genesyslab.platform.standby.WarmStandby;
import com.genesyslab.platform.standby.events.WSAllTriedUnsuccessfullyEvent;
import com.genesyslab.platform.standby.events.WSDisconnectedEvent;
import com.genesyslab.platform.standby.events.WSOpenedEvent;
import com.genesyslab.platform.standby.events.WSTriedUnsuccessfullyEvent;

public class Main {

	public static void main(String[] args) throws Exception {
		initLogger();

		showAsynchronousConnect();
		showSynchronousConnect();
	}

	/**
	 * Demonstrates how to open connection asynchronously using
	 * WarmStandby.autoRestore() method. This method also enables automatic
	 * reconnection upon unexpected client disconnect. It is possible to listen
	 * for WarmStandby service events (see warmStandby.setHandler(..)) 
	 * and customize reconnection logic: 
	 *   - Handle channel Registration Exception 
	 *   - Update list of server Endpoints, etc.
	 *   
	 *   See http://docs.genesys.com/Documentation/PSDK/latest/Developer/UsingWarmStandbyAB
	 */
	public static void showAsynchronousConnect() throws Exception {
		System.out.println("\n=============  Asynchronous connection sample ===================");

		final EspServer serverOne = new EspServer(new WildcardEndpoint(0));
		final EspServer serverTwo = new EspServer(new WildcardEndpoint(0));

		final EspClient client = new EspClient();
		client.setUserName("usr");
		client.setPassword("wrong pwd"); // to demonstrate how to handle registration exception

		// Create Warm Standby service for the client
		WarmStandby warmStandby = new WarmStandby(client);

		try {
			serverOne.open();
			serverTwo.open();

			final List<Endpoint> idlePool = new LinkedList<Endpoint>();
			idlePool.add(new Endpoint("localhost", 1)); // Non-working Server	
			idlePool.add(new Endpoint("localhost", 2)); // Non-working Server

			final List<Endpoint> goodPool = new LinkedList<Endpoint>();
			goodPool.add(new Endpoint("localhost", 3)); // Non-working Server
			goodPool.add(new Endpoint("localhost", 4)); // Non-working Server	
			goodPool.add(new Endpoint("localhost", serverOne.getLocalEndPoint().getPort())); // Normal Server
			goodPool.add(new Endpoint("localhost", serverTwo.getLocalEndPoint().getPort())); // Normal Server

			// Specify address list to connect to.
			WSConfig wsConfig = new WSConfig();
			wsConfig.setEndpoints(idlePool);

			//Delay before next iteration (after all Endpoints in the pool have been tried unsuccessfully) 
			wsConfig.setRetryDelay(100, 200);
			//Delay before reconnection attempt to unexpectedly disconnected server.
			wsConfig.setReconnectionRandomDelayRange(100);

			warmStandby.setConfig(wsConfig);
			//See http://docs.genesys.com/Documentation/PSDK/latest/Developer/UsingWarmStandbyAB#Using_WarmStandby_Event_Handlers
			warmStandby.setHandler(new WSHandler() {

				public void onChannelOpened(WSOpenedEvent event) {
					System.out.println("[WS Handler] Client successfully connected to " + event.getEndpoint());
					// send some message to server and receive response
					ClientChannel channel = event.getWarmStandby().getChannel();
					try {
						channel.requestAsync(createRequest(), channel,
								new CompletionHandler<Message, ClientChannel>() {
									public void completed(Message result, ClientChannel channel) {
										setCompleted();
									}

									public void failed(Throwable exc, ClientChannel attachment) {
										System.out.println("Request failed. " + exc);
										setCompleted();
									}
								});
					} catch (Throwable e) {
						System.out.println("[WS Handler] Error in channel.requestAsync operation.");
						e.printStackTrace();
					}
				}

				public void onChannelDisconnected(WSDisconnectedEvent event) {
					System.out.println("[WS Handler] Detected client disconnection");
				}

				public void onEndpointTriedUnsuccessfully(WSTriedUnsuccessfullyEvent event) {
					Throwable t = event.getCause();
					if (t instanceof RegistrationException) {
						System.out.println("[WS Handler] Got RegistrationException when connecting to "
								+ event.getEndpoint() + " Typing correct password... ");
						client.setPassword("pwd");
						event.resume(); // resume WarmStandby Service!
					} else {
						System.out.println("[WS Handler] Tried to connect to " + event.getEndpoint()
								+ ". The Server unavailable");

						// When connection is lost, WS starts iterating Endpoints list from its begin.
						// We can move unsuccessful Endpoint to the end of the list 
						// to start from unchecked Endpoint next time.
						WSConfig config = event.getWarmStandby().getConfig();
						List<Endpoint> currentList = config.getEndpoints();
						Endpoint triedUnsuccessfully = event.getEndpoint();
						int pos = currentList.indexOf(triedUnsuccessfully);
						if (pos >= 0 && pos < currentList.size() - 1) {
							currentList.remove(pos);
							currentList.add(triedUnsuccessfully);
						}
					}
				}

				public void onAllEndpointsTriedUnsuccessfully(WSAllTriedUnsuccessfullyEvent event) {
					int cnt = event.getRetryNumber();
					System.out.println("[WS Handler] All Servers unavailable. Pool retries count: " + cnt + "\n");
					if (cnt == 2) {
						try {
							System.out.println("[WS Handler] Switching to next pool. ");
							event.getWarmStandby().getConfig().setEndpoints(goodPool);
						} catch (Throwable t) {
							t.printStackTrace();
						}
					} else if (cnt > 2) {
						System.out.println("[WS Handler] Error. Should connect to second pool");
					}

				}
			});

			System.out.println("\nStart connecting...");

			//Asynchronous method that start WarmStandby service and 
			//trying to establish connection turning over all Endpoints in the WSConfig list.		
			warmStandby.autoRestore();
			waitCompleted();
			System.out.println("\nDone.");
			Thread.sleep(1000);

			System.out.println("\nSimulate unexpected client disconnect");
			serverOne.close();
			waitCompleted();

			System.out.println("\n=============  End sample ===================");

		} catch (Throwable ex) {
			ex.printStackTrace();
		} finally {
			setCompleted();
			warmStandby.setHandler(null);
			try {
				warmStandby.close();
			} catch (InterruptedException ex) {
				System.out.println("\n[Error] Warm Standby closing was interrupted");
			}
			serverOne.closeSilent();
			serverTwo.closeSilent();
		}
	}

	/**
	 * Demonstrates how to open connection with synchronous WarmStandby.open();
	 * This method will connect to the first available server from the WSConfig
	 * Endpoints list. 
	 * 
	 * NOTE: after successful connection WarmStandby will NOT
	 * handle unexpected client disconnections. Use WarmStandby.autoRestore()
	 * instead, or call autoRestore() after WarmStandby.open() completed
	 * successfully.
	 * 
	 * If all Endpoints have been checked unsuccessfully, the WarmStandby.open()
	 * throws WSNoAvailableServersException.
	 * 
	 * See http://docs.genesys.com/Documentation/PSDK/latest/Developer/UsingWarmStandbyAB
	 */
	public static void showSynchronousConnect() {
		System.out.println("\n=============  Synchronous connect sample ===================");

		EspServer serverOne = new EspServer(new WildcardEndpoint(0));
		final EspClient client = new EspClient();
		client.setUserName("usr");
		client.setPassword("pwd");
		final WarmStandby warmStandby = new WarmStandby(client);

		try {
			serverOne.open();

			List<Endpoint> endpointsPool = new ArrayList<Endpoint>();
			endpointsPool.add(new Endpoint("localhost", 1)); // Non-working Server 
			endpointsPool.add(new Endpoint("localhost", serverOne.getLocalEndPoint().getPort())); // normal Server																									

			WSConfig wsConfig = new WSConfig();
			wsConfig.setEndpoints(endpointsPool);
			warmStandby.setConfig(wsConfig);

			System.out.println("\nConnecting...");

			// Synchronous method that attempts to connect to Server turning over all Endpoints from the WSConfig.
			// To enable unexpected client disconnection handling call
			// warmStandby.autoRestore() after open() completed.
			warmStandby.open();
			System.out.println("\nDone.");

			// Sending something to Server and receive response
			Message response = client.request(createRequest());
			System.out.println("\n Message from server: " + response);

			System.out.println("\n=============  End sample ===================");
		} catch (Throwable ex) {
			ex.printStackTrace();
		} finally {
			setCompleted();
			warmStandby.setHandler(null);
			try {
				warmStandby.close();
			} catch (InterruptedException ex) {
				System.out.println("\n[Error] Warm Standby closing was interrupted");
			}
			serverOne.closeSilent();
		}

	}

	/**
	 * Helper method creates some request to test communication with server. 
	 * @return
	 */
	private static Message createRequest() {
		KeyValueCollection data = new KeyValueCollection();
		data.addInt("id", 2);
		data.addString("hello", "there");
		Request3rdServer message = Request3rdServer.create();
		message.setRequest(data);
		return message;
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
		
		// Hide sensitive data in logs
		KeyValueCollection keyFilter = new KeyValueCollection();
		keyFilter.addString("password", KeyValuePrinter.HIDE_FILTER_NAME);
		KeyValuePrinter hidePrinter = new KeyValuePrinter(null, keyFilter);
		KeyValuePrinter.setDefaultPrinter(hidePrinter);
		
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

	private static final Object syncObject = new Object();
	private static final int syncTimeout = 30 * 1000;

	private static void waitCompleted() {
		synchronized (syncObject) {
			try {
				syncObject.wait(syncTimeout);
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
	}

	private static void setCompleted() {
		synchronized (syncObject) {
			try {
				syncObject.notify();
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
	}

}
