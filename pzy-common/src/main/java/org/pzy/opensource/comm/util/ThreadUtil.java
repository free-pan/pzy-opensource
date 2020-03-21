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

package org.pzy.opensource.comm.util;

/**
 * @author pan
 * @date 2020-01-24
 */
public class ThreadUtil {
    private ThreadUtil() {
    }

    public static Thread getCurrentTread() {
        return Thread.currentThread();
    }

    public static String getCurrentThreadInfo() {
        Thread thread = getCurrentTread();
        return String.format("线程:%s-%s", thread.getName(), thread.getId());
    }
}
