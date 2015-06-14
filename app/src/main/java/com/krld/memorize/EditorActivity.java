package com.krld.memorize;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.*;
import com.krld.memorize.common.DataType;
import com.krld.memorize.common.DbOpenHelper;
import com.krld.memorize.common.ListAdapter;
import com.krld.memorize.model.MeasurementLegacy;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class EditorActivity extends Activity {
    EditText inputText = null;
    Button saveButton = null;
    Button readButton = null;
    public static DbOpenHelper dbHelper = null;
    private DataType datatype;
    private Intent shareIntent;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor, menu);
    /*    MenuItem item = menu.findItem(R.id.share_button);
        item.setOnMenuItemClickListener(menuItem -> {
            startActivity(Intent.createChooser(shareIntent, "Share"));
            return true;
        });*/
        //TODO
        return true;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_editor);
        datatype = DataType.valueOf(getIntent().getStringExtra(MenuActivity.DATATYPE));
        initTitle();
        dbHelper = new DbOpenHelper(EditorActivity.this, DbOpenHelper.DB_NAME, null, DbOpenHelper.DB_VERSION);
        initViews();

        refreshListViewMeasurement();
    }

    private void initTitle() {
        setTitle(getString(datatype.stringId).toUpperCase() +" : " + getString(R.string.label_editor));
    }

    private void initViews() {
        inputText = (EditText) findViewById(R.id.login);
        inputText.setImeActionLabel("Save", KeyEvent.KEYCODE_ENTER);
        inputText.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if (actionId == EditorInfo.IME_NULL && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                saveValue();
            }
            return false;
        });
        saveButton = (Button) findViewById(R.id.saveButton);
        readButton = (Button) findViewById(R.id.readButton);

        saveButton.setOnClickListener(view -> saveValue());
        readButton.setOnClickListener(view -> refreshListViewMeasurement());
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
        List<MeasurementLegacy> measurementLegacyList = new ArrayList<MeasurementLegacy>();
        while (cursor.moveToNext()) {
            //   Log.d("KRLD", "id: " + cursor.getInt(0) + " value:  " + cursor.getString(1) + " date: " + cursor.getInt(2) + " type " + cursor.getString(3));
            measurementLegacyList.add(new MeasurementLegacy(cursor.getInt(0), cursor.getString(1), cursor.getInt(2), cursor.getString(3)));
        }
        cursor.close();

        ListView listView = (ListView) findViewById(R.id.listView);
        ListAdapter listAdapter = new ListAdapter(EditorActivity.this, R.layout.li_table, measurementLegacyList);
        listView.setAdapter(listAdapter);
    }

    private void finishInput() {
        // TODO hide keyboard
    }
}
