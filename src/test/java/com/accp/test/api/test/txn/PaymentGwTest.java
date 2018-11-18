package com.accp.test.api.test.txn;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.accp.test.bean.txn.OrderInfo;
import com.accp.test.bean.txn.PayMethods;
import com.accp.test.bean.txn.PayeeInfo;
import com.accp.test.bean.txn.PayerInfo;
import com.accp.test.bean.txn.PaymentGw;
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
 * 网关支付接口测试
 */

@ContextConfiguration(locations = { "/consumer.xml" })
public class PaymentGwTest extends AbstractTestNGSpringContextTests {

	DataBaseAccess dataBaseAccess = new DataBaseAccess();
	DataBaseAccessPayCore dataBaseAccessPayCore = new DataBaseAccessPayCore();

	@Autowired
	private IAESCryptService aesCryptService;
	
//	@Autowired
//	private PaymentResultNotifyService paymentResultNotifyService;
	
	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "网关支付接口测试", timeOut = 60000, dataProvider = "paymentGw")
	public void paymentGw(Map<String, String> datadriven) throws Exception {

		String tradeCreateUrl = Property.get("tradeCreate.url.test");
		String paymentGwUrl = Property.get("paymentGw.url.test");
		String validationSmsUrl = Property.get("validationSms.url.test");
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
		String oid_partner = MyConfig.getOidPartner(datadriven.get("paymentGw.oid_partner"));
		String user_id = MyConfig.getUser(datadriven.get("user_id"));
		String productUpdate = datadriven.get("productUpdate");
		String ipUpdate = datadriven.get("ipUpdate");
		String traderStatUpdate = datadriven.get("traderStatUpdate");
		String userStatUpdate = datadriven.get("userStatUpdate");
		String traderLimitParamUpdate = datadriven.get("traderLimitParamUpdate");
		String traderVerifyParamUpdate = datadriven.get("traderVerifyParamUpdate");
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

			// 网关支付
			PaymentGw paymentGw = new PaymentGw();
			PayerInfo payerInfo = new PayerInfo();
			List<PayMethods> payMethods = new ArrayList<PayMethods>();
			String payMethods0 = datadriven.get("paymentGw.payMethods0");
			String payMethods1 = datadriven.get("paymentGw.payMethods1");
			String payMethods2 = datadriven.get("paymentGw.payMethods2");
			String payMethods3 = datadriven.get("paymentGw.payMethods3");
			
			paymentGw.setTimestamp(MyConfig.getTimestamp(datadriven.get("paymentGw.timestamp")));
			paymentGw.setOid_partner(oid_partner);
			if ("get".equals(datadriven.get("paymentGw.txn_seqno"))) {
				paymentGw.setTxn_seqno(txn_seqno);
			} else {
				paymentGw.setTxn_seqno(datadriven.get("paymentGw.txn_seqno"));
			}
			paymentGw.setTotal_amount(datadriven.get("paymentGw.total_amount"));
			paymentGw.setRisk_item(datadriven.get("paymentGw.risk_item"));
			paymentGw.setAppid(datadriven.get("paymentGw.appid"));
			paymentGw.setOpenid(datadriven.get("paymentGw.openid"));
			paymentGw.setBankcode(datadriven.get("paymentGw.bankcode"));
			paymentGw.setDevice_info(datadriven.get("paymentGw.device_info"));
			paymentGw.setClient_ip(datadriven.get("paymentGw.client_ip"));

			payerInfo.setPayer_accttype(datadriven.get("paymentGw.payer_accttype"));
			payerInfo.setPayer_id(datadriven.get("paymentGw.payer_id"));
			payerInfo.setPassword(datadriven.get("paymentGw.password"));
			payerInfo.setRandom_key(datadriven.get("paymentGw.random_key"));
			paymentGw.setPayerInfo(payerInfo);
			
			if (!StringUtils.isBlank(payMethods0)) {
				payMethods.add(JSON.parseObject(payMethods0, PayMethods.class));
			}
			if (!StringUtils.isBlank(payMethods1)) {
				payMethods.add(JSON.parseObject(payMethods1, PayMethods.class));
			}
			if (!StringUtils.isBlank(payMethods2)) {
				payMethods.add(JSON.parseObject(payMethods2, PayMethods.class));
			}
			if (!StringUtils.isBlank(payMethods3)) {
				payMethods.add(JSON.parseObject(payMethods3, PayMethods.class));
			}
			paymentGw.setPayMethods(payMethods);
			
			if (!StringUtils.isBlank(productUpdate)) {
				dataBaseAccess.deleteTraderProduct(productUpdate, oid_partner);
			}
			if (!StringUtils.isBlank(ipUpdate)){
				dataBaseAccess.updateIpRequest(ipUpdate, oid_partner);
			}
			if (!StringUtils.isBlank(traderStatUpdate)){
				dataBaseAccess.updateTraderEnableState(traderStatUpdate, oid_partner);
			}
			if (!StringUtils.isBlank(userStatUpdate)){
				dataBaseAccess.updateUserState1(userStatUpdate, datadriven.get("paymentGw.payer_id"), oid_partner);
			}
			if (!StringUtils.isBlank(traderLimitParamUpdate)) {
				String result[] = traderLimitParamUpdate.split(",");
				dataBaseAccess.updateTraderLimitParam(oid_partner, result[0], result[1], result[2], result[3]);
			}
			if (!StringUtils.isBlank(traderVerifyParamUpdate)) {
				String result1[] = traderVerifyParamUpdate.split(",");
				dataBaseAccess.updateTraderVerifyParam(oid_partner, result1[0], result1[1], result1[2], result1[3]);
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
				case "MCHCOUPON_PSETTLE":
					String oid_acctno3 = dataBaseAccess.getAcctNo(oid_partner, "MCHCOUPON_PSETTLE");
					String oid_acctno4 = dataBaseAccess.getAcctNo(oid_partner, "MCHCOUPON_AVAILABLE");
					dataBaseAccessPayCore.updateAcctinfo(oid_acctno3, 10);
					dataBaseAccessPayCore.updateAcctinfo(oid_acctno4, 10);
					break;
				case "MCHCOUPON_AVAILABLE":
					String oid_acctno5 = dataBaseAccess.getAcctNo(oid_partner, "MCHCOUPON_AVAILABLE");
					dataBaseAccessPayCore.updateAcctinfo(oid_acctno5, 10);
					break;
				default:

				}
			}
			
			String reqJson1 = JSON.toJSONString(paymentGw);
			Reporter.log("网关支付请求报文:" + reqJson1, true);
			String sign1 = SignatureUtil.getInstance().sign(datadriven.get("paymentGw.private_key"), reqJson1);
			String[] res1 = httpclient.post(paymentGwUrl, reqJson1, Property.get("SIGNATURE_TYPE"), sign1);
			String resJson1 = res1[0];
			String resSignatureData1 = res1[1];
			String token = JSONObject.parseObject(resJson1).getString("token");
			Reporter.log("网关支付返回报文:" + resJson1, true);
			// 验签
			assert SignatureUtil.getInstance().checksign(Property.get("YT_RSA_PUBLIC"), resJson1, resSignatureData1);

