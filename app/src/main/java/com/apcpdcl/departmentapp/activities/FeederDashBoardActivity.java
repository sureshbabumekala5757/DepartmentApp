package com.apcpdcl.departmentapp.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apcpdcl.departmentapp.R;
import com.apcpdcl.departmentapp.utils.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FeederDashBoardActivity extends AppCompatActivity {

    @BindView(R.id.ll_feeder_readings)
    LinearLayout ll_feeder_readings;

    @BindView(R.id.ll_feeder)
    LinearLayout ll_feeder;
    @BindView(R.id.iv_feeder)
    ImageView iv_feeder;
    @BindView(R.id.tv_feeder)
    TextView tv_feeder;

    @BindView(R.id.tv_sub_station)
    TextView tv_sub_station;

    private ProgressDialog pDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feeder_dashboard_activity);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        ButterKnife.bind(this);
        init();
    }

    /*Initialize Views*/
    private void init() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        SharedPreferences prefs = getSharedPreferences("loginPrefs", 0);
        tv_sub_station.setText(prefs.getString("SSNAME", ""));
        pDialog = new ProgressDialog(FeederDashBoardActivity.this);
        pDialog.setCancelable(false);

    }


    @OnClick({R.id.ll_feeder, R.id.iv_feeder, R.id.tv_feeder})
    void navigateToServiceDetails() {
        Intent in = new Intent(getApplicationContext(), FeederOutagesActivity.class);
        in.putExtra(Constants.FROM, "true");
        startActivity(in);
    }

    @OnClick(R.id.ll_feeder_readings)
    void navigateToWebView() {
      /*  if (Utility.isNetworkAvailable(this)) {
            createAESession();
        } else {
            Utility.showCustomOKOnlyDialog(this, Utility.getResourcesString(this, R.string.no_internet));
        }*/
        Intent intent = new Intent(FeederDashBoardActivity.this, WebViewActivity.class);
        intent.putExtra(Constants.FROM, FeederDashBoardActivity.class.getSimpleName());
        startActivity(intent);
    }

    /*Check Latest version API*/
   /* private void createAESession() {
        SharedPreferences prefs = getSharedPreferences("loginPrefs", 0);
        pDialog.show();
        String userId = prefs.getString("AEUSER", "");
        String pwd = prefs.getString("AEPWD", "");
        String mFinalURL = "http://103.231.214.192:8095/ords/eaudit/sschkuser?userid=" + userId + "&passwd=" + pwd;
        Utility.showLog("mFinalURL", mFinalURL);
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(mFinalURL, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                Utility.showLog("response", response);
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss();
                }
                Intent intent = new Intent(FeederDashBoardActivity.this, WebViewActivity.class);
                intent.putExtra(Constants.FROM, FeederDashBoardActivity.class.getSimpleName());
                startActivity(intent);
              *//*  Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://122.252.251.174:7070/ords/ssshowss?uid=" + sectionCode));
                startActivity(intent);*//*
            }

            @Override
            public void onFailure(int statusCode, Throwable error, String content) {
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss();
                }
                Utility.showCustomOKOnlyDialog(FeederDashBoardActivity.this,
                        Utility.getResourcesString(FeederDashBoardActivity.this, R.string.err_session));
                Utility.showLog("error", error.toString());
            }
        });
    }*/
}
