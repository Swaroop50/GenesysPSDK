//===============================================================================
// Genesys Platform SDK Samples
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

// Copyright (c) 2007 - 2019 Genesys Telecommunications Laboratories, Inc. All rights reserved.
//===============================================================================
package com.genesyslab.platform.samples.tserver;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.EventObject;

import com.genesyslab.platform.commons.connection.configuration.ClientConnectionOptions;
import com.genesyslab.platform.commons.connection.configuration.ConnectionConfiguration;
import com.genesyslab.platform.commons.connection.configuration.PropertyConfiguration;
import com.genesyslab.platform.commons.connection.configuration.ClientADDPOptions.AddpTraceMode;
import com.genesyslab.platform.commons.protocol.ChannelClosedEvent;
import com.genesyslab.platform.commons.protocol.ChannelClosedOnSendException;
import com.genesyslab.platform.commons.protocol.ChannelErrorEvent;
import com.genesyslab.platform.commons.protocol.ChannelListener;
import com.genesyslab.platform.commons.protocol.ChannelState;
import com.genesyslab.platform.commons.protocol.Endpoint;
import com.genesyslab.platform.commons.protocol.Message;
import com.genesyslab.platform.commons.protocol.MessageHandler;
import com.genesyslab.platform.commons.threading.CompletionHandler;
import com.genesyslab.platform.voice.protocol.TServerProtocol;
import com.genesyslab.platform.voice.protocol.tserver.AddressType;
import com.genesyslab.platform.voice.protocol.tserver.AgentWorkMode;
import com.genesyslab.platform.voice.protocol.tserver.ControlMode;
import com.genesyslab.platform.voice.protocol.tserver.RegisterMode;
import com.genesyslab.platform.voice.protocol.tserver.events.EventAgentLogin;
import com.genesyslab.platform.voice.protocol.tserver.events.EventAgentLogout;
import com.genesyslab.platform.voice.protocol.tserver.events.EventAgentNotReady;
import com.genesyslab.platform.voice.protocol.tserver.events.EventAgentReady;
import com.genesyslab.platform.voice.protocol.tserver.events.EventQueueLogout;
import com.genesyslab.platform.voice.protocol.tserver.events.EventRegistered;
import com.genesyslab.platform.voice.protocol.tserver.events.EventUnregistered;
import com.genesyslab.platform.voice.protocol.tserver.requests.agent.RequestAgentLogin;
import com.genesyslab.platform.voice.protocol.tserver.requests.agent.RequestAgentLogout;
import com.genesyslab.platform.voice.protocol.tserver.requests.agent.RequestAgentNotReady;
import com.genesyslab.platform.voice.protocol.tserver.requests.agent.RequestAgentReady;
import com.genesyslab.platform.voice.protocol.tserver.requests.dn.RequestRegisterAddress;
import com.genesyslab.platform.voice.protocol.tserver.requests.dn.RequestUnregisterAddress;

public class AgentModel extends AgentModelBase {

	private TServerProtocol channel;
	private ChannelEventsListener channelListener;
	
	/**
	 * Creates AgentModel that communicates with T-Server using asynchronous
	 * channel methods.
	 * 
	 * See http://docs.genesys.com/Documentation/PSDK/8.5.x/Developer/EventHandling#Receiving_a_Response_Asynchronously	 
	 */
	public AgentModel() {
	    
		//default ADDP settings
		//See http://docs.genesys.com/Documentation/PSDK/8.5.x/Developer/ConnectingtoaServer#Configuring_ADDP
		PropertyConfiguration options = new PropertyConfiguration();
		options.setUseAddp(true);
		options.setAddpClientTimeout(3);
		options.setAddpServerTimeout(4);
		options.setAddpTraceMode(AddpTraceMode.Local);
				
		channel = new TServerProtocol(new Endpoint("PSDK-Sample-App", "<Not Specified>", 7000, options));
		channelListener = new ChannelEventsListener();
		channel.addChannelListener(channelListener);
		channel.setMessageHandler(new ReceivedMessageHandler());	
		
		//Notify handlers from UI thread.
		//see http://docs.genesys.com/Documentation/PSDK/latest/Developer/ConnectingtoaServer#AsyncInvokers
		channel.setInvoker(new SwingInvoker());
	}
	
	
	/**
	 * Get client connection options like ADDP, String encoding, etc.	
	 * See http://docs.genesys.com/Documentation/PSDK/8.5.x/Developer/ManagedProtocolConfig
	 * @return
	 */
	public ClientConnectionOptions getConnectionConfiguration() {
		ClientConnectionOptions options = null;
		if (channel != null) {
			Endpoint ep = channel.getEndpoint();
			if (ep != null) {
				ConnectionConfiguration conf = ep.getConfiguration();
				if (conf instanceof ClientConnectionOptions) {
					options = (ClientConnectionOptions) conf;
				}
			}			
		}
		return options;
	}


