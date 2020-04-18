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

package org.pzy.opensource.mybatisplus.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.pzy.opensource.comm.util.DateUtil;
import org.pzy.opensource.comm.util.MySqlUtil;
import org.pzy.opensource.currentuser.ThreadCurrentUser;
import org.pzy.opensource.domain.GlobalConstant;
import org.pzy.opensource.domain.PageT;
import org.pzy.opensource.domain.dto.DateRangeSearchDTO;
import org.pzy.opensource.domain.dto.DateTimeRangeSearchDTO;
import org.pzy.opensource.domain.entity.BaseEnum;
import org.pzy.opensource.domain.enums.LocalDatePatternEnum;
import org.pzy.opensource.domain.enums.LocalDateTimePatternEnum;
import org.pzy.opensource.domain.vo.PageVO;
import org.pzy.opensource.mybatisplus.model.entity.BaseEntity;
import org.pzy.opensource.mybatisplus.model.entity.LogicDelBaseEntity;
import org.pzy.opensource.mybatisplus.util.MybatisPlusUtil;
import org.pzy.opensource.mybatisplus.util.PageUtil;
import org.pzy.opensource.mybatisplus.util.SpringUtil;
import org.springframework.context.ApplicationEvent;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 服务模板实现
 *
 * @author pan
 * @date 2019-12-12
 */
@Getter
@Slf4j
public abstract class ServiceTemplate<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> implements IService<T> {

    /**
     * 清除当前服务的查询缓存
     */
    public abstract void clearCache();

    /**
     * 计算hash表的实际大小
     *
     * @param elementCount hash表中实际要存放的元素个数
     * @param loadFactor   加载因子[可不指定].默认:0.75
     * @return
     */
    protected int computeHashSize(int elementCount, Float loadFactor) {
        return MybatisPlusUtil.computeHashSize(elementCount, loadFactor);
    }

    /**
     * 查询关键词特殊字符转义
     *
     * @param kw 原始的查询关键词
     * @return 转义之后的查询关键词
     */
    public String keywordEscape(String kw) {
        return MySqlUtil.escape(kw);
    }

    /**
     * 构建用于mybatis plus的作为查询条件构建的map
     *
     * @param column 表的字段名称
     * @param value  查询值(会自动去除空格)
     * @return 一个全新的map
     */
    public Map<String, Object> buildMybatisPlusQueryMap(String column, String value) {
        Map<String, Object> mybatisPlusQueryMap = new HashMap<>(5);
        mybatisPlusQueryMap.put(column, value.trim());
        return mybatisPlusQueryMap;
    }

    /**
     * 如果当前上线文存在事务则在当前事务提交完毕之后进行事件发布, 否则立刻进行事件发布
     * <p>事件发布/监听机制会将一些次要逻辑从主逻辑中抽取出来. 通常会结合异步任务使用.
     * 但此时要注意如果主逻辑中的事务如果提交比较慢, 可能会导致异步任务中, 无法从数据库中获取到主事务中的数据的问题. 该方法解决了这个问题
     * <p>详细参考: https://www.jyoryo.com/archives/155.html
     *
     * @param applicationEvent
     */
    public void publishEventOnAfterCommitIfNecessary(ApplicationEvent applicationEvent) {
        SpringUtil.publishEventOnAfterCommitIfNecessary(applicationEvent);
    }

    /**
     * 填充创建人信息
     *
     * @param baseEntity 基础实体
     */
    public void fitCreatorInfo(BaseEntity baseEntity) {
        LocalDateTime now = LocalDateTime.now();
        Long operatorId = getOperatorId();
        String operatorName = getOperatorName();
        baseEntity.setCreateTime(now);
        baseEntity.setCreatorId(operatorId);
        baseEntity.setCreatorName(operatorName);
        baseEntity.setEditorId(operatorId);
        baseEntity.setEditorName(operatorName);
        baseEntity.setEditTime(now);
    }

