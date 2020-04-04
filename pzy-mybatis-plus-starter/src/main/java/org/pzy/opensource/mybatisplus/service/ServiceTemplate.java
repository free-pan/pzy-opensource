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
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.pzy.opensource.domain.PageT;
import org.pzy.opensource.domain.vo.PageVO;

import java.io.Serializable;
import java.util.List;

/**
 * 服务模板接口. 将增,改,查与缓存操作关联起来
 * <p>默认实现是: {@link org.pzy.opensource.mybatisplus.service.ServiceTemplateImpl}
 *
 * @author 潘志勇
 */
public interface ServiceTemplate<T> extends IService<T> {

    /**
     * 清除当前服务的查询缓存
     */
    void clearCache();

    /**
     * 按条件分页查找. 并将查询结果缓存
     * <p>注意: 该方法必须使用 `对象名.方法名(...)` 调用会有缓存相关操作, 因为缓存相关操作是由spring走代理的时候添加的
     * <p>在该类的子类进行 `this.方法名(...)` 或 `super.方法名(...)` 是不会包含spring代理层面的缓存相关逻辑的
     *
     * @param page         分页条件
     * @param queryWrapper 查询条件
     * @return
     */
    PageT<T> searchPageAndCache(PageVO page, Wrapper<T> queryWrapper);

    /**
     * 分页查询
     *
     * @param page         分页条件
     * @param queryWrapper 查询条件
     * @return
     */
    IPage<T> searchPageVO(PageVO page, Wrapper<T> queryWrapper);

    /**
     * 新增, 并在真正的新增业务执行之前就清除与之关联的缓存
     * <p>注意: 该方法必须使用 `对象名.方法名(...)` 调用会有缓存相关操作, 因为缓存相关操作是由spring走代理的时候添加的
     * <p>在该类的子类进行 `this.方法名(...)` 或 `super.方法名(...)` 是不会包含spring代理层面的缓存相关逻辑的
     *
     * @param entity 待新增实体
     * @return true表示新增成功.
     */
    boolean addAndClearCache(T entity);

    /**
     * 编辑, 并在真正的新增业务执行之前就清除与之关联的缓存
     * <p>注意: 该方法必须使用 `对象名.方法名(...)` 调用会有缓存相关操作, 因为缓存相关操作是由spring走代理的时候添加的
     * <p>在该类的子类进行 `this.方法名(...)` 或 `super.方法名(...)` 是不会包含spring代理层面的缓存相关逻辑的
     *
     * @param entity
     * @return true表示编辑成功.
     */
    boolean editAndClearCache(T entity);

    /**
     * 根据id查找, 并将结果进行缓存
     * <p>注意: 该方法必须使用 `对象名.方法名(...)` 调用会有缓存相关操作, 因为缓存相关操作是由spring走代理的时候添加的
     * <p>在该类的子类进行 `this.方法名(...)` 或 `super.方法名(...)` 是不会包含spring代理层面的缓存相关逻辑的
     *
     * @param id
     * @return
     */
    T searchByIdAndCache(Serializable id);

    /**
     * 查询所有并缓存
     * <p>注意: 该方法必须使用 `对象名.方法名(...)` 调用会有缓存相关操作, 因为缓存相关操作是由spring走代理的时候添加的
     * <p>在该类的子类进行 `this.方法名(...)` 或 `super.方法名(...)` 是不会包含spring代理层面的缓存相关逻辑的
     *
     * @return 所有匹配的数据
     */
    List<T> searchAllAndCache();

    /**
     * 按条件查找并缓存
     * <p>注意: 该方法必须使用 `对象名.方法名(...)` 调用会有缓存相关操作, 因为缓存相关操作是由spring走代理的时候添加的
     * <p>在该类的子类进行 `this.方法名(...)` 或 `super.方法名(...)` 是不会包含spring代理层面的缓存相关逻辑的
     *
     * @param queryWrapper 查询条件
     * @return 所有匹配的数据
     */
    List<T> searchAllAndCache(QueryWrapper<T> queryWrapper);
}
