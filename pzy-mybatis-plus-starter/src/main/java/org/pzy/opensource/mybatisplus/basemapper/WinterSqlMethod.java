package org.pzy.opensource.mybatisplus.basemapper;

/**
 * WinterSqlMethod
 *
 * @author pan
 * @date 2020/4/6 10:09
 */
public enum WinterSqlMethod {

    LOGIC_DELETE_BY_ID("winterLogicDeleteById", "根据ID 逻辑删除一条数据", "<script>\nUPDATE %s %s WHERE %s=#{%s} %s\n</script>"),
    SELECT_LIST("winterSelectList", "查询满足条件所有数据", "<script>\nSELECT %s FROM %s %s %s\n</script>"),
    SELECT_ONE("winterSelectOne", "查询满足条件一条数据", "<script>\nSELECT %s FROM %s %s %s\n</script>"),
    SELECT_PAGE("winterSelectPage", "查询满足条件所有数据（并翻页）", "<script>\nSELECT %s FROM %s %s %s\n</script>"),
    SELECT_BY_MAP("winterSelectByMap", "根据columnMap 查询一条数据", "<script>\nSELECT %s FROM %s %s\n</script>"),
    LOGIC_SELECT_BY_ID("winterSelectById", "根据ID 查询一条数据", "SELECT %s FROM %s WHERE %s=#{%s} %s"),
    LOGIC_SELECT_BATCH_BY_IDS("winterSelectBatchIds", "根据ID集合，批量查询数据", "<script>\nSELECT %s FROM %s WHERE %s IN (%s) %s\n</script>"),
    SELECT_COUNT("winterSelectCount", "查询满足条件总记录数", "<script>\nSELECT COUNT(%s) FROM %s %s %s\n</script>"),

    /**
     * 修改
     */
    UPDATE_BY_ID("winterUpdateById", "根据ID 选择修改数据", "<script>\nUPDATE %s %s WHERE %s=#{%s} %s\n</script>");

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
