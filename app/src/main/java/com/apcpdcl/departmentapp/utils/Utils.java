package com.apcpdcl.departmentapp.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.apcpdcl.departmentapp.activities.Home;
import com.apcpdcl.departmentapp.models.Consumer;
import com.apcpdcl.departmentapp.models.MatsConsumerModel;
import com.apcpdcl.departmentapp.models.ReportsList;
import com.apcpdcl.departmentapp.services.ServiceConstants;
import com.apcpdcl.departmentapp.shared.AppPrefs;
import com.apcpdcl.departmentapp.sqlite.DatabaseHandler;
import com.apcpdcl.departmentapp.sqlite.MatsDatabaseHandler;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 05-02-2018.
 */

public class Utils {
    public static ArrayList<String> checkedCheckBoxList = new ArrayList<>();
    private JSONObject obj = null;
    private DatabaseHandler db;
    private MatsDatabaseHandler matsDatabaseHandler;
    private ProgressDialog prgDialog;
    public static String strNum;
    public static String strCat;
    //  public static String strVersion = "1.1";
    public static ArrayList<ReportsList> filterReportsList = new ArrayList<>();
    private String status = "";


    public void addRecordstoDB(final Context context, JSONObject responseObj) {

        prgDialog = new ProgressDialog(context);
        db = new DatabaseHandler(context);
        HttpEntity entity;
        try {
            db.clearTable();
            prgDialog.show();
            prgDialog.setMessage("Please wait Downloading is going on...");
            prgDialog.setCancelable(false);
            prgDialog.setCanceledOnTouchOutside(false);

            AsyncHttpClient client = new AsyncHttpClient();
            //client.addHeader("version", BuildConfig.VERSION_NAME);
            client.setTimeout(500000);
            entity = new StringEntity(responseObj.toString());
            BasicHeader[] headers = new BasicHeader[]{new BasicHeader("Authorization", "Basic "+AppPrefs.getInstance(context.getApplicationContext()).getString("USER_AUTH", ""))};
            Utility.showLog("URL", Constants.URL + Constants.GEO_LATLONGINPUT);
            client.post(context,"https://apcpcdcl-test-k5qoqm5y.it-cpi012-rt.cfapps.ap21.hana.ondemand.com/http/DepartmentalApp/ISU/DownloadorRefreshDownloadList/DEV", headers, entity, "application/json", new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(String responseStr) {
                    try {
//                        obj = new JSONObject(responseStr);
//                        String status = obj.getString("STATUS");
                        JSONObject responseObj = new JSONObject(responseStr);
                        responseObj = responseObj.getJSONObject("response");
                        String status = responseObj.getString("success");
                        switch (status) {
                            case "True":
                                JSONArray jsonArray = responseObj.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String strBldiscdt = jsonObject.getString("discondate");
                                    String strStype = jsonObject.getString("stype");
                                    String strCmcNames = jsonObject.getString("name");
                                    String strMeterNum = "";//jsonObject.getString("MTRNO")
                                    String strConsumerNums = jsonObject.getString("bpno");
                                    float strTotals = Float.parseFloat(jsonObject.getString("balancedue"));//Float.parseFloat(jsonObject.getString("TOT"))
                                    String strLastPayDates = jsonObject.getString("lastpaiddt");
                                    String strSocialCat = jsonObject.getString("socialcat");
                                    String strCmCats = jsonObject.getString("ratecat");//jsonObject.getString("ratecat")
                                    String strGovtCat = jsonObject.getString("cmgovt");
                                    float strLastPayAmnt = Float.parseFloat(jsonObject.getString("lastpaidamt"));
                                    String strDtrCode = jsonObject.getString("dtrcode");
                                    String poleNo = jsonObject.getString("poleno");
                                    String strGrp = "";//jsonObject.getString("GRP")
                                    String strAddr = jsonObject.getString("address");
                                    String strLocation = "";//jsonObject.getString("LOCATION")
                                    String strPhone = jsonObject.getString("cell");
                                    String strLat = "0";
                                    if (jsonObject.has("lat")) {
                                        strLat = jsonObject.getString("lat");
                                    }
                                    String strLong = "0";
                                    if (jsonObject.has("long")) {
                                        strLong = jsonObject.getString("long");
                                    }
                                    db.addConsumer(context,new Consumer(strBldiscdt, strStype, strCmcNames, strMeterNum, strConsumerNums,
                                            strTotals, strLastPayDates, strSocialCat, strCmCats, strGovtCat, strLastPayAmnt, strDtrCode,
                                            strGrp, strAddr, strLocation, strPhone, strLat, strLong,poleNo, Home.lmcode));
                                }
                                break;
                            case "False":
                                //db.addDcList(new DcList(strServiceNumber,strkwh,strkvah,strremarks,"FAILURE"));
                                if (prgDialog != null && prgDialog.isShowing()) {
                                    prgDialog.dismiss();
                                }
                                Toast.makeText(context, "Please Contact Your Adminstrator", Toast.LENGTH_LONG).show();
                                break;
                            case "FAIL":
                                if (prgDialog != null && prgDialog.isShowing()) {
                                    prgDialog.dismiss();
                                }
                                Toast.makeText(context, "Please check once", Toast.LENGTH_LONG).show();
                                break;
                        }

                        if (prgDialog != null && prgDialog.isShowing()) {
                            prgDialog.dismiss();
                            Toast.makeText(context.getApplicationContext(), "Records Download Completed", Toast.LENGTH_SHORT).show();
//                            if (context.getClass().equals(MainActivity.class)) {
//                                Intent in = new Intent(context.getApplicationContext(), Home.class);
//                                context.startActivity(in);
//                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(context.getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(int statusCode, Throwable error, String content) {
                    if (prgDialog != null && prgDialog.isShowing()) {
                        prgDialog.dismiss();
                    }
                    Toast toastf = Toast.makeText(context.getApplicationContext(), "failure" + error, Toast.LENGTH_SHORT);
                    toastf.show();
                    Log.e("error", error.toString());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getMatsDCList(final Context context, RequestParams params, final String lmcode) {

        prgDialog = new ProgressDialog(context);
        matsDatabaseHandler = new MatsDatabaseHandler(context);

        try {
            matsDatabaseHandler.clearTable();
            prgDialog.show();
            prgDialog.setMessage("Please wait Downloading is going on...");
            prgDialog.setCancelable(false);
            prgDialog.setCanceledOnTouchOutside(false);

            AsyncHttpClient client = new AsyncHttpClient();
           // client.addHeader("version", BuildConfig.VERSION_NAME);
            client.setTimeout(500000);
            client.post(Constants.URL+"mats/sectiondclist/", params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(String response) {
                    Utility.showLog("response",response);
                    try {
                        obj = new JSONObject(response);
                        String status = obj.getString("STATUS");
                        switch (status) {
                            case "SUCCESS":
                                JSONArray jsonArray = (JSONArray) obj.get("dcData");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    MatsConsumerModel matsConsumerModel = new MatsConsumerModel();
                                    /* MatsConsumerModel matsConsumerModel = new Gson().fromJson(jsonObject.toString(),
                                            MatsConsumerModel.class);*/
                                    matsConsumerModel.setCMADDRESS(jsonObject.getString("CMADDRESS"));
                                    matsConsumerModel.setLAT(jsonObject.getString("LAT"));
                                    matsConsumerModel.setCMCNAME(jsonObject.getString("CMCNAME"));
                                    matsConsumerModel.setCMUSCNO(jsonObject.getString("CMUSCNO"));
                                    matsConsumerModel.setTOT(Float.parseFloat(jsonObject.getString("TOT")));
                                    matsConsumerModel.setCMCAT(jsonObject.getString("CMCAT"));
                                    matsConsumerModel.setLONG(jsonObject.getString("LONG"));
                                    matsConsumerModel.setEXECSSLOAD(Float.parseFloat(jsonObject.getString("EXECSSLOAD")));
                                    matsConsumerModel.setPOLENO(jsonObject.getString("POLENO"));
                                    matsConsumerModel.setCASENO(jsonObject.getString("CASENO"));
                                    matsConsumerModel.setLMCODE(lmcode);
                                    matsDatabaseHandler.addConsumer(context,matsConsumerModel);
                                }
                                break;
                            case "ERROR":
                                //db.addDcList(new DcList(strServiceNumber,strkwh,strkvah,strremarks,"FAILURE"));
                                if (prgDialog != null && prgDialog.isShowing()) {
                                    prgDialog.dismiss();
                                }
                                Toast.makeText(context, "Please Contact Your Adminstrator", Toast.LENGTH_LONG).show();
                                break;
                            case "FAIL":
                                if (prgDialog != null && prgDialog.isShowing()) {
                                    prgDialog.dismiss();
                                }
                                Toast.makeText(context, "Please check once", Toast.LENGTH_LONG).show();
                                break;
                        }

                        if (prgDialog != null && prgDialog.isShowing()) {
                            prgDialog.dismiss();
                            Toast.makeText(context.getApplicationContext(), "Records Download Completed", Toast.LENGTH_SHORT).show();
//                            if (context.getClass().equals(MainActivity.class)) {
//                                Intent in = new Intent(context.getApplicationContext(), Home.class);
//                                context.startActivity(in);
//                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(context.getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(int statusCode, Throwable error, String content) {
                    if (prgDialog != null && prgDialog.isShowing()) {
                        prgDialog.dismiss();
                    }
                    Toast toastf = Toast.makeText(context.getApplicationContext(), "failure" + error, Toast.LENGTH_SHORT);
                    toastf.show();
                    Log.e("error", error.toString());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean IsNullOrBlank(String Input) {
        return Input == null || Input.trim().equals("") || Input.trim().length() == 0;
    }
    public static String generate_string(List<String> list) {
        String geneated_string_value = "";
        for (String v : list) {
            geneated_string_value = geneated_string_value + "|" + v;
        }
        return geneated_string_value + "|";
    }

    public static String removeLastChar(String s) {
        return (s == null || s.length() == 0)? null: (s.substring(0, s.length() - 1));
    }

}
