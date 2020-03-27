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

package org.pzy.opensource.comm.mapstruct;

/**
 * 自定义基础类型映射.
 * <p>
 * 用法示例:@Mapper(uses=StringDataMapper.class)
 *
 * @author pan
 * @date 2018/9/14
 **/
public class StringDataMapper {

    /**
     * 如果源字符串对象不为null,则去前后空格
     *
     * @param source 源字符串
     * @return 如果source为null, 则返回空字符串, 否则返回, 去掉了前后空格的字符串
     */
    public String stringMapper(String source) {
        return null != source ? source.trim() : "";
    }

}
