/**
 * Copyright (C): 恒大集团版权所有 Evergrande Group
 */
package org.pzy.opensource.mybatisplus.basemapper;

/**
 * WinterSqlMethod
 *
 * @author pan
 * @date 2020/4/6 10:09
 */
public enum WinterSqlMethod {

    LOGIC_DELETE_BY_ID("logicDeleteById", "根据ID 逻辑删除一条数据", "<script>\nUPDATE %s %s WHERE %s=#{%s} %s\n</script>");

    private final String method;
    private final String desc;
    private final String sql;

    WinterSqlMethod(String method, String desc, String sql) {
        this.method = method;
        this.desc = desc;
        this.sql = sql;
    }

    public String getMethod() {
        return method;
    }

    public String getDesc() {
        return desc;
    }

    public String getSql() {
        return sql;
    }
}
