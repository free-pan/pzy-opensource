package org.pzy.opensource.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 查询条件:包含查询关键词和日期时间范围查询条件
 *
 * @author pan
 * @date 2020/4/4 15:41
 */
@Data
public class KeywordDateRangeSearchDTO extends DateRangeSearchDTO {

    @ApiModelProperty(value = "查询关键词")
    private String kw;
}
