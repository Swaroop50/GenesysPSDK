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

import java.util.EventObject;

import java.util.HashMap;
import java.util.Map;

import com.genesyslab.platform.commons.collections.KeyValueCollection;
import com.genesyslab.platform.commons.log.ILogger;
import com.genesyslab.platform.commons.log.Log;
import com.genesyslab.platform.commons.protocol.ChannelClosedEvent;
import com.genesyslab.platform.commons.protocol.ChannelErrorEvent;
import com.genesyslab.platform.commons.protocol.ClientRequestHandler;
import com.genesyslab.platform.commons.protocol.DuplexChannel;
import com.genesyslab.platform.commons.protocol.Endpoint;
import com.genesyslab.platform.commons.protocol.Message;
import com.genesyslab.platform.commons.protocol.OutputChannel;
import com.genesyslab.platform.commons.protocol.RequestContext;
import com.genesyslab.platform.commons.protocol.ServerChannelListener;
import com.genesyslab.platform.commons.threading.InvokerFactory;
import com.genesyslab.platform.openmedia.protocol.ExternalServiceProtocolListener;
import com.genesyslab.platform.openmedia.protocol.externalservice.event.Event3rdServerFault;
import com.genesyslab.platform.openmedia.protocol.externalservice.event.Event3rdServerResponse;
import com.genesyslab.platform.openmedia.protocol.externalservice.request.Request3rdServer;

/**
 * ESP-based server for the WarmStandby sample. 
 */
public class EspServer extends ExternalServiceProtocolListener {

	private static ILogger log = Log.getLogger(EspServer.class);

	private static final String invokername = "ServerInvoker";
	private int port = 0;

	private ServerChannelListener channelListener;
	private ClientRequestHandler requestHandler;

	public EspServer(Endpoint endpoint) {
		super(endpoint);

		//Assign separate invoker for the server object, to not share the same one with client.
		//See also http://docs.genesys.com/Documentation/PSDK/latest/Developer/ConnectingtoaServer#AsyncInvokers
		setInvoker(InvokerFactory.namedInvoker(invokername));

		channelListener = new ServerChannelListener() {

			public void onChannelOpened(EventObject event) {
				port = EspServer.this.getLocalEndPoint().getPort();
				System.out.println("[Server] started listening on port: " + port);
			}

			public void onChannelClosed(ChannelClosedEvent event) {
				System.out.println("[Server] stopped listening on port: " + port);
				port = 0;
				InvokerFactory.releaseInvoker(invokername);
			}

			public void onChannelError(ChannelErrorEvent event) {
				System.out.println("Channel error happened: " + event);
			}

			public void onClientChannelOpened(OutputChannel channel) {
				((DuplexChannel) channel).setInvoker(InvokerFactory.namedInvoker(invokername));
			}

			public void onClientChannelClosed(ChannelClosedEvent event) {
				InvokerFactory.releaseInvoker(invokername);
			}

		};
		addChannelListener(channelListener);

		requestHandler = new ClientRequestHandler() {

			//Process requests, received from client and send response.
			public void processRequest(RequestContext context) {
				Message message = context.getRequestMessage();
				Message response = null;
				if (message instanceof Request3rdServer) {
					Request3rdServer request = (Request3rdServer) message;
					int refId = request.getReferenceId();
					KeyValueCollection usrRequest = request.getRequest();
					if (usrRequest != null) {
						Integer id = usrRequest.getInt("id");
						switch (id) {
						case 1:
							response = validateClientCredentials(usrRequest, refId);
							break;
						default:
							Event3rdServerResponse ok = Event3rdServerResponse.create();
							KeyValueCollection data = new KeyValueCollection();
							data.addInt("id", 200);
							data.addString("server response", "OK");
							ok.setRequest(data);
							ok.setReferenceId(refId);
							response = ok;
						}
					} else {
						Event3rdServerFault fault = Event3rdServerFault.create();
						fault.setReferenceId(refId);
						response = fault;
						log.warn("[Server] Empty request field: " + message);
					}

				} else {
					log.warn("[Server] Unexpected message: " + message);
				}

				try {
					if (response != null) {
						context.getClientChannel().send(response);
					}
				} catch (Exception e) {
					System.out.println("[Server] error happened: " + e.getMessage());
					e.printStackTrace();
				}
			}

		};
		setClientRequestHandler(requestHandler);

		grantAccess("usr", "pwd");
	}

	public void closeSilent() {
		if (channelListener != null) {
			removeChannelListener(channelListener);
		}
		try {
			close();
		} catch (Throwable e) {
			log.error(e.getMessage(), e);
		}
	}

	private Map<String, String> users = new HashMap<String, String>();

	public void grantAccess(String name, String ps_wrd) {
		users.put(name, ps_wrd);
	}

	//Check client credentials in the registration message.
	private Message validateClientCredentials(KeyValueCollection data, int refId) {
		String usr = data.getString("user");
		String ps_wrd = data.getString("password");
		if (usr == null || ps_wrd == null || !users.containsKey(usr) || !ps_wrd.equals(users.get(usr))) {
			Event3rdServerFault fault = Event3rdServerFault.create();
			fault.setReferenceId(refId);
			return fault;
		}
		KeyValueCollection respData = new KeyValueCollection();
		respData.addInt("id", 101);
		Event3rdServerResponse resp = Event3rdServerResponse.create();
		resp.setRequest(respData);
		resp.setReferenceId(refId);
		return resp;

	}
}
