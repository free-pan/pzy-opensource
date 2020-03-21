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

package org.pzy.opensource.security.domain.bo;

import lombok.*;

import java.io.Serializable;

/**
 * 权限信息
 *
 * @author 潘志勇
 * @date 2019-02-06
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PermissionInfoBO implements Serializable {

    private static final long serialVersionUID = 435382049247766615L;

    /**
     * 权限标识[非空]
     */
    private String flag;
    /**
     * ant路径表达式[可以为空或null]
     */
    private String pathPattern;
    /**
     * 请求方式[非空]. ALL,GET,POST,PATCH,PUT,DELETE
     */
    private String requestMethod;
}
