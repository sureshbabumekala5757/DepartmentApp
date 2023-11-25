package com.apcpdcl.departmentapp.adapters;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.TextView;

import com.apcpdcl.departmentapp.R;
import com.apcpdcl.departmentapp.activities.PendingDtrComplaintsDetailsActivity;
import com.apcpdcl.departmentapp.models.DTrComplaint;

import java.util.List;

public class DTRComplaintsRecyclerviewAdapter extends RecyclerView.Adapter<DTRComplaintsRecyclerviewAdapter.ViewHolder> {

    private Context mContext;
    private List<DTrComplaint> complaintsList;
    private LayoutInflater mInflater;

    // data is passed into the constructor
    public DTRComplaintsRecyclerviewAdapter(Context context, List<DTrComplaint> complaintsList) {
        this.mInflater = LayoutInflater.from(context);
        this.complaintsList = complaintsList;
        mContext = context;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.pending_dtrcomplaints_listitem, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.ComplaintIDtxt.setText(complaintsList.get(position).getComplaintID());
        holder.SecNametxt.setText(complaintsList.get(position).getsecName());
        holder.CreatedDttext.setText(complaintsList.get(position).getCreatedDate());
        holder.PhoneNotxt.setText(complaintsList.get(position).getPhoneNo());
        holder.Statustxt.setText(complaintsList.get(position).getStatus());
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return complaintsList.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView ComplaintIDtxt, SecNametxt, CreatedDttext, PhoneNotxt, Statustxt;
        TableRow tb_list;

        ViewHolder(View itemView) {
            super(itemView);
            tb_list = (TableRow) itemView.findViewById(R.id.tb_list);
            ComplaintIDtxt = (TextView) itemView.findViewById(R.id.ComplaintIDtxt);
            SecNametxt = (TextView) itemView.findViewById(R.id.SecNametxt);
            CreatedDttext = (TextView) itemView.findViewById(R.id.CreatedDttext);
            PhoneNotxt = (TextView) itemView.findViewById(R.id.PhoneNotxt);
            Statustxt = (TextView) itemView.findViewById(R.id.Statustxt);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, PendingDtrComplaintsDetailsActivity.class);
                    intent.putExtra("COMPID", complaintsList.get(getAdapterPosition()).getComplaintID());
                    intent.putExtra("TRANDT", complaintsList.get(getAdapterPosition()).getCreatedDate());
                    intent.putExtra("SECNAME", complaintsList.get(getAdapterPosition()).getsecName());
                    intent.putExtra("PHONENUM", complaintsList.get(getAdapterPosition()).getPhoneNo());
                    mContext.startActivity(intent);
                }
            });
        }

    }

    String getItem(int id) {
        return String.valueOf(complaintsList.get(id));
    }

}