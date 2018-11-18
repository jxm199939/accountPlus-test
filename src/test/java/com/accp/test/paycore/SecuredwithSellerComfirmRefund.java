package com.accp.test.paycore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.http.HttpResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import com.accp.test.bean.txn.BankCardInfo;
import com.accp.test.bean.txn.ConfirmOrderInfo;
import com.accp.test.bean.txn.OrderInfo;
import com.accp.test.bean.txn.OriginalOrderInfo;
import com.accp.test.bean.txn.PayMethods;
import com.accp.test.bean.txn.PayeeInfo;
import com.accp.test.bean.txn.PayerInfo;
import com.accp.test.bean.txn.PaymentBalance;
import com.accp.test.bean.txn.PaymentBankcard;
import com.accp.test.bean.txn.PaymentGw;
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
import com.tools.utils.FuncUtil;
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
 * @author jiangxm tengaj
 * 担保交易确认退款接口测试
 */

@ContextConfiguration(locations = { "/consumer.xml" })
public class SecuredwithSellerComfirmRefund extends AbstractTestNGSpringContextTests {

	FuncUtil funcUtil = new FuncUtil();
	DataBaseAccess dataBaseAccess = new DataBaseAccess();
	DataBaseAccessPayCore dataBaseAccessPayCore = new DataBaseAccessPayCore();
	
	boolean amt_Check=false;
	boolean paybill_state_Check=false;
	boolean SeuredPaybill_state_Check=false;
	
