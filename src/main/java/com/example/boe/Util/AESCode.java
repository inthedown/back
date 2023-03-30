package com.example.boe.Util;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
@Slf4j
public class AESCode {
    /*
     * 加密用的Key 可以用26个字母和数字组成 此处使用AES-128-CBC加密模式，key需要为16位。
     */
//	a0b891c2d563e4f7
    private final static String default_key = "a0b891c2d563e4f7";
    private static final String ivParameter = "0123456789abcdef";
    public static final String USER_NAME_KEY = "uavUserNameEncod";




    public static String encrypt(String sSrc){
        return encrypt(sSrc,default_key);
    }

    // 加密
    public static String encrypt(String sSrc, String key){
        String result = "";
        try {
            Cipher cipher;
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            byte[] raw = key.getBytes();
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());// 使用CBC模式，需要一个向量iv，可增加加密算法的强度
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
            byte[] encrypted = cipher.doFinal(sSrc.getBytes("utf-8"));
            result = Base64.getMimeEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            log.error("", e);
        }
        // 此处使用BASE64做转码。
        return result;

    }

    public static String decrypt(String sSrc) {
        return decrypt(sSrc, default_key);
    }

    // 解密
    public static String decrypt(String sSrc, String key){
        try {
            byte[] raw = key.getBytes("ASCII");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] encrypted1 = Base64.getMimeDecoder().decode(sSrc);// 先用base64解密
            byte[] original = cipher.doFinal(encrypted1);
            String originalString = new String(original, "utf-8");
            return originalString;
        } catch (Exception ex) {
            log.error("", ex);
            return null;
        }
    }
}
