package com.accp.test.api.test.txn;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.accp.test.bean.txn.ConfirmOrderInfo;
import com.accp.test.bean.txn.OrderInfo;
import com.accp.test.bean.txn.OriginalOrderInfo;
import com.accp.test.bean.txn.PayeeInfo;
import com.accp.test.bean.txn.PayerInfo;
import com.accp.test.bean.txn.PaymentBalance;
import com.accp.test.bean.txn.SecuredConfirm;
import com.accp.test.bean.txn.TradeCreate;
import com.accp.test.bean.txn.ValidationSms;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lianlian.crypt.service.IAESCryptService;
import com.tools.dataprovider.ExcelProvider;
import com.tools.utils.DataBaseAccess;
import com.tools.utils.DataBaseAccessPayCore;
import com.tools.utils.HttpRequestSimple;
import com.tools.utils.MyConfig;
import com.tools.utils.MyDate;
import com.tools.utils.Property;
import com.tools.utils.SampleFileUtils;
import com.tools.utils.SignatureUtil;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

/*
 * @author jiangxm
 * 担保交易确认接口测试
 */

@ContextConfiguration(locations = { "/consumer.xml" })
public class SecuredConfirmTest extends AbstractTestNGSpringContextTests {

	DataBaseAccess dataBaseAccess = new DataBaseAccess();
	DataBaseAccessPayCore dataBaseAccessPayCore = new DataBaseAccessPayCore();

