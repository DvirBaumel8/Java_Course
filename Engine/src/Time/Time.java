package Time;

public class Time {
    private int hours;
    private int minutes;
    private int day;

    public Time(int minutes, int hours, int day) {
        this.hours = hours;
        this.minutes = minutes;
        this.day = day;
    }

    public int getHours() {
        return hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public int getDay() {
        return day;
    }

    public void moveTimeForward(int timeToAdd) {
        switch (timeToAdd) {
            //5 minutes
            case 1: {
                minutes += 5;
                handleCircle();
            }

            //30 minutes
            case 2: {
                minutes += 30;
                handleCircle();
            }

            //60 minutes
            case 3: {
                hours += 1;
                handleCircle();
            }

            //120 minutes
            case 4: {
                hours += 2;
                handleCircle();
            }

            //a day
            case 5: {
                day += 1;
            }
        }
    }

    private void handleCircle() {
        while(minutes >= 60) {
            minutes -= 60;
            hours++;
        }
        while(hours >= 24) {
            hours -= 24;
            day++;
        }
    }

    public void moveTimeBack(int timeToBack) throws Exception {
        switch (timeToBack) {
            //5 minutes
            case 1: {
                minutes -= 5;
                break;
            }

            //30 minutes
            case 2: {
                minutes -= 30;
                break;
            }

            //60 minutes
            case 3: {
                hours -= 1;
                break;
            }

            //120 minutes
            case 4: {
                hours -= 2;
                break;
            }

            //a day
            case 5: {
                day -= 1;
                break;
            }
        }
        handleMinus();
    }

    private void handleMinus() throws Exception {
        if(minutes < 0) {
            if(hours == 0) {
                if(day <= 1) {
                    throw new Exception("Move time back failed.");
                }
                else {
                    day--;
                    hours += 24;
                    hours--;
                    minutes += 60;
                }
            }
            else {
                hours--;
                minutes += 60;
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append(String.format("Day %d, ", day));
        if(hours < 10) {
            str.append(String.format("0%d:", hours));
        }
        else {
            str.append(String.format("%d:", hours));
        }
        if(minutes < 10) {
            str.append(String.format("0%d", minutes));
        }
        else {
            str.append(String.format("%d", minutes));
        }

        return str.toString();
    }

    public void addMinutes(int sumMinutes) {
        this.minutes += sumMinutes;
        handleCircle();
    }

    @Override
    public boolean equals(Object obj) {
        Time timeToCompare = (Time) obj;

        if(this.getDay() > timeToCompare.getDay()) {
            return true;
        }
        else if(this.getDay() < timeToCompare.getDay()) {
            return false;
        }
        else if(this.getHours() > timeToCompare.getHours()) {
            return true;
        }
        else if(this.getHours() < timeToCompare.getHours()) {
            return false;
        }
        else if(this.getMinutes() > timeToCompare.getMinutes()) {
            return true;
        }
        else {
            return false;
        }
    }
}
