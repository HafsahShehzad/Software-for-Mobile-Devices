package com.example.apple.tabssample.Fragments;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import com.example.apple.tabssample.Activities.CreateEvent;

import java.util.Calendar;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

   public int val;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);


        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        CreateEvent callingActivity = (CreateEvent) getActivity();
        if(val == 0) {

            callingActivity.onUserSelectValueStartTime(hourOfDay, minute);

        }
        else if(val == 1) {

            callingActivity.onUserSelectValueEndTime(hourOfDay, minute);

        }
        dismiss();
    }

    public void setVal(int v)
    {
        val = v;
    }

}
