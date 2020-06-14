package Time;


public class TimeManager {
    private Time currTime;

    private static TimeManager instance = null;

    public static TimeManager getInstance() {
        if(instance == null) {
            instance = new TimeManager();
        }
        return instance;

    }

    private TimeManager() {
        currTime = new Time(00, 00, 1);
    }

    public void moveTimeForward(int choose) {
        currTime.moveTimeForward(choose);
    }

    public void moveTimeBack(int choose) throws Exception {
        currTime.moveTimeBack(choose);
    }

    public Time getCurrTime() {
        return currTime;
    }

    public void setCurrentTime(Time time) {
        currTime = time;
    }

}
