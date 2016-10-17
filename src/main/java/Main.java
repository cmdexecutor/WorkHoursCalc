/**
 * Created by Developer on 16.10.2016.
 */
import java.util.*;
import java.io.*;
import org.joda.time.*;
import org.joda.time.DateTime;
import org.joda.time.format.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Main {

    private static String datePattern = "yyyy-MM-dd'T'HH:mm:ss"; //TODO from settings.properties
    private static String CONFIG_PATH = "src/main/resources/settings.properties";
    private static FileInputStream configFile;
    private static BusinessCalendar calendar;
    private static int totalPeriodWorkingHours;
    private static LinkedHashMap<LocalDate, Integer> workingDays = new LinkedHashMap<>();
    private static LinkedHashMap<LocalDate, Integer> weekendDays = new LinkedHashMap<>();
    private static LinkedHashMap<LocalDate, Integer> celebrateDays = new LinkedHashMap<>();
    private static JSONObject allDays = new JSONObject();

    public static void main(String args[]) throws Exception {

        /*
        "2016-01-11T15:21:00,2016-03-31T12:30:00";
        "2016-04-07T06:59:00,2016-05-30T07:00:00";
        "2016-08-20T10:00:00,2016-09-01T18:30:00";
        */

        Properties calendarConfig = new Properties();
        try {
            configFile = new FileInputStream(CONFIG_PATH);
            calendarConfig.load(configFile);

        } catch (IOException e) {
            System.err.println("File settings.properties not found");
        }

        calendar = new BusinessCalendar(calendarConfig);
        DateTimeFormatter dtf = DateTimeFormat.forPattern(datePattern);

        //TODO parse args with keys
        DateTime startDate = dtf.parseDateTime("2016-01-11T15:21:00");
        DateTime endDate = dtf.parseDateTime("2016-03-31T12:30:00");


        for (DateTime _dayDate = startDate; !_dayDate.isAfter(endDate.plusDays(1)); _dayDate = _dayDate.plusDays(1)) {
            //First day in period
            if (_dayDate == startDate) {
                calculateHours(startDate, true);

                //TODO JSON output
                continue;
            }
            //Last day in period
            else if (_dayDate.isAfter(endDate)) {
                calculateHours(endDate, true);

                //TODO JSON output
                continue;
            }
            //Common midday in period
            calculateHours(_dayDate, false);

            //TODO JSON output
        }

        System.out.println("Total work hours: " + totalPeriodWorkingHours);
        //makeJSON();

    }


    private static void calculateHours(DateTime day, boolean isFirstOrLastDayInPeriod) {
        if (!calendar.isCelebrate(day.toLocalDate())) {
            int dayWorkHours = calendar.getWorkingHoursInDay(day, isFirstOrLastDayInPeriod);
            if (dayWorkHours == 0) {
                weekendDays.put(day.toLocalDate(), dayWorkHours);
                return;
            }
            workingDays.put(day.toLocalDate(), dayWorkHours);
            totalPeriodWorkingHours += dayWorkHours;
        } else {
            celebrateDays.put(day.toLocalDate(), 0); //TODO remove that 0
        }
    }

/*    private void makeJSON() {

    }*/
}
