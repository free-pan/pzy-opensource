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

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;
import java.util.List;

/**
 * @author 潘志勇
 * @date 2019-01-31
 */
@Data
@ConfigurationProperties(prefix = "component.static-mapping", ignoreInvalidFields = true)
public class StaticMappingProperties implements Serializable {

    private static final long serialVersionUID = 3883811985835962576L;
    /**
     * 是否启用静态目录映射
     */
    private boolean enable;

    /**
     * 静态目录映射列表
     */
    private List<SingleStaticDirMapping> mappingList;
}
