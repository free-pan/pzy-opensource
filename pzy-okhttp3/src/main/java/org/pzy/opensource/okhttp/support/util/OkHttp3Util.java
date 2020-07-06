package org.pzy.opensource.okhttp.support.util;

import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import okhttp3.logging.HttpLoggingInterceptor;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.pzy.opensource.comm.util.JsonUtil;
import org.pzy.opensource.comm.util.XmlUtil;
import org.pzy.opensource.domain.GlobalConstant;
import org.pzy.opensource.okhttp.domain.bo.*;
import org.pzy.opensource.okhttp.support.builder.OkHttp3RequestBuilder;
import org.pzy.opensource.okhttp.support.enums.BodyTypeEnum;
import org.pzy.opensource.okhttp.support.enums.RequestMethodEnum;
import org.pzy.opensource.spring.util.ResourceFileLoadUtil;
import org.springframework.util.CollectionUtils;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * okhttp3请求帮助类<br/>
 * 可在项目目录下创建`okhttp3.yml`配置文件, 用于自定义okhttp3的连接池和超时时间<br/>
 * <pre>
 * 配置文件说明:
 * connectionTimeout: ... #连接超时时间(单位:秒). 默认:10
 * readTimeout: ... #读超时时间(单位:秒). 默认:20
 * writeTimeout: ... #写超时时间(单位:秒). 默认:15
 * maxIdleConnections: ... #连接池中最大连接数. 默认:10
 * keepAliveDuration: ... #不活动的连接,多久之后被销毁(单位:秒). 默认:10
 * interceptors: ... #okhttp3拦截器类,多个类以`;`号分割
 *
 * </pre>
 *
 * @author 潘志勇
 * @date 2019-01-18
 */
@Slf4j
public class OkHttp3Util {

    /**
     * url与请求参数的分割字符串
     */
    private static final String URL_AND_PARAM_SPLIT_CHAR = "?";
    /**
     * 参数的分割字符串
     */
    private static final String PARAM_SPLIT_CHAR = "&";
    /**
     * 请求参数的key,value分割字符串
     */
    private static final String PARAM_KEY_VALUE_SPLIT_CHAR = "=";

    /**
     * 请求客户端
     */
    private static OkHttpClient OK_HTTP_CLIENT;

