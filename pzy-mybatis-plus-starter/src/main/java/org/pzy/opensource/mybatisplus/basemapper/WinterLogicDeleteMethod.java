/**
 * Copyright (C): 恒大集团版权所有 Evergrande Group
 */
package org.pzy.opensource.mybatisplus.basemapper;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import org.pzy.opensource.comm.util.DateUtil;
import org.pzy.opensource.currentuser.ThreadCurrentUser;
import org.pzy.opensource.domain.enums.LocalDateTimePatternEnum;
import org.pzy.opensource.mybatisplus.model.entity.LogicDelBaseEntity;

import java.time.LocalDateTime;

/**
 * <p>如果表是逻辑删除表,则执行逻辑删除, 否则抛出异常.
 * <p>逻辑删除表必须符合 {@link org.pzy.opensource.mybatisplus.model.entity.LogicDelBaseEntity} 的结构定义, 且进行了mybatis plus的逻辑删除配置
 *
 * @author pan
 * @date 2020/4/6 10:08
 * @see org.pzy.opensource.mybatisplus.model.entity.LogicDelBaseEntity
 */
public class WinterLogicDeleteMethod extends AbstractMethod {

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
            throw new RuntimeException(String.format("表[%s]不是逻辑删除表,无法执行逻辑删除操作!"));
        }
    }

    @Override
    protected String sqlLogicSet(TableInfo table) {
        String strValue = "'%s'";
        // 填充删除人信息以及删除时间
        String str = String.format(" %s=%s, %s=%s, %s=%s ", LogicDelBaseEntity.DISABLED_OPT_ID, ThreadCurrentUser.getUserId(0L), LogicDelBaseEntity.DISABLED_OPT_NAME, String.format(strValue, ThreadCurrentUser.getRealName("")), LogicDelBaseEntity.DISABLED_TIME, String.format(strValue, DateUtil.format(LocalDateTime.now(), LocalDateTimePatternEnum.DATE_TIME_PATTERN)));
        return "SET " + table.getLogicDeleteSql(false, false) + ", " + str;
    }
}
