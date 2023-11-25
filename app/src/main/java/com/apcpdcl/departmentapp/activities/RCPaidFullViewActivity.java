package com.apcpdcl.departmentapp.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import com.apcpdcl.departmentapp.R;
import com.apcpdcl.departmentapp.models.RCPaidModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RCPaidFullViewActivity extends AppCompatActivity {

    @BindView(R.id.tv_service_no)
    TextView tv_service_no;
    @BindView(R.id.tv_address)
    TextView tv_address;
    @BindView(R.id.tv_mobile_no)
    TextView tv_mobile_no;
    @BindView(R.id.tv_feeder_code)
    TextView tv_feeder_code;
    @BindView(R.id.tv_transformer)
    TextView tv_transformer;
    @BindView(R.id.tv_pole)
    TextView tv_pole;
    @BindView(R.id.tv_bill_date)
    TextView tv_bill_date;
    @BindView(R.id.tv_pay_date)
    TextView tv_pay_date;
    @BindView(R.id.tv_pr_num)
    TextView tv_pr_num;
    @BindView(R.id.tv_cc_charges)
    TextView tv_cc_charges;
    @BindView(R.id.tv_acd)
    TextView tv_acd;
    @BindView(R.id.tv_rc)
    TextView tv_rc;
    @BindView(R.id.tv_total_amt)
    TextView tv_total_amt;
    @BindView(R.id.tv_counter)
    TextView tv_counter;
    @BindView(R.id.tv_pending_bal)
    TextView tv_pending_bal;

    @BindView(R.id.tv_bp_no)
    TextView tv_bp_no;
    @BindView(R.id.btn_submit)
    Button btn_submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rc_paid_fullview);
        ButterKnife.bind(this);
        setData();
    }

    private void setData() {
        RCPaidModel rcPaidModel = (RCPaidModel) getIntent().getSerializableExtra(RCPaidModel.class.getSimpleName());
        tv_service_no.setText(rcPaidModel.getCMUSCNO());
        tv_bp_no.setText(rcPaidModel.getBPNUMBER());
        tv_address.setText(rcPaidModel.getCMDRNUM() + " " + rcPaidModel.getCMSTREET());
        tv_mobile_no.setText(rcPaidModel.getCMCELL());
        tv_feeder_code.setText(rcPaidModel.getCMFDRCD());
        tv_transformer.setText(rcPaidModel.getCMDTRCD());
        tv_pole.setText(rcPaidModel.getCMPOLENUM());
        tv_bill_date.setText(rcPaidModel.getBLCLRDT());
        tv_pay_date.setText(rcPaidModel.getPTPRDT());
        tv_pr_num.setText(rcPaidModel.getPTPRNO());
        tv_cc_charges.setText(rcPaidModel.getPTBLAMT());
        tv_acd.setText(rcPaidModel.getPTMISAMT());
        tv_rc.setText(rcPaidModel.getPTRECONCHG());
        tv_total_amt.setText(rcPaidModel.getTOT());
        tv_counter.setText(rcPaidModel.getPTCOUNTER());
        tv_pending_bal.setText(rcPaidModel.getDCList_Pending_Amt());
    }

    @OnClick(R.id.btn_submit)
    void navigateBack() {
        finish();
    }


}
