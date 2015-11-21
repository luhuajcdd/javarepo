package dehua.java.encrypt;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/***
 * Des�ļ����ܽ���
 * 
 * @author spring sky<br>
 * Email:vipa1888@163.com<br>
 * QQ:840950105
 * 
 */
public class DesFileEncrypt {
	/**
	 * Ҫ���ܵ��ļ�·���б�
	 */
	public static String[] filePath = { "C:/Users/ZOZT/Desktop/change.txt" };
	/**
	 * ���ܺ���ļ�·���б�
	 */
	public static String[] outFilePath = new String[filePath.length];
	private static final String KEY = "spring sky";

	public DesFileEncrypt() {
		super();
		getKey(KEY);
		initCipher();
		//��ʼ����ʼ�����ļ�
		crateEncryptFile();
	}

	private Key key;
	
	/***
	 * ��������
	 */
	private Cipher cipherDecrypt;
	/**
	 * ��������
	 */
	private Cipher cipherEncrypt;

	/**
	 * �����ļ�ƽ�Ҽ�¼���ܺ���ļ�·��
	 * */
	private void crateEncryptFile() {
		String outPath = null;
		for (int i = 0; i < filePath.length; i++) {
			try {
				outPath = filePath[i].substring(0,filePath[i].lastIndexOf("."))+".bin";
				encrypt(filePath[i], outPath);
				outFilePath[i] = outPath;
				System.out.println(filePath[i]+"������ɣ����ܺ���ļ���:"+outFilePath[i]);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		System.out.println("=========================�������=======================");
		
	}

	/**
	 * �����ļ��ĺ���
	 * 
	 * @param file
	 *            Ҫ���ܵ��ļ�
	 * @param destFile
	 *            ���ܺ��ŵ��ļ���
	 */
	public void encrypt(String file, String destFile) throws Exception {
		InputStream is = new FileInputStream(file);
		OutputStream out = new FileOutputStream(destFile);

		CipherInputStream cis = new CipherInputStream(is, cipherEncrypt);
		byte[] buffer = new byte[1024];
		int r;
		while ((r = cis.read(buffer)) > 0) {
			out.write(buffer, 0, r);
		}
		cis.close();
		is.close();
		out.close();
	}

	/***
	 * �����ļ�
	 * @param destFile
	 */
	public void decrypt(String destFile) {
		try {
			InputStream is = new FileInputStream(destFile);
			CipherInputStream cis = new CipherInputStream(is, cipherDecrypt);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					cis));
			String line = null;
			while ((line = reader.readLine()) != null) {
				System.out.println(line);
			}
			reader.close();
			cis.close();
			is.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void initCipher() {
		try {
			// ���ܵ�cipher
			cipherEncrypt = Cipher.getInstance("DES");
			cipherEncrypt.init(Cipher.ENCRYPT_MODE, this.key);
			// ���ܵ�cipher
			cipherDecrypt = Cipher.getInstance("DES");
			cipherDecrypt.init(Cipher.DECRYPT_MODE, this.key);
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		}
	}

	/**
	 * �Զ���һ��key
	 * 
	 * @param string
	 */
	public Key getKey(String keyRule) {
		// Key key = null;
		byte[] keyByte = keyRule.getBytes();
		// ����һ���յİ�λ����,Ĭ�������Ϊ0
		byte[] byteTemp = new byte[8];
		// ���û�ָ���Ĺ���ת���ɰ�λ����
		for (int i = 0; i < byteTemp.length && i < keyByte.length; i++) {
			byteTemp[i] = keyByte[i];
		}
		key = new SecretKeySpec(byteTemp, "DES");
		return key;
	}

	/**
	 * ��������
	 * 
	 * @return
	 */
	public Cipher getCipherEdcrypt() {
		return cipherDecrypt;
	}

	/**
	 * ��������
	 * 
	 * @return
	 */
	public Cipher getCipherEncrypt() {
		return cipherEncrypt;
	}

	/***
	 * ���Լ��ܽ���
	 * @param args
	 */
	public static void main(String[] args) {
		DesFileEncrypt desFileEncrypt = new DesFileEncrypt();
		desFileEncrypt.decrypt(outFilePath[0]);  //���ܵ�һ���ļ�ƽ�Ҳ��Խ��ܺ�Ľ��
	}
}