	/**
	 * Updates Endpoint's host, port and client name (client name will be present on server side logs).
	 * 
	 * @param host
	 * @param port
	 */
	protected void updateEndpoint(String name, String host, int port) {
		Endpoint existing = channel.getEndpoint();
		if (existing == null) {
			channel.setEndpoint(new Endpoint(name, host, port));
			return;
		}
		if (!existing.getHost().equals(host) || existing.getPort() != port || existing.getName() != name) {
			channel.setEndpoint(new Endpoint(name, host, port, existing.getConfiguration()));
		}
	}


	/**
	 * Initiates connection with T-Server. Result is processed in
	 * CompletionHandler which is invoked by the channel invoker.
	 * 
	 * @param host  server host
	 * @param port  server port
	 * @param clientName client name
	 * @param password client password
	 */
	public void connect(String host, int port, String clientName, String password) {
		if(channel.getState() != ChannelState.Closed)
			return;
		updateEndpoint("PSDK-Sample-App", host, port);
		channel.setClientName(clientName);
		channel.setClientPassword(password);
		displayControlMessage("Connecting...");
		channel.openAsync(new CompletionHandler<EventObject, Object>() {

			public void completed(EventObject result, Object obj) {
				setConnected(true);
				displayControlMessage("Connection opened!");
			}

			public void failed(Throwable ex, Object obj) {
				displayControlMessage(printThrowable(ex));
			}

		}, this);
	}

	/**
	 * Initiates disconnects from T-Server. Result is processed in
	 * CompletionHandler which is invoked by the channel invoker.
	 */
	public void diconnect() {
		displayControlMessage("Closing...");
		if (channel == null || channel.getState() == ChannelState.Closed) {
			return;
		}
		channel.closeAsync(new CompletionHandler<ChannelClosedEvent, Object>() {

			public void completed(ChannelClosedEvent arg0, Object obj) {
				setConnected(false);
			}

			public void failed(Throwable ex, Object obj) {
				displayControlMessage(printThrowable(ex));
			}

		}, this);
	}
	
	/**
	 * Initiate closing, remove channel listeners
	 */
	public void dispose() {
		if (channel == null)
			return;
		if(channelListener!=null) {
			channel.removeChannelListener(channelListener);
			channelListener = null;
		}		
		channel.closeAsync();			
	}

	/**
	 * Registers for a DN. Your application must register the DN before sending
	 * the request RequestAgentLogin. Server response is processed in
	 * CompletionHandler which is invoked by the channel invoker.
	 * 
	 * @param dn DN of the controlling agent or Route Point.
	 */
	public void register(String dn) {
		RequestRegisterAddress request = RequestRegisterAddress.create(dn,
				RegisterMode.ModePrivate, ControlMode.RegisterDefault, AddressType.DN);
		try {

			channel.requestAsync(request, this, new CompletionHandler<Message, Object>() {

				public void completed(Message message, Object obj) {
					displayControlMessage("Received: \n" + message.toString());
					if (EventRegistered.ID == message.messageId()) {
						setDn(((EventRegistered)message).getThisDN());
						setRegistered(true);
					}
					else
						displayControlMessage("\n Can't register.");
				}

				public void failed(Throwable arg0, Object obj) {
					displayControlMessage(printThrowable(arg0));
				}

			});
		} catch (ChannelClosedOnSendException closedEx) {
			displayControlMessage("Channel should be opened before sending request");
			displayControlMessage(printThrowable(closedEx));
		} catch (Throwable e) {
			displayControlMessage(printThrowable(e));
		}

	}

