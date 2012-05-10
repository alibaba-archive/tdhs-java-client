/*
 * Copyright(C) 2011-2012 Alibaba Group Holding Limited
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License version 2 as
 *  published by the Free Software Foundation.
 *
 *  Authors:
 *    wentong <wentong@taobao.com>
 */

package com.taobao.tdhs.client.statement;

import com.taobao.tdhs.client.common.TDHSCommon;
import com.taobao.tdhs.client.easy.Query;
import com.taobao.tdhs.client.exception.TDHSException;
import com.taobao.tdhs.client.request.*;
import com.taobao.tdhs.client.response.TDHSResponse;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * TDH_Socket client statement
 * @author <a href="mailto:wentong@taobao.com">文通</a>
 * @since 12-2-21 上午9:42
 */
public interface Statement {
    /**
     * get only one record
     * <p/>
     * like <code>select [fields] from [db].[table] where [field in index] = [key]</code>
     *
     * @param db     of type String      , the db name
     * @param table  of type String      , the table name
     * @param index  of type String      , the index name
     * @param fields of type String[]    , the fields that the record need retuen
     * @param keys   of type String[][]  , the key ,in this case only use the key[0] and FindFlag is <code>TDHSCommon.FindFlag#TDHS_EQ</code>
     *
     * @return TDHSResponse
     *
     * @throws TDHSException when has some error,like timeout , etc    .
     */
    TDHSResponse get(@NotNull String db, @NotNull String table, @Nullable String index, @NotNull String fields[],
                     @NotNull String keys[][])
            throws TDHSException;

    /**
     * get record for more condition
     * <p/>
     * like <code>select [fields] from [db].[table] where [field in index] [findFlag] [key] and [filters] limit [start],[limit]</code>
     *
     * @param db       of type String       , the db name
     * @param table    of type String       , the table name
     * @param index    of type String       , the index name
     * @param fields   of type String[]     , the fields that the record need retuen
     * @param keys     of type String[][]   , the key
     * @param findFlag of type FindFlag     , the condition for key
     * @param start    of type int          , the offset
     * @param limit    of type int          , the limit
     * @param filters  of type Filter[]     , the filter
     *
     * @return TDHSResponse
     *
     * @throws TDHSException when has some error,like timeout , etc    .
     */
    TDHSResponse get(@NotNull String db, @NotNull String table, @Nullable String index, @NotNull String fields[],
                     @NotNull String keys[][], @NotNull TDHSCommon.FindFlag findFlag, int start, int limit,
                     @Nullable Filter filters[])
            throws TDHSException;

    /**
     * get record for more condition
     *
     * @param get of type Get , a complex Get Object ,it contain all condition.
     *
     * @return TDHSResponse
     *
     * @throws TDHSException when has some error,like timeout , etc    .
     */
    TDHSResponse get(@NotNull Get get) throws TDHSException;

    /**
     * get the count of the records with condition
     * <p/>
     * like <code>select count(*) from [db].[table] where [field in index] [findFlag] [key] and [filters] limit [start],[limit]</code>
     *
     * @param db       of type String       , the db name
     * @param table    of type String       , the table name
     * @param index    of type String       , the index name
     * @param keys     of type String[][]   , the key
     * @param findFlag of type FindFlag     , the condition for key
     * @param start    of type int          , the offset
     * @param limit    of type int          , the limit
     * @param filters  of type Filter[]     , the filter
     *
     * @return TDHSResponse
     *
     * @throws TDHSException when has some error,like timeout , etc    .
     */
    TDHSResponse count(@NotNull String db, @NotNull String table, String index, @NotNull String keys[][],
                       @NotNull TDHSCommon.FindFlag findFlag, int start,
                       int limit, @Nullable Filter filters[])
            throws TDHSException;

    /**
     * get the count of the records with condition
     *
     * @param get of type Get , a complex Get Object ,it contain all condition.
     *
     * @return TDHSResponse
     *
     * @throws TDHSException when has some error,like timeout , etc    .
     */
    TDHSResponse count(@NotNull Get get) throws TDHSException;

