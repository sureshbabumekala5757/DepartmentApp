package com.apcpdcl.departmentapp.customviews;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.EditText;


import com.apcpdcl.departmentapp.utils.Utility;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;


public class DatePickerFragment extends DialogFragment {
    public DatePickerFragment() {
    }

    private static EditText editText;
    long minDateTime;
    long maxDateTime;
    boolean isMaxOnly = false;
    boolean offline = false;
    public static DatePickerDialog.OnDateSetListener ondate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            if (view.isShown()) {
                String month = String.valueOf(monthOfYear + 1);
                String mMonth = Utility.getMonthFormat(Integer.parseInt(month));
               /* if (month.length() == 1) {
                    mMonth = "0" + month;
                } else {
                    mMonth = month;
                }*/
                String day = String.valueOf(dayOfMonth);
                String mDay;
                if (day.length() == 1) {
                    mDay = "0" + day;
                } else {
                    mDay = day;
                }
                String mDate = mDay + "-" + mMonth + "-" + String.valueOf(year);
                editText.setText(mDate);
            }
        }
    };
    DatePickerDialog.OnDateSetListener ondateSet;
    private int year, month, day;

    @SuppressLint("ValidFragment")
    public DatePickerFragment(EditText editText, String date) {
        DatePickerFragment.editText = editText;
        Date getDate;
        try {
            getDate = Utility.existingUTCFormat.parse(date);
            minDateTime = getDate.getTime();
            maxDateTime = System.currentTimeMillis();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @SuppressLint("ValidFragment")
    public DatePickerFragment(EditText editText, boolean offline) {
        DatePickerFragment.editText = editText;
        this.offline = offline;
    }

    @SuppressLint("ValidFragment")
    public DatePickerFragment(EditText editText, String minDate, String maxDate) {
        isMaxOnly = true;
        DatePickerFragment.editText = editText;
        Date max_Date;
        try {
            max_Date = Utility.existingUTCFormat.parse(maxDate);
            maxDateTime = max_Date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }


    public void setCallBack(DatePickerDialog.OnDateSetListener ondate) {
        ondateSet = ondate;
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        year = args.getInt("year");
        month = args.getInt("month");
        day = args.getInt("day");
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        DatePickerDialog dialog = new DatePickerDialog(getActivity(), ondateSet, year, month, day);
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, 1);
        if (offline) {
            dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        } else {
            dialog.getDatePicker().setMaxDate(maxDateTime);
            if (!isMaxOnly)
                dialog.getDatePicker().setMinDate(minDateTime);
        }
        return dialog;
    }
}