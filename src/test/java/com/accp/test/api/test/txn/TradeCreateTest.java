package com.accp.test.api.test.txn;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.accp.test.bean.txn.OrderInfo;
import com.accp.test.bean.txn.PayeeInfo;
import com.accp.test.bean.txn.TradeCreate;
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
 * 支付统一创单接口测试
 */

@ContextConfiguration(locations = { "/consumer.xml" })
public class TradeCreateTest extends AbstractTestNGSpringContextTests {

	DataBaseAccess dataBaseAccess = new DataBaseAccess();

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "支付统一创单接口测试", timeOut = 60000, dataProvider = "tradeCreate")
	public void tradeCreate(Map<String, String> datadriven) throws Exception {

		String tradeCreateUrl = Property.get("tradeCreate.url.test");
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
		String oid_partner = MyConfig.getOidPartner(datadriven.get("oid_partner"));
		String user_id = MyConfig.getUser(datadriven.get("user_id"));
		String productUpdate = datadriven.get("productUpdate");
		String ipUpdate = datadriven.get("ipUpdate");
		String traderStatUpdate = datadriven.get("traderStatUpdate");
		String userStatUpdate = datadriven.get("userStatUpdate");

		try {

			Reporter.log("##############################【xxxx测试开始】############################", true);
			tradeCreate.setTimestamp(MyConfig.getTimestamp(datadriven.get("timestamp")));
			tradeCreate.setOid_partner(oid_partner);
			tradeCreate.setTxn_type(datadriven.get("txn_type"));
			tradeCreate.setUser_id(user_id);
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

			if (!StringUtils.isBlank(productUpdate)) {
				dataBaseAccess.deleteTraderProduct(productUpdate, oid_partner);
			}
			if (!StringUtils.isBlank(ipUpdate)) {
				dataBaseAccess.updateIpRequest(ipUpdate, oid_partner);
			}
			if (!StringUtils.isBlank(traderStatUpdate)) {
				dataBaseAccess.updateTraderEnableState(traderStatUpdate, oid_partner);
			}
			if (!StringUtils.isBlank(userStatUpdate)) {
				dataBaseAccess.updateUserState1(userStatUpdate, user_id, oid_partner);
			}

			String reqJson = JSON.toJSONString(tradeCreate);
			Reporter.log("创单请求报文:" + reqJson, true);
			String sign = SignatureUtil.getInstance().sign(datadriven.get("private_key"), reqJson);
			HttpRequestSimple httpclient = new HttpRequestSimple();
			String[] res = httpclient.post(tradeCreateUrl, reqJson, Property.get("SIGNATURE_TYPE"), sign);
			String resJson = res[0];
			String resSignatureData = res[1];
			Reporter.log("创单返回报文:" + resJson, true);

			// 创单请求返回检查
			assert resJson.contains(datadriven.get("expect_retcode"));
			if ("0000".equals(JSONObject.parseObject(resJson).getString("ret_code"))) {
				assert SignatureUtil.getInstance().checksign(Property.get("YT_RSA_PUBLIC"), resJson, resSignatureData);
				// 支付单状态校验
				BigDecimal AMT = new BigDecimal(String.valueOf(datadriven.get("total_amount")));
				assert "WAIT_PAY"
						.equals(dataBaseAccess.getPayBillState(JSONObject.parseObject(resJson).getString("accp_txno"),
								AMT.multiply(new BigDecimal(1000)).toString()));
			}

			// 请求&响应写入文件
			SampleFileUtils.appendLine("D://TA//accplog//tradeCreate.txt", MyDate.getStringDate());
			SampleFileUtils.appendLine("D://TA//accplog//tradeCreate.txt", reqJson);
			SampleFileUtils.appendLine("D://TA//accplog//tradeCreate.txt", resJson);
			SampleFileUtils.appendLine("D://TA//accplog//tradeCreate.txt",
					"===============================================");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			if (!StringUtils.isBlank(productUpdate)) {
				dataBaseAccess.insertTraderProduct(productUpdate, oid_partner);
			}
			if (!StringUtils.isBlank(ipUpdate)) {
				dataBaseAccess.updateIpRequest("*.*.*.*", oid_partner);
			}
			if (!StringUtils.isBlank(traderStatUpdate)) {
				dataBaseAccess.updateTraderEnableState("0", oid_partner);
			}
			if (!StringUtils.isBlank(userStatUpdate)) {
				dataBaseAccess.updateUserState1("NORMAL", user_id, oid_partner);
			}
			Reporter.log("支付统一创单接口测试： " + datadriven.get("row_num") + datadriven.get("case_title"), true);
			Reporter.log("##############################【xxxx测试结束】############################", true);
		}
	}

	@DataProvider(name = "tradeCreate")
	public Iterator<Object[]> data4tradeCreate() throws IOException {
		return new ExcelProvider(this, "tradeCreate");
	}

	@AfterClass
	public void afterClass() {
		dataBaseAccess.closeDBConnection();
	}

}