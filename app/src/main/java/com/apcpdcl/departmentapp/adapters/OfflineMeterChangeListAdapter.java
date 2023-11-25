package com.apcpdcl.departmentapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.apcpdcl.departmentapp.R;
import com.apcpdcl.departmentapp.activities.OfflineMeterChangeListActivity;
import com.apcpdcl.departmentapp.activities.OfflineMeterChangeRequestFormActivity;
import com.apcpdcl.departmentapp.models.MeterChangeEntryModel;
import com.apcpdcl.departmentapp.utils.Constants;
import com.apcpdcl.departmentapp.utils.Utility;

import java.util.ArrayList;

/**
 * Created by Haseen
 * on 01-03-2018.
 */

public class OfflineMeterChangeListAdapter extends BaseAdapter {
    private ArrayList<MeterChangeEntryModel> meterChangeModels;
    private Context context;
    private final LayoutInflater inflater;

    public OfflineMeterChangeListAdapter(Context context, ArrayList<MeterChangeEntryModel> meterChangeModels) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.meterChangeModels = meterChangeModels;
    }

    @Override
    public int getCount() {
        return meterChangeModels.size();
    }

    @Override
    public Object getItem(int i) {
        return meterChangeModels.get(i);
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
            view = inflater.inflate(R.layout.meter_change_item_layout, viewGroup, false);
            viewHolder = new ViewHolder();
            viewHolder.tv_service_number = (TextView) view.findViewById(R.id.tv_service_number);
            viewHolder.tv_msg = (TextView) view.findViewById(R.id.tv_msg);
            viewHolder.tv_status = (TextView) view.findViewById(R.id.tv_status);


            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        final MeterChangeEntryModel meterChangeModel = meterChangeModels.get(i);
        viewHolder.tv_service_number.setText(meterChangeModel.getServiceNo());
        if (!Utility.isValueNullOrEmpty(meterChangeModel.getStatus()) &&
                meterChangeModel.getStatus().equalsIgnoreCase("F")) {
            viewHolder.tv_status.setText("Failed");
            viewHolder.tv_status.setTextColor(Color.RED);
        } else {
            viewHolder.tv_status.setText(meterChangeModel.getStatus());
        }
        viewHolder.tv_msg.setText(meterChangeModel.getMsg());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, OfflineMeterChangeRequestFormActivity.class);
                intent.putExtra(Constants.FROM, OfflineMeterChangeListActivity.TAG);
                intent.putExtra(Constants.METER_CHANGE_MODEL, meterChangeModel);
                context.startActivity(intent);
            }
        });

        return view;
    }

    private static class ViewHolder {
        TextView tv_service_number;
        TextView tv_status;
        TextView tv_msg;


    }
}
