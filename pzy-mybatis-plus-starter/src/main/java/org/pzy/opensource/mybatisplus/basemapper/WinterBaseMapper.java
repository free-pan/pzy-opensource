package org.pzy.opensource.mybatisplus.basemapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * 对mybatis plus的BaseMapper进行扩展
 *
 * @author pan
 * @date 2020/4/6 10:07
 */
public interface WinterBaseMapper<T> extends BaseMapper<T> {

    /**
     * <p>逻辑删除. 会同时填充删除人信息以及删除时间
     * <p>如果表是逻辑删除表,则执行逻辑删除, 否则执行普通的删除.
     *
     * @param id 主键
     * @return 逻辑删除的记录数
     * @see org.pzy.opensource.mybatisplus.basemapper.WinterLogicDeleteMethod
     */
    int winterLogicDeleteById(Serializable id);

    /**
     * 按条件查询出匹配的所有数据(包括逻辑删除的数据)
     *
     * @param queryWrapper 查询条件
     * @return 匹配的记录
     */
    List<T> winterSelectList(Wrapper<T> queryWrapper);

    /**
     * 按条件查询出匹配的单条数据(包括逻辑删除的数据)
     *
     * @param queryWrapper 查询条件
     * @return 匹配的记录
     */
    T winterSelectOne(Wrapper<T> queryWrapper);

    /**
     * 根据 ID 查询(包括逻辑删除的数据)
     *
     * @param id 主键ID
     */
    T winterSelectById(Serializable id);

    /**
     * 查询（根据ID 批量查询）(包括逻辑删除的数据)
     *
     * @param idList 主键ID列表(不能为 null 以及 empty)
     */
    List<T> winterSelectBatchIds(@Param(Constants.COLLECTION) Collection<? extends Serializable> idList);

    /**
     * 按条件查询出匹配的单条数据(包括逻辑删除的数据)
     *
     * @param page         分页条件
     * @param queryWrapper 查询条件
     * @return 匹配的记录
     */
    <E extends IPage<T>> E winterSelectPage(E page, @Param(Constants.WRAPPER) Wrapper<T> queryWrapper);
}
