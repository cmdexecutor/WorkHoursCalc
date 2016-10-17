/**
 * Created by Developer on 16.10.2016.
 */
import java.text.ParseException;
import org.joda.time.DateTime;

public class DateHour {
    public DateHour(int hour, int minute, int seconds) {
        if (hour >= 0 && hour <= 23)
            this.hour = hour;
        else this.hour = 0;

        if (minute >= 0 && minute <= 59)
            this.minute = minute;
        else this.minute = 0;

        if (seconds >= 0 && seconds <= 59)
            this.seconds = seconds;
        else this.seconds = 0;
    }

    private int hour;
    private int minute;
    private int seconds;

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public int getSeconds() {
        return seconds;
    }


    public boolean isHourAfterThan(DateHour checkHour) {
        if(hour > checkHour.getHour())
            return true;
        else if(hour == checkHour.getHour()) {
            if(minute > checkHour.getMinute())
                return true;
            else if(minute == checkHour.getMinute()) {
                if(seconds > checkHour.getSeconds())
                    return  true;
            }
            return  false;
        }
        return  false;
    }

    public int toInt(boolean doRound) { //TODO make doRound work
        if(doRound) {
            if (seconds < 30) {
                if (minute < 30) {
                    return hour;
                } else return hour + 1;
            } else return hour + 1;
        }
        else return hour;

    }

    public static DateHour parseDateHour(String s) throws ParseException {
        String[] format = s.split(":");
        int _seconds = 0;
        if(format.length == 3) {
            _seconds = Integer.parseInt(format[2]);
        }

        return new DateHour(Integer.parseInt(format[0]), Integer.parseInt(format[1]), _seconds);
    }

    public static DateHour parseFromDateTime(DateTime dt) {
        return  new DateHour(dt.getHourOfDay(), dt.getMinuteOfHour(), dt.getSecondOfMinute());
    }

    public static DateHour substract(DateHour from, DateHour what) { //TODO check for correct, most result is right but calculation is bad
        return new DateHour(from.getHour() - what.getHour(),
                            from.getMinute() - what.getMinute(),
                            from.getSeconds() - what.getSeconds());
    }


}