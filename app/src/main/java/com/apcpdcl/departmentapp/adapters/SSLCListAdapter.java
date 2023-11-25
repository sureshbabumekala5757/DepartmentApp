package com.apcpdcl.departmentapp.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.apcpdcl.departmentapp.R;
import com.apcpdcl.departmentapp.activities.LCRequestListActivity;
import com.apcpdcl.departmentapp.models.LCRequestModel;
import com.apcpdcl.departmentapp.utils.Constants;
import com.apcpdcl.departmentapp.utils.Utility;
import com.apcpdcl.departmentapp.customviews.ExpandableRelativeLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Haseen
 * on 01-03-2018.
 */

public class SSLCListAdapter extends BaseAdapter {
    private ArrayList<LCRequestModel> mList;
    private final LayoutInflater inflater;
    private LCRequestListActivity lcRequestListActivity;
    private String name = "";
    private String userId = "";

    public SSLCListAdapter(LCRequestListActivity lcRequestListActivity, ArrayList<LCRequestModel> registrationModels,
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
            view = inflater.inflate(R.layout.lc_request_item_layout, viewGroup, false);
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
            viewHolder.et_lc_no = (EditText) view.findViewById(R.id.et_lc_no);
            viewHolder.tv_issued_by = (TextView) view.findViewById(R.id.tv_issued_by);
            viewHolder.et_issued_by = (EditText) view.findViewById(R.id.et_issued_by);
            viewHolder.tv_lc_issued_desig = (TextView) view.findViewById(R.id.tv_lc_issued_desig);
            viewHolder.tv_confirm = (TextView) view.findViewById(R.id.tv_confirm);
            viewHolder.cb_breaker = (CheckBox) view.findViewById(R.id.cb_breaker);
            viewHolder.cb_earthing = (CheckBox) view.findViewById(R.id.cb_earthing);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        final LCRequestModel lcRequestModel = mList.get(i);
        if (lcRequestModel.getLcIssuedFlag1().equalsIgnoreCase("Pending")) {
            viewHolder.tv_confirm.setVisibility(View.VISIBLE);
            viewHolder.et_lc_no.setFocusable(true);
            viewHolder.et_lc_no.setCursorVisible(true);
            viewHolder.et_issued_by.setVisibility(View.VISIBLE);
            viewHolder.tv_issued_by.setVisibility(View.GONE);
            viewHolder.et_issued_by.setText(name);
            viewHolder.et_lc_no.setText(lcRequestModel.getLcNo().substring(13,17));
            viewHolder.cb_breaker.setChecked(false);
            viewHolder.cb_breaker.setEnabled(true);  viewHolder.cb_earthing.setChecked(false);
            viewHolder.cb_earthing.setEnabled(true);
            viewHolder.rl_title_layout.setBackgroundColor(lcRequestListActivity.getResources().getColor(R.color.red_color));
        } else {
            viewHolder.tv_confirm.setVisibility(View.GONE);
            viewHolder.et_issued_by.setVisibility(View.GONE);
            viewHolder.tv_issued_by.setVisibility(View.VISIBLE);
            viewHolder.et_lc_no.setText(lcRequestModel.getLcNo().substring(13,17));
            viewHolder.et_lc_no.setFocusable(false);
            viewHolder.et_lc_no.setBackgroundColor(Color.TRANSPARENT);
            viewHolder.et_lc_no.setCursorVisible(false);
            viewHolder.cb_breaker.setChecked(true);
            viewHolder.cb_breaker.setEnabled(false);  viewHolder.cb_earthing.setChecked(true);
            viewHolder.cb_earthing.setEnabled(false);
            viewHolder.rl_title_layout.setBackgroundColor(lcRequestListActivity.getResources().getColor(R.color.green_color));
        }
        viewHolder.tv_title.setText("LC Request-" + (i + 1) + " (" + lcRequestModel.getLcIssuedFlag1() + ")");
        Utility.setExpandableButtonAnimators(viewHolder.expandable_view, viewHolder.iv_title);
        viewHolder.tv_ss_name.setText(lcRequestModel.getSubstationName());
        viewHolder.tv_feeder.setText(lcRequestModel.getFeederName());
        viewHolder.tv_lc_req.setText(lcRequestModel.getLcRequestedName());
        viewHolder.tv_req_desg.setText(lcRequestModel.getLcRequestedDesg());
        viewHolder.tv_date.setText(lcRequestModel.getLcRequestedDate());
        viewHolder.tv_from_time.setText(lcRequestModel.getLcFromTime());
        viewHolder.tv_to_time.setText(lcRequestModel.getLcToTime());
        viewHolder.tv_reason.setText(lcRequestModel.getReasonForLc());
        viewHolder.tv_issued_by.setText(name);
        viewHolder.tv_lc_issued_desig.setText("Shift Operator");
        viewHolder.tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utility.isValueNullOrEmpty(viewHolder.et_lc_no.getText().toString())) {
                    Utility.showCustomOKOnlyDialog(lcRequestListActivity, "Please Enter LC Number");
                } else if (Utility.isValueNullOrEmpty(viewHolder.et_issued_by.getText().toString())) {
                    Utility.showCustomOKOnlyDialog(lcRequestListActivity, "Please Enter Name");
                } else if (!viewHolder.cb_breaker.isChecked()) {
                    Utility.showCustomOKOnlyDialog(lcRequestListActivity, "Please Confirm Breaker Opened");
                } else if (!viewHolder.cb_earthing.isChecked()) {
                    Utility.showCustomOKOnlyDialog(lcRequestListActivity, "Please Confirm Earth rod Provided");
                } else {
                    if (Utility.isNetworkAvailable(lcRequestListActivity)) {
                        getJSON(viewHolder.et_issued_by.getText().toString(), lcRequestModel);
                    } else {
                        Utility.showCustomOKOnlyDialog(lcRequestListActivity,
                                Utility.getResourcesString(lcRequestListActivity,
                                        R.string.no_internet));
                    }
                }
            }
        });
        viewHolder.rl_title_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.expandable_view.toggle();
            }
        });
        return view;
    }

    private static class ViewHolder {
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
        EditText et_lc_no;
        EditText et_issued_by;
        TextView tv_issued_by;
        TextView tv_lc_issued_desig;
        TextView tv_confirm;
        CheckBox cb_breaker;
        CheckBox cb_earthing;
    }

    private void getJSON( String lc_name, LCRequestModel lcRequestModel) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("lcTrackingId", lcRequestModel.getLcTrackingId());
            jsonObject.put("lcNo", lcRequestModel.getLcNo());
           /* jsonObject.put("lcIssuedDate", Utility.nowDate());
            jsonObject.put("lcIssuedTime", Utility.nowTime());*/
            jsonObject.put("lcIssuedId", userId);
            jsonObject.put("lcIssuedName", lc_name);
            jsonObject.put("lcIssuedDesg", "SSO");
            jsonObject.put("lcIssuedIp", Utility.getSharedPrefStringData(lcRequestListActivity, Constants.IMEI_NUMBER));
            jsonObject.put("lcIssuedFlag1", "ISSUED");
            jsonObject.put("breakerOpened", "Y");
            jsonObject.put("earthrodProvided", "Y");
            jsonObject.put("lmMobileNo", lcRequestModel.getLmMobileNo());
            jsonObject.put("ssoMobileNo", lcRequestModel.getSsoMobileNo());
            jsonObject.put("sectionMobileNo", lcRequestModel.getSectionMobileNo());
            jsonObject.put("requestedSectionMobileNo", lcRequestModel.getRequestedSectionMobileNo());
            jsonObject.put("status", lcRequestModel.getStatus());
            jsonObject.put("feederName", lcRequestModel.getFeederName());
            jsonObject.put("lcRequestedDate", lcRequestModel.getLcRequestedDate());
            jsonObject.put("lcFromTime", lcRequestModel.getLcFromTime());
            jsonObject.put("lcToTime", lcRequestModel.getLcToTime());
            jsonObject.put("feederCode", lcRequestModel.getFeederCode());
            jsonObject.put("lcRequestedId", lcRequestModel.getLcRequestedId());
            lcRequestListActivity.postUpdateStatus(lcRequestModel.getLcTrackingId(), jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
