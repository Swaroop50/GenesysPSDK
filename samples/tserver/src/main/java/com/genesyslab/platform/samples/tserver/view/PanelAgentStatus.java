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

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JCheckBox;
import java.awt.FlowLayout;
import javax.swing.border.TitledBorder;
import java.awt.CardLayout;
import java.beans.PropertyChangeEvent;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;

public class PanelAgentStatus extends AbstractPanel {

	private static final long serialVersionUID = 1L;
	private JCheckBox chckbxConnected;
	private JCheckBox chckbxDnRegistered;
	private JCheckBox chckbxAgentLoggedIn;
	private JCheckBox chckbxAgentReady;
	private JTextArea textArea;
	
	/**
	 * Create the panel.
	 */
	public PanelAgentStatus() {
		setBorder(new TitledBorder(null, "Agent Status", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		setPreferredSize(new Dimension(500,300));
		setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();	
		add(panel, BorderLayout.NORTH);
		panel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		chckbxConnected = new JCheckBox("Connected");
		chckbxConnected.setEnabled(false);
		chckbxConnected.setMinimumSize(new Dimension(100, 23));
		panel.add(chckbxConnected);
		
		chckbxDnRegistered = new JCheckBox("DN registered");
		chckbxDnRegistered.setEnabled(false);
		chckbxDnRegistered.setMinimumSize(new Dimension(100, 23));
		panel.add(chckbxDnRegistered);
		
		chckbxAgentLoggedIn = new JCheckBox("Agent logged in");
		chckbxAgentLoggedIn.setEnabled(false);
		panel.add(chckbxAgentLoggedIn);
		
		chckbxAgentReady = new JCheckBox("Agent is ready");
		chckbxAgentReady.setEnabled(false);
		panel.add(chckbxAgentReady);
		
		JPanel panel_1 = new JPanel();
		add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(new CardLayout(0, 0));
		
		textArea = new JTextArea();
		
		
		JScrollPane scrollPane = new JScrollPane(textArea);
		panel_1.add(scrollPane, "name_152417337491106");
				
	}
	
	public void setMsg(String value) {
		textArea.append(value+"\n");
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
				chckbxConnected.setSelected((Boolean) value);
			}
		} else if (name.equalsIgnoreCase("registered")) {
			Object value = evt.getNewValue();
			if (value instanceof Boolean) {
				chckbxDnRegistered.setSelected((Boolean) value);
			}
		} else if (name.equalsIgnoreCase("loggedIn")) {
			Object value = evt.getNewValue();
			if (value instanceof Boolean) {
				chckbxAgentLoggedIn.setSelected((Boolean) value);
			}
		} else if (name.equalsIgnoreCase("ready")) {
			Object value = evt.getNewValue();
			if (value instanceof Boolean) {
				chckbxAgentReady.setSelected((Boolean) value);
			}
		} else if (name.equalsIgnoreCase("controlMessage")) {
			Object value = evt.getNewValue();
			if (value instanceof String) {
				textArea.append(value + "\n");				
			}
		}
	}
			
		
}
