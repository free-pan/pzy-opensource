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

package org.pzy.opensource.dbutil.support.exception;

import org.pzy.opensource.comm.exception.AbstractBusinessException;

/**
 * 当使用配置的数据库连接信息无法正确获取数据库连接时,抛出此异常
 *
 * @author pan
 * @date 2019-04-29
 */
public class DbConnectionException extends AbstractBusinessException {

    private static final long serialVersionUID = 678651223051244749L;

    public DbConnectionException(String message) {
        super(message);
    }

    public DbConnectionException(String message, Exception exp) {
        super(message, exp);
    }
}
