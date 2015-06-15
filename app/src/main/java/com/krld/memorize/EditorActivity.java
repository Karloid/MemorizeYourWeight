package com.krld.memorize;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.*;

import com.activeandroid.Model;
import com.activeandroid.query.From;
import com.activeandroid.query.Select;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.krld.memorize.common.DataType;
import com.krld.memorize.common.DbOpenHelper;
import com.krld.memorize.common.ListAdapter;
import com.krld.memorize.models.Measurement;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class EditorActivity extends Activity {
    EditText inputText = null;
    Button saveButton = null;
    Button readButton = null;
    public static DbOpenHelper dbHelper = null;
    private DataType datatype;
    private Intent shareIntent;
    private ListAdapter listAdapter;

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
        setTitle(getString(datatype.stringId).toUpperCase() + " : " + getString(R.string.label_editor));
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

        ListView listView = (ListView) findViewById(R.id.listView);
        listAdapter = new ListAdapter(EditorActivity.this);
        listView.setAdapter(listAdapter);
        listView.setOnItemLongClickListener((parent, view, position, id) -> {
                    longTapOn((Measurement) parent.getItemAtPosition(position));
                    return true;
                }
        );
    }

    private void saveValue() {
        double value;
        try {
            value = Double.parseDouble(inputText.getText().toString().replaceAll(",", "."));
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Not a number", Toast.LENGTH_SHORT).show();
            inputText.setText("");
            return;
        }
        Measurement measurement = new Measurement();

        measurement.value = value;
        measurement.datatype = datatype.toString();
        measurement.insertDate = Calendar.getInstance();
        measurement.save();

        inputText.setText("");
        Toast.makeText(this, "Inserted!", Toast.LENGTH_SHORT).show();
        refreshListViewMeasurement();
    }

    public void refreshListViewMeasurement() {
        List<Measurement> measurements = getSelectQuery().execute();
        Collections.sort(measurements, (lhs, rhs) -> rhs.insertDate.compareTo(lhs.insertDate));
        listAdapter.setItems(measurements);
    }

    @NonNull
    private From getSelectQuery() {
        return new Select().from(Measurement.class).where("DATATYPE = ?", datatype.toString());
    }

    //TODO
    public void longTapOn(Measurement obj) {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(
                EditorActivity.this);
        builderSingle.setIcon(R.drawable.sql_icon);
        builderSingle.setTitle(R.string.label_select_operation);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                EditorActivity.this,
                android.R.layout.simple_list_item_1);
        arrayAdapter.add(getString(R.string.label_edit));
        arrayAdapter.add(getString(R.string.label_delete));
        builderSingle.setNegativeButton(R.string.label_cancel,
                (dialog, which) -> {
                    dialog.dismiss();
                });

        builderSingle.setAdapter(arrayAdapter,
                (dialog, which) -> {
                    //TODO
                    //edit/delete
                   /* String strName = arrayAdapter.getItem(which);
                    AlertDialog.Builder builderInner = new AlertDialog.Builder(
                            EditorActivity.this);
                    builderInner.setMessage(strName);
                    builderInner.setTitle("Your Selected Item is");
                    builderInner.setPositiveButton("Ok",
                            (dialog1, which1) -> {
                                dialog1.dismiss();
                            });
                    builderInner.show();*/
                });
        builderSingle.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_editor_add_custom:
                break;
            case R.id.menu_editor_import:
                break;
            case R.id.menu_editor_export:
                exportData();
                break;
            case R.id.menu_editor_remove_all:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void exportData() {
        List<Measurement> items = new Select().from(Measurement.class).execute();
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        String json = gson.toJson(items);
        Log.d("MemorizeLog", json);
        showToast(String.format(getString(R.string.toast_exported), items.size() +""));

    }

    private void showToast(String string) {
       Toast.makeText(this, string, Toast.LENGTH_LONG).show();
    }
}
