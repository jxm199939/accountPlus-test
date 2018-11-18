package com.tools.utils;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import com.lianlian.crypt.exception.AESException;
import com.lianlian.crypt.service.IAESCryptService;

/**
 * 数据库操作
 * 
 * @author wanglin002
 */

public class DataBaseAccess extends AbstractTestNGSpringContextTests {

	private static Log log = LogFactory.getLog(DataBaseAccess.class);
	public Connection dbpayconn = DBRes.getPaymentConnection();
	public Connection dbcustconn = DBRes.getCustomerConnection();
	public Connection dbaccountconn = DBRes.getAccountConnection();
	public Connection dbcheckconn = DBRes.getCheckConnection();
	public Connection dbchnlconn = DBRes.getChnlConnection();

	PreparedStatement pstmt = null;
	public ResultSet results = null;
	int resultNum = 0;
	boolean flag = true;

	/**
	 * 关闭数据库连接
	 * 
	 * @throws SQLException
	 */
	public void closeDBConnection() {
		try {
			if (!dbpayconn.isClosed()) {
				dbpayconn.close();
				log.info("    【dbpayconn状态：" + dbpayconn.getCatalog() + "】");
			}
			if (!dbcustconn.isClosed()) {
				dbcustconn.close();
				log.info("    【dbcustconn状态：" + dbcustconn.getCatalog() + "】");
			}
			if (!dbaccountconn.isClosed()) {
				dbaccountconn.close();
				log.info("    【dbaccountconn状态：" + dbaccountconn.getCatalog() + "】");
			}
			if (!dbcheckconn.isClosed()) {
				dbcheckconn.close();
				log.info("    【dbcheckconn状态：" + dbcheckconn.getCatalog() + "】");
			}
			if (!dbchnlconn.isClosed()) {
				dbchnlconn.close();
				log.info("    【dbchnlconn状态：" + dbchnlconn.getCatalog() + "】");
			}
			if (results != null) {
				results.close();
				log.info("    【results状态：" + results.getConcurrency() + "】");
			}

		} catch (SQLException e) {
			e.printStackTrace();
			log.info("    【关闭数据库连接失败：" + e.toString() + "】");
		}
	}

