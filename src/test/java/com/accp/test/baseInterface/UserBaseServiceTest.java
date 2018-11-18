package com.accp.test.baseInterface;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.transaction.annotation.Transactional;
import com.alibaba.fastjson.JSONObject;
import com.lianpay.share.utils.DateUtil;
import com.lianpay.share.utils.Globals;
import com.lianpay.user.domain.UserBase;
import com.lianpay.user.dubbo.service.IUserBaseService;

/**
 * 用户信息服务单元测试，作为基础方法使用，如有需要，可自行传参数
 * @author wanglin002
 */
@ContextConfiguration(locations = { "/consumer.xml" })
@Transactional
public class UserBaseServiceTest extends AbstractTestNGSpringContextTests {

	@Autowired
	private IUserBaseService userBaseService;
	
	
	
	/**
	 * 个人用户开户,生成用户号
	 */

	public String userAdd(){
		String userno = DateUtil.getCurrentDateTimeStr1();
		String login = String.format("%08d", new SecureRandom().nextInt(99999999))+String.format("%04d", new SecureRandom().nextInt(9999));
		UserBase user = new UserBase();
		user.setOid_userno("18" + userno); //2014041634994748
		user.setOid_traderno("201608240000207011");
		user.setTrader_src("201608240000207011");
		user.setName_user("异步通知自动化专用");
		user.setType_user("0");
		user.setType_acctno("0");//0和Pay_account_flag的Y共同表示要生成支付账户(账户是生成实际未做控制)
		user.setPay_account_flag("Y");
		user.setUser_login("notify" + login);
		user.setFlag_signcode("1");
		user.setNum_passwd("1111111a");
		user.setMob_bind(user.getUser_login().substring(0, 10));
		user.setMob_bind("15700000000");
		user.setType_idcard("01");
		user.setNo_idcard("370782199010204101");
		user.setOid_acctno(userno+"001");
		user.setEml_bind("test_open@yintong.com.cn");	
		user.setExter_userno("1111112");
		user.setOpen_flag("1");
		user.setFlg_sex("1");
		user.setDate_birthday("19850807");
		user.setOid_job("IT");
		user.setTel_user("0571-99999999");
		user.setAddr_pro("330000");
		user.setAddr_city("330100");
		user.setAddr_conn("杭州滨江江南大道");
		user.setPreserving_info("15158044055");
		user.setPwd_login("DB64E7F16FF2480BCD2AB3BC0922B287");
		user.setPwd_pay("DB64E7F16FF2480BCD2AB3BC0922B287");
		System.err.println("用户号："+user.getOid_userno());
		System.err.println("用户登录号："+user.getUser_login());
		System.out.println("请求参数："+JSONObject.toJSONString(user));
		user = userBaseService.userAdd(user);
		String userRespString = JSONObject.toJSONString(user);
		System.out.println("返回参数："+JSONObject.toJSONString(user));
		System.out.println("开户处理结果:["+user.getRetcode()+" - "+user.getRetmsg()+"]");
		return userRespString;
	}
	
	/**
	 * 用户手机绑定
	 */
	public void userBindMobile(){
		UserBase user = new UserBase();
		user.setOid_userno("2920161129194351");
		user.setMob_bind("13858049601");
		user.setOid_traderno("201611290000260012");
		user = userBaseService.userBindMobile(user);
		System.out.print(user.getRetcode());
		System.out.print(user.getRetmsg());
		
	}
	
	/**
	 * 用户绑定手机修改
	 */
	public void userUpdMobile(){
		UserBase user = new UserBase();
		user.setOid_userno("2920161129195906"); //2920161129173620
		user.setOid_traderno("201611290000260009");//201510290000044002
		user.setOld_mob_bind("15869126001");
		user.setNew_mob_bind("13858049601");
		System.out.println(JSONObject.toJSONString(user));
		user = userBaseService.userUpdMobile(user);
		System.out.println(JSONObject.toJSONString(user));
	}
	
	
	/**
	 * 用户状态修改
	 */
	public void userStatEdit(){
		UserBase user = new UserBase();
		user.setOid_userno("3020150116092243");
		user.setOid_traderno("201310102000003524");
		user.setStat_user("0");
		user = userBaseService.userStatEdit(user);
		System.err.println(user.getRetcode()+"--"+user.getRetmsg());
	}
	
