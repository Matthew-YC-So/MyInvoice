package com.software_force.myinvoice;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.software_force.myinvoice.models.Invoice;
import com.software_force.myinvoice.models.InvoiceLine;
import com.software_force.myinvoice.models.Item;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Matthew So on 10/4/2015.
 */
public class InvoicesDataSource extends DataSource {

    // Database fields
    private String[] allColumns = { "rowid _id"
            , DBHelper.INVOICES_COLUMN_ID, DBHelper.INVOICES_COLUMN_MEMBER
            , DBHelper.INVOICES_COLUMN_INVOICEDATE, DBHelper.INVOICES_COLUMN_GROSS
            , DBHelper.INVOICES_COLUMN_DISCOUNT
            , DBHelper.INVOICES_COLUMN_NET
            , DBHelper.INVOICES_COLUMN_GST };

    public InvoicesDataSource(Context context){
        super(context);
    }

    public Invoice insertInvoice(String member, Date invoiceDate, BigDecimal gross,
                                 BigDecimal discount, BigDecimal net, BigDecimal gst) {

        ContentValues values = new ContentValues();
        values.put(DBHelper.INVOICES_COLUMN_MEMBER, member);
        values.put(DBHelper.INVOICES_COLUMN_INVOICEDATE, dateFormat.format(invoiceDate));
        values.put(DBHelper.INVOICES_COLUMN_GROSS, gross.toPlainString());
        values.put(DBHelper.INVOICES_COLUMN_DISCOUNT, discount.toPlainString());
        values.put(DBHelper.INVOICES_COLUMN_NET, net.toPlainString());
        values.put(DBHelper.INVOICES_COLUMN_GST, gst.toPlainString());

        long insertId = database.insert(DBHelper.INVOICES_TABLE_NAME, null, values);
        Cursor cursor = database.query(DBHelper.INVOICES_TABLE_NAME,
                allColumns, DBHelper.INVOICES_COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Invoice newInvoice = cursorToInvoice(cursor);
        cursor.close();
        return newInvoice;
    }

    public Invoice insertInvoice (Invoice invoice)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.INVOICES_COLUMN_MEMBER, invoice.getMember());
        contentValues.put(DBHelper.INVOICES_COLUMN_INVOICEDATE, dateFormat.format(invoice.getInvoiceDate()));
        contentValues.put(DBHelper.INVOICES_COLUMN_GROSS, invoice.getGross().toPlainString());
        contentValues.put(DBHelper.INVOICES_COLUMN_DISCOUNT, invoice.getDiscount().toPlainString());
        contentValues.put(DBHelper.INVOICES_COLUMN_NET, invoice.getNet().toPlainString());
        contentValues.put(DBHelper.INVOICES_COLUMN_GST, invoice.getGst().toPlainString());
        long id = database.insert(DBHelper.INVOICES_TABLE_NAME, null, contentValues);

        List<InvoiceLine> invoiceLines =invoice.getInvoiceLines();

