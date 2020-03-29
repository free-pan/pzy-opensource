package org.pzy.opensource.codegenerator.support.util;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;
import org.pzy.opensource.codegenerator.domain.bo.*;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 代码生成工具
 *
 * @author pan
 * @date 2020/3/29
 */
public class CodeGeneratorUtil {

    private CodeGeneratorUtil() {
    }

    /**
     * 根据配置生成Winter风格的代码
     *
     * @param winterCodeGeneratorConfigBO
     */
    public static void generateWinterStyle(WinterCodeGeneratorConfigBO winterCodeGeneratorConfigBO) {
        SuperEntityInfoBO superEntityInfoBO = new SuperEntityInfoBO(winterCodeGeneratorConfigBO.getSuperEntityInfoBO().getEntityClassName(), winterCodeGeneratorConfigBO.getSuperEntityInfoBO().getEntityColumns());
        CodeGeneratorConfigBO codeGeneratorConfigBO = new CodeGeneratorConfigBO(winterCodeGeneratorConfigBO.getDbConnectionInfo(), winterCodeGeneratorConfigBO.getModuleName(), winterCodeGeneratorConfigBO.getParentPackage(), winterCodeGeneratorConfigBO.getProjectPath(), superEntityInfoBO, winterCodeGeneratorConfigBO.getTableInfoBO());
        codeGeneratorConfigBO.setAuthor(winterCodeGeneratorConfigBO.getAuthor());

        CodeTemplateInfoBO codeTemplateInfoBO = new CodeTemplateInfoBO();
        codeTemplateInfoBO.setEntityJavaTemplate("/winter-style-template/entity.java");
        codeTemplateInfoBO.setDaoJavaTemplate("/winter-style-template/dao.java");
        codeTemplateInfoBO.setDaoXmlTemplate(null);
        codeTemplateInfoBO.setServiceJavaTemplate("/winter-style-template/service.java");
        codeTemplateInfoBO.setServiceImplJavaTemplate("/winter-style-template/serviceImpl.java");
        codeTemplateInfoBO.setControllerJavaTemplate("/winter-style-template/controller.java");
        List<FileOutConfig> otherExtendsCodeTemplate = new ArrayList<>();
        otherExtendsCodeTemplate.add(new FileOutConfig("/winter-style-template/dao.xml.vm") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                return winterCodeGeneratorConfigBO.getProjectPath() + "/src/main/resources/mapper/" + winterCodeGeneratorConfigBO.getModuleName()
                        + "/" + tableInfo.getEntityName() + "DAO" + StringPool.DOT_XML;
            }
        });
        otherExtendsCodeTemplate.add(new FileOutConfig("/winter-style-template/addDTO.java.vm") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                return winterCodeGeneratorConfigBO.getProjectPath() + "/src/main/java/" + winterCodeGeneratorConfigBO.getParentPackage().replaceAll("\\.", File.separator) + "/" + winterCodeGeneratorConfigBO.getModuleName()
                        + "/dto/" + tableInfo.getEntityName() + "AddDTO" + StringPool.DOT_JAVA;
            }
        });
        otherExtendsCodeTemplate.add(new FileOutConfig("/winter-style-template/editDTO.java.vm") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                return winterCodeGeneratorConfigBO.getProjectPath() + "/src/main/java/" + winterCodeGeneratorConfigBO.getParentPackage().replaceAll("\\.", File.separator) + "/" + winterCodeGeneratorConfigBO.getModuleName()
                        + "/dto/" + tableInfo.getEntityName() + "EditDTO" + StringPool.DOT_JAVA;
            }
        });
        otherExtendsCodeTemplate.add(new FileOutConfig("/winter-style-template/vo.java.vm") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                return winterCodeGeneratorConfigBO.getProjectPath() + "/src/main/java/" + winterCodeGeneratorConfigBO.getParentPackage().replaceAll("\\.", File.separator) + "/" + winterCodeGeneratorConfigBO.getModuleName()
                        + "/vo/" + tableInfo.getEntityName() + "VO" + StringPool.DOT_JAVA;
            }
        });
        otherExtendsCodeTemplate.add(new FileOutConfig("/winter-style-template/restApp.java.vm") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                return winterCodeGeneratorConfigBO.getProjectPath() + "/src/main/java/" + winterCodeGeneratorConfigBO.getParentPackage().replaceAll("\\.", File.separator)
                        + "/RestApp" + StringPool.DOT_JAVA;
            }
        });
        otherExtendsCodeTemplate.add(new FileOutConfig("/winter-style-template/application.yml.vm") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                return winterCodeGeneratorConfigBO.getProjectPath() + "/src/main/resources/application.yml";
            }
        });
        otherExtendsCodeTemplate.add(new FileOutConfig("/winter-style-template/logback.xml.vm") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                return winterCodeGeneratorConfigBO.getProjectPath() + "/src/main/resources/logback.xml";
            }
        });
        otherExtendsCodeTemplate.add(new FileOutConfig("/winter-style-template/redisson.yml.vm") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                return winterCodeGeneratorConfigBO.getProjectPath() + "/src/main/resources/redisson.yml";
            }
        });
        otherExtendsCodeTemplate.add(new FileOutConfig("/winter-style-template/spy.properties.vm") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                return winterCodeGeneratorConfigBO.getProjectPath() + "/src/main/resources/spy.properties";
            }
        });
        codeTemplateInfoBO.setOtherExtendsCodeTemplate(otherExtendsCodeTemplate);

        CodeGeneratorUtil.execute(codeGeneratorConfigBO, codeTemplateInfoBO);
    }

    /**
     * 根据配置生成mybatis plus风格的代码
     *
     * @param codeGeneratorConfigBO
     */
    public static void generate(CodeGeneratorConfigBO codeGeneratorConfigBO) {
        CodeGeneratorUtil.execute(codeGeneratorConfigBO, null);
    }

    /**
     * 根据配置执行代码生成
     *
     * @param codeGeneratorConfigBO
     */
    public static void execute(CodeGeneratorConfigBO codeGeneratorConfigBO, CodeTemplateInfoBO codeTemplateInfoBO) {
        DbConnectionInfo dbConnectionInfo = codeGeneratorConfigBO.getDbConnectionInfo();
        SuperEntityInfoBO superEntityInfoBO = codeGeneratorConfigBO.getSuperEntityInfoBO();
        TableInfoBO tableInfoBO = codeGeneratorConfigBO.getTableInfoBO();

        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();
        mpg.setTemplateEngine(new VelocityTemplateEngine());

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        // 设置id类型
        gc.setIdType(tableInfoBO.getIdType());
        gc.setBaseResultMap(true);
        gc.setBaseColumnList(true);
        // 设置mapper 命名方式
        gc.setMapperName("%sDAO");
        gc.setServiceName("%sService");
        // 覆盖已有文件
        gc.setFileOverride(true);
        String projectPath = codeGeneratorConfigBO.getProjectPath();
        gc.setOutputDir(projectPath + "/src/main/java");
        gc.setAuthor(codeGeneratorConfigBO.getAuthor());
        gc.setOpen(true);
        // 实体属性 Swagger2 注解
        gc.setSwagger2(true);
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl(dbConnectionInfo.getUrl());
        // dsc.setSchemaName("public");
        dsc.setDriverName(dbConnectionInfo.getDriverName());
        dsc.setUsername(dbConnectionInfo.getUsername());
        dsc.setPassword(dbConnectionInfo.getPassword());
        mpg.setDataSource(dsc);

        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setModuleName(codeGeneratorConfigBO.getModuleName());
        pc.setParent(codeGeneratorConfigBO.getParentPackage());
        mpg.setPackageInfo(pc);

        // 自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
                Map<String, Object> customParamMap = new HashMap<>();
                String modulePackage = codeGeneratorConfigBO.getParentPackage() + "." + codeGeneratorConfigBO.getModuleName();
                String dtoPackage = modulePackage + "." + "dto";
                String voPackage = modulePackage + "." + "vo";
                customParamMap.put("DTO", dtoPackage);
                customParamMap.put("VO", voPackage);
                customParamMap.put("BasePackage", codeGeneratorConfigBO.getParentPackage());
                this.setMap(customParamMap);
            }
        };

        // 配置模板
        TemplateConfig templateConfig = new TemplateConfig();

        // 自定义输出配置
        List<FileOutConfig> focList = new ArrayList<>();
        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);

        if (null == codeTemplateInfoBO) {
            // codeTemplateInfoBO为null表示使用mybatis plus的默认风格的代码生成模板
            String templatePath = "/templates/mapper.xml.vm";
            // 自定义配置会被优先输出
            focList.add(new FileOutConfig(templatePath) {
                @Override
                public String outputFile(TableInfo tableInfo) {
                    // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                    return projectPath + "/src/main/resources/mapper/" + pc.getModuleName()
                            + "/" + tableInfo.getEntityName() + "DAO" + StringPool.DOT_XML;
                }
            });
            /*
            cfg.setFileCreate(new IFileCreate() {
                @Override
                public boolean isCreate(ConfigBuilder configBuilder, FileType fileType, String filePath) {
                    // 判断自定义文件夹是否需要创建
                    checkDir("调用默认方法创建的目录，自定义目录用");
                    if (fileType == FileType.MAPPER) {
                        // 已经生成 mapper 文件判断存在，不想重新生成返回 false
                        return !new File(filePath).exists();
                    }
                    // 允许生成模板文件
                    return true;
                }
            });
            */
            templateConfig.setXml(null);
        } else {
            // codeTemplateInfoBO为null表示使用自定义风格的代码生成模板
            // 配置自定义输出模板
            //指定自定义模板路径，注意不要带上.ftl/.vm, 会根据使用的模板引擎自动识别
            templateConfig.setEntity(codeTemplateInfoBO.getEntityJavaTemplate());
            templateConfig.setMapper(codeTemplateInfoBO.getDaoJavaTemplate());
            templateConfig.setXml(codeTemplateInfoBO.getDaoXmlTemplate());
            templateConfig.setService(codeTemplateInfoBO.getServiceJavaTemplate());
            templateConfig.setServiceImpl(codeTemplateInfoBO.getServiceImplJavaTemplate());
            templateConfig.setController(codeTemplateInfoBO.getControllerJavaTemplate());
            focList.addAll(codeTemplateInfoBO.getOtherExtendsCodeTemplate());
        }

        mpg.setTemplate(templateConfig);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setEntityLombokModel(true);
        strategy.setRestControllerStyle(false);
        // Controller公共父类
//        strategy.setSuperControllerClass("你自己的父类控制器,没有就不用设置!");
        if (null != superEntityInfoBO) {
            // 实体父类
            strategy.setSuperEntityClass(superEntityInfoBO.getEntityClassName());
            // 写于父类中的公共字段
            strategy.setSuperEntityColumns(superEntityInfoBO.getEntityColumns());
        }
        strategy.setInclude(tableInfoBO.getTableArr());
        strategy.setControllerMappingHyphenStyle(true);
        if (!StringUtils.isBlank(tableInfoBO.getTablePrefix())) {
            strategy.setTablePrefix(tableInfoBO.getTablePrefix());
        }
        if (!StringUtils.isBlank(tableInfoBO.getColumnPrefix())) {
            strategy.setFieldPrefix(tableInfoBO.getColumnPrefix());
        }
        mpg.setStrategy(strategy);
        mpg.execute();
    }
}
