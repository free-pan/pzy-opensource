package org.pzy.opensource.mybatisplus.basemapper;

import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

/**
 * <p>如果表是逻辑删除表,则执行逻辑删除, 否则执行普通的删除
 * <p>逻辑删除表必须符合 {@link org.pzy.opensource.mybatisplus.model.entity.LogicDelBaseEntity} 的结构定义, 且进行了mybatis plus的逻辑删除配置
 *
 * @author pan
 * @date 2020/4/6 10:08
 * @see org.pzy.opensource.mybatisplus.model.entity.LogicDelBaseEntity
 */
public class WinterLogicDeleteMethod extends AbstractWinterMethod {

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        String sql;
        if (tableInfo.isLogicDelete()) {
            WinterSqlMethod sqlMethod = WinterSqlMethod.LOGIC_DELETE_BY_ID;
            sql = String.format(sqlMethod.getSql(), tableInfo.getTableName(), sqlLogicSet(tableInfo),
                    tableInfo.getKeyColumn(), tableInfo.getKeyProperty(),
                    tableInfo.getLogicDeleteSql(true, true));
            SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, Object.class);
            return addUpdateMappedStatement(mapperClass, modelClass, sqlMethod.getMethod(), sqlSource);
        } else {
            SqlMethod sqlMethod = SqlMethod.DELETE_BY_ID;
            sql = String.format(sqlMethod.getSql(), tableInfo.getTableName(), tableInfo.getKeyColumn(),
                    tableInfo.getKeyProperty());
            SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, Object.class);
            return this.addDeleteMappedStatement(mapperClass, getMethod(sqlMethod), sqlSource);
        }
    }
}
