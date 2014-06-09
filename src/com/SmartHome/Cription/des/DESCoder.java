package com.SmartHome.Cription.des;
import java.security.*;

import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
public abstract class DESCoder {
	public  static final  String KEY_ALGORITHM = "DES";//加密算法DES
	
	//加密算法/工作模式/填充方式
	public  static final  String CIPHER_ALGORITHM = "DES/ECB/PKCS5Padding";
	
	/*转换密钥
	 * @param key 二进制密钥
	 * @return key 密钥
	 * */
	private static Key toKey(byte[] key)throws Exception{
		DESKeySpec dks = new DESKeySpec(key);
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(KEY_ALGORITHM);
		SecretKey secretKey = keyFactory.generateSecret(dks);
		return secretKey;
	}
	
	/*解密
	 * @param data 待解密数据
	 * @param key 密钥
	 * @return byte[] 解密数据
	 * */
	public static byte[] decrypt(byte[] data,byte[] key)throws Exception{
		Key k = toKey(key);
		Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, k);
		return cipher.doFinal(data);
	}
	
	/*加密
	 * @param data 待加密数据
	 * @param key 密钥
	 * @return byte[] 加密数据
	 * */
	public static byte[] encrypt(byte[] data,byte[] key)throws Exception{
		Key k = toKey(key);
		Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, k);
		return cipher.doFinal(data);
	}
	
	/*生成密钥
	 * Java 只支持56位密钥
	 * @return byte[] 二进制密钥
	 * */
	public static byte[] initDESKey()throws Exception{
		KeyGenerator kg = KeyGenerator.getInstance(KEY_ALGORITHM);
		kg.init(56);
		SecretKey secretKey = kg.generateKey();
		return secretKey.getEncoded();
	}
}
