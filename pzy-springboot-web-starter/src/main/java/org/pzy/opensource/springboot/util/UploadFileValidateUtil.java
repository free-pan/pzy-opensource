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

package org.pzy.opensource.springboot.util;

import org.pzy.opensource.comm.exception.FileExtException;
import org.pzy.opensource.comm.exception.FileSizeException;
import org.pzy.opensource.comm.util.FileUtil;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 上传文件验证
 *
 * @author 潘志勇
 * @date 2019-01-15
 */
public class UploadFileValidateUtil {

    private UploadFileValidateUtil() {
    }

    /**
     * 验证上传文件的大小和后缀
     *
     * @param multipartFile 上传的文件
     * @param fileExt       支持的后缀
     * @param maxSize       最大文件大小
     * @throws FileExtException  当文件后缀未通过验证时,抛出该异常
     * @throws FileSizeException 当文件大小未通过验证时,抛出该异常
     */
    public static void validateFileExtAndSize(MultipartFile multipartFile, String fileExt, long maxSize) throws FileExtException, FileSizeException {
        String fileName = multipartFile.getOriginalFilename();
        FileUtil.fileExtValidate(fileName, fileExt);
        FileUtil.fileSizeValidate(multipartFile.getSize(), maxSize);
    }

    /**
     * 验证上传文件的大小
     *
     * @param multipartFile 上传的文件
     * @param maxSize       最大文件大小
     * @throws FileSizeException 当文件大小未通过验证时,抛出该异常
     */
    public static void validateFileExtAndSize(MultipartFile multipartFile, long maxSize) throws FileSizeException {
        String fileName = multipartFile.getOriginalFilename();
        FileUtil.fileSizeValidate(multipartFile.getSize(), maxSize);
    }

    /**
     * 验证上传文件的后缀
     *
     * @param multipartFile 上传的文件
     * @param fileExt       支持的后缀
     * @throws FileExtException 当文件后缀未通过验证时,抛出该异常
     */
    public static void validateFileExtAndSize(MultipartFile multipartFile, String fileExt) throws FileExtException {
        String fileName = multipartFile.getOriginalFilename();
        FileUtil.fileExtValidate(fileName, fileExt);
    }

    /**
     * 验证上传文件的大小和后缀
     *
     * @param multipartFile 上传的文件
     * @param fileExtList   支持的后缀
     * @param maxSize       最大文件大小
     * @throws FileExtException  当文件后缀未通过验证时,抛出该异常
     * @throws FileSizeException 当文件大小未通过验证时,抛出该异常
     */
    public static void validateFileExtAndSize(MultipartFile multipartFile, List<String> fileExtList, long maxSize) throws FileExtException, FileSizeException {
        String fileName = multipartFile.getOriginalFilename();
        for (String fileExt : fileExtList) {
            FileUtil.fileExtValidate(fileName, fileExt);
        }
        FileUtil.fileSizeValidate(multipartFile.getSize(), maxSize);
    }

    /**
     * 验证上传文件的后缀
     *
     * @param multipartFile 上传的文件
     * @param fileExtList   支持的后缀
     * @throws FileExtException 当文件后缀未通过验证时,抛出该异常
     */
    public static void validateFileExtAndSize(MultipartFile multipartFile, List<String> fileExtList) throws FileExtException {
        String fileName = multipartFile.getOriginalFilename();
        for (String fileExt : fileExtList) {
            FileUtil.fileExtValidate(fileName, fileExt);
        }
    }
}
