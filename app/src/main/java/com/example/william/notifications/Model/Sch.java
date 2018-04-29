package com.example.william.notifications.Model;

public class Sch {

    private String start_date;
    private String day;

    public Sch(String start_date, String day) {
        this.start_date = start_date;
        this.day = day;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }
}

