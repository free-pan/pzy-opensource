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

package org.pzy.opensource.web.domain.bo;

import lombok.*;

/**
 * 请求uri的信息
 *
 * @author pan
 * @date 2019-04-10
 */
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class HttpRequestUriInfoBO {

    /**
     * 请求方式
     */
    private String method;
    /**
     * 请求uri
     */
    private String uri;
}
