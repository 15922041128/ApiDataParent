package org.pbccrc.api.base.external.vip.grcredit;

import org.apache.commons.codec.binary.Base64;
import sun.misc.BASE64Decoder;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.concurrent.ConcurrentHashMap;

public class RSAUtil {

	/**
	 * 加解密算法类型
	 */
	private static final String KEY_ALGORITHM = "RSA";

	/**
	 * 签名算法
	 */
	private static final String SIGNATURE_ALGORITHM = "SHA1WithRSA";

	/**
	 * rsa加密算法描述
	 */
	private static final String CIPHER_ALGORITHM = "RSA/ECB/PKCS1Padding";

	private static final int KEY_BIT = 2048;

	private static ConcurrentHashMap<String, RSAPrivateKey> rsaPriKeyMap = new ConcurrentHashMap<String, RSAPrivateKey>();

	private static ConcurrentHashMap<String, RSAPublicKey> rsaPubKeyMap = new ConcurrentHashMap<String, RSAPublicKey>();

	public static byte[] encryptRSA(byte[] plainBytes, boolean useBase64Code, String charset, String publicKeyStr)
			throws Exception {

		PublicKey publicKey = getPublicKey(publicKeyStr);
		int keybit = KEY_BIT;
		int reservebytes = 11;
		Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
		int decryptBlock = keybit / 8;
		int encryptBlock = decryptBlock - reservebytes;

		int nBlock = (plainBytes.length / encryptBlock);
		if ((plainBytes.length % encryptBlock) != 0) {
			nBlock += 1;
		}

		ByteArrayOutputStream outbuf = new ByteArrayOutputStream(nBlock * decryptBlock);
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);

		for (int offset = 0; offset < plainBytes.length; offset += encryptBlock) {
			int inputLen = (plainBytes.length - offset);
			if (inputLen > encryptBlock) {
				inputLen = encryptBlock;
			}
			byte[] encryptedBlock = cipher.doFinal(plainBytes, offset, inputLen);
			outbuf.write(encryptedBlock);
		}
		if (useBase64Code) {
			return Base64.encodeBase64String(outbuf.toByteArray()).getBytes(charset);
		} else {
			return outbuf.toByteArray();
		}
	}

	public static byte[] decryptRSA(byte[] cryptedBytes, boolean useBase64Code, String charset, String rsaPrivateKeyStr)
			throws Exception {
		RSAPrivateKey rsAPrivateKey = getPrivateKey(rsaPrivateKeyStr);
		byte[] data = null;
		if (useBase64Code) {
			data = Base64.decodeBase64(new String(cryptedBytes, charset));
		} else {
			data = cryptedBytes;
		}

		int keybit = KEY_BIT;
		int reservebytes = 11;
		Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
		int decryptBlock = keybit / 8;
		int encryptBlock = decryptBlock - reservebytes;
		int nBlock = (data.length / decryptBlock);
		ByteArrayOutputStream outbuf = new ByteArrayOutputStream(nBlock * encryptBlock);
		cipher.init(Cipher.DECRYPT_MODE, rsAPrivateKey);
		for (int offset = 0; offset < data.length; offset += decryptBlock) {
			int inputLen = (data.length - offset);
			if (inputLen > decryptBlock) {
				inputLen = decryptBlock;
			}
			byte[] decryptedBlock = cipher.doFinal(data, offset, inputLen);
			outbuf.write(decryptedBlock);
		}
		outbuf.flush();
		outbuf.close();
		return outbuf.toByteArray();
	}

	public static String generateSign(byte[] data, String privateKey) throws Exception {
		byte[] keyBytes = Base64.decodeBase64(privateKey);
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
		Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
		signature.initSign(privateK);
		signature.update(data);
		return Base64.encodeBase64String(signature.sign());
	}

	public static boolean verifySign(byte[] data, String publicKey, String sign) throws Exception {
		byte[] keyBytes = Base64.decodeBase64(publicKey);
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		PublicKey publicK = keyFactory.generatePublic(keySpec);
		Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
		signature.initVerify(publicK);
		signature.update(data);
		return signature.verify(Base64.decodeBase64(sign));
	}

	private static RSAPublicKey getPublicKey(String publicKeyStr) throws Exception {
		RSAPublicKey publicKey = rsaPubKeyMap.get(publicKeyStr);
		if (publicKey != null) {
			return publicKey;
		}
		try {
			BASE64Decoder base64Decoder = new BASE64Decoder();
			byte[] buffer = base64Decoder.decodeBuffer(publicKeyStr);
			KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
			publicKey = (RSAPublicKey) keyFactory.generatePublic(keySpec);
			rsaPubKeyMap.put(publicKeyStr, publicKey);
			return publicKey;
		} catch (NoSuchAlgorithmException e) {
			throw new Exception("无此算法");
		} catch (InvalidKeySpecException e) {
			throw new Exception("公钥非法");
		} catch (IOException e) {
			throw new Exception("公钥数据内容读取错误");
		} catch (NullPointerException e) {
			throw new Exception("公钥数据为空");
		}
	}

	private static RSAPrivateKey getPrivateKey(String privateKeyStr) throws Exception {
		RSAPrivateKey privateKey = rsaPriKeyMap.get(privateKeyStr);
		if (privateKey != null) {
			return privateKey;
		}
		try {
			BASE64Decoder base64Decoder = new BASE64Decoder();
			byte[] buffer = base64Decoder.decodeBuffer(privateKeyStr);
			PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
			KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
			privateKey = (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
			rsaPriKeyMap.put(privateKeyStr, privateKey);
			return privateKey;
		} catch (NoSuchAlgorithmException e) {
			throw new Exception("无此算法");
		} catch (InvalidKeySpecException e) {
			throw new Exception("私钥非法");
		} catch (IOException e) {
			throw new Exception("私钥数据内容读取错误");
		} catch (NullPointerException e) {
			throw new Exception("私钥数据为空");
		}
	}


	public static void main(String[] args) {

	}
}
