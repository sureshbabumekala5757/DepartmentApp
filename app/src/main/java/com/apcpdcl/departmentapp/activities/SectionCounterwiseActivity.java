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
import com.apcpdcl.departmentapp.adapters.CounterSectionListAdapter;
import com.apcpdcl.departmentapp.models.CounterModel;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SectionCounterwiseActivity extends AppCompatActivity {

    String strObj, str_divName,str_Section;
    TextView subdivision_name_txt;
    CounterModel counterModel;
    private ArrayList<CounterModel> sectionModels;
    JSONArray sectionJsonArray;
    TableLayout counter_sec_tableHeadLayout;
    RecyclerView counter_sec_recyclerview;
    RecyclerView.LayoutManager recylerViewLayoutManager;
    CounterSectionListAdapter secListAdapter;
    JSONObject obj,secObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.section_counterwise);
        counter_sec_recyclerview = (RecyclerView) findViewById(R.id.counter_sec_recyclerview);

        recylerViewLayoutManager = new LinearLayoutManager(SectionCounterwiseActivity.this);
        counter_sec_recyclerview.setLayoutManager(recylerViewLayoutManager);

        counter_sec_tableHeadLayout = (TableLayout) findViewById(R.id.counter_sec_tableHeadLayout);
        subdivision_name_txt=(TextView) findViewById(R.id.counter_subdivision_name_txt);

        sectionModels = new ArrayList<>();


        Intent intent = getIntent();
        Bundle bd = intent.getExtras();
        if (bd != null) {
            strObj = (String) bd.get("Object");
            str_divName = (String) bd.get("Counter_SubDivision");
            try {
                obj = new JSONObject(strObj);
                str_Section=obj.getString("SECTION");
                secObj = new JSONObject(str_Section);
                sectionJsonArray = (JSONArray) secObj.get(str_divName);

                for (int i = 0; i < sectionJsonArray.length(); i++) {
                    JSONObject json = sectionJsonArray.getJSONObject(i);
                    counterModel = new Gson().fromJson(json.toString(), CounterModel.class);
                    sectionModels.add(counterModel);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (sectionModels.size() > 0) {
                counter_sec_recyclerview.setVisibility(View.VISIBLE);
                counter_sec_tableHeadLayout.setVisibility(View.VISIBLE);
                subdivision_name_txt.setText(str_divName);
                secListAdapter = new CounterSectionListAdapter(getApplicationContext(), sectionModels);
                counter_sec_recyclerview.setAdapter(secListAdapter);
            } else {

            }


        }
    }
}

