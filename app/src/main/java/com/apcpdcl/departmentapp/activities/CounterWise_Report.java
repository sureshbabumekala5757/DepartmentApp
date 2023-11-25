package com.apcpdcl.departmentapp.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.apcpdcl.departmentapp.R;
import com.apcpdcl.departmentapp.adapters.CounterSubDivListAdapter;
import com.apcpdcl.departmentapp.models.CounterModel;
import com.apcpdcl.departmentapp.services.NetworkReceiver;
import com.apcpdcl.departmentapp.utils.Constants;
import com.apcpdcl.departmentapp.utils.Utils;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CounterWise_Report extends AppCompatActivity implements CounterSubDivListAdapter.ItemClickListener {

    ProgressDialog pDialog;
    Spinner month_Spinner, year_Spinner,counter_Spinner;
    String str_month, str_monthnum, str_year,str_counter,Counter,str_Status,str_Response,mUserName;
    Button submit_button;
    TableLayout tableHeadLayout, subdiv_tableHeadLayout;
    RecyclerView subdiv_recyclerview, recyclerview;
    RecyclerView.LayoutManager counterRecylerViewLayoutManager;
    TextView ero,subdivision;
    private ArrayList<CounterModel> counter_EroModels, counter_subDivModels;
    NetworkReceiver objNetworkReceiver = new NetworkReceiver();
    JSONObject obj;
    private EroCounterListAdapter eroCounterListAdapter;
    private CounterSubDivListAdapter counterSubListAdapter;
    CounterModel counterModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.counterwise_report);
        init();
    }

    private void init() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        android.content.SharedPreferences prefs = getSharedPreferences("loginPrefs", 0);
        mUserName = prefs.getString("UserName", "");

        pDialog = new ProgressDialog(this);
        month_Spinner = (Spinner) findViewById(R.id.counter_monthspinner);
        year_Spinner = (Spinner) findViewById(R.id.counter_yearspinner);
        counter_Spinner= (Spinner) findViewById(R.id.counterSpinner);

        ero = (TextView) findViewById(R.id.counter_ero);
        subdivision = (TextView) findViewById(R.id.counter_subDiv);
        submit_button = (Button) findViewById(R.id.counter_btn_submit);

        tableHeadLayout = (TableLayout) findViewById(R.id.counter_tableHeadLayout);
        recyclerview = (RecyclerView) findViewById(R.id.counter_recyclerview);
        subdiv_tableHeadLayout = (TableLayout) findViewById(R.id.counter_subdiv_tableHeadLayout);
        subdiv_recyclerview = (RecyclerView) findViewById(R.id.counter_subdiv_recyclerview);

        counterRecylerViewLayoutManager = new LinearLayoutManager(CounterWise_Report.this);
        recyclerview.setLayoutManager(counterRecylerViewLayoutManager);

        counterRecylerViewLayoutManager = new LinearLayoutManager(CounterWise_Report.this);
        subdiv_recyclerview.setLayoutManager(counterRecylerViewLayoutManager);


        counter_EroModels = new ArrayList<>();
        counter_subDivModels = new ArrayList<>();


        List<String> list = new ArrayList<String>();
        list.add("Select");
        list.add("JAN");
        list.add("FEB");
        list.add("MAR");
        list.add("APR");
        list.add("MAY");
        list.add("JUN");
        list.add("JUL");
        list.add("AUG");
        list.add("SEP");
        list.add("OCT");
        list.add("NOV");
        list.add("DEC");

        ArrayAdapter<String> monthAdapter = new ArrayAdapter<String>(CounterWise_Report.this, android.R.layout.simple_spinner_item, list);
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        month_Spinner.setAdapter(monthAdapter);

        month_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View arg1, int position, long arg3) {
                str_month = parent.getItemAtPosition(position).toString();
                Date date = null;
                try {
                    date = new SimpleDateFormat("MMM").parse(str_month);
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(date);
                    str_monthnum = String.valueOf(cal.get(Calendar.MONTH) + 1);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        List<String> list_years = new ArrayList<String>();
        list_years.add("Select");
        list_years.add(String.valueOf(Calendar.getInstance().get(Calendar.YEAR) - 1));
        list_years.add(String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));


        ArrayAdapter<String> yearAdapter = new ArrayAdapter<String>(CounterWise_Report.this, android.R.layout.simple_spinner_item, list_years);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        year_Spinner.setAdapter(yearAdapter);

        year_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View arg1, int position, long arg3) {
                str_year = parent.getItemAtPosition(position).toString();

            }

            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        List<String> list_counter = new ArrayList<String>();
        list_counter.add("Select");
        list_counter.add("ALL");
        list_counter.add("ATP-PHOENIX");
        list_counter.add("BILLDESK");
        list_counter.add("ERO");
        list_counter.add("ESEVA");
        list_counter.add("SCM");
        list_counter.add("RSDP");
        list_counter.add("ICICI");
        list_counter.add("PAYTM");
        list_counter.add("TAWALLET");
        list_counter.add("ECSC");


        ArrayAdapter<String> counterAdapter = new ArrayAdapter<String>(CounterWise_Report.this, android.R.layout.simple_spinner_item, list_counter);
        counterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        counter_Spinner.setAdapter(counterAdapter);

        counter_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View arg1, int position, long arg3) {
                str_counter = parent.getItemAtPosition(position).toString();

            }

            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        submit_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (Utils.IsNullOrBlank(str_month)) {
                    Toast.makeText(getApplicationContext(), "Please select Month", Toast.LENGTH_LONG).show();
                } else if (str_month.equals("Select")) {
                    Toast.makeText(getApplicationContext(), "Please select Month", Toast.LENGTH_LONG).show();
                } else if (Utils.IsNullOrBlank(str_year)) {
                    Toast.makeText(getApplicationContext(), "Please select Year", Toast.LENGTH_LONG).show();
                } else if (str_year.equals("Select")) {
                    Toast.makeText(getApplicationContext(), "Please select Year", Toast.LENGTH_LONG).show();
                }else if (Utils.IsNullOrBlank(str_counter)) {
                    Toast.makeText(getApplicationContext(), "Please select Counter", Toast.LENGTH_LONG).show();
                } else if (str_counter.equals("Select")) {
                    Toast.makeText(getApplicationContext(), "Please select Counter", Toast.LENGTH_LONG).show();
                } else {
                    if (objNetworkReceiver.hasInternetConnection(getApplicationContext())) {
                        counter_EroModels.clear();
                        counter_subDivModels.clear();

                        ero.setVisibility(View.GONE);
                        subdivision.setVisibility(View.GONE);

                        recyclerview.setVisibility(View.GONE);
                        tableHeadLayout.setVisibility(View.GONE);

                        subdiv_recyclerview.setVisibility(View.GONE);
                        subdiv_tableHeadLayout.setVisibility(View.GONE);

                        RequestParams params = new RequestParams();
                        params.put("Month", str_monthnum);
                        params.put("Year", str_year);
                        params.put("Counter", str_counter);
                        params.put("EROCD", mUserName);
                        invokeCounterWS(params);

                    } else {
                        Toast.makeText(getApplicationContext(), "Please check your Internet connection", Toast.LENGTH_LONG).show();
                    }

                }
            }
        });
    }
    private void invokeCounterWS(RequestParams params) {
        pDialog.show();
        pDialog.setMessage("Please wait...");

        AsyncHttpClient client = new AsyncHttpClient();

        client.post(Constants.URL_ERO+"Collections_CounterWise_Report", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                try {
                    obj = new JSONObject(response);
                    if (pDialog != null && pDialog.isShowing()) {
                        pDialog.dismiss();
                    }
                    str_Status=obj.getString("STATUS");
                    if(str_Status.equalsIgnoreCase("TRUE")){
                        Counter=obj.getString("Counter");
                        JSONArray eroJsonArray = (JSONArray) obj.get("ERO");

                        for (int i = 0; i < eroJsonArray.length(); i++) {
                            JSONObject json = eroJsonArray.getJSONObject(i);
                            counterModel = new Gson().fromJson(json.toString(), CounterModel.class);
                            counter_EroModels.add(counterModel);
                        }

                        if (counter_EroModels.size() > 0) {
                            ero.setVisibility(View.VISIBLE);
                            recyclerview.setVisibility(View.VISIBLE);
                            tableHeadLayout.setVisibility(View.VISIBLE);
                            eroCounterListAdapter = new EroCounterListAdapter(getApplicationContext(), counter_EroModels);
                            recyclerview.setAdapter(eroCounterListAdapter);
                        } else {

                        }


                        JSONArray subdivJsonArray = (JSONArray) obj.get("DIVISION");

                        for (int i = 0; i < subdivJsonArray.length(); i++) {
                            JSONObject json = subdivJsonArray.getJSONObject(i);
                            counterModel = new Gson().fromJson(json.toString(), CounterModel.class);
                            counter_subDivModels.add(counterModel);
                        }

                        if (counter_subDivModels.size() > 0) {
                            counterSubListAdapter = new CounterSubDivListAdapter(getApplicationContext(), counter_subDivModels);
                            subdiv_recyclerview.setAdapter(counterSubListAdapter);
                            counterSubListAdapter.setClickListener(CounterWise_Report.this);
                        } else {

                        }

                    }else if(str_Status.equalsIgnoreCase("FALSE")){
                        str_Response=obj.getString("RESPONSE");
                        Toast.makeText(getApplicationContext(), str_Response, Toast.LENGTH_LONG).show();
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

    @Override
    public void onClick(View view, int position) {
        Intent i = new Intent(this, SectionCounterwiseActivity.class);
        i.putExtra("Object", obj.toString());
        i.putExtra("Counter_SubDivision", counter_subDivModels.get(position).getDIV_Name());
        startActivity(i);
    }

    public class EroCounterListAdapter extends RecyclerView.Adapter<EroCounterListAdapter.ViewHolder> {

        private Context mContext;
        private LayoutInflater mInflater;
        private ArrayList<CounterModel> eroList;

        // data is passed into the constructor
        public EroCounterListAdapter(Context context, ArrayList<CounterModel> eroList) {
            this.mInflater = LayoutInflater.from(context);
            this.eroList = eroList;
            mContext = context;
        }

        // inflates the row layout from xml when needed
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = mInflater.inflate(R.layout.counter_wise_listitem, parent, false);
            return new ViewHolder(view);
        }

        // binds the data to the TextView in each row
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            try {
                CounterModel counterModel = eroList.get(position);
                holder.name_txt.setText(counterModel.getERO_Name());
                holder.counter_num_of_scs_txt.setText(counterModel.getERO_SCS());
                holder.counter_amount_txt.setText(counterModel.getERO_AMT());

                holder.name_txt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        subdivision.setVisibility(View.VISIBLE);
                        subdiv_recyclerview.setVisibility(View.VISIBLE);
                        subdiv_tableHeadLayout.setVisibility(View.VISIBLE);
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }


        }

        // total number of rows
        @Override
        public int getItemCount() {
            return eroList.size();
        }


        // stores and recycles views as they are scrolled off screen
        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView name_txt,counter_num_of_scs_txt,counter_amount_txt;

            ViewHolder(View itemView) {
                super(itemView);
                name_txt = (TextView) itemView.findViewById(R.id.counter_name_txt);
                counter_num_of_scs_txt = (TextView) itemView.findViewById(R.id.counter_num_of_scs_txt);
                counter_amount_txt = (TextView) itemView.findViewById(R.id.counter_amount_txt);

            }
        }
    }
}
