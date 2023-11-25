package com.apcpdcl.departmentapp.activities;

import android.app.ProgressDialog;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

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
import android.widget.Toast;

import com.apcpdcl.departmentapp.R;
import com.apcpdcl.departmentapp.adapters.ReconnectionListAdapter;
import com.apcpdcl.departmentapp.interfaces.IUpdateList;
import com.apcpdcl.departmentapp.model.ReconnectionSrcModel;
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

public class ReconnectionList extends AppCompatActivity implements IUpdateList {
    @BindView(R.id.lv_reconnection)
    ListView lv_reconnection;
    @BindView(R.id.et_search)
    EditText et_search;
    @BindView(R.id.tv_no_data)
    TextView tv_no_data;
    @BindView(R.id.iv_search)
    ImageView iv_search;
    @BindView(R.id.rl_search)
    RelativeLayout rl_search;

    private ArrayList<ReconnectionSrcModel> reconnectionSrcModel;
    private ArrayList<JSONObject> reconnectionSrcArrList = new ArrayList<>();
    private ReconnectionListAdapter reconnectionListAdapter;
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
        setContentView(R.layout.activity_reconnection_list);

        iUpdateList = this;
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        ButterKnife.bind(this);

        pDialog = new ProgressDialog(this);

        sectionCode = AppPrefs.getInstance(getApplicationContext()).getString("SECTIONCODE", "");
        userId = AppPrefs.getInstance(getApplicationContext()).getString("USERID", "");

