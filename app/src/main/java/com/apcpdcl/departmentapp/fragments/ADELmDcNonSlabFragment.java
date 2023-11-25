package com.apcpdcl.departmentapp.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.apcpdcl.departmentapp.R;
import com.apcpdcl.departmentapp.activities.ADEDcListTrackingActivity;
import com.apcpdcl.departmentapp.adapters.LMDCListNonSlabAdapter;
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

import butterknife.ButterKnife;

public class ADELmDcNonSlabFragment extends Fragment {
    public static String TAG = ADELmDcNonSlabFragment.class.getSimpleName();
    private View rootView;
    private ADEDcListTrackingActivity mParent;
    private ArrayList<LmDcListModel> lmDcListModels = new ArrayList<>();
    private ListView lv_lmdc;
    private TextView tv_no_data;
    private LinearLayout ll_three;
    private String from = "";
    public ProgressDialog pDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mParent = (ADEDcListTrackingActivity) getActivity();
        if (getArguments().containsKey(LmDcListModel.class.getSimpleName())) {
            lmDcListModels = (ArrayList<LmDcListModel>) getArguments().getSerializable(LmDcListModel.class.getSimpleName());
            from = getArguments().getString(Constants.FROM);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView != null)
            return rootView;
        rootView = inflater.inflate(R.layout.fragment_lmdc_list, container, false);
        ButterKnife.bind(this, rootView);
        init();
        return rootView;
    }

    private void init() {
        lv_lmdc = (ListView) rootView.findViewById(R.id.lv_lmdc);
        tv_no_data = (TextView) rootView.findViewById(R.id.tv_no_data);
        ll_three = (LinearLayout) rootView.findViewById(R.id.ll_three);
        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);
        if (lmDcListModels.size() > 0) {
            LMDCListNonSlabAdapter eroExceptionsListAdapter = new LMDCListNonSlabAdapter(mParent, lmDcListModels);
            lv_lmdc.setAdapter(eroExceptionsListAdapter);
        } else {
            lv_lmdc.setVisibility(View.GONE);
            ll_three.setVisibility(View.GONE);
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             tv_no_data.setVisibility(View.VISIBLE);
        }
        lv_lmdc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (Utility.isNetworkAvailable(mParent)) {
                    pDialog.show();
                    pDialog.setMessage("Please wait...");
                    pDialog.setCancelable(false);
                    getLMDCAbstract(lmDcListModels.get(position).getUSERCODE());
                } else {
                    Utility.showCustomOKOnlyDialog(mParent,
                            Utility.getResourcesString(mParent,
                                    R.string.no_internet));
                }
            }
        });
    }

    private void getLMDCAbstract(String userName) {
        AsyncHttpClient client = new AsyncHttpClient();
        BasicHeader[] headers = new BasicHeader[]{new BasicHeader("Authorization", "Token " + userName)};
        // BasicHeader[] headers = new BasicHeader[]{new BasicHeader("Authorization", "Token AEOPN32212")};
        client.setTimeout(50000);
        client.get(mParent, Constants.AE_COUNT_NON_SLAB, headers, null, new AsyncHttpResponseHandler() {
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
                            lmDcListModels.add(lmDcListModel);
                        }
                        if (lmDcListModels.size() > 0) {
                            Bundle bundle = new Bundle();
                            bundle.putSerializable(LmDcListModel.class.getSimpleName(), lmDcListModels);
                            Utility.navigateFragment(new AELmDcNonSlabFragment(), AELmDcNonSlabFragment.TAG, bundle, mParent);

                        }
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
