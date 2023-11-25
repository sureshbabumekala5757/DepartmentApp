package com.apcpdcl.departmentapp.activities;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.Toast;

import com.apcpdcl.departmentapp.R;
import com.apcpdcl.departmentapp.adapters.FeederSaidiSaifiRecyclerviewAdapter;
import com.apcpdcl.departmentapp.models.FeederSaidiSaifi;
import com.apcpdcl.departmentapp.services.NetworkReceiver;
import com.apcpdcl.departmentapp.utils.Utils;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FeederSaidiSaifiActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.F_feedertypespinner)
    Spinner F_feedertypespinner;

    @BindView(R.id.F_et_fromdate)
    EditText F_et_fromdate;

    @BindView(R.id.F_et_todate)
    EditText F_et_todate;

    @BindView(R.id.F_from_calendar_btn)
    Button F_from_calendar_btn;

    @BindView(R.id.F_to_calendar_btn)
    Button F_to_calendar_btn;

    @BindView(R.id.btn_feeder_submit)
    Button btn_feeder_submit;

    @BindView(R.id.F_recyclerview)
    RecyclerView F_recyclerview;

    @BindView(R.id.tableHeadLayout)
    TableLayout tableHeadLayout;

    String str_f_feederType, str_FromDate, str_ToDate,str_f_FeederType,str_secCode;
    RecyclerView.LayoutManager recylerViewLayoutManager;
    FeederSaidiSaifiRecyclerviewAdapter f_saidisaifi_Adapter;
    ArrayList<FeederSaidiSaifi> feederArrayList = new ArrayList<FeederSaidiSaifi>();
    NetworkReceiver objNetworkReceiver = new NetworkReceiver();
    ProgressDialog pDialog;
    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feeder_saidi_saifi);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        ButterKnife.bind(this);


        pDialog = new ProgressDialog(this);
        btn_feeder_submit.setOnClickListener(this);
        F_from_calendar_btn.setOnClickListener(this);
        F_to_calendar_btn.setOnClickListener(this);

        SharedPreferences prefs = getSharedPreferences("loginPrefs", 0);
        str_secCode = prefs.getString("Section_Code", "");

        List<String> F_feederTypelist = new ArrayList<String>();
        F_feederTypelist.add("Select");
        F_feederTypelist.add("ALL");
        F_feederTypelist.add("ALL(Excluding LIS,AGL)");
        F_feederTypelist.add("INDL");
        F_feederTypelist.add("APIIC");

        ArrayAdapter<String> F_feederTypeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, F_feederTypelist);
        F_feederTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        F_feedertypespinner.setAdapter(F_feederTypeAdapter);

        F_feedertypespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View arg1, int position, long arg3) {
                str_f_feederType = parent.getItemAtPosition(position).toString();

                if (str_f_feederType.equalsIgnoreCase("Select")) {
                    str_f_FeederType = "";
                } else if (str_f_feederType.equalsIgnoreCase("ALL")) {
                    str_f_FeederType = "0";
                } else if (str_f_feederType.equalsIgnoreCase("ALL(Excluding LIS,AGL)")) {
                    str_f_FeederType = "1";
                } else if (str_f_feederType.equalsIgnoreCase("INDL")) {
                    str_f_FeederType = "2";
                } else if (str_f_feederType.equalsIgnoreCase("APIIC")) {
                    str_f_FeederType = "3";
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        recylerViewLayoutManager = new LinearLayoutManager(FeederSaidiSaifiActivity.this);
        F_recyclerview.setLayoutManager(recylerViewLayoutManager);

        f_saidisaifi_Adapter = new FeederSaidiSaifiRecyclerviewAdapter(FeederSaidiSaifiActivity.this, feederArrayList);
        F_recyclerview.setAdapter(f_saidisaifi_Adapter);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_feeder_submit:
                feederArrayList.clear();
                str_FromDate = F_et_fromdate.getText().toString();
                str_ToDate = F_et_todate.getText().toString();

                if (str_f_feederType.equals("Select")) {
                    Toast.makeText(getApplicationContext(), "Please select FeederType", Toast.LENGTH_LONG).show();
                } else if (Utils.IsNullOrBlank(str_FromDate)) {
                    Toast.makeText(getApplicationContext(), "Please select From Date", Toast.LENGTH_LONG).show();
                } else if (Utils.IsNullOrBlank(str_ToDate)) {
                    Toast.makeText(getApplicationContext(), "Please select To Date", Toast.LENGTH_LONG).show();
                } else {
                    if (objNetworkReceiver.hasInternetConnection(getApplicationContext())) {
                        JSONObject jInnerObject = new JSONObject();
                        try {
                            jInnerObject.put("sectionId", str_secCode);
                            jInnerObject.put("fromDate", str_FromDate);
                            jInnerObject.put("toDate", str_ToDate);
                            jInnerObject.put("feederType", str_f_FeederType);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        try {
                            StringEntity entity = new StringEntity(jInnerObject.toString());
                            AsyncHttpClient client = new AsyncHttpClient();
                            client.post(getApplicationContext(), "http://59.145.127.110:8088/mobileapp_oms/rest/mobileomsservices/saidisaififeeder", entity, "application/json", new AsyncHttpResponseHandler() {
                                @Override
                                public void onSuccess(String response) {
                                    try {
                                        JSONObject obj = new JSONObject(response);
                                        String status = obj.getString("STATUS");
                                        switch (status) {
                                            case "TRUE":
                                                if (pDialog != null && pDialog.isShowing()) {
                                                    pDialog.dismiss();
                                                }
                                                JSONArray jsonArray = (JSONArray) obj.get("RESPONSE");
                                                for (int i = 0; i < jsonArray.length(); i++) {
                                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                                    FeederSaidiSaifi objectFeederSaidi = new Gson().fromJson(jsonObject.toString(), FeederSaidiSaifi.class);
                                                    feederArrayList.add(objectFeederSaidi);
                                                }
                                                if(feederArrayList.size()>0) {
                                                    tableHeadLayout.setVisibility(View.VISIBLE);
                                                    F_recyclerview.setVisibility(View.VISIBLE);

                                                    recylerViewLayoutManager = new LinearLayoutManager(FeederSaidiSaifiActivity.this);
                                                    F_recyclerview.setLayoutManager(recylerViewLayoutManager);

                                                    f_saidisaifi_Adapter = new FeederSaidiSaifiRecyclerviewAdapter(FeederSaidiSaifiActivity.this, feederArrayList);
                                                    F_recyclerview.setAdapter(f_saidisaifi_Adapter);
                                                }
                                                break;
                                            case "ERROR":
                                                if (pDialog != null && pDialog.isShowing()) {
                                                    pDialog.dismiss();
                                                }

                                                Toast.makeText(getApplicationContext(), "Please Try Again", Toast.LENGTH_LONG).show();
                                                break;
                                            case "FALSE":
                                                if (pDialog != null && pDialog.isShowing()) {
                                                    pDialog.dismiss();
                                                }
                                                Toast.makeText(getApplicationContext(), "Please Try Again", Toast.LENGTH_LONG).show();
                                                break;
                                            default:
                                                if (pDialog != null && pDialog.isShowing()) {
                                                    pDialog.dismiss();
                                                }
                                                Toast.makeText(getApplicationContext(), "Please check your Details", Toast.LENGTH_LONG).show();
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
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }


                    } else {
                        Toast.makeText(getApplicationContext(), "Please check your Internet connection", Toast.LENGTH_LONG).show();
                    }
                }
                break;

            case R.id.F_from_calendar_btn:
                myCalendar = Calendar.getInstance();
                date = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // TODO Auto-generated method stub
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, monthOfYear);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        String myFormat = "dd-MMM-yyyy"; //In which you need put here
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                        F_et_fromdate.setText(sdf.format(myCalendar.getTime()));
                    }

                };
                setMaxTime();
                break;

            case R.id.F_to_calendar_btn:
                myCalendar = Calendar.getInstance();
                date = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // TODO Auto-generated method stub
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, monthOfYear);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        String myFormat = "dd-MMM-yyyy"; //In which you need put here
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                        F_et_todate.setText(sdf.format(myCalendar.getTime()));
                    }

                };
                setMaxTime();
                break;


            default:
                break;
        }
    }
    private void setMaxTime() {
        DatePickerDialog dpr = new DatePickerDialog(this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -1);
        dpr.getDatePicker().setMaxDate(c.getTimeInMillis());
        dpr.show();
    }
}
