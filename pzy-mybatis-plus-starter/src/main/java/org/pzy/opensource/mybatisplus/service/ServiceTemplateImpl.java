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
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
import lombok.Getter;
import org.apache.ibatis.session.SqlSession;
import org.pzy.opensource.comm.mapstruct.BaseMapStruct;
import org.pzy.opensource.domain.GlobalConstant;
import org.pzy.opensource.domain.PageT;
import org.pzy.opensource.domain.vo.PageVO;
import org.pzy.opensource.mybatisplus.model.entity.BaseEntity;
import org.pzy.opensource.mybatisplus.util.MybatisPlusUtil;
import org.pzy.opensource.mybatisplus.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author pan
 * @date 2019-12-12
 */
@Getter
public class ServiceTemplateImpl<AddVO, EditVO, Entity extends BaseEntity, DTO, BaseDao extends BaseMapper<Entity>, BaseObjectMapper extends BaseMapStruct<AddVO, EditVO, Entity, DTO>> implements ServiceTemplate<AddVO, EditVO, Entity, DTO, BaseDao, BaseObjectMapper> {

    /**
     * 数据库操作类
     */
    @Autowired
    private BaseDao baseDao;

    /**
     * po,vo,dto转换操作类
     */
    @Autowired
    private BaseObjectMapper baseObjectMapper;

    /**
     * 计算hash表的实际大小
     *
     * @param elementCount hash表中实际要存放的元素
     * @param loadFactor   加载因子[可不指定].默认:0.75
     * @return
     */
    public int computeHashSize(int elementCount, Float loadFactor) {
        return MybatisPlusUtil.computeHashSize(elementCount, loadFactor);
    }

    @Override
    @CacheEvict(allEntries = true, beforeInvocation = true)
    public void clearCache() {

    }

