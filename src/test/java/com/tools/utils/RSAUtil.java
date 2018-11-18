package com.tools.utils;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.Cipher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * RSA签名公共类
 * 
 */
public class RSAUtil {

	private static Logger log = LoggerFactory.getLogger(RSAUtil.class);
	private static  final String CHARSET="UTF-8";
	private static RSAUtil instance;
	private RSAUtil() {

	}

	public static RSAUtil getInstance() {
		if (null == instance)
			return new RSAUtil();
		return instance;
	}

	/**
	 * 签名处理
	 * 
	 * @param prikeyvalue
	 *            ：私钥文件
	 * @param sign_str
	 *            ：签名源内容
	 * @return
	 */
	public  String sign(String prikeyvalue, String sign_str) {
		try {
			PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64.getBytesBASE64(prikeyvalue));
			KeyFactory keyf = KeyFactory.getInstance("RSA");
			PrivateKey myprikey = keyf.generatePrivate(priPKCS8);
			// 用私钥对信息生成数字签名
			java.security.Signature signet = java.security.Signature.getInstance("MD5withRSA");
			signet.initSign(myprikey);
			signet.update(sign_str.getBytes(CHARSET));
			byte[] signed = signet.sign(); // 对信息的数字签名
			return new String(org.apache.commons.codec.binary.Base64.encodeBase64(signed));
		} catch (java.lang.Exception e) {
			log.error("签名失败,{}" , e.getMessage());
		}
		return null;
	}

	/**
	 * 签名验证
	 * 
	 * @param pubkeyvalue
	 *            ：公钥
	 * @param oid_str
	 *            ：源串
	 * @param signed_str
	 *            ：签名结果串
	 * @return
	 */
	public  boolean checksign(String pubkeyvalue, String oid_str, String signed_str) {
		try {
			X509EncodedKeySpec bobPubKeySpec = new X509EncodedKeySpec(Base64.getBytesBASE64(pubkeyvalue));
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			PublicKey pubKey = keyFactory.generatePublic(bobPubKeySpec);
			byte[] signed = Base64.getBytesBASE64(signed_str);// 这是SignatureData输出的数字签名
			java.security.Signature signetcheck = java.security.Signature.getInstance("MD5withRSA");
			signetcheck.initVerify(pubKey);
			signetcheck.update(oid_str.getBytes(CHARSET));
			return signetcheck.verify(signed);
		} catch (java.lang.Exception e) {
			log.error("签名验证异常,{}" , e.getMessage());
		}
		return false;
	}

	/**
	 * 公钥加密的方法
	 * 
	 * @param source
	 * @param public_key
	 * @return
	 * @throws Exception
	 */
	public  String encrypt(String source, String public_key) throws Exception {

		BASE64Decoder b64d = new BASE64Decoder();
		byte[] keyByte = b64d.decodeBuffer(public_key);
		X509EncodedKeySpec x509ek = new X509EncodedKeySpec(keyByte);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PublicKey publicKey = keyFactory.generatePublic(x509ek);

		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		byte[] sbt = source.getBytes(CHARSET); // 中文情况下 linux 环境要使用这个
		byte[] epByte = cipher.doFinal(sbt);
		BASE64Encoder encoder = new BASE64Encoder();
		String epStr = encoder.encode(epByte);
		return epStr;

	}

	/**
	 * 私钥解密的方法
	 * 
	 * @param cryptograph
	 * @param private_key
	 * @return
	 * @throws Exception
	 */
	public  String decrypt(String cryptograph, String private_key) throws Exception {

		BASE64Decoder b64d = new BASE64Decoder();
		byte[] keyByte = b64d.decodeBuffer(private_key);
		PKCS8EncodedKeySpec s8ek = new PKCS8EncodedKeySpec(keyByte);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PrivateKey privateKey = keyFactory.generatePrivate(s8ek);

		/** 得到Cipher对象对已用公钥加密的数据进行RSA解密 */
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		BASE64Decoder decoder = new BASE64Decoder();
		byte[] b1 = decoder.decodeBuffer(cryptograph);
		/** 执行解密操作 */
		byte[] b = cipher.doFinal(b1);
		return new String(b);
	}

	public static void main(String args[]){
//		String rsa_public ="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCqsEbJDbhe64JnkHNcpSljhkrE6rZekhE1cDklh/GLdoK4Gsd6/n/8icHNIIn6b8R3Ba+/3S5GxiuGfEc4rhBAffLKZPV2QNh0DafbWDHnu2S6PYGLrrWINawnKJJu5NLcLA0n5As8ZYmwCe5oO1rwCf2reoNWfd+K3b4LV/yTVwIDAQAB";
//		String oid_str ="api_version=1.0&no_order=20170216200007&oid_plat=201704100000005002&sign_type=RSA";
//		String signed_str ="oqqEKP2NMP15fPU1Z7ecOI5GXA213cx3ylTYkEQuLIF9tKkt6pRDNMxYmqiC1j96l0MJKBkIVYy/Nx86Il4mYiP/ZG+2buGLQ8sHhicz6Q16P1htcsV3sEauoYvTZ3jMfkTObce/adf9hHYx0OGnjbvhpDjnFi6ohGz5WnNT2jo=";

		String rsa_public ="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCSS/DiwdCf/aZsxxcacDnooGph3d2JOj5GXWi+q3gznZauZjkNP8SKl3J2liP0O6rU/Y/29+IUe+GTMhMOFJuZm1htAtKiu5ekW0GlBMWxf4FPkYlQkPE0FtaoMP3gYfh+OwI+fIRrpW3ySn3mScnc6Z700nU/VYrRkfcSCbSnRwIDAQAB";
		String oid_str ="dt_register=2017-11-29 15:22:50&kyc_status=4&mob_bind=140****0005&name_unit=测试个体&num_license=123123123123&oid_partner=201701120000283004&sign_type=RSA&type_register=1&user_id=2017112905";
		String signed_str ="dBZJNzSyFveKeSSMyJGGQ46GSWWZrknEy237orDkMXn48TRiiedEaJ5Z0TKa/RTtWCSUzCUP5b6Imfnai+m15x52pJSAWQgH2M002SKf3TWWLIgFvzKMzjurwXGKTKLDmlmjrA2ngNftjk9/KJInXr71CL6lwdJiG7MGn7C4Ob4=";

		System.out.print(RSAUtil.getInstance().checksign(rsa_public,oid_str,signed_str));
	}
}
