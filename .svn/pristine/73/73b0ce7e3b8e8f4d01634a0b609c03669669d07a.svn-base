package com.baishihuitong.untils;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Set;

import com.example.baishihuitong.SendscanActivity;

import android.util.Log;


public class HttpUrl {
	//获得URL
	public HttpURLConnection createURL (String strURL,String method){
		
			try {
				URL url = new URL(strURL);
				HttpURLConnection conn;
				conn = (HttpURLConnection) url.openConnection();
				//设置连接超时
				conn.setReadTimeout(SendscanActivity.send_time_limit);
				//设置请求方法
				conn.setRequestMethod(method);
				conn.setDoInput(true);
				conn.setDoOutput(true);
				
				return conn;
			} catch (IOException e) {
				e.printStackTrace();
			}
		return null;
		
	}
	
	//获得转码后的值
	public String mapToString(Map<String, String> mapParam){
		
		String str = "";
		Set<String> keys = mapParam.keySet();//获得键对象集合
		for(String key: keys){
			try {
				str += key + "=" + URLEncoder.encode(mapParam.get(key), "utf-8") + "&";
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return str.length() > 0 ?str.substring(0, str.length() - 1 ): "";
	}
	
	public void mapToHeadParams(HttpURLConnection conn, Map<String, String> headParams){
		
		String str = "";
		Set<String> keys = headParams.keySet();//获得键对象集合
		for(String key: keys){
			try {
				conn.setRequestProperty(key, headParams.get(key));
				str = key + ":" + URLEncoder.encode(headParams.get(key), "utf-8");
				System.out.println("头文件：" + str);
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	//向服务器发送参数
	
	public void sendParam(HttpURLConnection conn,String param){
		try {
			OutputStream os = conn.getOutputStream();
			os.write(param.getBytes());
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	
	}
	
	//将字节流转换成String
	
	public String inputStreamToString(InputStream is){
			try {
				BufferedReader br = new BufferedReader(new InputStreamReader(is,"utf-8"));
				
				String line = br.readLine();
				String s = "";
				while(line != null){
					s += line;
					line = br.readLine();
				}
				
				br.close();
				is.close();
				return s;
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		return null;
		
	}
	
	//提交POST请求，返回相应结果
	
	public String post(String strURL, Map<String, String> params, Map<String, String> headParams){
		HttpURLConnection conn = this.createURL(strURL,"POST");
		
		mapToHeadParams(conn, headParams);
		
		String paramString = this.mapToString(params);
		Log.i("请求参数：", paramString);
		this.sendParam(conn, paramString);
		try {
			int i = conn.getResponseCode();
			if(i == 200){
				Log.i("200：", "mmmmmmmmm");
				InputStream is = conn.getInputStream();
				return this.inputStreamToString(is);
			}else{
				return "" + i;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	//提交GET请求
	
	public String get(String strUrl, Map<String, String> params){
		//将参数转成字符串
		String paramString = this.mapToString(params);
		String newURL = strUrl + "?" + paramString;
		HttpURLConnection conn = this.createURL(newURL,"GET");
		//响应结果
		try {
			if(conn.getResponseCode() == 200){
				InputStream is = conn.getInputStream();
				String s = this.inputStreamToString(is);
				return s;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
		
	}
	
}