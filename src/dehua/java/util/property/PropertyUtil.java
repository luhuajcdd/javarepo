package dehua.java.util.property;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class PropertyUtil {

	Properties prop = null;
	
	public PropertyUtil(String path){
		try {
			prop = new Properties();
			prop.load(new FileInputStream(path));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			prop = null;
		} catch (IOException e) {
			e.printStackTrace();
			prop = null;
		}
	}
	
	public String read(String key){
		
		if(prop != null){
			return prop.getProperty(key);
		}
	    return null;
	}
	
}
