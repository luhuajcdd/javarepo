package dehua.java.util.file;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {

	public static final String KB = "KB";
	public static final String MB = "MB";
	public static final String GB = "GB";
	public static final int UNIT = 1024;
	private static final int IO_BUFFER_SIZE = 8 * 1024;

	/**
	 * get File in the dir
	 *
	 * @param dir
	 * @return
	 */
	public static File[] getFilesInDir(File dir) {
		if (dir == null) {
			return null;
		}
		return dir.listFiles();
	}

	/**
	 * get all file in the dir
	 *
	 * @param dir
	 * @return
	 */
	public static File[] getAllFilesInDir(File dir) {
		List<File> fileList = new ArrayList<File>();
		if (dir == null) {
			return null;
		}
		File[] files = dir.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				File[] subDirFiles = getAllFilesInDir(file);
				for (File file2 : subDirFiles) {
					fileList.add(file2);
				}
			} else {
				fileList.add(file);
			}
		}

		File[] allFiles = new File[fileList.size()];
		return fileList.toArray(allFiles);
	}

	/**
	 * @param file
	 * @return
	 */
	public static boolean deleteDirectory(File file) {
		File[] files = file.listFiles();
		if (files == null) {
			return true;
		}
		for (int i = 0; i < files.length; i++) {
			if (files[i].isDirectory()) {
				deleteDirectory(files[i]);
			} else {
				files[i].delete();
			}
		}
		return (file.delete());
	}

	public static boolean saveFile(InputStream is, OutputStream os) {
		BufferedOutputStream out = null;
		BufferedInputStream in = null;

		try {
			in = new BufferedInputStream(is, IO_BUFFER_SIZE);
			out = new BufferedOutputStream(os, IO_BUFFER_SIZE);
			byte[] buffer = new byte[1024];
			int len;
			while ((len = in.read(buffer)) != -1) {
				out.write(buffer, 0, len);
			}
			out.flush();
			return true;
		} catch (final IOException e) {
		} finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
			}
		}
		return false;
	}

	public static boolean saveStringToTargetFile(String content, File file) {
		return saveByteToTargetFile(content.getBytes(), file);
	}

	/**
	 * 保存数据到文件中
	 * 
	 * @param bytes
	 * @param mFile
	 * @return
	 */
	public static boolean saveByteToTargetFile(byte[] bytes, File file) {
		FileOutputStream os = null;
		InputStream is = null;

		try {
			os = new FileOutputStream(file);
			is = new ByteArrayInputStream(bytes);
			boolean b = saveFile(is, os);
			return b;

		} catch (final IOException e) {
		} finally {
			try {
				if (os != null) {
					os.close();
				}
				if (is != null) {
					is.close();
				}
			} catch (final IOException e) {
			}
		}

		return false;
	}

	public static byte[] readFile(File file) {
		FileInputStream inputStream = null;
		BufferedInputStream bis = null;
		try {
			inputStream = new FileInputStream(file);
			bis = new BufferedInputStream(inputStream);
			byte[] buffer = new byte[(int) file.length()];
			bis.read(buffer);
			return buffer;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
				if (bis != null) {
					bis.close();
				}
			} catch (IOException e) {
			}
		}
		return null;
	}

	/**
	 * Read file each line
	 * 
	 * @param oldPath
	 * @param newPath
	 */
	public static List<String> readFileEachLine(File file) {
		FileReader reader = null;
		BufferedReader br = null;
		try {
			reader = new FileReader(file);
			br = new BufferedReader(reader);
			List<String> contents = new ArrayList<String>();
			String tempString = null;
			while ((tempString = br.readLine()) != null) {
				contents.add(tempString);
			}
			return contents;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null) {
					br.close();
				}
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
			}
		}
		return null;
	}

	public static void copyFile(String oldPath, String newPath) {
		InputStream inStream = null;
		FileOutputStream fs = null;
		try {
			int byteread = 0;
			File oldfile = new File(oldPath);
			if (oldfile.exists()) { // 鏂囦欢瀛樺湪鏃�
				inStream = new FileInputStream(oldPath); // 璇诲叆鍘熸枃浠�
				fs = new FileOutputStream(newPath);
				byte[] buffer = new byte[1024];
				while ((byteread = inStream.read(buffer)) != -1) {
					fs.write(buffer, 0, byteread);
				}
				inStream.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (inStream != null) {
				try {
					inStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			if (fs != null) {
				try {
					fs.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static boolean isFileContainStr(File file, String[] strs) {
		if (file == null || strs == null) {
			return false;
		}

		byte[] b = readFile(file);

		String fileData = new String(b);
		for (String str : strs) {
			if (str == null) {
				continue;
			}
			if (fileData.contains(str)) {
				return true;
			}
		}
		return false;
	}

	public static boolean isFileContainStr(File file, List<String> strs) {
		if (file == null || strs == null) {
			return false;
		}

		byte[] b = readFile(file);

		String fileData = new String(b);
		//System.out.println("file data = " + fileData);
		for (String str : strs) {
			if (str == null) {
				continue;
			}
			if (fileData.contains(str)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 删除文件目录
	 * @param filepath
	 * @throws IOException
	 */
	public static void del(String filepath) throws IOException {
		File f = new File(filepath);// 定义文件路径
		del(f);
	}

	public static void del(File f) throws IOException {
		if (f.exists() && f.isDirectory()) {// 判断是文件还是目录
			if (f.listFiles().length == 0) {// 若目录下没有文件则直接删除
				f.delete();
			} else {// 若有则把文件放进数组，并判断是否有下级目录
				File delFile[] = f.listFiles();
				int i = f.listFiles().length;
				for (int j = 0; j < i; j++) {
					if (delFile[j].isDirectory()) {
						del(delFile[j].getAbsolutePath());// 递归调用del方法并取得子目录路径
					}
					delFile[j].delete();// 删除文件
				}
				f.delete();
			}
		}
	}
}
