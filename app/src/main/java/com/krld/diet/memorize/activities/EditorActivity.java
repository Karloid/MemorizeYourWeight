package com.krld.diet.memorize.activities;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.*;

import com.activeandroid.Model;
import com.activeandroid.query.From;
import com.activeandroid.query.Select;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.krld.diet.R;
import com.krld.diet.memorize.common.DataType;
import com.krld.diet.memorize.common.FormatterHelper;
import com.krld.diet.memorize.common.ListAdapter;
import com.krld.diet.common.helpers.DialogHelper;
import com.krld.diet.memorize.models.Measurement;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import rx.Observable;

public class EditorActivity extends Activity {
    private static final int PICKFILE_REQUEST_CODE = 1;
    public static final float ERROR_VALUE = -1f;
    EditText inputText = null;
    Button saveButton = null;
    Button readButton = null;
    private DataType datatype;
    private ListAdapter listAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_editor);
        datatype = DataType.valueOf(getIntent().getStringExtra(MenuActivity.DATATYPE));
        initTitle();
        initViews();
        refreshListView();

        ActionBar actionBar = getActionBar();
        //noinspection ConstantConditions
        actionBar.setDisplayHomeAsUpEnabled(true);
        //noinspection ConstantConditions
        actionBar.setHomeButtonEnabled(true);
    }


    private void initTitle() {
        setTitle(getString(datatype.stringId).toUpperCase() + " : " + getString(R.string.label_editor));
    }

    private void initViews() {
        inputText = (EditText) findViewById(R.id.login);
        inputText.setImeActionLabel("Save", KeyEvent.KEYCODE_ENTER);
        inputText.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if (actionId == EditorInfo.IME_NULL && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                saveValue(Calendar.getInstance(), datatype.toString());
            }
            return false;
        });
        saveButton = (Button) findViewById(R.id.saveButton);
        readButton = (Button) findViewById(R.id.readButton);

        saveButton.setOnClickListener(view -> saveValue(Calendar.getInstance(), datatype.toString()));
        readButton.setOnClickListener(view -> refreshListView());

        ListView listView = (ListView) findViewById(R.id.listView);
        listAdapter = new ListAdapter(EditorActivity.this);
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener((parent, view, position, id) -> {
                    longTapOn((Measurement) parent.getItemAtPosition(position));
                }
        );
    }

    private void saveValue(Calendar date, String dateType) {
        String string = inputText.getText().toString().replaceAll(",", ".");
        Measurement measurement = new Measurement();

        double v = parseDoubleFromString(string);
        if (v == ERROR_VALUE) {
            return;
        }
        measurement.value = v;
        measurement.datatype = dateType;
        measurement.insertDate = date;
        measurement.save();

        inputText.setText("");
        Toast.makeText(this, "Inserted!", Toast.LENGTH_SHORT).show();
        refreshListView();
    }

    public void refreshListView() {
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
        builderSingle.setIcon(R.drawable.sql_icon_mini);
        builderSingle.setTitle(R.string.label_select_operation);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                EditorActivity.this,
                android.R.layout.simple_list_item_1);
        arrayAdapter.add(getString(R.string.label_edit));
        arrayAdapter.add(getString(R.string.label_remove));
        builderSingle.setNegativeButton(R.string.label_cancel,
                (dialog, which) -> {
                    dialog.dismiss();
                });

        builderSingle.setAdapter(arrayAdapter,
                (dialog, which) -> {
                    //TODO
                    if (which == 0) {
                        showEditDialog(obj);
                    } else if (which == 1) {
                        obj.delete();
                        refreshListView();
                        DialogHelper.showInfo(this, String.format(getString(R.string.label_remove_successful),
                                        "\n\"" +
                                                FormatterHelper.formatDouble(obj.value) +
                                                " " +
                                                FormatterHelper.formatDate(obj.insertDate.getTime()) +
                                                "\""
                                )
                        );
                    }

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
                addCustom();
                break;
            case R.id.menu_editor_import:
                importData();
                break;
            case R.id.menu_editor_export:
                exportData();
                break;
            case R.id.menu_editor_remove_all:
                removeAllDataConfirmation();
                break;
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("NewApi")
    private void addCustom() {
        Measurement measurement = new Measurement();
        measurement.insertDate = Calendar.getInstance();
        showEditDialog(measurement);
    }

    private void showEditDialog(Measurement obj) {
        View v = getLayoutInflater().inflate(R.layout.v_custom_item, null);
        EditText editValue = (EditText) v.findViewById(R.id.edit_value);
        DatePicker datePicker = (DatePicker) v.findViewById(R.id.date_picker);
        TimePicker timePicker = (TimePicker) v.findViewById(R.id.time_picker);
        timePicker.setIs24HourView(true);

        datePicker.setDescendantFocusability(TimePicker.FOCUS_BLOCK_DESCENDANTS);
        timePicker.setDescendantFocusability(TimePicker.FOCUS_BLOCK_DESCENDANTS);

        editValue.setText(obj.value != 0f ?FormatterHelper.formatDouble(obj.value) : "");

        Calendar cal = obj.insertDate;

        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int min = cal.get(Calendar.MINUTE);

        datePicker.updateDate(year, month, day);

        timePicker.setCurrentHour(hour);
        timePicker.setCurrentMinute(min);

        //TODO
        new AlertDialog.Builder(this).setView(v)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                    String string = editValue.getText().toString().replaceAll(",", ".");
                    double v2 = parseDoubleFromString(string);
                    if (v2 == ERROR_VALUE) {
                        return;
                    }
                    obj.value = v2;
                    cal.set(Calendar.YEAR, datePicker.getYear());
                    cal.set(Calendar.MONTH, datePicker.getMonth());
                    cal.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
                    cal.set(Calendar.HOUR_OF_DAY, timePicker.getCurrentHour());
                    cal.set(Calendar.MINUTE, timePicker.getCurrentMinute());
                    obj.datatype = datatype.toString();
                    obj.save();
                    refreshListView();
                })
                .setNegativeButton(android.R.string.cancel, (dialog1, which1) -> dialog1.dismiss())
                .show();
    }

    private double parseDoubleFromString(String string) {
        double value = -1;
        try {
            value = Double.parseDouble(string);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Not a number", Toast.LENGTH_SHORT).show();
            inputText.setText("");
        }
        return value;
    }

    private void removeAllDataConfirmation() {
        DialogHelper.showAlert(this, getString(R.string.remove_all_confirmation), this::removeAllData);
    }

    private void removeAllData() {
        List<Measurement> items = new Select().from(Measurement.class).execute();
        Observable.from(items).subscribe(Model::delete, Throwable::printStackTrace,
                () -> {
                    refreshListView();
                    DialogHelper.showInfo(this, String.format(getString(R.string.remove_all_end), items.size() + ""));
                }
        );
    }

    private void importData() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("file/*.json");
        startActivityForResult(intent, PICKFILE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICKFILE_REQUEST_CODE) {
            handlePickedFile(data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void showToast(String string) {
        Toast.makeText(this, string, Toast.LENGTH_LONG).show();
    }

    private void handlePickedFile(Intent data) {
        if (data == null) return;
        Uri importPath = data.getData();
        if (!importPath.getPath().contains(".json")) {
            DialogHelper.showError(getString(R.string.label_import_bad_file), this);
            return;
        }
        File importFile = new File(importPath.getPath());
        try {
            String contents = new Scanner(importFile).useDelimiter("\\A").next();
            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
            List<Measurement> fromJson = gson.fromJson(contents, new TypeToken<ArrayList<Measurement>>() {
            }.getType());
            List<Measurement> currentMeasumenets = new Select().from(Measurement.class).execute();

            fromJson = Observable.from(fromJson).filter(measurement -> {
                for (Measurement item : currentMeasumenets) {
                    if (item.simpleEquals(measurement))
                        return false;
                }
                return true;
            }).toList().toBlocking().single();
            final List<Measurement> finalFromJson = fromJson;
            Observable.from(fromJson).subscribe(Model::save, Throwable::printStackTrace, () -> {
                refreshListView();
                DialogHelper.showInfo(this, String.format(getString(R.string.label_import_successful), finalFromJson.size() + ""));
            });

        } catch (Exception e) {
            e.printStackTrace();
            DialogHelper.showError(e.getMessage(), this);
        }
    }

    private void exportData() {
        List<Measurement> items = new Select().from(Measurement.class).execute();
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        String json = gson.toJson(items);
        Log.d("MemorizeLog", json);
        try {
            File root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            @SuppressLint("SimpleDateFormat") String fileName = "memorize " + new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()) + ".json";
            File gpxfile = new File(root, fileName);
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(json);
            writer.flush();
            writer.close();
            DialogHelper.showInfo(this, String.format(getString(R.string.toast_exported), fileName, items.size() + ""));
        } catch (IOException e) {
            e.printStackTrace();
            showToast(String.format(getString(R.string.toast_exported_error), e.getMessage()));
        }
    }


}
