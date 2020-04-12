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

package org.pzy.opensource.web.errorhandler;

import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.pzy.opensource.comm.exception.AbstractBusinessException;
import org.pzy.opensource.comm.util.JsonUtil;
import org.pzy.opensource.comm.util.XmlUtil;
import org.pzy.opensource.domain.ResultT;
import org.pzy.opensource.domain.enums.GlobalSystemErrorCodeEnum;
import org.pzy.opensource.web.domain.bo.HttpRequestUriInfoBO;
import org.pzy.opensource.web.util.HttpRequestUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
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
 * 统一异常处理实现模板
 *
 * @author pan
 * @date 2019-04-10
 */
@Slf4j
@RestControllerAdvice
public class WinterExceptionHandlerTemplate implements WinterExceptionHandler {

    public WinterExceptionHandlerTemplate() {
        log.debug("pzy组件:启用全局异常处理器[{}]!", this.getClass().getName());
    }

    /**
     * 执行子类的自定义异常处理扩展
     *
     * @param req      请求参数
     * @param response 响应参数
     * @param e        异常
     */
    public void executeCustomExtendsExceptionHandler(HttpServletRequest req, HttpServletResponse response, Exception e) {
        try {
            this.customExtendsExceptoinHandler(req, response, e);
        } catch (Exception ex) {
            log.error("执行子类的异常处理进行自定义扩展时出错!", e);
        }
    }

    /**
     * 提取请求uri
     *
     * @param req 请求对象
     * @return 请求uri
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
     * @param req      请求对象
     * @param e        异常
     * @param expMsg   异常信息
     * @param errorLog 是打印错误日志还是打印警告日志
     * @return 请求uri
     */
    public String recordLog(HttpServletRequest req, Exception e, String expMsg, boolean errorLog) {
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
     * @param e        异常
     * @return 响应结果
     * @throws IOException 抛出异常
     */
    @ResponseBody
    @ExceptionHandler(value = AbstractBusinessException.class)
    @ResponseStatus(HttpStatus.OK)
    public ResultT<Integer> handle(HttpServletRequest req, HttpServletResponse response, AbstractBusinessException e) throws IOException {
        String expMsg = String.format("自定义业务异常: %s", e.getMessage());
        String uri = recordLog(req, e, expMsg, false);
        ResultT<Integer> result = ResultT.error(e.getCode(), e.getMessage(), uri);
        // 执行子类的自定义扩展
        this.executeCustomExtendsExceptionHandler(req, response, e);
        return result;
    }

    // =============================== 自定义业务异常处理:结束

    /**
     * 文件上传异常处理
     *
     * @param req      请求对象
     * @param response 响应对象
     * @param e        异常
     * @return 响应结果
     * @throws IOException 抛出异常
     */
    @ResponseBody
    @ExceptionHandler(value = MaxUploadSizeExceededException.class)
    @ResponseStatus(HttpStatus.OK)
    public ResultT<Integer> handle(HttpServletRequest req, HttpServletResponse response, MaxUploadSizeExceededException e) throws IOException {
        String expMsg = String.format("上传的文件大小超过了,服务器允许的最大文件大小[%s]!", e.getMaxUploadSize());
        String uri = recordLog(req, e, expMsg, false);
        ResultT<Integer> result = ResultT.error(GlobalSystemErrorCodeEnum.FILE_TOO_LARGE.name(), GlobalSystemErrorCodeEnum.FILE_TOO_LARGE.getCode(), uri);
        // 执行子类的自定义扩展
        this.executeCustomExtendsExceptionHandler(req, response, e);
        return result;
    }

    /**
     * 请求头的accept值指定异常处理
     *
     * @param req      请求对象
     * @param response 响应对象
     * @param e        异常
     * @return 响应结果
     * @throws IOException 抛出异常
     */
    @ResponseBody
    @ExceptionHandler(value = HttpMediaTypeNotAcceptableException.class)
    @ResponseStatus(HttpStatus.OK)
    public ResultT<Integer> handle(HttpServletRequest req, HttpServletResponse response, HttpMediaTypeNotAcceptableException e) throws IOException {
        String accept = HttpRequestUtil.extractAccept(req);
        String expMsg = String.format("不支持该类型的Accept[%s]!", accept);
        String uri = recordLog(req, e, expMsg, false);
        ResultT<Integer> result = ResultT.error(GlobalSystemErrorCodeEnum.ACCEPT_TYPE_NOT_SUPPORT.name(), GlobalSystemErrorCodeEnum.ACCEPT_TYPE_NOT_SUPPORT.getCode(), uri);
        // 执行子类的自定义扩展
        this.executeCustomExtendsExceptionHandler(req, response, e);
        return result;
    }

    /**
     * path传参或uri传参,传递的参数类型与实际的参数类型不匹配时,抛出此异常
     *
     * @param req      请求对象
     * @param response 响应对象
     * @param e        异常
     * @return 响应结果
     * @throws IOException 抛出异常
     */
    @ResponseBody
    @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.OK)
    public ResultT<Integer> handle(HttpServletRequest req, HttpServletResponse response, MethodArgumentTypeMismatchException e) throws IOException {
        String expMsg = String.format("参数类型不匹配:参数名[%s]", e.getName());
        String uri = recordLog(req, e, expMsg, false);
        ResultT<Integer> result = ResultT.error(GlobalSystemErrorCodeEnum.REQUEST_PARAM_MISMATCH.name(), "实际请求参数[" + e.getName() + "]不符合预期!", uri);
        // 执行子类的自定义扩展
        this.executeCustomExtendsExceptionHandler(req, response, e);
        return result;
    }

