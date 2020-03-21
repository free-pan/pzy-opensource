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

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.pzy.opensource.springboot.domain.bo.CrossInfoBO;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;
import java.util.List;

/**
 * @author 潘志勇
 * @date 2019-02-02
 */
@Setter
@Getter
@ToString
@ConfigurationProperties(prefix = "component.cross", ignoreInvalidFields = true)
public class CrossPropeties implements Serializable {

    private static final long serialVersionUID = -3206502574570046645L;
    /**
     * 跨域支持是否启用
     */
    private boolean enable;
    /**
     * url跨域配置
     */
    private List<CrossInfoBO> list;
}
