package org.pzy.demo.security.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.pzy.demo.security.support.shiro.TestUsernamePasswordToken;
import org.pzy.opensource.domain.ResultT;
import org.pzy.opensource.security.domain.bo.ShiroUserBO;
import org.pzy.opensource.session.util.SpringSessionUtil;
import org.pzy.opensource.web.util.HttpResponseUtl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author pan
 * @date 2020-01-27
 */
@Controller
@Api(tags = "测试")
@Slf4j
public class TestController {

    @PostMapping(path = "login", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "账号", required = true, dataType = "string", paramType = "form"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, dataType = "string", paramType = "form"),
            @ApiImplicitParam(name = "rememberMe", value = "记住我", required = false, dataType = "boolean", paramType = "form")
    })
    @ApiOperation(value = "登录", notes = "普通登录,未实现最大登录限制!")
    @ResponseBody
    public ResultT login(String username, String password, Boolean rememberMe) {
        Subject subject = SecurityUtils.getSubject();
        if (!subject.isAuthenticated()) {
            log.debug("执行登录逻辑...");
            TestUsernamePasswordToken usernamePasswordToken = new TestUsernamePasswordToken();
            if (null != rememberMe && rememberMe) {
                // 记住我
                usernamePasswordToken.setRememberMe(rememberMe);
            }
            usernamePasswordToken.setUsername(username);
            usernamePasswordToken.setPassword(password.toCharArray());
            subject.login(usernamePasswordToken);
        }
        return ResultT.success(1);
    }

    @PostMapping(path = "login-max", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ApiOperation(value = "max登录", notes = "测试并发session限制.单个账号只允许一个session")
    @ResponseBody
    public ResultT login(HttpServletRequest httpServletRequest) {
        Subject subject = SecurityUtils.getSubject();
        if (!subject.isAuthenticated()) {
            log.debug("执行登录逻辑...");
            String username = "test";
            TestUsernamePasswordToken usernamePasswordToken = new TestUsernamePasswordToken();
            usernamePasswordToken.setUsername(username);
            usernamePasswordToken.setPassword(username.toCharArray());
            subject.login(usernamePasswordToken);
            ShiroUserBO shiroUserBO = (ShiroUserBO) subject.getPrincipal();
            boolean overMaxSessionCount = SpringSessionUtil.isOverMaxSessionCount(1, shiroUserBO.getUkFlag());
            if (overMaxSessionCount) {
                log.debug("超过最大session限制,将第一个session设置踢出标识");
                SpringSessionUtil.forceLogoutFirst(shiroUserBO.getUkFlag());
            } else {
                log.debug("未超过最大session限制,可以登录!");
                SpringSessionUtil.saveLoginUserPrincipalName(httpServletRequest, shiroUserBO.getUkFlag());
            }
        }
        return ResultT.success(1);
    }

    @GetMapping(path = "user-permission")
    @ApiOperation(value = "只有登录后或记住我后,且用户有这个权限时才能访问", notes = "用于测试uriUserAuthc过滤器!如果用户拥有该权限,该接口在`记住我`之后,即使sessionid由于超时已失效,依然还是可以访问,但是执行shiro的logout之后就不能再访问了")
    @ResponseBody
    public ResultT<String> userPermission() {
        return ResultT.success("user-permission成功!");
    }

    @GetMapping(path = "user")
    @ApiOperation(value = "只有登录后或记住我时才能访问", notes = "用于测试记住我功能!该接口在`记住我`之后,即使sessionid由于超时已失效,依然还是可以访问,但是执行shiro的logout之后就不能再访问了")
    @ResponseBody
    public ResultT<String> user() {
        return ResultT.success("user成功!");
    }

    @GetMapping(path = "redirect-login")
    @ApiOperation(value = "redirect登录", notes = "用于测试登录成功之后直接redirect到其它域名,web客户端(浏览器)再次发出请求时,服务器是否能够正确判断出该客户端已经登录!")
    public String redirectLogin() {
        Subject subject = SecurityUtils.getSubject();
        if (!subject.isAuthenticated()) {
            log.debug("执行登录逻辑...");
            TestUsernamePasswordToken usernamePasswordToken = new TestUsernamePasswordToken();
            String account = "test";
            usernamePasswordToken.setUsername(account);
            usernamePasswordToken.setPassword(account.toCharArray());
            subject.login(usernamePasswordToken);
        }
        return "redirect:http://www.baidu.com";
    }

    @GetMapping(path = "check-login")
    @ApiOperation(value = "检查是否登录")
    @ResponseBody
    public ResultT<Boolean> checkLogin() {
        Subject subject = SecurityUtils.getSubject();
        return ResultT.success(subject.isAuthenticated());
    }

    @GetMapping("test")
    @ApiOperation(value = "测试", notes = "用于测试登录过滤器")
    @ResponseBody
    public ResultT test() {
        return ResultT.success(1);
    }

    @GetMapping("write-cookie-test")
    @ApiOperation(value = "写cookie测试", notes = "写cookie测试")
    @ResponseBody
    public ResultT writeCookieTest() {
        HttpServletResponse response = HttpResponseUtl.loadHttpServletResponse();
        Cookie cookie = new Cookie("test", "你好");
        response.addCookie(cookie);
        return ResultT.success(1);
    }

    @PostMapping("read-cookie-test")
    @ApiOperation(value = "读cookie测试", notes = "读cookie测试")
    @ResponseBody
    public ResultT readCookieTest(@CookieValue(name = "test") String test) {
        return ResultT.success(test);
    }

    @GetMapping("forbidden-redirect")
    @ApiOperation(value = "无权限跳转路径")
    @ResponseBody
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResultT notPermission() {
        return ResultT.success("无权限");
    }

    @GetMapping("unauthorized-redirect")
    @ApiOperation(value = "未登录时,转入的url")
    @ResponseBody
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResultT unauthorizedRedirect() {
        return ResultT.success("您未登录");
    }

    @GetMapping("force-logout-redirect")
    @ApiOperation(value = "用户被强制登出后,转入的url")
    @ResponseBody
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResultT forceLogoutRedirect() {
        return ResultT.success("您的账户在异地登录,您被强制踢出了!");
    }

    @GetMapping("permission-test")
    @ApiOperation(value = "uri权限测试", notes = "用于测试uri鉴权")
    @ResponseBody
    public ResultT otherTest() {
        return ResultT.success(1);
    }

    @GetMapping("test-redirect")
    @ApiOperation("测试redirect")
    public String testRedirect() {
        return "redirect:http://www.baidu.com";
    }

    @GetMapping("logout")
    @ApiOperation("登出")
    @ResponseBody
    public ResultT logout() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return ResultT.success(1);
    }

}
