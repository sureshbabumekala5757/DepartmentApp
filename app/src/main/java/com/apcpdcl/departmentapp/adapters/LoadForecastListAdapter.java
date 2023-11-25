package com.apcpdcl.departmentapp.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.apcpdcl.departmentapp.R;
import com.apcpdcl.departmentapp.models.LoadForecastModel;

import java.util.ArrayList;

/**
 * Created by Haseen
 * on 01-03-2018.
 */

public class LoadForecastListAdapter extends BaseAdapter {
    private ArrayList<LoadForecastModel> mList;
    private final LayoutInflater inflater;

    public LoadForecastListAdapter(Activity context, ArrayList<LoadForecastModel> registrationModels) {
        inflater = LayoutInflater.from(context);
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
            view = inflater.inflate(R.layout.load_forecast_item_layout, viewGroup, false);
            viewHolder = new ViewHolder();
            viewHolder.tv_serial = (TextView) view.findViewById(R.id.tv_serial);
            viewHolder.tv_time = (TextView) view.findViewById(R.id.tv_time);
            viewHolder.tv_nature = (TextView) view.findViewById(R.id.tv_nature);
            viewHolder.tv_consumption = (TextView) view.findViewById(R.id.tv_consumption);
            viewHolder.tv_consumption_prev = (TextView) view.findViewById(R.id.tv_consumption_prev);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        final LoadForecastModel outagesModel = mList.get(i);
        viewHolder.tv_serial.setText("" + (i + 1));
        viewHolder.tv_time.setText(outagesModel.getLoadForeCastDate());
        viewHolder.tv_nature.setText(outagesModel.getNatureofLoad());
        viewHolder.tv_consumption.setText(""+outagesModel.getConsumption());
        viewHolder.tv_consumption_prev.setText(""+outagesModel.getPrevConsumption());


        return view;
    }

    private static class ViewHolder {
        TextView tv_serial;

        TextView tv_time;
        TextView tv_nature;
        TextView tv_consumption;
        TextView tv_consumption_prev;
    }
}
