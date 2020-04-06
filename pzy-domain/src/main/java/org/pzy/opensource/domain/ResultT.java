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

package org.pzy.opensource.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.pzy.opensource.domain.enums.RespCodeEnum;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * @author pan
 * @date 2019-12-11
 */
@Data
@Accessors(chain = true)
public class ResultT<T> implements Serializable {

    private static final long serialVersionUID = -5208061788117916814L;

    @ApiModelProperty("业务执行结果状态码")
    private String code;

    @ApiModelProperty("业务是否执行成功")
    private Boolean success;

    @ApiModelProperty("结果数据")
    private T resp;

    @ApiModelProperty("异常提示信息")
    private List<String> msgList;

    @ApiModelProperty("请求uri")
    private String uri;

    private ResultT() {
    }

    private ResultT(Boolean success, String code, T resp, List<String> msgList, String uri) {
        this.code = code;
        this.resp = resp;
        this.msgList = msgList;
        this.uri = uri;
        this.success = success;
    }

    /**
     * 成功
     *
     * @param resp 响应数据
     * @param <T>
     * @return
     */
    public static final <T> ResultT<T> success(T resp) {
        return new ResultT<>(true, String.valueOf(RespCodeEnum.SUCCESS_CODE.getCode()), resp, null, null);
    }

    /**
     * 成功
     *
     * @return
     */
    public static final ResultT success() {
        return new ResultT<>(true, String.valueOf(RespCodeEnum.SUCCESS_CODE.getCode()), null, null, null);
    }

    /**
     * 失败
     *
     * @param code 错误码
     * @param msg  错误提示信息
     * @param <T>
     * @return
     */
    public static final <T> ResultT<T> error(RespCodeEnum code, String msg) {
        return new ResultT<>(false, String.valueOf(code.getCode()), null, Arrays.asList(msg), null);
    }

    /**
     * 失败
     *
     * @param code 错误码
     * @param msg  错误提示信息
     * @param uri  当前请求uri
     * @param <T>
     * @return
     */
    public static final <T> ResultT<T> error(RespCodeEnum code, String msg, String uri) {
        return new ResultT<>(false, String.valueOf(code.getCode()), null, Arrays.asList(msg), uri);
    }

    /**
     * 失败
     *
     * @param code 错误码
     * @param msg  错误提示信息
     * @param uri  当前请求uri
     * @param <T>
     * @return
     */
    public static final <T> ResultT<T> error(String code, String msg, String uri) {
        return new ResultT<>(false, code, null, Arrays.asList(msg), uri);
    }

    /**
     * 失败
     *
     * @param code    错误码
     * @param msgList 错误提示信息
     * @param uri     当前请求uri
     * @param <T>
     * @return
     */
    public static final <T> ResultT<T> error(String code, List<String> msgList, String uri) {
        return new ResultT<>(false, code, null, msgList, uri);
    }
}
