package com.example.appsportshop.utils;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Convert {

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

    public static byte[] convertBitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }





}