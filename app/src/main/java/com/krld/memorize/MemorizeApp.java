package com.krld.memorize;

import android.app.Application;

import com.orm.Database;
import com.orm.SugarApp;

public class MemorizeApp extends Application {
    private Database database;
    private static MemorizeApp sugarContext;

    public MemorizeApp() {
    }

    public void onCreate() {
        super.onCreate();
        sugarContext = this;
        this.database = new Database(this);
    }

    public void onTerminate() {
        if(this.database != null) {
            this.database.getDB().close();
        }

        super.onTerminate();
    }

    public static MemorizeApp getSugarContext() {
        return sugarContext;
    }

    protected Database getDatabase() {
        return this.database;
    }
}