    static {
        // 读取配置文件(okhttp3.yml)
        String yamlFile = "classpath:okhttp3.yml";
        OkHttp3ConnectionPoolConfigBO okHttp3ConnectionPoolConfigBO = null;
        if (ResourceFileLoadUtil.configFileExists(yamlFile)) {
            try {
                okHttp3ConnectionPoolConfigBO = ResourceFileLoadUtil.readYamlFile(yamlFile, OkHttp3ConnectionPoolConfigBO.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (null == okHttp3ConnectionPoolConfigBO) {
            okHttp3ConnectionPoolConfigBO = new OkHttp3ConnectionPoolConfigBO();
        }
        List<Interceptor> interceptorList = new ArrayList<>();
        if (!StringUtils.isBlank(okHttp3ConnectionPoolConfigBO.getInterceptors())) {
            String[] arr = StringUtils.split(okHttp3ConnectionPoolConfigBO.getInterceptors(), GlobalConstant.DEFAULT_SPLIT_CHART);
            if (!ArrayUtils.isEmpty(arr)) {
                for (String interceptorCls : arr) {
                    try {
                        Interceptor interceptor = (Interceptor) Class.forName(interceptorCls).newInstance();
                        interceptorList.add(interceptor);
                    } catch (Exception e) {
                        System.err.println("okhttp3拦截器实例创建异常!");
                        e.printStackTrace();
                    }
                }
            }
        }
        // 建立http连接池
        ConnectionPool connectionPool = new ConnectionPool(okHttp3ConnectionPoolConfigBO.getMaxIdleConnections(), okHttp3ConnectionPoolConfigBO.getKeepAliveDuration(), TimeUnit.SECONDS);
        HttpLoggingInterceptor.Logger logger = new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                log.debug(message);
            }
        };
        // 创建http请求日志拦截器
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(logger);
        loggingInterceptor.level(HttpLoggingInterceptor.Level.BODY);
        interceptorList.add(loggingInterceptor);
        OkHttpClient.Builder builder = new OkHttpClient.Builder().connectionPool(connectionPool).connectTimeout(okHttp3ConnectionPoolConfigBO.getConnectionTimeout(), TimeUnit.SECONDS)
                .readTimeout(okHttp3ConnectionPoolConfigBO.getReadTimeout(), TimeUnit.SECONDS)
                .writeTimeout(okHttp3ConnectionPoolConfigBO.getWriteTimeout(), TimeUnit.SECONDS);
        for (Interceptor interceptor : interceptorList) {
            builder.addInterceptor(interceptor);
        }
        OK_HTTP_CLIENT = builder.build();
        String logInfo = "http3客户端建立! http连接池配置: 最大:{}, 单个连接最长空闲时间(秒):{}.  连接超时配置: 连接超时时间(秒):{}, 读超时时间(秒):{}, 写超时时间(秒):{}. 拦截器配置: 默认拦截器:{}, 自定义拦截器:{}";
        log.debug(logInfo,
                okHttp3ConnectionPoolConfigBO.getMaxIdleConnections(),
                okHttp3ConnectionPoolConfigBO.getKeepAliveDuration(),
                okHttp3ConnectionPoolConfigBO.getConnectionTimeout(),
                okHttp3ConnectionPoolConfigBO.getReadTimeout(),
                okHttp3ConnectionPoolConfigBO.getWriteTimeout(),
                HttpLoggingInterceptor.class.getName(),
                StringUtils.isBlank(okHttp3ConnectionPoolConfigBO.getInterceptors()) ? "" : okHttp3ConnectionPoolConfigBO.getInterceptors());
    }


    /**
     * 构建post表单参数
     *
     * @param paramCharset 参数字符编码
     * @param formParams   表单参数
     * @param needEncoding 是否需要对post表单参数进行编码
     * @return 表单
     */
    private static FormBody buildPostForm(String paramCharset, Map<String, String> formParams, boolean needEncoding) {
        FormBody.Builder formBuilder = new FormBody.Builder();
        Set<Map.Entry<String, String>> entrySet = formParams.entrySet();
        if (needEncoding) {
            for (Map.Entry<String, String> entry : entrySet) {
                try {
                    formBuilder.add(entry.getKey(), URLEncoder.encode(entry.getValue(), paramCharset));
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException("表单参数使用[" + paramCharset + "]编码出错!", e);
                }
            }
        } else {
            for (Map.Entry<String, String> entry : entrySet) {
                formBuilder.add(entry.getKey(), entry.getValue());
            }
        }
        return formBuilder.build();
    }

    /**
     * 设置请求头
     *
     * @param paramCharset 参数字符编码
     * @param headers      请求参数
     * @return 请求构建对象
     */
    private static Request.Builder requestBuilderSetHeaders(String paramCharset, Map<String, String> headers) {
        Request.Builder builder = new Request.Builder();
        if (!CollectionUtils.isEmpty(headers)) {
            Set<Map.Entry<String, String>> entrySet = headers.entrySet();
            for (Map.Entry<String, String> entry : entrySet) {
                try {
                    builder.addHeader(entry.getKey(), URLEncoder.encode(entry.getValue(), paramCharset));
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException("请求头参数使用[" + paramCharset + "]编码出错!", e);
                }
            }
        }
        return builder;
    }

    /**
     * 发送post请求,使用表单传参((可用于文件下载))
     *
     * @param okHttp3Request 请求条件. 可以使用 {@linkplain OkHttp3RequestBuilder OkHttp3RequestBuilder} 快速构建
     * @param bodyType       响应结果body的实际类型
     * @param needEncoding   是否需要对post表单参数进行编码
     * @return 请求结果
     */
    public static <T> OkHttp3Response<T> postFormRequest(OkHttp3Request okHttp3Request, OkHttp3BodyType<T> bodyType, boolean needEncoding) {
        String realUrl = buildRealUrl(okHttp3Request.getUrl(), okHttp3Request.getEncoding(), okHttp3Request.getUriParams());
        FormBody formBody = buildPostForm(okHttp3Request.getEncoding(), okHttp3Request.getFormParams(), needEncoding);
        Request.Builder builder = requestBuilderSetHeaders(okHttp3Request.getEncoding(), okHttp3Request.getHeaders());
        Request request = builder.url(realUrl).post(formBody).build();
        return execute(RequestMethodEnum.POST_FORM, realUrl, request, bodyType);
    }

    /**
     * 发送post请求,使用body传参(可用于文件下载)
     *
     * @param okHttp3Request 请求条件. 可以使用 {@linkplain OkHttp3RequestBuilder OkHttp3RequestBuilder} 快速构建
     * @param bodyType       响应结果body的实际类型
     * @return 请求结果
     */
    public static <T> OkHttp3Response<T> postBodyRequest(OkHttp3Request okHttp3Request, OkHttp3BodyType<T> bodyType) {
        String realUrl = buildRealUrl(okHttp3Request.getUrl(), okHttp3Request.getEncoding(), okHttp3Request.getUriParams());
        Request.Builder builder = requestBuilderSetHeaders(okHttp3Request.getEncoding(), okHttp3Request.getHeaders());
        Request request = builder.url(realUrl).post(RequestBody.create(okHttp3Request.getMediaType(), okHttp3Request.getContent())).build();
        return execute(RequestMethodEnum.POST_BODY, realUrl, request, bodyType);
    }

    /**
     * 执行请求并获取返回结果
     *
     * @param url     请求url
     * @param request 请求内容
     * @return 请求执行结果
     */
    private static <T> OkHttp3Response<T> execute(RequestMethodEnum method, String url, Request request, OkHttp3BodyType<T> bodyType) {
        OkHttp3Response okHttp3Response = new OkHttp3Response();
        try {
            Response response = OK_HTTP_CLIENT.newCall(request).execute();
            okHttp3Response.setStatus(response.code());
            if (null != response.body()) {
                InputStream inputStream = response.body().byteStream();
                okHttp3Response.setBody(bodyConvert(bodyType, inputStream));
            }
            Headers headers = response.headers();
            if (null != headers) {
                Map<String, List<String>> responseHeaders = headers.toMultimap();
                if (!CollectionUtils.isEmpty(responseHeaders)) {
                    okHttp3Response.setHeaders(responseHeaders);
                }
            }
            if (CollectionUtils.isEmpty(okHttp3Response.getHeaders())) {
                okHttp3Response.setHeaders(new HashMap<>(0));
            }
        } catch (IOException e) {
            throw new RuntimeException("使用Okhttp3发送请求出错! URL: " + method.name() + "  " + url, e);
        } catch (JAXBException e) {
            throw new RuntimeException("响应结果为xml数据,但无法将xml数据转换为java对象!" + e.getMessage(), e);
        }
        return okHttp3Response;
    }

    private static <T> T bodyConvert(OkHttp3BodyType<T> bodyType, InputStream inputStream) throws JAXBException {
        if (BodyTypeEnum.JSON == bodyType.getType()) {
            String str = convert2String(inputStream, bodyType.getEncoding());
            if (StringUtils.isBlank(str)) {
                return null;
            } else {
                return JsonUtil.toJavaBean(str, bodyType.getDataTypeCls());
            }
        } else if (BodyTypeEnum.XML == bodyType.getType()) {
            String str = convert2String(inputStream, bodyType.getEncoding());
            if (StringUtils.isBlank(str)) {
                return null;
            } else {
                return XmlUtil.toJavaBean(str, bodyType.getDataTypeCls());
            }
        } else if (BodyTypeEnum.String == bodyType.getType()) {
            return (T) convert2String(inputStream, bodyType.getEncoding());
        } else {
            return (T) inputStream;
        }
    }

    private static String convert2String(InputStream inputStream, String encoding) {
        if (null == inputStream) {
            return null;
        }
        try {
            return IOUtils.toString(inputStream, encoding);
        } catch (IOException e) {
            throw new RuntimeException("响应内容转换出错!", e);
        }
    }


    /**
     * 发送get请求(可用于文件下载)
     *
     * @param okHttp3Request 请求条件. 可以使用 {@linkplain OkHttp3RequestBuilder OkHttp3RequestBuilder} 快速构建
     * @param bodyType       响应结果body的实际类型
     * @return 请求结果
     */
    public static <T> OkHttp3Response<T> getRequest(OkHttp3Request okHttp3Request, OkHttp3BodyType bodyType) {
        String realUrl = buildRealUrl(okHttp3Request.getUrl(), okHttp3Request.getEncoding(), okHttp3Request.getUriParams());
        Request.Builder builder = requestBuilderSetHeaders(okHttp3Request.getEncoding(), okHttp3Request.getHeaders());
        Request request = builder.get().url(realUrl).build();
        return execute(RequestMethodEnum.GET, realUrl, request, bodyType);
    }

    /**
     * 根据请求参数构建最终的请求url
     *
     * @param url          请求url
     * @param paramCharset 请求参数字符编码
     * @param uriParams    请求参数
     * @return 最终的请求url
     */
    private static String buildRealUrl(String url, String paramCharset, Map<String, String> uriParams) {
        if (CollectionUtils.isEmpty(uriParams)) {
            return url;
        }
        String paramStr = uriParams2String(uriParams, paramCharset);
        String realUrl = url.trim();
        if (StringUtils.isNotBlank(paramCharset)) {
            if (realUrl.contains(URL_AND_PARAM_SPLIT_CHAR)) {
                if (realUrl.endsWith(PARAM_SPLIT_CHAR)) {
                    realUrl += paramStr;
                } else {
                    realUrl += PARAM_SPLIT_CHAR + paramStr;
                }
            } else {
                realUrl += URL_AND_PARAM_SPLIT_CHAR + paramStr;
            }
        }
        return realUrl;
    }

    /**
     * 获取字符编码. 未获取到则使用UTF-8编码
     *
     * @param paramCharset 字符编码
     * @return 最终的字符串编码
     */
    private static String getCharset(String paramCharset) {
        String realParamCharset = paramCharset;
        if (StringUtils.isBlank(paramCharset)) {
            realParamCharset = GlobalConstant.DEFAULT_CHARSET;
        }
        return realParamCharset;
    }

    /**
     * 构建uri的请求参数字符串
     *
     * @param uriParams    请求参数
     * @param paramCharset 请求参数的字符编码
     * @return 请求uri参数
     */
    private static String uriParams2String(Map<String, String> uriParams, String paramCharset) {
        StringBuilder stringBuilder = new StringBuilder();
        if (!CollectionUtils.isEmpty(uriParams)) {
            Set<Map.Entry<String, String>> entrySet = uriParams.entrySet();
            for (Map.Entry<String, String> entry : entrySet) {
                try {
                    stringBuilder.append(entry.getKey()).append(PARAM_KEY_VALUE_SPLIT_CHAR).append(URLEncoder.encode(entry.getValue(), paramCharset));
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException("请求参数使用[" + paramCharset + "]编码出错!", e);
                }
            }
        }
        return stringBuilder.toString();
    }

    /**
     * 执行put请求
     *
     * @param okHttp3Request 请求条件. 可以使用 {@linkplain OkHttp3RequestBuilder OkHttp3RequestBuilder} 快速构建
     * @param bodyType       响应结果body的实际类型
     * @return 请求结果
     */
    public static <T> OkHttp3Response<T> putBodyRequest(OkHttp3Request okHttp3Request, OkHttp3BodyType<T> bodyType) {
        String realUrl = buildRealUrl(okHttp3Request.getUrl(), okHttp3Request.getEncoding(), okHttp3Request.getUriParams());
        Request.Builder builder = requestBuilderSetHeaders(okHttp3Request.getEncoding(), okHttp3Request.getHeaders());
        Request request = builder.url(realUrl).put(RequestBody.create(okHttp3Request.getMediaType(), okHttp3Request.getContent())).build();
        return execute(RequestMethodEnum.PUT, realUrl, request, bodyType);
    }

    /**
     * 执行文件上传请求
     *
     * @param okHttp3Request 请求条件. 可以使用 {@linkplain OkHttp3RequestBuilder OkHttp3RequestBuilder} 快速构建
     * @param bodyType       响应结果body的实际类型
     * @return 请求结果
     */
    public static <T> OkHttp3Response<T> uploadRequest(OkHttp3Request okHttp3Request, OkHttp3BodyType<T> bodyType) {
        String realUrl = buildRealUrl(okHttp3Request.getUrl(), okHttp3Request.getEncoding(), okHttp3Request.getUriParams());

        MultipartBody multipartBody = buildMultipartBody(okHttp3Request.getEncoding(), okHttp3Request.getFormParams(), okHttp3Request.getUploadFileList());

        Request.Builder requestBuilder = requestBuilderSetHeaders(okHttp3Request.getEncoding(), okHttp3Request.getHeaders());
        Request request = requestBuilder.url(realUrl).post(multipartBody).build();
        return execute(RequestMethodEnum.POST_MULTIPART, realUrl, request, bodyType);
    }

    /**
     * 构建MultipartBody对象
     *
     * @param paramCharset 请求参数和文件名字符编码
     * @param formParams   表单参数
     * @param files        上传的文件信息
     * @return MultipartBody对象
     */
    private static MultipartBody buildMultipartBody(String paramCharset, Map<String, String> formParams, List<OkhttpUploadFile> files) {
        MediaType type = MediaType.parse("application/octet-stream");

        // 构建文件上传请求
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.ALTERNATIVE);
        // 给文件上传表单,添加请求参数
        addFileFormParams(paramCharset, formParams, builder);
        for (OkhttpUploadFile file : files) {
            // 构建文件上传的body内容
            RequestBody fileBody = RequestBody.create(type, file.getFileBytes());
            try {
                builder.addFormDataPart(file.getName(), URLEncoder.encode(file.getTitle(), paramCharset), fileBody);
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("上传文件名使用[" + paramCharset + "]编码出错!", e);
            }
        }
        return builder.build();
    }

