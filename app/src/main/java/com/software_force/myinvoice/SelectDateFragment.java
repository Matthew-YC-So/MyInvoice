package com.software_force.myinvoice;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Matthew So on 10/6/2015.
 */
public class SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private EditText editDate;

    public SelectDateFragment(EditText editDate){
        this.editDate = editDate;

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        int yy = calendar.get(Calendar.YEAR);
        int mm = calendar.get(Calendar.MONTH);
        int dd = calendar.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(getActivity(), this, yy, mm, dd);
    }

    public void onDateSet(DatePicker view, int yy, int mm, int dd){
        populateSetDate(yy, mm+1, dd);
    }

    public void populateSetDate(int year, int month, int day) {
        java.text.DateFormat formatter = android.text.format.DateFormat.getDateFormat(this.getContext());
        Date date = new Date( year, month, day);
        this.editDate.setText(formatter.format(date));
    }

}
