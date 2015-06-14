package com.krld.memorize.model;

import android.util.Log;
import com.krld.memorize.common.DataType;

import java.util.Date;

public class MeasurementLegacy {
    private String weight;
    private Date date;
    private int id;
    private DataType datatype;

    public MeasurementLegacy(int id, String string, int dateInt, String datatype) {

        // stub
        if (datatype != null) {
            setDatatype(DataType.valueOf(datatype));
        }
        setId(id);
        setWeight(string);
        Date date = new Date((long) dateInt * 1000L);
       // Log.d("KRLD", dateInt + " " + date + " " + new Date(dateInt) + " , * 1000" + new Date(dateInt * 1000) + " type: " + datatype);
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

    public void setDatatype(DataType datatype) {
        this.datatype = datatype;
    }

    public DataType getDatatype() {
        return datatype;
    }
}
