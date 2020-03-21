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

package org.pzy.opensource.swaggerui.config;

import lombok.extern.slf4j.Slf4j;
import org.pzy.opensource.swaggerui.controller.SwaggerUiController;
import org.pzy.opensource.web.util.IpUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.net.SocketException;
import java.util.List;

/**
 * @author pan
 * @date 2020-01-26
 */
@Configuration
@Slf4j
@ConditionalOnProperty(name = "component.swagger.enable", havingValue = "true")
@ConditionalOnWebApplication
public class SwaggerUIAutoConfiguration {

    @Value("${server.port:8080}")
    private Integer port;

    @Value("${server.context-path:}")
    private String contextPath;

    public static final String SWAGGER_URI = "pzy-swagger-ui";

    @PostConstruct
    public void init() throws SocketException {
        List<String> ipList = IpUtil.getCurrentMachineIp();
        StringBuilder sb = new StringBuilder();
        sb.append("\r\n").append("http://").append(ipList.get(0)).append(":").append(port).append(contextPath).append("/").append(SWAGGER_URI);
        log.debug("\r\n############# swagger-ui界面地址:{}", sb.toString());
    }

    @Bean
    SwaggerUiController swaggerUiController() {
        return new SwaggerUiController();
    }
}