    /**
     * delete the records with condition
     * <p/>
     * like <code>delete from [db].[table] where [field in index] [findFlag] [key] and [filters] limit [start],[limit]</code>
     *
     * @param db       of type String       , the db name
     * @param table    of type String       , the table name
     * @param index    of type String       , the index name
     * @param keys     of type String[][]   , the key
     * @param findFlag of type FindFlag     , the condition for key
     * @param start    of type int          , the offset
     * @param limit    of type int          , the limit
     * @param filters  of type Filter[]     , the filter
     *
     * @return TDHSResponse
     *
     * @throws TDHSException when has some error,like timeout , etc    .
     */
    TDHSResponse delete(@NotNull String db, @NotNull String table, String index, @NotNull String keys[][],
                        @NotNull TDHSCommon.FindFlag findFlag, int start,
                        int limit, @Nullable Filter filters[])
            throws TDHSException;

    /**
     * delete the records with condition
     *
     * @param get of type Get , a complex Get Object ,it contain all condition.
     *
     * @return TDHSResponse
     *
     * @throws TDHSException when has some error,like timeout , etc    .
     */
    TDHSResponse delete(@NotNull Get get) throws TDHSException;

    /**
     * update the records with condition
     * <p/>
     * like <code>update [db].[table] set [fields]=[valueEntry] where [field in index] [findFlag] [key] and [filters] limit [start],[limit]</code>
     *
     * @param db         of type String       , the db name
     * @param table      of type String       , the table name
     * @param index      of type String       , the index name
     * @param fields     of type String[]     , the fields that the record need be updated
     * @param valueEntry of type ValueEntry[] , the value need be updated
     * @param keys       of type String[][]   , the key
     * @param findFlag   of type FindFlag     , the condition for key
     * @param start      of type int          , the offset
     * @param limit      of type int          , the limit
     * @param filters    of type Filter[]     , the filter
     *
     * @return TDHSResponse
     *
     * @throws TDHSException when has some error,like timeout , etc    .
     */
    TDHSResponse update(@NotNull String db, @NotNull String table, String index, @NotNull String fields[],
                        @NotNull ValueEntry valueEntry[],
                        @NotNull String keys[][],
                        @NotNull TDHSCommon.FindFlag findFlag, int start,
                        int limit, @Nullable Filter filters[])
            throws TDHSException;

    /**
     * update records with condition
     *
     * @param update of type Update , a complex Update Object ,it contain all condition and the value need be updated.
     *
     * @return TDHSResponse
     *
     * @throws TDHSException when has some error,like timeout , etc    .
     */
    TDHSResponse update(@NotNull Update update) throws TDHSException;

    /**
     * insert the records
     * <p/>
     * like <code>insert into [db].[table] ([fields]) values([values])</code>
     *
     * @param db     of type String     ,the db name
     * @param table  of type String     ,the table name
     * @param fields of type String[]   , the fields that the record need inserted
     * @param values of type String[]   ,the value need be insert
     *
     * @return TDHSResponse
     *
     * @throws TDHSException when has some error,like timeout , etc    .
     */
    TDHSResponse insert(@NotNull String db, @NotNull String table, @NotNull String fields[],
                        @NotNull String values[])
            throws TDHSException;

    /**
     * insert the records
     *
     * @param insert of type Insert  , a complex Insert Object ,it contain all condition and the value need be inserted.
     *
     * @return TDHSResponse
     *
     * @throws TDHSException when has some error,like timeout , etc    .
     */
    TDHSResponse insert(@NotNull Insert insert) throws TDHSException;

    /**
     * create a Query for easy query or update
     *
     * @return Query
     */
    Query query();

    /**
     * create a Insert for easy insert.
     *
     * @return Insert
     */
    com.taobao.tdhs.client.easy.Insert insert();
}
