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
import com.apcpdcl.departmentapp.activities.MatsOperatingAllDetails;
import com.apcpdcl.departmentapp.models.MatsConsumerModel;
import com.apcpdcl.departmentapp.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 21-03-2018.
 */

public class MatsDCListAdapter extends RecyclerView.Adapter<MatsDCListAdapter.ViewHolder> {

    private Context mContext;
    private List<MatsConsumerModel> consumerlist;
    private ArrayList<String> sfilterReportsList = new ArrayList<>();
    private LayoutInflater mInflater;

    // data is passed into the constructor
    public MatsDCListAdapter(Context context, List<MatsConsumerModel> consumerlist) {
        this.mInflater = LayoutInflater.from(context);
        this.consumerlist = consumerlist;
        mContext = context;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.operate_mats_dc_list_item, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Utils.strNum = consumerlist.get(position).getCMUSCNO();
        holder.scnotxt.setText(String.valueOf(consumerlist.get(position).getId()));
        holder.cmuscnotxt.setText(Utils.strNum);
        holder.totaltxt.setText(String.valueOf(consumerlist.get(position).getTOT()));
        holder.cmcnametxt.setText(consumerlist.get(position).getCMCNAME());
        holder.cmcattxt.setText(consumerlist.get(position).getCASENO());


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
                    Intent intent = new Intent(mContext, MatsOperatingAllDetails.class);
                    intent.putExtra("EXCESSLOAD", String.valueOf(consumerlist.get(getAdapterPosition()).getEXECSSLOAD()));
                    intent.putExtra("CMCNAME", consumerlist.get(getAdapterPosition()).getCMCNAME());
                    intent.putExtra("CMUSCNO", consumerlist.get(getAdapterPosition()).getCMUSCNO());
                    intent.putExtra("TOT", String.valueOf(consumerlist.get(getAdapterPosition()).getTOT()));
                    intent.putExtra("CMCAT", consumerlist.get(getAdapterPosition()).getCMCAT());
                    intent.putExtra("CMADDRESS", consumerlist.get(getAdapterPosition()).getCMADDRESS());
                    intent.putExtra("LAT", consumerlist.get(getAdapterPosition()).getLAT());
                    intent.putExtra("LONG", consumerlist.get(getAdapterPosition()).getLONG());
                    intent.putExtra("Pole", consumerlist.get(getAdapterPosition()).getPOLENO());
                    intent.putExtra("CASENO", consumerlist.get(getAdapterPosition()).getCASENO());
                    mContext.startActivity(intent);
                }
            });
        }

    }

    String getItem(int id) {
        return String.valueOf(consumerlist.get(id));
    }


}
