package org.pyz.opensource.demo;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * ShardingJdbcApp
 *
 * @author pan
 * @date 6/5/20
 */
@SpringBootApplication
public class ShardingJdbcApp {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(ShardingJdbcApp.class);
        springApplication.setBannerMode(Banner.Mode.OFF);
        springApplication.run(args);
    }
}
