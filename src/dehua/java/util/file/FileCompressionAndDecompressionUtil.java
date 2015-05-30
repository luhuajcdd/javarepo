package dehua.java.util.file;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;

public class FileCompressionAndDecompressionUtil {

	final int buffersize = 2048;  
	final String TMP_TAR = "tmp.tar";
	final String TAR = ".tar";
	
	/**
	 * 压tgz或者tar.gz文件分成两步：首先先解压成tar文件，然后解压tar文件
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public boolean deCompressTGZFile(String file) throws IOException {  
		File tarFile = deCompressGZFile(new File(file));
		if(tarFile == null){
			return false;
		}
		boolean result = deCompressTARFile(tarFile);
		if(result){
			System.out.println(String.format("deCompress file = %s success",file));
			return true;
		}
		return false;
    }  
  
    private File deCompressGZFile(File file) throws IOException {  
        FileOutputStream out = null;  
        GzipCompressorInputStream gzIn = null;  
        try {  
        	
            FileInputStream fin = new FileInputStream(file);  
            BufferedInputStream in = new BufferedInputStream(fin);
            String fileName = file.getName();
            String filePrefix = getFilePrefix(fileName);
            File outFile = new File(file.getParent() + File.separator  
                    + (filePrefix == null ? TMP_TAR : filePrefix+TAR));  
            out = new FileOutputStream(outFile);  
            gzIn = new GzipCompressorInputStream(in);  
            final byte[] buffer = new byte[buffersize];  
            int n = 0;  
            while (-1 != (n = gzIn.read(buffer))) {  
                out.write(buffer, 0, n);  
            }  
            return outFile;  
        } finally {  
            try {  
                out.close();  
                gzIn.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
    }  
  
    private String getFilePrefix(String fileName) {
    	int index = fileName.indexOf(".");
    	if(index != -1){
    		return fileName.substring(0, index);
    	}
		return null;
	}

    /**
     * 解压.tar文件
     * @param file
     * @return true: 解压成功; false: 解压失败
     * @throws IOException
     */
	public boolean deCompressTARFile(File file) throws IOException {  
		if(file == null){
			return false;
		}
		String fileName = file.getName();
		String filePrefix = getFilePrefix(fileName);
        String basePath = file.getParent() + File.separator + (filePrefix != null ? filePrefix : null) + File.separator;  
        TarArchiveInputStream is = null;  
        try {  
            is = new TarArchiveInputStream(new FileInputStream(file));  
            while (true) {  
                TarArchiveEntry entry = is.getNextTarEntry();  
                if (entry == null) {  
                    break;  
                }  
                if (entry.isDirectory()) {// 这里貌似不会运行到，跟ZipEntry有点不一样  
                    new File(basePath + entry.getName()).mkdirs();  
                } else {  
                    FileOutputStream os = null;  
                    try {  
                        File f = new File(basePath + entry.getName());  
                        if (!f.getParentFile().exists()) {  
                            f.getParentFile().mkdirs();  
                        }  
                        if (!f.exists()) {  
                            f.createNewFile();  
                        }  
                        os = new FileOutputStream(f);  
                        byte[] bs = new byte[buffersize];  
                        int len = -1;  
                        while ((len = is.read(bs)) != -1) {  
                            os.write(bs, 0, len);  
                        }  
                        os.flush();
                        return true;
                    }finally {  
                        os.close();  
                    }  
                }  
            }  
        }finally {  
            try {  
                is.close();  
                file.delete();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
        return false;
    }  
}  
