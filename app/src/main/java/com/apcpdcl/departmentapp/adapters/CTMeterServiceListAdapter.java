package com.apcpdcl.departmentapp.adapters;

import android.content.Context;
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
import com.apcpdcl.departmentapp.activities.CTMetersFrom;
import com.apcpdcl.departmentapp.model.CTMServiceModel;
import com.apcpdcl.departmentapp.utils.Utility;

import java.util.ArrayList;
import java.util.List;

public class CTMeterServiceListAdapter extends BaseAdapter implements Filterable {
    private ArrayList<CTMServiceModel> sortedList;
    private ArrayList<CTMServiceModel> mList;
    private final LayoutInflater inflater;
    private Context context;

    public CTMeterServiceListAdapter(Context context, ArrayList<CTMServiceModel> ctmServiceModel) {
        inflater = LayoutInflater.from(context);
        this.mList = ctmServiceModel;
        this.context = context;
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
        final CTMServiceModel registrationModel = mList.get(i);
        viewHolder.tv_service_number.setText(registrationModel.getCUSCNO());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(context, CTMetersFrom.class);
                in.putExtra("CTMSERVICENO",registrationModel.getCUSCNO());
                context.startActivity(in);
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
                mList = (ArrayList<CTMServiceModel>) results.values; // has the filtered values
                notifyDataSetChanged();
                // notifies the data with new filtered values
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                List<CTMServiceModel> FilteredArrList = new ArrayList<>();
                if (sortedList == null) {
                    sortedList = new ArrayList<>(mList); // saves the original data in mOriginalValues
                }
                /* ******
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
                        CTMServiceModel data = sortedList.get(i);
                        if (data.getCUSCNO().contains(constraint.toString().toUpperCase())) {
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
