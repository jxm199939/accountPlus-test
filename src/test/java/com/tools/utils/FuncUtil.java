package com.tools.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.support.StaticApplicationContext;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import com.alibaba.fastjson.JSONObject;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.lianpay.share.utils.DateUtil;

public class FuncUtil {

	/**
	 * 生成待签名串
	 * 
	 * @param jsonObject
	 * @return
	 * @author wanglin002
	 */
	public static String genSignData(JSONObject jsonObject) {
		StringBuffer content = new StringBuffer();

		// 按照key做首字母升序排列
		List<String> keys = new ArrayList<String>(jsonObject.keySet());
		Collections.sort(keys, String.CASE_INSENSITIVE_ORDER);
		for (int i = 0; i < keys.size(); i++) {
			String key = (String) keys.get(i);
			if ("sign".equals(key)) {
				continue;
			}
			String value = (String) jsonObject.getString(key);
			// 空串不参与签名
			if (isNull(value)) {
				continue;
			}
			content.append((i == 0 ? "" : "&") + key + "=" + value);

		}
		String signSrc = content.toString();
		if (signSrc.startsWith("&")) {
			signSrc = signSrc.replaceFirst("&", "");
		}
		return signSrc;
	}

	public static boolean isNull(String str) {
		if (null == str || str.equalsIgnoreCase("NULL") || str.equals("")) {
			return true;
		} else
			return false;
	}

	/**
	 * 获取当前时间
	 * 
	 * @author wanglin002
	 */
	public static String getCurrentTime() {
		Calendar time = Calendar.getInstance();
		SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd");
		String currentTime = timeFormat.format(time.getTime());
		return currentTime;
	}

	/**
	 * 获取excel真实行数，去掉空行
	 * 
	 * @author wanglin002
	 */
	private int getRightRows(Sheet sheet) {
		int rsCols = sheet.getColumns(); // 列数
		int rsRows = sheet.getRows(); // 行数
		int nullCellNum;
		int afterRows = rsRows;
		for (int i = 1; i < rsRows; i++) { // 统计行中为空的单元格数
			nullCellNum = 0;
			for (int j = 0; j < rsCols; j++) {
				String val = sheet.getCell(j, i).getContents().trim();
				val = StringUtils.trimToEmpty(val);
				if (StringUtils.isBlank(val))
					nullCellNum++;
			}
			if (nullCellNum >= rsCols) { // 如果nullCellNum大于或等于总的列数
				{
					afterRows--; // 行数减一
				}
			}
		}
		return afterRows;
	}

	/**
	 * * 操作excel 将支付信息写入excel，通过第一行标题来作为标志起始列，使用中 * @author wanglin002
	 * */
	public void writeRet2Excel(Object o, String sheetName, String retcode,
			String retmsg) throws Exception {

		Workbook book = null;
		File file = new File("src/test/data/data/excel/" + "/"
				+ o.getClass().getName().replaceAll("\\.", "/") + ".xls");

		book = Workbook.getWorkbook(file);
		Sheet sheet = book.getSheet(sheetName);
		int flag_column = 0; // 指定标志列
		int retcode_column = 6;// 指定返回码列
		int retmsg_column = 4;// 指定返回信息列
		int totalRows = getRightRows(sheet);
		int currentRow = 0;
		WritableWorkbook workbook = Workbook.createWorkbook(file, book);
		WritableSheet writesheet = workbook.getSheet(sheetName);
		Label labelCurrnt, labelRetCode, labelRetMsg;

		try {
			for (int i = 0; i < totalRows; i++) {
				labelCurrnt = new Label(flag_column, i, "Y");

				if (("").equals(sheet.getCell(0, i).getContents())
						&& ("Y").equals(sheet.getCell(0, i - 1).getContents())) {
					currentRow = labelCurrnt.getRow();
					labelRetCode = new Label(retcode_column, currentRow,
							retcode);
					labelRetMsg = new Label(retmsg_column, currentRow, retmsg);
					writesheet.addCell(labelRetCode);
					writesheet.addCell(labelRetMsg);
					writesheet.addCell(labelCurrnt);
				}

				else {
					continue;
				}
			}
		}

		catch (Exception e) {
			e.printStackTrace();
		} finally {
			workbook.write();
			workbook.close();
			book.close();
		}
	}

