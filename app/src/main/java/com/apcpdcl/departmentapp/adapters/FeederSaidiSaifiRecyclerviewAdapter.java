package com.apcpdcl.departmentapp.adapters;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.apcpdcl.departmentapp.R;
import com.apcpdcl.departmentapp.models.FeederSaidiSaifi;

import java.util.List;

/**
 * Created by Admin on 21-03-2018.
 */

public class FeederSaidiSaifiRecyclerviewAdapter extends RecyclerView.Adapter<FeederSaidiSaifiRecyclerviewAdapter.ViewHolder> {

    private Context mContext;
    private List<FeederSaidiSaifi> feederList;
    private LayoutInflater mInflater;

    // data is passed into the constructor
    public FeederSaidiSaifiRecyclerviewAdapter(Context context, List<FeederSaidiSaifi> feederList) {
        this.mInflater = LayoutInflater.from(context);
        this.feederList = feederList;
        mContext = context;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.feeder_saidi_saifi_listitem, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.F_sl_no_txt.setText(String.valueOf(position));
        holder.F_feedername_txt.setText(feederList.get(position).getFeederName());
        holder.F_saidi_hh_mm_ss_txt.setText(feederList.get(position).getFeederSaidi());
        holder.F_saifi_no_s_txt.setText(feederList.get(position).getFeederSaifi());
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return feederList.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView F_sl_no_txt,F_feedername_txt,F_saifi_no_s_txt,F_saidi_hh_mm_ss_txt;
        ViewHolder(View itemView) {
            super(itemView);
            F_sl_no_txt = (TextView) itemView.findViewById(R.id.F_sl_no_txt);
            F_feedername_txt = (TextView) itemView.findViewById(R.id.F_feedername_txt);
            F_saidi_hh_mm_ss_txt = (TextView) itemView.findViewById(R.id.F_saidi_hh_mm_ss_txt);
            F_saifi_no_s_txt=(TextView) itemView.findViewById(R.id.F_saifi_no_s_txt);
        }

    }

    String getItem(int id) {
        return String.valueOf(feederList.get(id));
    }


}
