package com.krld.memorize;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import com.krld.memorize.common.DbOpenHelper;
import com.krld.memorize.common.ListAdapter;
import com.krld.memorize.model.Measurement;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class StartActivity extends Activity {
    EditText weightText = null;
    Button saveButton = null;
    Button readButton = null;
    public static DbOpenHelper dbHelper = null;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        dbHelper = new DbOpenHelper(StartActivity.this, DbOpenHelper.DB_NAME, null, DbOpenHelper.DB_VERSION);
        weightText = (EditText) findViewById(R.id.login);
        saveButton = (Button) findViewById(R.id.saveButton);
        readButton = (Button) findViewById(R.id.readButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues cv = new ContentValues();
                try {
                    cv.put(DbOpenHelper.WEIGHT, Double.parseDouble(weightText.getText().toString().replaceAll(",", ".")));
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(StartActivity.this, "Not a number", Toast.LENGTH_SHORT).show();
                    weightText.setText("");
                    return;
                }
                cv.put(DbOpenHelper.DATE, (int) (Calendar.getInstance().getTimeInMillis() / 1000));
                Log.d("KRLD", (int) (Calendar.getInstance().getTimeInMillis() / 1000) + "");
                db.insert(DbOpenHelper.MEASUREMENT, null, cv);
                db.close();
                weightText.setText("");
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

    public void refreshListViewMeasurement() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select _id, " + DbOpenHelper.WEIGHT + ", " + DbOpenHelper.DATE +
                " from " + DbOpenHelper.MEASUREMENT + " order by " + DbOpenHelper.DATE + " desc", null);
        List<Measurement> measurementList = new ArrayList<Measurement>();
        while (cursor.moveToNext()) {
            measurementList.add(new Measurement(cursor.getInt(0), cursor.getString(1), cursor.getInt(2)));
        }
        cursor.close();

        ListView listView = (ListView) findViewById(R.id.listView);
        ListAdapter listAdapter = new ListAdapter(StartActivity.this, R.layout.tablerow, measurementList);
        listView.setAdapter(listAdapter);
    }

    private void finishInput() {
        // TODO hide keyboard
    }
}
