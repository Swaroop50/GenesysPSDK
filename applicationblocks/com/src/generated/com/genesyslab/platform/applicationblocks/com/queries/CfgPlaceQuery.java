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


package com.genesyslab.platform.applicationblocks.com.queries;

import com.genesyslab.platform.applicationblocks.com.IConfService;
import com.genesyslab.platform.applicationblocks.com.CfgFilterBasedQuery;
import com.genesyslab.platform.applicationblocks.com.objects.CfgPlace;
import com.genesyslab.platform.applicationblocks.com.AsyncRequestResult;
import com.genesyslab.platform.applicationblocks.com.ConfigException;

import com.genesyslab.platform.applicationblocks.commons.Action;

import com.genesyslab.platform.configuration.protocol.types.*;

import javax.annotation.Generated;

import java.util.Collection;


/**
 * Class designed to construct queries to the Configuration Service for reading CfgPlace object(s).
 *
 * @see com.genesyslab.platform.applicationblocks.com.ConfService
 * @see com.genesyslab.platform.applicationblocks.com.objects.CfgPlace
 */
@SuppressWarnings({"unused", "unchecked"})
@Generated(value="com.genesyslab.platform.applicationblocks.com.generator.COMGenerator",
           date="2024-07-23T09:23:22-07:00")
public class CfgPlaceQuery extends CfgFilterBasedQuery<CfgPlace> {

    /**
     * Create query object with default filter value for configuration objects
     * of CfgPlace type. This query will not be executable.
     */
    public CfgPlaceQuery() {
        super(CfgObjectType.CFGPlace);
        setObjectClass(CfgPlace.class);
    }

    /**
     * Create query object with default filter value for configuration objects
     * of CfgPlace type. If an instance of the configuration service
     * is provided, the query will be executable.
     *
     * @param service The configuration service to use when executing this query
     */
    public CfgPlaceQuery(final IConfService service) {
        super(CfgObjectType.CFGPlace, service);
        setObjectClass(CfgPlace.class);
    }

    /**
     * Create query object with filter initialized for the object DBID.
     * Note: if dbid is specified, Configuration Server ignores other filter keys.
     *
     * @param dbid value for filter on object dbid
     * @see #setDbid(int)
     */
    public CfgPlaceQuery(final int dbid) {
        this();
        setDbid(dbid);
    }

    /**
     * Create query object with filter initialized for object name value.
     *
     * @param name value for filter on object name
     * @see #setName(String)
     */
    public CfgPlaceQuery(final String name) {
        this();
        setName(name);
    }

    /**
     * Executes a query the result of which is a single object. Exception will
     * be thrown if multiple objects are returned by the configuration server.
     *
     * @return the CfgPlace object retrieved as a result of this operation
     */
    public CfgPlace executeSingleResult()
                    throws ConfigException {
        return super.executeSingleResult(CfgPlace.class);
    }

    /**
     * Executes the query and returns a list of CfgPlace objects.
     *
     * @return A collection of CfgPlace objects
     */
    public Collection<CfgPlace> execute()
                    throws ConfigException, InterruptedException {
        return super.execute(CfgPlace.class);
    }

    /**
     * Begins the asynchronous execution of the current query.
     *
     * @param callback The method to be called when query results are available
     * @param state a user-defined object that qualifies or contains information about an asynchronous operation
     * @return AsyncRequestResult describing the current operation
     */
    public AsyncRequestResult<CfgPlace> beginExecute(
                final Action<AsyncRequestResult<CfgPlace>> callback,
                final Object state)
                    throws ConfigException {
        return super.beginExecute(CfgPlace.class, callback, state);
    }

    /**
     * A unique identifier of a place. If
     * specified, Configuration Server will return information only about
     * this place.
     * Note: if dbid is specified, Configuration Server ignores other filter keys
     *
     * @param value filter value on field "dbid".
     */
    public final void setDbid(final int value) {
        setProperty("dbid", value);
    }

    /**
     * A unique identifier of a place. If
     * specified, Configuration Server will return information only about
     * this place.
     * Note: if dbid is specified, Configuration Server ignores other filter keys
     *
     * @return filter value or null (if applicable)
     * @see #setDbid(int)
     */
    public final int getDbid() {
        return (getInt("dbid"));
    }

