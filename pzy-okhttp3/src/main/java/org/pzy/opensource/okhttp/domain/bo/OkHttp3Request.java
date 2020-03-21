package org.pzy.opensource.okhttp.domain.bo;

import lombok.Builder;
import lombok.Data;
import okhttp3.MediaType;
import org.pzy.opensource.domain.GlobalConstant;
import org.pzy.opensource.okhttp.support.enums.RequestMethodEnum;

import java.util.List;
import java.util.Map;

/**
 * @author 潘志勇
 * @date 2019-02-01
 */
@Data
@Builder
public class OkHttp3Request {
    /**
     * 请求方式
     */
    private RequestMethodEnum method;
    /**
     * 请求url
     */
    private String url;
    /**
     * 参数编码
     */
    private String encoding = GlobalConstant.DEFAULT_CHARSET;
    /**
     * 请求头参数
     */
    private Map<String, String> headers;
    /**
     * 表单参数
     */
    private Map<String, String> formParams;
    /**
     * url参数
     */
    private Map<String, String> uriParams;
    /**
     * request body的媒体类型
     */
    private MediaType mediaType;
    /**
     * request body内容
     */
    private byte[] content;
    /**
     * 用于多文件上传
     */
    private List<OkhttpUploadFile> uploadFileList;
}
