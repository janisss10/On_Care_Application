package com.example.oca;

public class Appointment {
    private String clinicName;
    private String contact;
    private String date;
    private String detail;
    private String email;
    private String id;
    private String time;
    private String datePosted;

    public Appointment() {}

    public Appointment(String clinicName, String contact, String date, String detail, String email, String id, String time, String datePosted) {
        this.clinicName = clinicName;
        this.contact = contact;
        this.date = date;
        this.detail = detail;
        this.email = email;
        this.id = id;
        this.time = time;
        this.datePosted = datePosted;
    }

    public String getClinicName() {
        return clinicName;
    }

    public void setClinicName(String clinicName) {
        this.clinicName = clinicName;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDatePosted() {
        return datePosted;
    }

    public void setDatePosted(String datePosted) {
        this.datePosted = datePosted;
    }
}
