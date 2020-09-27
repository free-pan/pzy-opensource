package org.pzy.opensource.codegenerator.domain.enums;

/**
 * Winter风格的父类实体枚举
 *
 * @author pan
 * @date 2020/3/29
 */
public enum WinterStyleSuperEntityEnum {
    /**
     * 无父类实体(适用于对应表为多对多关系表的情况,其它情况均不适用)
     */
    None(null, null),
    /**
     * 简单实体
     */
    SimpleBaseEntity("org.pzy.opensource.mybatisplus.model.entity.SimpleBaseEntity", new String[]{"id"}),
    /**
     * 基础实体
     */
    BaseEntity("org.pzy.opensource.mybatisplus.model.entity.BaseEntity", new String[]{"id", "create_time", "edit_time", "creator_id", "editor_id", "creator_name", "editor_name"}),
    /**
     * 逻辑删除实体
     */
    LogicDelBaseEntity("org.pzy.opensource.mybatisplus.model.entity.LogicDelBaseEntity", new String[]{"id", "create_time", "edit_time", "creator_id", "editor_id", "creator_name", "editor_name", "invalid_time", "invalid_operator_id", "invalid_name", "invalid"});

    /**
     * 构造方法
     *
     * @param entityClassName 实体类名
     * @param entityColumns   实体类公共字段
     */
    WinterStyleSuperEntityEnum(String entityClassName, String[] entityColumns) {
        this.entityClassName = entityClassName;
        this.entityColumns = entityColumns;
    }

    /**
     * 实体类名
     */
    private String entityClassName;
    /**
     * 公共字段
     */
    private String[] entityColumns;

    public String getEntityClassName() {
        return entityClassName;
    }

    public void setEntityClassName(String entityClassName) {
        this.entityClassName = entityClassName;
    }

    public String[] getEntityColumns() {
        return entityColumns;
    }

    public void setEntityColumns(String[] entityColumns) {
        this.entityColumns = entityColumns;
    }
}
