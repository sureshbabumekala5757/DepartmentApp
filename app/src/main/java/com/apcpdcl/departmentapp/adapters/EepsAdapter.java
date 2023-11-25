package com.apcpdcl.departmentapp.adapters;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.apcpdcl.departmentapp.R;
import com.apcpdcl.departmentapp.activities.EepsUpdateActivity;
import com.apcpdcl.departmentapp.models.Eeps;
import com.apcpdcl.departmentapp.utils.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 21-03-2018.
 */

public class EepsAdapter extends RecyclerView.Adapter<EepsAdapter.ViewHolder> {

    private Context mContext;
    List<Eeps> eepsList = new ArrayList<Eeps>();
    private LayoutInflater mInflater;
    private String from = "";

    // data is passed into the constructor
    public EepsAdapter(Context context, List<Eeps> eepsList, String from) {
        this.mInflater = LayoutInflater.from(context);
        this.eepsList = eepsList;
        this.from = from;
        mContext = context;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.eepsunit_listitem, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        try {
            holder.eeps_regnum.setText(eepsList.get(position).getreg_no());
            holder.eeps_servicenum.setText(eepsList.get(position).getUscno());
            holder.eeps_servicename.setText(eepsList.get(position).getcname());
            holder.eeps_status.setText(eepsList.get(position).getsocialcat());
            holder.eeps_load.setText(eepsList.get(position).getctr_load());
            holder.eeps_village.setText(eepsList.get(position).getdistname());
            holder.eeps_feeder.setText(eepsList.get(position).getFeeder());
            holder.eeps_substation.setText(eepsList.get(position).getSubStation());
            holder.eeps_remarks.setText(eepsList.get(position).getRemarks());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return eepsList.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView eeps_regnum,eeps_servicenum, eeps_servicename, eeps_status, eeps_load, eeps_village,eeps_feeder,eeps_substation,eeps_remarks;

        ViewHolder(View itemView) {
            super(itemView);
            eeps_regnum=(TextView) itemView.findViewById(R.id.eeps_regnum);
            eeps_servicenum = (TextView) itemView.findViewById(R.id.eeps_servicenum);
            eeps_servicename = (TextView) itemView.findViewById(R.id.eeps_servicename);
            eeps_status = (TextView) itemView.findViewById(R.id.eeps_status);
            eeps_load = (TextView) itemView.findViewById(R.id.eeps_load);
            eeps_village = (TextView) itemView.findViewById(R.id.eeps_village);
            eeps_feeder = (TextView) itemView.findViewById(R.id.eeps_feeder);
            eeps_substation = (TextView) itemView.findViewById(R.id.eeps_substation);
            eeps_remarks = (TextView) itemView.findViewById(R.id.eeps_remarks);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, EepsUpdateActivity.class);
                    intent.putExtra("RegNo",eepsList.get(getAdapterPosition()).getreg_no());
                    intent.putExtra("ConsumerName",eepsList.get(getAdapterPosition()).getcname());
                    intent.putExtra("Load",eepsList.get(getAdapterPosition()).getctr_load());
                    intent.putExtra("SocialStatus",eepsList.get(getAdapterPosition()).getsocialcat());
                    intent.putExtra("11KvFeeder",eepsList.get(getAdapterPosition()).getFeeder());
                    intent.putExtra("Village",eepsList.get(getAdapterPosition()).getdistname());
                    intent.putExtra(Constants.FROM,from);
                    mContext.startActivity(intent);
                }
            });
        }

    }

    String getItem(int id) {
        return String.valueOf(eepsList.get(id));
    }
}


