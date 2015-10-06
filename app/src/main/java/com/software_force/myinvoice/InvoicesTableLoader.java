package com.software_force.myinvoice;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.software_force.myinvoice.models.Invoice;

import java.text.SimpleDateFormat;
import java.util.List;


/**
 * Created by Matthew So on 10/5/textSize15.
 */
public class InvoicesTableLoader {

    TableLayout table;
    Context context;
    List<Invoice> data;

    public InvoicesTableLoader(Context context, TableLayout table, List<Invoice> data){
        this.context = context;
        this.table = table;
        this.data = data;
    }


    public void Build() {

        this.table.removeAllViewsInLayout();
        int flag = 1;

        int textSize =  (int) ( context.getResources().getDimension( R.dimen.text_size)/ context.getResources().getDisplayMetrics().density );
        // int textSize =  (int) ( context.getResources().getDimension( R.dimen.text_size) );
        int textColor =  ContextCompat.getColor(context, R.color.colorrText);
        int headerTextColor =  ContextCompat.getColor(context, R.color.colorHeaderText);
        SimpleDateFormat dateFormat = new    SimpleDateFormat("yyyy-MM-dd");

        // when i=-1, loop will display heading of each column
        // then usually data will be display from i=0 to data.length()
        for (int i = -1; i < data.size(); i++) {

            TableRow tr = new TableRow(this.context);

            tr.setLayoutParams(new TableLayout.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.MATCH_PARENT));

            // this will be executed once
            if (flag == 1) {

                TextView b3 = new TextView(this.context);
                b3.setText(context.getString(R.string.invoice_id));
                b3.setTextColor(headerTextColor);
                b3.setTextSize(textSize);
                tr.addView(b3);

                TextView b4 = new TextView(this.context);
                b4.setPadding(10, 0, 0, 0);
                b4.setTextSize(textSize);
                b4.setText(context.getString(R.string.invoice_date));
                b4.setTextColor(headerTextColor);
                tr.addView(b4);

                TextView b5 = new TextView(this.context);
                b5.setPadding(10, 0, 0, 0);
                b5.setText(context.getString(R.string.invoice_member));
                b5.setTextColor(headerTextColor);
                b5.setTextSize(textSize);
                tr.addView(b5);

                TextView b6 = new TextView(this.context);
                b6.setPadding(10, 0, 0, 0);
                b6.setText(context.getString(R.string.invoice_net));
                b6.setTextColor(headerTextColor);
                b6.setTextSize(textSize);
                tr.addView(b6);

                this.table.addView(tr);

                final View vline = new View(this.context);
                vline.setLayoutParams(new
                        TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, 2));
                vline.setBackgroundColor(Color.YELLOW);
                this.table.addView(vline); // add line below heading
                flag = 0;
            } else {

                Invoice dataItem = this.data.get(i);

                TextView b = new TextView(this.context);
                String str = String.valueOf(dataItem.getId());
                b.setText(str);
                b.setTextColor(textColor);
                b.setTextSize(textSize);
                b.setGravity(Gravity.CENTER_HORIZONTAL);
                tr.addView(b);

                TextView b1 = new TextView(this.context);
                b1.setPadding(10, 0, 0, 0);
                b1.setTextSize(textSize);
                String str1 = dateFormat.format(dataItem.getInvoiceDate());
                b1.setText(str1);
                b1.setTextColor(textColor);
                tr.addView(b1);

                TextView b2 = new TextView(this.context);
                b2.setPadding(10, 0, 0, 0);
                String str2 = dataItem.getMember();
                b2.setText(str2);
                b2.setTextColor(textColor);
                b2.setTextSize(textSize);
                tr.addView(b2);

                TextView b3 = new TextView(this.context);
                b3.setPadding(10, 0, 0, 0);
                b3.setText(dataItem.getNet().toString());
                b3.setTextColor(textColor);
                b3.setTextSize(textSize);
                b3.setGravity(Gravity.RIGHT);
                tr.addView(b3);


                this.table.addView(tr);

                final View vline1 = new View(this.context);
                vline1.setLayoutParams(new
                        TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, 1));
                vline1.setBackgroundColor(Color.WHITE);
                this.table.addView(vline1);  // add line below each row
            }
        }

    }
}
