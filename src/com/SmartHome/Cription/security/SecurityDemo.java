package com.SmartHome.Cription.security;

import android.util.Log;
import com.SmartHome.Cription.des.DESCoder;
import com.SmartHome.Cription.md5.MD5Coder;
import com.SmartHome.Cription.rsa.RSACoder;
import com.SmartHome.Cription.time.TimeDemo;

import java.util.Map;


public class SecurityDemo {
	private byte[] DESkey;
	private byte[] RSApublicKey;
	private byte[] RSAprivateKey;
	private TimeDemo time = new TimeDemo();
	private Base64Demo base64Demo = new Base64Demo();
	public SecurityDemo() throws Exception{
        Log.d("initsecurity","intosecurity");
		initRSAKey();
	}
	/*��ʼ��RSA��Կ
	 * */
	public void initRSAKey() throws Exception{
        Log.d("initRsa", "into initrsa");
		Map<String, Object> keyMap = RSACoder.initKey();
		this.RSApublicKey = RSACoder.getPublicKey(keyMap);
		this.RSAprivateKey = RSACoder.getPrivateKey(keyMap);
	}
	
	//���DES��Կ
	public byte[] getDESKey() throws Exception {
		this.DESkey = DESCoder.initDESKey();
		return DESkey;
	}

	//���RSA��Կ
	public byte[] getRSAPublicKey() {
		return this.RSApublicKey;
	}

	//���RSA˽Կ
	public byte[] getRSAPrivateKey() {
		return this.RSAprivateKey;
	}
	
	/*��ü�����ݡ���DES����
	 * @param metadata �������
	 * @return byte[] �������
	 * */
	public String getFirstEncodeData(String data) throws Exception{
		//data = data+"|"+time.getStringDate()  +"|"+ MD5Coder.sign(data.getBytes());//��Ԫ����м���ʱ���
		//String public_deskey = "helloworld";

        data = time.getStringDate()  +"|"+ MD5Coder.sign(data.getBytes())+"|"+data;//��Ԫ����м���ʱ���
		byte[] encodedData = DESCoder.encrypt(data.getBytes(), "12345678".getBytes());
		return base64Demo.encode(encodedData);
	}
    public String getEncodeData(String data) throws Exception{
        data = time.getStringDate()  +"|"+ MD5Coder.sign(data.getBytes())+"|"+data;//��Ԫ����м���ʱ���
        byte[] encodedData = DESCoder.encrypt(data.getBytes(), DESkey);
        return base64Demo.encode(encodedData);
    }
	/*��ý�����ݡ���DES����
	 * @param metadata �������
	 * @return byte[] �������
	 * */
	public String getDecodeData(String encodeData)throws Exception{
		byte[] decodeData = DESCoder.decrypt(base64Demo.decode(encodeData).getBytes("iso8859-1"), DESkey);
		return new String(decodeData);
	}
    public String getFirstDecodeData(String encodeData)throws Exception{
        byte[] decodeData = DESCoder.decrypt(base64Demo.decode(encodeData).getBytes("iso8859-1"), "12345678".getBytes());
        return new String(decodeData);
    }
	public String getData(String decodeData){
		String[] data = decodeData.split("\\|");
		return data[2];
	}
	/*��ü��ܺ��DES��Կ
	 * @param key DES��Կ
	 * @return byte[] ���ܺ��DES��Կ
	 * */
	public String getRSAEnceodeKey(byte[] key)throws Exception{
		byte[] encodedData = RSACoder.encryptByPublicKey(key, RSApublicKey);
		return base64Demo.encode(encodedData);
	}
	/*��ý��ܺ��DES��Կ
	 * @param encodeKey ���ܺ��DES��Կ
	 * @return byte[] DES��Կ
	 * */
	public byte[] getRSADecodeKey(String encodeKey)throws Exception{
		byte[] decodeData = RSACoder.decryptByPrivateKey(base64Demo.decode(encodeKey).getBytes("iso8859-1"), RSAprivateKey);
		return decodeData;
	}
	/*�Խ�����ݽ���У��
	 * @param decodeData �������
	 * @param sign ����ǩ��
	 * @return boolean У��ɹ�����true��ʧ�ܷ���false
	 * */
	public boolean verifyData(String decodeData)throws Exception{
		String[] data = decodeData.split("\\|");
		if(time.distanceBetweenCurren(data[0]) <= 180){//���յ������֮ǰ����ݣ�����Ϊ�������Ч
			
			//��֤ժҪ��Ϣ�Ƿ�һ�£����һ�£���ʾ�������
            return data[1].equals(MD5Coder.sign(data[2].getBytes()));
		}else {
			return false;
		}
	}
	
	public static void main(String[] args)throws Exception {
		// TODO �Զ���ɵķ������
		SecurityDemo des = new SecurityDemo();
		Base64Demo bDemo = new Base64Demo();
		des.initRSAKey();
		String inputString = "abcdefghijklnmopqrstwxyz1234567890";
		byte[] data = inputString.getBytes();
		byte[] key = des.getDESKey();
		String encodeKey = des.getRSAEnceodeKey(key);
		System.err.println(bDemo.encode(key));
		System.err.println("���ܺ���Կ:"+encodeKey);
		byte[] decodekey = des.getRSADecodeKey(encodeKey);
		System.err.println("���ܺ���Կ��\t"+bDemo.encode(decodekey));
		
		String encodedata =des. getEncodeData(inputString);
		System.err.println("���ܺ�\t"+encodedata);
		
		String dncodedata = des.getDecodeData(encodedata);
		
		System.err.println("���ܺ�\t"+new String(dncodedata));
	
		System.err.println(des.verifyData(dncodedata));
		System.err.println(des.getData(dncodedata));
	}
}
