package com.apcpdcl.departmentapp.fragments;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.apcpdcl.departmentapp.R;
import com.apcpdcl.departmentapp.adapters.LMDCListAdapter;
import com.apcpdcl.departmentapp.models.LmDcListModel;
import com.apcpdcl.departmentapp.utils.Constants;

import java.util.ArrayList;

import butterknife.ButterKnife;

public class LmDcSlabFragment extends Fragment {
    public static String TAG = LmDcSlabFragment.class.getSimpleName();
    private View rootView;
    private ArrayList<LmDcListModel> lmDcListModels = new ArrayList<>();
    private ListView lv_lmdc;
    private TextView tv_no_data;
    private LinearLayout ll_three;
    private String from;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments().containsKey(LmDcListModel.class.getSimpleName())) {
            lmDcListModels = (ArrayList<LmDcListModel>) getArguments().getSerializable(LmDcListModel.class.getSimpleName());
        }
        if (getArguments().containsKey(Constants.FROM)) {
            from = getArguments().getString(Constants.FROM);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView != null)
            return rootView;
        rootView = inflater.inflate(R.layout.fragment_lmdc_list, container, false);
        ButterKnife.bind(this, rootView);
        init();
        return rootView;
    }

    private void init() {
        tv_no_data = (TextView) rootView.findViewById(R.id.tv_no_data);
        ll_three = (LinearLayout) rootView.findViewById(R.id.ll_three);
        lv_lmdc = (ListView) rootView.findViewById(R.id.lv_lmdc);
        if (lmDcListModels.size()>0) {
            LMDCListAdapter eroExceptionsListAdapter = new LMDCListAdapter(getActivity(), lmDcListModels, from);
            lv_lmdc.setAdapter(eroExceptionsListAdapter);
        }else {
            lv_lmdc.setVisibility(View.GONE);
            ll_three.setVisibility(View.GONE);
            tv_no_data.setVisibility(View.VISIBLE);
        }

    }

}
