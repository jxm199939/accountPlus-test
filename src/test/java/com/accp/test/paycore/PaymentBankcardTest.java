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
import com.accp.test.bean.txn.OrderInfo;
import com.accp.test.bean.txn.PayMethods;
import com.accp.test.bean.txn.PayeeInfo;
import com.accp.test.bean.txn.PayerInfo;
import com.accp.test.bean.txn.PaymentBankcard;
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
 * @author jiangxm
 * 银行卡支付接口测试
 */

@ContextConfiguration(locations = { "/consumer.xml" })
public class PaymentBankcardTest extends AbstractTestNGSpringContextTests {

	FuncUtil funcUtil = new FuncUtil();
	DataBaseAccess dataBaseAccess = new DataBaseAccess();
	DataBaseAccessPayCore dataBaseAccessPayCore = new DataBaseAccessPayCore();
	
	boolean amt_Check=false;
	boolean paybill_state_Check=false;
	
	String accp_txno;
	String txn_seqno;
	String ret_code ;
	String ret_msg ;

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "银行卡支付接口测试", timeOut = 600000, dataProvider = "paymentBankcard")
	public void paymentBankcard(Map<String, String> datadriven) throws Exception {

		String tradeCreateUrl = Property.get("tradeCreate.url.test");
		String paymentBankcardUrl = Property.get("paymentBankcard.url.test");
		String validationSmsUrl = Property.get("validationSms.url.test");
		
		//初始化金额
		System.out.println("账户金额初始化");	
		dataBaseAccessPayCore.updateAcctinfo(MyConfig.getAccNO("get_user_A"),Float.parseFloat(datadriven.get("user_A_pre")));//付款方用户可用账户
		dataBaseAccessPayCore.updateAcctinfo(MyConfig.getAccNO("get_user_P"),Float.parseFloat(datadriven.get("user_P_pre")));//付款方用户待结算账户
		dataBaseAccessPayCore.updateAcctinfo(MyConfig.getAccNO("get_mchcoupon_A"),Float.parseFloat(datadriven.get("mchcoupon_A_pre"))); //优惠券商户可用账户
		dataBaseAccessPayCore.updateAcctinfo(MyConfig.getAccNO("get_mchcoupon_P"),Float.parseFloat(datadriven.get("mchcoupon_P_pre"))); //优惠券商户待结算账户
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
		String payeeInfo3 = datadriven.get("payeeInfo3");

		List<PayeeInfo> payeeInfoList = new ArrayList<PayeeInfo>();
		String oid_partner = MyConfig.getOidPartner(datadriven.get("paymentBankcard.oid_partner"));
		String user_id = MyConfig.getOidPartner(datadriven.get("paymentBankcard.user_id"));

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

			tradeCreate.setPayeeInfo(payeeInfoList);

			String reqJson = JSON.toJSONString(tradeCreate);
			Reporter.log("创单请求报文:" + reqJson, true);
			String sign = SignatureUtil.getInstance().sign(datadriven.get("private_key"), reqJson);
			HttpRequestSimple httpclient = new HttpRequestSimple();
			String[] res = httpclient.post(tradeCreateUrl, reqJson, Property.get("SIGNATURE_TYPE"), sign);
			String resJson =  res[0];
			ret_code=JSONObject.parseObject(resJson).getString("ret_code");
			ret_msg=JSONObject.parseObject(resJson).getString("ret_msg");
			txn_seqno = JSONObject.parseObject(resJson).getString("txn_seqno");
			accp_txno = JSONObject.parseObject(resJson).getString("accp_txno");
			Reporter.log("创单返回报文:" + resJson, true);
			Reporter.log("txn_seqno:" + txn_seqno, true);
			Reporter.log("accp_txno:" + accp_txno, true);

			// 银行卡支付
			if(!datadriven.get("paymentBankcard.Update").equals("")){

				switch (datadriven.get("paymentBankcard.UpdateType")) {
				case "1":
					dataBaseAccessPayCore.insertAcctConfiguration(datadriven.get("paymentBankcard.Update")+"'"+MyConfig.getAccNO("get_user_A")+"'");
					break;
				case "2":
					dataBaseAccessPayCore.insertAcctConfiguration(datadriven.get("paymentBankcard.Update")+"'"+MyConfig.getAccNO("get_mchcoupon_A")+"'");	
					break;	
				case "3":
					dataBaseAccessPayCore.insertAcctConfiguration(datadriven.get("paymentBankcard.Update")+"'"+MyConfig.getAccNO("get_payee_mch_P")+"'");	
					break;	
				}
			}
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
			
			String reqJson1 = JSON.toJSONString(paymentBankcard);
			Reporter.log("银行卡支付请求报文:" + reqJson1, true);
			String sign1 = SignatureUtil.getInstance().sign(datadriven.get("paymentBankcard.private_key"), reqJson1);
			//String[] res1 = httpclient.post(paymentBankcardUrl, reqJson, Property.get("SIGNATURE_TYPE"), sign1);
			String[] res1 = httpclient.post(paymentBankcardUrl, reqJson1, Property.get("SIGNATURE_TYPE"), sign1);
			String resJson1 =  res1[0];	
			ret_code=JSONObject.parseObject(resJson1).getString("ret_code");
			ret_msg=JSONObject.parseObject(resJson1).getString("ret_msg");
			Reporter.log("银行卡支付返回报文:" + resJson1, true);
			
			// 银行卡二次支付

			if(!datadriven.get("paymentBankcard1.private_key").equals("")){
				PaymentBankcard paymentBankcard1 = new PaymentBankcard();
				PayerInfo payerInfo1 = new PayerInfo();
				BankCardInfo bankCardInfo1 = new BankCardInfo();
				List<PayMethods> payMethodss = new ArrayList<PayMethods>();
				String payMethods01 = datadriven.get("paymentBankcard1.payMethods0");
				String payMethods11 = datadriven.get("paymentBankcard1.payMethods1");
				String payMethods21 = datadriven.get("paymentBankcard1.payMethods2");
				String payMethods31 = datadriven.get("paymentBankcard1.payMethods3");
				
				paymentBankcard1.setTimestamp(MyConfig.getTimestamp(datadriven.get("paymentBankcard1.timestamp")));
				paymentBankcard1.setOid_partner(MyConfig.getOidPartner(datadriven.get("paymentBankcard1.oid_partner")));
				if ("get".equals(datadriven.get("paymentBankcard1.txn_seqno"))) {
					paymentBankcard1.setTxn_seqno(txn_seqno);
				} else {
					paymentBankcard1.setTxn_seqno(datadriven.get("paymentBankcard1.txn_seqno"));
				}
				paymentBankcard1.setTotal_amount(datadriven.get("paymentBankcard1.total_amount"));
				paymentBankcard1.setRisk_item(datadriven.get("paymentBankcard1.risk_item"));
	
				payerInfo1.setUser_id(MyConfig.getOidPartner(datadriven.get("paymentBankcard1.user_id")));
				payerInfo1.setPassword(datadriven.get("paymentBankcard1.password"));
				payerInfo1.setRandom_key(datadriven.get("paymentBankcard1.random_key"));
				paymentBankcard1.setPayerInfo(payerInfo1);
				
				bankCardInfo1.setLinked_acctno(datadriven.get("paymentBankcard1.linked_acctno"));
				if ("get".equals(datadriven.get("paymentBankcard1.linked_agrtno"))){
					String linked_agrtno1 = dataBaseAccess.getAgreementNo(MyConfig.getUser(datadriven.get("user_id")),
							MyConfig.getOidPartner(datadriven.get("oid_partner")));
					bankCardInfo1.setLinked_agrtno(linked_agrtno1);
				}else{
					bankCardInfo1.setLinked_agrtno(datadriven.get("paymentBankcard1.linked_agrtno"));
				}
				bankCardInfo1.setLinked_phone(datadriven.get("paymentBankcard1.linked_phone"));
				bankCardInfo1.setLinked_acctname(datadriven.get("paymentBankcard1.linked_acctname"));
				bankCardInfo1.setId_type(datadriven.get("paymentBankcard1.id_type"));
				bankCardInfo1.setId_no(datadriven.get("paymentBankcard1.id_no"));
				paymentBankcard1.setBankCardInfo(bankCardInfo1);
				
				if (!StringUtils.isBlank(payMethods01)) {
					payMethodss.add(JSON.parseObject(payMethods01, PayMethods.class));
				}
				if (!StringUtils.isBlank(payMethods11)) {
					payMethodss.add(JSON.parseObject(payMethods11, PayMethods.class));
				}
				if (!StringUtils.isBlank(payMethods21)) {
					payMethodss.add(JSON.parseObject(payMethods21, PayMethods.class));
				}
				if (!StringUtils.isBlank(payMethods31)) {
					payMethodss.add(JSON.parseObject(payMethods31, PayMethods.class));
				}
				paymentBankcard1.setPayMethods(payMethodss);
	
				String reqJson2 = JSON.toJSONString(paymentBankcard1);
				Reporter.log("二次银行卡支付请求报文:" + reqJson2, true);
				String sign2 = SignatureUtil.getInstance().sign(datadriven.get("paymentBankcard.private_key"), reqJson2);
				String[] res2 = httpclient.post(paymentBankcardUrl, reqJson2, Property.get("SIGNATURE_TYPE"), sign2);
				String resJson2 =  res2[0];	
				ret_code=JSONObject.parseObject(resJson2).getString("ret_code");
				ret_msg=JSONObject.parseObject(resJson2).getString("ret_msg");
				Reporter.log("二次银行卡支付返回报文:" + resJson2, true);
			}
			if(!datadriven.get("paymentBankcard.Update1").equals("")){

				switch (datadriven.get("paymentBankcard.UpdateType")) {
				case "1":
					dataBaseAccessPayCore.insertAcctConfiguration(datadriven.get("paymentBankcard.Update1")+"'"+MyConfig.getAccNO("get_user_A")+"'");
					break;
				case "2":
					dataBaseAccessPayCore.insertAcctConfiguration(datadriven.get("paymentBankcard.Update1")+"'"+MyConfig.getAccNO("get_mchcoupon_A")+"'");	
					break;	
				case "3":
					dataBaseAccessPayCore.insertAcctConfiguration(datadriven.get("paymentBankcard.Update1")+"'"+MyConfig.getAccNO("get_payee_mch_P")+"'");	
					break;	
				}
			}
			 //验证金额
			String user_A_actual=dataBaseAccessPayCore.getAMT_BALAVAL(MyConfig.getAccNO("get_user_A"));
			String user_P_actual=dataBaseAccessPayCore.getAMT_BALAVAL(MyConfig.getAccNO("get_user_P"));
			String mchcoupon_A_actual=dataBaseAccessPayCore.getAMT_BALAVAL(MyConfig.getAccNO("get_mchcoupon_A"));
			String mchcoupon_P_actual=dataBaseAccessPayCore.getAMT_BALAVAL(MyConfig.getAccNO("get_mchcoupon_P"));
			String payee_user_A_actual=dataBaseAccessPayCore.getAMT_BALAVAL(MyConfig.getAccNO("get_payee_user_A"));
			String payee_user_P_actual=dataBaseAccessPayCore.getAMT_BALAVAL(MyConfig.getAccNO("get_payee_user_P"));
			String payee_mch_A_actual=dataBaseAccessPayCore.getAMT_BALAVAL(MyConfig.getAccNO("get_payee_mch_A"));
			String payee_mch_P_actual=dataBaseAccessPayCore.getAMT_BALAVAL(MyConfig.getAccNO("get_payee_mch_P"));
	
			boolean user_A_Check=false;
			boolean user_P_Check=false;
			boolean mchcoupon_A_Check=false;
			boolean mchcoupon_P_Check=false;
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
			
			amt_Check=(user_A_Check&&user_P_Check&&mchcoupon_A_Check&&mchcoupon_P_Check&&payee_user_A_Check&&payee_user_P_Check&&payee_mch_A_Check&&payee_mch_P_Check);
			//amt_Check=(user_A_Check&&user_P_Check&&mchcoupon_A_Check&&mchcoupon_P_Check&&payee_mch_A_Check&&payee_mch_P_Check);
			
			//检查支付单状态
			String PayBillState_actual=dataBaseAccessPayCore.getPayBillState(accp_txno);
			if(PayBillState_actual.equals(datadriven.get("paybill_stat"))){
				paybill_state_Check=true;
			}else{
				System.out.println("***********状态对比失败1 "+PayBillState_actual);
			}
			
			String Check=String.valueOf(amt_Check)+String.valueOf(paybill_state_Check);
			//将返回信息写入excel
			//执行全部数据时，使用该方法
			//funcUtil.writeRet2Excel(this, "PaymentBalanceTest",runRow,ret_code, ret_msg+oid_billno);
			//执行指定数据时，使用该方法
			//Reporter.log("ret_code:" + ret_code, true);
			//Reporter.log("ret_msg+accp_txno+Check:" + ret_msg+accp_txno+Check, true);
			//funcUtil.writeRet2Excel(this, "paymentBankcard",ret_code,ret_msg+accp_txno+Check);
		
			
			funcUtil.writeRet2Excel(this, "paymentBankcard",ret_code,ret_msg+accp_txno+Check);
			
			Assert.assertTrue(amt_Check&&paybill_state_Check);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			Reporter.log("##############################【xxxx测试结束】############################", true);
		}
	}

	@DataProvider(name = "paymentBankcard")
	public Iterator<Object[]> data4paymentBankcard() throws IOException {
		return new ExcelProvider(this, "paymentBankcard");
	}

	@AfterClass
	public void afterClass() {
		dataBaseAccess.closeDBConnection();
	}

}