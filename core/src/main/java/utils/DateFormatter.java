package utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatter {
    static SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
    public static String formatDate(Date date){
        return df.format(date);
    }
    public static String formatDate(java.sql.Date date){
        return df.format(date);
    }

}
