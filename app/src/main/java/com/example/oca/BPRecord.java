package com.example.oca;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class BPRecord implements Comparable<BPRecord> {
    private String date;
    private String time;
    private float systolicBP;
    private float diastolicBP;

    public BPRecord(String date, String time, float systolicBP, float diastolicBP) {
        this.date = date;
        this.time = time;
        this.systolicBP = systolicBP;
        this.diastolicBP = diastolicBP;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public float getSystolicBP() {
        return systolicBP;
    }

    public float getDiastolicBP() {
        return diastolicBP;
    }

    @Override
    public int compareTo(BPRecord other) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.ENGLISH);
        String dateTime1 = this.date + " " + this.time;
        String dateTime2 = other.date + " " + other.time;
        try {
            Date date1 = format.parse(dateTime1);
            Date date2 = format.parse(dateTime2);
            return date1.compareTo(date2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
