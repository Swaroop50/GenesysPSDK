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

// Copyright (c) 2006 - 2019 Genesys Telecommunications Laboratories, Inc. All rights reserved.
//===============================================================================

/* ATTENTION! DO NOT MODIFY THIS FILE - IT IS AUTOMATICALLY GENERATED! */

package com.genesyslab.platform.applicationblocks.com.objects;

import com.genesyslab.platform.applicationblocks.com.*;
import com.genesyslab.platform.configuration.protocol.obj.*;
import com.genesyslab.platform.configuration.protocol.types.*;
import com.genesyslab.platform.configuration.protocol.metadata.CfgDescriptionStructure;

import com.genesyslab.platform.commons.collections.KeyValueCollection;

import javax.annotation.Generated;

import java.util.Calendar;
import java.util.Collection;

import org.w3c.dom.Node;


/**
 * <p/>
 * This object is obsolete and no longer in use
 */
@SuppressWarnings({"unused", "unchecked"})
@Generated(value="com.genesyslab.platform.applicationblocks.com.generator.COMGenerator",
           date="2024-07-23T09:23:22-07:00")
public class CfgGVPCustomer
            extends CfgObject {

    public static final CfgObjectType OBJECT_TYPE = CfgObjectType.CFGGVPCustomer;

    /**
     * This constructor is intended for creation of objects from configuration protocol messages.
     * It is internally used by COM AB for objects deserialization.
     *
     * @param confService configuration service instance
     * @param objData configuration protocol object data structure
     * @param isSaved indicator of the object saved state
     * @param additionalParameters additional parameters from configuration protocol message
     */
    public CfgGVPCustomer(
            final IConfService confService,
            final ConfObject objData,
            final boolean isSaved,
            final Object[] additionalParameters) {
        super(confService, objData, isSaved, additionalParameters);
        if (objData.getObjectType() != OBJECT_TYPE) {
            throw new IllegalArgumentException(
                    "Incompatible object type for CfgGVPCustomer - "
                    + objData.getObjectType());
        }
    }

    /**
     * This constructor is intended for creation of objects from configuration protocol messages.
     * It is internally used by COM AB for objects deserialization.
     *
     * @param confService configuration service instance
     * @param xmlData XML object containing object data
     * @param additionalParameters additional parameters from configuration protocol message
     */
    public CfgGVPCustomer(
            final IConfService confService,
            final Node xmlData,
            final Object[] additionalParameters) {
        super(confService, xmlData, additionalParameters);
    }

    /**
     * This constructor is intended for creation of detached objects.
     *
     * @param confService configuration service instance
     */
    public CfgGVPCustomer(final IConfService confService) {
        super(confService,
                new ConfObject(confService.getMetaData()
                        .getClassById(OBJECT_TYPE.ordinal())),
                false, null);
    }


    /**
     * Synchronizes changes in the class with Configuration Server.
     *
     * @throws ConfigException in case of protocol level exception, data transformation,
     *                         or server side constraints
     */
    @Override
    @SuppressWarnings("unchecked")
    public void save() throws ConfigException {
        if ((getConfigurationService() != null)
            && getConfigurationService().getPolicy()
                   .getValidateBeforeSave()) {
            if (getMetaData().getAttribute("resellerDBID") != null) {
                if (getLinkValue("resellerDBID") == null) {
                    throw new ConfigException("Mandatory property 'resellerDBID' not set.");
                }
            }
            if (getMetaData().getAttribute("name") != null) {
                if (getProperty("name") == null) {
                    throw new ConfigException("Mandatory property 'name' not set.");
                }
            }
            if (getMetaData().getAttribute("displayName") != null) {
                if (getProperty("displayName") == null) {
                    throw new ConfigException("Mandatory property 'displayName' not set.");
                }
            }
            if (getMetaData().getAttribute("channel") != null) {
                if (getProperty("channel") == null) {
                    throw new ConfigException("Mandatory property 'channel' not set.");
                }
            }
            if (getMetaData().getAttribute("isProvisioned") != null) {
                if (getProperty("isProvisioned") == null) {
                    throw new ConfigException("Mandatory property 'isProvisioned' not set.");
                }
            }
            if (getMetaData().getAttribute("isAdminCustomer") != null) {
                if (getProperty("isAdminCustomer") == null) {
                    throw new ConfigException("Mandatory property 'isAdminCustomer' not set.");
                }
            }
            if (getMetaData().getAttribute("state") != null) {
                if (getProperty("state") == null) {
                    setProperty("state", 1);
                }
            }
        }
        super.save();
    }

    public final Integer getDBID() {
        return (Integer) getProperty("DBID");
    }

    public final void setDBID(final Integer value) {
        if (isSaved()) {
            throw new ConfigRuntimeException(
                "Can't set this property because containing object has been saved.");
        }
        setProperty("DBID", value);
    }

    public final CfgGVPReseller getReseller() {
        return (CfgGVPReseller) getProperty(CfgGVPReseller.class, "resellerDBID");
    }

    public final void setReseller(final CfgGVPReseller value) {
        if (isSaved()) {
            throw new ConfigRuntimeException(
                "Can't set this property because containing object has been saved.");
        }
        setProperty("resellerDBID", value);
    }

    public final void setResellerDBID(final int dbid) {
        if (isSaved()) {
            throw new ConfigRuntimeException(
                "Can't set this property because containing object has been saved.");
        }
        setProperty("resellerDBID", dbid);
    }

    /**
     * Retrieves the dbid of the object that is being linked to by the Reseller property.
     *
     * @return DBID identifier of referred object or null
     */
    public final Integer getResellerDBID() {
        return getLinkValue("resellerDBID");
    }

    public final CfgTenant getTenant() {
        return (CfgTenant) getProperty(CfgTenant.class, "tenantDBID");
    }

    public final String getName() {
        return (String) getProperty("name");
    }

    public final void setName(final String value) {
        setProperty("name", value);
    }

    public final String getDisplayName() {
        return (String) getProperty("displayName");
    }

    public final void setDisplayName(final String value) {
        setProperty("displayName", value);
    }

    public final String getChannel() {
        return (String) getProperty("channel");
    }

    public final void setChannel(final String value) {
        setProperty("channel", value);
    }

    public final String getNotes() {
        return (String) getProperty("notes");
    }

    public final void setNotes(final String value) {
        setProperty("notes", value);
    }

    public final CfgFlag getIsProvisioned() {
        return (CfgFlag) getProperty(CfgFlag.class, "isProvisioned");
    }

    public final void setIsProvisioned(final CfgFlag value) {
        setProperty("isProvisioned", value);
    }

    public final CfgFlag getIsAdminCustomer() {
        return (CfgFlag) getProperty(CfgFlag.class, "isAdminCustomer");
    }

    public final void setIsAdminCustomer(final CfgFlag value) {
        setProperty("isAdminCustomer", value);
    }

    public final CfgTimeZone getTimeZone() {
        return (CfgTimeZone) getProperty(CfgTimeZone.class, "timeZoneDBID");
    }

    public final void setTimeZone(final CfgTimeZone value) {
        setProperty("timeZoneDBID", value);
    }

    public final void setTimeZoneDBID(final int dbid) {
        setProperty("timeZoneDBID", dbid);
    }

    /**
     * Retrieves the dbid of the object that is being linked to by the TimeZone property.
     *
     * @return DBID identifier of referred object or null
     */
    public final Integer getTimeZoneDBID() {
        return getLinkValue("timeZoneDBID");
    }

    public final CfgObjectState getState() {
        return (CfgObjectState) getProperty(CfgObjectState.class, "state");
    }

    public final void setState(final CfgObjectState value) {
        setProperty("state", value);
    }

    public final KeyValueCollection getUserProperties() {
        return (KeyValueCollection) getProperty("userProperties");
    }

    public final void setUserProperties(final KeyValueCollection value) {
        setProperty("userProperties", value);
    }
}
