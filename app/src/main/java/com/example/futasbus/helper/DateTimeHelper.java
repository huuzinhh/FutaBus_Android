package com.example.futasbus.helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeHelper {
    private static String Pattern = "dd/MM/yyyy";

    public static String toDate(String st){
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = inputFormat.parse(st);
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return st; // Trả về chuỗi gốc nếu parse lỗi
        }
    }

    public static String toString(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(date);
    }

    public static String toHour(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
        return sdf.format(date);
    }

    public static String toHour(String st) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = inputFormat.parse(st);
            SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm");
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return st; // Trả về chuỗi gốc nếu parse lỗi
        }
    }

    public static String toFullDateTime(String input) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = inputFormat.parse(input);
            SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm - dd/MM/yyyy ");
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return input;
        }
    }
}