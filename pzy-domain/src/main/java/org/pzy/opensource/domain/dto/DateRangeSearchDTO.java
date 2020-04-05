package org.pzy.opensource.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.pzy.opensource.domain.GlobalConstant;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;

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
    private LocalDate beginDate;

    @ApiModelProperty(value = "结束日期. 格式:yyyy-MM-dd")
    @JsonFormat(pattern = GlobalConstant.DATE_PATTERN)
    @DateTimeFormat(pattern = GlobalConstant.DATE_PATTERN)
    private LocalDate endDate;

    /**
     * <p>目标查询字段是否为datetime类型(该字段的值建议由后端自动填充). 默认为true
     * <p>如果值为true, 则会将beginDate和endDate转换为. yyyy-MM-dd 00:00:00, yyyy-MM-dd 23:59:59 格式
     * <p>如果值为false, 则会将beginDate和endDate转换为. yyyy-MM-dd, yyyy-MM-dd 格式
     */
    @ApiModelProperty(hidden = true)
    private Boolean targetFieldIsDatetime;

    public DateRangeSearchDTO() {
        targetFieldIsDatetime = true;
    }
}
