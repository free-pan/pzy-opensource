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

/**
 * 与calendar.get(Calendar.DAY_OF_WEEK)返回值代表的星期N
 *
 * @author pzy
 * @date 2019/1/2
 */
public enum WeekdayEnum {

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
    private int val;
    /**
     * 文本描述
     */
    private String text;

    WeekdayEnum(int val, String text) {
        this.val = val;
        this.text = text;
    }

    public static WeekdayEnum val2Enum(int val) {
        if (val == WeekdayEnum.SUN.val) {
            return WeekdayEnum.SUN;
        } else if (val == WeekdayEnum.MON.val) {
            return WeekdayEnum.MON;
        } else if (val == WeekdayEnum.TUE.val) {
            return WeekdayEnum.TUE;
        } else if (val == WeekdayEnum.WED.val) {
            return WeekdayEnum.WED;
        } else if (val == WeekdayEnum.THU.val) {
            return WeekdayEnum.THU;
        } else if (val == WeekdayEnum.FRI.val) {
            return WeekdayEnum.FRI;
        } else if (val == WeekdayEnum.SAT.val) {
            return WeekdayEnum.SAT;
        }
        return null;
    }

    public int getVal() {
        return val;
    }

    public void setVal(int val) {
        this.val = val;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
