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

import com.genesyslab.platform.webmedia.protocol.BasicChatProtocol;
import com.genesyslab.platform.webmedia.protocol.basicchat.UserType;
import com.genesyslab.platform.commons.collections.KeyValueCollection;


/**
 * Protocol Manager Application Block is deprecated.
 *
 * @see com.genesyslab.platform.webmedia.protocol.BasicChatProtocolHandshakeOptions
 * @see com.genesyslab.platform.commons.connection.configuration.ClientConnectionOptions
 * @see com.genesyslab.platform.commons.connection.configuration.ClientADDPOptions
 * @see com.genesyslab.platform.commons.connection.configuration.PropertyConfiguration
 * @see com.genesyslab.platform.commons.connection.configuration.KeyValueConfiguration
 * @see com.genesyslab.platform.commons.protocol.Endpoint
 * @see com.genesyslab.platform.applicationblocks.warmstandby.WarmStandbyConfiguration
 * @see com.genesyslab.platform.apptemplate.configuration.ClientConfigurationHelper
 * @deprecated
 * Use {@link com.genesyslab.platform.commons.protocol.Endpoint Endpoint} to manage protocol's configuration.
 */
@Deprecated
public class BasicChatConfiguration extends ProtocolConfiguration {

    private String             userNickname;
    private String             personId;
    private UserType           userType;
    private KeyValueCollection userData;
    private Boolean            autoRegister;
    private Integer            timeZoneOffset;

    public BasicChatConfiguration(final String name) {
        super(name, BasicChatProtocol.class);
    }


    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(final String name) {
        this.userNickname = name;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(final String personId) {
        this.personId = personId;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(final UserType userType) {
        this.userType = userType;
    }

    public KeyValueCollection getUserData() {
        return userData;
    }

    public void setUserData(final KeyValueCollection userData) {
        this.userData = userData;
    }

    public Boolean isAutoRegister() {
        return autoRegister;
    }

    public void setAutoRegister(final Boolean autoRegister) {
        this.autoRegister = autoRegister;
    }

    public Integer getTimeZoneOffset() {
        return timeZoneOffset;
    }

    public void setTimeZoneOffset(final Integer timeZoneOffset) {
        this.timeZoneOffset = timeZoneOffset;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append("UserNickname: ").append(userNickname).append("\n");
        sb.append("PersonId: ").append(personId).append("\n");
        sb.append("UserType: ").append(userType).append("\n");
        sb.append("UserData: ").append(userData).append("\n");
        sb.append("AutoRegister: ").append(autoRegister).append("\n");
        sb.append("TimeZoneOffset: ").append(timeZoneOffset).append("\n");
        return sb.toString();
    }
}