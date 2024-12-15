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

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.border.TitledBorder;
import javax.swing.JButton;
import javax.swing.border.EmptyBorder;
import javax.swing.UIManager;

import com.genesyslab.platform.commons.protocol.ClientChannel;
import com.genesyslab.platform.commons.protocol.Endpoint;
import com.genesyslab.platform.commons.protocol.Message;
import com.genesyslab.platform.commons.protocol.MessageHandler;
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
import com.genesyslab.platform.reporting.protocol.statserver.events.EventStatisticClosed;
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

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Properties;

public class StatPrinter extends JFrame {

	private static final long serialVersionUID = 1L;
	private JTextArea textArea;
	private JPanel serverPanel;

	private JButton serverOpenButton;
	private JButton serverCloseButton;
	private JButton statOpenButton;
	private JButton statCloseButton;

	private WarmStandby wsEnabledClient;
	
	private int statisticId = -1;
	
	private Properties appProps = null;

	public StatPrinter() {
		setTitle("Stat Printer Sample");

		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 400, 400);
		getContentPane().setLayout(null);

		JPanel controlPanel = new JPanel();
		controlPanel.setBounds(0, 0, 395, 70);
		getContentPane().add(controlPanel);

		controlPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
		controlPanel.setPreferredSize(new Dimension(400, 90));
		controlPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

		serverPanel = new JPanel();
		serverPanel.setBorder(new TitledBorder(null, "Stat Server", TitledBorder.LEADING, TitledBorder.TOP,
				null, null));
		controlPanel.add(serverPanel);
		serverPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		serverOpenButton = new JButton("Open");
		serverOpenButton.setBounds(0, 0, 89, 23);
		serverPanel.add(serverOpenButton);

		serverCloseButton = new JButton("Close");
		serverCloseButton.setEnabled(false);
		serverPanel.add(serverCloseButton);

		JPanel statPanel = new JPanel();
		statPanel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Statistics",
				TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		controlPanel.add(statPanel);
		statPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		statOpenButton = new JButton("Open");
		statOpenButton.setEnabled(false);
		statPanel.add(statOpenButton);

		statCloseButton = new JButton("Close");
		statCloseButton.setEnabled(false);
		statPanel.add(statCloseButton);