	/**
	 * * 操作excel 指定写入某行 * @author wanglin002
	 * */
	public void writeRet2Excel(Object o, String sheetName, int row,
			String retcode, String retmsg) throws Exception {
		Workbook book = null;
		WritableWorkbook workbook =null;
		try {
		File file = new File("src/test/data/data/excel/" + "/"
				+ o.getClass().getName().replaceAll("\\.", "/") + ".xls");
		book = Workbook.getWorkbook(file);
		int retcode_column = 6;// 指定返回码列
		int retmsg_column = 4;// 指定返回信息列
		workbook = Workbook.createWorkbook(file, book);
		WritableSheet writesheet = workbook.getSheet(sheetName);
		Label labelRetCode, labelRetMsg,labelSign;
		
			labelRetCode = new Label(retcode_column, row, retcode);
			labelRetMsg = new Label(retmsg_column, row, retmsg);
			labelSign = new Label(0,row,"Y");
			writesheet.addCell(labelRetCode);
			writesheet.addCell(labelRetMsg);
			writesheet.addCell(labelSign);
		}

		catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (workbook != null) {
				workbook.write();
				workbook.close();
			}
			if (book != null) {
				book.close();
			}
		}
	}

	/**
	 * * 设置excel初始格式，在BeforeClass中调用 * @author wanglin002
	 * 
	 * @throws IOException
	 * @throws WriteException
	 * */
	public void prepare4Excel(Object o, String sheetName) throws IOException,WriteException {
		Workbook book = null;
		WritableWorkbook workbook = null;
		try {
			File file = new File("src/test/data/data/excel/" + "/"
					+ o.getClass().getName().replaceAll("\\.", "/") + ".xls");
			book = Workbook.getWorkbook(file);
			Sheet sheet = book.getSheet(sheetName);
			int totalRows = getRightRows(sheet);
			workbook = Workbook.createWorkbook(file, book);
			WritableSheet writesheet = workbook.getSheet(sheetName);
			for (int j = 1; j < totalRows;) {
				writesheet.addCell(new Label(0, j, ""));
				j++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (workbook != null) {
				workbook.write();
				workbook.close();
			}
			if (book != null) {
				book.close();
			}
		}
	}

	/**
	 * 在excel中，将实际返回信息跟预期信息作比对,比对返回码跟返回信息
	 * 
	 * @author wanglin002
	 */

	public boolean excelResultCheck(Object o, String sheetName)
			throws BiffException, IOException, WriteException {
		Workbook book = null;
		WritableWorkbook workbook =null;
		boolean assertReslut = false;
		try {
		System.out.println(o.getClass().getName().replaceAll("\\.", "/"));
		File file = new File("src/test/data/data/excel/" + "/"
				+ o.getClass().getName().replaceAll("\\.", "/") + ".xls");
		book = Workbook.getWorkbook(file);
		Sheet sheet = book.getSheet(sheetName);
		workbook = Workbook.createWorkbook(file, book);
		int totalRows = getRightRows(sheet);

		
			for (int i = 1; i < totalRows;) {
				if (sheet.getCell(3, i).getContents()
						.equals(sheet.getCell(4, i).getContents())
						&& sheet.getCell(5, i).getContents()
								.equals(sheet.getCell(6, i).getContents())) {
					assertReslut = true;
				} else {
					assertReslut = false;
				}
				i++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (workbook != null) {
				workbook.write();
				workbook.close();
			}
			if (book != null) {
				book.close();
			}
		}
		return assertReslut;
	}

	/**
	 * 在excel中，将实际返回信息跟预期信息作比对,比对返回码跟返回信息，指定比对某行数据
	 * 
	 * @author wanglin002
	 */

	public boolean excelResultCheck(Object o, String sheetName, int row)
			throws BiffException, IOException, WriteException {
		Workbook book = null;
		WritableWorkbook workbook = null;
		boolean assertReslut = false;
		try {
		System.out.println(o.getClass().getName().replaceAll("\\.", "/"));
		File file = new File("src/test/data/data/excel/" + "/"
				+ o.getClass().getName().replaceAll("\\.", "/") + ".xls");
		book = Workbook.getWorkbook(file);
		Sheet sheet = book.getSheet(sheetName);
		workbook = Workbook.createWorkbook(file, book);
		
			if (sheet.getCell(3, row).getContents()
					.equals(sheet.getCell(4, row).getContents())
					&& sheet.getCell(5, row).getContents()
							.equals(sheet.getCell(6, row).getContents())) {
				assertReslut = true;
			} else {
				assertReslut = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (workbook != null) {
				workbook.write();
				workbook.close();
			}
			if (book != null) {
				book.close();
			}
		}
		return assertReslut;
	}

	/**
	 * 连接linux服务器，对服务进行操作，模拟掉单
	 * 
	 * @author wanglin002
	 */

	public void simulate(String host, int port, String userName,
			String password, String serviceName) throws Exception {

		String cmd = "cd "
				+ serviceName
				+ "/conf \n  sed -i '/is_sim=true/d' chnl.properties \n  sed -i '/is_sim=false/d' chnl.properties \n  sed -i '/is_sim=/d' chnl.properties \n  sed -i  '1ais_sim=true' chnl.properties \n  sh ../bin/restart.sh \n";
		JSch jsch = new JSch(); // 创建JSch对象
		Session session = jsch.getSession(userName, host, port); // 根据用户名，主机ip，端口获取一个Session对象
		session.setPassword(password); // 设置密码
		Properties config = new Properties();
		config.put("StrictHostKeyChecking", "no");
		session.setConfig(config); // 为Session对象设置properties
		int timeout = 60000000;
		session.setTimeout(timeout); // 设置timeout时间
		session.connect(); // 通过Session建立链接
		ChannelExec channelExec = (ChannelExec) session.openChannel("exec");
		channelExec.setCommand(cmd);
		channelExec.setInputStream(null);
		channelExec.setErrStream(System.err);
		channelExec.connect();
		InputStream in = channelExec.getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(in,
				Charset.forName("UTF-8")));
		String buf = null;
		StringBuffer sb = new StringBuffer();
		while ((buf = reader.readLine()) != null) {
			sb.append(buf);
			System.out.println(buf);// 打印控制台输出
		}
		reader.close();
		channelExec.disconnect();
		if (null != session) {
			session.disconnect();
		}
	}

	/*
	 * 连接linux服务器，对服务进行操作，取消模拟掉单
	 * 
	 * @author wanglin002
	 */
	public void cancelSimulate(String host, int port, String userName,
			String password, String serviceName) throws Exception {

		String cmd = "cd "
				+ serviceName
				+ "/conf \n  sed -i '/is_sim=true/d' chnl.properties \n  sed -i '/is_sim=false/d' chnl.properties \n  sed -i '/is_sim=/d' chnl.properties \n  sh ../bin/restart.sh \n";
		JSch jsch = new JSch(); // 创建JSch对象
		Session session = jsch.getSession(userName, host, port); // 根据用户名，主机ip，端口获取一个Session对象
		session.setPassword(password); // 设置密码
		Properties config = new Properties();
		config.put("StrictHostKeyChecking", "no");
		session.setConfig(config); // 为Session对象设置properties
		int timeout = 60000000;
		session.setTimeout(timeout); // 设置timeout时间
		session.connect(); // 通过Session建立链接
		ChannelExec channelExec = (ChannelExec) session.openChannel("exec");
		channelExec.setCommand(cmd);
		channelExec.setInputStream(null);
		channelExec.setErrStream(System.err);
		channelExec.connect();
		InputStream in = channelExec.getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(in,
				Charset.forName("UTF-8")));
		String buf = null;
		StringBuffer sb = new StringBuffer();
		while ((buf = reader.readLine()) != null) {
			sb.append(buf);
			System.out.println(buf);// 打印控制台输出
		}
		reader.close();
		channelExec.disconnect();
		if (null != session) {
			session.disconnect();
		}
	}

	/**
	 * 连接linux服务器，对服务进行操作，删除缓存
	 * 
	 * @author wanglin002
	 */

	public void delRedis(String host1, int port1, String host, int port,
			String userName, String password, String traderNo) throws Exception {

		String cmd = "telnet " + host + " " + port
				+ " \n del xcpay_admin_merchantTRADER_PARA_" + traderNo
				+ " \n ";
		JSch jsch = new JSch(); // 创建JSch对象
		Session session = jsch.getSession(userName, host1, port1); // 根据用户名，主机ip，端口获取一个Session对象
		session.setPassword(password); // 设置密码
		Properties config = new Properties();
		config.put("StrictHostKeyChecking", "no");
		session.setConfig(config); // 为Session对象设置properties
		int timeout = 60000000;
		session.setTimeout(timeout); // 设置timeout时间
		session.connect(); // 通过Session建立链接
		ChannelExec channelExec = (ChannelExec) session.openChannel("exec");
		channelExec.setCommand(cmd);
		channelExec.setInputStream(null);
		channelExec.setErrStream(System.err);
		channelExec.connect();
		InputStream in = channelExec.getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(in,
				Charset.forName("UTF-8")));
		String buf = null;
		StringBuffer sb = new StringBuffer();
		while ((buf = reader.readLine()) != null) {
			sb.append(buf);
			System.out.println(buf);// 打印控制台输出
		}
		reader.close();
		channelExec.disconnect();
		if (null != session) {
			session.disconnect();
		}
	}
	
