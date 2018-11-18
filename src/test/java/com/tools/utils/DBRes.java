package com.tools.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBRes {
	
		
	/**
	 * 建立mysql数据库连接
	 * @author wanglin002
	 * */
	
	
	//获取mysql数据库连接：客户系统数据库
	public static Connection getCustomerConnection(){
		Connection conn = null;
		try {
			// 加载数据库驱、获取数据库连接对像
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://192.168.20.11/accp_customer", "accp_customer","accp_customer");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}
	
	
	//获取mysql数据库连接：账务系统数据库
	public static Connection getAccountConnection(){
		Connection conn = null;
		try {
			// 加载数据库驱、获取数据库连接对像
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://192.168.20.11/accp_account", "accp_account","accp_account");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}

	//获取mysql数据库连接：支付/出金系统数据库
	public static Connection getPaymentConnection(){
		Connection conn = null;
		try {
			// 加载数据库驱、获取数据库连接对像
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://192.168.20.11/accp_payment", "accp_payment","accp_payment");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}
	
	//获取mysql数据库连接：金融渠道数据库
	public static Connection getChnlConnection(){
		Connection conn = null;
		try {
			// 加载数据库驱、获取数据库连接对像
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://192.168.20.11/accp_chnl", "accp_chnl","accp_chnl");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}
	
	//获取mysql数据库连接：对账核算数据库
	public static Connection getCheckConnection(){
		Connection conn = null;
		try {
			// 加载数据库驱、获取数据库连接对像
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://192.168.20.11/accp_check", "accp_check","accp_check");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}
	
	
	
	// 获取预处理语句执行对象
	public static PreparedStatement getPreparedStatement(Connection conn,String sql) {
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return pstmt;
	}

	// 获取结果集对象--查询
	public static ResultSet getResultSet(PreparedStatement pstmt) {
		ResultSet res = null;
		try {
			res = pstmt.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	}
	
	// 获取结果集对象--修改删除
	public static int updateResultSet(PreparedStatement pstmt) {
		int res = 0;
		try {
			res = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	}
}
