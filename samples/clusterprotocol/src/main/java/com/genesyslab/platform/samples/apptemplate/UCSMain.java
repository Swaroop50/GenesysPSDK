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
import java.net.URISyntaxException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import com.genesyslab.platform.clusterprotocol.ClusterChannelListener;
import com.genesyslab.platform.clusterprotocol.DefaultClusterProtocolPolicy;
import com.genesyslab.platform.clusterprotocol.lb.DefaultClusterLoadBalancer;
import com.genesyslab.platform.clusterprotocol.ucs.UcsClusterProtocol;
import com.genesyslab.platform.clusterprotocol.ucs.UcsClusterProtocolBuilder;
import com.genesyslab.platform.commons.log.Log;
import com.genesyslab.platform.commons.log.Log4J2LoggerFactoryImpl;
import com.genesyslab.platform.commons.protocol.*;
import com.genesyslab.platform.commons.threading.CompletionHandler;
import com.genesyslab.platform.contacts.protocol.contactserver.AbstractMessage;
import com.genesyslab.platform.contacts.protocol.contactserver.requests.RequestGetVersion;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Configurator;



/**
 * This sample shows example how to use cluster protocol application block.
 *
 * This samples shows:
 *   - How to create and configure a cluster protocol.
 *   - How to write and set cluster protocol channel listener.
 *   - How to open and close a cluster protocol.
 *   - How to send messages to a cluster protocol.
 *   - How to receive messages from a cluster protocol.
 *   - How to make synchronous and asynchronous requests to a cluster protocol.
 */
public class UCSMain {

