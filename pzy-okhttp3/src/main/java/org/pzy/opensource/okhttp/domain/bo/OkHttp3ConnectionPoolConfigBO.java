package org.pzy.opensource.okhttp.domain.bo;

import lombok.Data;

import java.io.Serializable;

/**
 * okhttp3连接池配置信息
 *
 * @author 潘志勇
 * @date 2019-02-01
 */
@Data
public class OkHttp3ConnectionPoolConfigBO implements Serializable {

    private static final long serialVersionUID = 8666833791301740552L;

    /**
     * 默认连接超时时间
     */
    private static final int DEFAULT_CONNECTION_TIMEOUT = 10;

    /**
     * 默认读超时时间
     */
    private static final int DEFAULT_READ_TIMEOUT = 20;

    /**
     * 默认写超时时间
     */
    private static final int DEFAULT_WRITE_TIMEOUT = 15;

    /**
     * 默认最大连接数
     */
    private static final int DEFAULT_MAX_IDLE_CONNECTIONS = 10;

    /**
     * 默认不活动连接的销毁时间
     */
    private static final int DEFAULT_KEEP_ALIVE_DURATION = 10;

    /**
     * 连接超时时间(单位:秒)
     */
    private Integer connectionTimeout = DEFAULT_CONNECTION_TIMEOUT;

    /**
     * 读超时时间(单位:秒)
     */
    private Integer readTimeout = DEFAULT_READ_TIMEOUT;

    /**
     * 写超时时间(单位:秒)
     */
    private Integer writeTimeout = DEFAULT_WRITE_TIMEOUT;

    /**
     * 连接池中最大连接数
     */
    private Integer maxIdleConnections = DEFAULT_MAX_IDLE_CONNECTIONS;

    /**
     * 不活动的连接,多久之后被销毁(单位:秒)
     */
    private Integer keepAliveDuration = DEFAULT_KEEP_ALIVE_DURATION;

    /**
     * okhttp3拦截器类,多个类以`;`号分割
     */
    private String interceptors;
}
