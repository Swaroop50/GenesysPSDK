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

package com.genesyslab.platform.samples.tserver.view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JMenu;

import com.genesyslab.platform.samples.tserver.AgentModel;


public class MainView extends JFrame {


	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	
	private PanelConnection connectionPanel;
	private PanelAgentSetting agentViewPanel;
	private PanelAgentControl agentControlPanel;
	private PanelAgentStatus agentStatusPanel;
	private JMenuBar menuBar;
	private JMenu settingsMenu;
	private JMenuItem addpMenuItem;
	
	private AgentModel model;

	/**
	 * Create the frame.
	 */
	public MainView(AgentModel agentService) {
		this.model = agentService;
		
		setTitle("T-Server Client Sample");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 646, 571);
		
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		settingsMenu = new JMenu("Settings");
		menuBar.add(settingsMenu);
		
		addpMenuItem = new JMenuItem("Addp");
		settingsMenu.add(addpMenuItem);
				
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.CENTER);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
				
		connectionPanel = new PanelConnection();
		panel.add(connectionPanel);
		
		agentViewPanel = new PanelAgentSetting();
		panel.add(agentViewPanel);
		
		agentControlPanel = new PanelAgentControl();
		contentPane.add(agentControlPanel, BorderLayout.EAST);
		
		agentStatusPanel = new PanelAgentStatus();
		agentStatusPanel.setMsg(agentService.getDescription());
		contentPane.add(agentStatusPanel, BorderLayout.SOUTH);		
		
		agentControlPanel.addBtnOpenListener(new ConnectionAction());
		agentControlPanel.addBtnCloseListener(new DisconnectAction());

		agentControlPanel.addBtnRegListener(new RegisterAction());
		agentControlPanel.addBtnUnregListener(new UnregisterAction());

		agentControlPanel.addBtnLoginListener(new LoginAction());
		agentControlPanel.addBtnLogoutListener(new LogoutAction());

		agentControlPanel.addBtnReadyListener(new ReadyAction());
		agentControlPanel.addBtnNotReadyListener(new NotReadyAction());

		addpMenuItem.addActionListener(new AddpMenuItemAction(agentService));		
		addWindowListener(new MainWindowListener(model));
				
		agentService.addPropertyChangeListener(agentControlPanel);
		agentService.addPropertyChangeListener(agentStatusPanel);
		
	}
	
	public PanelAgentControl getAgentControlPanel() {
		return agentControlPanel;
	}
	
	public PanelAgentSetting getAgentViewPanel() {
		return agentViewPanel;
	}
	
	public PanelConnection getConnectionPanel() {
		return connectionPanel;
	}
	
	public PanelAgentStatus getAgentStatusPanel() {
		return agentStatusPanel;
	}
		
	public void addAddpMenuListener(ActionListener addpMenu) {
		 addpMenuItem.addActionListener(addpMenu);
	}
	
	public void removeAddpMenuListener(ActionListener addpMenu) {
		 addpMenuItem.removeActionListener(addpMenu);
	}

	/**
	 * Initiates connection to T-Server.
	 */
	private class ConnectionAction implements ActionListener {
		
		public void actionPerformed(ActionEvent e) {
			String host = getConnectionPanel().getHost();
			int port = getConnectionPanel().getPort();
			
			if (host == null || "".equals(host)) {
				JOptionPane.showMessageDialog(null, "Specify correct host name.", "TServer host name empty.",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			if (port == -1) {
				JOptionPane.showMessageDialog(null, "Specify correct port name.", "TServer port empty.",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			model.connect(host, port, connectionPanel.getClientName(), connectionPanel.getPassword());
		}
	}

	/**
	 * Initiates disconnect from T-Server.
	 */
	private class DisconnectAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) {
			model.diconnect();
		}
	}

	/**
	 * Initiates Agent registration for a DN.
	 *
	 */
	private class RegisterAction extends AbstractAction {

		private static final long serialVersionUID = 1L;
		
		public void actionPerformed(ActionEvent e) {
			String thisDn = agentViewPanel.getDn();
			model.register(thisDn);
		}
	}

	/**
	 * Initiates Agent unregister.
	 *
	 */
	private class UnregisterAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) {
			String thisDn = agentViewPanel.getDn();
			model.unregister(thisDn);
		}
	}

	/**
	 * Initiates Agent login.
	 *
	 */
	private class LoginAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) {						
			String thisDn = agentViewPanel.getDn();
			String queue = agentViewPanel.getQueue();
			String agentId = agentViewPanel.getAgentId();
			String ps_wrd = new String(agentViewPanel.getAgentPassword());
			
			model.login(thisDn, queue, agentId, ps_wrd);
		}
	}

	/**
	 * Initiates Agent logout.
	 *
	 */
	private class LogoutAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) {			
			String thisDn = agentViewPanel.getDn();
			String queue = agentViewPanel.getQueue();	
			
			model.logout(thisDn, queue);
		}
	}

	/**
	 * Initiates Agent state ready.
	 *
	 */
	private class ReadyAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) {			
			String thisDn = agentViewPanel.getDn();
			String queue = agentViewPanel.getQueue();
			
			model.setReady(thisDn, queue);
		}
	}

	/**
	 * Initiates Agent state not ready.
	 *
	 */
	private class NotReadyAction extends AbstractAction {

		private static final long serialVersionUID = 1L;
	
		public void actionPerformed(ActionEvent e) {			
			String thisDn = agentViewPanel.getDn();
			String queue = agentViewPanel.getQueue();
			
			model.setNotReady(thisDn, queue);
		}
	}

	
	private class MainWindowListener implements WindowListener {
		
		private AgentModel model;
		
		public MainWindowListener(AgentModel model) {
			if(model==null)
				throw new IllegalArgumentException("The model can't be null");
			this.model = model;
		}
		
		@Override
		public void windowClosing(WindowEvent arg0) {
			model.dispose();
		}

		@Override
		public void windowActivated(WindowEvent arg0) {	}

		@Override
		public void windowClosed(WindowEvent arg0) { }

		@Override
		public void windowDeactivated(WindowEvent arg0) { }

		@Override
		public void windowDeiconified(WindowEvent arg0) { }

		@Override
		public void windowIconified(WindowEvent arg0) {	}

		@Override
		public void windowOpened(WindowEvent arg0) { }
	}
	
	
	
}
