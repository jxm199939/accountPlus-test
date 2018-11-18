package com.accp.test.chnl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.accp.test.bean.acctmgr.UnlinkedacctIndApply;
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
import com.lianlian.crypt.service.IAESCryptService;
import com.tools.dataprovider.ExcelProvider;
import com.tools.utils.DataBaseAccessWL;
import com.tools.utils.DateUtil;
import com.tools.utils.FuncUtil;
import com.tools.utils.HttpRequestSimple;
import com.tools.utils.Property;
import com.tools.utils.RandomUtils;
import com.tools.utils.SignatureUtil;

@ContextConfiguration(locations = { "/consumer.xml" })
public class ProdSignAndPay extends AbstractTestNGSpringContextTests{
	@Autowired
	private IAESCryptService aesCryptService;	
	DataBaseAccessWL dataBaseAccess = new DataBaseAccessWL();
	private static Log log = LogFactory.getLog(ProdSignAndPay.class);
	FuncUtil funcUtil = new FuncUtil();
	DateUtil dateUtil = new DateUtil();
	RandomUtils randomUtils = new RandomUtils();
	boolean excelResultCheck=true;
	int row=60;
	String ret_code="";
	String ret_msg="";
	
	@BeforeClass
	public void beforeClass() throws Exception {
		Property.set();
		funcUtil.prepare4Excel(this, "signAndPayTest");
	}
	
	/**
	 * 测试用例说明：
	 * 银行卡签约支付流程
	 * @auther wanglin002
	 * /
	 */
	