	public static void main(String[] args) throws InterruptedException {
		initLogger();

		final UcsClusterProtocol ucsNProtocol = createClusterProtocol();

		if (!configureClusterProtocol(ucsNProtocol)) return;

		ucsNProtocol.addChannelListener(listener);


		try {
			System.out.println("Opening cluster protocol");
			ucsNProtocol.open();
			System.out.println("Opened cluster protocol");
		} catch (Throwable e) {
			System.err.println("Error opening cluster protocol " + e);
			e.printStackTrace();
			return;
		}


		try
		{
			example_fewSyncRequestsToFixedClusterNode(ucsNProtocol);

			example_fewSyncRequestsToAnyClusterNode(ucsNProtocol);

			example_fewAsyncRequestsUsingCompletionHandlerToAnyNode(ucsNProtocol);

			example_fewAsyncRequestsUsingFutureToAnyNode(ucsNProtocol);

			example_fewSendsAndReceivingResponsesUsingMessageHandler(ucsNProtocol);


		}
		catch (ProtocolException ex) {
			ex.printStackTrace();
		}
		finally {

			try {
				System.out.println("---------------------------------------------------");
				System.out.println("\nClosing cluster protocol\n");
				ucsNProtocol.close();
				System.out.println("\nClosed cluster protocol\n");
			} catch (ProtocolException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private static boolean configureClusterProtocol(UcsClusterProtocol ucsNProtocol) {
		Properties props = getProperties();
		if(props==null) {
			System.out.println("Can't get \'application.properties\'. Check if file available in resources.");
			return false;
		}

		ucsNProtocol.setClientName( props.getProperty("ucs.clientName") );
		ucsNProtocol.setClientApplicationType( props.getProperty("ucs.clientApplicationType") );

		Endpoint[] endpointsArray = parseEndpoints(props.getProperty("ucs.cluster.nodes"));
		ucsNProtocol.setNodesEndpoints(endpointsArray);
		return true;
	}

	private static UcsClusterProtocol createClusterProtocol() {
		DefaultClusterProtocolPolicy policy = new DefaultClusterProtocolPolicy();
		policy.waitOnChannelOpening(false);
		policy.useRequestProtocolId(true);

		DefaultClusterLoadBalancer loadBalancer = new DefaultClusterLoadBalancer();

		return new UcsClusterProtocolBuilder()
						.withLoadBalancer(loadBalancer)
				.withClusterProtocolPolicy(policy)
						.build();
	}

	private static void example_fewSendsAndReceivingResponsesUsingMessageHandler(UcsClusterProtocol ucsNProtocol) throws ProtocolException, InterruptedException {
		// Sends messages synchronously and receive responses using message handler;
		{
            System.out.println("---------------------------------------------------");
            System.out.println("\nClosing cluster protocol\n");
            ucsNProtocol.close();
            System.out.println("\nClosed cluster protocol\n");

            final Map<Object, AbstractMessage> requested = new ConcurrentHashMap();

            final CountDownLatch cdRequestsProcessed = new CountDownLatch(4);

            MessageHandler handler = new MessageHandler() {
                @Override
                public void onMessage(Message message) {
                    if (message instanceof AbstractMessage) {
                        AbstractMessage response = (AbstractMessage) message;
                        AbstractMessage request = requested.remove(response.getReferenceId());
                        if (request != null) {
                            showRequestResponseInfo(request, response);
                            cdRequestsProcessed.countDown();
                            return;
                        }
                    }
                    System.out.println("Unsolicited message: " + message);
                }
            };

            ucsNProtocol.setMessageHandler(handler);

            System.out.println("\nOpening cluster protocol\n");
            ucsNProtocol.open();
            System.out.println("\nOpened cluster protocol\n");

            System.out.println("---------------------------------------------------");
            System.out.println("\nSends messages synchronously and receive responses using message handler\n");
            System.out.println("---------------------------------------------------");


            ReferenceBuilder rb = ucsNProtocol.getReferenceBuilder();

            RequestGetVersion request1 = RequestGetVersion.create();
            RequestGetVersion request2 = RequestGetVersion.create();
            RequestGetVersion request3 = RequestGetVersion.create();
            RequestGetVersion request4 = RequestGetVersion.create();

            rb.updateReference(request1);
            rb.updateReference(request2);
            rb.updateReference(request3);
            rb.updateReference(request4);


            try {
                requested.put(request1.getReferenceId(), request1);
                ucsNProtocol.send(request1);
            } catch (Exception ex) { ex.printStackTrace(); requested.remove(request1); cdRequestsProcessed.countDown(); }

            try {
                requested.put(request2.getReferenceId(), request2);
                ucsNProtocol.send(request2);
            } catch (Exception ex) { ex.printStackTrace(); requested.remove(request2); cdRequestsProcessed.countDown(); }

            try {
                requested.put(request3.getReferenceId(), request3);
                ucsNProtocol.send(request3);
            } catch (Exception ex) { ex.printStackTrace(); requested.remove(request3); cdRequestsProcessed.countDown(); }

            try {
                requested.put(request4.getReferenceId(), request4);
                ucsNProtocol.send(request4);
            } catch (Exception ex) { ex.printStackTrace(); requested.remove(request4); cdRequestsProcessed.countDown(); }


            cdRequestsProcessed.await(10, TimeUnit.SECONDS);
        }
	}

	private static void example_fewAsyncRequestsUsingFutureToAnyNode(UcsClusterProtocol ucsNProtocol) throws ProtocolException {
		System.out.println("---------------------------------------------------");
		System.out.println("\nRequests asynchronously using future\n");
		System.out.println("---------------------------------------------------");
		{
            RequestGetVersion request1 = RequestGetVersion.create();
            RequestGetVersion request2 = RequestGetVersion.create();
            RequestGetVersion request3 = RequestGetVersion.create();
            RequestGetVersion request4 = RequestGetVersion.create();

            RequestFuture f1 = ucsNProtocol.beginRequest(request1);
            RequestFuture f2 = ucsNProtocol.beginRequest(request2);
            RequestFuture f3 = ucsNProtocol.beginRequest(request3);
            RequestFuture f4 = ucsNProtocol.beginRequest(request4);

            Message response1 = null;
            Message response2 = null;
            Message response3 = null;
            Message response4 = null;

            try {
                response1 = f1.get();
            } catch (Exception ex) { ex.printStackTrace(); }

            try {
                response2 = f2.get();
            } catch (Exception ex) { ex.printStackTrace(); }

            try {
                response3 = f3.get();
            } catch (Exception ex) { ex.printStackTrace(); }

            try {
                response4 = f4.get();
            } catch (Exception ex) { ex.printStackTrace(); }

            showRequestResponseInfo(request1, response1);
            showRequestResponseInfo(request2, response2);
            showRequestResponseInfo(request3, response3);
            showRequestResponseInfo(request4, response4);
        }
	}

	private static void example_fewAsyncRequestsUsingCompletionHandlerToAnyNode(UcsClusterProtocol ucsNProtocol) throws ProtocolException, InterruptedException {
		System.out.println("---------------------------------------------------");
		System.out.println("\nRequests asynchronously using completion handler\n");
		System.out.println("---------------------------------------------------");
		{
            final CountDownLatch cdRequestCompleted = new CountDownLatch(4);

            CompletionHandler<Message, RequestGetVersion> requestHandler = new CompletionHandler<Message, RequestGetVersion>() {
                @Override
                public void completed(Message response, RequestGetVersion request) {
                    showRequestResponseInfo(request, response);
                    cdRequestCompleted.countDown();
                }

                @Override
                public void failed(Throwable exc, RequestGetVersion attachment) {
                    exc.printStackTrace();
                    cdRequestCompleted.countDown();
                }
            };

            RequestGetVersion request1 = RequestGetVersion.create();
            RequestGetVersion request2 = RequestGetVersion.create();
            RequestGetVersion request3 = RequestGetVersion.create();
            RequestGetVersion request4 = RequestGetVersion.create();

            ucsNProtocol.requestAsync(request1, request1, requestHandler);
            ucsNProtocol.requestAsync(request2, request2, requestHandler);
            ucsNProtocol.requestAsync(request3, request3, requestHandler);
            ucsNProtocol.requestAsync(request4, request4, requestHandler);

            cdRequestCompleted.await(10, TimeUnit.SECONDS);
        }
	}

	private static void example_fewSyncRequestsToAnyClusterNode(UcsClusterProtocol ucsNProtocol) {
		System.out.println("---------------------------------------------------");
		System.out.println("\nRequests synchronously to any cluster node\n");
		System.out.println("---------------------------------------------------");
		{
            RequestGetVersion request1 = RequestGetVersion.create();
            RequestGetVersion request2 = RequestGetVersion.create();
            RequestGetVersion request3 = RequestGetVersion.create();
            RequestGetVersion request4 = RequestGetVersion.create();

            Message response1 = null;
            Message response2 = null;
            Message response3 = null;
            Message response4 = null;

            try {
                response1 = ucsNProtocol.request(request1);
            } catch (Exception ex) { ex.printStackTrace(); }

            try {
                response2 = ucsNProtocol.request(request2);
            } catch (Exception ex) { ex.printStackTrace(); }

            try {
                response3 = ucsNProtocol.request(request3);
            } catch (Exception ex) { ex.printStackTrace(); }

            try {
                response4 = ucsNProtocol.request(request4);
            } catch (Exception ex) { ex.printStackTrace(); }

            showRequestResponseInfo(request1, response1);
            showRequestResponseInfo(request2, response2);
            showRequestResponseInfo(request3, response3);
            showRequestResponseInfo(request4, response4);
        }
	}

	private static void example_fewSyncRequestsToFixedClusterNode(UcsClusterProtocol ucsNProtocol) {
		System.out.println("---------------------------------------------------");
		System.out.println("\nRequests synchronously to a fixed cluster node\n");
		System.out.println("---------------------------------------------------");
		{
            Protocol node = ucsNProtocol.getNextAvailableProtocol();


            RequestGetVersion request1 = RequestGetVersion.create();
            RequestGetVersion request2 = RequestGetVersion.create();
            RequestGetVersion request3 = RequestGetVersion.create();
            RequestGetVersion request4 = RequestGetVersion.create();

            Message response1 = null;
            Message response2 = null;
            Message response3 = null;
            Message response4 = null;


            try {
                response1 = node.request(request1);
            } catch (Exception ex) { ex.printStackTrace(); }

            try {
                response2 = node.request(request2);
            } catch (Exception ex) { ex.printStackTrace(); }

            try {
                response3 = node.request(request3);
            } catch (Exception ex) { ex.printStackTrace(); }

            try {
                response4 = node.request(request4);
            } catch (Exception ex) { ex.printStackTrace(); }

            showRequestResponseInfo(request1, response1);
            showRequestResponseInfo(request2, response2);
            showRequestResponseInfo(request3, response3);
            showRequestResponseInfo(request4, response4);
        }
	}

	private static void showRequestResponseInfo(AbstractMessage request, Message response) {
		System.out.println( ""
				+ "" + request.messageName()
				+ " -> " + (response == null ? "none" : response.messageName())
				+ "   referenceId: " + request.getReferenceId()
				+ "   protocolId: " + request.getProtocolId()
				+ "   " + request.getEndpoint()
		);
	}


	private static Endpoint[] parseEndpoints(String addresses) {
		Endpoint[] endpointsArray;
		if (addresses == null || addresses.trim().length() == 0) {
            System.err.println("UCS cluster nodes isn't specified");
			return null;
        }
		String[] uris = addresses.split(",");
		ArrayList<Endpoint> endpoints = new ArrayList<Endpoint>();
		for(String uri : uris) {
            URI u = null;
            try {
                String str = (uri.startsWith("tcp://") ? "" : "tcp://") + uri;
                u = new URI(str);
            } catch (URISyntaxException e) {
                e.printStackTrace();
                System.err.println("Invalid cluster node uri: " + uri);
				return null;
            }
            Endpoint endpoint = new Endpoint(u);
            endpoints.add(endpoint);
        }
		endpointsArray = endpoints.toArray(new Endpoint[endpoints.size()]);
		return endpointsArray;
	}

	private static Properties getProperties() {		
		InputStream is = null;
		Properties props = null;
		try {
			is = getResFile("application.properties");
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

	private static InputStream getResFile(String file) {
		return Thread.currentThread().getContextClassLoader().getResourceAsStream(file);
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
			is = getResFile("log4j2.xml");
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



	private static ClusterChannelListener listener = new ClusterChannelListener() {
		@Override
		public void onInternalChannelOpened(ChannelOpenedEvent event) {
			Protocol p = (Protocol) event.getSource();
			System.out.println("Connection to cluster node is opened." +
					" ProtocolId = " + p.getProtocolId()
					+ " " + event.getEndpoint()
			);
		}

		@Override
		public void onInternalChannelClosed(ChannelClosedEvent event) {
			Protocol p = (Protocol) event.getSource();
			System.out.println("Connection to cluster node is closed." +
					" ProtocolId = " + p.getProtocolId()
					+ " " + event.getEndpoint()
					+ " Cause " + event.getCause());
		}

		@Override
		public void onChannelOpened(EventObject event) {

		}

		@Override
		public void onChannelClosed(ChannelClosedEvent event) {

		}

		@Override
		public void onChannelError(ChannelErrorEvent event) {

		}
	};


}
