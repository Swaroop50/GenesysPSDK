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

package com.genesyslab.platform.samples.apptemplate;

import java.util.Properties;

import com.genesyslab.platform.applicationblocks.com.ConfServiceFactory;
import com.genesyslab.platform.applicationblocks.com.IConfService;
import com.genesyslab.platform.applicationblocks.com.objects.CfgApplication;
import com.genesyslab.platform.applicationblocks.com.queries.CfgApplicationQuery;
import com.genesyslab.platform.commons.protocol.ChannelState;
import com.genesyslab.platform.commons.protocol.Endpoint;
import com.genesyslab.platform.commons.protocol.Protocol;
import com.genesyslab.platform.configuration.protocol.ConfServerProtocol;
import com.genesyslab.platform.configuration.protocol.types.CfgAppType;

/**
 * Helper class to create, release Config Service and read Configuration Objects.
 *
 * See http://docs.genesys.com/Documentation/PSDK/latest/Developer/UsingtheCOMAB
 */
public class ConfigServiceHelper {

	private IConfService service;

	private String host;
	private int port;
	private String userName;
	private String user_ps;
	private String applicationName;
	private CfgAppType applicationType;

	/**
	 * Creates helper to communicate with Configuration Server.
	 * Properties specify Configuration Server address and user credentials. 
	 */
	public ConfigServiceHelper(Properties props) {
		this.host = props.getProperty("config.server.host");
		this.port = Integer.parseInt(props.getProperty("config.server.port"));
		this.userName = props.getProperty("config.server.user");
		this.user_ps = props.getProperty("config.server.password");
		this.applicationName = props.getProperty("application.name");
		this.applicationType = CfgAppType.valueOf(props.getProperty("application.type"));
	}

	/**
	 * Creates helper to communicate with Configuration Server. 
	 * Args represent Configuration Server address and user credentials. 
	 */
	public ConfigServiceHelper(String host, int port, String userName, String userPassword,
			String applicationName, CfgAppType applicationType) {

		this.host = host;
		this.port = port;
		this.userName = userName;
		this.user_ps = userPassword;
		this.applicationName = applicationName;
		this.applicationType = applicationType;

	}

	/**
	 * Creates IConfService object and opens connection to Configuration Server
	 * @throws Exception if IConfService wasn't created or Configuration Protocol wasn't opened.
	 */
	public void openService() throws Exception {
		try {

			ConfServerProtocol protocol = new ConfServerProtocol(new Endpoint(host, port));
			protocol.setUserName(userName);
			protocol.setUserPassword(user_ps);
			protocol.setClientName(applicationName);
			protocol.setClientApplicationType(applicationType.ordinal());

			service = ConfServiceFactory.createConfService(protocol);
			protocol.open();

		} finally {
			if (service != null) {
				Protocol protocol = service.getProtocol();
				if (protocol.getState() != ChannelState.Opened) {
					releaseConfigService();
					service = null;
				}
			}
		}
	}

	/**
	 * Releases IConfService
	 */
	public void releaseConfigService() {
		try {
			if (service != null) {
				Protocol protocol = service.getProtocol();
				if (protocol.getState() != ChannelState.Closed)
					protocol.close();
				ConfServiceFactory.releaseConfService(service);
			}
		} catch (Exception e) {
			System.out.println("Exception occured while releasing Config Service");
			e.printStackTrace();
		}
	}

	/**
	 * Tries to get CfgApplication using CfgApplicationQuery
	 * @param name application name
	 * @return CfgApplication
	 * @throws Exception
	 */
	public CfgApplication getApplication(String name) throws Exception {
		CfgApplicationQuery query = new CfgApplicationQuery(service);
		query.setName(name);
		return query.executeSingleResult();
	}

}