		JPanel miscPanel = new JPanel();
		miscPanel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Display",
				TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		controlPanel.add(miscPanel);
		miscPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		JButton displayClearButton = new JButton("Clear");
		miscPanel.add(displayClearButton);
		displayClearButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				textArea.setText("");
			}
		});

		JPanel textPanel = new JPanel();
		textPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		textPanel.setBounds(0, 69, 395, 307);
		getContentPane().add(textPanel);
		textPanel.setLayout(new CardLayout(0, 0));
		textArea = new JTextArea();
		JScrollPane scrollPane = new JScrollPane(textArea);
		textPanel.add(scrollPane, "scrollPane");

		serverOpenButton.addActionListener(new ConnectAction());
		serverCloseButton.addActionListener(new DisconnectAction());
		statOpenButton.addActionListener(new OpenStatisticsAction());
		statCloseButton.addActionListener(new CloseStatisticsAction());

		addWindowListener(new WindowAction());

		setServerClosed();
		
		appProps = getProperties();
		if(appProps==null) {
			textArea.append("Can't get \'application.properties\' file from resources. \n"
					+ "Check if file available in /resource folder and restart application.");
			
			serverOpenButton.setEnabled(false);
			displayClearButton.setEnabled(false);
		}
	}

	private void setServerOpened() {
		serverOpenButton.setEnabled(false);
		serverCloseButton.setEnabled(true);
		statOpenButton.setEnabled(true);
		statCloseButton.setEnabled(false);
	}

	private void setServerClosed() {
		serverOpenButton.setEnabled(true);
		serverCloseButton.setEnabled(false);
		statOpenButton.setEnabled(false);
		statCloseButton.setEnabled(false);
		if(wsEnabledClient!=null) {
			wsEnabledClient.getChannel().setMessageHandler(null);
			wsEnabledClient.setHandler(null);
			wsEnabledClient = null;
		}	
	}

	private void setStatOpened(int statisticId) {
		statOpenButton.setEnabled(false);
		statCloseButton.setEnabled(true);
		this.statisticId = statisticId;
	}

	private void setStatClosed() {
		statOpenButton.setEnabled(true);
		statCloseButton.setEnabled(false);
		statisticId = -1;
	}

	private class ConnectAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (wsEnabledClient == null) {
				try {
					createStatServerClient();					
					textArea.append("Connecting...\n");
					//Start Warm Standby Service and initiates connection open
					wsEnabledClient.autoRestore();					
					serverOpenButton.setEnabled(false);
					serverCloseButton.setEnabled(true);
				} catch (Throwable t) {
					printThrowable(t);
					setServerClosed();
				}
			}
		}
	}

	private class DisconnectAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (wsEnabledClient != null) {
				try {
					textArea.append("Disconnecting...\n");
					wsEnabledClient.closeAsync(new CompletionHandler<Void, DisconnectAction>(){

						@Override
						public void completed(Void result, DisconnectAction attachment) {
							textArea.append("[AsyncResult] Disconnected.\n");
							setServerClosed();							
						}

						@Override
						public void failed(Throwable exc, DisconnectAction attachment) {							
							printThrowable(exc);
						}						
					}, this);
					
				} catch (Throwable t) {
					printThrowable(t);
				}
			} 
		}
	}

	private class OpenStatisticsAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			if (wsEnabledClient != null) {
				try {
					if(appProps == null) {
						throw new IllegalStateException("Missing application.properties file in /resource dir\n");						
					}
					textArea.append("Opening statistic...\n");
					StatisticObject obj1 = StatisticObject.create(appProps.getProperty("statObj.1.objectId"),
							StatisticObjectType.valueOf(appProps.getProperty("statObj.1.type")),
							appProps.getProperty("statObj.1.tenant"),
							appProps.getProperty("statObj.1.tenantPassword"));

					StatisticMetricEx metric = StatisticMetricEx.create();
					metric.setMainMask(new DnActionsMask());
					metric.getMainMask().setAll();
					metric.setRelativeMask(new DnActionsMask());
					metric.getRelativeMask().clearAll();
					metric.setIntervalType(StatisticInterval.valueOf(appProps
							.getProperty("metric.intervalType")));
					metric.setIntervalLength(Integer.parseInt(appProps.getProperty("metric.intervalLength")));
					metric.setCategory(StatisticCategory.valueOf(appProps.getProperty("metric.category")));
					metric.setSubject(StatisticSubject.valueOf(appProps.getProperty("metric.subject")));

					Notification notification = Notification.create(
							NotificationMode.valueOf(appProps.getProperty("notification.mode")),
							Integer.parseInt(appProps.getProperty("notification.frequency")),
							Integer.parseInt(appProps.getProperty("notification.insensitivity")));

					RequestOpenStatisticEx request = RequestOpenStatisticEx
							.create(obj1, metric, notification);

					ClientChannel channel = wsEnabledClient.getChannel();
					
					channel.requestAsync(request, StatPrinter.this, new CompletionHandler<Message, StatPrinter>() {
						
								@Override
								public void completed(Message result, StatPrinter attachment) {
									if (result instanceof EventStatisticOpened) {									
										setStatOpened(((EventStatisticOpened) result).getReferenceId());
										textArea.append("Done.\n");
									} else {
										textArea.append("Failed to opend statistic: \n" + result + "\n");
									}
								}
								
								@Override
								public void failed(Throwable exc, StatPrinter attachment) {
									printThrowable(exc);
								}
							});
				} catch (Throwable t) {
					printThrowable(t);
				}
			}
		}
	}
	
	private class CloseStatisticsAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (wsEnabledClient != null && statisticId>=0) {
				try {
					textArea.append("Closing statistic...\n");						
					ClientChannel channel = wsEnabledClient.getChannel();					
					RequestCloseStatistic request = RequestCloseStatistic.create();
					request.setStatisticId(statisticId);
					channel.requestAsync(request, StatPrinter.this, new CompletionHandler<Message, StatPrinter>() {
						
						@Override
						public void completed(Message result, StatPrinter attachment) {
							if (result instanceof EventStatisticClosed) {									
								setStatClosed();
								textArea.append("Done.\n");
							} else {
								textArea.append("Failed to close statistic: \n" + result + "\n");
							}
						}

						@Override
						public void failed(Throwable exc, StatPrinter attachment) {
							printThrowable(exc);
						}
					});					
				} catch (Throwable t) {
					printThrowable(t);
					setServerClosed();
				}
			}
		}
	}
	
	//Protocol Message Handler
	private class StatServerMessageHandler implements MessageHandler {

		@Override
		public void onMessage(Message message) {

			switch(message.messageId()) {
				case EventInfo.ID:
					EventInfo info = (EventInfo)message;
					textArea.append(String.format("Received statistic: intValue=%d  stringValue=%s \n",
							info.getIntValue(),
							info.getStringValue()));
					break;
				default:
					textArea.append("Unsolicited evenet: "+message+"\n");
			}
		}
	}

	//WarmStandby Event Handler	
	private class ConnectionEvenetseHandler extends WSHandler {

		public void onChannelOpened(WSOpenedEvent event) {			
			textArea.append("[WSHandler] Client connected!\n");
			setServerOpened();
		}

		public void onChannelDisconnected(WSDisconnectedEvent event) {						
			if(event.getCause() != null) {							
				setStatClosed();
				statOpenButton.setEnabled(false);
				textArea.append("[WSHandler] Client unexpected disconnection. Will try to reconnect.\n");				
			}								
		}

		public void onEndpointTriedUnsuccessfully(WSTriedUnsuccessfullyEvent event) {
			textArea.append("[WSHandler] Tried to connect to " + event.getEndpoint()
					+ "Endpoints have been tried unsuccessfully \n");
		}

		public void onAllEndpointsTriedUnsuccessfully(WSAllTriedUnsuccessfullyEvent event) {
			textArea.append("[WSHandler] All Endpoints have been tried unsuccessfully \n");
		}
	}

	public class WindowAction implements WindowListener {

		//Release Warm Standby service and close client channel
		@Override
		public void windowClosing(WindowEvent arg0) {
			try {
				if (wsEnabledClient != null) {
					wsEnabledClient.getChannel().setMessageHandler(null);
					wsEnabledClient.setHandler(null);
					wsEnabledClient.closeAsync();
				}
			} catch (Throwable t) {
			}
		}

		@Override
		public void windowActivated(WindowEvent arg0) {
		}

		@Override
		public void windowClosed(WindowEvent arg0) {
		}

		@Override
		public void windowDeactivated(WindowEvent arg0) {
		}

		@Override
		public void windowDeiconified(WindowEvent arg0) {
		}

		@Override
		public void windowIconified(WindowEvent arg0) {
		}

		@Override
		public void windowOpened(WindowEvent arg0) {
		}
	}
	
	
	private void createStatServerClient() {
		if(appProps == null) {
			throw new IllegalStateException("Missing application.properties file in /resource dir\n");			
		}
		// See http://docs.genesys.com/Documentation/PSDK/8.5.x/Developer/ConnectingtoaServer#t-1
		StatServerProtocol statServer = new StatServerProtocol();
		// By setting an appropriate invoker, protocol events and callbacks are run by the GUI thread.
		statServer.setInvoker(new SwingInvoker());

		// Protocol events are handled by setting MessageHandler.
		// See http://docs.genesys.com/Documentation/PSDK/latest/Developer/EventHandling#t-1
		statServer.setMessageHandler(new StatServerMessageHandler());

		//Enable Warm Standby for the client
		WarmStandby wsClient = new WarmStandby(statServer);
		Endpoint primaryEndpoint = new Endpoint("StatPrinter",
				appProps.getProperty("statserver.primary.host"), Integer.parseInt(appProps
						.getProperty("statserver.primary.port")));

		Endpoint backupEndpoint = new Endpoint("StatPrinter", appProps.getProperty("statserver.backup.host"),
				Integer.parseInt(appProps.getProperty("statserver.backup.port")));

		WSConfig wsConfig = new WSConfig();
		wsConfig.setEndpoints(primaryEndpoint, backupEndpoint);
		//time required for backup Server to step into primary state after former primary failure.
		wsConfig.setBackupDelay(5000);

		wsClient.setConfig(wsConfig);
		wsClient.setHandler(new ConnectionEvenetseHandler());

		this.wsEnabledClient = wsClient;

	}

	private Properties getProperties() {		
		InputStream is = null;
		AppProperties props = null;
		try {
			is = Thread.currentThread().getContextClassLoader().getResourceAsStream("application.properties");
			if(is != null) {				
				props = new AppProperties();
				props.load(is);
			}
		} catch (IOException ex) {
			printThrowable(ex);
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
			if(value!=null)
				value = value.trim();
			return value;
		}
	}

	private void printThrowable(Throwable e) {
		if (e != null) {
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			textArea.append(String.format("Error happened: %s\n%s\n", e.getMessage(), sw.toString()));
		}
	}
}
