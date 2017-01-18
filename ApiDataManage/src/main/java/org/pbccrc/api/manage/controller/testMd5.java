package org.pbccrc.api.manage.controller;

import java.security.MessageDigest;

public class testMd5 {
	public static void main(String[] args) {
		String md5Str = MD5Encoder("吕少兵", "utf-8");
		System.out.println(md5Str);
		
	}
	public final static String MD5Encoder(String s, String charset) {
        try {
            byte[] btInput = s.getBytes(charset);
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            mdInst.update(btInput);
            byte[] md = mdInst.digest();
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < md.length; i++) {
                int val = ((int) md[i]) & 0xff;
                if (val < 16){
                	sb.append("0");
                }
                sb.append(Integer.toHexString(val));
            }
            return sb.toString();
        } catch (Exception e) {
            return null;
        }
    }

}
