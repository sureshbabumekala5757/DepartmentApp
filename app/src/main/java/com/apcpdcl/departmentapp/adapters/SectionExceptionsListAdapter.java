package com.apcpdcl.departmentapp.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apcpdcl.departmentapp.R;
import com.apcpdcl.departmentapp.models.ExceptionReportModel;

import java.util.ArrayList;

/**
 * Created by Haseen
 * on 01-03-2018.
 */

public class SectionExceptionsListAdapter extends BaseAdapter {
    private ArrayList<ExceptionReportModel> mList;
    private final LayoutInflater inflater;
    private Activity context;
    private boolean mShow;

    public SectionExceptionsListAdapter(Activity context, ArrayList<ExceptionReportModel> registrationModels, boolean show) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        mShow = show;
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
            view = inflater.inflate(R.layout.ero_item_layout, viewGroup, false);
            viewHolder = new ViewHolder();
            viewHolder.tv_serial = (TextView) view.findViewById(R.id.tv_serial);
            viewHolder.tv_serial_t = (TextView) view.findViewById(R.id.tv_serial_t);
            viewHolder.tv_name = (TextView) view.findViewById(R.id.tv_name);
            viewHolder.tv_name_t = (TextView) view.findViewById(R.id.tv_name_t);
            viewHolder.tv_status_lt = (TextView) view.findViewById(R.id.tv_status_lt);
            viewHolder.tv_status_lt_t = (TextView) view.findViewById(R.id.tv_status_lt_t);
            viewHolder.tv_status_gt = (TextView) view.findViewById(R.id.tv_status_gt);
            viewHolder.tv_status_gt_t = (TextView) view.findViewById(R.id.tv_status_gt_t);
            viewHolder.tv_mtr = (TextView) view.findViewById(R.id.tv_mtr);
            viewHolder.tv_balance = (TextView) view.findViewById(R.id.tv_balance);
            viewHolder.ll_five = (LinearLayout) view.findViewById(R.id.ll_five);
            viewHolder.ll_three = (LinearLayout) view.findViewById(R.id.ll_three);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        final ExceptionReportModel exceptionReportModel = mList.get(i);

        if (mShow){
            viewHolder.ll_five.setVisibility(View.VISIBLE);
            viewHolder.ll_three.setVisibility(View.GONE);
            viewHolder.tv_serial.setText(""+(i+1));
            viewHolder.tv_name.setText(exceptionReportModel.getSection());
            viewHolder.tv_status_lt.setText(exceptionReportModel.getLS_Status());
            viewHolder.tv_status_gt.setText(exceptionReportModel.getGS_Status());
            viewHolder.tv_mtr.setText(exceptionReportModel.getMeter_Replaced());
            viewHolder.tv_balance.setText(exceptionReportModel.getBalance());
        }else {
            viewHolder.ll_five.setVisibility(View.GONE);
            viewHolder.ll_three.setVisibility(View.VISIBLE);
            viewHolder.tv_serial_t.setText(""+(i+1));
            viewHolder.tv_name_t.setText(exceptionReportModel.getSection());
            viewHolder.tv_status_lt_t.setText(exceptionReportModel.getLS_Status());
            viewHolder.tv_status_gt_t.setText(exceptionReportModel.getGS_Status());
        }
        return view;
    }


    private static class ViewHolder {
        TextView tv_serial;
        TextView tv_serial_t;
        TextView tv_name;
        TextView tv_name_t;
        TextView tv_status_lt;
        TextView tv_status_lt_t;
        TextView tv_status_gt;
        TextView tv_status_gt_t;
        TextView tv_mtr;
        TextView tv_balance;
        LinearLayout ll_three;
        LinearLayout ll_five;
    }
}
