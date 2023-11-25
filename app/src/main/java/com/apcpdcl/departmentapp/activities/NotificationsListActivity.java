package com.apcpdcl.departmentapp.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;

import com.apcpdcl.departmentapp.R;
import com.apcpdcl.departmentapp.adapters.RegistrationListAdapter;
import com.apcpdcl.departmentapp.models.NotificationModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Haseen
 * on 24-03-2018.
 */

public class NotificationsListActivity extends AppCompatActivity {
    @BindView(R.id.lv_notifications)
    ListView lv_notifications;
    @BindView(R.id.tv_no_data)
    TextView tv_no_data;
    private ArrayList<NotificationModel> notificationModels;
    private RegistrationListAdapter registrationListAdapter;
    public ProgressDialog pDialog;
    public Toolbar mToolbar;
    private String sectionCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_list);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        ButterKnife.bind(this);
        init();
    }

    /*Initialize Views*/
    private void init() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setListData();
    }

    /*
     *Set Service List DAta to ListView
     * */
    private void setListData() {
       /*  registrationListAdapter = new RegistrationListAdapter(this, notificationModels);
        lv_reg.setAdapter(registrationListAdapter);*/
    }

}
