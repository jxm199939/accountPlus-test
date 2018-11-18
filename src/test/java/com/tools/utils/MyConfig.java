package com.tools.utils;

import java.util.Random;
import java.util.UUID;

/** 
 * 配置数据类 
 * @cfg   
 * @version 1.0 
 */

import com.tools.utils.Property;

public class MyConfig {

	
	/**
	 * 获取所属商户号
	 * 
	 * @return返回
	 */
	public static String getOidPartner(String cfg) {

		String partner;
		switch (cfg) {
		case "get_oid_partner":
			partner = Property.get("oid_partner");
			break;
		case "get_oid_partner_pause":
			partner = Property.get("oid_partner_pause");
			break;
		case "get_mchassure_id":
			partner = Property.get("mchassure_id");
			break;
		case "get_oid_partner1":
			partner = Property.get("oid_partner1");
			break;
		case "get_mchcoupon_id":
			partner = Property.get("mchcoupon_id");
			break;
		case "get_user_id":
			partner = Property.get("user_id");
			break;
		case "get_payee_user_id":
			partner = Property.get("payee_user_id");
			break;
		case "get_payee_mch_id":
			partner = Property.get("payee_mch_id");
			break;			
			
		default:
			partner = cfg;
		}
		return partner;
	}
	
	/**
	 * 获取账户号
	 * 
	 * @return返回
	 */
	public static String getAccNO(String cfg) {

		String acctno;
		switch (cfg) {
		case "get_mchassure_P":
			acctno = Property.get("mchassure_P");
			break;
		case "get_mchassure_A":
			acctno = Property.get("mchassure_A");
			break;
		case "get_mchcoupon_P":
			acctno = Property.get("mchcoupon_P");
			break;
		case "get_mchcoupon_A":
			acctno = Property.get("mchcoupon_A");
			break;
		case "get_user_P":
			acctno = Property.get("user_P");
			break;
		case "get_user_A":
			acctno = Property.get("user_A");
			break;
		case "get_payee_user_P":
			acctno = Property.get("payee_user_P");
			break;
		case "get_payee_user_A":
			acctno = Property.get("payee_user_A");
			break;
		case "get_payee_mch_P":
			acctno = Property.get("payee_mch_P");
			break;
		case "get_payee_mch_A":
			acctno = Property.get("payee_mch_A");
			break;	
		default:
			acctno = cfg;
		}
		return acctno;
	}


	/**
	 * 获取用户登录号
	 * 
	 * @return返回
	 */
	public static String getUser(String cfg) {

		String user_id;
		switch (cfg) {
		case "get_user_id_personal":
			user_id = Property.get("user_id_personal");
			break;
		case "get_user_id_enterprise":
			user_id = Property.get("user_id_enterprise");
			break;
		case "get_user_id":
			user_id = DateUtil.getCurrentDateMillisecondStr();
			break;
		case "get_user_id_random":
			user_id = DateUtil.getCurrentDateMillisecondStr() + "@anonymous";
			break;
		case "get_user_id_uuid":
			user_id = getUuid();
			break;
		case "get_user_id_random33":
			user_id = getRandomString(33);
			break;
		default:
			user_id = cfg;
		}
		return user_id;
	}

	/**
	 * 获取付款用户
	 * 
	 * @return返回
	 */
	public static String getPayerId(String cfg) {

		String payer_id;
		switch (cfg) {
		case "get_user_id_personal":
			payer_id = Property.get("user_id_personal");
			break;
		case "get_user_id_enterprise":
			payer_id = Property.get("user_id_enterprise");
			break;
		case "get_oid_partner":
			payer_id = Property.get("oid_partner");
			break;
		default:
			payer_id = cfg;
		}
		return payer_id;
	}
	
	/**
	 * 获取交易流水号
	 * 
	 * @return返回
	 */
	public static String getTxnSeqno(String cfg) {

		String txn_seqno;
		switch (cfg) {
		case "get_txn_seqno":
			txn_seqno = DateUtil.getCurrentDateMillisecondStr();
			break;
		case "get_txn_seqno_random65":
			txn_seqno = getRandomString(65);
			break;
		case "get_txn_seqno_random64":
			txn_seqno = getRandomString(64);
			break;
		default:
			txn_seqno = cfg;
		}
		return txn_seqno;
	}

