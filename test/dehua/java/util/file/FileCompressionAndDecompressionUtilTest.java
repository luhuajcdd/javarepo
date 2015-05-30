package dehua.java.util.file;

import java.io.IOException;

import junit.framework.TestCase;

public class FileCompressionAndDecompressionUtilTest extends TestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		System.out.println("setUp()");
	}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		System.out.println("tearDown()");
	}
	
	@Override
	protected void runTest() throws Throwable {
		super.runTest();
		System.out.println("runTest()");
	}
	
	public void testDeCompressTGZFile() throws IOException{
		setName(FileCompressionAndDecompressionUtil.class.getName());
		new FileCompressionAndDecompressionUtil().deCompressTGZFile("D:\\a\\client_20150515.tar.gz");
	}
}  
