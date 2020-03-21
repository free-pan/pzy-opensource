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

package org.pzy.opensource.comm.util;

import org.pzy.opensource.comm.domain.bo.WeekFirstLastDayBO;
import org.pzy.opensource.comm.domain.enums.MysqlWeekModeEnum;
import org.pzy.opensource.comm.domain.enums.WeekdayEnum;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 与mysql的week函数的模式: 0,1,2,3 对应的java实现
 *
 * @author pan
 * @date 2019-03-22
 */
public class MysqlWeekMethodUtil {

    private static final int MAX_DAY_VAL = 31;
    private static final int MIN_DAY_VAL = 1;

    /**
     * 获取指定年的周范围信息
     *
     * @param year     年
     * @param modeEnum 周模式(与mysql的week函数的mode参数对应)
     * @return year对应的周集合
     */
    public static List<WeekFirstLastDayBO> getRangeDateOfWeekByYear(int year, MysqlWeekModeEnum modeEnum) {
        List<WeekFirstLastDayBO> weekList = new ArrayList<>();

        if (MysqlWeekModeEnum.MODE_ZERO == modeEnum) {
            weekList = calculateByModeZero(year);
        } else if (MysqlWeekModeEnum.MODE_ONE == modeEnum) {
            weekList = calculateByModeOne(year);
        } else if (MysqlWeekModeEnum.MODE_TWO == modeEnum) {
            weekList = calculateByModeTwo(year);
        } else if (MysqlWeekModeEnum.MODE_THREE == modeEnum) {
            weekList = calculateByModeThree(year);
        }
        return weekList;
    }

    /**
     * 使用指定模式算法获取指定年的指定周信息
     *
     * @param year     年(这个是指周年,而不是普通的年)
     * @param week     周
     * @param modeEnum 算法模式
     * @return 找到则返回对应周信息, 未找到则返回null
     */
    public static WeekFirstLastDayBO getWeekInfo(int year, int week, MysqlWeekModeEnum modeEnum) {
        List<WeekFirstLastDayBO> weekList = getRangeDateOfWeekByYear(year, modeEnum);
        for (WeekFirstLastDayBO weekInfo : weekList) {
            if (weekInfo.getWeekOfYear().intValue() == week) {
                return weekInfo;
            }
        }
        return null;
    }

    /**
     * 以模式3,的方式计算,指定年拥有的周列表
     * <br/>
     * 计算第一周: 如果第一周在当前年的天超过3天，那么计算为本年的第一周
     * <br/>
     * 计算最后一周: 本年的最后一个星期天为最后一周的开始日期, 7天之后为结束日期(此时数据可能跨年了)
     *
     * @param year 年
     * @return 以模式3算法, 得出的year拥有的周集合
     */
    private static List<WeekFirstLastDayBO> calculateByModeThree(int year) {
        List<WeekFirstLastDayBO> resultList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        // 获取指定年的一月一号是所属周的第几周
        int dayOfWeek = getanuaryOneDay(year, calendar);
        WeekdayEnum weekdayEnum = WeekdayEnum.val2Enum(dayOfWeek);
        if (null == weekdayEnum) {
            throw new RuntimeException(String.format("日期[%s]获取所在星期数出错,得到的dayOfWeek值为:[%s],在SundayWeekdayEnum中不存在!", year + "-01-01", dayOfWeek));
        }
        List<WeekFirstLastDayBO> otherWeekList;
        if ((dayOfWeek <= WeekdayEnum.THU.getVal()) && (dayOfWeek != WeekdayEnum.SUN.getVal())) {
            // 1月1号至少是星期四,第一周在当前年的天超过3天,那么这周计算为本年的第一周.
            while (WeekdayEnum.MON.getVal() != calendar.get(Calendar.DAY_OF_WEEK)) {
                // 将日期往过去时间推算到最前一次的星期一
                calendar.add(Calendar.DAY_OF_MONTH, -1);
            }
            WeekFirstLastDayBO firstLastDayBO = new WeekFirstLastDayBO();
            firstLastDayBO.setFirstDate(calendar.getTime());
            while (WeekdayEnum.SUN.getVal() != calendar.get(Calendar.DAY_OF_WEEK)) {
                // 将日期往未来时间推算到最近一次的星期天
                calendar.add(Calendar.DAY_OF_MONTH, 1);
            }
            firstLastDayBO.setLastDate(calendar.getTime());
            firstLastDayBO.setYear(year);
            firstLastDayBO.setWeekOfYear(1);
            resultList.add(firstLastDayBO);
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            otherWeekList = computeOtherWeekStop2YearLastMonday(1, year, calendar);
        } else {
            while (WeekdayEnum.SUN.getVal() != calendar.get(Calendar.DAY_OF_WEEK)) {
                // 将日期往未来时间推算到最近一次的星期天
                calendar.add(Calendar.DAY_OF_MONTH, 1);
            }
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            otherWeekList = computeOtherWeekStop2YearLastMonday(0, year, calendar);
        }
        resultList.addAll(otherWeekList);
        return resultList;
    }

