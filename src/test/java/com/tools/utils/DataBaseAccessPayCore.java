package com.tools.utils;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import com.lianlian.crypt.exception.AESException;
import com.lianlian.crypt.service.IAESCryptService;

/**
 * 数据库操作
 * 
 * @author wanglin002  tengaj
 */

public class DataBaseAccessPayCore extends AbstractTestNGSpringContextTests {

	private static Log log = LogFactory.getLog(DataBaseAccessPayCore.class);
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

	//获取担保单状态
	public String getSeuredPayBillState(String paybillNo) throws Exception {

		String sql = "";
		String STAT = "";
		try {
			sql = "select STAT from TB_SECURED_TRANSACTIONS where PAY_BILL_NO="+paybillNo;
			pstmt = DBRes.getPreparedStatement(dbpayconn, sql);
			results = DBRes.getResultSet(pstmt);
			while (results != null && results.next()) {
				STAT = results.getString("STAT");
			}
			return STAT;

		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
		
	}
	
	// 更改账户余额  
	public void updateAcctinfo(String oid_acctno,float amt) throws UnsupportedEncodingException, AESException {
		String sql = "";

		try {
			//select t.*, t.rowid from TA_ACCT_INFO t where t.oid_acctno=201407031833422
					
			sql = "update TA_ACCT_INFO t set t.AMT_BALCUR=?,t.AMT_BALAVAL=?,t.amt_balfrz=0 where t.oid_acctno=?  ";
			
			pstmt = DBRes.getPreparedStatement(dbaccountconn, sql);
			pstmt.setFloat(1, amt);
			pstmt.setFloat(2, amt);
			pstmt.setString(3, oid_acctno);
			resultNum = DBRes.updateResultSet(pstmt);
			log.info("    【更改账户余额成功：" + oid_acctno + "】");

		} catch (Exception e) {
			e.printStackTrace();
			log.info("    【更改账户余额失败：" + e.toString() + "】");

		}
	}
	
	// 查询账户可用余额
	public String getAMT_BALAVAL(String oid_acctno) throws UnsupportedEncodingException, AESException {
		String sql = "";
		String AMT_BALAVAL = "";
		try {
			sql = "select AMT_BALAVAL from TA_ACCT_INFO where OID_ACCTNO = '"+ oid_acctno + "'";
			
			pstmt = DBRes.getPreparedStatement(dbaccountconn, sql);
			results = DBRes.getResultSet(pstmt);
			while (results != null && results.next()) {
				AMT_BALAVAL = results.getString("AMT_BALAVAL");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			log.info("    【查询账户可用余额失败：" + e.toString() + "】");
		}
		return AMT_BALAVAL;
	}

			
	//修改支付单状态
	public void updatePayBillState(String status,
			String OID_BILLNO) throws UnsupportedEncodingException, AESException {
		String sql = "";

		try {

			sql = "update TB_PAY_BILL t set t.PAY_BILL_STATUS=? where t.PAY_BILL_NO=?";
			pstmt = DBRes.getPreparedStatement(dbpayconn, sql);
			pstmt.setString(1, status);
			pstmt.setString(2, OID_BILLNO);
			resultNum = DBRes.updateResultSet(pstmt);

		} catch (Exception e) {
			e.printStackTrace();
			log.info("    【更改支付单状态失败：" + e.toString() + "】");
		}
	}
	
	//查询支付单状态
	public String getPayBillState(String OID_BILLNO) throws UnsupportedEncodingException, AESException {
		String sql = "";
		String PAY_BILL_STATUS = "";

		try {
			sql ="select PAY_BILL_STATUS from TB_PAY_BILL where PAY_BILL_NO = '" + OID_BILLNO + "'";
			
			pstmt = DBRes.getPreparedStatement(dbpayconn, sql);
			results = DBRes.getResultSet(pstmt);
			while (results != null && results.next()) {
				PAY_BILL_STATUS = results.getString("PAY_BILL_STATUS");
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.info("    【查询支付单状态失败：" + e.toString() + "】");
		}
		return PAY_BILL_STATUS;
	}
			
	// pay库新增修改配置
	public void updatePayConfiguration(String sql) throws UnsupportedEncodingException, AESException {

		try {
			pstmt = DBRes.getPreparedStatement(dbpayconn, sql);
			resultNum = DBRes.updateResultSet(pstmt);
		} catch (Exception e) {
			e.printStackTrace();
			log.info("    【插入配置数据失败：" + e.toString() + "】");
		}
	}

	// 新增配置
	public void insertdbcustConfiguration(String sql) throws UnsupportedEncodingException, AESException {

		try {
			pstmt = DBRes.getPreparedStatement(dbcustconn, sql);
			resultNum = DBRes.updateResultSet(pstmt);

		} catch (Exception e) {
			e.printStackTrace();
			log.info("    【插入配置数据失败：" + e.toString() + "】");
		}
	}
	// 新增配置
	public void insertAcctConfiguration(String sql) throws UnsupportedEncodingException, AESException {

		try {
			pstmt = DBRes.getPreparedStatement(dbaccountconn, sql);
			resultNum = DBRes.updateResultSet(pstmt);

		} catch (Exception e) {
			e.printStackTrace();
			log.info("    【插入配置数据失败：" + e.toString() + "】");
		}
	}

	// 删除配置
	public void deleteConfiguration(String sql) throws UnsupportedEncodingException, AESException {

		try {
			pstmt = DBRes.getPreparedStatement(dbcustconn, sql);
			resultNum = DBRes.updateResultSet(pstmt);

		} catch (Exception e) {
			e.printStackTrace();
			log.info("    【删除配置数据失败：" + e.toString() + "】");

		}
	}

}
