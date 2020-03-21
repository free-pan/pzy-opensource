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

import lombok.*;

import java.util.Date;

/**
 * 两个日期的比较结果. 将小日期放入dateMin中, 将大日期放入dateMax中
 *
 * @author pan
 * @date 2019-03-22
 */
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class DateCompareResultBO {

    /**
     * 存储较小的日期
     */
    private Date dateMin;
    /**
     * 存储较大的日期
     */
    private Date dateMax;

}
