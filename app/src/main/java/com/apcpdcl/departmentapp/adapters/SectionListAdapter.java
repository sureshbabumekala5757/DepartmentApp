package com.apcpdcl.departmentapp.adapters;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.apcpdcl.departmentapp.R;
import com.apcpdcl.departmentapp.models.DigitalModel;

import java.util.ArrayList;

public class SectionListAdapter extends RecyclerView.Adapter<SectionListAdapter.ViewHolder>{

    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<DigitalModel> secList;

    // data is passed into the constructor
    public SectionListAdapter(Context context, ArrayList<DigitalModel> secList) {
        this.mInflater = LayoutInflater.from(context);
        this.secList = secList;
        mContext = context;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.digital_trans_listitem, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        try {
            DigitalModel digitalModel=secList.get(position);
            holder.name_txt.setText(digitalModel.getSEC_Name());
            holder.cashscscount_txt.setText(digitalModel.getCash_count());
            holder.digitalscscount_txt.setText(digitalModel.getDigital_count());
            holder.cashscsamount_txt.setText(digitalModel.getCash_Amount());
            holder.digitalscsamount_txt.setText(digitalModel.getDigital_Amt());

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
        TextView name_txt, cashscscount_txt, digitalscscount_txt, cashscsamount_txt, digitalscsamount_txt;

        ViewHolder(View itemView) {
            super(itemView);
            name_txt = (TextView) itemView.findViewById(R.id.name_txt);
            cashscscount_txt = (TextView) itemView.findViewById(R.id.cashscscount_txt);
            digitalscscount_txt = (TextView) itemView.findViewById(R.id.digitalscscount_txt);
            cashscsamount_txt = (TextView) itemView.findViewById(R.id.cashscsamount_txt);
            digitalscsamount_txt = (TextView) itemView.findViewById(R.id.digitalscsamount_txt);
        }

    }

}


