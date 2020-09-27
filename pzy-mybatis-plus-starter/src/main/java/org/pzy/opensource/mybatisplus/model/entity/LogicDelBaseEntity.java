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

package org.pzy.opensource.mybatisplus.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 基础实体. 包含公共字段: invalid_time, invalid_operator_id, invalid_name, invalid
 * @author pan
 * @date 2019-12-11
 */
@Data
public class LogicDelBaseEntity extends BaseEntity {

    private static final long serialVersionUID = 1272777735771040750L;

    /**
     * 作废时间
     */
    public static final String INVALID_TIME = "invalid_time";
    /**
     * 执行作废操作的操作人id
     */
    public static final String INVALID_OPERATOR_ID = "invalid_operator_id";
    /**
     * 执行作废操作的操作人姓名
     */
    public static final String INVALID_NAME = "invalid_name";
    /**
     * 数据作废或有效. 0.作废 1.有效
     */
    public static final String INVALID = "invalid";

    /**
     * 是否禁用. 0.已禁用 1.未禁用
     */
    @TableLogic
    @TableField(value = INVALID)
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    private Short invalid;

    /**
     * 执行作废操作的操作时间
     */
    @TableField(value = INVALID_TIME)
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    private LocalDateTime invalidTime;

    /**
     * 执行作废操作的操作人id
     */
    @TableField(value = INVALID_OPERATOR_ID)
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    private Long invalidOperatorId;

    /**
     * 执行作废操作的操作人姓名
     */
    @TableField(value = INVALID_NAME)
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    private String invalidName;
}
