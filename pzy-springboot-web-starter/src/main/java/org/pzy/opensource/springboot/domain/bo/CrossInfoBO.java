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
 * @author 潘志勇
 * @date 2019-01-14
 */
@Setter
@Getter
@ToString
public class CrossInfoBO {

    /**
     * 接口URI(以`/`开头). 默认值: /**
     */
    private String uri;
    /**
     * 允许的访问的域名(多个使用分号分割).默认值:*
     * e.g *;http://www.baidu.com;http://localhost:8081;https://*.aliyun.com
     */
    private String allowedOrigins;
    /**
     * 允许的请求头(多个使用分号分割).默认值:*
     * e.g *;
     */
    private String allowedHeaders;
    /**
     * 允许的请求方法.(多个使用分好分割).默认值:*
     * e.g *;POST;GET
     */
    private String allowedMethods;

    /**
     * 跨域请求是否允许携带cookie. 默认: true
     */
    private Boolean allowCredentials;

    public CrossInfoBO() {
        this.allowedOrigins = "*";
        this.allowedMethods = "*";
        this.allowedHeaders = "*";
        this.uri = "/**";
        this.allowCredentials = true;
    }
}
