package com.tools.utils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.alibaba.fastjson.JSONObject;
import com.lianpay.api.util.TraderRSAUtil;

/**
 * 生成签名串
 * @author panj
 * 
 */
public class GenerateSign {
	
   private static String       TRADER_MD5_KEY = "201504080000007002";//201407032000003743 360_pay_mobilesafe_0703  md5key_201311062000003548_20131107  yintong1234567890
   private final static String prikeyvalue    = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAOilN4tR7HpNYvSBra/DzebemoAiGtGeaxa+qebx/O2YAdUFPI+xTKTX2ETyqSzGfbxXpmSax7tXOdoa3uyaFnhKRGRvLdq1kTSTu7q5s6gTryxVH2m62Py8Pw0sKcuuV0CxtxkrxUzGQN+QSxf+TyNAv5rYi/ayvsDgWdB3cRqbAgMBAAECgYEAj02d/jqTcO6UQspSY484GLsL7luTq4Vqr5L4cyKiSvQ0RLQ6DsUG0g+Gz0muPb9ymf5fp17UIyjioN+ma5WquncHGm6ElIuRv2jYbGOnl9q2cMyNsAZCiSWfR++op+6UZbzpoNDiYzeKbNUz6L1fJjzCt52w/RbkDncJd2mVDRkCQQD/Uz3QnrWfCeWmBbsAZVoM57n01k7hyLWmDMYoKh8vnzKjrWScDkaQ6qGTbPVL3x0EBoxgb/smnT6/A5XyB9bvAkEA6UKhP1KLi/ImaLFUgLvEvmbUrpzY2I1+jgdsoj9Bm4a8K+KROsnNAIvRsKNgJPWd64uuQntUFPKkcyfBV1MXFQJBAJGs3Mf6xYVIEE75VgiTyx0x2VdoLvmDmqBzCVxBLCnvmuToOU8QlhJ4zFdhA1OWqOdzFQSw34rYjMRPN24wKuECQEqpYhVzpWkA9BxUjli6QUo0feT6HUqLV7O8WqBAIQ7X/IkLdzLa/vwqxM6GLLMHzylixz9OXGZsGAkn83GxDdUCQA9+pQOitY0WranUHeZFKWAHZszSjtbe6wDAdiKdXCfig0/rOdxAODCbQrQs7PYy1ed8DuVQlHPwRGtokVGHATU=";

	
	public static String genSign(JSONObject reqObj)//API签名使用
    {
        String sign_type=reqObj.getString("sign_type");
        String sign_src = genSignData(reqObj);//生成待签名串

        if("MD5".equals(sign_type)){
            sign_src += "&key=" + TRADER_MD5_KEY;
            return signMD5(sign_src,"utf-8");
        }
        if("RSA".equals(sign_type)){
            System.out.println("商户[" + reqObj.getString("oid_partner") + "]RSA签名串:"
                    + getSignRSA(sign_src));
            return getSignRSA(sign_src);
        }
        return null;
    }
	
	public static String genSign(JSONObject reqObj,String key)//API签名使用
    {
        String sign_type=reqObj.getString("sign_type");
        String sign_src = genSignData(reqObj);//生成待签名串

        if("MD5".equals(sign_type)){
            sign_src += "&key=" + key;
            return signMD5(sign_src,"utf-8");
        }
        
        if("RSA".equals(sign_type)){
            System.out.println("商户[" + reqObj.getString("oid_partner") + "]RSA签名串:"
                    + getSignRSA(sign_src,key));
            return getSignRSA(sign_src,key);
        }
        return null;
    }
	
	public static String genSdkSign(JSONObject reqObj,String key)//SDK所有参数签名使用
    {
        String sign_type=reqObj.getString("sign_type");
        String sign_src = genSignData(reqObj);//生成待签名串
//        System.out.println("商户[" + reqObj.getString("oid_partner") + "]待签名原串:" + sign_src);

        if("MD5".equals(sign_type)){
            sign_src += "&sign_key=" + key;
//            System.out.println("商户[" + reqObj.getString("oid_partner") + "]MD5签名串:" + sign_src);
//            System.out.println("商户[" + reqObj.getString("oid_partner") + "]MD5签名值:" + signMD5(sign_src,"iso8859-1").toUpperCase());
            return signMD5(sign_src,"iso8859-1").toUpperCase();
        }
        
        if("RSA".equals(sign_type)){
            System.out.println("商户[" + reqObj.getString("oid_partner") + "]RSA签名串:"
                    + getSignRSA(sign_src,key));
            return getSignRSA(sign_src,key);
        }
        return null;
    }

    public static String signMD5(String signSrc, String encoding)
    {
        try
        {
            return Md5Algorithm.getInstance().md5Digest(
                    signSrc.getBytes(encoding));
        } catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
            return null;
        }
    }
    
    public static String addHMacSign(JSONObject reqObj, String md5_key)
    {
        // 生成待签名串
        String sign_src = genSignData(reqObj);
        System.out.println("商户[" + reqObj.getString("oid_partner") + "]待签名原串"
                + sign_src);
        return HMACsign(sign_src,md5_key);
    }
    
    public static String HMACsign(String signSrc,String md5_key)
    {
        String sign_res="";
        try
        {
            sign_res=MACCoder.encodeHmacSHA256(signSrc.getBytes("UTF-8"),md5_key.getBytes("UTF-8"));
        } catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
        return sign_res;
    }
    
    /**
     * RSA签名验证
     * 
     * @param reqObj
     * @return
     */
    public static String getSignRSA(String sign_src)
    {
        return TraderRSAUtil.sign(prikeyvalue, sign_src);
    }
    
    public static String getSignRSA(String sign_src,String prikeyvalue)
    {
        return TraderRSAUtil.sign(prikeyvalue, sign_src);
    }


    /**
     * 生成待签名串
     * 
     * @param paramMap
     * @return
     */
    public static String genSignData(JSONObject jsonObject)
    {
        StringBuffer content = new StringBuffer();

        // 按照key做首字母升序排列
        List<String> keys = new ArrayList<String>(jsonObject.keySet());
        Collections.sort(keys, String.CASE_INSENSITIVE_ORDER);
        for (int i = 0; i < keys.size(); i++)
        {
            String key = (String) keys.get(i);
            if ("sign".equals(key)) // sign 和ip_client 不参与签名
            {
                continue;
            }
            String value = (String) jsonObject.getString(key);
            if (null==value || value.equals("")) //空串不参与签名     value.equals("")新加
            {
                continue;
            }
            content.append((i == 0 ? "" : "&") + key + "=" + value);

        }
        String signSrc = content.toString();
        if (signSrc.startsWith("&"))
        {
            signSrc = signSrc.replaceFirst("&", "");
        }
        return signSrc;
    }

}
