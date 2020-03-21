package org.pzy.opensource.okhttp.domain.bo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * okhttp3响应结果
 *
 * @author 潘志勇
 * @date 2019-01-18
 */
@Setter
@Getter
@ToString
public class OkHttp3Response<T> implements Serializable {

    private static final long serialVersionUID = 8199096741863544270L;
    /**
     * http响应状态码
     */
    private int status;
    /**
     * 响应内容
     */
    private T body;
    /**
     * 响应的header
     */
    private Map<String, List<String>> headers;
}
