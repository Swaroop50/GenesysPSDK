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

package com.genesyslab.platform.samples.tserver;

import com.genesyslab.platform.samples.tserver.view.AbstractModel;

/**
 * Represent agent state on T-Server
 *
 */
public abstract class AgentModelBase extends AbstractModel {

	private boolean connected;
	private boolean registered;
	private boolean loggedIn;
	private boolean ready;
	private String dn;
	private String queue;
	
	/**
	 * Agent connection status.
	 * 
	 * @return
	 */
	public boolean isConnected() {
		return connected;
	}

	/**
	 * Agent registration status.
	 * 
	 * @return
	 */
	public boolean isRegistered() {
		return registered;
	}
	
	/**
	 * Agent login status.
	 * 
	 * @return
	 */
	public boolean isLoggedIn() {
		return loggedIn;
	}
	
	/**
	 * Agent ready status.
	 * 
	 * @return
	 */
	public boolean isReady() {
		return ready;
	}
	
	/**
	 * DN for which Agent is registered
	 * @return
	 */
	public String getDN() {
		return dn;
	}
	
	/**
	 * Set DN for which Agent is registered.
	 * 
	 * @param registered
	 */
	protected void setDn(String dn) {
		this.dn = dn;		
	}
	
	/**
	 * Queue to which Agent is logged in
	 * @return
	 */
	public String getQueue() {
		return queue;
	}
			
	/**
	 * Set Queue to which Agent is logged in.
	 * 
	 * @param registered
	 */
	protected void setQueue(String queue) {
		this.queue = queue;
	}
	
	/**
	 * Set whether agent is connected to T-Server or not.
	 * 
	 * @param connected
	 */
	protected void setConnected(boolean connected) {
		if (!connected) {
			setReady(false);
			setLoggedIn(false);
			setRegistered(false);
		}
		boolean old = this.connected;
		this.connected = connected;
		firePropertyChange("connected", old, this.connected);
	}

	/**
	 * Set whether agent is registered for a DN or not.
	 * 
	 * @param registered
	 */
	protected void setRegistered(boolean registered) {
		if (!registered) {
			setReady(false);
			setLoggedIn(false);
		}
		boolean old = this.registered;
		this.registered = registered;
		firePropertyChange("registered", old, this.registered);
	}


	/**
	 * Set whether agent is logged in or not.
	 * 
	 * @param registered
	 */
	protected void setLoggedIn(boolean loggedIn) {
		if (!loggedIn) {
			setReady(false);
		}
		boolean old = this.loggedIn;
		this.loggedIn = loggedIn;
		firePropertyChange("loggedIn", old, this.loggedIn);
	}


	/**
	 * Set whether agent is ready to receive calls or not.
	 * 
	 * @param registered
	 */
	protected void setReady(boolean ready) {
		boolean old = this.ready;
		this.ready = ready;
		firePropertyChange("ready", old, this.ready);
	}
	
	
	public String getDescription() {
		return "";
	}
	
}
