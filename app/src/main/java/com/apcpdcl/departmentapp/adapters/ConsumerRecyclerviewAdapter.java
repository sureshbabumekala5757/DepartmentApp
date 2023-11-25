package com.apcpdcl.departmentapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.TextView;

import com.apcpdcl.departmentapp.R;
import com.apcpdcl.departmentapp.activities.OperatingAllDetails;
import com.apcpdcl.departmentapp.models.Consumer;
import com.apcpdcl.departmentapp.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 21-03-2018.
 */

public class ConsumerRecyclerviewAdapter extends RecyclerView.Adapter<ConsumerRecyclerviewAdapter.ViewHolder> {

    private Context mContext;
    private List<Consumer> consumerlist;
    private ArrayList<String> sfilterReportsList = new ArrayList<>();
    private LayoutInflater mInflater;

    // data is passed into the constructor
    public ConsumerRecyclerviewAdapter(Context context, List<Consumer> consumerlist) {
        this.mInflater = LayoutInflater.from(context);
        this.consumerlist = consumerlist;
        mContext = context;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.operate_listitem, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Utils.strNum = consumerlist.get(position).getCmuscno();
        holder.scnotxt.setText(String.valueOf(consumerlist.get(position).getID()));
        holder.cmuscnotxt.setText(Utils.strNum);
        holder.totaltxt.setText(String.valueOf(consumerlist.get(position).getTotal()));
        holder.cmcnametxt.setText(consumerlist.get(position).getCmcName());
        holder.cmcattxt.setText(consumerlist.get(position).getCmcat());


        try {
            sfilterReportsList.clear();
            if (Utils.filterReportsList.size() != 0) {
                for (int i = 0; i < Utils.filterReportsList.size(); i++) {
                    sfilterReportsList.add(Utils.filterReportsList.get(i).getCmuscno());
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


    }

    // total number of rows
    @Override
    public int getItemCount() {
        return consumerlist.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView cmuscnotxt, totaltxt, cmcnametxt, cmcattxt, scnotxt;
        TableRow tb_list;

        ViewHolder(View itemView) {
            super(itemView);
            tb_list = (TableRow) itemView.findViewById(R.id.tb_list);
            scnotxt = (TextView) itemView.findViewById(R.id.serialnumtxt);
            cmuscnotxt = (TextView) itemView.findViewById(R.id.cmuscnotxt);
            totaltxt = (TextView) itemView.findViewById(R.id.totaltxt);
            cmcnametxt = (TextView) itemView.findViewById(R.id.cmcnametext);
            cmcattxt = (TextView) itemView.findViewById(R.id.cmcattxt);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, OperatingAllDetails.class);
                    intent.putExtra("BLDISCDT", consumerlist.get(getAdapterPosition()).getBldiscdt());
                    intent.putExtra("STYPE", consumerlist.get(getAdapterPosition()).getStype());
                    intent.putExtra("CMCNAME", consumerlist.get(getAdapterPosition()).getCmcName());
                    intent.putExtra("MTRNO", consumerlist.get(getAdapterPosition()).getMeterNum());
                    intent.putExtra("CMUSCNO", consumerlist.get(getAdapterPosition()).getCmuscno());
                    intent.putExtra("TOT", String.valueOf(consumerlist.get(getAdapterPosition()).getTotal()));
                    intent.putExtra("LASTPAIDDT", consumerlist.get(getAdapterPosition()).getLastpaydt());
                    intent.putExtra("CMSOCIALCAT", consumerlist.get(getAdapterPosition()).getSocialCat());
                    intent.putExtra("CMCAT", consumerlist.get(getAdapterPosition()).getCmcat());
                    intent.putExtra("CMGOVT", consumerlist.get(getAdapterPosition()).getGovtCat());
                    intent.putExtra("LASTPAIDAMT", String.valueOf(consumerlist.get(getAdapterPosition()).getLastPayAmnt()));
                    intent.putExtra("CMDTRCODE", consumerlist.get(getAdapterPosition()).getCmdtrCode());
                    intent.putExtra("CMADDRESS", consumerlist.get(getAdapterPosition()).getAddr());
                    intent.putExtra("LOCATION", consumerlist.get(getAdapterPosition()).getLoc());
                    intent.putExtra("CMPHONE", consumerlist.get(getAdapterPosition()).getPhone());
                    intent.putExtra("LAT", consumerlist.get(getAdapterPosition()).getLat());
                    intent.putExtra("LONG", consumerlist.get(getAdapterPosition()).getLong());
                    intent.putExtra("STATUS", consumerlist.get(getAdapterPosition()).getLong());
                    intent.putExtra("Pole", consumerlist.get(getAdapterPosition()).getPoleNo());
                    mContext.startActivity(intent);
                }
            });
        }

    }

    String getItem(int id) {
        return String.valueOf(consumerlist.get(id));
    }


}