	/**
	 * Unregisters for a DN Server response is processed in CompletionHandler
	 * which is invoked by the channel invoker.
	 * 
	 * @param dn DN of the controlling agent or Route Point.
	 */
	public void unregister(String dn) {
		RequestUnregisterAddress request = RequestUnregisterAddress.create(dn,
				ControlMode.RegisterDefault);
		try {

			channel.requestAsync(request, this, new CompletionHandler<Message, Object>() {

				public void completed(Message message, Object obj) {
					displayControlMessage("Received: \n" + message.toString());
					if (EventUnregistered.ID == message.messageId()) {
						setDn(null);
						setRegistered(false);
					}
					else
						displayControlMessage("\n Can't unregister.");
				}

				public void failed(Throwable arg0, Object obj) {
					displayControlMessage(printThrowable(arg0));
				}

			});
		} catch (ChannelClosedOnSendException closedEx) {
			displayControlMessage("Channel should be opened before sending request");
			displayControlMessage(printThrowable(closedEx));
		} catch (Throwable e) {
			displayControlMessage(printThrowable(e));
		}
	}

	/**
	 * Logs in the agent. Server response is processed in CompletionHandler
	 * which is invoked by the channel invoker.
	 * 
	 * @param dn DN of the controlling agent or Route Point.
	 * @param queue The queue related to ThisDN
	 * @param agentID The agent identifier specified by PBX or ACD.
	 * @param password Password that allows the agent to be logged in.
	 */
	public void login(String dn, String queue, String agentId, String password) {
		RequestAgentLogin request = RequestAgentLogin.create(dn, AgentWorkMode.Unknown, queue,
				agentId, password, null, null);
		try {

			channel.requestAsync(request, this, new CompletionHandler<Message, Object>() {

				public void completed(Message message, Object obj) {
					displayControlMessage("Received: \n" + message.toString());
					if (EventAgentLogin.ID == message.messageId()) {
						setQueue(((EventAgentLogin)message).getThisQueue());
						setLoggedIn(true);
					}
					else
						displayControlMessage("\n Can't login.");
				}

				public void failed(Throwable arg0, Object obj) {
					displayControlMessage(printThrowable(arg0));
				}

			});
		} catch (ChannelClosedOnSendException closedEx) {
			displayControlMessage("Channel should be opened before sending request");
			displayControlMessage(printThrowable(closedEx));
		} catch (Throwable e) {
			displayControlMessage(printThrowable(e));
		}
	}

	/**
	 * Logs the agent out of the DN Server response is processed in
	 * CompletionHandler which is invoked by the channel invoker.
	 * 
	 * @param dn DN of the controlling agent or Route Point.
	 * @param queue The queue related to ThisDN
	 */
	public void logout(String dn, String queue) {
		RequestAgentLogout request = RequestAgentLogout.create(dn, null, null, null);
		try {
			channel.requestAsync(request, this, new CompletionHandler<Message, Object>() {

				public void completed(Message message, Object obj) {
					displayControlMessage("Received: \n" + message.toString());
					if (EventAgentLogout.ID == message.messageId()) {
						setQueue(null);
						setLoggedIn(false);
					} else if (EventQueueLogout.ID == message.messageId()) {
						// in sample we using one queue - assumes this event
						// as complete logout.
						setQueue(null);
						setLoggedIn(false);
					} else {
						displayControlMessage("\n Can't logout.");
					}
				}

				public void failed(Throwable arg0, Object obj) {
					displayControlMessage(printThrowable(arg0));
				}

			});
		} catch (ChannelClosedOnSendException closedEx) {
			displayControlMessage("Channel should be opened before sending request");
			displayControlMessage(printThrowable(closedEx));
		} catch (Throwable e) {
			displayControlMessage(printThrowable(e));
		}
	}

