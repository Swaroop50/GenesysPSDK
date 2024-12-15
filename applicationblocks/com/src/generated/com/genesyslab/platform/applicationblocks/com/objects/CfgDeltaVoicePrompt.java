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

import com.genesyslab.platform.commons.collections.KeyValueCollection;

import javax.annotation.Generated;

import java.util.Calendar;
import java.util.Collection;

import org.w3c.dom.Node;


/**
 * <p/>
 * The changes to make to a <code>
 * <a href="CfgVoicePrompt.html">CfgVoicePrompt</a>
 * </code> object.
 */
@SuppressWarnings({"unused", "unchecked"})
@Generated(value="com.genesyslab.platform.applicationblocks.com.generator.COMGenerator",
           date="2024-07-23T09:23:22-07:00")
public class CfgDeltaVoicePrompt extends CfgDelta {

    /**
     * This constructor is intended for creation of detached objects.
     *
     * @param confService configuration service instance
     */
    public CfgDeltaVoicePrompt(final IConfService confService) {
        super(confService, "CfgDeltaVoicePrompt");
    }

    /**
     * This constructor is intended for creation of delta objects from configuration protocol messages.
     * It is internally used by COM AB for delta objects deserialization.
     *
     * @param confService configuration service instance
     * @param objData configuration protocol object delta data
     */
    public CfgDeltaVoicePrompt(
            final IConfService confService,
            final ConfObjectDelta objData) {
        super(confService, objData, null);
    }

    /**
     * This constructor is intended for creation of delta objects from configuration protocol messages.
     * It is internally used by COM AB for delta objects deserialization.
     *
     * @param confService configuration service instance
     * @param xmlData XML object containing object delta data
     */
    public CfgDeltaVoicePrompt(
            final IConfService confService,
            final Node         xmlData) {
        super(confService, xmlData, null);
    }

    /**
     * The DBID of the target CfgVoicePrompt configuration object.
     *
     * @return target object DBID
     */
    public final Integer getDBID() {
        return (Integer) getProperty("DBID");
    }

    /**
     * Read base CfgVoicePrompt configuration object caused this event.
     *
     * @return configuration object read
     * @throws ConfigException in case of problem while object reading
     */
    public final CfgVoicePrompt retrieveCfgVoicePrompt() throws ConfigException {
        return (CfgVoicePrompt) (super.retrieveObject());
    }

    /**
     * A unique identifier of the <code>
     * <a href="CfgSwitch.html">CfgSwitch</a>
     * </code>
     * to which this voice prompt belongs. Optional.
     *
     * @return instance of referred object or null
     */
    public final CfgSwitch getSwitch() {
        return (CfgSwitch) getProperty(CfgSwitch.class, "switchDBID");
    }

    /**
     * Retrieves the dbid of the object that is being linked to by the Switch property.
     * Configuration server provides it only if the property value has been changed.
     *
     * @return DBID identifier of referred object or null
     */
    public final Integer getSwitchDBID() {
        return getLinkValue("switchDBID");
    }

    /**
     * A unique identifier of the <code>
     * <a href="CfgTenant.html">CfgTenant</a>
     * </code>
     * to which this voice prompt is allocated. Read only.
     *
     * @return instance of referred object or null
     */
    public final CfgTenant getTenant() {
        return (CfgTenant) getProperty(CfgTenant.class, "tenantDBID");
    }

    /**
     * Retrieves the dbid of the object that is being linked to by the Tenant property.
     * Configuration server provides it only if the property value has been changed.
     *
     * @return DBID identifier of referred object or null
     */
    public final Integer getTenantDBID() {
        return getLinkValue("tenantDBID");
    }

    /**
     * A pointer to the voice prompt name
     * that identifies the voice prompt. Should be equal to the voice prompt
     * identifier provided by switch if used for parked call treating.
     * Mandatory. Once specified cannot be changed. Must be unique within
     * tenant it belongs to.
     *
     * @return property value or null
     */
    public final String getName() {
        return (String) getProperty("name");
    }

    /**
     * A pointer to the voice prompt
     * description. Can be used by parking platform or switch for generating
     * voice prompt (Text-to-Speech).
     *
     * @return property value or null
     */
    public final String getDescription() {
        return (String) getProperty("description");
    }

    /**
     * A unique identifier of the <code>
     * <a href="CfgScript.html">CfgScript</a>
     * </code>
     * for this voice prompt.
     *
     * @return instance of referred object or null
     */
    public final CfgScript getScript() {
        return (CfgScript) getProperty(CfgScript.class, "scriptDBID");
    }

    /**
     * Retrieves the dbid of the object that is being linked to by the Script property.
     * Configuration server provides it only if the property value has been changed.
     *
     * @return DBID identifier of referred object or null
     */
    public final Integer getScriptDBID() {
        return getLinkValue("scriptDBID");
    }

    /**
     * Current object state. Mandatory. Refer to <code>
     * <a href="../Enumerations/CfgObjectState.html">CfgObjectState</a>
     * </code>
     *
     * @return property value or null
     */
    public final CfgObjectState getState() {
        return (CfgObjectState) getProperty(CfgObjectState.class, "state");
    }

    /**
     * A pointer to the list of user-defined properties.Parameter userProperties has the following structure: Each key-value pair of the primary list (TKVList *userProperties) uses the key for the name of a user-defined section, and the value for a secondary list, that also has the TKVList structure and specifies the properties defined within that section.
     *
     * @return property value or null
     */
    public final KeyValueCollection getAddedUserProperties() {
        return (KeyValueCollection) getProperty("userProperties");
    }

    /**
     * A pointer to the list of deleted user-defined properties. Has the same structure as parameter userProperties.
     *
     * @return property value or null
     */
    public final KeyValueCollection getDeletedUserProperties() {
        return (KeyValueCollection) getProperty("deletedUserProperties");
    }

    /**
     * A pointer to the list of user-defined properties whose values have been changed. Has the same structure as parameter userProperties
     *
     * @return property value or null
     */
    public final KeyValueCollection getChangedUserProperties() {
        return (KeyValueCollection) getProperty("changedUserProperties");
    }
}