    /**
     * 计算剩余其它周
     *
     * @param startWeek 开始周
     * @param year      年
     * @param calendar  日历对象
     */
    private static List<WeekFirstLastDayBO> computeOtherWeekStop2YearLastMonday(int startWeek, int year, Calendar calendar) {
        List<WeekFirstLastDayBO> resultList = new ArrayList<>();
        int week = startWeek;
        int step = 1;
        WeekFirstLastDayBO otherWeek;
        // 计算其它周
        while (true) {
            week++;
            otherWeek = new WeekFirstLastDayBO();
            resultList.add(otherWeek);
            otherWeek.setYear(year);
            otherWeek.setWeekOfYear(week);
            otherWeek.setFirstDate(calendar.getTime());
            int month = 0;
            int dayOfMonth;
            // 最大循环次数
            int maxLoopTimes = 7;
            while (step < maxLoopTimes) {
                month = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                if (month == Calendar.DECEMBER && dayOfMonth >= MAX_DAY_VAL) {
                    break;
                }
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                step++;
            }
            step = 1;
            dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
            otherWeek.setLastDate(calendar.getTime());
            if (month == Calendar.DECEMBER && dayOfMonth >= MAX_DAY_VAL) {
                if (calendar.get(Calendar.DAY_OF_WEEK) != WeekdayEnum.SUN.getVal()) {
                    if (calendar.get(Calendar.DAY_OF_WEEK) >= WeekdayEnum.THU.getVal()) {
                        while (calendar.get(Calendar.DAY_OF_WEEK) != WeekdayEnum.SUN.getVal()) {
                            calendar.add(Calendar.DAY_OF_MONTH, 1);
                        }
                        otherWeek.setLastDate(calendar.getTime());
                    } else {
                        resultList.remove(resultList.size() - 1);
                    }
                }
                break;
            }
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        return resultList;
    }

    /**
     * 以模式2,的方式计算,指定年拥有的周列表
     * <br/>
     * 计算第一周: 本年的第一个星期天开始，是第一周的开始日期.
     * <br/>
     * 计算最后一周: 本年的最后一个星期天为最后一周的开始日期, 7天之后为结束日期(此时数据可能跨年了)
     *
     * @param year 年
     * @return 以模式2算法, 计算出的year的周集合
     */
    private static List<WeekFirstLastDayBO> calculateByModeTwo(int year) {
        List<WeekFirstLastDayBO> resultList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.SUNDAY);
        // 获取指定年的一月一号是所属周的第几周
        int dayOfWeek = getanuaryOneDay(year, calendar);
        WeekdayEnum weekdayEnum = WeekdayEnum.val2Enum(dayOfWeek);
        if (null == weekdayEnum) {
            throw new RuntimeException(String.format("日期[%s]获取所在星期数出错,得到的dayOfWeek值为:[%s],在SundayWeekdayEnum中不存在!", year + "-01-01", dayOfWeek));
        }
        if (weekdayEnum != WeekdayEnum.SUN) {
            // 跳过本月第一个周日之前的日期
            while (calendar.get(Calendar.DAY_OF_WEEK) != WeekdayEnum.SUN.getVal()) {
                calendar.add(Calendar.DAY_OF_MONTH, 1);
            }
        }
        // 模式0,计算剩余其它周
        List<WeekFirstLastDayBO> otherWeekList = computeOtherWeekStop2YearLastSunday(0, year, calendar);
        resultList.addAll(otherWeekList);
        return resultList;
    }

