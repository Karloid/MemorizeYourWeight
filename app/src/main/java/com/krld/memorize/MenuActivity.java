package com.krld.memorize;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.krld.memorize.common.DataType;
import com.krld.memorize.sugar.TestMeasure;

/**
 * Created by Andrey on 7/16/2014.
 */
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

        testSugar();
    }

    private void testSugar() {
        TestMeasure testMeasure = new TestMeasure(10);
        testMeasure.save();
        testMeasure.getId();
        Log.d("MemorizeTest", TestMeasure.listAll(TestMeasure.class).size() + " test measurements");
    }

    private void initButtons() {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToEditorActivity((DataType) view.getTag());
            }
        };

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
        showToast("Open " + dataType + " editor");
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
