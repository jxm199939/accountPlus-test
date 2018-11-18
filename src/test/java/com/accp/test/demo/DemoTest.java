package com.accp.test.demo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import com.tools.utils.DataBaseAccess;

public class DemoTest {
	
	DataBaseAccess dataBaseAccess = new DataBaseAccess();
	private static Log log = LogFactory.getLog(DemoTest.class);
	@Test(description = "demo数据库测试", timeOut = 60000)
	public void demoTest() throws Exception{
		String sms = dataBaseAccess.getSms("15105815672");
		log.info("++++++++++++++"+sms);
	}

	@AfterClass
	public void afterClass() throws Exception {
		dataBaseAccess.closeDBConnection();
		}

}