	@Test(description = "accp统一支付创单签约支付测试", dataProvider = "signAndPayTest")
	public void signAndPayTest(Map<String, String> datadriven) throws Exception{

		String dt_order = dateUtil.getCurrentDateTimeStr();// 订单时间yyyyMMddHHmmss
		String no_order = "ACCPTEST" + dt_order + randomUtils.getRandom(10);// 订单号
		//String no_order ="ACCPTEST20180625213140petkou2q2p";
		String randomUserId = randomUtils.getRandom(8);//匿名用户号 
		String tradeCreateUrl = Property.get("tradeCreate.url.prod");
		String cardPayUrl = Property.get("paymentBankcard.url.prod");
		String varifyUrl = Property.get("validationSms.url.prod");
		String unlinkedUrl = Property.get("unlinkedacctIndApply.url.prod");
		TradeCreate tradeCreate = new TradeCreate();
		PaymentBankcard paymentBankCard = new PaymentBankcard();
		ValidationSms validationSms = new ValidationSms();
		UnlinkedacctIndApply unlinkedacctIndApply = new UnlinkedacctIndApply();
		String token="";
		String sign="";
		String signType ="";

		try {
			//删除签约记录，解除认证记录
			if(datadriven.get("IfNewCard").equals("Y")){		
				unlinkedacctIndApply.setTimestamp(dt_order);
				unlinkedacctIndApply.setOid_partner(datadriven.get("oid_partner"));
				unlinkedacctIndApply.setUser_id(datadriven.get("user_id"));
				unlinkedacctIndApply.setTxn_seqno("unlink"+no_order);
				unlinkedacctIndApply.setTxn_time(dt_order);
				unlinkedacctIndApply.setNotify_url(datadriven.get("notify_url"));
				unlinkedacctIndApply.setLinked_acctno(datadriven.get("linked_acctno"));
				unlinkedacctIndApply.setPassword(datadriven.get("password"));
				unlinkedacctIndApply.setRandom_key(datadriven.get("random_key"));

				String reqJson = JSON.toJSONString(unlinkedacctIndApply);
				log.info("【解约】请求报文:" + reqJson);
				sign = SignatureUtil.getInstance().sign(datadriven.get("private_key"), reqJson);
				signType = datadriven.get("signType");
				HttpRequestSimple httpclient1 = new HttpRequestSimple();
				String rsp = httpclient1.postSendHttp(unlinkedUrl, reqJson, signType, sign);
				log.info("【解约】返回报文:" + rsp);}
			
			
			OrderInfo orderInfo = new OrderInfo();
			String payeeInfo0 = datadriven.get("payeeInfo0");
			List<PayeeInfo> payeeInfoList = new ArrayList<PayeeInfo>();
			
			log.info("##############################【银行卡签约支付测试开始】############################");
			tradeCreate.setTimestamp(dt_order);
			tradeCreate.setOid_partner(datadriven.get("oid_partner"));
			tradeCreate.setTxn_type(datadriven.get("txn_type"));
			tradeCreate.setUser_id(datadriven.get("user_id"));
			tradeCreate.setUser_type(datadriven.get("user_type"));
			tradeCreate.setNotify_url(datadriven.get("notify_url"));
			tradeCreate.setReturn_url(datadriven.get("return_url"));
			tradeCreate.setPay_expire(datadriven.get("pay_expire"));

			orderInfo.setTxn_seqno(no_order);
			orderInfo.setTxn_time(dt_order);
			orderInfo.setTotal_amount(datadriven.get("total_amount"));
			orderInfo.setOrder_info(datadriven.get("order_info"));
			orderInfo.setGoods_name(datadriven.get("goods_name"));
			orderInfo.setGoods_url(datadriven.get("goods_url"));
			tradeCreate.setOrderInfo(orderInfo);

			if (!StringUtils.isBlank(payeeInfo0)) {
				payeeInfoList.add(JSON.parseObject(payeeInfo0, PayeeInfo.class));
			}
			tradeCreate.setPayeeInfo(payeeInfoList);

			String reqJson1 = JSON.toJSONString(tradeCreate);
			log.info("【创单】申请报文:" + reqJson1);
			sign = SignatureUtil.getInstance().sign(datadriven.get("private_key"), reqJson1);
			signType = datadriven.get("signType");
			HttpRequestSimple httpclient = new HttpRequestSimple();
			String rsp1 = httpclient.postSendHttp(tradeCreateUrl, reqJson1, signType, sign);
			log.info("【创单】返回报文:" + rsp1);
			JSONObject json_rsp1 = JSON.parseObject(rsp1);
			ret_code = json_rsp1.getString("ret_code");
			ret_msg = json_rsp1.getString("ret_msg");
			if(ret_code.equals("0000")){
				log.info("##############################【创单成功，进入银行卡支付】############################");
				String payerInfo0 = datadriven.get("payerInfo0");
				PayerInfo payerInfo = new PayerInfo();
				payerInfo.setUser_id(datadriven.get("user_id"));
				payerInfo.setPassword(datadriven.get("password"));
				payerInfo.setRandom_key(datadriven.get("random_key"));
				BankCardInfo bankCardInfo = new BankCardInfo();
				bankCardInfo.setLinked_agrtno(datadriven.get("linked_agrtno"));
				bankCardInfo.setLinked_acctno(datadriven.get("linked_acctno"));
				bankCardInfo.setLinked_acctname(datadriven.get("linked_acctname"));
				bankCardInfo.setLinked_phone(datadriven.get("linked_phone"));
				bankCardInfo.setCvv2(datadriven.get("cvv2"));
				bankCardInfo.setValid_thru(datadriven.get("valid"));
				bankCardInfo.setId_type(datadriven.get("id_type"));
				bankCardInfo.setId_no(datadriven.get("id_no"));
				
				String payMethods = datadriven.get("payMethods");
				List<PayMethods> payMethodsList = new ArrayList<PayMethods>();
				payMethodsList.add(JSON.parseObject(payMethods, PayMethods.class));
				
				paymentBankCard.setTimestamp(dt_order);
				paymentBankCard.setOid_partner(datadriven.get("oid_partner"));
				paymentBankCard.setTxn_seqno(no_order);
				paymentBankCard.setTotal_amount(datadriven.get("total_amountCard"));
				paymentBankCard.setRisk_item(datadriven.get("risk_item"));
				paymentBankCard.setPayerInfo(payerInfo);
				paymentBankCard.setBankCardInfo(bankCardInfo);
				paymentBankCard.setPayMethods(payMethodsList);
							
				String reqJson2 = JSON.toJSONString(paymentBankCard);
				log.info("【银行卡支付申请】请求报文:" + reqJson2);
				sign = SignatureUtil.getInstance().sign(datadriven.get("private_key"), reqJson2);
				signType = datadriven.get("signType");
				String rsp2 = httpclient.postSendHttp(cardPayUrl, reqJson2, signType, sign);
				log.info("【银行卡支付申请】返回报文:" + rsp2);
				JSONObject json_rsp2 = JSON.parseObject(rsp2);
				ret_code = json_rsp2.getString("ret_code");
				ret_msg = json_rsp2.getString("ret_msg");
				token = json_rsp2.getString("token");
				
				if(ret_code.equals("0000")){
					log.info("##############################【银行卡支付成功】############################");
				}
				
				else if(ret_code.equals("8888")){
					log.info("##############################【银行卡支付申请成功，进入支付验证】############################");
					System.out.println("请输入验证码:\n");
					String Sms = new Scanner(System.in).nextLine();
					validationSms.setTimestamp(dt_order);
					validationSms.setOid_partner(datadriven.get("oid_partner"));
					validationSms.setPayer_type(datadriven.get("payer_type"));
					validationSms.setPayer_id(datadriven.get("user_id"));
					validationSms.setTxn_seqno(no_order);
					validationSms.setTotal_amount(datadriven.get("total_amount"));
					validationSms.setToken(token);
					validationSms.setVerify_code(Sms);
					
					String reqJson3 = JSON.toJSONString(validationSms);
					log.info("【银行卡支付验证】请求报文:" + reqJson3);
					sign = SignatureUtil.getInstance().sign(datadriven.get("private_key"), reqJson3);
					signType = datadriven.get("signType");
					String rsp3 = httpclient.postSendHttp(varifyUrl, reqJson3, signType, sign);
					log.info("【银行卡支付验证】返回报文:" + rsp3);
					JSONObject json_rsp3 = JSON.parseObject(rsp3);
					ret_code = json_rsp3.getString("ret_code");
					ret_msg = json_rsp3.getString("ret_msg");
					if(ret_code.equals("0000")){	
						log.info("##############################【银行卡支付验证成功，交易结束】############################");
					}
					else {
						log.info("##############################【银行卡支付验证异常】，异常返回码："+ret_code+"############################");
					}
					
							
				}else {
					log.info("##############################【银行卡支付申请异常】，异常返回码："+ret_code+"############################");
				}											
				
			}
			else {
				log.info("##############################【创单异常】，异常返回码："+ret_code+"############################");
			}
			
			 
		} catch (Exception e) {
			e.printStackTrace();
		}
	    finally {
	    	
	    	//将返回信息写入excel
			funcUtil.writeRet2Excel(this, "signAndPayTest",row,ret_code, ret_msg);
	    	//excel数据比对校验
			excelResultCheck=funcUtil.excelResultCheck(this, "signAndPayTest");
			Assert.assertTrue(excelResultCheck);			
			log.info("##############################【银行卡签约支付测试结束】############################");
	}
}	
	@DataProvider(name = "signAndPayTest")
	public Iterator<Object[]> data4signAndPay() throws IOException {
		//excel名称跟类名一致，sheet名称跟方法名一致
		return new ExcelProvider(this, "signAndPayTest",row);
	}

	@AfterClass
	public void afterClass() {
		dataBaseAccess.closeDBConnection();
	}

}