	@Autowired
	private IAESCryptService aesCryptService;

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "担保交易确认接口测试", timeOut = 60000, dataProvider = "securedConfirm")
	public void securedConfirm(Map<String, String> datadriven) throws Exception {

		String tradeCreateUrl = Property.get("tradeCreate.url.test");
		String paymentBalanceUrl = Property.get("paymentBalance.url.test");
		String validationSmsUrl = Property.get("validationSms.url.test");
		String securedConfirmUrl = Property.get("securedConfirm.url.test");
		TradeCreate tradeCreate = new TradeCreate();
		OrderInfo orderInfo = new OrderInfo();
		String payeeInfo0 = datadriven.get("payeeInfo0");
		String payeeInfo1 = datadriven.get("payeeInfo1");
		String payeeInfo2 = datadriven.get("payeeInfo2");
		String payeeInfo3 = datadriven.get("payeeInfo3");
		String payeeInfo4 = datadriven.get("payeeInfo4");
		String payeeInfo5 = datadriven.get("payeeInfo5");
		String payeeInfo6 = datadriven.get("payeeInfo6");
		String payeeInfo7 = datadriven.get("payeeInfo7");
		String payeeInfo8 = datadriven.get("payeeInfo8");
		String payeeInfo9 = datadriven.get("payeeInfo9");
		List<PayeeInfo> payeeInfoList = new ArrayList<PayeeInfo>();
		String oid_partner = MyConfig.getOidPartner(datadriven.get("securedConfirm.oid_partner"));
		String user_id = MyConfig.getUser(datadriven.get("securedConfirm.user_id"));
		String ipUpdate = datadriven.get("ipUpdate");
		String traderStatUpdate = datadriven.get("traderStatUpdate");
		String userStatUpdate = datadriven.get("userStatUpdate");
		String payBillStatUpdate = datadriven.get("payBillStatUpdate");
		String acctBalUpdate = datadriven.get("acctBalUpdate");

		try {
			// 创单
			Reporter.log("##############################【xxxx测试开始】############################", true);
			tradeCreate.setTimestamp(MyConfig.getTimestamp(datadriven.get("timestamp")));
			tradeCreate.setOid_partner(MyConfig.getOidPartner(datadriven.get("oid_partner")));
			tradeCreate.setTxn_type(datadriven.get("txn_type"));
			tradeCreate.setUser_id(MyConfig.getUser(datadriven.get("user_id")));
			tradeCreate.setUser_type(datadriven.get("user_type"));
			tradeCreate.setNotify_url(datadriven.get("notify_url"));
			tradeCreate.setReturn_url(datadriven.get("return_url"));
			tradeCreate.setPay_expire(datadriven.get("pay_expire"));

			orderInfo.setTxn_seqno(MyConfig.getTxnSeqno(datadriven.get("txn_seqno")));
			orderInfo.setTxn_time(MyConfig.getTxnTime(datadriven.get("txn_time")));
			orderInfo.setTotal_amount(datadriven.get("total_amount"));
			orderInfo.setOrder_info(datadriven.get("order_info"));
			orderInfo.setGoods_name(datadriven.get("goods_name"));
			orderInfo.setGoods_url(datadriven.get("goods_url"));
			tradeCreate.setOrderInfo(orderInfo);

			if (!StringUtils.isBlank(payeeInfo0)) {
				payeeInfoList.add(JSON.parseObject(payeeInfo0, PayeeInfo.class));
			}
			if (!StringUtils.isBlank(payeeInfo1)) {
				payeeInfoList.add(JSON.parseObject(payeeInfo1, PayeeInfo.class));
			}
			if (!StringUtils.isBlank(payeeInfo2)) {
				payeeInfoList.add(JSON.parseObject(payeeInfo2, PayeeInfo.class));
			}
			if (!StringUtils.isBlank(payeeInfo3)) {
				payeeInfoList.add(JSON.parseObject(payeeInfo3, PayeeInfo.class));
			}
			if (!StringUtils.isBlank(payeeInfo4)) {
				payeeInfoList.add(JSON.parseObject(payeeInfo4, PayeeInfo.class));
			}
			if (!StringUtils.isBlank(payeeInfo5)) {
				payeeInfoList.add(JSON.parseObject(payeeInfo5, PayeeInfo.class));
			}
			if (!StringUtils.isBlank(payeeInfo6)) {
				payeeInfoList.add(JSON.parseObject(payeeInfo6, PayeeInfo.class));
			}
			if (!StringUtils.isBlank(payeeInfo7)) {
				payeeInfoList.add(JSON.parseObject(payeeInfo7, PayeeInfo.class));
			}
			if (!StringUtils.isBlank(payeeInfo8)) {
				payeeInfoList.add(JSON.parseObject(payeeInfo8, PayeeInfo.class));
			}
			if (!StringUtils.isBlank(payeeInfo9)) {
				payeeInfoList.add(JSON.parseObject(payeeInfo9, PayeeInfo.class));
			}
			if (!payeeInfoList.isEmpty()) {
				tradeCreate.setPayeeInfo(payeeInfoList);
			}

			String reqJson = JSON.toJSONString(tradeCreate);
			Reporter.log("创单请求报文:" + reqJson, true);
			String sign = SignatureUtil.getInstance().sign(datadriven.get("private_key"), reqJson);
			HttpRequestSimple httpclient = new HttpRequestSimple();
			String[] res = httpclient.post(tradeCreateUrl, reqJson, Property.get("SIGNATURE_TYPE"), sign);
			String resJson = res[0];
			String resSignatureData = res[1];
			String txn_seqno = JSONObject.parseObject(resJson).getString("txn_seqno");
			String accp_txno = JSONObject.parseObject(resJson).getString("accp_txno");
			Reporter.log("创单返回报文:" + resJson, true);

			// 余额支付
			PaymentBalance paymentBalance = new PaymentBalance();
			PayerInfo payerInfo = new PayerInfo();

			paymentBalance.setTimestamp(MyConfig.getTimestamp(datadriven.get("paymentBalance.timestamp")));
			paymentBalance.setOid_partner(MyConfig.getOidPartner(datadriven.get("paymentBalance.oid_partner")));
			if ("get".equals(datadriven.get("paymentBalance.txn_seqno"))) {
				paymentBalance.setTxn_seqno(txn_seqno);
			} else {
				paymentBalance.setTxn_seqno(datadriven.get("paymentBalance.txn_seqno"));
			}
			paymentBalance.setTotal_amount(datadriven.get("paymentBalance.total_amount"));
			paymentBalance.setCoupon_amount(datadriven.get("paymentBalance.coupon_amount"));
			paymentBalance.setRisk_item(datadriven.get("paymentBalance.risk_item"));

			payerInfo.setUser_id(MyConfig.getUser(datadriven.get("paymentBalance.user_id")));
			payerInfo.setPassword(datadriven.get("paymentBalance.password"));
			payerInfo.setRandom_key(datadriven.get("paymentBalance.random_key"));
			paymentBalance.setPayerInfo(payerInfo);

			String reqJson1 = JSON.toJSONString(paymentBalance);
			Reporter.log("余额支付请求报文:" + reqJson1, true);
			String sign1 = SignatureUtil.getInstance().sign(datadriven.get("paymentBalance.private_key"), reqJson1);
			String[] res1 = httpclient.post(paymentBalanceUrl, reqJson1, Property.get("SIGNATURE_TYPE"), sign1);
			String resJson1 = res1[0];
			String resSignatureData1 = res1[1];
			String token = JSONObject.parseObject(resJson1).getString("token");
			Reporter.log("余额支付返回报文:" + resJson1, true);
			// 验签
			assert SignatureUtil.getInstance().checksign(Property.get("YT_RSA_PUBLIC"), resJson1, resSignatureData1);

			// 余额支付验证
			if ("8888".equals(JSONObject.parseObject(resJson1).getString("ret_code"))) {
				ValidationSms validationSms = new ValidationSms();
				validationSms.setTimestamp(MyConfig.getTimestamp(datadriven.get("validationSms.timestamp")));
				validationSms.setOid_partner(MyConfig.getOidPartner(datadriven.get("validationSms.oid_partner")));
				validationSms.setPayer_type(datadriven.get("validationSms.payer_type"));
				validationSms.setPayer_id(datadriven.get("validationSms.payer_id"));
				if ("get".equals(datadriven.get("validationSms.txn_seqno"))) {
					validationSms.setTxn_seqno(txn_seqno);
				} else {
					validationSms.setTxn_seqno(datadriven.get("validationSms.txn_seqno"));
				}
				validationSms.setTotal_amount(datadriven.get("validationSms.total_amount"));
				if ("get".equals(datadriven.get("validationSms.token"))) {
					validationSms.setToken(token);
				} else {
					validationSms.setToken(datadriven.get("validationSms.token"));
				}
				if ("get".equals(datadriven.get("validationSms.verify_code"))) {
					String reg_phone = dataBaseAccess.getUserInfo(aesCryptService,
							MyConfig.getUser(datadriven.get("user_id")),
							MyConfig.getOidPartner(datadriven.get("oid_partner")))[1];
					String verify_code = dataBaseAccess.getSms(reg_phone);
					validationSms.setVerify_code(verify_code);
				} else {
					validationSms.setVerify_code(datadriven.get("validationSms.verify_code"));
				}
				String reqJson2 = JSON.toJSONString(validationSms);
				Reporter.log("二次验证请求报文:" + reqJson2, true);
				String sign2 = SignatureUtil.getInstance().sign(datadriven.get("validationSms.private_key"), reqJson2);
				String[] res2 = httpclient.post(validationSmsUrl, reqJson2, Property.get("SIGNATURE_TYPE"), sign2);
				String resJson2 = res2[0];
				String resSignatureData2 = res2[1];
				Reporter.log("二次验证返回报文:" + resJson2, true);
				// 验签
				assert SignatureUtil.getInstance().checksign(Property.get("YT_RSA_PUBLIC"), resJson2,
						resSignatureData2);

			}

			// 担保交易确认
			SecuredConfirm securedConfirm = new SecuredConfirm();
			OriginalOrderInfo originalOrderInfo = new OriginalOrderInfo();
			ConfirmOrderInfo confirmOrderInfo = new ConfirmOrderInfo();
			String payeeInfo10 = datadriven.get("securedConfirm.payeeInfo0");
			String payeeInfo11 = datadriven.get("securedConfirm.payeeInfo1");
			String payeeInfo12 = datadriven.get("securedConfirm.payeeInfo2");
			String payeeInfo13 = datadriven.get("securedConfirm.payeeInfo3");
			String payeeInfo14 = datadriven.get("securedConfirm.payeeInfo4");
			String payeeInfo15 = datadriven.get("securedConfirm.payeeInfo5");
			String payeeInfo16 = datadriven.get("securedConfirm.payeeInfo6");
			String payeeInfo17 = datadriven.get("securedConfirm.payeeInfo7");
			String payeeInfo18 = datadriven.get("securedConfirm.payeeInfo8");
			String payeeInfo19 = datadriven.get("securedConfirm.payeeInfo9");
			List<PayeeInfo> payeeInfoList1 = new ArrayList<PayeeInfo>();

			securedConfirm.setTimestamp(MyConfig.getTimestamp(datadriven.get("securedConfirm.timestamp")));
			securedConfirm.setOid_partner(oid_partner);
			securedConfirm.setUser_id(user_id);
			securedConfirm.setNotify_url(datadriven.get("securedConfirm.notify_url"));
			securedConfirm.setConfirm_mode(datadriven.get("securedConfirm.confirm_mode"));

			if ("get".equals(datadriven.get("securedConfirm.txn_seqno"))) {
				originalOrderInfo.setTxn_seqno(txn_seqno);
			} else {
				originalOrderInfo.setTxn_seqno(datadriven.get("securedConfirm.txn_seqno"));
			}
			originalOrderInfo.setTotal_amount(datadriven.get("securedConfirm.total_amount"));
			securedConfirm.setOriginalOrderInfo(originalOrderInfo);

			confirmOrderInfo.setConfirm_seqno(MyConfig.getConfirmSeqno(datadriven.get("securedConfirm.confirm_seqno")));
			confirmOrderInfo.setConfirm_time(MyConfig.getConfirmTime(datadriven.get("securedConfirm.confirm_time")));
			confirmOrderInfo.setConfirm_amount(datadriven.get("securedConfirm.confirm_amount"));
			securedConfirm.setConfirmOrderInfo(confirmOrderInfo);

			if (!StringUtils.isBlank(payeeInfo10)) {
				payeeInfoList1.add(JSON.parseObject(payeeInfo10, PayeeInfo.class));
			}
			if (!StringUtils.isBlank(payeeInfo11)) {
				payeeInfoList1.add(JSON.parseObject(payeeInfo11, PayeeInfo.class));
			}
			if (!StringUtils.isBlank(payeeInfo12)) {
				payeeInfoList1.add(JSON.parseObject(payeeInfo12, PayeeInfo.class));
			}
			if (!StringUtils.isBlank(payeeInfo13)) {
				payeeInfoList1.add(JSON.parseObject(payeeInfo13, PayeeInfo.class));
			}
			if (!StringUtils.isBlank(payeeInfo14)) {
				payeeInfoList1.add(JSON.parseObject(payeeInfo14, PayeeInfo.class));
			}
			if (!StringUtils.isBlank(payeeInfo15)) {
				payeeInfoList1.add(JSON.parseObject(payeeInfo15, PayeeInfo.class));
			}
			if (!StringUtils.isBlank(payeeInfo16)) {
				payeeInfoList1.add(JSON.parseObject(payeeInfo16, PayeeInfo.class));
			}
			if (!StringUtils.isBlank(payeeInfo17)) {
				payeeInfoList1.add(JSON.parseObject(payeeInfo17, PayeeInfo.class));
			}
			if (!StringUtils.isBlank(payeeInfo18)) {
				payeeInfoList1.add(JSON.parseObject(payeeInfo18, PayeeInfo.class));
			}
			if (!StringUtils.isBlank(payeeInfo19)) {
				payeeInfoList1.add(JSON.parseObject(payeeInfo19, PayeeInfo.class));
			}
			if (!payeeInfoList1.isEmpty()) {
				securedConfirm.setPayeeInfo(payeeInfoList1);
			}

			if (!StringUtils.isBlank(ipUpdate)) {
				dataBaseAccess.updateIpRequest(ipUpdate, oid_partner);
			}
			if (!StringUtils.isBlank(traderStatUpdate)) {
				dataBaseAccess.updateTraderEnableState(traderStatUpdate, oid_partner);
			}
			if (!StringUtils.isBlank(userStatUpdate)) {
				dataBaseAccess.updateUserState1(userStatUpdate, user_id, oid_partner);
			}
			if (!StringUtils.isBlank(payBillStatUpdate)) {
				dataBaseAccessPayCore.updatePayBillState(payBillStatUpdate, accp_txno);
			}
			if (!StringUtils.isBlank(acctBalUpdate)) {
				switch (acctBalUpdate) {
				case "USEROWN_PSETTLE":
					String user_no = dataBaseAccess.getUserInfo(aesCryptService, user_id, oid_partner)[0];
					String oid_acctno = dataBaseAccess.getAcctNo(user_no, "USEROWN_PSETTLE");
					String oid_acctno1 = dataBaseAccess.getAcctNo(user_no, "USEROWN_AVAILABLE");
					dataBaseAccessPayCore.updateAcctinfo(oid_acctno, 10);
					dataBaseAccessPayCore.updateAcctinfo(oid_acctno1, 10);
					break;
				case "USEROWN_AVAILABLE":
					String user_no1 = dataBaseAccess.getUserInfo(aesCryptService, user_id, oid_partner)[0];
					String oid_acctno2 = dataBaseAccess.getAcctNo(user_no1, "USEROWN_AVAILABLE");
					dataBaseAccessPayCore.updateAcctinfo(oid_acctno2, 10);
					break;
				case "MCHASSURE_PSETTLE":
					String oid_acctno3 = dataBaseAccess.getAcctNo(oid_partner, "MCHASSURE_PSETTLE");
					String oid_acctno4 = dataBaseAccess.getAcctNo(oid_partner, "MCHASSURE_AVAILABLE");
					dataBaseAccessPayCore.updateAcctinfo(oid_acctno3, 10);
					dataBaseAccessPayCore.updateAcctinfo(oid_acctno4, 10);
					break;
				case "MCHASSURE_AVAILABLE":
					String oid_acctno5 = dataBaseAccess.getAcctNo(oid_partner, "MCHASSURE_AVAILABLE");
					dataBaseAccessPayCore.updateAcctinfo(oid_acctno5, 10);
					break;
				default:

				}
			}
			String reqJson3 = JSON.toJSONString(securedConfirm);
			Reporter.log("担保交易确认请求报文:" + reqJson3, true);
			String sign3 = SignatureUtil.getInstance().sign(datadriven.get("securedConfirm.private_key"), reqJson3);
			String[] res3 = httpclient.post(securedConfirmUrl, reqJson3, Property.get("SIGNATURE_TYPE"), sign3);
			String resJson3 = res3[0];
			String resSignatureData3 = res3[1];
			Reporter.log("担保交易确认返回报文:" + resJson3, true);

			// 担保交易确认请求返回检查
			assert resJson3.contains(datadriven.get("expect_retcode"));
			if ("0000".equals(JSONObject.parseObject(resJson3).getString("ret_code"))) {
				assert SignatureUtil.getInstance().checksign(Property.get("YT_RSA_PUBLIC"), resJson3,
						resSignatureData3);
			}

			// 请求&响应写入文件
			SampleFileUtils.appendLine("D://TA//accplog//securedConfirm.txt", MyDate.getStringDate());
			SampleFileUtils.appendLine("D://TA//accplog//securedConfirm.txt", reqJson);
			SampleFileUtils.appendLine("D://TA//accplog//securedConfirm.txt", resJson);
			SampleFileUtils.appendLine("D://TA//accplog//securedConfirm.txt", reqJson1);
			SampleFileUtils.appendLine("D://TA//accplog//securedConfirm.txt", resJson1);
			SampleFileUtils.appendLine("D://TA//accplog//securedConfirm.txt", reqJson3);
			SampleFileUtils.appendLine("D://TA//accplog//securedConfirm.txt", resJson3);
			SampleFileUtils.appendLine("D://TA//accplog//securedConfirm.txt",
					"===============================================");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			if (!StringUtils.isBlank(ipUpdate)) {
				dataBaseAccess.updateIpRequest("*.*.*.*", oid_partner);
			}
			if (!StringUtils.isBlank(traderStatUpdate)) {
				dataBaseAccess.updateTraderEnableState("0", oid_partner);
			}
			if (!StringUtils.isBlank(userStatUpdate)) {
				dataBaseAccess.updateUserState1("NORMAL", user_id, oid_partner);
			}
			if (!StringUtils.isBlank(acctBalUpdate)) {
				switch (acctBalUpdate) {
				case "USEROWN_PSETTLE":
					String user_no = dataBaseAccess.getUserInfo(aesCryptService, user_id, oid_partner)[0];
					String oid_acctno = dataBaseAccess.getAcctNo(user_no, "USEROWN_PSETTLE");
					String oid_acctno1 = dataBaseAccess.getAcctNo(user_no, "USEROWN_AVAILABLE");
					dataBaseAccessPayCore.updateAcctinfo(oid_acctno, 100000000);
					dataBaseAccessPayCore.updateAcctinfo(oid_acctno1, 100000000);
					break;
				case "USEROWN_AVAILABLE":
					String user_no1 = dataBaseAccess.getUserInfo(aesCryptService, user_id, oid_partner)[0];
					String oid_acctno2 = dataBaseAccess.getAcctNo(user_no1, "USEROWN_AVAILABLE");
					dataBaseAccessPayCore.updateAcctinfo(oid_acctno2, 100000000);
					break;
				case "MCHASSURE_PSETTLE":
					String oid_acctno3 = dataBaseAccess.getAcctNo(oid_partner, "MCHASSURE_PSETTLE");
					String oid_acctno4 = dataBaseAccess.getAcctNo(oid_partner, "MCHASSURE_AVAILABLE");
					dataBaseAccessPayCore.updateAcctinfo(oid_acctno3, 100000000);
					dataBaseAccessPayCore.updateAcctinfo(oid_acctno4, 100000000);
					break;
				case "MCHASSURE_AVAILABLE":
					String oid_acctno5 = dataBaseAccess.getAcctNo(oid_partner, "MCHASSURE_AVAILABLE");
					dataBaseAccessPayCore.updateAcctinfo(oid_acctno5, 100000000);
					break;
				default:

				}
			}
			Reporter.log("担保交易确认接口测试： " + datadriven.get("row_num") + datadriven.get("case_title"), true);
			Reporter.log("##############################【xxxx测试结束】############################", true);
		}
	}

	@Test(description = "担保交易确认接口测试", timeOut = 60000, dataProvider = "securedConfirm1")
	public void securedConfirm1(Map<String, String> datadriven) throws Exception {

		String tradeCreateUrl = Property.get("tradeCreate.url.test");
		String paymentBalanceUrl = Property.get("paymentBalance.url.test");
		String validationSmsUrl = Property.get("validationSms.url.test");
		String securedConfirmUrl = Property.get("securedConfirm.url.test");
		TradeCreate tradeCreate = new TradeCreate();
		OrderInfo orderInfo = new OrderInfo();
		String payeeInfo0 = datadriven.get("payeeInfo0");
		String payeeInfo1 = datadriven.get("payeeInfo1");
		String payeeInfo2 = datadriven.get("payeeInfo2");
		String payeeInfo3 = datadriven.get("payeeInfo3");
		String payeeInfo4 = datadriven.get("payeeInfo4");
		String payeeInfo5 = datadriven.get("payeeInfo5");
		String payeeInfo6 = datadriven.get("payeeInfo6");
		String payeeInfo7 = datadriven.get("payeeInfo7");
		String payeeInfo8 = datadriven.get("payeeInfo8");
		String payeeInfo9 = datadriven.get("payeeInfo9");
		List<PayeeInfo> payeeInfoList = new ArrayList<PayeeInfo>();

		try {
			// 创单
			Reporter.log("##############################【xxxx测试开始】############################", true);
			tradeCreate.setTimestamp(MyConfig.getTimestamp(datadriven.get("timestamp")));
			tradeCreate.setOid_partner(MyConfig.getOidPartner(datadriven.get("oid_partner")));
			tradeCreate.setTxn_type(datadriven.get("txn_type"));
			tradeCreate.setUser_id(MyConfig.getUser(datadriven.get("user_id")));
			tradeCreate.setUser_type(datadriven.get("user_type"));
			tradeCreate.setNotify_url(datadriven.get("notify_url"));
			tradeCreate.setReturn_url(datadriven.get("return_url"));
			tradeCreate.setPay_expire(datadriven.get("pay_expire"));

			orderInfo.setTxn_seqno(MyConfig.getTxnSeqno(datadriven.get("txn_seqno")));
			orderInfo.setTxn_time(MyConfig.getTxnTime(datadriven.get("txn_time")));
			orderInfo.setTotal_amount(datadriven.get("total_amount"));
			orderInfo.setOrder_info(datadriven.get("order_info"));
			orderInfo.setGoods_name(datadriven.get("goods_name"));
			orderInfo.setGoods_url(datadriven.get("goods_url"));
			tradeCreate.setOrderInfo(orderInfo);

			if (!StringUtils.isBlank(payeeInfo0)) {
				payeeInfoList.add(JSON.parseObject(payeeInfo0, PayeeInfo.class));
			}
			if (!StringUtils.isBlank(payeeInfo1)) {
				payeeInfoList.add(JSON.parseObject(payeeInfo1, PayeeInfo.class));
			}
			if (!StringUtils.isBlank(payeeInfo2)) {
				payeeInfoList.add(JSON.parseObject(payeeInfo2, PayeeInfo.class));
			}
			if (!StringUtils.isBlank(payeeInfo3)) {
				payeeInfoList.add(JSON.parseObject(payeeInfo3, PayeeInfo.class));
			}
			if (!StringUtils.isBlank(payeeInfo4)) {
				payeeInfoList.add(JSON.parseObject(payeeInfo4, PayeeInfo.class));
			}
			if (!StringUtils.isBlank(payeeInfo5)) {
				payeeInfoList.add(JSON.parseObject(payeeInfo5, PayeeInfo.class));
			}
			if (!StringUtils.isBlank(payeeInfo6)) {
				payeeInfoList.add(JSON.parseObject(payeeInfo6, PayeeInfo.class));
			}
			if (!StringUtils.isBlank(payeeInfo7)) {
				payeeInfoList.add(JSON.parseObject(payeeInfo7, PayeeInfo.class));
			}
			if (!StringUtils.isBlank(payeeInfo8)) {
				payeeInfoList.add(JSON.parseObject(payeeInfo8, PayeeInfo.class));
			}
			if (!StringUtils.isBlank(payeeInfo9)) {
				payeeInfoList.add(JSON.parseObject(payeeInfo9, PayeeInfo.class));
			}
			if (!payeeInfoList.isEmpty()) {
				tradeCreate.setPayeeInfo(payeeInfoList);
			}

			String reqJson = JSON.toJSONString(tradeCreate);
			Reporter.log("创单请求报文:" + reqJson, true);
			String sign = SignatureUtil.getInstance().sign(datadriven.get("private_key"), reqJson);
			HttpRequestSimple httpclient = new HttpRequestSimple();
			String[] res = httpclient.post(tradeCreateUrl, reqJson, Property.get("SIGNATURE_TYPE"), sign);
			String resJson = res[0];
			String resSignatureData = res[1];
			String txn_seqno = JSONObject.parseObject(resJson).getString("txn_seqno");
			Reporter.log("创单返回报文:" + resJson, true);

			// 余额支付
			PaymentBalance paymentBalance = new PaymentBalance();
			PayerInfo payerInfo = new PayerInfo();

			paymentBalance.setTimestamp(MyConfig.getTimestamp(datadriven.get("paymentBalance.timestamp")));
			paymentBalance.setOid_partner(MyConfig.getOidPartner(datadriven.get("paymentBalance.oid_partner")));
			if ("get".equals(datadriven.get("paymentBalance.txn_seqno"))) {
				paymentBalance.setTxn_seqno(txn_seqno);
			} else {
				paymentBalance.setTxn_seqno(datadriven.get("paymentBalance.txn_seqno"));
			}
			paymentBalance.setTotal_amount(datadriven.get("paymentBalance.total_amount"));
			paymentBalance.setCoupon_amount(datadriven.get("paymentBalance.coupon_amount"));
			paymentBalance.setRisk_item(datadriven.get("paymentBalance.risk_item"));

			payerInfo.setUser_id(MyConfig.getUser(datadriven.get("paymentBalance.user_id")));
			payerInfo.setPassword(datadriven.get("paymentBalance.password"));
			payerInfo.setRandom_key(datadriven.get("paymentBalance.random_key"));
			paymentBalance.setPayerInfo(payerInfo);

			String reqJson1 = JSON.toJSONString(paymentBalance);
			Reporter.log("余额支付请求报文:" + reqJson1, true);
			String sign1 = SignatureUtil.getInstance().sign(datadriven.get("paymentBalance.private_key"), reqJson1);
			String[] res1 = httpclient.post(paymentBalanceUrl, reqJson1, Property.get("SIGNATURE_TYPE"), sign1);
			String resJson1 = res1[0];
			String resSignatureData1 = res1[1];
			String token = JSONObject.parseObject(resJson1).getString("token");
			Reporter.log("余额支付返回报文:" + resJson1, true);
			// 验签
			assert SignatureUtil.getInstance().checksign(Property.get("YT_RSA_PUBLIC"), resJson1, resSignatureData1);

			// 余额支付验证
			if ("8888".equals(JSONObject.parseObject(resJson1).getString("ret_code"))) {
				ValidationSms validationSms = new ValidationSms();
				validationSms.setTimestamp(MyConfig.getTimestamp(datadriven.get("validationSms.timestamp")));
				validationSms.setOid_partner(MyConfig.getOidPartner(datadriven.get("validationSms.oid_partner")));
				validationSms.setPayer_type(datadriven.get("validationSms.payer_type"));
				validationSms.setPayer_id(datadriven.get("validationSms.payer_id"));
				if ("get".equals(datadriven.get("validationSms.txn_seqno"))) {
					validationSms.setTxn_seqno(txn_seqno);
				} else {
					validationSms.setTxn_seqno(datadriven.get("validationSms.txn_seqno"));
				}
				validationSms.setTotal_amount(datadriven.get("validationSms.total_amount"));
				if ("get".equals(datadriven.get("validationSms.token"))) {
					validationSms.setToken(token);
				} else {
					validationSms.setToken(datadriven.get("validationSms.token"));
				}
				if ("get".equals(datadriven.get("validationSms.verify_code"))) {
					String reg_phone = dataBaseAccess.getUserInfo(aesCryptService,
							MyConfig.getUser(datadriven.get("user_id")),
							MyConfig.getOidPartner(datadriven.get("oid_partner")))[1];
					String verify_code = dataBaseAccess.getSms(reg_phone);
					validationSms.setVerify_code(verify_code);
				} else {
					validationSms.setVerify_code(datadriven.get("validationSms.verify_code"));
				}
				String reqJson2 = JSON.toJSONString(validationSms);
				Reporter.log("二次验证请求报文:" + reqJson2, true);
				String sign2 = SignatureUtil.getInstance().sign(datadriven.get("validationSms.private_key"), reqJson2);
				String[] res2 = httpclient.post(validationSmsUrl, reqJson2, Property.get("SIGNATURE_TYPE"), sign2);
				String resJson2 = res2[0];
				String resSignatureData2 = res2[1];
				Reporter.log("二次验证返回报文:" + resJson2, true);
				// 验签
				assert SignatureUtil.getInstance().checksign(Property.get("YT_RSA_PUBLIC"), resJson2,
						resSignatureData2);

			}

			// 担保交易确认
			SecuredConfirm securedConfirm = new SecuredConfirm();
			OriginalOrderInfo originalOrderInfo = new OriginalOrderInfo();
			ConfirmOrderInfo confirmOrderInfo = new ConfirmOrderInfo();
			String payeeInfo10 = datadriven.get("securedConfirm.payeeInfo0");
			String payeeInfo11 = datadriven.get("securedConfirm.payeeInfo1");
			String payeeInfo12 = datadriven.get("securedConfirm.payeeInfo2");
			String payeeInfo13 = datadriven.get("securedConfirm.payeeInfo3");
			String payeeInfo14 = datadriven.get("securedConfirm.payeeInfo4");
			String payeeInfo15 = datadriven.get("securedConfirm.payeeInfo5");
			String payeeInfo16 = datadriven.get("securedConfirm.payeeInfo6");
			String payeeInfo17 = datadriven.get("securedConfirm.payeeInfo7");
			String payeeInfo18 = datadriven.get("securedConfirm.payeeInfo8");
			String payeeInfo19 = datadriven.get("securedConfirm.payeeInfo9");
			List<PayeeInfo> payeeInfoList1 = new ArrayList<PayeeInfo>();

			securedConfirm.setTimestamp(MyConfig.getTimestamp(datadriven.get("securedConfirm.timestamp")));
			securedConfirm.setOid_partner(MyConfig.getOidPartner(datadriven.get("securedConfirm.oid_partner")));
			securedConfirm.setUser_id(MyConfig.getUser(datadriven.get("securedConfirm.user_id")));
			securedConfirm.setNotify_url(datadriven.get("securedConfirm.notify_url"));
			securedConfirm.setConfirm_mode(datadriven.get("securedConfirm.confirm_mode"));

			if ("get".equals(datadriven.get("securedConfirm.txn_seqno"))) {
				originalOrderInfo.setTxn_seqno(txn_seqno);
			} else {
				originalOrderInfo.setTxn_seqno(datadriven.get("securedConfirm.txn_seqno"));
			}
			originalOrderInfo.setTotal_amount(datadriven.get("securedConfirm.total_amount"));
			securedConfirm.setOriginalOrderInfo(originalOrderInfo);

			confirmOrderInfo.setConfirm_seqno(MyConfig.getConfirmSeqno(datadriven.get("securedConfirm.confirm_seqno")));
			confirmOrderInfo.setConfirm_time(MyConfig.getConfirmTime(datadriven.get("securedConfirm.confirm_time")));
			confirmOrderInfo.setConfirm_amount(datadriven.get("securedConfirm.confirm_amount"));
			securedConfirm.setConfirmOrderInfo(confirmOrderInfo);

			if (!StringUtils.isBlank(payeeInfo10)) {
				payeeInfoList1.add(JSON.parseObject(payeeInfo10, PayeeInfo.class));
			}
			if (!StringUtils.isBlank(payeeInfo11)) {
				payeeInfoList1.add(JSON.parseObject(payeeInfo11, PayeeInfo.class));
			}
			if (!StringUtils.isBlank(payeeInfo12)) {
				payeeInfoList1.add(JSON.parseObject(payeeInfo12, PayeeInfo.class));
			}
			if (!StringUtils.isBlank(payeeInfo13)) {
				payeeInfoList1.add(JSON.parseObject(payeeInfo13, PayeeInfo.class));
			}
			if (!StringUtils.isBlank(payeeInfo14)) {
				payeeInfoList1.add(JSON.parseObject(payeeInfo14, PayeeInfo.class));
			}
			if (!StringUtils.isBlank(payeeInfo15)) {
				payeeInfoList1.add(JSON.parseObject(payeeInfo15, PayeeInfo.class));
			}
			if (!StringUtils.isBlank(payeeInfo16)) {
				payeeInfoList1.add(JSON.parseObject(payeeInfo16, PayeeInfo.class));
			}
			if (!StringUtils.isBlank(payeeInfo17)) {
				payeeInfoList1.add(JSON.parseObject(payeeInfo17, PayeeInfo.class));
			}
			if (!StringUtils.isBlank(payeeInfo18)) {
				payeeInfoList1.add(JSON.parseObject(payeeInfo18, PayeeInfo.class));
			}
			if (!StringUtils.isBlank(payeeInfo19)) {
				payeeInfoList1.add(JSON.parseObject(payeeInfo19, PayeeInfo.class));
			}
			if (!payeeInfoList1.isEmpty()) {
				securedConfirm.setPayeeInfo(payeeInfoList1);
			}

			String reqJson3 = JSON.toJSONString(securedConfirm);
			Reporter.log("担保交易确认请求报文:" + reqJson3, true);
			String sign3 = SignatureUtil.getInstance().sign(datadriven.get("securedConfirm.private_key"), reqJson3);
			String[] res3 = httpclient.post(securedConfirmUrl, reqJson3, Property.get("SIGNATURE_TYPE"), sign3);
			String resJson3 = res3[0];
			String resSignatureData3 = res3[1];
			Reporter.log("担保交易确认返回报文:" + resJson3, true);

			// 二次确认
			SecuredConfirm securedConfirm1 = new SecuredConfirm();
			OriginalOrderInfo originalOrderInfo1 = new OriginalOrderInfo();
			ConfirmOrderInfo confirmOrderInfo1 = new ConfirmOrderInfo();
			String payeeInfo20 = datadriven.get("securedConfirm1.payeeInfo0");
			String payeeInfo21 = datadriven.get("securedConfirm1.payeeInfo1");
			String payeeInfo22 = datadriven.get("securedConfirm1.payeeInfo2");
			String payeeInfo23 = datadriven.get("securedConfirm1.payeeInfo3");
			String payeeInfo24 = datadriven.get("securedConfirm1.payeeInfo4");
			String payeeInfo25 = datadriven.get("securedConfirm1.payeeInfo5");
			String payeeInfo26 = datadriven.get("securedConfirm1.payeeInfo6");
			String payeeInfo27 = datadriven.get("securedConfirm1.payeeInfo7");
			String payeeInfo28 = datadriven.get("securedConfirm1.payeeInfo8");
			String payeeInfo29 = datadriven.get("securedConfirm1.payeeInfo9");
			List<PayeeInfo> payeeInfoList2 = new ArrayList<PayeeInfo>();

			securedConfirm1.setTimestamp(MyConfig.getTimestamp(datadriven.get("securedConfirm1.timestamp")));
			securedConfirm1.setOid_partner(MyConfig.getOidPartner(datadriven.get("securedConfirm1.oid_partner")));
			securedConfirm1.setUser_id(MyConfig.getUser(datadriven.get("securedConfirm1.user_id")));
			securedConfirm1.setNotify_url(datadriven.get("securedConfirm1.notify_url"));
			securedConfirm1.setConfirm_mode(datadriven.get("securedConfirm1.confirm_mode"));

			if ("get".equals(datadriven.get("securedConfirm1.txn_seqno"))) {
				originalOrderInfo1.setTxn_seqno(txn_seqno);
			} else {
				originalOrderInfo1.setTxn_seqno(datadriven.get("securedConfirm1.txn_seqno"));
			}
			originalOrderInfo1.setTotal_amount(datadriven.get("securedConfirm1.total_amount"));
			securedConfirm1.setOriginalOrderInfo(originalOrderInfo1);

			confirmOrderInfo1
					.setConfirm_seqno(MyConfig.getConfirmSeqno(datadriven.get("securedConfirm1.confirm_seqno")));
			confirmOrderInfo1.setConfirm_time(MyConfig.getConfirmTime(datadriven.get("securedConfirm1.confirm_time")));
			confirmOrderInfo1.setConfirm_amount(datadriven.get("securedConfirm1.confirm_amount"));
			securedConfirm1.setConfirmOrderInfo(confirmOrderInfo1);

			if (!StringUtils.isBlank(payeeInfo20)) {
				payeeInfoList2.add(JSON.parseObject(payeeInfo20, PayeeInfo.class));
			}
			if (!StringUtils.isBlank(payeeInfo21)) {
				payeeInfoList2.add(JSON.parseObject(payeeInfo21, PayeeInfo.class));
			}
			if (!StringUtils.isBlank(payeeInfo22)) {
				payeeInfoList2.add(JSON.parseObject(payeeInfo22, PayeeInfo.class));
			}
			if (!StringUtils.isBlank(payeeInfo23)) {
				payeeInfoList2.add(JSON.parseObject(payeeInfo23, PayeeInfo.class));
			}
			if (!StringUtils.isBlank(payeeInfo24)) {
				payeeInfoList2.add(JSON.parseObject(payeeInfo24, PayeeInfo.class));
			}
			if (!StringUtils.isBlank(payeeInfo25)) {
				payeeInfoList2.add(JSON.parseObject(payeeInfo25, PayeeInfo.class));
			}
			if (!StringUtils.isBlank(payeeInfo26)) {
				payeeInfoList2.add(JSON.parseObject(payeeInfo26, PayeeInfo.class));
			}
			if (!StringUtils.isBlank(payeeInfo27)) {
				payeeInfoList2.add(JSON.parseObject(payeeInfo27, PayeeInfo.class));
			}
			if (!StringUtils.isBlank(payeeInfo28)) {
				payeeInfoList2.add(JSON.parseObject(payeeInfo28, PayeeInfo.class));
			}
			if (!StringUtils.isBlank(payeeInfo29)) {
				payeeInfoList2.add(JSON.parseObject(payeeInfo29, PayeeInfo.class));
			}
			if (!payeeInfoList2.isEmpty()) {
				securedConfirm1.setPayeeInfo(payeeInfoList2);
			}

			String reqJson4 = JSON.toJSONString(securedConfirm1);
			Reporter.log("担保交易确认请求报文:" + reqJson4, true);
			String sign4 = SignatureUtil.getInstance().sign(datadriven.get("securedConfirm1.private_key"), reqJson4);
			String[] res4 = httpclient.post(securedConfirmUrl, reqJson4, Property.get("SIGNATURE_TYPE"), sign4);
			String resJson4 = res4[0];
			String resSignatureData4 = res4[1];
			Reporter.log("担保交易确认返回报文:" + resJson4, true);

			// 二次确认请求返回检查
			assert resJson4.contains(datadriven.get("expect_retcode"));
			if ("0000".equals(JSONObject.parseObject(resJson4).getString("ret_code"))) {
				assert SignatureUtil.getInstance().checksign(Property.get("YT_RSA_PUBLIC"), resJson4,
						resSignatureData4);
			}

			// 请求&响应写入文件
			SampleFileUtils.appendLine("D://TA//accplog//securedConfirm.txt", MyDate.getStringDate());
			SampleFileUtils.appendLine("D://TA//accplog//securedConfirm.txt", reqJson);
			SampleFileUtils.appendLine("D://TA//accplog//securedConfirm.txt", resJson);
			SampleFileUtils.appendLine("D://TA//accplog//securedConfirm.txt", reqJson1);
			SampleFileUtils.appendLine("D://TA//accplog//securedConfirm.txt", resJson1);
			SampleFileUtils.appendLine("D://TA//accplog//securedConfirm.txt", reqJson3);
			SampleFileUtils.appendLine("D://TA//accplog//securedConfirm.txt", resJson3);
			SampleFileUtils.appendLine("D://TA//accplog//securedConfirm.txt", reqJson4);
			SampleFileUtils.appendLine("D://TA//accplog//securedConfirm.txt", resJson4);
			SampleFileUtils.appendLine("D://TA//accplog//securedConfirm.txt",
					"===============================================");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			Reporter.log("担保交易确认接口测试： " + datadriven.get("row_num") + datadriven.get("case_title"), true);
			Reporter.log("##############################【xxxx测试结束】############################", true);
		}
	}
	
	@Test(description = "担保交易确认接口测试", timeOut = 60000, dataProvider = "securedConfirm2")
