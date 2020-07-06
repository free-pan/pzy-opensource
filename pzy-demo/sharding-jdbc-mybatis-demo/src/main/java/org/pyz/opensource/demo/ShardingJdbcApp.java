package org.pyz.opensource.demo;

import org.pzy.opensource.domain.GlobalConstant;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * ShardingJdbcApp
 *
 * @author pan
 * @date 6/5/20
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableTransactionManagement(order = GlobalConstant.AOP_ORDER_TRANSACTIONAL)
public class ShardingJdbcApp {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(ShardingJdbcApp.class);
        springApplication.setBannerMode(Banner.Mode.OFF);
        springApplication.run(args);
    }
}
