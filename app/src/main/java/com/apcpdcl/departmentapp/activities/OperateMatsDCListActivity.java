package com.apcpdcl.departmentapp.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.apcpdcl.departmentapp.R;
import com.apcpdcl.departmentapp.adapters.MatsDCListAdapter;
import com.apcpdcl.departmentapp.customviews.MultiSelectSpinner;
import com.apcpdcl.departmentapp.models.MatsConsumerModel;
import com.apcpdcl.departmentapp.services.NetworkChangeReceiver;
import com.apcpdcl.departmentapp.sqlite.MatsDatabaseHandler;
import com.apcpdcl.departmentapp.sqlite.MatsReportsDataBaseHandler;
import com.apcpdcl.departmentapp.utils.Utility;
import com.apcpdcl.departmentapp.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 11/9/2017.
 */

public class OperateMatsDCListActivity extends AppCompatActivity {

    private ListView cat_ListView;
    private RecyclerView recyclerView;
    private MatsDCListAdapter consumer_Adapter;
    RecyclerView.LayoutManager recylerViewLayoutManager;
    private ArrayList<MatsConsumerModel> consumersList = new ArrayList<>();

    private ArrayList<MatsConsumerModel> poleList = new ArrayList<>();
    List<String> poles = new ArrayList<String>();

