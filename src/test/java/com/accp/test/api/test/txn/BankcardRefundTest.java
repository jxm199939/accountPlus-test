package com.accp.test.api.test.txn;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.accp.test.bean.txn.BankCardInfo;
import com.accp.test.bean.txn.ConfirmOrderInfo;
import com.accp.test.bean.txn.OrderInfo;
import com.accp.test.bean.txn.OriginalOrderInfo;
import com.accp.test.bean.txn.PayMethods;
import com.accp.test.bean.txn.PayeeInfo;
import com.accp.test.bean.txn.PayerInfo;
import com.accp.test.bean.txn.PaymentBankcard;
import com.accp.test.bean.txn.QueryRefund;
import com.accp.test.bean.txn.Refund;
import com.accp.test.bean.txn.RefundMethods;
import com.accp.test.bean.txn.RefundOrderInfo;
import com.accp.test.bean.txn.SecuredConfirm;
import com.accp.test.bean.txn.TradeCreate;
import com.accp.test.bean.txn.ValidationSms;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
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
 * 退款接口测试
 */

@ContextConfiguration(locations = { "/consumer.xml" })
public class BankcardRefundTest extends AbstractTestNGSpringContextTests {

	DataBaseAccess dataBaseAccess = new DataBaseAccess();
	DataBaseAccessPayCore dataBaseAccessPayCore = new DataBaseAccessPayCore();

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "退款接口测试", timeOut = 60000, dataProvider = "bankcardRefund")
	public void bankcardRefund(Map<String, String> datadriven) throws Exception {

		String tradeCreateUrl = Property.get("tradeCreate.url.test");
		String paymentBankcardUrl = Property.get("paymentBankcard.url.test");
		String validationSmsUrl = Property.get("validationSms.url.test");
		String refundUrl = Property.get("refund.url.test");
		String queryRefundUrl = Property.get("queryRefund.url.test");
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
		String txn_seqno = MyConfig.getTxnSeqno(datadriven.get("txn_seqno"));
		String oid_partner = MyConfig.getOidPartner(datadriven.get("refund.oid_partner"));
		String user_id = MyConfig.getUser(datadriven.get("refund.user_id"));
		String ipUpdate = datadriven.get("ipUpdate");
		String traderStatUpdate = datadriven.get("traderStatUpdate");
		String userStatUpdate = datadriven.get("userStatUpdate");

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

			orderInfo.setTxn_seqno(txn_seqno);
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
			Reporter.log("创单返回报文:" + resJson, true);

			// 银行卡支付
			PaymentBankcard paymentBankcard = new PaymentBankcard();
			PayerInfo payerInfo = new PayerInfo();
			BankCardInfo bankCardInfo = new BankCardInfo();
			List<PayMethods> payMethods = new ArrayList<PayMethods>();
			String payMethods0 = datadriven.get("paymentBankcard.payMethods0");
			String payMethods1 = datadriven.get("paymentBankcard.payMethods1");
			String payMethods2 = datadriven.get("paymentBankcard.payMethods2");
			String payMethods3 = datadriven.get("paymentBankcard.payMethods3");

			paymentBankcard.setTimestamp(MyConfig.getTimestamp(datadriven.get("paymentBankcard.timestamp")));
			paymentBankcard.setOid_partner(MyConfig.getOidPartner(datadriven.get("paymentBankcard.oid_partner")));
			if ("get".equals(datadriven.get("paymentBankcard.txn_seqno"))) {
				paymentBankcard.setTxn_seqno(txn_seqno);
			} else {
				paymentBankcard.setTxn_seqno(datadriven.get("paymentBankcard.txn_seqno"));
			}
			paymentBankcard.setTotal_amount(datadriven.get("paymentBankcard.total_amount"));
			paymentBankcard.setRisk_item(datadriven.get("paymentBankcard.risk_item"));

			payerInfo.setUser_id(MyConfig.getUser(datadriven.get("paymentBankcard.user_id")));
			payerInfo.setPassword(datadriven.get("paymentBankcard.password"));
			payerInfo.setRandom_key(datadriven.get("paymentBankcard.random_key"));
			paymentBankcard.setPayerInfo(payerInfo);

			bankCardInfo.setLinked_acctno(datadriven.get("paymentBankcard.linked_acctno"));
			if ("get".equals(datadriven.get("paymentBankcard.linked_agrtno"))) {
				String linked_agrtno = dataBaseAccess.getAgreementNo(MyConfig.getUser(datadriven.get("user_id")),
						MyConfig.getOidPartner(datadriven.get("oid_partner")));
				bankCardInfo.setLinked_agrtno(linked_agrtno);
			} else {
				bankCardInfo.setLinked_agrtno(datadriven.get("paymentBankcard.linked_agrtno"));
			}
			bankCardInfo.setLinked_phone(datadriven.get("paymentBankcard.linked_phone"));
			bankCardInfo.setLinked_acctname(datadriven.get("paymentBankcard.linked_acctname"));
			bankCardInfo.setId_type(datadriven.get("paymentBankcard.id_type"));
			bankCardInfo.setId_no(datadriven.get("paymentBankcard.id_no"));
			bankCardInfo.setCvv2(datadriven.get("paymentBankcard.cvv2"));
			bankCardInfo.setValid_thru(datadriven.get("paymentBankcard.valid_thru"));
			paymentBankcard.setBankCardInfo(bankCardInfo);

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
			paymentBankcard.setPayMethods(payMethods);

			String reqJson1 = JSON.toJSONString(paymentBankcard);
			Reporter.log("银行卡支付请求报文:" + reqJson1, true);
			String sign1 = SignatureUtil.getInstance().sign(datadriven.get("paymentBankcard.private_key"), reqJson1);
			String[] res1 = httpclient.post(paymentBankcardUrl, reqJson1, Property.get("SIGNATURE_TYPE"), sign1);
			String resJson1 = res1[0];
			String resSignatureData1 = res1[1];
			String token = JSONObject.parseObject(resJson1).getString("token");
			Reporter.log("银行卡支付返回报文:" + resJson1, true);
			// 验签
			assert SignatureUtil.getInstance().checksign(Property.get("YT_RSA_PUBLIC"), resJson1, resSignatureData1);

			// 银行卡支付验证
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
					String linked_phone = datadriven.get("paymentBankcard.linked_phone");
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

			// 退款
			Refund refund = new Refund();
			OriginalOrderInfo originalOrderInfo1 = new OriginalOrderInfo();
			RefundOrderInfo refundOrderInfo = new RefundOrderInfo();
			String refundMethods0 = datadriven.get("refund.refundMethods0");
			String refundMethods1 = datadriven.get("refund.refundMethods1");
			String refundMethods2 = datadriven.get("refund.refundMethods2");
			String refundMethods3 = datadriven.get("refund.refundMethods3");
			List<RefundMethods> refundMethods = new ArrayList<RefundMethods>();

			refund.setTimestamp(MyConfig.getTimestamp(datadriven.get("refund.timestamp")));
			refund.setOid_partner(oid_partner);
			refund.setUser_id(user_id);
			refund.setNotify_url(datadriven.get("refund.notify_url"));
			refund.setRefund_reason(datadriven.get("refund.refund_reason"));

			if ("get".equals(datadriven.get("refund.txn_seqno"))) {
				originalOrderInfo1.setTxn_seqno(txn_seqno);
			} else {
				originalOrderInfo1.setTxn_seqno(datadriven.get("refund.txn_seqno"));
			}
			originalOrderInfo1.setTotal_amount(datadriven.get("refund.total_amount"));
			refund.setOriginalOrderInfo(originalOrderInfo1);

			refundOrderInfo.setRefund_seqno(MyConfig.getRefundSeqno(datadriven.get("refund.refund_seqno")));
			refundOrderInfo.setRefund_time(MyConfig.getRefundTime(datadriven.get("refund.refund_time")));
			refundOrderInfo.setPayee_id(datadriven.get("refund.payee_id"));
			refundOrderInfo.setPayee_type(datadriven.get("refund.payee_type"));
			refundOrderInfo.setPayee_accttype(datadriven.get("refund.payee_accttype"));
			refundOrderInfo.setRefund_amount(datadriven.get("refund.refund_amount"));
			refund.setRefundOrderInfo(refundOrderInfo);

			if (!StringUtils.isBlank(refundMethods0)) {
				refundMethods.add(JSON.parseObject(refundMethods0, RefundMethods.class));
			}
			if (!StringUtils.isBlank(refundMethods1)) {
				refundMethods.add(JSON.parseObject(refundMethods1, RefundMethods.class));
			}
			if (!StringUtils.isBlank(refundMethods2)) {
				refundMethods.add(JSON.parseObject(refundMethods2, RefundMethods.class));
			}
			if (!StringUtils.isBlank(refundMethods3)) {
				refundMethods.add(JSON.parseObject(refundMethods3, RefundMethods.class));
			}
			refund.setRefundMethods(refundMethods);

			if (!StringUtils.isBlank(ipUpdate)) {
				dataBaseAccess.updateIpRequest(ipUpdate, oid_partner);
			}
			if (!StringUtils.isBlank(traderStatUpdate)) {
				dataBaseAccess.updateTraderEnableState(traderStatUpdate, oid_partner);
			}
			if (!StringUtils.isBlank(userStatUpdate)) {
				dataBaseAccess.updateUserState1(userStatUpdate, user_id, oid_partner);
			}

			String reqJson4 = JSON.toJSONString(refund);
			Reporter.log("退款请求报文:" + reqJson4, true);
			String sign4 = SignatureUtil.getInstance().sign(datadriven.get("refund.private_key"), reqJson4);
			String[] res4 = httpclient.post(refundUrl, reqJson4, Property.get("SIGNATURE_TYPE"), sign4);
			String resJson4 = res4[0];
			String resSignatureData4 = res4[1];
			String refund_seqno = JSONObject.parseObject(resJson4).getString("txn_seqno");
			String accp_txno = JSONObject.parseObject(resJson4).getString("accp_txno");
			Reporter.log("退款返回报文:" + resJson4, true);

			// 验签
			assert resJson4.contains(datadriven.get("expect_retcode"));
			if ("0000".equals(JSONObject.parseObject(resJson4).getString("ret_code"))) {
				assert SignatureUtil.getInstance().checksign(Property.get("YT_RSA_PUBLIC"), resJson4,
						resSignatureData4);
			}

			if ("0000".equals(JSONObject.parseObject(resJson4).getString("ret_code"))) {

				// 退款结果查询
				QueryRefund queryRefund = new QueryRefund();
				queryRefund.setTimestamp(MyConfig.getTimestamp(datadriven.get("timestamp")));
				queryRefund.setOid_partner(oid_partner);
				queryRefund.setRefund_seqno(refund_seqno);
				queryRefund.setAccp_txno(accp_txno);
				String reqJson5 = JSON.toJSONString(queryRefund);
				Reporter.log("退款结果查询请求报文:" + reqJson5, true);
				String sign5 = SignatureUtil.getInstance().sign(datadriven.get("private_key"), reqJson5);
				String[] res5 = httpclient.post(queryRefundUrl, reqJson5, Property.get("SIGNATURE_TYPE"), sign5);
				String resJson5 = res5[0];
				Reporter.log("退款结果查询返回报文:" + resJson5, true);
			}

			// 请求&响应写入文件
			SampleFileUtils.appendLine("D://TA//accplog//refund.txt", MyDate.getStringDate());
			SampleFileUtils.appendLine("D://TA//accplog//refund.txt", reqJson);
			SampleFileUtils.appendLine("D://TA//accplog//refund.txt", resJson);
			SampleFileUtils.appendLine("D://TA//accplog//refund.txt", reqJson1);
			SampleFileUtils.appendLine("D://TA//accplog//refund.txt", resJson1);
			SampleFileUtils.appendLine("D://TA//accplog//refund.txt", reqJson4);
			SampleFileUtils.appendLine("D://TA//accplog//refund.txt", resJson4);
			SampleFileUtils.appendLine("D://TA//accplog//refund.txt",
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
			Reporter.log("退款接口测试： " + datadriven.get("row_num") + datadriven.get("case_title"), true);
			Reporter.log("##############################【xxxx测试结束】############################", true);
		}
	}

	@Test(description = "退款接口测试", timeOut = 60000, dataProvider = "bankcardRefund1")
	public void bankcardRefund1(Map<String, String> datadriven) throws Exception {

		String tradeCreateUrl = Property.get("tradeCreate.url.test");
		String paymentBankcardUrl = Property.get("paymentBankcard.url.test");
		String validationSmsUrl = Property.get("validationSms.url.test");
		String refundUrl = Property.get("refund.url.test");
		String queryRefundUrl = Property.get("queryRefund.url.test");
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
		String txn_seqno = MyConfig.getTxnSeqno(datadriven.get("txn_seqno"));
		String oid_partner = MyConfig.getOidPartner(datadriven.get("refund.oid_partner"));
		String user_id = MyConfig.getUser(datadriven.get("refund.user_id"));
		String ipUpdate = datadriven.get("ipUpdate");
		String traderStatUpdate = datadriven.get("traderStatUpdate");
		String userStatUpdate = datadriven.get("userStatUpdate");

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

			orderInfo.setTxn_seqno(txn_seqno);
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
			Reporter.log("创单返回报文:" + resJson, true);

			// 银行卡支付
			PaymentBankcard paymentBankcard = new PaymentBankcard();
			PayerInfo payerInfo = new PayerInfo();
			BankCardInfo bankCardInfo = new BankCardInfo();
			List<PayMethods> payMethods = new ArrayList<PayMethods>();
			String payMethods0 = datadriven.get("paymentBankcard.payMethods0");
			String payMethods1 = datadriven.get("paymentBankcard.payMethods1");
			String payMethods2 = datadriven.get("paymentBankcard.payMethods2");
			String payMethods3 = datadriven.get("paymentBankcard.payMethods3");

			paymentBankcard.setTimestamp(MyConfig.getTimestamp(datadriven.get("paymentBankcard.timestamp")));
			paymentBankcard.setOid_partner(MyConfig.getOidPartner(datadriven.get("paymentBankcard.oid_partner")));
			if ("get".equals(datadriven.get("paymentBankcard.txn_seqno"))) {
				paymentBankcard.setTxn_seqno(txn_seqno);
			} else {
				paymentBankcard.setTxn_seqno(datadriven.get("paymentBankcard.txn_seqno"));
			}
			paymentBankcard.setTotal_amount(datadriven.get("paymentBankcard.total_amount"));
			paymentBankcard.setRisk_item(datadriven.get("paymentBankcard.risk_item"));

			payerInfo.setUser_id(MyConfig.getUser(datadriven.get("paymentBankcard.user_id")));
			payerInfo.setPassword(datadriven.get("paymentBankcard.password"));
			payerInfo.setRandom_key(datadriven.get("paymentBankcard.random_key"));
			paymentBankcard.setPayerInfo(payerInfo);

			bankCardInfo.setLinked_acctno(datadriven.get("paymentBankcard.linked_acctno"));
			if ("get".equals(datadriven.get("paymentBankcard.linked_agrtno"))) {
				String linked_agrtno = dataBaseAccess.getAgreementNo(MyConfig.getUser(datadriven.get("user_id")),
						MyConfig.getOidPartner(datadriven.get("oid_partner")));
				bankCardInfo.setLinked_agrtno(linked_agrtno);
			} else {
				bankCardInfo.setLinked_agrtno(datadriven.get("paymentBankcard.linked_agrtno"));
			}
			bankCardInfo.setLinked_phone(datadriven.get("paymentBankcard.linked_phone"));
			bankCardInfo.setLinked_acctname(datadriven.get("paymentBankcard.linked_acctname"));
			bankCardInfo.setId_type(datadriven.get("paymentBankcard.id_type"));
			bankCardInfo.setId_no(datadriven.get("paymentBankcard.id_no"));
			bankCardInfo.setCvv2(datadriven.get("paymentBankcard.cvv2"));
			bankCardInfo.setValid_thru(datadriven.get("paymentBankcard.valid_thru"));
			paymentBankcard.setBankCardInfo(bankCardInfo);

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
			paymentBankcard.setPayMethods(payMethods);

			String reqJson1 = JSON.toJSONString(paymentBankcard);
			Reporter.log("银行卡支付请求报文:" + reqJson1, true);
			String sign1 = SignatureUtil.getInstance().sign(datadriven.get("paymentBankcard.private_key"), reqJson1);
			String[] res1 = httpclient.post(paymentBankcardUrl, reqJson1, Property.get("SIGNATURE_TYPE"), sign1);
			String resJson1 = res1[0];
			String resSignatureData1 = res1[1];
			String token = JSONObject.parseObject(resJson1).getString("token");
			Reporter.log("银行卡支付返回报文:" + resJson1, true);
			// 验签
			assert SignatureUtil.getInstance().checksign(Property.get("YT_RSA_PUBLIC"), resJson1, resSignatureData1);

			// 银行卡支付验证
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
					String linked_phone = datadriven.get("paymentBankcard.linked_phone");
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

			// 退款
			Refund refund = new Refund();
			OriginalOrderInfo originalOrderInfo1 = new OriginalOrderInfo();
			RefundOrderInfo refundOrderInfo = new RefundOrderInfo();
			String refundMethods0 = datadriven.get("refund.refundMethods0");
			String refundMethods1 = datadriven.get("refund.refundMethods1");
			String refundMethods2 = datadriven.get("refund.refundMethods2");
			String refundMethods3 = datadriven.get("refund.refundMethods3");
			List<RefundMethods> refundMethods = new ArrayList<RefundMethods>();

			refund.setTimestamp(MyConfig.getTimestamp(datadriven.get("refund.timestamp")));
			refund.setOid_partner(oid_partner);
			refund.setUser_id(user_id);
			refund.setNotify_url(datadriven.get("refund.notify_url"));
			refund.setRefund_reason(datadriven.get("refund.refund_reason"));

			if ("get".equals(datadriven.get("refund.txn_seqno"))) {
				originalOrderInfo1.setTxn_seqno(txn_seqno);
			} else {
				originalOrderInfo1.setTxn_seqno(datadriven.get("refund.txn_seqno"));
			}
			originalOrderInfo1.setTotal_amount(datadriven.get("refund.total_amount"));
			refund.setOriginalOrderInfo(originalOrderInfo1);

			refundOrderInfo.setRefund_seqno(MyConfig.getRefundSeqno(datadriven.get("refund.refund_seqno")));
			refundOrderInfo.setRefund_time(MyConfig.getRefundTime(datadriven.get("refund.refund_time")));
			refundOrderInfo.setPayee_id(datadriven.get("refund.payee_id"));
			refundOrderInfo.setPayee_type(datadriven.get("refund.payee_type"));
			refundOrderInfo.setPayee_accttype(datadriven.get("refund.payee_accttype"));
			refundOrderInfo.setRefund_amount(datadriven.get("refund.refund_amount"));
			refund.setRefundOrderInfo(refundOrderInfo);

			if (!StringUtils.isBlank(refundMethods0)) {
				refundMethods.add(JSON.parseObject(refundMethods0, RefundMethods.class));
			}
			if (!StringUtils.isBlank(refundMethods1)) {
				refundMethods.add(JSON.parseObject(refundMethods1, RefundMethods.class));
			}
			if (!StringUtils.isBlank(refundMethods2)) {
				refundMethods.add(JSON.parseObject(refundMethods2, RefundMethods.class));
			}
			if (!StringUtils.isBlank(refundMethods3)) {
				refundMethods.add(JSON.parseObject(refundMethods3, RefundMethods.class));
			}
			refund.setRefundMethods(refundMethods);

			if (!StringUtils.isBlank(ipUpdate)) {
				dataBaseAccess.updateIpRequest(ipUpdate, oid_partner);
			}
			if (!StringUtils.isBlank(traderStatUpdate)) {
				dataBaseAccess.updateTraderEnableState(traderStatUpdate, oid_partner);
			}
			if (!StringUtils.isBlank(userStatUpdate)) {
				dataBaseAccess.updateUserState1(userStatUpdate, user_id, oid_partner);
			}

			String reqJson4 = JSON.toJSONString(refund);
			Reporter.log("退款请求报文:" + reqJson4, true);
			String sign4 = SignatureUtil.getInstance().sign(datadriven.get("refund.private_key"), reqJson4);
			String[] res4 = httpclient.post(refundUrl, reqJson4, Property.get("SIGNATURE_TYPE"), sign4);
			String resJson4 = res4[0];
			String resSignatureData4 = res4[1];
			String refund_seqno = JSONObject.parseObject(resJson4).getString("txn_seqno");
			String accp_txno = JSONObject.parseObject(resJson4).getString("accp_txno");
			Reporter.log("退款返回报文:" + resJson4, true);

			// 验签
			if ("0000".equals(JSONObject.parseObject(resJson4).getString("ret_code"))) {
				assert SignatureUtil.getInstance().checksign(Property.get("YT_RSA_PUBLIC"), resJson4,
						resSignatureData4);
			}

			// 二次退款
			Refund refund1 = new Refund();
			OriginalOrderInfo originalOrderInfo11 = new OriginalOrderInfo();
			RefundOrderInfo refundOrderInfo1 = new RefundOrderInfo();
			String refundMethods10 = datadriven.get("refund1.refundMethods0");
			String refundMethods11 = datadriven.get("refund1.refundMethods1");
			String refundMethods12 = datadriven.get("refund1.refundMethods2");
			String refundMethods13 = datadriven.get("refund1.refundMethods3");
			List<RefundMethods> refundMethodss = new ArrayList<RefundMethods>();

			refund1.setTimestamp(MyConfig.getTimestamp(datadriven.get("refund1.timestamp")));
			refund1.setOid_partner(oid_partner);
			refund1.setUser_id(user_id);
			refund1.setNotify_url(datadriven.get("refund1.notify_url"));
			refund1.setRefund_reason(datadriven.get("refund1.refund_reason"));

			if ("get".equals(datadriven.get("refund1.txn_seqno"))) {
				originalOrderInfo11.setTxn_seqno(txn_seqno);
			} else {
				originalOrderInfo11.setTxn_seqno(datadriven.get("refund1.txn_seqno"));
			}
			originalOrderInfo11.setTxn_seqno(txn_seqno);
			originalOrderInfo11.setTotal_amount(datadriven.get("refund1.total_amount"));
			refund1.setOriginalOrderInfo(originalOrderInfo11);

			refundOrderInfo1.setRefund_seqno(MyConfig.getRefundSeqno(datadriven.get("refund1.refund_seqno")));
			refundOrderInfo1.setRefund_time(MyConfig.getRefundTime(datadriven.get("refund1.refund_time")));
			refundOrderInfo1.setPayee_id(datadriven.get("refund1.payee_id"));
			refundOrderInfo1.setPayee_type(datadriven.get("refund1.payee_type"));
			refundOrderInfo1.setPayee_accttype(datadriven.get("refund1.payee_accttype"));
			refundOrderInfo1.setRefund_amount(datadriven.get("refund1.refund_amount"));
			refund1.setRefundOrderInfo(refundOrderInfo1);

			if (!StringUtils.isBlank(refundMethods10)) {
				refundMethodss.add(JSON.parseObject(refundMethods10, RefundMethods.class));
			}
			if (!StringUtils.isBlank(refundMethods11)) {
				refundMethodss.add(JSON.parseObject(refundMethods11, RefundMethods.class));
			}
			if (!StringUtils.isBlank(refundMethods12)) {
				refundMethodss.add(JSON.parseObject(refundMethods12, RefundMethods.class));
			}
			if (!StringUtils.isBlank(refundMethods13)) {
				refundMethodss.add(JSON.parseObject(refundMethods13, RefundMethods.class));
			}
			refund1.setRefundMethods(refundMethodss);

			String reqJson6 = JSON.toJSONString(refund1);
			Reporter.log("退款请求报文:" + reqJson6, true);
			String sign6 = SignatureUtil.getInstance().sign(datadriven.get("refund1.private_key"), reqJson6);
			String[] res6 = httpclient.post(refundUrl, reqJson6, Property.get("SIGNATURE_TYPE"), sign6);
			String resJson6 = res6[0];
			String resSignatureData6 = res6[1];
			String refund_seqno1 = JSONObject.parseObject(resJson6).getString("txn_seqno");
			String accp_txno1 = JSONObject.parseObject(resJson6).getString("accp_txno");
			Reporter.log("退款返回报文:" + resJson6, true);

			// 验签
			assert resJson6.contains(datadriven.get("expect_retcode"));
			if ("0000".equals(JSONObject.parseObject(resJson6).getString("ret_code"))) {
				assert SignatureUtil.getInstance().checksign(Property.get("YT_RSA_PUBLIC"), resJson6,
						resSignatureData6);
			}

			if ("0000".equals(JSONObject.parseObject(resJson6).getString("ret_code"))) {

				// 退款结果查询
				QueryRefund queryRefund = new QueryRefund();
				queryRefund.setTimestamp(MyConfig.getTimestamp(datadriven.get("timestamp")));
				queryRefund.setOid_partner(oid_partner);
				queryRefund.setRefund_seqno(refund_seqno1);
				queryRefund.setAccp_txno(accp_txno1);
				String reqJson5 = JSON.toJSONString(queryRefund);
				Reporter.log("退款结果查询请求报文:" + reqJson5, true);
				String sign5 = SignatureUtil.getInstance().sign(datadriven.get("private_key"), reqJson5);
				String[] res5 = httpclient.post(queryRefundUrl, reqJson5, Property.get("SIGNATURE_TYPE"), sign5);
				String resJson5 = res5[0];
				Reporter.log("退款结果查询返回报文:" + resJson5, true);
			}

			// 请求&响应写入文件
			SampleFileUtils.appendLine("D://TA//accplog//refund.txt", MyDate.getStringDate());
			SampleFileUtils.appendLine("D://TA//accplog//refund.txt", reqJson);
			SampleFileUtils.appendLine("D://TA//accplog//refund.txt", resJson);
			SampleFileUtils.appendLine("D://TA//accplog//refund.txt", reqJson1);
			SampleFileUtils.appendLine("D://TA//accplog//refund.txt", resJson1);
			SampleFileUtils.appendLine("D://TA//accplog//refund.txt", reqJson4);
			SampleFileUtils.appendLine("D://TA//accplog//refund.txt", resJson4);
			SampleFileUtils.appendLine("D://TA//accplog//refund.txt", reqJson6);
			SampleFileUtils.appendLine("D://TA//accplog//refund.txt", resJson6);
			SampleFileUtils.appendLine("D://TA//accplog//refund.txt",
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
			Reporter.log("退款接口测试： " + datadriven.get("row_num") + datadriven.get("case_title"), true);
			Reporter.log("##############################【xxxx测试结束】############################", true);
		}
	}

	@Test(description = "退款接口测试", timeOut = 60000, dataProvider = "bankcardRefund2")
	public void bankcardRefund2(Map<String, String> datadriven) throws Exception {

		String tradeCreateUrl = Property.get("tradeCreate.url.test");
		String paymentBankcardUrl = Property.get("paymentBankcard.url.test");
		String validationSmsUrl = Property.get("validationSms.url.test");
		String securedConfirmUrl = Property.get("securedConfirm.url.test");
		String refundUrl = Property.get("refund.url.test");
		String queryRefundUrl = Property.get("queryRefund.url.test");
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
		String txn_seqno = MyConfig.getTxnSeqno(datadriven.get("txn_seqno"));
		String oid_partner = MyConfig.getOidPartner(datadriven.get("refund.oid_partner"));
		String user_id = MyConfig.getUser(datadriven.get("refund.user_id"));
		String ipUpdate = datadriven.get("ipUpdate");
		String traderStatUpdate = datadriven.get("traderStatUpdate");
		String userStatUpdate = datadriven.get("userStatUpdate");

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

			orderInfo.setTxn_seqno(txn_seqno);
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
			Reporter.log("创单返回报文:" + resJson, true);

			// 银行卡支付
			PaymentBankcard paymentBankcard = new PaymentBankcard();
			PayerInfo payerInfo = new PayerInfo();
			BankCardInfo bankCardInfo = new BankCardInfo();
			List<PayMethods> payMethods = new ArrayList<PayMethods>();
			String payMethods0 = datadriven.get("paymentBankcard.payMethods0");
			String payMethods1 = datadriven.get("paymentBankcard.payMethods1");
			String payMethods2 = datadriven.get("paymentBankcard.payMethods2");
			String payMethods3 = datadriven.get("paymentBankcard.payMethods3");

			paymentBankcard.setTimestamp(MyConfig.getTimestamp(datadriven.get("paymentBankcard.timestamp")));
			paymentBankcard.setOid_partner(MyConfig.getOidPartner(datadriven.get("paymentBankcard.oid_partner")));
			if ("get".equals(datadriven.get("paymentBankcard.txn_seqno"))) {
				paymentBankcard.setTxn_seqno(txn_seqno);
			} else {
				paymentBankcard.setTxn_seqno(datadriven.get("paymentBankcard.txn_seqno"));
			}
			paymentBankcard.setTotal_amount(datadriven.get("paymentBankcard.total_amount"));
			paymentBankcard.setRisk_item(datadriven.get("paymentBankcard.risk_item"));

			payerInfo.setUser_id(MyConfig.getUser(datadriven.get("paymentBankcard.user_id")));
			payerInfo.setPassword(datadriven.get("paymentBankcard.password"));
			payerInfo.setRandom_key(datadriven.get("paymentBankcard.random_key"));
			paymentBankcard.setPayerInfo(payerInfo);

			bankCardInfo.setLinked_acctno(datadriven.get("paymentBankcard.linked_acctno"));
			if ("get".equals(datadriven.get("paymentBankcard.linked_agrtno"))) {
				String linked_agrtno = dataBaseAccess.getAgreementNo(MyConfig.getUser(datadriven.get("user_id")),
						MyConfig.getOidPartner(datadriven.get("oid_partner")));
				bankCardInfo.setLinked_agrtno(linked_agrtno);
			} else {
				bankCardInfo.setLinked_agrtno(datadriven.get("paymentBankcard.linked_agrtno"));
			}
			bankCardInfo.setLinked_phone(datadriven.get("paymentBankcard.linked_phone"));
			bankCardInfo.setLinked_acctname(datadriven.get("paymentBankcard.linked_acctname"));
			bankCardInfo.setId_type(datadriven.get("paymentBankcard.id_type"));
			bankCardInfo.setId_no(datadriven.get("paymentBankcard.id_no"));
			bankCardInfo.setCvv2(datadriven.get("paymentBankcard.cvv2"));
			bankCardInfo.setValid_thru(datadriven.get("paymentBankcard.valid_thru"));
			paymentBankcard.setBankCardInfo(bankCardInfo);

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
			paymentBankcard.setPayMethods(payMethods);

			String reqJson1 = JSON.toJSONString(paymentBankcard);
			Reporter.log("银行卡支付请求报文:" + reqJson1, true);
			String sign1 = SignatureUtil.getInstance().sign(datadriven.get("paymentBankcard.private_key"), reqJson1);
			String[] res1 = httpclient.post(paymentBankcardUrl, reqJson1, Property.get("SIGNATURE_TYPE"), sign1);
			String resJson1 = res1[0];
			String resSignatureData1 = res1[1];
			String token = JSONObject.parseObject(resJson1).getString("token");
			Reporter.log("银行卡支付返回报文:" + resJson1, true);
			// 验签
			assert SignatureUtil.getInstance().checksign(Property.get("YT_RSA_PUBLIC"), resJson1, resSignatureData1);

			// 银行卡支付验证
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
					String linked_phone = datadriven.get("paymentBankcard.linked_phone");
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

			// 退款
			Refund refund = new Refund();
			OriginalOrderInfo originalOrderInfo1 = new OriginalOrderInfo();
			RefundOrderInfo refundOrderInfo = new RefundOrderInfo();
			String refundMethods0 = datadriven.get("refund.refundMethods0");
			String refundMethods1 = datadriven.get("refund.refundMethods1");
			String refundMethods2 = datadriven.get("refund.refundMethods2");
			String refundMethods3 = datadriven.get("refund.refundMethods3");
			List<RefundMethods> refundMethods = new ArrayList<RefundMethods>();

			refund.setTimestamp(MyConfig.getTimestamp(datadriven.get("refund.timestamp")));
			refund.setOid_partner(oid_partner);
			refund.setUser_id(user_id);
			refund.setNotify_url(datadriven.get("refund.notify_url"));
			refund.setRefund_reason(datadriven.get("refund.refund_reason"));

			if ("get".equals(datadriven.get("refund.txn_seqno"))) {
				originalOrderInfo1.setTxn_seqno(txn_seqno);
			} else {
				originalOrderInfo1.setTxn_seqno(datadriven.get("refund.txn_seqno"));
			}
			originalOrderInfo1.setTotal_amount(datadriven.get("refund.total_amount"));
			refund.setOriginalOrderInfo(originalOrderInfo1);

			refundOrderInfo.setRefund_seqno(MyConfig.getRefundSeqno(datadriven.get("refund.refund_seqno")));
			refundOrderInfo.setRefund_time(MyConfig.getRefundTime(datadriven.get("refund.refund_time")));
			refundOrderInfo.setPayee_id(datadriven.get("refund.payee_id"));
			refundOrderInfo.setPayee_type(datadriven.get("refund.payee_type"));
			refundOrderInfo.setPayee_accttype(datadriven.get("refund.payee_accttype"));
			refundOrderInfo.setRefund_amount(datadriven.get("refund.refund_amount"));
			refund.setRefundOrderInfo(refundOrderInfo);

			if (!StringUtils.isBlank(refundMethods0)) {
				refundMethods.add(JSON.parseObject(refundMethods0, RefundMethods.class));
			}
			if (!StringUtils.isBlank(refundMethods1)) {
				refundMethods.add(JSON.parseObject(refundMethods1, RefundMethods.class));
			}
			if (!StringUtils.isBlank(refundMethods2)) {
				refundMethods.add(JSON.parseObject(refundMethods2, RefundMethods.class));
			}
			if (!StringUtils.isBlank(refundMethods3)) {
				refundMethods.add(JSON.parseObject(refundMethods3, RefundMethods.class));
			}
			refund.setRefundMethods(refundMethods);

			if (!StringUtils.isBlank(ipUpdate)) {
				dataBaseAccess.updateIpRequest(ipUpdate, oid_partner);
			}
			if (!StringUtils.isBlank(traderStatUpdate)) {
				dataBaseAccess.updateTraderEnableState(traderStatUpdate, oid_partner);
			}
			if (!StringUtils.isBlank(userStatUpdate)) {
				dataBaseAccess.updateUserState1(userStatUpdate, user_id, oid_partner);
			}

			String reqJson4 = JSON.toJSONString(refund);
			Reporter.log("退款请求报文:" + reqJson4, true);
			String sign4 = SignatureUtil.getInstance().sign(datadriven.get("refund.private_key"), reqJson4);
			String[] res4 = httpclient.post(refundUrl, reqJson4, Property.get("SIGNATURE_TYPE"), sign4);
			String resJson4 = res4[0];
			String resSignatureData4 = res4[1];
			String refund_seqno = JSONObject.parseObject(resJson4).getString("txn_seqno");
			String accp_txno = JSONObject.parseObject(resJson4).getString("accp_txno");
			Reporter.log("退款返回报文:" + resJson4, true);

			// 验签
			assert resJson4.contains(datadriven.get("expect_retcode"));
			if ("0000".equals(JSONObject.parseObject(resJson4).getString("ret_code"))) {
				assert SignatureUtil.getInstance().checksign(Property.get("YT_RSA_PUBLIC"), resJson4,
						resSignatureData4);
			}

			if ("0000".equals(JSONObject.parseObject(resJson4).getString("ret_code"))) {

				// 退款结果查询
				QueryRefund queryRefund = new QueryRefund();
				queryRefund.setTimestamp(MyConfig.getTimestamp(datadriven.get("timestamp")));
				queryRefund.setOid_partner(oid_partner);
				queryRefund.setRefund_seqno(refund_seqno);
				queryRefund.setAccp_txno(accp_txno);
				String reqJson5 = JSON.toJSONString(queryRefund);
				Reporter.log("退款结果查询请求报文:" + reqJson5, true);
				String sign5 = SignatureUtil.getInstance().sign(datadriven.get("private_key"), reqJson5);
				String[] res5 = httpclient.post(queryRefundUrl, reqJson5, Property.get("SIGNATURE_TYPE"), sign5);
				String resJson5 = res5[0];
				Reporter.log("退款结果查询返回报文:" + resJson5, true);
			}

			// 请求&响应写入文件
			SampleFileUtils.appendLine("D://TA//accplog//refund.txt", MyDate.getStringDate());
			SampleFileUtils.appendLine("D://TA//accplog//refund.txt", reqJson);
			SampleFileUtils.appendLine("D://TA//accplog//refund.txt", resJson);
			SampleFileUtils.appendLine("D://TA//accplog//refund.txt", reqJson1);
			SampleFileUtils.appendLine("D://TA//accplog//refund.txt", resJson1);
			SampleFileUtils.appendLine("D://TA//accplog//refund.txt", reqJson4);
			SampleFileUtils.appendLine("D://TA//accplog//refund.txt", resJson4);
			SampleFileUtils.appendLine("D://TA//accplog//refund.txt",
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
			Reporter.log("退款接口测试： " + datadriven.get("row_num") + datadriven.get("case_title"), true);
			Reporter.log("##############################【xxxx测试结束】############################", true);
		}
	}

	@Test(description = "退款接口测试", timeOut = 120000, dataProvider = "bankcardRefund3")
	public void bankcardRefund3(Map<String, String> datadriven) throws Exception {

		String tradeCreateUrl = Property.get("tradeCreate.url.test");
		String paymentBankcardUrl = Property.get("paymentBankcard.url.test");
		String validationSmsUrl = Property.get("validationSms.url.test");
		String securedConfirmUrl = Property.get("securedConfirm.url.test");
		String refundUrl = Property.get("refund.url.test");
		String queryRefundUrl = Property.get("queryRefund.url.test");
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
		String txn_seqno = MyConfig.getTxnSeqno(datadriven.get("txn_seqno"));
		String oid_partner = MyConfig.getOidPartner(datadriven.get("refund.oid_partner"));
		String user_id = MyConfig.getUser(datadriven.get("refund.user_id"));
		String ipUpdate = datadriven.get("ipUpdate");
		String traderStatUpdate = datadriven.get("traderStatUpdate");
		String userStatUpdate = datadriven.get("userStatUpdate");

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

			orderInfo.setTxn_seqno(txn_seqno);
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
			Reporter.log("创单返回报文:" + resJson, true);

			// 银行卡支付
			PaymentBankcard paymentBankcard = new PaymentBankcard();
			PayerInfo payerInfo = new PayerInfo();
			BankCardInfo bankCardInfo = new BankCardInfo();
			List<PayMethods> payMethods = new ArrayList<PayMethods>();
			String payMethods0 = datadriven.get("paymentBankcard.payMethods0");
			String payMethods1 = datadriven.get("paymentBankcard.payMethods1");
			String payMethods2 = datadriven.get("paymentBankcard.payMethods2");
			String payMethods3 = datadriven.get("paymentBankcard.payMethods3");

			paymentBankcard.setTimestamp(MyConfig.getTimestamp(datadriven.get("paymentBankcard.timestamp")));
			paymentBankcard.setOid_partner(MyConfig.getOidPartner(datadriven.get("paymentBankcard.oid_partner")));
			if ("get".equals(datadriven.get("paymentBankcard.txn_seqno"))) {
				paymentBankcard.setTxn_seqno(txn_seqno);
			} else {
				paymentBankcard.setTxn_seqno(datadriven.get("paymentBankcard.txn_seqno"));
			}
			paymentBankcard.setTotal_amount(datadriven.get("paymentBankcard.total_amount"));
			paymentBankcard.setRisk_item(datadriven.get("paymentBankcard.risk_item"));

			payerInfo.setUser_id(MyConfig.getUser(datadriven.get("paymentBankcard.user_id")));
			payerInfo.setPassword(datadriven.get("paymentBankcard.password"));
			payerInfo.setRandom_key(datadriven.get("paymentBankcard.random_key"));
			paymentBankcard.setPayerInfo(payerInfo);

			bankCardInfo.setLinked_acctno(datadriven.get("paymentBankcard.linked_acctno"));
			if ("get".equals(datadriven.get("paymentBankcard.linked_agrtno"))) {
				String linked_agrtno = dataBaseAccess.getAgreementNo(MyConfig.getUser(datadriven.get("user_id")),
						MyConfig.getOidPartner(datadriven.get("oid_partner")));
				bankCardInfo.setLinked_agrtno(linked_agrtno);
			} else {
				bankCardInfo.setLinked_agrtno(datadriven.get("paymentBankcard.linked_agrtno"));
			}
			bankCardInfo.setLinked_phone(datadriven.get("paymentBankcard.linked_phone"));
			bankCardInfo.setLinked_acctname(datadriven.get("paymentBankcard.linked_acctname"));
			bankCardInfo.setId_type(datadriven.get("paymentBankcard.id_type"));
			bankCardInfo.setId_no(datadriven.get("paymentBankcard.id_no"));
			bankCardInfo.setCvv2(datadriven.get("paymentBankcard.cvv2"));
			bankCardInfo.setValid_thru(datadriven.get("paymentBankcard.valid_thru"));
			paymentBankcard.setBankCardInfo(bankCardInfo);

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
			paymentBankcard.setPayMethods(payMethods);

			String reqJson1 = JSON.toJSONString(paymentBankcard);
			Reporter.log("银行卡支付请求报文:" + reqJson1, true);
			String sign1 = SignatureUtil.getInstance().sign(datadriven.get("paymentBankcard.private_key"), reqJson1);
			String[] res1 = httpclient.post(paymentBankcardUrl, reqJson1, Property.get("SIGNATURE_TYPE"), sign1);
			String resJson1 = res1[0];
			String resSignatureData1 = res1[1];
			String token = JSONObject.parseObject(resJson1).getString("token");
			Reporter.log("银行卡支付返回报文:" + resJson1, true);
			// 验签
			assert SignatureUtil.getInstance().checksign(Property.get("YT_RSA_PUBLIC"), resJson1, resSignatureData1);

			// 银行卡支付验证
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
					String linked_phone = datadriven.get("paymentBankcard.linked_phone");
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

			// 退款
			Refund refund = new Refund();
			OriginalOrderInfo originalOrderInfo1 = new OriginalOrderInfo();
			RefundOrderInfo refundOrderInfo = new RefundOrderInfo();
			String refundMethods0 = datadriven.get("refund.refundMethods0");
			String refundMethods1 = datadriven.get("refund.refundMethods1");
			String refundMethods2 = datadriven.get("refund.refundMethods2");
			String refundMethods3 = datadriven.get("refund.refundMethods3");
			List<RefundMethods> refundMethods = new ArrayList<RefundMethods>();

			refund.setTimestamp(MyConfig.getTimestamp(datadriven.get("refund.timestamp")));
			refund.setOid_partner(oid_partner);
			refund.setUser_id(user_id);
			refund.setNotify_url(datadriven.get("refund.notify_url"));
			refund.setRefund_reason(datadriven.get("refund.refund_reason"));

			if ("get".equals(datadriven.get("refund.txn_seqno"))) {
				originalOrderInfo1.setTxn_seqno(txn_seqno);
			} else {
				originalOrderInfo1.setTxn_seqno(datadriven.get("refund.txn_seqno"));
			}
			originalOrderInfo1.setTotal_amount(datadriven.get("refund.total_amount"));
			refund.setOriginalOrderInfo(originalOrderInfo1);

			refundOrderInfo.setRefund_seqno(MyConfig.getRefundSeqno(datadriven.get("refund.refund_seqno")));
			refundOrderInfo.setRefund_time(MyConfig.getRefundTime(datadriven.get("refund.refund_time")));
			refundOrderInfo.setPayee_id(datadriven.get("refund.payee_id"));
			refundOrderInfo.setPayee_type(datadriven.get("refund.payee_type"));
			refundOrderInfo.setPayee_accttype(datadriven.get("refund.payee_accttype"));
			refundOrderInfo.setRefund_amount(datadriven.get("refund.refund_amount"));
			refund.setRefundOrderInfo(refundOrderInfo);

			if (!StringUtils.isBlank(refundMethods0)) {
				refundMethods.add(JSON.parseObject(refundMethods0, RefundMethods.class));
			}
			if (!StringUtils.isBlank(refundMethods1)) {
				refundMethods.add(JSON.parseObject(refundMethods1, RefundMethods.class));
			}
			if (!StringUtils.isBlank(refundMethods2)) {
				refundMethods.add(JSON.parseObject(refundMethods2, RefundMethods.class));
			}
			if (!StringUtils.isBlank(refundMethods3)) {
				refundMethods.add(JSON.parseObject(refundMethods3, RefundMethods.class));
			}
			refund.setRefundMethods(refundMethods);

			if (!StringUtils.isBlank(ipUpdate)) {
				dataBaseAccess.updateIpRequest(ipUpdate, oid_partner);
			}
			if (!StringUtils.isBlank(traderStatUpdate)) {
				dataBaseAccess.updateTraderEnableState(traderStatUpdate, oid_partner);
			}
			if (!StringUtils.isBlank(userStatUpdate)) {
				dataBaseAccess.updateUserState1(userStatUpdate, user_id, oid_partner);
			}

			String reqJson4 = JSON.toJSONString(refund);
			Reporter.log("退款请求报文:" + reqJson4, true);
			String sign4 = SignatureUtil.getInstance().sign(datadriven.get("refund.private_key"), reqJson4);
			String[] res4 = httpclient.post(refundUrl, reqJson4, Property.get("SIGNATURE_TYPE"), sign4);
			String resJson4 = res4[0];
			String resSignatureData4 = res4[1];
			String refund_seqno = JSONObject.parseObject(resJson4).getString("txn_seqno");
			String accp_txno = JSONObject.parseObject(resJson4).getString("accp_txno");
			Reporter.log("退款返回报文:" + resJson4, true);

			// 验签
			if ("0000".equals(JSONObject.parseObject(resJson4).getString("ret_code"))) {
				assert SignatureUtil.getInstance().checksign(Property.get("YT_RSA_PUBLIC"), resJson4,
						resSignatureData4);
			}

			// 二次退款
			Refund refund1 = new Refund();
			OriginalOrderInfo originalOrderInfo11 = new OriginalOrderInfo();
			RefundOrderInfo refundOrderInfo1 = new RefundOrderInfo();
			String refundMethods10 = datadriven.get("refund1.refundMethods0");
			String refundMethods11 = datadriven.get("refund1.refundMethods1");
			String refundMethods12 = datadriven.get("refund1.refundMethods2");
			String refundMethods13 = datadriven.get("refund1.refundMethods3");
			List<RefundMethods> refundMethodss = new ArrayList<RefundMethods>();

			refund1.setTimestamp(MyConfig.getTimestamp(datadriven.get("refund1.timestamp")));
			refund1.setOid_partner(oid_partner);
			refund1.setUser_id(user_id);
			refund1.setNotify_url(datadriven.get("refund1.notify_url"));
			refund1.setRefund_reason(datadriven.get("refund1.refund_reason"));

			if ("get".equals(datadriven.get("refund1.txn_seqno"))) {
				originalOrderInfo11.setTxn_seqno(txn_seqno);
			} else {
				originalOrderInfo11.setTxn_seqno(datadriven.get("refund1.txn_seqno"));
			}
			originalOrderInfo11.setTxn_seqno(txn_seqno);
			originalOrderInfo11.setTotal_amount(datadriven.get("refund1.total_amount"));
			refund1.setOriginalOrderInfo(originalOrderInfo11);

			refundOrderInfo1.setRefund_seqno(MyConfig.getRefundSeqno(datadriven.get("refund1.refund_seqno")));
			refundOrderInfo1.setRefund_time(MyConfig.getRefundTime(datadriven.get("refund1.refund_time")));
			refundOrderInfo1.setPayee_id(datadriven.get("refund1.payee_id"));
			refundOrderInfo1.setPayee_type(datadriven.get("refund1.payee_type"));
			refundOrderInfo1.setPayee_accttype(datadriven.get("refund1.payee_accttype"));
			refundOrderInfo1.setRefund_amount(datadriven.get("refund1.refund_amount"));
			refund1.setRefundOrderInfo(refundOrderInfo1);

			if (!StringUtils.isBlank(refundMethods10)) {
				refundMethodss.add(JSON.parseObject(refundMethods10, RefundMethods.class));
			}
			if (!StringUtils.isBlank(refundMethods11)) {
				refundMethodss.add(JSON.parseObject(refundMethods11, RefundMethods.class));
			}
			if (!StringUtils.isBlank(refundMethods12)) {
				refundMethodss.add(JSON.parseObject(refundMethods12, RefundMethods.class));
			}
			if (!StringUtils.isBlank(refundMethods13)) {
				refundMethodss.add(JSON.parseObject(refundMethods13, RefundMethods.class));
			}
			refund1.setRefundMethods(refundMethodss);

			String reqJson6 = JSON.toJSONString(refund1);
			Reporter.log("退款请求报文:" + reqJson6, true);
			String sign6 = SignatureUtil.getInstance().sign(datadriven.get("refund1.private_key"), reqJson6);
			String[] res6 = httpclient.post(refundUrl, reqJson6, Property.get("SIGNATURE_TYPE"), sign6);
			String resJson6 = res6[0];
			String resSignatureData6 = res6[1];
			String refund_seqno1 = JSONObject.parseObject(resJson6).getString("txn_seqno");
			String accp_txno1 = JSONObject.parseObject(resJson6).getString("accp_txno");
			Reporter.log("退款返回报文:" + resJson6, true);

			// 验签
			assert resJson6.contains(datadriven.get("expect_retcode"));
			if ("0000".equals(JSONObject.parseObject(resJson6).getString("ret_code"))) {
				assert SignatureUtil.getInstance().checksign(Property.get("YT_RSA_PUBLIC"), resJson6,
						resSignatureData6);
			}

			if ("0000".equals(JSONObject.parseObject(resJson6).getString("ret_code"))) {

				// 退款结果查询
				QueryRefund queryRefund = new QueryRefund();
				queryRefund.setTimestamp(MyConfig.getTimestamp(datadriven.get("timestamp")));
				queryRefund.setOid_partner(oid_partner);
				queryRefund.setRefund_seqno(refund_seqno1);
				queryRefund.setAccp_txno(accp_txno1);
				String reqJson5 = JSON.toJSONString(queryRefund);
				Reporter.log("退款结果查询请求报文:" + reqJson5, true);
				String sign5 = SignatureUtil.getInstance().sign(datadriven.get("private_key"), reqJson5);
				String[] res5 = httpclient.post(queryRefundUrl, reqJson5, Property.get("SIGNATURE_TYPE"), sign5);
				String resJson5 = res5[0];
				Reporter.log("退款结果查询返回报文:" + resJson5, true);
			}

			// 请求&响应写入文件
			SampleFileUtils.appendLine("D://TA//accplog//refund.txt", MyDate.getStringDate());
			SampleFileUtils.appendLine("D://TA//accplog//refund.txt", reqJson);
			SampleFileUtils.appendLine("D://TA//accplog//refund.txt", resJson);
			SampleFileUtils.appendLine("D://TA//accplog//refund.txt", reqJson1);
			SampleFileUtils.appendLine("D://TA//accplog//refund.txt", resJson1);
			SampleFileUtils.appendLine("D://TA//accplog//refund.txt", reqJson4);
			SampleFileUtils.appendLine("D://TA//accplog//refund.txt", resJson4);
			SampleFileUtils.appendLine("D://TA//accplog//refund.txt", reqJson6);
			SampleFileUtils.appendLine("D://TA//accplog//refund.txt", resJson6);
			SampleFileUtils.appendLine("D://TA//accplog//refund.txt",
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
			Reporter.log("退款接口测试： " + datadriven.get("row_num") + datadriven.get("case_title"), true);
			Reporter.log("##############################【xxxx测试结束】############################", true);
		}
	}

	@DataProvider(name = "bankcardRefund")
	public Iterator<Object[]> data4bankcardRefund() throws IOException {
		return new ExcelProvider(this, "bankcardRefund");
	}

	@DataProvider(name = "bankcardRefund1")
	public Iterator<Object[]> data4bankcardRefund1() throws IOException {
		return new ExcelProvider(this, "bankcardRefund1");
	}

	@DataProvider(name = "bankcardRefund2")
	public Iterator<Object[]> data4bankcardRefund2() throws IOException {
		return new ExcelProvider(this, "bankcardRefund2");
	}

	@DataProvider(name = "bankcardRefund3")
	public Iterator<Object[]> data4bankcardRefund3() throws IOException {
		return new ExcelProvider(this, "bankcardRefund3");
	}

	@AfterClass
	public void afterClass() {
		dataBaseAccess.closeDBConnection();
	}

}