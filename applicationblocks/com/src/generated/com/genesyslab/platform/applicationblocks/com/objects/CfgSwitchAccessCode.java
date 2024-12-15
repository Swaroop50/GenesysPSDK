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
 * <em>CfgSwitchAccessCode</em> contains a list of Access Codes that are used to
 * place, route, or transfer calls from its Switch to other Switches in a
 * multi-site installation.
 * <p/>
 * Depending on the structure of a numbering plan, you may or may not need access
 * codes to reach DNs that belong to different Switches of a multi-site telephone
 * network.
 * <p/>
 * You can modify (that is, create, change, or delete) the contents of the Access
 * Codes for a particular Switch or for a set of Switches.
 *
 * <p/>
 * Uniqueness of a switch access code is defined by the
 * combination of values of its first three properties, i.e., <code>switchDBID</code>,
 * <code>accessCode</code>, and <code>targetType</code>.
 * Thus, when a certain access code is to be deleted, it is necessary
 * and sufficient to specify those three parameters in the corresponding
 * item of the <code>deletedSwitchAccessCodes</code> list in <code>CfgDeltaSwitch</code>.
 * <p/>
 * Function <code>TRouteCall</code> is a function of the T-Library
 * and is defined in the T-Library SDK C Developer's Guide.
 * <p/>
 * See also <code>
 * <a href="CfgSwitch.html">CfgSwitch</a>
 * </code>.
 * <p/>
 * If <code>targetType=CFGTargetISCC</code> the <code>dnSource</code> property
 * is used for definition of ISCC protocol parameters and presented
 * on GUI (Configuration Manager) with caption _ISCC Protocol Parameters_.
 * <p/>
 * If <code>targetType=CFGTargetISCC</code> the <code>destinationSource</code> property
 * is used for definition of ISCC call overflow parameters and presented
 * on GUI (Configuration Manager) with caption _ISCC Call Overflow
 * Parameters_.
 */
@SuppressWarnings("unused")
@Generated(value="com.genesyslab.platform.applicationblocks.com.generator.COMGenerator",
           date="2024-07-23T09:23:22-07:00")
public class CfgSwitchAccessCode extends CfgStructure {
    /**
     * This constructor is intended for creation of structures from external or imported XML data.
     * It is internally used by COM AB for objects and structures deserialization.
     *
     * @param confService configuration service instance
     * @param objData configuration protocol structure data
     * @param parent instance of configuration object as a parent for this structure instance
     */
    public CfgSwitchAccessCode(
            final IConfService confService,
            final ConfStructure objData,
            final ICfgObject parent) {
        super(confService, objData, parent);
    }

    /**
     * This constructor is intended for creation of structures from external or imported XML data.
     * It is internally used by COM AB for objects and structures deserialization.
     *
     * @param confService configuration service instance
     * @param xmlData XML object containing structure data
     * @param parent instance of configuration object as a parent for this structure instance
     */
    public CfgSwitchAccessCode(
            final IConfService confService,
            final Node xmlData,
            final ICfgObject parent) {
        super(confService, xmlData, parent);
    }

    /**
     * This constructor is intended for creation of detached objects.
     *
     * @param confService configuration service instance
     * @param parent instance of configuration object as a parent for this structure instance
     */
    public CfgSwitchAccessCode(final IConfService confService, final ICfgObject parent) {
        super(confService, new ConfStructure(
                (CfgDescriptionStructure) confService.getMetaData()
                        .getCfgClass("CfgSwitchAccessCode")), parent);
    }


    @SuppressWarnings("unchecked")
    final void checkPropertiesForSave() throws ConfigException {
            if (getMetaData().getAttribute("targetType") != null) {
                if (getProperty("targetType") == null) {
                    throw new ConfigException("Mandatory property 'targetType' not set.");
                }
            }
            if (getMetaData().getAttribute("routeType") != null) {
                if (getProperty("routeType") == null) {
                    throw new ConfigException("Mandatory property 'routeType' not set.");
                }
            }
    }

    /**
     * A unique identifier of the <code>
     * <a href="CfgSwitch.html">Switch</a>
     * </code>
     * to which this access code is assigned. Mandatory. If value is set
     * to 0 the accessCode value is used as default access to this switch
     * if no other access code is specified on source switch to access
     * this switch.
     *
     * @return instance of referred object or null
     */
    public final CfgSwitch getSwitch() {
        return (CfgSwitch) getProperty(CfgSwitch.class, "switchDBID");
    }

    /**
     * A unique identifier of the <code>
     * <a href="CfgSwitch.html">Switch</a>
     * </code>
     * to which this access code is assigned. Mandatory. If value is set
     * to 0 the accessCode value is used as default access to this switch
     * if no other access code is specified on source switch to access
     * this switch.
     *
     * @param value new property value
     * @see #getSwitch()
     */
    public final void setSwitch(final CfgSwitch value) {
        setProperty("switchDBID", value);
    }

    /**
     * A unique identifier of the <code>
     * <a href="CfgSwitch.html">Switch</a>
     * </code>
     * to which this access code is assigned. Mandatory. If value is set
     * to 0 the accessCode value is used as default access to this switch
     * if no other access code is specified on source switch to access
     * this switch.
     *
     * @param dbid DBID identifier of referred object
     * @see #getSwitch()
     */
    public final void setSwitchDBID(final int dbid) {
        setProperty("switchDBID", dbid);
    }

