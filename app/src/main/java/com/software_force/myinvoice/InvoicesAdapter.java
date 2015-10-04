package com.software_force.myinvoice;

/**
 * Created by Matthew So on 10/4/2015.
 */
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class InvoicesAdapter extends CursorAdapter {
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    public InvoicesAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        mContext = context;

        mLayoutInflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View v = mLayoutInflater.inflate(R.layout.invoice_item, parent, false);
        return v;
    }

    /**
     * @author Matthew So 2015-10-04
     *
     * @param   v
     *          The view in which the elements we set up here will be displayed.
     *
     * @param   context
     *          The running context where this ListView adapter will be active.
     *
     * @param   cursor
     *          The Cursor containing the query results we will display.
     */

    @Override
    public void bindView(View v, Context context, Cursor cursor) {
        long id = cursor.getLong(cursor.getColumnIndexOrThrow(DBHelper.INVOICES_COLUMN_ID));
        String member = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.INVOICES_COLUMN_MEMBER));
        Date invoiceDate = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        try {
            invoiceDate = dateFormat.parse(cursor.getString(cursor.getColumnIndex(
                    DBHelper.INVOICES_COLUMN_INVOICEDATE)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        BigDecimal gross =  new BigDecimal(cursor.getString((cursor.getColumnIndex(DBHelper.INVOICES_COLUMN_GROSS))));
        BigDecimal discount =  new BigDecimal(cursor.getString((cursor.getColumnIndex(DBHelper.INVOICES_COLUMN_DISCOUNT))));
        BigDecimal net =  new BigDecimal(cursor.getString((cursor.getColumnIndex(DBHelper.INVOICES_COLUMN_NET))));
        BigDecimal gstgross =  new BigDecimal(cursor.getString((cursor.getColumnIndex(DBHelper.INVOICES_COLUMN_GST))));

        /**
         * Next set the title of the entry.
         */
        TextView id_text = (TextView) v.findViewById(R.id.item_id);
        if (id_text != null) {
            id_text.setText(Long.toString(id));
        }

        TextView invoicedate_text = (TextView) v.findViewById(R.id.item_invoicedate);
        if (invoicedate_text != null) {
            invoicedate_text.setText(invoiceDate.toString());
        }

        TextView member_text = (TextView) v.findViewById(R.id.item_member);
        if (member_text != null) {
            member_text.setText(member);
        }

        TextView net_text = (TextView) v.findViewById(R.id.item_net);
        if (net_text != null) {
            net_text.setText(net.toString());
        }


    }
}