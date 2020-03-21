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

import org.apache.commons.lang3.StringUtils;
import org.pzy.opensource.dbutil.domain.bo.DbConnectionInfoBO;
import org.pzy.opensource.dbutil.support.exception.DbConnectionException;
import org.pzy.opensource.dbutil.support.exception.LoadDbConnectionInfoException;
import org.pzy.opensource.spring.util.ResourceFileLoadUtil;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * 用于获取和关闭数据库连接
 *
 * @author 潘志勇
 * @date 2019-01-12
 */
public class ConnectionUtil {

    private static final String ENCODE = "UTF-8";

    private ConnectionUtil() {
    }

    public static void commit(Connection connection) {
        try {
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException("事务提交失败!", e);
        }
    }

    /**
     * 获取数据库连接
     *
     * @param dbConnectionInfo 数据库连接信息
     * @param autoCommit       是否自动提交事务
     * @return 数据库连接
     */
    public static Connection getConnection(DbConnectionInfoBO dbConnectionInfo, boolean autoCommit) {
        try {
            Class.forName(dbConnectionInfo.getDriver());
        } catch (ClassNotFoundException e) {
            throw new DbConnectionException(String.format("数据库驱动[{}]未找到!", dbConnectionInfo.getDriver()), e);
        }
        try {
            Connection connection = DriverManager.getConnection(dbConnectionInfo.getUrl(), dbConnectionInfo.getUser(), dbConnectionInfo.getPassword());
            connection.setAutoCommit(autoCommit);
            return connection;
        } catch (SQLException e) {
            throw new DbConnectionException("数据库连接异常!", e);
        }
    }

    /**
     * 获取数据库连接<br>
     * propertiesFile支持的写法: <br>
     * file:C:/tmp/test.properties, file:/pan/tmp/test.properties, classpath:dbtest/test.properties, classpath:test.properties<br>
     * <pre>配置文件示例:
     * database.driver=com.mysql.jdbc.Driver
     * database.url=jdbc:mysql://localhost:3306/free-db?useUnicode=true&gt;characterEncoding=UTF8&gt;allowMultiQueries=true&gt;useSSL=false
     * database.user=root
     * database.password=root
     * </pre>
     *
     * @param propertiesFile 配置文件
     * @param autoCommit     是否自动提交事务
     * @return 数据库连接
     */
    public static Connection getConnection(String propertiesFile, boolean autoCommit) {
        DbConnectionInfoBO dbConnectionInfo = getDbConnectionInfoFromProperties(propertiesFile);
        return getConnection(dbConnectionInfo, autoCommit);
    }

    /**
     * propertiesFile支持的写法: <br>
     * file:C:/tmp/test.properties, file:/pan/tmp/test.properties, classpath:dbtest/test.properties, classpath:test.properties<br>
     * <pre>配置文件示例:
     * database.driver=com.mysql.jdbc.Driver
     * database.url=jdbc:mysql://localhost:3306/free-db?useUnicode=true&gt;characterEncoding=UTF8&gt;allowMultiQueries=true&gt;useSSL=false
     * database.user=root
     * database.password=root
     * </pre>
     *
     * @param propertiesFile 配置文件
     * @return
     */
    public static DbConnectionInfoBO getDbConnectionInfoFromProperties(String propertiesFile) {

        Properties properties = null;
        try {
            properties = ResourceFileLoadUtil.readAsProperties(propertiesFile, ENCODE);
        } catch (IOException e) {
            throw new LoadDbConnectionInfoException("数据库连接信息读取异常!");
        }

        String url = properties.getProperty("database.url", "").trim();
        if (StringUtils.isEmpty(url)) {
            throw new RuntimeException("dbunit.properties配置文件,database.url配置是必须的!");
        }
        String user = properties.getProperty("database.user", "").trim();
        if (StringUtils.isEmpty(user)) {
            throw new RuntimeException("dbunit.properties配置文件,database.user配置是必须的!");
        }
        String password = properties.getProperty("database.password", "").trim();
        if (StringUtils.isEmpty(password)) {
            throw new RuntimeException("dbunit.properties配置文件,database.password配置是必须的!");
        }
        String driver = properties.getProperty("database.driver", "").trim();
        if (StringUtils.isEmpty(driver)) {
            throw new RuntimeException("dbunit.properties配置文件,database.driver配置是必须的!");
        }
        DbConnectionInfoBO dbConnectionInfo = new DbConnectionInfoBO(driver, url, user, password);
        return dbConnectionInfo;
    }

    /**
     * 关闭数据库连接
     *
     * @param connection
     */
    public static void closeConnection(Connection connection) {
        if (null != connection) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException("数据库连接关闭异常!", e);
            }
        }
    }
}
