package com.apcpdcl.departmentapp.fragments;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.apcpdcl.departmentapp.R;
import com.apcpdcl.departmentapp.activities.ExceptionReportListActivity;
import com.apcpdcl.departmentapp.adapters.EROExceptionsListAdapter;
import com.apcpdcl.departmentapp.models.ExceptionalModel;
import com.apcpdcl.departmentapp.utils.Utility;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by haseen
 * on 17/12/18.
 **/
@SuppressWarnings("RedundantCast")
public class EROFragment extends Fragment implements AdapterView.OnItemClickListener {

    public static String TAG = EROFragment.class.getSimpleName();

    private View rootView;
    private ExceptionReportListActivity mParent;
    private ExceptionalModel exceptionalModel;
    @BindView(R.id.lv_exceptions)
    ListView lv_exceptions;

    @BindView(R.id.tv_name_lable)
    TextView tv_name_lable;
    @BindView(R.id.tv_header_name)
    TextView tv_header_name;

    @BindView(R.id.tv_status_lt)
    TextView tv_status_lt;
    @BindView(R.id.tv_status_gt)
    TextView tv_status_gt;
    @BindView(R.id.tv_status_lt_t)
    TextView tv_status_lt_t;
    @BindView(R.id.tv_status_gt_t)
    TextView tv_status_gt_t;

    @BindView(R.id.ll_three)
    LinearLayout ll_three;
    @BindView(R.id.ll_five)
    LinearLayout ll_five;
    @BindView(R.id.ll_total_five)
    LinearLayout ll_total_five;
    @BindView(R.id.ll_total_three)
    LinearLayout ll_total_three;



    @BindView(R.id.tv_total_lt)
    TextView tv_total_lt;
    @BindView(R.id.tv_total_gt)
    TextView tv_total_gt;
    @BindView(R.id.tv_total_lt_t)
    TextView tv_total_lt_t;
    @BindView(R.id.tv_total_gt_t)
    TextView tv_total_gt_t;
    @BindView(R.id.tv_mtr_total)
    TextView tv_mtr_total;
    @BindView(R.id.tv_bal_total)
    TextView tv_bal_total;
    private String mType ="";
    private boolean mShow ;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mParent = (ExceptionReportListActivity) getActivity();
        if (getArguments().containsKey(ExceptionalModel.class.getSimpleName())) {
            exceptionalModel = (ExceptionalModel) getArguments().getSerializable(ExceptionalModel.class.getSimpleName());
            mType =  getArguments().getString("Type");
            mShow =  getArguments().getBoolean("Show", false);
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
        rootView = inflater.inflate(R.layout.fragment_ero, container, false);
        ButterKnife.bind(this, rootView);
        init();
        return rootView;
    }

    private void init() {
        tv_name_lable.setText("ERO Name : ");
        tv_header_name.setText(exceptionalModel.getERO().get(0).getERO_Name());
        if (mShow) {
            ll_total_three.setVisibility(View.GONE);
            ll_three.setVisibility(View.GONE);
            ll_five.setVisibility(View.VISIBLE);
            ll_total_five.setVisibility(View.VISIBLE);
            tv_status_lt.setText(exceptionalModel.getERO().get(0).getStatus() + " < " + mType + " Months");
            tv_status_gt.setText(exceptionalModel.getERO().get(0).getStatus() + " > " + mType + " Months");
            tv_total_lt.setText(exceptionalModel.getERO().get(0).getLS_Status());
            tv_total_gt.setText(exceptionalModel.getERO().get(0).getGS_Status());
            tv_mtr_total.setText(exceptionalModel.getERO().get(0).getMeter_Replaced());
            tv_bal_total.setText(exceptionalModel.getERO().get(0).getBalance());
        }else {
            ll_total_three.setVisibility(View.VISIBLE);
            ll_three.setVisibility(View.VISIBLE);
            ll_five.setVisibility(View.GONE);
            ll_total_five.setVisibility(View.GONE);
            tv_status_lt_t.setText(exceptionalModel.getERO().get(0).getStatus() + " < " + mType + " Months");
            tv_status_gt_t.setText(exceptionalModel.getERO().get(0).getStatus() + " > " + mType + " Months");
            tv_total_lt_t.setText(exceptionalModel.getERO().get(0).getLS_Status());
            tv_total_gt_t.setText(exceptionalModel.getERO().get(0).getGS_Status());
        }
        EROExceptionsListAdapter eroExceptionsListAdapter = new EROExceptionsListAdapter(mParent, exceptionalModel.getERO(), mShow);
        lv_exceptions.setAdapter(eroExceptionsListAdapter);
        lv_exceptions.setOnItemClickListener(this);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Bundle bundle = new Bundle();
        bundle.putString("Type", mType);
        bundle.putBoolean("Show", mShow);
        bundle.putSerializable(ExceptionalModel.class.getSimpleName(), exceptionalModel);
        Utility.navigateFragment(new SubDivisionFragment(), SubDivisionFragment.TAG, bundle, mParent);
    }
}
