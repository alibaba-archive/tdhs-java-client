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

package com.taobao.tdhs.jdbc.sqlparser;

import com.taobao.tdhs.jdbc.util.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * function:处理单表的parse
 *
 * @author danchen
 */

public class ParseSQL {
    // log4j日志
    private static Logger logger = Logger.getLogger(ParseSQL.class);

    // 原始的SQL语句
    private String sql;
    // SQL类型 insert 0,update 1,delete 2,普通 select 3
    private SQLType sqlType;
    // 错误信息,多个错误信息,以:分隔
    private String errmsg;
    // 建议
    private String tip;
    // 表名
    private String tablename;
    // 表别名
    private String alias_tablename;
    // where条件根结点
    private TreeNode whereNode;
    // 查询字段
    private String select_column;
    // 查询别名与字段名的对应的列表
    private List<Entry<String, String>> columns;
    // group by字段
    private String groupbycolumn;
    // 排序字段
    private String orderbycolumn;
    // Muti-table sql statement tag
    private int tag;
    // index hint
    private HintStruct hint;
    // 将条件以OperationStruct的形式保存下来
    private List<OperationStruct> listOperationStructs;

    private Map<String, List<OperationStruct>> mapOperationStructs;
    // 排序方式
    private OrderByType sortMethod;
    // insert中column与value的对应
    private List<Entry<String, String>> insertEntries;
    // update中set column与value的对应
    private List<Entry<String, String>> updateEntries;

    // limit的两个参数
    private int limitStart;
    private int limit;

    // 构造函数
    public ParseSQL(@NotNull String sql) {
        sql = sql.trim();
        if (StringUtils.endsWith(sql, ";")) {
            sql = sql.substring(0, sql.length() - 1);
        }
        this.sql = sql;
        this.errmsg = "";
        this.tip = "";
        this.tablename = "";
        this.groupbycolumn = "";
        this.orderbycolumn = "";
        this.limitStart = 0;
        this.limit = 0;
        // 默认情况下都是单表查询0:单表;1:多表
        this.tag = 0;
        this.columns = new ArrayList<Entry<String, String>>();
        listOperationStructs = new LinkedList<OperationStruct>();
        this.updateEntries = new ArrayList<Entry<String, String>>();
    }

    public String getSql() {
        return sql;
    }

