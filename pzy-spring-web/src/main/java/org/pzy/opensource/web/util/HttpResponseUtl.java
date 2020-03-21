package org.pzy.opensource.web.util;

import org.apache.commons.lang3.StringUtils;
import org.pzy.opensource.comm.util.JsonUtil;
import org.pzy.opensource.domain.GlobalConstant;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;

/**
 * @author 潘志勇
 * @date 2019-02-06
 */
public class HttpResponseUtl {

    private static final String NO_CACHE = "no-cache";

    private HttpResponseUtl() {
    }

    /**
     * 通过spring获取HttpServletResponse对象<br/>
     *
     * <strong>前提条件: </strong>需要配置监听器 `org.springframework.web.context.request.RequestContextListener`
     *
     * @return
     */
    public static HttpServletResponse loadHttpServletResponse() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return servletRequestAttributes.getResponse();
    }

    /**
     * 禁用缓存
     *
     * @param response
     */
    public static void disableCache(HttpServletResponse response) {
        response.setDateHeader(HttpHeaders.EXPIRES, 0L);
        response.setHeader(HttpHeaders.CACHE_CONTROL, NO_CACHE);
        response.setHeader(HttpHeaders.PRAGMA, NO_CACHE);
    }

    /**
     * 允许跨域
     *
     * @param response
     */
    public static void allowHttpCross(HttpServletResponse response, HttpServletRequest httpServletRequest) {
        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, HttpRequestUtil.extractOrigin(httpServletRequest));
        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "*");
        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, "*");
        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
        // 将预检请求的结果缓存20分钟
        response.setHeader(HttpHeaders.ACCESS_CONTROL_MAX_AGE, "1200");
    }

    /**
     * 允许跨域
     *
     * @param response
     */
    public static void allowCross(ServletResponse response, ServletRequest request) {
        allowHttpCross((HttpServletResponse)response, (HttpServletRequest)request);
    }


    /**
     * 输出gif图片设置
     *
     * @param response
     * @param request
     */
    public static void gifPicSettings(HttpServletResponse response, HttpServletRequest request) {
        // 禁用缓存
        disableCache(response);
        // 允许跨域
        allowCross(response, request);
        // 设置输入出类型为gif
        response.setContentType(MediaType.IMAGE_GIF_VALUE);
    }


    /**
     * 输出json设置
     *
     * @param response
     * @param request
     */
    public static void jsonSettings(HttpServletResponse response, HttpServletRequest request) {
        // 设置输入出类型为json
        response.setCharacterEncoding(GlobalConstant.DEFAULT_CHARSET);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    }


    /**
     * 直接将obj转换为json字符串,输出. http的响应状态码为200
     *
     * @param request
     * @param response
     * @param obj
     * @param allowCross 是否允许跨域
     */
    public static void printJson(HttpServletRequest request, HttpServletResponse response, Object obj, boolean allowCross) {
        printJson(request, response, obj, HttpStatus.OK, allowCross);
    }

    /**
     * 直接将obj转换为json字符串,输出
     *
     * @param request
     * @param response
     * @param obj
     * @param httpStatus http的响应状态码
     * @param allowCross 是否允许跨域
     */
    public static void printJson(HttpServletRequest request, HttpServletResponse response, Object obj, HttpStatus httpStatus, boolean allowCross) {
        jsonSettings(response, request);
        response.setStatus(httpStatus.value());
        if (allowCross) {
            allowCross(response, request);
        }
        PrintWriter out = null;
        try {
            out = response.getWriter();
            if (null != obj) {
                out.write(JsonUtil.toJsonString(obj));
            }
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    /**
     * 输出jpg图片设置
     *
     * @param request
     * @param response
     */
    public static void jpgPicSettings(HttpServletResponse response, HttpServletRequest request) {
        // 禁用缓存
        disableCache(response);
        // 允许跨域
        allowCross(response, request);
        // 设置输入出类型为gif
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
    }

    /**
     * 下载的HttpServletResponse设置(设置跨域支持,并将文件名设置到response header的Content-Disposition中)
     *
     * @param fileName 下载的文件名
     * @param response HttpServletResponse实例
     * @return 输出流(将要下载的文件输出到该输出流即可实现文件的下载)
     * @throws IOException
     */
    public static OutputStream downloadSettings(String fileName, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String encodeFileName = URLEncoder.encode(fileName, GlobalConstant.DEFAULT_CHARSET);
        response.reset();
        String reqOrigin = request.getHeader("origin");
        if (StringUtils.isEmpty(reqOrigin)) {
            reqOrigin = request.getHeader("ORIGIN");
        }
        if (StringUtils.isEmpty(reqOrigin)) {
            reqOrigin = request.getHeader("Origin");
        }
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        if (!StringUtils.isEmpty(reqOrigin)) {
            response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, reqOrigin);
        }
        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, "*");
        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + encodeFileName);
        response.setHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "Content-Disposition");
        response.setHeader(HttpHeaders.PRAGMA, NO_CACHE);
        return response.getOutputStream();
    }

    /**
     * 将字节输入流中的内容输出到字节输出流中(完成之后,内部会关闭InputStream和OutputStream)
     *
     * @param bufferSize   缓冲大小(单位:byte)
     * @param inputStream  字节输入流(来源数据)
     * @param outputStream 字节输出流(输出目的地)
     */
    public static void inputStreamContentWrite2OutputStream(int bufferSize, InputStream inputStream, OutputStream outputStream) {
        byte[] b = new byte[bufferSize];
        int len;
        try {
            while ((len = inputStream.read(b)) > 0) {
                outputStream.write(b, 0, len);
            }
            outputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException("将字节流输入流中的数据,写到字节输出流时出现问题!", e);
        } finally {
            try {
                outputStream.close();
            } catch (IOException e) {
                throw new RuntimeException("字节输出流关闭异常!", e);
            }
            try {
                inputStream.close();
            } catch (IOException e) {
                throw new RuntimeException("字节输入流关闭异常!", e);
            }
        }
    }
}
