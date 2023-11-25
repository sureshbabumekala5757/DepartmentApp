package com.apcpdcl.departmentapp.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;

import com.apcpdcl.departmentapp.R;
import com.apcpdcl.departmentapp.adapters.OfflineMeterChangeListAdapter;
import com.apcpdcl.departmentapp.sqlite.MeterChangeDatabaseHandler;
import com.apcpdcl.departmentapp.utils.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Haseena
 * on 01-03-2018.
 */

public class OfflineMeterChangeListActivity extends AppCompatActivity {

    @BindView(R.id.lv_offline)
    ListView lv_offline;
    @BindView(R.id.btn_new_request)
    Button btn_new_request;
    private MeterChangeDatabaseHandler databaseHandler;
    public static final String TAG = OfflineMeterChangeListActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.offline_meter_change_list_layout);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        ButterKnife.bind(this);
        databaseHandler = new MeterChangeDatabaseHandler(this);
       // setOfflineData();
    }

    private void setOfflineData() {
        OfflineMeterChangeListAdapter meterChangeListAdapter = new OfflineMeterChangeListAdapter(this, databaseHandler.getAllMeterChangeDetails());
        lv_offline.setAdapter(meterChangeListAdapter);
    }

    @OnClick(R.id.btn_new_request)
    void navigateToOffline() {
        Intent intent = new Intent(this, OfflineMeterChangeRequestFormActivity.class);
        intent.putExtra(Constants.FROM, "NEW_REQUEST");
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setOfflineData();
    }
}
