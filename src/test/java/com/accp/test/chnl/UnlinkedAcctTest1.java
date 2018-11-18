package com.accp.test.chnl;

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
public class UnlinkedAcctTest1 extends AbstractTestNGSpringContextTests{
	@Autowired
	private IAESCryptService aesCryptService;	
	DataBaseAccessWL dataBaseAccess = new DataBaseAccessWL();
	private static Log log = LogFactory.getLog(UnlinkedAcctTest1.class);
	FuncUtil funcUtil = new FuncUtil();
	DateUtil dateUtil = new DateUtil();
	RandomUtils randomUtils = new RandomUtils();
	boolean dataResultCheck =true,excelResultCheck=true,chnlSignInfoCheck=true,userSignInfoCheck=true;
	int row=1;
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
	
	@Test(description = "accp解约测试", timeOut = 60000, dataProvider = "unlinkedAcctTest")
	public void unlinkedAcctTest(Map<String, String> datadriven) throws Exception{

		String dt_order = dateUtil.getCurrentDateTimeStr();// 订单时间yyyyMMddHHmmss
		String no_order = "ACCPTEST" + dt_order + randomUtils.getRandom(10);// 订单号
		String unlinkedUrl = Property.get("unlinkedacctIndApply.url.test");
		UnlinkedacctIndApply unlinkedacctIndApply = new UnlinkedacctIndApply();

		try {
			
			log.info("##############################【银行卡解约测试开始】############################");
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
					String sign = SignatureUtil.getInstance().sign(datadriven.get("private_key"), reqJson2);
					String signType = datadriven.get("signType");
					HttpRequestSimple httpclient = new HttpRequestSimple();
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
												
			 
		} catch (Exception e) {
			e.printStackTrace();
		}
	    finally {
	    	
	    	//将返回信息写入excel
			funcUtil.writeRet2Excel(this, "unlinkedAcctTest",row,ret_code, ret_msg);
	    	//excel数据比对校验
			excelResultCheck=funcUtil.excelResultCheck(this, "unlinkedAcctTest");
			//对支付单 支付流水 付款流水 卡签约信息 进行校验
			chnlSignInfoCheck = dataBaseAccess.chnlSignInfoCheck(aesCryptService, "SIGN_CANCEL", datadriven.get("linked_acctno"), "王琳", "370782199010204101", datadriven.get("linked_phone"));
			userSignInfoCheck = dataBaseAccess.userSignInfoCheck(aesCryptService, "AUTH_CANCEL", datadriven.get("linked_acctno"), "王琳", "370782199010204101", datadriven.get("linked_phone"));
			dataResultCheck = chnlSignInfoCheck&&userSignInfoCheck;
			Assert.assertTrue(dataResultCheck);			
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
