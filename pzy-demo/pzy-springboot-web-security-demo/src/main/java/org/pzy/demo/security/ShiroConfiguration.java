package org.pzy.demo.security;

import org.pzy.demo.security.support.shiro.TestShiroWinterUserService;
import org.pzy.demo.security.support.shiro.TestUsernamePasswordToken;
import org.pzy.opensource.security.shiro.CustomShiroFilterBoxTemplate;
import org.pzy.opensource.security.shiro.filter.*;
import org.pzy.opensource.security.shiro.realm.WinterRealmTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author pan
 * @date 2020-01-27
 */
@Configuration
public class ShiroConfiguration {

    /**
     * 用于实现根据登录信息查找用户信息,以及根据ShiroUserBO对象查找查找权限信息
     *
     * @return
     */
    @Bean
    TestShiroWinterUserService testShiroWinterUserService() {
        return new TestShiroWinterUserService();
    }

    /**
     * 自定义Realm
     *
     * @param testShiroWinterUserService
     * @return
     */
    @Bean
    WinterRealmTemplate winterRealm(TestShiroWinterUserService testShiroWinterUserService) {
        WinterRealmTemplate webListener = new WinterRealmTemplate(testShiroWinterUserService, TestUsernamePasswordToken.class);
        return webListener;
    }

    @Bean
    CustomShiroFilterBoxTemplate customShiroFilterBox(TestShiroWinterUserService testShiroWinterUserService) {

        CustomShiroFilterBoxTemplate customShiroFilterBox = new CustomShiroFilterBoxTemplate();
        // 未认证(未登录)时转向的url
        String unauthorizedRedirectUrl = "/unauthorized-redirect";
        // 发现用户无访问当前uri的权限之后,转入的url
        String forbiddenRedirectUrl = "/forbidden-redirect";
        // 用户被强制踢出之后转入的url
        String forceLogoutRedirectUrl = "/force-logout-redirect";
        customShiroFilterBox.addCustomShiroFilter(new CrossUserFilter(unauthorizedRedirectUrl));
        customShiroFilterBox.addCustomShiroFilter(new CrossFormAuthenticationFilter(unauthorizedRedirectUrl));
        customShiroFilterBox.addCustomShiroFilter(new CrossUriUserPermissionFilter(testShiroWinterUserService, forbiddenRedirectUrl));
        customShiroFilterBox.addCustomShiroFilter(new CrossUriPermissionFilter(testShiroWinterUserService, forbiddenRedirectUrl));
        customShiroFilterBox.addCustomShiroFilter(new CrossKickoutSessionControlFilter(forceLogoutRedirectUrl));

        return customShiroFilterBox;
    }
}
