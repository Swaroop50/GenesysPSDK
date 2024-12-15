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

import javax.swing.JDialog;

import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;
import javax.swing.JPanel;
import javax.swing.JButton;

import java.awt.Font;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;

import javax.swing.border.TitledBorder;
import javax.swing.SwingConstants;

public class DialogAddpSetting extends JDialog {

	private static final long serialVersionUID = 1L;
	private JRadioButton traceLocalButton;
	private JRadioButton traceBothButton;
	private JRadioButton traceRemoteButton;
	private JRadioButton traceNoneButton;
	private JButton btnSave;

	private JTextField clientTimeoutField;
	private JTextField serverTimeoutField;
	private ButtonGroup btngroup = new ButtonGroup();
	private JPanel timepanel;
	private JPanel panel_4;
	private JPanel panel_5;
	private JPanel panel;;

	public DialogAddpSetting() {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setResizable(false);
		setPreferredSize(new Dimension(340, 200));

		setFont(new Font("Tahoma", Font.PLAIN, 11));

		setTitle("ADDP settings");
		setModalityType(ModalityType.DOCUMENT_MODAL);
		getContentPane().setLayout(null);

		timepanel = new JPanel();
		timepanel.setBounds(0, 0, 331, 76);
		getContentPane().add(timepanel);

		JLabel lblNewLabel = new JLabel("Client Timeout");
		lblNewLabel.setBounds(15, 14, 68, 14);
		JLabel lblNewLabel_1 = new JLabel("Server Timeout");
		lblNewLabel_1.setBounds(15, 39, 73, 14);

		clientTimeoutField = new JTextField();
		clientTimeoutField.setBounds(162, 11, 86, 20);
		clientTimeoutField.setColumns(10);

		serverTimeoutField = new JTextField();
		serverTimeoutField.setBounds(162, 36, 86, 20);
		serverTimeoutField.setColumns(10);
		timepanel.setLayout(null);
		timepanel.add(lblNewLabel);
		timepanel.add(lblNewLabel_1);
		timepanel.add(clientTimeoutField);
		timepanel.add(serverTimeoutField);

		panel_4 = new JPanel();
		panel_4.setBounds(0, 76, 331, 97);
		getContentPane().add(panel_4);
		panel_4.setLayout(new BorderLayout(0, 0));

		panel_5 = new JPanel();
		panel_5.setBorder(new TitledBorder(null, "Trace", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_4.add(panel_5, BorderLayout.CENTER);

		traceLocalButton = new JRadioButton("Local");
		panel_5.add(traceLocalButton);

		btngroup.add(traceLocalButton);

		traceRemoteButton = new JRadioButton("Remote");
		panel_5.add(traceRemoteButton);
		btngroup.add(traceRemoteButton);

		traceBothButton = new JRadioButton("Both");
		panel_5.add(traceBothButton);
		btngroup.add(traceBothButton);

		traceNoneButton = new JRadioButton("None");
		panel_5.add(traceNoneButton);
		btngroup.add(traceNoneButton);

		panel = new JPanel();
		panel_4.add(panel, BorderLayout.SOUTH);

		btnSave = new JButton("Save");
		btnSave.setHorizontalAlignment(SwingConstants.LEFT);
		panel.add(btnSave);

	}

	public boolean getTraceLocalButton() {
		return traceLocalButton.isSelected();
	}

	public void setTraceLocalButton(boolean traceLocalButton) {
		this.traceLocalButton.setSelected(traceLocalButton);
	}

	public boolean getTraceBothButton() {
		return traceBothButton.isSelected();
	}

	public void setTraceBothButton(boolean traceBothButton) {
		this.traceBothButton.setSelected(traceBothButton);
	}

	public boolean getTraceRemoteButton() {
		return traceRemoteButton.isSelected();
	}

	public void setTraceRemoteButton(boolean traceRemoteButton) {
		this.traceRemoteButton.setSelected(traceRemoteButton);
	}

	public boolean getTraceNoneButton() {
		return traceNoneButton.isSelected();
	}

	public void setTraceNoneButton(boolean traceNoneButton) {
		this.traceNoneButton.setSelected(traceNoneButton);
	}

	public String getClientTimeoutField() {
		return clientTimeoutField.getText();
	}

	public void setClientTimeoutField(String clientTimeoutField) {
		this.clientTimeoutField.setText(clientTimeoutField);
	}

	public String getServerTimeoutField() {
		return serverTimeoutField.getText();
	}

	public void setServerTimeoutField(String serverTimeoutField) {
		this.serverTimeoutField.setText(serverTimeoutField);
	}

	public void addSaveListener(ActionListener saveListener) {
		btnSave.addActionListener(saveListener);
	}

	public void removeSaveListener(ActionListener saveListener) {
		btnSave.removeActionListener(saveListener);
	}
}
