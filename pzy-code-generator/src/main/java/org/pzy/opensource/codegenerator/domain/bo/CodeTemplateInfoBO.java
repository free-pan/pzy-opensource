package org.pzy.opensource.codegenerator.domain.bo;

import com.baomidou.mybatisplus.generator.config.FileOutConfig;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 模板信息
 *
 * @author pan
 * @date 2020/3/29
 */
@Data
public class CodeTemplateInfoBO implements Serializable {
    /**
     * 实体类模板. 不要带上.ftl/.vm后缀, 代码会根据使用的模板引擎自动适配
     */
    private String entityJavaTemplate;
    /**
     * dao类模板. 不要带上.ftl/.vm后缀, 代码会根据使用的模板引擎自动适配
     */
    private String daoJavaTemplate;
    /**
     * dao类的xml模板. 不要带上.ftl/.vm后缀, 代码会根据使用的模板引擎自动适配
     */
    private String daoXmlTemplate;
    /**
     * service接口模板. 不要带上.ftl/.vm后缀, 代码会根据使用的模板引擎自动适配
     */
    private String serviceJavaTemplate;
    /**
     * service实现类模板. 不要带上.ftl/.vm后缀, 代码会根据使用的模板引擎自动适配
     */
    private String serviceImplJavaTemplate;
    /**
     * controller类模板. 不要带上.ftl/.vm后缀, 代码会根据使用的模板引擎自动适配
     */
    private String controllerJavaTemplate;

    /**
     * 其它自定义的代码模板[可以为null]
     */
    private List<FileOutConfig> otherExtendsCodeTemplate;
}
