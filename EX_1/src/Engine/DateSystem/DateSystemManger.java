package Engine.DateSystem;

import java.util.Calendar;
import java.util.Date;

public class DateSystemManger {
    private static DateSystemManger dateSystemMangerSingle;
    Date startSystemDate = null;
    int startingDayNumber = -1;

    /** private constructor to prevent others from instantiating this class */
    private DateSystemManger() {
        this.startSystemDate = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(this.startSystemDate);
        startingDayNumber = cal.get(Calendar.DAY_OF_WEEK);
    }

    public static DateSystemManger getInstance()
    {
        if (dateSystemMangerSingle == null)
            dateSystemMangerSingle = new DateSystemManger();

        return dateSystemMangerSingle;
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
