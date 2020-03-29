package org.pzy.opensource.codegenerator.domain.bo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 代码生成配置信息
 *
 * @author pan
 * @date 2020/3/29
 */
@Data
@NoArgsConstructor
public abstract class AbstractCodeGeneratorConfigBO implements Serializable {

    protected static final String DEFAULT_AUTHOR = "pan";
    /**
     * 数据库连接信息
     */
    private DbConnectionInfo dbConnectionInfo;
    /**
     * 作者
     */
    private String author;
    /**
     * 模块名
     */
    private String moduleName;
    /**
     * 模块所属父包名
     */
    private String parentPackage;
    /**
     * 项目根目录(代码输出根目录).
     */
    private String projectPath;
    /**
     * 表相关配置
     */
    private TableInfoBO tableInfoBO;

    /**
     * 不需要指定作者时使用该构造方法
     *
     * @param dbConnectionInfo 数据库连接信息
     * @param moduleName       模块名
     * @param parentPackage    模块所属父包名
     * @param projectPath      项目根目录(代码输出根目录).
     * @param tableInfoBO      表相关配置
     */
    public AbstractCodeGeneratorConfigBO(DbConnectionInfo dbConnectionInfo, String moduleName, String parentPackage, String projectPath, TableInfoBO tableInfoBO) {
        this.author = AbstractCodeGeneratorConfigBO.DEFAULT_AUTHOR;
        this.dbConnectionInfo = dbConnectionInfo;
        this.moduleName = moduleName;
        this.parentPackage = parentPackage;
        this.projectPath = projectPath;
        this.tableInfoBO = tableInfoBO;
    }
}
