package com.fate.api.merchant.util;

import com.fate.common.util.RandomUtil;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @program: parent
 * @description: 订单号生成工具
 * @author: chenyixin
 * @create: 2019-07-28 00:59
 **/
public class OrderNoUtil {
    /**
     * 20位 随机2位+年月日时分10位+用户后6位
     * @return
     */
    public static String getOrderNo(Long customerId) {
        StringBuilder stringBuilder = new StringBuilder();
        LocalDateTime time = LocalDateTime.now();
        stringBuilder.append(RandomUtil.getRandomStringLongRange(1000,9999))
                .append(time.format(DateTimeFormatter.ofPattern("yyMMddhhmm")))
                .append(customerId % 1000000);
        return stringBuilder.toString();
    }

    public static void main(String[] args) {
        System.out.println(getOrderNo(123456789011L));
    }
}