    /**
     * 按条件查找数量
     *
     * @param wrapper 查询条件
     * @return
     */
    public Integer searchCount(Wrapper<Entity> wrapper) {
        return this.getBaseDao().selectCount(wrapper);
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @CacheEvict(allEntries = true, beforeInvocation = true)
    @Override
    public Long save(AddVO vo) {
        Entity entity = this.addVOtoEntity(vo);
        return this.save(entity);
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @CacheEvict(allEntries = true, beforeInvocation = true)
    @Override
    public Long save(Entity entity) {
        Integer count = this.baseDao.insert(entity);
        if (MybatisPlusUtil.retBool(count)) {
            return entity.getId();
        } else {
            return GlobalConstant.ZERO;
        }
    }

    protected Class<Entity> currentModelClass() {
        return (Class<Entity>) ReflectionKit.getSuperClassGenericType(getClass(), 2);
    }


    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @CacheEvict(allEntries = true, beforeInvocation = true)
    @Override
    public void saveBatch(List<AddVO> dataList, int batchSize) {
        List<Entity> entityList = this.addVOtoEntity(dataList);
        this.saveEntityBatch(entityList, batchSize);
    }

    /**
     * 批量保存Entity对象
     *
     * @param entityList 待保存数据
     * @param batchSize  批处理大小
     */
    public void saveEntityBatch(List<Entity> entityList, int batchSize) {
        Class<Entity> entityClass = currentModelClass();
        String sqlStatement = MybatisPlusUtil.sqlStatement(entityClass, SqlMethod.INSERT_ONE);
        Consumer<SqlSession> sqlSessionConsumer = MybatisPlusUtil.buildConsumer(sqlStatement, entityList, batchSize);
        MybatisPlusUtil.executeBatch(sqlSessionConsumer, entityClass);
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @CacheEvict(allEntries = true, beforeInvocation = true)
    @Override
    public boolean deleteById(Long id) {
        int deleteCount = this.baseDao.deleteById(id);
        return MybatisPlusUtil.retBool(deleteCount);
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @CacheEvict(allEntries = true, beforeInvocation = true)
    @Override
    public boolean delete(Wrapper<Entity> wrapper) {
        int deleteCount = this.getBaseDao().delete(wrapper);
        return MybatisPlusUtil.retBool(deleteCount);
    }


    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @CacheEvict(allEntries = true, beforeInvocation = true)
    @Override
    public boolean edit(EditVO vo) {
        Entity entity = this.editVOtoEntity(vo);
        return this.updateById(entity);
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @CacheEvict(allEntries = true, beforeInvocation = true)
    @Override
    public boolean edit(Entity entity) {
        return this.updateById(entity);
    }

    /**
     * 按id更新
     *
     * @param entity
     */
    public boolean updateById(Entity entity) {
        int updateCount = this.baseDao.updateById(entity);
        return MybatisPlusUtil.retBool(updateCount);
    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    @Cacheable(sync = true)
    @Override
    public DTO searchById(Long id) {
        Entity entity = this.baseDao.selectById(id);
        return this.toDTO(entity);
    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    @Cacheable(sync = true)
    @Override
    public Entity searchEntityById(Long id) {
        return this.baseDao.selectById(id);
    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    @Cacheable(sync = true)
    @Override
    public List<DTO> searchByIds(Collection<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Collections.EMPTY_LIST;
        }
        List<Entity> entityList = this.baseDao.selectBatchIds(ids);
        return this.toDTO(entityList);
    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    @Cacheable(sync = true)
    @Override
    public List<Entity> searchEntityByIds(Collection<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Collections.EMPTY_LIST;
        }
        return this.baseDao.selectBatchIds(ids);
    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    @Cacheable(sync = true)
    @Override
    public List<DTO> searchList(Wrapper<Entity> queryWrapper) {
        List<Entity> entityList = this.baseDao.selectList(queryWrapper);
        return this.toDTO(entityList);
    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    @Cacheable(sync = true)
    @Override
    public List<Entity> searchEntityList(Wrapper<Entity> queryWrapper) {
        return this.baseDao.selectList(queryWrapper);
    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    @Cacheable(sync = true)
    @Override
    public PageT<DTO> searchPage(PageVO page, Wrapper<Entity> queryWrapper) {
        IPage<Entity> mybatisPlusPage = PageUtil.pageVO2MybatisPlusPage(page);
        IPage<Entity> pageResult = this.baseDao.selectPage(mybatisPlusPage, queryWrapper);
        List<DTO> dtoList = this.baseObjectMapper.entityToDTO(pageResult.getRecords());
        PageT<DTO> result = new PageT<>();
        result.setList(dtoList);
        result.setTotal(pageResult.getTotal());
        result.setPage(pageResult.getCurrent());
        result.setSize(pageResult.getSize());
        return result;
    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    @Cacheable(sync = true)
    @Override
    public PageT<Entity> searchEntityPage(PageVO page, Wrapper<Entity> queryWrapper) {
        IPage<Entity> mybatisPlusPage = PageUtil.pageVO2MybatisPlusPage(page);
        IPage<Entity> pageResult = this.baseDao.selectPage(mybatisPlusPage, queryWrapper);
        PageT<Entity> result = new PageT<>();
        result.setList(pageResult.getRecords());
        result.setTotal(pageResult.getTotal());
        result.setPage(pageResult.getCurrent());
        result.setSize(pageResult.getSize());
        return result;
    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    @Override
    @Cacheable(sync = true)
    public List<DTO> searchAll() {
        List<Entity> entityList = this.getBaseDao().selectList(null);
        return this.toDTO(entityList);
    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    @Cacheable(sync = true)
    @Override
    public List<Entity> searchEntityAll() {
        return this.getBaseDao().selectList(null);
    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    @Cacheable(sync = true)
    @Override
    public DTO searchOne(Wrapper<Entity> queryWrapper) {
        Entity entity = this.getBaseDao().selectOne(queryWrapper);
        return this.toDTO(entity);
    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    @Cacheable(sync = true)
    @Override
    public Entity searchEntityOne(Wrapper<Entity> queryWrapper) {
        return this.getBaseDao().selectOne(queryWrapper);
    }

    /**
     * entity转dto
     *
     * @param entity
     * @return
     */
    public DTO toDTO(Entity entity) {
        return this.getBaseObjectMapper().entityToDTO(entity);
    }

    /**
     * entity转dto
     *
     * @param entityList
     * @return
     */
    public List<DTO> toDTO(Collection<Entity> entityList) {
        return this.getBaseObjectMapper().entityToDTO(entityList);
    }

    /**
     * add vo 转 entity
     *
     * @param vo
     * @return
     */
    public Entity addVOtoEntity(AddVO vo) {
        return this.getBaseObjectMapper().addSourceToEntity(vo);
    }

    /**
     * add vo 转 entity
     *
     * @param voList
     * @return
     */
    public List<Entity> addVOtoEntity(Collection<AddVO> voList) {
        return this.getBaseObjectMapper().addSourceToEntity(voList);
    }

    /**
     * edit vo 转 entity
     *
     * @param vo
     * @return
     */
    public Entity editVOtoEntity(EditVO vo) {
        return this.getBaseObjectMapper().editSourceToEntity(vo);
    }

    /**
     * edit vo 转 entity
     *
     * @param voList
     * @return
     */
    public List<Entity> editVOtoEntity(Collection<EditVO> voList) {
        return this.getBaseObjectMapper().editSourceToEntity(voList);
    }
}
