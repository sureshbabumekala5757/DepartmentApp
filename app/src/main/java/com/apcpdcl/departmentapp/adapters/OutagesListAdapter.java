package com.apcpdcl.departmentapp.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.apcpdcl.departmentapp.R;
import com.apcpdcl.departmentapp.models.OutagesModel;

import java.util.ArrayList;

/**
 * Created by Haseen
 * on 01-03-2018.
 */

public class OutagesListAdapter extends BaseAdapter {
    private ArrayList<OutagesModel> mList;
    private final LayoutInflater inflater;

    public OutagesListAdapter(Activity context, ArrayList<OutagesModel> registrationModels) {
        inflater = LayoutInflater.from(context);
        this.mList = registrationModels;
    }

    @Override
    public int getCount() {
        // return mList.size();
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
        ViewHolder viewHolder;
        // General ListView optimization code.
        if (view == null) {
            view = inflater.inflate(R.layout.outages_item_layout, viewGroup, false);
            viewHolder = new ViewHolder();
            viewHolder.tv_serial = (TextView) view.findViewById(R.id.tv_serial);
            viewHolder.tv_ss_name = (TextView) view.findViewById(R.id.tv_ss_name);
            viewHolder.tv_feeder = (TextView) view.findViewById(R.id.tv_feeder);
            viewHolder.tv_outage = (TextView) view.findViewById(R.id.tv_outage);
            viewHolder.tv_restore = (TextView) view.findViewById(R.id.tv_restore);
            viewHolder.tv_duration = (TextView) view.findViewById(R.id.tv_duration);
            viewHolder.tv_reason = (TextView) view.findViewById(R.id.tv_reason);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        final OutagesModel outagesModel = mList.get(i);
        viewHolder.tv_serial.setText("" + (i + 1));
        viewHolder.tv_ss_name.setText(outagesModel.getSubstationName());
        viewHolder.tv_feeder.setText(outagesModel.getFeederName() + " - " + outagesModel.getFeederCode());
        viewHolder.tv_outage.setText(outagesModel.getScheduledOutageTime());
        viewHolder.tv_restore.setText(outagesModel.getScheduledRestoreTime());
        viewHolder.tv_duration.setText(outagesModel.getDurationInMins());
        viewHolder.tv_reason.setText(outagesModel.getOutageReason());


        return view;
    }

    private static class ViewHolder {
        TextView tv_serial;
        TextView tv_ss_name;
        TextView tv_feeder;
        TextView tv_outage;
        TextView tv_restore;
        TextView tv_duration;
        TextView tv_reason;
    }
}
