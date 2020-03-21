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

package org.pzy.opensource.comm.domain.bo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.pzy.opensource.domain.GlobalConstant;

import java.io.Serializable;
import java.util.Date;

/**
 * 存储月的开始日期和结束日期
 *
 * @author pzy
 * @date 2019/1/1
 */
@Setter
@Getter
@ToString
public class MonthStartEndDayBO implements Serializable {

    private static final long serialVersionUID = 4667416639883221399L;
    /**
     * 年
     */
    private Integer year;
    /**
     * 月
     */
    private Integer month;
    /**
     * 第一天
     */
    @JSONField(format = GlobalConstant.DATE_PATTERN)
    private Date startDate;
    /**
     * 最后一天
     */
    @JSONField(format = GlobalConstant.DATE_PATTERN)
    private Date endDate;
}
