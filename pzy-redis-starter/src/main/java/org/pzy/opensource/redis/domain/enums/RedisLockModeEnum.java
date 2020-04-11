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

package org.pzy.opensource.redis.domain.enums;

import org.pzy.opensource.domain.entity.BaseEnum;

/**
 * redis锁模式
 *
 * @author pan
 * @date 2019-04-05
 */
public enum RedisLockModeEnum implements BaseEnum<String> {
    /**
     * 共享锁
     */
    Share("共享锁"),
    /**
     * 互斥锁
     */
    Exclusive("共享锁");

    private String code;

    RedisLockModeEnum(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public void setCode(String code) {
        this.code = code;
    }
}