        for(int i = 0; i < invoiceLines.size() ; ++i) {
            contentValues = new ContentValues();
            InvoiceLine invoiceLine = invoiceLines.get(i);
            invoiceLine.setInvoiceId(id);
            contentValues.put(DBHelper.INVOICELINES_COLUMN_INVOICEID, invoiceLine.getInvoiceId());
            contentValues.put(DBHelper.INVOICELINES_COLUMN_SEQ, invoiceLine.getSeq());
            contentValues.put(DBHelper.INVOICELINES_COLUMN_ITEMCODE, invoiceLine.getItemCode());
            contentValues.put(DBHelper.INVOICELINES_COLUMN_PRICE, invoiceLine.getPrice().toPlainString());
            contentValues.put(DBHelper.INVOICELINES_COLUMN_QTY, invoiceLine.getQty());
            contentValues.put(DBHelper.INVOICELINES_COLUMN_AMOUNT, invoiceLine.getAmount().toPlainString());
            database.insert(DBHelper.INVOICELINES_TABLE_NAME, null, contentValues);
        }
        return invoice;
    }

    public boolean updateInvoice (Invoice invoice)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.INVOICES_COLUMN_MEMBER, invoice.getMember());
        contentValues.put(DBHelper.INVOICES_COLUMN_INVOICEDATE, dateFormat.format(invoice.getInvoiceDate()));
        contentValues.put(DBHelper.INVOICES_COLUMN_GROSS, invoice.getGross().toPlainString());
        contentValues.put(DBHelper.INVOICES_COLUMN_DISCOUNT, invoice.getDiscount().toPlainString());
        contentValues.put(DBHelper.INVOICES_COLUMN_NET, invoice.getNet().toPlainString());
        contentValues.put(DBHelper.INVOICES_COLUMN_GST, invoice.getGst().toPlainString());
        database.update(DBHelper.INVOICES_TABLE_NAME, contentValues, "id = ? ",
                new String[]{ Long.toString(invoice.getId()) });
        return true;
    }

    public void deleteInvoice(long id) {
        database.delete(DBHelper.INVOICES_TABLE_NAME, DBHelper.INVOICES_COLUMN_ID
                + " = " + id, null);
        Log.i(this.getClass().getSimpleName(), "Invoice deleted with id: " + id);
    }

    public void deleteInvoice(Invoice invoice) {
        Long id = invoice.getId();
        database.delete(DBHelper.INVOICES_TABLE_NAME, DBHelper.INVOICES_COLUMN_ID
                + " = " + id, null);
        Log.i(this.getClass().getSimpleName(), "Invoice deleted with id: " + id);
    }

    public List<Invoice> getAllInvoices() {
        List<Invoice> invoices = new ArrayList<Invoice>();

        Cursor cursor = database.query(DBHelper.INVOICES_TABLE_NAME,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Invoice invoice = cursorToInvoice(cursor);
            invoices.add(invoice);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return invoices;
    }

    public Cursor getAllInvoicesAsCursor() {
        Cursor cursor = database.query(DBHelper.INVOICES_TABLE_NAME,
                allColumns, null, null, null, null, null);

        return cursor;
    }


    private Invoice cursorToInvoice(Cursor cursor) {
        Invoice invoice = new Invoice();
        invoice.setId(cursor.getLong(cursor.getColumnIndex(DBHelper.INVOICES_COLUMN_ID)));
        invoice.setMember(cursor.getString(cursor.getColumnIndex(DBHelper.INVOICES_COLUMN_MEMBER)));
        try {
            invoice.setInvoiceDate(dateFormat.parse(cursor.getString(cursor.getColumnIndex(
                    DBHelper.INVOICES_COLUMN_INVOICEDATE))));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        invoice.setGross(new BigDecimal( cursor.getString((cursor.getColumnIndex(DBHelper.INVOICES_COLUMN_GROSS)))));
        invoice.setDiscount(new BigDecimal(cursor.getString((cursor.getColumnIndex(DBHelper.INVOICES_COLUMN_DISCOUNT)))));
        invoice.setNet(new BigDecimal(cursor.getString((cursor.getColumnIndex(DBHelper.INVOICES_COLUMN_NET)))));
        invoice.setGst(new BigDecimal(cursor.getString((cursor.getColumnIndex(DBHelper.INVOICES_COLUMN_GST)))));
        return invoice;
    }

    public Cursor getInvoice(int id){
        Cursor cursor =  database.rawQuery( "select invoices." + DBHelper.INVOICES_COLUMN_ID +
                ", invoices." + DBHelper.INVOICES_COLUMN_MEMBER +
                ", invoices." + DBHelper.INVOICES_COLUMN_INVOICEDATE +
                ", invoices." + DBHelper.INVOICES_COLUMN_GROSS +
                ", invoices." + DBHelper.INVOICES_COLUMN_DISCOUNT +
                ", invoices." + DBHelper.INVOICES_COLUMN_NET +
                ", invoices." + DBHelper.INVOICES_COLUMN_GST +
                ", invoicelines." + DBHelper.INVOICELINES_COLUMN_SEQ +
                ", invoicelines." + DBHelper.INVOICELINES_COLUMN_ITEMCODE +
                ", invoicelines." + DBHelper.INVOICELINES_COLUMN_PRICE +
                ", invoicelines." + DBHelper.INVOICELINES_COLUMN_QTY  +
                ", invoicelines." + DBHelper.INVOICELINES_COLUMN_AMOUNT  +
                " from " + DBHelper.INVOICES_TABLE_NAME  +
                " as invoices inner join " + DBHelper.INVOICELINES_TABLE_NAME +
                " as invoicelines on invoices." + DBHelper.INVOICES_COLUMN_ID + " = invoicelines." +
                DBHelper.INVOICELINES_COLUMN_INVOICEID +
                " where " + DBHelper.INVOICES_COLUMN_ID + " = " + id + "", null );
        return cursor;
    }

    public int numberOfRows(){
        int numRows = (int) DatabaseUtils.queryNumEntries(database, DBHelper.INVOICES_TABLE_NAME);
        return numRows;
    }


    public Invoice CreateSample(){

        Cursor cursor = getAllInvoicesAsCursor();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            long id = cursor.getLong( cursor.getColumnIndex(DBHelper.INVOICES_COLUMN_ID));
            this.deleteInvoice(id);
            cursor.moveToNext();
        }
        cursor.close();

        Invoice invoice = new Invoice() ;
        invoice.setMember("112233");
        invoice.setInvoiceDate(new Date());
        invoice.setDiscount(new BigDecimal(0));
        invoice.setGst(new BigDecimal(0));

        ArrayList<InvoiceLine> invoiceLines = invoice.getInvoiceLines();
        BigDecimal total = new BigDecimal(0);
        for (int i = 0 ; i < 3 ; ++i ) {
            InvoiceLine invoiceLine = new InvoiceLine();
            invoiceLine.setSeq(i + 1);

            Item item = new Item();
            item.setCode("000" + i);
            item.setDescription("MODEL AX22" + i);
            item.setPrice(new BigDecimal( (i+1) + "00.00"));
            invoiceLine.setItemCode(item.getCode());
            BigDecimal price = item.getPrice();
            invoiceLine.setPrice(price);

            int qty = i+1;
            invoiceLine.setQty(qty);
            BigDecimal amount = price.multiply(new BigDecimal(qty));
            invoiceLine.setAmount(amount);
            total = total.add(amount);

            invoiceLines.add(invoiceLine);
        }

        invoice.setGross(total);
        invoice.setNet(total);

        this.insertInvoice(invoice);

        return invoice;
    }
}
