package com.apcpdcl.departmentapp.adapters;

import android.app.Activity;
import android.content.Intent;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.apcpdcl.departmentapp.R;
import com.apcpdcl.departmentapp.activities.LiveInterruptionsDetailActivity;
import com.apcpdcl.departmentapp.models.InterruptionModel;
import com.apcpdcl.departmentapp.utils.Utility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Haseen
 * on 01-03-2018.
 */

public class LiveInterruptionsListAdapter extends BaseAdapter implements Filterable {
    private ArrayList<InterruptionModel> sortedList;
    private ArrayList<InterruptionModel> mList;
    private final LayoutInflater inflater;
    private Activity context;


    public LiveInterruptionsListAdapter(Activity context, ArrayList<InterruptionModel> registrationModels) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.mList = registrationModels;
    }

    @Override
    public int getCount() {
       // return mList.size();
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
            view = inflater.inflate(R.layout.live_interruptions_item_layout, viewGroup, false);
            viewHolder = new ViewHolder();
            viewHolder.tv_serial = (TextView) view.findViewById(R.id.tv_serial);
            viewHolder.tv_sub_station = (TextView) view.findViewById(R.id.tv_sub_station);
            viewHolder.tv_feeder_name = (TextView) view.findViewById(R.id.tv_feeder_name);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        final InterruptionModel interruptionModel = mList.get(i);
        viewHolder.tv_serial.setText(""+(i+1));
        viewHolder.tv_sub_station.setText(interruptionModel.getSubstationName());
        viewHolder.tv_feeder_name.setText(interruptionModel.getFeederName());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(context, LiveInterruptionsDetailActivity.class);
                in.putExtra(InterruptionModel.class.getSimpleName(), interruptionModel);
                context.startActivity(in);
            }
        });
        return view;
    }

    private static class ViewHolder {
        TextView tv_serial;
        TextView tv_sub_station;
        TextView tv_feeder_name;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return new Filter() {
            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mList = (ArrayList<InterruptionModel>) results.values; // has the filtered values
                notifyDataSetChanged();
                // notifies the data with new filtered values
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                List<InterruptionModel> FilteredArrList = new ArrayList<>();
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
                        InterruptionModel data = sortedList.get(i);
                        if (data.getSubstationName().toUpperCase().contains(constraint.toString().toUpperCase()) ||
                                data.getFeederName().toUpperCase().contains(constraint.toString().toUpperCase()) ||
                                data.getMeterNo().toUpperCase().contains(constraint.toString().toUpperCase())) {
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
