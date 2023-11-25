package com.apcpdcl.departmentapp.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.apcpdcl.departmentapp.R;
import com.apcpdcl.departmentapp.models.ModuleModel;

import java.util.ArrayList;

public class DashboardTabAdapter extends ArrayAdapter<ModuleModel> {
    public DashboardTabAdapter(@NonNull Context context, ArrayList<ModuleModel> courseModelArrayList) {
        super(context, 0, courseModelArrayList);
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listitemView = convertView;
        if (listitemView == null) {
            // Layout Inflater inflates each item to be displayed in GridView.
            listitemView = LayoutInflater.from(getContext()).inflate(R.layout.modulue_layout, parent, false);
        }

        ModuleModel moduleModel = getItem(position);
////        TextView courseTV =  listitemView.findViewById(R.id.idTVTabName);
////        ImageView courseIV =  listitemView.findViewById(R.id.idIVTabIName);
//
//        courseTV.setText(moduleModel.getTabName());
//        courseIV.setImageResource(moduleModel.getImageName());
        return listitemView;
    }
}
