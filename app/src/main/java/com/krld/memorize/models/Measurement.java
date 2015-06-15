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

    public boolean simpleEquals(Measurement obj) {
        return Double.compare(obj.value,value) == 0
                && datatype.equals(obj.datatype)
                && Math.abs(insertDate.getTime().getTime() - obj.insertDate.getTime().getTime()) < 2000;//magic
    }
}
