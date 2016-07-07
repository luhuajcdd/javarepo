package dehua.java.util.time;

import dehua.java.util.time.TimeUtil.TimePattern;

public class TimeUtilTest {

	public static void main(String[] args) {
		long t = 1464570185150L;
		System.out.println(TimeUtil.parseTime(t, TimePattern.millsSecondPattern));;
	}
	
}
