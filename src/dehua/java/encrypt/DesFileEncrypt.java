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
 * Des文件加密解密
 * 
 * @author spring sky<br>
 * Email:vipa1888@163.com<br>
 * QQ:840950105
 * 
 */
public class DesFileEncrypt {
	/**
	 * 要加密的文件路径列表
	 */
	public static String[] filePath = { "C:/Users/ZOZT/Desktop/change.txt" };
	/**
	 * 加密后的文件路径列表
	 */
	public static String[] outFilePath = new String[filePath.length];
	private static final String KEY = "spring sky";

	public DesFileEncrypt() {
		super();
		getKey(KEY);
		initCipher();
		//初始化开始加密文件
		crateEncryptFile();
	}

	private Key key;
	
	/***
	 * 解密密码
	 */
	private Cipher cipherDecrypt;
	/**
	 * 加密密码
	 */
	private Cipher cipherEncrypt;

	/**
	 * 加密文件平且记录加密后的文件路径
	 * */
	private void crateEncryptFile() {
		String outPath = null;
		for (int i = 0; i < filePath.length; i++) {
			try {
				outPath = filePath[i].substring(0,filePath[i].lastIndexOf("."))+".bin";
				encrypt(filePath[i], outPath);
				outFilePath[i] = outPath;
				System.out.println(filePath[i]+"加密完成，加密后的文件是:"+outFilePath[i]);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		System.out.println("=========================加密完成=======================");
		
	}

	/**
	 * 加密文件的核心
	 * 
	 * @param file
	 *            要加密的文件
	 * @param destFile
	 *            加密后存放的文件名
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
	 * 解密文件
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
			// 加密的cipher
			cipherEncrypt = Cipher.getInstance("DES");
			cipherEncrypt.init(Cipher.ENCRYPT_MODE, this.key);
			// 解密的cipher
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
	 * 自定义一个key
	 * 
	 * @param string
	 */
	public Key getKey(String keyRule) {
		// Key key = null;
		byte[] keyByte = keyRule.getBytes();
		// 创建一个空的八位数组,默认情况下为0
		byte[] byteTemp = new byte[8];
		// 将用户指定的规则转换成八位数组
		for (int i = 0; i < byteTemp.length && i < keyByte.length; i++) {
			byteTemp[i] = keyByte[i];
		}
		key = new SecretKeySpec(byteTemp, "DES");
		return key;
	}

	/**
	 * 解密密码
	 * 
	 * @return
	 */
	public Cipher getCipherEdcrypt() {
		return cipherDecrypt;
	}

	/**
	 * 加密密码
	 * 
	 * @return
	 */
	public Cipher getCipherEncrypt() {
		return cipherEncrypt;
	}

	/***
	 * 测试加密解密
	 * @param args
	 */
	public static void main(String[] args) {
		DesFileEncrypt desFileEncrypt = new DesFileEncrypt();
		desFileEncrypt.decrypt(outFilePath[0]);  //解密第一个文件平且测试解密后的结果
	}
}


