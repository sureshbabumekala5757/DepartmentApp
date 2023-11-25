package com.apcpdcl.departmentapp.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.apcpdcl.departmentapp.models.DTRModel;
import com.apcpdcl.departmentapp.models.DistributionModel;
import com.apcpdcl.departmentapp.models.PoleModel;
import com.apcpdcl.departmentapp.models.PoleUploadDataModel;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PoleDataDataBaseHandler extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "POLEDATA";

    private static final String TABLE_DTR = "dtr";
    private static final String TABLE_DISTRIBUTION = "distribution";
    private static final String TABLE_POLE = "pole";
    private static final String TABLE_POLE_USCNO = "poleUSCNO";

    private static final String KEY_ID = "id";
    private static final String KEY_USER_NAME = "userName";
    private static final String KEY_SECTION_CODE = "sectionCode";
    private static final String KEY_SECTION_NAME = "status";
    private static final String KEY_DTR_CODE = "dtrCode";
    private static final String KEY_DTR_NAME = "dtrName";
    private static final String KEY_DISTRIBUTION_CODE = "distributionCode";
    private static final String KEY_DISTRIBUTION_NAME = "distributionName";
    private static final String KEY_POLE_NO = "poleNo";
    private static final String KEY_POLE_NAME = "poleName";
    private static final String KEY_USCNO = "USCNO";
    private static final String KEY_POLE_USCNOS_DATA = "poleUscNosData";
    private static final String KEY_DATE = "date";
    private static final String KEY_LIVE_STATUS = "liveStatus";
    private static final String KEY_POLE_LAT = "poleLat";
    private static final String KEY_POLE_LOG = "poleLog";
    private static final String KEY_DTR_LAT = "dtrLat";
    private static final String KEY_DTR_LOG = "dtrLog";
    private static final String KEY_REMARKS = "remarks";


    SQLiteDatabase db;
    private String CREATE_DTR_TABLE ="";
    private String CREATE_DISTRIBUTION_TABLE ="";
    private String CREATE_POLE_TABLE ="";
    private String CREATE_POLE_USCNO_TABLE ="";
    public PoleDataDataBaseHandler(Context context) {

        super(context,DATABASE_NAME,null, DATABASE_VERSION);

    }
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        CREATE_DTR_TABLE = "CREATE TABLE " + TABLE_DTR + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_SECTION_CODE + " TEXT,"
                + KEY_DTR_NAME + " TEXT,"+ KEY_DTR_CODE + " TEXT,"+ KEY_DTR_LAT + " TEXT,"+ KEY_DTR_LOG + " TEXT,"+ KEY_LIVE_STATUS + " TEXT,"+ KEY_REMARKS + " TEXT," + KEY_DATE + " TEXT"
                + ")";
        CREATE_DISTRIBUTION_TABLE = "CREATE TABLE " + TABLE_DISTRIBUTION + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_SECTION_CODE + " TEXT,"
                + KEY_DISTRIBUTION_NAME + " TEXT,"+ KEY_DISTRIBUTION_CODE + " TEXT," + KEY_DATE + " TEXT"
                + ")";
        CREATE_POLE_TABLE = "CREATE TABLE " + TABLE_POLE + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+ KEY_SECTION_CODE + " TEXT,"+ KEY_DISTRIBUTION_CODE + " TEXT," + KEY_DTR_CODE + " TEXT,"
                + KEY_POLE_NO + " TEXT,"+ KEY_POLE_NAME + " TEXT,"+ KEY_POLE_LAT + " TEXT,"+ KEY_POLE_LOG + " TEXT,"+ KEY_LIVE_STATUS + " TEXT,"+ KEY_REMARKS + " TEXT," + KEY_DATE + " TEXT"
                + ")";
        CREATE_POLE_USCNO_TABLE = "CREATE TABLE " + TABLE_POLE_USCNO + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+ KEY_USER_NAME + " TEXT,"+ KEY_SECTION_CODE + " TEXT,"+ KEY_DISTRIBUTION_CODE + " TEXT," + KEY_DTR_CODE + " TEXT,"
                + KEY_POLE_NO + " TEXT,"+ KEY_POLE_NAME + " TEXT,"+ KEY_USCNO + " TEXT,"+ KEY_POLE_USCNOS_DATA + " TEXT," + KEY_DATE + " TEXT,"+ KEY_REMARKS + " TEXT," + KEY_LIVE_STATUS + " TEXT"
                + ")";

        db.execSQL(CREATE_DTR_TABLE);
        db.execSQL(CREATE_DISTRIBUTION_TABLE);
        db.execSQL(CREATE_POLE_TABLE);
        db.execSQL(CREATE_POLE_USCNO_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /*Insert dtr data*/
    public boolean insertDTRData(ArrayList<DTRModel> dtrListArr, String sectionCode, String dtrLat, String dtrLog) {
        SimpleDateFormat sdf;
        sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        String date = sdf.format(new Date());
        try {
            db = this.getWritableDatabase();
            for(DTRModel dtrData:dtrListArr){
                ContentValues values = new ContentValues();
                values.put(KEY_SECTION_CODE,sectionCode);
                values.put(KEY_DTR_CODE,dtrData.getDTRName());
                values.put(KEY_DTR_NAME,dtrData.getDTRCode());
                values.put(KEY_DTR_LAT,dtrLat);
                values.put(KEY_DTR_LAT,dtrLog);
                values.put(KEY_LIVE_STATUS,"LIVE");
                values.put(KEY_REMARKS,"");
                values.put(KEY_DATE,date);
                db.insert(TABLE_DTR, null, values);
            }
            return true;
        }catch (Exception e) {
            e.printStackTrace();
        }
        db.close(); // Closing database connection
        return false;
    }
    /*Insert Distribution data*/
    public boolean insertDistributionData(ArrayList<DistributionModel> distributionListArr, String sectionCode) {
        SimpleDateFormat sdf;
        sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        String date = sdf.format(new Date());
        try {
            db = this.getWritableDatabase();
            for(DistributionModel distributionData:distributionListArr){
                ContentValues values = new ContentValues();
                values.put(KEY_SECTION_CODE,sectionCode);
                values.put(KEY_DISTRIBUTION_NAME,distributionData.getDistributionName());
                values.put(KEY_DISTRIBUTION_CODE,distributionData.getDistributionCode());
                values.put(KEY_DATE,date);
                db.insert(TABLE_DISTRIBUTION, null, values);
            }
            return true;
        }catch (Exception e) {
            e.printStackTrace();
        }
        db.close(); // Closing database connection
        return false;
    }
    /*Insert Pole data*/
    public boolean insertPoleData(String disCode,String dtrCode, String poleNo, String poleName, String poleLat, String poleLog) {
        SimpleDateFormat sdf;
        sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        String date = sdf.format(new Date());
        try {
            db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_SECTION_CODE, disCode.substring(0,5));
            values.put(KEY_DISTRIBUTION_CODE, disCode);
            values.put(KEY_DTR_CODE, dtrCode);
            values.put(KEY_POLE_NO, poleNo);
            values.put(KEY_POLE_NAME, poleName);
            values.put(KEY_POLE_LAT, poleLat);
            values.put(KEY_POLE_LOG, poleLog);
            values.put(KEY_LIVE_STATUS, "0");
            values.put(KEY_REMARKS, "");
            values.put(KEY_DATE, date);

            // Inserting Row
            db.insert(TABLE_POLE, null, values);
            return true;
        } catch (SQLException s) {
            new Exception("Error with DB Open");
        }

        db.close(); // Closing database connection
        return false;
    }
    /*Insert USCNO data*/
    public void insertUSCNOData(String userName,String distributionCode, String dtrCode, String poleNo, String poleName, String finalData, JSONObject finalJsonObj) {
        SimpleDateFormat sdf;
        sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        String date = sdf.format(new Date());
        try {
            db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_USER_NAME, userName);
            values.put(KEY_SECTION_CODE, distributionCode.substring(0,5));
            values.put(KEY_DISTRIBUTION_CODE, distributionCode);
            values.put(KEY_DTR_CODE, dtrCode);
            values.put(KEY_POLE_NO, poleNo);
            values.put(KEY_POLE_NAME, poleName);
            values.put(KEY_USCNO, finalData);
            values.put(KEY_POLE_USCNOS_DATA,finalJsonObj.toString());
            values.put(KEY_DATE, date);
            values.put(KEY_REMARKS, "");
            values.put(KEY_LIVE_STATUS, "0");

            // Inserting Row
            db.insert(TABLE_POLE_USCNO, null, values);

        } catch (SQLException s) {
            new Exception("Error with DB Open");
        }

        db.close(); // Closing database connection
    }

    //Count of section code
    public int getSectionCount(String sectionCode, String tblStr){
        String tableStr = "";
        int count = 0;
        if(tblStr.equalsIgnoreCase("DIS")){
            tableStr = TABLE_DISTRIBUTION;
        }else if(tblStr.equalsIgnoreCase("DTR")){
            tableStr = TABLE_DTR;
        }
        String countQuery = "SELECT  * FROM " + tableStr + " WHERE " + KEY_SECTION_CODE + " = " + sectionCode;

        try {
            db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(countQuery, null);

            count = cursor.getCount();
            cursor.close();
            return count;
        } catch (SQLException s) {
            new Exception("Error with DB Open");
        }

        db.close(); // Closing database connection
        // return count
        return count;
    }
    //Count of pole data
    public int getPoleDataCount(String dtrCode){

        int count=0;
        String countQuery = "SELECT  * FROM " + TABLE_POLE + " WHERE " + KEY_DTR_CODE + " = '" + dtrCode +"'";

        try {
            db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(countQuery, null);

            count = cursor.getCount();
            cursor.close();

        } catch (SQLException s) {
            new Exception("Error with DB Open");
        }
        db.close();
        // return count
        return count;
    }
    //Delet DTR DATA
    public void deleteDTRData(String sectionCode) {
        try {
            db = this.getWritableDatabase();
            db.delete(TABLE_DTR, KEY_SECTION_CODE + " = ?",
                    new String[]{String.valueOf(sectionCode)});
        } catch (SQLException s) {
            new Exception("Error with DB Open");
        }

        db.close();
    }
    //Delet Distribution DATA
    public void deleteDistributionData(String sectionCode) {
        try {
            db = this.getWritableDatabase();
            db.delete(TABLE_DISTRIBUTION, KEY_SECTION_CODE + " = ?",
                    new String[]{String.valueOf(sectionCode)});
        } catch (SQLException s) {
            new Exception("Error with DB Open");
        }

        db.close();
    }
    //Get DTR Data
    public ArrayList<DTRModel> getDTRData(String sectionCode){
        ArrayList<DTRModel> dtrDataList= new ArrayList<DTRModel>();
        // Select All Query

        try {
            db = this.getWritableDatabase();
            String selectQuery = "SELECT  DISTINCT" + " " + KEY_DTR_NAME + "," + KEY_DTR_CODE + " " + "FROM " + TABLE_DTR+ " WHERE "+ KEY_SECTION_CODE +"="+sectionCode;
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    dtrDataList.add(new DTRModel(cursor.getString(cursor.getColumnIndex(KEY_DTR_NAME)),cursor.getString(cursor.getColumnIndex(KEY_DTR_CODE))));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
        return dtrDataList;
    }
    //Get Distribution Data
    public ArrayList<DistributionModel> getDistributionData(String sectionCode){
        ArrayList<DistributionModel> distributionDataList= new ArrayList<DistributionModel>();
        // Select All Query

        try {
            db = this.getWritableDatabase();
            String selectQuery = "SELECT  DISTINCT" + " " + KEY_DISTRIBUTION_NAME + "," + KEY_DISTRIBUTION_CODE + " " + "FROM " + TABLE_DISTRIBUTION+ " WHERE "+ KEY_SECTION_CODE +"="+sectionCode;
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    distributionDataList.add(new DistributionModel(cursor.getString(cursor.getColumnIndex(KEY_DISTRIBUTION_NAME)),cursor.getString(cursor.getColumnIndex(KEY_DISTRIBUTION_CODE))));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
        return distributionDataList;
    }
    //Get Pole Data
    public ArrayList<PoleModel> getPoleData(String dtrCode){
        ArrayList<PoleModel> poleDataList= new ArrayList<PoleModel>();
        // Select All Query

        try {
            db = this.getWritableDatabase();
            String selectQuery = "SELECT  DISTINCT" + " " + KEY_POLE_NAME + "," + KEY_POLE_NO + " " + "FROM " + TABLE_POLE + " WHERE "+ KEY_DTR_CODE +" ='" + dtrCode + "'";
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    poleDataList.add(new PoleModel(cursor.getString(cursor.getColumnIndex(KEY_POLE_NAME)),cursor.getString(cursor.getColumnIndex(KEY_POLE_NO))));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
        return poleDataList;
    }
    //Get Pole Data based on dtr and poleno
    public ArrayList<PoleModel> getSinglePoleData(String dtrCode, String poleNo){
        ArrayList<PoleModel> poleDataList= new ArrayList<PoleModel>();
        // Select All Query

        try {
            db = this.getWritableDatabase();
            String selectQuery = "SELECT  DISTINCT" + " " + KEY_POLE_NAME + "," + KEY_POLE_NO + ","+ KEY_POLE_LAT + ","+ KEY_POLE_LOG + " " + "FROM " + TABLE_POLE + " WHERE "+ KEY_DTR_CODE +" ='" + dtrCode + "' AND "+KEY_POLE_NO+" ='" + poleNo + "'";
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    poleDataList.add(new PoleModel(cursor.getString(cursor.getColumnIndex(KEY_POLE_NAME)),cursor.getString(cursor.getColumnIndex(KEY_POLE_NO)), cursor.getString(cursor.getColumnIndex(KEY_POLE_LAT)), cursor.getString(cursor.getColumnIndex(KEY_POLE_LOG))));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
        return poleDataList;
    }
    //Get last PoleNo
    public String getlastPoleNo(String dtrCode){
        String sLastPoleNo = "";

        try {
            db = this.getWritableDatabase();
            String selectQuery = "SELECT" + " " + KEY_POLE_NO + " " + "FROM " + TABLE_POLE + " WHERE "+ KEY_DTR_CODE +" ='" + dtrCode + "'" + " ORDER BY "+ KEY_ID +" DESC LIMIT 1";
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    sLastPoleNo = cursor.getString(cursor.getColumnIndex(KEY_POLE_NO));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
        return sLastPoleNo;
    }
    //Get Pole Data
    public ArrayList<PoleUploadDataModel> getAllPoleDAta(){
        ArrayList<PoleUploadDataModel> poleDataList= new ArrayList<PoleUploadDataModel>();
        try {
            String nStatus = "0";
            db = this.getWritableDatabase();

            String selectQuery = "SELECT * FROM " + TABLE_POLE_USCNO + " WHERE "+ KEY_LIVE_STATUS +" ='" + nStatus + "'";
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    PoleUploadDataModel PoleUploadData = new PoleUploadDataModel();
                    PoleUploadData.setsSectionCode(cursor.getString(cursor.getColumnIndex(KEY_USER_NAME)));
                    PoleUploadData.setsSectionCode(cursor.getString(cursor.getColumnIndex(KEY_DISTRIBUTION_CODE)));
                    PoleUploadData.setsDistributionCode(cursor.getString(cursor.getColumnIndex(KEY_DISTRIBUTION_CODE)));
                    PoleUploadData.setsDtrCode(cursor.getString(cursor.getColumnIndex(KEY_DTR_CODE)));
                    PoleUploadData.setsPoleCode(cursor.getString(cursor.getColumnIndex(KEY_POLE_NO)));
                    PoleUploadData.setsPoleUscnodata(cursor.getString(cursor.getColumnIndex(KEY_USCNO)));
                    PoleUploadData.setsDate(cursor.getString(cursor.getColumnIndex(KEY_DATE)));
                    poleDataList.add(PoleUploadData);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
        return poleDataList;
    }
    //Get Pole Data
    public String getPoleUscnoData(String distCode,String dtrCode, String poleNo){
        String uscnoData = "";
        try {
            int nStatus = 0;
            db = this.getWritableDatabase();

            String selectQuery = "SELECT " + KEY_POLE_USCNOS_DATA + " FROM " + TABLE_POLE_USCNO + " WHERE "+ KEY_DTR_CODE +" ='" + dtrCode + "'"+ " AND "+KEY_DISTRIBUTION_CODE+" ="+ "'"+distCode+"'" +" AND "+ KEY_POLE_NO +" ='" + poleNo + "'";
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {

                    uscnoData = cursor.getString(cursor.getColumnIndex(KEY_POLE_USCNOS_DATA));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
        return uscnoData;
    }
    //Update uscno data
    public void updateUSCNOData(String dtrCode, String poleNo, String uscnoData, JSONObject finalPoleUscnoData) {

        try {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_USCNO, uscnoData);
        values.put(KEY_POLE_USCNOS_DATA, finalPoleUscnoData.toString());
            db.update(TABLE_POLE_USCNO, values, KEY_DTR_CODE + " = ?" + " AND "+ KEY_POLE_NO + " = ?",
                    new String[]{String.valueOf(dtrCode),String.valueOf(poleNo)});
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
        // updating row

    }
    //Update status poleUscno table
    public void updateStatus(String dtrCode, String poleNo, String statusValue) {

        try {
            db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_LIVE_STATUS, statusValue);
            db.update(TABLE_POLE_USCNO, values, KEY_DTR_CODE + " = ?" + " AND "+ KEY_POLE_NO + " = ?",
                    new String[]{String.valueOf(dtrCode),String.valueOf(poleNo)});
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
        // updating row

    }
    //Count of pole data
    public int recordIsAvilable(String dtrCode, String poleNo){

        int count=0;
        String countQuery = "SELECT  * FROM " + TABLE_POLE_USCNO + " WHERE " + KEY_DTR_CODE + " = '" + dtrCode +"' AND " + KEY_POLE_NO + " = '" + poleNo +"'";

        try {
            db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(countQuery, null);
            count = cursor.getCount();
            cursor.close();
        } catch (SQLException s) {
            new Exception("Error with DB Open");
        }
        db.close();
        // return count
        return count;
    }
    //Update DTR Data table
    public void updateDtrdata(String sectionCode,String dtrCode, String lat,String Log) {

        try {
            db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_DTR_LAT, lat);
            values.put(KEY_DTR_LOG, Log);
            db.update(TABLE_DTR, values, KEY_SECTION_CODE + " = ?" + " AND "+ KEY_DTR_CODE + " = ?",
                    new String[]{String.valueOf(sectionCode),String.valueOf(dtrCode)});
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
        // updating row

    }
    //Count of Data data
    public int getDtrDataCount(String sectionCode,String dtrCode){

        int count=0;
        String countQuery = "SELECT "+KEY_DTR_LAT+","+KEY_DTR_LOG+" FROM " + TABLE_DTR + " WHERE " + KEY_SECTION_CODE + " = '" + sectionCode +"' AND "+ KEY_DTR_CODE + " = '" + dtrCode +"' AND NOT(" +KEY_DTR_LAT+" is  null or "+ KEY_DTR_LAT+"='' or "+KEY_DTR_LOG+" is  null or "+  KEY_DTR_LOG+" ='')";

        try {
            db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(countQuery, null);

            count = cursor.getCount();
            cursor.close();

        } catch (SQLException s) {
            new Exception("Error with DB Open");
        }
        db.close();
        // return count
        return count;
    }

    //Get DTR Data based on Dis and dtr code
    public ArrayList<DTRModel> getSingleDtrData(String secCode, String dtrCode){
        ArrayList<DTRModel> dtrModelList= new ArrayList<DTRModel>();
        // Select All Query

        try {
            db = this.getWritableDatabase();
            String selectQuery = "SELECT  DISTINCT" + " " + KEY_DTR_NAME + "," + KEY_DTR_CODE + ","+ KEY_DTR_LAT + ","+ KEY_DTR_LOG + " " + "FROM " + TABLE_DTR + " WHERE "+ KEY_SECTION_CODE +" ='" + secCode + "' AND "+KEY_DTR_CODE+" ='" + dtrCode + "'";
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    dtrModelList.add(new DTRModel(cursor.getString(cursor.getColumnIndex(KEY_DTR_NAME)),cursor.getString(cursor.getColumnIndex(KEY_DTR_CODE)), cursor.getString(cursor.getColumnIndex(KEY_DTR_LAT)), cursor.getString(cursor.getColumnIndex(KEY_DTR_LOG))));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
        return dtrModelList;
    }
    public void openConnection(){
        db = this.getWritableDatabase();
    }
}
