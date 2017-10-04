package com.bSecure;

public class BSecureData {

	String phone_no;
	String _cname;
	String emailId;
	public String getPhone_no() {
		return phone_no;
	}

	public void setPhone_no(String phone_no) {
		this.phone_no = phone_no;
	}

	public BSecureData(String cname, String phone_no,String emailId) {
		this._cname = cname;
		this.phone_no =phone_no;
		this.emailId = emailId;
	}

	public String getEmailId(){
		return this.emailId;
	}

	public void setEmailId(String emailId){
		this.emailId = emailId;
	}

	public String getName1() {
		return this._cname;
	}

	public void setName1(String cname) {
		this._cname = cname;
	}

}