    /**
     * A unique identifier of a tenant. If specified, Configuration
     * Server will return information only about the places that belong
     * to this tenant.
     *
     * @param value filter value on field "tenant_dbid".
     */
    public final void setTenantDbid(final int value) {
        setProperty("tenant_dbid", value);
    }

    /**
     * A unique identifier of a tenant. If specified, Configuration
     * Server will return information only about the places that belong
     * to this tenant.
     *
     * @return filter value or null (if applicable)
     * @see #setTenantDbid(int)
     */
    public final int getTenantDbid() {
        return (getInt("tenant_dbid"));
    }

    /**
     * Name of a place. Shall be specified
     * as a character string. If specified, Configuration Server will return
     * information only about the place(s) with that name.
     *
     * @param value filter value on field "name".
     */
    public final void setName(final String value) {
        setProperty("name", value);
    }

    /**
     * Name of a place. Shall be specified
     * as a character string. If specified, Configuration Server will return
     * information only about the place(s) with that name.
     *
     * @return filter value or null (if applicable)
     * @see #setName(String)
     */
    public final String getName() {
        return (getString("name"));
    }

    /**
     * Current state of a place (see
     * CfgObjectState
     * ). If specified, Configuration Server
     * will return information only about places that are currently in
     * this state.
     *
     * @param value filter value on field "state".
     */
    public final void setState(final CfgObjectState value) {
        setProperty("state", value);
    }

    /**
     * Current state of a place (see
     * CfgObjectState
     * ). If specified, Configuration Server
     * will return information only about places that are currently in
     * this state.
     *
     * @return filter value or null (if applicable)
     * @see #setState(CfgObjectState)
     */
    public final CfgObjectState getState() {
        return (CfgObjectState) CfgObjectState.getValue(CfgObjectState.class, getInt("state"));
    }

    /**
     * A unique identifier of a place
     * group. If specified, Configuration Server will return information
     * only about the places that form this group.
     *
     * @param value filter value on field "group_dbid".
     */
    public final void setGroupDbid(final int value) {
        setProperty("group_dbid", value);
    }

    /**
     * A unique identifier of a place
     * group. If specified, Configuration Server will return information
     * only about the places that form this group.
     *
     * @return filter value or null (if applicable)
     * @see #setGroupDbid(int)
     */
    public final int getGroupDbid() {
        return (getInt("group_dbid"));
    }

    /**
     * A unique identifier of a DN. If
     * specified, Configuration Server will return information only about
     * the place this DN is assigned to.
     *
     * @param value filter value on field "dn_dbid".
     */
    public final void setDnDbid(final int value) {
        setProperty("dn_dbid", value);
    }

    /**
     * A unique identifier of a DN. If
     * specified, Configuration Server will return information only about
     * the place this DN is assigned to.
     *
     * @return filter value or null (if applicable)
     * @see #setDnDbid(int)
     */
    public final int getDnDbid() {
        return (getInt("dn_dbid"));
    }

    /**
     * Configuration Server will return
     * information only about the place(s) without DNs assigned to.
     *
     * @param value filter value on field "no_dn_dbid".
     */
    public final void setNoDnDbid(final int value) {
        setProperty("no_dn_dbid", value);
    }

    /**
     * Configuration Server will return
     * information only about the place(s) without DNs assigned to.
     *
     * @return filter value or null (if applicable)
     * @see #setNoDnDbid(int)
     */
    public final int getNoDnDbid() {
        return (getInt("no_dn_dbid"));
    }

    /**
     * A unique identifier of a person.
     * If specified, Configuration Server will return information only
     * about place associated with this person.
     *
     * @param value filter value on field "person_dbid".
     */
    public final void setPersonDbid(final int value) {
        setProperty("person_dbid", value);
    }

    /**
     * A unique identifier of a person.
     * If specified, Configuration Server will return information only
     * about place associated with this person.
     *
     * @return filter value or null (if applicable)
     * @see #setPersonDbid(int)
     */
    public final int getPersonDbid() {
        return (getInt("person_dbid"));
    }
}