	/**
	 * 获取交易时间
	 * 
	 * @return返回
	 */
	public static String getTxnTime(String cfg) {

		String txn_time;
		switch (cfg) {
		case "get_txn_time":
			txn_time = DateUtil.getCurrentDateTimeStr();
			break;
		case "get_txn_time_yyyymmdd":
			txn_time = DateUtil.getCurrentDateStr1();
			break;
		case "get_txn_time_yyyyMMdd HH:mm:ss":
			txn_time = DateUtil.getCurrentDateTimeStr1();
			break;
		default:
			txn_time = cfg;
		}
		return txn_time;
	}

	/**
	 * 获取确认订单号
	 * 
	 * @return返回
	 */
	public static String getConfirmSeqno(String cfg) {

		String confirm_seqno;
		switch (cfg) {
		case "get_confirm_seqno":
			confirm_seqno = DateUtil.getCurrentDateMillisecondStr();
			break;
		case "get_confirm_seqno_random65":
			confirm_seqno = getRandomString(65);
			break;
		case "get_confirm_seqno_random64":
			confirm_seqno = getRandomString(64);
			break;
		default:
			confirm_seqno = cfg;
		}
		return confirm_seqno;
	}

	/**
	 * 获取确认时间
	 * 
	 * @return返回
	 */
	public static String getConfirmTime(String cfg) {

		String confirm_time;
		switch (cfg) {
		case "get_confirm_time":
			confirm_time = DateUtil.getCurrentDateTimeStr();
			break;
		case "get_confirm_time_yyyymmdd":
			confirm_time = DateUtil.getCurrentDateStr1();
			break;
		case "get_confirm_time_yyyyMMdd HH:mm:ss":
			confirm_time = DateUtil.getCurrentDateTimeStr1();
			break;
		default:
			confirm_time = cfg;
		}
		return confirm_time;
	}

	/**
	 * 获取退款订单号
	 * 
	 * @return返回
	 */
	public static String getRefundSeqno(String cfg) {

		String refund_seqno;
		switch (cfg) {
		case "get_refund_seqno":
			refund_seqno = DateUtil.getCurrentDateMillisecondStr();
			break;
		case "get_refund_seqno_random65":
			refund_seqno = getRandomString(65);
			break;
		case "get_refund_seqno_random64":
			refund_seqno = getRandomString(64);
			break;
		default:
			refund_seqno = cfg;
		}
		return refund_seqno;
	}

	/**
	 * 获取退款时间
	 * 
	 * @return返回
	 */
	public static String getRefundTime(String cfg) {

		String refund_time;
		switch (cfg) {
		case "get_refund_time":
			refund_time = DateUtil.getCurrentDateTimeStr();
			break;
		case "get_refund_time_yyyymmdd":
			refund_time = DateUtil.getCurrentDateStr1();
			break;
		case "get_refund_time_yyyyMMdd HH:mm:ss":
			refund_time = DateUtil.getCurrentDateTimeStr1();
			break;
		default:
			refund_time = cfg;
		}
		return refund_time;
	}

	/**
	 * 获取时间戳
	 * 
	 * @return返回
	 */
	public static String getTimestamp(String cfg) {

		String timestamp;
		switch (cfg) {
		case "get_timestamp":
			timestamp = DateUtil.getCurrentDateTimeStr();
			break;
		case "get_timestamp_yyyymmdd":
			timestamp = DateUtil.getCurrentDateStr1();
			break;
		case "get_timestamp_yyyyMMdd HH:mm:ss":
			timestamp = DateUtil.getCurrentDateTimeStr1();
			break;
		case "get_timestamp_before30":
			timestamp = DateUtil.getBefore30MinutesStr();
			break;
		case "get_timestamp_after30":
			timestamp = DateUtil.getAfter30MinutesStr();
			break;
		default:
			timestamp = cfg;
		}
		return timestamp;
	}

	/**
	 * 获取随机字符串
	 * 
	 * @return返回指定长度随机字符串
	 */
	public static String getRandomString(int length) {
		String base = "abcdefghijklmnopqrstuvwxyz0123456789";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}

	/**
	 * 获取32位唯一标识
	 * 
	 * @return返回唯一标识uuid
	 */
	public static String getUuid() {
		String uuid = UUID.randomUUID().toString().replace("-", "");
		return uuid.toString();
	}

	public static void main(String[] args) throws Exception {
		Property.set();
		try {
			System.out.println(getOidPartner("get_oid_partner"));

			// System.out.println(getUuid());
			// System.out.println(getRandomString(6));

			System.out.println(getTxnTime("get_txn_time_yyyymmdd"));
		} catch (Exception e) {
			throw new Exception();
		}
	}
}
