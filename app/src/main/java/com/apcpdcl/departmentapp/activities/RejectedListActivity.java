package com.apcpdcl.departmentapp.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.apcpdcl.departmentapp.R;
import com.apcpdcl.departmentapp.adapters.RejectedListAdapter;
import com.apcpdcl.departmentapp.models.MeterChangeListModel;
import com.apcpdcl.departmentapp.utils.Constants;
import com.apcpdcl.departmentapp.utils.Utility;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RejectedListActivity extends AppCompatActivity {
    @BindView(R.id.lv_reg)
    ListView lv_reg;
    @BindView(R.id.tv_no_data)
    TextView tv_no_data;
    @BindView(R.id.toolbar_title)
    TextView toolbar_title;
    @BindView(R.id.ll_spinner)
    LinearLayout ll_spinner;
    private ArrayList<MeterChangeListModel> meterChangeListModels = new ArrayList<>();
    private ProgressDialog pDialog;
    private String sectionCode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meter_change_list);
        ButterKnife.bind(this);
        init();
    }

    /* Initialize Views*/
    private void init() {
        ll_spinner.setVisibility(View.GONE);
        toolbar_title.setText(Utility.getResourcesString(this, R.string.rejected_by_aao_list));
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        android.content.SharedPreferences prefs = getSharedPreferences("loginPrefs", 0);
        sectionCode = prefs.getString("Section_Code", "");
        pDialog = new ProgressDialog(RejectedListActivity.this);
        if (Utility.isNetworkAvailable(this)) {
            pDialog.show();
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            getMeterChangeList();
        } else {
            Utility.showCustomOKOnlyDialog(this,
                    Utility.getResourcesString(this,
                            R.string.no_internet));
        }
    }

    private void setListData() {
        RejectedListAdapter rejectedListAdapter = new RejectedListAdapter(this, meterChangeListModels);
        lv_reg.setAdapter(rejectedListAdapter);
    }

    /* *
     * Get SUB-STATIONS
     * */
    private void getMeterChangeList() {
        AsyncHttpClient client = new AsyncHttpClient();
        Utility.showLog("Url", Constants.METER_CHANGE_URL + Constants.REJECTED_MTR_DETAILS);
        Utility.showLog("UNITCODE", sectionCode);
        RequestParams params = new RequestParams();
        params.put("UNITCODE", sectionCode);
        client.setTimeout(50000);
        client.post(Constants.METER_CHANGE_URL + Constants.REJECTED_MTR_DETAILS, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                Utility.showLog("onSuccess", response);
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss();
                }
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.has("data")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        meterChangeListModels = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject json = jsonArray.optJSONObject(i);
                            MeterChangeListModel registrationModel = new Gson().fromJson(json.toString(),
                                    MeterChangeListModel.class);
                            meterChangeListModels.add(registrationModel);
                        }

                        if (meterChangeListModels.size() > 0) {
                            setListData();
                        } else {
                            if (pDialog != null && pDialog.isShowing()) {
                                pDialog.dismiss();
                            }
                            tv_no_data.setVisibility(View.VISIBLE);
                            lv_reg.setVisibility(View.GONE);
                        }
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
            }
        });
    }

}
