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


import javax.swing.JLabel;
import javax.swing.JPasswordField;

import java.awt.Dimension;
import java.beans.PropertyChangeEvent;

import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

public class PanelAgentSetting extends AbstractPanel{
	private static final long serialVersionUID = 1L;
	private JTextField dnField;
	private JTextField agentIdField;
	private JTextField queueField;
	private JPasswordField agent_ps_Wrd;
	public PanelAgentSetting() {
		setBorder(new TitledBorder(null, "Agent Settings", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		setPreferredSize(new Dimension(400,100));
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{59, 86, 78, 86, 0};
		gridBagLayout.rowHeights = new int[]{20, 20, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JLabel lblNewLabel = new JLabel("DN");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel.insets = new Insets(10, 10, 10, 10);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 0;
		add(lblNewLabel, gbc_lblNewLabel);
		
		dnField = new JTextField();
		dnField.setText("100");
		dnField.setColumns(15);
		GridBagConstraints gbc_dnField = new GridBagConstraints();
		gbc_dnField.anchor = GridBagConstraints.NORTHWEST;
		gbc_dnField.insets = new Insets(10, 10, 10, 10);
		gbc_dnField.gridx = 1;
		gbc_dnField.gridy = 0;
		add(dnField, gbc_dnField);
		
		JLabel lblNewLabel_1 = new JLabel("Queue");
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel_1.insets = new Insets(10, 10, 10, 10);
		gbc_lblNewLabel_1.gridx = 2;
		gbc_lblNewLabel_1.gridy = 0;
		add(lblNewLabel_1, gbc_lblNewLabel_1);
		
		queueField = new JTextField();
		queueField.setText("1010");
		queueField.setColumns(15);
		GridBagConstraints gbc_queueField = new GridBagConstraints();
		gbc_queueField.anchor = GridBagConstraints.NORTHWEST;
		gbc_queueField.insets = new Insets(10, 10, 10, 10);
		gbc_queueField.gridx = 3;
		gbc_queueField.gridy = 0;
		add(queueField, gbc_queueField);
		
		JLabel lblNewLabel_2 = new JLabel("Agent Name");
		GridBagConstraints gbc_lblNewLabel_2 = new GridBagConstraints();
		gbc_lblNewLabel_2.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel_2.insets = new Insets(10, 10, 10, 10);
		gbc_lblNewLabel_2.gridx = 0;
		gbc_lblNewLabel_2.gridy = 1;
		add(lblNewLabel_2, gbc_lblNewLabel_2);
		
		agentIdField = new JTextField();
		agentIdField.setText("100");
		agentIdField.setColumns(15);
		GridBagConstraints gbc_agentIdField = new GridBagConstraints();
		gbc_agentIdField.anchor = GridBagConstraints.NORTHWEST;
		gbc_agentIdField.insets = new Insets(10, 10, 10, 10);
		gbc_agentIdField.gridx = 1;
		gbc_agentIdField.gridy = 1;
		add(agentIdField, gbc_agentIdField);
		
		JLabel lblNewLabel_3 = new JLabel("Agent Password");
		GridBagConstraints gbc_lblNewLabel_3 = new GridBagConstraints();
		gbc_lblNewLabel_3.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel_3.insets = new Insets(10, 10, 10, 10);
		gbc_lblNewLabel_3.gridx = 2;
		gbc_lblNewLabel_3.gridy = 1;
		add(lblNewLabel_3, gbc_lblNewLabel_3);
		
		agent_ps_Wrd = new JPasswordField();
		agent_ps_Wrd.setColumns(15);
		GridBagConstraints gbc_agent_ps_wrd = new GridBagConstraints();
		gbc_agent_ps_wrd.insets = new Insets(10, 10, 10, 10);
		gbc_agent_ps_wrd.anchor = GridBagConstraints.NORTHWEST;
		gbc_agent_ps_wrd.gridx = 3;
		gbc_agent_ps_wrd.gridy = 1;
		add(agent_ps_Wrd, gbc_agent_ps_wrd);
	}
	public String getDn() {
		return dnField.getText();
	}
	public String getAgentId() {
		return agentIdField.getText();
	}
	public String getQueue() {
		return queueField.getText();
	}
	public char[] getAgentPassword() {
		return agent_ps_Wrd.getPassword();
	}
	

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		
	}
}
