package com.apcpdcl.departmentapp.adapters;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.TextView;

import com.apcpdcl.departmentapp.R;
import com.apcpdcl.departmentapp.models.FeederOutageModel;
import com.apcpdcl.departmentapp.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class FeederOutageAdapter extends RecyclerView.Adapter<FeederOutageAdapter.ViewHolder> {

    private Context mContext;
    private List<FeederOutageModel> feederOutageModels;
    private ArrayList<String> sfilterReportsList = new ArrayList<>();
    private LayoutInflater mInflater;
    public static final String TIME_FORMAT = "dd-MM-yyyy HH:mm:ss";

    // data is passed into the constructor
    public FeederOutageAdapter(Context context, List<FeederOutageModel> feederOutageModels) {
        this.mInflater = LayoutInflater.from(context);
        this.feederOutageModels = feederOutageModels;
        mContext = context;
    }

    // inflates the row layout from xml when needed
    @Override
    public FeederOutageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.feeder_outage_listitem, parent, false);
        return new FeederOutageAdapter.ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(FeederOutageAdapter.ViewHolder holder, int position) {

        holder.tv_ss_name.setText(String.valueOf(feederOutageModels.get(position).getSubstationName()));
        holder.tv_feeder_name.setText(feederOutageModels.get(position).getFeederName());
        holder.tv_meter_no.setText(String.valueOf(feederOutageModels.get(position).getMeterId()));
        holder.tv_occur.setText(feederOutageModels.get(position).getEventOccur());
        holder.tv_status.setText(feederOutageModels.get(position).getStatus());
        setBase(holder.tv_duration, feederOutageModels.get(position));


        try {
            sfilterReportsList.clear();
            if (Utils.filterReportsList.size() != 0) {
                for (int i = 0; i < Utils.filterReportsList.size(); i++) {
                    sfilterReportsList.add(Utils.filterReportsList.get(i).getCmuscno());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    // total number of rows
    @Override
    public int getItemCount() {
        return feederOutageModels.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_ss_name, tv_feeder_name, tv_meter_no, tv_status,tv_occur;
        Chronometer tv_duration;

        ViewHolder(View itemView) {
            super(itemView);
            tv_ss_name = (TextView) itemView.findViewById(R.id.tv_ss_name);
            tv_feeder_name = (TextView) itemView.findViewById(R.id.tv_feeder_name);
            tv_meter_no = (TextView) itemView.findViewById(R.id.tv_meter_no);
            tv_occur = (TextView) itemView.findViewById(R.id.tv_occur);
            tv_duration = (Chronometer) itemView.findViewById(R.id.tv_duration);
            tv_status = (TextView) itemView.findViewById(R.id.tv_status);
        }
    }

    String getItem(int id) {
        return String.valueOf(feederOutageModels.get(id));
    }

    private void setBase(Chronometer chronometer, final FeederOutageModel feederOutageModel) {
        chronometer.setText(feederOutageModel.getInterruptionDuraion());
      /*  chronometer.start();
        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                String[] s= feederOutageModel.getInterruptionDuraion().split(":");
                int hrs = Integer.parseInt(s[0]);
                int mts = Integer.parseInt(s[1]);
                int sec = Integer.parseInt(s[0]);
            }
        });*/
    }

}

