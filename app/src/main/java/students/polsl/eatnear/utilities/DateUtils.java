package students.polsl.eatnear.utilities;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtils {
    private static String randBetweenForDate(int start, int end) {
        String value = String.valueOf(start + (int) Math.round(Math.random() * (end - start)));
        return value.length() == 1 ? ("0" + value) : value;
    }

    public static Date generateRandomDate() {
        try {
            return new SimpleDateFormat("YYYY-MM-dd", Locale.getDefault()).parse(String.format("%s-%s-%s",
                    randBetweenForDate(2000, 2018),
                    randBetweenForDate(1, 12),
                    randBetweenForDate(1, 28)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Date createTodaysDate() {
        Calendar calendar = Calendar.getInstance();
        try {
            return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(String.format("%s-%s-%s",String.valueOf(calendar.get(calendar.YEAR)),
            String.valueOf(calendar.get(calendar.MONTH) + 1),
            String.valueOf(calendar.get(calendar.DAY_OF_MONTH))));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String convertDateToString(Date date) {
        SimpleDateFormat dt1 = new SimpleDateFormat("YYYY-MM-dd");
        return dt1.format(date);
    }
}

