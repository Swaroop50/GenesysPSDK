// ===============================================================================
//
//  Any authorized distribution of any copy of this code (including any related
//  documentation) must reproduce the following restrictions, disclaimer and copyright
//  notice:
//
//  The Genesys name, trademarks and/or logo(s) of Genesys shall not be used to name
//  (even as a part of another name), endorse and/or promote products derived from
//  this code without prior written permission from Genesys Telecommunications
//  Laboratories, Inc.
//
//  The use, copy, and/or distribution of this code is subject to the terms of the Genesys
//  Developer License Agreement.  This code shall not be used, copied, and/or
//  distributed under any other license agreement.
//
//  THIS CODE IS PROVIDED BY GENESYS TELECOMMUNICATIONS LABORATORIES, INC.
//  ("GENESYS") "AS IS" WITHOUT ANY WARRANTY OF ANY KIND. GENESYS HEREBY
//  DISCLAIMS ALL EXPRESS, IMPLIED, OR STATUTORY CONDITIONS, REPRESENTATIONS AND
//  WARRANTIES WITH RESPECT TO THIS CODE (OR ANY PART THEREOF), INCLUDING, BUT
//  NOT LIMITED TO, IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
//  PARTICULAR PURPOSE OR NON-INFRINGEMENT. GENESYS AND ITS SUPPLIERS SHALL
//  NOT BE LIABLE FOR ANY DAMAGE SUFFERED AS A RESULT OF USING THIS CODE. IN NO
//  EVENT SHALL GENESYS AND ITS SUPPLIERS BE LIABLE FOR ANY DIRECT, INDIRECT,
//  CONSEQUENTIAL, ECONOMIC, INCIDENTAL, OR SPECIAL DAMAGES (INCLUDING, BUT
//  NOT LIMITED TO, ANY LOST REVENUES OR PROFITS).
//
//  Copyright (c) 2007 - 2019 Genesys Telecommunications Laboratories, Inc. All rights reserved.
// ===============================================================================
package com.genesyslab.platform.applicationblocks.commons.protocols;

import com.genesyslab.platform.contacts.protocol.UniversalContactServerProtocol;

import com.genesyslab.platform.commons.connection.configuration.ConnectionConfiguration;
import com.genesyslab.platform.commons.connection.tls.SSLExtendedOptions;

import com.genesyslab.platform.commons.protocol.Endpoint;
import com.genesyslab.platform.commons.protocol.Protocol;

import javax.net.ssl.SSLContext;

import java.net.URI;


class UcsFacility extends ProtocolFacility {

    @Override
    public void applyConfiguration(
            final ProtocolInstance entry,
            final ProtocolConfiguration conf) {
        super.applyConfiguration(entry, conf);

        UcsConfiguration ucsConf = (UcsConfiguration) conf;
        UniversalContactServerProtocol ucsProtocol =
                (UniversalContactServerProtocol) entry.getProtocol();

        if (ucsConf.getClientName() != null) {
            ucsProtocol.setClientName(ucsConf.getClientName());
        }
        if (ucsConf.getClientApplicationType() != null) {
            ucsProtocol.setClientApplicationType(ucsConf.getClientApplicationType());
        }
    }

    @Override
    protected void fillConnectionOptions(
            final ConnectionConfiguration connConf,
            final ProtocolConfiguration protConf) {

        super.fillConnectionOptions(connConf, protConf);

        UcsConfiguration ucsConf = (UcsConfiguration) protConf;

        if (ucsConf.getUseUtfForRequests() != null) {
            connConf.setBoolean(
                    UniversalContactServerProtocol.USE_UTF_FOR_REQUESTS,
                    ucsConf.getUseUtfForRequests());
        }
        if (ucsConf.getUseUtfForResponses() != null) {
            connConf.setBoolean(
                    UniversalContactServerProtocol.USE_UTF_FOR_RESPONSES,
                    ucsConf.getUseUtfForResponses());
        }
    }

    @Override
    public Protocol createProtocol(final String name, final URI uri,
			final boolean tlsEnabled, final SSLContext sslContext, final SSLExtendedOptions sslOptions) {
        return new UniversalContactServerProtocol(
				new Endpoint(name, uri, tlsEnabled, sslContext, sslOptions));
    }
}