package com.apcpdcl.departmentapp.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.apcpdcl.departmentapp.R;
import com.apcpdcl.departmentapp.fragments.ADELmDcNonSlabFragment;
import com.apcpdcl.departmentapp.models.ExceptionalModel;
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

public class ADEDcListTrackingActivity extends AppCompatActivity {

    @BindView(R.id.main_content)
    FrameLayout main_content;
    @BindView(R.id.toolbar_title)
    TextView toolbar_title;
    private ExceptionalModel exceptionalModel;
    private String userName = "";
    public ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exception_report_list);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        toolbar_title.setText("DC-LIST TRACKING");
        userName = getIntent().getStringExtra(Constants.USER_ID);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        if (Utility.isNetworkAvailable(this)) {
            pDialog.show();
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            getLMDCAbstract();
        } else {
            Utility.showCustomOKOnlyDialog(this,
                    Utility.getResourcesString(this,
                            R.string.no_internet));
        }
    }

    private void getLMDCAbstract() {
        AsyncHttpClient client = new AsyncHttpClient();
        BasicHeader[] headers = new BasicHeader[]{new BasicHeader("Authorization", "Token " + userName)};
        client.setTimeout(50000);
        client.get(this, Constants.ADE_COUNT_NON_SLAB, headers, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                Utility.showLog("onSuccess", response);
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss();
                }
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    ArrayList<LmDcListModel> lmDcListModels = new ArrayList<>();
                    if (jsonArray.length() > 0) {
                        for (int j = 0; j < jsonArray.length(); j++) {
                            LmDcListModel lmDcListModel = new Gson().fromJson(jsonArray.get(j).toString(),
                                    LmDcListModel.class);
                            lmDcListModel.setNS(true);
                            lmDcListModels.add(lmDcListModel);
                        }
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(LmDcListModel.class.getSimpleName(), lmDcListModels);
                        Utility.navigateFragment(new ADELmDcNonSlabFragment(), ADELmDcNonSlabFragment.TAG, bundle, ADEDcListTrackingActivity.this);

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

    /* *
     *Handle back button
     * */
    public void onBackPressed() {
        try {
            FragmentManager fragmentManager = getSupportFragmentManager();
            if (fragmentManager.getBackStackEntryCount() > 0) {
                FragmentManager.BackStackEntry backStackEntry = fragmentManager
                        .getBackStackEntryAt(fragmentManager
                                .getBackStackEntryCount() - 1);
                Utility.showLog("BackStackEntry Name", backStackEntry.getName());
                if (backStackEntry.getName().equalsIgnoreCase(ADELmDcNonSlabFragment.TAG)) {
                    finish();
                } else {
                    super.onBackPressed();
                }
            } else {
                super.onBackPressed();
            }
        } catch (Exception e) {
            e.printStackTrace();
            getFragmentManager().popBackStack();
        }
    }
}
