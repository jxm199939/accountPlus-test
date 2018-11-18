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
import com.accp.test.bean.txn.BankCardInfo;
import com.accp.test.bean.txn.OrderInfo;
import com.accp.test.bean.txn.PayMethods;
import com.accp.test.bean.txn.PayeeInfo;
import com.accp.test.bean.txn.PayerInfo;
import com.accp.test.bean.txn.PaymentBankcard;
import com.accp.test.bean.txn.QueryPayment;
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
 * 银行卡支付接口测试
 */

@ContextConfiguration(locations = { "/consumer.xml" })
public class PaymentBankcardTest extends AbstractTestNGSpringContextTests {

	DataBaseAccess dataBaseAccess = new DataBaseAccess();
	DataBaseAccessPayCore dataBaseAccessPayCore = new DataBaseAccessPayCore();

	@Autowired
	private IAESCryptService aesCryptService;

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "银行卡支付接口测试", timeOut = 90000, dataProvider = "paymentBankcard")
	public void paymentBankcard(Map<String, String> datadriven) throws Exception {

		String tradeCreateUrl = Property.get("tradeCreate.url.test");
		String paymentBankcardUrl = Property.get("paymentBankcard.url.test");
		String validationSmsUrl = Property.get("validationSms.url.test");
		String queryPaymentUrl = Property.get("queryPayment.url.test");
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
		String oid_partner = MyConfig.getOidPartner(datadriven.get("paymentBankcard.oid_partner"));
		String user_id = MyConfig.getUser(datadriven.get("paymentBankcard.user_id"));
		String productUpdate = datadriven.get("productUpdate");
		String ipUpdate = datadriven.get("ipUpdate");
		String traderStatUpdate = datadriven.get("traderStatUpdate");
		String userStatUpdate = datadriven.get("userStatUpdate");
		String cardSignUpdate = datadriven.get("cardSignUpdate");
		String traderLimitParamUpdate = datadriven.get("traderLimitParamUpdate");
		String traderVerifyParamUpdate = datadriven.get("traderVerifyParamUpdate");
		String traderVerifyParamUpdate1 = datadriven.get("traderVerifyParamUpdate1");
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
			paymentBankcard.setOid_partner(oid_partner);
			if ("get".equals(datadriven.get("paymentBankcard.txn_seqno"))) {
				paymentBankcard.setTxn_seqno(txn_seqno);
			} else {
				paymentBankcard.setTxn_seqno(datadriven.get("paymentBankcard.txn_seqno"));
			}
			paymentBankcard.setTotal_amount(datadriven.get("paymentBankcard.total_amount"));
			paymentBankcard.setRisk_item(datadriven.get("paymentBankcard.risk_item"));

			payerInfo.setUser_id(user_id);
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

			if (!StringUtils.isBlank(productUpdate)) {
				dataBaseAccess.deleteTraderProduct(productUpdate, oid_partner);
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
			if (!StringUtils.isBlank(cardSignUpdate)) {
				dataBaseAccess.deleteCardSigned(aesCryptService, datadriven.get("paymentBankcard.linked_acctno"));
				dataBaseAccess.deleteUserBankCardInfo(aesCryptService, datadriven.get("paymentBankcard.linked_acctno"));
			}
			if (!StringUtils.isBlank(traderLimitParamUpdate)) {
				String result[] = traderLimitParamUpdate.split(",");
				dataBaseAccess.updateTraderLimitParam(oid_partner, result[0], result[1], result[2], result[3]);
			}
			if (!StringUtils.isBlank(traderVerifyParamUpdate)) {
				String result1[] = traderVerifyParamUpdate.split(",");
				dataBaseAccess.updateTraderVerifyParam(oid_partner, result1[0], result1[1], result1[2], result1[3]);
			}
			if (!StringUtils.isBlank(traderVerifyParamUpdate1)) {
				String result2[] = traderVerifyParamUpdate1.split(",");
				dataBaseAccess.updateTraderVerifyParam(oid_partner, result2[0], result2[1], result2[2], result2[3]);
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

			String reqJson1 = JSON.toJSONString(paymentBankcard);
			Reporter.log("银行卡支付请求报文:" + reqJson1, true);
			String sign1 = SignatureUtil.getInstance().sign(datadriven.get("paymentBankcard.private_key"), reqJson1);
			String[] res1 = httpclient.post(paymentBankcardUrl, reqJson1, Property.get("SIGNATURE_TYPE"), sign1);
			String resJson1 = res1[0];
			String resSignatureData1 = res1[1];
			String token = JSONObject.parseObject(resJson1).getString("token");
			Reporter.log("银行卡支付返回报文:" + resJson1, true);

			// 银行卡支付请求返回检查
			assert resJson1.contains(datadriven.get("expect_retmsg"));
			if ("0000".equals(JSONObject.parseObject(resJson1).getString("ret_code"))) {
				assert SignatureUtil.getInstance().checksign(Property.get("YT_RSA_PUBLIC"), resJson1,
						resSignatureData1);
				// 支付单状态校验
				BigDecimal AMT = new BigDecimal(String.valueOf(datadriven.get("total_amount")));
				assert "PAYBILL_FINISH"
						.equals(dataBaseAccess.getPayBillState(JSONObject.parseObject(resJson1).getString("accp_txno"),
								AMT.multiply(new BigDecimal(1000)).toString()));
			}

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
					String linked_phone = dataBaseAccess.getUserInfo(aesCryptService,
							MyConfig.getUser(datadriven.get("user_id")),
							MyConfig.getOidPartner(datadriven.get("oid_partner")))[1];
					if (!StringUtils.isBlank(datadriven.get("paymentBankcard.linked_phone"))) {
						linked_phone = datadriven.get("paymentBankcard.linked_phone");
					}
					String verify_code = dataBaseAccess.getSms(linked_phone);
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

				assert resJson2.contains("0000");
				if ("0000".equals(JSONObject.parseObject(resJson2).getString("ret_code"))) {
					assert SignatureUtil.getInstance().checksign(Property.get("YT_RSA_PUBLIC"), resJson2,
							resSignatureData2);
					// 支付单状态校验
					BigDecimal AMT = new BigDecimal(String.valueOf(datadriven.get("total_amount")));
					assert "PAYBILL_FINISH".equals(
							dataBaseAccess.getPayBillState(JSONObject.parseObject(resJson2).getString("accp_txno"),
									AMT.multiply(new BigDecimal(1000)).toString()));
				}

			}

			if ("0000".equals(JSONObject.parseObject(resJson1).getString("ret_code"))
					|| "8888".equals(JSONObject.parseObject(resJson1).getString("ret_code"))) {
				// 支付结果查询
				QueryPayment queryPayment = new QueryPayment();
				queryPayment.setTimestamp(MyConfig.getTimestamp(datadriven.get("timestamp")));
				queryPayment.setOid_partner(oid_partner);
				queryPayment.setTxn_seqno(txn_seqno);
				queryPayment.setAccp_txno(accp_txno);
				String reqJson3 = JSON.toJSONString(queryPayment);
				Reporter.log("支付结果查询请求报文:" + reqJson3, true);
				String sign3 = SignatureUtil.getInstance().sign(datadriven.get("private_key"), reqJson3);
				String[] res3 = httpclient.post(queryPaymentUrl, reqJson3, Property.get("SIGNATURE_TYPE"), sign3);
				String resJson3 = res3[0];
				String resSignatureData3 = res3[1];
				Reporter.log("支付结果查询返回报文:" + resJson3, true);
			}

			// 请求&响应写入文件
			SampleFileUtils.appendLine("D://TA//accplog//paymentBankcard.txt", MyDate.getStringDate());
			SampleFileUtils.appendLine("D://TA//accplog//paymentBankcard.txt", reqJson);
			SampleFileUtils.appendLine("D://TA//accplog//paymentBankcard.txt", resJson);
			SampleFileUtils.appendLine("D://TA//accplog//paymentBankcard.txt", reqJson1);
			SampleFileUtils.appendLine("D://TA//accplog//paymentBankcard.txt", resJson1);
			SampleFileUtils.appendLine("D://TA//accplog//paymentBankcard.txt",
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
			if (!StringUtils.isBlank(traderStatUpdate)) {
				dataBaseAccess.updateTraderEnableState("0", oid_partner);
			}
			if (!StringUtils.isBlank(userStatUpdate)) {
				dataBaseAccess.updateUserState1("NORMAL", user_id, oid_partner);
			}
			if (!StringUtils.isBlank(traderLimitParamUpdate)) {
				String result[] = traderLimitParamUpdate.split(",");
				dataBaseAccess.updateTraderLimitParam(oid_partner, result[0], result[1], result[2], "1000000");
			}
			if (!StringUtils.isBlank(traderVerifyParamUpdate)) {
				String result1[] = traderVerifyParamUpdate.split(",");
				dataBaseAccess.updateTraderVerifyParam(oid_partner, result1[0], result1[1], "1000000", "1000000");
			}
			if (!StringUtils.isBlank(traderVerifyParamUpdate1)) {
				String result2[] = traderVerifyParamUpdate1.split(",");
				dataBaseAccess.updateTraderVerifyParam(oid_partner, result2[0], result2[1], "1000000", "1000000");
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
			Reporter.log("银行卡支付接口测试： " + datadriven.get("row_num") + datadriven.get("case_title"), true);
			Reporter.log("##############################【xxxx测试结束】############################", true);
		}
	}

	@Test(description = "银行卡支付接口测试", timeOut = 120000, dataProvider = "paymentBankcard1")
	public void paymentBankcard1(Map<String, String> datadriven) throws Exception {

		String tradeCreateUrl = Property.get("tradeCreate.url.test");
		String paymentBankcardUrl = Property.get("paymentBankcard.url.test");
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
		String oid_partner = MyConfig.getOidPartner(datadriven.get("paymentBankcard.oid_partner"));
		String user_id = MyConfig.getUser(datadriven.get("paymentBankcard.user_id"));
		String productUpdate = datadriven.get("productUpdate");
		String ipUpdate = datadriven.get("ipUpdate");
		String traderStatUpdate = datadriven.get("traderStatUpdate");
		String userStatUpdate = datadriven.get("userStatUpdate");
		String cardSignUpdate = datadriven.get("cardSignUpdate");
		String traderLimitParamUpdate = datadriven.get("traderLimitParamUpdate");
		String traderVerifyParamUpdate = datadriven.get("traderVerifyParamUpdate");
		String traderVerifyParamUpdate1 = datadriven.get("traderVerifyParamUpdate1");
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
			paymentBankcard.setOid_partner(oid_partner);
			if ("get".equals(datadriven.get("paymentBankcard.txn_seqno"))) {
				paymentBankcard.setTxn_seqno(txn_seqno);
			} else {
				paymentBankcard.setTxn_seqno(datadriven.get("paymentBankcard.txn_seqno"));
			}
			paymentBankcard.setTotal_amount(datadriven.get("paymentBankcard.total_amount"));
			paymentBankcard.setRisk_item(datadriven.get("paymentBankcard.risk_item"));

			payerInfo.setUser_id(user_id);
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

			if (!StringUtils.isBlank(productUpdate)) {
				dataBaseAccess.deleteTraderProduct(productUpdate, oid_partner);
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
			if (!StringUtils.isBlank(cardSignUpdate)) {
				dataBaseAccess.deleteCardSigned(aesCryptService, datadriven.get("paymentBankcard.linked_acctno"));
				dataBaseAccess.deleteUserBankCardInfo(aesCryptService, datadriven.get("paymentBankcard.linked_acctno"));
			}
			if (!StringUtils.isBlank(traderLimitParamUpdate)) {
				String result[] = traderLimitParamUpdate.split(",");
				dataBaseAccess.updateTraderLimitParam(oid_partner, result[0], result[1], result[2], result[3]);
			}
			if (!StringUtils.isBlank(traderVerifyParamUpdate)) {
				String result1[] = traderVerifyParamUpdate.split(",");
				dataBaseAccess.updateTraderVerifyParam(oid_partner, result1[0], result1[1], result1[2], result1[3]);
			}
			if (!StringUtils.isBlank(traderVerifyParamUpdate1)) {
				String result2[] = traderVerifyParamUpdate1.split(",");
				dataBaseAccess.updateTraderVerifyParam(oid_partner, result2[0], result2[1], result2[2], result2[3]);
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

			// 二次重复支付
			List<PayMethods> payMethodss = new ArrayList<PayMethods>();
			String payMethods10 = datadriven.get("paymentBankcard1.payMethods0");
			String payMethods11 = datadriven.get("paymentBankcard1.payMethods1");
			String payMethods12 = datadriven.get("paymentBankcard1.payMethods2");
			String payMethods13 = datadriven.get("paymentBankcard1.payMethods3");

			paymentBankcard.setTimestamp(MyConfig.getTimestamp(datadriven.get("paymentBankcard1.timestamp")));
			paymentBankcard.setOid_partner(oid_partner);
			if ("get".equals(datadriven.get("paymentBankcard1.txn_seqno"))) {
				paymentBankcard.setTxn_seqno(txn_seqno);
			} else {
				paymentBankcard.setTxn_seqno(datadriven.get("paymentBankcard1.txn_seqno"));
			}
			paymentBankcard.setTotal_amount(datadriven.get("paymentBankcard1.total_amount"));
			paymentBankcard.setRisk_item(datadriven.get("paymentBankcard1.risk_item"));

			payerInfo.setUser_id(user_id);
			payerInfo.setPassword(datadriven.get("paymentBankcard1.password"));
			payerInfo.setRandom_key(datadriven.get("paymentBankcard1.random_key"));
			paymentBankcard.setPayerInfo(payerInfo);

			bankCardInfo.setLinked_acctno(datadriven.get("paymentBankcard1.linked_acctno"));
			if ("get".equals(datadriven.get("paymentBankcard1.linked_agrtno"))) {
				String linked_agrtno = dataBaseAccess.getAgreementNo(MyConfig.getUser(datadriven.get("user_id")),
						MyConfig.getOidPartner(datadriven.get("oid_partner")));
				bankCardInfo.setLinked_agrtno(linked_agrtno);
			} else {
				bankCardInfo.setLinked_agrtno(datadriven.get("paymentBankcard1.linked_agrtno"));
			}
			bankCardInfo.setLinked_phone(datadriven.get("paymentBankcard1.linked_phone"));
			bankCardInfo.setLinked_acctname(datadriven.get("paymentBankcard1.linked_acctname"));
			bankCardInfo.setId_type(datadriven.get("paymentBankcard1.id_type"));
			bankCardInfo.setId_no(datadriven.get("paymentBankcard1.id_no"));
			bankCardInfo.setCvv2(datadriven.get("paymentBankcard1.cvv2"));
			bankCardInfo.setValid_thru(datadriven.get("paymentBankcard1.valid_thru"));
			paymentBankcard.setBankCardInfo(bankCardInfo);

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
			paymentBankcard.setPayMethods(payMethodss);

			String reqJson3 = JSON.toJSONString(paymentBankcard);
			Reporter.log("银行卡支付请求报文:" + reqJson3, true);
			String sign3 = SignatureUtil.getInstance().sign(datadriven.get("paymentBankcard1.private_key"), reqJson3);
			String[] res3 = httpclient.post(paymentBankcardUrl, reqJson3, Property.get("SIGNATURE_TYPE"), sign3);
			String resJson3 = res3[0];
			String resSignatureData3 = res3[1];
			String token1 = JSONObject.parseObject(resJson3).getString("token");
			Reporter.log("银行卡支付返回报文:" + resJson3, true);
			// 验签
			assert SignatureUtil.getInstance().checksign(Property.get("YT_RSA_PUBLIC"), resJson3, resSignatureData3);
			assert resJson3.contains(datadriven.get("expect_retcode"));

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
					validationSms.setToken(token1);
				} else {
					validationSms.setToken(datadriven.get("validationSms.token"));
				}
				if ("get".equals(datadriven.get("validationSms.verify_code"))) {
					String linked_phone = dataBaseAccess.getUserInfo(aesCryptService,
							MyConfig.getUser(datadriven.get("user_id")),
							MyConfig.getOidPartner(datadriven.get("oid_partner")))[1];
					if (!StringUtils.isBlank(datadriven.get("paymentBankcard.linked_phone"))) {
						linked_phone = datadriven.get("paymentBankcard.linked_phone");
					}
					String verify_code = dataBaseAccess.getSms(linked_phone);
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

				if ("0000".equals(JSONObject.parseObject(resJson2).getString("ret_code"))) {
					assert SignatureUtil.getInstance().checksign(Property.get("YT_RSA_PUBLIC"), resJson2,
							resSignatureData2);
					// 支付单状态校验
					BigDecimal AMT = new BigDecimal(String.valueOf(datadriven.get("total_amount")));
					assert "PAYBILL_FINISH".equals(
							dataBaseAccess.getPayBillState(JSONObject.parseObject(resJson2).getString("accp_txno"),
									AMT.multiply(new BigDecimal(1000)).toString()));
				}
			}

			// 请求&响应写入文件
			SampleFileUtils.appendLine("D://TA//accplog//paymentBankcard.txt", MyDate.getStringDate());
			SampleFileUtils.appendLine("D://TA//accplog//paymentBankcard.txt", reqJson);
			SampleFileUtils.appendLine("D://TA//accplog//paymentBankcard.txt", resJson);
			SampleFileUtils.appendLine("D://TA//accplog//paymentBankcard.txt", reqJson1);
			SampleFileUtils.appendLine("D://TA//accplog//paymentBankcard.txt", resJson1);
			SampleFileUtils.appendLine("D://TA//accplog//paymentBankcard.txt", reqJson3);
			SampleFileUtils.appendLine("D://TA//accplog//paymentBankcard.txt", resJson3);
			SampleFileUtils.appendLine("D://TA//accplog//paymentBankcard.txt",
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
			if (!StringUtils.isBlank(traderStatUpdate)) {
				dataBaseAccess.updateTraderEnableState("0", oid_partner);
			}
			if (!StringUtils.isBlank(userStatUpdate)) {
				dataBaseAccess.updateUserState1("NORMAL", user_id, oid_partner);
			}
			if (!StringUtils.isBlank(traderLimitParamUpdate)) {
				String result[] = traderLimitParamUpdate.split(",");
				dataBaseAccess.updateTraderLimitParam(oid_partner, result[0], result[1], result[2], "1000000");
			}
			if (!StringUtils.isBlank(traderVerifyParamUpdate)) {
				String result1[] = traderVerifyParamUpdate.split(",");
				dataBaseAccess.updateTraderVerifyParam(oid_partner, result1[0], result1[1], "1000000", "1000000");
			}
			if (!StringUtils.isBlank(traderVerifyParamUpdate1)) {
				String result2[] = traderVerifyParamUpdate1.split(",");
				dataBaseAccess.updateTraderVerifyParam(oid_partner, result2[0], result2[1], "1000000", "1000000");
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
			Reporter.log("银行卡支付接口测试： " + datadriven.get("row_num") + datadriven.get("case_title"), true);
			Reporter.log("##############################【xxxx测试结束】############################", true);
		}
	}

	@Test(description = "银行卡支付接口测试", threadPoolSize = 2, invocationCount = 2, timeOut = 60000, dataProvider = "paymentBankcard2")
	public void paymentBankcard2(Map<String, String> datadriven) throws Exception {

		String paymentBankcardUrl = Property.get("paymentBankcard.url.test");
		String oid_partner = MyConfig.getOidPartner(datadriven.get("paymentBankcard.oid_partner"));
		String user_id = MyConfig.getUser(datadriven.get("paymentBankcard.user_id"));

		try {
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
			paymentBankcard.setOid_partner(oid_partner);
			paymentBankcard.setTxn_seqno(datadriven.get("paymentBankcard.txn_seqno"));
			paymentBankcard.setTotal_amount(datadriven.get("paymentBankcard.total_amount"));
			paymentBankcard.setRisk_item(datadriven.get("paymentBankcard.risk_item"));

			payerInfo.setUser_id(user_id);
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
			HttpRequestSimple httpclient = new HttpRequestSimple();
			String[] res1 = httpclient.post(paymentBankcardUrl, reqJson1, Property.get("SIGNATURE_TYPE"), sign1);
			String resJson1 = res1[0];
			String resSignatureData1 = res1[1];
			String token = JSONObject.parseObject(resJson1).getString("token");
			Reporter.log("银行卡支付返回报文:" + resJson1, true);

			// 银行卡支付请求返回检查
			assert resJson1.contains(datadriven.get("expect_retcode"));

			// 请求&响应写入文件
			SampleFileUtils.appendLine("D://TA//accplog//paymentBankcard.txt", MyDate.getStringDate());
			SampleFileUtils.appendLine("D://TA//accplog//paymentBankcard.txt", reqJson1);
			SampleFileUtils.appendLine("D://TA//accplog//paymentBankcard.txt", resJson1);
			SampleFileUtils.appendLine("D://TA//accplog//paymentBankcard.txt",
					"===============================================");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			Reporter.log("银行卡支付接口测试： " + datadriven.get("row_num") + datadriven.get("case_title"), true);
			Reporter.log("##############################【xxxx测试结束】############################", true);
		}
	}

	@DataProvider(name = "paymentBankcard")
	public Iterator<Object[]> data4paymentBankcard() throws IOException {
		return new ExcelProvider(this, "paymentBankcard");
	}

	@DataProvider(name = "paymentBankcard1")
	public Iterator<Object[]> data4paymentBankcard1() throws IOException {
		return new ExcelProvider(this, "paymentBankcard1");
	}

	@DataProvider(name = "paymentBankcard2")
	public Iterator<Object[]> data4paymentBankcard2() throws IOException {
		return new ExcelProvider(this, "paymentBankcard2", 99);
	}

	@AfterClass
	public void afterClass() {
		dataBaseAccess.closeDBConnection();
	}

}