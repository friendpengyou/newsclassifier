package com.yuan.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class ReadContentFromURL {
	/**
	 * 根据URL路径，获取服务器返回的字符串
	 */
	public static String getContentByUrl(String urlStr)throws Exception{
		HttpURLConnection connection=null;
		String tempStr = null;
		try{
			URL url=new URL(urlStr);
			StringBuffer bankXmlBuffer=new StringBuffer();
			//创建URL连接，提交到数据，获取返回结果
			connection=(HttpURLConnection)url.openConnection();
			connection.setRequestMethod("POST");
			connection.setDoOutput(true);
			//connection.setRequestProperty("User-Agent","directclient");
			connection.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.80 Safari/537.36");
			PrintWriter out=new PrintWriter(new OutputStreamWriter(connection.getOutputStream(),"GBK"));
			out.println();
			out.close();
			BufferedReader in=new BufferedReader(new InputStreamReader(connection
			.getInputStream(),"UTF-8"));
			String inputLine;
			while((inputLine = in.readLine())!=null){
				bankXmlBuffer.append(inputLine);
			}
			in.close();
			tempStr = bankXmlBuffer.toString();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return "error";
		}finally{
			if(connection!=null)
				connection.disconnect();
		}
		return tempStr;
	} 
}
