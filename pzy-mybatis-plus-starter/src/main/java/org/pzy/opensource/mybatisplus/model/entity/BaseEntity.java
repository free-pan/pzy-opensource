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

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.pzy.opensource.domain.GlobalConstant;

import java.util.Date;

/**
 * 基础实体: 包含公共字段 id, create_time, edit_time, creator_id, editor_id, creator_name, editor_name
 * @author pan
 * @date 2019-12-11
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseEntity extends SimpleBaseEntity {

    private static final long serialVersionUID = 3714369808680042179L;

    public static final String CREATE_TIME = "create_time";
    public static final String EDIT_TIME = "edit_time";
    public static final String CREATOR_ID = "creator_id";
    public static final String EDITOR_ID = "editor_id";
    public static final String CREATOR_NAME = "creator_name";
    public static final String EDITOR_NAME = "editor_name";

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE, value = CREATE_TIME)
    @ApiModelProperty("创建时间. 格式:yyyy-MM-dd HH:mm:ss")
    @JSONField(format = GlobalConstant.DATE_TIME_PATTERN)
    @JsonFormat(pattern = GlobalConstant.DATE_TIME_PATTERN)
    private Date createTime;

    /**
     * 编辑时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE, value = EDIT_TIME)
    @ApiModelProperty("编辑时间. 格式:yyyy-MM-dd HH:mm:ss")
    @JSONField(format = GlobalConstant.DATE_TIME_PATTERN)
    @JsonFormat(pattern = GlobalConstant.DATE_TIME_PATTERN)
    private Date editTime;

    /**
     * 创建人id
     */
    @TableField(fill = FieldFill.INSERT_UPDATE, value = CREATOR_ID)
    @ApiModelProperty("创建人id")
    private Long creatorId;

    /**
     * 编辑人id
     */
    @TableField(fill = FieldFill.INSERT_UPDATE, value = EDITOR_ID)
    @ApiModelProperty("编辑人id")
    private Long editorId;

    /**
     * 创建人姓名
     */
    @TableField(fill = FieldFill.INSERT_UPDATE, value = CREATOR_NAME)
    @ApiModelProperty("创建人姓名")
    private String creatorName;

    /**
     * 编辑人姓名
     */
    @TableField(fill = FieldFill.INSERT_UPDATE, value = EDITOR_NAME)
    @ApiModelProperty("编辑人姓名")
    private String editorName;

}
