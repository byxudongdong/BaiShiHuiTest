package userMessage;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.util.Log;

public class ReadFileSN {
	
@SuppressWarnings("resource")
public static String  ReadFile(){
 String devicesID = "";
	//定义存储空间
 byte[] bs=new byte[1024];
		//建立通道对象
 FileInputStream inputStream;
	ByteArrayOutputStream outputStream = null;
	try {
		outputStream=new ByteArrayOutputStream();
		inputStream = new FileInputStream(new File("/system/hw_info/serial_numbers"));		
		int len =-1;
		if (inputStream!=null) {
			while ((len=inputStream.read(bs))!=-1) {
				outputStream.write(bs, 0, len);	
			}	
		}
	
	} catch (Exception e) {
		// TODO Auto-generated catch block
		
		e.printStackTrace();
		return null;
		
	} 
	devicesID=new String(outputStream.toByteArray());
	devicesID = devicesID.substring(0,9);
	return devicesID;
	}
		
}