    /**
     * 以模式1,的方式计算,指点年拥有的周列表
     * <br/>
     * 计算第0周: 如果第一周在当前年的天没有超过3天，那么这些属于当前年的日期计算为本年的第0周
     * <br/>
     * 计算第一周: 如果第一周在当前年的天超过3天，那么计算为本年的第一周
     * <br/>
     * 计算最后一周: 以当前年的最后一个周一所在日期为开始日期,以当前年最后一天作为结束日期
     *
     * @param year 年份
     * @return 以模式1算法, 得到的year的周集合
     */
    private static List<WeekFirstLastDayBO> calculateByModeOne(int year) {
        List<WeekFirstLastDayBO> resultList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        // 获取指定年的一月一号是所属周的第几周
        int dayOfWeek = getanuaryOneDay(year, calendar);
        WeekdayEnum weekdayEnum = WeekdayEnum.val2Enum(dayOfWeek);
        if (null == weekdayEnum) {
            throw new RuntimeException(String.format("日期[%s]获取所在星期数出错,得到的dayOfWeek值为:[%s],在SundayWeekdayEnum中不存在!", year + "-01-01", dayOfWeek));
        }
        List<WeekFirstLastDayBO> otherWeekList;
        if (dayOfWeek >= WeekdayEnum.FRI.getVal() || dayOfWeek == WeekdayEnum.SUN.getVal()) {
            // 1月1号至少是星期五,说明第一周在当前年的天未超过3天
            // 说明有第0周
            WeekFirstLastDayBO zeroWeek = computeZeroWeekForOneMode(year, calendar, weekdayEnum.getVal());
            resultList.add(zeroWeek);
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            // 模式1,计算剩余其它周
            otherWeekList = computeOtherWeekStop2YearLastDay(0, year, calendar);
        } else {
            // 计算第一周
            WeekFirstLastDayBO firstWeek = new WeekFirstLastDayBO();
            firstWeek.setWeekOfYear(1);
            firstWeek.setYear(year);
            firstWeek.setFirstDate(calendar.getTime());
            while (calendar.get(Calendar.DAY_OF_WEEK) != WeekdayEnum.SUN.getVal()) {
                calendar.add(Calendar.DAY_OF_MONTH, 1);
            }
            firstWeek.setLastDate(calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            resultList.add(firstWeek);
            // 模式1,计算剩余其它周
            otherWeekList = computeOtherWeekStop2YearLastDay(1, year, calendar);
        }
        resultList.addAll(otherWeekList);
        return resultList;
    }

    /**
     * 模式1,方式计算第0周
     *
     * @param year      年
     * @param calendar  日历
     * @param dayOfWeek 周的第几天
     * @return 第0周的开始日期和结束日期
     */
    private static WeekFirstLastDayBO computeZeroWeekForOneMode(int year, Calendar calendar, int dayOfWeek) {
        WeekFirstLastDayBO zeroWeek = new WeekFirstLastDayBO();
        zeroWeek.setWeekOfYear(0);
        zeroWeek.setYear(year);
        // 获取第一天
        zeroWeek.setFirstDate(calendar.getTime());
        // 计算最后一天
        int index = dayOfWeek;
        if (index != 1) {
            int maxLoopTimes = 7;
            for (; index <= maxLoopTimes; index++) {
                calendar.add(Calendar.DAY_OF_MONTH, 1);
            }
        }
        // 获取最后一天
        zeroWeek.setLastDate(calendar.getTime());
        return zeroWeek;
    }

    /**
     * 以模式0,的方式计算,指定年拥有的周列表
     * <br/>
     * 计算第0周: 本年第一个星期天之前的, 属于本年的日期,为第0周
     * <br/>
     * 计算第1周: 遇到本年的第一个星期天开始，是第一周的开始, 7天之后是第一周的结束
     * <br/>
     * 计算最后1周: 当前年的最后一个星期天为这周的开始天, 当前年的最后一天为这周的结束天
     *
     * @param year 年
     * @return 周信息集合
     */
    private static List<WeekFirstLastDayBO> calculateByModeZero(int year) {
        List<WeekFirstLastDayBO> resultList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.SUNDAY);
        // 获取指定年的一月一号是所属周的第几周
        int dayOfWeek = getanuaryOneDay(year, calendar);
        WeekdayEnum weekdayEnum = WeekdayEnum.val2Enum(dayOfWeek);
        if (null == weekdayEnum) {
            throw new RuntimeException(String.format("日期[%s]获取所在星期数出错,得到的dayOfWeek值为:[%s],在SundayWeekdayEnum中不存在!", year + "-01-01", dayOfWeek));
        }
        if (weekdayEnum != WeekdayEnum.SUN) {
            // 当前不是星期天,说明有第0周
            WeekFirstLastDayBO zeroWeek = computeZeroWeekForZeroMode(year, calendar, weekdayEnum.getVal());
            // 加入第0周
            resultList.add(zeroWeek);
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        // 模式0,计算剩余其它周
        List<WeekFirstLastDayBO> otherWeekList = computeOtherWeekStop2YearLastDay(0, year, calendar);
        resultList.addAll(otherWeekList);

        return resultList;
    }

    /**
     * 计算剩余其它周(内部循环停止于year的最后一个星期天的7天后)
     *
     * @param startWeek 开始周
     * @param year      年
     * @param calendar  日历对象
     */
    private static List<WeekFirstLastDayBO> computeOtherWeekStop2YearLastSunday(int startWeek, int year, Calendar calendar) {
        List<WeekFirstLastDayBO> resultList = new ArrayList<>();
        int week = startWeek;
        int step = 1;
        WeekFirstLastDayBO otherWeek;
        // 计算其它周
        while (true) {
            week++;
            otherWeek = new WeekFirstLastDayBO();
            resultList.add(otherWeek);
            otherWeek.setYear(year);
            otherWeek.setWeekOfYear(week);
            otherWeek.setFirstDate(calendar.getTime());
            int month = 0;
            int dayOfMonth;
            int maxLoopTimes = 7;
            while (step < maxLoopTimes) {
                month = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                if (month == Calendar.DECEMBER && dayOfMonth >= MAX_DAY_VAL) {
                    break;
                }
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                step++;
            }
            step = 1;
            otherWeek.setLastDate(calendar.getTime());
            dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
            if (month == Calendar.DECEMBER && dayOfMonth >= MAX_DAY_VAL) {
                if (calendar.get(Calendar.DAY_OF_WEEK) != WeekdayEnum.SAT.getVal()) {
                    while (calendar.get(Calendar.DAY_OF_WEEK) != WeekdayEnum.SAT.getVal()) {
                        calendar.add(Calendar.DAY_OF_MONTH, 1);
                    }
                    otherWeek.setLastDate(calendar.getTime());
                }
                break;
            }
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        return resultList;
    }

    /**
     * 计算剩余其它周(内部循环停止于年的最后一天)
     *
     * @param startWeek 开始周
     * @param year      年
     * @param calendar  日历对象
     */
    private static List<WeekFirstLastDayBO> computeOtherWeekStop2YearLastDay(int startWeek, int year, Calendar calendar) {
        List<WeekFirstLastDayBO> resultList = new ArrayList<>();
        int week = startWeek;
        int step = 1;
        WeekFirstLastDayBO otherWeek;
        // 计算其它周
        while (true) {
            week++;
            otherWeek = new WeekFirstLastDayBO();
            resultList.add(otherWeek);
            otherWeek.setYear(year);
            otherWeek.setWeekOfYear(week);
            otherWeek.setFirstDate(calendar.getTime());
            int month = 0;
            int dayOfMonth = 0;
            int maxLoopTimes = 7;
            while (step < maxLoopTimes) {
                month = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                if (month == Calendar.DECEMBER && dayOfMonth >= MAX_DAY_VAL) {
                    break;
                }
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                step++;
            }
            step = 1;
            otherWeek.setLastDate(calendar.getTime());
            month = calendar.get(Calendar.MONTH);
            dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
            if (month == Calendar.DECEMBER && dayOfMonth >= MAX_DAY_VAL) {
                break;
            }
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        return resultList;
    }

    /**
     * 模式0:计算第0周
     *
     * @param year      年
     * @param calendar  日历对象
     * @param dayOfWeek 周的第几天
     * @return 模式0方式, 计算出的第0周的开始/结束日期
     */
    private static WeekFirstLastDayBO computeZeroWeekForZeroMode(int year, Calendar calendar, int dayOfWeek) {
        WeekFirstLastDayBO zeroWeek = new WeekFirstLastDayBO();
        zeroWeek.setWeekOfYear(0);
        zeroWeek.setYear(year);
        // 获取第一天
        zeroWeek.setFirstDate(calendar.getTime());
        // 计算最后一天
        int maxLoopTimes = 7;
        for (int index = dayOfWeek; index < maxLoopTimes; index++) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        // 获取最后一天
        zeroWeek.setLastDate(calendar.getTime());
        return zeroWeek;
    }

    /**
     * 获取指定年的一月一号是所属周的第几天
     *
     * @param year     年
     * @param calendar 日历对象
     * @return 日历对象代指日期处于所在周的第几天
     */
    private static int getanuaryOneDay(int year, Calendar calendar) {
        // 获取year的1月1号
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        // 获取1月1日,是周的第几天
        return calendar.get(Calendar.DAY_OF_WEEK);
    }
}