    /**
     * 填充编辑人信息
     *
     * @param baseEntity 基础实体
     */
    public void fitEditorInfo(BaseEntity baseEntity) {
        LocalDateTime now = LocalDateTime.now();
        Long operatorId = getOperatorId();
        String operatorName = getOperatorName();
        baseEntity.setEditorId(operatorId);
        baseEntity.setEditorName(operatorName);
        baseEntity.setEditTime(now);
    }

    /**
     * 填充删除人信息
     *
     * @param logicDelBaseEntity 逻辑删除实体
     */
    public void fitDeletorInfo(LogicDelBaseEntity logicDelBaseEntity) {
        LocalDateTime now = LocalDateTime.now();
        Long operatorId = getOperatorId();
        String operatorName = getOperatorName();
        logicDelBaseEntity.setDisabledOptId(operatorId);
        logicDelBaseEntity.setDisabledOptName(operatorName);
        logicDelBaseEntity.setDisabledTime(now);
    }

    /**
     * 获取操作人姓名
     *
     * @return 操作人姓名
     */
    public String getOperatorName() {
        return ThreadCurrentUser.getRealName(GlobalConstant.EMPTY_STRING);
    }

    /**
     * 获取操作人id
     *
     * @return 操作人id
     */
    public Long getOperatorId() {
        return ThreadCurrentUser.getUserId(0L);
    }

    /**
     * 获取当前bean在spring容器中的代理对象
     *
     * @return 当前bean在spring容器中的代理对象
     */
    public Object getCurrentBeanProxy() {
        return SpringUtil.getBean(this.getClass());
    }

    /**
     * 将系统的分页对象转换为mybatis plus的分页参数
     *
     * @param page 系统的分页条件
     * @return mybatis plus的分页条件
     */
    public IPage<T> toMybatisPlusPage(PageVO page) {
        return PageUtil.pageVO2MybatisPlusPage(page);
    }

    /**
     * 将mybatis plus的分页结果, 转换为系统的分页结果
     *
     * @param mybatisPlusPageResult mybatis plus的分页结果
     * @return 系统的分页结果
     */
    public PageT<T> toPageT(IPage<T> mybatisPlusPageResult) {
        return PageUtil.mybatisPlusPage2PageT(mybatisPlusPageResult);
    }

    /**
     * 日期格式化
     *
     * @param date    日期
     * @param pattern 格式化模板
     * @return 格式化之后的字符串
     */
    public String format(Date date, LocalDateTimePatternEnum pattern) {
        return DateUtil.format(date, pattern);
    }

    /**
     * LocalDate格式化
     *
     * @param localDate 日期
     * @param pattern   格式化模板
     * @return 格式化之后的字符串
     */
    public String format(LocalDate localDate, LocalDatePatternEnum pattern) {
        return DateUtil.format(localDate, pattern);
    }

    /**
     * LocalDateTime格式化
     *
     * @param localDateTime 日期
     * @param pattern       格式化模板
     * @return 格式化之后的字符串
     */
    public String format(LocalDateTime localDateTime, LocalDateTimePatternEnum pattern) {
        return DateUtil.format(localDateTime, pattern);
    }

    /**
     * 构建mybatis plus查询条件
     *
     * @return 查询条件
     */
    public QueryWrapper<T> buildQueryWrapper() {
        return new QueryWrapper<T>();
    }

    /**
     * 构建日期的范围查询条件
     *
     * @param queryWrapper 原始查询条件
     * @param field        查询字段(表的字段名)
     * @param dateRange    日期范围
     * @return 新的查询条件
     */
    public QueryWrapper<T> between(QueryWrapper<T> queryWrapper, String field, DateRangeSearchDTO dateRange) {
        if (null != dateRange) {
            return this.between(queryWrapper, field, dateRange.getBeginDate(), dateRange.getEndDate(), dateRange.getTargetFieldIsDatetime());
        } else {
            return queryWrapper;
        }
    }

