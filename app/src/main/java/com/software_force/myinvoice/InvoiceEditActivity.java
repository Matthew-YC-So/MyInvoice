package com.software_force.myinvoice;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class InvoiceEditActivity extends AppCompatActivity {

    private  EditText edit_invoiceDate;
    private java.text.DateFormat dateFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_edit);

        dateFormatter = android.text.format.DateFormat.getDateFormat(this);

        edit_invoiceDate = (EditText) findViewById(R.id.invoiceDate);
        setInvoiceDate(new Date());
        ImageButton selectDate =  (ImageButton) findViewById(R.id.selectInvoiceDate);
        selectDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showStartDateDialog(v);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_invoice_edit, menu);
        return super.onCreateOptionsMenu(menu);
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
