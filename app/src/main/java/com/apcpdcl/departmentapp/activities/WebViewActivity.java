package com.apcpdcl.departmentapp.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.apcpdcl.departmentapp.R;
import com.apcpdcl.departmentapp.shared.AppPrefs;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class WebViewActivity extends AppCompatActivity {

    private WebView webView;
    private TextView tv_sub_station;
    private ProgressDialog progressDialog;
    private String mUrl = "";
    private String sectionCode = "";
    private String userName = "";
    private String mFrom = "";
    private String strSeccode, sUserId;
    private TextView toolbar_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        //init();
    }

    public void kwhReadings(View view) {
        Intent intent = new Intent(WebViewActivity.this, ReadingEntry.class);
        intent.putExtra("ReadingType", "Feeder");
        startActivity(intent);
    }

    public void kvahReadings(View view) {
        Intent intent = new Intent(WebViewActivity.this, ReadingEntry.class);
        intent.putExtra("ReadingType", "Feeder");
        startActivity(intent);
    }

    public void agl3phReadings(View view) {
        Intent intent = new Intent(WebViewActivity.this, ReadingEntry.class);
        intent.putExtra("ReadingType", "AGL 3-Ph Feeder");
        startActivity(intent);
    }

    private void init() {
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        tv_sub_station = (TextView) findViewById(R.id.tv_sub_station);
        //mFrom = getIntent().getExtras().getString(Constants.FROM);
//        SharedPreferences prefs = getSharedPreferences("loginPrefs", 0);
//        userName = prefs.getString("UserName", "");
//        sectionCode = prefs.getString("SSCODE", "");
//        String deviceip= userName;// getDeviceIp();

        strSeccode = AppPrefs.getInstance(this).getString("SECTIONCODE", "");
        sUserId = AppPrefs.getInstance(this).getString("USERID", "");
//        mUrl = "https://10.64.22.36:44300/sap/bc/ui2/flp?sap-client=222&sap-language=EN#Shell-home";
        mUrl = "https://10.64.22.36:44300/sap/bc/ui2/flp?sap-client=999&sap-language=EN#Shell-home";
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading...");
        progressDialog.show();
        webView = (WebView) findViewById(R.id.webView);
        startWebView(mUrl);
//        if (!Utility.isValueNullOrEmpty(mFrom)) {
//            if (mFrom.equalsIgnoreCase(Constants.FEEDER_READING)) {
//                tv_sub_station.setVisibility(View.GONE);
//                sectionCode = prefs.getString("Section_Code", "");
//                mUrl = "http://sbm.apcpdcl.in:8095/ssshowss?uid=" + sectionCode;
//                Utility.showLog("mUrl", mUrl);
//            }else if (mFrom.equalsIgnoreCase(FeederDashBoardActivity.class.getSimpleName())){
//
//                mUrl = "http://sbm.apcpdcl.in:8095/ssforms?sscode=" + sectionCode+"&userid="+userName+"&remoteip="+deviceip;
//                Utility.showLog("mUrl", mUrl);
//                tv_sub_station.setVisibility(View.VISIBLE);
//                tv_sub_station.setText(prefs.getString("SSNAME", ""));
//          /*  } else {
//                toolbar_title.setText("REPORTS");
//                mUrl = "http://10.16.1.244:8080/Reports/";
//                tv_sub_station.setVisibility(View.GONE);*/
//            }
//            progressDialog = new ProgressDialog(this);
//            progressDialog.setTitle("Loading...");
//            progressDialog.show();
//            webView = (WebView) findViewById(R.id.webView);
//            startWebView(mUrl);
//        }

    }

    private String getDeviceIp() {
        String IPaddress = "";
        boolean WIFI = false;
        boolean MOBILE = false;

        ConnectivityManager CM = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo[] networkInfo = CM.getAllNetworkInfo();

        for (NetworkInfo netInfo : networkInfo) {

            if (netInfo.getTypeName().equalsIgnoreCase("WIFI"))

                if (netInfo.isConnected())

                    WIFI = true;

            if (netInfo.getTypeName().equalsIgnoreCase("MOBILE"))

                if (netInfo.isConnected())

                    MOBILE = true;
        }

        if (WIFI == true) {
            WifiManager wm = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);

            IPaddress = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());

            return IPaddress;
        }

        if (MOBILE == true) {

            try {
                for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
                     en.hasMoreElements(); ) {
                    NetworkInterface networkinterface = en.nextElement();
                    for (Enumeration<InetAddress> enumIpAddr = networkinterface.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                        InetAddress inetAddress = enumIpAddr.nextElement();
                        if (!inetAddress.isLoopbackAddress()) {
                            return inetAddress.getHostAddress().toString();
                        }
                    }
                }
            } catch (Exception ex) {
                Log.e("Current IP", ex.toString());
            }


        }
        return null;

    }

    private void startWebView(String url) {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setSupportMultipleWindows(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                // webView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(WebViewActivity.this, "Error:" + description, Toast.LENGTH_SHORT).show();
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }
        });
        webView.loadUrl(url);
    }
}
