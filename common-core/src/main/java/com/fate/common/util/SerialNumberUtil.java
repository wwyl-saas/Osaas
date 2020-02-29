package com.fate.common.util;

/**
 * @program: parent
 * @description: 序列化生成工具
 * @author: chenyixin
 * @create: 2019-05-25 23:58
 **/
public class SerialNumberUtil {

    /**
     * 获取条形码
     * @return
     */
    public static String getBarCode() {
        return RandomUtil.getRandomStringLongRange(100000000000L,999999999999L);
    }


}
