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
import com.apcpdcl.departmentapp.adapters.PollsListAdapter;
import com.apcpdcl.departmentapp.models.PollModel;
import com.apcpdcl.departmentapp.utils.Constants;
import com.apcpdcl.departmentapp.utils.Utility;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Haseen
 * on 24-03-2018.
 */

public class ExistingPollListActivity extends AppCompatActivity {
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
    private ArrayList<PollModel> registrationModels;
    private PollsListAdapter registrationListAdapter;
    public ProgressDialog pDialog;
    public Toolbar mToolbar;
    private String sectionCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poll_list);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        ButterKnife.bind(this);
        init();
    }

    /*Initialize Views*/
    private void init() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        pDialog = new ProgressDialog(ExistingPollListActivity.this);
        if (Utility.isNetworkAvailable(this)) {
            pDialog.show();
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            getRegistrationNumbers();
        } else {
            Utility.showCustomOKOnlyDialog(this,
                    Utility.getResourcesString(this,
                            R.string.no_internet));
        }
    }


    /*
     *Get Service Numbers
     * */
    private void getRegistrationNumbers() {
        AsyncHttpClient client = new AsyncHttpClient();

        client.setTimeout(50000);
        client.post(Constants.URL_GL+"poll/list", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                Utility.showLog("onSuccess", response);
                /*if (prgDialog != null && prgDialog.isShowing()) {
                    prgDialog.dismiss();
                }*/
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.has("DATA")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("DATA");
                        registrationModels = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject json = jsonArray.optJSONObject(i);
                            PollModel registrationModel = new Gson().fromJson(json.toString(),
                                    PollModel.class);
                            registrationModels.add(registrationModel);
                        }

                        if (registrationModels.size() > 0) {
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
                Utility.showCustomOKOnlyDialog(ExistingPollListActivity.this, "Currently Server is down, Please try again.");
            }
        });
    }

    /*
     *Set Service List DAta to ListView
     * */
    private void setListData() {
        registrationListAdapter = new PollsListAdapter(this, registrationModels);
        lv_reg.setAdapter(registrationListAdapter);
        implementsSearch();
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
        }
    }

    //IMPLEMENT SEARCH
    private void implementsSearch() {
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (registrationListAdapter != null)
                    registrationListAdapter.getFilter().filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }


}
