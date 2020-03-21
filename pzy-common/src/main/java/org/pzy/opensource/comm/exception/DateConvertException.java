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
 * 日期转换异常
 *
 * @author pan
 * @date 2019-03-22
 */
public class DateConvertException extends AbstractBusinessException {

    private static final long serialVersionUID = 5768447509646922582L;

    public DateConvertException(String message) {
        super(message);
    }

    public DateConvertException(String message, Exception exp) {
        super(message, exp);
    }
}
