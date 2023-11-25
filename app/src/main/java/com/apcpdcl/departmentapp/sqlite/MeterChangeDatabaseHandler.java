package com.apcpdcl.departmentapp.sqlite;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import com.apcpdcl.departmentapp.models.MeterChangeEntryModel;

import java.util.ArrayList;

public class MeterChangeDatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 2;

    // Database Name
    private static final String DATABASE_NAME = "meterChangeList";

    // Contacts table name
    private static final String TABLE_NAME = "meters";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_SERVICENO = "ServiceNo";
    private static final String KEY_STATUS = "Status";
    private static final String KEY_MSG = "MSG";
    private static final String KEY_MTRCHGDT = "MtrchgDt";
    private static final String KEY_OLDMTRNO = "Oldmtrno";
    private static final String KEY_OLDMTRKWH = "Oldmtrkwh";
    private static final String KEY_OLDMTRKVAH = "Oldmtrkvah";
    private static final String KEY_MTRCHFSLIP = "Mtrchfslip";
    private static final String KEY_NEWREMARKS = "Newremarks";
    private static final String KEY_OLDMTRRMD = "Oldmtrrmd";
    private static final String KEY_NEWMTRIRDA = "NewmtrIRDA";
    private static final String KEY_NEWMTRMAKE = "Newmtrmake";
    private static final String KEY_NEWMTRTYPE = "NewmtrType";
    private static final String KEY_NEWMTRNO = "Newmtrno";
    private static final String KEY_NEWMTRPHASE = "Newmtrphase";
    private static final String KEY_NEWMTRCURRENT = "Newmtrcurrent";
    private static final String KEY_NEWMTRCLASS = "Newmtrclass";
    private static final String KEY_NEWMTRDIGITS = "NewmtrDigits";
    private static final String KEY_NEWMTRMFGDT = "Newmtrmfgdt";
    private static final String KEY_NEWMTRKWH = "Newmtrkwh";
    private static final String KEY_NEWMTRKVAH = "Newmtrkvah";
    private static final String KEY_NEWMTRMF = "Newmtrmf";
    private static final String KEY_NEWMTRMRTSEAL1 = "NewmtrMRTSeal1";
    private static final String KEY_NEWMTRMRTSEAL2 = "NewmtrMRTSeal2";
    private static final String KEY_NEWMTRMRTSEAL3 = "NewmtrMRTSeal3";
    private static final String KEY_NEWMTRMRTSEAL4 = "NewmtrMRTSeal4";
    private static final String KEY_NEWMTRSEAL = "NewmtrSeal";
    private static final String KEY_LOGINUSERNAME = "LoginUserName";
    private static final String KEY_IMEI = "Imei";
    private static final String KEY_UNITCODE = "UnitCode";
    private static final String KEY_FINALDATASTRING = "finalDataString";
    private String CREATE_METERS_TABLE = "";

    public MeterChangeDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        CREATE_METERS_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_SERVICENO + " TEXT,"
                + KEY_MTRCHGDT + " TEXT,"
                + KEY_OLDMTRNO + " TEXT," + KEY_OLDMTRKWH + " TEXT,"
                + KEY_OLDMTRKVAH + " TEXT," + KEY_MTRCHFSLIP + " TEXT,"
                + KEY_NEWREMARKS + "  TEXT,"
                + KEY_OLDMTRRMD + "  TEXT," + KEY_NEWMTRIRDA + " TEXT,"
                + KEY_NEWMTRMAKE + "  TEXT," + KEY_NEWMTRTYPE + " TEXT,"
                + KEY_NEWMTRNO + "  TEXT," + KEY_NEWMTRPHASE + " TEXT,"
                + KEY_NEWMTRCURRENT + "  TEXT," + KEY_NEWMTRCLASS + " TEXT,"
                + KEY_NEWMTRDIGITS + "  TEXT," + KEY_NEWMTRMFGDT + " TEXT,"
                + KEY_NEWMTRKWH + "  TEXT," + KEY_NEWMTRKVAH + " TEXT,"
                + KEY_NEWMTRMF + "  TEXT," + KEY_NEWMTRMRTSEAL1 + " TEXT,"
                + KEY_NEWMTRMRTSEAL2 + " TEXT," + KEY_NEWMTRMRTSEAL3 + " TEXT,"
                + KEY_NEWMTRMRTSEAL4 + " TEXT,"
                + KEY_NEWMTRSEAL + "  TEXT," + KEY_LOGINUSERNAME + " TEXT,"
                + KEY_IMEI + "  TEXT," + KEY_UNITCODE + " TEXT,"
                + KEY_FINALDATASTRING + " TEXT,"
                + KEY_STATUS + "  TEXT," + KEY_MSG + " TEXT"
                + ")";
        db.execSQL(CREATE_METERS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        // Create tables again
        db.execSQL(CREATE_METERS_TABLE);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new contact
    public void saveModel(MeterChangeEntryModel meterChangeModel) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_SERVICENO, meterChangeModel.getServiceNo());
        values.put(KEY_MTRCHGDT, meterChangeModel.getMtrchgDt());
        values.put(KEY_OLDMTRNO, meterChangeModel.getOldmtrno());
        values.put(KEY_OLDMTRKWH, meterChangeModel.getOldmtrkwh());
        values.put(KEY_OLDMTRKVAH, meterChangeModel.getOldmtrkvah());
        values.put(KEY_MTRCHFSLIP, meterChangeModel.getMtrchfslip());
        values.put(KEY_NEWREMARKS, meterChangeModel.getNewremarks());
        values.put(KEY_OLDMTRRMD, meterChangeModel.getOldmtrrmd());
        values.put(KEY_NEWMTRIRDA, meterChangeModel.getNewmtrIRDA());
        values.put(KEY_NEWMTRMAKE, meterChangeModel.getNewmtrmake());
        values.put(KEY_NEWMTRTYPE, meterChangeModel.getNewmtrType());
        values.put(KEY_NEWMTRNO, meterChangeModel.getNewmtrno());
        values.put(KEY_NEWMTRPHASE, meterChangeModel.getNewmtrphase());
        values.put(KEY_NEWMTRCURRENT, meterChangeModel.getNewmtrcurrent());
        values.put(KEY_NEWMTRCLASS, meterChangeModel.getNewmtrclass());
        values.put(KEY_NEWMTRDIGITS, meterChangeModel.getNewmtrDigits());
        values.put(KEY_NEWMTRMFGDT, meterChangeModel.getNewmtrmfgdt());
        values.put(KEY_NEWMTRKWH, meterChangeModel.getNewmtrkwh());
        values.put(KEY_NEWMTRKVAH, meterChangeModel.getNewmtrkvah());
        values.put(KEY_NEWMTRMF, meterChangeModel.getNewmtrmf());
        values.put(KEY_NEWMTRMRTSEAL1, meterChangeModel.getNewmtrMRTSeal1());
        values.put(KEY_NEWMTRMRTSEAL2, meterChangeModel.getNewmtrMRTSeal2());
        values.put(KEY_NEWMTRMRTSEAL3, meterChangeModel.getNewmtrMRTSeal3());
        values.put(KEY_NEWMTRMRTSEAL4, meterChangeModel.getNewmtrMRTSeal4());
        values.put(KEY_NEWMTRSEAL, meterChangeModel.getNewmtrSeal());
        values.put(KEY_LOGINUSERNAME, meterChangeModel.getLoginUserName());
        values.put(KEY_IMEI, meterChangeModel.getImei());
        values.put(KEY_UNITCODE, meterChangeModel.getUnitCode());
        values.put(KEY_FINALDATASTRING, meterChangeModel.getFinalDataString());
        values.put(KEY_STATUS, meterChangeModel.getStatus());
        values.put(KEY_MSG, meterChangeModel.getMsg());

        // Inserting Row
        db.insert(TABLE_NAME, null, values);
        db.close(); // Closing database connection
    }

    public MeterChangeEntryModel fetchById(String scno) {
        SQLiteDatabase db = this.getWritableDatabase();
        MeterChangeEntryModel meterChangeModel = new MeterChangeEntryModel();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + KEY_SERVICENO + " = " + scno, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                meterChangeModel.setServiceNo(cursor.getString(cursor.getColumnIndex(KEY_SERVICENO)));
                meterChangeModel.setMtrchgDt(cursor.getString(cursor.getColumnIndex(KEY_MTRCHGDT)));
                meterChangeModel.setOldmtrno(cursor.getString(cursor.getColumnIndex(KEY_OLDMTRNO)));
                meterChangeModel.setOldmtrkwh(cursor.getString(cursor.getColumnIndex(KEY_OLDMTRKWH)));
                meterChangeModel.setOldmtrkvah(cursor.getString(cursor.getColumnIndex(KEY_OLDMTRKVAH)));
                meterChangeModel.setMtrchfslip(cursor.getString(cursor.getColumnIndex(KEY_MTRCHFSLIP)));
                meterChangeModel.setNewremarks(cursor.getString(cursor.getColumnIndex(KEY_NEWREMARKS)));
                meterChangeModel.setNewmtrIRDA(cursor.getString(cursor.getColumnIndex(KEY_NEWMTRIRDA)));
                meterChangeModel.setNewmtrType(cursor.getString(cursor.getColumnIndex(KEY_NEWMTRTYPE)));
                meterChangeModel.setNewmtrmake(cursor.getString(cursor.getColumnIndex(KEY_NEWMTRMAKE)));
                meterChangeModel.setNewmtrno(cursor.getString(cursor.getColumnIndex(KEY_NEWMTRNO)));
                meterChangeModel.setNewmtrphase(cursor.getString(cursor.getColumnIndex(KEY_NEWMTRPHASE)));
                meterChangeModel.setNewmtrcurrent(cursor.getString(cursor.getColumnIndex(KEY_NEWMTRCURRENT)));
                meterChangeModel.setNewmtrclass(cursor.getString(cursor.getColumnIndex(KEY_NEWMTRCLASS)));
                meterChangeModel.setNewmtrDigits(cursor.getString(cursor.getColumnIndex(KEY_NEWMTRDIGITS)));
                meterChangeModel.setNewmtrmfgdt(cursor.getString(cursor.getColumnIndex(KEY_NEWMTRMFGDT)));
                meterChangeModel.setNewmtrkwh(cursor.getString(cursor.getColumnIndex(KEY_NEWMTRKWH)));
                meterChangeModel.setNewmtrkvah(cursor.getString(cursor.getColumnIndex(KEY_NEWMTRKVAH)));
                meterChangeModel.setNewmtrmf(cursor.getString(cursor.getColumnIndex(KEY_NEWMTRMF)));
                meterChangeModel.setNewmtrMRTSeal1(cursor.getString(cursor.getColumnIndex(KEY_NEWMTRMRTSEAL1)));
                meterChangeModel.setNewmtrMRTSeal2(cursor.getString(cursor.getColumnIndex(KEY_NEWMTRMRTSEAL2)));
                meterChangeModel.setNewmtrMRTSeal3(cursor.getString(cursor.getColumnIndex(KEY_NEWMTRMRTSEAL3)));
                meterChangeModel.setNewmtrMRTSeal4(cursor.getString(cursor.getColumnIndex(KEY_NEWMTRMRTSEAL4)));
                meterChangeModel.setNewmtrSeal(cursor.getString(cursor.getColumnIndex(KEY_NEWMTRSEAL)));
                meterChangeModel.setLoginUserName(cursor.getString(cursor.getColumnIndex(KEY_LOGINUSERNAME)));
                meterChangeModel.setImei(cursor.getString(cursor.getColumnIndex(KEY_IMEI)));
                meterChangeModel.setUnitCode(cursor.getString(cursor.getColumnIndex(KEY_UNITCODE)));
                meterChangeModel.setFinalDataString(cursor.getString(cursor.getColumnIndex(KEY_FINALDATASTRING)));
                meterChangeModel.setStatus(cursor.getString(cursor.getColumnIndex(KEY_STATUS)));
                meterChangeModel.setMsg(cursor.getString(cursor.getColumnIndex(KEY_MSG)));
            }
        }
        return meterChangeModel;
    }

    // Getting All Contacts
    public ArrayList<MeterChangeEntryModel> getAllMeterChangeDetails() {
        ArrayList<MeterChangeEntryModel> contactList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                MeterChangeEntryModel meterChangeModel = new MeterChangeEntryModel();
                meterChangeModel.setServiceNo(cursor.getString(cursor.getColumnIndex(KEY_SERVICENO)));
                meterChangeModel.setMtrchgDt(cursor.getString(cursor.getColumnIndex(KEY_MTRCHGDT)));
                meterChangeModel.setOldmtrno(cursor.getString(cursor.getColumnIndex(KEY_OLDMTRNO)));
                meterChangeModel.setOldmtrkwh(cursor.getString(cursor.getColumnIndex(KEY_OLDMTRKWH)));
                meterChangeModel.setOldmtrkvah(cursor.getString(cursor.getColumnIndex(KEY_OLDMTRKVAH)));
                meterChangeModel.setMtrchfslip(cursor.getString(cursor.getColumnIndex(KEY_MTRCHFSLIP)));
                meterChangeModel.setNewremarks(cursor.getString(cursor.getColumnIndex(KEY_NEWREMARKS)));
                meterChangeModel.setOldmtrrmd(cursor.getString(cursor.getColumnIndex(KEY_OLDMTRRMD)));
                meterChangeModel.setNewmtrIRDA(cursor.getString(cursor.getColumnIndex(KEY_NEWMTRIRDA)));
                meterChangeModel.setNewmtrType(cursor.getString(cursor.getColumnIndex(KEY_NEWMTRTYPE)));
                meterChangeModel.setNewmtrmake(cursor.getString(cursor.getColumnIndex(KEY_NEWMTRMAKE)));
                meterChangeModel.setNewmtrno(cursor.getString(cursor.getColumnIndex(KEY_NEWMTRNO)));
                meterChangeModel.setNewmtrphase(cursor.getString(cursor.getColumnIndex(KEY_NEWMTRPHASE)));
                meterChangeModel.setNewmtrcurrent(cursor.getString(cursor.getColumnIndex(KEY_NEWMTRCURRENT)));
                meterChangeModel.setNewmtrclass(cursor.getString(cursor.getColumnIndex(KEY_NEWMTRCLASS)));
                meterChangeModel.setNewmtrDigits(cursor.getString(cursor.getColumnIndex(KEY_NEWMTRDIGITS)));
                meterChangeModel.setNewmtrmfgdt(cursor.getString(cursor.getColumnIndex(KEY_NEWMTRMFGDT)));
                meterChangeModel.setNewmtrkwh(cursor.getString(cursor.getColumnIndex(KEY_NEWMTRKWH)));
                meterChangeModel.setNewmtrkvah(cursor.getString(cursor.getColumnIndex(KEY_NEWMTRKVAH)));
                meterChangeModel.setNewmtrmf(cursor.getString(cursor.getColumnIndex(KEY_NEWMTRMF)));
                meterChangeModel.setNewmtrMRTSeal1(cursor.getString(cursor.getColumnIndex(KEY_NEWMTRMRTSEAL1)));
                meterChangeModel.setNewmtrMRTSeal2(cursor.getString(cursor.getColumnIndex(KEY_NEWMTRMRTSEAL2)));
                meterChangeModel.setNewmtrMRTSeal3(cursor.getString(cursor.getColumnIndex(KEY_NEWMTRMRTSEAL3)));
                meterChangeModel.setNewmtrMRTSeal4(cursor.getString(cursor.getColumnIndex(KEY_NEWMTRMRTSEAL4)));
                meterChangeModel.setNewmtrSeal(cursor.getString(cursor.getColumnIndex(KEY_NEWMTRSEAL)));
                meterChangeModel.setLoginUserName(cursor.getString(cursor.getColumnIndex(KEY_LOGINUSERNAME)));
                meterChangeModel.setImei(cursor.getString(cursor.getColumnIndex(KEY_IMEI)));
                meterChangeModel.setUnitCode(cursor.getString(cursor.getColumnIndex(KEY_UNITCODE)));
                meterChangeModel.setFinalDataString(cursor.getString(cursor.getColumnIndex(KEY_FINALDATASTRING)));
                meterChangeModel.setStatus(cursor.getString(cursor.getColumnIndex(KEY_STATUS)));
                meterChangeModel.setMsg(cursor.getString(cursor.getColumnIndex(KEY_MSG)));
                // Adding contact to list
                contactList.add(meterChangeModel);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }

    public ArrayList<String> getAllServiceNumbers() {

        ArrayList<String> arrayList = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            arrayList.add(cursor.getString(cursor.getColumnIndex(KEY_SERVICENO)));
            cursor.moveToNext();
        }
        return arrayList;

    }

    // Deleting single contact
    public void deleteContact(MeterChangeEntryModel contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_SERVICENO + " = ?",
                new String[]{String.valueOf(contact.getServiceNo())});
        db.close();
    }


    // Getting Meter Change request Count
    public int getMeterChangeRequestsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    // Getting Meter Change request Count excluding failed cases
    public int getMeterChangeRequestsCountExcludingFail() {
        String countQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE " + KEY_STATUS + " != 'F'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    // Updating single contact
    public void updateTotalModel(MeterChangeEntryModel meterChangeModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_SERVICENO, meterChangeModel.getServiceNo());
        values.put(KEY_MTRCHGDT, meterChangeModel.getMtrchgDt());
        values.put(KEY_OLDMTRNO, meterChangeModel.getOldmtrno());
        values.put(KEY_OLDMTRKWH, meterChangeModel.getOldmtrkwh());
        values.put(KEY_OLDMTRKVAH, meterChangeModel.getOldmtrkvah());
        values.put(KEY_MTRCHFSLIP, meterChangeModel.getMtrchfslip());
        values.put(KEY_NEWREMARKS, meterChangeModel.getNewremarks());
        values.put(KEY_OLDMTRRMD, meterChangeModel.getOldmtrrmd());
        values.put(KEY_NEWMTRIRDA, meterChangeModel.getNewmtrIRDA());
        values.put(KEY_NEWMTRMAKE, meterChangeModel.getNewmtrmake());
        values.put(KEY_NEWMTRTYPE, meterChangeModel.getNewmtrType());
        values.put(KEY_NEWMTRNO, meterChangeModel.getNewmtrno());
        values.put(KEY_NEWMTRPHASE, meterChangeModel.getNewmtrphase());
        values.put(KEY_NEWMTRCURRENT, meterChangeModel.getNewmtrcurrent());
        values.put(KEY_NEWMTRCLASS, meterChangeModel.getNewmtrclass());
        values.put(KEY_NEWMTRDIGITS, meterChangeModel.getNewmtrDigits());
        values.put(KEY_NEWMTRMFGDT, meterChangeModel.getNewmtrmfgdt());
        values.put(KEY_NEWMTRKWH, meterChangeModel.getNewmtrkwh());
        values.put(KEY_NEWMTRKVAH, meterChangeModel.getNewmtrkvah());
        values.put(KEY_NEWMTRMF, meterChangeModel.getNewmtrmf());
        values.put(KEY_NEWMTRMRTSEAL1, meterChangeModel.getNewmtrMRTSeal1());
        values.put(KEY_NEWMTRMRTSEAL2, meterChangeModel.getNewmtrMRTSeal2());
        values.put(KEY_NEWMTRMRTSEAL3, meterChangeModel.getNewmtrMRTSeal3());
        values.put(KEY_NEWMTRMRTSEAL4, meterChangeModel.getNewmtrMRTSeal4());
        values.put(KEY_NEWMTRSEAL, meterChangeModel.getNewmtrSeal());
        values.put(KEY_LOGINUSERNAME, meterChangeModel.getLoginUserName());
        values.put(KEY_IMEI, meterChangeModel.getImei());
        values.put(KEY_UNITCODE, meterChangeModel.getUnitCode());
        values.put(KEY_FINALDATASTRING, meterChangeModel.getFinalDataString());
        values.put(KEY_STATUS, meterChangeModel.getStatus());
        values.put(KEY_MSG, meterChangeModel.getMsg());
        // updating row
        db.update(TABLE_NAME, values, KEY_SERVICENO + " = ?", new String[]{String.valueOf(meterChangeModel.getServiceNo())});
    }
}
