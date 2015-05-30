package dehua.java.util.time;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class TimeUtil {

    public static boolean isTimeThisYear(long time){
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        c.set(year, 0, 1, 0, 0, 0);
        long start = c.getTimeInMillis();
        c.set(year + 1, 0, 1, 0, 0);
        long end = c.getTimeInMillis();
        return time > start && time < end;
    }


    public static String parseTime(long timeMillis, TimePattern pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern.pattern, Locale.getDefault());
        return sdf.format(new Date(timeMillis));
    }
    
    
    public static long parseTime(String time, TimePattern pattern) throws ParseException {
    	SimpleDateFormat format = new SimpleDateFormat(pattern.pattern, Locale.CHINESE);
    	return format.parse(time).getTime();
    }

  
    public static long gmtToLocal(long gmtMillis) {
        TimeZone timeZone = Calendar.getInstance().getTimeZone();
        return gmtMillis + timeZone.getRawOffset();
    }

    public static Date cvtToGmt(Date date) {
        TimeZone tz = TimeZone.getDefault();
        Date ret = new Date(date.getTime() - tz.getRawOffset());

        // if we are now in DST, back off by the delta. Note that we are
        // checking the GMT date, this is the KEY.
        if (tz.inDaylightTime(ret)) {
            Date dstDate = new Date(ret.getTime() - tz.getDSTSavings());

            // check to make sure we have not crossed back into
            // standard time
            // this happens when we are on the cusp of DST (7pm the
            // day before the change for PDT)
            if (tz.inDaylightTime(dstDate)) {
                ret = dstDate;
            }
        }
        return ret;
    }

    public static long cvtToGmt(long currentMillis) {
        return cvtToGmt(new Date(currentMillis)).getTime();
    }
    
    public static enum TimePattern{
    	
    	daysPattern("yyyy-MM-dd"),
    	secondOnlyPattern("mm:ss"),
        pattern0("yyyyMMddHHmmss"),
        pattern1("HH:mm"),
        pattern2("yyMMdd"),
        pattern5("MM-dd HH:mm"),
        pattern6("yyyy-MM-dd HH:mm:ss"),
        pattern7("yy-MM-dd HH:mm"),
        pattern8("yy-MM-dd"),
        secondPattern("HH:mm:ss"),
        millsSecondPattern("HH:mm:ss SSS");
        
        public String pattern;
        private TimePattern(String pattern){
        	this.pattern = pattern;
        }
        
        
    }

}
