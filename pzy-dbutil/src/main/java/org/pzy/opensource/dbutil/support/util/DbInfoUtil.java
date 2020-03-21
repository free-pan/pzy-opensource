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


import org.pzy.opensource.dbutil.domain.bo.ColumnInfoBO;
import org.pzy.opensource.dbutil.domain.bo.DbInfoBO;
import org.pzy.opensource.dbutil.domain.bo.TableInfoBO;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 用于获取数据库信息(表信息,字段信息)
 *
 * @author pzy
 * @date 2018/11/4
 */
public class DbInfoUtil {

    private DbInfoUtil() {
    }

    /**
     * 将数据库的字段数据类型转换为java对应的数据类型
     *
     * @param dbDataType 数据库中的字段数据类型
     * @return java中的类型
     */
    public static String dbDataType2JavaType(String dbDataType) {
        String tmpDbDataType = dbDataType.toUpperCase();
        String javaType;

        switch (tmpDbDataType) {
            case "CHAR":
                javaType = "String";
                break;
            case "VARCHAR":
                javaType = "String";
                break;
            case "VARCHAR2":
                javaType = "String";
                break;
            case "BLOB":
                javaType = "byte[]";
                break;
            case "CLOB":
                javaType = "byte[]";
                break;
            case "TEXT":
                javaType = "String";
                break;
            case "INTEGER":
                javaType = "Integer";
                break;
            case "INT":
                javaType = "Integer";
                break;
            case "INT UNSIGNED":
                javaType = "Integer";
                break;
            case "SMALLINT":
                javaType = "Short";
                break;
            case "SMALLINT UNSIGNED":
                javaType = "Short";
                break;
            case "TINYINT":
                javaType = "Short";
                break;
            case "TINYINT UNSIGNED":
                javaType = "Short";
                break;
            case "MEDIUMINT":
                javaType = "Integer";
                break;
            case "MEDIUMINT UNSIGNED":
                javaType = "Integer";
                break;
            case "BIT":
                javaType = "Boolean";
                break;
            case "BIGINT":
                javaType = "Long";
                break;
            case "BIGINT UNSIGNED":
                javaType = "Long";
                break;
            case "FLOAT":
                javaType = "Float";
                break;
            case "DOUBLE":
                javaType = "Double";
                break;
            case "DECIMAL":
                javaType = "java.math.BigDecimal";
                break;
            case "DATE":
                javaType = "java.time.LocalDate";
                break;
            case "TIME":
                javaType = "java.time.LocalTime";
                break;
            case "DATETIME":
                javaType = "java.time.LocalDateTime";
                break;
            case "TIMESTAMP":
                javaType = "java.time.LocalDateTime";
                break;
            case "LONGTEXT":
                javaType = "String";
                break;
            default:
                throw new RuntimeException(String.format("亲,你要转换的数据库列类型[%s]不存在!", tmpDbDataType));
        }
        return javaType;
    }

    /**
     * 获取数据库信息(数据库名,所有的表以及列信息)
     *
     * @param conn 数据库连接
     * @return key为表名, value为表详细信息
     */
    public static final DbInfoBO loadDbInfo(Connection conn) {

        DbInfoBO dbInfoBO = new DbInfoBO();

        // 获取数据库名
        String catalog = null;
        try {
            catalog = conn.getCatalog();
        } catch (SQLException e) {
            throw new RuntimeException("数据库库名获取异常!", e);
        }
        dbInfoBO.setCatalog(catalog);

        DatabaseMetaData dbMetaData = null;
        try {
            dbMetaData = conn.getMetaData();
        } catch (SQLException e) {
            throw new RuntimeException("数据库元数据获取异常!", e);
        }

        // 获取所有的表信息
        extractTableInfo(dbInfoBO, catalog, dbMetaData);
        return dbInfoBO;
    }

