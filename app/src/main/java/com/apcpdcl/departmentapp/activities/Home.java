package com.apcpdcl.departmentapp.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.apcpdcl.departmentapp.R;
import com.apcpdcl.departmentapp.services.NetworkChangeReceiver;
import com.apcpdcl.departmentapp.services.NetworkReceiver;
import com.apcpdcl.departmentapp.shared.AppPrefs;
import com.apcpdcl.departmentapp.utils.Constants;
import com.apcpdcl.departmentapp.utils.Utility;
import com.apcpdcl.departmentapp.utils.Utils;

import org.json.JSONObject;

/**
 * Created by Administrator on 11/8/2017.
 */

public class Home extends AppCompatActivity {

    String sec_code, userName, designation, userId;
    public static String lmcode;
    NetworkReceiver objNetworkReceiver = new NetworkReceiver();
    Button operatingBtn, downloadBtn, reportsBtn,smart_mtrs;
    ProgressDialog pDialog;
    LinearLayout name_Layout, version_Layout;
    RelativeLayout designation_layout;
    TextView apknametxt, versionTxt;
    private String status = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        pDialog = new ProgressDialog(Home.this);
        designation_layout = (RelativeLayout) findViewById(R.id.designation_layout);


        reportsBtn = (Button) findViewById(R.id.reports);
        reportsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent in = new Intent(getApplicationContext(), DailyReports.class);
                    startActivity(in);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        downloadBtn = (Button) findViewById(R.id.download);
        downloadBtn.setOnClickListener(      new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDownloadOptionsDialog();

            }
        });

        operatingBtn = (Button) findViewById(R.id.operate);
        operatingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showServiceTypeDialog();
            }
        });
        smart_mtrs = (Button) findViewById(R.id.smart_mtrs);
        smart_mtrs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getApplicationContext(), Operating.class);
                in.putExtra(Constants.SERVICE_TYPE, "Smart Meters");
                startActivity(in);
            }
        });
        name_Layout = (LinearLayout) findViewById(R.id.nameLayout);
        version_Layout = (LinearLayout) findViewById(R.id.versionLayout);
        apknametxt = (TextView) findViewById(R.id.apknametxt);
        versionTxt = (TextView) findViewById(R.id.apkVersiontxt);
    }


    public boolean IsNullOrBlank(String Input) {
        return Input == null || Input.trim().equals("") || Input.trim().length() == 0;
    }


    @Override
    public void onResume() {
        super.onResume();
//        SharedPreferences lprefs = getSharedPreferences("loginPrefs", 0);
//        sec_code = lprefs.getString("Section_Code", "");
//        userName = lprefs.getString("UserName", "");
//        lmcode = lprefs.getString("UserName", "");
//        userId = lprefs.getString("UserName", "");
//        designation = lprefs.getString("DESIG", "");
        userName = AppPrefs.getInstance(getApplicationContext()).getString("USERNAME", "");
        sec_code = AppPrefs.getInstance(getApplicationContext()).getString("SECTIONCODE", "");
        designation = AppPrefs.getInstance(getApplicationContext()).getString("DESIG", "");
        lmcode = AppPrefs.getInstance(getApplicationContext()).getString("USERID", "");
        userId = AppPrefs.getInstance(getApplicationContext()).getString("USERID", "");
        if (IsNullOrBlank(designation)) {
            Intent i = new Intent(Home.this, MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            finish();
        }
        SharedPreferences prefs = getSharedPreferences("operatingPrefs", 0);
        String strActivity = prefs.getString("StrActivity", "");

        if (strActivity.equals("OperatingDetails")) {
            IntentFilter intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
            this.registerReceiver(new NetworkChangeReceiver(), intentFilter);
        }
    }

    private void showDownloadOptionsDialog() {
        if (!Constants.isDialogOpen) {
            Constants.isDialogOpen = true;
            final Dialog dialog_confirm = new Dialog(Home.this);
            dialog_confirm.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog_confirm.setContentView(R.layout.dialog_service_type);
            WindowManager.LayoutParams lp1 = new WindowManager.LayoutParams();
            Window window1 = dialog_confirm.getWindow();
            if (window1 != null)
                lp1.copyFrom(window1.getAttributes());
            lp1.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp1.height = WindowManager.LayoutParams.WRAP_CONTENT;
            assert window1 != null;
            window1.setAttributes(lp1);
            TextView tv_heading = (TextView) dialog_confirm.findViewById(R.id.tv_heading);
            tv_heading.setText("Download Options");
            Button btn_ok = (Button) dialog_confirm.findViewById(R.id.btn_ok);
            final RadioGroup rg_status = (RadioGroup) dialog_confirm.findViewById(R.id.rg_status);
            RadioButton rb_slab = (RadioButton) dialog_confirm.findViewById(R.id.rb_slab);
            RadioButton rb_ns = (RadioButton) dialog_confirm.findViewById(R.id.rb_ns);
            rb_slab.setText("All");
            rb_ns.setText("Without Govt Services");
            rg_status.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    if (checkedId == R.id.rb_slab) {
                        status = "All";
                    } else {
                        status = "WO";
                    }
                    // status = (String) ((RadioButton) findViewById(checkedId)).getText();
                    Utility.showLog("status", status);
                }

            });
            btn_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (rg_status.getCheckedRadioButtonId() == -1) {
                        Utility.showToastMessage(Home.this, "Please Select Download Option.");
                    } else {
                        if (objNetworkReceiver.hasInternetConnection(Home.this)) {
                            JSONObject requestObj = new JSONObject();
                            try {
                                requestObj.put("LM_CODE", userId);//userId
                                if (status.equalsIgnoreCase("All")) {

                                    requestObj.put("CRITERIA","ALL");
                                    new Utils().addRecordstoDB(Home.this, requestObj);
                                }else {
                                    requestObj.put("CRITERIA","NGSCST");
                                    new Utils().addRecordstoDB(Home.this, requestObj);
                                }
                                dialog_confirm.dismiss();
                            }catch (Exception e){
                                e.printStackTrace();
                            }

                        } else {
                            Toast.makeText(getApplicationContext(), "Please check your Internet connection", Toast.LENGTH_LONG).show();
                        }
                    }

                }
            });
            dialog_confirm.show();
            dialog_confirm.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    Constants.isDialogOpen = false;
                }
            });
        }
    }

    private void showServiceTypeDialog() {
        if (!Constants.isDialogOpen) {
            Constants.isDialogOpen = true;
            final Dialog dialog_confirm = new Dialog(Home.this);
            dialog_confirm.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog_confirm.setContentView(R.layout.dialog_service_type);
            WindowManager.LayoutParams lp1 = new WindowManager.LayoutParams();
            Window window1 = dialog_confirm.getWindow();
            if (window1 != null)
                lp1.copyFrom(window1.getAttributes());
            lp1.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp1.height = WindowManager.LayoutParams.WRAP_CONTENT;
            assert window1 != null;
            window1.setAttributes(lp1);
            Button btn_ok = (Button) dialog_confirm.findViewById(R.id.btn_ok);
            RadioButton fisheries = (RadioButton) dialog_confirm.findViewById(R.id.rb_fisheries);
            fisheries.setVisibility(View.VISIBLE);
            RadioButton rb_agl = (RadioButton) dialog_confirm.findViewById(R.id.rb_agl);
            rb_agl.setVisibility(View.VISIBLE);
            final RadioGroup rg_status = (RadioGroup) dialog_confirm.findViewById(R.id.rg_status);
            rg_status.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    if (checkedId == R.id.rb_slab) {
                        status = "SL";
                    }else if (checkedId == R.id.rb_agl) {
                        status = "AG";
                    }else if (checkedId == R.id.rb_fisheries) {
                        status = "FS";
                    } else {
                        status = "NS";
                    }
                    // status = (String) ((RadioButton) findViewById(checkedId)).getText();
                    Utility.showLog("status", status);
                }

            });
            btn_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (rg_status.getCheckedRadioButtonId() == -1) {
                        Utility.showToastMessage(Home.this, "Please Select Service Type.");
                    } else {
                        try {
                            Intent in = new Intent(getApplicationContext(), Operating.class);
                            in.putExtra(Constants.SERVICE_TYPE, status);
                            startActivity(in);
                            dialog_confirm.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                }
            });
            dialog_confirm.show();
            dialog_confirm.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    Constants.isDialogOpen = false;
                }
            });
        }
    }

}
