package sendupSet;

import java.util.ArrayList;
import userMessage.SendScanDataList;
import android.util.Log;

import com.arrive.DaoJianSQLite;
import com.baishi.db.cn.FaJianSQLite;
import com.google.gson.Gson;
import com.packages.JiBaoSQLite;
public class JsonToString {
	public JsonToString() {
		// TODO Auto-generated constructor stub
	}
	public String getScanDataList(ArrayList<FaJianSQLite> fajianlist){
		ArrayList<SendScanDataList> array = new ArrayList<SendScanDataList>();
		for(int i = 0; i < fajianlist.size() ; i ++){
			SendScanDataList dataList = new SendScanDataList();
			dataList.setCID(i);
			dataList.setBillCode(fajianlist.get(i).getBillCode());
			dataList.setScanSite(fajianlist.get(i).getX_Auth_Site());
			dataList.setNextSite(fajianlist.get(i).getNextSite());
			dataList.setVehicleNo(fajianlist.get(i).getVehicleNo());
			dataList.setScanMan(fajianlist.get(i).getX_Auth_User());//mPreferences.getString("X-Auth-User", "")
			dataList.setScanTime(fajianlist.get(i).getScanDate() + "T" + fajianlist.get(i).getScanTime() + "+08:00");
			dataList.setItemCount(1);
			dataList.setDispatchClass("");
			dataList.setWeight("");
			dataList.setLocation(null);
			dataList.setCellTower(null);
			dataList.setDataType(1);
			dataList.setIsScanBillCode(true);
			dataList.setBarcodeType("BCT_CODE_128");
			array.add(dataList);
		}
		String ScanDataList = new Gson().toJson(array);
		Log.d("RRRRRRRRRR", ScanDataList);
		return ScanDataList;
	}
	
	public String getDaoJianDataList(ArrayList<DaoJianSQLite> daojianlist){
		ArrayList<SendScanDataList> array = new ArrayList<SendScanDataList>();
		for(int i = 0; i < daojianlist.size() ; i ++){
			SendScanDataList dataList = new SendScanDataList();
			dataList.setCID(i);
			dataList.setBillCode(daojianlist.get(i).getBillCode());
			dataList.setScanSite(daojianlist.get(i).getX_Auth_Site());
			dataList.setPreSite(daojianlist.get(i).getPreSite());
			dataList.setVehicleNo(daojianlist.get(i).getVehicleNo());
			dataList.setScanMan(daojianlist.get(i).getX_Auth_User());//mPreferences.getString("X-Auth-User", "")
			dataList.setScanTime(daojianlist.get(i).getScanDate() + "T" + daojianlist.get(i).getScanTime() + "+08:00");
			dataList.setItemCount(1);
			dataList.setDispatchClass("");
			dataList.setWeight("");
			dataList.setLocation(null);
			dataList.setCellTower(null);
			dataList.setDataType(1);
			dataList.setIsScanBillCode(true);
			dataList.setBarcodeType("BCT_CODE_128");
			array.add(dataList);
		}
		String ScanDataList = new Gson().toJson(array);
		Log.d("RRRRRRRRRR", ScanDataList);
		return ScanDataList;
	}
	
	public String getJiBaoDataList(ArrayList<JiBaoSQLite> jibaolist){
		ArrayList<SendScanDataList> array = new ArrayList<SendScanDataList>();
		for(int i = 0; i < jibaolist.size() ; i ++){
			SendScanDataList dataList = new SendScanDataList();
			dataList.setCID(i);
			dataList.setBillCode(jibaolist.get(i).getBillCode());
			dataList.setScanSite(jibaolist.get(i).getX_Auth_Site());
			dataList.setDestinationSite(jibaolist.get(i).getDestinationSite());
			dataList.setBagNumber(jibaolist.get(i).getBagNumber());
			dataList.setScanMan(jibaolist.get(i).getX_Auth_User());//mPreferences.getString("X-Auth-User", "")
			dataList.setScanTime(jibaolist.get(i).getScanDate() + "T" + jibaolist.get(i).getScanTime() + "+08:00");
			dataList.setItemCount(1);
			dataList.setDispatchClass("");
			dataList.setWeight("");
			dataList.setLocation(null);
			dataList.setCellTower(null);
			dataList.setDataType(1);
			dataList.setIsScanBillCode(true);
			dataList.setBarcodeType("BCT_CODE_128");
			array.add(dataList);
		}
		String ScanDataList = new Gson().toJson(array);
		Log.d("RRRRRRRRRR", ScanDataList);
		return ScanDataList;
	}
}
