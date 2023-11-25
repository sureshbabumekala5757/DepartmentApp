package com.apcpdcl.departmentapp.adapters;

import android.content.Intent;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.apcpdcl.departmentapp.R;
import com.apcpdcl.departmentapp.activities.DeleteMeterChangeActivity;
import com.apcpdcl.departmentapp.activities.MeterChangeListActivity;
import com.apcpdcl.departmentapp.activities.MeterChangeModifyFormActivity;
import com.apcpdcl.departmentapp.models.MeterChangeListModel;
import com.apcpdcl.departmentapp.utils.Constants;
import com.apcpdcl.departmentapp.utils.Utility;

import java.util.ArrayList;
import java.util.List;

public class MeterChangeListAdapter extends BaseAdapter implements Filterable {
    private ArrayList<MeterChangeListModel> sortedList;
    private ArrayList<MeterChangeListModel> mList;
    private final LayoutInflater inflater;
    private MeterChangeListActivity meterChangeListActivity;
    private String mFrom;

    public MeterChangeListAdapter(MeterChangeListActivity meterChangeListActivity,
                                  ArrayList<MeterChangeListModel> registrationModels, String mFrom) {
        inflater = LayoutInflater.from(meterChangeListActivity);
        this.mList = registrationModels;
        this.meterChangeListActivity = meterChangeListActivity;
        this.mFrom = mFrom;
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
            view = inflater.inflate(R.layout.item_meter_change_layout, viewGroup, false);

            viewHolder = new ViewHolder();
            viewHolder.tv_service_number = (TextView)view.findViewById(R.id.tv_service_no);
            viewHolder.tv_meter_change_date =(TextView) view.findViewById(R.id.tv_meter_change_date);
            viewHolder.tv_old_meter_no = (TextView)view.findViewById(R.id.tv_old_meter_no);
            viewHolder.tv_new_meter_no =(TextView) view.findViewById(R.id.tv_new_meter_no);
            viewHolder.tv_kwh_units = (TextView)view.findViewById(R.id.tv_kwh_units);
            viewHolder.tv_kvah_units = (TextView)view.findViewById(R.id.tv_kvah_units);
            viewHolder.btn_approve = (Button) view.findViewById(R.id.btn_approve);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        final MeterChangeListModel meterChangeListModel = mList.get(i);
        viewHolder.tv_service_number.setText(meterChangeListModel.getUSCNO());
        viewHolder.tv_meter_change_date.setText(meterChangeListModel.getMTRCHGDT());
        viewHolder.tv_old_meter_no.setText(meterChangeListModel.getOLDMTRNO());
        viewHolder.tv_new_meter_no.setText(meterChangeListModel.getNEWMTRNO());
        viewHolder.tv_kwh_units.setText(meterChangeListModel.getKWHUNITS());
        viewHolder.tv_kvah_units.setText(meterChangeListModel.getKVAHUNITS());
        if (mFrom.equalsIgnoreCase("Delete")) {
            viewHolder.btn_approve.setVisibility(View.GONE);
        }
        viewHolder.btn_approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                meterChangeListActivity.approveMeterChangeRequest(meterChangeListModel);
            }
        });
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFrom.equalsIgnoreCase("Delete")) {
                    Intent intent = new Intent(meterChangeListActivity, DeleteMeterChangeActivity.class);
                    intent.putExtra(Constants.USCNO, meterChangeListModel.getUSCNO());
                    intent.putExtra(Constants.MTRCHGDT, meterChangeListModel.getMTRCHGDT());
                    intent.putExtra(Constants.NEWMTRNO, meterChangeListModel.getNEWMTRNO());
                    meterChangeListActivity.startActivity(intent);
                }else {
                    Intent intent = new Intent(meterChangeListActivity, MeterChangeModifyFormActivity.class);
                    intent.putExtra(Constants.USCNO, meterChangeListModel.getUSCNO());
                    intent.putExtra(Constants.MTRCHGDT, meterChangeListModel.getMTRCHGDT());
                    intent.putExtra(Constants.NEWMTRNO, meterChangeListModel.getNEWMTRNO());
                    meterChangeListActivity.startActivity(intent);
                }
            }
        });
        return view;
    }

    private static class ViewHolder {
        TextView tv_service_number;
        TextView tv_meter_change_date;
        TextView tv_old_meter_no;
        TextView tv_new_meter_no;
        TextView tv_kvah_units;
        TextView tv_kwh_units;
        Button btn_approve;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return new Filter() {
            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mList = (ArrayList<MeterChangeListModel>) results.values; // has the filtered values
                notifyDataSetChanged();
                // notifies the data with new filtered values
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                List<MeterChangeListModel> FilteredArrList = new ArrayList<>();
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
                        MeterChangeListModel data = sortedList.get(i);
                        if (data.getUSCNO().contains(constraint.toString().toUpperCase())) {
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
