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
import android.widget.Toast;

import com.apcpdcl.departmentapp.R;
import com.apcpdcl.departmentapp.adapters.ConsumerRecyclerviewAdapter;
import com.apcpdcl.departmentapp.adapters.MyAdapter;
import com.apcpdcl.departmentapp.customviews.MultiSelectSpinner;
import com.apcpdcl.departmentapp.models.Consumer;
import com.apcpdcl.departmentapp.models.StateVO;
import com.apcpdcl.departmentapp.services.NetworkChangeReceiver;
import com.apcpdcl.departmentapp.sqlite.DatabaseHandler;
import com.apcpdcl.departmentapp.sqlite.MatsReportsDataBaseHandler;
import com.apcpdcl.departmentapp.utils.Constants;
import com.apcpdcl.departmentapp.utils.Utility;
import com.apcpdcl.departmentapp.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 11/9/2017.
 */

public class MatsDCListData extends AppCompatActivity {

    private ListView cat_ListView;
    private RecyclerView recyclerView;
    private ConsumerRecyclerviewAdapter consumer_Adapter;
    RecyclerView.LayoutManager recylerViewLayoutManager;
    private ArrayList<Consumer> consumersList = new ArrayList<>();

    private ArrayList<Consumer> socialCatconsumersList = new ArrayList<>();
    List<String> socialCatList = new ArrayList<String>();

    private ArrayList<Consumer> CatconsumersList = new ArrayList<>();
    List<String> CatList = new ArrayList<String>();

    private ArrayList<Consumer> govtCatconsumersList = new ArrayList<>();
    List<String> govtCatList = new ArrayList<String>();

   /* private ArrayList<Consumer> sTypeList = new ArrayList<>();
    List<String> sList = new ArrayList<String>();*/

    private ArrayList<Consumer> discDateList = new ArrayList<>();
    List<String> ddList = new ArrayList<String>();
    List<String> ddList_to = new ArrayList<String>();

    private ArrayList<Consumer> dtrCodeList = new ArrayList<>();
    List<String> dtrList = new ArrayList<String>();

    private ArrayList<Consumer> poleList = new ArrayList<>();
    List<String> poles = new ArrayList<String>();

    private ArrayList<Consumer> groupList = new ArrayList<>();
    List<String> grpList = new ArrayList<String>();


    DatabaseHandler db;
    MatsReportsDataBaseHandler rdb;
    ProgressDialog progressDialog;
    TableLayout tableHeadLayout;
    View bottomview;
    EditText editsearch, et_amounttxtvalue;
    String sec_code, strActivity;
    Button advancedSearchFilterBtn, dialogsubmitbtn;
    Spinner amountSpinner, sTypeSpinner, discDateSpinner, discdatespinner_to;
    RelativeLayout socialcat_layout;
    private MultiSelectSpinner socialCatSpinner, grpSpinner,pole_spinner, govtCatSpinner, dtrSpinner;
    String sAmount, sCat, sGovt, sAmountValue, sDdateFrom, sDdateTo, sDtr,sPole, sGrp;
    private String totalString = "";
    ImageView home_imageView, dialogclosebtn;
    int textlength;
    Dialog loadingValuesDialog;
    String flag = "N";
    //String advanceFlag = "F";
    private NetworkChangeReceiver networkChangeReceiver;
    public static String sstype = "";
    boolean isFirst = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.opearte_listlayout);

        if (getIntent().hasExtra(Constants.SERVICE_TYPE)) {
            sstype = getIntent().getStringExtra(Constants.SERVICE_TYPE);
        }

        SharedPreferences prefs = getSharedPreferences("loginPrefs", 0);
        sec_code = prefs.getString("Section_Code", "");


        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recylerViewLayoutManager = new LinearLayoutManager(MatsDCListData.this);
        recyclerView.setLayoutManager(recylerViewLayoutManager);
        bottomview = findViewById(R.id.bottomview);
        tableHeadLayout = (TableLayout) findViewById(R.id.tableHeadLayout);
        db = new DatabaseHandler(this);
        rdb = new MatsReportsDataBaseHandler(this);
        editsearch = (EditText) findViewById(R.id.search);
        advancedSearchFilterBtn = (Button) findViewById(R.id.filterbtn);
        home_imageView = (ImageView) findViewById(R.id.home);
        socialcat_layout = (RelativeLayout) findViewById(R.id.socialcat_layout);
        advancedSearchFilterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    view = MatsDCListData.this.getCurrentFocus();
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
                // TODO Auto-generated method stub
