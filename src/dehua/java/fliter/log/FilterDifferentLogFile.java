package dehua.java.fliter.log;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.List;

import dehua.java.util.DirUtil;
import dehua.java.util.Command.SystemCommand;
import dehua.java.util.file.FileCompressionAndDecompressionUtil;
import dehua.java.util.file.FileUtil;
import dehua.java.util.property.PropertyUtil;
import dehua.java.util.time.TimeUtil;
import dehua.java.util.time.TimeUtil.TimePattern;

public class FilterDifferentLogFile {

	final String Caused_by = "Caused by";
	final String com_sangfor_pocket = "com.sangfor.pocket" ;
	final String java_lang = "java.lang." ;
	
	
	public void filter() throws IOException, ParseException{
		/*
		 * 0. 创建存放目标文件目录 target_log
		 * 1. 读取预定义的已经存在的字符串
		 * 2. 读取目下的文件
		 * 3. 如果是压缩文件， 先解压
		 * 4. 读取解压之后的文件， 循环到子目录
		 * 5. 读取的文件是否包含已经定义的文件， N: 把文件copy到制定目录(target_log), 同时把特殊标识字符放入预定义的字符串中
		 * 6. 更新预定的字符文件
		 */
		
		LogConfigure logConfigure = getConfigure();
		
		//0. 创建存放目标文件目录 target_log
		String putTargetDir = logConfigure.putTargetDir;
		if(putTargetDir == null){
			System.out.println("putTargetDir is null");
			return;
		}
		
		File destinationDir = new File(putTargetDir);
		if(! destinationDir.exists()){
			boolean result = destinationDir.mkdirs();
			if(result){
				System.out.println("create target store file dir success");
			}else{
				System.out.println("create target store file dir failed");
				return ;
			}
		}
		
		//1. 读取预定义的已经存在的字符串
		List<String> predefinedStrs = getPredefinedStr();
		
		//2. 读取目下的文件
		String[] targetDirs = logConfigure.targetDirs;
		if(targetDirs == null){
			System.out.println("targetDirs is null");
			return ;
		}
		
		
		for(int i = 0 ; i < targetDirs.length; i ++){
			String targetDir = targetDirs[i];
			if(targetDir == null){
				continue;
			}
			File[] fs = new File(targetDir).listFiles();
			if(fs == null){
				continue;
			}
			long startTime = logConfigure.startTime[i];
			long endTime = logConfigure.endTime[i];
			for(File f : fs){
				String fileName = f.getName();
				if(fileName == null){
					continue;
				}
				if(fileName.contains(logConfigure.logPrefix) && fileName.contains(logConfigure.logSuffix)){
					String time = fileName.replace(logConfigure.logPrefix, "").replace(logConfigure.logSuffix, "");
					if(time == null){
						continue;
					}
					
					try {
						long logTime = TimeUtil.parseTime(time, TimePattern.pattern2);
						if(logTime < startTime || logTime > endTime){
							System.out.println("current time = " + time + "; not in defined time");
							return ;
						}
					} catch (ParseException e) {
						e.printStackTrace();
						return ;
					}
					
					//3. 如果是压缩文件， 先解压
					//4. 读取解压之后的文件， 循环到子目录
					boolean result = new FileCompressionAndDecompressionUtil().deCompressTGZFile(f.getAbsolutePath());
					if(! result){
						return ;
					}
					
					String parentPath = f.getParent();
					String subFileName = fileName.replace(logConfigure.logSuffix, "");
					File deCompressFile = new File(parentPath + File.separator + subFileName);
					boolean filterResult = filter(predefinedStrs, deCompressFile, destinationDir);
					if(filterResult){
						//删除解压后的文件
						//deCompressFile.delete();//不能删除文件夹
						//deCompressFile.deleteOnExit();//不能删除文件夹
						FileUtil.del(deCompressFile);
					}
				}
			}
		}
		
		// 6. 更新预定的字符文件
		StringBuilder strBuilder = new StringBuilder();
		for(String str : predefinedStrs){
			strBuilder.append(str).append((char)10);
		}
		updateFilterStr(strBuilder.toString());
		
		
	}

	private LogConfigure getConfigure() throws ParseException {
		LogConfigure logConfigure = new LogConfigure();
		PropertyUtil propertyUtil = new PropertyUtil(new DirUtil().getCurrentProjectPath() + 
				"/configure/read_file_dir_and_time.property");
		String targetDirStr = propertyUtil.read("target_dir");
		String putTargetDir = propertyUtil.read("put_target_dir");
		String startTime = propertyUtil.read("start_time");
		String endTime = propertyUtil.read("end_time");
		String logFilePrefix = propertyUtil.read("log_file_prefix");
		String logFileSuffix = propertyUtil.read("log_file_suffix");
		if(targetDirStr != null){
			String[] targetDirs = targetDirStr.split(",");
			logConfigure.targetDirs = targetDirs;
		}
		logConfigure.putTargetDir = putTargetDir;
		if(startTime != null){
			String[] startTimes = startTime.split(",");
			long[] sts = new long[startTimes.length];
			for(int i = 0 ; i < startTimes.length; i ++){
				sts[i] = TimeUtil.parseTime(startTimes[i], TimeUtil.TimePattern.pattern2);
			}
			logConfigure.startTime = sts;
		}
		if(endTime != null){
			String[] endTimes = endTime.split(",");
			long[] sts = new long[endTimes.length];
			for(int i = 0 ; i < endTimes.length; i ++){
				sts[i] = TimeUtil.parseTime(endTimes[i], TimeUtil.TimePattern.pattern2);
			}
			logConfigure.endTime = sts;
		}
		logConfigure.logPrefix = logFilePrefix;
		logConfigure.logSuffix = logFileSuffix;
		
		return logConfigure;
	}
	

