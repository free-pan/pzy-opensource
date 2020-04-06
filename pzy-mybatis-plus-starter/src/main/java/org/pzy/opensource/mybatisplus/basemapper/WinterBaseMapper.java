package org.pzy.opensource.mybatisplus.basemapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.io.Serializable;

/**
 * 对mybatis plus的BaseMapper进行扩展
 *
 * @author pan
 * @date 2020/4/6 10:07
 */
public interface WinterBaseMapper<T> extends BaseMapper<T> {

    /**
     * <p>逻辑删除. 会同时填充删除人信息以及删除时间
     * <p>如果表是逻辑删除表,则执行逻辑删除, 否则抛出异常.
     *
     * @param id 主键
     * @return 逻辑删除的记录数
     * @see org.pzy.opensource.mybatisplus.basemapper.WinterLogicDeleteMethod
     */
    int logicDeleteById(Serializable id);
}
