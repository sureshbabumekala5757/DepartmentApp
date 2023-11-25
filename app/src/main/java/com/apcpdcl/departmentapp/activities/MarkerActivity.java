package com.apcpdcl.departmentapp.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.TextView;

import com.apcpdcl.departmentapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MarkerActivity extends AppCompatActivity {

    @BindView(R.id.txt_latlang)
    TextView txt_latlang;

    String strtxt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_marker);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        Bundle bd = intent.getExtras();
        if (bd != null) {
            strtxt = (String) bd.get("markertitle");
            txt_latlang.setText(strtxt);
        }
    }
}
