package dehua.java.filter.commitrecord;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import dehua.java.util.file.FileUtil;

/**
 * 1. 读取提交记录文件
 * 2. 读取记录中每一行
 * 3. 根据规则生成分类文件
 * @author ZOZT
 *
 */
public class FilterCommitRecord {

	private String[] filterRules1 = new String[]{"功能","需要测试"};
	private static ArrayList<String> filterInDeveloperTests = new ArrayList<String>();
	static{
		filterInDeveloperTests.add("不需要测试");
	}
	
	
	public void filter(String filepath, String[] filterRules){
		File file = new File(filepath);
		if(! file.exists()){
			System.out.println("文件不存在  path =" + filepath);
			return;
		}
		//1. 读取提交记录文件
		List<String> fileContents = FileUtil.readFileEachLine(file);
		if(fileContents == null){
			System.out.println("empty file");
			return;
		}
		
		List<String> filterStrs = getFilterString();
		List<String> filterSaveSubmitLogs = getFilterString2();
		
		//2. 读取记录中每一行
		ArrayList<String> needTest = new ArrayList<>();
		ArrayList<String> developerTest = new ArrayList<>();
		
		for(String str : fileContents){
			if(str == null || "".equals(str)) continue;
						
			//检查过滤字符
			boolean willContinue = true;
			if(filterStrs != null && filterStrs.size() > 0){
				for(String filterStr : filterStrs){
					if(filterStr == null || "".equals(filterStr))continue;
					if(str.contains(filterStr)){
						willContinue = false;
					}
				}
			}
			if(!willContinue) continue;
			
			//保留包含制定字符的记录
			boolean willContinue2 = false;
			if(filterSaveSubmitLogs != null && filterSaveSubmitLogs.size() > 0){
				for(String filter : filterSaveSubmitLogs){
					if(filter == null || "".equals(filter))continue;
					if(str.contains(filter)){
						willContinue2 = true;
						break;
					}
				}
				if(!willContinue2) continue;
			}
			boolean isNotExcuteNeedTest = false;
			for(String inDeveloperRule : filterInDeveloperTests){
				if(str.contains(inDeveloperRule)){
					isNotExcuteNeedTest = true;
					break;
				}
			}
			if(! isNotExcuteNeedTest){
				boolean isNeedTest = false;
				for(String rule : filterRules){
					if(str.contains(rule)){
						if(isNeedTestHasStr(needTest,str)){
							continue;
						}
						needTest.add(str);
						isNeedTest = true;
						break;
					}
				}
				if(isNeedTest) continue;
			}
			developerTest.add(str);
		}
		
		//3. 根据规则生成分类文件
		deleteRepeatContent(needTest);
		File fNeedTest = new File("F:\\tools\\getSVNLog\\needtest.txt");
		FileUtil.writeStringsInFile(needTest, fNeedTest, false);
		
		deleteRepeatContent(developerTest);
		File fDevTest = new File("F:\\tools\\getSVNLog\\devtest.txt");
		FileUtil.writeStringsInFile(needTest, fDevTest, false);
		FileUtil.writeStringsInFile(developerTest, fDevTest, true);
		
	}
	
	private boolean isNeedTestHasStr(ArrayList<String> needTests, String str) {
		
		int index; 
		index = str.indexOf("#");
		if(index > -1){
			index += 1;
		}else{
			index = str.indexOf("功能");
			if(index == -1){
				return false;
			}
			index += 2;
		}
		String subStr = str.substring(index, str.length()).trim();
		index = getIndex(subStr);
		if(index == -1){
			return false;
		}
		String key = subStr.substring(0, index);
		if(key.contains("】")){
			key = key.substring(0, key.indexOf("】"));
		}
		
		System.out.println("再次去重的 key = " + key);
		
		for(String needTestStr : needTests){
			if(needTestStr.contains(key)){
				return true;
			}
		}
		
		return false;
	
	}

	public int getIndex(String subStr) {
		int index;
		index = subStr.indexOf(" ");
		if(index == -1){
			index = subStr.indexOf("】");
			if(index == -1){
				index = subStr.indexOf("}");
				if(index != -1){
					index += 1;
				}
			}else{
				index += 1;
			}
		}else{
			index += 1;
		}
		return index;
	}

	/**
	 * 需要删除的日志记录
	 * @return
	 */
	private List<String> getFilterString() {
		List<String> filterString = new ArrayList<String>();
		filterString.add("5.0");
		/*filterString.add("4.2");
		filterString.add("4.1");
		filterString.add("4.0");*/
		return filterString;
		//return null;
	}

	/**
	 * 需要保留的提交记录(正式版提交记录) 需要保留的日志
	 * @return
	 */
	private List<String> getFilterString2(){
		List<String> filterString = new ArrayList<String>();
		filterString.add("5.0");	
		//return filterString;
		return null;
	}
	
	public void deleteRepeatContent(List<String> fileContents ){
		if(fileContents == null){
			System.out.println("empty file");
			return;
		}
		
		HashSet<String> deleteContent = new HashSet<>();
		
		int size = fileContents.size();
		for(int i = 0 ; i < size ; i ++){
			String content1 = fileContents.get(i);
			for(int j = i+1 ; j < size ; j ++){
				String content2 = fileContents.get(j);
				if(content1.trim().equals(content2.trim())){
					deleteContent.add(content2);
				}
			}
		}
		
		fileContents.removeAll(deleteContent);
		fileContents.addAll(deleteContent);
		
	}
	
	public static void main(String[] args) {
		FilterCommitRecord fcr = new FilterCommitRecord();
		fcr.filter("F:\\tools\\getSVNLog\\luhua.txt", fcr.filterRules1);
		System.out.println("end");
	}
}
