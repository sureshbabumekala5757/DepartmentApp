package com.apcpdcl.departmentapp.activities;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.apcpdcl.departmentapp.R;
import com.apcpdcl.departmentapp.adapters.ExceptionListAdapter;
import com.apcpdcl.departmentapp.customviews.ExpandableLayoutListener;
import com.apcpdcl.departmentapp.customviews.Utils;
import com.apcpdcl.departmentapp.models.MeterExceptionListModel;
import com.apcpdcl.departmentapp.models.MeterExceptionsFullModel;
import com.apcpdcl.departmentapp.shared.AppPrefs;
import com.apcpdcl.departmentapp.utils.Utility;
import com.apcpdcl.departmentapp.customviews.ExpandableRelativeLayout;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MeterExceptionListActivity extends Activity {

    @BindView(R.id.ll_main)
    LinearLayout ll_main;
    @BindView(R.id.tv_no_data_found)
    TextView tv_no_data_found;
    private MeterExceptionsFullModel meterExceptionsFullModel;
    private ProgressDialog pDialog;
    private String lmcode, sectionCode, userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meter_exception_list);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        ButterKnife.bind(this);
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        sectionCode = AppPrefs.getInstance(getApplicationContext()).getString("SECTIONCODE", "");
        userId = AppPrefs.getInstance(getApplicationContext()).getString("USERID", "");
        init();
    }

    private void init() {
//        meterExceptionsFullModel = (MeterExceptionsFullModel) getIntent().getSerializableExtra(MeterExceptionsFullModel.class.getSimpleName());
//        if (meterExceptionsFullModel.getKeys() != null && meterExceptionsFullModel.getKeys().size() > 0) {
//            addExceptionList();
//        } else {
//            tv_no_data_found.setVisibility(View.VISIBLE);
//        }

        getExceptionList();
    }

    private void addExceptionList() {
        for (int i = 0; i < meterExceptionsFullModel.getKeys().size(); i++) {
            if (meterExceptionsFullModel.getMeterExceptionListModels().size() > 0) {
                LinearLayout ll = (LinearLayout) getLayoutInflater().inflate(R.layout.exception_list_layout, ll_main, false);
                RelativeLayout rl_all = (RelativeLayout) ll.findViewById(R.id.rl_all);
                TextView tv_name = (TextView) ll.findViewById(R.id.tv_name);
                TextView tv_not_found = (TextView) ll.findViewById(R.id.tv_not_found);
                ImageView iv_exception = (ImageView) ll.findViewById(R.id.iv_exception);
                final ExpandableRelativeLayout el_list = (ExpandableRelativeLayout) ll.findViewById(R.id.el_list);
                ListView lv_service_numbers = (ListView) ll.findViewById(R.id.lv_service_numbers);
                tv_name.setText(meterExceptionsFullModel.getKeys().get(i) + " : " + meterExceptionsFullModel.getMeterExceptionListModels().size());
                setExpandableButtonAnimators(el_list, iv_exception);
                rl_all.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        el_list.toggle();
                    }
                });
                if (meterExceptionsFullModel.getMeterExceptionListModels().size() > 0) {
                    ExceptionListAdapter exceptionListAdapter = new ExceptionListAdapter(this, meterExceptionsFullModel.getMeterExceptionListModels());
                    lv_service_numbers.setAdapter(exceptionListAdapter);
                    Utility.setListViewHeightBasedOnChildren(lv_service_numbers);
                } else {
                    lv_service_numbers.setVisibility(View.GONE);
                    tv_not_found.setVisibility(View.VISIBLE);
                }
                ll_main.addView(ll);
            }
        }
        if (ll_main.getChildCount() ==0){
            tv_no_data_found.setVisibility(View.VISIBLE);
        }
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

    /* *
     *SAP ISU Get Current Month Exceptions
     * */
    private void getExceptionList() {
        pDialog.show();
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(50000);

        HttpEntity entity;
        try {
            JSONObject requestObjPayLoad = new JSONObject();
            try {
                requestObjPayLoad = new JSONObject();
                requestObjPayLoad.put("Method_Name", "ServConnList");
                requestObjPayLoad.put("UserId", userId);//userId
                requestObjPayLoad.put("SectionCode", sectionCode);//sectionCode
            } catch (Exception e) {

            }
            entity = new StringEntity(requestObjPayLoad.toString());
            BasicHeader[] headers = new BasicHeader[]{new BasicHeader("Authorization", "Basic "+ AppPrefs.getInstance(getApplicationContext()).getString("USER_AUTH", ""))};
            client.post(this, "https://apcpcdcl-test-k5qoqm5y.it-cpi012-rt.cfapps.ap21.hana.ondemand.com/http/DepartmentalApp/SAPISU/MeterReplacement/GetServiceConnectionList/DEV", headers, entity,"application/json", new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(String responseStr) {
                    Utility.showLog("onSuccess", responseStr);
                    if (pDialog != null && pDialog.isShowing()) {
                        pDialog.dismiss();
                    }
                    try {
                        JSONObject jsonObject = new JSONObject(responseStr);
                        jsonObject = jsonObject.getJSONObject("response");
                        String successStr = jsonObject.getString("success");
                        meterExceptionsFullModel = new MeterExceptionsFullModel();
                        ArrayList<MeterExceptionListModel> meterExceptionListArrModels = new ArrayList<>();
                        ArrayList<String> keys = new ArrayList<>();
                        keys.add("SR Numbers");
                        if(successStr.equalsIgnoreCase("True")){
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            if(jsonArray.length()>0 && jsonArray != null) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject json = jsonArray.optJSONObject(i);
                                    MeterExceptionListModel exceptionListModel = new Gson().fromJson(json.toString(),
                                            MeterExceptionListModel.class);
                                    meterExceptionListArrModels.add(exceptionListModel);
                                }
                                meterExceptionsFullModel.setKeys(keys);
                                meterExceptionsFullModel.setMeterExceptionListModels(meterExceptionListArrModels);
//                                Intent intent = new Intent(LMMeterChangeDashBoardActivity.this, MeterExceptionListActivity.class);
//                                intent.putExtra(MeterExceptionsFullModel.class.getSimpleName(), meterExceptionsFullModel);
//                                startActivity(intent);

                                //meterExceptionsFullModel = (MeterExceptionsFullModel) getIntent().getSerializableExtra(MeterExceptionsFullModel.class.getSimpleName());
                                if (meterExceptionsFullModel.getKeys() != null && meterExceptionsFullModel.getKeys().size() > 0) {
                                    addExceptionList();
                                } else {
                                    tv_no_data_found.setVisibility(View.VISIBLE);
                                }
                            }

                        }else {
                            tv_no_data_found.setVisibility(View.VISIBLE);
                        }


//                        for (int i = 0; i < jsonArray.length(); i++) {
//                            JSONObject json = jsonArray.optJSONObject(i);
//                            MeterExceptionModel registrationModel = new Gson().fromJson(json.toString(),
//                                    MeterExceptionModel.class);
//                            meterExceptionModels.add(registrationModel);
//                        }
//                        meterExceptionsFullModel.setKeys(keys);
//                        meterExceptionsFullModel.setMeterExceptionModels(meterExceptionModelArrayList);
//                        Intent intent = new Intent(LMMeterChangeDashBoardActivity.this, MeterExceptionListActivity.class);
//                        intent.putExtra(MeterExceptionsFullModel.class.getSimpleName(), meterExceptionsFullModel);
//                        startActivity(intent);
//                        Iterator<String> iter = jsonObject.keys();
//                        ArrayList<MeterExceptionModel> meterExceptionModelArrayList = new ArrayList<>();
//                        while (iter.hasNext()) {
//                            String key = iter.next();
//                            keys.add(key);
//                            try {
//                                JSONArray jsonArray = jsonObject.getJSONArray(key);
//                                MeterExceptionModel meterExceptionModel = new MeterExceptionModel();
//                                ArrayList<MeterExceptionModel> meterExceptionModels = new ArrayList<>();
//                                for (int i = 0; i < jsonArray.length(); i++) {
//                                    JSONObject json = jsonArray.optJSONObject(i);
//                                    MeterExceptionModel registrationModel = new Gson().fromJson(json.toString(),
//                                            MeterExceptionModel.class);
//                                    meterExceptionModels.add(registrationModel);
//                                }
//                                meterExceptionModel.setMeterExceptionModels(meterExceptionModels);
//                                meterExceptionModelArrayList.add(meterExceptionModel);
//                            } catch (JSONException e) {
//                                // Something went wrong!
//                            }
//                        }
//                        meterExceptionsFullModel.setKeys(keys);
//                        meterExceptionsFullModel.setMeterExceptionModels(meterExceptionModelArrayList);
//                        Intent intent = new Intent(LMMeterChangeDashBoardActivity.this, MeterExceptionListActivity.class);
//                        intent.putExtra(MeterExceptionsFullModel.class.getSimpleName(), meterExceptionsFullModel);
//                        startActivity(intent);

                    } catch (JSONException e) {
                        if (pDialog != null && pDialog.isShowing()) {
                            pDialog.dismiss();
                        }
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Throwable error, String content) {
                    Utility.showLog("error", error.toString());
                    Utility.showCustomOKOnlyDialog(MeterExceptionListActivity.this, error.getLocalizedMessage());
                    if (pDialog != null && pDialog.isShowing()) {
                        pDialog.dismiss();
                    }
                }
            });
        } catch (Exception e) {
            if (pDialog != null && pDialog.isShowing()) {
                pDialog.dismiss();
            }
        }
    }
}
