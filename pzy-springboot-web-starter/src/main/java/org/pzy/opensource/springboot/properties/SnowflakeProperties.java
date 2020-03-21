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

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;
import java.util.HashSet;

/**
 * @author pan
 * @date 2019-07-08
 */
@ConfigurationProperties(prefix = "component.snowflake", ignoreUnknownFields = true)
public class SnowflakeProperties implements Serializable {

    private static final long serialVersionUID = 922078597034487384L;

    /**
     * 是否启用id生成器. 默认false
     */
    private Boolean enable;

    /**
     * 终端id. 最小值为: 1
     */
    private long workerId;
    /**
     * 数据中心id. 最小值为:1
     */
    private long dataCenterId;
    /**
     * id生成器标识列表
     */
    private HashSet<String> idGeneratorColl;

    public SnowflakeProperties() {
        this.enable = false;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public HashSet<String> getIdGeneratorColl() {
        return idGeneratorColl;
    }

    public void setIdGeneratorColl(HashSet<String> idGeneratorColl) {
        this.idGeneratorColl = idGeneratorColl;
    }

    public long getWorkerId() {
        return workerId;
    }

    public void setWorkerId(long workerId) {
        this.workerId = workerId;
    }

    public long getDataCenterId() {
        return dataCenterId;
    }

    public void setDataCenterId(long dataCenterId) {
        this.dataCenterId = dataCenterId;
    }
}
