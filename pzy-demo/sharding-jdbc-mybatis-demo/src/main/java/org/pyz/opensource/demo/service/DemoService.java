package org.pyz.opensource.demo.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.api.hint.HintManager;
import org.pyz.opensource.demo.dao.UserInfoDao;
import org.pyz.opensource.demo.domain.entity.UserInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * DemoService
 *
 * @author pan
 * @date 6/6/20
 */
@Slf4j
@Service
public class DemoService {

    @Resource
    UserInfoDao userInfoMapper;

    public static Long userId = 150L;

    public void demo() {
        System.out.println("Insert--------------");

        for (int i = 1; i <= 10; i++) {
            UserInfo userInfo = new UserInfo();
            userInfo.setUserId(userId);
            userInfo.setAccount("Account" + i);
            userInfo.setPassword("pass" + i);
            userInfo.setUserName("name" + i);
            userId++;
            if (i == 3) {
                HintManager hintManager = HintManager.getInstance();
                hintManager.addDatabaseShardingValue("user_info", userId);
                hintManager.addTableShardingValue("user_info", userId);
                System.out.println(userId);
            }
            userInfoMapper.insert(userInfo);
        }
        System.out.println("over..........");
    }
}
