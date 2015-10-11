package com.software_force.myinvoice;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.software_force.myinvoice.models.Invoice;
import com.software_force.myinvoice.models.InvoiceLine;
import com.software_force.myinvoice.models.Item;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class InvoiceEditActivity extends AppCompatActivity {

    private  EditText edit_invoiceDate;
    private java.text.DateFormat dateFormatter;
    private TableLayout table;
    private int textSize;
    private int textColor;
    private int headerTextColor;
    private SimpleDateFormat dateFormat;
    private TextView grossView;
    private EditText discountView;
    private TextView netView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_edit);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        table = (TableLayout)findViewById(R.id.invoice_details);

        dateFormatter = android.text.format.DateFormat.getDateFormat(this);
        edit_invoiceDate = (EditText) findViewById(R.id.invoiceDate);
        setInvoiceDate(new Date());
        ImageButton selectDate =  (ImageButton) findViewById(R.id.selectInvoiceDate);
        selectDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showStartDateDialog(v);
            }
        });

        textSize =  (int) ( this.getResources().getDimension( R.dimen.text_size)/ this.getResources().getDisplayMetrics().density );
        textColor =  ContextCompat.getColor(this, R.color.colorrText);
        int headerTextColor =  ContextCompat.getColor(this, R.color.colorHeaderText);
        dateFormat = new    SimpleDateFormat("yyyy-MM-dd");

        grossView = (TextView) this.findViewById(R.id.gross);
        discountView = (EditText) this.findViewById(R.id.discount);
        netView = (TextView) this.findViewById(R.id.net);

        discountView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                InvoiceEditActivity.this.recalculateDiscount();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_invoice_edit, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        } else if (id == R.id.action_add) {
            showSelectItems();
            return true;
        } else if (id == R.id.action_save)  {
            this.saveData();
            Intent output = new Intent();
            setResult(RESULT_OK, output);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    final static int SELECT_ITEMS = 1;
    final static String SELECTED_ITEMS = "SELECTED";

    private void  showSelectItems() {
        Intent intent = new Intent(this, ItemsActivity.class);
        // startActivity(intent);
        startActivityForResult(intent, SELECT_ITEMS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SELECT_ITEMS && resultCode == RESULT_OK && data != null) {
            final ArrayList items = data.getParcelableArrayListExtra(SELECTED_ITEMS);
            for (int i = 0 ; i < items.size(); ++i){
                Item item = (Item)items.get(i);
                addTableRow(item);
            }
        }
    }


    private void addTableRow(Item dataItem){
        TableRow tr;

        BigDecimal gross = new BigDecimal(grossView.getText().toString());
        int foundIndex = findDetailIndex(dataItem.getCode());
        if (foundIndex > -1) {
            tr = (TableRow)this.table.getChildAt(foundIndex);
            TextView qtyTextView = (TextView) tr.getChildAt(3);
            int qty = Integer.parseInt( qtyTextView.getText().toString());
            TextView priceTextView = (TextView) tr.getChildAt(2);
            BigDecimal price = new BigDecimal( priceTextView.getText().toString());
            BigDecimal amt_bf = BigDecimal.valueOf(qty).multiply(price) ;
            ++qty;
            qtyTextView.setText(Integer.toString(qty));
            BigDecimal amt_delta = BigDecimal.valueOf(qty).multiply(price).subtract(amt_bf) ;
            gross = gross.add(amt_delta);
        }
        else {
            tr = new TableRow(this);

            TextView b = new TextView(this);
            String str = dataItem.getCode();
            b.setText(str);
            b.setTextColor(textColor);
            b.setTextSize(textSize);
            b.setGravity(Gravity.CENTER_HORIZONTAL);
            tr.addView(b);

            TextView b1 = new TextView(this);
            b1.setPadding(10, 0, 0, 0);
            b1.setTextSize(textSize);
            String str1 = dataItem.getDescription();
            b1.setText(str1);
            b1.setTextColor(textColor);
            tr.addView(b1);

            TextView b3 = new TextView(this);
            b3.setPadding(10, 0, 0, 0);
            b3.setText(dataItem.getPrice().toString());
            b3.setTextColor(textColor);
            b3.setTextSize(textSize);
            b3.setGravity(Gravity.RIGHT);
            tr.addView(b3);

            TextView b4 = new TextView(this);
            b4.setPadding(10, 0, 0, 0);
            b4.setText("1");
            b4.setTextColor(textColor);
            b4.setTextSize(textSize);
            b4.setGravity(Gravity.RIGHT);
            tr.addView(b4);

            ImageButton b5 = new ImageButton(this);
            b5.setPadding(10, 0, 0, 0);
            b5.setImageResource(R.drawable.ic_action_clear);
            b5.setBackgroundColor(Color.TRANSPARENT);

            b5.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View v) {
                                          TableRow tableRow = (TableRow) v.getParent();
                                          ((TableLayout) tableRow.getParent()).removeView(tableRow);
                                          BigDecimal gross = new BigDecimal(InvoiceEditActivity.this.grossView.getText().toString());
                                          TextView qtyTextView = (TextView) tableRow.getChildAt(3);
                                          int qty = Integer.parseInt( qtyTextView.getText().toString());
                                          TextView priceTextView = (TextView) tableRow.getChildAt(2);
                                          BigDecimal price = new BigDecimal(priceTextView.getText().toString());
                                          BigDecimal amt  = BigDecimal.valueOf(qty).multiply(price) ;
                                          InvoiceEditActivity.this.grossView.setText(gross.subtract(amt).toString());
                                          InvoiceEditActivity.this.recalculateDiscount();
                                      }
                                  }
            );

            tr.addView(b5);
            
            this.table.addView(tr);

            final View vline1 = new View(this);
            vline1.setLayoutParams(new
                    TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, 1));
            vline1.setBackgroundColor(Color.WHITE);
            this.table.addView(vline1);  // add line below each row

            gross = gross.add(dataItem.getPrice()); // Qty = 1
        }

        grossView.setText(gross.toString());
        this.recalculateDiscount();
    }

    private void recalculateDiscount() {
        BigDecimal gross = new BigDecimal(grossView.getText().toString());
        BigDecimal discount = BigDecimal.ZERO ;
        try {
            discount = new BigDecimal(discountView.getText().toString());
        }
        catch(Exception e) {
            discount = BigDecimal.ZERO ;
        }

        BigDecimal netAmt = BigDecimal.valueOf(100).subtract(discount).divide(BigDecimal.valueOf(100)).multiply(gross);
        this.netView.setText(netAmt.toString());
    }

    private int findDetailIndex(String itemCode) {
        int foundIndex = -1;
        for (int i = 1; i < this.table.getChildCount(); ++i) {
            View view = this.table.getChildAt(i);
            if (view instanceof TableRow) {
                TableRow row = (TableRow) view;
                String code = ((TextView) row.getChildAt(0)).getText().toString();
                if (code.equals(itemCode)) {
                    foundIndex = i;
                    break;
                }
            }
        }
        return foundIndex ;
    }

    private void saveData() {
        Invoice invoice = new Invoice();
        InvoicesDataSource dataSource = null;
        try {
            dataSource = new InvoicesDataSource(this);
            dataSource.open();

            invoice.setInvoiceDate(dateFormat.parse(edit_invoiceDate.getText().toString()));
            invoice.setMember(((EditText) findViewById(R.id.member)).getText().toString());
            invoice.setGross(new BigDecimal(grossView.getText().toString()));
            String discount_str =  discountView.getText().toString();
            if (discount_str == "") {
                discount_str = "0";
            }
            invoice.setDiscount( new BigDecimal(discount_str) );
            invoice.setNet(new BigDecimal(((TextView) findViewById(R.id.net)).getText().toString()));
            invoice.setGst(BigDecimal.ZERO);

            // Invoice details
            for (int i = 1; i < this.table.getChildCount(); ++i) {
                View view = this.table.getChildAt(i);
                if (view instanceof TableRow) {
                    InvoiceLine invoiceLine = new InvoiceLine();
                    TableRow row = (TableRow) view;
                    String code = ((TextView) row.getChildAt(0)).getText().toString();
                    invoiceLine.setSeq(i);
                    invoiceLine.setItemCode(code);
                    invoiceLine.setPrice(new BigDecimal(((TextView) row.getChildAt(2)).getText().toString()));
                    invoiceLine.setQty((Integer.valueOf(((TextView) row.getChildAt(3)).getText().toString())));
                    invoiceLine.setAmount(invoiceLine.getPrice().multiply(BigDecimal.valueOf(invoiceLine.getQty())));

                    invoice.getInvoiceLines().add(invoiceLine);
                }
            }

            dataSource.insertInvoice(invoice);
            Toast.makeText(this, R.string.message_saved , Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            Log.e(this.getClass().getName(), e.getMessage());
        }
        finally {
            if (dataSource != null)
                dataSource.close();
        }

    }


    private void  setInvoiceDate(Date date){
        edit_invoiceDate.setText(dateFormatter.format(date));
    }

    public void showStartDateDialog(View v){
        SelectDateFragment dialogFragment = new SelectDateFragment();

        int yy;
        int mm;
        int dd;

        Calendar calender = Calendar.getInstance();
        String dt = edit_invoiceDate.getText().toString();
        try {
            Date date = dateFormatter.parse(dt);
            calender.setTime(date);
        } catch (ParseException ignored) {
        }

        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        dialogFragment.setArguments(args);
        /**
         * Set Call back to capture selected date
         */
        dialogFragment.setCallBack(ondate);
        dialogFragment.show(this.getFragmentManager(), "Date Picker");
    }

    DatePickerDialog.OnDateSetListener ondate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.MONTH, monthOfYear);
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            Date dateRepresentation = cal.getTime();
            setInvoiceDate(dateRepresentation);
        }
    };

    // DatePicker dialog
    static public class SelectDateFragment extends DialogFragment {

        private DatePickerDialog.OnDateSetListener ondateSet;
        private int year, month, day;

        public SelectDateFragment() {
        }

        public void setCallBack(DatePickerDialog.OnDateSetListener ondate) {
            this.ondateSet = ondate;
        }

        @Override
        public void setArguments(Bundle args) {
            super.setArguments(args);
            year = args.getInt("year");
            month = args.getInt("month");
            day = args.getInt("day");
        }


        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return new DatePickerDialog(getActivity(), this.ondateSet, year, month, day);
        }
     }
}
