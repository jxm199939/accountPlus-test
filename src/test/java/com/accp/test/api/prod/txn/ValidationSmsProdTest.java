package com.accp.test.api.prod.txn;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.accp.test.bean.txn.ValidationSms;
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
 * 二次验证接口测试
 */

@ContextConfiguration(locations = { "/consumer.xml" })
public class ValidationSmsProdTest extends AbstractTestNGSpringContextTests {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "二次验证接口测试", timeOut = 60000, dataProvider = "validationSms")
	public void validationSms(Map<String, String> datadriven) throws Exception {

		String validationSmsUrl = Property.get("validationSms.url.prod");
		ValidationSms validationSms = new ValidationSms();

		try {

			Reporter.log("##############################【xxxx测试开始】############################", true);
			validationSms.setTimestamp(MyConfig.getTimestamp(datadriven.get("timestamp")));
			validationSms.setOid_partner(MyConfig.getOidPartner(datadriven.get("oid_partner")));
			validationSms.setPayer_type(datadriven.get("payer_type"));
			validationSms.setPayer_id(datadriven.get("payer_id"));
			validationSms.setTxn_seqno(datadriven.get("txn_seqno"));
			validationSms.setTotal_amount(datadriven.get("total_amount"));
			validationSms.setToken(datadriven.get("token"));
			validationSms.setVerify_code(datadriven.get("verify_code"));
			String reqJson = JSON.toJSONString(validationSms);
			Reporter.log("二次验证请求报文:" + reqJson, true);
			String sign = SignatureUtil.getInstance().sign(datadriven.get("private_key"), reqJson);
			HttpRequestSimple httpclient = new HttpRequestSimple();
			String[] res = httpclient.post(validationSmsUrl, reqJson, Property.get("SIGNATURE_TYPE"), sign);
			String resJson = res[0];
			String resSignatureData = res[1];
			Reporter.log("二次验证返回报文:" + resJson, true);

			// 请求&响应写入文件
			SampleFileUtils.appendLine("D://TA//accpprodlog//validationSms.txt", MyDate.getStringDate());
			SampleFileUtils.appendLine("D://TA//accpprodlog//validationSms.txt", reqJson);
			SampleFileUtils.appendLine("D://TA//accpprodlog//validationSms.txt", resJson);
			SampleFileUtils.appendLine("D://TA//accpprodlog//validationSms.txt",
					"===============================================");

			// 二次验证请求返回检查
			assert resJson.contains(datadriven.get("expect_retcode"));
			if ("0000".equals(JSONObject.parseObject(resJson).getString("ret_code"))) {
				assert SignatureUtil.getInstance().checksign(Property.get("YT_RSA_PUBLIC"), resJson, resSignatureData);

			}

		} catch (

		Exception e) {
			e.printStackTrace();
		} finally {

			Reporter.log("二次验证接口测试： " + datadriven.get("row_num") + datadriven.get("case_title"), true);
			Reporter.log("##############################【xxxx测试结束】############################", true);
		}
	}

	@DataProvider(name = "validationSms")
	public Iterator<Object[]> data4validationSms() throws IOException {
		return new ExcelProvider(this, "validationSms", 5);
	}

	@AfterClass
	public void afterClass() {

	}

}