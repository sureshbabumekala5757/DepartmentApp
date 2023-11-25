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
import com.apcpdcl.departmentapp.adapters.SaidiSaifiRecyclerviewAdapter;
import com.apcpdcl.departmentapp.models.SecSaidiSaifi;
import com.apcpdcl.departmentapp.services.NetworkReceiver;
import com.apcpdcl.departmentapp.utils.Constants;
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

public class SaidiSaifiActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.feedertypespinner)
    Spinner feedertypespinner;

    @BindView(R.id.et_fromdate)
    EditText et_fromdate;

    @BindView(R.id.et_todate)
    EditText et_todate;

    @BindView(R.id.from_calendar_btn)
    Button from_calendar_btn;

    @BindView(R.id.to_calendar_btn)
    Button to_calendar_btn;

    @BindView(R.id.btn_sec_submit)
    Button btn_sec_submit;

    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;

    @BindView(R.id.tableHeadLayout)
    TableLayout tableHeadLayout;

    String str_feederType, str_FromDate, str_ToDate, strFeederType, str_secCode;
    RecyclerView.LayoutManager recylerViewLayoutManager;
    SaidiSaifiRecyclerviewAdapter saidisaifi_Adapter;
    ArrayList<SecSaidiSaifi> secArrayList = new ArrayList<SecSaidiSaifi>();
    NetworkReceiver objNetworkReceiver = new NetworkReceiver();
    ProgressDialog pDialog;
    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.saidi_saifi_activity);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        ButterKnife.bind(this);

        pDialog = new ProgressDialog(this);
        btn_sec_submit.setOnClickListener(this);
        from_calendar_btn.setOnClickListener(this);
        to_calendar_btn.setOnClickListener(this);

        SharedPreferences prefs = getSharedPreferences("loginPrefs", 0);
        str_secCode = prefs.getString("Section_Code", "");

        List<String> feederTypelist = new ArrayList<String>();
        feederTypelist.add("Select");
        feederTypelist.add("ALL");
        feederTypelist.add("ALL(Excluding LIS,AGL)");
        feederTypelist.add("INDL");
        feederTypelist.add("APIIC");

        ArrayAdapter<String> feederTypeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, feederTypelist);
        feederTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        feedertypespinner.setAdapter(feederTypeAdapter);

        feedertypespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View arg1, int position, long arg3) {
                str_feederType = parent.getItemAtPosition(position).toString();

                if (str_feederType.equalsIgnoreCase("Select")) {
                    strFeederType = "";
                } else if (str_feederType.equalsIgnoreCase("ALL")) {
                    strFeederType = "0";
                } else if (str_feederType.equalsIgnoreCase("ALL(Excluding LIS,AGL)")) {
                    strFeederType = "1";
                } else if (str_feederType.equalsIgnoreCase("INDL")) {
                    strFeederType = "2";
                } else if (str_feederType.equalsIgnoreCase("APIIC")) {
                    strFeederType = "3";
                }

            }

            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sec_submit:
                secArrayList.clear();
                str_FromDate = et_fromdate.getText().toString();
                str_ToDate = et_todate.getText().toString();

                if (str_feederType.equals("Select")) {
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
                            jInnerObject.put("feederType", strFeederType);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        try {
                            StringEntity entity = new StringEntity(jInnerObject.toString());
                            AsyncHttpClient client = new AsyncHttpClient();
                            client.post(getApplicationContext(), Constants.OMS_URL+"saidisaifisection", entity, "application/json", new AsyncHttpResponseHandler() {
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
                                                    SecSaidiSaifi objectSecSaidi = new Gson().fromJson(jsonObject.toString(), SecSaidiSaifi.class);
                                                    secArrayList.add(objectSecSaidi);
                                                }
                                                if(secArrayList.size()>0) {
                                                    tableHeadLayout.setVisibility(View.VISIBLE);
                                                    recyclerview.setVisibility(View.VISIBLE);

                                                    recylerViewLayoutManager = new LinearLayoutManager(SaidiSaifiActivity.this);
                                                    recyclerview.setLayoutManager(recylerViewLayoutManager);

                                                    saidisaifi_Adapter = new SaidiSaifiRecyclerviewAdapter(SaidiSaifiActivity.this, secArrayList);
                                                    recyclerview.setAdapter(saidisaifi_Adapter);
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

            case R.id.from_calendar_btn:
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
                        et_fromdate.setText(sdf.format(myCalendar.getTime()));
                    }

                };
                setMaxTime();
                break;

            case R.id.to_calendar_btn:
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
                        et_todate.setText(sdf.format(myCalendar.getTime()));
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
