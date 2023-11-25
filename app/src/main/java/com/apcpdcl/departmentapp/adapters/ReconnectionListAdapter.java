package com.apcpdcl.departmentapp.adapters;

import android.content.Context;

import androidx.annotation.NonNull;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.apcpdcl.departmentapp.R;
import com.apcpdcl.departmentapp.activities.Reconnect;
import com.apcpdcl.departmentapp.utils.Utility;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ReconnectionListAdapter extends BaseAdapter implements Filterable {

    private ArrayList<JSONObject> sortedList = null;
    private ArrayList<JSONObject> mList;
    private final LayoutInflater inflater;
    private Context context;

    public ReconnectionListAdapter(Context context, ArrayList<JSONObject> reconnectionSrcModels) {
        inflater = LayoutInflater.from(context);
        this.mList = reconnectionSrcModels;
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
            viewHolder.tv_service_number = view.findViewById(R.id.tv_service_no);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        final ArrayList<JSONObject> reconnectionSrcModels = mList;
        viewHolder.tv_service_number.setText(reconnectionSrcModels.get(i).optString("bpno"));
        view.setOnClickListener(v -> {


            Intent in = new Intent(context, Reconnect.class);
            in.putExtra("OBJ", reconnectionSrcModels.get(i).toString());
            context.startActivity(in);
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
                // notifies the data with new filtered values
                mList = (ArrayList<JSONObject>) results.values; // has the filtered values
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                ArrayList<JSONObject> FilteredArrList = new ArrayList<>();
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
                        String data = sortedList.get(i).optString("bpno");
                        if (data.contains(constraint.toString())) {
                            FilteredArrList.add(sortedList.get(i));
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