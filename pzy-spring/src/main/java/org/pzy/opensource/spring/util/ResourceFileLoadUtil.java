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

package org.pzy.opensource.spring.util;

import org.pzy.opensource.comm.util.InputStreamUtil;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * 资源文件加载帮助类
 *
 * @author pan
 * @date 2019-04-10
 */
public class ResourceFileLoadUtil {

    private ResourceFileLoadUtil() {
    }

    /**
     * 判断配置文件是否存在<br>
     * configFile支持的写法: <br>
     * file:C:/tmp/test.txt, file:/pan/tmp/test.properties, classpath:dbtest/test.xml, classpath:test.json<br>
     *
     * @param configFile 配置文件路径
     */
    public static boolean configFileExists(String configFile) {
        Resource resource = new DefaultResourceLoader().getResource(configFile);
        return resource.exists();
    }

    /**
     * 资源文件加载<br>
     * resourceFile支持的写法:
     * <ul>
     * <li>文件物理路径: file:C:/test.dat</li>
     * <li>类路径: classpath:test.dat</li>
     * <li>WEB路径: WEB-INF/test.dat</li>
     * </ul>
     *
     * @param resourceFile 资源文件路径
     * @return 资源对象
     */
    public static Resource loadAsResource(String resourceFile) {
        return new DefaultResourceLoader().getResource(resourceFile);
    }

    /**
     * 资源文件加载<br>
     * resourceFile支持的写法:
     * <ul>
     * <li>文件物理路径: file:C:/test.dat</li>
     * <li>类路径: classpath:test.dat</li>
     * <li>WEB路径: WEB-INF/test.dat</li>
     * </ul>
     *
     * @param resourceFile 资源文件路径
     * @return 文件对象
     * @throws IOException 资源文件不存在时,抛出该异常
     */
    public static File loadAsFile(String resourceFile) throws IOException {
        return loadAsResource(resourceFile).getFile();
    }

    /**
     * 资源文件加载<br>
     * resourceFile支持的写法:
     * <ul>
     * <li>文件物理路径: file:C:/test.dat</li>
     * <li>类路径: classpath:test.dat</li>
     * <li>WEB路径: WEB-INF/test.dat</li>
     * </ul>
     *
     * @param resourceFile 资源文件路径
     * @return 字节流
     * @throws IOException 资源文件不存在时,抛出该异常
     */
    public static InputStream loadAsInputStream(String resourceFile) throws IOException {
        return loadAsResource(resourceFile).getInputStream();
    }

    /**
     * 资源文件加载<br>
     * <p>
     * resourceFile支持的写法:
     * <ul>
     * <li>文件物理路径: file:C:/test.dat</li>
     * <li>类路径: classpath:test.dat</li>
     * <li>WEB路径: WEB-INF/test.dat</li>
     * </ul>
     *
     * @param resourceFile 资源文件路径
     * @param encoding     资源文件编码
     * @return 资源文件的内容字符串
     * @throws IOException
     */
    public static String loadAsString(String resourceFile, String encoding) throws IOException {
        return InputStreamUtil.toString(loadAsInputStream(resourceFile), encoding);
    }

    /**
     * 资源文件加载<br>
     * <p>
     * resourceFile支持的写法:
     * <ul>
     * <li>文件物理路径: file:C:/test.properties</li>
     * <li>类路径: classpath:test.properties</li>
     * <li>WEB路径: WEB-INF/test.properties</li>
     * </ul>
     *
     * @param propertiesFile properties文件路径(只支持properties文件)
     * @param encoding       资源文件编码
     * @return Properties对象
     * @throws IOException
     */
    public static Properties readAsProperties(String propertiesFile, String encoding) throws IOException {
        InputStream inputStream = loadAsInputStream(propertiesFile);
        Properties properties = new Properties();
        properties.load(new InputStreamReader(inputStream, encoding));
        return properties;
    }

    /**
     * 读取yaml配置文件,并将数据设置到type实例对应的属性中. 当资源文件不存在时会抛出RuntimeException<br>
     * yamlFile支持的写法: <br>
     * file:C:/tmp/test.yml, file:/pan/tmp/test.yaml, classpath:dbtest/test.yml, classpath:test.yaml<br>
     *
     * @param yamlFile yaml配置文件(.yaml或.yml)配置文件必须以UTF-8编码
     * @param type     类型.该类必须是public的
     * @param <T>      返回类型
     * @return 当资源文件的内容为空时, 返回结果为null
     */
    public static <T> T readYamlFile(String yamlFile, Class<T> type) throws IOException {
        Yaml yaml = new Yaml();
        InputStream inputStream = loadAsInputStream(yamlFile);
        return yaml.loadAs(inputStream, type);
    }
}
