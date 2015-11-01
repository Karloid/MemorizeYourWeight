package com.krld.diet.memorize.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.krld.diet.R;
import com.krld.diet.memorize.common.DataType;

public class MemorizeActivity extends Activity {
    public static final String DATATYPE = "datatype";
    private Button weightButton;
    private Button waistButton;
    private Button hipsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_menu);
        initButtons();

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
