package com.apcpdcl.departmentapp.adapters;

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
import com.apcpdcl.departmentapp.activities.AddSubStationCoordinates;
import com.apcpdcl.departmentapp.activities.SubStationListActivity;
import com.apcpdcl.departmentapp.models.SubStationModel;
import com.apcpdcl.departmentapp.utils.Utility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Haseen
 * on 01-03-2018.
 */

public class SubStationListAdapter extends BaseAdapter implements Filterable {
    private ArrayList<SubStationModel> sortedList;
    private ArrayList<SubStationModel> mList;
    private final LayoutInflater inflater;
    private SubStationListActivity subStationListActivity;

    public SubStationListAdapter(SubStationListActivity subStationListActivity, ArrayList<SubStationModel> registrationModels) {
        inflater = LayoutInflater.from(subStationListActivity);
        this.mList = registrationModels;
        this.subStationListActivity = subStationListActivity;
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
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        final SubStationModel registrationModel = mList.get(i);
        viewHolder.tv_service_number.setText(registrationModel.getSSNAME());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (registrationModel.getEXISTS().equalsIgnoreCase("YES")) {
                    Utility.showCustomOKOnlyDialog(subStationListActivity, "Co-ordinates already added to " + registrationModel.getSSNAME());
                } else {
                    if (Utility.isLocationEnabled(subStationListActivity)) {
                        Intent intent = new Intent(subStationListActivity, AddSubStationCoordinates.class);
                        intent.putExtra("SS_NAME", registrationModel.getSSNAME());
                        intent.putExtra("SS_CODE", registrationModel.getSSCODE());
                        subStationListActivity.startActivity(intent);
                    } else {
                        subStationListActivity.displayLocationSettingsRequest(subStationListActivity);
                    }
                }
            }
        });
        return view;
    }

    private static class ViewHolder {
        TextView tv_service_number;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return new Filter() {
            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mList = (ArrayList<SubStationModel>) results.values; // has the filtered values
                notifyDataSetChanged();
                // notifies the data with new filtered values
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                List<SubStationModel> FilteredArrList = new ArrayList<>();
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
                        SubStationModel data = sortedList.get(i);
                        if (data.getSSNAME().contains(constraint.toString().toUpperCase())) {
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
