package org.pzy.opensource.comm.util;

import org.apache.commons.lang3.time.FastDateFormat;
import org.pzy.opensource.domain.dto.DateRangeSearchDTO;
import org.pzy.opensource.domain.dto.DateTimeRangeSearchDTO;
import org.pzy.opensource.domain.enums.LocalDatePatternEnum;
import org.pzy.opensource.domain.enums.LocalDateTimePatternEnum;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * DateUtil
 *
 * @author pan
 * @date 2020/4/5 17:04
 */
public class DateUtil {

    private DateUtil() {
    }

    /**
     * 日期格式化
     *
     * @param date    日期
     * @param pattern 格式化模板
     * @return 格式化之后的字符串
     */
    public static String format(Date date, LocalDateTimePatternEnum pattern) {
        return FastDateFormat.getInstance(pattern.getPattern()).format(date);
    }

    /**
     * LocalDate格式化
     *
     * @param localDate 日期
     * @param pattern   格式化模板
     * @return 格式化之后的字符串
     */
    public static String format(LocalDate localDate, LocalDatePatternEnum pattern) {
        return localDate.format(DateTimeFormatter.ofPattern(pattern.getPattern()));
    }

    /**
     * LocalDateTime格式化
     *
     * @param localDateTime 日期
     * @param pattern       格式化模板
     * @return 格式化之后的字符串
     */
    public static String format(LocalDateTime localDateTime, LocalDateTimePatternEnum pattern) {
        return localDateTime.format(DateTimeFormatter.ofPattern(pattern.getPattern()));
    }

    /**
     * 交换beginDate和endDate的值, 如果beginDate的时间戳小于endDate的时间戳
     *
     * @param dateRangeSearchDTO 待转换数据
     * @return 转换之后的结果
     */
    public static DateRangeSearchDTO switchDateRangeSearchIfNecessary(DateRangeSearchDTO dateRangeSearchDTO) {
        LocalDate beginDate = dateRangeSearchDTO.getBeginDate();
        LocalDate endDate = dateRangeSearchDTO.getEndDate();
        if (beginDate != null && endDate != null) {
            LocalDate fromDate = null;
            LocalDate toDate = null;
            if (beginDate.isAfter(endDate)) {
                fromDate = endDate;
                toDate = beginDate;
            } else {
                fromDate = beginDate;
                toDate = endDate;
            }
            dateRangeSearchDTO.setBeginDate(fromDate);
            dateRangeSearchDTO.setEndDate(toDate);
        }
        return dateRangeSearchDTO;
    }

    /**
     * 交换beginDateTime和endDateTime的值, 如果beginDateTime的时间戳小于endDateTime的时间戳
     *
     * @param dateRangeSearchDTO 待转换数据
     * @return 转换之后的结果
     */
    public static DateTimeRangeSearchDTO switchDateTimeRangeSearchIfNecessary(DateTimeRangeSearchDTO dateRangeSearchDTO) {
        LocalDateTime beginDateTime = dateRangeSearchDTO.getBeginDateTime();
        LocalDateTime endDateTime = dateRangeSearchDTO.getEndDateTime();
        if (beginDateTime != null && endDateTime != null) {
            LocalDateTime fromDateTime = null;
            LocalDateTime toDateTime = null;
            if (beginDateTime.isAfter(endDateTime)) {
                fromDateTime = endDateTime;
                toDateTime = beginDateTime;
            } else {
                fromDateTime = beginDateTime;
                toDateTime = endDateTime;
            }
            dateRangeSearchDTO.setBeginDateTime(fromDateTime);
            dateRangeSearchDTO.setEndDateTime(toDateTime);
        }
        return dateRangeSearchDTO;
    }
}
