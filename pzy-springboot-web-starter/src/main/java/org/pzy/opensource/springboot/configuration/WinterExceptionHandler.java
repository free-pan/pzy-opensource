package org.pzy.opensource.springboot.configuration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * WinterExceptionHandler
 *
 * @author pan
 * @date 4/11/20
 */
public interface WinterExceptionHandler {

    /**
     * 用于自定制异常处理的自定义扩展
     *
     * @param req      请求对象
     * @param response 响应对象
     * @param e        异常
     */
    default void customExtendsExceptoinHandler(HttpServletRequest req, HttpServletResponse response, Exception e) {

    }
}
