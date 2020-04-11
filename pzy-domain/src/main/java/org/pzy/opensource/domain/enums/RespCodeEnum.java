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

package org.pzy.opensource.domain.enums;

import org.pzy.opensource.domain.entity.BaseEnum;

/**
 * 业务执行结果状态码
 *
 * @author pan
 * @date 2019-12-07
 */
public enum RespCodeEnum implements BaseEnum<Integer> {

    /**
     * 请求执行成功
     */
    SUCCESS_CODE(200, "请求执行成功"),

    /**
     * 请求信息错误(参数验证验证未通过)
     */
    REQUEST_INFO_ERROR_CODE(400, "请求信息错误(参数验证验证未通过)"),
    /**
     * 当前请求需要先进行身份认证(未登录)
     */
    UNAUTHORIZED_CODE(401, "当前请求需要先进行身份认证(未登录)"),
    /**
     * 当前请求不被服务器运行(权限不足)
     */
    FORBIDDEN_CODE(403, "当前请求不被服务器运行(权限不足)"),
    /**
     * 资源不存在
     */
    RESOURCE_NOT_FOUD_CODE(404, "资源不存在"),
    /**
     * 当前客户端的request method不被服务器支持(如:当前只支持post,但请求方式为get,则会出现该异常)
     */
    METHOD_NOT_ALLOW_CODE(405, "当前客户端的request method不被服务器支持(如:当前只支持post,但请求方式为get,则会出现该异常)"),
    /**
     * 执行超时
     */
    TIMEOUT_CODE(408, "执行超时"),
    /**
     * 状态冲突(如:更新时当前版本与服务器数据的版本不一致)
     */
    CONFLICT_CODE(409, "状态冲突"),
    /**
     * 客户端被手动强制踢出(如:管理员强制将某个用户踢出)
     */
    FORCE_LOGOUT(452, "您被强制踢出"),
    /**
     * 客户端被自动强制踢出(如:某个账号的session数量超过系统的最大限制,自动踢出)
     */
    FORCE_LOGOUT_AUTO(453, "账号在多个客户端登录,同时在线数量超过系统限制,您被强制踢出"),
    /**
     * 未知服务器错误
     */
    UNKNOW_EXP_CODE(500, "未知服务器错误");

    private Integer code;
    private String msg;


    private RespCodeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return code + " --> " + msg;
    }}
