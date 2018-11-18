package com.accp.test.bean.txn;

import java.util.List;

import com.accp.test.bean.BaseBean;

public class Refund extends BaseBean {
	private static final long serialVersionUID = 1L;
	private String user_id;
	private String notify_url;
	private String refund_reason;
	private OriginalOrderInfo originalOrderInfo;
	private RefundOrderInfo refundOrderInfo;
	private List<RefundMethods> refundMethods;

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

	public String getRefund_reason() {
		return refund_reason;
	}

	public void setRefund_reason(String refund_reason) {
		this.refund_reason = refund_reason;
	}

	public OriginalOrderInfo getOriginalOrderInfo() {
		return originalOrderInfo;
	}

	public void setOriginalOrderInfo(OriginalOrderInfo originalOrderInfo) {
		this.originalOrderInfo = originalOrderInfo;
	}

	public RefundOrderInfo getRefundOrderInfo() {
		return refundOrderInfo;
	}

	public void setRefundOrderInfo(RefundOrderInfo refundOrderInfo) {
		this.refundOrderInfo = refundOrderInfo;
	}

	public List<RefundMethods> getRefundMethods() {
		return refundMethods;
	}

	public void setRefundMethods(List<RefundMethods> refundMethods) {
		this.refundMethods = refundMethods;
	}

}