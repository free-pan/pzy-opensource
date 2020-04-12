package org.pzy.opensource.web.errorhandler;

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
     * 用于子类对每个异常处理进行自定义扩展
     *
     * @param req      请求对象
     * @param response 响应对象
     * @param e        异常
     * @throws Exception 抛出异常
     */
    default void customExtendsExceptoinHandler(HttpServletRequest req, HttpServletResponse response, Exception e) throws Exception {

    }
}
