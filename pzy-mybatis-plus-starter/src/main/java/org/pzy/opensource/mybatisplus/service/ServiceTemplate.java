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
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.pzy.opensource.comm.mapstruct.BaseMapStruct;
import org.pzy.opensource.domain.PageT;
import org.pzy.opensource.domain.vo.PageVO;
import org.pzy.opensource.mybatisplus.model.entity.BaseEntity;

import java.util.Collection;
import java.util.List;

/**
 * 服务模板
 *
 * @param <AddVO>  用于新增的vo类
 * @param <EditVO> 用于编辑的vo类
 * @param <Entity> 实体类
 * @param <DTO>    查询返回值
 * @author 潘志勇
 */
public interface ServiceTemplate<AddVO, EditVO, Entity extends BaseEntity, DTO, BaseDao extends BaseMapper<Entity>, BaseObjectMapper extends BaseMapStruct<AddVO, EditVO, Entity, DTO>> {

    /**
     * 清除当前服务的查询缓存
     */
    void clearCache();

    /**
     * 新增
     *
     * @param vo 待新增数据
     * @return 新增成功, 则返回实际的id, 新增失败, 则返回0
     */
    Long save(AddVO vo);

    /**
     * 新增
     *
     * @param entity 待新增数据
     * @return 新增成功, 则返回实际的id, 新增失败, 则返回0
     */
    Long save(Entity entity);

    /**
     * 批量保存VO对象
     *
     * @param dataList  待批量保存的数据
     * @param batchSize 批处理大小
     */
    void saveBatch(List<AddVO> dataList, int batchSize);

    /**
     * 按id删除
     *
     * @param id
     * @return 是否有删除到实际的数据
     */
    boolean deleteById(Long id);

    /**
     * 按条件删除
     *
     * @param wrapper
     * @return 是否有删除到实际的数据
     */
    boolean delete(Wrapper<Entity> wrapper);

    /**
     * 按id更新
     *
     * @param vo
     * @return 更新是否成功
     */
    boolean edit(EditVO vo);

    /**
     * 按id更新
     *
     * @param entity
     * @return 更新是否成功
     */
    boolean edit(Entity entity);

    /**
     * 按id查找
     *
     * @param id
     * @return
     */
    DTO searchById(Long id);

    /**
     * 按id查找
     *
     * @param id
     * @return
     */
    Entity searchEntityById(Long id);

    /**
     * 按id集合,批量查找
     *
     * @param ids
     * @return
     */
    List<DTO> searchByIds(Collection<Long> ids);

    /**
     * 按id集合,批量查找
     *
     * @param ids
     * @return
     */
    List<Entity> searchEntityByIds(Collection<Long> ids);

    /**
     * 按条件查找
     *
     * @param queryWrapper
     * @return
     */
    List<DTO> searchList(Wrapper<Entity> queryWrapper);

    /**
     * 按条件查找
     *
     * @param queryWrapper
     * @return
     */
    List<Entity> searchEntityList(Wrapper<Entity> queryWrapper);

    /**
     * 按条件分页查找
     *
     * @param page
     * @param queryWrapper
     * @return
     */
    PageT<DTO> searchPage(PageVO page, Wrapper<Entity> queryWrapper);

    /**
     * 按条件分页查找
     *
     * @param page
     * @param queryWrapper
     * @return
     */
    PageT<Entity> searchEntityPage(PageVO page, Wrapper<Entity> queryWrapper);

    /**
     * 查询所有
     *
     * @return
     */
    List<DTO> searchAll();

    /**
     * 查找所有
     *
     * @return
     */
    List<Entity> searchEntityAll();

    /**
     * 按条件查找单个结果
     *
     * @param queryWrapper 查询条件
     * @return
     */
    DTO searchOne(Wrapper<Entity> queryWrapper);

    /**
     * 按条件查找单个结果
     *
     * @param queryWrapper
     * @return
     */
    Entity searchEntityOne(Wrapper<Entity> queryWrapper);
}
