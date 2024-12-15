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

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import com.genesyslab.platform.commons.connection.configuration.ClientADDPOptions.AddpTraceMode;
import com.genesyslab.platform.commons.connection.configuration.ClientConnectionOptions;
import com.genesyslab.platform.samples.tserver.AgentModel;

public class AddpMenuItemAction implements ActionListener{
	
	private AgentModel model;	
	
	public AddpMenuItemAction(AgentModel model) {
		this.model = model;
	}
	
	//Configure ADDP at a runtime
	//See http://docs.genesys.com/Documentation/PSDK/8.5.x/Developer/ConnectingtoaServer#Configuring_ADDP
	public void actionPerformed(ActionEvent e) {
		DialogAddpSetting dialog = new DialogAddpSetting();	
		
		ClientConnectionOptions config = model.getConnectionConfiguration();
		
		Integer value = config.getAddpClientTimeout();			
		dialog.setClientTimeoutField(value != null ?  Integer.toString(value) : "");
		
		value = config.getAddpServerTimeout();
		dialog.setServerTimeoutField(value != null ?  Integer.toString(value) : "");	
				
		AddpTraceMode traceMode = config.getAddpTraceMode();		
		if(traceMode==null)
			dialog.setTraceNoneButton(true);
		else if(traceMode.equals(AddpTraceMode.Local))
			dialog.setTraceLocalButton(true);
		else if(traceMode.equals(AddpTraceMode.Remote))
			dialog.setTraceRemoteButton(true);
		else if(traceMode.equals(AddpTraceMode.Both))
			dialog.setTraceBothButton(true);
		else 
			dialog.setTraceNoneButton(true);
			
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (screenSize.width - dialog.getWidth()) / 2;
		int y = (screenSize.height - dialog.getHeight()) / 2;
		dialog.setLocation(x, y);
        dialog.pack();
        
        SaveAction action = new SaveAction(dialog);
        dialog.addSaveListener(action);
        dialog.setModal(true);
        dialog.setVisible(true); 
        dialog.removeSaveListener(action);
        dialog.dispose();
	}
	
	private class SaveAction extends AbstractAction {
		
		private static final long serialVersionUID = 744595811641797016L;
		DialogAddpSetting dialog;
		
		public SaveAction(DialogAddpSetting dialog) {
			this.dialog = dialog;
		}

		public void actionPerformed(ActionEvent e) {			
			AddpTraceMode mode;
			if(dialog.getTraceLocalButton()) {
				mode = AddpTraceMode.Local;
			}
			else if(dialog.getTraceRemoteButton()) {
				mode = AddpTraceMode.Remote;
			} 
			else if(dialog.getTraceBothButton()) {
				mode = AddpTraceMode.Both;
			}
			else {
				mode =  AddpTraceMode.None;
			}	
			Integer clientTimout = null;
			Integer serverTimeout = null;
			
			try {				
				clientTimout = Integer.parseInt(dialog.getClientTimeoutField());
				serverTimeout = Integer.parseInt(dialog.getServerTimeoutField());				
			}
			catch(NumberFormatException ex) {
				JOptionPane.showMessageDialog(null, "Specify correct timeout.", "Wrong timeout format.",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			ClientConnectionOptions config = model.getConnectionConfiguration();
			config.setAddpClientTimeout(clientTimout);
			config.setAddpServerTimeout(serverTimeout);
			config.setAddpTraceMode(mode);
			
			dialog.setVisible(false);
		}		
		
	}
}
