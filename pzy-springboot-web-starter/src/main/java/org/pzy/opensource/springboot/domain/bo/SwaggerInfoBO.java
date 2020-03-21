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

package org.pzy.opensource.springboot.domain.bo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * @author pzy
 * @date 2018/12/16
 */
@Setter
@Getter
@ToString
public class SwaggerInfoBO {

    /**
     * 文档标题
     */
    private String title;
    /**
     * 文档简要描述
     */
    private String description;
    /**
     * 版本
     */
    private String version;
    /**
     * 条款地址(默认为空)
     */
    private String termsOfServiceUrl;
    /**
     * 负责人信息
     */
    private SwaggerContactInfoBO contactInfo;

    /**
     * 匹配的包路径(只有对应controller的包路径与之匹配时,才会生成文档数据). 支持模糊匹配
     */
    private List<String> matchPackages;

    public SwaggerInfoBO() {
        this.termsOfServiceUrl = "";
        this.title = "自动生成的标题(默认标题)";
        this.version = "0.0.1";
        this.matchPackages = new ArrayList<>();
    }

}
