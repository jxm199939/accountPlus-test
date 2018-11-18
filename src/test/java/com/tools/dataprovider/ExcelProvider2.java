package com.tools.dataprovider;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import bsh.This;
import com.sun.xml.bind.v2.schemagen.xmlschema.List;
import com.tools.utils.KeyWordUtil;

/**
 * <h1>读取所有sheet页的provider</h1>
 * <p>表格开始不能包含空的列  ◑︿◐
 * @author huangfucf
 * @author wanglin002
 *
 */
public class ExcelProvider2 implements Iterator<Object[]> {
	public LinkedHashMap<String, LinkedList<LinkedHashMap<String,String>>> sheetMap=new LinkedHashMap<String, LinkedList<LinkedHashMap<String,String>>>();//存放sheet和sheet数据的hashmap
	public LinkedList<String> sheetList=new LinkedList<String>();	//存放excel所有sheet名字的list
	public LinkedHashMap<String,Integer> sheetKeyRowMap=new LinkedHashMap<String,Integer>();	//存放excel第一行数据和列数的map
	public int numberindex;//当前执行的行数，可以控制是否返回excel前两行的参数名和参数的注释
	public int line2;//获取数据的行数范围
	public int paramLine1=7;//excel表格中，接口参数的起始列数
	public int maxRow;//最大行数
	public String flag;//标记执行的行数flag，single单行 multi多行full全部执行
	public String excel;//excel路径
	public Workbook wb;
	public FileInputStream fis;
	public ArrayList funcMethodList= new ArrayList();
	
	public boolean hasNext() {
		if (this.flag.equals("full")) {
			System.out.println(this.numberindex);
			System.out.println(this.maxRow);
			if ( this.numberindex < this.maxRow) {
				return true;
			}
		} else if (this.flag.equals("multi")) {
			if ( this.numberindex <= this.line2) {
				return true;
			}
		} else if (this.flag.equals("single")) {
			if ( this.numberindex < this.line2) {
				return true;
			}
		}
		return false;
	}

	public Object[] next() {	
		if (this.flag.equals("full")) {
			//获取多个sheet中的参数
			LinkedHashMap<String, LinkedHashMap<String,String>> result=new LinkedHashMap<String, LinkedHashMap<String,String>>();
			for (String sheetName:this.sheetList) {				
				result.put(sheetName, this.sheetMap.get(sheetName).get(this.numberindex));
			}

			//返回object对象
			Object[] objresult = new Object[2];
			objresult[0]=result;
			objresult[1]=this;
			this.numberindex += 1;
			return objresult;
		} else if (this.flag.equals("single")) {
			//获取多个sheet中的参数
			LinkedHashMap<String, LinkedHashMap<String,String>> result=new LinkedHashMap<String, LinkedHashMap<String,String>>();
			for (String sheetName:this.sheetList) {				
				result.put(sheetName, this.sheetMap.get(sheetName).get(this.numberindex));
			}
			//返回object对象
			Object[] objresult = new Object[2];
			objresult[0]=result;
			objresult[1]=this;
			this.numberindex += 1;
			return objresult;
		} else if (this.flag.equals("multi")) {
			//获取多个sheet中的参数
			LinkedHashMap<String, LinkedHashMap<String,String>> result=new LinkedHashMap<String, LinkedHashMap<String,String>>();
			for (String sheetName:this.sheetList) {				
				result.put(sheetName, this.sheetMap.get(sheetName).get(this.numberindex));
			}
			//返回object对象
			Object[] objresult = new Object[2];
			objresult[0]=result;
			objresult[1]=this;
			this.numberindex += 1;
			return objresult;
		}
		return null;
	}
	
	public void remove() {
		throw new UnsupportedOperationException("Not supported yet.");
	}
	
	/**
	 * 获取Excel表格中的所有数据，将取出来的值放到map中
	 * @param workbook
	 * @param sheetName
	 * @author wanglin002
	 * @author huangfucf
	 */
	public HashMap getParamMap(LinkedHashMap<String,String> lmap) throws NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException, Exception {
		HashMap map=new HashMap();
		int i=0;
		getReflectAllMethod(KeyWordUtil.class);
		for( String key: lmap.keySet()) {
			i++;
			if(!lmap.get(key).contains("[")){
			if (i>=this.paramLine1) {
				//如果从excel里获取到的函数存在共总类中，那么就根据函数名执行该函数
				if(this.funcMethodList.contains(lmap.get(key)))
				{	
					map.put(key,execute(new KeyWordUtil(), lmap.get(key)));
				}
				else {
					map.put(key, lmap.get(key));
				}
			}
			}
		}
		return map;
	}
	
	
	public void appendCurrenCell(String sheetName,String key,String value) throws IOException {
		String con=this.wb.getSheet(sheetName).getRow(this.numberindex-1).getCell(this.sheetKeyRowMap.get(key)).getStringCellValue();
		if (con.equals("N/A")) {
			this.wb.getSheet(sheetName).getRow(this.numberindex-1).getCell(this.sheetKeyRowMap.get(key)).setCellValue(value);
		} else {
			this.wb.getSheet(sheetName).getRow(this.numberindex-1).getCell(this.sheetKeyRowMap.get(key)).setCellValue(con+"::"+value);
		}
		this.fis.close();
		FileOutputStream fos = new FileOutputStream(this.excel);
		this.wb.write(fos);
		fos.close();
	}
	
	public void writeCurrentCell(String sheetName,String key,String value) throws IOException, InterruptedException {
		this.wb.getSheet(sheetName).getRow(this.numberindex-1).getCell(this.sheetKeyRowMap.get(key)).setCellValue(value);
		this.fis.close();
		FileOutputStream fos = new FileOutputStream(this.excel);
		this.wb.write(fos);
		fos.close();
	}
	
