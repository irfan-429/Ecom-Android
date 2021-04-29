package com.maq.ecom.helper;

import android.app.TimePickerDialog;
import android.content.Context;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by irfan A. on 07/09/2020.
 */

public class CustomTimePicker implements  TimePickerDialog.OnTimeSetListener {

    private TextView view;

    public CustomTimePicker(Context context, TextView view) {
        this.view =view;
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog dialog = new TimePickerDialog(context, this, hour, minute, true);
        dialog.show();
    }


    @Override
    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
        String time = hourOfDay + ":" + minutes;
//        String newTime = Utils.formatDateTimeFromString(time, "HH:mm", "hh:mm a");

        updateDisplay(time);
    }

    private void updateDisplay(String newTime) {
        view.setText(newTime);
    }
}