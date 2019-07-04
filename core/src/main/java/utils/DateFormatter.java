package utils;

import java.sql.Date;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class DateFormatter {
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static String formatDate(LocalDate date) {
        return date.format(formatter);
    }

    public static LocalDate parseDate(String date){
        if (date != null && !date.equals("")) {
            return LocalDate.parse(date, formatter);
        }
        return null;
    }
    public static Date convertToDatabaseColumn(LocalDate localDate) {
        return Optional.ofNullable(localDate)
                .map(Date::valueOf)
                .orElse(null);
    }
}
