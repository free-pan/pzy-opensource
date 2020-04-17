package org.pzy.opensource.mybatisplus.basemapper;

import com.baomidou.mybatisplus.core.metadata.TableInfo;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

import java.util.Map;

/**
 * <p>按ID集合查询出匹配的数据(包括逻辑删除的数据)
 *
 * @author pan
 * @date 2020/4/6 10:08
 */
public class WinterSelectByMapMethod extends AbstractWinterMethod {

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        WinterSqlMethod sqlMethod = WinterSqlMethod.SELECT_BY_MAP;
        String sql = String.format(sqlMethod.getSql(), sqlSelectColumns(tableInfo, false),
                tableInfo.getTableName(), sqlWhereByMap(tableInfo));
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, Map.class);
        return this.addSelectMappedStatementForTable(mapperClass, sqlMethod.getMethod(), sqlSource, tableInfo);
    }
}
