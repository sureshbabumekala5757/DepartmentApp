package com.apcpdcl.departmentapp.activities;

import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.StrictMode;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.apcpdcl.departmentapp.R;
import com.apcpdcl.departmentapp.customviews.ExpandableLayoutListener;
import com.apcpdcl.departmentapp.customviews.Utils;
import com.apcpdcl.departmentapp.models.AdvanceAbstractModel;
import com.apcpdcl.departmentapp.shared.AppPrefs;
import com.apcpdcl.departmentapp.utils.Utility;
import com.apcpdcl.departmentapp.customviews.ExpandableRelativeLayout;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DC_AbstractListActivity extends AppCompatActivity {

    @BindView(R.id.sv_main)
    ScrollView sv_main;

    @BindView(R.id.rl_all)
    RelativeLayout rl_all;
    @BindView(R.id.iv_all)
    ImageView iv_all;
    @BindView(R.id.el_all)
    ExpandableRelativeLayout el_all;

    @BindView(R.id.rl_govt)
    RelativeLayout rl_govt;
    @BindView(R.id.iv_govt)
    ImageView iv_govt;
    @BindView(R.id.el_govt)
    ExpandableRelativeLayout el_govt;

    @BindView(R.id.rl_collections)
    RelativeLayout rl_collections;
    @BindView(R.id.iv_collections)
    ImageView iv_collections;
    @BindView(R.id.el_collections)
    ExpandableRelativeLayout el_collections;

    @BindView(R.id.rl_balance)
    RelativeLayout rl_balance;
    @BindView(R.id.iv_balance)
    ImageView iv_balance;
    @BindView(R.id.el_balance)
    ExpandableRelativeLayout el_balance;

    @BindView(R.id.rl_status)
    RelativeLayout rl_status;
    @BindView(R.id.iv_status)
    ImageView iv_status;
    @BindView(R.id.el_status)
    ExpandableRelativeLayout el_status;

    @BindView(R.id.tv_services_all)
    TextView tv_services_all;
    @BindView(R.id.tv_total)
    TextView tv_total;
    @BindView(R.id.tv_services_all_ns)
    TextView tv_services_all_ns;
    @BindView(R.id.tv_total_ns)
    TextView tv_total_ns;
    @BindView(R.id.tv_services)
    TextView tv_services;
    @BindView(R.id.tv_amount)
    TextView tv_amount;

    @BindView(R.id.tv_services_all_govt)
    TextView tv_services_all_govt;
    @BindView(R.id.tv_total_govt)
    TextView tv_total_govt;
    @BindView(R.id.tv_services_govt_ns)
    TextView tv_services_govt_ns;
    @BindView(R.id.tv_total_govt_ns)
    TextView tv_total_govt_ns;
    @BindView(R.id.tv_services_govt)
    TextView tv_services_govt;
    @BindView(R.id.tv_amount_govt)
    TextView tv_amount_govt;


    @BindView(R.id.tv_services_all_col)
    TextView tv_services_all_col;
    @BindView(R.id.tv_total_col)
    TextView tv_total_col;
    @BindView(R.id.tv_services_col_ns)
    TextView tv_services_col_ns;
    @BindView(R.id.tv_total_col_ns)
    TextView tv_total_col_ns;
    @BindView(R.id.tv_services_col)
    TextView tv_services_col;
    @BindView(R.id.tv_amount_col)
    TextView tv_amount_col;

    @BindView(R.id.tv_services_all_bal)
    TextView tv_services_all_bal;
    @BindView(R.id.tv_total_bal)
    TextView tv_total_bal;
    @BindView(R.id.tv_services_bal_ns)
    TextView tv_services_bal_ns;
    @BindView(R.id.tv_total_bal_ns)
    TextView tv_total_bal_ns;
    @BindView(R.id.tv_services_bal)
    TextView tv_services_bal;
    @BindView(R.id.tv_amount_bal)
    TextView tv_amount_bal;

    @BindView(R.id.tv_services_all_bal_col)
    TextView tv_services_all_bal_col;
    @BindView(R.id.tv_total_bal_col)
    TextView tv_total_bal_col;
    @BindView(R.id.tv_services_bal_ns_col)
    TextView tv_services_bal_ns_col;
    @BindView(R.id.tv_total_bal_ns_col)
    TextView tv_total_bal_ns_col;
    @BindView(R.id.tv_services_bal_col)
    TextView tv_services_bal_col;
    @BindView(R.id.tv_amount_bal_col)
    TextView tv_amount_bal_col;

    @BindView(R.id.tv_verify)
    TextView tv_verify;
    @BindView(R.id.tv_payment)
    TextView tv_payment;
    @BindView(R.id.tv_disc)
    TextView tv_disc;
    @BindView(R.id.tv_unable)
    TextView tv_unable;
    @BindView(R.id.tv_verify_amount)
    TextView tv_verify_amount;
    @BindView(R.id.tv_payment_amount)
    TextView tv_payment_amount;
    @BindView(R.id.tv_disc_amount)
    TextView tv_disc_amount;
    @BindView(R.id.tv_unable_amount)
    TextView tv_unable_amount;

    @BindView(R.id.tv_verify_ns)
    TextView tv_verify_ns;
    @BindView(R.id.tv_payment_ns)
    TextView tv_payment_ns;
    @BindView(R.id.tv_disc_ns)
    TextView tv_disc_ns;
    @BindView(R.id.tv_unable_ns)
    TextView tv_unable_ns;
    @BindView(R.id.tv_verify_amount_ns)
    TextView tv_verify_amount_ns;
    @BindView(R.id.tv_payment_amount_ns)
    TextView tv_payment_amount_ns;
    @BindView(R.id.tv_disc_amount_ns)
    TextView tv_disc_amount_ns;
    @BindView(R.id.tv_unable_amount_ns)
    TextView tv_unable_amount_ns;


    @BindView(R.id.tv_services_sl_stat)
    TextView tv_services_sl_stat;
    @BindView(R.id.tv_amount_sl_stat)
    TextView tv_amount_sl_stat;

    @BindView(R.id.tv_services_ns_stat)
    TextView tv_services_ns_stat;
    @BindView(R.id.tv_amount_ns_stat)
    TextView tv_amount_ns_stat;

    @BindView(R.id.toolbar_title)
    TextView toolbar_title;

    private ProgressDialog pDialog;
    private String lmcode = "";
    private String sUserName, sUserCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dc_abstract_list_layout);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        String date = new SimpleDateFormat("MMM-yy", Locale.getDefault()).format(new Date());
        toolbar_title.setText("DC-List Reports for " + date);
