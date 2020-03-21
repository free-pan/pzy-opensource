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
 * @author pan
 * @date 2019-04-11
 */
public class LoadDbConnectionInfoException extends AbstractBusinessException {

    private static final long serialVersionUID = 693169222148929140L;

    public LoadDbConnectionInfoException(String message) {
        super(message);
    }
}
