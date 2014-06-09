package com.SmartHome.Cription.rsa;

import android.util.Log;
import com.SmartHome.DataType.PublicState;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;

import javax.crypto.Cipher;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

public abstract class RSACoder{
	public static final String KEY_ALGORITHM = "RSA";//�ǶԳƼ�����Կ�㷨
	public static final String SIGNATURE_ALGORITHM = "MD5withRSA";
	private static final String PUBLIC_KEY = "RSAPublicKey";//��Կ
	private static final String PRIVATE_KEY = "RSAPrivateKey";//˽Կ
	
	//˽Կ�ļ�
	private static final String PRIVATE_KEY_FILE = "pkcs8_priv.pem";
	
	//��Կ�ļ�
	public static final String PUBLIC_KEY_FILE = "pub.key";
	private static final int MAX_ENCRYPT_BLOCK = 117;//RSA���������Ĵ�С 
	private static final int MAX_DECRYPT_BLOCK = 128;//RSA���������Ĵ�С 
	private static final int KEY_SIZE = 512;//��Կ���ȣ�������64�ı���
	/*ǩ��
	 * @param data ��ǩ�����
	 * @param privateKey ˽Կ
	 * @return byte[] ����ǩ��
	 * */
	public static byte[] sign(byte[] data,byte[] privateKey) throws Exception{
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(privateKey);//ת��˽Կ����
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);//ʵ����Կ����
		PrivateKey prikey = keyFactory.generatePrivate(pkcs8KeySpec);//���˽Կ
		Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
		signature.initSign(prikey);
		signature.update(data);
		return signature.sign();
	}
	
	/*У��
	 * @param data ��У�����
	 * @param publicKey ��Կ
	 * @param sign ����ǩ��
	 * @return boolean У��ɹ�����true ʧ�ܷ���false
	 * */
	public static boolean verify(byte[] data, byte[] publicKey, byte[] sign)throws Exception{
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKey);//ת����Կ����
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);//ʵ����Կ����
		PublicKey pubKey = keyFactory.generatePublic(keySpec);//��ɹ�Կ
		Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
		signature.initVerify(pubKey);
		signature.update(data);
		return signature.verify(sign);
	}
	
	/*ȡ��˽Կ
	 * @param keyMap ��ԿMap
	 * @return byte[]  ˽Կ
	 * */
	public static byte[] getPrivateKey(Map<String, Object>keyMap) throws Exception{
		//Key key = (Key) keyMap.get(PRIVATE_KEY);
		//return key.getEncoded();
		return (byte [])keyMap.get(PRIVATE_KEY);
	}
	
	/*ȡ�ù�Կ
	 * @param keyMap ��ԿMap
	 * @return byte[]  ��Կ
	 * */
	public static byte[] getPublicKey(Map<String, Object>keyMap) throws Exception{
		//Key key = (Key) keyMap.get(PUBLIC_KEY);
		//return  key.getEncoded();
		return (byte [])keyMap.get(PUBLIC_KEY);
	}
	
	/*��ʼ����Կ
	 * @param Map ��ԿMap
	 * */
	public static Map<String, Object> initKey() throws Exception{
        Log.d("privatekey","into initkey");
		Security.addProvider(new BouncyCastleProvider());
        InputStream pri_stream = PublicState.getInstance().getResources().getAssets().open(PRIVATE_KEY_FILE);
        InputStream pub_stream = PublicState.getInstance().getResources().getAssets().open(PUBLIC_KEY_FILE);

        BufferedReader privateKey = new BufferedReader(new InputStreamReader(pri_stream));
        BufferedReader publicKey = new BufferedReader(new InputStreamReader(pub_stream));
        String strPrivateKey = "";
        String strPublicKey = "";
        String line = "";

        while((line = privateKey.readLine()) != null){
            strPrivateKey += line;
        }
        while((line = publicKey.readLine()) != null){
            strPublicKey += line;
        }



        privateKey.close();
        publicKey.close();
        pri_stream.close();
        pub_stream.close();

        // ˽Կ��Ҫʹ��pkcs8��ʽ�ģ���Կʹ��x509��ʽ��
        String strPrivKey = strPrivateKey.replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "");                
        String strPubKey = strPublicKey.replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "");
       // System.out.println("˽Կ"+strPrivKey);
        //System.out.println("��Կ"+strPubKey);    

        byte [] privKeyByte = Base64.decode(strPrivKey);
        byte [] pubKeyByte = Base64.decode(strPubKey);
		Map<String, Object> keyMap = new HashMap<String, Object>(2);
		keyMap.put(PUBLIC_KEY, pubKeyByte);
		keyMap.put(PRIVATE_KEY, privKeyByte);
		return keyMap;
	}

	/*��Կ����
	 * @param data ��������
	 * @param key ��Կ
	 * @return byte[] �������
	 * */
	public static byte[] encryptByPublicKey(byte[] data,byte[] key) throws Exception{
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(key);//ʵ����Կ�滮����
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		PublicKey publicKey = keyFactory.generatePublic(x509KeySpec);//�����Կ�淶����ɹ�Կ����
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");//RSA/ECB/PKCS1Padding//keyFactory.getAlgorithm()
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		return cipher.doFinal(data);
	}

	/*˽Կ����
	 * @param data ��������
	 * @param key ˽Կ
	 * @return byte[] �������
	 * */
	public static byte[] decryptByPrivateKey(byte[] data,byte[] key)throws Exception{
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(key);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		PrivateKey privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");//RSA/ECB/PKCS1Padding//keyFactory.getAlgorithm();
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		return cipher.doFinal(data);
	}
 }
