package com.apcpdcl.departmentapp.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.apcpdcl.departmentapp.R;

import com.apcpdcl.departmentapp.activities.MeterChangeEntryFormActivity;
import com.apcpdcl.departmentapp.activities.MeterChangeReqListActivity;
import com.apcpdcl.departmentapp.models.MeterChangeReqModel;
import com.apcpdcl.departmentapp.utils.Constants;
import com.apcpdcl.departmentapp.utils.Utility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Haseen
 * on 01-03-2018.
 */

public class MeterChangeReqListAdapter extends BaseAdapter implements Filterable {
    private ArrayList<MeterChangeReqModel> sortedList;
    private ArrayList<MeterChangeReqModel> mList;
    private final LayoutInflater inflater;
    private MeterChangeReqListActivity meterChangeReqListActivity;


    public MeterChangeReqListAdapter(MeterChangeReqListActivity context, ArrayList<MeterChangeReqModel> meterChangeReqModels) {
        inflater = LayoutInflater.from(context);
        meterChangeReqListActivity = context;
        this.mList = meterChangeReqModels;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        // General ListView optimization code.
        if (view == null) {
            view = inflater.inflate(R.layout.registration_item_layout, viewGroup, false);
            viewHolder = new ViewHolder();
            viewHolder.tv_service_number = (TextView) view.findViewById(R.id.tv_service_no);
            viewHolder.iv_location = (ImageView) view.findViewById(R.id.iv_location);
            viewHolder.iv_location.setVisibility(View.VISIBLE);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        final MeterChangeReqModel meterChangeReqModel = mList.get(i);
        viewHolder.tv_service_number.setText(meterChangeReqModel.getServiceRequest());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in = new Intent(meterChangeReqListActivity, MeterChangeEntryFormActivity.class);
                in.putExtra(Constants.USCNO, meterChangeReqModel.getUscNo());
                in.putExtra("SERVICEREQUESTNO", meterChangeReqModel.getServiceRequest());
                meterChangeReqListActivity.startActivity(in);
            }
        });
        return view;
    }

    private static class ViewHolder {
        TextView tv_service_number;
        ImageView iv_location;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return new Filter() {
            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                mList = (ArrayList<MeterChangeReqModel>) results.values;

                notifyDataSetChanged();
                // notifies the data with new filtered values
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                List<MeterChangeReqModel> FilteredArrList = new ArrayList<>();
                if (sortedList == null) {
                    sortedList = new ArrayList<>(mList); // saves the original data in mOriginalValues
                }
                /* *******
                 *
                 *  If constraint(CharSequence that is received) is null returns the mOriginalValues(Original) values
                 *  else does the Filtering and returns FilteredArrList(Filtered)
                 *
                 ********/
                if (constraint == null || constraint.length() == 0) {
                    // set the Original result to return
                    results.count = sortedList.size();
                    results.values = sortedList;
                } else {
                    constraint = constraint.toString().toLowerCase();
                    for (int i = 0; i < sortedList.size(); i++) {
                        MeterChangeReqModel data = sortedList.get(i);
                        if (data.getServiceRequest().contains(constraint.toString().toUpperCase())) {
                            FilteredArrList.add(data);
                        }
                    }
                    // set the Filtered result to return
                    results.count = FilteredArrList.size();
                    results.values = FilteredArrList;
                    Utility.showLog("Count", "" + results.count);
                }
                return results;
            }
        };
    }
}
