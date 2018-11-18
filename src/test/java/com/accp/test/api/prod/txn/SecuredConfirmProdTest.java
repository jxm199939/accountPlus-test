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
import com.accp.test.bean.txn.ConfirmOrderInfo;
import com.accp.test.bean.txn.OriginalOrderInfo;
import com.accp.test.bean.txn.PayeeInfo;
import com.accp.test.bean.txn.SecuredConfirm;
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
 * 担保交易确认接口测试
 */

@ContextConfiguration(locations = { "/consumer.xml" })
public class SecuredConfirmProdTest extends AbstractTestNGSpringContextTests {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "担保交易确认接口测试", timeOut = 60000, dataProvider = "securedConfirm")
	public void securedConfirm(Map<String, String> datadriven) throws Exception {

		String securedConfirmUrl = Property.get("securedConfirm.url.prod");
		String oid_partner = MyConfig.getOidPartner(datadriven.get("securedConfirm.oid_partner"));
		String user_id = MyConfig.getUser(datadriven.get("securedConfirm.user_id"));

		try {

			Reporter.log("##############################【xxxx测试开始】############################", true);
			SecuredConfirm securedConfirm = new SecuredConfirm();
			OriginalOrderInfo originalOrderInfo = new OriginalOrderInfo();
			ConfirmOrderInfo confirmOrderInfo = new ConfirmOrderInfo();
			String payeeInfo10 = datadriven.get("securedConfirm.payeeInfo0");
			String payeeInfo11 = datadriven.get("securedConfirm.payeeInfo1");
			String payeeInfo12 = datadriven.get("securedConfirm.payeeInfo2");
			String payeeInfo13 = datadriven.get("securedConfirm.payeeInfo3");
			String payeeInfo14 = datadriven.get("securedConfirm.payeeInfo4");
			String payeeInfo15 = datadriven.get("securedConfirm.payeeInfo5");
			String payeeInfo16 = datadriven.get("securedConfirm.payeeInfo6");
			String payeeInfo17 = datadriven.get("securedConfirm.payeeInfo7");
			String payeeInfo18 = datadriven.get("securedConfirm.payeeInfo8");
			String payeeInfo19 = datadriven.get("securedConfirm.payeeInfo9");
			List<PayeeInfo> payeeInfoList1 = new ArrayList<PayeeInfo>();

			securedConfirm.setTimestamp(MyConfig.getTimestamp(datadriven.get("securedConfirm.timestamp")));
			securedConfirm.setOid_partner(oid_partner);
			securedConfirm.setUser_id(user_id);
			securedConfirm.setNotify_url(datadriven.get("securedConfirm.notify_url"));
			securedConfirm.setConfirm_mode(datadriven.get("securedConfirm.confirm_mode"));

			originalOrderInfo.setTxn_seqno(datadriven.get("securedConfirm.txn_seqno"));
			originalOrderInfo.setTotal_amount(datadriven.get("securedConfirm.total_amount"));
			securedConfirm.setOriginalOrderInfo(originalOrderInfo);

			confirmOrderInfo.setConfirm_seqno(MyConfig.getConfirmSeqno(datadriven.get("securedConfirm.confirm_seqno")));
			confirmOrderInfo.setConfirm_time(MyConfig.getConfirmTime(datadriven.get("securedConfirm.confirm_time")));
			confirmOrderInfo.setConfirm_amount(datadriven.get("securedConfirm.confirm_amount"));
			securedConfirm.setConfirmOrderInfo(confirmOrderInfo);

			if (!StringUtils.isBlank(payeeInfo10)) {
				payeeInfoList1.add(JSON.parseObject(payeeInfo10, PayeeInfo.class));
			}
			if (!StringUtils.isBlank(payeeInfo11)) {
				payeeInfoList1.add(JSON.parseObject(payeeInfo11, PayeeInfo.class));
			}
			if (!StringUtils.isBlank(payeeInfo12)) {
				payeeInfoList1.add(JSON.parseObject(payeeInfo12, PayeeInfo.class));
			}
			if (!StringUtils.isBlank(payeeInfo13)) {
				payeeInfoList1.add(JSON.parseObject(payeeInfo13, PayeeInfo.class));
			}
			if (!StringUtils.isBlank(payeeInfo14)) {
				payeeInfoList1.add(JSON.parseObject(payeeInfo14, PayeeInfo.class));
			}
			if (!StringUtils.isBlank(payeeInfo15)) {
				payeeInfoList1.add(JSON.parseObject(payeeInfo15, PayeeInfo.class));
			}
			if (!StringUtils.isBlank(payeeInfo16)) {
				payeeInfoList1.add(JSON.parseObject(payeeInfo16, PayeeInfo.class));
			}
			if (!StringUtils.isBlank(payeeInfo17)) {
				payeeInfoList1.add(JSON.parseObject(payeeInfo17, PayeeInfo.class));
			}
			if (!StringUtils.isBlank(payeeInfo18)) {
				payeeInfoList1.add(JSON.parseObject(payeeInfo18, PayeeInfo.class));
			}
			if (!StringUtils.isBlank(payeeInfo19)) {
				payeeInfoList1.add(JSON.parseObject(payeeInfo19, PayeeInfo.class));
			}
			if (!payeeInfoList1.isEmpty()) {
				securedConfirm.setPayeeInfo(payeeInfoList1);
			}

			String reqJson3 = JSON.toJSONString(securedConfirm);
			Reporter.log("担保交易确认请求报文:" + reqJson3, true);
			String sign3 = SignatureUtil.getInstance().sign(datadriven.get("securedConfirm.private_key"), reqJson3);
			HttpRequestSimple httpclient = new HttpRequestSimple();
			String[] res3 = httpclient.post(securedConfirmUrl, reqJson3, Property.get("SIGNATURE_TYPE"), sign3);
			String resJson3 = res3[0];
			String resSignatureData3 = res3[1];
			Reporter.log("担保交易确认返回报文:" + resJson3, true);

			// 请求&响应写入文件
			SampleFileUtils.appendLine("D://TA//accpprodlog//securedConfirm.txt", MyDate.getStringDate());
			SampleFileUtils.appendLine("D://TA//accpprodlog//securedConfirm.txt", reqJson3);
			SampleFileUtils.appendLine("D://TA//accpprodlog//securedConfirm.txt", resJson3);
			SampleFileUtils.appendLine("D://TA//accpprodlog//securedConfirm.txt",
					"===============================================");

			// 担保交易确认请求返回检查
			assert resJson3.contains(datadriven.get("expect_retcode"));
			if ("0000".equals(JSONObject.parseObject(resJson3).getString("ret_code"))) {
				assert SignatureUtil.getInstance().checksign(Property.get("YT_RSA_PUBLIC"), resJson3,
						resSignatureData3);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			Reporter.log("担保交易确认接口测试： " + datadriven.get("row_num") + datadriven.get("case_title"), true);
			Reporter.log("##############################【xxxx测试结束】############################", true);
		}
	}

	@DataProvider(name = "securedConfirm")
	public Iterator<Object[]> data4securedConfirm() throws IOException {
		return new ExcelProvider(this, "securedConfirm", 3);
	}

	@AfterClass
	public void afterClass() {

	}

}