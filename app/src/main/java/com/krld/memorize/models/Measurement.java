package com.krld.memorize.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "Measurement")
public class Measurement extends Model {

    @Column()
    public String value;
}
