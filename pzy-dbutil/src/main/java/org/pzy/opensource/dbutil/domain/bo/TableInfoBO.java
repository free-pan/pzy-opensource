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

import java.util.List;

/**
 * 数据库表信息
 *
 * @author pzy
 * @date 2018/11/4
 */
@Setter
@Getter
@ToString
public class TableInfoBO {

    /**
     * 表名
     */
    @ApiModelProperty("表名")
    private String name;
    /**
     * 表类型
     */
    @ApiModelProperty("表类型")
    private String type;
    /**
     * 表备注信息
     */
    @ApiModelProperty("表备注信息")
    private String comment;
    /**
     * 列信息.
     * key为列名,value为列详细信息
     */
    @ApiModelProperty("列信息")
    private List<ColumnInfoBO> columnList;

}
