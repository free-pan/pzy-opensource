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

package org.pzy.opensource.mybatisplus.configuration;

import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.pzy.opensource.comm.mapstruct.StringDataMapper;
import org.pzy.opensource.domain.GlobalConstant;
import org.pzy.opensource.mybatisplus.basemapper.WinterSqlInjector;
import org.pzy.opensource.mybatisplus.idgenerator.PzyMybatisIdGenerator;
import org.pzy.opensource.mybatisplus.objecthandler.CreatorEditorMetaObjectHandler;
import org.pzy.opensource.mybatisplus.util.SpringUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author pan
 * @date 2019-12-11
 */
@Configuration
@Slf4j
public class WinterMybatisPlusAutoConfig {

    @Bean
    public PaginationInterceptor paginationInterceptor() {
        log.debug("已启用mybatis plus的分页插件!");
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        // 设置请求的页面大于最大页后操作， true调回到首页，false 继续请求  默认false
        paginationInterceptor.setOverflow(true);
        // 设置最大单页限制数量，默认 500 条，-1 不受限制
        paginationInterceptor.setLimit(GlobalConstant.MAX_PAGE_SIZE);
        return paginationInterceptor;
    }

    @Bean
    public IdentifierGenerator idGenerator() {
        log.debug("mybatis plus已启用自定义的ID生成器!");
        return new PzyMybatisIdGenerator();
    }

    @Bean
    public CreatorEditorMetaObjectHandler creatorEditorMetaObjectHandler() {
        log.debug("mybatis plus已启用操作人信息自动填充器!");
        return new CreatorEditorMetaObjectHandler();
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

    @Bean
    @ConditionalOnMissingBean
    SpringUtil springUtil() {
        return SpringUtil.getInstance();
    }

    /**
     * 自定义 SqlInjector, 里面包含自定义的全局方法
     *
     * @return
     */
    @Bean
    WinterSqlInjector winterSqlInjector() {
        return new WinterSqlInjector();
    }
}
