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

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.pzy.opensource.domain.PageT;
import org.pzy.opensource.domain.vo.PageVO;
import org.pzy.opensource.mybatisplus.util.MybatisPlusUtil;
import org.pzy.opensource.mybatisplus.util.PageUtil;
import org.pzy.opensource.mybatisplus.util.SpringUtil;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 服务模板实现
 *
 * @author pan
 * @date 2019-12-12
 */
@Getter
@Slf4j
public abstract class ServiceTemplateImpl<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> implements ServiceTemplate<T> {

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

    @CacheEvict(allEntries = true, beforeInvocation = true)
    @Override
    public void clearCache() {
        if (log.isDebugEnabled()) {
            log.debug(String.format("清除[%s]服务类缓存!", this.getClass().getName()));
        }
    }

    @Cacheable(sync = true)
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    @Override
    public PageT<T> searchPageAndCache(PageVO page, Wrapper<T> queryWrapper) {
        // 系统的分页条件转换为mybatis plus的分页条件
        IPage<T> mybatisPlusPageCondition = toMybatisPlusPage(page);
        // mybatis plus分页查询
        IPage<T> entityPageResult = super.page(mybatisPlusPageCondition, queryWrapper);
        // mybatis plus分页结果转换为系统分页结果
        return toPageT(entityPageResult);
    }

    @Override
    public IPage<T> searchPageVO(PageVO page, Wrapper<T> queryWrapper) {
        IPage<T> mybatisPlusPageCondition = toMybatisPlusPage(page);
        return super.page(mybatisPlusPageCondition, queryWrapper);
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @CacheEvict(allEntries = true, beforeInvocation = true)
    @Override
    public boolean addAndClearCache(T entity) {
        return super.save(entity);
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @CacheEvict(allEntries = true, beforeInvocation = true)
    @Override
    public boolean editAndClearCache(T entity) {
        return super.updateById(entity);
    }

    @Cacheable(sync = true)
    @Transactional(readOnly = true, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @Override
    public List<T> searchAllAndCache() {
        return super.list();
    }

    @Cacheable(sync = true)
    @Transactional(readOnly = true, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @Override
    public List<T> searchAllAndCache(QueryWrapper<T> queryWrapper) {
        return super.list(queryWrapper);
    }

    @Cacheable(sync = true)
    @Transactional(readOnly = true, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @Override
    public T searchByIdAndCache(Serializable id) {
        return super.getById(id);
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
     * 构建mybatis plus查询条件
     *
     * @return
     */
    public QueryWrapper<T> buildQueryWrapper() {
        return new QueryWrapper<T>();
    }

    /**
     * 构建日期的范围查询条件
     *
     * @param queryWrapper 原始查询条件
     * @param field        查询字段
     * @param beginDate    开始日期时间
     * @param endDate      结束日期时间
     * @return
     */
    public QueryWrapper<T> between(QueryWrapper<T> queryWrapper, String field, Date beginDate, Date endDate) {
        if (beginDate != null && endDate != null) {
            Date fromDate = beginDate.getTime() > endDate.getTime() ? endDate : beginDate;
            Date toDate = beginDate.getTime() > endDate.getTime() ? beginDate : endDate;
            queryWrapper.between(field, fromDate, toDate);
        } else if (beginDate != null && endDate == null) {
            queryWrapper.ge(field, beginDate);
        } else if (beginDate == null && endDate != null) {
            queryWrapper.le(field, endDate);
        }
        return queryWrapper;
    }
}
