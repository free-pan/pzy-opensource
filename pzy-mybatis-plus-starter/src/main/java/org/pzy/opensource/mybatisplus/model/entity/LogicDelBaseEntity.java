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

import java.util.Date;

/**
 * @author pan
 * @date 2019-12-11
 */
@Data
public class LogicDelBaseEntity extends BaseEntity {

    private static final long serialVersionUID = 1272777735771040750L;

    public static final String DEL_UID = "del_uid";
    public static final String DEL_TIME = "del_time";
    /**
     * 删除人id
     */
    public static final String DELETOR_ID = "deletor_id";
    /**
     * 删除人姓名
     */
    public static final String DELETOR_NAME = "deletor_name";
    /**
     * 逻辑删除标识. 0.未删除 1.已删除
     */
    public static final String DELETED = "deleted";

    /**
     * 删除uid. 当删除时,则将delUid设置一个uuid值,主要是用于有唯一约束的情况
     */
    @TableField(value = DEL_UID)
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    private String delUid;

    @TableLogic
    @TableField(value = DELETED)
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    private Short deleted;

    /**
     * 删除时间
     */
    @TableField(value = DEL_TIME)
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    private Date delTime;

    /**
     * 删除人id
     */
    @TableField(value = DELETOR_ID)
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    private Long deletorId;

    /**
     * 删除人姓名
     */
    @TableField(value = DELETOR_NAME)
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    private String deletorName;
}