    /**
     * 构建日期时间的范围查询条件
     *
     * @param queryWrapper           原始查询条件
     * @param columName              查询字段(表的字段名)
     * @param dateTimeRangeSearchDTO 日期时间范围
     * @return 新的查询条件
     */
    public QueryWrapper<T> between(QueryWrapper<T> queryWrapper, String columName, DateTimeRangeSearchDTO dateTimeRangeSearchDTO) {
        if (null != dateTimeRangeSearchDTO) {
            return this.between(queryWrapper, columName, dateTimeRangeSearchDTO.getBeginDateTime(), dateTimeRangeSearchDTO.getEndDateTime());
        } else {
            return queryWrapper;
        }
    }

    /**
     * 交换beginDate和endDate的值, 如果beginDate的时间戳小于endDate的时间戳
     *
     * @param dateRangeSearchDTO 待转换数据
     * @return 转换之后的结果
     */
    public DateRangeSearchDTO switchDateRangeSearchIfNecessary(DateRangeSearchDTO dateRangeSearchDTO) {
        return DateUtil.switchDateRangeSearchIfNecessary(dateRangeSearchDTO);
    }

    /**
     * 交换beginDateTime和endDateTime的值, 如果beginDateTime的时间戳小于endDateTime的时间戳
     *
     * @param dateRangeSearchDTO 待转换数据
     * @return 转换之后的结果
     */
    public DateTimeRangeSearchDTO switchDateTimeRangeSearchIfNecessary(DateTimeRangeSearchDTO dateRangeSearchDTO) {
        return DateUtil.switchDateTimeRangeSearchIfNecessary(dateRangeSearchDTO);
    }

