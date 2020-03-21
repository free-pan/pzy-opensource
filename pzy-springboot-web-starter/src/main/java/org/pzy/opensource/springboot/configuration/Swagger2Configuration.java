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

package org.pzy.opensource.springboot.configuration;

import com.fasterxml.classmate.TypeResolver;
import lombok.extern.slf4j.Slf4j;
import org.pzy.opensource.comm.util.JsonUtil;
import org.pzy.opensource.springboot.domain.bo.GlobalHeaderBO;
import org.pzy.opensource.springboot.properties.SwaggerProperties;
import org.pzy.opensource.springboot.swagger.factory.AlternateTypeRuleConventionFactory;
import org.pzy.opensource.springboot.swagger.factory.DocketFactory;
import org.pzy.opensource.springboot.swagger.plugin.ApiStatusOperationBuilderPlugin;
import org.pzy.opensource.web.util.IpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.schema.AlternateTypeRuleConvention;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.service.OperationBuilderPlugin;
import springfox.documentation.spring.web.plugins.Docket;

import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

/**
 * swagger2配置
 *
 * @author 潘志勇
 * @date 2019-01-14
 */
@Slf4j
@Configuration
@ConditionalOnProperty(name = "component.swagger.enable", havingValue = "true")
@EnableConfigurationProperties({SwaggerProperties.class})
public class Swagger2Configuration {

    private static final String SWAGGER_URI = "/v2/api-docs";

    @Value("${server.port:8080}")
    private Integer port;

    @Value("${server.context-path:}")
    private String contextPath;

    @Autowired
    private SwaggerProperties swaggerInfo;

    /**
     * swagger基本配置
     *
     * @return
     * @throws SocketException
     */
    @Bean
    Docket swaggerDocket() throws Exception {
        if (CollectionUtils.isEmpty(swaggerInfo.getMatchPackages())) {
            throw new RuntimeException("请配置swagger匹配的包路径!");
        }
        log.debug("\r\n############# 启用swagger文档!匹配的包路径:{}", JsonUtil.toJsonString(this.swaggerInfo.getMatchPackages()));
        List<String> ipList = IpUtil.getCurrentMachineIp();
        StringBuilder sb = new StringBuilder();
        sb.append("\r\n").append("http://").append(ipList.get(0)).append(":").append(port).append(contextPath).append(SWAGGER_URI);
        log.debug("\r\n############# swagger文档json字符串地址(适用swagger2.0版本){}", sb.toString());
        Docket docket = DocketFactory.newInstance(swaggerInfo);
        docket.enable(swaggerInfo.isEnable());
        List<GlobalHeaderBO> globalHeaderList = swaggerInfo.getGlobalHeaderList();
        if (!CollectionUtils.isEmpty(globalHeaderList)) {
            List<Parameter> pars = new ArrayList<Parameter>();
            for (GlobalHeaderBO globalHeaderBO : globalHeaderList) {
                ParameterBuilder parameterBuilder = new ParameterBuilder();
                parameterBuilder.name(globalHeaderBO.getParamName())
                        .modelRef(new ModelRef(globalHeaderBO.getParamType()))
                        .parameterType("header")
                        .description(globalHeaderBO.getNote());
                pars.add(parameterBuilder.build());
            }
            docket.globalOperationParameters(pars);
        }
        return docket;
    }

    /**
     * swagger特殊类解析配置
     *
     * @param resolver
     * @return
     */
    @Bean
    AlternateTypeRuleConvention typeRuleConvention(TypeResolver resolver) {
        return AlternateTypeRuleConventionFactory.newInstance(resolver, swaggerInfo.getCleanClassList(), swaggerInfo.getMappingClass());
    }

    @Bean
    OperationBuilderPlugin newApiStatusOperationBuilderPlugin() {
        log.debug("\r\n############# 已注册自定义的swagger扩展[ApiStatusOperationBuilderPlugin]!");
        return new ApiStatusOperationBuilderPlugin(swaggerInfo);
    }
}
