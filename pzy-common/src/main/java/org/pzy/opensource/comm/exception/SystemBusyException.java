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
 * 系统资源不足时,抛出该异常
 *
 * @author pan
 * @date 2019-03-22
 */
public class SystemBusyException extends AbstractBusinessException {

    private static final long serialVersionUID = 5223292547317534142L;

    public SystemBusyException(String message) {
        super(message);
    }

    public SystemBusyException(String message, Exception exp) {
        super(message, exp);
    }
}
