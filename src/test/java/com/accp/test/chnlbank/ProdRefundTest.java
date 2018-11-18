package com.accp.test.chnlbank;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.accp.test.bean.txn.OriginalOrderInfo;
import com.accp.test.bean.txn.Refund;
import com.accp.test.bean.txn.RefundMethods;
import com.accp.test.bean.txn.RefundOrderInfo;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.fastjson.JSON;
import com.tools.dataprovider.ExcelProvider;
import com.tools.utils.DateUtil;
import com.tools.utils.HttpRequestSimple;
import com.tools.utils.MyConfig;
import com.tools.utils.MyDate;
import com.tools.utils.Property;
import com.tools.utils.RandomUtils;
import com.tools.utils.SampleFileUtils;
import com.tools.utils.SignatureUtil;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

/**
 * @author wanglin002
 * 退款接口测试
 */

@ContextConfiguration(locations = { "/consumer.xml" })
public class ProdRefundTest extends AbstractTestNGSpringContextTests {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}
	DateUtil dateUtil = new DateUtil();
	RandomUtils randomUtils = new RandomUtils();

	@Test(description = "退款接口测试", timeOut = 60000, dataProvider = "refund")
	public void refund(Map<String, String> datadriven) throws Exception {

		String refundUrl = Property.get("refund.url.prod");
		String oid_partner = MyConfig.getOidPartner(datadriven.get("refund.oid_partner"));
		String user_id = MyConfig.getUser(datadriven.get("refund.user_id"));
		String dt_order = dateUtil.getCurrentDateTimeStr();// 订单时间yyyyMMddHHmmss
		String no_order = "ACCPREFUND" + dt_order + randomUtils.getRandom(10);// 订单号

		try {

			Reporter.log("##############################【退款测试开始】############################", true);
			Refund refund = new Refund();
			OriginalOrderInfo originalOrderInfo1 = new OriginalOrderInfo();
			RefundOrderInfo refundOrderInfo = new RefundOrderInfo();
			String refundMethods0 = datadriven.get("refund.refundMethods0");
			List<RefundMethods> refundMethods = new ArrayList<RefundMethods>();

			refund.setTimestamp(dt_order);
			refund.setOid_partner(oid_partner);
			refund.setUser_id(user_id);
			refund.setNotify_url(datadriven.get("refund.notify_url"));
			refund.setRefund_reason(datadriven.get("refund.refund_reason"));

			originalOrderInfo1.setTxn_seqno(datadriven.get("refund.txn_seqno"));
			originalOrderInfo1.setTotal_amount(datadriven.get("refund.total_amount"));
			refund.setOriginalOrderInfo(originalOrderInfo1);

			refundOrderInfo.setRefund_seqno(no_order);
			refundOrderInfo.setRefund_time(dt_order);
			refundOrderInfo.setPayee_id(datadriven.get("refund.payee_id"));
			refundOrderInfo.setPayee_type(datadriven.get("refund.payee_type"));
			refundOrderInfo.setPayee_accttype(datadriven.get("refund.payee_accttype"));
			refundOrderInfo.setRefund_amount(datadriven.get("refund.refund_amount"));
			refund.setRefundOrderInfo(refundOrderInfo);

			if (!StringUtils.isBlank(refundMethods0)) {
				refundMethods.add(JSON.parseObject(refundMethods0, RefundMethods.class));
			}
			refund.setRefundMethods(refundMethods);

			String reqJson4 = JSON.toJSONString(refund);
			Reporter.log("退款请求报文:" + reqJson4, true);
			String sign4 = SignatureUtil.getInstance().sign(datadriven.get("refund.private_key"), reqJson4);
			HttpRequestSimple httpclient = new HttpRequestSimple();
			String[] res4 = httpclient.post(refundUrl, reqJson4, Property.get("SIGNATURE_TYPE"), sign4);
			String resJson4 = res4[0];
			String resSignatureData4 = res4[1];
			Reporter.log("退款返回报文:" + resJson4, true);

			// 请求&响应写入文件
			SampleFileUtils.appendLine("D://TA//accpprodlog//refund.txt", MyDate.getStringDate());
			SampleFileUtils.appendLine("D://TA//accpprodlog//refund.txt", reqJson4);
			SampleFileUtils.appendLine("D://TA//accpprodlog//refund.txt", resJson4);
			SampleFileUtils.appendLine("D://TA//accpprodlog//refund.txt",
					"===============================================");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			Reporter.log("退款接口测试： " + datadriven.get("row_num") + datadriven.get("case_title"), true);
			Reporter.log("##############################【退款测试结束】############################", true);
		}
	}

	@DataProvider(name = "refund")
	public Iterator<Object[]> data4refund() throws IOException {
		return new ExcelProvider(this, "refund",12);
	}

	@AfterClass
	public void afterClass() {

	}

}