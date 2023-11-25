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

public class CounterSubDivListAdapter extends RecyclerView.Adapter<CounterSubDivListAdapter.ViewHolder>{

    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<CounterModel> subDivList;
    private ItemClickListener clickListener;

    // data is passed into the constructor
    public CounterSubDivListAdapter(Context context, ArrayList<CounterModel> subDivList) {
        this.mInflater = LayoutInflater.from(context);
        this.subDivList = subDivList;
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
            CounterModel counterModel=subDivList.get(position);
            holder.name_txt.setText(counterModel.getDIV_Name());
            holder.counter_num_of_scs_txt.setText(counterModel.getSubDiv_SCS());
            holder.counter_amount_txt.setText(counterModel.getSubDiv_AMT());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // total number of rows
    @Override
    public int getItemCount() {
        return subDivList.size();
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name_txt,counter_num_of_scs_txt,counter_amount_txt;

        ViewHolder(View itemView) {
            super(itemView);
            name_txt = (TextView) itemView.findViewById(R.id.counter_name_txt);
            counter_num_of_scs_txt = (TextView) itemView.findViewById(R.id.counter_num_of_scs_txt);
            counter_amount_txt = (TextView) itemView.findViewById(R.id.counter_amount_txt);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (clickListener != null) {
                clickListener.onClick(v, getAdapterPosition());
            }
        }
    }

    public interface ItemClickListener {
        void onClick(View view, int position);
    }
}