        String strData = "{\n" +
                "    \"success\": \"False\",\n" +
                "    \"message\": \"No Data Found for the Line man code\",\n" +
                "    \"Message\" : \"\",\n" +
                "    \"Status\" : \"True\",\n" +
                "    \"response\": [\n" +
                "     {\n" +
                "        \"Date\" : \"\",\n" +
                "        \"USC_No\" : \"6511428000621\",\n" +
                "        \"BPNumber\" : \"1201303317\",\n" +
                "        \"bpno\" : \"1201303317\",\n" +
                "        \"prno\" : \"20\",\n" +
                "        \"MobileNo\" : \"9989212252\",\n" +
                "        \"FeederName\" : \"R.C.PURAM\",\n" +
                "        \"DTRName\" : \"33/11KV Surampalli\",\n" +
                "        \"PoleNo\" : \"301511330502\",\n" +
                "        \"Rate_Cat\" : \"\",\n" +
                "        \"CONT_LOAD\" : \"\",\n" +
                "        \"Address\" : \",28-SURAMPALLI,SURAMPALLE,,521212\",\n" +
                "        \"DiscDate\" : \"0\",\n" +
                "        \"Disc_Read_KWH\" : \"\",\n" +
                "        \"Disc_Read_KVH\" : \"\",\n" +
                "        \"DCAmt\" : \"350.74\",\n" +
                "        \"PayRecDt\" : \"20230809\",\n" +
                "        \"PayRecNo\" : \"002790005055\",\n" +
                "        \"PRCounter\" : \"ERO Counter\",\n" +
                "        \"BillAmt\" : \"254.74\",\n" +
                "        \"OthAmt\" : \"0.00\",\n" +
                "        \"RCAmt\" : \"100.00\",\n" +
                "        \"PRAmt\" : \"100.00-\",\n" +
                "        \"Bal_Due\" : \"\"\n" +
                "    },\n" +
                "{\n" +
                "        \"Date\" : \"\",\n" +
                "        \"USC_No\" : \"6511428000622\",\n" +
                "        \"BPNumber\" : \"1201303317\",\n" +
                "        \"bpno\" : \"1201303317\",\n" +
                "        \"prno\" : \"20\",\n" +
                "        \"MobileNo\" : \"9989212252\",\n" +
                "        \"FeederName\" : \"R.C.PURAM\",\n" +
                "        \"DTRName\" : \"33/11KV Surampalli\",\n" +
                "        \"PoleNo\" : \"301511330502\",\n" +
                "        \"Rate_Cat\" : \"\",\n" +
                "        \"CONT_LOAD\" : \"\",\n" +
                "        \"Address\" : \",28-SURAMPALLI,SURAMPALLE,,521212\",\n" +
                "        \"DiscDate\" : \"0\",\n" +
                "        \"Disc_Read_KWH\" : \"\",\n" +
                "        \"Disc_Read_KVH\" : \"\",\n" +
                "        \"DCAmt\" : \"350.74\",\n" +
                "        \"PayRecDt\" : \"20230809\",\n" +
                "        \"PayRecNo\" : \"002790005055\",\n" +
                "        \"PRCounter\" : \"ERO Counter\",\n" +
                "        \"BillAmt\" : \"254.74\",\n" +
                "        \"OthAmt\" : \"0.00\",\n" +
                "        \"RCAmt\" : \"100.00\",\n" +
                "        \"PRAmt\" : \"100.00-\",\n" +
                "        \"Bal_Due\" : \"\"\n" +
                "    },\n" +
                "{\n" +
                "        \"Date\" : \"\",\n" +
                "        \"USC_No\" : \"6511428000623\",\n" +
                "        \"BPNumber\" : \"1201303317\",\n" +
                "        \"bpno\" : \"1201303317\",\n" +
                "        \"prno\" : \"20\",\n" +
                "        \"MobileNo\" : \"9989212252\",\n" +
                "        \"FeederName\" : \"R.C.PURAM\",\n" +
                "        \"DTRName\" : \"33/11KV Surampalli\",\n" +
                "        \"PoleNo\" : \"301511330502\",\n" +
                "        \"Rate_Cat\" : \"\",\n" +
                "        \"CONT_LOAD\" : \"\",\n" +
                "        \"Address\" : \",28-SURAMPALLI,SURAMPALLE,,521212\",\n" +
                "        \"DiscDate\" : \"0\",\n" +
                "        \"Disc_Read_KWH\" : \"\",\n" +
                "        \"Disc_Read_KVH\" : \"\",\n" +
                "        \"DCAmt\" : \"350.74\",\n" +
                "        \"PayRecDt\" : \"20230809\",\n" +
                "        \"PayRecNo\" : \"002790005055\",\n" +
                "        \"PRCounter\" : \"ERO Counter\",\n" +
                "        \"BillAmt\" : \"254.74\",\n" +
                "        \"OthAmt\" : \"0.00\",\n" +
                "        \"RCAmt\" : \"100.00\",\n" +
                "        \"PRAmt\" : \"100.00-\",\n" +
                "        \"Bal_Due\" : \"\"\n" +
                "    },\n" +
                "{\n" +
                "        \"Date\" : \"\",\n" +
                "        \"USC_No\" : \"6511428000624\",\n" +
                "        \"BPNumber\" : \"1201303317\",\n" +
                "        \"bpno\" : \"1201303317\",\n" +
                "        \"prno\" : \"20\",\n" +
                "        \"MobileNo\" : \"9989212252\",\n" +
                "        \"FeederName\" : \"R.C.PURAM\",\n" +
                "        \"DTRName\" : \"33/11KV Surampalli\",\n" +
                "        \"PoleNo\" : \"301511330502\",\n" +
                "        \"Rate_Cat\" : \"\",\n" +
                "        \"CONT_LOAD\" : \"\",\n" +
                "        \"Address\" : \",28-SURAMPALLI,SURAMPALLE,,521212\",\n" +
                "        \"DiscDate\" : \"0\",\n" +
                "        \"Disc_Read_KWH\" : \"\",\n" +
                "        \"Disc_Read_KVH\" : \"\",\n" +
                "        \"DCAmt\" : \"350.74\",\n" +
                "        \"PayRecDt\" : \"20230809\",\n" +
                "        \"PayRecNo\" : \"002790005055\",\n" +
                "        \"PRCounter\" : \"ERO Counter\",\n" +
                "        \"BillAmt\" : \"254.74\",\n" +
                "        \"OthAmt\" : \"0.00\",\n" +
                "        \"RCAmt\" : \"100.00\",\n" +
                "        \"PRAmt\" : \"100.00-\",\n" +
                "        \"Bal_Due\" : \"\"\n" +
                "    },\n" +
                "{\n" +
                "        \"Date\" : \"\",\n" +
                "        \"USC_No\" : \"6511428000625\",\n" +
                "        \"BPNumber\" : \"1201303317\",\n" +
                "        \"bpno\" : \"1201303317\",\n" +
                "        \"prno\" : \"20\",\n" +
                "        \"MobileNo\" : \"9989212252\",\n" +
                "        \"FeederName\" : \"R.C.PURAM\",\n" +
                "        \"DTRName\" : \"33/11KV Surampalli\",\n" +
                "        \"PoleNo\" : \"301511330502\",\n" +
                "        \"Rate_Cat\" : \"\",\n" +
                "        \"CONT_LOAD\" : \"\",\n" +
                "        \"Address\" : \",28-SURAMPALLI,SURAMPALLE,,521212\",\n" +
                "        \"DiscDate\" : \"0\",\n" +
                "        \"Disc_Read_KWH\" : \"\",\n" +
                "        \"Disc_Read_KVH\" : \"\",\n" +
                "        \"DCAmt\" : \"350.74\",\n" +
                "        \"PayRecDt\" : \"20230809\",\n" +
                "        \"PayRecNo\" : \"002790005055\",\n" +
                "        \"PRCounter\" : \"ERO Counter\",\n" +
                "        \"BillAmt\" : \"254.74\",\n" +
                "        \"OthAmt\" : \"0.00\",\n" +
                "        \"RCAmt\" : \"100.00\",\n" +
                "        \"PRAmt\" : \"100.00-\",\n" +
                "        \"Bal_Due\" : \"\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        try {
            JSONObject jsonObject = new JSONObject(strData);
            if (jsonObject.optString("Status").equalsIgnoreCase("True")) {
                JSONArray jsonArray = jsonObject.optJSONArray("response");
                if (jsonArray != null && jsonArray.length() > 0) {
                    for (int i = 0; i < jsonArray.length(); i++)
                        reconnectionSrcArrList.add(jsonArray.optJSONObject(i));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //setListData();

        if (Utility.isNetworkAvailable(this)) {
            pDialog.show();
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            JSONObject requestObj = new JSONObject();
            try {
                requestObj.put("LineManCode", userId);
                getReconnectionList(requestObj);
            } catch (Exception e) {

            }
        } else {
            Utility.showCustomOKOnlyDialog(this,
                    Utility.getResourcesString(this,
                            R.string.no_internet));
        }
    }

    /* *
     *Get Reconnection List
     * */
    private void getReconnectionList(JSONObject requestObjVal) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(50000);
        HttpEntity entity;
        try {
            entity = new StringEntity(requestObjVal.toString());
            BasicHeader[] headers = new BasicHeader[]{new BasicHeader("Authorization", "Basic " + AppPrefs.getInstance(getApplicationContext()).getString("USER_AUTH", ""))};
            client.post(this, "https://apcpcdcl-test-k5qoqm5y.it-cpi012-rt.cfapps.ap21.hana.ondemand.com/http/DeptApp/SAPISU/RCPaidList/DEV", headers, entity, "application/json", new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(String responseStr) {
                    Utility.showLog("onSuccess", responseStr);
                    if (pDialog != null && pDialog.isShowing()) {
                        pDialog.dismiss();
                    }
                    try {
                        JSONObject jsonObject = new JSONObject(responseStr);
                        if (jsonObject != null && jsonObject.length() > 0) {
                            if (jsonObject.optString("success").equalsIgnoreCase("True")) {
                                JSONArray dataArr = jsonObject.getJSONArray("response");
                                if (dataArr != null && dataArr.length() > 0) {
                                    reconnectionSrcArrList = new ArrayList<>();
                                    for (int i = 0; i < dataArr.length(); i++) {
                                        reconnectionSrcArrList.add(dataArr.optJSONObject(i));
                                    }
                                    if (reconnectionSrcArrList.size() > 0) {
                                        setListData();
                                    } else {
                                        if (pDialog != null && pDialog.isShowing()) {
                                            pDialog.dismiss();
                                        }
                                        tv_no_data.setVisibility(View.VISIBLE);
                                        rl_search.setVisibility(View.GONE);
                                    }
                                }
                            } else
                                Toast.makeText(ReconnectionList.this, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
                        }
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
                    Utility.showCustomOKOnlyDialog(ReconnectionList.this, error.getMessage());
                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    //Set data to List
    private void setListData() {
        reconnectionListAdapter = new ReconnectionListAdapter(this, reconnectionSrcArrList);
        lv_reconnection.setAdapter(reconnectionListAdapter);
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
                if (reconnectionListAdapter != null)
                    reconnectionListAdapter.getFilter().filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void updateList(String id) {
        for (int i = 0; i < reconnectionSrcArrList.size(); i++) {
            if (id.equalsIgnoreCase(reconnectionSrcArrList.get(i).optString("USC_No"))) {
                reconnectionSrcArrList.remove(i);
                if (reconnectionSrcArrList.size() > 0) {
                    reconnectionListAdapter.notifyDataSetChanged();
                } else {
                    tv_no_data.setVisibility(View.VISIBLE);
                    lv_reconnection.setVisibility(View.GONE);
                }
                break;
            }
        }
    }
}