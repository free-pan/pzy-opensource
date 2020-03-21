package org.pzy.opensource.okhttp.support.enums;

/**
 * @author 潘志勇
 * @date 2019-01-18
 */
public enum RequestMethodEnum {
    /**
     * GET请求
     */
    GET,
    /**
     * POST请求,使用表单传参
     */
    POST_FORM,
    /**
     * POST请求,使用request body传参
     */
    POST_BODY,
    /**
     * POST文件上传
     */
    POST_MULTIPART,
    /**
     * PUT请求
     */
    PUT,
    /**
     * DELETE请求
     */
    DELETE
}
