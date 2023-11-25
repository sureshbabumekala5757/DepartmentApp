package com.apcpdcl.departmentapp.adapters;

import android.app.Activity;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.apcpdcl.departmentapp.R;
import com.apcpdcl.departmentapp.activities.DTRTrackingActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class DtrListviewAdapter extends BaseAdapter {
    public ArrayList<HashMap> list;
    Activity activity;

    public DtrListviewAdapter(Activity activity, ArrayList<HashMap> list) {
        super();
        this.activity = activity;
        this.list = list;
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }
    private class ViewHolder {
        TextView txtFirst;
        TextView txtSecond;
        TextView txtThird;
        TextView txtFourth;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        // TODO Auto-generated method stub
        ViewHolder holder;
        LayoutInflater inflater =  activity.getLayoutInflater();

        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.dtr_listview_row, null);
            holder = new ViewHolder();
            holder.txtFirst = (TextView) convertView.findViewById(R.id.txt_complaint);
            holder.txtSecond = (TextView) convertView.findViewById(R.id.txt_section);
            holder.txtThird = (TextView) convertView.findViewById(R.id.txt_staus);
            holder.txtFourth = (TextView) convertView.findViewById(R.id.txt_date);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        HashMap map = list.get(position);
        holder.txtFirst.setText(map.get("COMPLAINT").toString());
        holder.txtFirst.setPaintFlags(holder.txtFirst.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        holder.txtFirst.setTextColor(activity.getResources().getColor(R.color.colorAccent));
        holder.txtSecond.setText(map.get("SECTION").toString());
        holder.txtThird.setText(map.get("STATUS").toString());
        holder.txtFourth.setText(map.get("DATE").toString());

        holder.txtFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> hashMap = list.get(position);
                if (hashMap != null) {
                    if (activity instanceof DTRTrackingActivity)
                        ((DTRTrackingActivity) activity).getListItem(hashMap);
                }
            }
        });

        return convertView;
    }
}
