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

import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.pzy.opensource.comm.exception.AbstractBusinessException;
import org.pzy.opensource.comm.util.JsonUtil;
import org.pzy.opensource.comm.util.XmlUtil;
import org.pzy.opensource.domain.ResultT;
import org.pzy.opensource.web.domain.bo.HttpRequestUriInfoBO;
import org.pzy.opensource.web.util.HttpRequestUtil;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.TransientDataAccessResourceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


/**
 * 统一异常处理
 *
 * @author pan
 * @date 2019-04-10
 */
@Slf4j
@RestControllerAdvice
public class WinterExceptionHandler {

    public WinterExceptionHandler() {
        log.debug("pzy组件:启用全局异常处理器!");
    }

    /**
     * 提取请求uri
     *
     * @param req
     * @return
     */
    private String getReqUrl(HttpServletRequest req) {
        HttpRequestUriInfoBO requestUriInfoBO = HttpRequestUtil.extractHttpRequestUriInfo(req);
        return String.format("%s : %s", requestUriInfoBO.getMethod(), requestUriInfoBO.getUri());
    }

    private void printResponse(HttpServletRequest req, HttpServletResponse response, ResultT<Integer> resp) throws IOException, JAXBException {
        @Cleanup OutputStream outputStream = response.getOutputStream();
        @Cleanup PrintWriter printWriter = new PrintWriter(outputStream);
        if (HttpRequestUtil.isResponseJsonData(req)) {
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            printWriter.println(JsonUtil.toJsonString(resp));
        } else if (HttpRequestUtil.isResponseXmlData(req)) {
            response.setContentType(MediaType.APPLICATION_XML_VALUE);
            printWriter.println(XmlUtil.toXmlString(resp));
        } else {
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            printWriter.println(JsonUtil.toJsonString(resp));
        }
    }

    /**
     * 记录日志
     *
     * @param req
     * @param e
     * @param expMsg
     * @return
     */
    private String recordLog(HttpServletRequest req, Exception e, String expMsg, boolean errorLog) {
        String uri = getReqUrl(req);
        // 第一个%s表示uri, 第二个%s表示异常类名, 第三个%s表示自定义的错误信息, 第四个%s表示实际的异常信息
        String errorMessage = String.format("[%s], [%s], [%s], [%s]", uri, e.getClass().getSimpleName(), expMsg, e.getMessage());
        if (errorLog) {
            log.error(errorMessage, e);
        } else {
            log.warn(errorMessage, e);
        }
        return uri;
    }

    // =============================== 自定义业务异常处理:开始

    /**
     * 捕获并处理自定义异常
     *
     * @param req      请求对象
     * @param response 响应对象
     * @param e        自定义异常
     * @throws IOException
     */
    @ResponseBody
    @ExceptionHandler(value = AbstractBusinessException.class)
    @ResponseStatus(HttpStatus.OK)
    public ResultT<Integer> handle(HttpServletRequest req, HttpServletResponse response, AbstractBusinessException e) throws IOException {
        String expMsg = String.format("自定义业务异常: %s", e.getMessage());
        String uri = recordLog(req, e, expMsg, false);
        ResultT<Integer> result = ResultT.error(e.getCode(), e.getMessage(), uri);
        return result;
    }

    // =============================== 自定义业务异常处理:结束

    /**
     * 文件上传异常处理
     *
     * @param req
     * @param response
     * @param e
     * @throws IOException
     */
    @ResponseBody
    @ExceptionHandler(value = MaxUploadSizeExceededException.class)
    @ResponseStatus(HttpStatus.OK)
    public ResultT<Integer> handle(HttpServletRequest req, HttpServletResponse response, MaxUploadSizeExceededException e) throws IOException {
        String expMsg = String.format("上传的文件大小超过了,服务器允许的最大文件大小[%s]!", e.getMaxUploadSize());
        String uri = recordLog(req, e, expMsg, false);
        ResultT<Integer> result = ResultT.error("FILE_TOO_LARGE", "上传的文件大小超过了,服务器允许的最大文件大小!", uri);
        return result;
    }

