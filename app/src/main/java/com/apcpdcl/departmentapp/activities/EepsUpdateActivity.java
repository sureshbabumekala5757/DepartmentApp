package com.apcpdcl.departmentapp.activities;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.apcpdcl.departmentapp.R;
import com.apcpdcl.departmentapp.utils.Constants;
import com.apcpdcl.departmentapp.utils.ImageUtil;
import com.apcpdcl.departmentapp.utils.Utility;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EepsUpdateActivity extends AppCompatActivity implements LocationListener {

    @BindView(R.id.uploadimage1)
    ImageView uploadimage1;
    @BindView(R.id.uploadimage2)
    ImageView uploadimage2;
    @BindView(R.id.uploadimage3)
    ImageView uploadimage3;
    @BindView(R.id.nametxt)
    TextView nametxt;
    @BindView(R.id.loadtxt)
    TextView loadtxt;
    @BindView(R.id.socialtxt)
    TextView socialtxt;
    @BindView(R.id.feedertxt)
    TextView feedertxt;
    @BindView(R.id.villagetxt)
    TextView villagetxt;
    @BindView(R.id.sectiontxt)
    TextView sectiontxt;
    @BindView(R.id.lattxt)
    TextView lattxt;
    @BindView(R.id.longtxt)
    TextView longtxt;
    @BindView(R.id.et_mmake)
    EditText et_mmake;
    @BindView(R.id.et_msno)
    EditText et_msno;
    @BindView(R.id.et_mfixdate)
    EditText et_mfixdate;
    @BindView(R.id.et_removedmmake)
    EditText et_removedmmake;
    @BindView(R.id.et_removedmsno)
    EditText et_removedmsno;
    @BindView(R.id.btn_save)
    Button btn_save;
    @BindView(R.id.btn_cancel)
    Button btn_cancel;
    @BindView(R.id.calendarbtn)
    Button calendarbtn;


    private Bitmap bitmap1, bitmap2, bitmap3;
    private String str_uploadimage, str_uploadimage1, str_uploadimage2, str_uploadimage3;
    private String strUnit_name;
    ProgressDialog pDialog;
    LocationManager locationManager;
    String mprovider;
    private String str_Latitude = "";
    private String str_Longitude = "";
    private String from = "";
    private String strRegnum, strConsumerName, strLoad, strSocialStatus, strFeeder, strVillage, str_Status;
    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eeps_update);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        pDialog = new ProgressDialog(this);
        SharedPreferences prefs = getSharedPreferences("loginPrefs", 0);
        strUnit_name = prefs.getString("UNITNAME", "");

        Intent intent = getIntent();
        Bundle bd = intent.getExtras();
        if (bd != null) {
            strRegnum = (String) bd.get("RegNo");
            strConsumerName = (String) bd.get("ConsumerName");
            strLoad = (String) bd.get("Load");
            strSocialStatus = (String) bd.get("SocialStatus");
            strFeeder = (String) bd.get("11KvFeeder");
            strVillage = (String) bd.get("Village");
            from = getIntent().getStringExtra(Constants.FROM);
        }


        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        mprovider = locationManager.getBestProvider(criteria, false);

        if (mprovider != null && !mprovider.equals("")) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            Location location = locationManager.getLastKnownLocation(mprovider);
            locationManager.requestLocationUpdates(mprovider, 15000, 1, (LocationListener) this);

            if (location != null)
                onLocationChanged(location);
            else
                Toast.makeText(getBaseContext(), "No Location Provider Found Check Your Code", Toast.LENGTH_SHORT).show();
        }

        setData();
    }

    private void setData() {
        nametxt.setText(strConsumerName);
        loadtxt.setText(strLoad);
        socialtxt.setText(strSocialStatus);
        feedertxt.setText(strFeeder);
        villagetxt.setText(strVillage);
        sectiontxt.setText(strUnit_name);
        lattxt.setText(str_Latitude);
        longtxt.setText(str_Longitude);
    }

    @OnClick(R.id.uploadimage1)
    void openCamera1() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 101);
    }

    @OnClick(R.id.uploadimage2)
    void openCamera2() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 102);
    }

    @OnClick(R.id.uploadimage3)
    void openCamera3() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 103);
    }

    @OnClick(R.id.calendarbtn)
    void calendarbtn() {
        myCalendar = Calendar.getInstance();
        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };
        DatePickerDialog dpr = new DatePickerDialog(this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, 1);
        dpr.getDatePicker().setMaxDate(System.currentTimeMillis());
        dpr.show();
    }

    private void updateLabel() {
        String myFormat = "dd-MMM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        et_mfixdate.setText(sdf.format(myCalendar.getTime()));
    }


    @OnClick(R.id.btn_save)
    void saveDetails() {
        if (validateFields()) {
            if (Utility.isNetworkAvailable(this)) {
                addMotorData();
            } else {
                Utility.showCustomOKOnlyDialog(this, Utility.getResourcesString(this, R.string.no_internet));
            }
        }

    }

    private boolean validateFields() {
        if (Utility.isValueNullOrEmpty(et_mmake.getText().toString())) {
            Utility.showCustomOKOnlyDialog(this, "Please Enter MotorMake");
            return false;
        } else if (Utility.isValueNullOrEmpty(et_msno.getText().toString())) {
            Utility.showCustomOKOnlyDialog(this, "Please Enter Motorsno");
            return false;
        } else if (Utility.isValueNullOrEmpty(et_mfixdate.getText().toString())) {
            Utility.showCustomOKOnlyDialog(this, "Please Enter Motor FixDate");
            return false;
        } else if (Utility.isValueNullOrEmpty(et_removedmmake.getText().toString())) {
            Utility.showCustomOKOnlyDialog(this, "Please Enter Removed Motor Make");
            return false;
        } else if (Utility.isValueNullOrEmpty(et_removedmsno.getText().toString())) {
            Utility.showCustomOKOnlyDialog(this, "Please Enter Removed Motor sno");
            return false;
        } else if (Utility.isValueNullOrEmpty(str_uploadimage1)) {
            Utility.showCustomOKOnlyDialog(this, "Please Upload Image");
            return false;
        }
        return true;
    }

    @OnClick(R.id.btn_cancel)
    void cancelDetails() {
        Intent intent = new Intent(this, AeDashBoardActivity.class);
        startActivity(intent);
    }

    private void addMotorData() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("REGNO", strRegnum);
            jsonObject.put("LATITUDE", str_Latitude);
            jsonObject.put("LONGITUDE", str_Longitude);
            jsonObject.put("MOTORMAKE", et_mmake.getText().toString());
            jsonObject.put("MOTORSNO", et_msno.getText().toString());
            jsonObject.put("FIXDATE", et_mfixdate.getText().toString());
            jsonObject.put("MOTORMAKEREM", et_removedmmake.getText().toString());
            jsonObject.put("MOTORSRREM", et_removedmsno.getText().toString());
            jsonObject.put("IMAGE1", str_uploadimage1.replace("\n", ""));
            jsonObject.put("IMAGE2", str_uploadimage2.replace("\n", ""));
            jsonObject.put("IMAGE3", str_uploadimage3.replace("\n", ""));
            saveMotorData(jsonObject);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void saveMotorData(JSONObject jsonObject) {
        pDialog.show();
        pDialog.setMessage("Please wait...");
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(50000);
        HttpEntity entity;
        try {
            entity = new StringEntity(jsonObject.toString());
            client.post(this, Constants.URL+"eesl/saveData", entity, "application/json", new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(String response) {
                    Utility.showLog("onSuccess", response);
                    try {
                        if (pDialog != null && pDialog.isShowing()) {
                            pDialog.dismiss();
                        }
                        JSONObject jsonObject = new JSONObject(response);

                        str_Status = jsonObject.getString("status");
                        if (str_Status.equalsIgnoreCase("true")) {
                            String str_Msg = jsonObject.getString("msg");
                            showOKOnlyDialog(EepsUpdateActivity.this, str_Msg);
                        } else if (str_Status.equalsIgnoreCase("false")) {
                            String str_Error = jsonObject.getString("error");
                            Utility.showCustomOKOnlyDialog(EepsUpdateActivity.this, str_Error);
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
                    Utility.showCustomOKOnlyDialog(EepsUpdateActivity.this, error.getMessage());
                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 101:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    str_uploadimage1 = stringfromImage(data, bitmap1);
                    if (!Utility.isValueNullOrEmpty(str_uploadimage1)) {
                        uploadimage1.setImageBitmap(ImageUtil.convert(str_uploadimage1));
                        uploadimage2.setVisibility(View.VISIBLE);
                    }
                }
                break;
            case 102:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    str_uploadimage2 = stringfromImage(data, bitmap2);
                    if (!Utility.isValueNullOrEmpty(str_uploadimage2)) {
                        uploadimage2.setImageBitmap(ImageUtil.convert(str_uploadimage2));
                        uploadimage3.setVisibility(View.VISIBLE);
                    }
                }
                break;
            case 103:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    str_uploadimage3 = stringfromImage(data, bitmap3);
                    uploadimage3.setImageBitmap(ImageUtil.convert(str_uploadimage3));
                }
                break;
        }
    }

    private Uri getImageUri(Context inContext, Bitmap inImage) {
        Bitmap OutImage = Bitmap.createScaledBitmap(inImage, 1000, 1000, true);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), OutImage, "Title", null);
        return Uri.parse(path);
    }

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    private String getRealPathFromURI(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection, null, null, null);
        int column_index_data = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToLast();

        return cursor.getString(column_index_data);
    }

    private String stringfromImage(Intent data, Bitmap bitmap) {

        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        assert thumbnail != null;
        Uri filepath = getImageUri(getApplicationContext(), thumbnail);
        File finalFile = new File(getRealPathFromURI(filepath));//here we get Path
        Uri picUri = Uri.fromFile(finalFile);
        InputStream iStream = null;
        try {
            iStream = EepsUpdateActivity.this.getContentResolver().openInputStream(picUri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        byte[] inputData = new byte[0];
        try {
            inputData = getBytes(iStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        bitmap = BitmapFactory.decodeByteArray(inputData, 0, inputData.length);
        str_uploadimage = ImageUtil.updateConvert(bitmap);
        str_uploadimage = str_uploadimage.replace("\r\n", "");
        Utility.showLog("base64", str_uploadimage);
        return str_uploadimage;
    }

    @Override
    public void onLocationChanged(Location location) {
        str_Latitude = String.valueOf(location.getLatitude());
        str_Longitude = String.valueOf(location.getLongitude());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public void showOKOnlyDialog(final Context context, String message) {
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
                    if (from.equalsIgnoreCase(LMDashBoardActivity.class.getSimpleName())) {
                        context.startActivity(new Intent(context, LMDashBoardActivity.class));
                    } else {
                        context.startActivity(new Intent(context, AeDashBoardActivity.class));
                    }
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
}