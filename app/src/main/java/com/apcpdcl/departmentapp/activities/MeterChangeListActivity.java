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
import com.apcpdcl.departmentapp.adapters.MeterChangeListAdapter;
import com.apcpdcl.departmentapp.interfaces.IUpdateList;
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

public class MeterChangeListActivity extends AppCompatActivity implements IUpdateList {
    @BindView(R.id.lv_reg)
    ListView lv_reg;
    @BindView(R.id.tv_no_data)
    TextView tv_no_data;
    @BindView(R.id.toolbar_title)
    TextView toolbar_title;
    @BindView(R.id.ll_spinner)
    LinearLayout ll_spinner;
    private ArrayList<MeterChangeListModel> meterChangeListModels = new ArrayList<>();
    private MeterChangeListAdapter meterChangeListAdapter;
    private android.app.ProgressDialog pDialog;
    private String sectionCode = "";
    private String mFrom = "";
    private static IUpdateList iUpdateList;

    public static IUpdateList getInstance() {
        return iUpdateList;
    }
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
        mFrom = getIntent().getExtras().getString(Constants.FROM);
        if (mFrom.equalsIgnoreCase("Delete")) {
            toolbar_title.setText(Utility.getResourcesString(this, R.string.delete_meter_change));
        } else {
            toolbar_title.setText(Utility.getResourcesString(this, R.string.pending_o_m_list));
        }
        iUpdateList = this;
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        android.content.SharedPreferences prefs = getSharedPreferences("loginPrefs", 0);
        sectionCode = prefs.getString("Section_Code", "");
        pDialog = new ProgressDialog(MeterChangeListActivity.this);
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
        meterChangeListAdapter = new MeterChangeListAdapter(this, meterChangeListModels, mFrom);
        lv_reg.setAdapter(meterChangeListAdapter);
    }

    /* *
     * Get SUB-STATIONS
     * */
    private void getMeterChangeList() {
        AsyncHttpClient client = new AsyncHttpClient();
        String url;
        if (mFrom.equalsIgnoreCase("Delete")) {
            url = Constants.METER_CHANGE_URL + Constants.DELETE_MTR_INITIATION_DETAIS;
        } else {
            url = Constants.METER_CHANGE_URL + Constants.FIELD_METER_LIST;
        }
        Utility.showLog("Url", url);
        Utility.showLog("UNITCODE", sectionCode);
        RequestParams params = new RequestParams();
        params.put("UNITCODE", sectionCode);
        client.setTimeout(50000);
        client.post(url, params, new AsyncHttpResponseHandler() {
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

    /* *
     * Approve Meter Change Request
     * */
    public void approveMeterChangeRequest(final MeterChangeListModel meterChangeListModel) {
        pDialog.show();
        AsyncHttpClient client = new AsyncHttpClient();
        String finalStr = meterChangeListModel.getUSCNO() + "|" + meterChangeListModel.getMTRCHGDT() + "|" + meterChangeListModel.getNEWMTRNO();
        Utility.showLog("Url", Constants.METER_CHANGE_URL + Constants.UPDATE_FIELD_MTR_DETAILS);
        Utility.showLog("INPUT", finalStr);
        RequestParams params = new RequestParams();
        params.put("INPUT", finalStr);
        client.setTimeout(50000);
        client.post(Constants.METER_CHANGE_URL + Constants.UPDATE_FIELD_MTR_DETAILS, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                Utility.showLog("onSuccess", response);
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss();
                }
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.has("RQSTATUS")) {
                        Utility.showCustomOKOnlyDialog(MeterChangeListActivity.this, jsonObject.optString("MSG"));
                        if (jsonObject.optString("RQSTATUS").equalsIgnoreCase("VALID")) {
                            meterChangeListModels.remove(meterChangeListModel);
                            if (meterChangeListModels.size() > 0) {
                                meterChangeListAdapter.notifyDataSetChanged();
                            } else {
                                tv_no_data.setVisibility(View.VISIBLE);
                                lv_reg.setVisibility(View.GONE);
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Throwable error, String content) {
                Utility.showLog("error", error.toString());
                tv_no_data.setVisibility(View.VISIBLE);
                lv_reg.setVisibility(View.GONE);
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss();
                }
            }
        });
    }
    @Override
    public void updateList(String id) {
        for (int i = 0; i < meterChangeListModels.size(); i++) {
            if (id.equalsIgnoreCase(meterChangeListModels.get(i).getUSCNO())) {
                meterChangeListModels.remove(i);
                if (meterChangeListModels.size() > 0) {
                    meterChangeListAdapter.notifyDataSetChanged();
                }else {
                    tv_no_data.setVisibility(View.VISIBLE);
                    lv_reg.setVisibility(View.GONE);
                }
                break;
            }
        }
    }
}
