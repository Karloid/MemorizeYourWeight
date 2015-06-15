package com.krld.memorize.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.Expose;

import java.util.Calendar;

@Table(name = "Measurement")
public class Measurement extends Model {

    @Expose
    @Column()
    public double value;

    @Expose
    @Column()
    public String datatype;

    @Expose
    @Column()
    public Calendar insertDate;
}
