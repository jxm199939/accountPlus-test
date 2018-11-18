package com.accp.test.api.prod.txn;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.accp.test.bean.txn.BankCardInfo;
import com.accp.test.bean.txn.PayMethods;
import com.accp.test.bean.txn.PayerInfo;
import com.accp.test.bean.txn.PaymentBankcard;
import com.alibaba.dubbo.common.utils.StringUtils;
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
 * 银行卡支付接口测试
 */

@ContextConfiguration(locations = { "/consumer.xml" })
public class PaymentBankcardProdTest extends AbstractTestNGSpringContextTests {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "银行卡支付接口测试", timeOut = 60000, dataProvider = "paymentBankcard")
	public void paymentBankcard(Map<String, String> datadriven) throws Exception {

		String paymentBankcardUrl = Property.get("paymentBankcard.url.prod");
		String oid_partner = MyConfig.getOidPartner(datadriven.get("paymentBankcard.oid_partner"));
		String user_id = MyConfig.getUser(datadriven.get("paymentBankcard.user_id"));

		try {

			Reporter.log("##############################【xxxx测试开始】############################", true);
			PaymentBankcard paymentBankcard = new PaymentBankcard();
			PayerInfo payerInfo = new PayerInfo();
			BankCardInfo bankCardInfo = new BankCardInfo();
			List<PayMethods> payMethods = new ArrayList<PayMethods>();
			String payMethods0 = datadriven.get("paymentBankcard.payMethods0");
			String payMethods1 = datadriven.get("paymentBankcard.payMethods1");
			String payMethods2 = datadriven.get("paymentBankcard.payMethods2");
			String payMethods3 = datadriven.get("paymentBankcard.payMethods3");

			paymentBankcard.setTimestamp(MyConfig.getTimestamp(datadriven.get("paymentBankcard.timestamp")));
			paymentBankcard.setOid_partner(oid_partner);
			paymentBankcard.setTxn_seqno(datadriven.get("paymentBankcard.txn_seqno"));
			paymentBankcard.setTotal_amount(datadriven.get("paymentBankcard.total_amount"));
			paymentBankcard.setRisk_item(datadriven.get("paymentBankcard.risk_item"));

			payerInfo.setUser_id(user_id);
			payerInfo.setPassword(datadriven.get("paymentBankcard.password"));
			payerInfo.setRandom_key(datadriven.get("paymentBankcard.random_key"));
			paymentBankcard.setPayerInfo(payerInfo);

			bankCardInfo.setLinked_acctno(datadriven.get("paymentBankcard.linked_acctno"));
			bankCardInfo.setLinked_agrtno(datadriven.get("paymentBankcard.linked_agrtno"));
			bankCardInfo.setLinked_phone(datadriven.get("paymentBankcard.linked_phone"));
			bankCardInfo.setLinked_acctname(datadriven.get("paymentBankcard.linked_acctname"));
			bankCardInfo.setId_type(datadriven.get("paymentBankcard.id_type"));
			bankCardInfo.setId_no(datadriven.get("paymentBankcard.id_no"));
			bankCardInfo.setCvv2(datadriven.get("paymentBankcard.cvv2"));
			bankCardInfo.setValid_thru(datadriven.get("paymentBankcard.valid_thru"));
			paymentBankcard.setBankCardInfo(bankCardInfo);

			if (!StringUtils.isBlank(payMethods0)) {
				payMethods.add(JSON.parseObject(payMethods0, PayMethods.class));
			}
			if (!StringUtils.isBlank(payMethods1)) {
				payMethods.add(JSON.parseObject(payMethods1, PayMethods.class));
			}
			if (!StringUtils.isBlank(payMethods2)) {
				payMethods.add(JSON.parseObject(payMethods2, PayMethods.class));
			}
			if (!StringUtils.isBlank(payMethods3)) {
				payMethods.add(JSON.parseObject(payMethods3, PayMethods.class));
			}
			paymentBankcard.setPayMethods(payMethods);

			String reqJson1 = JSON.toJSONString(paymentBankcard);
			Reporter.log("银行卡支付请求报文:" + reqJson1, true);
			String sign1 = SignatureUtil.getInstance().sign(datadriven.get("paymentBankcard.private_key"), reqJson1);
			HttpRequestSimple httpclient = new HttpRequestSimple();
			String[] res1 = httpclient.post(paymentBankcardUrl, reqJson1, Property.get("SIGNATURE_TYPE"), sign1);
			String resJson1 = res1[0];
			String resSignatureData1 = res1[1];
			Reporter.log("银行卡支付返回报文:" + resJson1, true);

			// 请求&响应写入文件
			SampleFileUtils.appendLine("D://TA//accpprodlog//paymentBankcard.txt", MyDate.getStringDate());
			SampleFileUtils.appendLine("D://TA//accpprodlog//paymentBankcard.txt", reqJson1);
			SampleFileUtils.appendLine("D://TA//accpprodlog//paymentBankcard.txt", resJson1);
			SampleFileUtils.appendLine("D://TA//accpprodlog//paymentBankcard.txt",
					"===============================================");

			// 银行卡支付请求返回检查
			assert resJson1.contains(datadriven.get("expect_retmsg"));
			if ("0000".equals(JSONObject.parseObject(resJson1).getString("ret_code"))) {
				assert SignatureUtil.getInstance().checksign(Property.get("YT_RSA_PUBLIC"), resJson1,
						resSignatureData1);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			Reporter.log("银行卡支付接口测试： " + datadriven.get("row_num") + datadriven.get("case_title"), true);
			Reporter.log("##############################【xxxx测试结束】############################", true);
		}
	}

	@DataProvider(name = "paymentBankcard")
	public Iterator<Object[]> data4paymentBankcard() throws IOException {
		return new ExcelProvider(this, "paymentBankcard", 10);
	}

	@AfterClass
	public void afterClass() {

	}

}