package org.pyz.opensource.demo.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.pyz.opensource.demo.ShardingJdbcApp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * DemoServiceTest
 *
 * @author pan
 * @date 6/6/20
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ShardingJdbcApp.class)
@Slf4j
public class DemoServiceTest {

    @Autowired
    private DemoService demoService;

    @Test
    public void demo() {
        demoService.demo();
    }
}