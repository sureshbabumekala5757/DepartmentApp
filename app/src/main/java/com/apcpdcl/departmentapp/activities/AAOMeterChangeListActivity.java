package com.apcpdcl.departmentapp.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.apcpdcl.departmentapp.R;
import com.apcpdcl.departmentapp.adapters.AAOMeterChangeListAdapter;
import com.apcpdcl.departmentapp.adapters.SpinnerAdapter;
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

public class AAOMeterChangeListActivity extends AppCompatActivity implements IUpdateList {
    @BindView(R.id.lv_reg)
    ListView lv_reg;
    @BindView(R.id.tv_no_data)
    TextView tv_no_data;
    @BindView(R.id.toolbar_title)
    TextView toolbar_title;
    @BindView(R.id.ll_spinner)
    LinearLayout ll_spinner;
    @BindView(R.id.spn_section)
    Spinner spn_section;
    private ArrayList<MeterChangeListModel> meterChangeListModels = new ArrayList<>();
    private AAOMeterChangeListAdapter meterChangeListAdapter;
    private ProgressDialog pDialog;
    private String sectionCode = "";
    private String mUserName = "";
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
        toolbar_title.setText(Utility.getResourcesString(this, R.string.pending_o_m_list));
        ll_spinner.setVisibility(View.VISIBLE);
        iUpdateList = this;
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        android.content.SharedPreferences prefs = getSharedPreferences("loginPrefs", 0);
        mUserName = prefs.getString("UserName", "");
        pDialog = new ProgressDialog(AAOMeterChangeListActivity.this);
        if (Utility.isNetworkAvailable(this)) {
            pDialog.show();
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            getSectionsList();
        } else {
            Utility.showCustomOKOnlyDialog(this,
                    Utility.getResourcesString(this,
                            R.string.no_internet));
        }
    }

    private void setListData() {
        meterChangeListAdapter = new AAOMeterChangeListAdapter(this, meterChangeListModels, mFrom);
        lv_reg.setAdapter(meterChangeListAdapter);
        lv_reg.setVisibility(View.VISIBLE);
        tv_no_data.setVisibility(View.GONE);
    }

    /* *
     * Get METER CHANGE LIST
     * */
    private void getMeterChangeList() {
        AsyncHttpClient client = new AsyncHttpClient();
        Utility.showLog("UNITCODE", sectionCode);
        RequestParams params = new RequestParams();
        params.put("SECCD", sectionCode);
        client.setTimeout(50000);
        client.post(Constants.URL_ERO + "pending_status", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                Utility.showLog("onSuccess", response);
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss();
                }
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.has("MeterChangeDetails")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("MeterChangeDetails");
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
     * Get SECTIONS
     * */
    private void getSectionsList() {
        AsyncHttpClient client = new AsyncHttpClient();
        String url;
        Utility.showLog("UNITCODE", sectionCode);
        RequestParams params = new RequestParams();
        params.put("EROCD", mUserName);
        client.setTimeout(50000);
        client.post(Constants.URL_ERO + "section", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                Utility.showLog("onSuccess", response);
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss();
                }
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.has("Section")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("Section");
                        ArrayList<String> sections = new ArrayList<>();
                        for (int j = 0; j < jsonArray.length(); j++) {
                            sections.add(jsonArray.getString(j));
                        }
                        if (sections.size() > 0) {
                            setSpinnerData(sections);
                        } else {
                            if (pDialog != null && pDialog.isShowing()) {
                                pDialog.dismiss();
                            }
                            tv_no_data.setVisibility(View.GONE);
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

    private void setSpinnerData(ArrayList<String> sections) {
        sections.add(0, "--Select--");

        SpinnerAdapter newlineAdapter = new SpinnerAdapter(this,
                android.R.layout.simple_spinner_item, sections);
        spn_section.setAdapter(newlineAdapter);
        spn_section.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View arg1, int position, long arg3) {
                if (position != 0) {
                    sectionCode = parent.getItemAtPosition(position).toString();
                    if (Utility.isNetworkAvailable(AAOMeterChangeListActivity.this)) {
                        pDialog.show();
                        getMeterChangeList();
                    } else {
                        Utility.showCustomOKOnlyDialog(AAOMeterChangeListActivity.this,
                                Utility.getResourcesString(AAOMeterChangeListActivity.this,
                                        R.string.no_internet));
                    }
                } else {
                    sectionCode = "";
                    tv_no_data.setVisibility(View.GONE);
                    lv_reg.setVisibility(View.GONE);
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
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
                        Utility.showCustomOKOnlyDialog(AAOMeterChangeListActivity.this, jsonObject.optString("MSG"));
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
                } else {
                    tv_no_data.setVisibility(View.VISIBLE);
                    lv_reg.setVisibility(View.GONE);
                }
                break;
            }
        }
    }
}
