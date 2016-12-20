package dehua.java.filter.androidmainlist;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import dehua.java.util.file.FileUtil;

public class AndroidMainList {

	private static final String POCKET = "com/sangfor/pocket";
	private static final String FILE_MAIN_LIST = "E:\\android_package\\maindexlist.txt";
	private static final String FILE_CONTAIN_POCKET = "E:\\android_package\\contains.txt";
	private static final String FILE_UNCONTAIN_POCKET = "E:\\android_package\\others.txt";
	
	private static final String[] filters = new String[]{
		"com/sangfor/pocket/acl",
		"com/sangfor/pocket/cloud",
		"com/sangfor/pocket/customer",
		"com/sangfor/pocket/expenses",
		"com/sangfor/pocket/legwork",
		"com/sangfor/pocket/moment",
		"com/sangfor/pocket/notepad",
		"com/sangfor/pocket/notify",
		"com/sangfor/pocket/reply",
		"com/sangfor/pocket/salesopp",
		"com/sangfor/pocket/schedule",
		"com/sangfor/pocket/store",
		"com/sangfor/pocket/task",
		"com/sangfor/pocket/workflow",
		"com/sangfor/pocket/workreport",
		"com/sangfor/pockettest",
		"com/sangfor/pocket/search",
		"com/sangfor/pocket/workattendance",
		
		"com/sangfor/pocket/protobuf/PB_Oprt",
		"com/sangfor/pocket/protobuf/PB_Noti",
		"com/sangfor/pocket/protobuf/PB_Note",
		"com/sangfor/pocket/protobuf/PB_Task",
		"com/sangfor/pocket/protobuf/PB_Vote",
		"com/sangfor/pocket/protobuf/PB_Custm",
		"com/sangfor/pocket/protobuf/PB_Work"
	};
	
	public static void main(String[] args) {
		AndroidMainList mainList = new AndroidMainList();
		mainList.getPocket(FILE_MAIN_LIST);
		mainList.getOther(FILE_MAIN_LIST);
	}
	
	public void getPocket(String mainlist){
		List<String> contents = getFileLineContent(mainlist);
		
		ArrayList<String> containInfo = new ArrayList<String>();
		for(String content : contents){
			if(content == null || "".equals(content.trim())) continue;
			if(content.contains(POCKET) && ! isBeFiltered(content)){
				containInfo.add(content);
			}
		}
		Collections.sort(containInfo);
		File containInfoFile = new File(FILE_CONTAIN_POCKET);
		FileUtil.writeStringsInFile(containInfo, containInfoFile, false);
	}
	
	private boolean isBeFiltered(String content){
		for(String fStr : filters){
			if(content.startsWith(fStr)){
				return true;
			}
		}
		return false;
	}
	
	public void getOther(String mainlist){
		List<String> contents = getFileLineContent(mainlist);
		
		ArrayList<String> otherInfo = new ArrayList<String>();
		for(String content : contents){
			if(content == null || "".equals(content.trim())) continue;
			if(! content.contains(POCKET)){
				otherInfo.add(content);
			}
		}
		Collections.sort(otherInfo);
		File unContainFile = new File(FILE_UNCONTAIN_POCKET);
		FileUtil.writeStringsInFile(otherInfo, unContainFile, false);
	}
	
	/**
	 * 读取文件内容
	 * @param path 文件路径
	 * @return List<String> 文件每一行内容
	 */
	public List<String> getFileLineContent(String path){
		List<String> contents = FileUtil.readFileEachLine(new File(path));
		return contents;
	}

}