//                if (advanceFlag.equals("T")) {
//                    try {
//                            /*Here records are loaded fastly. User Doesn't know here records are loading again.
//                             and app hangs some time. for that purpose here i am using thread to stop user for 5 secs*/
//                        progressDialog = new ProgressDialog(Operating.this);
//                        progressDialog.setMessage("Please Wait Fetching is going on.....");
//                        progressDialog.setCancelable(false);
//                        progressDialog.show();
//                        new Handler().postDelayed(new Runnable() {
//                            public void run() {
//                                consumersList.clear();
//                                consumersList.addAll(db.getConsumers());
//                                consumerAdapter = new ConsumerAdapter(Operating.this, consumersList);
//                                listView.setAdapter(consumerAdapter);
//                                progressDialog.dismiss();
//                            }
//                        }, 3000);
//                        advanceFlag = "F";
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }

            }

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                if (cs.length() >= 3) {
                    textlength = cs.length();
                    ArrayList<Consumer> tempArrayList = new ArrayList<Consumer>();
                    try {
                        for (Consumer c : consumersList) {
                            if (textlength <= c.getCmuscno().length()) {
                                if (c.getCmuscno().toLowerCase().contains(cs.toString().toLowerCase())) {
                                    tempArrayList.add(c);
                                }
                            }
                        }
                        consumer_Adapter = new ConsumerRecyclerviewAdapter(MatsDCListData.this, tempArrayList);
                        //consumer_Adapter.setClickListener(Operating.this);
                        recyclerView.setAdapter(consumer_Adapter);
//                        consumerAdapter = new ConsumerAdapter(Operating.this, tempArrayList);
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
                            progressDialog = new ProgressDialog(MatsDCListData.this);
                            progressDialog.setMessage("Please Wait Fetching is going on.....");
                            progressDialog.setCancelable(false);
                            progressDialog.show();
                            new Handler().postDelayed(new Runnable() {
                                public void run() {
                                    consumersList.clear();
                                    consumersList.addAll(db.getConsumers(sstype));
                                    consumer_Adapter = new ConsumerRecyclerviewAdapter(MatsDCListData.this, consumersList);
                                    //consumer_Adapter.setClickListener(Operating.this);
                                    recyclerView.setAdapter(consumer_Adapter);
//                                    consumerAdapter = new ConsumerAdapter(Operating.this, consumersList);
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

    private void loadingValuesPopup() {

        loadingValuesDialog = new Dialog(MatsDCListData.this);
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
                totalString = totalString + " " + "AND" + " " + "sType" + " " + "=" + "'" + sstype + "'";
                Utility.showLog("Final Query", totalString);
                if (!totalString.equals("")) {
                    try {
                        consumersList.clear();
                        new MyFilterAsyncTasks().execute();
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

            ArrayAdapter<String> amountAdapter = new ArrayAdapter<String>(MatsDCListData.this, android.R.layout.simple_spinner_item, list);
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
                socialCatconsumersList.clear();
                socialCatList.clear();
                //socialCatList.add("Select");
                socialCatconsumersList.addAll(db.getAllSocialCat());
                for (int i = 0; i < socialCatconsumersList.size(); i++) {
                    socialCatList.add(socialCatconsumersList.get(i).getSocialCat());
                }

                /*For removing null and empty values in arraylist*/
                socialCatList.removeAll(Arrays.asList(null, ""));


                final ArrayAdapter<String> socialCatAdapter = new ArrayAdapter<String>(MatsDCListData.this, android.R.layout.simple_spinner_item, socialCatList);
                socialCatAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                boolean[] selectedItems = new boolean[socialCatAdapter.getCount()];
                socialCatSpinner.setAdapter(socialCatAdapter, false, new MultiSelectSpinner.MultiSpinnerListener() {
                    @Override
                    public void onItemsSelected(boolean[] selected) {
                        // socialCatSpinner.setAllText("");
                        StringBuilder builder = new StringBuilder();
                        String sep = "";
                        for (int i = 0; i < selected.length; i++) {
                            if (selected[i]) {
                                builder.append(sep);
                                builder.append("'" + socialCatAdapter.getItem(i) + "'");
                                sep = ",";
                            }
                        }
                        sCat = builder.toString();
                        if (!sCat.contains(",")) {
                            sCat = sCat.replace("'", "");
                        }

                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }


            try {
                govtCatconsumersList.clear();
                govtCatList.clear();
                govtCatconsumersList.addAll(db.getAllGovtCat());
                for (int i = 0; i < govtCatconsumersList.size(); i++) {
                    govtCatList.add(govtCatconsumersList.get(i).getGovtCat());
                }
                govtCatList.removeAll(Arrays.asList(null, ""));
                final ArrayAdapter<String> govtCatAdapter = new ArrayAdapter<String>(MatsDCListData.this, android.R.layout.simple_spinner_item, govtCatList);
                govtCatAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

               /*  govtCatSpinner.setAdapter(govtCatAdapter);
                    govtCatSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View arg1, int position, long arg3) {
                        sGovt = parent.getItemAtPosition(position).toString();

                    }

                    public void onNothingSelected(AdapterView<?> arg0) {

                    }
                });*/
                govtCatSpinner.setAdapter(govtCatAdapter, false, new MultiSelectSpinner.MultiSpinnerListener() {
                    @Override
                    public void onItemsSelected(boolean[] selected) {
                        // socialCatSpinner.setAllText("");
                        StringBuilder builder = new StringBuilder();
                        String sep = "";
                        for (int i = 0; i < selected.length; i++) {
                            if (selected[i]) {
                                builder.append(sep);
                                builder.append("'" + govtCatAdapter.getItem(i) + "'");
                                sep = ",";
                            }
                        }
                        sGovt = builder.toString();
                        if (!sGovt.contains(",")) {
                            sGovt = sGovt.replace("'", "");
                        }

                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                discDateList.clear();
                ddList.clear();
                discDateList.addAll(db.getAllDiscDates());
                ddList.add("Select");
                for (int i = 0; i < discDateList.size(); i++) {
                    ddList.add(discDateList.get(i).getBldiscdt());
                }
                ddList.removeAll(Arrays.asList(null, ""));
                final ArrayAdapter<String> ddAdapter = new ArrayAdapter<String>(MatsDCListData.this, android.R.layout.simple_spinner_item, ddList);
                ddAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                discDateSpinner.setAdapter(ddAdapter);
                discdatespinner_to.setAdapter(ddAdapter);
                discDateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View arg1, int position, long arg3) {
                        if (position == 0) {
                            sDdateFrom = "";
                            discdatespinner_to.setAdapter(ddAdapter);
                        } else {
                            sDdateFrom = parent.getItemAtPosition(position).toString();
                            ddList_to.clear();
                            ddList_to.add("Select");
                            for (int i = position - 1; i < discDateList.size(); i++) {
                                ddList_to.add(discDateList.get(i).getBldiscdt());
                            }
                            ArrayAdapter<String> ddAdapter = new ArrayAdapter<String>(MatsDCListData.this, android.R.layout.simple_spinner_item, ddList_to);
                            ddAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            discdatespinner_to.setAdapter(ddAdapter);
                        }
                    }

                    public void onNothingSelected(AdapterView<?> arg0) {

                    }
                });
                discdatespinner_to.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View arg1, int position, long arg3) {
                        if (position == 0) {
                            sDdateTo = "";
                        } else if (Utility.isValueNullOrEmpty(sDdateFrom)) {
                            discdatespinner_to.setSelection(0);
                            Utility.showToastMessage(MatsDCListData.this, "Please select from Date First");
                        } else {
                            sDdateTo = parent.getItemAtPosition(position).toString();
                        }
                    }

                    public void onNothingSelected(AdapterView<?> arg0) {

                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                dtrCodeList.clear();
                dtrList.clear();
                dtrCodeList.addAll(db.getAllDtrCodes());
                for (int i = 0; i < dtrCodeList.size(); i++) {
                    dtrList.add(dtrCodeList.get(i).getCmdtrCode());
                }
                dtrList.removeAll(Arrays.asList(null, ""));
                final ArrayAdapter<String> dtrAdapter = new ArrayAdapter<String>(MatsDCListData.this, android.R.layout.simple_spinner_item, dtrList);
                dtrAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                dtrSpinner.setAdapter(dtrAdapter, false, new MultiSelectSpinner.MultiSpinnerListener() {
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
                        sDtr = builder.toString();
                        if (!sDtr.contains(",")) {
                            sDtr = sDtr.replace("'", "");
                        }

                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                poleList.clear();
                poles.clear();
                poleList.addAll(db.getAllPoles());
                for (int i = 0; i < poleList.size(); i++) {
                    poles.add(poleList.get(i).getPoleNo());
                }
                poles.removeAll(Arrays.asList(null, ""));
                final ArrayAdapter<String> dtrAdapter = new ArrayAdapter<String>(MatsDCListData.this, android.R.layout.simple_spinner_item, poles);
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
                            sPole = sPole  .replace("'", "");
                        }

                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                groupList.clear();
                grpList.clear();
                groupList.addAll(db.getAllGroups());
                for (int i = 0; i < groupList.size(); i++) {
                    grpList.add(groupList.get(i).getGrp());
                }
                grpList.removeAll(Arrays.asList(null, ""));
                final ArrayAdapter<String> grpAdapter = new ArrayAdapter<String>(MatsDCListData.this, android.R.layout.simple_spinner_item, grpList);
                grpAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
              /*  grpSpinner.setAdapter(grpAdapter);

                grpSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View arg1, int position, long arg3) {
                        sGrp = parent.getItemAtPosition(position).toString();
                    }

                    public void onNothingSelected(AdapterView<?> arg0) {

                    }
                });*/
                grpSpinner.setAdapter(grpAdapter, false, new MultiSelectSpinner.MultiSpinnerListener() {
                    @Override
                    public void onItemsSelected(boolean[] selected) {
                        // socialCatSpinner.setAllText("");
                        StringBuilder builder = new StringBuilder();
                        String sep = "";
                        for (int i = 0; i < selected.length; i++) {
                            if (selected[i]) {
                                builder.append(sep);
                                builder.append("'" + grpAdapter.getItem(i) + "'");
                                sep = ",";
                            }
                        }
                        sGrp = builder.toString();
                        if (!sGrp.contains(",")) {
                            sGrp = sGrp.replace("'", "");
                        }

                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                ArrayList<StateVO> listVOs = new ArrayList<>();
                CatconsumersList.clear();
                CatList.clear();
                CatconsumersList.addAll(db.getAllCats(sstype));
                for (int i = 0; i < CatconsumersList.size(); i++) {
                    CatList.add(CatconsumersList.get(i).getCmcat());
                }

                /*For removing null and empty values in arraylist*/
                CatList.removeAll(Arrays.asList(null, ""));
                for (int i = 0; i < CatList.size(); i++) {
                    StateVO stateVO = new StateVO();
                    stateVO.setTitle(String.valueOf(CatList.get(i)));
                    stateVO.setSelected(false);
                    listVOs.add(stateVO);
                }
                MyAdapter myAdapter = new MyAdapter(MatsDCListData.this, 0, listVOs);
                cat_ListView.setAdapter(myAdapter);
                Utility.setListViewHeightBasedOnChildren(cat_ListView);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


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
            progressDialog = new ProgressDialog(MatsDCListData.this);
            progressDialog.setMessage("Please Wait Fetching is going on.....");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                consumersList.addAll(db.getConsumers(sstype));
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
                    consumer_Adapter = new ConsumerRecyclerviewAdapter(MatsDCListData.this, consumersList);
                    //consumer_Adapter.setClickListener(Operating.this);
                    recyclerView.setAdapter(consumer_Adapter);
//                consumerAdapter = new ConsumerAdapter(Operating.this, consumersList);
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
            progressDialog = new ProgressDialog(MatsDCListData.this);
            progressDialog.setMessage("Please Wait Fetching is going on.....");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                consumersList.addAll(db.getAllFilterConsumers(totalString));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            if (consumersList.size() > 0) {
                consumer_Adapter = new ConsumerRecyclerviewAdapter(MatsDCListData.this, consumersList);
                //consumer_Adapter.setClickListener(Operating.this);
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
}