	/**
	 * 用户信息修改
	 */
	public void userInfoEdit(){
		UserBase user = new UserBase();
		user.setOid_userno("3020150116092243");
		user.setOid_traderno("201310102000003524");
		user.setUser_login("test597985120429");
		user.setName_user("潘潘");
		user.setFlg_sex("1");
		user.setDate_birthday("19850807");
		user.setType_idcard("0");
		user.setNo_idcard("330723198508070655");
		user.setTel_user("0571-00000000");
		user.setAddr_pro("浙江");
		user.setAddr_city("675555");
		user.setAddr_dist("滨江区");
		user.setAddr_conn("杭州");
		user.setNo_postcode("310015");
		user.setPreserving_info("法理上大家发的");
		user.setNation_user("中国");
		user.setExp_idcard("");
		user.setType_user("0");
		user.setAcctno_dirc_flag("0");
		user.setOid_job("");
		user = userBaseService.userInfoEdit(user);
		System.err.println(user.getRetcode()+"--"+user.getRetmsg());
	}
	
	/**
	 * 用户登录操作
	 */
	public void userLogin(){
		UserBase userBase = new UserBase();
		userBase.setUser_login("test120008313310");
		userBase.setPwd_login("DB64E7F16FF2480BCD2AB3BC0922B287");
		userBase.setOid_traderno("201310102000003524");
		userBase.setIp_login("192.168.1.0");
		UserBase res = null;
		try {
			res = userBaseService.userLogin(userBase);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(res.getRetcode() + ", " + res.getRetmsg());
	}
	
	/**
	 * 用户注销
	 */
	public void userCancel(){
		UserBase user = new UserBase();
		user.setOid_userno("3020150115164155");
		user.setOid_traderno("201310102000003524");
		user.setStat_user("2");
		user = userBaseService.userCancel(user);
		System.err.println(user.getRetcode()+"--"+user.getRetmsg());
		System.err.println(user.getRetcode()+"--"+user.getRetmsg());
	}
	
	/**
	 * 用户信息单条查询
	 */
	public void userInfoQuery(){
		UserBase user = new UserBase();
		user.setOid_userno("2015011415083001");
		user.setUser_login("test195076575949");
		user.setOid_traderno("201310102000003524");
		user.setOid_acctno("20150114150830001");
		user.setMob_bind("13858049605");
		user = userBaseService.userInfoQuery(user);
		if(Globals.TRANS_SUCCESS_RETCODE.equals(user.getRetcode())){
			System.out.println("用户号:"+user.getOid_userno());
			System.out.println("用户登录号:"+user.getBindList().get(0).getUser_login());
			System.out.println("用户帐号:"+user.getOid_acctno());
			System.out.println("所属商户号:"+user.getBindList().get(0).getOid_traderno());
		}
	}
	
	
	/**
	 * 用户登录密码重置
	 */

	public void userLoginPassSet(){
		UserBase user = new UserBase();
		user.setOid_traderno("201310102000003524");
		user.setUser_login("test@yintong.com.cn");
		user.setPwd_login("a333111112");
		//user.setNum_passwd("11111121");
		//user.setFlag_signcode("2");
		
		user = userBaseService.userLoginPassSet(user);
		System.out.print(user.getRetcode());
		System.out.print(user.getRetmsg());
	}
	
	/**
	 * 用户开户绑定
	 */
	public void userBind(){
		UserBase user = new UserBase();
		user.setOid_userno("3020160310145350");
		user.setOid_traderno("201310102000003527");
		user.setExter_userno("11111112");
		user.setAcctno_dirc_flag("0");
		user.setUser_login("test911278444220");
		user.setPwd_login("a111113");
		user.setName_user("李测试");
		user.setType_idcard("1");
		user.setNo_idcard("331082198310284677");
		
		user = userBaseService.userBind(user);
		System.out.print(user.getRetcode());
		System.out.print(user.getRetmsg());
	}
	
	/**
	 * 用户登录密码修改
	 */
	public void userLoginPassEdit(){
		UserBase user = new UserBase();
		user.setOid_traderno("201310102000003524");
		user.setUser_login("test@yintong.com.cn");
		user.setPwd_login("a2111112");
		user.setPwd_login_new("a2222221");
		
		user = userBaseService.userLoginPassEdit(user);
		System.out.print(user.getRetcode());
		System.out.print(user.getRetmsg());
		
	}
	
	/**
	 * 用户绑定手机查询
	 */
	public void userQryBindMobile(){
		UserBase user = new UserBase();
		user.setOid_userno("3020160310145350");
		
		user = userBaseService.userQryBindMobile(user);
		System.out.print(user.getRetcode());
		System.out.print(user.getRetmsg());
		
	}
	

	
	/**
	 * 用户手机解绑
	 */
	public void userUNBindMobile(){
		UserBase user = new UserBase();
		user.setOid_userno("1720170217123941");
		user.setOid_traderno("201702160000294008");
		user.setMob_bind("15500000000");
		user = userBaseService.userUNBindMobile(user);
		System.out.println("返回参数："+JSONObject.toJSONString(user));
		
	}
	
	/**
	 * 用户绑定邮箱查询
	 */
	public void userQryBindEmail(){
		UserBase user = new UserBase();
		user.setOid_userno("3020160310145350");

		user = userBaseService.userQryBindEmail(user);
		System.out.println("返回参数："+JSONObject.toJSONString(user));
		
	}
	
	/**
	 * 用户邮箱绑定
	 */
	public void userBindEmail(){
		UserBase user = new UserBase();
		user.setOid_userno("3020160315172508");
		user.setEml_bind("test_open@yintong.com.cn");
		user.setOid_traderno("201310102000003524");

		user = userBaseService.userBindEmail(user);
		System.out.print(user.getRetcode());
		System.out.print(user.getRetmsg());
	}
	
	/**
	 * 用户绑定邮箱修改
	 */
	public void userUpdEmail(){
		UserBase user = new UserBase();
		user.setOid_userno("3020160315172508");
		user.setOid_traderno("201310102000003524");
		user.setOld_eml_bind("test1@yintong.com.cn");
		user.setNew_eml_bind("test@yintong.com.cn");
		
		user = userBaseService.userUpdEmail(user);
		System.out.print(user.getRetcode());
		System.out.print(user.getRetmsg());
		
	}
	
	/**
	 * 用户邮箱解绑
	 */
	public void userUNBindEmail(){
		UserBase user = new UserBase();
		user.setOid_userno("1720170217123941");
		user.setEml_bind("auto@yintong.com.cn");
		user.setOid_traderno("201702160000294008");
		user = userBaseService.userUNBindEmail(user);
		System.out.print("返回参数："+JSONObject.toJSONString(user));
		
	}
	
	/**
	 * 用户所属信息多条查询
	 */
	public void userBelongQuery(){
		UserBase user = new UserBase();
		Map map = new HashMap();
		user.setOid_userno("3020160310145350");
		//user.setOid_traderno("201310102000003524");
		user.setOffset("0");
		user.setMaxrecordes("2");	
		map = userBaseService.userBelongQuery(user);		
		Object obj = new Object();
		obj = map.get("data");
		List<Object> list= (List<Object>) obj;
		
		for (int i=0;i<list.size();i ++) {
			UserBase result = (UserBase)list.get(i);
			String jsonStr = JSONObject.toJSONString(result);
			System.out.println(jsonStr);
	        
		}
        
		System.out.print(map.get("returnCode"));
		System.out.print(map.get("returnMsg"));
	
	}
}
