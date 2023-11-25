package com.apcpdcl.departmentapp.sqlite;

/*
 * Created by Admin on 18-12-2017.
 */


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.apcpdcl.departmentapp.activities.Home;
import com.apcpdcl.departmentapp.models.Consumer;
import com.apcpdcl.departmentapp.utils.Utility;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 5;

    // Database Name
    private static final String DATABASE_NAME = "DCList";

    public static final String TABLE_CONSUMERS = "consumers";

    private static final String KEY_ID = "id";
    private static final String KEY_BLDISCDT = "bldiscdt";
    private static final String KEY_STYPE = "stype";
    private static final String KEY_CMCNAME = "cmcname";
    private static final String KEY_MTRNO = "meternum";
    private static final String KEY_CMUSCNO = "cmuscno";
    private static final String KEY_TOTAL = "total";
    private static final String KEY_LASTPAYDT = "lastpaydt";
    private static final String KEY_SOCIALCAT = "cmsocialcat";
    private static final String KEY_CMCAT = "cmcat";
    private static final String KEY_GOVCAT = "govcat";
    private static final String KEY_LASTPAIDAMT = "lastpaidamnt";
    private static final String KEY_CMDTRCODE = "cmdtrcode";
    private static final String KEY_GRP = "grp";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_LOCATION = "location";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_LAT = "lat";
    private static final String KEY_LONG = "long";
    private static final String KEY_LMCODE = "lmcode";
    private static final String KEY_POLENO = "poleno";
    private SQLiteDatabase db;
    private String CREATE_CONSUMERS_TABLE = "";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        CREATE_CONSUMERS_TABLE = "CREATE TABLE " + TABLE_CONSUMERS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_BLDISCDT + " TEXT," + KEY_STYPE + " TEXT,"
                + KEY_CMCNAME + " TEXT," + KEY_MTRNO + " TEXT," + KEY_CMUSCNO + " TEXT,"
                + KEY_TOTAL + " INTEGER," + KEY_LASTPAYDT + " TEXT," + KEY_SOCIALCAT + " TEXT," + KEY_CMCAT + " TEXT,"
                + KEY_GOVCAT + " TEXT," + KEY_LASTPAIDAMT + " INTEGER," + KEY_CMDTRCODE + " TEXT," + KEY_GRP + " TEXT," + KEY_ADDRESS + " TEXT,"
                + KEY_LOCATION + " TEXT," + KEY_PHONE + " TEXT," + KEY_LAT + " TEXT," + KEY_LONG + " TEXT," + KEY_LMCODE + " TEXT," + KEY_POLENO + " TEXT" + ")";
        db.execSQL(CREATE_CONSUMERS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONSUMERS);
            db.execSQL(CREATE_CONSUMERS_TABLE);
        }

    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    public void addConsumer(Context context,Consumer consumer) {
        try {
            db = this.getWritableDatabase();


            ContentValues values = new ContentValues();
            values.put(KEY_BLDISCDT, consumer.getBldiscdt());
            values.put(KEY_STYPE, consumer.getStype());
            values.put(KEY_CMCNAME, consumer.getCmcName());
            values.put(KEY_MTRNO, consumer.getMeterNum());
            values.put(KEY_CMUSCNO, consumer.getCmuscno());
            values.put(KEY_TOTAL, consumer.getTotal());
            values.put(KEY_LASTPAYDT, consumer.getLastpaydt());
            values.put(KEY_SOCIALCAT, consumer.getSocialCat());
            values.put(KEY_CMCAT, consumer.getCmcat());
            values.put(KEY_GOVCAT, consumer.getGovtCat());
            values.put(KEY_LASTPAIDAMT, consumer.getLastPayAmnt());
            values.put(KEY_CMDTRCODE, consumer.getCmdtrCode());
            values.put(KEY_GRP, consumer.getGrp());
            values.put(KEY_ADDRESS, consumer.getAddr());
            values.put(KEY_LOCATION, consumer.getLoc());
            values.put(KEY_PHONE, consumer.getPhone());
            values.put(KEY_LAT, consumer.getLat());
            values.put(KEY_LONG, consumer.getLong());
            values.put(KEY_LMCODE, consumer.getLmcode());
            values.put(KEY_POLENO, consumer.getPoleNo());


            // Inserting Row
            db.insert(TABLE_CONSUMERS, null, values);
            db.close(); // Closing database connection
        } catch (SQLException s) {
            s.printStackTrace();
            Utility.showToastMessage(context,s.getMessage());
        }

    }

    public List<Consumer> getConsumers(String sType) {
        List<Consumer> allconsumerList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CONSUMERS + " " + "WHERE" + " " + KEY_LMCODE + " " + "=" + " " + "'" + Home.lmcode + "'"
                + "AND" + " " + KEY_STYPE + " " + " = " + "'" + sType + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Consumer consumer = new Consumer();
                consumer.setID(Integer.parseInt(cursor.getString(0)));
                consumer.setBldiscdt(cursor.getString(1));
                consumer.setStype(cursor.getString(2));
                consumer.setCmcName(cursor.getString(3));
                consumer.setMeterNum(cursor.getString(4));
                consumer.setCmuscno(cursor.getString(5));
                consumer.setTotal(cursor.getInt(6));
                consumer.setLastpaydt(cursor.getString(7));
                consumer.setSocialCat(cursor.getString(8));
                consumer.setCmcat(cursor.getString(9));
                consumer.setGovtCat(cursor.getString(10));
                consumer.setLastPayAmnt(cursor.getInt(11));
                consumer.setCmdtrCode(cursor.getString(12));
                consumer.setGrp(cursor.getString(13));
                consumer.setAddr(cursor.getString(14));
                consumer.setLoc(cursor.getString(15));
                consumer.setPhone(cursor.getString(16));
                consumer.setLat(cursor.getString(17));
                consumer.setLong(cursor.getString(18));
                consumer.setLmcode(cursor.getString(19));
                consumer.setPoleNo(cursor.getString(20));

                allconsumerList.add(consumer);
            } while (cursor.moveToNext());
        }

        return allconsumerList;
    }


    public void clearTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONSUMERS, null, null);
    }

    public List<Consumer> getAllSocialCat() {
        List<Consumer> SocialCatList = new ArrayList<Consumer>();
        // Select All Query
        String selectQuery = "SELECT  DISTINCT" + " " + KEY_SOCIALCAT + " " + "FROM " + TABLE_CONSUMERS + " " + "WHERE" + " " + KEY_LMCODE + " " + "=" + " " + "'" + Home.lmcode + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Consumer socialtcatConsumer = new Consumer();
                socialtcatConsumer.setSocialCat(cursor.getString(0));
                SocialCatList.add(socialtcatConsumer);
            } while (cursor.moveToNext());
        }

        return SocialCatList;
    }

    public List<Consumer> getAllGovtCat() {
        List<Consumer> GovCatList = new ArrayList<Consumer>();
        // Select All Query
        String selectQuery = "SELECT  DISTINCT" + " " + KEY_GOVCAT + " " + "FROM " + TABLE_CONSUMERS + " " + "WHERE" + " " + KEY_LMCODE + " " + "=" + " " + "'" + Home.lmcode + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Consumer govtcatConsumer = new Consumer();
                govtcatConsumer.setGovtCat(cursor.getString(0));
                GovCatList.add(govtcatConsumer);
            } while (cursor.moveToNext());
        }

        return GovCatList;
    }

    public List<Consumer> getAllServiceType() {
        List<Consumer> sTypeList = new ArrayList<Consumer>();
        // Select All Query
        String selectQuery = "SELECT  DISTINCT" + " " + KEY_STYPE + " " + "FROM " + TABLE_CONSUMERS + " " + "WHERE" + " " + KEY_LMCODE + " " + "=" + " " + "'" + Home.lmcode + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Consumer stypeConsumer = new Consumer();
                stypeConsumer.setStype(cursor.getString(0));
                sTypeList.add(stypeConsumer);
            } while (cursor.moveToNext());
        }

        return sTypeList;
    }

    public List<Consumer> getAllDiscDates() {
        List<Consumer> discDateList = new ArrayList<Consumer>();
        // Select All Query
        String selectQuery = "SELECT  DISTINCT" + " " + KEY_BLDISCDT + " " + "FROM " + TABLE_CONSUMERS + " " + "WHERE" + " " + KEY_LMCODE + " " + "=" + " " + "'" + Home.lmcode + "'" + " " + "ORDER BY" + " " + KEY_BLDISCDT;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Consumer discdateConsumer = new Consumer();
                discdateConsumer.setBldiscdt(cursor.getString(0));
                discDateList.add(discdateConsumer);
            } while (cursor.moveToNext());
        }

        return discDateList;
    }

    public List<Consumer> getAllDtrCodes() {
        List<Consumer> dtrCodeList = new ArrayList<Consumer>();
        // Select All Query
        String selectQuery = "SELECT  DISTINCT" + " " + KEY_CMDTRCODE + " " + "FROM " + TABLE_CONSUMERS + " " + "WHERE" + " " + KEY_LMCODE + " " + "=" + " " + "'" + Home.lmcode + "'" + " " + "ORDER BY" + " " + KEY_CMDTRCODE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Consumer dtrCodeConsumer = new Consumer();
                dtrCodeConsumer.setCmdtrCode(cursor.getString(0));
                dtrCodeList.add(dtrCodeConsumer);
            } while (cursor.moveToNext());
        }

        return dtrCodeList;
    }

    public List<Consumer> getAllPoles() {
        List<Consumer> dtrCodeList = new ArrayList<Consumer>();
        // Select All Query
        String selectQuery = "SELECT  DISTINCT" + " " + KEY_POLENO + " " + "FROM " + TABLE_CONSUMERS + " " + "WHERE" + " " + KEY_LMCODE + " " + "=" + " " + "'" + Home.lmcode + "'" + " " + "ORDER BY" + " " + KEY_POLENO;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Consumer dtrCodeConsumer = new Consumer();
                dtrCodeConsumer.setPoleNo(cursor.getString(0));
                dtrCodeList.add(dtrCodeConsumer);
            } while (cursor.moveToNext());
        }

        return dtrCodeList;
    }

    public List<Consumer> getAllGroups() {
        List<Consumer> groupList = new ArrayList<Consumer>();
        // Select All Query
        String selectQuery = "SELECT  DISTINCT" + " " + KEY_GRP + " " + "FROM " + TABLE_CONSUMERS + " " + "WHERE" + " " + KEY_LMCODE + " " + "=" + " " + "'" + Home.lmcode + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Consumer grpConsumer = new Consumer();
                grpConsumer.setGrp(cursor.getString(0));
                groupList.add(grpConsumer);
            } while (cursor.moveToNext());
        }

        return groupList;
    }

    public List<Consumer> getAllCats(String sType) {
        List<Consumer> catList = new ArrayList<Consumer>();
        // Select All Query
        String selectQuery = "SELECT  DISTINCT" + " " + KEY_CMCAT + " " + "FROM " + TABLE_CONSUMERS + " " + "WHERE" + " " + KEY_LMCODE + " " + "=" + " " + "'" + Home.lmcode + "'" + "AND" + " " +
                KEY_STYPE + " " + "=" + " " + "'" + sType + "'" + " " + "ORDER BY" + " " + KEY_CMCAT;
        //String selectQuery = "SELECT  DISTINCT" + " " + KEY_CMCAT + " " + "FROM " + TABLE_CONSUMERS + " " + "WHERE" + " " + KEY_LMCODE + " " + "=" + " " + "'" + Home.lmcode + "'" + " " + "ORDER BY" + " " + KEY_CMCAT;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Consumer catConsumer = new Consumer();
                catConsumer.setCmcat(cursor.getString(0));
                catList.add(catConsumer);
            } while (cursor.moveToNext());
        }

        return catList;
    }


    public List<Consumer> getAllFilterConsumers(String totalString) {
        List<Consumer> filterConsumerList = new ArrayList<Consumer>();
        // Select All Query
        String selectQuery = "SELECT * FROM" + " " + TABLE_CONSUMERS + " " + "WHERE" + " " + totalString;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Consumer filterconsumer = new Consumer();
                filterconsumer.setID(Integer.parseInt(cursor.getString(0)));
                filterconsumer.setBldiscdt(cursor.getString(1));
                filterconsumer.setStype(cursor.getString(2));
                filterconsumer.setCmcName(cursor.getString(3));
                filterconsumer.setMeterNum(cursor.getString(4));
                filterconsumer.setCmuscno(cursor.getString(5));
                filterconsumer.setTotal(cursor.getInt(6));
                filterconsumer.setLastpaydt(cursor.getString(7));
                filterconsumer.setSocialCat(cursor.getString(8));
                filterconsumer.setCmcat(cursor.getString(9));
                filterconsumer.setGovtCat(cursor.getString(10));
                filterconsumer.setLastPayAmnt(cursor.getInt(11));
                filterconsumer.setCmdtrCode(cursor.getString(12));
                filterconsumer.setGrp(cursor.getString(13));
                filterconsumer.setAddr(cursor.getString(14));
                filterconsumer.setLoc(cursor.getString(15));
                filterconsumer.setPhone(cursor.getString(16));
                filterconsumer.setLat(cursor.getString(17));
                filterconsumer.setLong(cursor.getString(18));
                filterconsumer.setLmcode(cursor.getString(19));
                filterconsumer.setPoleNo(cursor.getString(20));
                filterConsumerList.add(filterconsumer);
            } while (cursor.moveToNext());
        }

        return filterConsumerList;
    }

    public void updateDCList(String CMUSCNO, String MLAT, String MLONG) {
        // Select All Query
        try {
            db = this.getWritableDatabase();
        } catch (SQLException s) {
            s.printStackTrace();
        }
        String selectQuery = "UPDATE" + " " + TABLE_CONSUMERS + " " + "SET" + " " + KEY_LAT + " " + "=" + " " + "'" + MLAT + "'" + " ," + " " + KEY_LONG + " " + "=" + " " + "'" + MLONG + "'" + " " + "WHERE" + " " + KEY_CMUSCNO + " " + "=" + " " + "'" + CMUSCNO + "'";
        db.execSQL(selectQuery);
        db.close();
    }
}
