package com.arrive;

public class DaoJianSQLite {
	public String  BillCode ;
	public String  PreSite ;
	public String  VehicleNo ;
	public String  ScanTime ;
	public String ScanDate;
	public String X_Auth_User;
	public String X_Auth_Site;
	public int index;
	public int send_up_flag;
	public DaoJianSQLite() {
		// TODO Auto-generated constructor stub
	}
	
	public DaoJianSQLite(String billCode, String preSite, String vehicleNo,
			String scanTime, String scanDate,String x_Auth_User,String x_Auth_Site,int flag) {
		super();
		BillCode = billCode;
		PreSite = preSite;
		VehicleNo = vehicleNo;
		ScanTime = scanTime;
		ScanDate = scanDate;
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
	
	public String getPreSite() {
		return PreSite;
	}

	public void setPreSite(String preSite) {
		PreSite = preSite;
	}

	public String getVehicleNo() {
		return VehicleNo;
	}
	public void setVehicleNo(String vehicleNo) {
		VehicleNo = vehicleNo;
	}
	public String getScanTime() {
		return ScanTime;
	}
	public void setScanTime(String scanTime) {
		ScanTime = scanTime;
	}
	public void setIndex(int pox){
		index = pox;
	}
	public int getIndex(){
		return index;
	}
	
	
}
