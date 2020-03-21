/*
 * Copyright (c) [2019] [潘志勇]
 *    [pzy-opensource] is licensed under the Mulan PSL v1.
 *    You can use this software according to the terms and conditions of the Mulan PSL v1.
 *    You may obtain a copy of Mulan PSL v1 at:
 *       http://license.coscl.org.cn/MulanPSL
 *    THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND, EITHER EXPRESS OR
 *    IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT, MERCHANTABILITY OR FIT FOR A PARTICULAR
 *    PURPOSE.
 *    See the Mulan PSL v1 for more details.
 */

package org.pzy.opensource.dbutil.support.util;


import org.pzy.opensource.dbutil.domain.bo.ExecuteResultBO;
import org.pzy.opensource.dbutil.domain.enums.SqlTypeEnum;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 潘志勇
 * @date 2019-01-12
 */
public class SqlExecutorUtil {

    private SqlExecutorUtil() {
    }


    public static ExecuteResultBO executeSql(Connection connection, String sql) {
        ExecuteResultBO executeResult = new ExecuteResultBO();

        executeResult.setSql(sql);

        // 判断sql语句类型
        SqlTypeEnum sqlTypeEnum = sqlType(sql);
        executeResult.setSqlType(sqlTypeEnum);

        Statement statement;
        try {
            statement = connection.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException("通过数据库连接实例获取Statement对象,发生异常!", e);
        }
        long start = System.currentTimeMillis();
        if (sqlTypeEnum == SqlTypeEnum.SELECT) {
            executeSelectSql(sql, executeResult, statement);
        } else {
            executeOtherSql(sql, executeResult, statement);
        }
        long end = System.currentTimeMillis();
        // 计算SQL执行用时
        float useTime = (end - start) / 1000f;

        executeResult.setUseTime(useTime);
        return executeResult;
    }

    /**
     * 执行非查询类型的sql语句
     *
     * @param sql           sql语句
     * @param executeResult 用于存放执行结果的对象
     * @param statement     用于发起sql执行请求的对象
     */
    private static void executeOtherSql(String sql, ExecuteResultBO executeResult, Statement statement) {
        try {
            int updateCount = statement.executeUpdate(sql);
            executeResult.setEffectRows(updateCount);
        } catch (SQLException e) {
            throw new RuntimeException(String.format("执行SQL[%s]时出现异常!", sql), e);
        }
    }

    /**
     * 执行查询语句
     *
     * @param sql           sql语句
     * @param executeResult 用于存放执行结果的对象
     * @param statement     用于发起sql执行请求的对象
     */
    private static void executeSelectSql(String sql, ExecuteResultBO executeResult, Statement statement) {
        List<String> columnNameList = new ArrayList<>();
        ResultSet resultSet = null;
        try {
            resultSet = statement.executeQuery(sql);
        } catch (SQLException e) {
            throw new RuntimeException(String.format("执行SQL语句[%s]发生异常!", sql), e);
        }
        ResultSetMetaData rsmd = null;
        try {
            rsmd = resultSet.getMetaData();
        } catch (SQLException e) {
            throw new RuntimeException("SQL结果集元数据获取异常!", e);
        }
        int columnCount = 0;
        if (null != rsmd) {
            try {
                columnCount = rsmd.getColumnCount();
            } catch (SQLException e) {
                throw new RuntimeException("获取列名时出现异常!", e);
            }
            String columnName;
            for (int i = 0; i < columnCount; i++) {
                try {
                    columnName = rsmd.getColumnName(i + 1);
                    columnNameList.add(columnName);
                } catch (SQLException e) {
                    throw new RuntimeException("获取列名时出现异常!", e);
                }
            }
        }
        executeResult.setColumnNameList(columnNameList);

        List<List<Object>> dataResultList = new ArrayList<>();
        if (columnCount > 0) {
            try {
                while (resultSet.next()) {
                    List<Object> row = new ArrayList<>();
                    for (int i = 1; i <= columnCount; i++) {
                        row.add(resultSet.getObject(i));
                    }
                    dataResultList.add(row);
                }
            } catch (Exception e) {
                throw new RuntimeException("SQL执行结果获取异常!", e);
            }
        }
        executeResult.setDataResultList(dataResultList);
    }

    /**
     * 根据sql语句判断,sql语句的类型
     *
     * @param sql sql语句
     * @return 语句类型
     */
    private static SqlTypeEnum sqlType(String sql) {
        String tmpSql = sql.trim().toLowerCase();
        if (tmpSql.startsWith("select ") || tmpSql.startsWith("explain ")) {
            return SqlTypeEnum.SELECT;
        }
        return SqlTypeEnum.OTHER;
    }
}