    /**
     * 构建日期的范围查询条件
     *
     * @param queryWrapper          原始查询条件
     * @param columnName            查询字段(表的字段名)
     * @param beginDate             开始日期时间
     * @param endDate               结束日期时间
     * @param targetFieldIsDatetime 目标查询字段是否为datetime类型. <p>如果值为true, 则会将beginDate和endDate转换为. yyyy-MM-dd 00:00:00, yyyy-MM-dd 23:59:59 格式. <p>如果值为false, 则会将beginDate和endDate转换为. yyyy-MM-dd, yyyy-MM-dd 格式
     * @return 新的查询条件
     */
    public QueryWrapper<T> between(QueryWrapper<T> queryWrapper, String columnName, LocalDate beginDate, LocalDate endDate, boolean targetFieldIsDatetime) {
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
            if (targetFieldIsDatetime) {
                queryWrapper.between(columnName, fromDate.format(DateTimeFormatter.ofPattern(GlobalConstant.DATE_TIME_PATTERN_E)), toDate.format(DateTimeFormatter.ofPattern(GlobalConstant.DATE_TIME_PATTERN_F)));
            } else {
                queryWrapper.between(columnName, fromDate.format(DateTimeFormatter.ofPattern(GlobalConstant.DATE_PATTERN)), toDate.format(DateTimeFormatter.ofPattern(GlobalConstant.DATE_PATTERN)));
            }
        } else if (beginDate != null && endDate == null) {
            if (targetFieldIsDatetime) {
                queryWrapper.ge(columnName, beginDate.format(DateTimeFormatter.ofPattern(GlobalConstant.DATE_TIME_PATTERN_E)));
            } else {
                queryWrapper.ge(columnName, beginDate.format(DateTimeFormatter.ofPattern(GlobalConstant.DATE_PATTERN)));
            }
        } else if (beginDate == null && endDate != null) {
            if (targetFieldIsDatetime) {
                queryWrapper.le(columnName, endDate.format(DateTimeFormatter.ofPattern(GlobalConstant.DATE_TIME_PATTERN_F)));
            } else {
                queryWrapper.le(columnName, endDate.format(DateTimeFormatter.ofPattern(GlobalConstant.DATE_PATTERN)));
            }
        }
        return queryWrapper;
    }

    /**
     * 构建日期的范围查询条件
     *
     * @param queryWrapper 原始查询条件
     * @param columnName   查询字段(表的字段名)
     * @param beginDate    开始日期时间
     * @param endDate      结束日期时间
     * @return 新的查询条件
     */
    public QueryWrapper<T> between(QueryWrapper<T> queryWrapper, String columnName, LocalDateTime beginDate, LocalDateTime endDate) {
        if (beginDate != null && endDate != null) {
            LocalDateTime fromDate = null;
            LocalDateTime toDate = null;
            if (beginDate.isAfter(endDate)) {
                fromDate = endDate;
                toDate = beginDate;
            } else {
                fromDate = beginDate;
                toDate = endDate;
            }
            queryWrapper.between(columnName, fromDate.format(DateTimeFormatter.ofPattern(GlobalConstant.DATE_TIME_PATTERN_C)), toDate.format(DateTimeFormatter.ofPattern(GlobalConstant.DATE_TIME_PATTERN_D)));
        } else if (beginDate != null && endDate == null) {
            queryWrapper.ge(columnName, beginDate.format(DateTimeFormatter.ofPattern(GlobalConstant.DATE_TIME_PATTERN_C)));
        } else if (beginDate == null && endDate != null) {
            queryWrapper.le(columnName, endDate.format(DateTimeFormatter.ofPattern(GlobalConstant.DATE_TIME_PATTERN_D)));
        }
        return queryWrapper;
    }

    /**
     * 单字段模糊查询
     *
     * @param queryWrapper 原始查询条件
     * @param columnName   查询字段(表的字段名)
     * @param originalKw   未经处理的查询关键词(会自动去空格,以及对特殊字符进行转义)
     * @return 新的查询条件
     */
    public QueryWrapper<T> like(QueryWrapper<T> queryWrapper, String columnName, String originalKw) {
        if (!StringUtils.isEmpty(originalKw)) {
            return queryWrapper.like(columnName, this.keywordEscape(originalKw));
        }
        return queryWrapper;
    }

    /**
     * 多字段模糊查询(多个条件使用or连接)
     *
     * @param queryWrapper   原始查询条件
     * @param columnNameColl 查询字段(表的字段名)
     * @param originalKw     未经处理的查询关键词(会自动去空格,以及对特殊字符进行转义)
     * @return 新的查询条件
     */
    public QueryWrapper<T> like(QueryWrapper<T> queryWrapper, Collection<String> columnNameColl, String originalKw) {
        if (!StringUtils.isEmpty(originalKw)) {
            String kw = this.keywordEscape(originalKw);
            queryWrapper.or(qw -> {
                for (String columnName : columnNameColl) {
                    qw.or().like(columnName, kw);
                }
            });
        }
        return queryWrapper;
    }

    /**
     * 字符串等值查询
     *
     * @param queryWrapper 原始查询条件
     * @param columnName   查询字段(表的字段名)
     * @param originalKw   未经处理的查询词
     * @return 新的查询条件
     */
    public QueryWrapper<T> eq(QueryWrapper<T> queryWrapper, String columnName, String originalKw) {
        if (!StringUtils.isEmpty(originalKw)) {
            queryWrapper.eq(columnName, originalKw.trim());
        }
        return queryWrapper;
    }

    /**
     * 使用枚举的code值进行等值查询
     *
     * @param queryWrapper 原始查询条件
     * @param columnName   查询字段(表的字段名)
     * @param baseEnum     查询值
     * @return
     */
    public QueryWrapper<T> eq(QueryWrapper<T> queryWrapper, String columnName, BaseEnum<?> baseEnum) {
        if (null != baseEnum) {
            queryWrapper.eq(columnName, baseEnum.getCode());
        }
        return queryWrapper;
    }

    /**
     * 字符串等值查询.(多个条件使用 and 连接)
     *
     * @param queryWrapper   原始查询条件
     * @param columnNameColl 查询字段(表的字段名)
     * @param originalKw     未经处理的查询词
     * @return 新的查询条件
     */
    public QueryWrapper<T> eq(QueryWrapper<T> queryWrapper, Collection<String> columnNameColl, String originalKw) {
        if (!StringUtils.isEmpty(originalKw)) {
            for (String columnName : columnNameColl) {
                queryWrapper.eq(columnName, originalKw.trim());
            }
        }
        return queryWrapper;
    }
}
