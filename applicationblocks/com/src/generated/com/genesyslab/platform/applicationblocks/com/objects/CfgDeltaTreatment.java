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
 * <code>CfgDeltaTreatment</code> is applicable for
 * Configuration Library/Server release 5.1.5xx and later.
 */
@SuppressWarnings({"unused", "unchecked"})
@Generated(value="com.genesyslab.platform.applicationblocks.com.generator.COMGenerator",
           date="2024-07-23T09:23:22-07:00")
public class CfgDeltaTreatment extends CfgDelta {

    /**
     * This constructor is intended for creation of detached objects.
     *
     * @param confService configuration service instance
     */
    public CfgDeltaTreatment(final IConfService confService) {
        super(confService, "CfgDeltaTreatment");
    }

    /**
     * This constructor is intended for creation of delta objects from configuration protocol messages.
     * It is internally used by COM AB for delta objects deserialization.
     *
     * @param confService configuration service instance
     * @param objData configuration protocol object delta data
     */
    public CfgDeltaTreatment(
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
    public CfgDeltaTreatment(
            final IConfService confService,
            final Node         xmlData) {
        super(confService, xmlData, null);
    }

    /**
     * The DBID of the target CfgTreatment configuration object.
     *
     * @return target object DBID
     */
    public final Integer getDBID() {
        return (Integer) getProperty("DBID");
    }

    /**
     * Read base CfgTreatment configuration object caused this event.
     *
     * @return configuration object read
     * @throws ConfigException in case of problem while object reading
     */
    public final CfgTreatment retrieveCfgTreatment() throws ConfigException {
        return (CfgTreatment) (super.retrieveObject());
    }

    /**
     * A unique identifier of the <code>
     * <a href="CfgTenant.html">CfgTenant</a>
     * </code>
     * to which this treatment action is allocated. Mandatory.Once specified,
     * cannot be changed.
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
     * A pointer to treatment action name.
     * Mandatory.
     *
     * @return property value or null
     */
    public final String getName() {
        return (String) getProperty("name");
    }

    /**
     * A pointer to treatment action
     * description.
     *
     * @return property value or null
     */
    public final String getDescription() {
        return (String) getProperty("description");
    }

    /**
     * A call result related to this
     * treatment. Refer to <code>
     * <a href="../Enumerations/GctiCallState.html">GctiCallState</a>
     * </code> in Variable Types of
     * <code>Common</code> APIs. Mandatory.
     *
     * @return property value or null
     */
    public final GctiCallState getCallResult() {
        return (GctiCallState) getProperty(GctiCallState.class, "callResult");
    }

    /**
     * A record action code. Refer
     * to <code>
     * <a href="../Enumerations/CfgRecActionCode.html">CfgRecActionCode</a>
     * </code> in User Defined Variable Types.
     *
     * @return property value or null
     */
    public final CfgRecActionCode getRecActionCode() {
        return (CfgRecActionCode) getProperty(CfgRecActionCode.class, "recActionCode");
    }

    /**
     * An attempt number to which the
     * action should be performed.
     *
     * @return property value or null
     */
    public final Integer getAttempts() {
        return (Integer) getProperty("attempts");
    }

    /**
     * A time and date when another attempt
     * must be applied again to dn. The parameter is used if recActionCode
     * is set to <code>CFGRACRetryAtDate</code>. Refer to time_t from time.h
     * of ANSI C library.
     *
     * @return property value or null
     */
    public final Calendar getDateTime() {
        return (Calendar) getProperty("dateTime");
    }

    /**
     * An maximum number of sequential
     * attempts the treatment can be applied to dn. The parameter is used
     * if recActionCode is set to <code>CFGRACCycle</code>.
     *
     * @return property value or null
     */
    public final Integer getCycleAttempt() {
        return (Integer) getProperty("cycleAttempt");
    }

    /**
     * A time interval in minutes between
     * attempts. The parameter is used if
     * <code>recActionCode</code> is set either to <code>CFGRACCycle</code> or
     * <code>CFGRACRetryIn</code>.
     *
     * @return property value or null
     */
    public final Integer getInterval() {
        return (Integer) getProperty("interval");
    }

    /**
     * The time in interval in minutes
     * which increments the interval after each attempt. The parameter
     * is used if <code>recActionCode</code> is set either to <code>CFGRACCycle</code>.
     *
     * @return property value or null
     */
    public final Integer getIncrement() {
        return (Integer) getProperty("increment");
    }

    /**
     * A call action code. Refer to
     * <code>
     * <a href="../Enumerations/CfgCallActionCode.html">CfgCallActionCode</a>
     * </code> in User Defined Variable
     * Types. The <code>callActionCode</code> can be applied to following
     * call results only. (Refer to <code>GctiCallState</code> in Variable Types
     * of <code>Common</code> APIs):
     *
     * @return property value or null
     */
    public final CfgCallActionCode getCallActionCode() {
        return (CfgCallActionCode) getProperty(CfgCallActionCode.class, "callActionCode");
    }

    public final CfgDN getDestDN() {
        return (CfgDN) getProperty(CfgDN.class, "destDNDBID");
    }

    /**
     * Retrieves the dbid of the object that is being linked to by the DestDN property.
     * Configuration server provides it only if the property value has been changed.
     *
     * @return DBID identifier of referred object or null
     */
    public final Integer getDestDNDBID() {
        return getLinkValue("destDNDBID");
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
     * Parameter defining a time range.
     *
     * @return property value or null
     */
    public final Integer getRange() {
        return (Integer) getProperty("range");
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
