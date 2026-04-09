package com.example.baitapnhom.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public final class DateTimeUtils {
    private static final String ISO_PATTERN = "yyyy-MM-dd";
    private static final String DISPLAY_DATE_PATTERN = "dd/MM/yyyy";
    private static final String DISPLAY_DATE_TIME_PATTERN = "dd/MM/yyyy HH:mm";

    private DateTimeUtils() {
    }

    public static String todayIsoDate() {
        return new SimpleDateFormat(ISO_PATTERN, Locale.getDefault()).format(new Date());
    }

    public static boolean isExpired(String expiryDate) {
        if (expiryDate == null || expiryDate.trim().isEmpty()) {
            return true;
        }
        return expiryDate.compareTo(todayIsoDate()) <= 0;
    }

    public static String formatIsoDate(String isoDate) {
        if (isoDate == null || isoDate.trim().isEmpty()) {
            return "--/--/----";
        }

        try {
            Date date = new SimpleDateFormat(ISO_PATTERN, Locale.getDefault()).parse(isoDate);
            if (date == null) {
                return isoDate;
            }
            return new SimpleDateFormat(DISPLAY_DATE_PATTERN, Locale.getDefault()).format(date);
        } catch (ParseException ignored) {
            return isoDate;
        }
    }

    public static String formatTimestamp(long timestamp) {
        return new SimpleDateFormat(DISPLAY_DATE_TIME_PATTERN, Locale.getDefault()).format(new Date(timestamp));
    }
}
