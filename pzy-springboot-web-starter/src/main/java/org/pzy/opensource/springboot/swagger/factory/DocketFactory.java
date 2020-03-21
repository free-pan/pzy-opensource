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

package org.pzy.opensource.springboot.swagger.factory;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import io.swagger.annotations.Api;
import org.pzy.opensource.springboot.domain.bo.SwaggerInfoBO;
import org.pzy.opensource.springboot.swagger.util.ExternalDocLoadUtil;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.List;


/**
 * Docket实例构建工厂
 *
 * @author 潘志勇
 * @date 2019-01-14
 */
public class DocketFactory {

    /**
     * 默认的分割字符串';'
     */
    private static final String DEFAULT_SPLIT_CHART = ";";

    private static final AntPathMatcher antPathMatcher = new AntPathMatcher();

    private DocketFactory() {
    }

    /**
     * 创建Docket实例
     *
     * @param swaggerInfo swagger文档信息
     * @return
     */
    public static Docket newInstance(SwaggerInfoBO swaggerInfo) {
        if (CollectionUtils.isEmpty(swaggerInfo.getMatchPackages())) {
            throw new RuntimeException("请配置swagger扫描的包路径,多个包路径使用;号分割");
        }
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo(swaggerInfo))
                .select()
                //为当前包路径
                .apis(basePackage(swaggerInfo.getMatchPackages()))
                .paths(PathSelectors.any())
                .build();
    }

    private static ApiInfo apiInfo(SwaggerInfoBO swaggerInfo) {
        boolean needLoadExternalDoc = ExternalDocLoadUtil.needLoadExternalDoc(swaggerInfo.getDescription());
        String desc = swaggerInfo.getDescription();
        if (needLoadExternalDoc) {
            // 将外部文档的内容作为desc的内容
            desc = desc.substring(1, desc.length() - 1);
            desc = ExternalDocLoadUtil.loadExternalDoc(desc);
        }
        ApiInfoBuilder builder = new ApiInfoBuilder()
                .title(swaggerInfo.getTitle())
                .description(desc)
                .termsOfServiceUrl(swaggerInfo.getTermsOfServiceUrl()).version(swaggerInfo.getVersion());
        if (null != swaggerInfo.getContactInfo()) {
            builder.contact(new Contact(swaggerInfo.getContactInfo().getName(), swaggerInfo.getContactInfo().getUrl(), swaggerInfo.getContactInfo().getEmail()));
        }
        return builder.build();
    }

    public static Predicate<RequestHandler> basePackage(final List<String> basePackageList) {
        return input -> declaringClass(input).transform(handlerPackage(basePackageList)).or(true);
    }

    private static Function<Class<?>, Boolean> handlerPackage(final List<String> basePackageList) {
        return input -> {
            // 循环判断匹配
            for (String strPackage : basePackageList) {
                boolean isMatch = antPathMatcher.match(strPackage,input.getPackage().getName());
                boolean annoMatch = null != input.getAnnotation(Api.class);
                if (isMatch && annoMatch) {
                    return true;
                }
            }
            return false;
        };
    }

    private static Optional<? extends Class<?>> declaringClass(RequestHandler input) {
        return Optional.fromNullable(input.declaringClass());
    }

}
