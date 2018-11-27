package com.zm.utils;

import org.apache.commons.codec.digest.DigestUtils;

public class SignatureUtil {

    /**
     * MD5签名
     * @param secret 秘钥
     * @param body 签名字段
     * @return 签名后md5
     */
    public static String sign(String secret, String body) {
        // 第一步：对于Body进行md5
        String bodyDate = DigestUtils.md5Hex(body);
        // 第二步：secret+MD5(Body)
        String query = secret + bodyDate;
        // 第三步：使用MD5加密
        return DigestUtils.md5Hex(query);
    }
}
