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

package org.pzy.opensource.security.shiro.exception;

import org.apache.shiro.authc.CredentialsException;

/**
 * @author 潘志勇
 * @date 2019-02-06
 */
public class ExpiredAccountException extends CredentialsException {

    private static final long serialVersionUID = 1933553944189663491L;

    public ExpiredAccountException() {
    }

    public ExpiredAccountException(String message) {
        super(message);
    }

    public ExpiredAccountException(Throwable cause) {
        super(cause);
    }

    public ExpiredAccountException(String message, Throwable cause) {
        super(message, cause);
    }
}
