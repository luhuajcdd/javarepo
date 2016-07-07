package dehua.java.androidmultipackage;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Tool {

	private static final String CHANNEL_PREFIX = "/META-INF/";
	private static final String CHANNEL_PATH_MATCHER = "regex:/META-INF/mtchannel_[0-9a-zA-Z]{1,5}";
	private static String source_path;
	private static final String channel_file_name = "channel_list.txt";
	private static final String channel_flag = "channel_";

	public static void main(String[] args) throws Exception {
		if (args.length <= 0) {
			System.out.println("�������ļ�·����Ϊ����");
			return;
		}

		final String source_apk_path = args[0];//main���������Դapk��·������ִ��jarʱ�����д���ģ����������¿���
		int last_index = source_apk_path.lastIndexOf("/") + 1;
		source_path = source_apk_path.substring(0, last_index);
		final String source_apk_name = source_apk_path.substring(last_index, source_apk_path.length());

		System.out.println("��·����" + source_path);
		System.out.println("�ļ�����" + source_apk_name);

		ArrayList<String> channel_list = getChannelList(source_path + channel_file_name);
		final String last_name = ".apk";
		for (int i = 0; i < channel_list.size(); i++) {
			String channel = channel_list.get(i).trim();
			final String new_apk_path = source_path + source_apk_name.substring(0, source_apk_name.length() - last_name.length()) //
					+ "_" + channel + last_name;
			copyFile(source_apk_path, new_apk_path);
			changeChannel(new_apk_path, channel_flag + channel);
		}
	}

	/**
	 * �޸������ţ�ԭ������apk��META-INF���½�һ���ļ���Ϊ�����ŵ��ļ�
	 */
	public static boolean changeChannel(final String zipFilename, final String channel) {
		try (FileSystem zipfs = createZipFileSystem(zipFilename, false)) {

			final Path root = zipfs.getPath("/META-INF/");
			ChannelFileVisitor visitor = new ChannelFileVisitor();
			Files.walkFileTree(root, visitor);

			Path existChannel = visitor.getChannelFile();
			Path newChannel = zipfs.getPath(CHANNEL_PREFIX + channel);
			if (existChannel != null) {
				Files.move(existChannel, newChannel, StandardCopyOption.ATOMIC_MOVE);
			} else {
				Files.createFile(newChannel);
			}

			return true;

		} catch (IOException e) {
			System.out.println("���������ʧ�ܣ�" + channel);
			e.printStackTrace();
		}

		return false;
	}

	private static FileSystem createZipFileSystem(String zipFilename, boolean create) throws IOException {
		final Path path = Paths.get(zipFilename);
		final URI uri = URI.create("jar:file:" + path.toUri().getPath());

		final Map<String, String> env = new HashMap<>();
		if (create) {
			env.put("create", "true");
		}
		return FileSystems.newFileSystem(uri, env);
	}

	private static class ChannelFileVisitor extends SimpleFileVisitor<Path> {
		private Path channelFile;
		private PathMatcher matcher = FileSystems.getDefault().getPathMatcher(CHANNEL_PATH_MATCHER);

		public Path getChannelFile() {
			return channelFile;
		}

		@Override
		public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
			if (matcher.matches(file)) {
				channelFile = file;
				return FileVisitResult.TERMINATE;
			} else {
				return FileVisitResult.CONTINUE;
			}
		}
	}

	/** �õ������б� */
	private static ArrayList<String> getChannelList(String filePath) {
		ArrayList<String> channel_list = new ArrayList<String>();

		try {
			String encoding = "UTF-8";
			File file = new File(filePath);
			if (file.isFile() && file.exists()) { // �ж��ļ��Ƿ����
				InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);// ���ǵ������ʽ
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				while ((lineTxt = bufferedReader.readLine()) != null) {
					// System.out.println(lineTxt);
					if (lineTxt != null && lineTxt.length() > 0) {
						channel_list.add(lineTxt);
					}
				}
				read.close();
			} else {
				System.out.println("�Ҳ���ָ�����ļ� file = " + file.getAbsolutePath());
			}
		} catch (Exception e) {
			System.out.println("��ȡ�ļ����ݳ���");
			e.printStackTrace();
		}

		return channel_list;
	}

	/** �����ļ� */
	private static void copyFile(final String source_file_path, final String target_file_path) throws IOException {

		File sourceFile = new File(source_file_path);
		File targetFile = new File(target_file_path);

		BufferedInputStream inBuff = null;
		BufferedOutputStream outBuff = null;
		try {
			// �½��ļ����������������л���
			inBuff = new BufferedInputStream(new FileInputStream(sourceFile));

			// �½��ļ���������������л���
			outBuff = new BufferedOutputStream(new FileOutputStream(targetFile));

			// ��������
			byte[] b = new byte[1024 * 5];
			int len;
			while ((len = inBuff.read(b)) != -1) {
				outBuff.write(b, 0, len);
			}
			// ˢ�´˻���������
			outBuff.flush();
		} catch (Exception e) {
			System.out.println("�����ļ�ʧ�ܣ�" + target_file_path);
			e.printStackTrace();
		} finally {
			// �ر���
			if (inBuff != null)
				inBuff.close();
			if (outBuff != null)
				outBuff.close();
		}
	}
}

/** ��ע�� android�ж�Ӧ�Ĵ���
 * ��ȡapk���ļ����е������ַ���
 * @param context Context
 * @return String
 */
/*public static String getChannel(Context context) {
    if(context == null) return "";

    String channel = "";

    final String start_flag = "META-INF/channel_";
    ApplicationInfo appinfo = context.getApplicationInfo();
    if(appinfo == null) return "";

    String sourceDir = appinfo.sourceDir;
    if(TextUtils.isEmpty(sourceDir)) return "";
    ZipFile zipfile = null;
    try {
        zipfile = new ZipFile(sourceDir);
        Enumeration<?> entries = zipfile.entries();
        if(entries == null) return "";
        while (entries.hasMoreElements()) {
            ZipEntry entry = ((ZipEntry) entries.nextElement());
            if(entry == null) continue;
            String entryName = entry.getName();
            if (entryName != null && entryName.contains(start_flag)) {
                channel = entryName.replace(start_flag, "");
                break;
            }
        }
    } catch (IOException e) {
        LogCore.i(TAG,Log.getStackTraceString(e));
    } finally {
        if (zipfile != null) {
            try {
                zipfile.close();
            } catch (IOException e) {
                LogCore.i(TAG, Log.getStackTraceString(e));
            }
        }
    }

    if (channel == null || channel.length() <= 0) {
        channel = "guanwang";//�����������ž�Ĭ���ǹٷ�����
    }

    return channel;
}*/