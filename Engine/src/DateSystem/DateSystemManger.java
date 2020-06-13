package DateSystem;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateSystemManger {
    Date startSystemDate = null;
    int startingDayNumber = -1;
    String dateByString = null;

    /** private constructor to prevent others from instantiating this class */
    private DateSystemManger() {
        this.startSystemDate = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(this.startSystemDate);
        startingDayNumber = cal.get(Calendar.DAY_OF_WEEK);
    }

    /** thread safe */
    public static DateSystemManger getDateSystemMangerInstance()
    {
        return SingletonHelper.instance;
    }

    private static class SingletonHelper {
        private static DateSystemManger instance = new DateSystemManger();
    }

    public Date getStartSystemDate() {
        return startSystemDate;
    }

    public void setStartSystemDate(Date startSystemDate) {
        this.startSystemDate = startSystemDate;
    }

    public int getStartingDayNumber() {
        return startingDayNumber;
    }

    public void setStartingDayNumber(int startingDayNumber) {
        this.startingDayNumber = startingDayNumber;
    }

    public String getTimeFormatInsanceForUI() {
        String pattern = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String timeString = simpleDateFormat.format(startSystemDate);
        dateByString = timeString;
        return timeString;
    }

    public String getDateByString() {
        return dateByString;
    }

    public String setDateByString(String dateByString, boolean isForward) {
        String res = null;
        if(isForward) {

        }
        else {

        }
        // this.dateByString = res;
        return res;
    }

    public String setDateString5Min(boolean isForward) {
        String res = null;
        if(isForward) {

        }
        else {

        }
        // this.dateByString = res;
        return res;
    }

    public String setDateString30Min(boolean isForward) {
        String res = null;
        if(isForward) {

        }
        else {

        }
        // this.dateByString = res;
        return res;
    }

    public String setDateString1Hour(boolean isForward) {
        String res = null;
        if(isForward) {

        }
        else {

        }
        // this.dateByString = res;
        return res;
    }

    public String setDateString2Hours(boolean isForward) {
        String res = null;
        if(isForward) {

        }
        else {

        }
        // this.dateByString = res;
        return res;
    }

    public String setDateString1Day(boolean isForward) {
        String res = null;
        if(isForward) {

        }
        else {

        }
        // this.dateByString = res;
        return res;
    }
}
