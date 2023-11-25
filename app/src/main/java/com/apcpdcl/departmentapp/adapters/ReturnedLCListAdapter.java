package com.apcpdcl.departmentapp.adapters;

import android.app.TimePickerDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.apcpdcl.departmentapp.R;
import com.apcpdcl.departmentapp.activities.LCReturnedListActivity;
import com.apcpdcl.departmentapp.customviews.ExpandableRelativeLayout;
import com.apcpdcl.departmentapp.models.LCRequestModel;
import com.apcpdcl.departmentapp.utils.Constants;
import com.apcpdcl.departmentapp.utils.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Haseen
 * on 01-03-2018.
 */

public class ReturnedLCListAdapter extends BaseAdapter {
    private ArrayList<LCRequestModel> sortedList;
    private ArrayList<LCRequestModel> mList;
    private final LayoutInflater inflater;
    private LCReturnedListActivity lcRequestListActivity;
    private String name = "";
    private String userId = "";
    private int mHour, mMinute;
    private int mFromHour, mFromMinute;


    public ReturnedLCListAdapter(LCReturnedListActivity lcRequestListActivity, ArrayList<LCRequestModel> registrationModels,
                                 String name, String userId) {
        inflater = LayoutInflater.from(lcRequestListActivity);
        this.lcRequestListActivity = lcRequestListActivity;
        this.name = name;
        this.userId = userId;
        this.mList = registrationModels;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ViewHolder viewHolder;
        // General ListView optimization code.
        if (view == null) {
            view = inflater.inflate(R.layout.lm_return_item_layout, viewGroup, false);
            viewHolder = new ViewHolder();
            viewHolder.tv_title = (TextView) view.findViewById(R.id.tv_title);
            viewHolder.iv_title = (ImageView) view.findViewById(R.id.iv_title);
            viewHolder.rl_title_layout = (RelativeLayout) view.findViewById(R.id.rl_title_layout);
            viewHolder.expandable_view = (ExpandableRelativeLayout) view.findViewById(R.id.expandable_view);
            viewHolder.tv_ss_name = (TextView) view.findViewById(R.id.tv_ss_name);
            viewHolder.tv_feeder = (TextView) view.findViewById(R.id.tv_feeder);
            viewHolder.tv_lc_req = (TextView) view.findViewById(R.id.tv_lc_req);
            viewHolder.tv_req_desg = (TextView) view.findViewById(R.id.tv_req_desg);
            viewHolder.tv_date = (TextView) view.findViewById(R.id.tv_date);
            viewHolder.tv_from_time = (TextView) view.findViewById(R.id.tv_from_time);
            viewHolder.tv_to_time = (TextView) view.findViewById(R.id.tv_to_time);
            viewHolder.tv_reason = (TextView) view.findViewById(R.id.tv_reason);
            viewHolder.tv_lc_no = (TextView) view.findViewById(R.id.tv_lc_no);
            viewHolder.tv_issued_by = (TextView) view.findViewById(R.id.tv_issued_by);
            viewHolder.tv_lc_issued_desig = (TextView) view.findViewById(R.id.tv_lc_issued_desig);
            viewHolder.tv_return = (TextView) view.findViewById(R.id.tv_return);
            viewHolder.et_feeder_charge_time = (EditText) view.findViewById(R.id.et_feeder_charge_time);
            viewHolder.ll_feeder = (LinearLayout) view.findViewById(R.id.ll_feeder);
            viewHolder.cb_cleared = (CheckBox) view.findViewById(R.id.cb_cleared);
            viewHolder.cb_cleared.setText("Earthing Removed");
            viewHolder.ll_feeder.setVisibility(View.GONE);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        final LCRequestModel lcRequestModel = mList.get(i);
        if (lcRequestModel.getLcLMtoSSOReturnedFlag3().equalsIgnoreCase("returned")) {
            if (lcRequestModel.getFeederOverLopping().equalsIgnoreCase("Y")) {
                viewHolder.tv_return.setBackgroundColor(lcRequestListActivity.getResources().getColor(R.color.dark_gray));
            } else {
                viewHolder.tv_return.setBackgroundColor(lcRequestListActivity.getResources().getColor(R.color.colorPrimary));
            }
            viewHolder.cb_cleared.setChecked(false);
            viewHolder.cb_cleared.setEnabled(true);
            viewHolder.tv_return.setVisibility(View.VISIBLE);
            viewHolder.rl_title_layout.setBackgroundColor(lcRequestListActivity.getResources().getColor(R.color.red_color));
            viewHolder.et_feeder_charge_time.setFocusable(true);
            viewHolder.et_feeder_charge_time.setCursorVisible(true);
        } else {
            viewHolder.tv_return.setVisibility(View.GONE);
            viewHolder.cb_cleared.setChecked(true);
            viewHolder.cb_cleared.setEnabled(false);
            viewHolder.rl_title_layout.setBackgroundColor(lcRequestListActivity.getResources().getColor(R.color.green_color));
            viewHolder.et_feeder_charge_time.setText(lcRequestModel.getLcFeederChargeTime());
            viewHolder.et_feeder_charge_time.setFocusable(false);
            viewHolder.et_feeder_charge_time.setCursorVisible(false);
        }
        viewHolder.tv_title.setText("LC Returned-" + (i + 1) + " (" + lcRequestModel.getLcLMtoSSOReturnedFlag3() + ")");
        Utility.setExpandableButtonAnimators(viewHolder.expandable_view, viewHolder.iv_title);
        viewHolder.tv_ss_name.setText(lcRequestModel.getSubstationName());
        viewHolder.tv_feeder.setText(lcRequestModel.getFeederName());
        viewHolder.tv_lc_req.setText(lcRequestModel.getLcRequestedName());
        viewHolder.tv_req_desg.setText(lcRequestModel.getLcRequestedDesg());
        viewHolder.tv_date.setText(lcRequestModel.getLcRequestedDate());
        viewHolder.tv_from_time.setText(lcRequestModel.getLcFromTime());
        viewHolder.tv_to_time.setText(lcRequestModel.getLcToTime());
        viewHolder.tv_reason.setText(lcRequestModel.getReasonForLc());
        viewHolder.tv_lc_no.setText(lcRequestModel.getLcNo().substring(13,17));
        viewHolder.tv_issued_by.setText(name);
        viewHolder.tv_lc_issued_desig.setText("Shift Operator");
        viewHolder.tv_return.setText("Close");

        /*Alekhya Changes*/
        viewHolder.tv_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lcRequestModel.getFeederOverLopping().equalsIgnoreCase("Y")) {
                    Utility.showCustomOKOnlyDialog(lcRequestListActivity, "All Lcs are not returned from feeder " + lcRequestModel.getFeederName());
                } else {
                    if (viewHolder.cb_cleared.isChecked()) {
                    if (Utility.isNetworkAvailable(lcRequestListActivity)) {
                        getJSON(lcRequestModel);
                    } else {
                        Utility.showCustomOKOnlyDialog(lcRequestListActivity,
                                Utility.getResourcesString(lcRequestListActivity,
                                        R.string.no_internet));
                    }
                    } else {
                        Utility.showCustomOKOnlyDialog(lcRequestListActivity, "Please Confirm Earth rod Removed.");
                    }
                }
            }
        });/**/

        viewHolder.rl_title_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.expandable_view.toggle();
            }
        });

        /*Alekhya Changes*/
        viewHolder.et_feeder_charge_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lcRequestModel.getLcLMtoSSOReturnedFlag3().equalsIgnoreCase("returned")) {
                    selectFeederChangeTime(viewHolder.et_feeder_charge_time);
                }
            }
        });/**/

        return view;
    }

    public static class ViewHolder {
        RelativeLayout rl_title_layout;
        TextView tv_title;
        ImageView iv_title;
        ExpandableRelativeLayout expandable_view;
        TextView tv_ss_name;
        TextView tv_feeder;
        TextView tv_lc_req;
        TextView tv_req_desg;
        TextView tv_date;
        TextView tv_from_time;
        TextView tv_to_time;
        TextView tv_reason;
        TextView tv_lc_no;
        TextView tv_issued_by;
        TextView tv_lc_issued_desig;
        TextView tv_return;
        EditText et_feeder_charge_time;
        LinearLayout ll_feeder;
        CheckBox cb_cleared;
    }


    private void getJSON(LCRequestModel lcRequestModel) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("lcTrackingId", lcRequestModel.getLcTrackingId());
         /*   jsonObject.put("lcReturnedDate", Utility.nowDate());
            jsonObject.put("lcReturnedTime", Utility.nowTime());*/
            jsonObject.put("lcClosedId", userId);
            jsonObject.put("lcClosedName", name);
            jsonObject.put("lcClosedDesg", "SSO");
            jsonObject.put("lcClosedIp", Utility.getSharedPrefStringData(lcRequestListActivity, Constants.IMEI_NUMBER));
            jsonObject.put("lcLMtoSSOReturnedFlag3", "CLOSED");
            jsonObject.put("earthrodRemoved", "Y");
            jsonObject.put("lmMobileNo", lcRequestModel.getLmMobileNo());
            jsonObject.put("ssoMobileNo", lcRequestModel.getSsoMobileNo());
            jsonObject.put("sectionMobileNo", lcRequestModel.getSectionMobileNo());
            jsonObject.put("requestedSectionMobileNo", lcRequestModel.getRequestedSectionMobileNo());
            jsonObject.put("status", lcRequestModel.getStatus());
            jsonObject.put("feederName", lcRequestModel.getFeederName());
            jsonObject.put("lcRequestedDate", lcRequestModel.getLcRequestedDate());
            jsonObject.put("lcFromTime", lcRequestModel.getLcFromTime());
            jsonObject.put("lcToTime", lcRequestModel.getLcToTime());
            jsonObject.put("lcNo", lcRequestModel.getLcNo());
            jsonObject.put("feederCode", lcRequestModel.getFeederCode());
            jsonObject.put("lcRequestedId", lcRequestModel.getLcRequestedId());
            jsonObject.put("lcRequestedId", lcRequestModel.getLcRequestedId());
            lcRequestListActivity.postUpdateStatus(lcRequestModel.getLcTrackingId(), jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /*Alekhya Changes*/
    private boolean sameDate(EditText et_feedChangeTime) {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat requiredFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
        String date = requiredFormat.format(c.getTimeInMillis());
        return et_feedChangeTime.getText().toString().equalsIgnoreCase(date);
    }/**/

    /*Alekhya Changes*/

    public void selectFeederChangeTime(final EditText et_feederChargeTime) {
        Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(lcRequestListActivity,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        if (!sameDate(et_feederChargeTime)) {
                            mFromHour = hourOfDay;
                            mFromMinute = minute;
                            et_feederChargeTime.setText((hourOfDay < 10 && minute < 10) ? "0" + hourOfDay + ":0" + minute
                                    : (hourOfDay < 10 && minute > 9) ? "0" + hourOfDay + ":" + minute
                                    : (hourOfDay > 9 && minute < 10) ? hourOfDay + ":0" + minute
                                    : hourOfDay + ":" + minute);
                        } else if (hourOfDay > mHour || (hourOfDay == mHour && minute >= mMinute)) {
                            mFromHour = hourOfDay;
                            mFromMinute = minute;
                            et_feederChargeTime.setText((hourOfDay < 10 && minute < 10) ? "0" + hourOfDay + ":0" + minute
                                    : (hourOfDay < 10 && minute > 9) ? "0" + hourOfDay + ":" + minute
                                    : (hourOfDay > 9 && minute < 10) ? hourOfDay + ":0" + minute
                                    : hourOfDay + ":" + minute);
                        } else {
                            Utility.showToastMessage(lcRequestListActivity, "From Time must be greater than or equal to Current Time.");
                        }
                    }
                }, mHour, mMinute, true);
        timePickerDialog.show();
    }/**/

}
