package com.apcpdcl.departmentapp.activities;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.apcpdcl.departmentapp.R;
import com.apcpdcl.departmentapp.adapters.OtherAdapter;
import com.apcpdcl.departmentapp.customviews.ExpandableLayoutListener;
import com.apcpdcl.departmentapp.customviews.Utils;
import com.apcpdcl.departmentapp.models.PendingData;
import com.apcpdcl.departmentapp.services.NetworkReceiver;
import com.apcpdcl.departmentapp.shared.AppPrefs;
import com.apcpdcl.departmentapp.sqlite.PendingDatabaseHandler;
import com.apcpdcl.departmentapp.utils.Constants;
import com.apcpdcl.departmentapp.customviews.ExpandableRelativeLayout;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by Admin on 31-01-2018.
 */

public class OperatingAllDetails extends Activity implements View.OnClickListener {
    String sec_code, strBldiscdt, strStype, strcmcname, strMeterNum, strtotal, strlastpaydt,
            strSocialCat, strcmcat, strGovtCat, strLastpayamnt, strDtrCode,
            strAddr, strLocation, strPhone, strLat, strLong,strPole;
    public static String strcmuscno;
    TextView servicenumtxt, consumernametxt, catgeorytxt, totalamntduetxt,tv_pole,
            dateofdisconnectiontxt, lastpaydatext, lastpayamounttxt, servicetypetxt, socialcatgeorytxt,
            govtcodetxt, dtrcodetxt, consumeraddrtxt, consumerloctxt, consumerphonetxt, norecordstxt_all;
    Button submitbtn;
    private RadioGroup userTypeRadio;
    private RadioButton disconnectradio, paymentdoneradio, userradiobutton;
    String strRadio = "", status, strPtprno, strPrdt, strAmount, strScnum;
    ProgressDialog pDialog;
    String tag_json_obj = "json_obj_req";
    NetworkReceiver objNetworkReceiver = new NetworkReceiver();
    public SharedPreferences operatingAllPreferences;
    SharedPreferences.Editor operatingAllPrefsEditor;
    ImageView home_imageView;
    RelativeLayout consumerdetails_layout, otherdetails_layout;
    ExpandableRelativeLayout consumerdetails_expandable, otherdetails_expandable;
    ImageView iv_consumer_details, iv_other_details;
    private ListView other_ListView;
    private ArrayList<PendingData> pendingDataArrayList = new ArrayList<>();
    PendingDatabaseHandler pdb;
    OtherAdapter otherAdapter;
    public boolean expand = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.operatingalldetails);

