package com.software_force.myinvoice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class InvoiceEditActivity extends AppCompatActivity {

    private  EditText edit_invoiceDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_edit);
        edit_invoiceDate = (EditText) findViewById(R.id.invoiceDate);

        setInvoiceDate(new Date());

    }

    private void  setInvoiceDate(Date date){
        java.text.DateFormat formatter = android.text.format.DateFormat.getDateFormat(this);

        edit_invoiceDate.setText(formatter.format(date));

    }
}
