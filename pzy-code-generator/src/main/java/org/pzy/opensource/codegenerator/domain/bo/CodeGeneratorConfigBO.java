package org.pzy.opensource.codegenerator.domain.bo;

import lombok.Data;

/**
 * 代码生成配置信息
 *
 * @author pan
 * @date 2020/3/29
 */
@Data
public class CodeGeneratorConfigBO extends AbstractCodeGeneratorConfigBO {

    /**
     * 父类实体信息[没有就不用设置]
     */
    private SuperEntityInfoBO superEntityInfoBO;
    /**
     * DAO接口的父类接口. 只有当有自己扩展BaseMapper时, 才需要指定
     */
    private String superMapperClass;

    /**
     * 不需要指定作者时使用该构造方法
     *
     * @param dbConnectionInfo  数据库连接信息
     * @param moduleName        模块名
     * @param parentPackage     模块所属父包名
     * @param projectPath       项目根目录(代码输出根目录).
     * @param superEntityInfoBO 父类实体信息[没有就不用设置]
     * @param tableInfoBO       表相关配置
     */
    public CodeGeneratorConfigBO(DbConnectionInfo dbConnectionInfo, String moduleName, String parentPackage, String projectPath, SuperEntityInfoBO superEntityInfoBO, TableInfoBO tableInfoBO) {
        super(dbConnectionInfo, moduleName, parentPackage, projectPath, tableInfoBO);
        this.superEntityInfoBO = superEntityInfoBO;
    }

    /**
     * 不需要指定作者,父类信息时使用该构造方法
     *
     * @param dbConnectionInfo 数据库连接信息
     * @param moduleName       模块名
     * @param parentPackage    模块所属父包名
     * @param projectPath      项目根目录(代码输出根目录).
     * @param tableInfoBO      表相关配置
     */
    public CodeGeneratorConfigBO(DbConnectionInfo dbConnectionInfo, String moduleName, String parentPackage, String projectPath, TableInfoBO tableInfoBO) {
        super(dbConnectionInfo, moduleName, parentPackage, projectPath, tableInfoBO);
    }
}
