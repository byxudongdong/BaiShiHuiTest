package com.packages;

public class JiBaoSQLite {
	public String  DestinationSite ;
	public String  BagNumber ;
	public String  BillCode ;
	public String  ScanDate ;
	public String ScanTime;
	public String X_Auth_User;
	public String X_Auth_Site;
	public int index;
	public int send_up_flag;
	public JiBaoSQLite() {
		// TODO Auto-generated constructor stub
	}
	public JiBaoSQLite(String destinationSite, String bagNumber,
			String billCode, String scanDate, String scanTime,String x_Auth_User,String x_Auth_Site,int flag) {
		super();
		DestinationSite = destinationSite;
		BagNumber = bagNumber;
		BillCode = billCode;
		ScanDate = scanDate;
		ScanTime = scanTime;
		X_Auth_User = x_Auth_User;
		X_Auth_Site = x_Auth_Site;
		send_up_flag = flag;
	}
	public int getSend_up_flag() {
		return send_up_flag;
	}
	public void setSend_up_flag(int send_up_flag) {
		this.send_up_flag = send_up_flag;
	}
	public String getX_Auth_User() {
		return X_Auth_User;
	}
	public void setX_Auth_User(String x_Auth_User) {
		X_Auth_User = x_Auth_User;
	}
	public String getX_Auth_Site() {
		return X_Auth_Site;
	}
	public void setX_Auth_Site(String x_Auth_Site) {
		X_Auth_Site = x_Auth_Site;
	}
	public String getDestinationSite() {
		return DestinationSite;
	}
	public void setDestinationSite(String destinationSite) {
		DestinationSite = destinationSite;
	}
	public String getBagNumber() {
		return BagNumber;
	}
	public void setBagNumber(String bagNumber) {
		BagNumber = bagNumber;
	}
	public String getBillCode() {
		return BillCode;
	}
	public void setBillCode(String billCode) {
		BillCode = billCode;
	}
	public String getScanDate() {
		return ScanDate;
	}
	public void setScanDate(String scanDate) {
		ScanDate = scanDate;
	}
	public String getScanTime() {
		return ScanTime;
	}
	public void setScanTime(String scanTime) {
		ScanTime = scanTime;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	
}
