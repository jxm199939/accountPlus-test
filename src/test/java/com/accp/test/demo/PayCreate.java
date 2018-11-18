package com.accp.test.demo;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
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
import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lianlian.crypt.service.IAESCryptService;
import com.tools.dataprovider.ExcelProvider;
import com.tools.utils.DataBaseAccess;
import com.tools.utils.DateUtil;
import com.tools.utils.FuncUtil;
import com.tools.utils.GenerateSign;
import com.tools.utils.HttpRequestSimple;
import com.tools.utils.Property;
import com.tools.utils.RandomUtils;

@ContextConfiguration(locations = { "/consumer.xml" })
public class PayCreate extends AbstractTestNGSpringContextTests{
	@Autowired
	private IAESCryptService aesCryptService;	
	DataBaseAccess dataBaseAccess = new DataBaseAccess();
	private static Log log = LogFactory.getLog(PayCreate.class);
	FuncUtil funcUtil = new FuncUtil();
	DateUtil dateUtil = new DateUtil();
	RandomUtils randomUtils = new RandomUtils();
	boolean dataResultCheck =true;
	int row=1;
	
	@BeforeClass
	public void beforeClass() throws Exception {
		Property.set();
		funcUtil.prepare4Excel(this, "payCreateTest");
	}
	
	/*
	 * 测试用例说明：
	 * 统一支付创单
	 * /
	 */
	
	@Test(description = "accp统一支付创单接口测试", timeOut = 60000, dataProvider = "payCreateTest")
	public void payCreateTest(Map<String, String> datadriven) throws Exception{

		String SERVER_TradeCreate = Property.get("tradeCreate.url.test");
		boolean excelResultCheck =true;
		JSONObject orderInfo  = new JSONObject();
		PayCreate payCreate = new PayCreate();
	
		String dt_order = DateUtil.getCurrentDateStr();// 订单时间
		String no_order = "ACCPTEST" + dt_order + randomUtils.getRandom(10);// 订单号
		String ret_code="11111",ret_msg="222222";
		
		try {
			
			//新增签约渠道
			//前置条件函数
			
			//单独签约都会发短信
			log.info("##############################【xxxx测试开始】############################");
						
			//payCreate.setOid_partner(datadriven.get("oid_partner"));
			//payCreate.setTxn_type(datadriven.get("txn_type"));
			//xxxxxxxxxxxxxxxx;
			//xxxxxxxxxxxxxxxx;
			//orderInfo.put("txn_seqno", "xxxx");
			//orderInfo.put("txn_time", "xxxxx");
			//xxxxxxxxxxxxxxxxx;
			//payCreate.setOrderInfo(orderInfo.toString());
			
			String sign = GenerateSign.genSign(JSON.parseObject(JSON.toJSONString(payCreate)),datadriven.get("private_key"));
			String signType = datadriven.get("signType");
			String reqJson_apply = JSON.toJSONString(payCreate);
			log.info("创单申请报文:" + reqJson_apply);
			HttpRequestSimple httpclent = new HttpRequestSimple();
			String resJson_apply = httpclent.postSendHttp(SERVER_TradeCreate, reqJson_apply,signType,sign);
			
			log.info("创单返回报文:" + resJson_apply);		
			JSONObject json_Apply = JSON.parseObject(resJson_apply);
			ret_code = json_Apply.getString("ret_code");
			ret_msg = json_Apply.getString("ret_msg");
			 
		} catch (Exception e) {
			e.printStackTrace();
		}
	    finally {
	    	
	    	//将返回信息写入excel
			funcUtil.writeRet2Excel(this, "payCreateTest",ret_code, ret_msg);
	    	//excel数据比对校验
			excelResultCheck=funcUtil.excelResultCheck(this, "payCreateTest");
			//对支付单 支付流水 付款流水 卡签约信息 进行校验
			//dataResultCheck = dataBaseAccess.assertPayandSignReslut(aesCryptService,card_no,acct_name,id_no, bind_mob,money_order, oid_paybill, datadriven.get("paybillStat"), payType, chnlW1, oid_partner, merchantIdW1, bank_code,datadriven.get("payBilllSerilaStat"), datadriven.get("finserialStat"),datadriven.get("signStat"),"N","N");
			Assert.assertTrue(excelResultCheck&&dataResultCheck);			
			log.info("##############################【xxxxxxx测试结束】############################");
	}
}	
	@DataProvider(name = "payCreateTest")
	public Iterator<Object[]> data4payCreate() throws IOException {
		//excel名称跟类名一致，sheet名称跟方法名一致
		return new ExcelProvider(this, "payCreateTest");
	}

	@AfterClass
	public void afterClass() {
		dataBaseAccess.closeDBConnection();
	}

}
