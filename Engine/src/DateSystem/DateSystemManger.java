package DateSystem;

import java.util.Calendar;
import java.util.Date;

public class DateSystemManger {
    Date startSystemDate = null;
    int startingDayNumber = -1;

    /** private constructor to prevent others from instantiating this class */
    private DateSystemManger() {
        this.startSystemDate = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(this.startSystemDate);
        startingDayNumber = cal.get(Calendar.DAY_OF_WEEK);
    }

    /** thread safe */
    public static DateSystemManger getInstance()
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
}
