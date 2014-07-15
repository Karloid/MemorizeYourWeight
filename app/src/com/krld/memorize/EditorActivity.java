package com.krld.memorize;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.*;
import com.krld.memorize.common.DataType;
import com.krld.memorize.common.DbOpenHelper;
import com.krld.memorize.common.ListAdapter;
import com.krld.memorize.model.Measurement;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class EditorActivity extends Activity {
    EditText inputText = null;
    Button saveButton = null;
    Button readButton = null;
    public static DbOpenHelper dbHelper = null;
    private DataType datatype;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editor);
        datatype = DataType.valueOf(getIntent().getStringExtra(MenuActivity.DATATYPE));
        initTitle();
        dbHelper = new DbOpenHelper(EditorActivity.this, DbOpenHelper.DB_NAME, null, DbOpenHelper.DB_VERSION);
        initViews();

        refreshListViewMeasurement();
    }

    private void initTitle() {
        if (datatype.equals(DataType.WEIGHT)) {
            setTitle("Вес");
        } else if (datatype.equals(DataType.WAIST)) {
            setTitle("Объём талии");
        } else if (datatype.equals(DataType.HIPS)) {
            setTitle("Объём бедер");
        }
    }

    private void initViews() {
        inputText = (EditText) findViewById(R.id.login);
        inputText.setImeActionLabel("Save", KeyEvent.KEYCODE_ENTER);
        inputText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_NULL && keyEvent.getAction() == KeyEvent.ACTION_DOWN){
                   saveValue();
                }
                return false;
            }
        });
        saveButton = (Button) findViewById(R.id.saveButton);
        readButton = (Button) findViewById(R.id.readButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveValue();
            }
        });
        readButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshListViewMeasurement();
            }
        });
    }

    private void saveValue() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        try {
            cv.put(DbOpenHelper.VALUE, Double.parseDouble(inputText.getText().toString().replaceAll(",", ".")));
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Not a number", Toast.LENGTH_SHORT).show();
            inputText.setText("");
            return;
        }
        cv.put(DbOpenHelper.DATE, (int) (Calendar.getInstance().getTimeInMillis() / 1000));
        cv.put(DbOpenHelper.TYPE, datatype.toString());
        Log.d("KRLD", (int) (Calendar.getInstance().getTimeInMillis() / 1000) + "");
        db.insert(DbOpenHelper.MEASUREMENT, null, cv);
        db.close();
        inputText.setText("");
        Toast.makeText(this, "Inserting!", Toast.LENGTH_SHORT).show();
        refreshListViewMeasurement();
    }

    public void refreshListViewMeasurement() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select _id, " + DbOpenHelper.VALUE + ", " + DbOpenHelper.DATE + ", " + DbOpenHelper.TYPE +
                " from " + DbOpenHelper.MEASUREMENT +
                " where " + DbOpenHelper.TYPE + " = '" + datatype + "' " +
                " order by " + DbOpenHelper.DATE + " desc", null);
        List<Measurement> measurementList = new ArrayList<Measurement>();
        while (cursor.moveToNext()) {
            //   Log.d("KRLD", "id: " + cursor.getInt(0) + " value:  " + cursor.getString(1) + " date: " + cursor.getInt(2) + " type " + cursor.getString(3));
            measurementList.add(new Measurement(cursor.getInt(0), cursor.getString(1), cursor.getInt(2), cursor.getString(3)));
        }
        cursor.close();

        ListView listView = (ListView) findViewById(R.id.listView);
        ListAdapter listAdapter = new ListAdapter(EditorActivity.this, R.layout.tablerow, measurementList);
        listView.setAdapter(listAdapter);
    }

    private void finishInput() {
        // TODO hide keyboard
    }
}