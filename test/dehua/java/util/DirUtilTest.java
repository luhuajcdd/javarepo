package dehua.java.util;

import java.io.IOException;

import junit.framework.TestCase;

public class DirUtilTest extends TestCase{
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
	
	public void testGetCurrentProjectPath() throws IOException{
		System.out.println(new DirUtil().getCurrentProjectPath());
	}
}
