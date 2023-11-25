package com.apcpdcl.departmentapp.activities;

import android.os.Bundle;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.apcpdcl.departmentapp.R;
import com.apcpdcl.departmentapp.fragments.EROFragment;
import com.apcpdcl.departmentapp.models.ExceptionalModel;
import com.apcpdcl.departmentapp.utils.Utility;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ExceptionReportListActivity extends AppCompatActivity {

    @BindView(R.id.main_content)
    FrameLayout main_content;
    @BindView(R.id.toolbar_title)
    TextView toolbar_title;
    private ExceptionalModel exceptionalModel;
    private String mType ="";
    private boolean mShow ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exception_report_list);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        toolbar_title.setText("Exceptional Report");
        exceptionalModel = (ExceptionalModel) getIntent().getSerializableExtra(ExceptionReportFormActivity.class.getSimpleName());
        mType =  getIntent().getStringExtra("Type");
        mShow =  getIntent().getBooleanExtra("Show", false);
        if (exceptionalModel != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(ExceptionalModel.class.getSimpleName(), exceptionalModel);
            bundle.putString("Type", mType);
            bundle.putBoolean("Show", mShow);
            Utility.navigateFragment(new EROFragment(), EROFragment.TAG, bundle, this);
        }
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
                if (backStackEntry.getName().equalsIgnoreCase(EROFragment.TAG)) {
                        finish();
                }else {
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
