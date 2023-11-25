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
import com.apcpdcl.departmentapp.activities.OperatingAllDetails;
import com.apcpdcl.departmentapp.models.Consumer;
import com.apcpdcl.departmentapp.models.ReportsList;
import com.apcpdcl.departmentapp.sqlite.ReportsDataBaseHandler;
import com.apcpdcl.departmentapp.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 19-12-2017.
 */

public class ConsumerAdapter extends BaseAdapter {

    // Declare Variables
    Context mContext;
    LayoutInflater inflater;
    private List<Consumer> consumerlist = null;
    private ArrayList<ReportsList> filterReportsList = new ArrayList<>();
    private ArrayList<String> sfilterReportsList = new ArrayList<>();

    ReportsDataBaseHandler rdb;


    public ConsumerAdapter(Context context, List<Consumer> consumerlist) {
        mContext = context;
        this.consumerlist = consumerlist;
        inflater = LayoutInflater.from(mContext);
    }

    public class ViewHolder {
        TextView cmuscnotxt, totaltxt, cmcnametxt, cmcattxt,scnotxt;
    }

    @Override
    public int getCount() {
        return consumerlist.size();
    }

    @Override
    public Consumer getItem(int position) {
        return consumerlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        rdb = new ReportsDataBaseHandler(mContext);

        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.operate_listitem, null);
            holder.scnotxt=(TextView)  view.findViewById(R.id.serialnumtxt);
            holder.cmuscnotxt = (TextView) view.findViewById(R.id.cmuscnotxt);
            holder.totaltxt = (TextView) view.findViewById(R.id.totaltxt);
            holder.cmcnametxt = (TextView) view.findViewById(R.id.cmcnametext);
            holder.cmcattxt = (TextView) view.findViewById(R.id.cmcattxt);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        Utils.strNum = consumerlist.get(position).getCmuscno();
        holder.scnotxt.setText(String.valueOf(consumerlist.get(position).getID()));
        holder.cmuscnotxt.setText(Utils.strNum);
        holder.totaltxt.setText(String.valueOf(consumerlist.get(position).getTotal()));
        holder.cmcnametxt.setText(consumerlist.get(position).getCmcName());
        holder.cmcattxt.setText(consumerlist.get(position).getCmcat());

        try {
            filterReportsList.clear();
            sfilterReportsList.clear();
            filterReportsList.addAll(rdb.getAllServiceNum());
            if (filterReportsList.size() != 0) {
                for (int i = 0; i < filterReportsList.size(); i++) {
                    sfilterReportsList.add(filterReportsList.get(i).getCmuscno());
                }
            }

            if (sfilterReportsList.size() != 0) {
                boolean flag = sfilterReportsList.contains(Utils.strNum);
                System.out.println("String exists in ArrayList? : " + flag);
                if (flag) {
                    holder.scnotxt.setTextColor(Color.parseColor("#006400"));
                    holder.cmuscnotxt.setTextColor(Color.parseColor("#006400"));
                    holder.totaltxt.setTextColor(Color.parseColor("#006400"));
                    holder.cmcnametxt.setTextColor(Color.parseColor("#006400"));
                    holder.cmcattxt.setTextColor(Color.parseColor("#006400"));
                } else {
                    holder.scnotxt.setTextColor(Color.parseColor("#3F51B5"));
                    holder.cmuscnotxt.setTextColor(Color.parseColor("#3F51B5"));
                    holder.totaltxt.setTextColor(Color.parseColor("#3F51B5"));
                    holder.cmcnametxt.setTextColor(Color.parseColor("#3F51B5"));
                    holder.cmcattxt.setTextColor(Color.parseColor("#3F51B5"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, OperatingAllDetails.class);
                intent.putExtra("BLDISCDT", consumerlist.get(position).getBldiscdt());
                intent.putExtra("STYPE", consumerlist.get(position).getStype());
                intent.putExtra("CMCNAME", consumerlist.get(position).getCmcName());
                intent.putExtra("MTRNO", consumerlist.get(position).getMeterNum());
                intent.putExtra("CMUSCNO", consumerlist.get(position).getCmuscno());
                intent.putExtra("TOT", String.valueOf(consumerlist.get(position).getTotal()));
                intent.putExtra("LASTPAIDDT", consumerlist.get(position).getLastpaydt());
                intent.putExtra("CMSOCIALCAT", consumerlist.get(position).getSocialCat());
                intent.putExtra("CMCAT", consumerlist.get(position).getCmcat());
                intent.putExtra("CMGOVT", consumerlist.get(position).getGovtCat());
                intent.putExtra("LASTPAIDAMT", String.valueOf(consumerlist.get(position).getLastPayAmnt()));
                intent.putExtra("CMDTRCODE", consumerlist.get(position).getCmdtrCode());
                intent.putExtra("CMADDRESS", consumerlist.get(position).getAddr());
                intent.putExtra("LOCATION", consumerlist.get(position).getLoc());
                intent.putExtra("CMPHONE", consumerlist.get(position).getPhone());
                intent.putExtra("LAT", consumerlist.get(position).getLat());
                intent.putExtra("LONG", consumerlist.get(position).getLong());
                mContext.startActivity(intent);
            }
        });

        return view;
    }

}
