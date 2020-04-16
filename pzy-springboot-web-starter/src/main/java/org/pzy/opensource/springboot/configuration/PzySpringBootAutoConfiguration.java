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

import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import lombok.extern.slf4j.Slf4j;
import org.pzy.opensource.comm.mapstruct.StringDataMapper;
import org.pzy.opensource.comm.util.WinterSnowflake;
import org.pzy.opensource.comm.util.WinterSnowflakeUtil;
import org.pzy.opensource.domain.enums.LocalDatePatternEnum;
import org.pzy.opensource.springboot.errorhandler.DefaultWinterExceptionHandlerImpl;
import org.pzy.opensource.springboot.factory.CorsFilterFactory;
import org.pzy.opensource.springboot.properties.CrossPropeties;
import org.pzy.opensource.springboot.properties.SnowflakeProperties;
import org.pzy.opensource.springboot.properties.StaticMappingProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.util.CollectionUtils;
import org.springframework.web.filter.CorsFilter;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * @author pan
 * @date 2019-12-14
 */
@EnableConfigurationProperties({StaticMappingProperties.class, CrossPropeties.class, SnowflakeProperties.class})
@Import({Swagger2Configuration.class, BaseWebMvcConfiguration.class})
@EnableSwagger2
@Slf4j
@Configuration
public class PzySpringBootAutoConfiguration {

    @Autowired
    private CrossPropeties crossPropeties;
    @Autowired
    private StaticMappingProperties staticMappingProperties;
    @Autowired
    private SnowflakeProperties snowflakeProperties;

    @Value("${spring.jackson.date-format:yyyy-MM-dd HH:mm:ss}")
    private String pattern;

    @Bean
    @ConditionalOnMissingBean
    public LocalDateTimeSerializer localDateTimeDeserializer() {
        return new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(pattern));
    }

    @Bean
    @ConditionalOnMissingBean
    public LocalDateSerializer localDateDeserializer() {
        return new LocalDateSerializer(DateTimeFormatter.ofPattern(LocalDatePatternEnum.DATE_PATTERN.getCode()));
    }

    @Bean
    @ConditionalOnMissingBean
    public LocalTimeSerializer localTimeDeserializer() {
        return new LocalTimeSerializer(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }

    @Bean
    @ConditionalOnMissingBean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return builder -> builder
                .serializerByType(LocalDateTime.class, localDateTimeDeserializer())
                .serializerByType(LocalDate.class, localDateDeserializer())
                .serializerByType(LocalTime.class, localTimeDeserializer());
    }

    /**
     * id生成器配置
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(value = WinterSnowflakeUtil.class)
    @ConditionalOnProperty(name = "component.snowflake.enable", havingValue = "true")
    public WinterSnowflakeUtil winterSnowflakeHelper() {
        if (this.snowflakeProperties.getWorkerId() <= 0) {
            throw new RuntimeException("id生成器的客户端id必须大于0!");
        }
        if (this.snowflakeProperties.getDataCenterId() <= 0) {
            throw new RuntimeException("id生成器的数据中心id必须大于0!");
        }
        HashSet<String> idGeneratorColl = this.snowflakeProperties.getIdGeneratorColl();
        if (CollectionUtils.isEmpty(idGeneratorColl)) {
            throw new RuntimeException("请指定id生成器标识!");
        }
        Map<String, WinterSnowflake> winterSnowflakeMap = new HashMap<>();
        for (String idGeneratorFlag : idGeneratorColl) {
            winterSnowflakeMap.put(idGeneratorFlag, new WinterSnowflake(this.snowflakeProperties.getWorkerId(), this.snowflakeProperties.getDataCenterId()));
        }
        log.debug("基于Twitter Snowflake算法的id生成器已启用!");
        log.debug("成功构造如下标识的id生成器:[" + idGeneratorColl + "], 可以通过 WinterSnowflakeUtil 的静态方法直接使用!");
        return WinterSnowflakeUtil.newInstance(winterSnowflakeMap);
    }

    /**
     * 跨域配置
     *
     * @return
     */
    @Bean
    @ConditionalOnProperty(name = "component.cross.enable", havingValue = "true")
    @ConditionalOnMissingBean(CorsFilter.class)
    public CorsFilter corsFilter() {
        log.debug("pzy组件:cross启用!");
        return CorsFilterFactory.newInstance(crossPropeties.getList());
    }

    /**
     * 全局异常处理器
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    DefaultWinterExceptionHandlerImpl winterExceptionHandler() {
        return new DefaultWinterExceptionHandlerImpl();
    }

    /**
     * 将string处理mapper注入到spring容器, 让mapStruct在spring模式下可以直接引用到.
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    StringDataMapper stringDataMapper() {
        return new StringDataMapper();
    }

}
