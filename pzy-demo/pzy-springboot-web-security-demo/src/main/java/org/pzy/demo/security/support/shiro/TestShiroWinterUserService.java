package org.pzy.demo.security.support.shiro;

import org.pzy.opensource.security.domain.bo.PermissionInfoBO;
import org.pzy.opensource.security.domain.bo.ShiroUserBO;
import org.pzy.opensource.security.domain.bo.SimpleShiroUserBO;
import org.pzy.opensource.security.service.ShiroWinterUserService;
import org.pzy.opensource.security.shiro.matcher.BCryptPasswordEncoder;

import java.util.Arrays;
import java.util.List;

/**
 * @author pan
 * @date 2020-01-27
 */
public class TestShiroWinterUserService implements ShiroWinterUserService {

    private static final BCryptPasswordEncoder ENCODER = new BCryptPasswordEncoder();
    private static final String ACCOUNT = "test";
    private static final ShiroUserBO TEST_SHIRO_USER = new ShiroUserBO(ENCODER.encode(ACCOUNT));

    @Override
    public ShiroUserBO loadUserInfoByUsername(String username) {
        return TEST_SHIRO_USER;
    }

    @Override
    public List<String> loadRoleByUsername(SimpleShiroUserBO shiroUser) {
        return Arrays.asList("test");
    }

    @Override
    public List<PermissionInfoBO> loadPermissionByUsername(SimpleShiroUserBO shiroUser) {
        PermissionInfoBO permissionInfoBO = new PermissionInfoBO("test", "/test", "GET");
        PermissionInfoBO userPermissionInfoBO = new PermissionInfoBO("up", "/user-permission", "ALL");
        return Arrays.asList(permissionInfoBO, userPermissionInfoBO);
    }
}
