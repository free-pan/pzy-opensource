package org.pzy.opensource.mybatisplus.basemapper;

import com.baomidou.mybatisplus.core.metadata.TableInfo;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import org.pzy.opensource.mybatisplus.model.entity.LogicDelBaseEntity;

/**
 * <p>按条件查询出匹配的单条数据(包括逻辑删除的数据)
 *
 * @author pan
 * @date 2020/4/6 10:08
 */
public class WinterSelectOneMethod extends AbstractWinterMethod {

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        WinterSqlMethod sqlMethod = WinterSqlMethod.SELECT_ONE;
        String sql = String.format(sqlMethod.getSql(), sqlSelectColumns(tableInfo, true),
                tableInfo.getTableName(), sqlWhereEntityWrapper(true, tableInfo),
                sqlComment());
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, modelClass);
        return this.addSelectMappedStatementForTable(mapperClass, sqlMethod.getMethod(), sqlSource, tableInfo);
    }
}
