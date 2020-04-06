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

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author 潘志勇
 * @date 2019-02-06
 */
@Data
@AllArgsConstructor
@Accessors(chain = true)
public class SimpleShiroUserBO implements Serializable {

    private static final long serialVersionUID = -2216198644853379021L;

    /**
     * 在当前系统中用户真正的唯一值
     */
    private String ukFlag;

    /**
     * 用户名/账号/邮箱等可以用来唯一查找的字段(用户登录时,实际使用的唯一标识)
     */
    private String username;
    /**
     * 账号是否未过期. true表示未过期
     */
    private Boolean accountNonExpired;
    /**
     * 账号是否未被锁定. true表示未被锁定
     */
    private Boolean accountNonLocked;
    /**
     * 登录凭证(密码)是否过期. true表示未过期
     */
    private Boolean credentialsNonExpired;
    /**
     * 账户是否启用(未被逻辑删除). true表示可用(未被逻辑删除,已激活)
     */
    private Boolean enabled;

    public SimpleShiroUserBO() {
        // 账号未过期
        this.accountNonExpired = true;
        // 账号未锁定
        this.accountNonLocked = true;
        // 密码未过期
        this.credentialsNonExpired = true;
        // 账号已启用
        this.enabled = true;
    }

}
