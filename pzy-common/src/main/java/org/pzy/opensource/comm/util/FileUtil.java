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

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.pzy.opensource.comm.exception.FileExtException;
import org.pzy.opensource.comm.exception.FileSizeException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @author pzy
 * @date 2018/12/20
 */
@Slf4j
public class FileUtil {

    private FileUtil() {
    }

    /**
     * 验证fileName的后缀名是否为ext. 如果不是,则抛出FileExtException异常
     *
     * @param fileName 文件名
     * @param ext      后缀名(不要包含`.`号)
     * @throws FileExtException 当验证失败时,抛出该异常
     */
    public static void fileExtValidate(String fileName, String ext) throws FileExtException {
        if (StringUtils.isBlank(fileName)) {
            throw new FileExtException(String.format("文件名不是以[%s]结尾!", ext));
        }
        String tmp = extractFileExt(fileName.trim().toLowerCase());
        String tmpExt = ext.trim().toLowerCase();
        if (!tmpExt.equalsIgnoreCase(tmp)) {
            throw new FileExtException(String.format("文件名不是以[%s]结尾!", ext));
        }
    }

    /**
     * 验证文件大小是否超过了maxFileSize的大小,如果超过,则抛出FileSizeException异常
     *
     * @param fileSize    文件实际大小
     * @param maxFileSize 允许的最大大小
     * @throws FileSizeException 当验证失败时,抛出该异常
     */
    public static void fileSizeValidate(long fileSize, long maxFileSize) throws FileSizeException {
        if (fileSize > maxFileSize) {
            throw new FileSizeException(String.format("文件大小超过[%s]!", maxFileSize));
        }
    }

    /**
     * 获取文件后缀名
     *
     * @param fileName 带后缀的文件名
     * @return 文件后缀
     * @throws FileExtException 未获取到文件后缀名时,抛出该异常
     */
    public static String extractFileExt(String fileName) throws FileExtException {
        if (StringUtils.isBlank(fileName)) {
            throw new FileExtException("未找到文件后缀名!");
        }
        String tmp = fileName.trim();
        int pos = tmp.lastIndexOf(".");
        if (pos == -1) {
            throw new FileExtException("未找到文件后缀名!");
        }
        if (pos == (tmp.length() - 1)) {
            throw new FileExtException("未找到文件后缀名!");
        }
        return tmp.substring(pos + 1).trim();
    }

    /**
     * 获取系统临时目录
     *
     * @return 系统的临时目录地址(全路径)
     */
    public static String getSystemTmpDir() {
        return System.getProperty("java.io.tmpdir");
    }

    /**
     * 获取类加载目录(classpath目录)
     *
     * @return 系统的临时目录地址(全路径)
     */
    public static String getClasspathDir() {
        File f = new File(FileUtil.class.getResource("/").getPath());
        return f.toString();
    }

    /**
     * 目录删除(会将目录下的文件和子目录一并删除)
     *
     * @param directory 待删除目录
     * @throws IOException 如果目录删除失败会抛出此异常
     */
    public static void deleteDirectory(final File directory) throws IOException {
        if (!directory.exists()) {
            return;
        }

        if (!isSymlink(directory)) {
            cleanDirectory(directory);
        }

        if (!directory.delete()) {
            final String message =
                    "Unable to delete directory " + directory + ".";
            throw new IOException(message);
        }
    }

    /**
     * Cleans a directory without deleting it.
     *
     * @param directory directory to clean
     * @throws IOException              in case cleaning is unsuccessful
     * @throws IllegalArgumentException if {@code directory} does not exist or is not a directory
     */
    private static void cleanDirectory(final File directory) throws IOException {
        if (!directory.exists()) {
            final String message = directory + " does not exist";
            throw new IllegalArgumentException(message);
        }

        if (!directory.isDirectory()) {
            final String message = directory + " is not a directory";
            throw new IllegalArgumentException(message);
        }

        final File[] files = directory.listFiles();
        if (files == null) {
            throw new IOException("Failed to list contents of " + directory);
        }

        IOException exception = null;
        for (File file : files) {
            try {
                forceDelete(file);
            } catch (IOException ioe) {
                exception = ioe;
            }
        }

        if (null != exception) {
            throw exception;
        }
    }

    /**
     * Deletes a file. If file is a directory, delete it and all sub-directories.
     * <p>
     * The difference between File.delete() and this method are:
     * <ul>
     * <li>A directory to be deleted does not have to be empty.</li>
     * <li>You get exceptions when a file or directory cannot be deleted.
     * (java.io.File methods returns a boolean)</li>
     * </ul>
     *
     * @param file file or directory to delete, must not be {@code null}
     * @throws NullPointerException  if the directory is {@code null}
     * @throws FileNotFoundException if the file was not found
     * @throws IOException           in case deletion is unsuccessful
     */
    private static void forceDelete(final File file) throws IOException {
        if (file.isDirectory()) {
            deleteDirectory(file);
        } else {
            final boolean filePresent = file.exists();
            if (!file.delete()) {
                if (!filePresent) {
                    throw new FileNotFoundException("文件不存在: " + file);
                }
                final String message =
                        "无法对文件进行删除操作: " + file;
                throw new IOException(message);
            }
        }
    }