/** 
 * @author wanglin002
    身份证第十八位的计算方法为：
  1.将身份证号码的前17位数分别乘以不同的系数，从第一位到第十七位的系数分别为：7 9 10 5 8 4 2 1 6 3 7 9 10 5 8 4 2
  2.将这17位数字和系数相乘的结果相加
  3.用加出来的和除以11，看余数是多少
  4.余数只可能有0 1 2 3 4 5 6 7 8 9 10这11个数字，分别对应最后一位身份证号码为1 0 X 9 8 7 6 5 4 3 2
  5.通过上面得知如果余数是2，就会在身份证的第18位数字上出现罗马数字的Ⅹ，如果余数是10，身份证的最后一位号码就是2
 * */
public static String generateIdno(String idno) {
	//使用方法：输入前17位，生成第18位校验位
	List<Integer> list = new ArrayList<Integer>();
	String endno = "";
	for (int i = 0; i < 17; i++) {
		list.add(Integer.parseInt(String.valueOf(idno.charAt(i))));
	}
	int sum = list.get(0) * 7 + list.get(1) * 9 + list.get(2) * 10
			+ list.get(3) * 5 + list.get(4) * 8 + list.get(5) * 4
			+ list.get(6) * 2 + list.get(7) * 1 + list.get(8) * 6
			+ list.get(9) * 3 + list.get(10) * 7 + list.get(11) * 9
			+ list.get(12) * 10 + list.get(13) * 5 + list.get(14) * 8
			+ list.get(15) * 4 + list.get(16) * 2;
	int mod = sum % 11;
	switch (mod) {
	case 0:
		endno = "1";
		break;
	case 1:
		endno = "0";
		break;
	case 2:
		endno = "X";
		break;
	case 3:
		endno = "9";
		break;
	case 4:
		endno = "8";
		break;
	case 5:
		endno = "7";
		break;
	case 6:
		endno = "6";
		break;
	case 7:
		endno = "5";
		break;
	case 8:
		endno = "4";
		break;
	case 9:
		endno = "3";
		break;
	case 10:
		endno = "2";
		break;
	default:
		break;
	}
	System.out.println("身份证：" + idno + endno);
	return idno + endno;
}
	
	
	
}
