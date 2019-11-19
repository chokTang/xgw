package com.wbg.xigui.utils;


import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author lijun
 * AES 加解密
 */
public class AesEncryptUtils {

    private static final String ALGORITHMSTR = "AES/ECB/PKCS5Padding";
    public static final String AESKEY = "C8960BF9A8E2DBFC2222CC0BBE4A22CD";

    /**
     * base64 加密
     *
     * @param bytes 待加密数据
     * @return 加密后的数据
     */
    public static String base64Encode(byte[] bytes) {
        return Base64Util.encode(bytes);
    }

    /**
     * base 64 解密
     *
     * @param base64Code 待解密的数据
     * @return 解密后的数据
     */
    public static byte[] base64Decode(String base64Code) {
        return Base64Util.decode(base64Code);
    }

    /**
     * AES 加密
     *
     * @param content    待加密的数据
     * @return byte 加密后的数据
     * @throws Exception 异常
     */
    public static byte[] aesEncryptToBytes(String content) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(128);
        Cipher cipher = Cipher.getInstance(ALGORITHMSTR);
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(AESKEY.getBytes(), "AES"));
        return cipher.doFinal(content.getBytes("UTF-8"));
    }

    /**
     * AES 加密
     *
     * @param content    待加密的数据
     * @return String 加密后的数据
     * @throws Exception 异常
     */
    public static String aesEncrypt(String content) throws Exception {
        return base64Encode(aesEncryptToBytes(content));
    }

    /**
     * AES 解密
     *
     * @param encryptBytes 待解密的数据
     * @return String 解密数据
     * @throws Exception
     */
    public static String aesDecryptByBytes(byte[] encryptBytes) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(128);
        Cipher cipher = Cipher.getInstance(ALGORITHMSTR);
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(AESKEY.getBytes(), "AES"));
        byte[] decryptBytes = cipher.doFinal(encryptBytes);
        return new String(decryptBytes, "UTF-8");
    }

    /**
     * AES 解密
     *
     * @param encryptStr 待解密的数据
     * @return String 解密数据
     * @throws Exception
     */
    public static String aesDecrypt(String encryptStr) throws Exception {
        return aesDecryptByBytes(base64Decode(encryptStr));
    }

}
