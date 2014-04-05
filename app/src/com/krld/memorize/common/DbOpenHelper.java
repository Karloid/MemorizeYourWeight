package com.krld.memorize.common;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbOpenHelper extends SQLiteOpenHelper {
    public static final int DB_VERSION = 2;
    public static final String DB_NAME = "mysqllite";

    public static final String MEASUREMENT = "measurement";
    public static final String WEIGHT = "weight";
    public static final String DATE = "date";
    // public static final String LOGIN = "login";
    //public static final String PASSW = "passw";
    public static final String CREATE_TABLE = "create table " + MEASUREMENT + " ( _id integer primary key autoincrement, "
            + WEIGHT + " REAL, " + DATE + " INTEGER)";

    public DbOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

    }
}
