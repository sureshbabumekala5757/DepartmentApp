package com.apcpdcl.departmentapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.apcpdcl.departmentapp.R;
import com.apcpdcl.departmentapp.model.AchieversModel;
import com.apcpdcl.departmentapp.shared.AppPrefs;
import com.apcpdcl.departmentapp.utils.Constants;
import com.apcpdcl.departmentapp.utils.Utility;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void onResume() {
        super.onResume();
        int SPLASH_DISPLAY_LENGTH = 3000;
        new Handler().postDelayed(new Runnable() {
            public void run() {
                navigateToMainActivity();
            }
        }, SPLASH_DISPLAY_LENGTH);
  /*      if (Utility.isNetworkAvailable(this)) {
            getAchievers();
        } else {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    navigateToMainActivity();
                }
            }, SPLASH_DISPLAY_LENGTH);
        }*/
    }

    private void getAchievers() {
        AsyncHttpClient client = new AsyncHttpClient();
        Utility.showLog("Url", Constants.URL + "achiever/info/");
        client.setTimeout(50000);
        client.post(Constants.URL + "achiever/info/", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                Utility.showLog("onSuccess", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.has("STATUS") && jsonObject.optString("STATUS").equalsIgnoreCase("SUCCESS")) {
                        String[] categories = jsonObject.optString("CATEGORY").replace("||", "@").split("@");
                        String[] content = jsonObject.optString("CONTENT").replace("||", "@").split("@");
                        String[] image = jsonObject.optString("IMAGES").replace("||", "@").split("@");
                        ArrayList<AchieversModel> achieversModels = new ArrayList<>();
                        for (int i = 0; i < categories.length; i++) {
                            AchieversModel achieversModel = new AchieversModel();
                            achieversModel.setCATEGORY(categories[i]);
                            ArrayList<AchieversModel> achieversModelArrayList = new ArrayList<>();
                            String[] contents = content[i].split("\\|");
                            String[] images = image[i].split("\\|");

                            for (int j = 0; j < contents.length; j++) {
                                AchieversModel model = new AchieversModel();
                                model.setCONTENT(contents[j]);
                                model.setIMAGES(Constants.URL_IMAGES + images[j]);
                                achieversModelArrayList.add(model);
                                achieversModel.setAchieversModels(achieversModelArrayList);
                            }
                            achieversModels.add(achieversModel);
                        }

                        if (achieversModels.size() > 0) {
                            finish();
                            Intent mainIntent = new Intent(getApplicationContext(), AchieversActivity.class);
                            mainIntent.putExtra(AchieversModel.class.getName(), achieversModels);
                            startActivity(mainIntent);
                        } else {
                            navigateToMainActivity();
                        }
                    } else {
                        navigateToMainActivity();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    navigateToMainActivity();
                }
            }

            @Override
            public void onFailure(int statusCode, Throwable error, String content) {
                Utility.showLog("error", error.toString());
                navigateToMainActivity();
            }
        });
    }
    //Navigate to Main Activity
    private void navigateToMainActivity() {
        if(AppPrefs.getInstance(getApplicationContext()).getString("FIRST_LOGIN","").equalsIgnoreCase("TRUE") &&
                AppPrefs.getInstance(getApplicationContext()).getString("FIRST_LOGIN","") != null){
            finish();
            Intent dbIntent = new Intent(getApplicationContext(), DashboardActivity.class);
            startActivity(dbIntent);
        }else {
            finish();
            Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(mainIntent);
        }
    }


}

