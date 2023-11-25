package com.apcpdcl.departmentapp.activities;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.view.WindowManager;

import com.apcpdcl.departmentapp.R;
import com.apcpdcl.departmentapp.adapters.ViewPagerAdapter;
import com.apcpdcl.departmentapp.models.LmDcListModel;
import com.apcpdcl.departmentapp.utils.Constants;
import com.apcpdcl.departmentapp.utils.Utility;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.message.BasicHeader;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AAODcListTrackingActivity extends AppCompatActivity {

    public ProgressDialog pDialog;
    private String userName;
    private ArrayList<LmDcListModel> lmDcListModelsSlab;
    private ArrayList<LmDcListModel> lmDcListModelsNonSlab;

    @BindView(R.id.vp_type)
    ViewPager vp_type;

    @BindView(R.id.tl_type)
    TabLayout tl_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ae_dclist_tracking_activity);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        ButterKnife.bind(this);
        init();
    }

    private void init() {

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        SharedPreferences lprefs = getSharedPreferences("loginPrefs", 0);
        userName = lprefs.getString("UserName", "");
        //Adding the tabs using addTab() method
        tl_type.addTab(tl_type.newTab().setText("Slab"));
        tl_type.addTab(tl_type.newTab().setText("Non Slab"));
        tl_type.setTabGravity(TabLayout.GRAVITY_FILL);


        lmDcListModelsSlab = new ArrayList<>();
        lmDcListModelsNonSlab = new ArrayList<>();
        if (Utility.isNetworkAvailable(this)) {
            pDialog.show();
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            getLMDCAbstract(Constants.AAO_COUNT_SLAB, false);
        } else {
            Utility.showCustomOKOnlyDialog(this,
                    Utility.getResourcesString(this,
                            R.string.no_internet));
        }
    }

    private void getLMDCAbstract(String url, final boolean isNS) {
        AsyncHttpClient client = new AsyncHttpClient();
         BasicHeader[] headers = new BasicHeader[]{new BasicHeader("Authorization", "Token " + userName)};
        client.setTimeout(50000);
        client.get(this, url, headers, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                Utility.showLog("onSuccess", response);
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    ArrayList<LmDcListModel> lmDcListModels = new ArrayList<>();
                    if (jsonArray.length() > 0) {
                        for (int j = 0; j < jsonArray.length(); j++) {
                            LmDcListModel lmDcListModel = new Gson().fromJson(jsonArray.get(j).toString(),
                                    LmDcListModel.class);
                            lmDcListModel.setNS(isNS);
                            lmDcListModels.add(lmDcListModel);
                        }
                        if (isNS) {
                            if (pDialog != null && pDialog.isShowing()) {
                                pDialog.dismiss();
                            }
                            lmDcListModelsNonSlab = lmDcListModels;
                            //Creating our pager adapter
                            ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(),
                                    tl_type.getTabCount(), lmDcListModelsSlab, lmDcListModelsNonSlab,
                                    AAODcListTrackingActivity.class.getSimpleName());

                            //Adding adapter to pager
                            vp_type.setAdapter(adapter);
                            tl_type.setupWithViewPager(vp_type);
                        } else {
                            lmDcListModelsSlab = lmDcListModels;
                            getLMDCAbstract(Constants.AAO_COUNT_NON_SLAB, true);
                        }
                    }else if (isNS){
                        if (pDialog != null && pDialog.isShowing()) {
                            pDialog.dismiss();
                        }
                        lmDcListModelsNonSlab = lmDcListModels;
                        //Creating our pager adapter
                        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(),
                                tl_type.getTabCount(), lmDcListModelsSlab, lmDcListModelsNonSlab,
                                AAODcListTrackingActivity.class.getSimpleName());

                        //Adding adapter to pager
                        vp_type.setAdapter(adapter);
                        tl_type.setupWithViewPager(vp_type);
                    } else {
                        lmDcListModelsSlab = lmDcListModels;
                        getLMDCAbstract(Constants.AAO_COUNT_NON_SLAB, true);
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
            }
        });
    }
}
