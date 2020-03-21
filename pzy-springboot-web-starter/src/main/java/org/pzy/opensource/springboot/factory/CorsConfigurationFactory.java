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


import org.pzy.opensource.domain.GlobalConstant;
import org.pzy.opensource.springboot.domain.bo.CrossInfoBO;
import org.springframework.util.StringUtils;
import org.springframework.web.cors.CorsConfiguration;

/**
 * CorsConfiguration实例工厂
 *
 * @author 潘志勇
 * @date 2019-01-14
 */
public class CorsConfigurationFactory {

    private CorsConfigurationFactory() {
    }

    /**
     * 创建CorsConfiguration实例
     *
     * @param crosInfo 跨域配置信息
     * @return
     */
    public static CorsConfiguration newInstance(CrossInfoBO crosInfo) {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        // 1允许跨域访问的域名(e.g. *, http://www.baidu.com, http://localhost:8080)
        String allowedValues = crosInfo.getAllowedOrigins();
        if (StringUtils.isEmpty(allowedValues)) {
            corsConfiguration.addAllowedOrigin("*");
        } else {
            String[] arr = allowedValues.split(GlobalConstant.DEFAULT_SPLIT_CHART);
            for (String tmpStr : arr) {
                if (!StringUtils.isEmpty(tmpStr)) {
                    corsConfiguration.addAllowedOrigin(tmpStr);
                }
            }
        }
        // 2允许任何头
        allowedValues = crosInfo.getAllowedHeaders();
        if (StringUtils.isEmpty(allowedValues)) {
            corsConfiguration.addAllowedHeader("*");
        } else {
            String[] arr = allowedValues.split(GlobalConstant.DEFAULT_SPLIT_CHART);
            for (String tmpStr : arr) {
                if (!StringUtils.isEmpty(tmpStr)) {
                    corsConfiguration.addAllowedHeader(tmpStr);
                }
            }
        }
        // 3允许任何方法（post、get等）
        allowedValues = crosInfo.getAllowedMethods();
        if (StringUtils.isEmpty(allowedValues)) {
            corsConfiguration.addAllowedMethod("*");
        } else {
            String[] arr = allowedValues.split(GlobalConstant.DEFAULT_SPLIT_CHART);
            for (String tmpStr : arr) {
                if (!StringUtils.isEmpty(tmpStr)) {
                    corsConfiguration.addAllowedMethod(tmpStr);
                }
            }
        }
        // 4是否允许请求中自动携带当前域名下的cookie数据
        corsConfiguration.setAllowCredentials(crosInfo.getAllowCredentials());
        // 将预检请求的结果缓存20分钟
        corsConfiguration.setMaxAge(1200L);
        return corsConfiguration;
    }

}
