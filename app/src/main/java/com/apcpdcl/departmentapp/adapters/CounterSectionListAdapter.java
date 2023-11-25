package com.apcpdcl.departmentapp.adapters;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.apcpdcl.departmentapp.R;
import com.apcpdcl.departmentapp.models.CounterModel;

import java.util.ArrayList;

public class CounterSectionListAdapter extends RecyclerView.Adapter<CounterSectionListAdapter.ViewHolder>{

    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<CounterModel> secList;

    // data is passed into the constructor
    public CounterSectionListAdapter(Context context, ArrayList<CounterModel> secList) {
        this.mInflater = LayoutInflater.from(context);
        this.secList = secList;
        mContext = context;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.counter_wise_listitem, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        try {
            CounterModel counterModel=secList.get(position);
            holder.name_txt.setText(counterModel.getSection());
            holder.counter_num_of_scs_txt.setText(counterModel.getSec_SCS());
            holder.counter_amount_txt.setText(counterModel.getSec_AMT());


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    // total number of rows
    @Override
    public int getItemCount() {
        return secList.size();
    }



    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name_txt,counter_num_of_scs_txt,counter_amount_txt;

        ViewHolder(View itemView) {
            super(itemView);
            name_txt = (TextView) itemView.findViewById(R.id.counter_name_txt);
            counter_num_of_scs_txt = (TextView) itemView.findViewById(R.id.counter_num_of_scs_txt);
            counter_amount_txt = (TextView) itemView.findViewById(R.id.counter_amount_txt);

        }

    }

}


