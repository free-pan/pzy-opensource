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

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 远程服务异常
 *
 * @author pan
 * @date 2019-03-22
 */
@Setter
@Getter
@ToString
public class RemoteServerException extends AbstractBusinessException {

    private static final long serialVersionUID = 9165797361408053651L;
    /**
     * 远程服务标识
     */
    private String remoteService;

    public RemoteServerException(String message) {
        super(message);
    }

    public RemoteServerException(String message, Exception exp) {
        super(message, exp);
    }
}