	/**
	 * Sets the agent status to ready for this DN. In ready status, the agent is
	 * ready to receive calls. Server response is processed in CompletionHandler
	 * which is invoked by the channel invoker.
	 * 
	 * @param thisDN DN of the controlling agent or Route Point.
	 * @param thisQueue The queue related to ThisDN
	 */
	public void setReady(String dn, String queue) {
		RequestAgentReady request = RequestAgentReady.create(dn, AgentWorkMode.Unknown, queue,
				null, null);
		try {
			
			channel.requestAsync(request, this, new CompletionHandler<Message, Object>() {

				public void completed(Message message, Object obj) {
					displayControlMessage("Received: \n" + message.toString());
					if (EventAgentReady.ID == message.messageId())
						setReady(true);
					else
						displayControlMessage("\n Can't set agent ready.");
				}

				public void failed(Throwable arg0, Object obj) {
					displayControlMessage(printThrowable(arg0));
				}

			});
		} catch (ChannelClosedOnSendException closedEx) {
			displayControlMessage("Channel should be opened before sending request");
			displayControlMessage(printThrowable(closedEx));
		} catch (Throwable e) {
			displayControlMessage(printThrowable(e));
		}
	}

	/**
	 * Sets the agent status to not ready Server response is processed in
	 * CompletionHandler which is invoked by the channel invoker.
	 * 
	 * @param thisDN DN of the controlling agent or Route Point.
	 * @param thisQueue The queue related to ThisDN
	 */
	public void setNotReady(String dn, String queue) {
		RequestAgentNotReady request = RequestAgentNotReady.create(dn, AgentWorkMode.Unknown,
				queue, null, null);
		try {
			channel.requestAsync(request, this, new CompletionHandler<Message, Object>() {

				public void completed(Message message, Object obj) {
					displayControlMessage("Received: \n" + message.toString());
					if (EventAgentNotReady.ID == message.messageId())
						setReady(false);
					else
						displayControlMessage("\n Can't set agent not ready.");
				}

				public void failed(Throwable arg0, Object obj) {
					displayControlMessage(printThrowable(arg0));
				}

			});
		} catch (ChannelClosedOnSendException closedEx) {
			displayControlMessage("Channel should be opened before sending request");
			displayControlMessage(printThrowable(closedEx));
		} catch (Throwable e) {
			displayControlMessage(printThrowable(e));
		}
	}


	public String getDescription() {
		return "This Platform SDK Sample communicates with TServer using asynchronouse methods";
	}

	/**
	 * Display service messages on the view.
	 * 
	 * @param controlMessage
	 */
	protected void displayControlMessage(String controlMessage) {
		firePropertyChange("controlMessage", "", controlMessage);
	}

	/**
	 * Format exception.
	 * 
	 * @param e
	 * @return
	 */
	protected String printThrowable(Throwable e) {
		if (e == null)
			return "";
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		return String.format("Error happened: %s\n%s", e.getMessage(), sw.toString());
	}	
	
	
	/**
	 * Implements ChannelListener. Handles channel Open, Close events and
	 * Channel Errors.
	 */
	private class ChannelEventsListener implements ChannelListener {

		/**
		 * Change model state to connected.
		 */
		public void onChannelOpened(EventObject arg0) {
		}

		/**
		 * Change model state to disconnected.
		 */
		public void onChannelClosed(ChannelClosedEvent arg0) {
			setConnected(false);
			displayControlMessage("Connection closed! \n" + printThrowable(arg0.getCause()));
		}

		/**
		 * Display channel errors on the main panel.
		 */
		public void onChannelError(ChannelErrorEvent arg0) {
			displayControlMessage(printThrowable(arg0.getCause()));
		}
	}
	
	/**
	 * Implements MessageHandler. Should be used to processes unsolicited events from
	 * T-Server, for example incoming calls.
	 * 
	 * See http://docs.genesys.com/Documentation/PSDK/8.5.x/Developer/EventHandling#Receiving_Messages_Asynchronously
	 */
	private class ReceivedMessageHandler implements MessageHandler {
	
		public void onMessage(Message message) {
			displayControlMessage("Unsolicited event: \n" + message.toString());
	
		}
	}

}
