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

package org.pzy.opensource.springboot.properties;

import lombok.Data;
import org.pzy.opensource.springboot.domain.bo.GlobalHeaderBO;
import org.pzy.opensource.springboot.domain.bo.SwaggerInfoBO;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 潘志勇
 * @date 2019-01-31
 */
@Data
@ConfigurationProperties(prefix = "component.swagger", ignoreInvalidFields = true)
public class SwaggerProperties extends SwaggerInfoBO {

    /**
     * 是否启用
     */
    private boolean enable;

    /**
     * 最小的分页大小. 默认值:2
     */
    private int minSize = 2;

    /**
     * 最大分页大小. 默认值: 100
     */
    private int maxSize = 100;

    /**
     * 默认分页大小. 默认值: 10
     */
    private int defaultSize = 10;

    /**
     * 最小页号. 默认值:1
     */
    private int minPage = 1;

    /**
     * 默认页号. 默认值:minPage
     */
    private int defaultPage = minPage;

    /**
     * 最大页号. 默认值:200
     */
    private int maxPage = 200;

    /**
     * 传递页号的参数名. 默认值:page
     */
    private String pageParamName = "page";

    /**
     * 传递分页大小的参数名. 默认值:size
     */
    private String sizeParamName = "size";

    /**
     * http响应状态码以及说明. key为http响应状态码, value为说明
     */
    private Map<Integer, String> httpCodeDescMap = new HashMap<>();

    /**
     * 需要swagger忽略的类集合<br/>
     * 存储的是需要被清除所有属性的类的类全名
     */
    private List<String> cleanClassList;
    /**
     * 需要被swagger以另外方式映射的类<br/>
     * 存储的是key为无swagger注解的类的类全名,value为有swagger注解的类的类全名<br/>
     */
    private Map<String, String> mappingClass;
    /**
     * 全局header参数(所有请求都需要携带的header参数)
     */
    private List<GlobalHeaderBO> globalHeaderList = new ArrayList<>();

    public SwaggerProperties() {
        httpCodeDescMap.put(200, "服务器已收到请求,并执行.");
        httpCodeDescMap.put(201, "添加成功.");
        httpCodeDescMap.put(400, "请求数据未通过验证或请求不符合系统规范.");
        httpCodeDescMap.put(401, "认证失败(需要登录/重新登录).");
        httpCodeDescMap.put(403, "权限不足.");
        httpCodeDescMap.put(404, "资源/服务不存在.");
        httpCodeDescMap.put(500, "服务端异常.");
        httpCodeDescMap.put(503, "服务不可用.");
    }
}
