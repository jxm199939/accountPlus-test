package com.accp.test.transfer;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
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
import com.accp.test.bean.txn.OrderInfo;
import com.accp.test.bean.txn.PayeeInfo;
import com.accp.test.bean.txn.PayerInfo;
import com.accp.test.bean.txn.Transfer;
import com.accp.test.bean.txn.ValidationSms;
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
public class TransferTest1 extends AbstractTestNGSpringContextTests{
	@Autowired
	private IAESCryptService aesCryptService;	
	DataBaseAccessWL dataBaseAccess = new DataBaseAccessWL();
	ValidationSms validationSms = new ValidationSms();
	private static Log log = LogFactory.getLog(Transfer.class);
	FuncUtil funcUtil = new FuncUtil();
	DateUtil dateUtil = new DateUtil();
	RandomUtils randomUtils = new RandomUtils();
	boolean dataResultCheck,payBillCheck,payBillSerialCheck,finSerialCheck,excelResultCheck;
	boolean payerAssert,payeeAssert,DZAssert,signAssert,signAssert1;
	int row=3;
	//int row =108;
	String ret_code="";
	String ret_msg="";
	
	@BeforeClass
	public void beforeClass() throws Exception {
		Property.set();
		funcUtil.prepare4Excel(this, "transferTest");
	}
	
