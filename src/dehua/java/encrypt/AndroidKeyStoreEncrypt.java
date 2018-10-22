package dehua.java.encrypt;

import java.io.File;
import java.io.FileInputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.Cipher;

public class AndroidKeyStoreEncrypt {
	public static void main(String[] args) {

		try {
			String curDir = System.getProperty("user.dir");
			// 用证书的私钥解密 - 该私钥存在生成该证书的密钥库中
			FileInputStream fis2 = new FileInputStream(curDir + "/src/dehua/java/encrypt/dehua_tec.jks");
			KeyStore ks = KeyStore.getInstance("JKS"); // 加载证书库
			char[] kspwd = "123456".toCharArray(); // 证书库密码
			char[] keypwd = "123456".toCharArray(); // 证书密码
			String alias = "dehua";// 别名
			ks.load(fis2, kspwd); // 加载证书
			PrivateKey privateKey = (PrivateKey) ks.getKey(alias, keypwd); // 获取证书私钥
			PublicKey publicKey = ks.getCertificate(alias).getPublicKey();// 获取证书公钥
			fis2.close();


			// 测试加密解密字符串
			String srcContent = "今天天气不错。";

			// 将字符串使用公钥加密后，再用私钥解密后，验证是否能正常还原。
			// 因为非对称加密算法适合对小数据量的数据进行加密和解密，而且性能比较差，所以在实际的操作过程中，我们通常采用的方式是：采用非对称加密算法管理对称算法的密钥，然后用对称加密算法加密数据，这样我们就集成了两类加密算法的优点，既实现了加密速度快的优点，又实现了安全方便管理密钥的优点。
			byte[] d1 = crypt(publicKey, srcContent.getBytes(),
					Cipher.ENCRYPT_MODE);
			System.out.println(new String(d1));
			byte[] d2 = crypt(privateKey, d1, Cipher.DECRYPT_MODE);
			System.out.println(new String(d2));

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 将KEY转换为字符串
	 * 
	 * @param key
	 * @return
	 * @author SHANHY
	 */
	private static String getKeyString(Key key) {
		byte[] keyBytes = key.getEncoded();
		String s = new String(
				org.apache.commons.codec.binary.Base64.encodeBase64(keyBytes));
		return s;
	}

	/**
	 * 加密/解密
	 * 
	 * @param key
	 *            私钥打包成byte[]形式
	 * @param data
	 *            要解密的数据
	 * @param opmode
	 *            操作类型（Cipher.DECRYPT_MODE为解密，Cipher.ENCRYPT_MODE为加密）
	 * @return 解密数据
	 */
	public static byte[] crypt(Key key, byte[] data, int opmode) {
		try {
			long startTime = System.currentTimeMillis();
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");// jdk默认标准
			// Cipher cipher = Cipher.getInstance("RSA/ECB/NoPadding");//
			// android默认标准
			cipher.init(opmode, key);

			byte[] result = cipher.doFinal(data);

			System.out.println((Cipher.DECRYPT_MODE == opmode ? "解密" : "加密")
					+ "耗时：" + (System.currentTimeMillis() - startTime));
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}
}
