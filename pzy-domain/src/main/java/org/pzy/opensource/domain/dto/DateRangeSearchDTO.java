/**
 * Copyright (C): 恒大集团版权所有 Evergrande Group
 */
package org.pzy.opensource.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.pzy.opensource.domain.GlobalConstant;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 查询条件:日期范围查询条件
 *
 * @author pan
 * @date 2020/4/4 15:38
 */
@Data
public class DateRangeSearchDTO implements Serializable {

    @ApiModelProperty(value = "开始日期. 格式:yyyy-MM-dd")
    @JsonFormat(pattern = GlobalConstant.DATE_PATTERN)
    @DateTimeFormat(pattern = GlobalConstant.DATE_PATTERN)
    private Date beginDate;

    @ApiModelProperty(value = "结束日期. 格式:yyyy-MM-dd")
    @JsonFormat(pattern = GlobalConstant.DATE_PATTERN)
    @DateTimeFormat(pattern = GlobalConstant.DATE_PATTERN)
    private Date endDate;
}
