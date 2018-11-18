package com.accp.test.bean.txn;

import java.util.List;
import com.accp.test.bean.BaseBean;

public class PaymentBankcard extends BaseBean {
	private static final long serialVersionUID = 1L;
	private String txn_seqno;
	private String total_amount;
	private String risk_item;
	private PayerInfo payerInfo;
	private BankCardInfo bankCardInfo;
	private List<PayMethods> payMethods;

	public String getTxn_seqno() {
		return txn_seqno;
	}

	public void setTxn_seqno(String txn_seqno) {
		this.txn_seqno = txn_seqno;
	}

	public String getTotal_amount() {
		return total_amount;
	}

	public void setTotal_amount(String total_amount) {
		this.total_amount = total_amount;
	}

	public String getRisk_item() {
		return risk_item;
	}

	public void setRisk_item(String risk_item) {
		this.risk_item = risk_item;
	}

	public PayerInfo getPayerInfo() {
		return payerInfo;
	}

	public void setPayerInfo(PayerInfo payerInfo) {
		this.payerInfo = payerInfo;
	}

	public BankCardInfo getBankCardInfo() {
		return bankCardInfo;
	}

	public void setBankCardInfo(BankCardInfo bankCardInfo) {
		this.bankCardInfo = bankCardInfo;
	}

	public List<PayMethods> getPayMethods() {
		return payMethods;
	}

	public void setPayMethods(List<PayMethods> payMethods) {
		this.payMethods = payMethods;
	}

}