//			// 网银异步回调处理成功
//			if (("0000".equals(JSONObject.parseObject(resJson1).getString("ret_code"))) {
//				String oid_rungroupid = null;
//				oid_rungroupid = dbUtils.queryPayBillRunGroupId(txn_seqno);
//				String amount = new BigDecimal(datadriven.get("paymentGw.amount")).multiply(new BigDecimal(100)).toBigInteger().toString();
//				System.out.println("amount：" + amount);
//				PaymentProcessReq paymentprocessreq = new PaymentProcessReq();
//				paymentprocessreq.setStatus("00");
//				paymentprocessreq.setPayRungroupid(oid_rungroupid);
//				paymentprocessreq.setSettleDate("20180503");
//				paymentprocessreq.setBankSerial(txn_seqno);
//				paymentprocessreq.setPayAmount(amount);
//				System.out.println("paymentprocessreq： " + JSON.toJSONString(paymentprocessreq));
//				String rsp4 = JSON.toJSONString(paymentResultNotifyService.processAfterNotify(paymentprocessreq));
//				System.out.println("异步回调返回：" + rsp4);
//			}
			
			// 网关支付验证
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
					String linked_phone = datadriven.get("paymentGw.linked_phone");
					String verify_code = dataBaseAccess.getSms(linked_phone);
					validationSms.setToken(verify_code);
				} else {
					validationSms.setToken(datadriven.get("validationSms.verify_code"));
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

			// 请求&响应写入文件
			SampleFileUtils.appendLine("D://TA//accplog//paymentGw.txt", MyDate.getStringDate());
			SampleFileUtils.appendLine("D://TA//accplog//paymentGw.txt", reqJson);
			SampleFileUtils.appendLine("D://TA//accplog//paymentGw.txt", resJson);
			SampleFileUtils.appendLine("D://TA//accplog//paymentGw.txt", reqJson1);
			SampleFileUtils.appendLine("D://TA//accplog//paymentGw.txt", resJson1);
			SampleFileUtils.appendLine("D://TA//accplog//paymentGw.txt",
					"===============================================");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			if (!StringUtils.isBlank(productUpdate)) {
				dataBaseAccess.insertTraderProduct(productUpdate, oid_partner);
			}
			if (!StringUtils.isBlank(ipUpdate)) {
				dataBaseAccess.updateIpRequest("*.*.*.*", oid_partner);
			}
			if (!StringUtils.isBlank(traderStatUpdate)){
				dataBaseAccess.updateTraderEnableState("0", oid_partner);
			}
			if (!StringUtils.isBlank(userStatUpdate)){
				dataBaseAccess.updateUserState1("NORMAL", datadriven.get("paymentGw.payer_id"), oid_partner);
			}
			if (!StringUtils.isBlank(traderLimitParamUpdate)) {
				String result[] = traderLimitParamUpdate.split(",");
				dataBaseAccess.updateTraderLimitParam(oid_partner, result[0], result[1], result[2], "1000000");
			}
			if (!StringUtils.isBlank(traderVerifyParamUpdate)) {
				String result1[] = traderVerifyParamUpdate.split(",");
				dataBaseAccess.updateTraderVerifyParam(oid_partner, result1[0], result1[1], "1000000", "1000000");
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
				case "MCHCOUPON_PSETTLE":
					String oid_acctno3 = dataBaseAccess.getAcctNo(oid_partner, "MCHCOUPON_PSETTLE");
					String oid_acctno4 = dataBaseAccess.getAcctNo(oid_partner, "MCHCOUPON_AVAILABLE");
					dataBaseAccessPayCore.updateAcctinfo(oid_acctno3, 100000000);
					dataBaseAccessPayCore.updateAcctinfo(oid_acctno4, 100000000);
					break;
				case "MCHCOUPON_AVAILABLE":
					String oid_acctno5 = dataBaseAccess.getAcctNo(oid_partner, "MCHCOUPON_AVAILABLE");
					dataBaseAccessPayCore.updateAcctinfo(oid_acctno5, 100000000);
					break;
				default:

				}
			}
			Reporter.log("##############################【xxxx测试结束】############################", true);
		}
	}

	@Test(description = "网关支付接口测试", timeOut = 60000, dataProvider = "paymentGw1")
	public void paymentGw1(Map<String, String> datadriven) throws Exception {

		String tradeCreateUrl = Property.get("tradeCreate.url.test");
		String paymentGwUrl = Property.get("paymentGw.url.test");
		String validationSmsUrl = Property.get("validationSms.url.test");
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
		String oid_partner = MyConfig.getOidPartner(datadriven.get("paymentGw.oid_partner"));
		String user_id = MyConfig.getUser(datadriven.get("user_id"));
		String productUpdate = datadriven.get("productUpdate");
		String ipUpdate = datadriven.get("ipUpdate");
		String traderStatUpdate = datadriven.get("traderStatUpdate");
		String userStatUpdate = datadriven.get("userStatUpdate");
		String traderLimitParamUpdate = datadriven.get("traderLimitParamUpdate");
		String traderVerifyParamUpdate = datadriven.get("traderVerifyParamUpdate");
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

			// 网关支付
			PaymentGw paymentGw = new PaymentGw();
			PayerInfo payerInfo = new PayerInfo();
			List<PayMethods> payMethods = new ArrayList<PayMethods>();
			String payMethods0 = datadriven.get("paymentGw.payMethods0");
			String payMethods1 = datadriven.get("paymentGw.payMethods1");
			String payMethods2 = datadriven.get("paymentGw.payMethods2");
			String payMethods3 = datadriven.get("paymentGw.payMethods3");
			
			paymentGw.setTimestamp(MyConfig.getTimestamp(datadriven.get("paymentGw.timestamp")));
			paymentGw.setOid_partner(oid_partner);
			if ("get".equals(datadriven.get("paymentGw.txn_seqno"))) {
				paymentGw.setTxn_seqno(txn_seqno);
			} else {
				paymentGw.setTxn_seqno(datadriven.get("paymentGw.txn_seqno"));
			}
			paymentGw.setTotal_amount(datadriven.get("paymentGw.total_amount"));
			paymentGw.setRisk_item(datadriven.get("paymentGw.risk_item"));
			paymentGw.setAppid(datadriven.get("paymentGw.appid"));
			paymentGw.setOpenid(datadriven.get("paymentGw.openid"));
			paymentGw.setBankcode(datadriven.get("paymentGw.bankcode"));
			paymentGw.setDevice_info(datadriven.get("paymentGw.device_info"));
			paymentGw.setClient_ip(datadriven.get("paymentGw.client_ip"));

			payerInfo.setPayer_accttype(datadriven.get("paymentGw.payer_accttype"));
			payerInfo.setPayer_id(datadriven.get("paymentGw.payer_id"));
			payerInfo.setPassword(datadriven.get("paymentGw.password"));
			payerInfo.setRandom_key(datadriven.get("paymentGw.random_key"));
			paymentGw.setPayerInfo(payerInfo);
			
			if (!StringUtils.isBlank(payMethods0)) {
				payMethods.add(JSON.parseObject(payMethods0, PayMethods.class));
			}
			if (!StringUtils.isBlank(payMethods1)) {
				payMethods.add(JSON.parseObject(payMethods1, PayMethods.class));
			}
			if (!StringUtils.isBlank(payMethods2)) {
				payMethods.add(JSON.parseObject(payMethods2, PayMethods.class));
			}
			if (!StringUtils.isBlank(payMethods3)) {
				payMethods.add(JSON.parseObject(payMethods3, PayMethods.class));
			}
			paymentGw.setPayMethods(payMethods);
			
			if (!StringUtils.isBlank(productUpdate)) {
				dataBaseAccess.deleteTraderProduct(productUpdate, oid_partner);
			}
			if (!StringUtils.isBlank(ipUpdate)){
				dataBaseAccess.updateIpRequest(ipUpdate, oid_partner);
			}
			if (!StringUtils.isBlank(traderStatUpdate)){
				dataBaseAccess.updateTraderEnableState(traderStatUpdate, oid_partner);
			}
			if (!StringUtils.isBlank(userStatUpdate)){
				dataBaseAccess.updateUserState1(userStatUpdate, datadriven.get("paymentGw.payer_id"), oid_partner);
			}
			if (!StringUtils.isBlank(traderLimitParamUpdate)) {
				String result[] = traderLimitParamUpdate.split(",");
				dataBaseAccess.updateTraderLimitParam(oid_partner, result[0], result[1], result[2], result[3]);
			}
			if (!StringUtils.isBlank(traderVerifyParamUpdate)) {
				String result1[] = traderVerifyParamUpdate.split(",");
				dataBaseAccess.updateTraderVerifyParam(oid_partner, result1[0], result1[1], result1[2], result1[3]);
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
				case "MCHCOUPON_PSETTLE":
					String oid_acctno3 = dataBaseAccess.getAcctNo(oid_partner, "MCHCOUPON_PSETTLE");
					String oid_acctno4 = dataBaseAccess.getAcctNo(oid_partner, "MCHCOUPON_AVAILABLE");
					dataBaseAccessPayCore.updateAcctinfo(oid_acctno3, 10);
					dataBaseAccessPayCore.updateAcctinfo(oid_acctno4, 10);
					break;
				case "MCHCOUPON_AVAILABLE":
					String oid_acctno5 = dataBaseAccess.getAcctNo(oid_partner, "MCHCOUPON_AVAILABLE");
					dataBaseAccessPayCore.updateAcctinfo(oid_acctno5, 10);
					break;
				default:

				}
			}
			
			String reqJson1 = JSON.toJSONString(paymentGw);
			Reporter.log("网关支付请求报文:" + reqJson1, true);
			String sign1 = SignatureUtil.getInstance().sign(datadriven.get("paymentGw.private_key"), reqJson1);
			String[] res1 = httpclient.post(paymentGwUrl, reqJson1, Property.get("SIGNATURE_TYPE"), sign1);
			String resJson1 = res1[0];
			String resSignatureData1 = res1[1];
			String token = JSONObject.parseObject(resJson1).getString("token");
			Reporter.log("网关支付返回报文:" + resJson1, true);
			// 验签
			assert SignatureUtil.getInstance().checksign(Property.get("YT_RSA_PUBLIC"), resJson1, resSignatureData1);

			// 二次重复支付
			List<PayMethods> payMethodss = new ArrayList<PayMethods>();
			String payMethods10 = datadriven.get("paymentGw1.payMethods0");
			String payMethods11 = datadriven.get("paymentGw1.payMethods1");
			String payMethods12 = datadriven.get("paymentGw1.payMethods2");
			String payMethods13 = datadriven.get("paymentGw1.payMethods3");
			
			paymentGw.setTimestamp(MyConfig.getTimestamp(datadriven.get("paymentGw1.timestamp")));
			paymentGw.setOid_partner(oid_partner);
			if ("get".equals(datadriven.get("paymentGw1.txn_seqno"))) {
				paymentGw.setTxn_seqno(txn_seqno);
			} else {
				paymentGw.setTxn_seqno(datadriven.get("paymentGw1.txn_seqno"));
			}
			paymentGw.setTotal_amount(datadriven.get("paymentGw1.total_amount"));
			paymentGw.setRisk_item(datadriven.get("paymentGw1.risk_item"));
			paymentGw.setAppid(datadriven.get("paymentGw1.appid"));
			paymentGw.setOpenid(datadriven.get("paymentGw1.openid"));
			paymentGw.setBankcode(datadriven.get("paymentGw1.bankcode"));
			paymentGw.setDevice_info(datadriven.get("paymentGw1.device_info"));
			paymentGw.setClient_ip(datadriven.get("paymentGw1.client_ip"));

			payerInfo.setPayer_accttype(datadriven.get("paymentGw1.payer_accttype"));
			payerInfo.setPayer_id(datadriven.get("paymentGw1.payer_id"));
			payerInfo.setPassword(datadriven.get("paymentGw1.password"));
			payerInfo.setRandom_key(datadriven.get("paymentGw1.random_key"));
			paymentGw.setPayerInfo(payerInfo);
			
			if (!StringUtils.isBlank(payMethods10)) {
				payMethodss.add(JSON.parseObject(payMethods10, PayMethods.class));
			}
			if (!StringUtils.isBlank(payMethods11)) {
				payMethodss.add(JSON.parseObject(payMethods11, PayMethods.class));
			}
			if (!StringUtils.isBlank(payMethods12)) {
				payMethodss.add(JSON.parseObject(payMethods12, PayMethods.class));
			}
			if (!StringUtils.isBlank(payMethods13)) {
				payMethodss.add(JSON.parseObject(payMethods13, PayMethods.class));
			}
			paymentGw.setPayMethods(payMethodss);
			
			String reqJson3 = JSON.toJSONString(paymentGw);
			Reporter.log("网关支付请求报文:" + reqJson3, true);
			String sign3 = SignatureUtil.getInstance().sign(datadriven.get("paymentGw1.private_key"), reqJson3);
			String[] res3 = httpclient.post(paymentGwUrl, reqJson3, Property.get("SIGNATURE_TYPE"), sign3);
			String resJson3 = res3[0];
			String resSignatureData3 = res3[1];
			Reporter.log("网关支付返回报文:" + resJson3, true);
			// 验签
			assert SignatureUtil.getInstance().checksign(Property.get("YT_RSA_PUBLIC"), resJson3, resSignatureData3);

