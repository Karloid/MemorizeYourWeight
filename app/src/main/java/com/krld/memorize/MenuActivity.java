package com.krld.memorize;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.activeandroid.Model;
import com.activeandroid.query.Select;
import com.krld.memorize.common.DataType;
import com.krld.memorize.models.Measurement;

import java.util.List;

public class MenuActivity extends Activity {
    public static final String DATATYPE = "datatype";
    private Button weightButton;
    private Button waistButton;
    private Button hipsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_menu);
        initButtons();

        testAA();
    }

    private void testAA() {
        Measurement measurement = new Measurement();
        measurement.value = "test";
        measurement.save();
        List<Measurement> list = new Select().from(Measurement.class).execute();
        for (Measurement m : list) {
            Log.d("MemorizeDebug", m.value + " " + m.getId());
        }

    }

    private void initButtons() {
        View.OnClickListener listener = view -> goToEditorActivity((DataType) view.getTag());

        weightButton = (Button) findViewById(R.id.weight_button);
        weightButton.setTag(DataType.WEIGHT);
        weightButton.setOnClickListener(listener);
        waistButton = (Button) findViewById(R.id.waist_button);
        waistButton.setTag(DataType.WAIST);
        waistButton.setOnClickListener(listener);
        hipsButton = (Button) findViewById(R.id.hips_button);
        hipsButton.setTag(DataType.HIPS);
        hipsButton.setOnClickListener(listener);
    }

    private void goToEditorActivity(DataType dataType) {
        showToast(String.format(getString(R.string.menu_button_clicked_toast), getString(dataType.stringId)));
        Intent intent = new Intent(this, EditorActivity.class);
        intent.putExtra(DATATYPE, dataType.toString());
        startActivity(intent);
    }

    private void showToast(String message) {
        try {
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
