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

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;

import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;

public class PanelAgentControl extends AbstractPanel {

	private static final long serialVersionUID = 1L;
	private JButton btnOpen;
	private JButton btnRegisterDn;
	private JButton btnLogin;
	private JButton btnAgentReady;
	private JButton btnAgentNotReady;
	private JButton btnLogout;
	private JButton btnUnregisterDn;
	private JButton btnClose;

	/**
	 * Create the panel.
	 */
	public PanelAgentControl() {
		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		setPreferredSize(new Dimension(150, 250));

		btnOpen = new JButton("Open");
		btnOpen.setMinimumSize(new Dimension(130, 25));
		btnOpen.setMaximumSize(new Dimension(130, 25));
		btnOpen.setEnabled(true);
		add(btnOpen);

		btnRegisterDn = new JButton("Register DN");
		btnRegisterDn.setMinimumSize(new Dimension(130, 25));
		btnRegisterDn.setMaximumSize(new Dimension(130, 25));
		btnRegisterDn.setEnabled(false);
		add(btnRegisterDn);

		btnLogin = new JButton("Login");
		btnLogin.setMinimumSize(new Dimension(130, 25));
		btnLogin.setMaximumSize(new Dimension(130, 25));
		btnLogin.setEnabled(false);
		add(btnLogin);

		btnAgentReady = new JButton("Agent Ready");
		btnAgentReady.setMinimumSize(new Dimension(130, 25));
		btnAgentReady.setMaximumSize(new Dimension(130, 25));
		btnAgentReady.setEnabled(false);
		add(btnAgentReady);

		btnAgentNotReady = new JButton("Agent not ready");
		btnAgentNotReady.setMinimumSize(new Dimension(130, 25));
		btnAgentNotReady.setMaximumSize(new Dimension(130, 25));
		btnAgentNotReady.setEnabled(false);
		add(btnAgentNotReady);

		btnLogout = new JButton("Logout");
		btnLogout.setMinimumSize(new Dimension(130, 25));
		btnLogout.setMaximumSize(new Dimension(130, 25));
		btnLogout.setEnabled(false);
		add(btnLogout);

		btnUnregisterDn = new JButton("Unregister DN");
		btnUnregisterDn.setMinimumSize(new Dimension(130, 25));
		btnUnregisterDn.setMaximumSize(new Dimension(130, 25));
		btnUnregisterDn.setEnabled(false);
		add(btnUnregisterDn);

		btnClose = new JButton("Close");
		btnClose.setMinimumSize(new Dimension(130, 25));
		btnClose.setMaximumSize(new Dimension(130, 25));
		btnClose.setEnabled(false);
		add(btnClose);
	}

	public void addBtnOpenListener(ActionListener openListener) {
		btnOpen.addActionListener(openListener);
	}

	public void removeBtnOpenListener(ActionListener openListener) {
		btnOpen.removeActionListener(openListener);
	}

	public void addBtnRegListener(ActionListener regDnListener) {
		btnRegisterDn.addActionListener(regDnListener);
	}

	public void removeBtnRegListener(ActionListener regDnListener) {
		btnRegisterDn.removeActionListener(regDnListener);
	}

	public void addBtnLoginListener(ActionListener loginListener) {
		btnLogin.addActionListener(loginListener);
	}

	public void removeBtnLoginListener(ActionListener loginListener) {
		btnLogin.removeActionListener(loginListener);
	}

	public void addBtnReadyListener(ActionListener readyListener) {
		btnAgentReady.addActionListener(readyListener);
	}

	public void removeBtnReadyListener(ActionListener readyListener) {
		btnAgentReady.removeActionListener(readyListener);
	}

	public void addBtnNotReadyListener(ActionListener notReadyListener) {
		btnAgentNotReady.addActionListener(notReadyListener);
	}

	public void removeBtnNotReadyListener(ActionListener notReadyListener) {
		btnAgentNotReady.removeActionListener(notReadyListener);
	}

	public void addBtnLogoutListener(ActionListener logoutListener) {
		btnLogout.addActionListener(logoutListener);
	}

	public void removeBtnLogoutListener(ActionListener logoutListener) {
		btnLogout.removeActionListener(logoutListener);
	}

	public void addBtnUnregListener(ActionListener unregListener) {
		btnUnregisterDn.addActionListener(unregListener);
	}

	public void removeBtnUnregListener(ActionListener unregListener) {
		btnUnregisterDn.removeActionListener(unregListener);
	}

	public void addBtnCloseListener(ActionListener closeListener) {
		btnClose.addActionListener(closeListener);
	}

	public void removeBtnCloseListener(ActionListener closeListener) {
		btnClose.removeActionListener(closeListener);
	}

	/**
	 * Update UI when model is changed.
	 */
	@Override
	public void propertyChange(PropertyChangeEvent evt) {

		String name = evt.getPropertyName();

		if (name.equalsIgnoreCase("connected")) {
			Object value = evt.getNewValue();
			if (value instanceof Boolean) {
				setConnectedEvent((Boolean) value);
			}
		} else if (name.equalsIgnoreCase("registered")) {
			Object value = evt.getNewValue();
			if (value instanceof Boolean) {
				setRegisteredEvent((Boolean) value);
			}
		} else if (name.equalsIgnoreCase("loggedIn")) {
			Object value = evt.getNewValue();
			if (value instanceof Boolean) {
				setLoginedEvent((Boolean) value);
			}
		} else if (name.equalsIgnoreCase("ready")) {
			Object value = evt.getNewValue();
			if (value instanceof Boolean) {
				setReadyEvent((Boolean) value);
			}
		}
	}

	private void setConnectedEvent(boolean value) {
		btnOpen.setEnabled(!value);
		btnClose.setEnabled(value);

		btnRegisterDn.setEnabled(value);
		btnUnregisterDn.setEnabled(false);

		btnLogout.setEnabled(false);
		btnAgentNotReady.setEnabled(false);
	}

	private void setRegisteredEvent(boolean value) {
		btnRegisterDn.setEnabled(!value);
		btnUnregisterDn.setEnabled(value);

		btnLogin.setEnabled(value);
		btnLogout.setEnabled(false);

		btnAgentNotReady.setEnabled(false);
	}

	private void setLoginedEvent(boolean value) {
		btnLogin.setEnabled(!value);
		btnLogout.setEnabled(value);

		btnUnregisterDn.setEnabled(!value);

		btnAgentReady.setEnabled(value);
		btnAgentNotReady.setEnabled(false);
	}

	private void setReadyEvent(boolean value) {
		btnAgentReady.setEnabled(!value);
		btnAgentNotReady.setEnabled(value);
	}

}
