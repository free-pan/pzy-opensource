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

package org.pzy.opensource.springboot.factory;

import lombok.extern.slf4j.Slf4j;
import org.pzy.opensource.comm.util.JsonUtil;
import org.pzy.opensource.springboot.domain.bo.CrossInfoBO;
import org.springframework.util.CollectionUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

/**
 * 跨域过滤器构建工厂
 *
 * @author 潘志勇
 * @date 2019-01-14
 */
@Slf4j
public class CorsFilterFactory {

    private CorsFilterFactory() {
    }

    /**
     * 创建跨域支持过滤器实例
     *
     * @param crosInfoList
     * @return
     */
    public static CorsFilter newInstance(List<CrossInfoBO> crosInfoList) {
        if (!CollectionUtils.isEmpty(crosInfoList)) {
            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
            for (CrossInfoBO entry : crosInfoList) {
                source.registerCorsConfiguration(entry.getUri(), CorsConfigurationFactory.newInstance(entry));
            }
            log.debug("\r\n############# 已启用自定义跨域配置!{}", JsonUtil.toPrettyJsonString(crosInfoList));
            return new CorsFilter(source);
        } else {
            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
            CrossInfoBO crosInfo = new CrossInfoBO();
            crosInfo.setUri("/**");
            source.registerCorsConfiguration("/**", CorsConfigurationFactory.newInstance(crosInfo));
            log.debug("\r\n############# 已启用默认跨域配置![所有接口都允许跨域访问]{}", JsonUtil.toPrettyJsonString(crosInfo));
            return new CorsFilter(source);
        }
    }
}
