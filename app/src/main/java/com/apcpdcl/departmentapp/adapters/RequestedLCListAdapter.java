package com.apcpdcl.departmentapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.apcpdcl.departmentapp.R;
import com.apcpdcl.departmentapp.activities.RequestedLCListActivity;
import com.apcpdcl.departmentapp.customviews.ExpandableRelativeLayout;
import com.apcpdcl.departmentapp.models.LCRequestModel;
import com.apcpdcl.departmentapp.utils.Utility;

import java.util.ArrayList;

/**
 * Created by Haseen
 * on 01-03-2018.
 */

public class RequestedLCListAdapter extends BaseAdapter {
    private ArrayList<LCRequestModel> mList;
    private final LayoutInflater inflater;
    private RequestedLCListActivity lcRequestListActivity;
    private String name = "";

    public RequestedLCListAdapter(RequestedLCListActivity lcRequestListActivity, ArrayList<LCRequestModel> registrationModels,
                                  String name, String userId) {
        inflater = LayoutInflater.from(lcRequestListActivity);
        this.lcRequestListActivity = lcRequestListActivity;
        this.name = name;
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
            view = inflater.inflate(R.layout.lm_requested_item_layout, viewGroup, false);
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
            viewHolder.tv_return = (TextView) view.findViewById(R.id.tv_return);/*
            viewHolder.ll_feeder = (LinearLayout) view.findViewById(R.id.ll_feeder);
            viewHolder.ll_feeder.setVisibility(View.GONE);*/
            viewHolder.ll_lc = (LinearLayout) view.findViewById(R.id.ll_lc);
            viewHolder.ll_rejected = (LinearLayout) view.findViewById(R.id.ll_rejected);
            viewHolder.tv_return.setVisibility(View.GONE);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        final LCRequestModel lcRequestModel = mList.get(i);
        if (lcRequestModel.getLcTrackingStatus().toLowerCase().contains("pending")) {
            viewHolder.rl_title_layout.setBackgroundColor(lcRequestListActivity.getResources().getColor(R.color.red_color));
            viewHolder.ll_lc.setVisibility(View.GONE);
            viewHolder.ll_rejected .setVisibility(View.GONE);
        } else if (lcRequestModel.getLcTrackingStatus().toLowerCase().equalsIgnoreCase("approved")) {
            viewHolder.ll_lc.setVisibility(View.GONE);
            viewHolder.ll_rejected.setVisibility(View.GONE);
        } else if (lcRequestModel.getLcTrackingStatus().toLowerCase().equalsIgnoreCase("rejected")) {
            viewHolder.ll_lc.setVisibility(View.GONE);
            viewHolder.ll_rejected.setVisibility(View.VISIBLE);
        } else if (lcRequestModel.getLcTrackingStatus().toLowerCase().equalsIgnoreCase("issued")||
                lcRequestModel.getLcTrackingStatus().toLowerCase().equalsIgnoreCase("returned")) {
            viewHolder.ll_lc.setVisibility(View.VISIBLE);
            viewHolder.ll_rejected.setVisibility(View.GONE);
        } else if (lcRequestModel.getLcTrackingStatus().toLowerCase().equalsIgnoreCase("closed")) {
            viewHolder.rl_title_layout.setBackgroundColor(lcRequestListActivity.getResources().getColor(R.color.green_color));
            viewHolder.ll_lc.setVisibility(View.VISIBLE);
            viewHolder.ll_rejected.setVisibility(View.GONE);
        }

        viewHolder.tv_title.setText("LC Request-" + (i + 1) + " (" + lcRequestModel.getLcTrackingStatus() + ")");
        Utility.setExpandableButtonAnimators(viewHolder.expandable_view, viewHolder.iv_title);
        viewHolder.tv_ss_name.setText(lcRequestModel.getSubstationName());
        viewHolder.tv_feeder.setText(lcRequestModel.getFeederName());
        viewHolder.tv_lc_req.setText(lcRequestModel.getLcRequestedName());
        viewHolder.tv_req_desg.setText(lcRequestModel.getLcRequestedDesg());
        viewHolder.tv_date.setText(lcRequestModel.getLcRequestedDate());
        viewHolder.tv_from_time.setText(lcRequestModel.getLcFromTime());
        viewHolder.tv_to_time.setText(lcRequestModel.getLcToTime());
        viewHolder.tv_reason.setText(lcRequestModel.getReasonForLc());
        if(!Utility.isValueNullOrEmpty(lcRequestModel.getLcNo())){
            viewHolder.tv_lc_no.setText(lcRequestModel.getLcNo().substring(13, 17));
        }
        viewHolder.tv_issued_by.setText(lcRequestModel.getLcIssuedName());
        viewHolder.tv_lc_issued_desig.setText("Shift Operator");

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
        TextView tv_lc_no;
        TextView tv_issued_by;
        TextView tv_lc_issued_desig;
        TextView tv_return;
      //  LinearLayout ll_feeder;
        LinearLayout ll_lc;
        LinearLayout ll_rejected;
    }

}
