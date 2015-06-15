package com.krld.memorize;

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
import android.view.inputmethod.EditorInfo;
import android.widget.*;

import com.activeandroid.Model;
import com.activeandroid.query.From;
import com.activeandroid.query.Select;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.krld.memorize.common.DataType;
import com.krld.memorize.common.DbOpenHelper;
import com.krld.memorize.common.ListAdapter;
import com.krld.memorize.models.Measurement;

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
import rx.functions.Action0;

public class EditorActivity extends Activity {
    private static final int PICKFILE_REQUEST_CODE = 1;
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
                importData();
                break;
            case R.id.menu_editor_export:
                exportData();
                break;
            case R.id.menu_editor_remove_all:
                removeAllDataConfirmation();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void removeAllDataConfirmation() {
        showAlert(getString(R.string.remove_all_confirmation), this::removeAllData);
    }

    private void removeAllData() {
        List<Measurement> items = new Select().from(Measurement.class).execute();
        Observable.from(items).subscribe(Model::delete, Throwable::printStackTrace,
                () -> {
                    refreshListViewMeasurement();
                    showInfo(String.format(getString(R.string.remove_all_end), items.size() + ""));
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
            if (handlePickedFile(data)) return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private boolean handlePickedFile(Intent data) {
        Uri importPath = data.getData();
        if (!importPath.getPath().contains(".json")) {
            showError(getString(R.string.label_import_bad_file));
            return true;
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
                refreshListViewMeasurement();
                showInfo(String.format(getString(R.string.label_import_successful), finalFromJson.size() + ""));
            });

        } catch (Exception e) {
            e.printStackTrace();
            showError(e.getMessage());
        }
        return false;
    }

    private void exportData() {
        List<Measurement> items = new Select().from(Measurement.class).execute();
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        String json = gson.toJson(items);
        Log.d("MemorizeLog", json);
        try {
            File root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            String fileName = "memorize " + new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()) + ".json";
            File gpxfile = new File(root, fileName);
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(json);
            writer.flush();
            writer.close();
            showInfo(String.format(getString(R.string.toast_exported), fileName, items.size() + ""));
        } catch (IOException e) {
            e.printStackTrace();
            showToast(String.format(getString(R.string.toast_exported_error), e.getMessage()));
        }
    }

    private void showToast(String string) {
        Toast.makeText(this, string, Toast.LENGTH_LONG).show();
    }

    private void showError(String message) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.label_error)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                    dialog.dismiss();
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void showAlert(String message, Action0 action0) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.label_alert)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                    action0.call();
                    dialog.dismiss();
                })
                .setNegativeButton(android.R.string.cancel, (dialog, which) -> {
                    dialog.dismiss();
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void showInfo(String message) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.label_info)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                    dialog.dismiss();
                })
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
    }
}
