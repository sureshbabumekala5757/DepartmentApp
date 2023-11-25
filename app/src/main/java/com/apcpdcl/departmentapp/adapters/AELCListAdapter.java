package com.apcpdcl.departmentapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.apcpdcl.departmentapp.R;
import com.apcpdcl.departmentapp.activities.AELCRequestListActivity;
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

public class AELCListAdapter extends BaseAdapter {
    private ArrayList<LCRequestModel> mList;
    private final LayoutInflater inflater;
    private AELCRequestListActivity aelcRequestListActivity;
    private String name;
    private String userID;


    public AELCListAdapter(AELCRequestListActivity aelcRequestListActivity, ArrayList<LCRequestModel> registrationModels,
                           String section, String name, String userID) {
        inflater = LayoutInflater.from(aelcRequestListActivity);
        this.aelcRequestListActivity = aelcRequestListActivity;
        this.mList = registrationModels;
        this.name = name;
        this.userID = userID;
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
            view = inflater.inflate(R.layout.ae_lc_request_item_layout, viewGroup, false);
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
            viewHolder.tv_reject = (TextView) view.findViewById(R.id.tv_reject);
            viewHolder.tv_approve = (TextView) view.findViewById(R.id.tv_approve);
            viewHolder.tv_reason = (TextView) view.findViewById(R.id.tv_reason);
            viewHolder.ll_bottom = (LinearLayout) view.findViewById(R.id.ll_bottom);
            viewHolder.cb_ensure = (CheckBox) view.findViewById(R.id.cb_ensure);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        Utility.setExpandableButtonAnimators(viewHolder.expandable_view, viewHolder.iv_title);
        final LCRequestModel lcRequestModel = mList.get(i);
        if (lcRequestModel.getSectionId().equalsIgnoreCase(lcRequestModel.getRequestedSectionId())) {
            viewHolder.tv_title.setText("LC Request-" + (i + 1) + " (" + lcRequestModel.getStatus() + ")");
            if (lcRequestModel.getStatus().equalsIgnoreCase("Pending")) {
                viewHolder.ll_bottom.setVisibility(View.VISIBLE);
                viewHolder.tv_approve.setText("Approve");
                viewHolder.cb_ensure.setVisibility(View.VISIBLE);
                viewHolder.cb_ensure.setEnabled(true);
                viewHolder.cb_ensure.setChecked(false);
                viewHolder.rl_title_layout.setBackgroundColor(aelcRequestListActivity.getResources().getColor(R.color.red_color));
            } else {
                viewHolder.ll_bottom.setVisibility(View.GONE);
                if (lcRequestModel.getStatus().equalsIgnoreCase("Rejected")) {
                    viewHolder.cb_ensure.setVisibility(View.GONE);
                } else {
                    viewHolder.cb_ensure.setVisibility(View.VISIBLE);
                    viewHolder.cb_ensure.setChecked(true);
                    viewHolder.cb_ensure.setEnabled(false);
                }
                viewHolder.rl_title_layout.setBackgroundColor(aelcRequestListActivity.getResources().getColor(R.color.green_color));
            }
        } else {
            if (!Utility.isValueNullOrEmpty(lcRequestModel.getLcApproved2Flag()) && !lcRequestModel.getLcApproved2Flag().equalsIgnoreCase("Pending")) {
                if (userID.contains(lcRequestModel.getRequestedSectionId())) {
                    viewHolder.tv_title.setText("LC Request-" + (i + 1) + " (" + lcRequestModel.getLcApproved2Flag() + ")");
                    viewHolder.cb_ensure.setVisibility(View.VISIBLE);
                    viewHolder.cb_ensure.setChecked(true);
                    viewHolder.cb_ensure.setEnabled(false);
                } else {
                    viewHolder.cb_ensure.setVisibility(View.GONE);
                    viewHolder.tv_title.setText("LC Request-" + (i + 1) + " (" + lcRequestModel.getStatus() + ")");
                }
                viewHolder.ll_bottom.setVisibility(View.GONE);
                viewHolder.rl_title_layout.setBackgroundColor(aelcRequestListActivity.getResources().getColor(R.color.green_color));
            } else {
                if (lcRequestModel.getStatus().equalsIgnoreCase("Pending")
                        || (lcRequestModel.getStatus().equalsIgnoreCase("Forwarded")
                        && userID.contains(lcRequestModel.getRequestedSectionId()))) {
                    viewHolder.tv_title.setText("LC Request-" + (i + 1) + " (" + lcRequestModel.getStatus() + ")");
                    viewHolder.ll_bottom.setVisibility(View.VISIBLE);
                    if (userID.contains(lcRequestModel.getRequestedSectionId())) {
                        viewHolder.tv_approve.setText("Approve");
                        viewHolder.cb_ensure.setEnabled(true);
                        viewHolder.cb_ensure.setVisibility(View.VISIBLE);
                    } else {
                        viewHolder.tv_approve.setText("Forward");
                        viewHolder.cb_ensure.setVisibility(View.GONE);
                    }
                    viewHolder.rl_title_layout.setBackgroundColor(aelcRequestListActivity.getResources().getColor(R.color.red_color));
                } else {
                    viewHolder.tv_title.setText("LC Request-" + (i + 1) + " (" + lcRequestModel.getStatus() + ")");
                    viewHolder.ll_bottom.setVisibility(View.GONE);
                    if (lcRequestModel.getStatus().equalsIgnoreCase("Forwarded") || lcRequestModel.getStatus().equalsIgnoreCase("Rejected")) {
                        viewHolder.cb_ensure.setVisibility(View.GONE);
                    } else {
                        viewHolder.cb_ensure.setVisibility(View.VISIBLE);
                        viewHolder.cb_ensure.setChecked(true);
                        viewHolder.cb_ensure.setEnabled(false);
                    }
                    viewHolder.rl_title_layout.setBackgroundColor(aelcRequestListActivity.getResources().getColor(R.color.green_color));
                }
            }
        }
        viewHolder.tv_ss_name.setText(lcRequestModel.getSubstationName());
        viewHolder.tv_feeder.setText(lcRequestModel.getFeederName());
        viewHolder.tv_lc_req.setText(lcRequestModel.getLcRequestedName());
        viewHolder.tv_req_desg.setText(lcRequestModel.getLcRequestedDesg());
        viewHolder.tv_date.setText(lcRequestModel.getLcRequestedDate());
        viewHolder.tv_from_time.setText(lcRequestModel.getLcFromTime());
        viewHolder.tv_to_time.setText(lcRequestModel.getLcToTime());
        viewHolder.tv_reason.setText(lcRequestModel.getReasonForLc());
        viewHolder.rl_title_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.expandable_view.toggle();
            }
        });
        viewHolder.tv_reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utility.isNetworkAvailable(aelcRequestListActivity)) {
                    getJSON("REJECTED", lcRequestModel);
                } else {
                    Utility.showCustomOKOnlyDialog(aelcRequestListActivity,
                            Utility.getResourcesString(aelcRequestListActivity,
                                    R.string.no_internet));
                }

            }
        });
        viewHolder.tv_approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Utility.isNetworkAvailable(aelcRequestListActivity)) {
                    if (userID.contains(lcRequestModel.getRequestedSectionId())) {
                        if (viewHolder.cb_ensure.isChecked()) {
                            getJSON("APPROVED", lcRequestModel);
                        } else {
                            Utility.showCustomOKOnlyDialog(aelcRequestListActivity, "Please Confirm there is no back feeding.");
                        }
                    } else {
                        getJSON("FORWARDED", lcRequestModel);
                    }
                } else {
                    Utility.showCustomOKOnlyDialog(aelcRequestListActivity,
                            Utility.getResourcesString(aelcRequestListActivity,
                                    R.string.no_internet));
                }
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
        TextView tv_reject;
        TextView tv_approve;
        LinearLayout ll_bottom;
        CheckBox cb_ensure;
    }

    private void getJSON(String status, LCRequestModel lcRequestModel) {
        JSONObject jsonObject = new JSONObject();
        try {
            if (status.equalsIgnoreCase("APPROVED")
                    && !lcRequestModel.getSectionId().equalsIgnoreCase(lcRequestModel.getRequestedSectionId())) {
                jsonObject.put("lcTrackingId", lcRequestModel.getLcTrackingId());
                jsonObject.put("lcApproved2", userID);
                jsonObject.put("lcApproved2Id", userID);
                jsonObject.put("lcApproved2Name", name);
                jsonObject.put("lcApproved2Desg", "AE");
                jsonObject.put("lcApproved2Ip", Utility.getSharedPrefStringData(aelcRequestListActivity, Constants.IMEI_NUMBER));
                jsonObject.put("lcApproved2Flag", status);
                jsonObject.put("backFeeding", "Y");
                jsonObject.put("lmMobileNo", lcRequestModel.getLmMobileNo());
                jsonObject.put("ssoMobileNo", lcRequestModel.getSsoMobileNo());
                jsonObject.put("sectionMobileNo", lcRequestModel.getSectionMobileNo());
                jsonObject.put("requestedSectionMobileNo", lcRequestModel.getRequestedSectionMobileNo());
                jsonObject.put("feederName", lcRequestModel.getFeederName());
                jsonObject.put("lcRequestedDate", lcRequestModel.getLcRequestedDate());
                jsonObject.put("lcFromTime", lcRequestModel.getLcFromTime());
                jsonObject.put("lcToTime", lcRequestModel.getLcToTime());
                jsonObject.put("feederCode", lcRequestModel.getFeederCode());
            } else {
                jsonObject.put("lcTrackingId", lcRequestModel.getLcTrackingId());
                jsonObject.put("lcApproved1", userID);
                jsonObject.put("lcApproved1Id", userID);
                jsonObject.put("lcApproved1Name", name);
                jsonObject.put("lcApproved1Desg", "AE");
                jsonObject.put("lcApproved1Ip", Utility.getSharedPrefStringData(aelcRequestListActivity, Constants.IMEI_NUMBER));
                jsonObject.put("status", status);
                jsonObject.put("lmMobileNo", lcRequestModel.getLmMobileNo());
                jsonObject.put("ssoMobileNo", lcRequestModel.getSsoMobileNo());
                jsonObject.put("sectionMobileNo", lcRequestModel.getSectionMobileNo());
                jsonObject.put("requestedSectionMobileNo", lcRequestModel.getRequestedSectionMobileNo());
                if (status.equalsIgnoreCase("APPROVED")) {
                    jsonObject.put("backFeeding", "Y");
                }
                jsonObject.put("feederName", lcRequestModel.getFeederName());
                jsonObject.put("lcRequestedDate", lcRequestModel.getLcRequestedDate());
                jsonObject.put("lcFromTime", lcRequestModel.getLcFromTime());
                jsonObject.put("lcToTime", lcRequestModel.getLcToTime());
                jsonObject.put("feederCode", lcRequestModel.getFeederCode());
            }
            aelcRequestListActivity.postUpdateStatus(lcRequestModel.getLcTrackingId(), status, jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
