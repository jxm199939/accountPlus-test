package com.accp.test.api.prod.txn;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.accp.test.bean.txn.PayerInfo;
import com.accp.test.bean.txn.PaymentBalance;
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
 * 余额支付接口测试
 */

@ContextConfiguration(locations = { "/consumer.xml" })
public class PaymentBalanceProdTest extends AbstractTestNGSpringContextTests {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "余额支付接口测试", timeOut = 60000, dataProvider = "paymentBalance")
	public void paymentBalance(Map<String, String> datadriven) throws Exception {

		String paymentBalanceUrl = Property.get("paymentBalance.url.prod");
		String oid_partner = MyConfig.getOidPartner(datadriven.get("paymentBalance.oid_partner"));
		String user_id = MyConfig.getUser(datadriven.get("paymentBalance.user_id"));

		try {

			Reporter.log("##############################【xxxx测试开始】############################", true);
			PaymentBalance paymentBalance = new PaymentBalance();
			PayerInfo payerInfo = new PayerInfo();

			paymentBalance.setTimestamp(MyConfig.getTimestamp(datadriven.get("paymentBalance.timestamp")));
			paymentBalance.setOid_partner(oid_partner);
			paymentBalance.setTxn_seqno(datadriven.get("paymentBalance.txn_seqno"));
			paymentBalance.setTotal_amount(datadriven.get("paymentBalance.total_amount"));
			paymentBalance.setCoupon_amount(datadriven.get("paymentBalance.coupon_amount"));
			paymentBalance.setRisk_item(datadriven.get("paymentBalance.risk_item"));

			payerInfo.setUser_id(user_id);
			payerInfo.setPassword(datadriven.get("paymentBalance.password"));
			payerInfo.setRandom_key(datadriven.get("paymentBalance.random_key"));
			paymentBalance.setPayerInfo(payerInfo);

			String reqJson1 = JSON.toJSONString(paymentBalance);
			Reporter.log("余额支付请求报文:" + reqJson1, true);
			String sign1 = SignatureUtil.getInstance().sign(datadriven.get("paymentBalance.private_key"), reqJson1);
			HttpRequestSimple httpclient = new HttpRequestSimple();
			String[] res1 = httpclient.post(paymentBalanceUrl, reqJson1, Property.get("SIGNATURE_TYPE"), sign1);
			String resJson1 = res1[0];
			String resSignatureData1 = res1[1];
			Reporter.log("余额支付返回报文:" + resJson1, true);

			// 请求&响应写入文件
			SampleFileUtils.appendLine("D://TA//accpprodlog//paymentBalance.txt", MyDate.getStringDate());
			SampleFileUtils.appendLine("D://TA//accpprodlog//paymentBalance.txt", reqJson1);
			SampleFileUtils.appendLine("D://TA//accpprodlog//paymentBalance.txt", resJson1);
			SampleFileUtils.appendLine("D://TA//accpprodlog//paymentBalance.txt",
					"===============================================");

			// 余额支付请求返回检查
			assert resJson1.contains(datadriven.get("expect_retmsg"));
			if ("0000".equals(JSONObject.parseObject(resJson1).getString("ret_code"))) {
				assert SignatureUtil.getInstance().checksign(Property.get("YT_RSA_PUBLIC"), resJson1,
						resSignatureData1);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			Reporter.log("余额支付接口测试： " + datadriven.get("row_num") + datadriven.get("case_title"), true);
			Reporter.log("##############################【xxxx测试结束】############################", true);
		}
	}

	@DataProvider(name = "paymentBalance")
	public Iterator<Object[]> data4paymentBalance() throws IOException {
		return new ExcelProvider(this, "paymentBalance", 5);
	}

	@AfterClass
	public void afterClass() {

	}

}