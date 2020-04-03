/**
 * Copyright (C): 恒大集团版权所有 Evergrande Group
 */
package org.pzy.opensource.codegenerator.domain.enums;

/**
 * CodeGeneratorModelEnum
 *
 * @author pan
 * @date 2020/4/3 15:04
 */
public enum CodeGeneratorModelEnum {

    /**
     * 只生成controller相关代码
     */
    ONLY_CONTROLLER,
    /**
     * 生成除controller之外的所有代码
     */
    EXCEPT_CONTROLLER,
    /**
     * 生成所有代码
     */
    ALL;
}
