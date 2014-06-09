package com.SmartHome.Cription.md5;

import java.security.MessageDigest;
public class MD5Coder {
	public static String sign(byte[] data) throws Exception{
		MessageDigest mdDigest = MessageDigest.getInstance("MD5");
		int i; 
		byte[] b = mdDigest.digest(data);
		StringBuffer buf = new StringBuffer(""); 
		for (int offset = 0; offset < b.length; offset++) { 
			i = b[offset]; 
			if(i<0) i+= 256; 
			if(i<16) 
			buf.append("0"); 
			buf.append(Integer.toHexString(i)); 
		}
		return buf.toString();
	}
}
