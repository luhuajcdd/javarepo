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
		
		//2. 读取记录中每一行
		ArrayList<String> needTest = new ArrayList<>();
		ArrayList<String> developerTest = new ArrayList<>();
		
		for(String str : fileContents){
			if(str == null || "".equals(str)) continue;
			boolean isNeedTest = false;
			for(String rule : filterRules){
				if(str.contains(rule)){
					needTest.add(str);
					isNeedTest = true;
					break;
				}
			}
			if(isNeedTest) continue;
			developerTest.add(str);
		}
		
		//3. 根据规则生成分类文件
		deleteRepeatContent(needTest);
		File fNeedTest = new File("F:\\tools\\getSVNLog\\needtest.txt");
		FileUtil.writeStringsInFile(needTest, fNeedTest, false);
		
		deleteRepeatContent(developerTest);
		File fDevTest = new File("F:\\tools\\getSVNLog\\devtest.txt");
		FileUtil.writeStringsInFile(developerTest, fDevTest, false);
		
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
