package com.apcpdcl.departmentapp.utils;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import com.apcpdcl.departmentapp.BuildConfig;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.apcpdcl.departmentapp.R;
import com.apcpdcl.departmentapp.activities.MainActivity;
import com.apcpdcl.departmentapp.customviews.ExpandableLayoutListener;
import com.apcpdcl.departmentapp.customviews.ExpandableRelativeLayout;
import com.apcpdcl.departmentapp.customviews.Utils;
import com.apcpdcl.departmentapp.shared.AppPrefs;
import com.apcpdcl.departmentapp.sqlite.DatabaseHandler;
import com.apcpdcl.departmentapp.sqlite.MatsDatabaseHandler;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by haseen
 * on 5/12/17.
 */

public class Utility {

    public static SimpleDateFormat existingUTCFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);

    /**
     * Checks the Internet connection.
     *
     * @param context Used to get the connectivity service of the APP
     */
    public static boolean isNetworkAvailable(Context context) {
        try {
            ConnectivityManager connMgr = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            assert connMgr != null;
            return connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED ||
                    connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTING ||
                    connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                    connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTING;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isLocationEnabled(Context context) {
        int locationMode;
        String locationProviders;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }

            return locationMode != Settings.Secure.LOCATION_MODE_OFF;

        } else {
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }


    }

    /**
     * Check the value is null or empty
     *
     * @param value Value of that string
     * @return Boolean returns the value true or false
     */
    public static boolean isValueNullOrEmpty(String value) {
        boolean isValue = false;
        if (value == null || value.equals("") || value.equals("0.0")
                || value.equals("null") || value.trim().length() == 0) {
            isValue = true;
        }
        return isValue;
    }

    /**
     * HIDE THE KEYBOARD FOR FRAGMENT
     **/

    public static void hideSoftKeyboard(Activity activity, View view) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
    }

    /**
     * To Print logcat this method is used.
     *
     * @param logMsg Purpose of the log
     * @param logVal What you want to print
     */
    public static void showLog(String logMsg, String logVal) {
        try {
            if (Constants.logMessageOnOrOff) {
                if (!isValueNullOrEmpty(logMsg) && !isValueNullOrEmpty(logVal)) {
                    Log.e(logMsg, logVal);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Shows toast message
     *
     * @param context Context of the class
     * @param message What message you have to show
     */
    public static void showToastMessage(Context context, String message) {
        try {
            if (!isValueNullOrEmpty(message) && context != null) {
                final Toast toast = Toast.makeText(
                        context.getApplicationContext(), message,
                        Toast.LENGTH_LONG);
                toast.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * TO CHECK IS IT BELOW MARSHMALLOW OR NOT
     */
    public static boolean isMarshmallowOS() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    /**
     * GET Resources String
     *
     * @param context Context of the class
     * @param id      Id of the resource
     * @return String
     */
    public static String getResourcesString(Context context, int id) {
        String value = null;
        if (context != null && id != -1) {
            value = context.getResources().getString(id);
        }
        return value;
    }


    /**
     * Sharedpreference method to set and get string variable
     */
    public static void setSharedPrefStringData(Context context, String key, String value) {
        try {
            if (context != null) {
                SharedPreferences appInstallInfoSharedPref = context.getSharedPreferences(Constants.APP_PREF,
                        Context.MODE_PRIVATE);
                SharedPreferences.Editor appInstallInfoEditor = appInstallInfoSharedPref.edit();
                appInstallInfoEditor.putString(key, value);
                appInstallInfoEditor.apply();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * GET SHARED PREFERENCES STRING DATA
     */
    public static String getSharedPrefStringData(Context context, String key) {

        try {
            SharedPreferences userAcountPreference = context
                    .getSharedPreferences(Constants.APP_PREF,
                            Context.MODE_PRIVATE);
            return userAcountPreference.getString(key, "");
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return "";

    }

    /*
     *Get Month Name from number
     */

    public static String getMonthFormat(int monthNumber) {
        String mMonthsArray[] = {
                "",
                "Jan",
                "Feb",
                "Mar",
                "Apr",
                "May",
                "Jun",
                "Jul",
                "Aug",
                "Sep",
                "Oct",
                "Nov",
                "Dec"
        };
        return mMonthsArray[monthNumber];
    }

    public static void showCustomOKOnlyDialog(Context context, String message) {
        if (!Constants.isDialogOpen) {
            Constants.isDialogOpen = true;
            final Dialog dialog_confirm = new Dialog(context);
            dialog_confirm.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog_confirm.setContentView(R.layout.dialog_ok);
            WindowManager.LayoutParams lp1 = new WindowManager.LayoutParams();
            Window window1 = dialog_confirm.getWindow();
            if (window1 != null)
                lp1.copyFrom(window1.getAttributes());
            lp1.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp1.height = WindowManager.LayoutParams.WRAP_CONTENT;
            assert window1 != null;
            window1.setAttributes(lp1);
            Button btn_ok = (Button) dialog_confirm.findViewById(R.id.btn_ok);

            TextView txt_msz = (TextView) dialog_confirm.findViewById(R.id.txt_heading);
            txt_msz.setText(message);
            btn_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog_confirm.dismiss();
                }
            });
            dialog_confirm.show();
            dialog_confirm.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    Constants.isDialogOpen = false;
                }
            });
        }
    }

    /**
     * Open a web page of a specified URL
     *
     * @param url URL to open
     */
    public static void openWebPage(Context context, String url) {
        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        }

        File file = new File(Environment.getExternalStorageDirectory().toString() + "/" + Environment.DIRECTORY_DOWNLOADS + "/department-app-" + BuildConfig.VERSION_NAME + ".apk");
        if (file.exists()) {
            file.delete();
        }
    }

    public static ArrayList<String> getMeterMakeList(Context context) {
        String meterMakeList = getSharedPrefStringData(context, Constants.GET_METER_MAKE_LIST);
        String[] strings = meterMakeList.split("\\|");
        ArrayList<String> meterMake = new ArrayList<String>();
        for (String string : strings) {
            if (string.equalsIgnoreCase("lnt") || string.equalsIgnoreCase("lng")) {
                string = string.replace("n", "&");
            }
            meterMake.add(string);
        }
        meterMake.add(0, "Select");
        return meterMake;
    }

    //    public static void getMeterMake(final Context context) {
//        if (isNetworkAvailable(context)) {
//            AsyncHttpClient client = new AsyncHttpClient();
//            client.get(Constants.GET_METER_MAKE_LIST, new AsyncHttpResponseHandler() {
//                @Override
//                public void onSuccess(String response) {
//                    setSharedPrefStringData(context, Constants.GET_METER_MAKE_LIST, response);
//                }
//
//                @Override
//                public void onFailure(int statusCode, Throwable error, String content) {
//                    showLog("error", error.toString());
//                }
//            });
//        }
//    }
    public static void getMeterMake(final Context context) {
        if (isNetworkAvailable(context)) {
            AsyncHttpClient client = new AsyncHttpClient();
            client.post(Constants.GET_METER_MAKE_LIST, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(String response) {
                    JSONObject obj = null;
                    String resStr = "";
                    try {
                        obj = new JSONObject(response);
                        if (obj != null && obj.length() > 0) {
                            JSONArray makeArr = obj.getJSONArray("data");

                            if (makeArr != null) {

                                //Iterating JSON array
                                for (int i = 0; i < makeArr.length(); i++) {
                                    resStr += makeArr.get(i).toString() + "|";
                                }
                            }


                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    setSharedPrefStringData(context, Constants.GET_METER_MAKE_LIST, resStr);
                }

                @Override
                public void onFailure(int statusCode, Throwable error, String content) {
                    showLog("error", error.toString());
                }
            });
        }
    }

    /**** Method for Setting the Height of the ListView dynamically.
     **** Hack to fix the issue of not showing all the items of the ListView
     **** when placed inside a ScrollView  ****/
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    /* Set Expandable buttons animator*/
    public static void setExpandableButtonAnimators(final ExpandableRelativeLayout expandableRelativeLayout, final ImageView imageView) {
        expandableRelativeLayout.setListener(new ExpandableLayoutListener() {
            @Override
            public void onAnimationStart() {

            }

            @Override
            public void onAnimationEnd() {

            }

            @Override
            public void onPreOpen() {
                createRotateAnimator(imageView, 0f, 180f).start();
            }

            @Override
            public void onPreClose() {
                createRotateAnimator(imageView, 180f, 0f).start();
            }

            @Override
            public void onOpened() {
              /*  expandableRelativeLayout.setLayoutParams(new RelativeLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));*/
            }

            @Override
            public void onClosed() {

            }
        });
    }

    /*Rotate Animator for buttons*/
    public static ObjectAnimator createRotateAnimator(final View target, final float from, final float to) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(target, "rotation", from, to);
        animator.setDuration(300);
        animator.setInterpolator(Utils.createInterpolator(Utils.LINEAR_INTERPOLATOR));
        return animator;
    }

    public static String nowDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat requiredFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
        return requiredFormat.format(calendar.getTimeInMillis());
    }

    public static String nowTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat requiredFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        return requiredFormat.format(calendar.getTimeInMillis());
    }

    // NAVIGATE TO FRAGMENT
    public static void navigateFragment(Fragment fragment, String tag, Bundle bundle,
                                        FragmentActivity fragmentActivity) {
        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //fragmentTransaction.setCustomAnimations(R.anim.window_enter, R.anim.window_close);
        if (bundle != null) {
            fragment.setArguments(bundle);
        }
        fragmentTransaction.replace(R.id.main_content, fragment, tag);
        fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.commitAllowingStateLoss();
        fragmentManager.executePendingTransactions();
    }

    /*Get Random colors based on position*/
    public static int randomColor(int alpha) {

        int r = (int) (0xff * Math.random());
        int g = (int) (0xff * Math.random());
        int b = (int) (0xff * Math.random());

        return Color.argb(alpha, r, g, b);
    }

    /**
     * UNIVERSAL IMAGE LOADER
     * <p>
     * to load image uri to image
     */
    public static void universalImageLoaderPicLoading(ImageView ivImageView,
                                                      String ImageUrl,
                                                      final ProgressBar progressBar,
                                                      int placeholder) {

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(placeholder)
                .showImageForEmptyUri(placeholder)
                .showImageOnFail(placeholder)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        if (progressBar != null) {
            ImageLoader.getInstance().displayImage(ImageUrl, ivImageView, options, new SimpleImageLoadingListener() {

                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    progressBar.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    progressBar.setVisibility(View.GONE);
                }


            });
        } else {
            ImageLoader.getInstance().displayImage(ImageUrl, ivImageView, options);
        }

    }


    /*DELETE FCM ID*/
   /*
    public static void deleteToken(final Activity activity, final ProgressDialog progressDialog) {
        progressDialog.show();
        SharedPreferences prefs = activity.getSharedPreferences("loginPrefs", 0);
        JSONObject params = new JSONObject();
        try {
            params.put("fcmid", getSharedPrefStringData(activity, Constants.FCM_TOKEN));
            AsyncHttpClient client = new AsyncHttpClient();
            BasicHeader[] headers = new BasicHeader[]{new BasicHeader("Authorization",
                    "Token " + prefs.getString("UserName", ""))};
            client.setTimeout(50000);
            HttpEntity entity;
            try {
                entity = new StringEntity(params.toString());
                Utility.showLog("jsonObject", params.toString());
                client.post(activity, Constants.FCM_DELETE, headers, entity, "application/json", new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(String response) {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        Utility.showLog("response", response);
                        Utility.setSharedPrefStringData(activity, Constants.FCM_TOKEN, "");
                        try {
                            JSONObject obj = new JSONObject(response);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            SharedPreferences preferences = activity.getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.clear();
                            editor.apply();

                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }
                        Intent i = new Intent(activity, MainActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        activity.startActivity(i);
                        activity.finish();
                    }

                    @Override
                    public void onFailure(int statusCode, Throwable error, String content) {
                        Utility.showLog("error", error.toString());
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }
                });
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    */

    /*public static void callLogout(final Activity activity, final ProgressDialog progressDialog) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        alertDialogBuilder.setTitle("Are you sure you want to clear all the data?");
        alertDialogBuilder
                .setMessage("Click yes to Logout!")
                .setCancelable(false)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                deleteToken(activity, progressDialog);
                            }
                        })

                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }*/
    public static void callLogout(final Activity activity, final ProgressDialog progressDialog) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        alertDialogBuilder.setTitle("Are you sure you want to clear all the data?");
        alertDialogBuilder
                .setMessage("Click yes to Logout!")
                .setCancelable(false)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                try {
//                                    SharedPreferences preferences = activity.getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
//                                    SharedPreferences.Editor editor = preferences.edit();
//                                    editor.clear();
//                                    editor.apply();
                                    AppPrefs.getInstance(activity.getApplicationContext()).putString("FIRST_LOGIN","FALSE");

                                } catch (NullPointerException e) {
                                    e.printStackTrace();
                                }
                                Intent i = new Intent(activity, MainActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                activity.startActivity(i);
                                activity.finish();
                            }
                        })

                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public static void clearDatabase(Context context) {
        DatabaseHandler helper = new DatabaseHandler(context);
        SQLiteDatabase database = helper.getWritableDatabase();
        database.delete(DatabaseHandler.TABLE_CONSUMERS, null, null);
        database.close();

        MatsDatabaseHandler matsDatabaseHandler = new MatsDatabaseHandler(context);
        SQLiteDatabase matsdatabase = matsDatabaseHandler.getWritableDatabase();
        matsdatabase.delete(MatsDatabaseHandler.TABLE_MATS_DC_LIST, null, null);
        matsdatabase.close();
    }
}