    /**
     * 添加表单参数
     *
     * @param paramCharset 字符编码
     * @param formParams   表单参数
     * @param builder      请求构建对象
     */
    private static void addFileFormParams(String paramCharset, Map<String, String> formParams, MultipartBody.Builder builder) {
        if (!CollectionUtils.isEmpty(formParams)) {
            Set<Map.Entry<String, String>> entrySet = formParams.entrySet();
            for (Map.Entry<String, String> entry : entrySet) {
                try {
                    builder.addFormDataPart(entry.getKey(), URLEncoder.encode(entry.getValue(), paramCharset));
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException("表单参数使用[" + paramCharset + "]编码出错!", e);
                }
            }
        }
    }

    /**
     * 发送delete请求
     *
     * @param okHttp3Request 请求条件. 可以使用 {@linkplain OkHttp3RequestBuilder OkHttp3RequestBuilder} 快速构建
     * @param bodyType       响应结果body的实际类型
     * @return 请求结果
     */
    public static <T> OkHttp3Response<T> deleteRequest(OkHttp3Request okHttp3Request, OkHttp3BodyType<T> bodyType) {
        String realUrl = buildRealUrl(okHttp3Request.getUrl(), okHttp3Request.getEncoding(), okHttp3Request.getUriParams());
        Request.Builder builder = requestBuilderSetHeaders(okHttp3Request.getEncoding(), okHttp3Request.getHeaders());
        Request request = builder.get().url(realUrl).build();
        return execute(RequestMethodEnum.DELETE, realUrl, request, bodyType);
    }

