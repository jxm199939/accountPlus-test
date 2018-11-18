package com.accp.test.bean.txn;

import java.util.List;
import com.accp.test.bean.BaseBean;

public class TradeCreate extends BaseBean {
	private static final long serialVersionUID = 1L;
	private String txn_type;
	private String user_id;
	private String user_type;
	private String notify_url;
	private String return_url;
	private String pay_expire;
	private OrderInfo orderInfo;
	private List<PayeeInfo> payeeInfo;

	public String getTxn_type() {
		return txn_type;
	}

	public void setTxn_type(String txn_type) {
		this.txn_type = txn_type;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getUser_type() {
		return user_type;
	}

	public void setUser_type(String user_type) {
		this.user_type = user_type;
	}

	public String getNotify_url() {
		return notify_url;
	}

	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}

	public String getReturn_url() {
		return return_url;
	}

	public void setReturn_url(String return_url) {
		this.return_url = return_url;
	}

	public String getPay_expire() {
		return pay_expire;
	}

	public void setPay_expire(String pay_expire) {
		this.pay_expire = pay_expire;
	}

	public OrderInfo getOrderInfo() {
		return orderInfo;
	}

	public void setOrderInfo(OrderInfo orderInfo) {
		this.orderInfo = orderInfo;
	}

	public List<PayeeInfo> getPayeeInfo() {
		return payeeInfo;
	}

	public void setPayeeInfo(List<PayeeInfo> payeeInfo) {
		this.payeeInfo = payeeInfo;
	}

}