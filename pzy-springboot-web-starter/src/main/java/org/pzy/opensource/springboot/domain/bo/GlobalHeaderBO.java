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

package org.pzy.opensource.springboot.domain.bo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author 潘志勇
 * @date 2019-02-08
 */
@Setter
@Getter
@ToString
public class GlobalHeaderBO implements Serializable {

    private static final long serialVersionUID = 9098101989798662636L;

    /**
     * 参数名. 如:name
     */
    private String paramName;
    /**
     * 参数类型. 如: string, integer(int32)
     */
    private String paramType;
    /**
     * 是否必须. 默认:false
     */
    private boolean must;
    /**
     * 参数说明. 如: 姓名
     */
    private String note;
}