	/**
	 * 获取短信验证码
	 */
	public String getSms(String phone) throws SQLException {

		String sql = "";
		String CODE_MESSAGE = "";

		try {
			Thread.sleep(1000);

			sql = "SELECT CODE_MESSAGE FROM(SELECT CODE_MESSAGE FROM TB_VALIDATIONCODE where TRIM(RECEIVER)='" + phone
					+ "' ORDER BY CREATE_TIME DESC) a LIMIT 0,1";
			pstmt = DBRes.getPreparedStatement(dbcustconn, sql);
			results = DBRes.getResultSet(pstmt);

			while (results != null && results.next()) {
				CODE_MESSAGE = results.getString("CODE_MESSAGE");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return CODE_MESSAGE;
	}

	/**
	 * 获取绑定手机号
	 */
	public String getRegPhone(String user_id, String oid_partner) throws SQLException {

		String sql = "";
		String REG_PHONE = "";

		try {

			sql = "SELECT REG_PHONE FROM ACCP_CUST_INFO WHERE USER_ID='" + user_id + "' AND OID_PARTNER = '"
					+ oid_partner + "'";
			pstmt = DBRes.getPreparedStatement(dbcustconn, sql);
			results = DBRes.getResultSet(pstmt);

			while (results != null && results.next()) {
				REG_PHONE = results.getString("REG_PHONE");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return REG_PHONE;
	}

	/**
	 * 获取用户号、绑定手机号
	 */
	public String[] getUserInfo(IAESCryptService aesCryptService, String user_id, String oid_partner)
			throws SQLException {

		String sql = "";
		String[] userInfo = new String[2];

		try {

			sql = "SELECT OID_USERNO,REG_PHONE FROM ACCP_CUST_INFO WHERE USER_ID='" + user_id + "' AND OID_PARTNER = '"
					+ oid_partner + "'";
			pstmt = DBRes.getPreparedStatement(dbcustconn, sql);
			results = DBRes.getResultSet(pstmt);

			while (results != null && results.next()) {
				userInfo[0] = results.getString("OID_USERNO");
				// userInfo[1] = results.getString("REG_PHONE");
				userInfo[1] = new String(aesCryptService.decrypt(results.getString("REG_PHONE")), "UTF-8");

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return userInfo;
	}

	/**
	 * 获取绑卡协议号
	 */
	public String getAgreementNo(String user_id, String oid_partner) throws SQLException {

		String sql = "";
		String AGREEMENTNO = "";

		try {

			sql = "SELECT RECORD_INFO_ID FROM(SELECT RECORD_INFO_ID FROM TC_INFO_SIGNED where OID_CUST='" + user_id
					+ "' AND OID_TRADERNO='" + oid_partner
					+ "' AND AGREEMENT_S='SIGN_SUCCESS' AND CARDTYPE='DEBIT' ORDER BY UPDATE_TIME DESC) a LIMIT 0,1";
			pstmt = DBRes.getPreparedStatement(dbchnlconn, sql);
			results = DBRes.getResultSet(pstmt);

			while (results != null && results.next()) {
				AGREEMENTNO = results.getString("RECORD_INFO_ID");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return AGREEMENTNO;
	}

	// 查询支付单状态
	public String getPayBillState(String OID_BILLNO, String amt) throws UnsupportedEncodingException, AESException {
		String sql = "";
		String STAT_BILL = "";

		try {
			sql = "select PAY_BILL_STATUS from TB_PAY_BILL where PAY_BILL_NO = '" + OID_BILLNO
					+ "' and AMT_PAY_BILL = '" + amt + "' ";

			pstmt = DBRes.getPreparedStatement(dbpayconn, sql);
			results = DBRes.getResultSet(pstmt);
			while (results != null && results.next()) {
				STAT_BILL = results.getString("PAY_BILL_STATUS");
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.info("    【查询支付单状态失败：" + e.toString() + "】");
		}
		return STAT_BILL;
	}

	/**
	 * 根据支付单号获取成功的银行卡流水
	 */
	public String getCashFinserialNo(String payBillNo) throws SQLException {

		String sql = "";
		String OUT_SERIAL = "";

		try {

			sql = "SELECT OUT_SERIAL FROM TB_CASH_FINSERIAL t where t.OID_BILLNO='" + payBillNo
					+ "' AND t.STAT='PAY_SUCCESS' ";
			pstmt = DBRes.getPreparedStatement(dbpayconn, sql);
			results = DBRes.getResultSet(pstmt);

			while (results != null && results.next()) {
				OUT_SERIAL = results.getString("OUT_SERIAL");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return OUT_SERIAL;
	}

	/**
	 * 根据支付单号获取成功的付款流水
	 */
	public String getFinserialNo(String payBillNo) throws SQLException {

		String sql = "";
		String OUT_SERIAL = "";

		try {

			sql = "SELECT OID_BILLNO FROM TB_FINSERIAL t where t.OID_BILLNO='" + payBillNo
					+ "' AND t.STAT='PAY_SUCCESS' ";
			pstmt = DBRes.getPreparedStatement(dbpayconn, sql);
			results = DBRes.getResultSet(pstmt);

			while (results != null && results.next()) {
				OUT_SERIAL = results.getString("OUT_SERIAL");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return OUT_SERIAL;
	}

	// 付款流水信息校验
	public boolean finSerialCheck(String payBillNo, String amt, String biz, String chnlcode, String merchantId,
			String acctNo, String custType, String payType, String custNo, String custAcctNo, String state) {

		String sql = "";
		String money = "";
		String OID_BIZ = "";
		String AMT_PAYBILL = "";
		String BANKCODE = "";
		String TRADECODE = "";
		String TRADEACCTNO = "";
		String CUST_TYPE = "";
		String PAY_TYPE = "";
		String CUSTNO = "";
		String CUSTACCTNO = "";
		String DATE_ACCT = "";
		String STAT = "";

		boolean finserialStateCheck = true;

		try {
			String oidSerialNo = getFinserialNo(payBillNo);
			// 付款流水表
			sql = "select * from tb_finserial where OID_SERIALNO = '" + oidSerialNo + "'";
			pstmt = DBRes.getPreparedStatement(dbchnlconn, sql);
			results = DBRes.getResultSet(pstmt);

			while (results != null && results.next()) {

				money = results.getString("AMT_PAYBILL");
				// 进行金额单位转换
				BigDecimal amount1 = new BigDecimal(money);
				BigDecimal b = new BigDecimal(1000);
				BigDecimal amount = amount1.divide(b);
				AMT_PAYBILL = String.valueOf(amount);

				OID_BIZ = results.getString("OID_BIZ");
				BANKCODE = results.getString("OID_CHNL");
				TRADECODE = results.getString("TRADECODE");
				TRADEACCTNO = results.getString("TRADEACCTNO");
				CUST_TYPE = results.getString("CUST_TYPE");
				PAY_TYPE = results.getString("PAY_TYPE");
				CUSTNO = results.getString("CUSTNO");
				CUSTACCTNO = results.getString("CUSTACCTNO");
				STAT = results.getString("STAT");
				DATE_ACCT = results.getString("DATE_ACCT");

			}

			if (AMT_PAYBILL != null && AMT_PAYBILL.equals(amt)) {
				log.info("    【付款流水金额校验成功】");
			} else {
				log.info("    【付款流水金额校验失败：AMT_PAYBILL=" + AMT_PAYBILL + "】");
				finserialStateCheck = false;
			}

			if (OID_BIZ != null && OID_BIZ.equals(biz)) {
				log.info("    【付款流水业务名称校验成功】");
			} else {
				log.info("    【付款流水业务名称校验失败：OID_BIZ=" + OID_BIZ + "】");
				finserialStateCheck = false;
			}

			if (BANKCODE != null && BANKCODE.equals(chnlcode)) {
				log.info("    【付款流水渠道编码校验成功】");
			} else {
				log.info("    【付款渠道编码校验失败：BANKCODE=" + BANKCODE + "】");
				finserialStateCheck = false;
			}

			if (TRADECODE != null && TRADECODE.equals(merchantId)) {
				log.info("    【付款流水渠道商户号校验成功】");
			} else {
				log.info("    【付款流水渠道商户号校验失败：TRADECODE=" + TRADECODE + "】");
				finserialStateCheck = false;
			}

			if (TRADEACCTNO != null && TRADEACCTNO.equals(acctNo)) {
				log.info("    【付款流水交易账号校验成功】");
			} else {
				log.info("    【付款流水交易账号校验失败：TRADEACCTNO=" + TRADEACCTNO + "】");
				finserialStateCheck = false;
			}

			if (CUST_TYPE != null && CUST_TYPE.equals(custType)) {
				log.info("    【付款流水客户类型校验成功】");
			} else {
				log.info("    【付款流水客户类型校验失败：CUST_TYPE=" + CUST_TYPE + "】");
				finserialStateCheck = false;
			}
			if (PAY_TYPE != null && PAY_TYPE.equals(payType)) {
				log.info("    【付款流水付款方式校验成功】");
			} else {
				log.info("    【付款流水付款方式校验失败：PAY_TYPE=" + PAY_TYPE + "】");
				finserialStateCheck = false;
			}
			if (CUSTNO != null && CUSTNO.equals(custNo)) {
				log.info("    【付款流水客户号校验成功】");
			} else {
				log.info("    【付款流水客户号校验失败：CUSTNO=" + CUSTNO + "】");
				finserialStateCheck = false;
			}
			if (CUSTACCTNO != null && CUSTACCTNO.equals(custAcctNo)) {
				log.info("    【付款流水客户卡号校验成功】");
			} else {
				log.info("    【付款流水客户卡号校验失败：CUSTACCTNO=" + CUSTACCTNO + "】");
				finserialStateCheck = false;
			}

			Date now = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
			String date = dateFormat.format(now);

			if (DATE_ACCT != null && DATE_ACCT.equals(date)) {
				log.info("    【付款流水账务日期校验成功】");
			} else {
				log.info("    【付款流水账务日期校验失败：DATE_ACCT=" + DATE_ACCT + "】");
				finserialStateCheck = false;
			}
			if (STAT != null && STAT.equals(state)) {
				log.info("    【付款流水状态校验成功】");
			} else {
				log.info("    【付款流水状态校验失败：STAT=" + STAT + "】");
				finserialStateCheck = false;
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.info("    【付款流水校验异常：异常原因：" + e.toString() + "】");
			finserialStateCheck = false;
		}
		return finserialStateCheck;

	}

	// 支付流水信息校验
	public boolean payBillSerialCheck(String amt, String payBillNo, String serialType, String payBillSerialState,
			String curCode, String payMode, String pyerType, String pyerId, String pyerUserNo, String pyerAcctType,
			String pyerAcctId, String pyerTxStat, String pyerTxChnlCode, String pyerTxType, String pyeeId,
			String pyeeUserNo, String pyeeType, String pyeeAcctType, String pyeeAcctId, String pyeeTxStat,
			String pyeeTxChnlCode, String pyeeTxType, String refundAmt) {

		String sql = "";
		String money = "";
		String PAY_SERIAL_AMT = "";
		String CUR_CODE = "";
		String DATE_ACCT = "";
		String SERIAL_TYPE = "";
		String SERIAL_STATUS = "";
		String PAY_MODE = "";
		String PYER_TYPE = "";
		String PYER_ID = "";
		String PYER_USERNO = "";
		String PYER_ACCT_TYPE = "";
		String PYER_ACCT_ID = "";
		String PYER_TX_STATUS = "";
		String PYER_TX_CHNL_CODE = "";
		String PYER_TX_TYPE = "";
		String PYEE_ID = "";
		String PYEE_USERNO = "";
		String PYEE_TYPE = "";
		String PYEE_ACCT_TYPE = "";
		String PYEE_ACCT_ID = "";
		String PYEE_TX_STATUS = "";
		String PYEE_TX_CHNL_CODE = "";
		String PYEE_TX_TYPE = "";
		String REFUND_AMT = "";
		String FREEZE_NO = "";
		boolean paybillSerialStateCheck = true;

		try {
			// 支付流水
			sql = "select * from TB_PAY_BILL_SERIAL where PAY_BILL_NO = '" + payBillNo + "'";
			pstmt = DBRes.getPreparedStatement(dbpayconn, sql);
			results = DBRes.getResultSet(pstmt);

			while (results != null && results.next()) {
				money = results.getString("AMT_PAYSERIAL");
				// 进行金额单位转换
				BigDecimal amount1 = new BigDecimal(money);
				BigDecimal b = new BigDecimal(1000);
				BigDecimal amount = amount1.divide(b);
				PAY_SERIAL_AMT = String.valueOf(amount);

				DATE_ACCT = results.getString("DATE_ACCT");
				SERIAL_TYPE = results.getString("SERIAL_TYPE");
				CUR_CODE = results.getString("CUR_CODE");
				PAY_MODE = results.getString("PAY_MODE");
				PYER_TYPE = results.getString("PYER_TYPE");
				PYER_ID = results.getString("PYER_ID");
				PYER_USERNO = results.getString("PYER_USERNO");
			}

			if (PAY_SERIAL_AMT != null && PAY_SERIAL_AMT.equals(amt)) {
				log.info("    【支付流水支付金额校验成功】");
			} else {
				log.info("    【支付流水支付金额校验失败：AMT_PAYBILL=" + PAY_SERIAL_AMT + ",amt=" + amt + "】");
				paybillSerialStateCheck = false;
			}

			Date now = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
			String date = dateFormat.format(now);

			if (DATE_ACCT != null && DATE_ACCT.equals(date)) {
				log.info("    【支付流水账务日期校验成功】");
			} else {
				log.info("    【支付流水账务日期校验失败：DATE_ACCT=" + DATE_ACCT + "】");
				paybillSerialStateCheck = false;
			}

			if (SERIAL_TYPE != null && SERIAL_TYPE.equals(serialType)) {
				logger.info("    【支付流水流水类型校验成功】");
			} else {
				log.info("    【支付流水流水类型校验失败：PAY_TYPE=" + SERIAL_TYPE + "】");
				paybillSerialStateCheck = false;
			}

			if (CUR_CODE != null && CUR_CODE.equals(curCode)) {
				logger.info("    【支付流水币种成功】");
			} else {
				log.info("    【支付流水币种校验失败：PAY_TYPE=" + CUR_CODE + "】");
				paybillSerialStateCheck = false;
			}
			if (SERIAL_STATUS != null && SERIAL_STATUS.equals(payBillSerialState)) {
				log.info("    【支付流水状态校验成功】");
			} else {
				log.info("    【支付流水状态校验失败：STAT_SERIAL=" + SERIAL_STATUS + "】");
				paybillSerialStateCheck = false;
			}

			if (PAY_MODE != null && PAY_MODE.equals(payMode)) {
				log.info("    【支付流水支付模式校验成功】");
			} else {
				log.info("    【支付流水支付模式校验失败：PAY_MODE=" + PAY_MODE + "】");
				paybillSerialStateCheck = false;
			}

			if (PYER_TYPE != null && PYER_TYPE.equals(pyerType)) {
				log.info("    【支付流水付款方类型校验成功】");
			} else {
				log.info("    【支付流水付款方类型校验失败：PYER_TYPE=" + PYER_TYPE + "】");
				paybillSerialStateCheck = false;
			}
			if (PYER_ID != null && PYER_ID.equals(pyerId)) {
				log.info("    【支付流水付款方ID校验成功】");
			} else {
				log.info("    【支付流水付款方ID校验失败：PYER_ID=" + PYER_ID + "】");
				paybillSerialStateCheck = false;
			}
			if (PYER_USERNO != null && PYER_USERNO.equals(pyerUserNo)) {
				log.info("    【支付流水付款客户号校验成功】");
			} else {
				log.info("    【支付流水付款客户号校验失败：PYER_USERNO=" + PYER_USERNO + "】");
				paybillSerialStateCheck = false;
			}
			if (PYER_ACCT_TYPE != null && PYER_ACCT_TYPE.equals(pyerAcctType)) {
				log.info("    【支付流水付款方账户类型校验成功】");
			} else {
				log.info("    【支付流水付款方账户类型校验失败：PYER_ACCT_TYPE=" + PYER_ACCT_TYPE + "】");
				paybillSerialStateCheck = false;
			}
			if (PYER_ACCT_ID != null && PYER_ACCT_ID.equals(pyerAcctId)) {
				log.info("    【支付流水付款方帐号校验成功】");
			} else {
				log.info("    【支付流水付款方帐号校验失败：PYER_ACCT_ID=" + PYER_ACCT_ID + "】");
				paybillSerialStateCheck = false;
			}
			if (PYER_TX_STATUS != null && PYER_TX_STATUS.equals(pyerTxStat)) {
				log.info("    【支付流水状态校验成功】");
			} else {
				log.info("    【支付流水状态校验失败：PYER_TX_STATUS=" + PYER_TX_STATUS + "】");
				paybillSerialStateCheck = false;
			}
			if (PYER_TX_CHNL_CODE != null && PYER_TX_CHNL_CODE.equals(pyerTxChnlCode)) {
				log.info("    【支付流水付款方渠道校验成功】");
			} else {
				log.info("    【支付流水付款方渠道校验失败：PYER_TX_CHNL_CODE=" + PYER_TX_CHNL_CODE + "】");
				paybillSerialStateCheck = false;
			}
			if (PYER_TX_TYPE != null && PYER_TX_TYPE.equals(pyerTxType)) {
				log.info("    【支付流水付款类型校验成功】");
			} else {
				log.info("    【支付流水付款类型校验失败：PYER_TX_TYPE=" + PYER_TX_TYPE + "】");
				paybillSerialStateCheck = false;
			}
			if (PYEE_ID != null && PYEE_ID.equals(pyeeId)) {
				log.info("    【支付流水收款方ID校验成功】");
			} else {
				log.info("    【支付流水收款方ID校验失败：PYEE_ID=" + PYEE_ID + "】");
				paybillSerialStateCheck = false;
			}
			if (PYEE_USERNO != null && PYEE_USERNO.equals(pyeeUserNo)) {
				log.info("    【支付流水收款方用户号校验成功】");
			} else {
				log.info("    【支付流水收款方用户号校验失败：PYEE_USERNO=" + PYEE_USERNO + "】");
				paybillSerialStateCheck = false;
			}
			if (PYEE_TYPE != null && PYEE_TYPE.equals(pyeeType)) {
				log.info("    【支付流水收款方客户类型校验成功】");
			} else {
				log.info("    【支付流水收款方客户类型校验失败：PYEE_TYPE=" + PYEE_TYPE + "】");
				paybillSerialStateCheck = false;
			}
			if (PYEE_ACCT_TYPE != null && PYEE_ACCT_TYPE.equals(pyeeAcctType)) {
				log.info("    【支付流水收款方账户类型校验成功】");
			} else {
				log.info("    【支付流水收款方账户类型校验失败：PYEE_ACCT_TYPE=" + PYEE_ACCT_TYPE + "】");
				paybillSerialStateCheck = false;
			}
			if (PYEE_ACCT_ID != null && PYEE_ACCT_ID.equals(pyeeAcctId)) {
				log.info("    【支付流水收款方账户校验成功】");
			} else {
				log.info("    【支付流水收款方账号校验失败：PYEE_ACCT_ID=" + PYEE_ACCT_ID + "】");
				paybillSerialStateCheck = false;
			}
			if (PYEE_TX_STATUS != null && PYEE_TX_STATUS.equals(pyeeTxStat)) {
				log.info("    【支付流水收款状态校验成功】");
			} else {
				log.info("    【支付流水收款状态校验失败：PYEE_TX_STATUS=" + PYEE_TX_STATUS + "】");
				paybillSerialStateCheck = false;
			}
			if (PYEE_TX_CHNL_CODE != null && PYEE_TX_CHNL_CODE.equals(pyeeTxChnlCode)) {
				log.info("    【支付流水收款渠道校验成功】");
			} else {
				log.info("    【支付流水收款渠道校验失败：PYEE_TX_CHNL_CODE=" + PYEE_TX_CHNL_CODE + "】");
				paybillSerialStateCheck = false;
			}
			if (PYEE_TX_TYPE != null && PYEE_TX_TYPE.equals(pyeeTxType)) {
				log.info("    【支付流水收款方式校验成功】");
			} else {
				log.info("    【支付流水收款方式校验失败：PYEE_TX_TYPE=" + PYEE_TX_TYPE + "】");
				paybillSerialStateCheck = false;
			}
			if (REFUND_AMT != null && REFUND_AMT.equals(refundAmt)) {
				log.info("    【支付流水已退金额校验成功】");
			} else {
				log.info("    【支付流水已退金额校验失败：REFUND_AMT=" + REFUND_AMT + "】");
				paybillSerialStateCheck = false;
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.info("    【支付流水校验异常：异常原因：" + e.toString() + "】");
			paybillSerialStateCheck = false;
		}
		return paybillSerialStateCheck;

	}

	// 银行卡流水信息校验
	public boolean cashFinSerialCheck(String payBillNo, String chnl, String bankCode, String acctBank, String state) {

		String sql = "";
		String money = "";
		String OUT_SERIAL = "";
		String OID_BILLNO = "";
		String OID_PNO = "";
		String OID_CHNL = "";
		String ID_MODEL = "";
		String AMT_PAY = "";
		String CUR_CODE = "";
		String OID_BANK = "";
		String ACCT_BANK = "";
		String NAME_CUST = "";
		String COL_ACCTTYPE = "";
		String BANK_SERIAL = "";
		String STAT = "";
		String INFO_MD5 = "";
		String YTRET_CODE = "";
		String YTRET_MSG = "";
		String RET_CODE = "";
		String RET_MSG = "";
		String PRVS_ACCT = "";
		String PRVS_ACCT_NAME = "";
		String PRVS_BANK_NAME = "";
		String PRVS_BANKCODE = "";

		boolean finserialStateCheck = true;

		try {
			String oidSerialNo = getCashFinserialNo(payBillNo);
			// 付款流水表
			sql = "select * from tb_finserial where OID_SERIALNO = '" + oidSerialNo + "'";
			pstmt = DBRes.getPreparedStatement(dbpayconn, sql);
			results = DBRes.getResultSet(pstmt);

			while (results != null && results.next()) {

				money = results.getString("AMT_PAY");
				// 进行金额单位转换
				BigDecimal amount1 = new BigDecimal(money);
				BigDecimal b = new BigDecimal(1000);
				BigDecimal amount = amount1.divide(b);
				AMT_PAY = String.valueOf(amount);

				OUT_SERIAL = results.getString("OUT_SERIAL");
				OID_PNO = results.getString("OID_PNO");
				OID_CHNL = results.getString("OID_CHNL");
				ID_MODEL = results.getString("ID_MODEL");
				CUR_CODE = results.getString("CUR_CODE");
				OID_BANK = results.getString("OID_BANK");
				ACCT_BANK = results.getString("ACCT_BANK");
				NAME_CUST = results.getString("NAME_CUST");
				COL_ACCTTYPE = results.getString("COL_ACCTTYPE");
				BANK_SERIAL = results.getString("BANK_SERIAL");
				STAT = results.getString("STAT");
				INFO_MD5 = results.getString("INFO_MD5");
				YTRET_CODE = results.getString("YTRET_CODE");
				YTRET_MSG = results.getString("YTRET_MSG");
				RET_CODE = results.getString("RET_CODE");
				RET_MSG = results.getString("RET_MSG");
				PRVS_ACCT = results.getString("PRVS_ACCT");
				PRVS_ACCT_NAME = results.getString("PRVS_ACCT_NAME");
				PRVS_BANK_NAME = results.getString("PRVS_BANK_NAME");
				PRVS_BANKCODE = results.getString("PRVS_BANKCODE");

			}

			if (OID_CHNL != null && OID_CHNL.equals(chnl)) {
				log.info("    【银行卡流水付款渠道校验成功】");
			} else {
				log.info("    【银行卡流水付款渠道校验失败：OID_CHNL=" + OID_CHNL + "】");
				finserialStateCheck = false;
			}

			if (OID_BANK != null && OID_BANK.equals(bankCode)) {
				log.info("    【银行卡流水付款银行校验成功】");
			} else {
				log.info("    【银行卡流水付款银行校验失败：OID_BANK=" + OID_BANK + "】");
				finserialStateCheck = false;
			}

			if (ACCT_BANK != null && ACCT_BANK.equals(acctBank)) {
				log.info("    【银行卡流水付款账户校验成功】");
			} else {
				log.info("    【银行卡流水付款账户校验失败：ACCT_BANK=" + ACCT_BANK + "】");
				finserialStateCheck = false;
			}

			if (STAT != null && STAT.equals(state)) {
				log.info("    【银行卡流水付款状态校验成功】");
			} else {
				log.info("    【银行卡流水付款状态校验失败：STAT=" + STAT + "】");
				finserialStateCheck = false;
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.info("    【银行卡流水校验异常：异常原因：" + e.toString() + "】");
			finserialStateCheck = false;
		}
		return finserialStateCheck;

	}

	// 支付单信息校验
	public boolean payBillCheck(String payBillNo, String merchantId, String amt, String biz, String prodCode,
			String clientRefer, String payModo, String status, String curCode) {

		String sql = "";
		String money = "";
		String SRC_MERCHANT_ID = "";
		String BIZ_CODE = "";
		String PRODUCT_CODE = "";
		String CLIENT_REFER = "";
		String PAY_MODO = "";
		String PAY_BILL_STATUS = "";
		String AMT_PAY_BILL = "";
		String CUR_CODE = "";
		String DATE_ACCT = "";

		boolean finserialStateCheck = true;

		try {
			// 支付单表
			sql = "select * from TB_PAY_BILL where PAY_BILL_NO = '" + payBillNo + "'";
			pstmt = DBRes.getPreparedStatement(dbpayconn, sql);
			results = DBRes.getResultSet(pstmt);

			while (results != null && results.next()) {

				money = results.getString("AMT_PAY_BILL");
				// 进行金额单位转换
				BigDecimal amount1 = new BigDecimal(money);
				BigDecimal b = new BigDecimal(1000);
				BigDecimal amount = amount1.divide(b);
				AMT_PAY_BILL = String.valueOf(amount);

				SRC_MERCHANT_ID = results.getString("SRC_MERCHANT_ID");
				BIZ_CODE = results.getString("BIZ_CODE");
				PRODUCT_CODE = results.getString("PRODUCT_CODE");
				CLIENT_REFER = results.getString("CLIENT_REFER");
				PAY_MODO = results.getString("PAY_MODO");
				PAY_BILL_STATUS = results.getString("PAY_BILL_STATUS");
				AMT_PAY_BILL = results.getString("AMT_PAY_BILL");
				CUR_CODE = results.getString("CUR_CODE");
				DATE_ACCT = results.getString("DATE_ACCT");

			}

			if (SRC_MERCHANT_ID != null && SRC_MERCHANT_ID.equals(merchantId)) {
				log.info("    【支付单平台商户号校验成功】");
			} else {
				log.info("    【支付单平台商户号校验失败：SRC_MERCHANT_ID=" + SRC_MERCHANT_ID + "】");
				finserialStateCheck = false;
			}

			if (BIZ_CODE != null && BIZ_CODE.equals(biz)) {
				log.info("    【支付单业务名称校验成功】");
			} else {
				log.info("    【支付单业务名称校验失败：BIZ_CODE=" + BIZ_CODE + "】");
				finserialStateCheck = false;
			}

			if (PRODUCT_CODE != null && PRODUCT_CODE.equals(prodCode)) {
				log.info("    【支付单产品编码校验成功】");
			} else {
				log.info("    【支付单产品编码校验失败：PRODUCT_CODE=" + PRODUCT_CODE + "】");
				finserialStateCheck = false;
			}

			if (CLIENT_REFER != null && CLIENT_REFER.equals(clientRefer)) {
				log.info("    【支付单支付来源标示校验成功】");
			} else {
				log.info("    【付款单支付来源标示校验失败：CLIENT_REFER=" + CLIENT_REFER + "】");
				finserialStateCheck = false;
			}

			if (PAY_MODO != null && PAY_MODO.equals(payModo)) {
				log.info("    【支付单交易模式校验成功】");
			} else {
				log.info("    【支付单交易模式校验失败：PAY_MODO=" + PAY_MODO + "】");
				finserialStateCheck = false;
			}

			if (PAY_BILL_STATUS != null && PAY_BILL_STATUS.equals(status)) {
				log.info("    【支付单状态校验成功】");
			} else {
				log.info("    【支付单状态校验失败：PAY_BILL_STATUS=" + PAY_BILL_STATUS + "】");
				finserialStateCheck = false;
			}
			if (AMT_PAY_BILL != null && AMT_PAY_BILL.equals(amt)) {
				log.info("    【支付单金额校验成功】");
			} else {
				log.info("    【支付单金额校验失败：AMT_PAY_BILL=" + AMT_PAY_BILL + "】");
				finserialStateCheck = false;
			}
			if (CUR_CODE != null && CUR_CODE.equals(curCode)) {
				log.info("    【支付单币种校验成功】");
			} else {
				log.info("    【支付单币种校验失败：CUR_CODE=" + CUR_CODE + "】");
				finserialStateCheck = false;
			}

			Date now = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
			String date = dateFormat.format(now);

			if (DATE_ACCT != null && DATE_ACCT.equals(date)) {
				log.info("    【支付单账务日期校验成功】");
			} else {
				log.info("    【支付单账务日期校验失败：DATE_ACCT=" + DATE_ACCT + "】");
				finserialStateCheck = false;
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.info("    【支付单校验异常：异常原因：" + e.toString() + "】");
			finserialStateCheck = false;
		}
		return finserialStateCheck;

	}

	// 退货单信息校验
	public boolean refundPayBillCheck(String payBillNo, String merchantId, String prodSrc, String prodType,
			String amtRefund, String chnl, String status, String curCode) {

		String sql = "";
		String money = "";
		String SRC_MERCHANT_ID = "";
		String PROC_SRC = "";
		String PROC_TYPE = "";
		String AMT_REFUND = "";
		String CUR_CODE = "";
		String OID_CHNL = "";
		String REFUND_BILL_STATUS = "";
		String DATE_ACCT = "";

		boolean finserialStateCheck = true;

		try {
			// 退货单表
			sql = "select * from TB_REFUND_BILL where PAY_BILL_NO = '" + payBillNo + "'";
			pstmt = DBRes.getPreparedStatement(dbpayconn, sql);
			results = DBRes.getResultSet(pstmt);

			while (results != null && results.next()) {

				money = results.getString("AMT_REFUND");
				// 进行金额单位转换
				BigDecimal amount1 = new BigDecimal(money);
				BigDecimal b = new BigDecimal(1000);
				BigDecimal amount = amount1.divide(b);
				AMT_REFUND = String.valueOf(amount);

				SRC_MERCHANT_ID = results.getString("SRC_MERCHANT_ID");
				PROC_SRC = results.getString("PROC_SRC");
				PROC_TYPE = results.getString("PROC_TYPE");
				AMT_REFUND = results.getString("AMT_REFUND");
				CUR_CODE = results.getString("CUR_CODE");
				OID_CHNL = results.getString("OID_CHNL");
				REFUND_BILL_STATUS = results.getString("REFUND_BILL_STATUS");
				DATE_ACCT = results.getString("DATE_ACCT");

			}

			if (SRC_MERCHANT_ID != null && SRC_MERCHANT_ID.equals(merchantId)) {
				log.info("    【退货单平台商户号校验成功】");
			} else {
				log.info("    【退货单平台商户号校验失败：SRC_MERCHANT_ID=" + SRC_MERCHANT_ID + "】");
				finserialStateCheck = false;
			}

			if (PROC_SRC != null && PROC_SRC.equals(prodSrc)) {
				log.info("    【退货单产品来源校验成功】");
			} else {
				log.info("    【退货单产品来源校验失败：PROC_SRC=" + PROC_SRC + "】");
				finserialStateCheck = false;
			}

			if (PROC_TYPE != null && PROC_TYPE.equals(prodType)) {
				log.info("    【退货单处理类别校验成功】");
			} else {
				log.info("    【退货单处理类别校验失败：PROC_TYPE=" + PROC_TYPE + "】");
				finserialStateCheck = false;
			}

			if (AMT_REFUND != null && AMT_REFUND.equals(amtRefund)) {
				log.info("    【退货单金额校验成功】");
			} else {
				log.info("    【退货单金额校验失败：AMT_REFUND=" + AMT_REFUND + "】");
				finserialStateCheck = false;
			}

			if (CUR_CODE != null && CUR_CODE.equals(curCode)) {
				log.info("    【退货单币种校验成功】");
			} else {
				log.info("    【退货单币种校验失败：CUR_CODE=" + CUR_CODE + "】");
				finserialStateCheck = false;
			}

			if (OID_CHNL != null && OID_CHNL.equals(chnl)) {
				log.info("    【退货单发生渠道校验成功】");
			} else {
				log.info("    【退货单发生渠道校验失败：OID_CHNL=" + OID_CHNL + "】");
				finserialStateCheck = false;
			}

			if (REFUND_BILL_STATUS != null && REFUND_BILL_STATUS.equals(status)) {
				log.info("    【退货单状态校验成功】");
			} else {
				log.info("    【退货单状态校验失败：PAY_BILL_STATUS=" + REFUND_BILL_STATUS + "】");
				finserialStateCheck = false;
			}

			Date now = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
			String date = dateFormat.format(now);

			if (DATE_ACCT != null && DATE_ACCT.equals(date)) {
				log.info("    【退货单账务日期校验成功】");
			} else {
				log.info("    【退货单账务日期校验失败：DATE_ACCT=" + DATE_ACCT + "】");
				finserialStateCheck = false;
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.info("    【退货单校验异常：异常原因：" + e.toString() + "】");
			finserialStateCheck = false;
		}
		return finserialStateCheck;
	}

	// 渠道签约关系校验
	public boolean chnlSignInfoCheck(IAESCryptService aesCryptService, String signState, String cardNum, String name,
			String idNum, String phoneNum, String chnlCode) throws UnsupportedEncodingException, AESException {
		String sql = "";
		String agreementNo = "";
		String chnl = "";
		String custName = "";
		String contactMob = "";
		String states = "";
		String idNo = "";
		String cardno = aesCryptService.encrypt(cardNum.getBytes("UTF-8"));
		idNum = aesCryptService.encrypt(idNum.getBytes("UTF-8"));
		phoneNum = aesCryptService.encrypt(phoneNum.getBytes("UTF-8"));
		name = aesCryptService.encrypt(name.getBytes("UTF-8"));
		boolean signInfoStateCheck = true;

		try {

			sql = "select * from (select * from TC_INFO_SIGNED where CARDNO=? order by REG_DATE desc) a where rownum=1";

			pstmt = DBRes.getPreparedStatement(dbchnlconn, sql);
			pstmt.setString(1, cardno);
			results = DBRes.getResultSet(pstmt);

			while (results != null && results.next()) {
				agreementNo = results.getString("AGREEMENTNO");
				custName = results.getString("ACNAME");
				contactMob = results.getString("CONTACT_MOB");
				idNo = results.getString("IDNO");
				chnl = results.getString("OID_CHNL");
				states = results.getString("AGREEMENT_S");
				log.info("对卡号" + cardNum + "进行签约校验，签约协议号为：" + agreementNo);
			}

			if (states.equals(signState)) {
				log.info("    【签约状态校验成功】");
			} else {
				log.info("    【签约状态校验失败：status=" + states + "】");
				signInfoStateCheck = false;
			}

			if (chnlCode.equals(chnl)) {
				log.info("    【签约渠道校验成功】");
			} else {
				log.info("    【签约渠道校验失败：chnl=" + chnl + "】");
				signInfoStateCheck = false;
			}

			if (name.equals(custName)) {
				log.info("    【签约卡姓名校验成功】");
			} else {
				log.info("    【签约卡姓名校验失败：name=" + custName + "】");
				signInfoStateCheck = false;
			}

			if (idNum.equals(idNo)) {
				log.info("    【签约卡证件号校验成功】");
			} else {
				log.info("    【签约卡证件号校验失败：idnum=" + aesCryptService.decrypt(idNo) + "】");
				signInfoStateCheck = false;
			}

			if (phoneNum.equals(contactMob)) {
				log.info("    【签约卡手机号校验成功】");
			} else {
				log.info("    【签约卡手机号校验失败：phoneNum=" + aesCryptService.decrypt(contactMob) + "】");
				signInfoStateCheck = false;
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.info("    【卡号签约校验异常：异常原因：" + e.toString() + "】");
			signInfoStateCheck = false;

		}
		return signInfoStateCheck;
	}

	// 用户签约关系校验
	public boolean userSignInfoCheck(IAESCryptService aesCryptService, String signState, String cardNum, String name,
			String idNum, String phoneNum) throws UnsupportedEncodingException, AESException {
		String sql = "";
		String RECORD_INFO_ID = "";
		String OID_USER_NO = "";
		String CVV2 = "";
		String VALIDATE = "";
		String custName = "";
		String contactMob = "";
		String states = "";
		String idNo = "";
		String CARD_NUMBER = aesCryptService.encrypt(cardNum.getBytes("UTF-8"));
		idNum = aesCryptService.encrypt(idNum.getBytes("UTF-8"));
		phoneNum = aesCryptService.encrypt(phoneNum.getBytes("UTF-8"));
		name = aesCryptService.encrypt(name.getBytes("UTF-8"));
		boolean signInfoStateCheck = true;

		try {

			sql = "select * from (select * from USER_BANK_CARD_INFO where CARD_NUMBER=? order by CREATE_TIME desc) a where rownum=1";

			pstmt = DBRes.getPreparedStatement(dbchnlconn, sql);
			pstmt.setString(1, CARD_NUMBER);
			results = DBRes.getResultSet(pstmt);

			while (results != null && results.next()) {
				RECORD_INFO_ID = results.getString("RECORD_INFO_ID");
				OID_USER_NO = results.getString("OID_USER_NO");
				custName = results.getString("ACCT_NAME");
				contactMob = results.getString("PHONE_NUMBER");
				idNo = results.getString("CREDENTIAL_NUMBER");
				CVV2 = results.getString("SCR1");
				VALIDATE = results.getString("SCR2");
				states = results.getString("STATUS");
				log.info("对卡号" + CARD_NUMBER + "进行用户签约校验，签约协议号为：" + RECORD_INFO_ID);
			}

			if (states.equals(signState)) {
				log.info("    【签约状态校验成功】");
			} else {
				log.info("    【签约状态校验失败：status=" + states + "】");
				signInfoStateCheck = false;
			}

			if (name.equals(custName)) {
				log.info("    【签约卡姓名校验成功】");
			} else {
				log.info("    【签约卡姓名校验失败：name=" + custName + "】");
				signInfoStateCheck = false;
			}

			if (idNum.equals(idNo)) {
				log.info("    【签约卡证件号校验成功】");
			} else {
				log.info("    【签约卡证件号校验失败：idnum=" + aesCryptService.decrypt(idNo) + "】");
				signInfoStateCheck = false;
			}

			if (phoneNum.equals(contactMob)) {
				log.info("    【签约卡手机号校验成功】");
			} else {
				log.info("    【签约卡手机号校验失败：phoneNum=" + aesCryptService.decrypt(contactMob) + "】");
				signInfoStateCheck = false;
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.info("    【用户签约校验异常：异常原因：" + e.toString() + "】");
			signInfoStateCheck = false;

		}
		return signInfoStateCheck;
	}

	// 更改商户秘钥
	public void updateTraderLicense(String traderNo, String license) throws SQLException {
		String sql = "";
		try {
			sql = "UPDATE TB_TRADER_LICENSE t SET t.LICENSE='" + license + "' WHERE t.OID_PARTNER=?";
			pstmt = DBRes.getPreparedStatement(dbcustconn, sql);
			pstmt.setString(1, traderNo);
			resultNum = DBRes.updateResultSet(pstmt);

		} catch (Exception e) {
			e.printStackTrace();
			log.info("    【更改商户秘钥失败：" + e.toString() + "】");

		}
	}

	// 更改用户状态
	public void updateUserState(String userNo, String state) throws SQLException {
		String sql = "";
		try {
			sql = "UPDATE ACCP_CUST_INFO t SET t.STAT='" + state + "' WHERE t.OID_USERNO=?";
			pstmt = DBRes.getPreparedStatement(dbcustconn, sql);
			pstmt.setString(1, userNo);
			resultNum = DBRes.updateResultSet(pstmt);
		} catch (Exception e) {
			e.printStackTrace();
			log.info("    【更改用户状态失败：" + e.toString() + "】");
		}
	}

	// 更改商户状态
	public void updateTraderState(String traderNo, String state) throws SQLException {
		String sql = "";
		try {
			sql = "UPDATE TT_BASE_MCHINFO t SET t.stat_trader='" + state + "' WHERE t.oid_traderno=?";
			pstmt = DBRes.getPreparedStatement(dbcustconn, sql);
			pstmt.setString(1, traderNo);
			resultNum = DBRes.updateResultSet(pstmt);
		} catch (Exception e) {
			e.printStackTrace();
			log.info("    【更改商户状态失败：" + e.toString() + "】");
		}
	}

	// 更改账户状态
	public void updateAcctState(String acctNo, String state) throws SQLException {
		String sql = "";
		try {
			sql = "UPDATE TA_ACCT_INFO t SET t.STAT_ACCT='" + state + "' WHERE t.OID_ACCTNO=?";
			pstmt = DBRes.getPreparedStatement(dbaccountconn, sql);
			pstmt.setString(1, acctNo);
			resultNum = DBRes.updateResultSet(pstmt);
		} catch (Exception e) {
			e.printStackTrace();
			log.info("    【更改账户状态失败：" + e.toString() + "】");
		}
	}

	// 更改商户请求IP限制
	public void updateIpRequest(String ipRequest, String oidTraderNo) throws SQLException {
		String sql = "";
		try {
			sql = "UPDATE TB_TRADERPARA t SET t.ip_request='" + ipRequest + "' WHERE t.oid_traderno='" + oidTraderNo
					+ "'";
			pstmt = DBRes.getPreparedStatement(dbcustconn, sql);
			resultNum = DBRes.updateResultSet(pstmt);

		} catch (Exception e) {
			e.printStackTrace();
			log.info("    【更改商户请求IP限制失败：" + e.toString() + "】");

		}
	}

	// 更改商户支付状态
	public void updateTraderEnableState(String stat, String oidTraderNo) throws SQLException {
		String sql = "";
		try {
			sql = "UPDATE TB_TRADERPARA t SET t.enable_flag='" + stat + "' WHERE t.oid_traderno='" + oidTraderNo + "'";
			pstmt = DBRes.getPreparedStatement(dbcustconn, sql);
			resultNum = DBRes.updateResultSet(pstmt);

		} catch (Exception e) {
			e.printStackTrace();
			log.info("    【更改商户支付状态失败：" + e.toString() + "】");

		}
	}

	// 更改用户状态
	public void updateUserState1(String stat, String user_id, String oid_partner) throws SQLException {
		String sql = "";
		try {
			sql = "UPDATE ACCP_CUST_INFO t SET t.stat='" + stat + "' WHERE t.user_id = '" + user_id
					+ "' AND t.oid_partner='" + oid_partner + "'";
			pstmt = DBRes.getPreparedStatement(dbcustconn, sql);
			resultNum = DBRes.updateResultSet(pstmt);

		} catch (Exception e) {
			e.printStackTrace();
			log.info("    【更改用户状态失败：" + e.toString() + "】");

		}
	}

	// 更改商户产品限额
	public void updateTraderLimitParam(String oid_partner, String cust_type, String prod_id, String pay_type,
			String amt_limit) throws SQLException {
		String sql = "";
		try {
			sql = "UPDATE ACCP_TRADE_LIMIT_PARAM t SET t.amt_limit='" + amt_limit + "' WHERE t.oid_partner = '"
					+ oid_partner + "' AND t.cust_type='" + cust_type + "' AND t.prod_id='" + prod_id
					+ "' AND t.pay_type='" + pay_type + "' AND t.limit_type='single'";
			pstmt = DBRes.getPreparedStatement(dbcustconn, sql);
			resultNum = DBRes.updateResultSet(pstmt);

		} catch (Exception e) {
			e.printStackTrace();
			log.info("    【更改商户产品限额失败：" + e.toString() + "】");

		}
	}

	// 更改商户验密方式
	public void updateTraderVerifyParam(String oid_partner, String verify_type, String trade_type,
			String nosecret_limit, String noverify_limit) throws SQLException {
		String sql = "";
		try {
			sql = "UPDATE ACCP_TRADE_VERIFY_PARAM t SET t.verify_type='" + verify_type + "',t.nosecret_limit='"
					+ nosecret_limit + "',t.noverify_limit='" + noverify_limit + "' WHERE t.oid_partner = '"
					+ oid_partner + "' AND t.trade_type='" + trade_type + "'";
			pstmt = DBRes.getPreparedStatement(dbcustconn, sql);
			resultNum = DBRes.updateResultSet(pstmt);

		} catch (Exception e) {
			e.printStackTrace();
			log.info("    【更改商户验密方式失败：" + e.toString() + "】");

		}
	}

	// 查询账户各余额
	public String getBalance(String oid_acctno, String balanceType) throws UnsupportedEncodingException, AESException {
		// 总余额：AMT_BALCUR；可用余额：AMT_BALAVAL；冻结金额：AMT_BALFRZ
		String sql = "";
		String balance = "";
		try {
			sql = "select " + balanceType + " from TA_ACCT_INFO where OID_ACCTNO = '" + oid_acctno + "'";
			pstmt = DBRes.getPreparedStatement(dbaccountconn, sql);
			results = DBRes.getResultSet(pstmt);
			while (results != null && results.next()) {
				balance = results.getString(balanceType);
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.info("    【查询账户余额失败：" + e.toString() + "】");
		}
		return balance;
	}

	// 根据用户id或者商户id查询各类型账户
	public String getAcctNo(String userId, String acctType) throws UnsupportedEncodingException, AESException {
		String sql = "";
		String OID_ACCTNO = "";
		try {
			sql = "select OID_ACCTNO from TA_ACCT_INFO where USER_NO = '" + userId + "' AND TYPE_BAL= '" + acctType
					+ "'";
			pstmt = DBRes.getPreparedStatement(dbaccountconn, sql);
			results = DBRes.getResultSet(pstmt);
			while (results != null && results.next()) {
				OID_ACCTNO = results.getString("OID_ACCTNO");
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.info("    【查询商户账户失败：" + e.toString() + "】");
		}
		return OID_ACCTNO;
	}

	// 删除商户产品
	public void deleteTraderProduct(String prod_id, String oid_partner) throws SQLException {
		String sql = "";
		try {
			sql = "DELETE FROM TT_TRADER_PRODUCT WHERE prob_id =  '" + prod_id + "' AND oid_traderno='" + oid_partner
					+ "'";
			pstmt = DBRes.getPreparedStatement(dbcustconn, sql);
			resultNum = DBRes.updateResultSet(pstmt);

		} catch (Exception e) {
			e.printStackTrace();
			log.info("    【删除商户产品配置数据失败：" + e.toString() + "】");

		}
	}

	// 删除绑卡签约信息
	public void deleteCardSigned(IAESCryptService aesCryptService, String card_no)
			throws UnsupportedEncodingException, AESException {
		String sql = "";
		String cardno = aesCryptService.encrypt(card_no.getBytes("UTF-8"));
		try {
			sql = "DELETE FROM TC_INFO_SIGNED WHERE CARDNO = '" + cardno + "'";
			pstmt = DBRes.getPreparedStatement(dbchnlconn, sql);
			resultNum = DBRes.updateResultSet(pstmt);

		} catch (Exception e) {
			e.printStackTrace();
			log.info("    【删除绑卡签约信息数据失败：" + e.toString() + "】");

		}
	}

	// 删除绑卡信息
	public void deleteUserBankCardInfo(IAESCryptService aesCryptService, String card_no)
			throws UnsupportedEncodingException, AESException {
		String sql = "";
		String cardno = aesCryptService.encrypt(card_no.getBytes("UTF-8"));
		try {
			sql = "DELETE FROM USER_BANK_CARD_INFO WHERE CARD_NUMBER = '" + cardno + "'";
			pstmt = DBRes.getPreparedStatement(dbchnlconn, sql);
			resultNum = DBRes.updateResultSet(pstmt);

		} catch (Exception e) {
			e.printStackTrace();
			log.info("    【删除绑卡签约信息数据失败：" + e.toString() + "】");

		}
	}

	// 新增商户产品
	public void insertTraderProduct(String prod_id, String oid_partner) throws SQLException {
		String sql = "";
		try {
			sql = "INSERT INTO TT_TRADER_PRODUCT(prob_id,oid_traderno) VALUES('" + prod_id + "','" + oid_partner + "')";
			pstmt = DBRes.getPreparedStatement(dbcustconn, sql);
			resultNum = DBRes.updateResultSet(pstmt);

		} catch (Exception e) {
			e.printStackTrace();
			log.info("    【插入商户产品配置数据失败：" + e.toString() + "】");

		}
	}

	// 新增配置
	public void insertChnlConfiguration(String sql) throws UnsupportedEncodingException, AESException {

		try {
			pstmt = DBRes.getPreparedStatement(dbchnlconn, sql);
			resultNum = DBRes.updateResultSet(pstmt);

		} catch (Exception e) {
			e.printStackTrace();
			log.info("    【插入配置数据失败：" + e.toString() + "】");
		}
	}

	// 删除配置
	public void deleteChnlConfiguration(String sql) throws UnsupportedEncodingException, AESException {

		try {
			pstmt = DBRes.getPreparedStatement(dbchnlconn, sql);
			resultNum = DBRes.updateResultSet(pstmt);

		} catch (Exception e) {
			e.printStackTrace();
			log.info("    【删除配置数据失败：" + e.toString() + "】");

		}
	}

}
