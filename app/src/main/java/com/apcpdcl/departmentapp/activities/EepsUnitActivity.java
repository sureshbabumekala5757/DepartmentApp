package com.apcpdcl.departmentapp.activities;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TableLayout;
import android.widget.Toast;

import com.apcpdcl.departmentapp.R;
import com.apcpdcl.departmentapp.adapters.EepsAdapter;
import com.apcpdcl.departmentapp.models.Eeps;
import com.apcpdcl.departmentapp.services.NetworkReceiver;
import com.apcpdcl.departmentapp.utils.Constants;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class EepsUnitActivity extends AppCompatActivity {
    TableLayout tableHeadLayout;
    LinearLayoutManager recylerViewLayoutManager;
    private RecyclerView recyclerView;
    View bottomview;
    private EepsAdapter eeps_Adapter;
    List<Eeps> eepsList = new ArrayList<Eeps>();
    NetworkReceiver objNetworkReceiver = new NetworkReceiver();
    String sectionCode, userID;
    ProgressDialog pDialog;
    Eeps eeps;
    private String str_status = "";
    private String from = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eepsunit_layout);
        init();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void init() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        tableHeadLayout = (TableLayout) findViewById(R.id.tableHeadLayout);
        recyclerView = (RecyclerView) findViewById(R.id.eepsunit_recyclerview);
        recylerViewLayoutManager = new LinearLayoutManager(EepsUnitActivity.this);
        recyclerView.setLayoutManager(recylerViewLayoutManager);
        bottomview = findViewById(R.id.bottomview);
        pDialog = new ProgressDialog(this);

        SharedPreferences prefs = getSharedPreferences("loginPrefs", 0);
        sectionCode = prefs.getString("Section_Code", "");
        userID = prefs.getString("UserName", "");
        from = getIntent().getStringExtra(Constants.FROM);

        if (objNetworkReceiver.hasInternetConnection(getApplicationContext())) {
            eepsList.clear();

            RequestParams params = new RequestParams();
            params.put("uscno", sectionCode);
            invokeListWS(params);

        } else {
            Toast.makeText(getApplicationContext(), "Please check your Internet connection", Toast.LENGTH_LONG).show();
        }


    }

    private void invokeListWS(RequestParams params) {
        pDialog.show();
        pDialog.setMessage("Please wait...");

        AsyncHttpClient client = new AsyncHttpClient();

        client.post(Constants.URL+"eesl/fetch", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                try {
                    if (pDialog != null && pDialog.isShowing()) {
                        pDialog.dismiss();
                    }
                    JSONObject obj = new JSONObject(response);

                    str_status = obj.getString("status");
                    if (str_status.equalsIgnoreCase("true")) {
                        JSONArray eepsJsonArray = (JSONArray) obj.get("EeslObjects");

                        for (int i = 0; i < eepsJsonArray.length(); i++) {
                            JSONObject json = eepsJsonArray.getJSONObject(i);
                            eeps = new Gson().fromJson(json.toString(), Eeps.class);
                            eepsList.add(eeps);
                        }

                        if (eepsList.size() > 0) {
                            recyclerView.setVisibility(View.VISIBLE);
                            tableHeadLayout.setVisibility(View.VISIBLE);
                            bottomview.setVisibility(View.VISIBLE);
                            eeps_Adapter = new EepsAdapter(EepsUnitActivity.this, eepsList, from);
                            recyclerView.setAdapter(eeps_Adapter);
                        }
                    } else if (str_status.equalsIgnoreCase("false")) {
                        Toast.makeText(getApplicationContext(), "No data Found", Toast.LENGTH_LONG).show();
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
}
