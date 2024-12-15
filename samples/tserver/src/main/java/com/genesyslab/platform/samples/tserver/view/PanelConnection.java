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

import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

public class PanelConnection extends JPanel{
	private static final long serialVersionUID = 1L;
	private JTextField hostField;
	private JTextField portField;
	private JTextField clientNameField;
	private JTextField ps_Field;
	public PanelConnection() {
		setBorder(new TitledBorder(null, "Connection Settings", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		setPreferredSize(new Dimension(400,100));
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{57, 78, 76, 78, 0};
		gridBagLayout.rowHeights = new int[]{20, 20, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JLabel lblNewLabel = new JLabel("Host");		
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel.insets = new Insets(10, 10, 10, 10);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 0;
		add(lblNewLabel, gbc_lblNewLabel);
		hostField = new JTextField();
		hostField.setText("<TServer Host>");
		hostField.setColumns(15);
		GridBagConstraints gbc_hostField = new GridBagConstraints();
		gbc_hostField.anchor = GridBagConstraints.NORTHWEST;
		gbc_hostField.insets = new Insets(10, 10, 10, 10);
		gbc_hostField.gridx = 1;
		gbc_hostField.gridy = 0;
		add(hostField, gbc_hostField);
		
		JLabel lblNewLabel_1 = new JLabel("Port");		
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel_1.insets = new Insets(10, 10, 10, 10);
		gbc_lblNewLabel_1.gridx = 2;
		gbc_lblNewLabel_1.gridy = 0;
		add(lblNewLabel_1, gbc_lblNewLabel_1);
		portField = new JTextField();
		portField.setText("<TServer Port>");
		portField.setColumns(15);
		GridBagConstraints gbc_portField = new GridBagConstraints();
		gbc_portField.anchor = GridBagConstraints.NORTHWEST;
		gbc_portField.insets = new Insets(10, 10, 10, 10);
		gbc_portField.gridx = 3;
		gbc_portField.gridy = 0;
		add(portField, gbc_portField);
		
		JLabel lblNewLabel_2 = new JLabel("Client Name");		
		GridBagConstraints gbc_lblNewLabel_2 = new GridBagConstraints();
		gbc_lblNewLabel_2.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel_2.insets = new Insets(10, 10, 10, 10);
		gbc_lblNewLabel_2.gridx = 0;
		gbc_lblNewLabel_2.gridy = 1;
		add(lblNewLabel_2, gbc_lblNewLabel_2);
		clientNameField = new JTextField();
		clientNameField.setText("TS-G3-SIM");
		clientNameField.setColumns(15);
		GridBagConstraints gbc_clientNameField = new GridBagConstraints();
		gbc_clientNameField.anchor = GridBagConstraints.NORTHWEST;
		gbc_clientNameField.insets = new Insets(10, 10, 10, 10);
		gbc_clientNameField.gridx = 1;
		gbc_clientNameField.gridy = 1;
		add(clientNameField, gbc_clientNameField);
		
		JLabel lblNewLabel_3 = new JLabel("Client Password");
		GridBagConstraints gbc_lblNewLabel_3 = new GridBagConstraints();
		gbc_lblNewLabel_3.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel_3.insets = new Insets(10, 10, 10, 10);
		gbc_lblNewLabel_3.gridx = 2;
		gbc_lblNewLabel_3.gridy = 1;
		add(lblNewLabel_3, gbc_lblNewLabel_3);
		
		ps_Field = new JTextField();
		ps_Field.setColumns(15);
		GridBagConstraints gbc_ps_Field = new GridBagConstraints();
		gbc_ps_Field.insets = new Insets(10, 10, 10, 10);
		gbc_ps_Field.anchor = GridBagConstraints.NORTHWEST;
		gbc_ps_Field.gridx = 3;
		gbc_ps_Field.gridy = 1;
		add(ps_Field, gbc_ps_Field);
	}
	
	public String getHost() {
		return hostField.getText();
	}
	
	public int getPort() {
		try {
			return Integer.parseInt(portField.getText());
		}
		catch(Exception e) {			
			return -1;
		}		
	}
	
	public String getClientName() {
		return clientNameField.getText();
	}
	
	public String getPassword() {
		return ps_Field.getText();
	}
}