//        SharedPreferences prefs = getSharedPreferences("loginPrefs", 0);
//        lmcode = prefs.getString("LMCode", "");
        //Shared Data
        sUserName = AppPrefs.getInstance(this).getString("USERNAME","");
        sUserCode = AppPrefs.getInstance(this).getString("USERID","");
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        if (Utility.isNetworkAvailable(this)) {
            pDialog.show();
            JSONObject requestObj = new JSONObject();
            try{
                requestObj.put("LMCODE",sUserCode);//sUserCode
            }catch (Exception e){

            }
            getAbstractCount(requestObj);
        } else {
            Utility.showCustomOKOnlyDialog(this,
                    Utility.getResourcesString(this, R.string.no_internet));
        }
        setExpandableButtonAnimators(el_all, iv_all);
        setExpandableButtonAnimators(el_govt, iv_govt);
        setExpandableButtonAnimators(el_collections, iv_collections);
        setExpandableButtonAnimators(el_balance, iv_balance);
        setExpandableButtonAnimators(el_status, iv_status);
    }

    /* Set Expandable buttons animator*/
    private void setExpandableButtonAnimators(ExpandableRelativeLayout expandableRelativeLayout,
                                              final ImageView imageView) {
        expandableRelativeLayout.setListener(new ExpandableLayoutListener() {
            @Override
            public void onAnimationStart() {

            }

            @Override
            public void onAnimationEnd() {

            }

            @Override
            public void onPreOpen() {
                createRotateAnimator(imageView, 0f, 180f).start();
            }

            @Override
            public void onPreClose() {
                createRotateAnimator(imageView, 180f, 0f).start();
            }

            @Override
            public void onOpened() {

            }

            @Override
            public void onClosed() {

            }
        });
    }

    /*Rotate Animator for buttons*/
    public ObjectAnimator createRotateAnimator(final View target, final float from, final float to) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(target, "rotation", from, to);
        animator.setDuration(300);
        animator.setInterpolator(Utils.createInterpolator(Utils.LINEAR_INTERPOLATOR));
        return animator;
    }

    @OnClick({R.id.rl_all, R.id.iv_all})
    void toggleAllDetails() {
        el_all.toggle();
    }

    @OnClick({R.id.rl_govt, R.id.iv_govt})
    void toggleGovtDetails() {
        el_govt.toggle();
    }

    @OnClick({R.id.rl_collections, R.id.iv_collections})
    void toggleCollectionDetails() {
        el_collections.toggle();
    }

    @OnClick({R.id.rl_balance, R.id.iv_balance})
    void toggleBalanceDetails() {
        el_balance.toggle();
    }

    @OnClick({R.id.rl_status, R.id.iv_status})
    void toggleStatusDetails() {
        el_status.toggle();
    }

    /* *
     *Get Abstract Count
     * */
    private void getAbstractCount(JSONObject requestObj) {

//        RequestParams requestParams = new RequestParams();
//        requestParams.put("LMCODE", lmcode);
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(50000);
        HttpEntity entity;
        try {
            entity = new StringEntity(requestObj.toString());
            BasicHeader[] headers = new BasicHeader[]{new BasicHeader("Authorization", "Basic " + AppPrefs.getInstance(getApplicationContext()).getString("USER_AUTH", ""))};
            client.post(this, "https://apcpcdcl-test-k5qoqm5y.it-cpi012-rt.cfapps.ap21.hana.ondemand.com/http/DeptApp/SAPISU/DisconnectionReport/DEV", headers, entity, "application/json", new AsyncHttpResponseHandler() {
                //client.post(Constants.GET_ADV_ABSTRACT_COUNT, requestParams, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(String response) {
                    Utility.showLog("onSuccess", response);
                    if (pDialog != null && pDialog.isShowing()) {
                        pDialog.dismiss();
                    }
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        jsonObject = jsonObject.getJSONObject("Response");
                        String statusStr = jsonObject.getString("success");
                        if(statusStr.equalsIgnoreCase("TRUE")) {
                            //New code 08-09-2023
                            //Create a new JSONObject with changed parameter names
                            JSONObject newJsonObject = new JSONObject();
                            if (jsonObject != null && jsonObject.length() > 0) {
                                try {
                                    newJsonObject.put("DC_Slab_Count", jsonObject.getString("dc_sl_opn_count"));    //Slab DC list Total count
                                    newJsonObject.put("DC_Slab_Amount", jsonObject.getString("dc_sl_opn_amount"));    //Slab DC list Total Amount
                                    newJsonObject.put("DC_NonSlab_Count", jsonObject.getString("dc_ns_opn_count"));    //Non-Slab DC list Total count
                                    newJsonObject.put("DC_NonSlab_Amount", jsonObject.getString("dc_ns_opn_amount"));    //Non-Slab DC list Amount
                                    newJsonObject.put("DC_Total_Count", Integer.parseInt(jsonObject.getString("dc_sl_opn_count")) + Integer.parseInt(jsonObject.getString("dc_ns_opn_count")));    //DC list (Slab+Non-Slab) Total count
                                    newJsonObject.put("DC_Total_Amount", Double.parseDouble(jsonObject.getString("dc_sl_opn_amount")) + Double.parseDouble(jsonObject.getString("dc_ns_opn_amount")));    //DC list (Slab+Non-Slab) Total Amount

                                    newJsonObject.put("DCG_SL_Count", jsonObject.getString("dc_sl_gov_count"));    //Slab DC list Government Services Total Count
                                    newJsonObject.put("DCG_SL_Amount", jsonObject.getString("dc_sl_gov_amount"));    //Slab DC list Government Services Total Amount
                                    newJsonObject.put("DCG_NS_Count", jsonObject.getString("dc_ns_gov_count"));    //Non-Slab DC list Government Services Total Count
                                    newJsonObject.put("DCG_NS_Amount", jsonObject.getString("dc_ns_gov_amount"));    //Non-Slab DC list Government Services Total Amount
                                    newJsonObject.put("DCG_Total_Count", Integer.parseInt(jsonObject.getString("dc_sl_gov_count")) + Integer.parseInt(jsonObject.getString("dc_ns_gov_count")));    //DC list Government Services (Slab+Non-Slab) Total Count
                                    newJsonObject.put("DCG_Total_Amount", Double.parseDouble(jsonObject.getString("dc_sl_gov_amount")) + Double.parseDouble(jsonObject.getString("dc_ns_gov_amount")));    //DC list Government Services (Slab+Non-Slab) Total Amount

                                    newJsonObject.put("DCStat_UDSL_Count", jsonObject.getString("dc_stat_sl_unable_to_disc_count"));    //Slab DC list Statuswise (Unable to Disconnect Services) Count
                                    newJsonObject.put("DCStat_UDSL_Amount", jsonObject.getString("dc_stat_sl_unable_to_disc_amount"));    //Slab DC list Statuswise (Unable to Disconnect Services) Amount
                                    newJsonObject.put("DCStat_UDNS_Count", jsonObject.getString("dc_stat_ns_unable_to_disc_count"));    //Non-Slab DC list Statuswise (Unable to Disconnect Services) Count
                                    newJsonObject.put("DCStat_UDNS_Amount", jsonObject.getString("dc_stat_ns_unable_to_disc_amount"));    //Non-Slab DC list Statuswise (Unable to Disconnect Services) Amount

                                    newJsonObject.put("DCStat_VSL_Count", jsonObject.getString("dc_stat_sl_pay_verf_count"));    //Slab DC list Statuswise (Payment Verified) Count
                                    newJsonObject.put("DCStat_VSL_Amount", jsonObject.getString("dc_stat_sl_pay_verf_amount")); //Slab DC list Statuswise (Payment Verified) Amount
                                    newJsonObject.put("DCStat_VNS_Count", jsonObject.getString("dc_stat_ns_pay_verf_count"));    //Non-Slab DC list Statuswise (Payment Verified) Count
                                    newJsonObject.put("DCStat_VNS_Amount", jsonObject.getString("dc_stat_ns_pay_verf_amount"));    //Non-Slab DC list Statuswise (Payment Verified) Amount

                                    newJsonObject.put("DCStat_PSL_Count", "0");    //Slab DC list Statuswise (Pending for Verification) Count
                                    newJsonObject.put("DCStat_PSL_Amount", "0");    //Slab DC list Statuswise (Pending for Verification) Amount
                                    newJsonObject.put("DCStat_PNS_Count", "0");    //Non-Slab DC list Statuswise (Pending for Verification) Count
                                    newJsonObject.put("DCStat_PNS_Amount", "0");    //Non-Slab DC list Statuswise (Pending for Verification) Amount

                                    newJsonObject.put("DCStat_DSL_Count", jsonObject.getString("dc_stat_sl_disconnected_count"));    //Slab DC list Statuswise (Disconnected) Count	jsonObject.getString("dc_stat_sl_disconnected_count"));
                                    newJsonObject.put("DCStat_DSL_Amount", jsonObject.getString("dc_stat_sl_disconnected_amount"));//Slab DC list Statuswise (Disconnected) Amount
                                    newJsonObject.put("DCStat_DNS_Count", jsonObject.getString("dc_stat_ns_disconnected_count"));    //Non-Slab DC list Statuswise (Disconnected) Count
                                    newJsonObject.put("DCStat_DNS_Amount", jsonObject.getString("dc_stat_ns_disconnected_amount"));    //Non-Slab DC list Statuswise (Disconnected) Amount

                                    newJsonObject.put("DCStat_SL_Total_Count", Integer.parseInt(jsonObject.getString("dc_stat_sl_unable_to_disc_count"))+Integer.parseInt(jsonObject.getString("dc_stat_sl_pay_verf_count")) + Integer.parseInt(jsonObject.getString("dc_stat_sl_disconnected_count")));    //Slab DC list Statuswise Total Count
                                    newJsonObject.put("DCStat_SL_Total_Amount", Double.parseDouble(jsonObject.getString("dc_stat_sl_unable_to_disc_amount"))+Double.parseDouble(jsonObject.getString("dc_stat_sl_pay_verf_amount")) + Double.parseDouble(jsonObject.getString("dc_stat_sl_disconnected_amount")));    //Slab DC list Statuswise Slab Total Amount
                                    newJsonObject.put("DCStat_NS_Total_Count", Integer.parseInt(jsonObject.getString("dc_stat_ns_unable_to_disc_count"))+Integer.parseInt( jsonObject.getString("dc_stat_ns_pay_verf_count") )+ Integer.parseInt(jsonObject.getString("dc_stat_ns_disconnected_count")));    //Non-Slab DC list Statuswise Total Count
                                    newJsonObject.put("DCStat_NS_Total_Amount", Double.parseDouble(jsonObject.getString("dc_stat_ns_unable_to_disc_amount"))+Double.parseDouble(jsonObject.getString("dc_stat_ns_pay_verf_amount")) + Double.parseDouble(jsonObject.getString("dc_stat_ns_disconnected_amount")));    //Non-Slab DC list Statuswise Total Amount

                                    newJsonObject.put("DCStat_Total_Count", Double.parseDouble(jsonObject.getString("dc_stat_sl_unable_to_disc_count")) + Double.parseDouble(jsonObject.getString("dc_stat_sl_disconnected_count"))+Double.parseDouble(jsonObject.getString("dc_stat_sl_pay_verf_count"))+Double.parseDouble(jsonObject.getString("dc_stat_ns_disconnected_count")) + Double.parseDouble(jsonObject.getString("dc_stat_ns_unable_to_disc_count")) + Double.parseDouble(jsonObject.getString("dc_stat_ns_pay_verf_count")));    //DC list Statuswise (Slab+Non-Slab) Total Count
                                    newJsonObject.put("DCStat_Total_Amount", Double.parseDouble(jsonObject.getString("dc_stat_sl_unable_to_disc_amount")) + Double.parseDouble(jsonObject.getString("dc_stat_sl_disconnected_amount"))+Double.parseDouble(jsonObject.getString("dc_stat_sl_pay_verf_amount"))+Double.parseDouble(jsonObject.getString("dc_stat_ns_disconnected_amount")) + Double.parseDouble(jsonObject.getString("dc_stat_ns_unable_to_disc_amount")) + Double.parseDouble(jsonObject.getString("dc_stat_ns_pay_verf_amount")));    //DC list Statuswise (Slab+Non-Slab) Total Amount

                                    newJsonObject.put("DC_Bal_SL_Count", jsonObject.getString("dc_sl_pen_count"));    //Slab DC list Balance Count
                                    newJsonObject.put("DC_Bal_SL_Amount", jsonObject.getString("dc_sl_pen_amount"));    //Slab DC list Balance Amount
                                    newJsonObject.put("DC_Bal_NS_Count", jsonObject.getString("dc_ns_pen_count"));//Non-Slab DC list Balance count
                                    newJsonObject.put("DC_Bal_NS_Amount", jsonObject.getString("dc_ns_pen_amount"));    //Non-Slab DC list Balance Amount
                                    newJsonObject.put("DC_Bal_Total_Count", Integer.parseInt(jsonObject.getString("dc_sl_pen_count")) + Integer.parseInt(jsonObject.getString("dc_ns_pen_count")));    //DC list Balance (Slab+Non-Slab) Total Amount
                                    newJsonObject.put("DC_Bal_Total_Amount", Double.parseDouble(jsonObject.getString("dc_sl_pen_amount")) + Double.parseDouble(jsonObject.getString("dc_ns_pen_amount")));    //DC list Balance (Slab+Non-Slab) Total Amount

                                    newJsonObject.put("DC_COL_SL_Count", jsonObject.getString("dc_sl_paid_count"));    //Slab DC list Collection count
                                    newJsonObject.put("DC_COL_SL_Amount", jsonObject.getString("dc_sl_paid_amount"));    //Slab DC list Collection Amount
                                    newJsonObject.put("DC_COL_NS_Count", jsonObject.getString("dc_ns_paid_count"));    //Non-Slab DC list Collection count
                                    newJsonObject.put("DC_COL_NS_Amount", jsonObject.getString("dc_ns_paid_amount"));    //Non-Slab DC list Collection Amount
                                    newJsonObject.put("DC_COL_Total_Count", Integer.parseInt(jsonObject.getString("dc_sl_paid_count")) + Integer.parseInt(jsonObject.getString("dc_ns_paid_count")));    //DC list Collection (Slab+Non-Slab) Total count
                                    newJsonObject.put("DC_COL_Total_Amount", Double.parseDouble(jsonObject.getString("dc_sl_paid_amount")) + Double.parseDouble(jsonObject.getString("dc_ns_paid_amount")));    //DC list Collection (Slab+Non-Slab) Total Amount
                                }catch (Exception e){
                                    e.printStackTrace();
                                    if (pDialog != null && pDialog.isShowing()) {
                                        pDialog.dismiss();
                                    }
                                }

                            }
//                        newJsonObject.put("DCStat_VSL_Amount", "0");    //Slab DC list Statuswise (Payment Verified) Amount
//                        newJsonObject.put("DC_NonSlab_Count", jsonObject.get("dc_ns_opn_count"));    //Non-Slab DC list Total count
//                        newJsonObject.put("DCG_NS_Count", jsonObject.get("dc_ns_gov_count"));    //Non-Slab DC list Government Services Total Count
//                        newJsonObject.put("DCStat_DNS_Amount", jsonObject.get("dc_stat_ns_disconnected_amount"));    //Non-Slab DC list Statuswise (Disconnected) Amount
//                        newJsonObject.put("DCStat_PSL_Amount", "0");    //Slab DC list Statuswise (Pending for Verification) Amount
//                        newJsonObject.put("DC_Bal_SL_Amount", jsonObject.get("dc_sl_pen_amount"));    //Slab DC list Balance Amount
//                        newJsonObject.put("DCStat_UDSL_Amount", jsonObject.get("dc_stat_sl_unable_to_disc_amount")); //Slab DC list Statuswise (Unable to Disconnect Services) Amount
//                        newJsonObject.put("DC_COL_Total_Count", "0");    //Slab DC list Collection count
//                        newJsonObject.put("DCG_SL_Count", jsonObject.get("dc_sl_gov_count"));        //Slab DC list Government Services Total Count
//                        newJsonObject.put("DCStat_DSL_Amount", jsonObject.get("dc_stat_sl_disconnected_amount")); //Slab DC list Statuswise (Disconnected) Amount
//                        newJsonObject.put("DCStat_Total_Amount", "0");    //Slab DC list Statuswise Total Amount
//                        newJsonObject.put("DC_COL_SL_Amount", jsonObject.get("dc_sl_paid_amount"));    //Slab DC list Collection Amount
//                        newJsonObject.put("DCStat_UDNS_Amount", jsonObject.get("dc_stat_ns_unable_to_disc_amount"));    //Non-Slab DC list Statuswise (Unable to Disconnect Services) Amount
//                        newJsonObject.put("DCStat_NS_Total_Count", "0");    //Non-Slab DC list Statuswise Total Count
//                        newJsonObject.put("DC_Total_Count", "0");    //DC list (Slab+Non-Slab) Total count
//                        newJsonObject.put("DCStat_VSL_Count", "0");    //Slab DC list Statuswise (Payment Verified) Count
//                        newJsonObject.put("DCStat_PSL_Count", "0");    //Slab DC list Statuswise (Pending for Verification) Count
//                        newJsonObject.put("DCG_Total_Amount", "0");    //DC list Government Services (Slab+Non-Slab) Total Amount
//                        newJsonObject.put("DCStat_DSL_Count", jsonObject.get("dc_stat_sl_disconnected_count"));    //Slab DC list Statuswise (Disconnected) Count
//                        newJsonObject.put("DCStat_PNS_Count", "0");    //Non-Slab DC list Statuswise (Pending for Verification) Count
//                        newJsonObject.put("DC_NonSlab_Amount", jsonObject.get("dc_ns_opn_amount"));    //Non-Slab DC list Amount
//                        newJsonObject.put("DCStat_SL_Total_Count", "0");    //Slab DC list Statuswise Total Count
//                        newJsonObject.put("DC_COL_NS_Amount", jsonObject.get("dc_ns_paid_amount"));    //Non-Slab DC list Collection Amount
//                        newJsonObject.put("DC_COL_Total_Amount", "0");//DC list Collection (Slab+Non-Slab) Amount
//                        newJsonObject.put("DCStat_UDSL_Count", jsonObject.get("dc_stat_sl_unable_to_disc_count"));    //Slab DC list Statuswise (Unable to Disconnect Services) Count
//                        newJsonObject.put("DCG_NS_Amount", jsonObject.get("dc_ns_gov_amount"));        //Non-Slab DC list Government Services Total Amount
//                        newJsonObject.put("DCStat_DNS_Count", jsonObject.get("dc_stat_ns_disconnected_count"));    //Non-Slab DC list Statuswise (Disconnected) Count
//                        newJsonObject.put("DC_Bal_Total_Count", "0");    //DC list Balance (Slab+Non-Slab) Total Amount
//                        newJsonObject.put("DC_Bal_SL_Count", jsonObject.get("dc_sl_pen_count"));    //Slab DC list Balance Count

                            // Serialize the new JSONObject back to a string
                            //String newJsonString = newJsonObject.toString();

                            //System.out.println(newJsonString);
                            //End New code 08-09-2023
                            AdvanceAbstractModel advanceAbstractModel = new Gson().fromJson(newJsonObject.toString(),
                                    AdvanceAbstractModel.class);
                            setData(advanceAbstractModel);
                        }else {

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        if (pDialog != null && pDialog.isShowing()) {
                            pDialog.dismiss();
                        }
                    }
                }

                @Override
                public void onFailure(int statusCode, Throwable error, String content) {
                    Utility.showLog("error", error.toString());
                    if (pDialog != null && pDialog.isShowing()) {
                        pDialog.dismiss();
                    }
                    Utility.showCustomOKOnlyDialog(DC_AbstractListActivity.this, error.getMessage());
                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void setData(AdvanceAbstractModel advanceAbstractModel) {
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
        }
        sv_main.setVisibility(View.VISIBLE);
        tv_services_all.setText(advanceAbstractModel.getDC_Slab_Count());
        tv_total.setText(advanceAbstractModel.getDC_Slab_Amount());
        tv_services_all_ns.setText(advanceAbstractModel.getDC_NonSlab_Count());
        tv_total_ns.setText(advanceAbstractModel.getDC_NonSlab_Amount());
        tv_amount.setText(advanceAbstractModel.getDC_Total_Amount());
        tv_services.setText(advanceAbstractModel.getDC_Total_Count());


        tv_services_all_govt.setText(advanceAbstractModel.getDCG_SL_Count());
        tv_total_govt.setText(advanceAbstractModel.getDCG_SL_Amount());
        tv_services_govt_ns.setText(advanceAbstractModel.getDCG_NS_Count());
        tv_total_govt_ns.setText(advanceAbstractModel.getDCG_NS_Amount());
        tv_services_govt.setText(advanceAbstractModel.getDCG_Total_Count());
        tv_amount_govt.setText(advanceAbstractModel.getDCG_Total_Amount());

        tv_verify.setText(advanceAbstractModel.getDCStat_VSL_Count());
        tv_payment.setText(advanceAbstractModel.getDCStat_PSL_Count());
        tv_disc.setText(advanceAbstractModel.getDCStat_DSL_Count());
        tv_unable.setText(advanceAbstractModel.getDCStat_UDSL_Count());
        tv_verify_amount.setText(advanceAbstractModel.getDCStat_VSL_Amount());
        tv_payment_amount.setText(advanceAbstractModel.getDCStat_PSL_Amount());
        tv_disc_amount.setText(advanceAbstractModel.getDCStat_DSL_Amount());
        tv_unable_amount.setText(advanceAbstractModel.getDCStat_UDSL_Amount());
        tv_services_sl_stat.setText(advanceAbstractModel.getDCStat_SL_Total_Count());
        tv_amount_sl_stat.setText(advanceAbstractModel.getDCStat_SL_Total_Amount());

        tv_verify_ns.setText(advanceAbstractModel.getDCStat_VNS_Count());
        tv_payment_ns.setText(advanceAbstractModel.getDCStat_PNS_Count());
        tv_disc_ns.setText(advanceAbstractModel.getDCStat_DNS_Count());
        tv_unable_ns.setText(advanceAbstractModel.getDCStat_UDNS_Count());
        tv_verify_amount_ns.setText(advanceAbstractModel.getDCStat_VNS_Amount());
        tv_payment_amount_ns.setText(advanceAbstractModel.getDCStat_PNS_Amount());
        tv_disc_amount_ns.setText(advanceAbstractModel.getDCStat_DNS_Amount());
        tv_unable_amount_ns.setText(advanceAbstractModel.getDCStat_UDNS_Amount());
        tv_services_ns_stat.setText(advanceAbstractModel.getDCStat_NS_Total_Count());
        tv_amount_ns_stat.setText(advanceAbstractModel.getDCStat_NS_Total_Amount());

        tv_services_all_col.setText(advanceAbstractModel.getDC_COL_SL_Count());
        tv_total_col.setText(advanceAbstractModel.getDC_COL_SL_Amount());
        tv_services_col_ns.setText(advanceAbstractModel.getDC_COL_NS_Count());
        tv_total_col_ns.setText(advanceAbstractModel.getDC_COL_NS_Amount());
        tv_services_col.setText(advanceAbstractModel.getDC_COL_Total_Count());
        tv_amount_col.setText(advanceAbstractModel.getDC_COL_Total_Amount());

        tv_services_all_bal.setText(advanceAbstractModel.getDC_Bal_SL_Count());
        tv_total_bal.setText(advanceAbstractModel.getDC_Bal_SL_Amount());
        tv_services_bal_ns.setText(advanceAbstractModel.getDC_Bal_NS_Count());
        tv_total_bal_ns.setText(advanceAbstractModel.getDC_Bal_NS_Amount());
        tv_services_bal.setText(advanceAbstractModel.getDC_Bal_Total_Count());
        tv_amount_bal.setText(advanceAbstractModel.getDC_Bal_Total_Amount());

    }
}
