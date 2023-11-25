package com.apcpdcl.departmentapp.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.apcpdcl.departmentapp.R;
import com.apcpdcl.departmentapp.models.LmDcListModel;

import java.util.ArrayList;

/**
 * Created by Haseen
 * on 01-03-2018.
 */

public class LMDCListNonSlabAdapter extends BaseAdapter {
    private ArrayList<LmDcListModel> mList;
    private final LayoutInflater inflater;
    private Activity context;


    public LMDCListNonSlabAdapter(Activity context, ArrayList<LmDcListModel> lmDcListModels) {
        inflater = LayoutInflater.from(context);
        this.context = context;
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

        return view;
    }

    private static class ViewHolder {
        TextView tv_user_code;
        TextView tv_pending;
        TextView tv_accepted;
        TextView tv_rejected;
    }
}
