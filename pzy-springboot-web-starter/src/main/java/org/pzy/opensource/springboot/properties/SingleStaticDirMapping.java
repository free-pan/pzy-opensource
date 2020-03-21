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

package org.pzy.opensource.springboot.properties;

/**
 * 单个静态目录映射
 *
 * @author pzy
 * @date 2018/12/20
 */
public class SingleStaticDirMapping {

    /**
     * 自定义前缀 systemTmpDir: 表示系统的临时目录
     */
    public static final String CUSTOMIZED_PREFIX = "systemTmpDir:";
    /**
     * 表示类加载目录的前缀
     */
    public static final String CLASSPATH_PREFIX = "classpath:";
    /**
     * 表示物理目录的前缀
     */
    public static final String FILE_PREFIX = "file:";
    public static final String HTTP_PREFIX = "http:";
    /**
     * 映射的 uri
     * 如: /upload/**, /download/**
     */
    private String uri;

    /**
     * uri 映射的目录(多个目录使用;号分割)
     * <br/>
     * 支持的格式: classpath:/xxx/xx/, file:D:/xxx/xx/, systemTmpDir:/xxx/xx/, http://xxx.com.xx/
     * <br/>
     * `systemTmpDir:`表示文件夹在系统的临时目录下,如果系统临时文件夹下不存在这个目录,则会被自动创建
     */
    private String dirs;

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }


    public String getDirs() {
        return dirs;
    }

    public void setDirs(String dirs) {
        this.dirs = dirs;
    }
}