    public SQLType getSqlType() {
        return sqlType;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public String getTableName() {
        return tablename;
    }

    public String getAlias_tablename() {
        return alias_tablename;
    }

    public TreeNode getWhereNode() {
        return whereNode;
    }

    public List<OperationStruct> getListOperationStructs() {
        if (listOperationStructs.isEmpty()) {
            getListOperationStructsFromWhereTree(this.whereNode);
        }
        return listOperationStructs;
    }

    public List<Entry<String, String>> getUpdateEntries() {
        return updateEntries;
    }

    public Map<String, List<OperationStruct>> getMapOperationStructs() {
        if (mapOperationStructs == null) {
            mapOperationStructs = new HashMap<String, List<OperationStruct>>();
            for (OperationStruct o : getListOperationStructs()) {
                List<OperationStruct> value;
                String key = o.getColumnName();
                key = StringUtil.escapeField(key);
                if (!mapOperationStructs.containsKey(key)) {
                    value = new ArrayList<OperationStruct>();
                    mapOperationStructs.put(key, value);
                } else {
                    value = mapOperationStructs.get(key);
                }
                value.add(o);
            }
        }
        return mapOperationStructs;
    }

    // 遍历where tree
    private void getListOperationStructsFromWhereTree(TreeNode node) {
        if (node == null) {
            return;
        }
        if (node.node_type == 4) {
            if (node.node_content.equalsIgnoreCase("or")) {
                errmsg = "where is not support or!";
                return;
            }
            getListOperationStructsFromWhereTree(node.left_node);
            getListOperationStructsFromWhereTree(node.right_node);
        } else {
            if (node.node_type == 2) {
                OperationStruct operationStruct = new OperationStruct();
                operationStruct.setOper(node.node_content);
                if (node.left_node != null) {
                    operationStruct.setColumnName(node.left_node.node_content);
                }
                if (node.right_node != null) {
                    operationStruct.setValue(node.right_node.node_content);
                }
                listOperationStructs.add(operationStruct);
            } else {
                errmsg = "where tree has some error.";
            }
        }

    }

    public List<String> getSelect_column() {
        return changeToList(select_column);
    }

    public List<Entry<String, String>> getColumns() {
        return columns;
    }

    public List<String> getGroupbycolumn() {
        return changeToList(groupbycolumn);
    }

    public List<String> getOrderByColumn() {
        return changeToList(orderbycolumn);
    }

    public HintStruct getHint() {
        return hint;
    }

    public OrderByType getSortMethod() {
        return sortMethod;
    }

    public List<Entry<String, String>> getInsertEntries() {
        return insertEntries;
    }

    public int getLimitOffset() {
        return limitStart;
    }

    public int getLimit() {
        return limit;
    }

    private void analyzeSQLHint() {
        int addr = StringUtils.indexOfIgnoreCase(sql, "/*");
        if (addr < 0) {
            return;
        }
        int addr_right = StringUtils.indexOfIgnoreCase(sql, "*/");
        if (addr < 0 || addr_right < addr) {
            errmsg = "hint systax is not right.";
            return;
        }

        String hintString = sql.substring(addr, addr_right + 2);
        this.hint = new HintStruct(hintString);
        this.hint.AnalyzeHint();
        this.sql = sql.substring(0, addr) + sql.substring(addr_right + 2);
    }

    private void analyzeSortMethod() {
        if (StringUtils.indexOfIgnoreCase(sql, "order by") > 0
                && StringUtils.indexOfIgnoreCase(sql, " asc") > 0) {
            this.sortMethod = OrderByType.ASC;
        } else if (StringUtils.indexOfIgnoreCase(sql, "order by") > 0
                && StringUtils.indexOfIgnoreCase(sql, " desc") > 0) {
            this.sortMethod = OrderByType.DESC;
        } else if (StringUtils.indexOfIgnoreCase(sql, "order by") > 0) {
            // 默认升序
            this.sortMethod = OrderByType.ASC;
        } else {
            this.sortMethod = null;
        }
    }

    /*
      * 文通的场景 limit a,b后面是SQL的结束符 limit offset,count
      */
    private void analyzeLimit() {
        int addr = StringUtils.indexOfIgnoreCase(sql, " limit ");
        if (addr < 0) {
            return;
        }
        addr = addr + 7;
        String limitstr = sql.substring(addr).trim();
        String[] array_limit = limitstr.split(",");
        if (array_limit.length == 1) {
            this.limitStart = Integer.valueOf(array_limit[0]);
            if (this.limitStart < 0) {
                this.errmsg = "limitStart should larger than 0";
            }
        } else if (array_limit.length == 2) {
            this.limitStart = Integer.valueOf(array_limit[0]);
            this.limit = Integer.valueOf(array_limit[1]);
            if (this.limitStart < 0) {
                this.errmsg = "limitStart should larger than 0";
                return;
            }
            if (this.limit < 0) {
                this.errmsg = "limitOffset should larger than 0";
                return;
            }
        } else {
            this.errmsg = "wrong limit systax.";
        }
    }

    // 将一个以,分隔的字符串转换成一个list
    public List<String> changeToList(String str) {
        if (StringUtils.isBlank(str)) {
            return null;
        }
        String[] array = str.split(",");
        List<String> list = new LinkedList<String>();
        Collections.addAll(list, array);
        return list;
    }

    /*
      * parameter 在sqlstring中搜索searchStr,看其是否存在,这个searchStr不能存在''中
      */
    private boolean checkSpecialStr(String sqlstring, String searchStr) {

        //即不考虑两个单引号中的searchStr
        Stack<String> stack = new Stack<String>();
        boolean exist_danyinhao = false;
        for (int i = 0; i < sqlstring.length(); i++) {
            //普通字符全部压栈
            if (sqlstring.substring(i, i + 1).equals("'") == false) {
                stack.push(sqlstring.substring(i, i + 1));
            }

            //特殊字符'
            if (sqlstring.substring(i, i + 1).equals("'")) {
                //判断是否使用了转义字符\,往前推,看看有没有连续出现的\,是奇数个,还是偶数个?
                int count = 0;
                int k = i;
                boolean real_danyinhao;
                while (k - 1 >= 0 && sqlstring.substring(k - 1, k).equals("\\") == true) {
                    k--;
                    count++;
                }
                //System.out.println("\\个数为:"+count);
                if (count % 2 == 0) {
                    //这是一个单引号
                    real_danyinhao = true;
                } else {
                    //这不是一个单引号,是value的一部份
                    real_danyinhao = false;
                    stack.push(sqlstring.substring(i, i + 1));
                }
                if (real_danyinhao == true) {
                    if (exist_danyinhao == false) {
                        exist_danyinhao = true;
                        stack.push(sqlstring.substring(i, i + 1));
                    } else {
                        boolean find_real_danyinhao = false;
                        while (find_real_danyinhao == false) {
                            while (!stack.pop().equals("'")) {
                                ;
                            }
                            //这里检查是否是一个真正的单引号,数前面连续\的个数
                            if (stack.isEmpty() == false && stack.peek().equals("\\")) {
                                //这种情况,有可能是真正的单引号
                                count = 0;
                                while (stack.peek().equals("\\")) {
                                    stack.pop();
                                    count++;
                                }
                                if (count % 2 == 0) {
                                    //这是一个真正的引号
                                    find_real_danyinhao = true;
                                } else {
                                    //要继续出栈
                                    find_real_danyinhao = false;
                                }
                            } else {
                                //退出整个大循环
                                find_real_danyinhao = true;
                            }

                        }

                        exist_danyinhao = false;
                    }
                }

            }
        }//end for


        logger.debug(stack.toString());

        if (stack.isEmpty() == false && stack.search(searchStr) > -1) {
            stack.clear();
            return true;
        } else {
            return false;
        }
    }

    // 分析SQL注入
    public boolean analyzeSqlInjection(String sqlstring) {
        //大多数都是安全的SQL
        if (sqlstring.indexOf(";") == -1) {
            return false;
        }

        return checkSpecialStr(sqlstring, ";");

    }


    // 分析一个SQL的类型select,insert,update,delete
    public void sqlDispatch() {
        if (analyzeSqlInjection(sql)) {
            errmsg = "too many \";\"";
            return;
        }
        if (sql.substring(0, 6).equalsIgnoreCase("select")) {
            // 处理select
            analyzeSQLHint();
            analyzeSortMethod();
            analyzeLimit();
            parseSQLSelect();
            sqlType = SQLType.SELECT;
        } else if (sql.substring(0, 6).equalsIgnoreCase("insert")) {
            // 处理insert
            parseSQLInsert();
            sqlType = SQLType.INSERT;
        } else if (sql.substring(0, 6).equalsIgnoreCase("update")) {
            // 处理update
            analyzeSQLHint();
            analyzeSortMethod();
            analyzeLimit();
            parseSQLUpdate();
            sqlType = SQLType.UPDATE;
        } else if (sql.substring(0, 6).equalsIgnoreCase("delete")) {
            // 处理delete
            analyzeSQLHint();
            analyzeSortMethod();
            analyzeLimit();
            parseSQLDelete();
            sqlType = SQLType.DELETE;
        } else
            // 非正常SQL语句
            sqlType = null;
    }

    /*
      * where条件生成的树的根结点,不能是or 如果确实存在这样的情况,需要DBA介入
      */
    private void checkWhereTreeRootNode(TreeNode treeRootNode) {
        if (treeRootNode == null) {
            this.errmsg = "where tree root node is empty.";
            logger.warn(this.errmsg);
            return;
        }

        if (treeRootNode.node_content.equalsIgnoreCase("or")) {
            this.errmsg = "where tree root node appears or key word,this is not allowed.";
            logger.error(this.errmsg);
        }
    }

    private void parseSQLDelete() {
        // delete语句对于SQL auto review,只需要分析where条件字段即可
        logger.debug("SQL at parsing:" + this.sql);
        int i = 0;
        int loop = 0;
        // 第一次移动下标
        if (i + 6 < sql.length()
                && sql.substring(0, 6).equalsIgnoreCase("delete"))
            i = i + 6;
        else {
            this.errmsg = "not delete SQL statement.";
            return;
        }

        // 第二次移动下标,处理空格
        while (i + 1 < sql.length()
                && sql.substring(i, i + 1).equalsIgnoreCase(" "))
            i++;

        // 第三次移动下标,检测from
        if (i + 4 < sql.length()
                && sql.substring(i, i + 4).equalsIgnoreCase("from"))
            i = i + 4;
        else {
            this.errmsg = "not find from key word.";
            return;
        }

        // 第四次移动下标,处理空格
        while (i + 1 < sql.length()
                && sql.substring(i, i + 1).equalsIgnoreCase(" "))
            i++;

        // 第五次移动下标,检测tablename
        while (i + 1 < sql.length()
                && !sql.substring(i, i + 1).equalsIgnoreCase(" ")) {
            tablename = tablename + sql.substring(i, i + 1);
            i++;
        }

        logger.debug("table name:" + this.tablename);

        // 第六次移动下标,处理空格
        while (i + 1 < sql.length()
                && sql.substring(i, i + 1).equalsIgnoreCase(" "))
            i++;

        // 第七次移动下标,检测where字段
        if (i + 5 <= sql.length()
                && sql.substring(i, i + 5).equalsIgnoreCase("where"))
            i = i + 5;
        else {
            this.errmsg = "not find where key word.";
            return;
        }

        // 异常处理
        if (i > sql.length()) {
            this.errmsg = "not find where condition.";
            logger.warn(this.errmsg);
            return;
        } else {
            if (sql.substring(i).trim().length() == 0) {
                this.errmsg = "not find where condition.";
                logger.warn(this.errmsg);
                return;
            }
        }

        int addrOderBy = StringUtils.indexOfIgnoreCase(sql, "order by");
        int addrLimit = StringUtils.indexOfIgnoreCase(sql, " limit ");
        String whereStr;
        if (addrOderBy > 0) {
            whereStr = sql.substring(i, addrOderBy);
        } else if (addrLimit > 0) {
            whereStr = sql.substring(i, addrLimit);
        } else {
            whereStr = sql.substring(i);
        }

        whereNode = parseWhere(null, whereStr, loop);

        // 处理后面的条件字段
        whereNode = parseWhere(null, whereStr.trim(), loop);

        // check whereNode tree
        checkWhereTreeRootNode(whereNode);
        analyzeOrderByStr();
        logger.debug("where condition:" + whereStr.trim());
    }

    // 把order by columns字段找出来
    public void analyzeOrderByStr() {
        logger.debug("enter function AnalyzeOrderByStr");
        String orderbycolumns = "";
        int addr_order_by = StringUtils.indexOfIgnoreCase(sql, "order by");
        if (addr_order_by < 0)
            return;

        if (StringUtils.indexOfIgnoreCase(sql, " limit ", addr_order_by) > addr_order_by) {
            orderbycolumns = sql.substring(addr_order_by + 8, StringUtils
                    .indexOfIgnoreCase(sql, " limit ", addr_order_by));
        } else {
            // no limit key word
            orderbycolumns = sql.substring(addr_order_by + 8).trim();
        }

        orderbycolumns = orderbycolumns.replace(" asc", " ");
        orderbycolumns = orderbycolumns.replace(" desc", " ");
        this.orderbycolumn = orderbycolumns;
        logger.debug("order by columns:" + orderbycolumn);
    }

    private void parseSQLUpdate() {
        // update语句对于SQL auto review,只需要分析出tablename,以及where条件字段即可
        // update语句可能存在与select嵌套的情况
        logger.debug("SQL at parsing:" + this.sql);
        int addr_where = 0;
        int loop = 0;
        tablename = "";
        int i = 0;

        // 第一次移动下标
        if (i + 6 < sql.length()
                && sql.substring(0, 6).equalsIgnoreCase("update"))
            i = i + 6;
        else {
            this.errmsg = "not update SQL statement.";
            return;
        }

        // 第二次移动下标,处理空格
        while (i + 1 < sql.length()
                && sql.substring(i, i + 1).equalsIgnoreCase(" "))
            i++;

        // 第三次移动下标,检测tablename
        while (i + 1 < sql.length()
                && !sql.substring(i, i + 1).equalsIgnoreCase(" ")) {
            tablename = tablename + sql.substring(i, i + 1);
            i++;
        }

        logger.debug("table name:" + this.tablename);
        int addrSet = StringUtils.indexOfIgnoreCase(sql, " set ");
        if (addrSet < 0) {
            this.errmsg = "not find set key word.";
            logger.warn(this.errmsg);
            return;
        }
        // check where key word
        addr_where = StringUtils.indexOfIgnoreCase(sql, " where");
        if (addr_where < 0) {
            this.errmsg = "not find where key word.";
            logger.warn(this.errmsg);
            return;
        }

        analyzeUpdateSetColumns(sql.substring(addrSet + 5, addr_where));

        // 检查一下在values中是否有用sysdate()函数,这个函数会造成主备不一致
        if (StringUtils.indexOfIgnoreCase(sql, "sysdate()", i) > 0
                && StringUtils.indexOfIgnoreCase(sql, "sysdate()", i) < addr_where) {
            errmsg = "use sysdate() function,this not allowed,you should use now() replace it.";
            logger.warn(errmsg);
            return;
        }

        if (addr_where + 6 >= sql.length()) {
            this.errmsg = "not find where condition.";
            logger.warn(this.errmsg);
            return;
        }

        int addrOderBy = StringUtils.indexOfIgnoreCase(sql, "order by");
        int addrLimit = StringUtils.indexOfIgnoreCase(sql, " limit ");
        String whereStr;
        if (addrOderBy > 0) {
            whereStr = sql.substring(addr_where + 6, addrOderBy);
        } else if (addrLimit > 0) {
            whereStr = sql.substring(addr_where + 6, addrLimit);
        } else {
            whereStr = sql.substring(addr_where + 6);
        }

        whereNode = parseWhere(null, whereStr.trim(), loop);

        // check whereNode tree
        checkWhereTreeRootNode(whereNode);

        analyzeOrderByStr();
        logger.debug("where condition:" + whereStr);
    }

    public boolean checkRealEqual(Stack<String> stack) {
        logger.debug("checkRealEqual:" + stack.toString());
        String tmp_str = "";
        @SuppressWarnings("unchecked")
        Stack<String> tmpStack = (Stack<String>) stack.clone();
        while (tmpStack.isEmpty() == false) {
            tmp_str = tmpStack.pop() + tmp_str;
        }

        //如果没有分号',说明是真正的等号,如果不是,则是value值的一部份
        boolean result = !checkSpecialStr(tmp_str, "'");
        logger.debug(result ? "这是真正的=号" : "这不是真正的=号");
        return result;
    }

    /*
      * 分析update set语句中的column,以及value,要注意value中的,
      * ,不能直接作为分隔符
      */
    private void analyzeUpdateSetColumns(String substring) {
        if (substring == null)
            return;

        /*String[] array_setColumn = substring.split(",");
          for (String setColumn : array_setColumn) {
              int addr = StringUtils.indexOfIgnoreCase(setColumn, "=");
              String column = setColumn.substring(0, addr).trim();
              String value = setColumn.substring(addr + 1).trim();
              this.updateEntries.add(new Entry<String, String>(column, value));
          }*/

        //采用Stack来处理
        Stack<String> updateColumnValueStack = new Stack<String>();
        for (int i = 0; i < substring.length(); i++) {
            updateColumnValueStack.push(substring.substring(i, i + 1));
        }

        String column = "";
        String value = "";
        while (updateColumnValueStack.isEmpty() == false) {
            column = "";
            value = "";
            //弹出value String
            while (updateColumnValueStack.peek().equals("=") == false
                    || checkRealEqual(updateColumnValueStack) == false) {
                value = updateColumnValueStack.pop() + value;
            }
            //弹出=
            updateColumnValueStack.pop();
            //弹出column String
            try {
                while (updateColumnValueStack.peek().equals(",") == false) {
                    column = updateColumnValueStack.pop() + column;
                }
            } catch (EmptyStackException e) {
                //保存结果
                this.updateEntries.add(new Entry<String, String>(column, value));
                break;
            }

            //弹出,
            updateColumnValueStack.pop();
            //保存结果
            this.updateEntries.add(new Entry<String, String>(column, value));
        }

    }

    /*
      * 检查查询字段是否符合规则
      */
    private void selectColumnCheckValid(String columnsString) {
        if (columnsString.equalsIgnoreCase("*"))
            this.errmsg = "can't support select * !";
    }

    /*
      * 取得下一个单词,遇到单词后的空格后停止
      */
    public String getNextToken(String str, int from_addr) {
        String token = "";
        // 参数安全检查
        if (str == null || str.length() < from_addr) {
            return null;
        }
        // 空格
        while (from_addr < str.length()
                && str.substring(from_addr, from_addr + 1)
                .equalsIgnoreCase(" ")) {
            from_addr++;
        }
        // 检查退出条件
        if (from_addr > str.length()) {
            return null;
        }
        // token
        while (from_addr < str.length()
                && str.substring(from_addr, from_addr + 1)
                .equalsIgnoreCase(" ") == false) {
            token = token + str.substring(from_addr, from_addr + 1);
            from_addr++;
        }

        return token;
    }

    /*
      * 对各种当前支持的select SQL语句起到一个分发作用
      */
    private void parseSQLSelect() {
        // where word check
        if (StringUtils.indexOfIgnoreCase(sql, " where ") < 0) {
            this.errmsg = "don't have where!";
            return;
        }
        // and word check
        if (getNextToken(sql, StringUtils.indexOfIgnoreCase(sql, " where ") + 7)
                .equalsIgnoreCase("and")) {
            this.errmsg = "and after where,syntax error";
            return;
        }
        // &gt; &lt;这是SQLMAP中常见的一种错误
        if (StringUtils.indexOfIgnoreCase(sql, "&gt;") > 0
                || StringUtils.indexOfIgnoreCase(sql, "&lt;") > 0) {
            this.errmsg = "error in < and > , syntax error";
            return;
        }

        // join关联方式暂不支持
        if (StringUtils.indexOfIgnoreCase(sql, " join ") > 0
                && StringUtils.indexOfIgnoreCase(sql, " on ") > 0) {
            this.errmsg = "join or left join or right join is not supported now.";
            return;
        }

        // 最简单的SQL语句
        if (StringUtils.indexOfIgnoreCase(sql, ".") < 0) {
            parseSQLSelectBase();
            return;
        }

        // 并且存在多个select,现在也不支持
        if (StringUtils.indexOfIgnoreCase(sql, "select ", 7) > 0) {
            this.errmsg = "don't support multi-select.";
            return;
        }

        // 看看是不是单表查询使用了别名
        int is_mutiple_table = checkMutipleTable(sql);
        // 表名使用了db.tablename
        if (is_mutiple_table == 0) {
            parseSQLSelectBase();
            return;
        }
        // 单表使用了别名
        if (is_mutiple_table == 1) {
            parseSQLSelectBase();
            return;
        }
        // 多表关联
        if (is_mutiple_table == 2) {
            this.tag = 1;
            return;
        }

        this.errmsg = "don't support this select";
        return;
    }

    // 单表查询是否使用了别名
    // 主要看from关键字后面
    // from tablename t where
    // 返回值-1有错误
    // 返回值0,单表未使用别名
    // 返回值1,单表使用别名
    // 返回值2,多表
    private int checkMutipleTable(String sql) {
        int addr;
        int length = sql.length();
        int i;
        int start;
        boolean is_find_as = false;
        String alias_name = "";
        addr = StringUtils.indexOfIgnoreCase(sql, " from ");
        if (addr < 0)
            return -1;
        i = addr + 6;
        // space
        while (i + 1 < length && sql.substring(i, i + 1).equalsIgnoreCase(" "))
            i++;
        // table name
        while (i + 1 < length && !sql.substring(i, i + 1).equalsIgnoreCase(" "))
            i++;
        // space
        while (i + 1 < length && sql.substring(i, i + 1).equalsIgnoreCase(" "))
            i++;
        // 检查有没有tablename as t1这种特殊语法
        if (i + 3 < sql.length()
                && sql.substring(i, i + 3).equalsIgnoreCase("as ")) {
            i = i + 3;
            is_find_as = true;
        }
        // token=where?
        start = i;
        while (i + 1 < length && !sql.substring(i, i + 1).equalsIgnoreCase(" ")
                && !sql.substring(i, i + 1).equalsIgnoreCase(","))
            i++;
        alias_name = sql.substring(start, i).trim();
        if (alias_name.equalsIgnoreCase("where")) {
            return 0;
        } else {
            // 判断第一个非空字符是否为,号
            while (i + 1 < length
                    && sql.substring(i, i + 1).equalsIgnoreCase(" "))
                i++;
            if (sql.substring(i, i + 1).equalsIgnoreCase(",")) {
                logger.debug("mutiple tables,this is not support now.");
                return 2;
            }
            // 单表使用了别名,则需要进行替换
            logger.debug("alias name:" + alias_name);
            this.sql = this.sql.replace(" " + alias_name + " ", " ");
            alias_name = alias_name + ".";
            this.sql = this.sql.replace(alias_name, "");
            if (is_find_as == true) {
                this.sql = this.sql.replace(" as ", " ");
            }

            return 1;
        }

    }

    /*
      * select column_name,[column_name] from table_name where 条件 order by
      * column_name limit #endnum 处理最简单的select SQL查询
      */
    private void parseSQLSelectBase() {
        int i = 0, tmp = 0;
        int addr_from;
        int addr_where;
        int addr_group_by;
        int addr_order_by;
        int addr_limit;
        String wherestr = "";
        int loop = 0;

        logger.debug("SQL at parsing:" + sql);

        // 检查select关键字
        if (i + 6 < sql.length()
                && sql.substring(0, 6).equalsIgnoreCase("select")) {
            i = i + 6;
        } else {
            this.errmsg = "not select SQL statement.";
            return;
        }

        // 处理查询字段，并检查合法性
        addr_from = StringUtils.indexOfIgnoreCase(sql, " from ");
        if (addr_from == -1) {
            this.errmsg = "not find from key word.";
            return;
        }
        this.select_column = sql.substring(i, addr_from).trim();
        selectColumnCheckValid(this.select_column);
        // 处理列名的别名映射
        addToColumnHashMap(this.select_column, this.columns);

        logger.debug("select columns:" + this.select_column);

        // 处理table name
        i = addr_from + 6;
        addr_where = StringUtils.indexOfIgnoreCase(sql, " where ", i);
        if (addr_where == -1) {
            this.errmsg = "don't have where!";
            return;
        }

        this.tablename = sql.substring(i, addr_where);

        logger.debug("table name:" + this.tablename);

        // 处理where条件
        i = addr_where + 7;
        addr_group_by = StringUtils.indexOfIgnoreCase(sql, "group by");
        addr_order_by = StringUtils.indexOfIgnoreCase(sql, "order by");
        addr_limit = StringUtils.indexOfIgnoreCase(sql, "limit ");

        if (addr_group_by < 0 && addr_order_by < 0 && addr_limit < 0) {
            wherestr = sql.substring(i);
        } else {
            for (tmp = i; tmp < sql.length() - 8; tmp++) {
                if (!sql.substring(tmp, tmp + 8).equalsIgnoreCase("group by")
                        && !sql.substring(tmp, tmp + 8).equalsIgnoreCase(
                        "order by")
                        && !sql.substring(tmp, tmp + 6).equalsIgnoreCase(
                        "limit "))
                    wherestr = wherestr + sql.substring(tmp, tmp + 1);
                else {
                    break;
                }
            }
        }
        // 处理where string
        int wherestr_len = wherestr.length();
        wherestr = handleBetweenAnd(wherestr);
        this.whereNode = this.parseWhere(null, wherestr, loop);

        // check whereNode tree
        checkWhereTreeRootNode(whereNode);

        logger.debug("where condition:" + wherestr);

        // 继续处理排序,只能加上handleBetweenAnd函数处理之前的wherestr的长度
        i = i + wherestr_len;
        if (i < sql.length()) {
            if (sql.substring(i, i + 8).equalsIgnoreCase("group by")) {
                // 解析后面的排序字段,也包括order by的字段,碰到语句结束
                // 有group by的时候,后面通常有having关键字
                if (StringUtils.indexOfIgnoreCase(sql, "having", i + 8) > 0) {
                    this.groupbycolumn = sql
                            .substring(
                                    i + 8,
                                    StringUtils.indexOfIgnoreCase(sql,
                                            "having", i + 7)).trim();
                } else if (StringUtils
                        .indexOfIgnoreCase(sql, "order by", i + 8) > 0) {
                    this.groupbycolumn = sql.substring(
                            i + 8,
                            StringUtils.indexOfIgnoreCase(sql, "order by",
                                    i + 8)).trim();

                } else if (StringUtils.indexOfIgnoreCase(sql, "limit", i + 8) > 0) {
                    this.groupbycolumn = sql.substring(i + 8,
                            StringUtils.indexOfIgnoreCase(sql, "limit", i + 8))
                            .trim();
                }
            }

            logger.debug("group by columns:" + this.groupbycolumn);

            if (StringUtils.indexOfIgnoreCase(sql, "order by", i) >= i) {
                if (StringUtils.indexOfIgnoreCase(sql, "limit ", i) > StringUtils
                        .indexOfIgnoreCase(sql, "order by", i)) {
                    // 含有limit,解析后面的排序字段,遇到limit终止
                    if (this.orderbycolumn.length() > 0)
                        this.orderbycolumn = this.orderbycolumn
                                + ","
                                + sql.substring(StringUtils.indexOfIgnoreCase(
                                sql, "order by") + 8, StringUtils
                                .indexOfIgnoreCase(sql, "limit"));
                    else {
                        this.orderbycolumn = sql.substring(StringUtils
                                .indexOfIgnoreCase(sql, "order by", i) + 8,
                                StringUtils.indexOfIgnoreCase(sql, "limit "));
                    }
                } else {
                    // 不含有limit,则直接到末尾
                    if (this.orderbycolumn.length() > 0)
                        this.orderbycolumn = this.orderbycolumn
                                + ","
                                + sql.substring(StringUtils.indexOfIgnoreCase(
                                sql, "order by", i) + 8);
                    else {
                        this.orderbycolumn = sql.substring(StringUtils
                                .indexOfIgnoreCase(sql, "order by", i) + 8);
                    }
                }

                this.orderbycolumn = this.orderbycolumn.replace(" asc", " ");
                this.orderbycolumn = this.orderbycolumn.replace(" desc", " ");
            }

            this.orderbycolumn = this.orderbycolumn.replace(" ", "");
            logger.debug("order by columns:" + this.orderbycolumn);
        }
    }

    /*
      * 处理列名的别名映射 column as alias_column,column as alias_column or
      * function(column) as alias_column 示例: SELECT CONCAT(last_name,',
      * ',first_name) AS full_name FROM mytable ORDER BY full_name;
      * 在为select_expr给定别名时，AS关键词是自选的。前面的例子可以这样编写： SELECT CONCAT(last_name,',
      * ',first_name) full_name FROM mytable ORDER BY full_name;
      */
    public static void addToColumnHashMap(String select_exprs,
                                          List<Entry<String, String>> entries) {
        // 参数判断
        if (select_exprs == null) {
            return;
        }
        select_exprs = select_exprs.toLowerCase();
        logger.debug("addToColumnHashMap select_exprs:" + select_exprs);
        // 先处理最简单的情况
        if (StringUtils.indexOfIgnoreCase(select_exprs, "(") < 0) {
            String[] array_columns = select_exprs.split(",");
            for (String array_column : array_columns) {
                dealSingleSelectExpr(array_column, entries);
            }
            return;
        }

        // 使用了函数,处理有括号的情况,括号
        int i = 0;
        int start = 0;
        int addr_douhao = 0;
        int douhao_before_left_kuohao;
        int douhao_before_right_kuohao;
        String select_expr;
        while (i < select_exprs.length()) {
            addr_douhao = StringUtils.indexOfIgnoreCase(select_exprs, ",", i);
            if (addr_douhao < 0) {
                // 最后一组select_expr
                select_expr = select_exprs.substring(start);
                dealSingleSelectExpr(select_expr, entries);
                break;
            }
            // 检查这个逗号是否是正确的逗号,而不是函数里所使用的逗号
            douhao_before_left_kuohao = getWordCountInStr(select_exprs, "(",
                    addr_douhao);
            douhao_before_right_kuohao = getWordCountInStr(select_exprs, ")",
                    addr_douhao);
            if (douhao_before_left_kuohao == douhao_before_right_kuohao) {
                // 这是一个完整的select_expr
                select_expr = select_exprs.substring(start, addr_douhao);
                dealSingleSelectExpr(select_expr, entries);
                start = addr_douhao + 1;
                i = start;
            } else {
                // 这是函数里面的,寻找下一个逗号
                i = addr_douhao + 1;
            }
        }
    }

    /*
      * 统计一个符号出现的次数
      */
    private static int getWordCountInStr(String str, String symbol,
                                         int addr_douhao) {
        int count = 0;
        if (str == null || symbol == null || str.length() <= addr_douhao) {
            return -1;
        }
        for (int i = 0; i < addr_douhao; i++) {
            if (str.substring(i, i + 1).equalsIgnoreCase(symbol)) {
                count++;
            }
        }

        return count;
    }

    /*
      * 处理单个的select_expr column as alias_column or function(column) as
      * alias_column
      */
    private static void dealSingleSelectExpr(String select_expr,
                                             List<Entry<String, String>> entries) {
        String alias_column_name = "";
        String column_name = "";
        String word = "";

        if (select_expr == null || select_expr.trim().equalsIgnoreCase("")) {
            return;
        }

        logger.debug("dealSingleSelectExpr select_expr:" + select_expr);

        int k = select_expr.length();
        // 获得别名
        while (k - 1 >= 0
                && !select_expr.substring(k - 1, k).equalsIgnoreCase(" ")) {
            alias_column_name = select_expr.substring(k - 1, k)
                    + alias_column_name;
            k--;
        }
        if (k == 0) {
            // 列名不含有别名
            column_name = alias_column_name;
            entries.add(new Entry<String, String>(alias_column_name,
                    column_name));
            logger.debug("column_name:" + column_name + " alias_column_name:"
                    + alias_column_name);
            return;
        }
        // 处理空格
        while (k - 1 >= 0
                && select_expr.substring(k - 1, k).equalsIgnoreCase(" ")) {
            k--;
        }
        // 处理as,或者列真名或者函数名
        while (k - 1 >= 0
                && !select_expr.substring(k - 1, k).equalsIgnoreCase(" ")) {
            word = select_expr.substring(k - 1, k) + word;
            k--;
        }

        if (!word.equalsIgnoreCase("as")) {
            column_name = word;
            logger.debug("column_name:" + column_name + " alias_column_name:"
                    + alias_column_name);
            entries.add(new Entry<String, String>(alias_column_name,
                    column_name));
            return;
        }

        // 处理空格
        while (k - 1 >= 0
                && select_expr.substring(k - 1, k).equalsIgnoreCase(" ")) {
            k--;
        }

        // 列真名或者函数名
        column_name = select_expr.substring(0, k);
        logger.debug("column_name:" + column_name + " alias_column_name:"
                + alias_column_name);
        entries.add(new Entry<String, String>(alias_column_name, column_name));
    }

    /*
      * 处理where语句中的between and 语法
      */
    public String handleBetweenAnd(String wherestr) {
        String tmp_wherestr = wherestr;
        String resultString = "";
        String column_name;
        int start = 0;
        String matchString;
        int addr, len;

        if (StringUtils.indexOfIgnoreCase(tmp_wherestr, " between ") < 0) {
            resultString = tmp_wherestr;
        } else {
            // 把between #value# and中间#value#中的空格要去掉
            tmp_wherestr = removeSpace(tmp_wherestr);
            Pattern pattern = Pattern
                    .compile("\\s+[a-zA-Z][0-9_a-zA-Z\\.]+\\s+between\\s+[',:#+\\-0-9_a-zA-Z\\(\\)]+\\sand\\s+");
            Matcher matcher = pattern.matcher(tmp_wherestr);
            while (matcher.find()) {
                matchString = matcher.group();
                len = matchString.length();
                addr = StringUtils.indexOfIgnoreCase(tmp_wherestr, matchString);
                column_name = matchString.trim().substring(0,
                        matchString.trim().indexOf(" "));
                // 把between替换成>=符号
                matchString = matchString.replace(" between ", " >= ");
                // 在and后面的空格处插入<=符号
                matchString = matchString + column_name + " <= ";
                // 构造当前的resultString
                resultString = resultString
                        + tmp_wherestr.substring(start, addr) + matchString;
                // 计算下次开始的start位置
                start = addr + len;
            }// end while

            // 补全后面的SQL
            if (start < tmp_wherestr.length()) {
                resultString = resultString + tmp_wherestr.substring(start);
            }

        }

        return resultString;
    }

    /*
      * 把between #value# and中间#value#中的空格要去掉,以#代替
      */
    public String removeSpace(String tmp_wherestr) {
        String tmpString = "";
        int addr_between = StringUtils.indexOfIgnoreCase(tmp_wherestr,
                " between ");
        int addr_and;
        int start = 0;
        while (addr_between > -1) {
            addr_and = StringUtils.indexOfIgnoreCase(tmp_wherestr, " and ",
                    addr_between);
            tmpString = tmpString
                    + tmp_wherestr.substring(start, addr_between)
                    + " between "
                    + tmp_wherestr.substring(addr_between + 9, addr_and).trim()
                    .replaceAll(" ", "#") + " and ";
            addr_between = StringUtils.indexOfIgnoreCase(tmp_wherestr,
                    " between ", addr_and + 5);
            start = addr_and + 5;
        }
        if (start < tmp_wherestr.length()) {
            tmpString = tmpString + tmp_wherestr.substring(start);
        }
        return tmpString;
    }

    private void parseSQLInsert() {
        // insert SQL
        logger.debug(sql);
        int i = 0;
        int addr_values;
        String columns;
        String values;
        // 检查insert关键字
        if (sql.substring(0, 6).equalsIgnoreCase("insert")) {
            i = i + 6;
        } else {
            errmsg = "it is not a insert SQL";
            return;
        }

        // 接下来的关键字是into
        while (sql.substring(i, i + 1).equalsIgnoreCase(" ")) {
            i++;
        }
        if (!sql.substring(i, i + 4).equalsIgnoreCase("into")) {
            errmsg = "insert sql miss into,syntax error!";
            return;
        } else {
            i = i + 4;
        }

        // 接下来处理表名
        while (sql.substring(i, i + 1).equalsIgnoreCase(" ")) {
            i++;
        }
        while (!sql.substring(i, i + 1).equalsIgnoreCase(" ")
                && !sql.substring(i, i + 1).equalsIgnoreCase("(")) {
            tablename = tablename + sql.substring(i, i + 1);
            i++;
        }
        logger.debug(tablename);
        // (col1,col2)values(#col1#,#col2#)
        addr_values = StringUtils.indexOfIgnoreCase(sql, "values", i);
        if (addr_values < 0) {
            errmsg = "not find values key word.";
            logger.warn(errmsg);
            return;
        }

        // 检查有没有写列名,必须要明确写明列名,不能为空
        int kuohao_left = StringUtils.indexOfIgnoreCase(sql, "(", i);
        int kuohao_right = StringUtils.indexOfIgnoreCase(sql, ")", i);
        if (kuohao_left >= i && kuohao_right > kuohao_left
                && kuohao_right < addr_values) {
            columns = sql.substring(kuohao_left + 1, kuohao_right);
        } else {
            errmsg = "between tablename and values key word,you must write columns clearly.";
            logger.warn(errmsg);
            return;
        }

        // 检查一下在values中是否有用sysdate()函数,这个函数会造成主备不一致
        if (StringUtils.indexOfIgnoreCase(sql, "sysdate()", addr_values) > 0) {
            errmsg = "use sysdate() function,this not allowed,you should use now() replace it.";
            logger.warn(errmsg);
            return;
        }

        kuohao_left = StringUtils.indexOfIgnoreCase(sql, "(", addr_values);
        kuohao_right = StringUtils.lastIndexOfIgnoreCase(sql, ")");
        values = sql.substring(kuohao_left + 1, kuohao_right);
        // 将列名与value对应出来,保存到一个map<String,String>里面
        String[] array_columns = columns.split(",");
        String[] array_values = dealInsertValues(values);
        if (array_columns.length != array_values.length) {
            errmsg = "insert sql columns is not map with values.";
            return;
        }

        List<Entry<String, String>> entries = new ArrayList<Entry<String, String>>(
                array_columns.length);
        for (int j = 0; j < array_columns.length; j++) {
            entries.add(new Entry<String, String>(array_columns[j],
                    array_values[j]));
        }
        this.insertEntries = entries;
    }

    private String[] dealInsertValues(String values) {
        List<String> list_values = new LinkedList<String>();
        int addr_douhao;
        int last_position = 0;
        addr_douhao = values.indexOf(",");
        while (addr_douhao > 0) {
            String tmp_str = values.substring(last_position, addr_douhao);
            logger.debug("dealInsertValues function:" + tmp_str);
            if (checkSpecialStr(tmp_str, "'")) {
                //找到了',说明是value中的,号
                //要继续寻找,直到找到真正的逗号为止
                addr_douhao = values.indexOf(",", addr_douhao + 1);
            } else {
                //没找到单引号,说明是一个真正的逗号
                list_values.add(tmp_str);
                last_position = addr_douhao + 1;
                addr_douhao = values.indexOf(",", last_position);
            }
        }
        //处理最后一个value
        list_values.add(values.substring(last_position));

        //返回
        String[] str_array = new String[list_values.size()];
        for (int i = 0; i < str_array.length; i++) {
            str_array[i] = list_values.get(i);
        }
        return str_array;
    }

    /*
      * 这个函数把基本的操作,例如a=5 build成一棵树 被 parseBase()函数调用
      */
    private TreeNode buildTree(TreeNode rootnode, String str, int addr,
                               int offset) {
        TreeNode node = new TreeNode();
        TreeNode left_child_node = new TreeNode();
        TreeNode right_child_node = new TreeNode();

        // 提取出运算符
        node.node_content = str.substring(addr, addr + offset).trim();
        node.node_type = 2;
        node.parent_node = rootnode;
        node.left_node = left_child_node;
        node.right_node = right_child_node;
        // 左孩子
        left_child_node.node_content = str.substring(0, addr).trim();
        left_child_node.node_type = 1;
        left_child_node.parent_node = node;
        left_child_node.left_node = null;
        left_child_node.right_node = null;
        // 右孩子
        right_child_node.node_content = str.substring(addr + offset).trim();
        right_child_node.node_type = 3;
        right_child_node.parent_node = node;
        right_child_node.left_node = null;
        right_child_node.right_node = null;

        return node;
    }

    /*
      * 处理最基本的运算,例如a=5 或者 a>#abc#
      */
    private TreeNode parseBase(TreeNode rootnode, String str) {
        int addr;

        addr = StringUtils.indexOfIgnoreCase(str, ">=");
        if (addr > 0) {
            return buildTree(rootnode, str, addr, 2);
        }

        addr = StringUtils.indexOfIgnoreCase(str, "<=");
        if (addr > 0) {
            return buildTree(rootnode, str, addr, 2);
        }

        addr = StringUtils.indexOfIgnoreCase(str, ">");
        if (addr > 0) {
            return buildTree(rootnode, str, addr, 1);
        }

        addr = StringUtils.indexOfIgnoreCase(str, "<");
        if (addr > 0) {
            return buildTree(rootnode, str, addr, 1);
        }

        addr = StringUtils.indexOfIgnoreCase(str, "!=");
        if (addr > 0) {
            return buildTree(rootnode, str, addr, 2);
        }

        addr = StringUtils.indexOfIgnoreCase(str, "=");
        if (addr > 0) {
            return buildTree(rootnode, str, addr, 1);
        }

        addr = StringUtils.indexOfIgnoreCase(str, " in ");
        if (addr > 0) {
            // 运算符为in,需要处理括号,这部份代码需要完善
            // 这里可能含有子查询
            return buildTree(rootnode, str, addr, 4);
        }

        addr = StringUtils.indexOfIgnoreCase(str, " like ");
        if (addr > 0) {
            return buildTree(rootnode, str, addr, 6);
        }

        addr = StringUtils.indexOfIgnoreCase(str, " is ");
        if (addr > 0) {
            return buildTree(rootnode, str, addr, 4);
        }

        return null;
    }

    public TreeNode parseWhere(TreeNode rootnode, String str_where, int loop) {
        // 递归深度控制
        loop++;
        if (loop > 10000)
            return null;

        String str = str_where.trim();
        TreeNode node = new TreeNode();
        int addr_and;
        int addr_or;
        // 检查是否有左括号出现,将对括号内的表达式进行递归
        if (str.substring(0, 1).equalsIgnoreCase("(")) {
            // 需找到跟它对称的右括号的位置
            // SQL语句中含有in关键字段,需要处理括号
            Stack<String> stack = new Stack<String>();
            int k = 0;
            String tmp_s;
            while (k < str.length()) {
                tmp_s = str.substring(k, k + 1);
                if (!tmp_s.equalsIgnoreCase(")"))
                    // 将所有压栈
                    stack.push(tmp_s);
                else {
                    // 出栈,直到遇到左括号
                    while (!stack.pop().equalsIgnoreCase("(")) {
                        ;
                    }
                    // 判断栈是否为空,为空,则已找到正确位置
                    if (stack.isEmpty())
                        break;
                }

                k++;
            }// end while

            if (k == str.length() - 1) {
                // 则右侧无表达式
                return parseWhere(rootnode, str.substring(1, k), loop);
            } else {
                // 右侧有表达式,并找到第一个and 或者 or,至少有一个
                if (str.substring(k + 1, k + 6).equalsIgnoreCase(" and ")) {
                    node.node_content = "and";
                    node.node_type = 4;
                    node.left_node = parseWhere(node, str.substring(1, k), loop);
                    node.right_node = parseWhere(node, str.substring(k + 6),
                            loop);
                    node.parent_node = rootnode;
                } else if (str.substring(k + 1, k + 5).equalsIgnoreCase(" or ")) {
                    node.node_content = "or";
                    node.node_type = 4;
                    node.left_node = parseWhere(node, str.substring(1, k), loop);
                    node.right_node = parseWhere(node, str.substring(k + 5),
                            loop);
                    node.parent_node = rootnode;
                }

                return node;

            }
        } else {
            addr_and = StringUtils.indexOfIgnoreCase(str, " and ");
            addr_or = StringUtils.indexOfIgnoreCase(str, " or ");
            if (addr_and > 0 && addr_or > 0)
                if (addr_and < addr_or) {
                    // 最早找到and
                    node.node_content = "and";
                    node.node_type = 4;
                    node.parent_node = rootnode;
                    node.left_node = parseBase(node, str.substring(0, addr_and)
                            .trim());
                    node.right_node = parseWhere(node,
                            str.substring(addr_and + 5), loop);
                    return node;
                } else {
                    // 最早找到or
                    node.node_content = "or";
                    node.node_type = 4;
                    node.parent_node = rootnode;
                    node.left_node = parseBase(node, str.substring(0, addr_or)
                            .trim());
                    node.right_node = parseWhere(node,
                            str.substring(addr_or + 4), loop);
                    return node;
                }
            else if (addr_and > 0) {
                node.node_content = "and";
                node.node_type = 4;
                node.parent_node = rootnode;
                node.left_node = parseBase(node, str.substring(0, addr_and)
                        .trim());
                node.right_node = parseWhere(node, str.substring(addr_and + 5),
                        loop);
                return node;
            } else if (addr_or > 0) {
                node.node_content = "or";
                node.node_type = 4;
                node.parent_node = rootnode;
                node.left_node = parseBase(node, str.substring(0, addr_or)
                        .trim());
                node.right_node = parseWhere(node, str.substring(addr_or + 4),
                        loop);
                return node;
            } else {
                // 处理基本运算
                return parseBase(rootnode, str);
            }
        }
    }

    /*
      * 输出一颗树的信息
      */
    public void printTree(TreeNode rootnode) {
        if (rootnode != null) {
            System.out.println("NODE ID:" + rootnode.hashCode()
                    + ", NODE CONTENT:" + rootnode.node_content);
        }

        if (rootnode.left_node != null) {
            System.out.println("My PARENT NODE CONTENT:"
                    + rootnode.node_content + ", NODE ID:"
                    + rootnode.hashCode() + ", LEFT CHILD ");
            printTree(rootnode.left_node);
        }

        if (rootnode.right_node != null) {
            System.out.println("My PARENT NODE CONTENT:"
                    + rootnode.node_content + ", NODE ID:"
                    + rootnode.hashCode() + ", RIGHT CHILD ");
            printTree(rootnode.right_node);
        }

    }

}