	private List<String> getPredefinedStr() {
		return FileUtil.readFileEachLine(new File(new DirUtil().getCurrentProjectPath() + 
				"/configure/Predefined_crash_str.configure"));
	}
	
	private void updateFilterStr(String filterStr){
		FileUtil.saveStringToTargetFile(filterStr, new File(new DirUtil().getCurrentProjectPath() + 
				"/configure/Predefined_crash_str.configure"));
	}
	

	private boolean filter(List<String> predefinedStrs, File deCompressFile, File destinationDir) throws IOException {
		 
		if(deCompressFile.isDirectory()){
			//4. 读取解压之后的文件， 循环到子目录
			File[] files = FileUtil.getAllFilesInDir(deCompressFile);
			if(files == null){
				return true;
			}
			for(File f : files){
				if(f == null){
					continue;
				}
				if(! f.getName().contains("Android")){
					continue;
				}
				
				if(! f.getName().contains(".txt")){
					continue;
				}
				
				boolean result = FileUtil.isFileContainStr(f, predefinedStrs);
				if(! result){//不包含指定的字符
					// 5. 读取的文件是否包含已经定义的文件， N: 把文件copy到制定目录(target_log), 同时把特殊标识字符放入预定义的字符串中
					boolean copyResult = copyFileToDestinationDir(f, destinationDir);
					if(!copyResult){
						System.out.println("copy f = " + f.getAbsolutePath() + " to target dir failed" );
					}
					boolean putResult = putToPredefinedStr(f, predefinedStrs);
					if(!putResult){
						System.out.println("put f = " + f.getAbsolutePath() + " main key words in predefinedStrs failed");
					}
					
					if(copyResult && putResult){
						return true;
					}
				}
			}
		}
		
		return false;
		
	}

	private boolean copyFileToDestinationDir(File f, File destinationDir) {
		return SystemCommand.copy(f.getAbsolutePath(), destinationDir.getAbsolutePath());
	}

	/**
	 * 算法说明: 日志文件包含Cause By 取从cause by 开始的 ，包含com.sangfor.pocket字样的日志
	 *        不包含， 是否存在com.sangfor.pocket，如果存在； 不存在： 取第一行错误信息
	 * @param f
	 * @param predefinedStrs
	 * @return
	 * @throws IOException 
	 */
	private boolean putToPredefinedStr(File f, List<String> predefinedStrs) throws IOException {
		byte[] b = FileUtil.readFile(f);
		String fileData = new String(b);
		if(fileData.contains(Caused_by)){
			String subData = fileData.substring(fileData.indexOf(Caused_by));
			if(subData.contains(com_sangfor_pocket)){
				String subStr = subData.substring(subData.indexOf(com_sangfor_pocket));
				int pos = getStringFirstLineEndPosition(subStr);
				if(pos > 0){
					predefinedStrs.add(subStr.substring(0, pos));
				}
				return true;
			}else{
				String subStr = fileData.substring(fileData.indexOf(Caused_by));
				int pos = getStringFirstLineEndPosition(subStr);
				if(pos > 0){
					String subSubStr = subStr.substring(0, pos);
					if(subSubStr.contains(com_sangfor_pocket)){
						predefinedStrs.add(subStr.substring(0, pos));
					}
				}
				return true;
			}
		}else{
			if(fileData.contains(com_sangfor_pocket)){
				String subStr = fileData.substring(fileData.indexOf(com_sangfor_pocket));
				int pos = getStringFirstLineEndPosition(subStr);
				if(pos > 0){
					predefinedStrs.add(subStr.substring(0, pos));
				}
				return true;
			}else{
				String subStr = fileData.substring(fileData.indexOf(java_lang));
				int pos = getStringFirstLineEndPosition(subStr);
				if(pos > 0){
					predefinedStrs.add(subStr.substring(0, pos));
				}
				return true;
			}
		}
	}

	private int getStringFirstLineEndPosition(String subStr)
			throws IOException {
		InputStream is = new ByteArrayInputStream(subStr.getBytes());
		int pos = 0;
		int i ;
		while((i = is.read()) != -1){
			pos ++ ;
			if(i == 10){//换行
				return pos;
			}
		}
		return 0;
	}


	
}
