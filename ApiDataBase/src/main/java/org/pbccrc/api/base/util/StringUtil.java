package org.pbccrc.api.base.util;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
	
	public enum JSON_TYPE{ 
	    /**JSONObject*/     
	    JSON_TYPE_OBJECT, 
	    /**JSONArray*/ 
	    JSON_TYPE_ARRAY, 
	    /**不是JSON格式的字符串*/ 
	    JSON_TYPE_ERROR 
    }
	
	 public static JSON_TYPE getJSONType(String str){ 
        if(isNull(str)){ 
            return JSON_TYPE.JSON_TYPE_ERROR; 
        } 
         
        final char[] strChar = str.substring(0, 1).toCharArray(); 
        final char firstChar = strChar[0]; 
         
        if(firstChar == '{'){ 
            return JSON_TYPE.JSON_TYPE_OBJECT; 
        }else if(firstChar == '['){ 
            return JSON_TYPE.JSON_TYPE_ARRAY; 
        }else{ 
            return JSON_TYPE.JSON_TYPE_ERROR; 
        } 
	    }

	/** 
     * 解码 Unicode \\uXXXX 
     * @param str 
     * @return 
     */  
    public static String decodeUnicode(String str) {  
        Charset set = Charset.forName("UTF-16");  
        Pattern p = Pattern.compile("\\\\u([0-9a-fA-F]{4})");  
        Matcher m = p.matcher( str );  
        int start = 0 ;  
        int start2 = 0 ;  
        StringBuffer sb = new StringBuffer();  
        while( m.find( start ) ) {  
            start2 = m.start() ;  
            if( start2 > start ){  
                String seg = str.substring(start, start2) ;  
                sb.append( seg );  
            }  
            String code = m.group( 1 );  
            int i = Integer.valueOf( code , 16 );  
            byte[] bb = new byte[ 4 ] ;  
            bb[ 0 ] = (byte) ((i >> 8) & 0xFF );  
            bb[ 1 ] = (byte) ( i & 0xFF ) ;  
            ByteBuffer b = ByteBuffer.wrap(bb);  
            sb.append( String.valueOf( set.decode(b) ).trim() );  
            start = m.end() ;  
        }  
        start2 = str.length() ;  
        if( start2 > start ){  
            String seg = str.substring(start, start2) ;  
            sb.append( seg );  
        }  
        return sb.toString() ;  
    }
    
    public static boolean isNull(String str) {
    	
    	if (null == str || Constants.BLANK.equals(str) || Constants.STR_NULL.equals(str) || str.length() == 0) {
    		return true;
    	}
    	
    	return false;
    }
    
    public static String string2MD5(String inStr){  
        MessageDigest md5 = null;  
        try{  
            md5 = MessageDigest.getInstance("MD5");  
        }catch (Exception e){  
            e.printStackTrace();  
            return "";  
        }  
        char[] charArray = inStr.toCharArray();  
        byte[] byteArray = new byte[charArray.length];  
  
        for (int i = 0; i < charArray.length; i++)  
            byteArray[i] = (byte) charArray[i];  
        byte[] md5Bytes = md5.digest(byteArray);  
        StringBuffer hexValue = new StringBuffer();  
        for (int i = 0; i < md5Bytes.length; i++){  
            int val = ((int) md5Bytes[i]) & 0xff;  
            if (val < 16)  
                hexValue.append("0");  
            hexValue.append(Integer.toHexString(val));  
        }  
        return hexValue.toString();  
    }
    
    /***
     * 生成4位验证码
     * @return
     */
    public static String createVCode4() {
    	int min = 1000;
    	int max = 9999;
    	Random rand = new Random();
	    int tmp = Math.abs(rand.nextInt());
	    int vCode = tmp % (max - min + 1) + min;
	    return String.valueOf(vCode);
    }
    
    public static String null2Blank(String str) {
    	if (isNull(str)) {
    		return Constants.BLANK;
    	}
    	return str;
    }
    
    public static String createUUID() {
    	UUID uuid = UUID.randomUUID();
		String uuidStr = uuid.toString();
		uuidStr = uuidStr.toUpperCase();
		return uuidStr;
    }
    
    public final static String MD5Encoder(String s) {
        try {
        	return MD5Encoder(s, "gbk");
        } catch (Exception e) {
            return Constants.BLANK;
        }
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
            return Constants.BLANK;
        }
    }
    
    public static String createApiKey() {
    	return UUID.randomUUID().toString().replaceAll(Constants.CONNECTOR_LINE, Constants.BLANK); 
    }
      
    public static void main(String[] args) {
//    	String str = "庄传礼";
//    	String md5Str = MD5Encoder(str);
//		System.out.println(str + " : " + md5Str);  
    	System.out.println(createApiKey());
    }  
}
