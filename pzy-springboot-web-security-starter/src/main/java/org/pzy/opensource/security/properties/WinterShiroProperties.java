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

package org.pzy.opensource.security.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.convert.DurationUnit;

import java.io.Serializable;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * @author 潘志勇
 * @date 2019-02-07
 */
@Setter
@Getter
@ToString
@ConfigurationProperties(prefix = "component.security", ignoreInvalidFields = true)
public class WinterShiroProperties implements Serializable {

    private static final long serialVersionUID = -5470605842397400841L;

    /**
     * 记住我cookie值的加密秘钥,每个项目都应该不一样. 可用如下方式产生一个秘钥.<br>
     * KeyGenerator keygen = KeyGenerator.getInstance("AES");
     * SecretKey deskey = keygen.generateKey();
     * System.out.println(Base64.encodeToString(deskey.getEncoded()));
     */
    private String rememberMeBase64CipherKey = "QOsdkm0BH9FBO57y+cwmOA==";
    /**
     * 记住我的cookie名称. 默认:rememberMe
     */
    private String rememberMeCookieName = "rememberMe";

    /**
     * 记住我cookie的有效期. 默认7天. 填写值可以是: 7d, 3h, 2m, 1s. 分别表示: 7天, 3小时, 2分钟, 1秒钟
     */
    @DurationUnit(ChronoUnit.SECONDS)
    private Duration rememberMeMaxAge = Duration.ofDays(7);

    /**
     * 是否启用安全组件. 默认:true
     */
    private Boolean enable = true;
    /**
     * 登录表单的提交地址[该地址不会被鉴权]
     */
    private String loginUrl;
    /**
     * 登录成功之后转入的地址[如果登录成功之后由前端决定要跳转到哪里,则无需配置该链接]
     */
    private String loginSuccessUrl;
    /**
     * 登出请求提交地址[该地址不会被鉴权]
     */
    private String logoutUrl;
    /**
     * 无需进行权限校验的uri地址[该地址不会被鉴权]
     */
    private HashSet<String> anonUriPatternColl;

    /**
     * 配置自定义的shiro过滤器链. 元素的path为ant路径表达式,filters为shiro过滤器[多个使用逗号分隔]
     */
    private List<PathFilterChain> shiroFilterChain = new ArrayList<>();

}
