package dehua.java.util.file;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map.Entry;

import dehua.java.util.Hex;
  
/** 
 * �ļ������ж��� 
 */  
public final class FileTypeJudge2 {  
      
    /** 
     * Constructor 
     */  
    private FileTypeJudge2() {}  
      
    /** 
     * ���ļ�ͷת����16�����ַ��� 
     *  
     * @param ԭ��byte 
     * @return 16�����ַ��� 
     */  
    private static String bytesToHexString(byte[] src){  
          
        StringBuilder stringBuilder = new StringBuilder();     
        if (src == null || src.length <= 0) {     
            return null;     
        }     
        for (int i = 0; i < src.length; i++) {     
            int v = src[i] & 0xFF;     
            String hv = Integer.toHexString(v);     
            if (hv.length() < 2) {     
                stringBuilder.append(0);     
            }     
            stringBuilder.append(hv);     
        }     
        return stringBuilder.toString();     
    }  
     
    /** 
     * �õ��ļ�ͷ 
     *  
     * @param filePath �ļ�·�� 
     * @return �ļ�ͷ 
     * @throws IOException 
     */  
    private static String getFileContent(String filePath) throws IOException {  
          
        byte[] b = new byte[28];  
          
        InputStream inputStream = null;  
          
        try {  
            inputStream = new FileInputStream(filePath);  
            inputStream.read(b, 0, 28);  
        } catch (IOException e) {  
            e.printStackTrace();  
            throw e;  
        } finally {  
            if (inputStream != null) {  
                try {  
                    inputStream.close();  
                } catch (IOException e) {  
                    e.printStackTrace();  
                    throw e;  
                }  
            }  
        }  
        //return bytesToHexString(b);
        return Hex.encodeHexStr(b, false);
    }  
      
    /** 
     * �ж��ļ����� 
     *  
     * @param filePath �ļ�·�� 
     * @return �ļ����� 
     */  
    public static String getType(String filePath) throws IOException {  
          
        String fileHead = getFileContent(filePath);  
        
        System.out.println(filePath + "; fileHead = " + fileHead);
          
        if (fileHead == null || fileHead.length() == 0) {  
            return null;  
        }  
          
        fileHead = fileHead.toUpperCase();  
        
        FileType fileType = new FileType();
        HashMap<String, String> fileTypeCache = fileType.fileTypeCache;
        for(Entry<String,String> entry : fileTypeCache.entrySet()){
        	String value = entry.getValue();
        	if (fileHead.startsWith(value)) {  
                return entry.getKey();  
            }  
        }
        
        return null;  
    }  
    
    public static class FileType {  
        public HashMap<String, String> fileTypeCache = new HashMap<>(); 
        
        public FileType(){
        	fileTypeCache.put("JPEG", "FFD8FF");
        	fileTypeCache.put("PNG", "89504E47");
        	fileTypeCache.put("GIF", "47494638");
        	fileTypeCache.put("BMP", "424D");
        	
        	fileTypeCache.put("TIFF", "49492A00");
        	fileTypeCache.put("DWG", "41433130");
        	fileTypeCache.put("PSD", "38425053");
        	fileTypeCache.put("RTF", "7B5C727466");
        	fileTypeCache.put("XML", "3C3F786D6C");
        	fileTypeCache.put("HTML", "68746D6C3E");
        	fileTypeCache.put("EML", "44656C69766572792D646174653A");
        	fileTypeCache.put("DBX", "CFAD12FEC5FD746F");
        	fileTypeCache.put("PST", "2142444E");
        	fileTypeCache.put("XLS_DOC", "D0CF11E0");
        	fileTypeCache.put("MDB", "5374616E64617264204A");
        	fileTypeCache.put("WPD", "FF575043");
        	fileTypeCache.put("EPS", "252150532D41646F6265");
        	fileTypeCache.put("PDF", "255044462D312E");
        	fileTypeCache.put("QDF", "AC9EBD8F");
        	fileTypeCache.put("PWL", "E3828596");
        	fileTypeCache.put("ZIP", "504B0304");
        	fileTypeCache.put("RAR", "52617221");
        	
        	fileTypeCache.put("RAM", "2E7261FD");
        	fileTypeCache.put("RM", "2E524D46");
        	fileTypeCache.put("MPG", "000001BA");
        	fileTypeCache.put("ASF", "3026B2758E66CF11");
        	fileTypeCache.put("MID", "4D546864");
        	
        	fileTypeCache.put("MOV", "6D6F6F76");
        	fileTypeCache.put("AVI", "41564920");
        	fileTypeCache.put("MP4", "0000001866");
        	
        	fileTypeCache.put("WAV", "57415645");
        	fileTypeCache.put("MP3", "49443303");
        }  
    }  
}  