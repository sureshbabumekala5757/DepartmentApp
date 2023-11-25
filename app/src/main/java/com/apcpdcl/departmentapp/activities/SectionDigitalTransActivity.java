package com.apcpdcl.departmentapp.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;

import com.apcpdcl.departmentapp.R;
import com.apcpdcl.departmentapp.adapters.SectionListAdapter;
import com.apcpdcl.departmentapp.models.DigitalModel;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SectionDigitalTransActivity extends AppCompatActivity {

    private ArrayList<DigitalModel> sectionModels, filter_Section_Models;
    SectionListAdapter secListAdapter;
    RecyclerView sec_recyclerView;
    TextView subdivision_name_txt;
    TableLayout sec_tableHeadLayout;
    String strObj, str_divName;
    DigitalModel digitalModel;
    RecyclerView.LayoutManager recylerViewLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.section_digitaltransaction);
        sectionModels = new ArrayList<>();
        filter_Section_Models = new ArrayList<>();
        sec_recyclerView = (RecyclerView) findViewById(R.id.sec_recyclerview);

        recylerViewLayoutManager = new LinearLayoutManager(SectionDigitalTransActivity.this);
        sec_recyclerView.setLayoutManager(recylerViewLayoutManager);

        sec_tableHeadLayout = (TableLayout) findViewById(R.id.sec_tableHeadLayout);
        subdivision_name_txt=(TextView) findViewById(R.id.subdivision_name_txt);

        Intent intent = getIntent();
        Bundle bd = intent.getExtras();
        if (bd != null) {
            strObj = (String) bd.get("Object");
            str_divName = (String) bd.get("SubDivision");
            try {
                JSONObject obj = new JSONObject(strObj);
                JSONArray sectionJsonArray = (JSONArray) obj.get("SECTION");

                for (int i = 0; i < sectionJsonArray.length(); i++) {
                    JSONObject json = sectionJsonArray.getJSONObject(i);
                    digitalModel = new Gson().fromJson(json.toString(), DigitalModel.class);
                    sectionModels.add(digitalModel);
                }

                /*Filteration of Sections*/
                for (DigitalModel digitalModel : sectionModels) {
                    if (digitalModel.getDIV_Name().equalsIgnoreCase(str_divName)) {
                        filter_Section_Models.add(digitalModel);
                    }
                }

                if (filter_Section_Models.size() > 0) {
                    sec_recyclerView.setVisibility(View.VISIBLE);
                    sec_tableHeadLayout.setVisibility(View.VISIBLE);
                    subdivision_name_txt.setText(str_divName);
                    secListAdapter = new SectionListAdapter(getApplicationContext(), filter_Section_Models);
                    sec_recyclerView.setAdapter(secListAdapter);
                } else {

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
