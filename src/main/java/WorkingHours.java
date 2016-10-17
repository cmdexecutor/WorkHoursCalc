/**
 * Created by Developer on 16.10.2016.
 */
import org.joda.time.DateTime;

public class WorkingHours {

    public WorkingHours(DateHour from, DateHour to) {
        this.from = from;
        this.to = to;
    }
    public DateHour getFrom() {
        return  from;
    }

    public DateHour getTo() {
        return to;
    }

    private DateHour from;
    private DateHour to;

    public boolean isDateInCurrentWorkingPeriod(DateTime date) {
        DateHour checkDateHour = new DateHour(date.getHourOfDay(), date.getMinuteOfHour(), date.getSecondOfMinute());
        if(checkDateHour.isHourAfterThan(from) && !checkDateHour.isHourAfterThan(to))
            return true;
        else
            return  false;
    }




}
