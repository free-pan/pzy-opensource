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

/**
 * swagger项目的主要联系人信息
 *
 * @author pzy
 * @date 2018/12/16
 */
@Setter
@Getter
@ToString
public class SwaggerContactInfoBO {

    /**
     * 负责人名字
     */
    private String name;
    /**
     * 负责人主页
     */
    private String url;
    /**
     * 负责人邮件地址
     */
    private String email;

    public SwaggerContactInfoBO() {
        this.name = "自动生成name";
        this.url = "自动生成url";
        this.email = "自动生成email";
    }

}
