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

package org.pzy.opensource.comm.domain.enums;

import org.pzy.opensource.domain.entity.BaseEnum;

/**
 * mysql的week函数对应的mode值
 * @author pzy
 * @date 2019/1/2
 */
public enum MysqlWeekModeEnum implements BaseEnum<Integer> {

    /**
     * 每周第一天是星期天.周取值范围[0,53].
     * <br>
     * 对应mysql函数:`week(now(),0)`
     * <br>
     * 遇到本年的第一个星期天开始，是第一周,开始日期。前面的属于本年的天计算为第0周。
     * <br>
     * 优点: 按周统计时,数据不会跨年
     * <br>
     * 缺点: 按周统计时,是多出一个第0周, 并且, 有些年份可能没有第0周
     */
    MODE_ZERO(0),
    /**
     * 每周第一天是星期一.周取值范围[0,53]
     * <br>
     * 对应mysql函数:`week(now(),1)`
     * <br>
     * 如果第一周在当前年的天超过3天，那么计算为本年的第一周。否则为第0周
     * <br>
     * 优点: 按周统计时,数据不会跨年
     * <br>
     * 缺点: 按周统计时,是多出一个第0周, 并且, 有些年份可能没有第0周
     */
    MODE_ONE(1),
    /**
     * 每周第一天是星期天.周取值范围[1,53]
     * <br>
     * 对应mysql函数:`week(now(),2)`
     * <br>
     * 遇到本年的第一个星期天开始，是第一周。前面的计算为上年度的最后一周
     * <br>
     * 优点: 按周统计, 没有第0周了
     * <br>
     * 缺点: 按周统计, 数据会跨年. (最后一周的数据可能会跨年)
     */
    MODE_TWO(2),
    /**
     * 每周第一天是星期一.周取值范围[1,53]
     * <br>
     * 对应mysql函数:`week(now(),3)`
     * <br>
     * 如果第一周在当前年的天超过3天，那么计算为本年的第一周。否则为上年度的最后一周
     * <br>
     * 优点: 按周统计, 没有第0周了
     * <br>
     * 缺点: 按周统计, 数据会跨年. (第一周的数据可能会跨年)
     */
    MODE_THREE(3);

    /**
     * 模式值
     */
    private Integer code;

    MysqlWeekModeEnum(Integer code) {
        this.code = code;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public void setCode(Integer code) {
        this.code = code;
    }
}
