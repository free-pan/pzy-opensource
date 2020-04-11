package org.pzy.opensource.domain.enums;

import org.pzy.opensource.domain.GlobalConstant;
import org.pzy.opensource.domain.entity.BaseEnum;

/**
 * 禁用/启用枚举
 *
 * @author pan
 * @date 4/11/20
 */
public enum DisableStatusEnum implements BaseEnum<Short> {

    /**
     * 启用
     */
    enable(GlobalConstant.ENABLE),
    /**
     * 禁用
     */
    disable(GlobalConstant.DISABLE);

    private Short code;

    DisableStatusEnum(Short code) {
        this.code = code;
    }

    @Override
    public Short getCode() {
        return code;
    }

    @Override
    public void setCode(Short code) {
        this.code = code;
    }
}
