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

package org.pzy.opensource.springboot.configuration;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.pzy.opensource.comm.util.FileUtil;
import org.pzy.opensource.comm.util.JsonUtil;
import org.pzy.opensource.springboot.properties.SingleStaticDirMapping;
import org.pzy.opensource.springboot.properties.StaticMappingProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * json转换器配置(使用fastjson代替jackson)
 *
 * @author pan
 * @date 2019-12-14
 */
@Slf4j
@Configuration
@ConditionalOnMissingBean
@ConditionalOnProperty(name = "component.static-mapping.enable", havingValue = "true")
public class BaseWebMvcConfiguration implements WebMvcConfigurer {

    private static final String END_CHAR = "/";

    private static final String DEFAULT_SPLIT_CHART = ";";

    private StaticMappingProperties webStarterProperties;

    public BaseWebMvcConfiguration(StaticMappingProperties webStarterProperties) {
        log.debug("BaseWebMvcConfig初始化!");
        this.webStarterProperties = webStarterProperties;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        if (this.webStarterProperties.isEnable()) {
            log.debug("\r\n############# 已启用静态目录映射!");
            try {
                addStaticDirMapping(registry);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 添加静态资源目录映射
     *
     * @param registry
     */
    private void addStaticDirMapping(ResourceHandlerRegistry registry) throws JsonProcessingException {
        List<SingleStaticDirMapping> mappingList = webStarterProperties.getMappingList();
        if (!CollectionUtils.isEmpty(mappingList)) {
            StringBuilder debugInfo = new StringBuilder();
            for (SingleStaticDirMapping singleMapping : mappingList) {
                if (!StringUtils.isEmpty(singleMapping.getUri())) {
                    String dirs = singleMapping.getDirs();
                    String[] dirArr = convert(dirs);
                    registry.addResourceHandler(singleMapping.getUri()).addResourceLocations(dirArr);
                    debugInfo.append(singleMapping.getUri() + " --> " + JsonUtil.toJsonString(dirArr)).append("\r\n");
                }
            }
            if (debugInfo.length() > 0) {
                log.debug("\r\n映射的静态目录有:\r\n{}", debugInfo.toString());
            }
        }
    }

    private String[] convert(String dirs) {
        List<String> tmpList = new ArrayList<>();
        String[] tmpArr = dirs.split(DEFAULT_SPLIT_CHART);
        String tmp;
        String realDir = null;
        for (String singleDir : tmpArr) {
            if (!StringUtils.isEmpty(singleDir)) {
                tmp = singleDir.trim();
                if (!tmp.endsWith(BaseWebMvcConfiguration.END_CHAR)) {
                    tmp += BaseWebMvcConfiguration.END_CHAR;
                }
                if (tmp.startsWith(SingleStaticDirMapping.CUSTOMIZED_PREFIX)) {
                    tmp = tmp.replaceFirst(SingleStaticDirMapping.CUSTOMIZED_PREFIX, "");
                    File dir = FileUtil.mkdirInSystemTmpDir(tmp, false);
                    realDir = "file:" + dir.toString() + BaseWebMvcConfiguration.END_CHAR;
                } else if (tmp.startsWith(SingleStaticDirMapping.CLASSPATH_PREFIX)) {
                    String d = tmp.substring(SingleStaticDirMapping.CLASSPATH_PREFIX.length());
                    FileUtil.mkdirInClasspathDir(d, false);
                    realDir = tmp;
                } else if (tmp.startsWith(SingleStaticDirMapping.FILE_PREFIX)) {
                    String d = tmp.substring(SingleStaticDirMapping.CLASSPATH_PREFIX.length());
                    File f = new File(d);
                    if (!f.exists()) {
                        f.mkdirs();
                        log.debug("静态目录{}不存在,自动创建!", f);
                    }
                    realDir = tmp;
                } else {
                    if (!tmp.startsWith(SingleStaticDirMapping.HTTP_PREFIX)) {
                        throw new RuntimeException("静态目录映射配置错误!");
                    }

                }
                tmpList.add(realDir);
            }
        }
        if (!CollectionUtils.isEmpty(tmpList)) {
            String[] resultArr = new String[tmpList.size()];
            tmpList.toArray(resultArr);
            return resultArr;
        }
        return null;
    }

//    @Override
//    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
//        /*
//         先把JackSon的消息转换器删除.
//         备注: (1)源码分析可知，返回json的过程为:
//                    Controller调用结束后返回一个数据对象，for循环遍历conventers，找到支持application/json的HttpMessageConverter，然后将返回的数据序列化成json。
//                    具体参考org.springframework.web.servlet.mvc.method.annotation.AbstractMessageConverterMethodProcessor的writeWithMessageConverters方法
//               (2)由于是list结构，我们添加的fastjson在最后。因此必须要将jackson的转换器删除，不然会先匹配上jackson，导致没使用fastjson
//        */
//        for (int i = converters.size() - 1; i >= 0; i--) {
//            if (converters.get(i) instanceof MappingJackson2HttpMessageConverter) {
//                converters.remove(i);
//            }
//        }
//        FastJsonHttpMessageConverter fastJsonHttpMessageConverter = new FastJsonHttpMessageConverter();
//        //自定义fastjson配置
//        FastJsonConfig config = new FastJsonConfig();
//        config.setSerializerFeatures(
//                // 是否输出值为null的字段,默认为false,我们将它打开
//                SerializerFeature.WriteMapNullValue,
//                // 将Collection类型字段的字段空值输出为[]
//                SerializerFeature.WriteNullListAsEmpty,
//                // 将字符串类型字段的空值输出为空字符串
//                SerializerFeature.WriteNullStringAsEmpty,
//                // 将数值类型字段的空值输出为0
//                SerializerFeature.WriteNullNumberAsZero,
//                SerializerFeature.WriteDateUseDateFormat,
//                // 禁用循环引用
//                SerializerFeature.DisableCircularReferenceDetect
//        );
//        fastJsonHttpMessageConverter.setFastJsonConfig(config);
//        // 添加支持的MediaTypes;不添加时默认为*/*,也就是默认支持全部
//        // 但是MappingJackson2HttpMessageConverter里面支持的MediaTypes为application/json
//        // 参考它的做法, fastjson也只添加application/json的MediaType
//        List<MediaType> fastMediaTypes = new ArrayList<>();
//        fastMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
//        fastJsonHttpMessageConverter.setSupportedMediaTypes(fastMediaTypes);
//        converters.add(fastJsonHttpMessageConverter);
//    }
}
