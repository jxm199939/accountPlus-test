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
import com.accp.test.bean.acctmgr.UnlinkedacctIndApply;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lianlian.crypt.service.IAESCryptService;
import com.tools.dataprovider.ExcelProvider;
import com.tools.utils.DataBaseAccessWL;
import com.tools.utils.DateUtil;
import com.tools.utils.FuncUtil;
import com.tools.utils.GenerateSign;
import com.tools.utils.HttpRequestSimple;
import com.tools.utils.Property;
import com.tools.utils.RandomUtils;
import com.tools.utils.SignatureUtil;

@ContextConfiguration(locations = { "/consumer.xml" })
public class ProdUnlinkedAcctTest extends AbstractTestNGSpringContextTests{
	@Autowired
	private IAESCryptService aesCryptService;	
	DataBaseAccessWL dataBaseAccess = new DataBaseAccessWL();
	private static Log log = LogFactory.getLog(ProdUnlinkedAcctTest.class);
	FuncUtil funcUtil = new FuncUtil();
	DateUtil dateUtil = new DateUtil();
	RandomUtils randomUtils = new RandomUtils();
	boolean excelResultCheck=true;
	int row=11;
	String ret_code="";
	String ret_msg="";
	
	@BeforeClass
	public void beforeClass() throws Exception {
		Property.set();
		funcUtil.prepare4Excel(this, "unlinkedAcctTest");
	}
	
	/**
	 * 测试用例说明：
	 * 银行卡签约解约流程
	 * @author wanglin002
	 * /
	 */
	
	@Test(description = "accp解约测试", dataProvider = "unlinkedAcctTest")
	public void unlinkedAcctTest(Map<String, String> datadriven) throws Exception{

		String dt_order = dateUtil.getCurrentDateTimeStr();// 订单时间yyyyMMddHHmmss
		String no_order = "ACCPTEST" + dt_order + randomUtils.getRandom(10);// 订单号
		String linkedApplyUrl = Property.get("linkedacctIndApply.url.test");
		String linkedApproveUrl = Property.get("linkedacctIndApprove.url.test");
		String unlinkedUrl = Property.get("unlinkedacctIndApply.url.test");
		UnlinkedacctIndApply unlinkedacctIndApply = new UnlinkedacctIndApply();
		IndividualBindcardApply individualBindcardApply = new IndividualBindcardApply();
		IndividualBindcardApprove individualBindcardApprove = new IndividualBindcardApprove();
		String token="";

		try {
			
			log.info("##############################【银行卡解约测试开始】############################");
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
				System.out.println("请输入验证码:\n");
				String Sms = new Scanner(System.in).nextLine();
				individualBindcardApprove.setTimestamp(dt_order);
				individualBindcardApprove.setOid_partner(datadriven.get("oid_partner"));
				individualBindcardApprove.setUser_id(datadriven.get("user_id"));
				individualBindcardApprove.setTxn_seqno(no_order);
				individualBindcardApprove.setToken(token);
				individualBindcardApprove.setVerify_code(Sms);
				
				String reqJson1 = JSON.toJSONString(individualBindcardApprove);
				log.info("【签约验证】请求报文:" + reqJson1);
				sign = SignatureUtil.getInstance().sign(datadriven.get("private_key"), reqJson1);
				signType = datadriven.get("signType");
				String rsp1 = httpclient.postSendHttp(linkedApproveUrl, reqJson1, signType, sign);
				log.info("【签约验证】返回报文:" + rsp1);
				JSONObject json_rsp1 = JSON.parseObject(rsp1);
				ret_code = json_rsp1.getString("ret_code");
				ret_msg = json_rsp1.getString("ret_msg");
				if(ret_code.equals("0000")){
					log.info("##############################【签约验证成功，进入解约】############################");
					unlinkedacctIndApply.setTimestamp(dt_order);
					unlinkedacctIndApply.setOid_partner(datadriven.get("oid_partner"));
					unlinkedacctIndApply.setUser_id(datadriven.get("user_id"));
					unlinkedacctIndApply.setTxn_seqno("unlink"+no_order);
					unlinkedacctIndApply.setTxn_time(dt_order);
					unlinkedacctIndApply.setNotify_url(datadriven.get("notify_url"));
					unlinkedacctIndApply.setLinked_acctno(datadriven.get("linked_acctno"));
					unlinkedacctIndApply.setPassword(datadriven.get("password"));
					unlinkedacctIndApply.setRandom_key(datadriven.get("random_key"));

					String reqJson2 = JSON.toJSONString(unlinkedacctIndApply);
					log.info("【解约】请求报文:" + reqJson2);
					sign = SignatureUtil.getInstance().sign(datadriven.get("private_key"), reqJson2);
					signType = datadriven.get("signType");
					String rsp2 = httpclient.postSendHttp(unlinkedUrl, reqJson2, signType, sign);
					log.info("【解约】返回报文:" + rsp2);
					JSONObject json_rsp2 = JSON.parseObject(rsp2);
					ret_code = json_rsp2.getString("ret_code");
					ret_msg = json_rsp2.getString("ret_msg");
					if(ret_code.equals("0000")){
						log.info("##############################【解约成功】############################");							
					}
					else {
						log.info("##############################【解约异常】，异常返回码："+ret_code+"############################");
					}
								
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
			funcUtil.writeRet2Excel(this, "unlinkedAcctTest",row,ret_code, ret_msg);
	    	//excel数据比对校验
			excelResultCheck=funcUtil.excelResultCheck(this, "unlinkedAcctTest");
			//对支付单 支付流水 付款流水 卡签约信息 进行校验
			Assert.assertTrue(excelResultCheck);			
			log.info("##############################【解约测试结束】############################");
	}
}	
	@DataProvider(name = "unlinkedAcctTest")
	public Iterator<Object[]> data4UnlinkedacctIndApply() throws IOException {
		//excel名称跟类名一致，sheet名称跟方法名一致
		return new ExcelProvider(this, "unlinkedAcctTest",row);
	}

	@AfterClass
	public void afterClass() {
		dataBaseAccess.closeDBConnection();
	}

}
