package org.pzy.opensource.mybatisplus.basemapper;

import com.baomidou.mybatisplus.core.metadata.TableInfo;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.scripting.defaults.RawSqlSource;
import org.pzy.opensource.domain.GlobalConstant;
import org.pzy.opensource.mybatisplus.model.entity.LogicDelBaseEntity;

/**
 * <p>按ID查询出匹配的单条数据(包括逻辑删除的数据)
 *
 * @author pan
 * @date 2020/4/6 10:08
 * @see LogicDelBaseEntity
 */
public class WinterSelectByIdMethod extends AbstractWinterMethod {

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        WinterSqlMethod sqlMethod = WinterSqlMethod.LOGIC_SELECT_BY_ID;
        String sql = String.format(sqlMethod.getSql(),
                sqlSelectColumns(tableInfo, false),
                tableInfo.getTableName(), tableInfo.getKeyColumn(), tableInfo.getKeyProperty(),
                GlobalConstant.EMPTY_STRING);
        SqlSource sqlSource = new RawSqlSource(configuration, sql, Object.class);
        return this.addSelectMappedStatementForTable(mapperClass, sqlMethod.getMethod(), sqlSource, tableInfo);
    }
}