    /**
     * 提取所有的表信息
     *
     * @param dbInfoBO   用于存放提取出来的表信息
     * @param catalog    数据库名
     * @param dbMetaData 数据库元信息
     * @throws SQLException
     */
    private static void extractTableInfo(DbInfoBO dbInfoBO, String catalog, DatabaseMetaData dbMetaData) {
        ResultSet tablesResultSet = null;
        try {
            tablesResultSet = dbMetaData.getTables(catalog, null, null, new String[]{"TABLE"});
            while (tablesResultSet.next()) {
                // 获取表名
                String tableName = tablesResultSet.getString("TABLE_NAME");
                // 表类型(TABLE,VIEW,SYSTEM TABLE,GLOBAL TEMPORARY,LOCAL TEMPORARY,ALIAS,SYNONYM)
                String tableType = tablesResultSet.getString("TABLE_TYPE");
                // 表注释
                String tableComment = tablesResultSet.getString("REMARKS");
                TableInfoBO tableInfoBO = new TableInfoBO();
                tableInfoBO.setName(tableName);
                tableInfoBO.setType(tableType);
                tableInfoBO.setComment(tableComment);
                List<TableInfoBO> tableInfoList = dbInfoBO.getTableInfoList();
                if (null == tableInfoList) {
                    tableInfoList = new ArrayList<>();
                    dbInfoBO.setTableInfoList(tableInfoList);
                }
                tableInfoList.add(tableInfoBO);
                // 获取当前表的列信息
                extractColumnInfo(dbMetaData, tableName, tableInfoBO);
            }
        } catch (Exception e) {
            throw new RuntimeException("表信息获取异常:" + e.getMessage(), e);
        } finally {
            if (null != tablesResultSet) {
                try {
                    tablesResultSet.close();
                } catch (SQLException e) {
                    throw new RuntimeException("表信息获取异常:" + e.getMessage(), e);
                }
            }
        }
    }

    /**
     * 提取表的字段信息
     *
     * @param dbMetaData  数据库元信息
     * @param tableName   表名
     * @param tableInfoBO 用于存放提取出来的列信息
     * @throws SQLException
     */
    private static void extractColumnInfo(DatabaseMetaData dbMetaData, String tableName, TableInfoBO tableInfoBO) throws SQLException {
        ResultSet columnsResultSet = null;
        try {
            columnsResultSet = dbMetaData.getColumns(null, "%", tableName, "%");
            while (columnsResultSet.next()) {
                // 获取字段名
                String columnName = columnsResultSet.getString("COLUMN_NAME");
                // 获取字段的数据库类型
                String columnType = columnsResultSet.getString("TYPE_NAME");
                // 获取字段描述
                String columnComment = columnsResultSet.getString("REMARKS");
                // 获取列大小
                Integer columnSize = columnsResultSet.getInt("COLUMN_SIZE");
                // 是否允许为空
                Integer nullAble = columnsResultSet.getInt("NULLABLE");
                // 获取列默认值
                String defaultVal = columnsResultSet.getString("COLUMN_DEF");
                ColumnInfoBO columnInfoBO = new ColumnInfoBO();
                columnInfoBO.setNullAble(nullAble != 0);
                columnInfoBO.setComment(columnComment);
                columnInfoBO.setDbDataType(columnType);
                columnInfoBO.setName(columnName);
                columnInfoBO.setColumnSize(columnSize);
                columnInfoBO.setDefaultVal(defaultVal);
                // 找到数据库列类型对应的java数据类型
                try {
                    columnInfoBO.setJavaType(DbInfoUtil.dbDataType2JavaType(columnType));
                } catch (RuntimeException e) {
                    throw new RuntimeException(String.format("[%s]表[%s]字段,sql类型转java类型时异常:%s", tableName, columnName, e.getMessage()), e);
                }
                List<ColumnInfoBO> list = tableInfoBO.getColumnList();
                if (null == list) {
                    list = new ArrayList<>();
                    tableInfoBO.setColumnList(list);
                }
                list.add(columnInfoBO);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (null != columnsResultSet) {
                columnsResultSet.close();
            }
        }
    }

}
