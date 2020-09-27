package org.pzy.opensource.mybatisplus.basemapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 对mybatis plus的BaseMapper进行扩展
 *
 * @author pan
 * @date 2020/4/6 10:07
 */
public interface WinterBaseMapper<T> extends BaseMapper<T> {

    /**
     * <p>数据作废. 会同时填充作废操作人信息以及作废时间
     * <p>如果表是逻辑删除表,则执行作废, 否则执行普通的删除.
     *
     * @param id 主键
     * @return 逻辑删除的记录数
     * @see WinterInvalidMethod
     */
    int invalidById(Serializable id);

    /**
     * 按条件查询出匹配的所有数据(包括作废的数据)
     *
     * @param queryWrapper 查询条件
     * @return 匹配的记录
     * @see WinterSelectListMethod
     */
    List<T> winterSelectList(@Param(Constants.WRAPPER) Wrapper<T> queryWrapper);

    /**
     * 按条件查询出匹配的单条数据(包括作废的数据)
     *
     * @param queryWrapper 查询条件
     * @return 匹配的记录
     * @see WinterSelectOneMethod
     */
    T winterSelectOne(@Param(Constants.WRAPPER) Wrapper<T> queryWrapper);

    /**
     * 根据 ID 查询(包括作废的数据)
     *
     * @param id 主键ID
     * @see WinterSelectByIdMethod
     */
    T winterSelectById(Serializable id);

    /**
     * 查询（根据ID 批量查询）(包括作废的数据)
     *
     * @param idList 主键ID列表(不能为 null 以及 empty)
     * @see WinterSelectByIdsMethod
     */
    List<T> winterSelectBatchIds(@Param(Constants.COLLECTION) Collection<? extends Serializable> idList);

    /**
     * 根据 Wrapper 条件，查询总记录数(包含作废的数据)
     *
     * @param queryWrapper 实体对象封装操作类（可以为 null）
     * @see WinterSelectCountMethod
     */
    Integer winterSelectCount(@Param(Constants.WRAPPER) Wrapper<T> queryWrapper);

    /**
     * 查询（根据 columnMap 条件）(包括作废的数据)
     *
     * @param columnMap 表字段 map 对象
     * @see WinterSelectByMapMethod
     */
    List<T> winterSelectByMap(@Param(Constants.COLUMN_MAP) Map<String, Object> columnMap);

    /**
     * 按条件查询出匹配的单条数据(包括作废的数据)
     *
     * @param page         分页条件
     * @param queryWrapper 查询条件
     * @return 匹配的记录
     * @see WinterSelectPageMethod
     */
    <E extends IPage<T>> E winterSelectPage(E page, @Param(Constants.WRAPPER) Wrapper<T> queryWrapper);

    /**
     * 根据 ID 修改(包括作废的数据)
     *
     * @param entity 实体对象
     * @see WinterUpdateByIdMethod
     */
    int winterUpdateById(@Param(Constants.ENTITY) T entity);
}
