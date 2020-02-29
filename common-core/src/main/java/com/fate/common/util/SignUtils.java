package com.fate.common.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import java.security.MessageDigest;
import java.util.*;

/**
 * 参数字典排序签名相关工具类.
 */
@Slf4j
public class SignUtils {

  private static final char[] HEX_DIGITS = { '0', '1', '2', '3', '4', '5',
          '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
  public static final String TYPE_MD5 = "md5";
  public static final String TYPE_SHA1 = "sha1";
  /**
   * 编码的字符串
   *
   * @param algorithm
   * @param str
   * @return String
   */
  public static String sign(String algorithm, String str) {
    if (str == null) {
      return null;
    }
    try {
      MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
      messageDigest.update(str.getBytes("UTF-8"));
      return getFormattedText(messageDigest.digest());
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

  }

  /**
   *获取原始字节并将其格式化。
   * @param bytes
   *            the raw bytes from the digest.
   * @return the formatted bytes.
   */
  private static String getFormattedText(byte[] bytes) {
    int len = bytes.length;
    StringBuilder buf = new StringBuilder(len * 2);
    for (int j = 0; j < len; j++) { 			buf.append(HEX_DIGITS[(bytes[j] >> 4) & 0x0f]);
      buf.append(HEX_DIGITS[bytes[j] & 0x0f]);
    }
    return buf.toString();
  }


  /**
   * 根据参数名称对参数进行字典排序
   * @param params
   * @return
   */
  public static String sortParams(Map<String, String> params) {
    SortedMap<String, String> sortedMap = new TreeMap<>(params);

    StringBuilder toSign = new StringBuilder();
    for (String key : sortedMap.keySet()) {
      String value = params.get(key);
      if (StringUtils.isNotEmpty(value)) {
        toSign.append(key).append("=").append(value).append("&");
      }
    }
    return toSign.substring(0, toSign.lastIndexOf("&"));
  }


}
