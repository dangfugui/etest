package com.dang.etest.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Description: 摘要算法  MD5 SHA 安全散列算法
 * SHA的全称是Secure Hash Algorithm，即安全散列算法。 1993年，安全散列算法(SHA)由美国国家标准和技术协会（NIST)提出，
 * 并作为联邦信息处理标准(FIPS PUB 180)公布， 1995年又发布了一个修订版FIPS PUB 180-1，通常称之为SHA-1。
 * SHA-1是基于MD4算法的，现在已成为公认的最安全的散列算法之一，并被广泛使用。SHA-1算法生成的摘要信息的长度为160位，
 * 由于生成的摘要信息更长，运算的过程更加复杂，在相同的硬件上， SHA-1的运行速度比MD5更慢，但是也更为安全。
 */
public class DigestUtil {

    /**
     * 定义加密方式
     */
    private final static String KEY_SHA = "SHA";
    // SHA-1，SHA-224，SHA-256，SHA-384，和SHA-512
    private static final String KEY_MD5 = "MD5";
    private static final String KEY_SHA1 = "SHA-1";
    private static final String KEY_SHA224 = "SHA-224";
    private static final String KEY_SHA256 = "SHA-256";
    private static final String KEY_SHA384 = "SHA-384";
    private static final String KEY_SHA512 = "SHA-512";

    /**
     * 全局数组
     */
    private static final String[] HEX_DIGITS = {"0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    /**
     * SHA 加密
     *
     * @param string 需要加密的字符串
     *
     * @return 加密之后的字符串
     *
     * @throws Exception
     */
    public static String toSHA(String string) {
        return toSHA(string.getBytes());
    }

    public static String toSHA(byte[] bytes) {
        return toHexString(digest(bytes, KEY_SHA));
    }

    public static String toMD5(String string) {
        return toMD5(string.getBytes());
    }

    public static String toMD5(byte[] bytes) {
        return toHexString(digest(bytes, KEY_MD5));
    }

    private static String digest(String data, String algorithm) {
        return toHexString(digest(data.getBytes(), algorithm));
    }

    /**
     * SHA 加密
     *
     * @param data 需要加密的字节数组
     *
     * @return 加密之后的字节数组
     *
     * @throws Exception
     */
    private static byte[] digest(byte[] data, String algorithm) {
        // 创建具有指定算法名称的信息摘要
        //        MessageDigest sha = MessageDigest.getInstance(KEY_SHA);
        MessageDigest sha = null;
        try {
            sha = MessageDigest.getInstance(algorithm);
            // 使用指定的字节数组对摘要进行最后更新
            sha.update(data);
            // 完成摘要计算并返回
            return sha.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 转换字节数组为十六进制字符串
     *
     * @param bytes 字节数组
     *
     * @return 十六进制字符串
     */
    private static String toHexString(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            sb.append(HEX_DIGITS[(bytes[i] & 0xf0) >>> 4]);
            sb.append(HEX_DIGITS[bytes[i] & 0x0f]);
        }
        return sb.toString();
    }

}
