package org.pzy.opensource.codegenerator.support.util;

import org.pzy.opensource.codegenerator.domain.bo.DbConnectionInfo;
import org.pzy.opensource.codegenerator.domain.bo.TableInfoBO;
import org.pzy.opensource.codegenerator.domain.bo.WinterCodeGeneratorConfigBO;
import org.pzy.opensource.codegenerator.domain.enums.WinterStyleSuperEntityEnum;
import org.testng.annotations.Test;

/**
 * @author pan
 * @date 2020/3/29
 */
public class CodeGeneratorUtilTest {

    @Test
    public void testGenerateWinterStyle() {
        String driverName = "com.mysql.cj.jdbc.Driver";
        String username = "root";
        String password = "root";
        String url = "jdbc:mysql://localhost:3306/pzy-acl?useUnicode=true&characterEncoding=UTF8&allowMultiQueries=true&useSSL=false";
        DbConnectionInfo dbConnectionInfo = new DbConnectionInfo(driverName, username, password, url);

        String moduleName = "sys";
        String parentPackage = "org.pzy.opensource.archetypesystem";
        String projectPath = "/Users/pan/草稿/tmp";

        WinterStyleSuperEntityEnum superEntityInfoBO = WinterStyleSuperEntityEnum.LogicDelBaseEntity;

        String[] tableArr = new String[]{"sys_user"};
        String tablePrefix = null;
        String columnPrefix = null;
        TableInfoBO tableInfoBO = new TableInfoBO(tableArr, tablePrefix, columnPrefix);

        WinterCodeGeneratorConfigBO winterCodeGeneratorConfigBO = new WinterCodeGeneratorConfigBO(dbConnectionInfo, moduleName, parentPackage, projectPath, superEntityInfoBO, tableInfoBO);
        CodeGeneratorUtil.generateWinterStyle(winterCodeGeneratorConfigBO);
    }
}