package com.apcpdcl.departmentapp.adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.apcpdcl.departmentapp.R;
import com.apcpdcl.departmentapp.activities.AAODcListTrackingActivity;
import com.apcpdcl.departmentapp.activities.ADEDcListTrackingActivity;
import com.apcpdcl.departmentapp.activities.AEDcListTrackingActivity;
import com.apcpdcl.departmentapp.models.LmDcListModel;
import com.apcpdcl.departmentapp.utils.Constants;

import java.util.ArrayList;

/**
 * Created by Haseen
 * on 01-03-2018.
 */

public class LMDCListAdapter extends BaseAdapter {
    private ArrayList<LmDcListModel> mList;
    private final LayoutInflater inflater;
    private Activity context;
    private String from;


    public LMDCListAdapter(Activity context, ArrayList<LmDcListModel> lmDcListModels, String from) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.from = from;
        this.mList = lmDcListModels;
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
            view = inflater.inflate(R.layout.item_lm_dc_layout, viewGroup, false);
            viewHolder = new ViewHolder();
            viewHolder.tv_user_code = (TextView) view.findViewById(R.id.tv_user_code);
            viewHolder.tv_pending = (TextView) view.findViewById(R.id.tv_pending);
            viewHolder.tv_accepted = (TextView) view.findViewById(R.id.tv_accepted);
            viewHolder.tv_rejected = (TextView) view.findViewById(R.id.tv_rejected);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        final LmDcListModel lmDcListModel = mList.get(i);

        viewHolder.tv_user_code.setText(lmDcListModel.getUSERCODE());
        viewHolder.tv_pending.setText(lmDcListModel.getPENDING());
        viewHolder.tv_accepted.setText(lmDcListModel.getACCEPTED());
        viewHolder.tv_rejected.setText(lmDcListModel.getREJECTED());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (from.equalsIgnoreCase(AAODcListTrackingActivity.class.getSimpleName())) {
                    if (lmDcListModel.isNS()) {
                        Intent intent = new Intent(context, ADEDcListTrackingActivity.class);
                        intent.putExtra(Constants.USER_ID, lmDcListModel.getUSERCODE());
                        context.startActivity(intent);
                    } else {
                        Intent intent = new Intent(context, AEDcListTrackingActivity.class);
                        intent.putExtra(Constants.USER_ID, lmDcListModel.getUSERCODE());
                        context.startActivity(intent);
                    }
                }
            }
        });
        return view;
    }

    private static class ViewHolder {
        TextView tv_user_code;
        TextView tv_pending;
        TextView tv_accepted;
        TextView tv_rejected;
    }
}