//			// 网关支付验证
//			if ("8888".equals(JSONObject.parseObject(resJson1).getString("ret_code"))) {
//				ValidationSms validationSms = new ValidationSms();
//				validationSms.setTimestamp(MyConfig.getTimestamp(datadriven.get("validationSms.timestamp")));
//				validationSms.setOid_partner(MyConfig.getOidPartner(datadriven.get("validationSms.oid_partner")));
//				validationSms.setPayer_type(datadriven.get("validationSms.payer_type"));
//				validationSms.setPayer_id(datadriven.get("validationSms.payer_id"));
//				if ("get".equals(datadriven.get("validationSms.txn_seqno"))) {
//					validationSms.setTxn_seqno(txn_seqno);
//				} else {
//					validationSms.setTxn_seqno(datadriven.get("validationSms.txn_seqno"));
//				}
//				validationSms.setTotal_amount(datadriven.get("validationSms.total_amount"));
//				if ("get".equals(datadriven.get("validationSms.token"))) {
//					validationSms.setToken(token);
//				} else {
//					validationSms.setToken(datadriven.get("validationSms.token"));
//				}
//				if ("get".equals(datadriven.get("validationSms.verify_code"))) {
//					String linked_phone = datadriven.get("paymentGw.linked_phone");
//					String verify_code = dataBaseAccess.getSms(linked_phone);
//					validationSms.setToken(verify_code);
//				} else {
//					validationSms.setToken(datadriven.get("validationSms.verify_code"));
//				}
//				String reqJson2 = JSON.toJSONString(validationSms);
//				Reporter.log("二次验证请求报文:" + reqJson2, true);
//				String sign2 = SignatureUtil.getInstance().sign(datadriven.get("validationSms.private_key"), reqJson2);
//				HttpResponse resp2 = httpclient.post(validationSmsUrl, reqJson2, Property.get("SIGNATURE_TYPE"), sign2);
//				String resJson2 = httpclient.getResponseBody(resp2);
//				String resSignatureData2 = httpclient.getResponseHeader(resp2);
//				Reporter.log("二次验证返回报文:" + resJson2, true);
//				// 验签
//				assert SignatureUtil.getInstance().checksign(Property.get("YT_RSA_PUBLIC"), resJson2,
//						resSignatureData2);
//
//			}

			// 请求&响应写入文件
			SampleFileUtils.appendLine("D://TA//accplog//paymentGw.txt", MyDate.getStringDate());
			SampleFileUtils.appendLine("D://TA//accplog//paymentGw.txt", reqJson);
			SampleFileUtils.appendLine("D://TA//accplog//paymentGw.txt", resJson);
			SampleFileUtils.appendLine("D://TA//accplog//paymentGw.txt", reqJson1);
			SampleFileUtils.appendLine("D://TA//accplog//paymentGw.txt", resJson1);
			SampleFileUtils.appendLine("D://TA//accplog//paymentGw.txt", reqJson3);
			SampleFileUtils.appendLine("D://TA//accplog//paymentGw.txt", resJson3);
			SampleFileUtils.appendLine("D://TA//accplog//paymentGw.txt",
					"===============================================");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			if (!StringUtils.isBlank(productUpdate)) {
				dataBaseAccess.insertTraderProduct(productUpdate, oid_partner);
			}
			if (!StringUtils.isBlank(ipUpdate)) {
				dataBaseAccess.updateIpRequest("*.*.*.*", oid_partner);
			}
			if (!StringUtils.isBlank(traderStatUpdate)){
				dataBaseAccess.updateTraderEnableState("0", oid_partner);
			}
			if (!StringUtils.isBlank(userStatUpdate)){
				dataBaseAccess.updateUserState1("NORMAL", datadriven.get("paymentGw.payer_id"), oid_partner);
			}
			if (!StringUtils.isBlank(traderLimitParamUpdate)) {
				String result[] = traderLimitParamUpdate.split(",");
				dataBaseAccess.updateTraderLimitParam(oid_partner, result[0], result[1], result[2], "1000000");
			}
			if (!StringUtils.isBlank(traderVerifyParamUpdate)) {
				String result1[] = traderVerifyParamUpdate.split(",");
				dataBaseAccess.updateTraderVerifyParam(oid_partner, result1[0], result1[1], "1000000", "1000000");
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
				case "MCHCOUPON_PSETTLE":
					String oid_acctno3 = dataBaseAccess.getAcctNo(oid_partner, "MCHCOUPON_PSETTLE");
					String oid_acctno4 = dataBaseAccess.getAcctNo(oid_partner, "MCHCOUPON_AVAILABLE");
					dataBaseAccessPayCore.updateAcctinfo(oid_acctno3, 100000000);
					dataBaseAccessPayCore.updateAcctinfo(oid_acctno4, 100000000);
					break;
				case "MCHCOUPON_AVAILABLE":
					String oid_acctno5 = dataBaseAccess.getAcctNo(oid_partner, "MCHCOUPON_AVAILABLE");
					dataBaseAccessPayCore.updateAcctinfo(oid_acctno5, 100000000);
					break;
				default:

				}
			}
			Reporter.log("##############################【xxxx测试结束】############################", true);
		}
	}
	
	@DataProvider(name = "paymentGw")
	public Iterator<Object[]> data4paymentGw() throws IOException {
		return new ExcelProvider(this, "paymentGw", 4);
	}

	@DataProvider(name = "paymentGw1")
	public Iterator<Object[]> data4paymentGw1() throws IOException {
		return new ExcelProvider(this, "paymentGw1", 111);
	}
	
	@AfterClass
	public void afterClass() {
		dataBaseAccess.closeDBConnection();
	}

}