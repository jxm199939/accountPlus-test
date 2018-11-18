package com.accp.test.bean.txn;

import java.io.Serializable;

public class BankCardInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	private String linked_acctno;
	private String linked_agrtno;
	private String linked_phone;
	private String linked_acctname;
	private String id_type;
	private String id_no;
	private String cvv2;
	private String valid_thru;

	public String getLinked_acctno() {
		return linked_acctno;
	}

	public void setLinked_acctno(String linked_acctno) {
		this.linked_acctno = linked_acctno;
	}

	public String getLinked_agrtno() {
		return linked_agrtno;
	}

	public void setLinked_agrtno(String linked_agrtno) {
		this.linked_agrtno = linked_agrtno;
	}

	public String getLinked_phone() {
		return linked_phone;
	}

	public void setLinked_phone(String linked_phone) {
		this.linked_phone = linked_phone;
	}

	public String getLinked_acctname() {
		return linked_acctname;
	}

	public void setLinked_acctname(String linked_acctname) {
		this.linked_acctname = linked_acctname;
	}

	public String getId_type() {
		return id_type;
	}

	public void setId_type(String id_type) {
		this.id_type = id_type;
	}

	public String getId_no() {
		return id_no;
	}

	public void setId_no(String id_no) {
		this.id_no = id_no;
	}

	public String getCvv2() {
		return cvv2;
	}

	public void setCvv2(String cvv2) {
		this.cvv2 = cvv2;
	}

	public String getValid_thru() {
		return valid_thru;
	}

	public void setValid_thru(String valid_thru) {
		this.valid_thru = valid_thru;
	}

}