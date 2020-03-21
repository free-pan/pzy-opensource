/*
 * Copyright (c) [2019] [潘志勇]
 *    [pzy-opensource] is licensed under the Mulan PSL v1.
 *    You can use this software according to the terms and conditions of the Mulan PSL v1.
 *    You may obtain a copy of Mulan PSL v1 at:
 *       http://license.coscl.org.cn/MulanPSL
 *    THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND, EITHER EXPRESS OR
 *    IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT, MERCHANTABILITY OR FIT FOR A PARTICULAR
 *    PURPOSE.
 *    See the Mulan PSL v1 for more details.
 */

package org.pzy.opensource.comm.util;


import org.apache.commons.io.IOUtils;
import org.pzy.opensource.domain.GlobalConstant;

import java.io.*;


/**
 * @author pan
 */
public class InputStreamUtil {

    public static void closeInputStream(InputStream inputStream) {
        if (null != inputStream) {
            try {
                inputStream.close();
            } catch (IOException e) {
                throw new RuntimeException("流关闭异常!", e);
            }
        }
    }

    /**
     * 从文件的物理绝对路径获取输入流
     *
     * @param filePath 文件绝对路径
     * @return 文件输入流
     * @throws FileNotFoundException 当在指定路径找不到该文件时抛出此异常
     */
    public static InputStream getInputStreamFromPhysicsAbsolutePath(String filePath)
            throws FileNotFoundException {
        File file = new File(filePath);
        return new FileInputStream(file);
    }

    /**
     * 获取指定jar包的指定文件
     *
     * @param jarClass 指定jar包的一个class实例
     * @param fileName 要读取的jar包中的文件
     * @return FileNotFoundException 当在指定路径找不到该文件时抛出此异常
     */
    public static InputStream getJarFileInputStream(Class<?> jarClass, String fileName) {
        return jarClass.getResourceAsStream(fileName);
    }

    /**
     * 将字节流转换为字符串
     *
     * @param inputStream 字节流
     * @param encoding    字符编码
     * @return 字符串结果
     * @throws IOException
     */
    public static String toString(InputStream inputStream, String encoding) throws IOException {
        return IOUtils.toString(inputStream, encoding);
    }

    /**
     * 获取cls表示的类所在项目的绝对路径
     *
     * @param cls
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String getClassProjectPath(Class<?> cls)
            throws UnsupportedEncodingException {
        String path = cls.getResource("/").getPath();
        return java.net.URLDecoder.decode(path, GlobalConstant.DEFAULT_CHARSET);
    }

    /**
     * 获取cls表示类所在目录的绝对路径
     *
     * @param cls
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String getClassAbsolutePath(Class<?> cls)
            throws UnsupportedEncodingException {
        String path = cls.getResource("").getPath();
        return java.net.URLDecoder.decode(path, GlobalConstant.DEFAULT_CHARSET);
    }

    /**
     * 获取src目录下指定文件的InputStream
     *
     * @param fileName 文件名
     * @return 文件字节流
     * @throws IOException
     */
    public static InputStream getSrcFileInputStream(String fileName)
            throws IOException {
        return InputStreamUtil.class.getClassLoader().getResource(fileName).openStream();
    }

    /**
     * 获取项目的src/test/resources目录下的文件(只有在使用idea或eclipse打开项目时,才能正确获取到)
     *
     * @param fileName 文件名
     * @return 文件字节流
     * @throws FileNotFoundException 当文件不存在时,抛出此异常
     */
    public static InputStream getTestResourcesInputStream(String fileName) throws FileNotFoundException {
        String tmpFileName = fileName.trim();
        // 项目根目录
        String projectDir = System.getProperty("user.dir");
        String tmp = projectDir + "/src/test/resources";
        String fileSperator = "/";
        if (tmpFileName.startsWith(fileSperator) || tmpFileName.startsWith(File.separator)) {
            tmp = tmp + tmpFileName;
        } else {
            tmp = tmp + fileSperator + tmpFileName;
        }
        return new FileInputStream(new File(tmp));
    }

}
