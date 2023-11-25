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

public class SubDivListAdapter extends RecyclerView.Adapter<SubDivListAdapter.ViewHolder>{

    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<DigitalModel> subDivList;
    private ItemClickListener clickListener;

    // data is passed into the constructor
    public SubDivListAdapter(Context context, ArrayList<DigitalModel> subDivList) {
        this.mInflater = LayoutInflater.from(context);
        this.subDivList = subDivList;
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
            DigitalModel digitalModel=subDivList.get(position);
            holder.name_txt.setText(digitalModel.getDIV_Name());
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
        return subDivList.size();
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name_txt, cashscscount_txt, digitalscscount_txt, cashscsamount_txt, digitalscsamount_txt;

        ViewHolder(View itemView) {
            super(itemView);
            name_txt = (TextView) itemView.findViewById(R.id.name_txt);
            cashscscount_txt = (TextView) itemView.findViewById(R.id.cashscscount_txt);
            digitalscscount_txt = (TextView) itemView.findViewById(R.id.digitalscscount_txt);
            cashscsamount_txt = (TextView) itemView.findViewById(R.id.cashscsamount_txt);
            digitalscsamount_txt = (TextView) itemView.findViewById(R.id.digitalscsamount_txt);
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


