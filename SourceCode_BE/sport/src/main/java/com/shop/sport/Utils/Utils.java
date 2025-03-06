package com.shop.sport.Utils;

import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Random;

public class Utils {
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");

    public static Date convertStringToDate(String dateString) {
        try {
            java.util.Date utilDate = DATE_FORMAT.parse(dateString);
            long timeInMillis = utilDate.getTime();
            return new Date(timeInMillis);
        } catch (ParseException e) {
            e.printStackTrace();
            // Handle the exception gracefully, return null or throw a custom exception
            return null;
        }
    }
    public static String generateRandomString(int length) {
        String characters = "0123456789abcdefghijklmnopqrstuvwxyz";
        StringBuilder sb = new StringBuilder();

        Random random = new Random();
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(characters.length());
            char randomChar = characters.charAt(randomIndex);
            sb.append(randomChar);
        }

        return sb.toString();
    }
}
