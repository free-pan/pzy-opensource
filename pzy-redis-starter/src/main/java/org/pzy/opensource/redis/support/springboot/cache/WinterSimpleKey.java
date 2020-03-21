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

package org.pzy.opensource.redis.support.springboot.cache;

import lombok.Getter;
import lombok.Setter;
import org.springframework.cache.interceptor.SimpleKey;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.Arrays;

/**
 * @author pan
 * @date 2019-07-17
 */
@Setter
@Getter
public class WinterSimpleKey implements Serializable {

    private String prefix;

    /**
     * An empty key.
     */
    public static final SimpleKey EMPTY = new SimpleKey();


    private final Object[] params;

    private final int hashCode;

    public WinterSimpleKey(String prefix, Object... elements) {
        Assert.notNull(elements, "Elements must not be null");
        this.params = new Object[elements.length];
        System.arraycopy(elements, 0, this.params, 0, elements.length);

        // 计算hashcode
        int tmpHashCode = Arrays.deepHashCode(this.params);
        this.hashCode = 31 * tmpHashCode + prefix.hashCode();

        this.prefix = prefix;
    }

    @Override
    public boolean equals(Object other) {
        boolean superEq = this.eq(other);
        if (superEq && (other instanceof WinterSimpleKey)) {
            WinterSimpleKey tmp = (WinterSimpleKey) other;
            return prefix.equals(tmp.getPrefix());
        }
        return superEq;
    }

    private boolean eq(Object other) {
        return (this == other ||
                (other instanceof WinterSimpleKey && Arrays.deepEquals(this.params, ((WinterSimpleKey) other).params)));
    }

    @Override
    public int hashCode() {
        return this.hashCode;
    }

    @Override
    public String toString() {
        return this.prefix + getClass().getSimpleName() + " [" + StringUtils.arrayToCommaDelimitedString(this.params) + "]";
    }
}
