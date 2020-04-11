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
 * 与calendar.get(Calendar.DAY_OF_WEEK)返回值代表的星期N
 *
 * @author pzy
 * @date 2019/1/2
 */
public enum WeekdayEnum implements BaseEnum<Integer> {

    /**
     * 星期天
     */
    SUN(1, "星期天"),
    /**
     * 星期一
     */
    MON(2, "星期一"),
    /**
     * 星期二
     */
    TUE(3, "星期二"),
    /**
     * 星期三
     */
    WED(4, "星期三"),
    /**
     * 星期四
     */
    THU(5, "星期四"),
    /**
     * 星期五
     */
    FRI(6, "星期五"),
    /**
     * 星期六
     */
    SAT(7, "星期六");

    /**
     * 与calendar.get(Calendar.DAY_OF_WEEK)返回的值对应
     */
    private Integer code;
    /**
     * 文本描述
     */
    private String text;

    WeekdayEnum(Integer code, String text) {
        this.code = code;
        this.text = text;
    }

    public static WeekdayEnum val2Enum(int code) {
        for (WeekdayEnum e : WeekdayEnum.values()) {
            if (e.code == code) {
                return e;
            }
        }
        return null;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public void setCode(Integer code) {
        this.code = code;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
