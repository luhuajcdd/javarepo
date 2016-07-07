package dehua.java.util;

import java.io.IOException;

import dehua.java.util.system.Envrionment;
import junit.framework.TestCase;

public class DirUtilTest extends TestCase{
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		Envrionment.out.println("setUp()");
	}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		Envrionment.out.println("tearDown()");
	}
	
	@Override
	protected void runTest() throws Throwable {
		super.runTest();
		Envrionment.out.println("runTest()");
	}
	
	public void testGetCurrentProjectPath() throws IOException{
		Envrionment.out.println(new DirUtil().getCurrentProjectPath());
	}
}
