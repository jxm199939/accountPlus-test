package com.accp.test.bean.txn;

import java.util.List;

import com.accp.test.bean.BaseBean;

public class SecuredConfirm extends BaseBean {
	private static final long serialVersionUID = 1L;
	private String user_id;
	private String notify_url;
	private String confirm_mode;
	private OriginalOrderInfo originalOrderInfo;
	private ConfirmOrderInfo confirmOrderInfo;
	private List<PayeeInfo> payeeInfo;

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getNotify_url() {
		return notify_url;
	}

	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}

	public String getConfirm_mode() {
		return confirm_mode;
	}

	public void setConfirm_mode(String confirm_mode) {
		this.confirm_mode = confirm_mode;
	}

	public OriginalOrderInfo getOriginalOrderInfo() {
		return originalOrderInfo;
	}

	public void setOriginalOrderInfo(OriginalOrderInfo originalOrderInfo) {
		this.originalOrderInfo = originalOrderInfo;
	}

	public ConfirmOrderInfo getConfirmOrderInfo() {
		return confirmOrderInfo;
	}

	public void setConfirmOrderInfo(ConfirmOrderInfo confirmOrderInfo) {
		this.confirmOrderInfo = confirmOrderInfo;
	}

	public List<PayeeInfo> getPayeeInfo() {
		return payeeInfo;
	}

	public void setPayeeInfo(List<PayeeInfo> payeeInfo) {
		this.payeeInfo = payeeInfo;
	}

}