	/**
	 * 测试用例说明：
	 * 代发流程
	 * @author wanglin002
	 * /
	 */
	//threadPoolSize = 2, invocationCount = 2,
	@Test(description = "accp代发测试", timeOut = 60000, dataProvider = "transferTest")
	public void transferTest(Map<String, String> datadriven) throws Exception{

		String dt_order = dateUtil.getCurrentDateTimeStr();// 订单时间yyyyMMddHHmmss
		//String dt_order = "2016";// 订单时间yyyyMMddHHmmss
		String no_order = "ACCPTEST" + dt_order + randomUtils.getRandom(10);// 订单号
		//String no_order="ACCPTEST20180625174452g5bme33h4";
		String transferUrl = Property.get("transfer.url.test");
		String varifyUrl = Property.get("validationSms.url.test");
		Transfer transfer = new Transfer();
		String token="";
		String payBillNo ="";
		String payerUserNo="";
		String payeeUserNo="";
		String payerAcctNo="";
		String payeeAcctNo="";

		try {		
						
			
			OrderInfo orderInfo = new OrderInfo();
			PayerInfo payerInfo = new PayerInfo();
			PayeeInfo payeeInfo = new PayeeInfo();
			
			
			log.info("##############################【进入代发测试】############################");

			orderInfo.setTxn_seqno(no_order);
			orderInfo.setTxn_time(dt_order);
			orderInfo.setTotal_amount(datadriven.get("total_amount"));
			orderInfo.setTxn_purpose(datadriven.get("txn_purpose"));
			orderInfo.setOrder_info(datadriven.get("order_info"));
			orderInfo.setPostscript(datadriven.get("postscript"));
			
			payerInfo.setPayer_type(datadriven.get("payer_type"));
			payerInfo.setPayer_id(datadriven.get("payer_id"));
			payerInfo.setPayer_accttype(datadriven.get("payer_accttype"));
			payerInfo.setPassword(datadriven.get("password"));
			payerInfo.setRandom_key(datadriven.get("random_key"));
			
			payeeInfo.setPayee_type(datadriven.get("payee_type"));
			payeeInfo.setPayee_id(datadriven.get("payee_id"));
			payeeInfo.setPayee_accttype(datadriven.get("payee_accttype"));
			payeeInfo.setBank_acctno(datadriven.get("bank_acctno"));
			payeeInfo.setBank_acctname(datadriven.get("bank_acctname"));
			payeeInfo.setBank_code(datadriven.get("bank_code"));
			payeeInfo.setCnaps_code(datadriven.get("cnaps_code"));
			
			transfer.setOrderInfo(orderInfo);
			transfer.setPayerInfo(payerInfo);
			transfer.setPayeeInfo(payeeInfo);
			transfer.setTimestamp(dt_order);
			transfer.setOid_partner(datadriven.get("oid_partner"));
			transfer.setNotify_url(datadriven.get("notify_url"));
			transfer.setPay_expire(datadriven.get("pay_expire"));
			transfer.setFunds_flag(datadriven.get("funds_flag"));
			String reqJson = JSON.toJSONString(transfer);
						
			log.info("【代发】申请报文:" + reqJson);
			String sign = SignatureUtil.getInstance().sign(datadriven.get("private_key"), reqJson);
			String signType = datadriven.get("signType");
			HttpRequestSimple httpclient = new HttpRequestSimple();
			String[] rsp = httpclient.post(transferUrl, reqJson, Property.get("SIGNATURE_TYPE"), sign);
			String repJson =  rsp[0];
			String resSignatureData = rsp[1];
			log.info("【代发】返回报文:" + repJson);
			JSONObject json_rsp = JSON.parseObject(repJson);
			ret_code = json_rsp.getString("ret_code");
			ret_msg = json_rsp.getString("ret_msg");
			payBillNo = json_rsp.getString("accp_txno");	
			token = json_rsp.getString("token");
			// 验签
			signAssert = SignatureUtil.getInstance().checksign(Property.get("YT_RSA_PUBLIC"), repJson, resSignatureData);
			// 验签	
			//解锁数据
/*			if(!StringUtils.isBlank(datadriven.get("unlock_data")))
			{
				dataBaseAccess.updateAccountConfiguration(datadriven.get("unlock_data"));
			}*/
									
			if(ret_code.equals("0000")){
				log.info("##############################【代发成功】############################");				
			}
			else if(ret_code.equals("8888")){
				log.info("##############################【代发申请成功，进入代发验证】############################");
				String Sms = dataBaseAccess.getSms(dataBaseAccess.getRegPhone(aesCryptService,datadriven.get("payer_id"), datadriven.get("oid_partner")));
				//String Sms = "123456";
				validationSms.setTimestamp(dt_order);
				validationSms.setOid_partner(datadriven.get("oid_partner"));
				validationSms.setPayer_type(datadriven.get("payer_type"));
				validationSms.setPayer_id(datadriven.get("payer_id"));
				validationSms.setTxn_seqno(no_order);
				validationSms.setTotal_amount(datadriven.get("total_amount"));
				validationSms.setToken(token);
				validationSms.setVerify_code(Sms);
				
				String reqJson1 = JSON.toJSONString(validationSms);
				log.info("【代发验证】请求报文:" + reqJson1);
				//sign = GenerateSign.genSign(JSON.parseObject(reqJson3), datadriven.get("private_key"));
				sign = SignatureUtil.getInstance().sign(datadriven.get("private_key"), reqJson1);
				signType = datadriven.get("signType");
				String[] rsp1 = httpclient.post(varifyUrl, reqJson1, signType, sign);
				String repJson1 =  rsp1[0];
				String resSignatureData1 = rsp1[1];
				log.info("【代发验证】返回报文:" + repJson1);
				JSONObject json_rsp1 = JSON.parseObject(repJson1);
				ret_code = json_rsp1.getString("ret_code");
				ret_msg = json_rsp1.getString("ret_msg");
				payBillNo = json_rsp1.getString("accp_txno");	
				token = json_rsp1.getString("token");
				// 验签
				signAssert1 = SignatureUtil.getInstance().checksign(Property.get("YT_RSA_PUBLIC"), repJson1, resSignatureData1);
				
				if(ret_code.equals("0000")){	
					log.info("##############################【代发验证成功，交易结束】############################");
				}
				else {
					log.info("##############################【代发验证异常】，异常返回码："+ret_code+"############################");
				}
				
			}
			else {
				log.info("##############################【代发异常】，异常返回码："+ret_code+"############################");
				}
			} catch (Exception e) {
			e.printStackTrace();
		}
	    finally {
	    	
	    	//将返回信息写入excel
			funcUtil.writeRet2Excel(this, "transferTest",row,ret_code, ret_msg);
	    	//excel数据比对校验
			excelResultCheck=funcUtil.excelResultCheck(this,"transferTest");
			//对支付单 支付流水 付款流水 卡签约信息 进行校验
			payBillCheck = dataBaseAccess.payBillCheck(payBillNo, datadriven.get("oid_partner"), datadriven.get("total_amount"), datadriven.get("biz_code"), datadriven.get("prod_code"),"ONLINE", datadriven.get("paybillStat"), "CNY");		
			payBillSerialCheck = dataBaseAccess.payBillSerialCheck(datadriven.get("total_amount"), payBillNo, "PAYMENT", datadriven.get("sreialState"), "CNY", "ONLINE", datadriven.get("payer_id"), payerUserNo, datadriven.get("payer_accttype")+"_AVAILABLE", payerAcctNo, datadriven.get("pyerTxStat"), "BALANCE", datadriven.get("payee_id"), payeeUserNo, datadriven.get("payee_accttype")+"_AVAILABLE", payeeAcctNo, datadriven.get("pyeeTxStat"), "BALANCE");
			dataResultCheck = payBillCheck&&payBillSerialCheck;
			Assert.assertTrue(signAssert&&signAssert1);
			
			log.info("##############################【代发测试结束】############################");
	}
}	
	@DataProvider(name = "transferTest")
	public Iterator<Object[]> data4transferTest() throws IOException {
		//excel名称跟类名一致，sheet名称跟方法名一致
		return new ExcelProvider(this, "transferTest",row);
	}

	@AfterClass
	public void afterClass() {
		dataBaseAccess.closeDBConnection();
	}

}
