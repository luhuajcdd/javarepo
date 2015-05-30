package dehua.java.util.Command;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class SystemCommand {

	//window copy D:\a\a.txt D:\b
	public static boolean copy(String source, String destination){
		return excute(String.format("copy %s %s", source, destination));
	}
	
	public static boolean excute(String cmd){
		Runtime run = Runtime.getRuntime();//返回与当前 Java 应用程序相关的运行时对象  
      
        Process p = null;
		try {
			p = run.exec(cmd);// 启动另一个进程来执行命令  
		} catch (IOException e) {
			e.printStackTrace();
		}
        BufferedInputStream in = null ;
        BufferedReader inBr = null;
        
        if(p == null){
        	return false;
        }
        
        try {  
            in =  new BufferedInputStream(p.getInputStream());  
            inBr = new BufferedReader(new InputStreamReader(in));  
            String lineStr;  
            while ((lineStr = inBr.readLine()) != null){
                //获得命令执行后在控制台的输出信息  
                System.out.println(lineStr);// 打印输出信息  
            }
            //检查命令是否执行失败。  
            if (p.waitFor() != 0) {  
                if (p.exitValue() == 1)//p.exitValue()==0表示正常结束，1：非正常结束  
                    System.err.println("命令执行失败!");  
            } else{
            	return true;
            } 
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally{
        	try {  
        		inBr.close();  
        	}catch(IOException e){
        		
        	}
        	try {
		        in.close();
	        }catch(IOException e){
	    		
	    	}
        }
        
        return false;
	}
	
}
