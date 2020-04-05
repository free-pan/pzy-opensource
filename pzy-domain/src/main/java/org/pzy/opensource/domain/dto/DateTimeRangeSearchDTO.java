package org.pzy.opensource.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.pzy.opensource.domain.GlobalConstant;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 查询条件:日期时间范围查询条件
 *
 * @author pan
 * @date 2020/4/4 15:38
 */
@Data
public class DateTimeRangeSearchDTO implements Serializable {

    @ApiModelProperty(value = "开始日期时间. 格式:yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = GlobalConstant.DATE_TIME_PATTERN_B)
    @DateTimeFormat(pattern = GlobalConstant.DATE_TIME_PATTERN_B)
    private LocalDateTime beginDateTime;

    @ApiModelProperty(value = "结束日期时间. 格式:yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = GlobalConstant.DATE_TIME_PATTERN_B)
    @DateTimeFormat(pattern = GlobalConstant.DATE_TIME_PATTERN_B)
    private LocalDateTime endDateTime;
}
