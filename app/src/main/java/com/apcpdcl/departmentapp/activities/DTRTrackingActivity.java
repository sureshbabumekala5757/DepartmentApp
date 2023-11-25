package com.apcpdcl.departmentapp.activities;

import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.apcpdcl.departmentapp.R;
import com.apcpdcl.departmentapp.adapters.DtrListviewAdapter;
import com.apcpdcl.departmentapp.fragments.DtrTrackingDetails;
import com.apcpdcl.departmentapp.fragments.DtrTrackingNotification;
import com.apcpdcl.departmentapp.services.ServiceConstants;
import com.apcpdcl.departmentapp.shared.AppPrefs;
import com.apcpdcl.departmentapp.utils.Constants;
import com.apcpdcl.departmentapp.utils.Utility;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;

public class DTRTrackingActivity extends AppCompatActivity {

   /* @BindView(R.id.btn_dtr_complaints)
    Button btn_dtr_complaints;
    @BindView(R.id.btn_pending_noti)
    Button btn_pending_noti;
    @BindView(R.id.btn_completed_noti)
    Button btn_completed_noti;*/

    @BindView(R.id.webView)
    WebView webView;
    private ProgressDialog pDialog;
    private String strSeccode,sUserId,costCenterCode;
    private ArrayList<HashMap> list;
    DtrListviewAdapter adapter;
    private Bundle bundle = new Bundle();
    @BindView(R.id.layoutBody)
    LinearLayout layoutBody;

    @BindView(R.id.frameLayout)
    FrameLayout frameLayout;

