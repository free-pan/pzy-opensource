package org.pzy.opensource.comm.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 异常信息提取帮助类
 *
 * @author pan
 * @date 4/11/20
 */
public class ExceptionUtil {

    private ExceptionUtil() {
    }

    /**
     * 提取异常的堆栈信息
     *
     * @param e 异常
     * @return 异常堆栈信息字符串
     */
    public static String extractExceptionStackTrace(Exception e) throws IOException {
        PrintWriter pw = null;
        StringWriter sw = null;
        try {
            sw = new StringWriter();
            pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            return sw.toString();
        } finally {
            if (null != pw) {
                pw.close();
            }
            if (null != sw) {
                sw.close();
            }
        }
    }
}
