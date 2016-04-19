package dehua.java.util.system;

public class Envrionment {
	public static void main(String[] args) {
		/*Map<String,String> map = System.getenv();
		Set<Map.Entry<String,String>> entries = map.entrySet();
		for (Map.Entry<String, String> entry : entries) {
            System.out.println(entry.getKey() + ":" + entry.getValue());
        }*/
		System.out.println(System.getenv("android_keystore"));
	}
}
