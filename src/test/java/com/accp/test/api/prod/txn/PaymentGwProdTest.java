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
import com.accp.test.bean.txn.OrderInfo;
import com.accp.test.bean.txn.PayMethods;
import com.accp.test.bean.txn.PayeeInfo;
import com.accp.test.bean.txn.PayerInfo;
import com.accp.test.bean.txn.PaymentGw;
import com.accp.test.bean.txn.TradeCreate;
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
 * 网关支付接口测试
 */

@ContextConfiguration(locations = { "/consumer.xml" })
public class PaymentGwProdTest extends AbstractTestNGSpringContextTests {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "网关支付接口测试", timeOut = 60000, dataProvider = "paymentGw")
	public void paymentGw(Map<String, String> datadriven) throws Exception {

		String tradeCreateUrl = Property.get("tradeCreate.url.prod");
		String paymentGwUrl = Property.get("paymentGw.url.prod");
		TradeCreate tradeCreate = new TradeCreate();
		OrderInfo orderInfo = new OrderInfo();
		String payeeInfo0 = datadriven.get("payeeInfo0");
		String payeeInfo1 = datadriven.get("payeeInfo1");
		String payeeInfo2 = datadriven.get("payeeInfo2");
		String payeeInfo3 = datadriven.get("payeeInfo3");
		String payeeInfo4 = datadriven.get("payeeInfo4");
		String payeeInfo5 = datadriven.get("payeeInfo5");
		String payeeInfo6 = datadriven.get("payeeInfo6");
		String payeeInfo7 = datadriven.get("payeeInfo7");
		String payeeInfo8 = datadriven.get("payeeInfo8");
		String payeeInfo9 = datadriven.get("payeeInfo9");
		List<PayeeInfo> payeeInfoList = new ArrayList<PayeeInfo>();
		String oid_partner = MyConfig.getOidPartner(datadriven.get("paymentGw.oid_partner"));
		String user_id = MyConfig.getUser(datadriven.get("user_id"));

		try {
			// 创单
			Reporter.log("##############################【xxxx测试开始】############################", true);
			tradeCreate.setTimestamp(MyConfig.getTimestamp(datadriven.get("timestamp")));
			tradeCreate.setOid_partner(MyConfig.getOidPartner(datadriven.get("oid_partner")));
			tradeCreate.setTxn_type(datadriven.get("txn_type"));
			tradeCreate.setUser_id(MyConfig.getUser(datadriven.get("user_id")));
			tradeCreate.setUser_type(datadriven.get("user_type"));
			tradeCreate.setNotify_url(datadriven.get("notify_url"));
			tradeCreate.setReturn_url(datadriven.get("return_url"));
			tradeCreate.setPay_expire(datadriven.get("pay_expire"));

			orderInfo.setTxn_seqno(MyConfig.getTxnSeqno(datadriven.get("txn_seqno")));
			orderInfo.setTxn_time(MyConfig.getTxnTime(datadriven.get("txn_time")));
			orderInfo.setTotal_amount(datadriven.get("total_amount"));
			orderInfo.setOrder_info(datadriven.get("order_info"));
			orderInfo.setGoods_name(datadriven.get("goods_name"));
			orderInfo.setGoods_url(datadriven.get("goods_url"));
			tradeCreate.setOrderInfo(orderInfo);

			if (!StringUtils.isBlank(payeeInfo0)) {
				payeeInfoList.add(JSON.parseObject(payeeInfo0, PayeeInfo.class));
			}
			if (!StringUtils.isBlank(payeeInfo1)) {
				payeeInfoList.add(JSON.parseObject(payeeInfo1, PayeeInfo.class));
			}
			if (!StringUtils.isBlank(payeeInfo2)) {
				payeeInfoList.add(JSON.parseObject(payeeInfo2, PayeeInfo.class));
			}
			if (!StringUtils.isBlank(payeeInfo3)) {
				payeeInfoList.add(JSON.parseObject(payeeInfo3, PayeeInfo.class));
			}
			if (!StringUtils.isBlank(payeeInfo4)) {
				payeeInfoList.add(JSON.parseObject(payeeInfo4, PayeeInfo.class));
			}
			if (!StringUtils.isBlank(payeeInfo5)) {
				payeeInfoList.add(JSON.parseObject(payeeInfo5, PayeeInfo.class));
			}
			if (!StringUtils.isBlank(payeeInfo6)) {
				payeeInfoList.add(JSON.parseObject(payeeInfo6, PayeeInfo.class));
			}
			if (!StringUtils.isBlank(payeeInfo7)) {
				payeeInfoList.add(JSON.parseObject(payeeInfo7, PayeeInfo.class));
			}
			if (!StringUtils.isBlank(payeeInfo8)) {
				payeeInfoList.add(JSON.parseObject(payeeInfo8, PayeeInfo.class));
			}
			if (!StringUtils.isBlank(payeeInfo9)) {
				payeeInfoList.add(JSON.parseObject(payeeInfo9, PayeeInfo.class));
			}
			if (!payeeInfoList.isEmpty()) {
				tradeCreate.setPayeeInfo(payeeInfoList);
			}

			String reqJson = JSON.toJSONString(tradeCreate);
			Reporter.log("创单请求报文:" + reqJson, true);
			String sign = SignatureUtil.getInstance().sign(datadriven.get("private_key"), reqJson);
			HttpRequestSimple httpclient = new HttpRequestSimple();
			String[] res = httpclient.post(tradeCreateUrl, reqJson, Property.get("SIGNATURE_TYPE"), sign);
			String resJson = res[0];
			String resSignatureData = res[1];
			String txn_seqno = JSONObject.parseObject(resJson).getString("txn_seqno");
			String accp_txno = JSONObject.parseObject(resJson).getString("accp_txno");
			Reporter.log("创单返回报文:" + resJson, true);

			// 网关支付
			PaymentGw paymentGw = new PaymentGw();
			PayerInfo payerInfo = new PayerInfo();
			List<PayMethods> payMethods = new ArrayList<PayMethods>();
			String payMethods0 = datadriven.get("paymentGw.payMethods0");
			String payMethods1 = datadriven.get("paymentGw.payMethods1");
			String payMethods2 = datadriven.get("paymentGw.payMethods2");
			String payMethods3 = datadriven.get("paymentGw.payMethods3");

			paymentGw.setTimestamp(MyConfig.getTimestamp(datadriven.get("paymentGw.timestamp")));
			paymentGw.setOid_partner(oid_partner);
			if ("get".equals(datadriven.get("paymentGw.txn_seqno"))) {
				paymentGw.setTxn_seqno(txn_seqno);
			} else {
				paymentGw.setTxn_seqno(datadriven.get("paymentGw.txn_seqno"));
			}
			paymentGw.setTotal_amount(datadriven.get("paymentGw.total_amount"));
			paymentGw.setRisk_item(datadriven.get("paymentGw.risk_item"));
			paymentGw.setAppid(datadriven.get("paymentGw.appid"));
			paymentGw.setOpenid(datadriven.get("paymentGw.openid"));
			paymentGw.setBankcode(datadriven.get("paymentGw.bankcode"));
			paymentGw.setDevice_info(datadriven.get("paymentGw.device_info"));
			paymentGw.setClient_ip(datadriven.get("paymentGw.client_ip"));

			payerInfo.setPayer_type(datadriven.get("paymentGw.payer_type"));
			payerInfo.setPayer_id(datadriven.get("paymentGw.payer_id"));
			payerInfo.setPassword(datadriven.get("paymentGw.password"));
			payerInfo.setRandom_key(datadriven.get("paymentGw.random_key"));
			paymentGw.setPayerInfo(payerInfo);

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
			paymentGw.setPayMethods(payMethods);

			String reqJson1 = JSON.toJSONString(paymentGw);
			Reporter.log("网关支付请求报文:" + reqJson1, true);
			String sign1 = SignatureUtil.getInstance().sign(datadriven.get("paymentGw.private_key"), reqJson1);
			String[] res1 = httpclient.post(paymentGwUrl, reqJson1, Property.get("SIGNATURE_TYPE"), sign1);
			String resJson1 = res1[0];
			String resSignatureData1 = res1[1];
			Reporter.log("网关支付返回报文:" + resJson1, true);

			// 请求&响应写入文件
			SampleFileUtils.appendLine("D://TA//accpprodlog//paymentGw.txt", MyDate.getStringDate());
			SampleFileUtils.appendLine("D://TA//accpprodlog//paymentGw.txt", reqJson);
			SampleFileUtils.appendLine("D://TA//accpprodlog//paymentGw.txt", resJson);
			SampleFileUtils.appendLine("D://TA//accpprodlog//paymentGw.txt", reqJson1);
			SampleFileUtils.appendLine("D://TA//accpprodlog//paymentGw.txt", resJson1);
			SampleFileUtils.appendLine("D://TA//accpprodlog//paymentGw.txt",
					"===============================================");

			// 验签
			assert SignatureUtil.getInstance().checksign(Property.get("YT_RSA_PUBLIC"), resJson1, resSignatureData1);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			Reporter.log("网关支付接口测试： " + datadriven.get("row_num") + datadriven.get("case_title"), true);
			Reporter.log("##############################【xxxx测试结束】############################", true);
		}
	}

	@DataProvider(name = "paymentGw")
	public Iterator<Object[]> data4paymentGw() throws IOException {
		return new ExcelProvider(this, "paymentGw", 3);
	}

	@AfterClass
	public void afterClass() {

	}

}