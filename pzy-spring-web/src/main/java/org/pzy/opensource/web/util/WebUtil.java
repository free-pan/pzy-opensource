package org.pzy.opensource.web.util;

import org.pzy.opensource.web.domain.bo.RedirectView;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * 代码来自shiro-web
 *
 * @author pan
 * @date 4/12/20
 */
public class WebUtil {

    private WebUtil() {
    }

    /**
     * 转到一个新请求, 同时保持当前参数
     *
     * @param request  the servlet request.
     * @param response the servlet response.
     * @param url      需要转入的新请求
     * @throws java.io.IOException if thrown by response methods.
     */
    public static void issueRedirect(ServletRequest request, ServletResponse response, String url) throws IOException {
        issueRedirect(request, response, url, null, true, true);
    }

    /**
     * Redirects the current request to a new URL based on the given parameters.
     *
     * @param request          the servlet request.
     * @param response         the servlet response.
     * @param url              the URL to redirect the user to.
     * @param queryParams      a map of parameters that should be set as request parameters for the new request.
     * @param contextRelative  true if the URL is relative to the servlet context path, or false if the URL is absolute.
     * @param http10Compatible whether to stay compatible with HTTP 1.0 clients.
     * @throws java.io.IOException if thrown by response methods.
     */
    public static void issueRedirect(ServletRequest request, ServletResponse response, String url, Map queryParams, boolean contextRelative, boolean http10Compatible) throws IOException {
        RedirectView view = new RedirectView(url, contextRelative, http10Compatible);
        view.renderMergedOutputModel(queryParams, toHttp(request), toHttp(response));
    }

    /**
     * <p>A convenience method that merely casts the incoming <code>ServletRequest</code> to an
     * <code>HttpServletRequest</code>:
     *
     * <p><code>return (HttpServletRequest)request;</code>
     *
     * Logic could be changed in the future for logging or throwing an meaningful exception in
     * non HTTP request environments (e.g. Portlet API).
     *
     * @param request the incoming ServletRequest
     * @return the <code>request</code> argument casted to an <code>HttpServletRequest</code>.
     */
    public static HttpServletRequest toHttp(ServletRequest request) {
        return (HttpServletRequest) request;
    }

    /**
     * <p>A convenience method that merely casts the incoming <code>ServletResponse</code> to an
     * <code>HttpServletResponse</code>:
     *
     * <p><code>return (HttpServletResponse)response;</code>
     *
     * <p>Logic could be changed in the future for logging or throwing an meaningful exception in
     * non HTTP request environments (e.g. Portlet API).
     *
     * @param response the outgoing ServletResponse
     * @return the <code>response</code> argument casted to an <code>HttpServletResponse</code>.
     */
    public static HttpServletResponse toHttp(ServletResponse response) {
        return (HttpServletResponse) response;
    }
}
