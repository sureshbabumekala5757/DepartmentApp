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

import java.util.Calendar;


public class LCDatePickerFragment extends DialogFragment {
    public LCDatePickerFragment() {
    }

    private static EditText editText;
    private static EditText et_fromTime;
    private static EditText et_toTime;
    private static boolean isSearch = false;

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
                if (!isSearch) {
                    et_fromTime.setText("");
                    et_toTime.setText("");
                }
            }
        }
    };
    DatePickerDialog.OnDateSetListener ondateSet;
    private int year, month, day;


    @SuppressLint("ValidFragment")
    public LCDatePickerFragment(EditText editText, EditText et_fromTime, EditText et_toTime, boolean isLc) {
        LCDatePickerFragment.editText = editText;
        LCDatePickerFragment.et_fromTime = et_fromTime;
        LCDatePickerFragment.et_toTime = et_toTime;
        isSearch = isLc;
    }

    @SuppressLint("ValidFragment")
    public LCDatePickerFragment(EditText editText) {
        LCDatePickerFragment.editText = editText;
        isSearch = true;
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
        if (!isSearch) {
            dialog.getDatePicker().setMinDate(System.currentTimeMillis());
        }
        return dialog;
    }
}