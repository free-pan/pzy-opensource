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

package org.pzy.opensource.dbutil.domain.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 列信息
 *
 * @author pzy
 * @date 2018/11/4
 */
@Setter
@Getter
@ToString
public class ColumnInfoBO {
    /**
     * 列名
     */
    @ApiModelProperty("列名")
    private String name;
    /**
     * 是否允许为空. 默认:false
     */
    @ApiModelProperty("是否允许为空. 默认:false")
    private Boolean nullAble;
    /**
     * 数据库类型
     */
    @ApiModelProperty("数据库类型")
    private String dbDataType;
    /**
     * java数据类型
     */
    @ApiModelProperty("java数据类型")
    private String javaType;
    /**
     * java数据类型
     */
    @ApiModelProperty("js数据类型")
    private String jsType;
    /**
     * 备注信息
     */
    @ApiModelProperty("备注信息")
    private String comment;
    /**
     * 列的指定列大小.
     * <br/>
     * 对于数值数据，这是最大精度。对于字符数据，这是字符长度。对于日期时间数据类型，这是 String 表示形式的字符长度.
     * 于二进制数据，这是字节长度。对于 ROWID 数据类型，这是字节长度。对于列大小不适用的数据类型，则返回 Null
     */
    @ApiModelProperty("列的指定列大小.")
    private Integer columnSize;
    /**
     * 默认值
     */
    @ApiModelProperty("默认值")
    private String defaultVal;

    public ColumnInfoBO() {
        this.nullAble = false;
    }
}
