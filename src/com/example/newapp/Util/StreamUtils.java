package com.example.newapp.Util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class StreamUtils {

	/*
	 * 将输入流读取成String后返回
	 */
	public static String ReadFromStream(InputStream in) throws IOException{
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		
		int len = 0; //定义一个变量，记录长度
		
		byte[] buffer = new byte[1024];
		
		while((len=in.read(buffer)) != -1)
		{
			out.write(buffer, 0, len);
		}
		String result = out.toString();
		in.close();
		out.close();
		return result;
	}
}
