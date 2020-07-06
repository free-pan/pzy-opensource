package org.pzy.opensource.web.util;


import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;

/**
 * 实现controller文件下载的前置设置
 *
 * @author pzy
 * @date 2018/12/19
 */
public class FileDownloadSettingsUtil {

    private static final String DEFAULT_CHARSET = "UTF-8";

    private FileDownloadSettingsUtil() {
    }

    /**
     * 下载的HttpServletResponse设置(设置跨域支持,并将文件名设置到response header的Content-Disposition中)
     *
     * @param fileName 下载的文件名
     * @param response HttpServletResponse实例
     * @return 输出流(将要下载的文件输出到该输出流即可实现文件的下载)
     * @throws IOException IO输出操作出问题时,将抛出此异常
     */
    public static OutputStream downloadSettings(String fileName, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String encodeFileName = URLEncoder.encode(fileName, DEFAULT_CHARSET);
        response.reset();
        String reqOrigin = request.getHeader("origin");
        if (StringUtils.isBlank(reqOrigin)) {
            reqOrigin = request.getHeader("ORIGIN");
        }
        if (StringUtils.isBlank(reqOrigin)) {
            reqOrigin = request.getHeader("Origin");
        }
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        if (!StringUtils.isBlank(reqOrigin)) {
            response.setHeader("Access-Control-Allow-Origin", reqOrigin);
        }
        response.setHeader("Access-Control-Expose-Headers", "*");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Content-Disposition", "attachment;filename=" + encodeFileName);
        response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
        response.setHeader("Pragma", "no-cache");
        return response.getOutputStream();
    }

    /**
     * 将字节输入流中的内容输出到字节输出流中(完成之后,内部会关闭InputStream和OutputStream)
     *
     * @param bufferSize   缓冲大小
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
