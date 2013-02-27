package com.krld.memorize;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import com.krld.memorize.common.DbOpenHelper;
import com.krld.memorize.common.ListAdapter;
import com.krld.memorize.model.Measurement;

import java.util.ArrayList;
import java.util.List;

public class StartActivity extends Activity {
    EditText loginEditText = null;
    EditText passEditText = null;
    Button saveButton = null;
    Button readButton = null;
    DbOpenHelper dbHelper = null;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        dbHelper = new DbOpenHelper(StartActivity.this, DbOpenHelper.DB_NAME, null, DbOpenHelper.DB_VERSION);
        loginEditText = (EditText) findViewById(R.id.login);
        passEditText = (EditText) findViewById(R.id.passw);
        saveButton = (Button) findViewById(R.id.saveButton);
        readButton = (Button) findViewById(R.id.readButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues cv = new ContentValues();
                cv.put(DbOpenHelper.LOGIN, loginEditText.getText().toString());
                cv.put(DbOpenHelper.PASSW, passEditText.getText().toString());
                db.insert(DbOpenHelper.TABLE_NAME, null, cv);
                db.close();
                loginEditText.setText("");
                passEditText.setText("");
                Toast.makeText(StartActivity.this, "Inserting!", Toast.LENGTH_SHORT).show();
                refreshListViewMeasurement();
            }
        });
        readButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshListViewMeasurement();
            }
        });

        refreshListViewMeasurement();

    }

    private void refreshListViewMeasurement() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select " + DbOpenHelper.LOGIN + ", " + DbOpenHelper.PASSW +" from " + DbOpenHelper.TABLE_NAME, null);
        List<Measurement> measurementList = new ArrayList<Measurement>();
        while (cursor.moveToNext()) {
            measurementList.add(new Measurement(cursor.getString(0) + " / " + cursor.getString(1)));
        }
        cursor.close();

        ListView listView = (ListView) findViewById(R.id.listView);
        ListAdapter listAdapter = new ListAdapter(StartActivity.this, R.layout.tablerow, measurementList );
        listView.setAdapter(listAdapter);
    }
}
