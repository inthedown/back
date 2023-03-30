package com.example.boe.Util;

import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.Cipher;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

public class RSAEncrypt {
    private static Map<Integer, String> keyMap = new HashMap<Integer, String>();  //用于封装随机产生的公钥与私钥

    private static String DEC_KEY = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAOAhBm07jK3NMP36aBsFu0A1NwSufWGg4sPKmGC0e2/IX6eFQpHzV8eTp8jUeimOVzMKQkW41pSmCni1ik0r4VU1/KPLoFTP71wuMVHW3gOH+varmj8TmVxVSLlzo/8HVh6jAiD2hq/KurBuK0SEIbK/8APaCX67I3KQ2hob8YoFAgMBAAECgYA/+c1O0se2OLL6XEbpJ7qqekYpJPGPM5BnGOZj/ECbyNNdCxHUfWSaCFDFZ7kzMXk8jXobUsaVicvFZvSCULsxwcSWoQGdL6Fp0StbP1tmm3XYzdtUW9LHL22oD3GeNjUQa3jNWqQUX1bnsfwcSp84BSlL2hckF6DgLnWObyhrJQJBAPIhrGvPb9AVZO3gD5mdMOGn6BQ/pYWwZQtfDDvlbTBATXWoZchS5AL7aLjLharJXlCzePUbhi8cfPx50YJAL0sCQQDs92JP7LoDKyoxA/m21ULvSP6/lcwFf7d5XCEAtXlcUNuqfoSgi79czB2rqReAlDgTF8yFV4hEXFrCgsjC+0nvAkEApdLf9bnL9rMgOzUPGgIoXvKSI4PvRR6oJOCETWTzoW74XZLlmgsCsvQhJSXlNjDQke5H8X1XKV6WTobTMCPq/wJAY+ZjNwi43s8ayXFAnFjKEDD43PIdAhB23B3/99mJ4WZhRT5zUW+47DlNJOpzcwyJm1sEKQ7e7tr0lF4onzLrVwJAGoBTMgSx2wmXtuJq4jAcN0QvqE3lJEduvGQO/MvdoxU2rzjZO+t483AKirI+TS2BtlV/mifh1we3S173+zs/5w==";
    private static String ENC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDgIQZtO4ytzTD9+mgbBbtANTcErn1hoOLDyphgtHtvyF+nhUKR81fHk6fI1HopjlczCkJFuNaUpgp4tYpNK+FVNfyjy6BUz+9cLjFR1t4Dh/r2q5o/E5lcVUi5c6P/B1YeowIg9oavyrqwbitEhCGyv/AD2gl+uyNykNoaG/GKBQIDAQAB";

    /**
     * 随机生成密钥对
     * @throws NoSuchAlgorithmException
     */
    public static void genKeyPair() throws NoSuchAlgorithmException {
        // KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        // 初始化密钥对生成器，密钥大小为96-1024位
        keyPairGen.initialize(1024,new SecureRandom());
        // 生成一个密钥对，保存在keyPair中
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();   // 得到私钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();  // 得到公钥
        String publicKeyString = new String(Base64.encodeBase64(publicKey.getEncoded()));
        // 得到私钥字符串
        String privateKeyString = new String(Base64.encodeBase64((privateKey.getEncoded())));
        // 将公钥和私钥保存到Map
        keyMap.put(0,publicKeyString);  //0表示公钥
        keyMap.put(1,privateKeyString);  //1表示私钥
    }
    /**
     * RSA公钥加密
     *
     * @param str
     *            加密字符串
     * @param publicKey
     *            公钥
     * @return 密文
     * @throws Exception
     *             加密过程中的异常信息
     */
    public static String encrypt( String str, String publicKey ) throws Exception{
        //base64编码的公钥
        byte[] decoded = Base64.decodeBase64(publicKey);
        RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
        //RSA加密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        String outStr = Base64.encodeBase64String(cipher.doFinal(str.getBytes("UTF-8")));
        return outStr;
    }

    /**
     * RSA私钥解密
     *
     * @param str
     *            加密字符串
     * @param privateKey
     *            私钥
     * @return 铭文
     * @throws Exception
     *             解密过程中的异常信息
     */
    public static String decrypt(String str, String privateKey) throws Exception{
        //64位解码加密后的字符串
        byte[] inputByte = Base64.decodeBase64(str.getBytes("UTF-8"));
        //base64编码的私钥
        byte[] decoded = Base64.decodeBase64(privateKey);
        RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
        //RSA解密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, priKey);
        String outStr = new String(cipher.doFinal(inputByte));
        return outStr;
    }

    public static String decrypt(String str) throws Exception {
        return decrypt(str, DEC_KEY);
    }
}