    /**
     * Retrieves the dbid of the object that is being linked to by the Switch property.
     *
     * @return DBID identifier of referred object or null
     */
    public final Integer getSwitchDBID() {
        return getLinkValue("switchDBID");
    }

    /**
     * A pointer to the access code.
     *
     * @return property value or null
     */
    public final String getAccessCode() {
        return (String) getProperty("accessCode");
    }

    /**
     * A pointer to the access code.
     *
     * @param value new property value
     * @see #getAccessCode()
     */
    public final void setAccessCode(final String value) {
        setProperty("accessCode", value);
    }

    /**
     * Type of the target within the
     * switch specified by <code>switchDBID</code> for which all the routing parameters
     * below are specified. See <code>
     * <a href="../Enumerations/CfgTargetType.html">CfgTargetType</a>
     * </code>.
     *
     * @return property value or null
     */
    public final CfgTargetType getTargetType() {
        return (CfgTargetType) getProperty(CfgTargetType.class, "targetType");
    }

    /**
     * Type of the target within the
     * switch specified by <code>switchDBID</code> for which all the routing parameters
     * below are specified. See <code>
     * <a href="../Enumerations/CfgTargetType.html">CfgTargetType</a>
     * </code>.
     *
     * @param value new property value
     * @see #getTargetType()
     */
    public final void setTargetType(final CfgTargetType value) {
        setProperty("targetType", value);
    }

    /**
     * Type of routing for the target
     * specified in <code>targetType</code> for this switch.
     * See <code>
     * <a href="../Enumerations/CfgRouteType.html">CfgRouteType</a>
     * </code>.
     *
     * @return property value or null
     */
    public final CfgRouteType getRouteType() {
        return (CfgRouteType) getProperty(CfgRouteType.class, "routeType");
    }

    /**
     * Type of routing for the target
     * specified in <code>targetType</code> for this switch.
     * See <code>
     * <a href="../Enumerations/CfgRouteType.html">CfgRouteType</a>
     * </code>.
     *
     * @param value new property value
     * @see #getRouteType()
     */
    public final void setRouteType(final CfgRouteType value) {
        setProperty("routeType", value);
    }

    /**
     * Source of information to specify
     * parameter <code>dn</code> in function <code>TRouteCall.</code>
     * See comments.
     *
     * @return property value or null
     */
    public final String getDnSource() {
        return (String) getProperty("dnSource");
    }

    /**
     * Source of information to specify
     * parameter <code>dn</code> in function <code>TRouteCall.</code>
     * See comments.
     *
     * @param value new property value
     * @see #getDnSource()
     */
    public final void setDnSource(final String value) {
        setProperty("dnSource", value);
    }

    /**
     * Source of information to specify parameter
     * <code>destination</code> in function <code>TRouteCall.</code>
     * See comments.
     *
     * @return property value or null
     */
    public final String getDestinationSource() {
        return (String) getProperty("destinationSource");
    }

    /**
     * Source of information to specify parameter
     * <code>destination</code> in function <code>TRouteCall.</code>
     * See comments.
     *
     * @param value new property value
     * @see #getDestinationSource()
     */
    public final void setDestinationSource(final String value) {
        setProperty("destinationSource", value);
    }

    /**
     * Source of information to specify parameter
     * <code>location</code> in function <code>TRouteCall</code>.
     *
     * @return property value or null
     */
    public final String getLocationSource() {
        return (String) getProperty("locationSource");
    }

    /**
     * Source of information to specify parameter
     * <code>location</code> in function <code>TRouteCall</code>.
     *
     * @param value new property value
     * @see #getLocationSource()
     */
    public final void setLocationSource(final String value) {
        setProperty("locationSource", value);
    }

    /**
     * Source of information to specify
     * parameter <code>dnis</code> in function <code>TRouteCall</code>.
     *
     * @return property value or null
     */
    public final String getDnisSource() {
        return (String) getProperty("dnisSource");
    }

    /**
     * Source of information to specify
     * parameter <code>dnis</code> in function <code>TRouteCall</code>.
     *
     * @param value new property value
     * @see #getDnisSource()
     */
    public final void setDnisSource(final String value) {
        setProperty("dnisSource", value);
    }

    /**
     * Source of information to specify
     * parameter <code>reasons</code> in function <code>TRouteCall</code>.
     *
     * @return property value or null
     */
    public final String getReasonSource() {
        return (String) getProperty("reasonSource");
    }

    /**
     * Source of information to specify
     * parameter <code>reasons</code> in function <code>TRouteCall</code>.
     *
     * @param value new property value
     * @see #getReasonSource()
     */
    public final void setReasonSource(final String value) {
        setProperty("reasonSource", value);
    }

    /**
     * Source of information to specify parameter
     * <code>extensions</code> in function <code>TRouteCall</code>.
     *
     * @return property value or null
     */
    public final String getExtensionSource() {
        return (String) getProperty("extensionSource");
    }

    /**
     * Source of information to specify parameter
     * <code>extensions</code> in function <code>TRouteCall</code>.
     *
     * @param value new property value
     * @see #getExtensionSource()
     */
    public final void setExtensionSource(final String value) {
        setProperty("extensionSource", value);
    }
}
