package com.accp.test.api.test.txn;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.accp.test.bean.txn.QueryPayment;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tools.dataprovider.ExcelProvider;
import com.tools.utils.DataBaseAccess;
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
 * 支付结果查询接口测试
 */

@ContextConfiguration(locations = { "/consumer.xml" })
public class QueryPaymentTest extends AbstractTestNGSpringContextTests {

	DataBaseAccess dataBaseAccess = new DataBaseAccess();

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "支付结果查询接口测试", timeOut = 60000, dataProvider = "queryPayment")
	public void queryPayment(Map<String, String> datadriven) throws Exception {

		String queryPaymentUrl = Property.get("queryPayment.url.test");
		QueryPayment queryPayment = new QueryPayment();
		String oid_partner = MyConfig.getOidPartner(datadriven.get("oid_partner"));
		String ipUpdate = datadriven.get("ipUpdate");
		String traderStatUpdate = datadriven.get("traderStatUpdate");

		try {
			Reporter.log("##############################【xxxx测试开始】############################", true);
			queryPayment.setTimestamp(MyConfig.getTimestamp(datadriven.get("timestamp")));
			queryPayment.setOid_partner(oid_partner);
			queryPayment.setTxn_seqno(datadriven.get("txn_seqno"));
			queryPayment.setAccp_txno(datadriven.get("accp_txno"));

			if (!StringUtils.isBlank(ipUpdate)) {
				dataBaseAccess.updateIpRequest(ipUpdate, oid_partner);
			}
			if (!StringUtils.isBlank(traderStatUpdate)) {
				dataBaseAccess.updateTraderEnableState(traderStatUpdate, oid_partner);
			}

			String reqJson = JSON.toJSONString(queryPayment);
			Reporter.log("支付结果查询请求报文:" + reqJson, true);
			String sign = SignatureUtil.getInstance().sign(datadriven.get("private_key"), reqJson);
			HttpRequestSimple httpclient = new HttpRequestSimple();
			String[] res = httpclient.post(queryPaymentUrl, reqJson, Property.get("SIGNATURE_TYPE"), sign);
			String resJson = res[0];
			String resSignatureData = res[1];
			Reporter.log("支付结果查询返回报文:" + resJson, true);

			// 支付结果查询请求返回检查
			assert resJson.contains(datadriven.get("expect_retcode"));
			if ("0000".equals(JSONObject.parseObject(resJson).getString("ret_code"))) {
				assert SignatureUtil.getInstance().checksign(Property.get("YT_RSA_PUBLIC"), resJson, resSignatureData);
			}

			// 请求&响应写入文件
			SampleFileUtils.appendLine("D://TA//accplog//queryPayment.txt", MyDate.getStringDate());
			SampleFileUtils.appendLine("D://TA//accplog//queryPayment.txt", reqJson);
			SampleFileUtils.appendLine("D://TA//accplog//queryPayment.txt", resJson);
			SampleFileUtils.appendLine("D://TA//accplog//queryPayment.txt",
					"===============================================");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			if (!StringUtils.isBlank(ipUpdate)) {
				dataBaseAccess.updateIpRequest("*.*.*.*", oid_partner);
			}
			if (!StringUtils.isBlank(traderStatUpdate)) {
				dataBaseAccess.updateTraderEnableState("0", oid_partner);
			}
			Reporter.log("支付结果查询接口测试： " + datadriven.get("row_num") + datadriven.get("case_title"), true);
			Reporter.log("##############################【xxxx测试结束】############################", true);
		}
	}

	@DataProvider(name = "queryPayment")
	public Iterator<Object[]> data4queryPayment() throws IOException {
		return new ExcelProvider(this, "queryPayment");
	}

	@AfterClass
	public void afterClass() {
		dataBaseAccess.closeDBConnection();
	}

}