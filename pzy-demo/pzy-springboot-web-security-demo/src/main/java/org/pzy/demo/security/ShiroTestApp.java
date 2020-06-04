package org.pzy.demo.security;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author pan
 * @date 2020-01-27
 */
@SpringBootApplication
public class ShiroTestApp {
    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(ShiroTestApp.class);
        springApplication.setBannerMode(Banner.Mode.OFF);
        springApplication.run(args);
    }
}