    /**
     * request body传参无法通过验证时,抛出此异常
     *
     * @param req      请求对象
     * @param response 响应对象
     * @param e        异常
     * @return 响应结果
     * @throws IOException 抛出异常
     */
    @ResponseBody
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.OK)
    public ResultT<Integer> handle(HttpServletRequest req, HttpServletResponse response, MethodArgumentNotValidException e) throws IOException {
        BindingResult bindingResult = e.getBindingResult();
        Iterator<FieldError> iter = bindingResult.getFieldErrors().iterator();
        ResultT<Integer> result = dataNotPassedValidate(req, response, e, iter);
        // 执行子类的自定义扩展
        this.executeCustomExtendsExceptionHandler(req, response, e);
        return result;
    }

    /**
     * 当使用spring的MethodValidationPostProcessor进行数据验证,但数据未通过验证时,抛出该异常
     *
     * @param req      请求对象
     * @param response 响应对象
     * @param e        异常
     * @return 响应结果
     * @throws IOException 抛出异常
     */
    @ResponseBody
    @ExceptionHandler(value = ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.OK)
    public ResultT<Integer> handle(HttpServletRequest req, HttpServletResponse response, ConstraintViolationException e) throws IOException {
        String expMsg = "业务数据未通过验证!";
        String uri = recordLog(req, e, expMsg, false);
        Set<ConstraintViolation<?>> constraintViolationSet = e.getConstraintViolations();
        ResultT<Integer> result = multiParamValidateExp(req, response, uri, constraintViolationSet);
        // 执行子类的自定义扩展
        this.executeCustomExtendsExceptionHandler(req, response, e);
        return result;
    }

    private ResultT<Integer> multiParamValidateExp(HttpServletRequest req, HttpServletResponse response, String uri, Set<ConstraintViolation<?>> constraintViolationSet) throws IOException {
        List<String> fieldErrors = new ArrayList<>();
        for (ConstraintViolation<?> constraintViolation : constraintViolationSet) {
            fieldErrors.add(constraintViolation.getMessage());
        }
        ResultT<Integer> result = ResultT.error(GlobalSystemErrorCodeEnum.PARAM_VALIDATE_EXCEPTION.name(), fieldErrors, uri);
        return result;
    }

    private ResultT<Integer> multiParamValidateExp(HttpServletRequest req, HttpServletResponse response, String uri, Iterator<FieldError> iter) throws IOException {
        List<String> fieldErrors = new ArrayList<>();
        FieldError fieldError;
        while (iter.hasNext()) {
            fieldError = iter.next();
            fieldErrors.add(fieldError.getDefaultMessage());
        }
        ResultT<Integer> result = ResultT.error(GlobalSystemErrorCodeEnum.PARAM_VALIDATE_EXCEPTION.name(), fieldErrors, uri);
        return result;
    }

    /**
     * 当uri参数无法通过数据验证时,将抛出此异常
     *
     * @param req      请求对象
     * @param response 响应对象
     * @param e        异常
     * @return 响应结果
     * @throws IOException 抛出异常
     */
    @ResponseBody
    @ExceptionHandler(value = BindException.class)
    @ResponseStatus(HttpStatus.OK)
    public ResultT<Integer> handle(HttpServletRequest req, HttpServletResponse response, BindException e) throws IOException {
        Iterator<FieldError> iter = e.getFieldErrors().iterator();
        ResultT<Integer> result = dataNotPassedValidate(req, response, e, iter);
        // 执行子类的自定义扩展
        this.executeCustomExtendsExceptionHandler(req, response, e);
        return result;
    }

    private ResultT<Integer> dataNotPassedValidate(HttpServletRequest req, HttpServletResponse response, Exception e, Iterator<FieldError> iter) throws IOException {
        String expMsg = "业务数据未通过验证!";
        String uri = recordLog(req, e, expMsg, false);
        return multiParamValidateExp(req, response, uri, iter);
    }

    /**
     * request body传参无法解析
     *
     * @param req      请求对象
     * @param response 响应对象
     * @param e        异常
     * @return 响应结果
     * @throws IOException 抛出异常
     */
    @ResponseBody
    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.OK)
    public ResultT<Integer> handle(HttpServletRequest req, HttpServletResponse response, HttpMessageNotReadableException e) throws IOException {
        String expMsg = "request body参数解析异常!";
        String uri = recordLog(req, e, expMsg, false);
        ResultT<Integer> result = ResultT.error(GlobalSystemErrorCodeEnum.REQUEST_MESSAGE_EXTRACT.name(), expMsg, uri);
        // 执行子类的自定义扩展
        this.executeCustomExtendsExceptionHandler(req, response, e);
        return result;
    }

    /**
     * 请求类型不被服务器支持
     *
     * @param req      请求对象
     * @param response 响应对象
     * @param e        异常
     * @return 响应结果
     * @throws IOException 抛出异常
     */
    @ResponseBody
    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.OK)
    public ResultT<Integer> handle(HttpServletRequest req, HttpServletResponse response, HttpRequestMethodNotSupportedException e) throws IOException {
        String expMsg = "不支持的请求类型!";
        String uri = recordLog(req, e, expMsg, false);
        ResultT<Integer> result = ResultT.error(GlobalSystemErrorCodeEnum.REQUEST_METHOD_NOT_SUPPORT.name(), expMsg, uri);
        // 执行子类的自定义扩展
        this.executeCustomExtendsExceptionHandler(req, response, e);
        return result;
    }

    /**
     * 错误的contentType类型值处理
     *
     * @param req      请求对象
     * @param response 响应对象
     * @param e        异常
     * @return 响应结果
     * @throws IOException 抛出异常
     */
    @ResponseBody
    @ExceptionHandler(value = HttpMediaTypeNotSupportedException.class)
    @ResponseStatus(HttpStatus.OK)
    public ResultT<Integer> handle(HttpServletRequest req, HttpServletResponse response, HttpMediaTypeNotSupportedException e) throws IOException {
        String expMsg = "不支持的ContentType类型(客户端指定了错误的ContentType值)!" + e.getContentType();
        String uri = recordLog(req, e, expMsg, false);
        ResultT<Integer> result = ResultT.error(GlobalSystemErrorCodeEnum.CONTENT_TYPE_NOT_SUPPORT.name(), "不支持的contentType类型值:" + e.getContentType(), uri);
        // 执行子类的自定义扩展
        this.executeCustomExtendsExceptionHandler(req, response, e);
        return result;
    }

    /**
     * 缺少请求参数
     *
     * @param req      请求对象
     * @param response 响应对象
     * @param e        异常
     * @return 响应结果
     * @throws IOException 抛出异常
     */
    @ResponseBody
    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.OK)
    public ResultT<Integer> handle(HttpServletRequest req, HttpServletResponse response, MissingServletRequestParameterException e) throws IOException {
        String expMsg = "缺少请求参数!" + e.getParameterName();
        String uri = recordLog(req, e, expMsg, false);
        ResultT<Integer> result = ResultT.error(GlobalSystemErrorCodeEnum.REQUEST_PARAM_NOT_FOUND.name(), expMsg, uri);
        // 执行子类的自定义扩展
        this.executeCustomExtendsExceptionHandler(req, response, e);
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
    @ExceptionHandler(value = SQLIntegrityConstraintViolationException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResultT<Integer> handle(HttpServletRequest req, HttpServletResponse response, SQLIntegrityConstraintViolationException e) throws IOException {
        ResultT<Integer> result = handlerDataRepeatException(req, e);
        // 执行子类的自定义扩展
        this.executeCustomExtendsExceptionHandler(req, response, e);
        return result;
    }

    private ResultT<Integer> handlerDataRepeatException(HttpServletRequest req, Exception e) {
        String expMsg = "数据库操作异常(请求数据不符合数据库索引约束)!";
        String uri = recordLog(req, e, expMsg, true);
        ResultT<Integer> result = ResultT.error(GlobalSystemErrorCodeEnum.DATA_REPEAT.name(), GlobalSystemErrorCodeEnum.DATA_REPEAT.getCode(), uri);
        return result;
    }

    /**
     * 出现该异常说明,系统中存在未预料到的异常(业务逻辑实现不严谨)
     *
     * @param req      请求对象
     * @param response 响应对象
     * @param e        异常
     * @return 响应结果
     * @throws IOException 抛出异常
     */
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResultT<Integer> handle(HttpServletRequest req, HttpServletResponse response, Exception e) throws IOException {
        String expMsg = "系统发生非预期异常!";
        String uri = recordLog(req, e, expMsg, true);
        ResultT<Integer> result = ResultT.error(GlobalSystemErrorCodeEnum.SERVER_EXCEPTION.name(), GlobalSystemErrorCodeEnum.SERVER_EXCEPTION.getCode(), uri);
        // 执行子类的自定义扩展
        this.executeCustomExtendsExceptionHandler(req, response, e);
        return result;
    }
}
