package com.example.baitapnhom.utils;

import java.text.NumberFormat;
import java.util.Locale;

public class CurrencyFormatter {
    private static final NumberFormat FORMAT = NumberFormat.getInstance(new Locale("vi", "VN"));

    public static String format(double amount) {
        return FORMAT.format((long) amount) + "đ";
    }

    public static String format(long amount) {
        return FORMAT.format(amount) + "đ";
    }
}