    private static boolean isSymlink(File file) throws IOException {
        if (file == null) {
            throw new NullPointerException("File must not be null");
        }
        //FilenameUtils.isSystemWindows()
        char fileSeperator = '\\';
        if (File.separatorChar == fileSeperator) {
            return false;
        }
        File fileInCanonicalDir;
        if (file.getParent() == null) {
            fileInCanonicalDir = file;
        } else {
            File canonicalDir = file.getParentFile().getCanonicalFile();
            fileInCanonicalDir = new File(canonicalDir, file.getName());
        }

        if (fileInCanonicalDir.getCanonicalFile().equals(fileInCanonicalDir.getAbsoluteFile())) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 在系统临时目录下创建文件夹
     * <br>
     * dirPath支持的值格式(子目录数量无限定).<br>
     * 如:<br>
     * 值支持的格式如: test[创建test目录], test/demo[创建test目录,并在test目录下创建demo子目录], test/demo/simple[创建test目录,并在test目录下创建demo子目录,并在demo目录下创建simple子目录]
     *
     * @param dirPath        需要在系统临时目录下创建的文件夹
     * @param ifExistsDelete 如果文件夹已存在是否删除
     * @return 文件夹的 file 对象
     */
    public static File mkdirInSystemTmpDir(String dirPath, boolean ifExistsDelete) {
        String systemTmpDirStr = FileUtil.getSystemTmpDir();
        File systemTmpDir = new File(systemTmpDirStr);
        File dir = new File(systemTmpDir, dirPath);
        if (dir.exists() && ifExistsDelete) {
            try {
                FileUtil.deleteDirectory(dir);
                log.debug("系统临时目录下已存在:" + dirPath + "目录,强制删除!");
            } catch (IOException e) {
                throw new RuntimeException("目录删除失败!" + dir.toString(), e);
            }
        }
        if (!dir.exists()) {
            dir.mkdirs();
            log.debug("系统临时目录下不存在:" + dirPath + "目录,自动创建!");
        }
        log.debug("目录实际位置:{}", dir.toString());
        return dir;
    }

    /**
     * 在类加载目录下创建文件夹
     * <br>
     * dirPath支持的值格式(子目录数量无限定).<br>
     * 如:<br>
     * 值支持的格式如: test[创建test目录], test/demo[创建test目录,并在test目录下创建demo子目录], test/demo/simple[创建test目录,并在test目录下创建demo子目录,并在demo目录下创建simple子目录]
     *
     * @param dirPath        需要在系统临时目录下创建的文件夹
     * @param ifExistsDelete 如果文件夹已存在是否删除
     * @return 文件夹的 file 对象
     */
    public static File mkdirInClasspathDir(String dirPath, boolean ifExistsDelete) {
        String classpathDirStr = FileUtil.getClasspathDir();
        File classpathDir = new File(classpathDirStr);
        File dir = new File(classpathDir, dirPath);
        if (dir.exists() && ifExistsDelete) {
            try {
                FileUtil.deleteDirectory(dir);
                log.debug("classpath目录下已存在:" + dirPath + "目录,强制删除!");
            } catch (IOException e) {
                throw new RuntimeException("目录删除失败!" + dir.toString(), e);
            }
        }
        if (!dir.exists()) {
            dir.mkdirs();
            log.debug("classpath目录下不存在:" + dirPath + "目录,自动创建!");
        }
        log.debug("目录实际位置:{}", dir.toString());
        return dir;
    }

    /**
     * 删除系统临时目录下的文件及
     *
     * @param dirPath 待删除目录
     */
    public static void removeDirInSystemTmpDir(String dirPath) {
        if (StringUtils.isBlank(dirPath)) {
            return;
        }
        String systemTmpDirStr = FileUtil.getSystemTmpDir();
        File systemTmpDir = new File(systemTmpDirStr);
        File dir = new File(systemTmpDir, dirPath);
        if (dir.exists()) {
            try {
                FileUtil.deleteDirectory(dir);
            } catch (IOException e) {
                throw new RuntimeException("系统临时目录下的文件夹:" + dirPath + "删除失败!", e);
            }
        }
        log.debug(dir.toString() + "目录已经成功删除!");
    }

    /**
     * 如果当前程序以 jar 包运行, 那么通过该方法可以得到 jar 包所在目录
     *
     * @return jar 包所在目录的 file 对象
     */
    public static File getJarDir() {
        // path值: /xxx/xxx.jar这种形式
        String path = System.getProperty("java.class.path");
        int firstIndex = path.lastIndexOf(System.getProperty("path.separator")) + 1;
        int lastIndex = path.lastIndexOf(File.separator) + 1;
        path = path.substring(firstIndex, lastIndex);
        return new File(path);
    }
}
