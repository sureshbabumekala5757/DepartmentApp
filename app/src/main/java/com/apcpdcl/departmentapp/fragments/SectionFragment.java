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
import com.apcpdcl.departmentapp.activities.ExceptionReportListActivity;
import com.apcpdcl.departmentapp.adapters.SectionExceptionsListAdapter;
import com.apcpdcl.departmentapp.models.ExceptionReportModel;
import com.apcpdcl.departmentapp.models.ExceptionalModel;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by haseen
 * on 17/12/18.
 **/
@SuppressWarnings("RedundantCast")
public class SectionFragment extends Fragment {

    public static String TAG = SectionFragment.class.getSimpleName();

    private View rootView;
    private ExceptionReportListActivity mParent;
    private ExceptionReportModel exceptionalModel;
    @BindView(R.id.lv_exceptions)
    ListView lv_exceptions;

    @BindView(R.id.tv_name_lable)
    TextView tv_name_lable;
    @BindView(R.id.tv_header_name)
    TextView tv_header_name;

    @BindView(R.id.tv_name)
    TextView tv_name;
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
    @BindView(R.id.tv_name_t)
    TextView tv_name_t;
    private String mType = "";
    private boolean mShow;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mParent = (ExceptionReportListActivity) getActivity();
        if (getArguments().containsKey(ExceptionalModel.class.getSimpleName())) {
            exceptionalModel = (ExceptionReportModel) getArguments().getSerializable(ExceptionalModel.class.getSimpleName());
            mType = getArguments().getString("Type");
            mShow = getArguments().getBoolean("Show", false);
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
        tv_name.setText("SECTION  NAME");
        tv_name_t.setText("SECTION  NAME");

        tv_name_lable.setText("SUB-DIVISION Name : ");
        tv_header_name.setText(exceptionalModel.getDIV_Name());
        if (mShow) {
            ll_total_three.setVisibility(View.GONE);
            ll_three.setVisibility(View.GONE);
            ll_five.setVisibility(View.VISIBLE);
            ll_total_five.setVisibility(View.VISIBLE);
            tv_status_lt.setText(exceptionalModel.getStatus() + " < " + mType + " Months");
            tv_status_gt.setText(exceptionalModel.getStatus() + " > " + mType + " Months");
            tv_total_lt.setText(exceptionalModel.getLS_Status());
            tv_total_gt.setText(exceptionalModel.getGS_Status());
            tv_mtr_total.setText(exceptionalModel.getMeter_Replaced());
            tv_bal_total.setText(exceptionalModel.getBalance());
        } else {
            ll_total_three.setVisibility(View.VISIBLE);
            ll_three.setVisibility(View.VISIBLE);
            ll_five.setVisibility(View.GONE);
            ll_total_five.setVisibility(View.GONE);
            tv_status_lt_t.setText(exceptionalModel.getStatus() + " < " + mType + " Months");
            tv_status_gt_t.setText(exceptionalModel.getStatus() + " > " + mType + " Months");
            tv_total_lt_t.setText(exceptionalModel.getLS_Status());
            tv_total_gt_t.setText(exceptionalModel.getGS_Status());
        }
        SectionExceptionsListAdapter eroExceptionsListAdapter = new SectionExceptionsListAdapter(mParent, exceptionalModel.getSECTION(), mShow);
        lv_exceptions.setAdapter(eroExceptionsListAdapter);
    }

}
