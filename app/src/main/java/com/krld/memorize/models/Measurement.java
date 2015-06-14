package com.krld.memorize.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.Calendar;

@Table(name = "Measurement")
public class Measurement extends Model {

    @Column()
    public double value;
    @Column()
    public String datatype;
    @Column()
    public Calendar insertDate;
}
