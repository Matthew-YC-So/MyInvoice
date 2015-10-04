package com.software_force.myinvoice;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by Matthew So on 10/4/2015.
 */
public class DataSource {
    protected SQLiteDatabase database;
    protected DBHelper dbHelper;
    protected SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

    public DataSource(Context context){
        dbHelper = new DBHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
        database.execSQL("PRAGMA foreign_keys = ON");
    }

    public void close() {
        dbHelper.close();
    }

}
