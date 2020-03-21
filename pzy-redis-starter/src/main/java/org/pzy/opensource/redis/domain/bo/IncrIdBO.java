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

package org.pzy.opensource.redis.domain.bo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author 潘志勇
 * @date 2019-02-03
 */
@Setter
@Getter
@ToString
public class IncrIdBO {

    /**
     * 生成该id所使用的key
     */
    private String key;
    /**
     * 最新id
     */
    private Long id;
}
