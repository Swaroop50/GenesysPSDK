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
 * <a href="CfgAccessGroup.html">CfgAccessGroup</a>
 * </code> object.
 */
@SuppressWarnings({"unused", "unchecked"})
@Generated(value="com.genesyslab.platform.applicationblocks.com.generator.COMGenerator",
           date="2024-07-23T09:23:22-07:00")
public class CfgDeltaAccessGroup extends CfgDelta {

    /**
     * This constructor is intended for creation of detached objects.
     *
     * @param confService configuration service instance
     */
    public CfgDeltaAccessGroup(final IConfService confService) {
        super(confService, "CfgDeltaAccessGroup");
    }

    /**
     * This constructor is intended for creation of delta objects from configuration protocol messages.
     * It is internally used by COM AB for delta objects deserialization.
     *
     * @param confService configuration service instance
     * @param objData configuration protocol object delta data
     */
    public CfgDeltaAccessGroup(
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
    public CfgDeltaAccessGroup(
            final IConfService confService,
            final Node         xmlData) {
        super(confService, xmlData, null);
    }

    /**
     * The DBID of the target CfgAccessGroup configuration object.
     *
     * @return target object DBID
     */
    public final Integer getDBID() {
        return (Integer) getProperty("DBID");
    }

    /**
     * Read base CfgAccessGroup configuration object caused this event.
     *
     * @return configuration object read
     * @throws ConfigException in case of problem while object reading
     */
    public final CfgAccessGroup retrieveCfgAccessGroup() throws ConfigException {
        return (CfgAccessGroup) (super.retrieveObject());
    }

    /**
     * A pointer to the structure containing information about changes
     * made to this group data. Mandatory.
     *
     * @return property value or null
     */
    public final CfgDeltaGroup getDeltaGroupInfo() {
        return (CfgDeltaGroup) getProperty(CfgDeltaGroup.class, "deltaGroupInfo");
    }

    /**
     * A pointer to the list of Persons
     * (every item of this list is structured as <code>
     * <a href="CfgID.html">CfgID</a>
     * </code>) deleted from
     * this group.
     *
     * @return list of structures or null
     */
    public final Collection<CfgMemberID> getDeletedMemberIDs() {
        return (Collection<CfgMemberID>) getProperty("deletedMemberIDs");
    }

    /**
     * A pointer to the list of the Persons
     * added to this group (every item of this list is structured as <code>
     * <a href="CfgID.html">CfgID</a>
     * </code>).
     *
     * @return list of structures or null
     */
    public final Collection<CfgMemberID> getAddedMemberIDs() {
        return (Collection<CfgMemberID>) getProperty("addedMemberIDs");
    }
}
