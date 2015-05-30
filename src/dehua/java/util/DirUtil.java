package dehua.java.util;

import java.io.File;

import dehua.java.util.property.PropertyUtilTest;

public class DirUtil {

	/**
	 * 获取工程的目录
	 * @return path of current project
	 */
	public String getCurrentProjectPath(){
		File jarPath=new File(PropertyUtilTest.class.getProtectionDomain().getCodeSource().getLocation().getPath());
		
		return jarPath.getParentFile().getParent();
	}
	
}
