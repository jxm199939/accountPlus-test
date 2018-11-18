package com.accp.test.chnl;

import java.io.IOException;
import java.util.Iterator;
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
import com.accp.test.bean.acctmgr.IndividualBindcardApply;
import com.accp.test.bean.acctmgr.IndividualBindcardApprove;
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
public class LinkedAcctTest extends AbstractTestNGSpringContextTests{
	@Autowired
	private IAESCryptService aesCryptService;	
	DataBaseAccessWL dataBaseAccess = new DataBaseAccessWL();
	private static Log log = LogFactory.getLog(LinkedAcctTest.class);
	FuncUtil funcUtil = new FuncUtil();
	DateUtil dateUtil = new DateUtil();
	RandomUtils randomUtils = new RandomUtils();
	boolean dataResultCheck =true,excelResultCheck=true,chnlSignInfoCheck=true,userSignInfoCheck=true;
	int row=32;
	String ret_code="";
	String ret_msg="";
	String token="";

	
	@BeforeClass
	public void beforeClass() throws Exception {
		Property.set();
		funcUtil.prepare4Excel(this, "linkedAcctTest");
	}
	
	/**
	 * 测试用例说明：
	 * 银行卡签约解约流程
	 * @author wanglin002
	 * /
	 */
	
	@Test(description = "accp签约绑卡测试", timeOut = 100000, dataProvider = "linkedAcctTest")
	public void linkedAcctTest(Map<String, String> datadriven) throws Exception{

		String dt_order = dateUtil.getCurrentDateTimeStr();// 订单时间yyyyMMddHHmmss
		String no_order = "ACCPTEST" + dt_order + randomUtils.getRandom(10);// 订单号
		String linkedApplyUrl = Property.get("linkedacctIndApply.url.test");
		String linkedApproveUrl = Property.get("linkedacctIndApprove.url.test");
		IndividualBindcardApply individualBindcardApply = new IndividualBindcardApply();
		IndividualBindcardApprove individualBindcardApprove = new IndividualBindcardApprove();
		
		String bank_code = datadriven.get("bankcode");
		String bank_name = datadriven.get("bankname");
		//String bizType = "AGRT_DEBIT_CARD";
		String bizType = datadriven.get("biz_type");
		//String bizType = "AGRT_CREDIT_CARD";
		String status = "OPEN";
		String YMchnl = datadriven.get("Ymchnl");
		String YMchnlMerchant = datadriven.get("YMchnlMerchant");
		String WMchnl = datadriven.get("WMchnl");
		String WMchnlMerchant = datadriven.get("WMchnlMerchant");
		String Paychnl = datadriven.get("Paychnl");
		String PaychnlMerchant = datadriven.get("PaychnlMerchant");
		String card_type = datadriven.get("card_type");
		String priority = "1";
		String oid_partner = datadriven.get("oid_partner");
		String limit="100000";
		String chnl_weight="60";
		String amt_flag="0";
		String cardYM = aesCryptService.encrypt(datadriven.get("linked_acctno").getBytes("UTF-8"));
		String Sms ="";
		
		try {
			
			//新增签约渠道
			String deleteSignCofSql = "delete from AUTH_CHNL_INFO where OID_TRADERNO='" + oid_partner + "'AND BANK_CODE='" + bank_code + "' AND BIZ_TYPE='"+ bizType +"'";		
			String insertSignCofSql = "insert into AUTH_CHNL_INFO(OID_TRADERNO,BANK_CODE,BANK_NAME,BIZ_TYPE,STATUS,OID_CHNL,MERCHANT_ID,CARD_TYPE,PRIORITY) values('"+oid_partner+"','"+bank_code+"','"+bank_name+"','"+bizType+"','"+status+"','"+YMchnl+"','"+YMchnlMerchant+"','"+card_type+"','"+priority+"')";				
			String insertSignCofSql1 = "insert into AUTH_CHNL_INFO(OID_TRADERNO,BANK_CODE,BANK_NAME,BIZ_TYPE,STATUS,OID_CHNL,MERCHANT_ID,CARD_TYPE,PRIORITY) values('"+oid_partner+"','"+bank_code+"','"+bank_name+"','"+bizType+"','"+status+"','"+WMchnl+"','"+WMchnlMerchant+"','"+card_type+"','"+priority+"')";										
			
			//新增支付渠道配置
			String deletePayCofSql = "delete from PAY_CHNL_INFO where OID_TRADERNO ='"+oid_partner+"' AND BANKCODE='" + bank_code + "'AND BIZ_TYPE='"+ bizType +"'";
			String insertPayCofSql = "insert into PAY_CHNL_INFO(OID_TRADERNO,BANKCODE,OID_CHNL,BIZ_TYPE,MERCHANTID,CARD_TYPE,APART_LIMIT,CHNL_WEIGHT,AMT_FLAG,STATUS,PRIORITY) values('"+oid_partner+"','"+bank_code+"','"+Paychnl+"','"+bizType+"','"+PaychnlMerchant+"','"+card_type+"','"+limit+"','"+chnl_weight+"','"+amt_flag+"','"+status+"','"+priority+"')";
			
			//删除认证签约信息
			String deleteAuthInfoSql = "delete from USER_BANK_CARD_INFO where CARD_NUMBER='" + cardYM + "'";
			//删除快捷签约信息
			String deleteSignInfoSql = "delete from TC_INFO_SIGNED where CARDNO='" + cardYM + "'";
					
			//删除签约表配置信息，删除支付表配置信息
			dataBaseAccess.deleteChnlConfiguration(deleteSignCofSql);
			dataBaseAccess.deleteChnlConfiguration(deletePayCofSql);
			
			//插入签约支付渠道配置
			if(!StringUtils.isBlank(WMchnl)){
				dataBaseAccess.insertChnlConfiguration(insertSignCofSql1);	
			}
			if(!StringUtils.isBlank(YMchnl)){
				dataBaseAccess.insertChnlConfiguration(insertSignCofSql);					
			}
			if(!StringUtils.isBlank(Paychnl)){
				dataBaseAccess.insertChnlConfiguration(insertPayCofSql);
			}
			
			//删除签约记录，删除认证记录
			if(datadriven.get("IfNewCard").equals("Y")){
				dataBaseAccess.deleteChnlConfiguration(deleteAuthInfoSql);
				dataBaseAccess.deleteChnlConfiguration(deleteSignInfoSql);
				log.info(deleteSignInfoSql);
				}
			
			log.info("##############################【银行卡签约测试开始】############################");
			individualBindcardApply.setTimestamp(dt_order);
			individualBindcardApply.setOid_partner(datadriven.get("oid_partner"));
			individualBindcardApply.setUser_id(datadriven.get("user_id"));
			individualBindcardApply.setTxn_seqno(no_order);
			individualBindcardApply.setTxn_time(dt_order);
			individualBindcardApply.setNotify_url(datadriven.get("notify_url"));
			individualBindcardApply.setLinked_acctno(datadriven.get("linked_acctno"));
			individualBindcardApply.setLinked_phone(datadriven.get("linked_phone"));
			individualBindcardApply.setCvv2(datadriven.get("cvv2"));
			individualBindcardApply.setValid_thru(datadriven.get("valid"));
			individualBindcardApply.setPassword(datadriven.get("password"));
			individualBindcardApply.setRandom_key(datadriven.get("random_key"));
			
			String reqJson = JSON.toJSONString(individualBindcardApply);
			log.info("【签约申请】请求报文:" + reqJson);
			//String sign = GenerateSign.genSign(JSON.parseObject(reqJson), datadriven.get("private_key"));
			String sign = SignatureUtil.getInstance().sign(datadriven.get("private_key"), reqJson);
			String signType = datadriven.get("signType");
			HttpRequestSimple httpclient = new HttpRequestSimple();
			String rsp = httpclient.postSendHttp(linkedApplyUrl, reqJson, signType, sign);
			log.info("【签约申请】返回报文:" + rsp);
			JSONObject json_rsp = JSON.parseObject(rsp);
			ret_code = json_rsp.getString("ret_code");
			ret_msg = json_rsp.getString("ret_msg");
			token = json_rsp.getString("token");
			
			if(ret_code.equals("0000")){
				log.info("##############################【签约申请成功，进入签约验证】############################");
			//	if(!StringUtils.isBlank(dataBaseAccess.getSms(datadriven.get("linked_phone")))){
			//		Sms = dataBaseAccess.getSms(datadriven.get("linked_phone"));
			//	}
				//else {
					System.out.println("请输入验证码:\n");
					Sms = new Scanner(System.in).nextLine();
				//}
				individualBindcardApprove.setTimestamp(dt_order);
				individualBindcardApprove.setOid_partner(datadriven.get("oid_partner"));
				individualBindcardApprove.setUser_id(datadriven.get("user_id"));
				individualBindcardApprove.setTxn_seqno(no_order);
				individualBindcardApprove.setToken(token);
				individualBindcardApprove.setVerify_code(Sms);
				//individualBindcardApprove.setVerify_code("123456");

				
				String reqJson1 = JSON.toJSONString(individualBindcardApprove);
				log.info("【签约验证】请求报文:" + reqJson1);
				//sign = SignatureUtil.getInstance().sign(datadriven.get("private_key"), reqJson);
				sign = SignatureUtil.getInstance().sign(datadriven.get("private_key"), reqJson1);
				signType = datadriven.get("signType");
				String rsp1 = httpclient.postSendHttp(linkedApproveUrl, reqJson1, signType, sign);
				log.info("【签约验证】返回报文:" + rsp1);
				JSONObject json_rsp1 = JSON.parseObject(rsp1);
				ret_code = json_rsp1.getString("ret_code");
				ret_msg = json_rsp1.getString("ret_msg");
				if(ret_code.equals("0000")){	
					log.info("##############################【签约验证成功，签约结束】############################");
				}
				else {
					log.info("##############################【签约验证异常】，异常返回码："+ret_code+"############################");

				}
				
			}else {
				log.info("##############################【签约申请异常】，异常返回码："+ret_code+"############################");

			}
				
			 
		} catch (Exception e) {
			e.printStackTrace();
		}
	    finally {
	    	
	    	//将返回信息写入excel
			funcUtil.writeRet2Excel(this, "linkedAcctTest",row,ret_code, ret_msg);
	    	//excel数据比对校验
			excelResultCheck=funcUtil.excelResultCheck(this, "linkedAcctTest");
			//对支付单 支付流水 付款流水 卡签约信息 进行校验
			chnlSignInfoCheck = dataBaseAccess.chnlSignInfoCheck(aesCryptService, "SIGN_SUCCESS", datadriven.get("linked_acctno"), "王琳", "370782199010204101", datadriven.get("linked_phone"));
			userSignInfoCheck = dataBaseAccess.userSignInfoCheck(aesCryptService, "AUTH_SUCCESS", datadriven.get("linked_acctno"), "王琳", "370782199010204101", datadriven.get("linked_phone"));
			dataResultCheck = chnlSignInfoCheck&&userSignInfoCheck;
			Assert.assertTrue(dataResultCheck);			
			log.info("##############################【解约测试结束】############################");
	}
}	
	@DataProvider(name = "linkedAcctTest")
	public Iterator<Object[]> data4UnlinkedacctIndApply() throws IOException {
		//excel名称跟类名一致，sheet名称跟方法名一致
		return new ExcelProvider(this, "linkedAcctTest",row);
	}

	@AfterClass
	public void afterClass() {
		dataBaseAccess.closeDBConnection();
	}

}
