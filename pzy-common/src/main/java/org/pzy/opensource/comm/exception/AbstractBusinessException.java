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

package org.pzy.opensource.comm.exception;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 抽象的业务异常, 所有其他异常都必须继承该异常
 *
 * @author pan
 * @date 2019-03-22
 */
@Setter
@Getter
@ToString(callSuper = true)
public abstract class AbstractBusinessException extends RuntimeException {

    private static final long serialVersionUID = -8912367058092407311L;
    /**
     * 存放导致异常的数据
     */
    private Map<String, Object> data = new HashMap<>(5);

    /**
     * 异常状态码
     */
    private String code;

    /**
     * 通过异常信息构造业务异常对象
     *
     * @param message 异常提示信息
     */
    public AbstractBusinessException(String message) {
        super(message);
    }

    /**
     * 将其他异常包装为系统的业务异常
     *
     * @param message 异常提示信息
     * @param exp     其他非BusinessException的异常实例
     */
    public AbstractBusinessException(String message, Exception exp) {
        super(message, exp);
    }

    /**
     * 添加单个异常数据
     *
     * @param key key值
     * @param val value值
     * @return 异常时的数据
     */
    public Map<String, Object> addData(String key, Object val) {
        data.put(key, val);
        return data;
    }

    /**
     * 批量添加异常数据
     *
     * @param data 异常时的数据
     * @return 异常时的数据
     */
    public Map<String, Object> addAllData(Map<String, Object> data) {
        data.putAll(data);
        return data;
    }

    public String getCode() {
        return StringUtils.isBlank(this.code) ? this.getClass().getSimpleName() : this.code;
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
