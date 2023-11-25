package com.apcpdcl.departmentapp.activities;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.apcpdcl.departmentapp.R;
import com.apcpdcl.departmentapp.adapters.ComplaintsListAdapter;
import com.apcpdcl.departmentapp.interfaces.IUpdateList;
import com.apcpdcl.departmentapp.models.ComplaintModel;
import com.apcpdcl.departmentapp.shared.AppPrefs;
import com.apcpdcl.departmentapp.utils.Utility;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Haseen
 * on 21-04-2018.
 */

public class ComplaintsListActivity extends AppCompatActivity implements IUpdateList {
    @BindView(R.id.lv_reg)
    ListView lv_reg;
    @BindView(R.id.et_search)
    EditText et_search;
    @BindView(R.id.tv_no_data)
    TextView tv_no_data;
    @BindView(R.id.iv_search)
    ImageView iv_search;
    @BindView(R.id.rl_search)
    RelativeLayout rl_search;


    private ArrayList<ComplaintModel> complaintModels;
    private ArrayList<String> complaintIdArrList = new ArrayList<>();

    private ComplaintsListAdapter complaintsListAdapter;
    public ProgressDialog pDialog;
    public Toolbar mToolbar;
    private String sectionCode, userId;
    private static IUpdateList iUpdateList;


    public static IUpdateList getInstance() {
        return iUpdateList;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint_list);

        iUpdateList = this;
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        SharedPreferences prefs = getSharedPreferences("loginPrefs", 0);
//        sectionCode = prefs.getString("Section_Code", "");
        sectionCode = AppPrefs.getInstance(getApplicationContext()).getString("SECTIONCODE", "");
        userId = AppPrefs.getInstance(getApplicationContext()).getString("USERID", "");
        ButterKnife.bind(this);
        pDialog = new ProgressDialog(ComplaintsListActivity.this);
        if (Utility.isNetworkAvailable(this)) {
            pDialog.show();
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            //getComplaints();
            JSONObject requestObj = new JSONObject();
            try {
                requestObj.put("bname", userId);
                requestObj.put("type", "CCC");
                getComplaints(requestObj);
            } catch (Exception e) {

            }
        } else {
            Utility.showCustomOKOnlyDialog(this,
                    Utility.getResourcesString(this,
                            R.string.no_internet));
        }
    }

    /* *
     *Get Complaints List
     * */
    private void getComplaints(JSONObject requestObjVal) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(50000);
//        RequestParams requestParams = new RequestParams();
//        requestParams.put("SECTION", sectionCode);
        HttpEntity entity;
        try {
            entity = new StringEntity(requestObjVal.toString());
            BasicHeader[] headers = new BasicHeader[]{new BasicHeader("Authorization", "Basic " + AppPrefs.getInstance(getApplicationContext()).getString("USER_AUTH", ""))};
            client.post(this, "https://apcpcdcl-test-k5qoqm5y.it-cpi012-rt.cfapps.ap21.hana.ondemand.com/http/DepartmentalApp/SAPISU/ComplaintList/DEV", headers, entity, "application/json", new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(String responseStr) {
                    Utility.showLog("onSuccess", responseStr);
                    if (pDialog != null && pDialog.isShowing()) {
                        pDialog.dismiss();
                    }
                    try {
                        JSONObject responseObj = new JSONObject(responseStr);
                        responseObj = responseObj.getJSONObject("response");
                        String status = responseObj.getString("success");
                        if (status.equalsIgnoreCase("True")) {
                            JSONArray dataArr = responseObj.getJSONArray("data");
                            complaintIdArrList = new ArrayList<>();
                            if (dataArr.length() > 0) {
                                for (int i = 0; i < dataArr.length(); i++) {
                                    JSONObject comIDObj = dataArr.getJSONObject(i);

//                                    ComplaintIdModel registrationIDModel = new Gson().fromJson(comIDObj.toString(),
//                                        ComplaintIdModel.class);
                                    complaintIdArrList.add(comIDObj.getString("complaint_id"));
                                }
                                if (complaintIdArrList.size() > 0) {
                                    setListData();
                                } else {
                                    if (pDialog != null && pDialog.isShowing()) {
                                        pDialog.dismiss();
                                    }
                                    tv_no_data.setVisibility(View.VISIBLE);
                                    lv_reg.setVisibility(View.GONE);
                                    rl_search.setVisibility(View.GONE);
                                }
                            }
                        }

//                        if (jsonObject.has("T_CODE_RESPONSE")) {
//                            JSONArray jsonArray = jsonObject.getJSONArray("T_CODE_RESPONSE");
//                            complaintModels = new ArrayList<>();
//                            for (int i = 0; i < jsonArray.length(); i++) {
//                                JSONObject json = jsonArray.optJSONObject(i);
//                                ComplaintModel registrationModel = new Gson().fromJson(json.toString(),
//                                        ComplaintModel.class);
//                                complaintModels.add(registrationModel);
//                            }
//
//                            if (complaintModels.size() > 0) {
//                                setListData();
//                            } else {
//                                if (pDialog != null && pDialog.isShowing()) {
//                                    pDialog.dismiss();
//                                }
//                                tv_no_data.setVisibility(View.VISIBLE);
//                                lv_reg.setVisibility(View.GONE);
//                                rl_search.setVisibility(View.GONE);
//                            }
//                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Throwable error, String content) {
                    Utility.showLog("error", error.toString());
                    if (pDialog != null && pDialog.isShowing()) {
                        pDialog.dismiss();
                    }
                    Utility.showCustomOKOnlyDialog(ComplaintsListActivity.this, error.getMessage());
                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void setListData() {
        complaintsListAdapter = new ComplaintsListAdapter(this, complaintIdArrList);
        lv_reg.setAdapter(complaintsListAdapter);
        implementsSearch();
    }

    //IMPLEMENT SEARCH
    private void implementsSearch() {
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (complaintsListAdapter != null)
                    complaintsListAdapter.getFilter().filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }


    @Override
    public void updateList(String id) {
        for (int i = 0; i < complaintIdArrList.size(); i++) {
            if (id.equalsIgnoreCase(complaintIdArrList.get(i))) {
                complaintIdArrList.remove(i);
                if (complaintIdArrList.size() > 0) {
                    complaintsListAdapter.notifyDataSetChanged();
                } else {
                    tv_no_data.setVisibility(View.VISIBLE);
                    lv_reg.setVisibility(View.GONE);
                }
                break;
            }
        }
    }
}
