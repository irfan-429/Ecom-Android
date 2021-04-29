package com.maq.ecom.helper;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by irfan A. on 07/09/2020.
 */

public class CustomDatePicker implements DatePickerDialog.OnDateSetListener  {

    private TextView view;

    public CustomDatePicker(Context context, TextView view) {
        this.view =view;
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        DatePickerDialog dialog = new DatePickerDialog(context, this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
//        dialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
        dialog.show();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        updateDisplay(day, month + 1, year);
    }

    @SuppressLint("SetTextI18n")
    private void updateDisplay(int day, int month, int year) {
        view.setText(year + "-" + month + "-" + day);

    }
}
