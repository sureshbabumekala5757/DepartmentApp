package com.apcpdcl.departmentapp.adapters;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.apcpdcl.departmentapp.R;
import com.apcpdcl.departmentapp.activities.FeederSaidiSaifiActivity;
import com.apcpdcl.departmentapp.models.SecSaidiSaifi;

import java.util.List;

/**
 * Created by Admin on 21-03-2018.
 */

public class SaidiSaifiRecyclerviewAdapter extends RecyclerView.Adapter<SaidiSaifiRecyclerviewAdapter.ViewHolder> {

    private Context mContext;
    private List<SecSaidiSaifi> sectionList;
    private LayoutInflater mInflater;

    // data is passed into the constructor
    public SaidiSaifiRecyclerviewAdapter(Context context, List<SecSaidiSaifi> sectionList) {
        this.mInflater = LayoutInflater.from(context);
        this.sectionList = sectionList;
        mContext = context;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.saidi_saifi_activity_listitem, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.sl_no_txt.setText(String.valueOf(position));
        holder.sec_name_txt.setText(sectionList.get(position).getSecName());
        holder.total_feeders_txt.setText(sectionList.get(position).getTotalFeeders());
        holder.saifi_no_s_txt.setText(sectionList.get(position).getSectionSaifi());
        holder.saidi_hh_mm_ss_txt.setText(sectionList.get(position).getSectionSaidi());
        holder.total_feeders_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, FeederSaidiSaifiActivity.class);
                mContext.startActivity(intent);
            }
        });

    }

    // total number of rows
    @Override
    public int getItemCount() {
        return sectionList.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        //declaration
        TextView sl_no_txt,sec_name_txt,total_feeders_txt,saifi_no_s_txt,saidi_hh_mm_ss_txt;
        ViewHolder(View itemView) {
            super(itemView);
            sl_no_txt = (TextView) itemView.findViewById(R.id.sl_no_txt);
            sec_name_txt = (TextView) itemView.findViewById(R.id.sec_name_txt);
            saifi_no_s_txt = (TextView) itemView.findViewById(R.id.saifi_no_s_txt);
            /*saidi_sec_s_txt = (TextView) itemView.findViewById(R.id.saidi_sec_s_txt);*/
            saidi_hh_mm_ss_txt = (TextView) itemView.findViewById(R.id.saidi_hh_mm_ss_txt);
            total_feeders_txt=(TextView) itemView.findViewById(R.id.total_feeders_txt);
        }
    }

    String getItem(int id) {
        return String.valueOf(sectionList.get(id));
    }

}
