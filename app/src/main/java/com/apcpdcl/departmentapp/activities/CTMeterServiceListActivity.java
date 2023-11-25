package com.apcpdcl.departmentapp.activities;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.apcpdcl.departmentapp.R;
import com.apcpdcl.departmentapp.adapters.CTMeterServiceListAdapter;
import com.apcpdcl.departmentapp.interfaces.IUpdateList;
import com.apcpdcl.departmentapp.model.CTMServiceModel;
import com.apcpdcl.departmentapp.utils.Constants;
import com.apcpdcl.departmentapp.utils.Utility;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import butterknife.ButterKnife;

public class CTMeterServiceListActivity extends AppCompatActivity implements IUpdateList {
    ListView lv_service_list;
    EditText et_search;
    TextView tv_no_data;
    ImageView iv_search;
    RelativeLayout rl_search;
    private String sectionCode;
    public ProgressDialog pDialog;

    private CTMeterServiceListAdapter ctmeterServiceListAdapter;
    private ArrayList<CTMServiceModel> ctmServiceList = new ArrayList<CTMServiceModel>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ctmeters_service_list_activity);

        lv_service_list = findViewById(R.id.lv_service_list);
        et_search = findViewById(R.id.et_search);
        tv_no_data = findViewById(R.id.tv_no_data);
        iv_search = findViewById(R.id.iv_search);
        rl_search = findViewById(R.id.rl_search);

        SharedPreferences prefs = getSharedPreferences("loginPrefs", 0);
        sectionCode = prefs.getString("Section_Code", "");
        ButterKnife.bind(this);
        pDialog = new ProgressDialog(CTMeterServiceListActivity.this);
        if (Utility.isNetworkAvailable(this)) {
//            pDialog.show();
//            pDialog.setMessage("Please wait...");
//            pDialog.setCancelable(false);
            getCTMServiceList(sectionCode);
        } else {
            Utility.showCustomOKOnlyDialog(this,
                    Utility.getResourcesString(this,
                            R.string.no_internet));
        }
    }
    /**************************************************************************************
     * Get CTMeters Service List
     **************************************************************************************/
    private void getCTMServiceList(String sectionCode) {

//        try
//        {
//            JSONArray arrData=new JSONArray();
//
//            arrData.put(new JSONObject().put("ctmServiceNo", "6425205141376"));
//            arrData.put(new JSONObject().put("ctmServiceNo", "6425205141377"));
//            arrData.put(new JSONObject().put("ctmServiceNo", "6425205141378"));
//            for(int i=0;i<arrData.length();i++){
//                JSONObject jsonObject = arrData.getJSONObject(i);
//                CTMServiceModel ctmServiceModel = new Gson().fromJson(jsonObject.toString(),
//                        CTMServiceModel.class);
//                ctmServiceList.add(ctmServiceModel);
//            }
//            setListData();
//        }
//        catch (IllegalStateException | JsonSyntaxException | JSONException exception) {
//            exception.printStackTrace();
//        }
        JSONObject obj = new JSONObject();
        try{
            obj.put("seccd",sectionCode);
        }catch (IllegalStateException | JsonSyntaxException | JSONException exception) {
            exception.printStackTrace();
        }

        HttpEntity entity;
        try {
            entity = new StringEntity(obj.toString());
            pDialog.show();
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            AsyncHttpClient client = new AsyncHttpClient();
            BasicHeader[] headers = new BasicHeader[]{new BasicHeader("Authorization", "Bearer ybgasdf54bYHFr2347yh786%$#NHS&ghgapcpdcl*")};
            client.post(this,Constants.CTMETERS_URL+ "CTMeterSRList/",headers, entity, "application/json", new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(String response) {
                    Utility.showLog("onSuccess", response);
                    if (pDialog != null && pDialog.isShowing()) {
                        pDialog.dismiss();
                    }
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        if(jsonArray != null && jsonArray.length() > 0 ){
                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                CTMServiceModel ctmServiceModel = new Gson().fromJson(jsonObject.toString(),
                                        CTMServiceModel.class);
                                ctmServiceList.add(ctmServiceModel);
                            }
                            setListData();
                        }else{
                            Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Throwable error, String content) {
                    Utility.showLog("error", error.toString());
                    if (pDialog != null && pDialog.isShowing()) {
                        pDialog.dismiss();
                    }
                    Utility.showCustomOKOnlyDialog(CTMeterServiceListActivity.this, error.getMessage());
                }
            });
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
    }
    private void setListData() {
        ctmeterServiceListAdapter = new CTMeterServiceListAdapter(this, ctmServiceList);
        lv_service_list.setAdapter(ctmeterServiceListAdapter);
        implementsSearch();
    }



    //IMPLEMENT SEARCH
    private void implementsSearch() {
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (ctmeterServiceListAdapter != null)
                    ctmeterServiceListAdapter.getFilter().filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }


    @Override
    public void updateList(String id) {
        for (int i = 0; i < ctmServiceList.size(); i++) {
            if (id.equalsIgnoreCase(ctmServiceList.get(i).getCUSCNO())) {
                ctmServiceList.remove(i);
                if (ctmServiceList.size() > 0) {
                    ctmeterServiceListAdapter.notifyDataSetChanged();
                }else {
                    tv_no_data.setVisibility(View.VISIBLE);
                    lv_service_list.setVisibility(View.GONE);
                }
                break;
            }
        }
    }
}
