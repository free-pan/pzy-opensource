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

package org.pzy.opensource.springboot.swagger.plugin;

import io.swagger.annotations.ApiOperation;
import org.pzy.opensource.springboot.properties.SwaggerProperties;
import org.pzy.opensource.springboot.swagger.annotation.ApiStatus;
import org.pzy.opensource.springboot.swagger.util.ExternalDocLoadUtil;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.OperationBuilderPlugin;
import springfox.documentation.spi.service.contexts.OperationContext;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author pan
 */
@Order(Ordered.HIGHEST_PRECEDENCE - 10)
public class ApiStatusOperationBuilderPlugin implements OperationBuilderPlugin {

    private SwaggerProperties swaggerProperties;

    public ApiStatusOperationBuilderPlugin(SwaggerProperties swaggerProperties) {
        this.swaggerProperties = swaggerProperties;
    }

    @Override
    public boolean supports(DocumentationType delimiter) {
        return true;
    }

    @Override
    public void apply(OperationContext context) {
        List<ApiOperation> list = context.findAllAnnotations(ApiOperation.class);
        if (list.size() > 0) {
            List<ApiStatus> statusList = context.findAllAnnotations(ApiStatus.class);
            if (statusList.size() > 0) {
                ApiStatus status = statusList.get(0);
                //替换summary默认值
                updateSummary(context, list, status);
                //替换notes默认值
                updateNotes(context, list);
            }
//            List<SplitPage> splitPage = context.findAllAnnotations(SplitPage.class);
//            if (splitPage.size() > 0) {
//                List<Parameter> parameters = new ArrayList<>();
//                String desc = String.format("页号. 最小值:%s, 最大值:%s, 默认值:%s. 错误值会被强转为:%s. 既可以通过query方式传递也可以通过header方式传递(优先取header中的)", swaggerProperties.getMinPage(), swaggerProperties.getMaxPage(), swaggerProperties.getDefaultPage(), swaggerProperties.getDefaultPage());
//                parameters.add(new ParameterBuilder().name(swaggerProperties.getPageParamName())
//                        .description(desc)
//                        .modelRef(new ModelRef("int")).parameterType("header").required(false).build());
//                desc = String.format("每页显示记录数. 最小值:%s, 最大值:%s. 默认值:%s. 错误值会被强转为:%s. 既可以通过query方式传递也可以通过header方式传递(优先取header中的)", swaggerProperties.getMinSize(), swaggerProperties.getMaxSize(), swaggerProperties.getDefaultSize(), swaggerProperties.getDefaultSize());
//                parameters.add(new ParameterBuilder().name(swaggerProperties.getSizeParamName())
//                        .description(desc)
//                        .modelRef(new ModelRef("int")).parameterType("header").required(false).build());
//                context.operationBuilder().parameters(parameters);
//            }
        }

        Set<ResponseMessage> responseMessagesSet = new HashSet<>();
        Map<Integer, String> httpCodeDescMap = swaggerProperties.getHttpCodeDescMap();
        Set<Map.Entry<Integer, String>> entrySet = httpCodeDescMap.entrySet();
        for (Map.Entry<Integer, String> entry : entrySet) {
            responseMessagesSet.add(new ResponseMessageBuilder().code(entry.getKey()).message(entry.getValue()).build());
        }
        context.operationBuilder().responseMessages(responseMessagesSet);
    }

    /**
     * 替换summary默认值
     *
     * @param context
     * @param list
     * @param status
     */
    private void updateSummary(OperationContext context, List<ApiOperation> list, ApiStatus status) {
        context.operationBuilder().summary(status.status().getMsg() + list.get(0).value());
    }

    /**
     * 替换notes默认值
     *
     * @param context
     * @param list
     */
    private void updateNotes(OperationContext context, List<ApiOperation> list) {
        String originalNotes = list.get(0).notes();
        if (ExternalDocLoadUtil.needLoadExternalDoc(originalNotes)) {
            originalNotes = originalNotes.substring(1,originalNotes.length()-1);
            String newNotes = ExternalDocLoadUtil.loadExternalDoc(originalNotes);
            context.operationBuilder().notes(newNotes);
        }
    }

}