	String ret_code ;
	String ret_msg ;
	String accp_txno;
	String txn_seqno;
	
	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "担保交易确认退款接口测试", timeOut = 600000, dataProvider = "SecuredwithSellerComfirmRefund")
	public void SecuredwithSellerComfirmRefund(Map<String, String> datadriven) throws Exception {
		
		String tradeCreateUrl = Property.get("tradeCreate.url.test");
		String paymentBalanceUrl = Property.get("paymentBalance.url.test");
		String paymentBankcardUrl = Property.get("paymentBankcard.url.test");
		String paymentGwUrl = Property.get("paymentGw.url.test");
		String validationSmsUrl = Property.get("validationSms.url.test");
		String securedConfirmUrl = Property.get("securedConfirm.url.test");
		String RefundUrl = Property.get("refund.url.test");
		txn_seqno=MyConfig.getTxnSeqno(datadriven.get("txn_seqno"));
		String oid_partner = MyConfig.getOidPartner(datadriven.get("paymentBankcard.oid_partner"));
		String user_id = MyConfig.getOidPartner(datadriven.get("paymentBankcard.user_id"));
		
		//初始化金额
		System.out.println("账户金额初始化");	
		
		dataBaseAccessPayCore.updateAcctinfo(MyConfig.getAccNO("get_user_A"),Float.parseFloat(datadriven.get("user_A_pre")));//付款方用户可用账户
		dataBaseAccessPayCore.updateAcctinfo(MyConfig.getAccNO("get_user_P"),Float.parseFloat(datadriven.get("user_P_pre")));//付款方用户待结算账户
		dataBaseAccessPayCore.updateAcctinfo(MyConfig.getAccNO("get_mchcoupon_A"),Float.parseFloat(datadriven.get("mchcoupon_A_pre"))); //优惠券商户可用账户
		dataBaseAccessPayCore.updateAcctinfo(MyConfig.getAccNO("get_mchcoupon_P"),Float.parseFloat(datadriven.get("mchcoupon_P_pre"))); //优惠券商户待结算账户
		dataBaseAccessPayCore.updateAcctinfo(MyConfig.getAccNO("get_mchassure_P"),Float.parseFloat(datadriven.get("mchassure_P_pre")));//担保方待结算账户
		dataBaseAccessPayCore.updateAcctinfo(MyConfig.getAccNO("get_payee_user_A"),Float.parseFloat(datadriven.get("payee_user_A_pre")));//卖方用户可用账户
		dataBaseAccessPayCore.updateAcctinfo(MyConfig.getAccNO("get_payee_user_P"),Float.parseFloat(datadriven.get("payee_user_P_pre")));//卖方用户待结算账户
		dataBaseAccessPayCore.updateAcctinfo(MyConfig.getAccNO("get_payee_mch_A"),Float.parseFloat(datadriven.get("payee_mch_A_pre")));//卖方商户可用账户
		dataBaseAccessPayCore.updateAcctinfo(MyConfig.getAccNO("get_payee_mch_P"),Float.parseFloat(datadriven.get("payee_mch_P_pre")));//卖方商户待结算账户		
		Thread.sleep(1000);
		
		TradeCreate tradeCreate = new TradeCreate();
		OrderInfo orderInfo = new OrderInfo();
		String payeeInfo0 = datadriven.get("payeeInfo0");
		String payeeInfo1 = datadriven.get("payeeInfo1");
		String payeeInfo2 = datadriven.get("payeeInfo2");
		List<PayeeInfo> payeeInfoList = new ArrayList<PayeeInfo>();


		try {// 创单
			Reporter.log("##############################【xxxx测试开始】############################", true);
			tradeCreate.setTimestamp(MyConfig.getTimestamp(datadriven.get("timestamp")));
			tradeCreate.setOid_partner(MyConfig.getOidPartner(datadriven.get("oid_partner")));
			tradeCreate.setTxn_type(datadriven.get("txn_type"));
			tradeCreate.setUser_id(MyConfig.getOidPartner(datadriven.get("user_id")));
			tradeCreate.setUser_type(datadriven.get("user_type"));
			tradeCreate.setNotify_url(datadriven.get("notify_url"));
			tradeCreate.setReturn_url(datadriven.get("return_url"));
			tradeCreate.setPay_expire(datadriven.get("pay_expire"));
			//orderInfo.setTxn_seqno(MyConfig.getTxnSeqno(datadriven.get("txn_seqno")));
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

			tradeCreate.setPayeeInfo(payeeInfoList);

			String reqJson = JSON.toJSONString(tradeCreate);
			Reporter.log("创单请求报文:" + reqJson, true);
			String sign = SignatureUtil.getInstance().sign(datadriven.get("private_key"), reqJson);
			HttpRequestSimple httpclient = new HttpRequestSimple();
			String[] res = httpclient.post(tradeCreateUrl, reqJson, Property.get("SIGNATURE_TYPE"), sign);
			String resJson =  res[0];
			String resSignatureData = res[1];
			Reporter.log("创单返回报文:" + resJson, true);
			accp_txno=JSONObject.parseObject(resJson).getString("accp_txno");
			Reporter.log("txn_seqno:" + txn_seqno, true);
			Reporter.log("accp_txno:" + accp_txno, true);

			
			switch (datadriven.get("pay_type")) {
				case "1":
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
					payerInfo.setUser_id(MyConfig.getOidPartner(datadriven.get("paymentBalance.user_id")));
					payerInfo.setPassword(datadriven.get("paymentBalance.password"));
					payerInfo.setRandom_key(datadriven.get("paymentBalance.random_key"));
					paymentBalance.setPayerInfo(payerInfo);

					String reqJson1 = JSON.toJSONString(paymentBalance);
					Reporter.log("余额支付请求报文:" + reqJson1, true);
					String sign1 = SignatureUtil.getInstance().sign(datadriven.get("paymentBalance.private_key"), reqJson1);
					String[] res1 = httpclient.post(paymentBalanceUrl, reqJson1, Property.get("SIGNATURE_TYPE"), sign1);
					String resJson1 = res1[0];
					ret_code=JSONObject.parseObject(resJson1).getString("ret_code");
					ret_msg=JSONObject.parseObject(resJson1).getString("ret_msg");
					Reporter.log("余额支付返回报文:" + resJson1, true);

					break;
					
				case "2":
					// 银行卡支付
					PaymentBankcard paymentBankcard = new PaymentBankcard();
					PayerInfo payerInfo1 = new PayerInfo();
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

					payerInfo1.setUser_id(user_id);
					payerInfo1.setPassword(datadriven.get("paymentBankcard.password"));
					payerInfo1.setRandom_key(datadriven.get("paymentBankcard.random_key"));
					paymentBankcard.setPayerInfo(payerInfo1);
					
					bankCardInfo.setLinked_acctno(datadriven.get("paymentBankcard.linked_acctno"));
					if ("get".equals(datadriven.get("paymentBankcard.linked_agrtno"))){
						String linked_agrtno = dataBaseAccess.getAgreementNo(MyConfig.getUser(datadriven.get("user_id")),
								MyConfig.getOidPartner(datadriven.get("oid_partner")));
						bankCardInfo.setLinked_agrtno(linked_agrtno);
					}else{
						bankCardInfo.setLinked_agrtno(datadriven.get("paymentBankcard.linked_agrtno"));
					}
					bankCardInfo.setLinked_phone(datadriven.get("paymentBankcard.linked_phone"));
					bankCardInfo.setLinked_acctname(datadriven.get("paymentBankcard.linked_acctname"));
					bankCardInfo.setId_type(datadriven.get("paymentBankcard.id_type"));
					bankCardInfo.setId_no(datadriven.get("paymentBankcard.id_no"));
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
				
					String reqJson2 = JSON.toJSONString(paymentBankcard);
					Reporter.log("银行卡支付请求报文:" + reqJson2, true);
					String sign2 = SignatureUtil.getInstance().sign(datadriven.get("paymentBankcard.private_key"), reqJson2);
					String[] res2 = httpclient.post(paymentBankcardUrl, reqJson2, Property.get("SIGNATURE_TYPE"), sign2);
					String resJson2 = res2[0];

					ret_code=JSONObject.parseObject(resJson2).getString("ret_code");
					ret_msg=JSONObject.parseObject(resJson2).getString("ret_msg");				
					Reporter.log("银行卡支付返回报文:" + resJson2, true);
					break;
					
					
				case "3":
					// 网关支付
					PaymentGw paymentGw = new PaymentGw();
					PayerInfo payerInfo2 = new PayerInfo();
					List<PayMethods> payMethodslist = new ArrayList<PayMethods>();
					String payMethods00 = datadriven.get("paymentGw.payMethods0");
					String payMethods01 = datadriven.get("paymentGw.payMethods1");
					String payMethods02 = datadriven.get("paymentGw.payMethods2");
					String payMethods03 = datadriven.get("paymentGw.payMethods3");
					
					paymentGw.setTimestamp(MyConfig.getTimestamp(datadriven.get("paymentGw.timestamp")));
					//paymentGw.setOid_partner(oid_partner);
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

					payerInfo2.setPayer_accttype(datadriven.get("paymentGw.payer_accttype"));
					payerInfo2.setPayer_id(datadriven.get("paymentGw.payer_id"));
					payerInfo2.setPassword(datadriven.get("paymentGw.password"));
					payerInfo2.setRandom_key(datadriven.get("paymentGw.random_key"));
					paymentGw.setPayerInfo(payerInfo2);
					
					if (!StringUtils.isBlank(payMethods00)) {
						payMethodslist.add(JSON.parseObject(payMethods00, PayMethods.class));
					}
					if (!StringUtils.isBlank(payMethods01)) {
						payMethodslist.add(JSON.parseObject(payMethods01, PayMethods.class));
					}
					if (!StringUtils.isBlank(payMethods02)) {
						payMethodslist.add(JSON.parseObject(payMethods02, PayMethods.class));
					}
					if (!StringUtils.isBlank(payMethods03)) {
						payMethodslist.add(JSON.parseObject(payMethods03, PayMethods.class));
					}
					paymentGw.setPayMethods(payMethodslist);

					
					String reqJson3 = JSON.toJSONString(paymentGw);
					Reporter.log("网关支付请求报文:" + reqJson3, true);
					String sign3 = SignatureUtil.getInstance().sign(datadriven.get("paymentGw.private_key"), reqJson3);
					String[] res3 = httpclient.post(paymentBalanceUrl, reqJson3, Property.get("SIGNATURE_TYPE"), sign3);
					String resJson3 = res3[0];
					ret_code=JSONObject.parseObject(resJson3).getString("ret_code");
					ret_msg=JSONObject.parseObject(resJson3).getString("ret_msg");
					Reporter.log("网关支付返回报文:" + resJson3, true);
					break;
			}
			
			
			// 担保交易确认
			if(!datadriven.get("securedConfirm.private_key").equals("")){
				SecuredConfirm securedConfirm = new SecuredConfirm();
				OriginalOrderInfo originalOrderInfo = new OriginalOrderInfo();
				ConfirmOrderInfo confirmOrderInfo = new ConfirmOrderInfo();
				String payeeInfo10 = datadriven.get("securedConfirm.payeeInfo0");
				String payeeInfo11 = datadriven.get("securedConfirm.payeeInfo1");
				String payeeInfo12 = datadriven.get("securedConfirm.payeeInfo2");
				String payeeInfo13 = datadriven.get("securedConfirm.payeeInfo3");
				List<PayeeInfo> payeeInfoList1 = new ArrayList<PayeeInfo>();
	
				securedConfirm.setTimestamp(MyConfig.getTimestamp(datadriven.get("securedConfirm.timestamp")));
				securedConfirm.setOid_partner(MyConfig.getOidPartner(datadriven.get("securedConfirm.oid_partner")));
				securedConfirm.setUser_id(MyConfig.getOidPartner(datadriven.get("securedConfirm.user_id")));
				securedConfirm.setNotify_url(datadriven.get("securedConfirm.notify_url"));
				securedConfirm.setConfirm_mode(datadriven.get("securedConfirm.confirm_mode"));
	
				originalOrderInfo.setTxn_seqno(txn_seqno);
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
				securedConfirm.setPayeeInfo(payeeInfoList1);
	
				String reqJson3 = JSON.toJSONString(securedConfirm);
				Reporter.log("担保交易确认请求报文:" + reqJson3, true);
				String sign3 = SignatureUtil.getInstance().sign(datadriven.get("securedConfirm.private_key"), reqJson3);
				String[] res3 = httpclient.post(securedConfirmUrl, reqJson3, Property.get("SIGNATURE_TYPE"), sign3);
				String resJson3 = res3[0];
				Reporter.log("担保交易确认返回报文:" + resJson3, true);
			}
		
			// 退款
			if(!datadriven.get("refund.Update").equals("")){
				switch (datadriven.get("refund.UpdateType")) {
				case "1":
					dataBaseAccessPayCore.updatePayConfiguration(datadriven.get("refund.Update")+"'"+accp_txno+"'");	
					break;
				case "2":
					dataBaseAccessPayCore.insertAcctConfiguration(datadriven.get("refund.Update"));	
					break;
				case "3":
					dataBaseAccessPayCore.insertAcctConfiguration(datadriven.get("refund.Update"));
					dataBaseAccessPayCore.insertAcctConfiguration(datadriven.get("refund.Update1"));
					break;
				case "4":
					dataBaseAccessPayCore.insertdbcustConfiguration(datadriven.get("refund.Update"));	
					break;
				}
			}
			Refund refund = new Refund();
			OriginalOrderInfo originalOrderInfo1 = new OriginalOrderInfo();
			RefundOrderInfo refundOrderInfo = new RefundOrderInfo();
			String refundMethods0 = datadriven.get("refund.refundMethods0");
			String refundMethods1 = datadriven.get("refund.refundMethods1");
			String refundMethods2 = datadriven.get("refund.refundMethods2");
			String refundMethods3 = datadriven.get("refund.refundMethods3");
			List<RefundMethods> refundMethods = new ArrayList<RefundMethods>();

			refund.setTimestamp(MyConfig.getTimestamp(datadriven.get("refund.timestamp")));
			refund.setOid_partner(MyConfig.getOidPartner(datadriven.get("refund.oid_partner")));
			refund.setUser_id(MyConfig.getOidPartner(datadriven.get("refund.user_id")));
			refund.setNotify_url(datadriven.get("refund.notify_url"));
			refund.setRefund_reason(datadriven.get("refund.refund_reason"));

			if ("get".equals(datadriven.get("refund.txn_seqno"))) {
				originalOrderInfo1.setTxn_seqno(txn_seqno);
			} else {
				originalOrderInfo1.setTxn_seqno(datadriven.get("refund.txn_seqno"));
			}
			originalOrderInfo1.setTxn_seqno(txn_seqno);
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
			
			String reqJson4 = JSON.toJSONString(refund);
			Reporter.log("退款请求报文:" + reqJson4, true);
			String sign4 = SignatureUtil.getInstance().sign(datadriven.get("refund.private_key"), reqJson4);
			String[] res4 = httpclient.post(RefundUrl, reqJson4, Property.get("SIGNATURE_TYPE"), sign4);
			String resJson4 = res4[0];
			ret_code=JSONObject.parseObject(resJson4).getString("ret_code");
			ret_msg=JSONObject.parseObject(resJson4).getString("ret_msg");
			Reporter.log("退款返回报文:" + resJson4, true);
			
			
			//  2次退款
			// 二次退款
			if(!datadriven.get("refund1.private_key").equals("")){
				Refund refund1 = new Refund();
				OriginalOrderInfo originalOrderInfo2 = new OriginalOrderInfo();
				RefundOrderInfo refundOrderInfo1 = new RefundOrderInfo();
				String refundMethods00 = datadriven.get("refund1.refundMethods0");
				String refundMethods11 = datadriven.get("refund1.refundMethods1");
				String refundMethods22 = datadriven.get("refund1.refundMethods2");
				String refundMethods33 = datadriven.get("refund1.refundMethods3");
				List<RefundMethods> refundMethodss= new ArrayList<RefundMethods>();
	
				refund1.setTimestamp(MyConfig.getTimestamp(datadriven.get("refund1.timestamp")));
				refund1.setOid_partner(MyConfig.getOidPartner(datadriven.get("refund1.oid_partner")));
				refund1.setUser_id(MyConfig.getOidPartner(datadriven.get("refund1.user_id")));
				refund1.setNotify_url(datadriven.get("refund1.notify_url"));
				refund1.setRefund_reason(datadriven.get("refund1.refund_reason"));
	
				if ("get".equals(datadriven.get("refund1.txn_seqno"))) {
					originalOrderInfo2.setTxn_seqno(txn_seqno);
				} else {
					originalOrderInfo2.setTxn_seqno(datadriven.get("refund1.txn_seqno"));
				}
				
				originalOrderInfo2.setTotal_amount(datadriven.get("refund1.total_amount"));
				refund1.setOriginalOrderInfo(originalOrderInfo2);
	
				refundOrderInfo1.setRefund_seqno(MyConfig.getRefundSeqno(datadriven.get("refund1.refund_seqno")));
				refundOrderInfo1.setRefund_time(MyConfig.getRefundTime(datadriven.get("refund1.refund_time")));
				refundOrderInfo1.setPayee_id(datadriven.get("refund1.payee_id"));
				refundOrderInfo1.setPayee_type(datadriven.get("refund1.payee_type"));
				refundOrderInfo1.setPayee_accttype(datadriven.get("refund1.payee_accttype"));
				refundOrderInfo1.setRefund_amount(datadriven.get("refund1.refund_amount"));
				refund1.setRefundOrderInfo(refundOrderInfo1);
	
				if (!StringUtils.isBlank(refundMethods00)) {
					refundMethodss.add(JSON.parseObject(refundMethods00, RefundMethods.class));
				}
				if (!StringUtils.isBlank(refundMethods11)) {
					refundMethodss.add(JSON.parseObject(refundMethods11, RefundMethods.class));
				}
				if (!StringUtils.isBlank(refundMethods22)) {
					refundMethodss.add(JSON.parseObject(refundMethods22, RefundMethods.class));
				}
				if (!StringUtils.isBlank(refundMethods33)) {
					refundMethodss.add(JSON.parseObject(refundMethods33, RefundMethods.class));
				}
				refund1.setRefundMethods(refundMethodss);
				
				String reqJson5 = JSON.toJSONString(refund1);
				Reporter.log("二次退款请求报文:" + reqJson5, true);
				String sign5 = SignatureUtil.getInstance().sign(datadriven.get("refund1.private_key"), reqJson5);
				String[] res5 = httpclient.post(RefundUrl, reqJson5, Property.get("SIGNATURE_TYPE"), sign5);
				String resJson5 = res5[0];
				ret_code=JSONObject.parseObject(resJson5).getString("ret_code");
				ret_msg=JSONObject.parseObject(resJson5).getString("ret_msg");
				Reporter.log("二次退款返回报文:" + resJson5, true);
			}
			
			// 二次确认
			if(!datadriven.get("securedConfirm1.private_key").equals("")){
				SecuredConfirm securedConfirm3 = new SecuredConfirm();
				OriginalOrderInfo originalOrderInfo3 = new OriginalOrderInfo();
				ConfirmOrderInfo confirmOrderInfo3 = new ConfirmOrderInfo();
				String payeeInfo30 = datadriven.get("securedConfirm1.payeeInfo0");
				String payeeInfo31 = datadriven.get("securedConfirm1.payeeInfo1");
				String payeeInfo32 = datadriven.get("securedConfirm1.payeeInfo2");
				String payeeInfo33 = datadriven.get("securedConfirm1.payeeInfo3");
	
				List<PayeeInfo> payeeInfoList3 = new ArrayList<PayeeInfo>();
	
				securedConfirm3.setTimestamp(MyConfig.getTimestamp(datadriven.get("securedConfirm1.timestamp")));
				securedConfirm3.setOid_partner(MyConfig.getOidPartner(datadriven.get("securedConfirm1.oid_partner")));
				securedConfirm3.setUser_id(MyConfig.getUser(datadriven.get("securedConfirm1.user_id")));
				securedConfirm3.setNotify_url(datadriven.get("securedConfirm1.notify_url"));
				securedConfirm3.setConfirm_mode(datadriven.get("securedConfirm1.confirm_mode"));
	
				originalOrderInfo3.setTxn_seqno(txn_seqno);
				originalOrderInfo3.setTotal_amount(datadriven.get("securedConfirm1.total_amount"));
				securedConfirm3.setOriginalOrderInfo(originalOrderInfo3);
	
				confirmOrderInfo3.setConfirm_seqno(MyConfig.getConfirmSeqno(datadriven.get("securedConfirm1.confirm_seqno")));
				confirmOrderInfo3.setConfirm_time(MyConfig.getConfirmTime(datadriven.get("securedConfirm1.confirm_time")));
				confirmOrderInfo3.setConfirm_amount(datadriven.get("securedConfirm1.confirm_amount"));
				securedConfirm3.setConfirmOrderInfo(confirmOrderInfo3);
	
				if (!StringUtils.isBlank(payeeInfo30)) {
					payeeInfoList3.add(JSON.parseObject(payeeInfo30, PayeeInfo.class));
				}
				if (!StringUtils.isBlank(payeeInfo31)) {
					payeeInfoList3.add(JSON.parseObject(payeeInfo31, PayeeInfo.class));
				}
				if (!StringUtils.isBlank(payeeInfo32)) {
					payeeInfoList3.add(JSON.parseObject(payeeInfo32, PayeeInfo.class));
				}
				if (!StringUtils.isBlank(payeeInfo33)) {
					payeeInfoList3.add(JSON.parseObject(payeeInfo33, PayeeInfo.class));
				}
	
				securedConfirm3.setPayeeInfo(payeeInfoList3);
	
				String reqJson6 = JSON.toJSONString(securedConfirm3);
				Reporter.log("担保交易确认请求报文:" + reqJson6, true);
				String sign6 = SignatureUtil.getInstance().sign(datadriven.get("securedConfirm1.private_key"), reqJson6);
				String[] res6 = httpclient.post(securedConfirmUrl, reqJson6, Property.get("SIGNATURE_TYPE"), sign6);
				String resJson6 = res6[0];
				Reporter.log("担保交易确认返回报文:" + resJson6, true);
			}
			
			 //验证金额
			String user_A_actual=dataBaseAccessPayCore.getAMT_BALAVAL(MyConfig.getAccNO("get_user_A"));
			String user_P_actual=dataBaseAccessPayCore.getAMT_BALAVAL(MyConfig.getAccNO("get_user_P"));
			String mchcoupon_A_actual=dataBaseAccessPayCore.getAMT_BALAVAL(MyConfig.getAccNO("get_mchcoupon_A"));
			String mchcoupon_P_actual=dataBaseAccessPayCore.getAMT_BALAVAL(MyConfig.getAccNO("get_mchcoupon_P"));
			String mchassure_P_actual=dataBaseAccessPayCore.getAMT_BALAVAL(MyConfig.getAccNO("get_mchassure_P"));
			String payee_user_A_actual=dataBaseAccessPayCore.getAMT_BALAVAL(MyConfig.getAccNO("get_payee_user_A"));
			String payee_user_P_actual=dataBaseAccessPayCore.getAMT_BALAVAL(MyConfig.getAccNO("get_payee_user_P"));
			String payee_mch_A_actual=dataBaseAccessPayCore.getAMT_BALAVAL(MyConfig.getAccNO("get_payee_mch_A"));
			String payee_mch_P_actual=dataBaseAccessPayCore.getAMT_BALAVAL(MyConfig.getAccNO("get_payee_mch_P"));
	
			boolean user_A_Check=false;
			boolean user_P_Check=false;
			boolean mchcoupon_A_Check=false;
			boolean mchcoupon_P_Check=false;
			boolean mchassure_P_Check=false;
			boolean payee_user_A_Check=false;
			boolean payee_user_P_Check=false;
			boolean payee_mch_A_Check=false;
			boolean payee_mch_P_Check=false;
			
			if(user_A_actual.equals(datadriven.get("user_A"))){
				user_A_Check=true;
			}else{
				System.out.println("***********金额对比失败1 "+user_A_actual);
			}
			if(user_P_actual.equals(datadriven.get("user_P"))){
				user_P_Check=true;
			}else{
				System.out.println("***********金额对比失败2 "+user_P_actual);
			}
			if(mchcoupon_A_actual.equals(datadriven.get("mchcoupon_A"))){
				mchcoupon_A_Check=true;
			}else{
				System.out.println("***********金额对比失败3 "+mchcoupon_A_actual);
			}
			if(mchcoupon_P_actual.equals(datadriven.get("mchcoupon_P"))){
				mchcoupon_P_Check=true;
			}else{
				System.out.println("***********金额对比失败4 "+mchcoupon_P_actual);
			}
			if(payee_user_A_actual.equals(datadriven.get("payee_user_A"))){
				payee_user_A_Check=true;
			}else{
				System.out.println("***********金额对比失败5 "+payee_user_A_actual);
			}	
			if(payee_user_P_actual.equals(datadriven.get("payee_user_P"))){
				payee_user_P_Check=true;
			}else{
				System.out.println("***********金额对比失败6 "+payee_user_P_actual);
			}
			if(payee_mch_A_actual.equals(datadriven.get("payee_mch_A"))){
				payee_mch_A_Check=true;
			}else{
				System.out.println("***********金额对比失败7 "+payee_mch_A_actual);
			}
			if(payee_mch_P_actual.equals(datadriven.get("payee_mch_P"))){
				payee_mch_P_Check=true;
			}else{
				System.out.println("***********金额对比失败8 "+payee_mch_P_actual);
			}
			if(mchassure_P_actual.equals(datadriven.get("mchassure_P"))){
				mchassure_P_Check=true;
			}else{
				System.out.println("***********金额对比失败9 "+mchassure_P_actual);
			}
			
			amt_Check=(user_A_Check&&user_P_Check&&mchcoupon_A_Check&&mchcoupon_P_Check&&payee_user_A_Check&&payee_user_P_Check&&payee_mch_A_Check&&payee_mch_P_Check&&mchassure_P_Check);
			//检查支付单状态
			String PayBillState_actual=dataBaseAccessPayCore.getPayBillState(accp_txno);
			if(PayBillState_actual.equals(datadriven.get("paybill_stat"))){
				paybill_state_Check=true;
			}else{
				System.out.println("***********支付单状态对比失败1 "+PayBillState_actual);
			}
			//检查担保单状态
			String SecuredStat_actual=dataBaseAccessPayCore.getSeuredPayBillState(accp_txno);
			if(SecuredStat_actual.equals(datadriven.get("secured_stat"))){
				SeuredPaybill_state_Check=true;
			}else{
				System.out.println("***********担保单状态对比失败1 "+SecuredStat_actual);
			}
			
			String Check=String.valueOf(amt_Check)+String.valueOf(paybill_state_Check)+String.valueOf(SeuredPaybill_state_Check);
			//将返回信息写入excel
			//执行全部数据时，使用该方法
			//funcUtil.writeRet2Excel(this, "signPay_Apply_Verify",runRow,ret_code, ret_msg+oid_billno);
			//执行指定数据时，使用该方法
			System.out.println("***********Check "+Check);

			funcUtil.writeRet2Excel(this, "SecuredwithSellerComfirmRefund",ret_code, ret_msg+accp_txno+Check);
			Assert.assertTrue(amt_Check&&paybill_state_Check&&SeuredPaybill_state_Check);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			Reporter.log("##############################【xxxx测试结束】############################", true);
		}
	}

	@DataProvider(name = "SecuredwithSellerComfirmRefund")
	public Iterator<Object[]> data4SecuredwithSellerComfirmRefund() throws IOException {
		return new ExcelProvider(this, "SecuredwithSellerComfirmRefund");
	}

	@AfterClass
	public void afterClass() {
		dataBaseAccess.closeDBConnection();
	}

}