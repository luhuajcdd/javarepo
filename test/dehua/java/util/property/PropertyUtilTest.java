package dehua.java.util.property;

import java.io.IOException;

import dehua.java.util.DirUtil;
import junit.framework.TestCase;

public class PropertyUtilTest extends TestCase {

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
	
	public void testRead() throws IOException{
		System.out.println(new PropertyUtil(new DirUtil().getCurrentProjectPath() + "/configure/read_file_dir_and_time.property").read("target_dir"));
		assertEquals("a", new PropertyUtil(new DirUtil().getCurrentProjectPath() + "/configure/read_file_dir_and_time.property").read("target_dir"));
	}
	
}
