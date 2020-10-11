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

package org.pzy.opensource.springboot.errorhandler;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authz.UnauthorizedException;
import org.pzy.opensource.domain.ResultT;
import org.pzy.opensource.domain.enums.GlobalSystemErrorCodeEnum;
import org.pzy.opensource.web.errorhandler.WinterExceptionHandlerTemplate;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.TransientDataAccessResourceException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * 统一异常处理默认参考实现
 *
 * @author pan
 * @date 2019-04-10
 */
@Slf4j
@RestControllerAdvice
public class DefaultWinterExceptionHandlerImpl extends WinterExceptionHandlerTemplate {

    /**
     * sql异常
     *
     * @param req      请求对象
     * @param response 响应对象
     * @param e        异常
     * @return 响应结果
     * @throws IOException 抛出异常
     */
    @ResponseBody
    @ExceptionHandler(value = BadSqlGrammarException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResultT<Integer> handle(HttpServletRequest req, HttpServletResponse response, BadSqlGrammarException e) throws IOException {
        String expMsg = "执行的sql语句有问题(sql语法错误或非空字段插入了空值或sql插入的数据,为数据库不支持的字符集)!" + e.getSql();
        String uri = super.recordLog(req, e, expMsg, true);
        ResultT<Integer> result = ResultT.error(GlobalSystemErrorCodeEnum.SERVER_EXCEPTION.name(), GlobalSystemErrorCodeEnum.SERVER_EXCEPTION.getCode(), uri);
        // 执行子类的自定义扩展
        super.executeCustomExtendsExceptionHandler(req, response, e);
        return result;
    }

    /**
     * 当spring无法创建事务时,将抛出此异常(可能是数据库宕机也可能是数据库压力过大,单个连接超时了,也可能是等待数据库连接池返回可用连接直到达到了最大等待时间依然未获取到可用数据库连接)
     *
     * @param req      请求对象
     * @param response 响应对象
     * @param e        异常
     * @return 响应结果
     * @throws IOException 抛出异常
     */
    @ResponseBody
    @ExceptionHandler(value = CannotCreateTransactionException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResultT<Integer> handle(HttpServletRequest req, HttpServletResponse response, CannotCreateTransactionException e) throws IOException {
        String expMsg = "数据库操作异常(无法创建事务:可能是数据库宕机了. 也可能是数据库压力过大,单个连接超时了. 也可能是等待数据库连接池返回可用连接直到达到了最大等待时间依然未获取到可用数据库连接)!";
        String uri = recordLog(req, e, expMsg, true);
        ResultT<Integer> result = ResultT.error(GlobalSystemErrorCodeEnum.SERVER_EXCEPTION.name(), GlobalSystemErrorCodeEnum.SERVER_EXCEPTION.getCode(), uri);
        // 执行子类的自定义扩展
        super.executeCustomExtendsExceptionHandler(req, response, e);
        return result;
    }

    private ResultT<Integer> handlerDataRepeatException(HttpServletRequest req, Exception e) {
        String expMsg = "数据库操作异常(请求数据不符合数据库索引约束)!";
        String uri = recordLog(req, e, expMsg, true);
        ResultT<Integer> result = ResultT.error(GlobalSystemErrorCodeEnum.DATA_REPEAT.name(), GlobalSystemErrorCodeEnum.DATA_REPEAT.getCode(), uri);
        return result;
    }

    /**
     * 当客户端数据不符合数据库索引约束,且程序中未对该异常进行捕获处理, 则会执行到这里.
     *
     * @param req      请求对象
     * @param response 响应对象
     * @param e        异常
     * @return 响应结果
     * @throws IOException 抛出异常
     */
    @ResponseBody
    @ExceptionHandler(value = DuplicateKeyException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResultT<Integer> handle(HttpServletRequest req, HttpServletResponse response, DuplicateKeyException e) throws IOException {
        ResultT<Integer> result = handlerDataRepeatException(req, e);
        // 执行子类的自定义扩展
        super.executeCustomExtendsExceptionHandler(req, response, e);
        return result;
    }

    /**
     * 当客户端的数据,不符合数据库表约束时,将抛出此异常
     *
     * @param req      请求对象
     * @param response 响应对象
     * @param e        异常
     * @return 响应结果
     * @throws IOException 抛出异常
     */
    @ResponseBody
    @ExceptionHandler(value = DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResultT<Integer> handle(HttpServletRequest req, HttpServletResponse response, DataIntegrityViolationException e) throws IOException {
        String expMsg = "数据库操作异常(请求数据不符合表字段约束)!";
        String uri = recordLog(req, e, expMsg, true);
        ResultT<Integer> result = ResultT.error(GlobalSystemErrorCodeEnum.SERVER_EXCEPTION.name(), GlobalSystemErrorCodeEnum.SERVER_EXCEPTION.getCode(), uri);
        // 执行子类的自定义扩展
        super.executeCustomExtendsExceptionHandler(req, response, e);
        return result;
    }

    /**
     * 业务代码尝试在只读事务中,进行当前读操作
     *
     * @param req      请求对象
     * @param response 响应对象
     * @param e        异常
     * @return 响应结果
     * @throws IOException 抛出异常
     */
    @ResponseBody
    @ExceptionHandler(value = TransientDataAccessResourceException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResultT<Integer> handle(HttpServletRequest req, HttpServletResponse response, TransientDataAccessResourceException e) throws IOException {
        String expMsg = "业务代码尝试在只读事务中,进行当前读操作[只读事务只允许进行快照读]!";
        String uri = recordLog(req, e, expMsg, true);
        ResultT<Integer> result = ResultT.error(GlobalSystemErrorCodeEnum.SERVER_EXCEPTION.name(), GlobalSystemErrorCodeEnum.SERVER_EXCEPTION.getCode(), uri);
        // 执行子类的自定义扩展
        super.executeCustomExtendsExceptionHandler(req, response, e);
        return result;
    }

    /**
     * shiro登录异常处理
     *
     * @param req      请求对象
     * @param response 响应对象
     * @param e        异常
     * @return 响应结果
     * @throws IOException 抛出异常
     */
    @ResponseBody
    @ExceptionHandler(value = AuthenticationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResultT<Integer> handle(HttpServletRequest req, HttpServletResponse response, AuthenticationException e) throws IOException {
        String expMsg = "shiro登录异常!";
        String uri = recordLog(req, e, expMsg, true);
        ResultT<Integer> result = ResultT.error(GlobalSystemErrorCodeEnum.SECURITY_LOGIN_EXCEPTION.name(), e.getMessage(), uri);
        // 执行子类的自定义扩展
        super.executeCustomExtendsExceptionHandler(req, response, e);
        return result;
    }

    /**
     * shiro登录异常处理
     *
     * @param req
     * @param response
     * @param e
     * @return
     * @throws IOException
     */
    @ResponseBody
    @ExceptionHandler(value = IncorrectCredentialsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResultT<Integer> handle(HttpServletRequest req, HttpServletResponse response, IncorrectCredentialsException e) throws IOException {
        String expMsg = "shiro登录异常:账号或密码错误!";
        String uri = recordLog(req, e, expMsg, true);
        ResultT<Integer> result = ResultT.error(GlobalSystemErrorCodeEnum.SECURITY_LOGIN_EXCEPTION.name(), "账号或密码错误!", uri);
        // 执行子类的自定义扩展
        super.executeCustomExtendsExceptionHandler(req, response, e);
        return result;
    }

    /**
     * shiro权限验证未通过时,抛出该异常
     *
     * @param req      请求对象
     * @param response 响应对象
     * @param e        异常
     * @return 响应结果
     * @throws IOException 抛出异常
     */
    @ResponseBody
    @ExceptionHandler(value = UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResultT<Integer> handle(HttpServletRequest req, HttpServletResponse response, UnauthorizedException e) throws IOException {
        String expMsg = "shiro权限异常(未通过shiro的鉴权)!";
        String uri = recordLog(req, e, expMsg, true);
        ResultT<Integer> result = ResultT.error(GlobalSystemErrorCodeEnum.SECURITY_FORBIDDEN_EXCEPTION.name(), e.getMessage(), uri);
        // 执行子类的自定义扩展
        super.executeCustomExtendsExceptionHandler(req, response, e);
        return result;
    }
}
