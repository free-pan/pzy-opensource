package org.pzy.opensource.domain.enums;

import org.pzy.opensource.domain.entity.BaseEnum;

/**
 * 全局的系统错误编码枚举
 *
 * @author pan
 * @date 2020/4/6 16:37
 */
public enum GlobalSystemErrorCodeEnum implements BaseEnum<String> {
    /**
     * 表示:文件太大
     */
    FILE_TOO_LARGE("上传的文件大小超过了,服务器允许的最大文件大小!"),
    /**
     * 表示:请求头中指定了错误的accept值
     */
    ACCEPT_TYPE_NOT_SUPPORT("请求头中指定了错误的accept值!"),
    /**
     * 表示:实际请求参数不符合预期
     */
    REQUEST_PARAM_MISMATCH("实际请求参数不符合预期!"),
    /**
     * 表示:参数验证未通过
     */
    PARAM_VALIDATE_EXCEPTION("参数验证未通过!"),
    /**
     * 表示:request body参数解析异常
     */
    REQUEST_MESSAGE_EXTRACT("request body参数解析异常!"),
    /**
     * 表示:不支持的请求类型
     */
    REQUEST_METHOD_NOT_SUPPORT("不支持的请求类型!"),
    /**
     * 表示:不支持的content-type类型
     */
    CONTENT_TYPE_NOT_SUPPORT("不支持的content-type类型!"),
    /**
     * 表示:缺少请求参数
     */
    REQUEST_PARAM_NOT_FOUND("缺少请求参数!"),
    /**
     * 表示:服务器异常
     */
    SERVER_EXCEPTION("服务异常"),
    /**
     * 表示:数据重复
     */
    DATA_REPEAT("数据重复"),
    /**
     * 表示:shiro登录异常
     */
    SECURITY_LOGIN_EXCEPTION("shiro登录异常!"),
    /**
     * 表示:未通过shiro的鉴权(权限不足)
     */
    SECURITY_FORBIDDEN_EXCEPTION("未通过shiro的鉴权(权限不足)"),
    /**
     * 表示:未认证试图访问受保护资源
     */
    SECURITY_UNAUTHORIZED_EXCEPTION("未认证访问受保护资源");


    private String code;

    GlobalSystemErrorCodeEnum(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public void setCode(String code) {
        this.code = code;
    }
}
