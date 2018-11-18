package com.tools.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * RSA签名公共类
 * 
 */
public class SignatureUtil {

	private static Logger log = LoggerFactory.getLogger(SignatureUtil.class);
	private static  final String CHARSET="UTF-8";
	private static SignatureUtil instance;
	private SignatureUtil() {

	}

	public static SignatureUtil getInstance() {
		if (null == instance)
			return new SignatureUtil();
		return instance;
	}

	/**
	 * 签名处理
	 * 
	 * @param prikeyvalue
	 *            ：私钥
	 * @param sign_str
	 *            ：签名源内容
	 * @return
	 */
	public  String sign(String prikeyvalue, String sign_str) {
		try {
			String hash = Md5Algorithm.getInstance().md5Digest(sign_str.getBytes(CHARSET));
			return RSAUtil.getInstance().sign(prikeyvalue, hash);
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
	 * @param sign_str
	 *            ：源串
	 * @param signed_str
	 *            ：签名结果串
	 * @return
	 */
	public  boolean checksign(String pubkeyvalue, String sign_str, String signed_str) {
		try {
			String hash = Md5Algorithm.getInstance().md5Digest(sign_str.getBytes(CHARSET));
			return RSAUtil.getInstance().checksign(pubkeyvalue, hash, signed_str);
		} catch (java.lang.Exception e) {
			log.error("签名验证异常,{}" , e.getMessage());
		}
		return false;
	}
	public static void main(String args[]){
//		String rsa_public ="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCqsEbJDbhe64JnkHNcpSljhkrE6rZekhE1cDklh/GLdoK4Gsd6/n/8icHNIIn6b8R3Ba+/3S5GxiuGfEc4rhBAffLKZPV2QNh0DafbWDHnu2S6PYGLrrWINawnKJJu5NLcLA0n5As8ZYmwCe5oO1rwCf2reoNWfd+K3b4LV/yTVwIDAQAB";
//		String oid_str ="api_version=1.0&no_order=20170216200007&oid_plat=201704100000005002&sign_type=RSA";
//		String signed_str ="oqqEKP2NMP15fPU1Z7ecOI5GXA213cx3ylTYkEQuLIF9tKkt6pRDNMxYmqiC1j96l0MJKBkIVYy/Nx86Il4mYiP/ZG+2buGLQ8sHhicz6Q16P1htcsV3sEauoYvTZ3jMfkTObce/adf9hHYx0OGnjbvhpDjnFi6ohGz5WnNT2jo=";
		String rsa_public ="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCSS/DiwdCf/aZsxxcacDnooGph3d2JOj5GXWi+q3gznZauZjkNP8SKl3J2liP0O6rU/Y/29+IUe+GTMhMOFJuZm1htAtKiu5ekW0GlBMWxf4FPkYlQkPE0FtaoMP3gYfh+OwI+fIRrpW3ySn3mScnc6Z700nU/VYrRkfcSCbSnRwIDAQAB";
		String oid_str ="{\"orderInfo\":{\"total_amount\":\"0.99\",\"txn_seqno\":\"20180621141927882\",\"order_info\":\"test\",\"txn_time\":\"20180621141927\"},\"payeeInfo\":[{\"amount\":\"0.99\",\"payee_type\":\"MERCHANT\",\"payee_id\":\"201701120000283004\"}],\"oid_partner\":\"201701120000283004\",\"accounting_date\":\"20180621\",\"txn_status\":\"TRADE_SUCCESS\",\"finish_time\":\"20180621141627\",\"accp_txno\":\"2018062100130866\",\"txn_type\":\"GENERAL_CONSUME\",\"payerInfo\":[{\"amount\":\"0.98\",\"payer_id\":\"201806181436365850\",\"payer_type\":\"USER\",\"method\":\"BALANCE\"},{\"amount\":\"0.01\",\"payer_id\":\"201701120000283004\",\"payer_type\":\"MERCHANT\",\"method\":\"COUPON\"}]}";
		String signed_str ="EZr+Gtt5Gl60QwvEr9Pl/FUh0RBOjvrh/TublunPRKFraZikjcO3bCz41IORutrekBDAjt1J/25KI0f32rvy9l3oVa8WCUzi4Fu63n+KSmO2Pt5geEY2nuDi7OGT0UHdybUCDRNs1u3b/7p4aIZxw1aFXiqtJnaxNixtXXDBvpE=";

		System.out.print(SignatureUtil.getInstance().checksign(rsa_public,oid_str,signed_str));
	}
}