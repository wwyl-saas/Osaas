package com.fate.common.util;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @program: parent
 * @description: 随机数工具
 * @author: chenyixin
 * @create: 2019-05-25 23:58
 **/
public class RandomUtil {
    private static ThreadLocalRandom r =  ThreadLocalRandom.current();

    /**
     * @param min
     * @param max
     * @return Random number
     */

    public static Long getRandomLongInRange(long min, long max) {
        return r.longs(min, (max + 1)).limit(1).findFirst().getAsLong();
    }

    /**
     * @param min
     * @param max
     * @return Random number string
     */
    public static String getRandomStringLongRange(long min, long max) {
        return String.valueOf(getRandomLongInRange(min,max));
    }


    /**
     * 获取uuid字符串
     * @return
     */
    public static String getUUIDRandomString() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static void main(String[] args) {
        System.out.println(getUUIDRandomString());
    }
}