    ListView lview;
    TextView text_nodata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dtrtacking_main);
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
//        ButterKnife.bind(this);


        pDialog = new ProgressDialog(this);
        pDialog.show();
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);


        strSeccode = AppPrefs.getInstance(this).getString("SECTIONCODE", "");
        sUserId = AppPrefs.getInstance(this).getString("USERID", "");
        costCenterCode = AppPrefs.getInstance(DTRTrackingActivity.this).getString("COSTCENTER","");
        //Toast.makeText(getApplicationContext(), strSeccode, Toast.LENGTH_LONG).show();
        //     startWebView("http://103.231.215.245:8080/DELC/ComplaintStatusServlet?seccd="+strSeccode);
        /* startWebView("https://apspdcl.in/DELC/ComplaintStatusServlet?seccd="+strSeccode);*/

        layoutBody = findViewById(R.id.layoutBody);
        layoutBody.setVisibility(View.VISIBLE);

        frameLayout = findViewById(R.id.frameLayout);
        frameLayout.setVisibility(View.GONE);

        text_nodata = findViewById(R.id.text_nodata);
        text_nodata.setVisibility(View.GONE);

        lview = (ListView) findViewById(R.id.listview);
        list = new ArrayList<>();
        //populateList();
        adapter = new DtrListviewAdapter(this, list);
        lview.setAdapter(adapter);
        getDTRComplaints();

    }
    @Override
    public void onBackPressed() {
        if (frameLayout.getVisibility() == View.VISIBLE) {
            frameLayout.setVisibility(View.GONE);
            layoutBody.setVisibility(View.VISIBLE);
        } else if (layoutBody.getVisibility() == View.VISIBLE){
            finish();
        }

    }

    private void populateList() {
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
        }

        list = new ArrayList<HashMap>();
        HashMap temp = new HashMap();

        for (int i = 0; i < 10; i++) {
            temp = new HashMap();
            temp.put("COMPLAINT", "210723926" + i);
            temp.put("SECTION", "RACHERLA");
            temp.put("STATUS", "NOTIFICATION");
            if (i == 6)
                temp.put("STATUS", "CREATED");
            temp.put("DATE", "21-08-21 13:08:46");

            list.add(temp);
        }
    }

    /**************************************Previos code ******************************************/
    private void startWebView(String url) {
        webView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setSupportMultipleWindows(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                //view.loadDataWithBaseURL(url,"Relative Link","text/html","UTF-8",url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss();
                }
                // webView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(DTRTrackingActivity.this, "Error:" + description, Toast.LENGTH_SHORT).show();
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss();
                }
            }
        });
        webView.loadUrl(url);
    }

  /*  @OnClick(R.id.btn_dtr_complaints)
    void navigateToPendingDTRComplaints() {
        Intent in = new Intent(getApplicationContext(), PendingDTRComplaintsActivity.class);
        startActivity(in);
    }

    @OnClick(R.id.btn_pending_noti)
    void navigateToNotificationsPending() {

    }

    @OnClick(R.id.btn_completed_noti)
    void navigateToNoticationsCompleted() {
       *//* Intent in = new Intent(getApplicationContext(), LTLineSurveyActivity.class);
        in.putExtra(Constants.FROM, "LT LINE");
        startActivity(in);*//*
    }*/

    public void getListItem(HashMap<String, String> listItem) {
        if (listItem != null) {
            if (listItem.get("STATUS").equalsIgnoreCase("Open") || listItem.get("STATUS").equalsIgnoreCase("NOTIFICATION DELETED")) {
                bundle = new Bundle();
                bundle.putString("COMPLAINTID", listItem.get("COMPLAINT"));
                bundle.putString("SECTION_CODE", strSeccode);
                createdAlert(listItem.get("COMPLAINT"));
            }else if(listItem.get("STATUS").equalsIgnoreCase("SPM HEALTHY DTR ISSUED")){
                bundle = new Bundle();
                bundle.putString("COMPLAINTID", listItem.get("COMPLAINT"));
                bundle.putString("SECTION_CODE", strSeccode);
                DtrTrackingNotification myObj = new DtrTrackingNotification();
                myObj.setArguments(bundle);
                loadFragment(myObj);
            }
        }
    }
    public void createdAlert(String strMsg) {
        final Dialog dialog = new Dialog(this);//, R.style.LightThemeSelector);
        dialog.setCancelable(false);
        // hide to default title for Dialog
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dtr_created_status, null, false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(view);
        //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(R.color.transperant));

        // set the custom dialog components - text, image and button

        ImageView icon = (ImageView) dialog.findViewById(R.id.close_img_btn);
        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        TextView text_msg = (TextView) dialog.findViewById(R.id.tv_complaintid);
        text_msg.setText(strMsg);

        TextView fuseOfCallBtn = (TextView) dialog.findViewById(R.id.text_fuse_of_call);
        fuseOfCallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                Intent in = new Intent(view.getContext(), ComplaintDetailActivity.class);
                in.putExtra(Constants.COMPLAINT_DATA, strMsg);
                in.putExtra("DTRC", "Yes");
                view.getContext().startActivity(in);
            }
        });

        TextView dtrTrackingBtn = (TextView) dialog.findViewById(R.id.text_dtr_tracking);
        dtrTrackingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                DtrTrackingDetails myObj = new DtrTrackingDetails();
                myObj.setArguments(bundle);
                loadFragment(myObj);
            }
        });

        dialog.show();
    }

    private void loadFragment(Fragment fragment) {
        layoutBody.setVisibility(View.GONE);
        frameLayout.setVisibility(View.VISIBLE);

        // create a FragmentManager
        FragmentManager fm = getFragmentManager();
        // create a FragmentTransaction to begin the transaction and replace the Fragment
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        // replace the FrameLayout with new Fragment
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit(); // save the changes
    }

    private void dismissDialog() {
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
        }
    }

    /* *
     *Get DTR Complaints List
     * */
    private void getDTRComplaints() {
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(50000);

        BasicHeader[] headers = new BasicHeader[]{new BasicHeader("Authorization", "Basic "+AppPrefs.getInstance(getApplicationContext()).getString("USER_AUTH", ""))};
        StringEntity entity = null;
        try{
            JSONObject jsonParams = new JSONObject();
            jsonParams.put("bname", sUserId);
            jsonParams.put("type", "DTR");
            entity = new StringEntity(jsonParams.toString());
        }catch (Exception e){

        }
        client.post(this, ServiceConstants.USER_DTR_COMPLAINTLIST,headers,entity,"application/json", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String responseStr) {
                Utility.showLog("onSuccess", responseStr);
                try {
                      JSONObject resposneObj = new JSONObject(responseStr);
                      resposneObj = resposneObj.getJSONObject("response");
                      String status = resposneObj.getString("success");
                      if(status.equalsIgnoreCase("True")){
                          if (resposneObj.has("data")) {
                              JSONArray jsonArray = resposneObj.getJSONArray("data");
                              if(jsonArray != null && jsonArray.length()>0){
                                  list.clear();
                                  HashMap temp = new HashMap();
                                  JSONObject singleResponseObj = null;

                                  for (int i = 0; i < jsonArray.length() ; i++) {
                                      singleResponseObj = jsonArray.getJSONObject(i);
                                      if(singleResponseObj != null && singleResponseObj.length()>0) {
                                          temp = new HashMap();
                                          temp.put("COMPLAINT", singleResponseObj.getString("complaint_id"));
                                          temp.put("STATUS", singleResponseObj.getString("complaint_status"));
                                          temp.put("SECTION", costCenterCode);
                                          temp.put("DATE", singleResponseObj.getString("complaint_creation"));
//                                          if (i == 2)
//                                              temp.put("STATUS", "NOTIFICATION");
//                                          else
//                                              temp.put("STATUS", "CREATED");
//
//                                          temp.put("SECTION", "SEC0086");
//                                          temp.put("DATE", "25-03-23 13:08");
//                                    temp.put("SECTION", responseObj.getString("SECTION_NAME"));
//                                    temp.put("STATUS", responseObj.getString("COMPLAINT_STATUS"));
//                                    temp.put("DATE", responseObj.getString("TRANSACTION_DATE"));
                                          list.add(temp);
                                      }
                                  }
                                  dismissDialog();
                                  adapter.notifyDataSetChanged();
                              }else{
                                  dismissDialog();
                                  lview.setVisibility(View.GONE);
                                  text_nodata.setVisibility(View.VISIBLE);
                                  text_nodata.setText(resposneObj.getString("message"));
                              }
                          }else{
                              dismissDialog();
                          }
                      }else {
                          dismissDialog();
                          lview.setVisibility(View.GONE);
                          text_nodata.setVisibility(View.VISIBLE);
                          text_nodata.setText(resposneObj.getString("message"));
                      }

                } catch (JSONException e) {
                    dismissDialog();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Throwable error, String content) {
                Utility.showLog("error", error.toString());
                dismissDialog();
                Utility.showCustomOKOnlyDialog(DTRTrackingActivity.this, error.getMessage());
            }
        });
    }

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent event) {
//        if (event.getAction() == MotionEvent.ACTION_DOWN) {
//            View v = getCurrentFocus();
//            if ( v instanceof EditText) {
//                Rect outRect = new Rect();
//                v.getGlobalVisibleRect(outRect);
//                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
//                    v.clearFocus();
//                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
//                }
//            }
//        }
//        return super.dispatchTouchEvent( event );
//    }

}
