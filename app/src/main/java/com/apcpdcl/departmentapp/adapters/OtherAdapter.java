package com.apcpdcl.departmentapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.apcpdcl.departmentapp.R;
import com.apcpdcl.departmentapp.models.PendingData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 23-02-2018.
 */

public class OtherAdapter extends BaseAdapter {

    // Declare Variables
    Context mContext;
    LayoutInflater inflater;
    private List<PendingData> pendinglist = null;
    private ArrayList<PendingData> arraylist;

    public OtherAdapter(Context context, List<PendingData> pendinglist) {
        mContext = context;
        this.pendinglist = pendinglist;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<PendingData>();
        this.arraylist.addAll(pendinglist);
    }

    public class ViewHolder {
        TextView totamnttxt,statustxt,penamnttxt,typetxt,casenumtxt;
    }

    @Override
    public int getCount() {
        return pendinglist.size();
    }

    @Override
    public PendingData getItem(int position) {
        return pendinglist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {

        final OtherAdapter.ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.otherdetails_listitem, null);
            holder.totamnttxt = (TextView) view.findViewById(R.id.totamnttxt);
            holder.statustxt = (TextView) view.findViewById(R.id.statustxt);
            holder.penamnttxt = (TextView) view.findViewById(R.id.penamnttxt);
            holder.typetxt = (TextView) view.findViewById(R.id.typetxt);
            holder.casenumtxt = (TextView) view.findViewById(R.id.casenumtxt);
            view.setTag(holder);
        } else {
            holder = (OtherAdapter.ViewHolder) view.getTag();
        }

        holder.totamnttxt.setText(pendinglist.get(position).getTotAmnt());
        holder.statustxt.setText(pendinglist.get(position).getStatus());
        holder.penamnttxt.setText(pendinglist.get(position).getPendingAmnt());
        holder.typetxt.setText(pendinglist.get(position).getType());
        holder.casenumtxt.setText(pendinglist.get(position).getCaseNum());
        return view;
    }
}
