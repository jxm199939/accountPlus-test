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
 * 担保交易确认接口测试
 */

@ContextConfiguration(locations = { "/consumer.xml" })
public class SecuredPay extends AbstractTestNGSpringContextTests {

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

	@Test(description = "担保交易支付测试", timeOut = 60000, dataProvider = "securedPay")
	public void securedPay(Map<String, String> datadriven) throws Exception {

		String tradeCreateUrl = Property.get("tradeCreate.url.test");
		String paymentBalanceUrl = Property.get("paymentBalance.url.test");
		String paymentBankcardUrl = Property.get("paymentBankcard.url.test");
		String paymentGwUrl = Property.get("paymentGw.url.test");
		String validationSmsUrl = Property.get("validationSms.url.test");
		txn_seqno=MyConfig.getTxnSeqno(datadriven.get("txn_seqno"));
		
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
		String oid_partner = MyConfig.getOidPartner(datadriven.get("oid_partner"));
		String user_id = MyConfig.getOidPartner(datadriven.get("user_id"));

		try {
			// 创单
			Reporter.log("##############################【xxxx测试开始】############################", true);
			tradeCreate.setTimestamp(MyConfig.getTimestamp(datadriven.get("timestamp")));
			tradeCreate.setOid_partner(MyConfig.getOidPartner(datadriven.get("oid_partner")));
			tradeCreate.setTxn_type(datadriven.get("txn_type"));
			tradeCreate.setUser_id(MyConfig.getOidPartner(datadriven.get("user_id")));
			tradeCreate.setUser_type(datadriven.get("user_type"));
			tradeCreate.setNotify_url(datadriven.get("notify_url"));
			tradeCreate.setReturn_url(datadriven.get("return_url"));
			tradeCreate.setPay_expire(datadriven.get("pay_expire"));
			orderInfo.setTxn_seqno(txn_seqno);
			//orderInfo.setTxn_seqno(MyConfig.getTxnSeqno(datadriven.get("txn_seqno")));
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
					String token = JSONObject.parseObject(resJson1).getString("token");
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
					String resSignatureData12= res2[1];
					String token2 = JSONObject.parseObject(resJson2).getString("token");
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
					String[] res3 = httpclient.post(paymentGwUrl, reqJson3, Property.get("SIGNATURE_TYPE"), sign3);
					String resJson3 = res3[0];
					String resSignatureData3= res3[1];
					String token3 = JSONObject.parseObject(resJson3).getString("token");
					ret_code=JSONObject.parseObject(resJson3).getString("ret_code");
					ret_msg=JSONObject.parseObject(resJson3).getString("ret_msg");
					Reporter.log("网关支付返回报文:" + resJson3, true);
					break;
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
			funcUtil.writeRet2Excel(this, "securedPay",ret_code, ret_msg+accp_txno+Check);
			
			Assert.assertTrue(amt_Check&&paybill_state_Check&&SeuredPaybill_state_Check);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			Reporter.log("##############################【xxxx测试结束】############################", true);
		}
	}

	@DataProvider(name = "securedPay")
	public Iterator<Object[]> data4securedPay() throws IOException {
		return new ExcelProvider(this, "securedPay");
	}

	@AfterClass
	public void afterClass() {
		dataBaseAccess.closeDBConnection();
	}

}