    /**
     * 请求头的accept值指定异常处理
     *
     * @param req
     * @param response
     * @param e
     * @throws IOException
     */
    @ResponseBody
    @ExceptionHandler(value = HttpMediaTypeNotAcceptableException.class)
    @ResponseStatus(HttpStatus.OK)
    public ResultT<Integer> handle(HttpServletRequest req, HttpServletResponse response, HttpMediaTypeNotAcceptableException e) throws IOException {
        String accept = HttpRequestUtil.extractAccept(req);
        String expMsg = String.format("不支持该类型的Accept[%s]!", accept);
        String uri = recordLog(req, e, expMsg, false);
        ResultT<Integer> result = ResultT.error("ACCEPT_TYPE_NOT_SUPPORT", "请求头中指定了错误的accept值!", uri);
        return result;
    }

    /**
     * path传参或uri传参,传递的参数类型与实际的参数类型不匹配时,抛出此异常
     *
     * @param req
     * @param response
     * @param e
     * @throws IOException
     */
    @ResponseBody
    @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.OK)
    public ResultT<Integer> handle(HttpServletRequest req, HttpServletResponse response, MethodArgumentTypeMismatchException e) throws IOException {
        String expMsg = String.format("参数类型不匹配:参数名[%s]", e.getName());
        String uri = recordLog(req, e, expMsg, false);
        ResultT<Integer> result = ResultT.error("REQUEST_PARAM_MISMATCH", "实际请求参数[" + e.getName() + "]不符合预期!", uri);
        return result;
    }

    /**
     * request body传参无法通过验证时,抛出此异常
     *
     * @param req
     * @param response
     * @param e
     * @throws IOException
     */
    @ResponseBody
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.OK)
    public ResultT<Integer> handle(HttpServletRequest req, HttpServletResponse response, MethodArgumentNotValidException e) throws IOException {
        BindingResult bindingResult = e.getBindingResult();
        Iterator<FieldError> iter = bindingResult.getFieldErrors().iterator();
        return dataNotPassedValidate(req, response, e, iter);
    }

    /**
     * 当使用spring的MethodValidationPostProcessor进行数据验证,但数据未通过验证时,抛出该异常
     *
     * @param req
     * @param response
     * @param e
     * @return
     * @throws IOException
     */
    @ResponseBody
    @ExceptionHandler(value = ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.OK)
    public ResultT<Integer> handle(HttpServletRequest req, HttpServletResponse response, ConstraintViolationException e) throws IOException {
        String expMsg = "业务数据未通过验证!";
        String uri = recordLog(req, e, expMsg, false);
        Set<ConstraintViolation<?>> constraintViolationSet = e.getConstraintViolations();
        return multiParamValidateExp(req, response, uri, constraintViolationSet);
    }

    private ResultT<Integer> multiParamValidateExp(HttpServletRequest req, HttpServletResponse response, String uri, Set<ConstraintViolation<?>> constraintViolationSet) throws IOException {
        List<String> fieldErrors = new ArrayList<>();
        for (ConstraintViolation<?> constraintViolation : constraintViolationSet) {
            fieldErrors.add(constraintViolation.getMessage());
        }
        ResultT<Integer> result = ResultT.error("PARAM_VALIDATE_EXCEPTION", fieldErrors, uri);
        return result;
    }

    private ResultT<Integer> multiParamValidateExp(HttpServletRequest req, HttpServletResponse response, String uri, Iterator<FieldError> iter) throws IOException {
        List<String> fieldErrors = new ArrayList<>();
        FieldError fieldError;
        while (iter.hasNext()) {
            fieldError = iter.next();
            fieldErrors.add(fieldError.getDefaultMessage());
        }
        ResultT<Integer> result = ResultT.error("PARAM_VALIDATE_EXCEPTION", fieldErrors, uri);
        return result;
    }

    /**
     * 当uri参数无法通过数据验证时,将抛出此异常
     *
     * @param req
     * @param response
     * @param e
     * @throws IOException
     */
    @ResponseBody
    @ExceptionHandler(value = BindException.class)
    @ResponseStatus(HttpStatus.OK)
    public ResultT<Integer> handle(HttpServletRequest req, HttpServletResponse response, BindException e) throws IOException {
        Iterator<FieldError> iter = e.getFieldErrors().iterator();
        return dataNotPassedValidate(req, response, e, iter);
    }

    private ResultT<Integer> dataNotPassedValidate(HttpServletRequest req, HttpServletResponse response, Exception e, Iterator<FieldError> iter) throws IOException {
        String expMsg = "业务数据未通过验证!";
        String uri = recordLog(req, e, expMsg, false);
        return multiParamValidateExp(req, response, uri, iter);
    }

    /**
     * request body传参无法解析
     *
     * @param req
     * @param response
     * @param e
     * @throws IOException
     */
    @ResponseBody
    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.OK)
    public ResultT<Integer> handle(HttpServletRequest req, HttpServletResponse response, HttpMessageNotReadableException e) throws IOException {
        String expMsg = "request body参数解析异常!";
        String uri = recordLog(req, e, expMsg, false);
        ResultT<Integer> result = ResultT.error("REQUEST_MESSAGE_EXTRACT", expMsg, uri);
        return result;
    }

    /**
     * 请求类型不被服务器支持
     *
     * @param req
     * @param response
     * @param e
     * @throws IOException
     */
    @ResponseBody
    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.OK)
    public ResultT<Integer> handle(HttpServletRequest req, HttpServletResponse response, HttpRequestMethodNotSupportedException e) throws IOException {
        String expMsg = "不支持的请求类型!";
        String uri = recordLog(req, e, expMsg, false);
        ResultT<Integer> result = ResultT.error("REQUEST_METHOD_NOT_SUPPORT", expMsg, uri);
        return result;
    }

    /**
     * 错误的contentType类型值处理
     *
     * @param req
     * @param response
     * @param e
     * @throws IOException
     */
    @ResponseBody
    @ExceptionHandler(value = HttpMediaTypeNotSupportedException.class)
    @ResponseStatus(HttpStatus.OK)
    public ResultT<Integer> handle(HttpServletRequest req, HttpServletResponse response, HttpMediaTypeNotSupportedException e) throws IOException {
        String expMsg = "不支持的ContentType类型(客户端指定了错误的ContentType值)!" + e.getContentType();
        String uri = recordLog(req, e, expMsg, false);
        ResultT<Integer> result = ResultT.error("CONTENT_TYPE_NOT_SUPPORT", "错误的contentType类型值!", uri);
        return result;
    }

    /**
     * 缺少请求参数
     *
     * @param req
     * @param response
     * @param e
     * @throws IOException
     */
    @ResponseBody
    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.OK)
    public ResultT<Integer> handle(HttpServletRequest req, HttpServletResponse response, MissingServletRequestParameterException e) throws IOException {
        String expMsg = "缺少请求参数!" + e.getParameterName();
        String uri = recordLog(req, e, expMsg, false);
        ResultT<Integer> result = ResultT.error("REQUEST_PARAM_NOT_FOUND", expMsg, uri);
        return result;
    }

    /**
     * sql异常
     *
     * @param req
     * @param response
     * @param e
     * @throws IOException
     */
    @ResponseBody
    @ExceptionHandler(value = BadSqlGrammarException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResultT<Integer> handle(HttpServletRequest req, HttpServletResponse response, BadSqlGrammarException e) throws IOException {
        String expMsg = "执行的sql语句有问题(sql语法错误或非空字段插入了空值或sql插入的数据,为数据库不支持的字符集)!" + e.getSql();
        String uri = recordLog(req, e, expMsg, true);
        ResultT<Integer> result = ResultT.error("SERVER_EXCEPTION", "服务出错了!", uri);
        return result;
    }

    /**
     * 当spring无法创建事务时,将抛出此异常(可能是数据库宕机也可能是数据库压力过大,单个连接超时了,也可能是等待数据库连接池返回可用连接直到达到了最大等待时间依然未获取到可用数据库连接)
     *
     * @param req
     * @param e
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = CannotCreateTransactionException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResultT<Integer> handle(HttpServletRequest req, HttpServletResponse response, CannotCreateTransactionException e) throws IOException {
        String expMsg = "数据库操作异常(无法创建事务:可能是数据库宕机了. 也可能是数据库压力过大,单个连接超时了. 也可能是等待数据库连接池返回可用连接直到达到了最大等待时间依然未获取到可用数据库连接)!";
        String uri = recordLog(req, e, expMsg, true);
        ResultT<Integer> result = ResultT.error("SERVER_EXCEPTION", "服务出错了!", uri);
        return result;
    }

    /**
     * 当客户端数据不符合数据库索引约束,且程序中未对该异常进行捕获处理, 则会执行到这里.
     *
     * @param req
     * @param e
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = SQLIntegrityConstraintViolationException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResultT<Integer> handle(HttpServletRequest req, HttpServletResponse response, SQLIntegrityConstraintViolationException e) throws IOException {
        return handlerDataRepeatException(req, e);
    }

    private ResultT<Integer> handlerDataRepeatException(HttpServletRequest req, Exception e) {
        String expMsg = "数据库操作异常(请求数据不符合数据库索引约束)!";
        String uri = recordLog(req, e, expMsg, true);
        ResultT<Integer> result = ResultT.error("DATA_REPEAT", "数据重复!", uri);
        return result;
    }

    /**
     * 当客户端数据不符合数据库索引约束,且程序中未对该异常进行捕获处理, 则会执行到这里.
     *
     * @param req
     * @param e
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = DuplicateKeyException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResultT<Integer> handle(HttpServletRequest req, HttpServletResponse response, DuplicateKeyException e) throws IOException {
        return handlerDataRepeatException(req, e);
    }

    /**
     * 当客户端的数据,不符合数据库表约束时,将抛出此异常
     *
     * @param req
     * @param e
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResultT<Integer> handle(HttpServletRequest req, HttpServletResponse response, DataIntegrityViolationException e) throws IOException {
        String expMsg = "数据库操作异常(请求数据不符合表字段约束)!";
        String uri = recordLog(req, e, expMsg, true);
        ResultT<Integer> result = ResultT.error("SERVER_EXCEPTION", "服务出错了!", uri);
        return result;
    }

    /**
     * 业务代码尝试在只读事务中,进行当前读操作
     *
     * @param req
     * @param response
     * @param e
     * @throws IOException
     */
    @ResponseBody
    @ExceptionHandler(value = TransientDataAccessResourceException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResultT<Integer> handle(HttpServletRequest req, HttpServletResponse response, TransientDataAccessResourceException e) throws IOException {
        String expMsg = "业务代码尝试在只读事务中,进行当前读操作[只读事务只允许进行快照读]!";
        String uri = recordLog(req, e, expMsg, true);
        ResultT<Integer> result = ResultT.error("SERVER_EXCEPTION", "服务出错了!", uri);
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
    @ExceptionHandler(value = AuthenticationException.class)
    @ResponseStatus(HttpStatus.OK)
    public ResultT<Integer> handle(HttpServletRequest req, HttpServletResponse response, AuthenticationException e) throws IOException {
        String expMsg = "shiro登录异常!";
        String uri = recordLog(req, e, expMsg, true);
        ResultT<Integer> result = ResultT.error("SHIRO_LOGIN_EXCEPTION", e.getMessage(), uri);
        return result;
    }

    /**
     * shiro权限验证未通过时,抛出该异常
     *
     * @param req
     * @param response
     * @param e
     * @return
     * @throws IOException
     */
    @ResponseBody
    @ExceptionHandler(value = UnauthorizedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResultT<Integer> handle(HttpServletRequest req, HttpServletResponse response, UnauthorizedException e) throws IOException {
        String expMsg = "shiro权限异常!";
        String uri = recordLog(req, e, expMsg, true);
        ResultT<Integer> result = ResultT.error("SHIRO_PERMISSION_EXCEPTION", e.getMessage(), uri);
        return result;
    }

    /**
     * 出现该异常说明,系统中存在未预料到的异常(业务逻辑实现不严谨)
     *
     * @param req
     * @param e
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResultT<Integer> handle(HttpServletRequest req, HttpServletResponse response, Exception e) throws IOException {
        String expMsg = "系统发生非预期异常!";
        String uri = recordLog(req, e, expMsg, true);
        ResultT<Integer> result = ResultT.error("SERVER_EXCEPTION", "服务出错了!", uri);
        return result;
    }
}
