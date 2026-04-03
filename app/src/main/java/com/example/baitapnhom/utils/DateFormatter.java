package com.example.baitapnhom.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateFormatter {
    public static String format(long millis) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", new Locale("vi", "VN"));
        return sdf.format(new Date(millis));
    }

    public static String formatDate(long millis) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", new Locale("vi", "VN"));
        return sdf.format(new Date(millis));
    }
}