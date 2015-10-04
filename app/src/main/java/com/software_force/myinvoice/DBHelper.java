package com.software_force.myinvoice;

/**
 * Created by Matthew So on 10/3/2015.
 */
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;


public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "MyInvoiceDB.db";

    // System Settings
    public static final String SYSTEMSETTINGS_TABLE_NAME = "systemsettings";
    public static final String SYSTEMSETTINGS_COLUMN_CODE = "code";
    public static final String SYSTEMSETTINGS_COLUMN_VALUE = "value";

    // Invoices
    public static final String INVOICES_TABLE_NAME = "invoices";
    public static final String INVOICES_COLUMN_ID = "id";
    public static final String INVOICES_COLUMN_MEMBER = "member";
    public static final String INVOICES_COLUMN_INVOICEDATE = "invoicedate";
    public static final String INVOICES_COLUMN_GROSS = "gross";
    public static final String INVOICES_COLUMN_DISCOUNT = "discount";
    public static final String INVOICES_COLUMN_NET = "net";
    public static final String INVOICES_COLUMN_GST = "gst";

    // InvoiceLines
    public static final String INVOICELINES_TABLE_NAME = "invoicelines";
    public static final String INVOICELINES_COLUMN_INVOICEID = "invoiceid";
    public static final String INVOICELINES_COLUMN_SEQ = "seq";
    public static final String INVOICELINES_COLUMN_ITEMCODE = "itemcode";
    public static final String INVOICELINES_COLUMN_PRICE = "price";
    public static final String INVOICELINES_COLUMN_QTY = "qty";
    public static final String INVOICELINES_COLUMN_AMOUNT = "amount";

    // Items
    public static final String ITEMS_TABLE_NAME = "items";
    public static final String ITEMS_COLUMN_CODE = "code";
    public static final String ITEMS_COLUMN_DESCRIPTION = "description";
    public static final String ITEMS_COLUMN_PRICE = "price";

    // private HashMap hp;

    public DBHelper(Context context)
    {
        super(context, DATABASE_NAME, null, 5);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create tables
        db.execSQL(
                "create table " + SYSTEMSETTINGS_TABLE_NAME +
                        "(" + SYSTEMSETTINGS_COLUMN_CODE + " text primary key, " +
                        SYSTEMSETTINGS_COLUMN_VALUE + " text)"
        );

        db.execSQL(
                "create table " + ITEMS_TABLE_NAME +
                        "(" + ITEMS_COLUMN_CODE + " text primary key, " +
                        ITEMS_COLUMN_DESCRIPTION + " text, " +
                        ITEMS_COLUMN_PRICE + " text)"
        );


        db.execSQL(
                "create table " + INVOICES_TABLE_NAME +
                        "(" + INVOICES_COLUMN_ID + " integer primary key, " +
                        INVOICES_COLUMN_MEMBER + " text, " +
                        INVOICES_COLUMN_INVOICEDATE + " text," +
                        INVOICES_COLUMN_GROSS + " text, " +
                        INVOICES_COLUMN_DISCOUNT + " text, " +
                        INVOICES_COLUMN_NET + " text, " +
                        INVOICES_COLUMN_GST + " text)"
        );

        db.execSQL(
                "create table " + INVOICELINES_TABLE_NAME +
                        "(" + INVOICELINES_COLUMN_INVOICEID + " integer REFERENCES " + INVOICES_TABLE_NAME + "(" + INVOICES_COLUMN_ID + ") ON DELETE CASCADE, " +
                        INVOICELINES_COLUMN_SEQ + " integer , " +
                        INVOICELINES_COLUMN_ITEMCODE + " text," +
                        INVOICELINES_COLUMN_PRICE + " text, " +
                        INVOICELINES_COLUMN_QTY + " text, " +
                        INVOICELINES_COLUMN_AMOUNT + " text, " +
                        "primary key ( " + INVOICELINES_COLUMN_INVOICEID + "," +
                        INVOICELINES_COLUMN_SEQ + " ))"
        );
        // trackartist INTEGER DEFAULT 0 REFERENCES artist(artistid) ON DELETE CASCADE

        // Insert factory default data
        db.execSQL(
                "insert into " + SYSTEMSETTINGS_TABLE_NAME + " values (?, ?)",
                new String[]{"DBversion", "1.0"}
        );

        // Sample data
        db.execSQL("insert into " + ITEMS_TABLE_NAME + " values (?, ?, ?)",
                new String[]{"0001", "MODEL AX221", "100.00"});
        db.execSQL("insert into " + ITEMS_TABLE_NAME + " values (?, ?, ?)",
                new String[]{"0002", "MODEL AX222", "200.00"});
        db.execSQL("insert into " + ITEMS_TABLE_NAME + " values (?, ?, ?)",
                new String[]{"0003", "MODEL AX223", "300.00"});

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS " + SYSTEMSETTINGS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ITEMS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + INVOICES_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + INVOICELINES_TABLE_NAME);
        onCreate(db);
    }

}
