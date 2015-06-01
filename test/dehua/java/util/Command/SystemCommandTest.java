package dehua.java.util.Command;

import junit.framework.TestCase;

public class SystemCommandTest extends TestCase{

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
	
	
	public void testCopy(){
		SystemCommand.copy("D:\\aa\\a.txt", "D:\\bb");
	}
}
