package org.lostfan.ktv.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public final class DateFormatter {

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    private DateFormatter() {}

    public static String format(LocalDate date) {
        if (date == null) {
            return "";
        }
        return date.format(formatter);
    }

    public LocalDate parse(CharSequence text) {
        return LocalDate.parse(text, formatter);
    }
}
