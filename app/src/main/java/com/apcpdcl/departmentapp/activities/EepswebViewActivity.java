package com.apcpdcl.departmentapp.activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.http.SslCertificate;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.webkit.PermissionRequest;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.apcpdcl.departmentapp.R;
import com.apcpdcl.departmentapp.utils.Utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import pub.devrel.easypermissions.EasyPermissions;

public class EepswebViewActivity extends AppCompatActivity implements LocationListener, EasyPermissions.PermissionCallbacks {

    private WebView webView;
    private ProgressDialog progressDialog;
    private String mUrl = "";
    private TextView toolbar_title;
    private String strRegnum = "";
    private String strLatitude = "";
    private String strLongitude = "";
    LocationManager locationManager;
    String mprovider;
    Button btn_home;
    String permission = Manifest.permission.CAMERA;
    private PermissionRequest mPermissionRequest;
    private static final int REQUEST_CAMERA_PERMISSION = 1;
    private static final String[] PERM_CAMERA =
            {Manifest.permission.CAMERA};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eeps_webview);
        init();
    }

    private void init() {
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        btn_home = (Button) findViewById(R.id.btn_home);
/*
        int grant = ContextCompat.checkSelfPermission(this, permission);
        if (grant != PackageManager.PERMISSION_GRANTED) {
            String[] permission_list = new String[1];
            permission_list[0] = permission;
            ActivityCompat.requestPermissions(this, permission_list, 1);
        }*/

        Intent intent = getIntent();
        Bundle bd = intent.getExtras();
        if (bd != null) {
            strRegnum = (String) bd.get("RegNo");
        }
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        mprovider = locationManager.getBestProvider(criteria, false);

        if (mprovider != null && !mprovider.equals("")) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            Location location = locationManager.getLastKnownLocation(mprovider);
            locationManager.requestLocationUpdates(mprovider, 15000, 1, (LocationListener) this);

            if (location != null)
                onLocationChanged(location);
            else
                Toast.makeText(getBaseContext(), "No Location Provider Found Check Your Code", Toast.LENGTH_SHORT).show();
        }

        mUrl = "https://10.50.55.47/EESL_RestService/EESLview.jsp?regNo=" + strRegnum + "&latitude=" + strLatitude + "&longitude=" + strLongitude;
        toolbar_title.setText("Eeps Unit");
        Utility.showLog("mUrl", mUrl);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading...");
        progressDialog.show();
        webView = (WebView) findViewById(R.id.eeps_webView);
        /*startWebView(mUrl);*/
        webView.setWebViewClient(new CustomWebViewClient(EepswebViewActivity.this));
        startWebChromeClient(mUrl);


        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AeDashBoardActivity.class);
                startActivity(intent);
            }
        });
    }
    private void startWebChromeClient(String url) {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAllowFileAccessFromFileURLs(true);
        webView.getSettings().setAllowUniversalAccessFromFileURLs(true);

        //myWebView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient() {
            // Grant permissions for cam
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onPermissionRequest(final PermissionRequest request) {
                mPermissionRequest = request;
                final String[] requestedResources = request.getResources();
                for (String r : requestedResources) {
                    if (r.equals(PermissionRequest.RESOURCE_VIDEO_CAPTURE)) {
                        // In this sample, we only accept video capture request.
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(EepswebViewActivity.this)
                                .setTitle("Allow Permission to camera")
                                .setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        mPermissionRequest.grant(new String[]{PermissionRequest.RESOURCE_VIDEO_CAPTURE});
                                    }
                                })
                                .setNegativeButton("Deny", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        mPermissionRequest.deny();
                                    }
                                });
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                        break;
                    }
                }
            }

            @Override
            public void onPermissionRequestCanceled(PermissionRequest request) {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                super.onPermissionRequestCanceled(request);
                Toast.makeText(EepswebViewActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        });


        if (hasCameraPermission()) {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            webView.loadUrl(mUrl);
        } else {
            EasyPermissions.requestPermissions(this, "This app needs access to your camera so you can take pictures.", REQUEST_CAMERA_PERMISSION, PERM_CAMERA);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        strLatitude = String.valueOf(location.getLatitude());
        strLongitude = String.valueOf(location.getLongitude());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


    private boolean hasCameraPermission() {
        return EasyPermissions.hasPermissions(EepswebViewActivity.this, PERM_CAMERA);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

    }


    private class CustomWebViewClient extends WebViewClient {

        Activity parentActivity;

        public CustomWebViewClient(Activity activity) {
            parentActivity = activity;
        }

        @Override
        public void onPageStarted(WebView webview, String url, Bitmap favicon) {
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }


        @Override
        public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {
            // Checks Embedded certificates
            SslCertificate serverCertificate = error.getCertificate();
            ArrayList<SslCertificate> certificates = new Certificates().loadSSLCertificates(getApplicationContext());
            Bundle serverBundle = SslCertificate.saveState(serverCertificate);
            for (SslCertificate appCertificate : certificates) {
                if (TextUtils.equals(serverCertificate.toString(), appCertificate.toString())) { // First fast check
                    Bundle appBundle = SslCertificate.saveState(appCertificate);
                    Set<String> keySet = appBundle.keySet();
                    boolean matches = true;
                    for (String key : keySet) {
                        Object serverObj = serverBundle.get(key);
                        Object appObj = appBundle.get(key);
                        if (serverObj instanceof byte[] && appObj instanceof byte[]) {     // key "x509-certificate"
                            if (!Arrays.equals((byte[]) serverObj, (byte[]) appObj)) {
                                matches = false;
                                break;
                            }
                        } else if ((serverObj != null) && !serverObj.equals(appObj)) {
                            matches = false;
                            break;
                        }
                    }
                    if (matches) {
                        handler.proceed();
                        return;
                    }
                }
            }

            handler.cancel();
            String message = "SSL Error " + error.getPrimaryError();
            //Log.w(TAG, message);

        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
        }
    }
}


