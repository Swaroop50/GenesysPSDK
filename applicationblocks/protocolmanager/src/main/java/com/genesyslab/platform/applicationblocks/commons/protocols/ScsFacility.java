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

import com.genesyslab.platform.commons.connection.tls.SSLExtendedOptions;
import com.genesyslab.platform.commons.protocol.Endpoint;
import com.genesyslab.platform.commons.protocol.Protocol;
import com.genesyslab.platform.management.protocol.SolutionControlServerProtocol;

import javax.net.ssl.SSLContext;
import java.net.URI;


final class ScsFacility
        extends ProtocolFacility {

    public void applyConfiguration(
                final ProtocolInstance instance, final ProtocolConfiguration conf) {
        super.applyConfiguration(instance, conf);

        ScsConfiguration scsConf = (ScsConfiguration) conf;
        SolutionControlServerProtocol scsProtocol =
                    (SolutionControlServerProtocol) instance.getProtocol();

        if (scsConf.getClientName() != null) {
            scsProtocol.setClientName(scsConf.getClientName());
        }
        if (scsConf.getClientType() != null) {
            scsProtocol.setClientType(scsConf.getClientType());
        }
        if (scsConf.getClientId() != null) {
            scsProtocol.setClientId(scsConf.getClientId());
        }
        if (scsConf.getUserName() != null) {
            scsProtocol.setUserName(scsConf.getUserName());
        }
    }

    public Protocol createProtocol(final String name, final URI uri,
			final boolean tlsEnabled, final SSLContext sslContext, final SSLExtendedOptions sslOptions) {
        return new SolutionControlServerProtocol(
				new Endpoint(name, uri, tlsEnabled, sslContext, sslOptions));
    }
}