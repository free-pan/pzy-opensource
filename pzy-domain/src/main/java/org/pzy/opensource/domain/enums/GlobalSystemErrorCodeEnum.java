package org.pzy.opensource.domain.enums;

/**
 * GlobalSystemErrorCodeEnum
 *
 * @author pan
 * @date 2020/4/6 16:37
 */
public enum GlobalSystemErrorCodeEnum {
    /**
     * 表示:文件太大
     */
    FILE_TOO_LARGE("FILE_TOO_LARGE", "上传的文件大小超过了,服务器允许的最大文件大小!"),
    /**
     * 表示:请求头中指定了错误的accept值
     */
    ACCEPT_TYPE_NOT_SUPPORT("ACCEPT_TYPE_NOT_SUPPORT", "请求头中指定了错误的accept值!"),
    /**
     * 表示:实际请求参数不符合预期
     */
    REQUEST_PARAM_MISMATCH("REQUEST_PARAM_MISMATCH", "实际请求参数不符合预期!"),
    /**
     * 表示:参数验证未通过
     */
    PARAM_VALIDATE_EXCEPTION("PARAM_VALIDATE_EXCEPTION", "参数验证未通过!"),
    /**
     * 表示:request body参数解析异常
     */
    REQUEST_MESSAGE_EXTRACT("REQUEST_MESSAGE_EXTRACT", "request body参数解析异常!"),
    /**
     * 表示:不支持的请求类型
     */
    REQUEST_METHOD_NOT_SUPPORT("REQUEST_METHOD_NOT_SUPPORT", "不支持的请求类型!"),
    /**
     * 表示:不支持的content-type类型
     */
    CONTENT_TYPE_NOT_SUPPORT("CONTENT_TYPE_NOT_SUPPORT", "不支持的content-type类型!"),
    /**
     * 表示:缺少请求参数
     */
    REQUEST_PARAM_NOT_FOUND("REQUEST_PARAM_NOT_FOUND", "缺少请求参数!"),
    /**
     * 表示:服务器异常
     */
    SERVER_EXCEPTION("SERVER_EXCEPTION", "服务异常"),
    /**
     * 表示:数据重复
     */
    DATA_REPEAT("DATA_REPEAT", "数据重复"),
    /**
     * 表示:shiro登录异常
     */
    SHIRO_LOGIN_EXCEPTION("SHIRO_LOGIN_EXCEPTION", "shiro登录异常!"),
    /**
     * 表示:未通过shiro的鉴权(权限不足)
     */
    SHIRO_FORBIDDEN_EXCEPTION("SHIRO_FORBIDDEN_EXCEPTION", "未通过shiro的鉴权(权限不足)");


    private String errorCode;
    private String defaultErrorMsg;

    GlobalSystemErrorCodeEnum(String errorCode, String defaultErrorMsg) {
        this.errorCode = errorCode;
        this.defaultErrorMsg = defaultErrorMsg;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getDefaultErrorMsg() {
        return defaultErrorMsg;
    }

    public void setDefaultErrorMsg(String defaultErrorMsg) {
        this.defaultErrorMsg = defaultErrorMsg;
    }
}
