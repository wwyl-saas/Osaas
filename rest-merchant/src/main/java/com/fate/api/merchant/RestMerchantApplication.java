package com.fate.api.merchant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;

import java.util.TimeZone;

@ServletComponentScan
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class RestMerchantApplication {
    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
        SpringApplication.run(RestMerchantApplication.class, args);
    }
}
