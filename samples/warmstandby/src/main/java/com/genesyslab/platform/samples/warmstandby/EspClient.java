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

package com.genesyslab.platform.samples.warmstandby;

import com.genesyslab.platform.commons.collections.KeyValueCollection;
import com.genesyslab.platform.commons.protocol.Endpoint;
import com.genesyslab.platform.commons.protocol.Message;
import com.genesyslab.platform.commons.protocol.ProtocolException;
import com.genesyslab.platform.commons.protocol.RegistrationException;
import com.genesyslab.platform.openmedia.protocol.ExternalServiceProtocol;
import com.genesyslab.platform.openmedia.protocol.externalservice.event.Event3rdServerFault;
import com.genesyslab.platform.openmedia.protocol.externalservice.event.Event3rdServerResponse;
import com.genesyslab.platform.openmedia.protocol.externalservice.request.Request3rdServer;

/**
 * ESP-based client. Implements custom handshake to demonstrate how to handle Registration Exception 
 * in Warm Standby service.
 *
 */
public class EspClient extends ExternalServiceProtocol {

	private String usrName = "";
	private String usr_ps_wrd = "";

	public EspClient() {
		super();
	}

	public EspClient(Endpoint endpoint) {
		super(endpoint);
	}

	public void setUserName(String name) {
		usrName = name;
	}

	public void setPassword(String ps_wrd) {
		usr_ps_wrd = ps_wrd;
	}

	@Override
	protected void onOpen() throws ProtocolException {
		setHandshakePhase(new SimpleHandshake());
		super.onOpen();
	}

	//Handles server registration response. 
	private class SimpleHandshake extends BasicClientHandshakeStep {

		public Message getRegistrationRequest() throws ProtocolException {
			KeyValueCollection message = new KeyValueCollection();
			message.addInt("id", 1);
			message.addString("user", usrName != null ? usrName : "");
			message.addString("password", usr_ps_wrd != null ? usr_ps_wrd : "");

			Request3rdServer request = Request3rdServer.create();
			request.setRequest(message);

			return request;
		}

		public ClientHandshakeStep handleMessage(Message event) throws ProtocolException {
			if (event instanceof Event3rdServerResponse) {
				Event3rdServerResponse resp = (Event3rdServerResponse) event;
				KeyValueCollection data = resp.getRequest();
				if (data != null && data.getInt("id") == 101)
					return null; //registration completed
			}
			if (event instanceof Event3rdServerFault)
				throw new RegistrationException("Wrong credentials!");

			throw new RegistrationException("Unexpected registration response: " + event);
		}
	}
}