	public void genKeyRowMap(String sheetName,int maxColumn) {
		for (int i=0;i<maxColumn;i++){
			//System.out.println(this.wb.getSheet(sheetName).getRow(0).getCell(i));
			this.sheetKeyRowMap.put(this.wb.getSheet(sheetName).getRow(0).getCell(i).toString(), i);
		}
	}
	
	/**
	 * 获取excel表格中的所有数据
	 * @author huangfucf
	 * @throws IOException 
	 */
	public void getExcelData(String excel) throws IOException {
		this.fis = new FileInputStream(excel);
		try {
			this.wb = new HSSFWorkbook(this.fis);//读取.xls
		} catch (Exception e) {
			//e.printStackTrace();
			this.fis = new FileInputStream(excel);
			this.wb = new XSSFWorkbook(this.fis); //读取.xlsx
		}
		//获取第一页sheet最大行数
		this.maxRow=this.wb.getSheetAt(0).getPhysicalNumberOfRows();		
		//获取所有的sheet名字
		int sheetNum=this.wb.getNumberOfSheets();
		for (int i=0;i<sheetNum;i++) {
			this.sheetList.add(wb.getSheetName(i));
		}
		//获取所有的数据
		for (String sheet:sheetList) {
			sheetMap.put(sheet,this.getSheetDate(wb,sheet));
		}
	}
	
	/**
	 * 获取Excel表格中的所有数据
	 * @param workbook
	 * @param sheetName
	 * @return
	 */
	public LinkedList<LinkedHashMap<String, String>> getSheetDate(Workbook workbook,String sheetName) {
		Sheet sheet=workbook.getSheet(sheetName);
		LinkedList <LinkedHashMap<String,String>>list = new LinkedList <LinkedHashMap<String,String>>();
		//获取最大行数，列数
		int maxRow=sheet.getPhysicalNumberOfRows();
		int maxColumn=sheet.getRow(0).getPhysicalNumberOfCells();
		this.genKeyRowMap(sheetName, maxColumn);
		int intColumn=0;//设置读取json参数初始化列数
		//读取参数
		for (int i=0;i<maxRow;i++) {
			//初始化一个map用来存储每一行的hashmap数据
			LinkedHashMap<String,String> map = new LinkedHashMap<String, String>();
			Row row=sheet.getRow(i);
			int Column;
			Column=intColumn;
			while(Column<maxColumn) {
				String key=sheet.getRow(0).getCell(Column).toString();
				String value = "";
				try{
					value=row.getCell(Column).toString();	
				}
				catch (Exception e){
					value = "N/A";
				}
				map.put(key, value);
				Column++;
			}
			list.add(map);
		}
		return list;
	}

	
	/**
	 * 获取所有sheet内的内容方法 ( ´◔ ‸◔`) <br> 全部执行
	 * @author huangfucf
	 * @param aimob
	 * @throws IOException 
	 */
	public ExcelProvider2(Object aimob , String excelName) throws IOException {
		this.flag="full";
		this.numberindex=2;
		//this.excel=System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+"java"+File.separator+aimob.getClass().getPackage().getName().replace(".", File.separator)+File.separator+aimob.getClass().getSimpleName()+".xlsx";
		this.excel=System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+"java"+File.separator+aimob.getClass().getPackage().getName().replace(".", File.separator)+File.separator+excelName+".xlsx";
		System.out.println(this.excel);
		this.getExcelData(this.excel);
	}

	/**
	 * 单行执行用例  (o◕∀◕)ﾉ
	 * @param aimob
	 * @param line1
	 * @param line2
	 * @throws IOException
	 */
	public ExcelProvider2(Object aimob,String excelName,int line1) throws IOException {
		this(aimob, excelName);
		this.flag="single";
		this.numberindex=line1-1;
		this.line2=line1;
	}
	
	/**
	 * 执行从line1到line2之间的用例  (｀◕‸◕´+)
	 * @param aimob
	 * @param line1
	 * @param line2
	 * @throws IOException
	 */
	public ExcelProvider2(Object aimob,String excelName,int line1,int line2) throws IOException {
		this(aimob,excelName);		
		this.flag="multi";
		this.numberindex=line1-1;
		this.line2=line2-1;
	}
	
	/**
	 * 根据函数方法名执行该方法
	 * @author wanglin002
	 */
    public static Object execute(Object object,String methodName) throws NoSuchMethodException, SecurityException, Exception, IllegalArgumentException, InvocationTargetException {                     
        Class clazz = object.getClass();   
        Method m1;  
        m1 = clazz.getDeclaredMethod(methodName);  
        return m1.invoke(object);         
    }  
    
	/**
	 * 获取一个类中所有的方法名
	 * @author wanglin002
	 */
    private void getReflectAllMethod( Class <?> mLocalClass){  
    	Class<?> c;  
    	c = mLocalClass;  
    	try {  
    		do{  
    			Method m[] = c.getDeclaredMethods(); // 取得全部的方法  
	    	      for (int i = 0; i < m.length; i++) {  
	    	      String mod = Modifier.toString(m[i].getModifiers()); // 取得访问权限  
	    	      String metName = m[i].getName(); // 取得方法名称 
	    	      this.funcMethodList.add(metName);
    	     }  
	    	     c=c.getSuperclass();  
	    	}while(c!=null);  
	    	} catch (Exception e) {   
	    	e.printStackTrace();  
	    	}  
}  


	

	
	
	
	
	
}