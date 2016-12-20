package dehua.java.filter.filesdiff;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import dehua.java.util.file.FileUtil;

public class FilesDiff {
	
	public static void main(String[] args) {
		FilesDiff instance = new FilesDiff();
		instance.inNewNotInOld();
		instance.inOldNotInNew();
	}

	//F:\tools\maindex
	public void inOldNotInNew(){
		FilesDiff instance = new FilesDiff();
		List<String> oldContents = instance.getFileLineContent("F:\\tools\\maindex\\oldversion.txt");
		List<String> newContents = instance.getFileLineContent("F:\\tools\\maindex\\newversion.txt");
		
		if(oldContents == null || newContents == null){
			return;
		}
		
		ArrayList<String> filterContents = new ArrayList<String>();
		for(String oldCon : oldContents){
			if(oldCon == null || "".equals(oldCon)){continue;}
			if(oldCon.contains("-keep")){continue;}
			if(! oldCon.contains("com/sangfor/pocket")){continue;}
			if(! newContents.contains(oldCon)){
				filterContents.add(oldCon);
			}
		}
		
		File fNeedTest = new File("F:\\tools\\maindex\\inOldNotInNew.txt");
		FileUtil.writeStringsInFile(filterContents, fNeedTest, false);
		
		System.out.println("end");
	}
	
	public void inNewNotInOld(){
		FilesDiff instance = new FilesDiff();
		List<String> oldContents = instance.getFileLineContent("F:\\tools\\maindex\\oldversion.txt");
		List<String> newContents = instance.getFileLineContent("F:\\tools\\maindex\\newversion.txt");
		
		if(oldContents == null || newContents == null){
			return;
		}
		
		ArrayList<String> filterContents = new ArrayList<String>();
		for(String newCon : newContents){
			if(newCon == null || "".equals(newCon)){continue;}
			if(newCon.contains("-keep")){continue;}
			if(! newCon.contains("com/sangfor/pocket")){continue;}
			if(! oldContents.contains(newCon)){
				filterContents.add(newCon);
			}
		}
		
		File fNeedTest = new File("F:\\tools\\maindex\\inNewNotInOld.txt");
		FileUtil.writeStringsInFile(filterContents, fNeedTest, false);
		
		System.out.println("end");
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
