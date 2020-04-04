/**
 * Copyright (C): 恒大集团版权所有 Evergrande Group
 */
package org.pzy.opensource.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 字段排序
 *
 * @author pan
 * @date 2020/4/4 15:46
 */
@Data
public class FieldSortDTO implements Serializable {

    @ApiModelProperty("排序字段")
    private String field;

    @ApiModelProperty("是否升序")
    private Boolean asc;
}
