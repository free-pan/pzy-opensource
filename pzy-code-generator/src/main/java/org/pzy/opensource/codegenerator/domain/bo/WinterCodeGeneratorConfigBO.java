package org.pzy.opensource.codegenerator.domain.bo;

import lombok.Data;
import org.pzy.opensource.codegenerator.domain.enums.CodeGeneratorModelEnum;
import org.pzy.opensource.codegenerator.domain.enums.WinterStyleSuperEntityEnum;

/**
 * 代码生成配置信息
 *
 * @author pan
 * @date 2020/3/29
 */
@Data
public class WinterCodeGeneratorConfigBO extends AbstractCodeGeneratorConfigBO {

    /**
     * 父类实体信息
     */
    private WinterStyleSuperEntityEnum superEntityInfoBO;
    /**
     * 代码生成模式.默认为:CodeGeneratorModelEnum.EXCEPT_CONTROLLER;
     */
    private CodeGeneratorModelEnum codeGeneratorModelEnum;

    /**
     * 不需要指定作者时使用该构造方法
     *
     * @param dbConnectionInfo  数据库连接信息
     * @param moduleName        模块名
     * @param parentPackage     模块所属父包名
     * @param projectPath       项目根目录(代码输出根目录).
     * @param superEntityInfoBO 父类实体信息
     * @param tableInfoBO       表相关配置
     */
    public WinterCodeGeneratorConfigBO(DbConnectionInfo dbConnectionInfo, String moduleName, String parentPackage, String projectPath, WinterStyleSuperEntityEnum superEntityInfoBO, TableInfoBO tableInfoBO) {
        super(dbConnectionInfo, moduleName, parentPackage, projectPath, tableInfoBO);
        this.superEntityInfoBO = superEntityInfoBO;
        this.codeGeneratorModelEnum = CodeGeneratorModelEnum.EXCEPT_CONTROLLER;
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
    public WinterCodeGeneratorConfigBO(DbConnectionInfo dbConnectionInfo, String moduleName, String parentPackage, String projectPath, TableInfoBO tableInfoBO) {
        super(dbConnectionInfo, moduleName, parentPackage, projectPath, tableInfoBO);
        this.superEntityInfoBO = WinterStyleSuperEntityEnum.None;
        this.codeGeneratorModelEnum = CodeGeneratorModelEnum.EXCEPT_CONTROLLER;
    }

    public WinterCodeGeneratorConfigBO() {
        this.codeGeneratorModelEnum = CodeGeneratorModelEnum.EXCEPT_CONTROLLER;
    }
}
