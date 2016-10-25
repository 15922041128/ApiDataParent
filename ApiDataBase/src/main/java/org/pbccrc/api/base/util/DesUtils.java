package org.pbccrc.api.base.util;
import java.io.IOException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
 
@SuppressWarnings("restriction")
public class DesUtils {
 
    private final static String DES = "DES";
    private static final String KEY = "0002000200020002";
 
	public static void main(String[] args) throws Exception {
//		String data = "123 456";
//		String key = "wang!@#$%";
//		System.err.println(encrypt(data, key));
//		System.err.println(decrypt(encrypt(data, key), key));
		String driverClassName = "com.mysql.jdbc.Driver";
		String url = "jdbc:mysql://127.0.0.1:3306/apidb?useUnicode=true&amp;characterEncoding=UTF-8&amp;zeroDateTimeBehavior=convertToNull";
		String username = "root";
		String passwrod = "root";
		String username2 = "openApi";
		String passwrod2 = "1234qwer";
		
		driverClassName = encrypt(driverClassName, KEY);
		url = encrypt(url, KEY);
		username = encrypt(username, KEY);
		passwrod = encrypt(passwrod, KEY);
		username2 = encrypt(username2, KEY);
		passwrod2 = encrypt(passwrod2, KEY);
		
		System.out.println("加密后 : " + driverClassName);
		System.out.println("加密后 : " + url);
		System.out.println("加密后 : " + username);
		System.out.println("加密后 : " + passwrod);
		System.out.println("加密后 : " + username2);
		System.out.println("加密后 : " + passwrod2);
		
		driverClassName = decrypt(driverClassName, KEY);
		url = decrypt(url, KEY);
		username = decrypt(username, KEY);
		passwrod = decrypt(passwrod, KEY);
		username2 = decrypt(username2, KEY);
		passwrod2 = decrypt(passwrod2, KEY);
		
		System.out.println("解密后 : " + driverClassName);
		System.out.println("解密后 : " + url);
		System.out.println("解密后 : " + username);
		System.out.println("解密后 : " + passwrod);
		System.out.println("解密后 : " + username2);
		System.out.println("解密后 : " + passwrod2);
	}
     
    /**
     * Description 根据键值进行加密
     * @param data 
     * @param key  加密键byte数组
     * @return
     * @throws Exception
     */
    public static String encrypt(String data, String key) throws Exception {
        byte[] bt = encrypt(data.getBytes(), key.getBytes());
        String strs = new BASE64Encoder().encode(bt);
        return strs;
    }
 
    /**
     * Description 根据键值进行解密
     * @param data
     * @param key  加密键byte数组
     * @return
     * @throws IOException
     * @throws Exception
     */
    public static String decrypt(String data, String key) throws IOException,
            Exception {
        if (data == null)
            return null;
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] buf = decoder.decodeBuffer(data);
        byte[] bt = decrypt(buf,key.getBytes());
        return new String(bt);
    }
 
    /**
     * Description 根据键值进行加密
     * @param data
     * @param key  加密键byte数组
     * @return
     * @throws Exception
     */
    public static byte[] encrypt(byte[] data, byte[] key) throws Exception {
        // 生成一个可信任的随机数源
        SecureRandom sr = new SecureRandom();
 
        // 从原始密钥数据创建DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(key);
 
        // 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
        SecretKey securekey = keyFactory.generateSecret(dks);
 
        // Cipher对象实际完成加密操作
        Cipher cipher = Cipher.getInstance(DES);
 
        // 用密钥初始化Cipher对象
        cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);
 
        return cipher.doFinal(data);
    }
     
     
    /**
     * Description 根据键值进行解密
     * @param data
     * @param key  加密键byte数组
     * @return
     * @throws Exception
     */
    public static byte[] decrypt(byte[] data, byte[] key) throws Exception {
        // 生成一个可信任的随机数源
        SecureRandom sr = new SecureRandom();
 
        // 从原始密钥数据创建DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(key);
 
        // 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
        SecretKey securekey = keyFactory.generateSecret(dks);
 
        // Cipher对象实际完成解密操作
        Cipher cipher = Cipher.getInstance(DES);
 
        // 用密钥初始化Cipher对象
        cipher.init(Cipher.DECRYPT_MODE, securekey, sr);
 
        return cipher.doFinal(data);
    }
}