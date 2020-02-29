package com.fate.api.customer.controller;

import com.fate.common.util.IpUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: parent
 * @description: 健康检测
 * @author: chenyixin
 * @create: 2019-06-27 15:07
 **/
@ApiIgnore//使用该注解忽略这个API
@RestController
@RequestMapping("/health")
public class HealthyController {


    @PostMapping("/test")
    public Object test() {
        return "ok";
    }

    @GetMapping("/ip")
    public Map getIp() {
        Map<String,String> result=new HashMap<>();
        result.put("getHostIp", IpUtil.getHostIp());
        result.put("getServerIp",IpUtil.getSerIp());
        return result;
    }
}