    /**
     * 构建并执行http请求,请求方式由okHttp3Request参数决定
     *
     * @param okHttp3Request 请求条件. 可以使用 {@linkplain OkHttp3RequestBuilder OkHttp3RequestBuilder} 快速构建
     * @param bodyType       response body的实际类型
     * @param needEncoding   是否需要对post表单参数进行编码
     * @param <T>
     * @return
     */
    public static <T> OkHttp3Response<T> request(OkHttp3Request okHttp3Request, OkHttp3BodyType<T> bodyType, boolean needEncoding) {
        RequestMethodEnum requestMethodEnum = okHttp3Request.getMethod();
        if (requestMethodEnum == RequestMethodEnum.GET) {
            return getRequest(okHttp3Request, bodyType);
        } else if (requestMethodEnum == RequestMethodEnum.POST_BODY) {
            return postBodyRequest(okHttp3Request, bodyType);
        } else if (requestMethodEnum == RequestMethodEnum.POST_FORM) {
            return postFormRequest(okHttp3Request, bodyType, needEncoding);
        } else if (requestMethodEnum == RequestMethodEnum.PUT) {
            return putBodyRequest(okHttp3Request, bodyType);
        } else if (requestMethodEnum == RequestMethodEnum.DELETE) {
            return deleteRequest(okHttp3Request, bodyType);
        } else if (requestMethodEnum == RequestMethodEnum.POST_MULTIPART) {
            return uploadRequest(okHttp3Request, bodyType);
        }
        throw new RuntimeException(String.format("未知的请求类型:[%s]", okHttp3Request.getMethod()));
    }
}
