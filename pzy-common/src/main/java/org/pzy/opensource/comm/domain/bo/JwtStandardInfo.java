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

package org.pzy.opensource.comm.domain.bo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author 潘志勇
 * @date 2019-02-08
 */
@Setter
@Getter
@ToString
public class JwtStandardInfo implements Serializable {

    private static final long serialVersionUID = -3609574606061137860L;

    /**
     * jwt签发者(公司/项目)
     */
    private String issuer;
    /**
     * 接收jwt的一方(公司/项目/团体)[可选]
     */
    private String audience;
    /**
     * 多久之后过期(单位:秒)
     */
    private Long expiresAt;

    /**
     *
     * @param issuer jwt签发者(公司/项目)
     * @param expiresAt 多久之后过期(单位:秒)
     */
    public JwtStandardInfo(String issuer, Long expiresAt) {
        this.issuer = issuer;
        this.expiresAt = expiresAt;
    }
}
