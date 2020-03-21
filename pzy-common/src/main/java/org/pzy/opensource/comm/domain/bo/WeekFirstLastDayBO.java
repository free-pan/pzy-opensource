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
 * 存储周的开始日期和结束日期
 *
 * @author pzy
 * @date 2019/1/1
 */
@Setter
@Getter
@ToString
public class WeekFirstLastDayBO implements Serializable {

    private static final long serialVersionUID = -798303160788706133L;
    /**
     * 年
     */
    private Integer year;
    /**
     * 年的第几周
     */
    private Integer weekOfYear;
    /**
     * 第一天
     */
    @JSONField(format = GlobalConstant.DATE_PATTERN)
    private Date firstDate;
    /**
     * 最后一天
     */
    @JSONField(format = GlobalConstant.DATE_PATTERN)
    private Date lastDate;
}