//        SharedPreferences prefs = getSharedPreferences("loginPrefs", 0);
//        sec_code = prefs.getString("Section_Code", "");
        sec_code = AppPrefs.getInstance(getApplicationContext()).getString("SECTIONCODE", "");


        pDialog = new ProgressDialog(OperatingAllDetails.this);
        pdb = new PendingDatabaseHandler(this);

        consumerdetails_layout = (RelativeLayout) findViewById(R.id.consumerdetails_layout);
        consumerdetails_expandable = (ExpandableRelativeLayout) findViewById(R.id.consumerdetails_expandable);
        iv_consumer_details = (ImageView) findViewById(R.id.iv_consumer_details);
        consumerdetails_layout.setOnClickListener(this);

        consumerdetails_expandable.setListener(new ExpandableLayoutListener() {
            @Override
            public void onAnimationStart() {

            }

            @Override
            public void onAnimationEnd() {

            }

            @Override
            public void onPreOpen() {
                createRotateAnimator(iv_consumer_details, 0f, 180f).start();
            }

            @Override
            public void onPreClose() {
                createRotateAnimator(iv_consumer_details, 180f, 0f).start();
            }

            @Override
            public void onOpened() {
            }

            @Override
            public void onClosed() {
            }
        });


        otherdetails_layout = (RelativeLayout) findViewById(R.id.otherdetails_layout);
        otherdetails_expandable = (ExpandableRelativeLayout) findViewById(R.id.otherdetails_expandable);
        iv_other_details = (ImageView) findViewById(R.id.iv_other_details);
        otherdetails_layout.setOnClickListener(this);


        otherdetails_expandable.setListener(new ExpandableLayoutListener() {
            @Override
            public void onAnimationStart() {

            }

            @Override
            public void onAnimationEnd() {

            }

            @Override
            public void onPreOpen() {
                createRotateAnimator(iv_other_details, 0f, 180f).start();
            }

            @Override
            public void onPreClose() {
                createRotateAnimator(iv_other_details, 180f, 0f).start();
            }

            @Override
            public void onOpened() {
            }

            @Override
            public void onClosed() {
            }
        });


        servicenumtxt = (TextView) findViewById(R.id.servicenumtxt);
        consumernametxt = (TextView)findViewById(R.id.consumernametxt);
        catgeorytxt = (TextView)findViewById(R.id.catgeorytxt);
        tv_pole = (TextView)findViewById(R.id.tv_pole);
        totalamntduetxt = (TextView)findViewById(R.id.totalamntduetxt);
        dateofdisconnectiontxt = (TextView)findViewById(R.id.dateofdisconnectiontxt);
        lastpaydatext = (TextView)findViewById(R.id.lastpaydatext);
        lastpayamounttxt = (TextView)findViewById(R.id.lastpayamounttxt);
        servicetypetxt = (TextView)findViewById(R.id.servicetypetxt);
        socialcatgeorytxt = (TextView)findViewById(R.id.socialcatgeorytxt);
        govtcodetxt = (TextView)findViewById(R.id.govtcodetxt);
        dtrcodetxt = (TextView)findViewById(R.id.dtrcodetxt);
        consumeraddrtxt = (TextView)findViewById(R.id.consumeraddrtxt);
        consumerloctxt =(TextView) findViewById(R.id.consumerloctxt);
        consumerphonetxt = (TextView)findViewById(R.id.consumerphonetxt);
        norecordstxt_all = (TextView)findViewById(R.id.norecordstxt_all);

        userTypeRadio = (RadioGroup) findViewById(R.id.userTypeRadio);
        home_imageView = (ImageView) findViewById(R.id.home);
        other_ListView = (ListView) findViewById(R.id.otherdetailslistview);

        disconnectradio = (RadioButton) findViewById(R.id.disconnectradio);
        paymentdoneradio = (RadioButton)findViewById(R.id.paymentdoneradio);

        submitbtn = (Button)findViewById(R.id.submitbtn);

    }

    private void paymentDone(String response) {
        try {
            JSONObject obj = new JSONObject(response);
            status = obj.getString("STATUS");
            strPtprno = obj.getString("PTPRNO");
            strPrdt = obj.getString("PRDT");
            strAmount = obj.getString("AMT");
            strScnum = obj.getString("SCNO");

            Intent intent = new Intent(getApplicationContext(), PaymentDone.class);
            intent.putExtra("PTPRNO", strPtprno);
            intent.putExtra("PRDT", strPrdt);
            intent.putExtra("AMT", strAmount);
            intent.putExtra("SCNO", strScnum);
            intent.putExtra("DiscDate", strBldiscdt);
            startActivity(intent);
            finish();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public boolean IsNullOrBlank(String Input) {
        if (Input == null)
            return true;
        else
            return Input.trim() == "" || Input.trim().length() == 0;
    }

    @Override
    protected void onResume() {
        super.onResume();
        //consumerdetails_expandable.expand();
        ((RadioButton) userTypeRadio.getChildAt(0)).setChecked(true);
        ((RadioButton) userTypeRadio.getChildAt(1)).setChecked(false);
        ((RadioButton) userTypeRadio.getChildAt(2)).setChecked(false);

        Intent intent = getIntent();
        Bundle bd = intent.getExtras();
        if (bd != null) {
            strBldiscdt = (String) bd.get("BLDISCDT");
            strStype = (String) bd.get("STYPE");
            strcmcname = (String) bd.get("CMCNAME");
            strMeterNum = (String) bd.get("MTRNO");
            strcmuscno = (String) bd.get("CMUSCNO");
            strtotal = (String) bd.get("TOT");
            strlastpaydt = (String) bd.get("LASTPAIDDT");
            strSocialCat = (String) bd.get("CMSOCIALCAT");
            strcmcat = (String) bd.get("CMCAT");
            strGovtCat = (String) bd.get("CMGOVT");
            strLastpayamnt = (String) bd.get("LASTPAIDAMT");
            strDtrCode = (String) bd.get("CMDTRCODE");
            strAddr = (String) bd.get("CMADDRESS");
            strLocation = (String) bd.get("LOCATION");
            strPhone = (String) bd.get("CMPHONE");
            strLat = (String) bd.get("LAT");
            strLong = (String) bd.get("LONG");
            strPole = (String) bd.get("Pole");


            servicenumtxt.setText(strcmuscno);
            consumernametxt.setText(strcmcname);
            catgeorytxt.setText(strcmcat);
            tv_pole.setText(strPole);
            totalamntduetxt.setText(strtotal);
            dateofdisconnectiontxt.setText(strBldiscdt);
            lastpaydatext.setText(strlastpaydt);
            lastpayamounttxt.setText(strLastpayamnt);
            servicetypetxt.setText(strStype);
            socialcatgeorytxt.setText(strSocialCat);
            govtcodetxt.setText(strGovtCat);
            dtrcodetxt.setText(strDtrCode);
            consumeraddrtxt.setText(strAddr);
            consumerloctxt.setText(strLocation);
            consumerphonetxt.setText(strPhone);


//            pendingDataArrayList.clear();
//            pendingDataArrayList.addAll(pdb.getAllPendingData());
//
//            if (pendingDataArrayList.size() > 0) {
//                otherAdapter = new OtherAdapter(OperatingAllDetails.this, pendingDataArrayList);
//                other_ListView.setAdapter(otherAdapter);
//            } else {
//                norecordstxt_all.setVisibility(View.VISIBLE);
//            }


            operatingAllPreferences = getSharedPreferences("OperatingAllPrefs", MODE_PRIVATE);
            operatingAllPrefsEditor = operatingAllPreferences.edit();
            operatingAllPrefsEditor.putString("Service_Number", strcmuscno);
            operatingAllPrefsEditor.apply();
        }
        home_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(OperatingAllDetails.this,Home.class);
                i.setFlags(i.FLAG_ACTIVITY_NEW_TASK | i.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);

            }
        });

        submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = userTypeRadio.getCheckedRadioButtonId();
                userradiobutton = (RadioButton) findViewById(selectedId);
                strRadio = (String) userradiobutton.getText();
                switch (strRadio) {
                    case "Disconnect": {
                        Intent intent = new Intent(getApplicationContext(), OperatingDetails.class);
                        intent.putExtra("servicenumber", strcmuscno);
                        intent.putExtra("MeterNum", strMeterNum);
                        intent.putExtra("DiscDate", strBldiscdt);
                        intent.putExtra("Location", strLocation);
                        intent.putExtra("LAT", strLat);
                        intent.putExtra("LONG", strLong);
                        startActivity(intent);
                        finish();
                        break;
                    }
                    case "VerifyPayment":
                        if (objNetworkReceiver.hasInternetConnection(OperatingAllDetails.this)) {
                            try {
                                RequestParams params = new RequestParams();
                                params.put("SCNO", strcmuscno);
                                pDialog.setMessage("Loading...");
                                pDialog.show();
                                AsyncHttpClient client = new AsyncHttpClient();
                                client.post(Constants.PAY_DETAILS, params, new AsyncHttpResponseHandler() {
                                    @Override
                                    public void onSuccess(String response) {
                                        try {
                                            JSONObject obj = new JSONObject(response);
                                            String status = obj.getString("STATUS");
                                            if (status.equals("TRUE")) {
                                                if (pDialog != null && pDialog.isShowing()) {
                                                    pDialog.dismiss();
                                                }
                                                paymentDone(response);

                                            } else if (status.equals("NO DATA")) {
                                                if (pDialog != null && pDialog.isShowing()) {
                                                    pDialog.dismiss();
                                                }
                                                Intent intent = new Intent(getApplicationContext(), PaymentDone.class);
                                                intent.putExtra("DiscDate", strBldiscdt);
                                                intent.putExtra("LAT", strLat);
                                                intent.putExtra("LONG", strLong);
                                                startActivity(intent);
                                                finish();
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    @Override
                                    public void onFailure(int statusCode, Throwable error, String content) {
                                        if (pDialog != null && pDialog.isShowing()) {
                                            pDialog.dismiss();
                                        }
                                        switch (statusCode) {
                                            case 404:
                                                Toast.makeText(getApplicationContext(), "Unable to Connect Server", Toast.LENGTH_LONG).show();
                                                break;
                                            case 500:
                                                Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                                                break;
                                            default:
                                                Toast.makeText(getApplicationContext(), "Check Your Internet Connection and Try Again", Toast.LENGTH_LONG).show();
                                                break;
                                        }
                                        Log.e("error", error.toString());
                                    }
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            Intent intent = new Intent(getApplicationContext(), PaymentDone.class);
                            intent.putExtra("DiscDate", strBldiscdt);
                            intent.putExtra("LAT", strLat);
                            intent.putExtra("LONG", strLong);
                            startActivity(intent);
                            finish();

                        }
                        break;
                    case "Unable to DisConnect": {
                        Intent intent = new Intent(getApplicationContext(), UnableDisconnection.class);
                        intent.putExtra("servicenumber", strcmuscno);
                        intent.putExtra("DiscDate", strBldiscdt);
                        intent.putExtra("LAT", strLat);
                        intent.putExtra("LONG", strLong);
                        startActivity(intent);
                        finish();
                        break;
                    }
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.consumerdetails_layout:
                consumerdetails_expandable.toggle();
                break;
            case R.id.otherdetails_layout:
                if (objNetworkReceiver.hasInternetConnection(OperatingAllDetails.this)) {
                    otherdetails_expandable.toggle();
                    iv_other_details.setVisibility(View.VISIBLE);
                    otherdetails_expandable.setVisibility(View.VISIBLE);
                    RequestParams params = new RequestParams();
                    params.put("SCNO", strcmuscno);
                    invokeMatsWebService(params);
                } else {
                    iv_other_details.setVisibility(View.GONE);
                    otherdetails_expandable.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Please check your Internet connection", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    private void invokeMatsWebService(RequestParams params) {

        pDialog.show();
        pDialog.setMessage("Please wait...");
        pdb.clearTable();

        AsyncHttpClient client = new AsyncHttpClient();
        client.post(Constants.MATS, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    String status = obj.getString("STATUS");
                    switch (status) {
                        case "SUCCESS":
                            if (pDialog != null && pDialog.isShowing()) {
                                pDialog.dismiss();
                            }
                            JSONArray matjsonArray = (JSONArray) obj.get("MATS");
                            for (int i = 0; i < matjsonArray.length(); i++) {
                                JSONObject matjsonObject = matjsonArray.getJSONObject(i);
                                String strTotAmnt = matjsonObject.getString("TOTALAMOUNT");
                                String strStatus = matjsonObject.getString("STATUS");
                                String strPendingAmnt = matjsonObject.getString("PENDINGAMOUNT");
                                String strType = matjsonObject.getString("TYPE");
                                String strCaseNum = matjsonObject.getString("CASENO");
                                pdb.addPendingData(new PendingData(strcmuscno, strTotAmnt, strStatus, strPendingAmnt, strType, strCaseNum));
                            }
                            try {
                                pendingDataArrayList.clear();
                                pendingDataArrayList.addAll(pdb.getAllPendingData());

                                if (pendingDataArrayList.size() > 0) {
                                    otherAdapter = new OtherAdapter(OperatingAllDetails.this, pendingDataArrayList);
                                    other_ListView.setAdapter(otherAdapter);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            break;
                        case "NODATA":
                            if (pDialog != null && pDialog.isShowing()) {
                                pDialog.dismiss();
                            }
                            norecordstxt_all.setVisibility(View.VISIBLE);
                            break;
                        case "ERROR":
                            if (pDialog != null && pDialog.isShowing()) {
                                pDialog.dismiss();
                            }
                            Toast.makeText(getApplicationContext(), "Please Contact Your Adminstrator", Toast.LENGTH_LONG).show();
                            break;
                        case "FAIL":
                            if (pDialog != null && pDialog.isShowing()) {
                                pDialog.dismiss();
                            }
                            Toast.makeText(getApplicationContext(), "Please check once", Toast.LENGTH_LONG).show();
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Throwable error, String content) {
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss();
                }
                switch (statusCode) {
                    case 404:
                        Toast.makeText(getApplicationContext(), "Unable to Connect Server", Toast.LENGTH_LONG).show();
                        break;
                    case 500:
                        Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                        break;
                    default:
                        Toast.makeText(getApplicationContext(), "Check Your Internet Connection and Try Again", Toast.LENGTH_LONG).show();
                        break;
                }
                Log.e("error", error.toString());
            }
        });
    }

    /*Rotate Animator for buttons*/
    public ObjectAnimator createRotateAnimator(final View target, final float from, final float to) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(target, "rotation", from, to);
        animator.setDuration(300);
        animator.setInterpolator(Utils.createInterpolator(Utils.LINEAR_INTERPOLATOR));
        return animator;
    }
}
