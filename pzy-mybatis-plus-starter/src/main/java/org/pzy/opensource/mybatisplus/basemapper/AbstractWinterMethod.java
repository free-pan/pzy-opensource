package org.pzy.opensource.mybatisplus.basemapper;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlScriptUtils;
import org.pzy.opensource.comm.util.DateUtil;
import org.pzy.opensource.currentuser.ThreadCurrentUser;
import org.pzy.opensource.domain.enums.LocalDateTimePatternEnum;
import org.pzy.opensource.mybatisplus.model.entity.LogicDelBaseEntity;

import java.time.LocalDateTime;

/**
 * AbstractWinterMethod
 *
 * @author pan
 * @date 4/17/20
 */
public abstract class AbstractWinterMethod extends AbstractMethod {

    @Override
    protected String sqlWhereEntityWrapper(boolean newLine, TableInfo table) {
        String sqlScript = table.getAllSqlWhere(false, true, WRAPPER_ENTITY_DOT);
        sqlScript = SqlScriptUtils.convertIf(sqlScript, String.format("%s != null", WRAPPER_ENTITY), true);
        sqlScript += NEWLINE;
        sqlScript += SqlScriptUtils.convertIf(String.format(SqlScriptUtils.convertIf(" AND", String.format("%s and %s", WRAPPER_NONEMPTYOFENTITY, WRAPPER_NONEMPTYOFNORMAL), false) + " ${%s}", WRAPPER_SQLSEGMENT),
                String.format("%s != null and %s != '' and %s", WRAPPER_SQLSEGMENT, WRAPPER_SQLSEGMENT,
                        WRAPPER_NONEMPTYOFWHERE), true);
        sqlScript = SqlScriptUtils.convertWhere(sqlScript) + NEWLINE;
        sqlScript += SqlScriptUtils.convertIf(String.format(" ${%s}", WRAPPER_SQLSEGMENT),
                String.format("%s != null and %s != '' and %s", WRAPPER_SQLSEGMENT, WRAPPER_SQLSEGMENT,
                        WRAPPER_EMPTYOFWHERE), true);
        sqlScript = SqlScriptUtils.convertIf(sqlScript, String.format("%s != null", WRAPPER), true);
        return newLine ? NEWLINE + sqlScript : sqlScript;
    }

    @Override
    protected String sqlLogicSet(TableInfo table) {
        String strValue = "'%s'";
        // 填充删除人信息以及删除时间
        String str = String.format(" %s=%s, %s=%s, %s=%s ", LogicDelBaseEntity.DISABLED_OPT_ID, ThreadCurrentUser.getUserId(0L), LogicDelBaseEntity.DISABLED_OPT_NAME, String.format(strValue, ThreadCurrentUser.getRealName("")), LogicDelBaseEntity.DISABLED_TIME, String.format(strValue, DateUtil.format(LocalDateTime.now(), LocalDateTimePatternEnum.DATE_TIME_PATTERN)));
        return "SET " + table.getLogicDeleteSql(false, false) + ", " + str;
    }
}
