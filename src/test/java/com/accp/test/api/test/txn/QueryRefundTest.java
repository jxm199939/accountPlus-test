package com.accp.test.api.test.txn;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.accp.test.bean.txn.QueryRefund;
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
 * 退款结果查询接口测试
 */

@ContextConfiguration(locations = { "/consumer.xml" })
public class QueryRefundTest extends AbstractTestNGSpringContextTests {

	DataBaseAccess dataBaseAccess = new DataBaseAccess();

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "退款结果查询接口测试", timeOut = 60000, dataProvider = "queryRefund")
	public void queryRefund(Map<String, String> datadriven) throws Exception {

		String queryRefundUrl = Property.get("queryRefund.url.test");
		QueryRefund queryRefund = new QueryRefund();
		String oid_partner = MyConfig.getOidPartner(datadriven.get("oid_partner"));
		String ipUpdate = datadriven.get("ipUpdate");
		String traderStatUpdate = datadriven.get("traderStatUpdate");

		try {
			Reporter.log("##############################【xxxx测试开始】############################", true);
			queryRefund.setTimestamp(MyConfig.getTimestamp(datadriven.get("timestamp")));
			queryRefund.setOid_partner(oid_partner);
			queryRefund.setRefund_seqno(datadriven.get("refund_seqno"));
			queryRefund.setAccp_txno(datadriven.get("accp_txno"));

			if (!StringUtils.isBlank(ipUpdate)) {
				dataBaseAccess.updateIpRequest(ipUpdate, oid_partner);
			}
			if (!StringUtils.isBlank(traderStatUpdate)) {
				dataBaseAccess.updateTraderEnableState(traderStatUpdate, oid_partner);
			}

			String reqJson = JSON.toJSONString(queryRefund);
			Reporter.log("退款结果查询请求报文:" + reqJson, true);
			String sign = SignatureUtil.getInstance().sign(datadriven.get("private_key"), reqJson);
			HttpRequestSimple httpclient = new HttpRequestSimple();
			String[] res = httpclient.post(queryRefundUrl, reqJson, Property.get("SIGNATURE_TYPE"), sign);
			String resJson = res[0];
			String resSignatureData = res[1];
			Reporter.log("退款结果查询返回报文:" + resJson, true);

			// 退款结果查询请求返回检查
			assert resJson.contains(datadriven.get("expect_retcode"));
			if ("0000".equals(JSONObject.parseObject(resJson).getString("ret_code"))) {
				assert SignatureUtil.getInstance().checksign(Property.get("YT_RSA_PUBLIC"), resJson, resSignatureData);
			}

			// 请求&响应写入文件
			SampleFileUtils.appendLine("D://TA//accplog//queryRefund.txt", MyDate.getStringDate());
			SampleFileUtils.appendLine("D://TA//accplog//queryRefund.txt", reqJson);
			SampleFileUtils.appendLine("D://TA//accplog//queryRefund.txt", resJson);
			SampleFileUtils.appendLine("D://TA//accplog//queryRefund.txt",
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
			Reporter.log("退款结果查询接口测试： " + datadriven.get("row_num") + datadriven.get("case_title"), true);
			Reporter.log("##############################【xxxx测试结束】############################", true);
		}
	}

	@DataProvider(name = "queryRefund")
	public Iterator<Object[]> data4queryRefund() throws IOException {
		return new ExcelProvider(this, "queryRefund");
	}

	@AfterClass
	public void afterClass() {
		dataBaseAccess.closeDBConnection();
	}

}