    MatsDatabaseHandler db;
    MatsReportsDataBaseHandler rdb;
    ProgressDialog progressDialog;
    TableLayout tableHeadLayout;
    View bottomview;
    EditText editsearch, et_amounttxtvalue;
    String sec_code, strActivity;
    Button advancedSearchFilterBtn, dialogsubmitbtn;
    Spinner amountSpinner, sTypeSpinner, discDateSpinner, discdatespinner_to;
    RelativeLayout socialcat_layout;
    private MultiSelectSpinner socialCatSpinner, grpSpinner, pole_spinner, govtCatSpinner, dtrSpinner;
    String sAmount, sCat, sGovt, sAmountValue, sDdateFrom, sDdateTo, sDtr, sPole, sGrp;
    private String totalString = "";
    ImageView home_imageView, dialogclosebtn;
    int textlength;
    Dialog loadingValuesDialog;
    String flag = "N";
    //String advanceFlag = "F";
    private NetworkChangeReceiver networkChangeReceiver;
    boolean isFirst = true;
    private TextView toolbar_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.opearte_mats_dc_list_layout);
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        toolbar_title.setText("Operate MATS DC-List");
        SharedPreferences prefs = getSharedPreferences("loginPrefs", 0);
        sec_code = prefs.getString("Section_Code", "");
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recylerViewLayoutManager = new LinearLayoutManager(OperateMatsDCListActivity.this);
        recyclerView.setLayoutManager(recylerViewLayoutManager);
        bottomview = findViewById(R.id.bottomview);
        tableHeadLayout = (TableLayout) findViewById(R.id.tableHeadLayout);
        db = new MatsDatabaseHandler(this);
        rdb = new MatsReportsDataBaseHandler(this);
        editsearch = (EditText) findViewById(R.id.search);
        advancedSearchFilterBtn = (Button) findViewById(R.id.filterbtn);
        home_imageView = (ImageView) findViewById(R.id.home);
        socialcat_layout = (RelativeLayout) findViewById(R.id.socialcat_layout);
        advancedSearchFilterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    view = OperateMatsDCListActivity.this.getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    editsearch.setText("");
                    isFirst = true;
                    loadingValuesPopup();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        flag = "N";
        editsearch.setText("");

        editsearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
            }

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                if (cs.length() >= 3) {
                    textlength = cs.length();
                    ArrayList<MatsConsumerModel> tempArrayList = new ArrayList<MatsConsumerModel>();
                    try {
                        for (MatsConsumerModel c : consumersList) {
                            if (textlength <= c.getCMUSCNO().length()) {
                                if (c.getCMUSCNO().toLowerCase().contains(cs.toString().toLowerCase())) {
                                    tempArrayList.add(c);
                                }
                            }
                        }
                        consumer_Adapter = new MatsDCListAdapter(OperateMatsDCListActivity.this, tempArrayList);
                        //consumer_Adapter.setClickListener(OperateMatsDCListActivity.this);
                        recyclerView.setAdapter(consumer_Adapter);
//                        consumerAdapter = new ConsumerAdapter(OperateMatsDCListActivity.this, tempArrayList);
//                        listView.setAdapter(consumerAdapter);
                        flag = "Y";
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    if (flag.equals("Y")) {
                        try {
                            /*Here records are loaded fastly. User Doesn't know here records are loading again.
                             and app hangs some time. for that purpose here i am using thread to stop user for 5 secs*/
                            progressDialog = new ProgressDialog(OperateMatsDCListActivity.this);
                            progressDialog.setMessage("Please Wait Fetching is going on.....");
                            progressDialog.setCancelable(false);
                            progressDialog.show();
                            new Handler().postDelayed(new Runnable() {
                                public void run() {
                                    consumersList.clear();
                                    consumersList.addAll(db.getConsumers());
                                    consumer_Adapter = new MatsDCListAdapter(OperateMatsDCListActivity.this, consumersList);
                                    //consumer_Adapter.setClickListener(OperateMatsDCListActivity.this);
                                    recyclerView.setAdapter(consumer_Adapter);
//                                    consumerAdapter = new ConsumerAdapter(OperateMatsDCListActivity.this, consumersList);
//                                    listView.setAdapter(consumerAdapter);
                                    progressDialog.dismiss();
                                }
                            }, 3000);
                            flag = "N";
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

        });

    }


    @Override
    public void onResume() {
        super.onResume();

        try {
            consumersList.clear();
            if (totalString.equalsIgnoreCase("")) {
                new MyAsyncTasks().execute();
            } else {
                new MyFilterAsyncTasks().execute();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        SharedPreferences prefs = getSharedPreferences("operatingPrefs", 0);
        strActivity = prefs.getString("StrActivity", "");

        if (strActivity.equals("OperatingDetails")) {
            IntentFilter intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
            networkChangeReceiver = new NetworkChangeReceiver();
            this.registerReceiver(networkChangeReceiver, intentFilter);
        }


    }

    public class MyAsyncTasks extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(OperateMatsDCListActivity.this);
            progressDialog.setMessage("Please Wait Fetching is going on.....");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                consumersList.addAll(db.getConsumers());
                /* Needed Code
                Filter downloaded list using SC_NO */

//                for (int j = consumersList.size() - 1; j >= 0; j--) {
//                    String s = consumersList.get(j).getCmuscno();
//                    String upToNCharacters = s.substring(0, Math.min(s.length(), 5));
//                    if (!(upToNCharacters.equals(sec_code))) {
//                        consumersList.remove(j);
//                    }
//                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return "Successful";
        }

        @Override
        protected void onPostExecute(String s) {
            if (s.equalsIgnoreCase("Successful")) {
                if (consumersList.size() > 0) {
                    consumer_Adapter = new MatsDCListAdapter(OperateMatsDCListActivity.this, consumersList);
                    //consumer_Adapter.setClickListener(OperateMatsDCListActivity.this);
                    recyclerView.setAdapter(consumer_Adapter);
//                consumerAdapter = new ConsumerAdapter(OperateMatsDCListActivity.this, consumersList);
//                listView.setAdapter(consumerAdapter);
                    Utils.filterReportsList.clear();
                    Utils.filterReportsList.addAll(rdb.getAllServiceNum());
                    progressDialog.dismiss();
                } else {
                    progressDialog.dismiss();
                    tableHeadLayout.setVisibility(View.GONE);
                    bottomview.setVisibility(View.GONE);
                    editsearch.setVisibility(View.GONE);
                    advancedSearchFilterBtn.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "There are no records", Toast.LENGTH_LONG).show();
                }

            }
        }

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (networkChangeReceiver != null) {
            this.unregisterReceiver(networkChangeReceiver);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public class MyFilterAsyncTasks extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(OperateMatsDCListActivity.this);
            progressDialog.setMessage("Please Wait Fetching is going on.....");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                // TODO: 25-09-2019
                // consumersList.addAll(db.getAllFilterConsumers(totalString));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            if (consumersList.size() > 0) {
                consumer_Adapter = new MatsDCListAdapter(OperateMatsDCListActivity.this, consumersList);
                //consumer_Adapter.setClickListener(OperateMatsDCListActivity.this);
                recyclerView.setAdapter(consumer_Adapter);
                progressDialog.dismiss();

            } else {
                progressDialog.dismiss();
                tableHeadLayout.setVisibility(View.GONE);
                bottomview.setVisibility(View.GONE);
                editsearch.setVisibility(View.GONE);
                advancedSearchFilterBtn.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "There are no records", Toast.LENGTH_LONG).show();
            }

        }
    }

    private void loadingValuesPopup() {

        loadingValuesDialog = new Dialog(OperateMatsDCListActivity.this);
        loadingValuesDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loadingValuesDialog.setContentView(R.layout.advancedfilter);
        Window window = loadingValuesDialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        dialogsubmitbtn = (Button) loadingValuesDialog.findViewById(R.id.dialogsubmitbtn);
        dialogclosebtn = (ImageView) loadingValuesDialog.findViewById(R.id.dialogclosebtn);


        amountSpinner = (Spinner) loadingValuesDialog.findViewById(R.id.amounttxtspin);
        et_amounttxtvalue = (EditText) loadingValuesDialog.findViewById(R.id.amounttxtvalue);
        socialCatSpinner = (MultiSelectSpinner) loadingValuesDialog.findViewById(R.id.socialcattxtspinner);
        socialCatSpinner.setAllText("Select");
        socialCatSpinner.setDefaultText("Select");
        govtCatSpinner = (MultiSelectSpinner) loadingValuesDialog.findViewById(R.id.govtcodespinner);
        govtCatSpinner.setAllText("Select");
        govtCatSpinner.setDefaultText("Select");
        sTypeSpinner = (Spinner) loadingValuesDialog.findViewById(R.id.servicetxtspinner);
        cat_ListView = (ListView) loadingValuesDialog.findViewById(R.id.catList);
        discDateSpinner = (Spinner) loadingValuesDialog.findViewById(R.id.discdatespinner);
        discdatespinner_to = (Spinner) loadingValuesDialog.findViewById(R.id.discdatespinner_to);
        dtrSpinner = (MultiSelectSpinner) loadingValuesDialog.findViewById(R.id.dtrcodespinner);
        dtrSpinner.setAllText("Select");
        dtrSpinner.setDefaultText("Select");
        grpSpinner = (MultiSelectSpinner) loadingValuesDialog.findViewById(R.id.groupspinner);
        grpSpinner.setAllText("Select");
        grpSpinner.setDefaultText("Select");
        pole_spinner = (MultiSelectSpinner) loadingValuesDialog.findViewById(R.id.pole_spinner);
        pole_spinner.setAllText("Select");
        pole_spinner.setDefaultText("Select");
        loadingValues();

        dialogclosebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                totalString = "";
                amountSpinner.setSelection(0);
                socialCatSpinner.setSelected(false);
                govtCatSpinner.setSelected(false);
                sTypeSpinner.setSelection(0);
                discDateSpinner.setSelection(0);
                loadingValuesDialog.dismiss();

            }
        });

        dialogsubmitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                totalString = "";
                sAmountValue = sAmount + et_amounttxtvalue.getText().toString();
                amountSpinner.setSelection(0);
                socialCatSpinner.setSelected(false);
                govtCatSpinner.setSelected(false);
                sTypeSpinner.setSelection(0);
                discDateSpinner.setSelection(0);

                Utils.strCat = "";
                for (int i = 0; i < Utils.checkedCheckBoxList.size(); i++) {
                    Utils.strCat = TextUtils.join(",", Utils.checkedCheckBoxList);
                }
                Utils.checkedCheckBoxList.clear();

                if (!Utils.strCat.equals("Select")) {
                    if (!Utils.strCat.equals("")) {
                        if (!totalString.equals("")) {
                            totalString = totalString + " " + "AND" + " " + "cmcat" + " " + "IN" + "(" + Utils.strCat + ")";
                        } else {
                            totalString = "cmcat" + " " + "IN" + "(" + Utils.strCat + ")";
                        }
                    }

                }
                if (!sAmountValue.equals("Select")) {
                    if (!totalString.equals("")) {
                        totalString = totalString + " " + "AND" + " " + "total" + " " + sAmountValue;
                    } else {
                        totalString = "total" + " " + sAmountValue;
                    }
                }
                if (!Utility.isValueNullOrEmpty(sCat)) {
                    String cat = "";
                    if (sCat.contains(",")) {
                        cat = "cmsocialcat" + " " + "IN" + "(" + sCat + ")";
                    } else {
                        cat = "cmsocialcat" + " " + "IN" + "('" + sCat + "')";
                    }
                    if (!totalString.equals("")) {
                        totalString = totalString + " " + "AND" + " " + cat;

                    } else {
                        totalString = cat;
                    }
                }
               /* if (!sGovt.equals("Select")) {
                    if (!totalString.equals("")) {
                        totalString = totalString + " " + "AND" + " " + "govcat" + " " + "=" + "'" + sGovt + "'";
                    } else {
                        totalString = "govcat" + " " + "=" + "'" + sGovt + "'";
                    }
                }*/
                if (!Utility.isValueNullOrEmpty(sGovt)) {
                    String govt = "";
                    if (sGovt.contains(",")) {
                        govt = "govcat" + " " + "IN" + "(" + sGovt + ")";
                    } else {
                        govt = "govcat" + " " + "IN" + "('" + sGovt + "')";
                    }
                    if (!totalString.equals("")) {
                        totalString = totalString + " " + "AND" + " " + govt;

                    } else {
                        totalString = govt;
                    }
                }
                if (!Utility.isValueNullOrEmpty(sDdateFrom)) {
                    String discDate = "";
                    if (!sDdateTo.equalsIgnoreCase("Select")) {
                        discDate = "bldiscdt" + " " + ">=" + "'" + sDdateFrom + "'" + " " + "AND" + " " + "bldiscdt" + " " + "<=" + "'" + sDdateTo + "'";
                    } else {
                        discDate = "bldiscdt" + " " + ">=" + "'" + sDdateFrom + "'";
                    }
                    if (!totalString.equals("")) {
                        totalString = totalString + " " + "AND" + " " + discDate;
                    } else {
                        totalString = discDate;
                    }
                }
               /* if (!sDtr.equals("Select")) {
                    if (!totalString.equals("")) {
                        totalString = totalString + " " + "AND" + " " + "cmdtrcode" + " " + "=" + "'" + sDtr + "'";
                    } else {
                        totalString = "cmdtrcode" + " " + "=" + "'" + sDtr + "'";
                    }
                }*/
                if (!Utility.isValueNullOrEmpty(sDtr)) {
                    String dtr = "";
                    if (sDtr.contains(",")) {
                        dtr = "cmdtrcode" + " " + "IN" + "(" + sDtr + ")";
                    } else {
                        dtr = "cmdtrcode" + " " + "IN" + "('" + sDtr + "')";
                    }
                    if (!totalString.equals("")) {
                        totalString = totalString + " " + "AND" + " " + dtr;

                    } else {
                        totalString = dtr;
                    }
                }
                if (!Utility.isValueNullOrEmpty(sPole)) {
                    String dtr = "";
                    if (sPole.contains(",")) {
                        dtr = "poleno" + " " + "IN" + "(" + sPole + ")";
                    } else {
                        dtr = "poleno" + " " + "IN" + "('" + sPole + "')";
                    }
                    if (!totalString.equals("")) {
                        totalString = totalString + " " + "AND" + " " + dtr;

                    } else {
                        totalString = dtr;
                    }
                }
              /*  if (!sGrp.equals("Select")) {
                    if (!totalString.equals("")) {
                        totalString = totalString + " " + "AND" + " " + "grp" + " " + "=" + "'" + sGrp + "'";
                    } else {
                        totalString = "grp" + " " + "=" + "'" + sGrp + "'";
                    }
                }*/
                if (!Utility.isValueNullOrEmpty(sGrp)) {
                    String group = "";
                    if (sGrp.contains(",")) {
                        group = "grp" + " " + "IN" + "(" + sGrp + ")";
                    } else {
                        group = "grp" + " " + "IN" + "('" + sGrp + "')";
                    }
                    if (!totalString.equals("")) {
                        totalString = totalString + " " + "AND" + " " + group;

                    } else {
                        totalString = group;
                    }
                }
                Utility.showLog("Final Query", totalString);
                if (!totalString.equals("")) {
                    try {
                        consumersList.clear();
                        new OperateMatsDCListActivity.MyFilterAsyncTasks().execute();
                        //advanceFlag = "T";
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                loadingValuesDialog.dismiss();
            }

        });

        editsearch.setText("");
        loadingValuesDialog.show();
    }

    public void loadingValues() {
        try {
            List<String> list = new ArrayList<String>();
            list.add("Select");
            list.add("<");
            list.add("=");
            list.add(">");

            ArrayAdapter<String> amountAdapter = new ArrayAdapter<String>(OperateMatsDCListActivity.this, android.R.layout.simple_spinner_item, list);
            amountAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            amountSpinner.setAdapter(amountAdapter);

            amountSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> parent, View arg1, int position, long arg3) {
                    sAmount = parent.getItemAtPosition(position).toString();
                }

                public void onNothingSelected(AdapterView<?> arg0) {

                }
            });


            try {
                poleList.clear();
                poles.clear();
                poleList.addAll(db.getAllPoles());
                for (int i = 0; i < poleList.size(); i++) {
                    poles.add(poleList.get(i).getPOLENO());
                }
                poles.removeAll(Arrays.asList(null, ""));
                final ArrayAdapter<String> dtrAdapter = new ArrayAdapter<String>(OperateMatsDCListActivity.this, android.R.layout.simple_spinner_item, poles);
                dtrAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                pole_spinner.setAdapter(dtrAdapter, false, new MultiSelectSpinner.MultiSpinnerListener() {
                    @Override
                    public void onItemsSelected(boolean[] selected) {
                        // socialCatSpinner.setAllText("");
                        StringBuilder builder = new StringBuilder();
                        String sep = "";
                        for (int i = 0; i < selected.length; i++) {
                            if (selected[i]) {
                                builder.append(sep);
                                builder.append("'" + dtrAdapter.getItem(i) + "'");
                                sep = ",";
                            }
                        }
                        sPole = builder.toString();
                        if (!sPole.contains(",")) {
                            sPole = sPole.replace("'", "");
                        }

                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}