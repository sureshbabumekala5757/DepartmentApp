package com.apcpdcl.departmentapp.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;


import com.apcpdcl.departmentapp.R;
import com.apcpdcl.departmentapp.adapters.ReportListAdapter;
import com.apcpdcl.departmentapp.models.ReportsList;
import com.apcpdcl.departmentapp.services.NetworkChangeReceiver;
import com.apcpdcl.departmentapp.sqlite.ReportsDataBaseHandler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;

/**
 * Created by Admin on 19-02-2018.
 */

public class DailyReports extends Activity {
    ReportsDataBaseHandler rdb;
    ReportListAdapter reportListAdapter;
    private ArrayList<ReportsList> reportsList = new ArrayList<>();
    ListView reportsListView;
    ProgressDialog progressDialog;
    TextView norecordstxt;
    public static String strDate, date_string, month_string, year_string,strActivity;
    TableLayout tableHeadLayout;
    RelativeLayout reportslist_layout;
    ImageView home_imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reportlist_layout);

        rdb = new ReportsDataBaseHandler(DailyReports.this);
        reportsListView = (ListView) findViewById(R.id.reports_listview);
        tableHeadLayout = (TableLayout) findViewById(R.id.tableHeadLayout);
        reportslist_layout = (RelativeLayout) findViewById(R.id.reportslist_layout);
        norecordstxt = (TextView) findViewById(R.id.norecordstxt);
        home_imageView = (ImageView) findViewById(R.id.home);

    }

    @Override
    public void onResume() {
        super.onResume();

        home_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(DailyReports.this,Home.class);
                i.setFlags(i.FLAG_ACTIVITY_NEW_TASK | i.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);

            }
        });
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        strDate = df.format(c);

        StringTokenizer tokens = new StringTokenizer(strDate, "-");
        date_string = tokens.nextToken();
        month_string = tokens.nextToken();
        year_string = tokens.nextToken();

        try {
            reportsList.clear();
            new MyReportsAsyncTasks().execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        SharedPreferences prefs = getSharedPreferences("operatingPrefs", 0);
        strActivity = prefs.getString("StrActivity", "");

        if (strActivity.equals("OperatingDetails")) {
            IntentFilter intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
            this.registerReceiver(new NetworkChangeReceiver(), intentFilter);
        }
    }

    public class MyReportsAsyncTasks extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(DailyReports.this);
            progressDialog.setMessage("Please Wait Fetching is going on.....");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                rdb.getDeleteCount();
                reportsList.addAll(rdb.getAllreportslists());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            if (reportsList.size() > 0) {
                norecordstxt.setVisibility(View.GONE);
                reportListAdapter = new ReportListAdapter(DailyReports.this, reportsList);
                reportsListView.setAdapter(reportListAdapter);
                progressDialog.dismiss();
            } else {
                progressDialog.dismiss();
                tableHeadLayout.setVisibility(View.GONE);
                norecordstxt.setVisibility(View.VISIBLE);
            }

        }
    }
}
