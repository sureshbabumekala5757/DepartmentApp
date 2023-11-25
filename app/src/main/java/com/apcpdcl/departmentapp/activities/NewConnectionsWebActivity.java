package com.apcpdcl.departmentapp.activities;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.apcpdcl.departmentapp.R;
import com.apcpdcl.departmentapp.utils.Utility;

public class NewConnectionsWebActivity extends AppCompatActivity {

    private WebView webView;
    private ProgressDialog progressDialog;
    private String mUrl = "";
    private String LMCode = "";
    private String mFrom = "";
    private TextView toolbar_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        init();
    }

    private void init() {
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        SharedPreferences prefs = getSharedPreferences("loginPrefs", 0);
        LMCode = prefs.getString("LMCode", "");
        // mUrl =
        // "http://103.231.215.245:8080/CSCCPDCL/forms/lmresponse_nos.jsp?lmcode=" +
        // LMCode;
        mUrl = "http://112.133.252.110:8080/CSCCPDCL/forms/lmresponse_nos.jsp?lmcode=" + LMCode;
        toolbar_title.setText("New Connections To Be Released");
        Utility.showLog("mUrl", mUrl);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading...");
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        webView = (WebView) findViewById(R.id.webView);
        startWebView(mUrl);
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
                Toast.makeText(NewConnectionsWebActivity.this, "Error:" + description, Toast.LENGTH_SHORT).show();
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }
        });
        webView.loadUrl(url);
    }
}
