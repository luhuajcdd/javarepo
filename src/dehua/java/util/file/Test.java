package dehua.java.util.file;
public class Test {  
  
    /** 
     * @param args 
     */  
    public static void main(String args[]) throws Exception {  
          test1();
          System.out.println("------------------------------------------------------");
          test2();
          String formatType = "image/*";
          System.out.println(formatType.substring(0,formatType.indexOf('/')));
    }  
    
    static void test1() throws Exception {
    	System.out.println(FileTypeJudge.getType("E:\\test\\a.txt"));  
        System.out.println(FileTypeJudge.getType("E:\\test\\a.zip"));  
        System.out.println(FileTypeJudge.getType("E:\\test\\a.png"));  
        System.out.println(FileTypeJudge.getType("E:\\test\\a.js"));  
        System.out.println(FileTypeJudge.getType("E:\\test\\a.doc"));  
        System.out.println(FileTypeJudge.getType("E:\\test\\a.xls"));  
        System.out.println(FileTypeJudge.getType("E:\\test\\a.wmv"));  
        System.out.println(FileTypeJudge.getType("E:\\test\\a.pdf"));  
        System.out.println(FileTypeJudge.getType("E:\\test\\a.gif"));  
        System.out.println(FileTypeJudge.getType("E:\\test\\a.JPG"));  
        System.out.println(FileTypeJudge.getType("E:\\test\\b.png"));
        System.out.println(FileTypeJudge.getType("E:\\test\\a.mp3"));
        System.out.println(FileTypeJudge.getType("E:\\test\\b.mp3"));
        System.out.println(FileTypeJudge.getType("E:\\test\\a.mp4"));
        System.out.println(FileTypeJudge.getType("E:\\test\\b.mp4"));
        System.out.println(FileTypeJudge.getType("E:\\test\\c.mp4"));
    }
  
    
    static void test2() throws Exception {
    	System.out.println(FileTypeJudge2.getType("E:\\test\\a.txt"));  
    	System.out.println(FileTypeJudge2.getType("E:\\test\\a.zip"));  
    	System.out.println(FileTypeJudge2.getType("E:\\test\\a.png"));  
    	System.out.println(FileTypeJudge2.getType("E:\\test\\a.js"));  
    	System.out.println(FileTypeJudge2.getType("E:\\test\\a.doc"));  
    	System.out.println(FileTypeJudge2.getType("E:\\test\\a.xls"));  
    	System.out.println(FileTypeJudge2.getType("E:\\test\\a.wmv"));  
    	System.out.println(FileTypeJudge2.getType("E:\\test\\a.pdf"));  
    	System.out.println(FileTypeJudge2.getType("E:\\test\\a.gif"));  
    	System.out.println(FileTypeJudge2.getType("E:\\test\\a.JPG"));  
    	System.out.println(FileTypeJudge2.getType("E:\\test\\b.png"));
    	System.out.println(FileTypeJudge2.getType("E:\\test\\a.mp3"));
    	System.out.println(FileTypeJudge2.getType("E:\\test\\b.mp3"));
    	System.out.println(FileTypeJudge2.getType("E:\\test\\a.mp4"));
    	System.out.println(FileTypeJudge2.getType("E:\\test\\b.mp4"));
    	System.out.println(FileTypeJudge2.getType("E:\\test\\c.mp4"));
    }
    
}  