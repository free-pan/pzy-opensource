package org.pzy.opensource.domain.entity;

/**
 * <p>系统中使用的自定义枚举都要继承该接口, 该接口定义了自定义枚举的通用方法
 * <p>同时该方法也表明, 系统自定义枚举必须有个code属性, 用于设置自定义值
 *
 * @author pan
 * @date 4/11/20
 */
public interface BaseEnum<T> {
    /**
     * 设置自定义编码或中文名称或其它自定义内容.
     *
     * @param code 编码或中文名称
     */
    void setCode(T code);

    /**
     * 获取自定义编码或中文名称或其它自定义内容
     *
     * @return 编码或中文名称
     */
    T getCode();

    /**
     * 获取枚举名称. 如: Color.red 枚举, 则这里会返回red
     *
     * @return 枚举名称
     */
    String name();
}
