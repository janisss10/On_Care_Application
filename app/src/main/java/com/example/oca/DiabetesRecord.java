package com.example.oca;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DiabetesRecord implements Comparable<DiabetesRecord> {
    private String date;
    private String time;
    private String diabetes;
    private String readings_for;
    private float reading;

    public DiabetesRecord(String date, String time, String diabetes, String readings_for, float reading) {
        this.date = date;
        this.time = time;
        this.diabetes=diabetes;
        this.readings_for=readings_for;
        this.reading=reading;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getDiabetes() {
        return diabetes;
    }

    public String getReadings_for() {
        return readings_for;
    }

    public float getReading() {
        return reading;
    }


    @Override
    public int compareTo(DiabetesRecord other) {
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
