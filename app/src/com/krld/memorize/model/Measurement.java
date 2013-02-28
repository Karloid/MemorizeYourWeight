package com.krld.memorize.model;

import android.util.Log;

import java.util.Date;

public class Measurement {
    private String weight;
    private Date date;
    private int id;

    public Measurement(int id, String string, int dateInt) {
        // stub
        setId(id);
        setWeight(string);
        Date date = new Date((long) dateInt * 1000L);
        Log.d("KRLD", dateInt + " " + date + " " + new Date(dateInt) + " , * 1000" + new Date(dateInt * 1000));
        setDate(date);
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
