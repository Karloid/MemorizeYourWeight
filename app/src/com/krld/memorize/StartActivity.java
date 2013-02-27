package com.krld.memorize;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.krld.memorize.common.DbOpenHelper;

public class StartActivity extends Activity {
    EditText loginEditText = null;
    EditText passEditText = null;
    Button saveButton = null;
    Button readButton = null;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        loginEditText = (EditText) findViewById(R.id.login);
        passEditText = (EditText) findViewById(R.id.passw);
        saveButton = (Button) findViewById(R.id.saveButton);
        readButton = (Button) findViewById(R.id.readButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DbOpenHelper dbOpenHelper = new DbOpenHelper(StartActivity.this, DbOpenHelper.DB_NAME, null, DbOpenHelper.DB_VERSION);
                SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
                ContentValues cv = new ContentValues();
                cv.put(DbOpenHelper.LOGIN, loginEditText.getText().toString());
                cv.put(DbOpenHelper.PASSW, passEditText.getText().toString());
                db.insert(DbOpenHelper.TABLE_NAME, null, cv);
                db.close();
                loginEditText.setText("");
                passEditText.setText("");
                Toast.makeText(StartActivity.this, "Insert", Toast.LENGTH_SHORT).show();

            }
        });
                readButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DbOpenHelper dbOpenHelper = new DbOpenHelper(StartActivity.this, DbOpenHelper.DB_NAME, null, DbOpenHelper.DB_VERSION);
                        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
                        Cursor cursor = db.rawQuery("select " + DbOpenHelper.LOGIN + ", " + DbOpenHelper.PASSW +" from " + DbOpenHelper.TABLE_NAME, null);
                        while (cursor.moveToNext()) {
                            Toast.makeText(StartActivity.this, "Login: " + cursor.getString(0) + " / " + cursor.getString(1), Toast.LENGTH_SHORT).show();
                        }
                        cursor.close();

                    }
                });
    }
}