//	@Test(description = "担保交易确认接口测试", threadPoolSize = 2, invocationCount = 2, timeOut = 60000, dataProvider = "securedConfirm2")
	public void securedConfirm2(Map<String, String> datadriven) throws Exception {

		String securedConfirmUrl = Property.get("securedConfirm.url.test");
		String oid_partner = MyConfig.getOidPartner(datadriven.get("securedConfirm.oid_partner"));
		String user_id = MyConfig.getUser(datadriven.get("securedConfirm.user_id"));

		try {

			// 担保交易确认
			SecuredConfirm securedConfirm = new SecuredConfirm();
			OriginalOrderInfo originalOrderInfo = new OriginalOrderInfo();
			ConfirmOrderInfo confirmOrderInfo = new ConfirmOrderInfo();
			String payeeInfo10 = datadriven.get("securedConfirm.payeeInfo0");
			String payeeInfo11 = datadriven.get("securedConfirm.payeeInfo1");
			String payeeInfo12 = datadriven.get("securedConfirm.payeeInfo2");
			String payeeInfo13 = datadriven.get("securedConfirm.payeeInfo3");
			String payeeInfo14 = datadriven.get("securedConfirm.payeeInfo4");
			String payeeInfo15 = datadriven.get("securedConfirm.payeeInfo5");
			String payeeInfo16 = datadriven.get("securedConfirm.payeeInfo6");
			String payeeInfo17 = datadriven.get("securedConfirm.payeeInfo7");
			String payeeInfo18 = datadriven.get("securedConfirm.payeeInfo8");
			String payeeInfo19 = datadriven.get("securedConfirm.payeeInfo9");
			List<PayeeInfo> payeeInfoList1 = new ArrayList<PayeeInfo>();

			securedConfirm.setTimestamp(MyConfig.getTimestamp(datadriven.get("securedConfirm.timestamp")));
			securedConfirm.setOid_partner(oid_partner);
			securedConfirm.setUser_id(user_id);
			securedConfirm.setNotify_url(datadriven.get("securedConfirm.notify_url"));
			securedConfirm.setConfirm_mode(datadriven.get("securedConfirm.confirm_mode"));

			originalOrderInfo.setTxn_seqno(datadriven.get("securedConfirm.txn_seqno"));
			originalOrderInfo.setTotal_amount(datadriven.get("securedConfirm.total_amount"));
			securedConfirm.setOriginalOrderInfo(originalOrderInfo);
			String confirm_seqno = UUID.randomUUID().toString();
			confirmOrderInfo.setConfirm_seqno(confirm_seqno);
			confirmOrderInfo.setConfirm_time(MyConfig.getConfirmTime(datadriven.get("securedConfirm.confirm_time")));
			confirmOrderInfo.setConfirm_amount(datadriven.get("securedConfirm.confirm_amount"));
			securedConfirm.setConfirmOrderInfo(confirmOrderInfo);

			if (!StringUtils.isBlank(payeeInfo10)) {
				payeeInfoList1.add(JSON.parseObject(payeeInfo10, PayeeInfo.class));
			}
			if (!StringUtils.isBlank(payeeInfo11)) {
				payeeInfoList1.add(JSON.parseObject(payeeInfo11, PayeeInfo.class));
			}
			if (!StringUtils.isBlank(payeeInfo12)) {
				payeeInfoList1.add(JSON.parseObject(payeeInfo12, PayeeInfo.class));
			}
			if (!StringUtils.isBlank(payeeInfo13)) {
				payeeInfoList1.add(JSON.parseObject(payeeInfo13, PayeeInfo.class));
			}
			if (!StringUtils.isBlank(payeeInfo14)) {
				payeeInfoList1.add(JSON.parseObject(payeeInfo14, PayeeInfo.class));
			}
			if (!StringUtils.isBlank(payeeInfo15)) {
				payeeInfoList1.add(JSON.parseObject(payeeInfo15, PayeeInfo.class));
			}
			if (!StringUtils.isBlank(payeeInfo16)) {
				payeeInfoList1.add(JSON.parseObject(payeeInfo16, PayeeInfo.class));
			}
			if (!StringUtils.isBlank(payeeInfo17)) {
				payeeInfoList1.add(JSON.parseObject(payeeInfo17, PayeeInfo.class));
			}
			if (!StringUtils.isBlank(payeeInfo18)) {
				payeeInfoList1.add(JSON.parseObject(payeeInfo18, PayeeInfo.class));
			}
			if (!StringUtils.isBlank(payeeInfo19)) {
				payeeInfoList1.add(JSON.parseObject(payeeInfo19, PayeeInfo.class));
			}
			if (!payeeInfoList1.isEmpty()) {
				securedConfirm.setPayeeInfo(payeeInfoList1);
			}

			String reqJson3 = JSON.toJSONString(securedConfirm);
			Reporter.log("担保交易确认请求报文:" + reqJson3, true);
			String sign3 = SignatureUtil.getInstance().sign(datadriven.get("securedConfirm.private_key"), reqJson3);
			HttpRequestSimple httpclient = new HttpRequestSimple();
			String[] res3 = httpclient.post(securedConfirmUrl, reqJson3, Property.get("SIGNATURE_TYPE"), sign3);
			String resJson3 = res3[0];
			String resSignatureData3 = res3[1];
			Reporter.log("担保交易确认返回报文:" + resJson3, true);

			// 担保交易确认请求返回检查
			assert resJson3.contains(datadriven.get("expect_retcode"));
			if ("0000".equals(JSONObject.parseObject(resJson3).getString("ret_code"))) {
				assert SignatureUtil.getInstance().checksign(Property.get("YT_RSA_PUBLIC"), resJson3,
						resSignatureData3);
			}

			// 请求&响应写入文件
			SampleFileUtils.appendLine("D://TA//accplog//securedConfirm.txt", MyDate.getStringDate());
			SampleFileUtils.appendLine("D://TA//accplog//securedConfirm.txt", reqJson3);
			SampleFileUtils.appendLine("D://TA//accplog//securedConfirm.txt", resJson3);
			SampleFileUtils.appendLine("D://TA//accplog//securedConfirm.txt",
					"===============================================");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			Reporter.log("担保交易确认接口测试： " + datadriven.get("row_num") + datadriven.get("case_title"), true);
			Reporter.log("##############################【xxxx测试结束】############################", true);
		}
	}

	@DataProvider(name = "securedConfirm")
	public Iterator<Object[]> data4securedConfirm() throws IOException {
		return new ExcelProvider(this, "securedConfirm");
	}

	@DataProvider(name = "securedConfirm1")
	public Iterator<Object[]> data4securedConfirm1() throws IOException {
		return new ExcelProvider(this, "securedConfirm1");
	}

	@DataProvider(name = "securedConfirm2")
	public Iterator<Object[]> data4securedConfirm2() throws IOException {
		return new ExcelProvider(this, "securedConfirm2", 61);
	}

	@AfterClass
	public void afterClass() {
		dataBaseAccess.closeDBConnection();
	}

}