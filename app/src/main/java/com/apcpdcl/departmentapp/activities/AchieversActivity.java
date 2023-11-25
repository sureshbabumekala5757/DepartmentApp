package com.apcpdcl.departmentapp.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;

import com.apcpdcl.departmentapp.R;
import com.apcpdcl.departmentapp.adapters.AchieversAdapter;
import com.apcpdcl.departmentapp.model.AchieversModel;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ViewListener;

import java.util.ArrayList;

import butterknife.ButterKnife;

public class AchieversActivity extends AppCompatActivity {

    /*    private ImageView iv_achievers;
        private ProgressBar homeprogress;
        private String image ="";
        private Handler handler;
        private Runnable runnable;*/
    private CarouselView carouselView;
    private TextView tv_next;
    private TextView tv_header_name;
    private ListView lv_achievers;
    private ArrayList<AchieversModel> achieversModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acheivers_activity);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        achieversModels = (ArrayList<AchieversModel>) getIntent().getSerializableExtra(AchieversModel.class.getName());
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        tv_next = (TextView) findViewById(R.id.tv_next);
        tv_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToMainActivity();
            }
        });
        carouselView = (CarouselView) findViewById(R.id.carouselView);
        carouselView.setPageCount(achieversModels.size());

        carouselView.setViewListener(viewListener);
        carouselView.setSlideInterval(5000);
    }

    ViewListener viewListener = new ViewListener() {

        @Override
        public View setViewForPosition(int position) {
            View customView = getLayoutInflater().inflate(R.layout.acheivers_pager_item, null);
            //set view attributes here
            tv_header_name = (TextView) customView.findViewById(R.id.tv_header_name);
            tv_header_name.setText("Best Performers for "+achieversModels.get(position).getCATEGORY());
            lv_achievers = (ListView) customView.findViewById(R.id.lv_achievers);
            AchieversAdapter achieversAdapter = new AchieversAdapter(AchieversActivity.this, achieversModels.get(position).getAchieversModels());
            lv_achievers.setAdapter(achieversAdapter);

            return customView;
        }
    };

    /*  private void init() {
          tv_next = (TextView) findViewById(R.id.tv_next);
          tv_next.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  navigateToMainActivity();
              }
          });
          handler = new Handler();
           runnable = new Runnable() {
              @Override
              public void run() {
                  navigateToMainActivity();

              }
          };
          iv_achievers = (ImageView) findViewById(R.id.iv_achievers);
          homeprogress = (ProgressBar) findViewById(R.id.homeprogress);
          Picasso.with(this).load(image)
                  .placeholder(R.drawable.user_pic_place_holder)
                  .into(iv_achievers, new Callback() {
                      @Override
                      public void onSuccess() {
                          homeprogress.setVisibility(View.GONE);
                          handler.postDelayed(runnable, 10000);
                      }

                      @Override
                      public void onError() {

                      }
                  });
      }



      @Override
      protected void onDestroy() {
          super.onDestroy();
          handler.removeCallbacks(runnable);
      }*/
    private void navigateToMainActivity() {
        finish();
        Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(mainIntent);
    }
}
