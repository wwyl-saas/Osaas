package com.fate.api.merchant.util;

import com.fate.common.cons.Regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 短信
 */
public class SmsUtil {


    /**
     * 通用判断
     * @param telNum
     * @return
     */
    public static boolean isMobileNum(String telNum){
        String regex = Regex.PHONE;
        Pattern p = Pattern.compile(regex,Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(telNum);
        return m.matches();
    }


    public static void main(String[] args) {
        System.out.println(isMobileNum("17710800540"));
    }

}
