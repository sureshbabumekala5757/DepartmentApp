package com.apcpdcl.departmentapp.activities;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.apcpdcl.departmentapp.R;
import com.apcpdcl.departmentapp.service.ApiServiceTask;
import com.apcpdcl.departmentapp.service.ApiTaskCallBack;
import com.apcpdcl.departmentapp.shared.AppPrefs;
import com.apcpdcl.departmentapp.utils.Utility;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReadingEntry extends AppCompatActivity implements ApiTaskCallBack {

    private LinearLayout reading_ll, kwh_Kvah_reading_ll, agl_reading_ll;
    private String readingType;
    private TextView toolbar_title;
    @BindView(R.id.reading_date)
    EditText reading_date;
    private int minutes = -1;
    private int mYear = -1;
    private int mMonth = -1;
    private int mDay = -1;
    private boolean isRestore = false;
    private LinearLayout commanLayout;

    @BindView(R.id.layoutFeeder)
    LinearLayout layoutFeeder;
    @BindView(R.id.spinFeeder)
    AppCompatSpinner spinFeeder;
    @BindView(R.id.imagePlus)
    ImageView imagePlus;
    @BindView(R.id.textSS)
    TextView textSS;

    private int spellCount = 0;
    private ProgressDialog pDialog;

    private int nCase = 1;
    private String userId = "";
    private String getFeederDetails = "https://apcpcdcl-test-k5qoqm5y.it-cpi012-rt.cfapps.ap21.hana.ondemand.com/http/ConsumerApp/SAPISU/GetFeederDetails/DEV";
    private String saveReadings = "https://apcpcdcl-test-k5qoqm5y.it-cpi012-rt.cfapps.ap21.hana.ondemand.com/http/ConsumerApp/SAPISU/SaveReadings/DEV";

    private JSONObject feederJson = null;
    private JSONArray feedersDetailsArray = null;
    private JSONArray lvDetailsArray = null;
    private JSONObject saveReadingsJson = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading_entry);

        ButterKnife.bind(this);
        toolbar_title = findViewById(R.id.toolbar_title);
        kwh_Kvah_reading_ll = findViewById(R.id.kwh_Kvah_reading_ll);
        agl_reading_ll = findViewById(R.id.agl_reading_ll);
        reading_ll = findViewById(R.id.reading_ll);

        Intent intent = getIntent();
        Bundle bd = intent.getExtras();
        if (bd != null) {
            readingType = (String) bd.get("ReadingType");
            if (!readingType.equalsIgnoreCase("AGL 3-Ph Feeder")) {
                toolbar_title.setText(readingType + " and LV Reading Entry");
            }else{
                toolbar_title.setText(readingType + " Reading Entry");
            }

        }

        pDialog = new ProgressDialog(this);

        layoutFeeder.setVisibility(View.GONE);
        imagePlus.setVisibility(View.GONE);

        // Remove all the existing TextViews
        kwh_Kvah_reading_ll.removeAllViews();
        agl_reading_ll.removeAllViews();

        userId = AppPrefs.getInstance(getApplicationContext()).getString("USERID", "");
        //userId = "SS3027212402";

        if (!readingType.equalsIgnoreCase("AGL 3-Ph Feeder")) {
            nCase = 1;
        } else {
            nCase = 2;
            getFeederDetails = "https://apcpcdcl-test-k5qoqm5y.it-cpi012-rt.cfapps.ap21.hana.ondemand.com/http/ConsumerApp/SAPISU/GetAGLFeederReadings/DEV";
            saveReadings = "https://apcpcdcl-test-k5qoqm5y.it-cpi012-rt.cfapps.ap21.hana.ondemand.com/http/ConsumerApp/SAPISU/SaveAGLFeederReadings/DEV";
        }

        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        String formattedDate = dateFormat.format(currentDate);
        reading_date.setText(formattedDate);

        getFeederDetails();
    }

    /* *
     *Get Feeder Details
     * */
    private void getFeederDetails() {
        JSONObject requestObj = new JSONObject();
        try {
            requestObj.put("date", reading_date.getText().toString());
            requestObj.put("userid", userId);
//            requestObj.put("unit_type", "KWH");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (Utility.isNetworkAvailable(this))
            new ApiServiceTask(this, this, "2", getFeederDetails).execute(requestObj.toString());
        else {
            Utility.showCustomOKOnlyDialog(this,
                    Utility.getResourcesString(this,
                            R.string.no_internet));
        }
    }

    private void addFeeder(JSONObject jsonObject) {
        feederJson = jsonObject;
        try {
            // Create a new TextView
            TextView feederTv = new TextView(this);
            feederTv.setText(jsonObject.optString("fl_name"));
            agl_reading_ll.addView(feederTv);
            imagePlus.setVisibility(View.VISIBLE);

            JSONObject ATN_LV_DETAILS_TO_SPELLS_NAV = jsonObject.optJSONObject("ATN_LV_DETAILS_TO_SPELLS_NAV");
            if (ATN_LV_DETAILS_TO_SPELLS_NAV != null && ATN_LV_DETAILS_TO_SPELLS_NAV.length() > 0) {

                JSONArray spellDetails = ATN_LV_DETAILS_TO_SPELLS_NAV.optJSONArray("ETY_SPELLS_DETAILS");
                if (spellDetails != null && spellDetails.length() > 0) {
                    for (int i = 0; i < spellDetails.length(); i++)
                        addSpell(spellDetails.optJSONObject(i));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addSpell(JSONObject jsonObject) {
        spellCount++;

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View spellView = inflater.inflate(R.layout.spell_entry, null);

        TextView textSpell = spellView.findViewById(R.id.textSpell);
        textSpell.setText(jsonObject.optString("spell_no"));

        EditText editOpen = spellView.findViewById(R.id.editOpen);
        editOpen.setText(jsonObject.optString("kwh_intial"));

        EditText editClose = spellView.findViewById(R.id.editClose);
        editClose.setText(jsonObject.optString("kwh_final"));

        EditText editDuration = spellView.findViewById(R.id.editDuration);
        editDuration.setText(jsonObject.optString("duration"));

        // Add the TextView and EditText to the LinearLayout
        agl_reading_ll.addView(spellView);
    }

    public void addSpell(View view) {
        spellCount++;

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View spellView = inflater.inflate(R.layout.spell_entry, null);

        TextView textSpell = spellView.findViewById(R.id.textSpell);
        textSpell.setText(String.valueOf(spellCount));

        // Add the TextView and EditText to the LinearLayout
        agl_reading_ll.addView(spellView);
    }

    public void submitBtn(View view) {
        try {
            // Get a reference to the LinearLayout
            if (!readingType.equalsIgnoreCase("AGL 3-Ph Feeder")) {
                commanLayout = kwh_Kvah_reading_ll;
            } else {
                commanLayout = agl_reading_ll;
            }

            saveReadingsJson = new JSONObject();

            saveReadingsJson.put("date", reading_date.getText().toString());
//            saveReadingsJson.put("unit_type", "KWH");
            if (!readingType.equalsIgnoreCase("AGL 3-Ph Feeder")) {
                saveReadingsJson.put("user_id", userId);
            } else {
                saveReadingsJson.put("userid", userId);
            }


            if (!readingType.equalsIgnoreCase("AGL 3-Ph Feeder")) {

                JSONArray feederJsonArray = new JSONArray();
                JSONArray lvJsonArray = new JSONArray();

                // Loop through the child views of the LinearLayout
                for (int i = 0; i < commanLayout.getChildCount(); i++) {

                    View childView = commanLayout.getChildAt(i);

                    TextView textFeeder = childView.findViewById(R.id.textFeeder);

                    EditText editKWH = childView.findViewById(R.id.editKWH);
                    String kwhValue = "";
                    // Get the value of the EditText
                    if (editKWH != null)
                        kwhValue = editKWH.getText().toString();

                    EditText editKVAH = childView.findViewById(R.id.editKVAH);
                    String kvahValue = "";
                    // Get the value of the EditText
                    if (editKVAH != null)
                        kvahValue = editKVAH.getText().toString();

                    JSONObject jsonObject1 = new JSONObject();
                    jsonObject1.put("eq_no", textFeeder.getTag().toString());
                    jsonObject1.put("kwh", kwhValue);
                    jsonObject1.put("kvah", kvahValue);
                    jsonObject1.put("duration", "");
                    if (textFeeder.getText().toString().contains("LV"))
                        lvJsonArray.put(jsonObject1);
                    else
                        feederJsonArray.put(jsonObject1);
                }

                saveReadingsJson.put("ATN_READINGS_NAV", feederJsonArray);
                saveReadingsJson.put("ATN_LV_READINGS_NAV", lvJsonArray);
            } else {

                JSONArray jsonArray = new JSONArray();
                JSONObject jsonObject1 = new JSONObject();
                jsonObject1.put("eq_no", feederJson.optString("eq_no"));
                JSONArray jsonArray1 = new JSONArray();

                // Loop through the child views of the LinearLayout
                for (int i = 0; i < commanLayout.getChildCount(); i++) {

                    View childView = commanLayout.getChildAt(i);

                    if (childView instanceof ViewGroup) {
                        TextView textSpell = childView.findViewById(R.id.textSpell);
                        String spellNo = textSpell.getText().toString();

                        EditText editOpen = childView.findViewById(R.id.editOpen);
                        String openValue = "";
                        // Get the value of the EditText
                        if (editOpen != null)
                            openValue = editOpen.getText().toString();

                        EditText editClose = childView.findViewById(R.id.editClose);
                        String closeValue = "";
                        // Get the value of the EditText
                        if (editClose != null)
                            closeValue = editClose.getText().toString();

                        EditText editDuration = childView.findViewById(R.id.editDuration);
                        String durationValue = "";
                        // Get the value of the EditText
                        if (editDuration != null)
                            durationValue = editDuration.getText().toString();

                        JSONObject jsonObject2 = new JSONObject();
                        jsonObject2.put("spell_no", spellNo);
                        jsonObject2.put("kwh_intial", openValue);
                        jsonObject2.put("kwh_final", closeValue);
                        jsonObject2.put("duration", durationValue);
                        jsonArray1.put(jsonObject2);
                    }
                }

                jsonObject1.put("ATN_READING_TO_SPELLS_NAV", jsonArray1);
                jsonArray.put(jsonObject1);
                saveReadingsJson.put("ATN_SAVE_AGL_HEADER_TO_LV_NAV", jsonArray);
            }
            Log.d("FEEDER_READINGS", saveReadingsJson.toString());

            nCase = 3;
            if (Utility.isNetworkAvailable(this))
                new ApiServiceTask(this, this, "2", saveReadings).execute(saveReadingsJson.toString());
            else {
                Utility.showCustomOKOnlyDialog(this,
                        Utility.getResourcesString(this,
                                R.string.no_internet));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            if (isRestore) {
                isRestore = false;
            } else {
//                if (dayOfMonth < 10) {
//                    reading_date.setText("0" + dayOfMonth + "-" + Utility.getMonthFormat(monthOfYear + 1) + "-" + year);
//                } else {
//                    reading_date.setText(dayOfMonth + "-" + Utility.getMonthFormat(monthOfYear + 1) + "-" + year);
//                }
                if (dayOfMonth < 10) {
                    reading_date.setText("0" + dayOfMonth + "-" + "0" + (monthOfYear + 1) + "-" + year);
                } else {
                    reading_date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                }
                mYear = year;
                mMonth = monthOfYear + 1;
                mDay = dayOfMonth;
            }

            getFeederDetails();

            //setDuration();
        }
    };

    @OnClick(R.id.reading_date)
    void openOutageDatePicker() {
        final Calendar c = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, date, c.get(Calendar.YEAR),
                c.get(Calendar.MONTH), c.get(Calendar.DATE));
        datePickerDialog.getDatePicker().setMaxDate(c.getTimeInMillis());
        datePickerDialog.getDatePicker().setMinDate(Calendar.DATE);
        datePickerDialog.show();
        //setDuration();
    }

    @Override
    public void onTaskCompleted(String strResponse) {
        Log.d("RESULT", strResponse);

        try {
            if (strResponse != null && !strResponse.isEmpty()) {
                if (nCase == 1) {

                    JSONObject jsonObject = new JSONObject(strResponse);
                    if (jsonObject != null && jsonObject.length() > 0) {
                        JSONObject inputFeederDetails = jsonObject.optJSONObject("ETY_INPUT_GET_FEEDER_DETAILS");
                        if (inputFeederDetails != null && inputFeederDetails.length() > 0) {

                            if (inputFeederDetails.optString("success").equalsIgnoreCase("True")) {
                                JSONObject ATN_INPUT_TO_FEED_DETAILS_NAV = inputFeederDetails.optJSONObject("ATN_INPUT_TO_FEED_DETAILS_NAV");
                                if (ATN_INPUT_TO_FEED_DETAILS_NAV != null && ATN_INPUT_TO_FEED_DETAILS_NAV.length() > 0) {
                                    JSONObject ETY_SUB_RESPONSE_FEEDER = ATN_INPUT_TO_FEED_DETAILS_NAV.optJSONObject("ETY_SUB_RESPONSE_FEEDER");
                                    if (ETY_SUB_RESPONSE_FEEDER != null && ETY_SUB_RESPONSE_FEEDER.length() > 0) {

                                        textSS.setText(ETY_SUB_RESPONSE_FEEDER.optString("substation_name"));
                                        JSONObject feederDetailsObj = ETY_SUB_RESPONSE_FEEDER.optJSONObject("ATN_SUBRES_TO_FEED_DETAIL_NAV");
                                        if (feederDetailsObj != null && feederDetailsObj.length() > 0) {
                                            feedersDetailsArray = feederDetailsObj.optJSONArray("ETY_FEEDER_DETAILS");
                                        }

                                        JSONObject lvDetailsOnj = ETY_SUB_RESPONSE_FEEDER.optJSONObject("ATN_SUBRES_TO_LV_DETAIL_NAV");
                                        if (lvDetailsOnj != null && lvDetailsOnj.length() > 0) {

//                                            JSONObject lvDetails = lvDetailsOnj.optJSONObject("ETY_LV_DETAILS");
//                                            if (lvDetails != null && lvDetails.length() > 0) {
//
//                                                if (feedersDetailsArray == null)
//                                                    feedersDetailsArray = new JSONArray();
//
//                                                feedersDetailsArray.put(lvDetails);
//
//                                            }
                                            JSONArray lvDetails = lvDetailsOnj.optJSONArray("ETY_LV_DETAILS");
                                            if (lvDetails != null && lvDetails.length() > 0) {

                                                if (feedersDetailsArray == null)
                                                    feedersDetailsArray = new JSONArray();

                                                for (int i = 0; i < lvDetails.length(); i++) {
                                                    feedersDetailsArray.put(lvDetails.optJSONObject(i));
                                                }

                                            }
                                        }
                                    }
                                }
                                if (feedersDetailsArray != null && feedersDetailsArray.length() > 0) {
                                    // Loop through the desired number of times
                                    kwh_Kvah_reading_ll.removeAllViews();
                                    for (int i = 0; i < feedersDetailsArray.length(); i++) {

                                        JSONObject obj = feedersDetailsArray.optJSONObject(i);
                                        if (obj != null && obj.length() > 0) {
                                            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                            View view = inflater.inflate(R.layout.reading_entry, null);

                                            TextView textFeeder = view.findViewById(R.id.textFeeder);
                                            textFeeder.setText(obj.optString("fl_name"));
                                            textFeeder.setTag(obj.optString("eq_no"));

                                            EditText etKWHFeeder = view.findViewById(R.id.editKWH);
                                            etKWHFeeder.setText(obj.optString("present_kwh_reading"));

                                            EditText etKVAHFeeder = view.findViewById(R.id.editKVAH);
                                            etKVAHFeeder.setText(obj.optString("present_kvah_reading"));

                                            kwh_Kvah_reading_ll.addView(view);
                                        }
                                    }
                                }
                            }else{
                                Toast.makeText(this, inputFeederDetails.optString("message"), Toast.LENGTH_LONG).show();
                            }
                        }else{
                            Toast.makeText(this, "Server Not Working", Toast.LENGTH_LONG).show();
                        }
                    }


                } else if (nCase == 2) {
                    JSONObject jsonObject = new JSONObject(strResponse);
                    if (jsonObject != null && jsonObject.length() > 0) {
                        JSONObject ETY_INPUT_GET_AGL_FEEDER_DETAILS = jsonObject.optJSONObject("ETY_INPUT_GET_AGL_FEEDER_DETAILS");
                        if (ETY_INPUT_GET_AGL_FEEDER_DETAILS != null && ETY_INPUT_GET_AGL_FEEDER_DETAILS.length() > 0) {

                            if (ETY_INPUT_GET_AGL_FEEDER_DETAILS.optString("success").equalsIgnoreCase("True")) {

                                JSONObject aglFeederDetails = ETY_INPUT_GET_AGL_FEEDER_DETAILS.optJSONObject("ATN_AGL_FEED_TO_LV_DETAILS_NAV");
                                if (aglFeederDetails != null && aglFeederDetails.length() > 0) {

                                    lvDetailsArray = aglFeederDetails.optJSONArray("ETY_LV_DETAILS");
                                    if (lvDetailsArray != null && lvDetailsArray.length() > 0) {

                                        layoutFeeder.setVisibility(View.VISIBLE);

                                        ArrayList<String> feedersList = new ArrayList<>();

                                        for (int i = 0; i < lvDetailsArray.length(); i++) {
                                            JSONObject lv = lvDetailsArray.optJSONObject(i);
                                            if (lv != null && lv.length() > 0) {
                                                feedersList.add(lv.optString("fl_name"));
                                            }
                                        }

                                        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.auto_item, R.id.tv_item, feedersList);
                                        spinFeeder.setAdapter(arrayAdapter);

                                        spinFeeder.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                                                try {
                                                    String selectedItem = adapterView.getItemAtPosition(i).toString();
                                                    spellCount = 0;
                                                    agl_reading_ll.removeAllViews();
                                                    if (!selectedItem.contains("SELECT")) {
                                                        addFeeder(lvDetailsArray.getJSONObject(i));
                                                    }
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> adapterView) {

                                            }
                                        });
                                    }
                                }
                            }else{
                                Toast.makeText(this, ETY_INPUT_GET_AGL_FEEDER_DETAILS.optString("message"), Toast.LENGTH_LONG).show();
                            }
                        }else{
                            Toast.makeText(this, "Server Not Working", Toast.LENGTH_LONG).show();
                        }
                    }
                } else {
                    JSONObject jsonObject = new JSONObject(strResponse);
                    if (!readingType.equalsIgnoreCase("AGL 3-Ph Feeder")) {
                        jsonObject = jsonObject.getJSONObject("ETY_SAVE_INPUT");
                        if(jsonObject != null && jsonObject.length() > 0){
                            if (jsonObject.optString("success").equalsIgnoreCase("True")) {
                                Toast.makeText(this, jsonObject.optString("message"), Toast.LENGTH_LONG).show();
                            }else{
                                Toast.makeText(this, jsonObject.optString("message"), Toast.LENGTH_LONG).show();
                            }
                        }else {
                            Toast.makeText(this, "Server Not Working", Toast.LENGTH_LONG).show();
                        }
//                        nCase = 1;
//                        JSONObject requestObj = new JSONObject();
//                        try {
//                            requestObj.put("date", jsonObject.getString(""));
//                            requestObj.put("userid", jsonObject.getString(""));
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
                    } else{
                        jsonObject = jsonObject.getJSONObject("ETY_INPUT_GET_FEEDER_DETAILS");
                        if(jsonObject != null && jsonObject.length() > 0){
                            if (jsonObject.optString("success").equalsIgnoreCase("True")) {
                                Toast.makeText(this, jsonObject.optString("message"), Toast.LENGTH_LONG).show();

                                JSONObject requestObj = new JSONObject();
                                try {
                                    requestObj.put("date", jsonObject.getString("date"));
                                    requestObj.put("userid", jsonObject.getString("userid"));
                                    nCase = 2;
                                    new ApiServiceTask(this, this, "2", "https://apcpcdcl-test-k5qoqm5y.it-cpi012-rt.cfapps.ap21.hana.ondemand.com/http/ConsumerApp/SAPISU/GetAGLFeederReadings/DEV").execute(requestObj.toString());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }else{
                                Toast.makeText(this, jsonObject.optString("message"), Toast.LENGTH_LONG).show();
                            }
                        }else {
                            Toast.makeText(this, "Server Not Working", Toast.LENGTH_LONG).show();
                        }
                    }

                }
            }else{
                Toast.makeText(this, "Server Not Working", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}