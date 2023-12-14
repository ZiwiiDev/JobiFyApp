package com.example.jobifyapp.utils;
// -----------------------------------------------------------------------------------------------------------------------------
import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
// -----------------------------------------------------------------------------------------------------------------------------
public class Utils {
    // -----------------------------------------------------------------------------------------------------------------------------
    public static Date parseDateFromString(String dateString) {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        try {
            return format.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    // -----------------------------------------------------------------------------------------------------------------------------
}
// -----------------------------------------------------------------------------------------------------------------------------
