package advancedTDDKata.utils;

import org.joda.time.DateTimeZone;
import org.joda.time.format.ISODateTimeFormat;

import java.util.Date;


public final class DateConverter {

    private DateConverter() {
    }


    public static String formatDate(Date date, DateTimeZone timezone) {
        if (null == date) {
            return null;
        }
        return ISODateTimeFormat.dateTime().withZone(timezone).print(date.getTime());
    }

}
