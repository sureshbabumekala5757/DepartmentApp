package com.apcpdcl.departmentapp.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import com.apcpdcl.departmentapp.activities.OperatingAllDetails;
import com.apcpdcl.departmentapp.models.PendingData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 22-02-2018.
 */

public class PendingDatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "pendingListManager";

    private static final String TABLE_PENDINGLIST = "pendingList";

    private static final String KEY_ID = "id";
    private static final String KEY_CMUSCNO = "cmuscno";
    private static final String KEY_TOTALAMOUNT = "totalamnt";
    private static final String KEY_STATUS = "status";
    private static final String KEY_PENDINGAMOUNT= "pendingamnt";
    private static final String KEY_TYPE= "type";
    private static final String KEY_CASENO= "casenum";
    SQLiteDatabase db;
    private String CREATE_CONSUMERS_TABLE = "";

    public PendingDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        CREATE_CONSUMERS_TABLE = "CREATE TABLE " + TABLE_PENDINGLIST + "(" + KEY_ID + " INTEGER PRIMARY KEY,"+ KEY_CMUSCNO + " TEXT,"
                + KEY_TOTALAMOUNT + " TEXT," + KEY_STATUS + " TEXT," + KEY_PENDINGAMOUNT + " TEXT,"
                + KEY_TYPE + " TEXT,"  + KEY_CASENO + " TEXT"  +")";
        db.execSQL(CREATE_CONSUMERS_TABLE);
    }
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PENDINGLIST);

        // Create tables again
        db.execSQL(CREATE_CONSUMERS_TABLE);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    public void addPendingData(PendingData pendingData) {
        try {
            db = this.getWritableDatabase();
        } catch (SQLException s) {
            new Exception("Error with DB Open");
        }

        ContentValues values = new ContentValues();
        values.put(KEY_CMUSCNO, pendingData.getCmuscno());
        values.put(KEY_TOTALAMOUNT, pendingData.getTotAmnt());
        values.put(KEY_STATUS, pendingData.getStatus());
        values.put(KEY_PENDINGAMOUNT, pendingData.getPendingAmnt());
        values.put(KEY_TYPE, pendingData.getType());
        values.put(KEY_CASENO, pendingData.getCaseNum());


        // Inserting Row
        db.insert(TABLE_PENDINGLIST, null, values);
        db.close(); // Closing database connection
    }


    public List<PendingData> getAllPendingData() {
        List<PendingData> pendingDataList = new ArrayList<PendingData>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_PENDINGLIST + " " + "WHERE" + " " + KEY_CMUSCNO + " " + "=" + " " + "'" + OperatingAllDetails.strcmuscno+ "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                PendingData pendingData = new PendingData();
                pendingData.setID(Integer.parseInt(cursor.getString(0)));
                pendingData.setCmuscno(cursor.getString(1));
                pendingData.setTotAmnt(cursor.getString(2));
                pendingData.setStatus(cursor.getString(3));
                pendingData.setPendingAmnt(cursor.getString(4));
                pendingData.setType(cursor.getString(5));
                pendingData.setCaseNum(cursor.getString(6));

                pendingDataList.add(pendingData);
            } while (cursor.moveToNext());
        }

        return pendingDataList;
    }

    public void clearTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PENDINGLIST, null, null);
    }

}
