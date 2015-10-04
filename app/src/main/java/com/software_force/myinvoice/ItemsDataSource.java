package com.software_force.myinvoice;

/**
 * Created by Matthew So on 10/4/2015.
 */
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.software_force.myinvoice.models.Item;

public class ItemsDataSource extends DataSource {

    // Database fields
    private String[] allColumns = { DBHelper.ITEMS_COLUMN_CODE,
            DBHelper.ITEMS_COLUMN_DESCRIPTION, DBHelper.ITEMS_COLUMN_PRICE };

    public ItemsDataSource(Context context){
        super(context);
    }

    public Item insertItem(String code, String description, BigDecimal price) {

        ContentValues values = new ContentValues();
        values.put(DBHelper.ITEMS_COLUMN_CODE, code);
        values.put(DBHelper.ITEMS_COLUMN_DESCRIPTION, description);
        values.put(DBHelper.ITEMS_COLUMN_PRICE, price.toPlainString());

        long insertId = database.insert(DBHelper.ITEMS_TABLE_NAME, null, values);
        Cursor cursor = database.query(DBHelper.ITEMS_TABLE_NAME,
                allColumns, DBHelper.ITEMS_COLUMN_CODE + " = '" + code + "'", null,
                null, null, null);
        cursor.moveToFirst();
        Item newItem = cursorToItem(cursor);
        cursor.close();
        return newItem;
    }

    public void deleteItem(Item item) {
        String code = item.getCode();
        database.delete(DBHelper.ITEMS_TABLE_NAME, DBHelper.ITEMS_COLUMN_CODE
                + " = " + code, null);
        Log.i(this.getClass().getSimpleName(), "Item deleted with code: " + code);
    }

    public List<Item> getAllItems() {
        List<Item> items = new ArrayList<Item>();

        Cursor cursor = database.query(DBHelper.ITEMS_TABLE_NAME,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Item item = cursorToItem(cursor);
            items.add(item);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return items;
    }

    public Item getItem (String itemCode) {
        Cursor cursor =  database.rawQuery("select * from " + DBHelper.ITEMS_TABLE_NAME + " where "
                + DBHelper.ITEMS_COLUMN_CODE + " =" + itemCode + "", null);
        Item item = null;
        cursor.moveToFirst();
        while(cursor.isAfterLast() == false){
            item = cursorToItem(cursor);
            break;
        }
        return item;
    }

    private Item cursorToItem(Cursor cursor) {
        Item item = new Item();
        item.setCode(cursor.getString(cursor.getColumnIndex(DBHelper.ITEMS_COLUMN_CODE)));
        item.setDescription(cursor.getString(cursor.getColumnIndex(
                DBHelper.ITEMS_COLUMN_DESCRIPTION)));
        item.setPrice(new BigDecimal(cursor.getString(cursor.getColumnIndex(
                DBHelper.ITEMS_COLUMN_PRICE))));
        return item;
    }

    public int numberOfRows(){
        int numRows = (int) DatabaseUtils.queryNumEntries(database, DBHelper.ITEMS_TABLE_NAME);
        return numRows;
    }

}