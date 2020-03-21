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

package org.pzy.opensource.comm.exception;

/**
 * 验证异常. 当数据无法通过验证时,抛出此异常
 *
 * @author pan
 * @date 2019-03-22
 */
public class ValidateException extends AbstractBusinessException {

    private static final long serialVersionUID = -6806691741546394269L;

    public ValidateException(String message) {
        super(message);
    }

    public ValidateException(String message, Exception exp) {
        super(message, exp);
    }
}
