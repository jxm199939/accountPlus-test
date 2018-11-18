package com.accp.test.api.prod.txn;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.accp.test.bean.txn.QueryPayment;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tools.dataprovider.ExcelProvider;
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
public class QueryPaymentProdTest extends AbstractTestNGSpringContextTests {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "支付结果查询接口测试", timeOut = 60000, dataProvider = "queryPayment")
	public void queryPayment(Map<String, String> datadriven) throws Exception {

		String queryPaymentUrl = Property.get("queryPayment.url.prod");
		QueryPayment queryPayment = new QueryPayment();
		String oid_partner = MyConfig.getOidPartner(datadriven.get("oid_partner"));

		try {
			Reporter.log("##############################【xxxx测试开始】############################", true);
			queryPayment.setTimestamp(MyConfig.getTimestamp(datadriven.get("timestamp")));
			queryPayment.setOid_partner(oid_partner);
			queryPayment.setTxn_seqno(datadriven.get("txn_seqno"));
			queryPayment.setAccp_txno(datadriven.get("accp_txno"));

			String reqJson = JSON.toJSONString(queryPayment);
			Reporter.log("支付结果查询请求报文:" + reqJson, true);
			String sign = SignatureUtil.getInstance().sign(datadriven.get("private_key"), reqJson);
			HttpRequestSimple httpclient = new HttpRequestSimple();
			String[] res = httpclient.post(queryPaymentUrl, reqJson, Property.get("SIGNATURE_TYPE"), sign);
			String resJson = res[0];
			String resSignatureData = res[1];
			Reporter.log("支付结果查询返回报文:" + resJson, true);

			// 请求&响应写入文件
			SampleFileUtils.appendLine("D://TA//accpprodlog//queryPayment.txt", MyDate.getStringDate());
			SampleFileUtils.appendLine("D://TA//accpprodlog//queryPayment.txt", reqJson);
			SampleFileUtils.appendLine("D://TA//accpprodlog//queryPayment.txt", resJson);
			SampleFileUtils.appendLine("D://TA//accpprodlog//queryPayment.txt",
					"===============================================");

			// 支付结果查询请求返回检查
			assert resJson.contains(datadriven.get("expect_retcode"));
			if ("0000".equals(JSONObject.parseObject(resJson).getString("ret_code"))) {
				assert SignatureUtil.getInstance().checksign(Property.get("YT_RSA_PUBLIC"), resJson, resSignatureData);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			Reporter.log("支付结果查询接口测试： " + datadriven.get("row_num") + datadriven.get("case_title"), true);
			Reporter.log("##############################【xxxx测试结束】############################", true);
		}
	}

	@DataProvider(name = "queryPayment")
	public Iterator<Object[]> data4queryPayment() throws IOException {
		return new ExcelProvider(this, "queryPayment", 2);
	}

	@AfterClass
	public void afterClass() {

	}

}