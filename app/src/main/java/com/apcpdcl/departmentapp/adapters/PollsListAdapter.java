package com.apcpdcl.departmentapp.adapters;

import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.apcpdcl.departmentapp.R;
import com.apcpdcl.departmentapp.activities.ExistingPollListActivity;
import com.apcpdcl.departmentapp.models.PollModel;
import com.apcpdcl.departmentapp.utils.Utility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Haseen
 * on 01-03-2018.
 */

public class PollsListAdapter extends BaseAdapter implements Filterable {
    private ArrayList<PollModel> sortedList;
    private ArrayList<PollModel> mList;
    private final LayoutInflater inflater;


    public PollsListAdapter(ExistingPollListActivity context, ArrayList<PollModel> registrationModels) {
        inflater = LayoutInflater.from(context);
        this.mList = registrationModels;
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
            view = inflater.inflate(R.layout.polling_list_item, viewGroup, false);
            viewHolder = new ViewHolder();
            viewHolder.txt_heading = (TextView) view.findViewById(R.id.txt_heading);
            viewHolder.tv_yes_result = (TextView) view.findViewById(R.id.tv_yes_result);
            viewHolder.tv_no_result = (TextView) view.findViewById(R.id.tv_no_result);
            viewHolder.pb_yes = (ProgressBar) view.findViewById(R.id.pb_yes);
            viewHolder.pb_no = (ProgressBar) view.findViewById(R.id.pb_no);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        final PollModel registrationModel = mList.get(i);
        if (registrationModel.getQUESTION().contains("?")) {
            viewHolder.txt_heading.setText(registrationModel.getQUESTION());
        } else {
            viewHolder.txt_heading.setText(registrationModel.getQUESTION() + "?");
        }
        if (!Utility.isValueNullOrEmpty(registrationModel.getYES()) &&
                !registrationModel.getYES().equalsIgnoreCase("-")) {
            viewHolder.tv_yes_result.setVisibility(View.VISIBLE);
            viewHolder.tv_yes_result.setText(registrationModel.getYES() + "%");
            viewHolder.pb_yes.setProgress((int) Math.round(Double.parseDouble(registrationModel.getYES())));
        } else {
            viewHolder.tv_yes_result.setText("0%");
            viewHolder.pb_yes.setProgress(0);
        }
        if (!Utility.isValueNullOrEmpty(registrationModel.getNO()) &&
                !registrationModel.getNO().equalsIgnoreCase("-")) {
            viewHolder.tv_no_result.setVisibility(View.VISIBLE);
            viewHolder.tv_no_result.setText(registrationModel.getNO() + "%");
            viewHolder.pb_no.setProgress((int) Math.round(Double.parseDouble(registrationModel.getNO())));

        } else {
            viewHolder.pb_no.setProgress(0);
            viewHolder.tv_no_result.setText("0%");
        }

        return view;
    }

    private static class ViewHolder {
        TextView txt_heading;
        TextView tv_yes_result;
        TextView tv_no_result;
        ProgressBar pb_yes;
        ProgressBar pb_no;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return new Filter() {
            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mList = (ArrayList<PollModel>) results.values; // has the filtered values
                notifyDataSetChanged();
                // notifies the data with new filtered values
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                List<PollModel> FilteredArrList = new ArrayList<>();
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
                        PollModel data = sortedList.get(i);
                        if (data.getQUESTION().contains(constraint.toString().toUpperCase())) {
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
