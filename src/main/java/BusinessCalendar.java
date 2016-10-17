/**
 * Created by Developer on 16.10.2016.
 */
import java.text.ParseException;
import java.util.*;
import org.joda.time.*;
import org.joda.time.DateTime;
import org.joda.time.format.*;
import org.joda.time.chrono.GregorianChronology;

public class BusinessCalendar {

    DateTimeZone currentTimeZone;
    Chronology _calendar;
    DateTimeFormatter formatter;
    ArrayList<LocalDate> celebrates = new ArrayList();
    ArrayList<WorkingHours> mondayWorkingHours;
    ArrayList<WorkingHours> tuesdayWorkingHours;
    ArrayList<WorkingHours> weddayWorkingHours;
    ArrayList<WorkingHours> thursdayWorkingHours;
    ArrayList<WorkingHours> fridayWorkingHours;
    ArrayList<WorkingHours> satdayWorkingHours;
    ArrayList<WorkingHours> sundayWorkingHours;


    public BusinessCalendar(Properties properties) throws Exception {
        loadBusinessCalendarFromPropertyFile(properties);
    }

    public int getWorkingHoursInDay(DateTime _date, boolean isBeginOrEndDay) {
        isBeginOrEndDay = false;
        int dayWorkHours = 0;
        ArrayList<WorkingHours> workingHourses = getWorkingHour(_date.getDayOfWeek());

        if (workingHourses == null)
            return 0;

        for (WorkingHours hours : workingHourses) {
            if(hours.isDateInCurrentWorkingPeriod(_date)) {
                if(!isBeginOrEndDay)
                    dayWorkHours += DateHour.substract(hours.getTo(), hours.getFrom()).toInt(true);
                else {

                }
            }
        }
        return dayWorkHours;
    }

    private void loadHolidays(Properties prop) throws ParseException {
        Enumeration<?> e = prop.propertyNames();
        while (e.hasMoreElements()) {
            String key = (String) e.nextElement();
            if (key.startsWith("celebrate")) {
                LocalDate date = formatter.parseLocalDate(prop.getProperty(key));
                celebrates.add(date);
            }
        }
    }

    private ArrayList<WorkingHours> loadWorkingHour(String timeExpression) throws ParseException {
        if (timeExpression.isEmpty()) {
            return null;
        }
        String[] schedules = timeExpression.split(" \\& ");
        ArrayList<WorkingHours> workingHourses = new ArrayList<>();

        for (String schedule : schedules) {
            String[] time = schedule.split("-");
            DateHour fromHours = DateHour.parseDateHour(time[0]);
            DateHour toHours = DateHour.parseDateHour(time[1]);
            workingHourses.add(new WorkingHours(fromHours, toHours));
        }
        return workingHourses;
    }

    private void loadWorkingHours(Properties prop) throws ParseException {
        Enumeration<?> e = prop.propertyNames();
        while (e.hasMoreElements()) {
            String key = (String) e.nextElement();
            if (key.startsWith("weekday")) {
                ArrayList<WorkingHours> list = loadWorkingHour(prop.getProperty(key));
                switch (key) {

                    case "weekday.monday":
                        mondayWorkingHours = list;
                        break;
                    case "weekday.tuesday":
                        tuesdayWorkingHours = list;
                        break;
                    case "weekday.wednesday":
                        weddayWorkingHours = list;
                        break;
                    case "weekday.thursday":
                        thursdayWorkingHours = list;
                        break;
                    case "weekday.friday":
                        fridayWorkingHours = list;
                        break;
                    case "weekday.saturday":
                        satdayWorkingHours = list;
                        break;
                    case "weekday.sunday":
                        sundayWorkingHours = list;
                        break;
                }
            }
        }
    }

    private void loadBusinessCalendarFromPropertyFile(Properties prop) throws Exception {
        String timeZone = prop.getProperty("calendar.timezone");
        if (timeZone == null || timeZone.isEmpty())
            currentTimeZone = DateTimeZone.forID(TimeZone.getDefault().getID());
        else
            currentTimeZone = DateTimeZone.forID(timeZone);

         _calendar = GregorianChronology.getInstance(currentTimeZone);

        String dateFormat = prop.getProperty("day.format");
        if (dateFormat == null || dateFormat.isEmpty()) {
            throw new Exception("day.format property is not defined");
        }
        formatter = DateTimeFormat.forPattern(dateFormat);

        loadHolidays(prop);

        loadWorkingHours(prop);
    }

    private ArrayList<WorkingHours> getWorkingHour(int day_of_week) {
        switch (day_of_week) {

            case 1:
                return mondayWorkingHours;
            case 2:
                return tuesdayWorkingHours;
            case 3:
                return weddayWorkingHours;
            case 4:
                return thursdayWorkingHours;
            case 5:
                return fridayWorkingHours;
            case 6:
                return satdayWorkingHours;
            case 7:
                return sundayWorkingHours;
            default:
                return null;
        }
    }

    public boolean isCelebrate(LocalDate date) {
        for (LocalDate _date : celebrates) {
            if (_date.getMonthOfYear() == date.getMonthOfYear() && _date.getDayOfMonth() == date.getDayOfMonth()) {
                return true;
            }
        }
        return false;
    }
}
