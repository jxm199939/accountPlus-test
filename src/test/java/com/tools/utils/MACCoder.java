package com.tools.utils;

import java.security.NoSuchAlgorithmException;  

import javax.crypto.KeyGenerator;  
import javax.crypto.Mac;  
import javax.crypto.SecretKey;  
import javax.crypto.spec.SecretKeySpec;  

/**
 *  MAC算法工具类 
 * 对于HmacMD5、HmacSHA1、HmacSHA256、HmacSHA384、HmacSHA512应用的步骤都是一模一样的。具体看下面的代码 
 * @author duzl
 * 2015 9:52:12 AM
 */
public class MACCoder {  
    /** 
     * 产生HmacMD5摘要算法的密钥 
     */  
    public static byte[] initHmacMD5Key() throws NoSuchAlgorithmException {  
        // 初始化HmacMD5摘要算法的密钥产生器  
        KeyGenerator generator = KeyGenerator.getInstance("HmacMD5");  
        // 产生密钥  
        SecretKey secretKey = generator.generateKey();  
        // 获得密钥  
        byte[] key = secretKey.getEncoded();  
        return key;  
    }  
  
    /** 
     * HmacMd5摘要算法 
     * 对于给定生成的不同密钥，得到的摘要消息会不同，所以在实际应用中，要保存我们的密钥 
     */  
    public static String encodeHmacMD5(byte[] data, byte[] key) throws Exception {  
        // 还原密钥  
        SecretKey secretKey = new SecretKeySpec(key, "HmacMD5");  
        // 实例化Mac  
        Mac mac = Mac.getInstance(secretKey.getAlgorithm());  
        //初始化mac  
        mac.init(secretKey);  
        //执行消息摘要  
        byte[] digest = mac.doFinal(data);  
        return bytesToHexString(digest);//转为十六进制的字符串  
    }  
      
      
    /** 
     * 产生HmacSHA1摘要算法的密钥 
     */  
    public static byte[] initHmacSHAKey() throws NoSuchAlgorithmException {  
        // 初始化HmacMD5摘要算法的密钥产生器  
        KeyGenerator generator = KeyGenerator.getInstance("HmacSHA1");  
        // 产生密钥  
        SecretKey secretKey = generator.generateKey();  
        // 获得密钥  
        byte[] key = secretKey.getEncoded();  
        return key;  
    }  
  
    /** 
     * HmacSHA1摘要算法 
     * 对于给定生成的不同密钥，得到的摘要消息会不同，所以在实际应用中，要保存我们的密钥 
     */  
    public static String encodeHmacSHA(byte[] data, byte[] key) throws Exception {  
        // 还原密钥  
        SecretKey secretKey = new SecretKeySpec(key, "HmacSHA1");  
        // 实例化Mac  
        Mac mac = Mac.getInstance(secretKey.getAlgorithm());  
        //初始化mac  
        mac.init(secretKey);  
        //执行消息摘要  
        byte[] digest = mac.doFinal(data);  
        return bytesToHexString(digest);//转为十六进制的字符串  
    }  
      
      
      
    /** 
     * 产生HmacSHA256摘要算法的密钥 
     */  
    public static byte[] initHmacSHA256Key() throws NoSuchAlgorithmException {  
        // 初始化HmacMD5摘要算法的密钥产生器  
        KeyGenerator generator = KeyGenerator.getInstance("HmacSHA256");  
        // 产生密钥  
        SecretKey secretKey = generator.generateKey();  
        // 获得密钥  
        byte[] key = secretKey.getEncoded();  
        return key;  
    }  
  
    /** 
     * HmacSHA1摘要算法 
     * 对于给定生成的不同密钥，得到的摘要消息会不同，所以在实际应用中，要保存我们的密钥 
     */  
    public static String encodeHmacSHA256(byte[] data, byte[] key) throws Exception {  
        // 还原密钥  
        SecretKey secretKey = new SecretKeySpec(key, "HmacSHA256");  
        // 实例化Mac  
        Mac mac = Mac.getInstance(secretKey.getAlgorithm());  
        //初始化mac  
        mac.init(secretKey);  
        //执行消息摘要  
        byte[] digest = mac.doFinal(data);
        return bytesToHexString(digest);//转为十六进制的字符串  
    }  
    
   /*
    * Convert byte[] to hex string.这里我们可以将byte转换成int，然后利用Integer.toHexString(int)来转换成16进制字符串。   
    * @param src byte[] data   
    * @return hex string   
    */      
   public static String bytesToHexString(byte[] src){   
       StringBuilder stringBuilder = new StringBuilder("");   
       if (src == null || src.length <= 0) {   
           return null;   
       }   
       for (int i = 0; i < src.length; i++) {   
           int v = src[i] & 0xFF;   
           String hv = Integer.toHexString(v);   
           if (hv.length() < 2) {   
               stringBuilder.append(0);   
           }   
           stringBuilder.append(hv);   
       }   
       return stringBuilder.toString().toUpperCase();   
   }  
  
    /** 
     * 产生HmacSHA256摘要算法的密钥 
     */  
    public static byte[] initHmacSHA384Key() throws NoSuchAlgorithmException {  
        // 初始化HmacMD5摘要算法的密钥产生器  
        KeyGenerator generator = KeyGenerator.getInstance("HmacSHA384");  
        // 产生密钥  
        SecretKey secretKey = generator.generateKey();  
        // 获得密钥  
        byte[] key = secretKey.getEncoded();  
        return key;  
    }  
  
    /** 
     * HmacSHA1摘要算法 
     * 对于给定生成的不同密钥，得到的摘要消息会不同，所以在实际应用中，要保存我们的密钥 
     */  
    public static String encodeHmacSHA384(byte[] data, byte[] key) throws Exception {  
        // 还原密钥  
        SecretKey secretKey = new SecretKeySpec(key, "HmacSHA384");  
        // 实例化Mac  
        Mac mac = Mac.getInstance(secretKey.getAlgorithm());  
        //初始化mac  
        mac.init(secretKey);  
        //执行消息摘要  
        byte[] digest = mac.doFinal(data);  
        return bytesToHexString(digest);//转为十六进制的字符串  
    }  
      
      
  
    /** 
     * 产生HmacSHA256摘要算法的密钥 
     */  
    public static byte[] initHmacSHA512Key() throws NoSuchAlgorithmException {  
        // 初始化HmacMD5摘要算法的密钥产生器  
        KeyGenerator generator = KeyGenerator.getInstance("HmacSHA512");  
        // 产生密钥  
        SecretKey secretKey = generator.generateKey();  
        // 获得密钥  
        byte[] key = secretKey.getEncoded();  
        return key;  
    }  
  
    /** 
     * HmacSHA1摘要算法 
     * 对于给定生成的不同密钥，得到的摘要消息会不同，所以在实际应用中，要保存我们的密钥 
     */  
    public static String encodeHmacSHA512(byte[] data, byte[] key) throws Exception {  
        // 还原密钥  
        SecretKey secretKey = new SecretKeySpec(key, "HmacSHA512");  
        // 实例化Mac  
        Mac mac = Mac.getInstance(secretKey.getAlgorithm());  
        //初始化mac  
        mac.init(secretKey);  
        //执行消息摘要  
        byte[] digest = mac.doFinal(data);  
        return bytesToHexString(digest);//转为十六进制的字符串  
    }
}  
  
