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

package org.pzy.opensource.swaggerui.controller;

import org.pzy.opensource.domain.GlobalConstant;
import org.pzy.opensource.swaggerui.config.SwaggerUIAutoConfiguration;
import org.pzy.opensource.web.util.IpUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.UnsupportedEncodingException;
import java.net.SocketException;
import java.net.URLEncoder;
import java.util.List;

/**
 * @author pan
 * @date 2020-01-26
 */
@Controller
@RequestMapping(SwaggerUIAutoConfiguration.SWAGGER_URI)
public class SwaggerUiController {

    @Value("${server.port:8080}")
    private Integer port;

    @Value("${server.context-path:}")
    private String contextPath;

    @GetMapping
    public String redirect() throws SocketException, UnsupportedEncodingException {
        List<String> ipList = IpUtil.getCurrentMachineIp();
        String swaggerDocUrl = String.format("%s:%s/v2/api-docs", ipList.get(0), port);
        String redirectFormat = String.format("/%s.html#/index?httpType=1&swaggerUri=%s", SwaggerUIAutoConfiguration.SWAGGER_URI, URLEncoder.encode(swaggerDocUrl, GlobalConstant.DEFAULT_CHARSET));
        return "redirect:" + redirectFormat;
    }
}
