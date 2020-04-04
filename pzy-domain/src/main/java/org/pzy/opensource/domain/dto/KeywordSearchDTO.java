/**
 * Copyright (C): 恒大集团版权所有 Evergrande Group
 */
package org.pzy.opensource.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 查询条件:关键词查询条件
 *
 * @author pan
 * @date 2020/4/4 15:38
 */
@Data
public class KeywordSearchDTO implements Serializable {

    @ApiModelProperty(value = "查询关键词")
    private String kw;
}
