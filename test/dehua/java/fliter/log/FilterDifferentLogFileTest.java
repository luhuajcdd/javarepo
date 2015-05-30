package dehua.java.fliter.log;

import java.io.IOException;
import java.text.ParseException;

import junit.framework.TestCase;

public class FilterDifferentLogFileTest extends TestCase{

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
	
	public void testFilter() {
		try {
			new FilterDifferentLogFile().filter();
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
	}
		
}
