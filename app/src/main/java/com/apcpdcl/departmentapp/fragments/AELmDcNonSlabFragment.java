package com.apcpdcl.departmentapp.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.apcpdcl.departmentapp.R;
import com.apcpdcl.departmentapp.activities.ADEDcListTrackingActivity;
import com.apcpdcl.departmentapp.adapters.LMDCListNonSlabAdapter;
import com.apcpdcl.departmentapp.models.LmDcListModel;
import com.apcpdcl.departmentapp.utils.Constants;

import java.util.ArrayList;

import butterknife.ButterKnife;

public class AELmDcNonSlabFragment extends Fragment {
    public static String TAG = AELmDcNonSlabFragment.class.getSimpleName();
    private View rootView;
    private ADEDcListTrackingActivity mParent;
    private ArrayList<LmDcListModel> lmDcListModels = new ArrayList<>();
    private ListView lv_lmdc;
    private String from = "";
    public ProgressDialog pDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mParent = (ADEDcListTrackingActivity) getActivity();
        if (getArguments().containsKey(LmDcListModel.class.getSimpleName())) {
            lmDcListModels = (ArrayList<LmDcListModel>) getArguments().getSerializable(LmDcListModel.class.getSimpleName());
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
        lv_lmdc = (ListView) rootView.findViewById(R.id.lv_lmdc);
        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);
        LMDCListNonSlabAdapter eroExceptionsListAdapter = new LMDCListNonSlabAdapter(mParent, lmDcListModels);
        lv_lmdc.setAdapter(eroExceptionsListAdapter);

    }


}
