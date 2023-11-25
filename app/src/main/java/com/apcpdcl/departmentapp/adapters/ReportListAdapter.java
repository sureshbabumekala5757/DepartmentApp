package com.apcpdcl.departmentapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.apcpdcl.departmentapp.R;
import com.apcpdcl.departmentapp.models.ReportsList;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Admin on 19-02-2018.
 */

public class ReportListAdapter extends BaseAdapter {

    // Declare Variables
    Context mContext;
    LayoutInflater inflater;
    private List<ReportsList> reportsList = null;
    private ArrayList<ReportsList> arraylist;

    public ReportListAdapter(Context context, List<ReportsList> reportsList) {
        mContext = context;
        this.reportsList = reportsList;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<ReportsList>();
        this.arraylist.addAll(reportsList);
    }

    public class ViewHolder {
        TextView cmuscnotxt, statustxt,datetxt;
    }

    @Override
    public int getCount() {
        return reportsList.size();
    }

    @Override
    public ReportsList getItem(int position) {
        return reportsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ReportListAdapter.ViewHolder holder;
        if (view == null) {
            holder = new ReportListAdapter.ViewHolder();
            view = inflater.inflate(R.layout.report_listitem, null);
            holder.cmuscnotxt = (TextView) view.findViewById(R.id.cmuscnotxt);
            holder.statustxt = (TextView) view.findViewById(R.id.statustext);
            holder.datetxt = (TextView) view.findViewById(R.id.datetxt);
            view.setTag(holder);
        } else {
            holder = (ReportListAdapter.ViewHolder) view.getTag();
        }
        holder.cmuscnotxt.setText(reportsList.get(position).getCmuscno());
        holder.datetxt.setText(reportsList.get(position).getDate());
        holder.statustxt.setText(reportsList.get(position).getStatus());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        return view;
    }
}
