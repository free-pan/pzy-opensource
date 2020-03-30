package org.pzy.opensource.okhttp.support.builder;

import okhttp3.MediaType;
import org.pzy.opensource.okhttp.domain.bo.OkHttp3Request;
import org.pzy.opensource.okhttp.domain.bo.OkhttpUploadFile;
import org.pzy.opensource.okhttp.support.enums.RequestMethodEnum;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * okhttp3请求构建器
 *
 * @author 潘志勇
 * @date 2019-02-01
 */
public class OkHttp3RequestBuilder {

    private OkHttp3RequestBuilder() {
    }

    /**
     * post表单提交
     *
     * @param url        请求地址
     * @param formParams 参数
     * @return 请求构建器
     */
    public static OkHttp3Request.OkHttp3RequestBuilder postForm(String url, Map<String, String> formParams) {
        return OkHttp3Request.builder().method(RequestMethodEnum.POST_FORM).formParams(formParams).url(url);
    }

    /**
     * post请求体提交
     *
     * @param url       请求地址
     * @param mediaType request body的媒体类型
     * @param content   request body的内容
     * @return 请求构建器
     */
    public static OkHttp3Request.OkHttp3RequestBuilder postBody(String url, MediaType mediaType, byte[] content) {
        return OkHttp3Request.builder().method(RequestMethodEnum.POST_BODY).mediaType(mediaType).content(content).url(url);
    }

    /**
     * put提交
     *
     * @param url     请求地址
     * @param content request body的内容
     * @return 请求构建器
     */
    public static OkHttp3Request.OkHttp3RequestBuilder put(String url, MediaType mediaType, byte[] content) {
        return OkHttp3Request.builder().method(RequestMethodEnum.PUT).mediaType(mediaType).url(url).content(content);
    }

    /**
     * get提交
     *
     * @param url       请求地址
     * @param uriParams 参数
     * @return 请求构建器
     */
    public static OkHttp3Request.OkHttp3RequestBuilder get(String url, Map<String, String> uriParams) {
        return OkHttp3Request.builder().method(RequestMethodEnum.GET).uriParams(uriParams).url(url);
    }

    /**
     * delete提交
     *
     * @param url       请求地址
     * @param uriParams 参数
     * @return 请求构建器
     */
    public static OkHttp3Request.OkHttp3RequestBuilder delete(String url, Map<String, String> uriParams) {
        return OkHttp3Request.builder().method(RequestMethodEnum.DELETE).uriParams(uriParams).url(url);
    }

    /**
     * 单文件上传
     *
     * @param url        上传地址
     * @param formParams 表单参数
     * @param uploadFile 文件
     * @return 请求构建器
     */
    public static OkHttp3Request.OkHttp3RequestBuilder uploadSingleFile(String url, Map<String, String> formParams, OkhttpUploadFile uploadFile) {
        return OkHttp3Request.builder().method(RequestMethodEnum.POST_MULTIPART).formParams(formParams).url(url).uploadFileList(Arrays.asList(uploadFile));
    }

    /**
     * 多文件上传
     *
     * @param url        上传地址
     * @param formParams 表单参数
     * @param files      文件
     * @return 请求构建器
     */
    public static OkHttp3Request.OkHttp3RequestBuilder uploadSingleFile(String url, Map<String, String> formParams, List<OkhttpUploadFile> files) {
        return OkHttp3Request.builder().method(RequestMethodEnum.POST_MULTIPART).formParams(formParams).url(url).uploadFileList(files);
    }
}
