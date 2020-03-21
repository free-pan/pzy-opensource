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
 * 当文件的文件后缀名不符合规范时,抛出该异常
 *
 * @author pan
 * @date 2019-04-10
 */
public class FileExtException extends ValidateException {

    private static final long serialVersionUID = -1122936976800572185L;

    public FileExtException(String message) {
        super(message);
    }

    public FileExtException(String message, Exception exp) {
        super(message, exp);
    }
}
