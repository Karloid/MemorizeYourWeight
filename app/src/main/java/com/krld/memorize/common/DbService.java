package com.krld.memorize.common;

import android.database.sqlite.SQLiteDatabase;
import com.krld.memorize.EditorActivity;

public class DbService {
    public static void removeMeasurement(int id) {
        DbOpenHelper dbHelper = EditorActivity.dbHelper;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("delete from " + DbOpenHelper.MEASUREMENT + " where _id = " + id);
        db.close();
    }
}