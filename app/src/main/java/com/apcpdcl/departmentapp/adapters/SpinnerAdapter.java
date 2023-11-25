package com.apcpdcl.departmentapp.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.apcpdcl.departmentapp.R;

import java.util.ArrayList;

/**
 * Created by Haseena
 * on 23/02/2018.
 **/

public class SpinnerAdapter extends ArrayAdapter<String> {
    private ArrayList<String> mFilterDropDownModels;
    private LayoutInflater inflater;


    public SpinnerAdapter(Context context, int textViewResourceId,
                          ArrayList<String> mFilterDropDownModels) {
        super(context, textViewResourceId, mFilterDropDownModels);
        this.mFilterDropDownModels = mFilterDropDownModels;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return mFilterDropDownModels.size();
    }

    public String getItem(int position) {
        return mFilterDropDownModels.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        final Holder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.spinner_item_layout, parent, false);
            holder = new Holder();
            holder.txt_value = (TextView) convertView.findViewById(R.id.tvSpinner);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        holder.txt_value.setText(mFilterDropDownModels.get(position));
        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                @NonNull ViewGroup parent) {
        final Holder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.spinner_item_layout, parent, false);
            holder = new Holder();
            holder.txt_value = (TextView) convertView.findViewById(R.id.tvSpinner);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        holder.txt_value.setText(mFilterDropDownModels.get(position));
        return convertView;
    }

    private class Holder {
        TextView txt_value;
    }

    @Override
    public boolean isEnabled(int position) {
        return position > -1; // Don't allow the 'nothing selected'
        // item to be picked.
    }
}

