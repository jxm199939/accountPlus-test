package com.accp.test.bean.txn;

import java.io.Serializable;

public class OrderInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	private String txn_seqno;
	private String txn_time;
	private String total_amount;
	private String txn_purpose;
	private String order_info;
	private String goods_name;
	private String goods_url;
	private String postscript;

	public String getTxn_seqno() {
		return txn_seqno;
	}

	public void setTxn_seqno(String txn_seqno) {
		this.txn_seqno = txn_seqno;
	}

	public String getTxn_time() {
		return txn_time;
	}

	public void setTxn_time(String txn_time) {
		this.txn_time = txn_time;
	}

	public String getTotal_amount() {
		return total_amount;
	}

	public void setTotal_amount(String total_amount) {
		this.total_amount = total_amount;
	}

	public String getOrder_info() {
		return order_info;
	}

	public void setOrder_info(String order_info) {
		this.order_info = order_info;
	}

	public String getGoods_name() {
		return goods_name;
	}

	public void setGoods_name(String goods_name) {
		this.goods_name = goods_name;
	}

	public String getGoods_url() {
		return goods_url;
	}

	public void setGoods_url(String goods_url) {
		this.goods_url = goods_url;
	}

	public String getTxn_purpose() {
		return txn_purpose;
	}

	public void setTxn_purpose(String txn_purpose) {
		this.txn_purpose = txn_purpose;
	}

	public String getPostscript() {
		return postscript;
	}

	public void setPostscript(String postscript) {
		this.postscript = postscript;
	}

}