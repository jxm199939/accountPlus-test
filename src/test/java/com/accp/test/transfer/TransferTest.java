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
public class TransferTest extends AbstractTestNGSpringContextTests{
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
	
	@Test(description = "accp代发测试", timeOut = 60000, dataProvider = "transferTest")
	public void transferTest(Map<String, String> datadriven) throws Exception{

		String dt_order = dateUtil.getCurrentDateTimeStr();// 订单时间yyyyMMddHHmmss
		String no_order = "ACCPTEST" + dt_order + randomUtils.getRandom(10);// 订单号
		//String no_order = "ACCPTEST11111111111111";// 订单号
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
			
			if(datadriven.get("payer_type").equals("MERCHANT"))
			{
				payerAcctNo = dataBaseAccess.getAcctNo(datadriven.get("payer_id"), datadriven.get("payer_accttype")+"_AVAILABLE");
			}
			else {
				payerUserNo = dataBaseAccess.getUserNo(datadriven.get("payer_id"));
				payerAcctNo = dataBaseAccess.getAcctNo(payerUserNo, datadriven.get("payer_accttype")+"_AVAILABLE");
			}
			
			if(datadriven.get("payee_type").equals("USER")){
				payeeUserNo = dataBaseAccess.getUserNo(datadriven.get("payee_id"));
				payeeAcctNo = dataBaseAccess.getAcctNo(payeeUserNo, datadriven.get("payee_accttype")+"_AVAILABLE");
			}
			else {
				payeeAcctNo = dataBaseAccess.getAcctNo(datadriven.get("payee_id"), datadriven.get("payee_accttype")+"_AVAILABLE");
			}
			String DZAcctNo = dataBaseAccess.getAcctNo(datadriven.get("oid_partner"), "MCHOWN_AVAILABLE");
			
			//更新商户用户账户状态
			if(!StringUtils.isBlank(datadriven.get("MerchantState")))
			{
				dataBaseAccess.updateTraderState(datadriven.get("oid_partner"),datadriven.get("MerchantState"));
			}
			if(!StringUtils.isBlank(datadriven.get("PayerState")))
			{
				dataBaseAccess.updateUserState(datadriven.get("payer_id"), datadriven.get("PayerState"));
			}
			if(!StringUtils.isBlank(datadriven.get("PayeeState")))
			{
				dataBaseAccess.updateUserState(datadriven.get("payee_id"), datadriven.get("PayeeState"));
			}
			if(!StringUtils.isBlank(datadriven.get("payeeMerState")))
			{
				dataBaseAccess.updateTraderState(datadriven.get("payee_id"), datadriven.get("payeeMerState"));
			}
			if(!StringUtils.isBlank(datadriven.get("PayerAccountState")))
			{
				dataBaseAccess.updateAcctState(payerAcctNo, datadriven.get("PayerAccountState"));
			}
			if(!StringUtils.isBlank(datadriven.get("PayeeAccountState")))
			{
				dataBaseAccess.updateAcctState(payeeAcctNo, datadriven.get("PayeeAccountState"));
			}
			if(!StringUtils.isBlank(datadriven.get("DZAccountState")))
			{
				dataBaseAccess.updateAcctState(DZAcctNo, datadriven.get("DZAccountState"));
			}
			//更改账户余额
			if(!StringUtils.isBlank(datadriven.get("USEROWN_AVAILABLE_balance")))
			{
				dataBaseAccess.updateAccountConfiguration(datadriven.get("USEROWN_AVAILABLE_balance")+"'"+payerUserNo+"'");			
			}
			if(!StringUtils.isBlank(datadriven.get("USEROWN_PSETTLE_balance")))
			{
				dataBaseAccess.updateAccountConfiguration(datadriven.get("USEROWN_PSETTLE_balance")+"'"+payerUserNo+"'");			
			}
			if(!StringUtils.isBlank(datadriven.get("MCHOWN_AVAILABLE_balance")))
			{
				dataBaseAccess.updateAccountConfiguration(datadriven.get("MCHOWN_AVAILABLE_balance")+"'"+datadriven.get("oid_partner")+"'");			
			}
			if(!StringUtils.isBlank(datadriven.get("MCHOWN_PSETTLE_balance")))
			{
				dataBaseAccess.updateAccountConfiguration(datadriven.get("MCHOWN_PSETTLE_balance")+"'"+datadriven.get("oid_partner")+"'");			
			}
/*			//锁表数据
			if(!StringUtils.isBlank(datadriven.get("lock_data")))
			{
				dataBaseAccess.lockAccountConfiguration(datadriven.get("lock_data"));
				dataBaseAccess.updateAccountConfiguration(datadriven.get("USEROWN_AVAILABLE_balance"));
			}*/
			
			
			OrderInfo orderInfo = new OrderInfo();
			PayerInfo payerInfo = new PayerInfo();
			PayeeInfo payeeInfo = new PayeeInfo();
			
			
			log.info("##############################【进入代发测试】############################");

			orderInfo.setTxn_seqno(no_order);
			orderInfo.setTxn_time(dt_order);
			//orderInfo.setTxn_time("20180000");
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
			
			
			//付款方账户代发前余额	        
	        long Payer_AMT_BALCUR_before = Long.parseLong(dataBaseAccess.getBalance(payerAcctNo, "AMT_BALCUR"));
	        long Payer_AMT_BALAVAL_before = Long.parseLong(dataBaseAccess.getBalance(payerAcctNo, "AMT_BALAVAL"));
	        long Payer_AMT_BALFRZ_before = Long.parseLong(dataBaseAccess.getBalance(payerAcctNo, "AMT_BALFRZ"));
	        //收款方账户代发前余额 
	        long Payee_AMT_BALCUR_before = Long.parseLong(dataBaseAccess.getBalance(payeeAcctNo, "AMT_BALCUR"));
	        long Payee_AMT_BALAVAL_before = Long.parseLong(dataBaseAccess.getBalance(payeeAcctNo, "AMT_BALAVAL"));
	        long Payee_AMT_BALFRZ_before = Long.parseLong(dataBaseAccess.getBalance(payeeAcctNo, "AMT_BALFRZ"));
	        //垫资账户代发前余额
	        long DZ_AMT_BALCUR_before = Long.parseLong(dataBaseAccess.getBalance(DZAcctNo, "AMT_BALCUR"));
	        long DZ_AMT_BALAVAL_before = Long.parseLong(dataBaseAccess.getBalance(DZAcctNo, "AMT_BALAVAL"));
	        long DZ_AMT_BALFRZ_before = Long.parseLong(dataBaseAccess.getBalance(DZAcctNo, "AMT_BALFRZ"));	
			
			log.info("代发前--付款方现金账户:"+payerAcctNo+"总余额:"+Payer_AMT_BALCUR_before+"可用余额："+Payer_AMT_BALAVAL_before+"冻结余额"+Payer_AMT_BALFRZ_before);
	        log.info("代发前--垫资方现金账户:"+DZAcctNo+"总余额:"+DZ_AMT_BALCUR_before+"可用余额："+DZ_AMT_BALAVAL_before+"冻结余额"+DZ_AMT_BALFRZ_before);
	        log.info("代发前--收款方现金账户:"+payeeAcctNo+"总余额:"+Payee_AMT_BALCUR_before+"可用余额："+Payee_AMT_BALAVAL_before+"冻结余额"+Payee_AMT_BALFRZ_before);
			
			
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
			log.info(repJson);
			log.info(resSignatureData);
			// 验签
			signAssert = SignatureUtil.getInstance().checksign(Property.get("YT_RSA_PUBLIC"), repJson, resSignatureData);
			// 验签	
			//解锁数据
/*			if(!StringUtils.isBlank(datadriven.get("unlock_data")))
			{
				dataBaseAccess.updateAccountConfiguration(datadriven.get("unlock_data"));
			}*/
						
			  //付款方代发消费后余额
	        long Payer_AMT_BALCUR_after = Long.parseLong(dataBaseAccess.getBalance(payerAcctNo, "AMT_BALCUR"));
	        long Payer_AMT_BALAVAL_after = Long.parseLong(dataBaseAccess.getBalance(payerAcctNo, "AMT_BALAVAL"));
	        long Payer_AMT_BALFRZ_after = Long.parseLong(dataBaseAccess.getBalance(payerAcctNo, "AMT_BALFRZ"));
	        //收款方代发消费后余额 
	        long Payee_AMT_BALCUR_after = Long.parseLong(dataBaseAccess.getBalance(payeeAcctNo, "AMT_BALCUR"));
	        long Payee_AMT_BALAVAL_after = Long.parseLong(dataBaseAccess.getBalance(payeeAcctNo, "AMT_BALAVAL"));
	        long Payee_AMT_BALFRZ_after = Long.parseLong(dataBaseAccess.getBalance(payeeAcctNo, "AMT_BALFRZ"));
	        //垫资账户代发后余额
	        long DZ_AMT_BALCUR_after = Long.parseLong(dataBaseAccess.getBalance(DZAcctNo, "AMT_BALCUR"));
	        long DZ_AMT_BALAVAL_after = Long.parseLong(dataBaseAccess.getBalance(DZAcctNo, "AMT_BALAVAL"));
	        long DZ_AMT_BALFRZ_after = Long.parseLong(dataBaseAccess.getBalance(DZAcctNo, "AMT_BALFRZ"));
	        //付款方代发前后余额差
	        long Payer_AMT_BALCUR_change = Payer_AMT_BALCUR_before - Payer_AMT_BALCUR_after;
	        long Payer_AMT_BALAVAL_change = Payer_AMT_BALAVAL_before - Payer_AMT_BALAVAL_after;
	        long Payer_AMT_BALFRZ_change = Payer_AMT_BALFRZ_before - Payer_AMT_BALFRZ_after;
	        //收款方代发前后余额差
	        long Payee_AMT_BALCUR_change = Payee_AMT_BALCUR_after - Payee_AMT_BALCUR_before;
	        long Payee_AMT_BALAVAL_change = Payee_AMT_BALAVAL_after - Payee_AMT_BALAVAL_before;
	        long Payee_AMT_BALFRZ_change = Payee_AMT_BALFRZ_after - Payee_AMT_BALFRZ_before;
	        //垫资方代发前后余额差
	        long DZ_AMT_BALCUR_change = DZ_AMT_BALCUR_before - DZ_AMT_BALCUR_after;
	        long DZ_AMT_BALAVAL_change = DZ_AMT_BALAVAL_before - DZ_AMT_BALAVAL_after;
	        long DZ_AMT_BALFRZ_change = DZ_AMT_BALFRZ_before - DZ_AMT_BALFRZ_after;
	        
			
			if(ret_code.equals("0000")){
				log.info("##############################【代发成功】############################");				
				
				log.info("代发后--付款方现金账户:"+payerAcctNo+"总余额:"+Payer_AMT_BALCUR_after+"可用余额："+Payer_AMT_BALAVAL_after+"冻结余额"+Payer_AMT_BALFRZ_after);
				log.info("代发后--垫资方现金账户:"+DZAcctNo+"总余额:"+DZ_AMT_BALCUR_after+"可用余额："+DZ_AMT_BALAVAL_after+"冻结余额"+DZ_AMT_BALFRZ_after);
				log.info("代发后--收款方现金账户:"+payeeAcctNo+"总余额:"+Payee_AMT_BALCUR_after+"可用余额："+Payee_AMT_BALAVAL_after+"冻结余额"+Payee_AMT_BALFRZ_after);
				
				log.info("代发后--付款方现金账户:"+payerAcctNo+"总余额差:"+Payer_AMT_BALCUR_change+"可用余额差："+Payer_AMT_BALAVAL_change+"冻结余额差"+Payer_AMT_BALFRZ_change);
				log.info("代发后--垫资方现金账户:"+DZAcctNo+"总余额差:"+DZ_AMT_BALCUR_change+"可用余额差："+DZ_AMT_BALAVAL_change+"冻结余额差"+DZ_AMT_BALFRZ_change);
				log.info("代发后--收款方现金账户:"+payeeAcctNo+"总余额差:"+Payee_AMT_BALCUR_change+"可用余额差："+Payee_AMT_BALAVAL_change+"冻结余额差"+Payee_AMT_BALFRZ_change);

		      		       		        
		        if(((double)Payer_AMT_BALCUR_change/1000)==Double.valueOf(datadriven.get("total_amount")) && ((double)Payer_AMT_BALAVAL_change/1000)==Double.valueOf(datadriven.get("total_amount")) && Payer_AMT_BALFRZ_change==0 ){
		        	payerAssert=true;
				}
		        
		        if(((double)Payee_AMT_BALCUR_change/1000)==Double.valueOf(datadriven.get("total_amount")) && ((double)Payee_AMT_BALAVAL_change/1000)==Double.valueOf(datadriven.get("total_amount")) && Payee_AMT_BALFRZ_change==0 ){
		        	payeeAssert=true;
				}
/*		        if((double)(Payer_AMT_BALAVAL_change/1000)<Double.valueOf(datadriven.get("total_amount")) && datadriven.get("funds_flag").equals("Y") && DZ_AMT_BALAVAL_change==(Double.valueOf(datadriven.get("total_amount"))-Payer_AMT_BALCUR_before)){
		        	DZAssert=true;
		        }*/
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
				log.info(repJson1);
				log.info(resSignatureData1);
				// 验签
				signAssert1 = SignatureUtil.getInstance().checksign(Property.get("YT_RSA_PUBLIC"), repJson1, resSignatureData1);
				
				if(ret_code.equals("0000")){	
					log.info("##############################【代发验证成功，交易结束】############################");
					
					  //付款方代发消费后余额
			        Payer_AMT_BALCUR_after = Long.parseLong(dataBaseAccess.getBalance(payerAcctNo, "AMT_BALCUR"));
			        Payer_AMT_BALAVAL_after = Long.parseLong(dataBaseAccess.getBalance(payerAcctNo, "AMT_BALAVAL"));
			        Payer_AMT_BALFRZ_after = Long.parseLong(dataBaseAccess.getBalance(payerAcctNo, "AMT_BALFRZ"));
			        //收款方代发消费后余额 
			        Payee_AMT_BALCUR_after = Long.parseLong(dataBaseAccess.getBalance(payeeAcctNo, "AMT_BALCUR"));
			        Payee_AMT_BALAVAL_after = Long.parseLong(dataBaseAccess.getBalance(payeeAcctNo, "AMT_BALAVAL"));
			        Payee_AMT_BALFRZ_after = Long.parseLong(dataBaseAccess.getBalance(payeeAcctNo, "AMT_BALFRZ"));
			        //垫资账户代发后余额
			        DZ_AMT_BALCUR_after = Long.parseLong(dataBaseAccess.getBalance(DZAcctNo, "AMT_BALCUR"));
			        DZ_AMT_BALAVAL_after = Long.parseLong(dataBaseAccess.getBalance(DZAcctNo, "AMT_BALAVAL"));
			        DZ_AMT_BALFRZ_after = Long.parseLong(dataBaseAccess.getBalance(DZAcctNo, "AMT_BALFRZ"));
			        //付款方代发前后余额差
			        Payer_AMT_BALCUR_change = Payer_AMT_BALCUR_before - Payer_AMT_BALCUR_after;
			        Payer_AMT_BALAVAL_change = Payer_AMT_BALAVAL_before - Payer_AMT_BALAVAL_after;
			        Payer_AMT_BALFRZ_change = Payer_AMT_BALFRZ_before - Payer_AMT_BALFRZ_after;
			        //收款方代发前后余额差
			        Payee_AMT_BALCUR_change = Payee_AMT_BALCUR_after - Payee_AMT_BALCUR_before;
			        Payee_AMT_BALAVAL_change = Payee_AMT_BALAVAL_after - Payee_AMT_BALAVAL_before;
			        Payee_AMT_BALFRZ_change = Payee_AMT_BALFRZ_after - Payee_AMT_BALFRZ_before;
			        //垫资方代发前后余额差
			        DZ_AMT_BALCUR_change = DZ_AMT_BALCUR_before - DZ_AMT_BALCUR_after;
			        DZ_AMT_BALAVAL_change = DZ_AMT_BALAVAL_before - DZ_AMT_BALAVAL_after;
			        DZ_AMT_BALFRZ_change = DZ_AMT_BALFRZ_before - DZ_AMT_BALFRZ_after;

					
					log.info("代发后--付款方现金账户:"+payerAcctNo+"总余额:"+Payer_AMT_BALCUR_after+"可用余额："+Payer_AMT_BALAVAL_after+"冻结余额"+Payer_AMT_BALFRZ_after);
					log.info("代发后--垫资方现金账户:"+DZAcctNo+"总余额:"+DZ_AMT_BALCUR_after+"可用余额："+DZ_AMT_BALAVAL_after+"冻结余额"+DZ_AMT_BALFRZ_after);
					log.info("代发后--收款方现金账户:"+payeeAcctNo+"总余额:"+Payee_AMT_BALCUR_after+"可用余额："+Payee_AMT_BALAVAL_after+"冻结余额"+Payee_AMT_BALFRZ_after);
					
					log.info("代发后--付款方现金账户:"+payerAcctNo+"总余额差:"+Payer_AMT_BALCUR_change+"可用余额差："+Payer_AMT_BALAVAL_change+"冻结余额差"+Payer_AMT_BALFRZ_change);
					log.info("代发后--垫资方现金账户:"+DZAcctNo+"总余额差:"+DZ_AMT_BALCUR_change+"可用余额差："+DZ_AMT_BALAVAL_change+"冻结余额差"+DZ_AMT_BALFRZ_change);
					log.info("代发后--收款方现金账户:"+payeeAcctNo+"总余额差:"+Payee_AMT_BALCUR_change+"可用余额差："+Payee_AMT_BALAVAL_change+"冻结余额差"+Payee_AMT_BALFRZ_change);

			        if(((double)Payer_AMT_BALCUR_change/1000)==Double.valueOf(datadriven.get("total_amount")) && ((double)Payer_AMT_BALAVAL_change/1000)==Double.valueOf(datadriven.get("total_amount")) && Payer_AMT_BALFRZ_change==0 ){
			        	payerAssert=true;
					}
			        
			        if(((double)Payee_AMT_BALCUR_change/1000)==Double.valueOf(datadriven.get("total_amount")) && ((double)Payee_AMT_BALAVAL_change/1000)==Double.valueOf(datadriven.get("total_amount")) && Payee_AMT_BALFRZ_change==0 ){
			        	payeeAssert=true;
					}
/*			        if((Payer_AMT_BALAVAL_change/1000)<Double.valueOf(datadriven.get("total_amount")) && datadriven.get("funds_flag").equals("Y") && DZ_AMT_BALAVAL_change==(Double.valueOf(datadriven.get("total_amount"))-Payer_AMT_BALCUR_before)){
			        	DZAssert=true;
			        }*/
				}
				else {
					log.info("##############################【代发验证异常】，异常返回码："+ret_code+"############################");
					log.info("代发后--付款方现金账户:"+payerAcctNo+"总余额:"+Payer_AMT_BALCUR_after+"可用余额："+Payer_AMT_BALAVAL_after+"冻结余额"+Payer_AMT_BALFRZ_after);
					log.info("代发后--垫资方现金账户:"+DZAcctNo+"总余额:"+DZ_AMT_BALCUR_after+"可用余额："+DZ_AMT_BALAVAL_after+"冻结余额"+DZ_AMT_BALFRZ_after);
					log.info("代发后--收款方现金账户:"+payeeAcctNo+"总余额:"+Payee_AMT_BALCUR_after+"可用余额："+Payee_AMT_BALAVAL_after+"冻结余额"+Payee_AMT_BALFRZ_after);
					
					log.info("代发后--付款方现金账户:"+payerAcctNo+"总余额差:"+Payer_AMT_BALCUR_change+"可用余额差："+Payer_AMT_BALAVAL_change+"冻结余额差"+Payer_AMT_BALFRZ_change);
					log.info("代发后--垫资方现金账户:"+DZAcctNo+"总余额差:"+DZ_AMT_BALCUR_change+"可用余额差："+DZ_AMT_BALAVAL_change+"冻结余额差"+DZ_AMT_BALFRZ_change);
					log.info("代发后--收款方现金账户:"+payeeAcctNo+"总余额差:"+Payee_AMT_BALCUR_change+"可用余额差："+Payee_AMT_BALAVAL_change+"冻结余额差"+Payee_AMT_BALFRZ_change);
					
				     if(Payer_AMT_BALCUR_change==0 && Payer_AMT_BALAVAL_change==0 && Payee_AMT_BALCUR_change==0 && Payee_AMT_BALAVAL_change==0 && DZ_AMT_BALCUR_change==0 && DZ_AMT_BALAVAL_change==0)
				     {
				    	 payerAssert = true;
				    	 payeeAssert =true;
				    	 DZAssert = true;
				     }	
				}
				
			}
			else {
				log.info("##############################【代发异常】，异常返回码："+ret_code+"############################");
				  							
				log.info("代发后--付款方现金账户:"+payerAcctNo+"总余额:"+Payer_AMT_BALCUR_after+"可用余额："+Payer_AMT_BALAVAL_after+"冻结余额"+Payer_AMT_BALFRZ_after);
		        log.info("代发后--垫资方现金账户:"+DZAcctNo+"总余额:"+DZ_AMT_BALCUR_after+"可用余额："+DZ_AMT_BALAVAL_after+"冻结余额"+DZ_AMT_BALFRZ_after);
		        log.info("代发后--收款方现金账户:"+payeeAcctNo+"总余额:"+Payee_AMT_BALCUR_after+"可用余额："+Payee_AMT_BALAVAL_after+"冻结余额"+Payee_AMT_BALFRZ_after);
		        
		        log.info("代发后--付款方现金账户:"+payerAcctNo+"总余额差:"+Payer_AMT_BALCUR_change+"可用余额差："+Payer_AMT_BALAVAL_change+"冻结余额差"+Payer_AMT_BALFRZ_change);
		        log.info("代发后--垫资方现金账户:"+DZAcctNo+"总余额差:"+DZ_AMT_BALCUR_change+"可用余额差："+DZ_AMT_BALAVAL_change+"冻结余额差"+DZ_AMT_BALFRZ_change);
		        log.info("代发后--收款方现金账户:"+payeeAcctNo+"总余额差:"+Payee_AMT_BALCUR_change+"可用余额差："+Payee_AMT_BALAVAL_change+"冻结余额差"+Payee_AMT_BALFRZ_change);

			     if(Payer_AMT_BALCUR_change==0 && Payer_AMT_BALAVAL_change==0 && Payee_AMT_BALCUR_change==0 && Payee_AMT_BALAVAL_change==0 && DZ_AMT_BALCUR_change==0 && DZ_AMT_BALAVAL_change==0)
			     {
			    	 payerAssert = true;
			    	 payeeAssert =true;
			    	 DZAssert = true;
			     }			        	        		        
			}
			} catch (Exception e) {
			e.printStackTrace();
		}
	    finally {
	    	
	    	//将返回信息写入excel
			funcUtil.writeRet2Excel(this, "transferTest",row,ret_code, ret_msg);
	    	//excel数据比对校验
			excelResultCheck=funcUtil.excelResultCheck(this, "transferTest");
			//对支付单 支付流水 付款流水 卡签约信息 进行校验
			payBillCheck = dataBaseAccess.payBillCheck(payBillNo, datadriven.get("oid_partner"), datadriven.get("total_amount"), datadriven.get("biz_code"), datadriven.get("prod_code"),"ONLINE", datadriven.get("paybillStat"), "CNY");		
			payBillSerialCheck = dataBaseAccess.payBillSerialCheck(datadriven.get("total_amount"), payBillNo, "PAYMENT", datadriven.get("sreialState"), "CNY", "ONLINE", datadriven.get("payer_id"), payerUserNo, datadriven.get("payer_accttype")+"_AVAILABLE", payerAcctNo, datadriven.get("pyerTxStat"), "BALANCE", datadriven.get("payee_id"), payeeUserNo, datadriven.get("payee_accttype")+"_AVAILABLE", payeeAcctNo, datadriven.get("pyeeTxStat"), "BALANCE");
			dataResultCheck = payBillCheck&&payBillSerialCheck;
			//Assert.assertTrue(payerAssert&&payeeAssert&&dataResultCheck&&signAssert&&signAssert1);
			//Assert.assertTrue(signAssert&&signAssert1);
			Assert.assertTrue(signAssert1);
			
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
