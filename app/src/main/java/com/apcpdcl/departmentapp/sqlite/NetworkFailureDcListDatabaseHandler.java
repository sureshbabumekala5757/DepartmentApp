package com.apcpdcl.departmentapp.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import com.apcpdcl.departmentapp.models.FailureDcList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 05-02-2018.
 */

public class NetworkFailureDcListDatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 3;

    // Database Name
    private static final String DATABASE_NAME = "TotalFailureDcListManager";

    private static final String TABLE_FAILUREDCLIST = "FailureDcList";

    private static final String KEY_ID = "id";
    private static final String KEY_TOTALSTRING = "totalstring";
    SQLiteDatabase db;
    private  String CREATE_DCFAILURELIST_TABLE = "";


    public NetworkFailureDcListDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        CREATE_DCFAILURELIST_TABLE = "CREATE TABLE " + TABLE_FAILUREDCLIST + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TOTALSTRING + " TEXT" + ")";
        db.execSQL(CREATE_DCFAILURELIST_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAILUREDCLIST);

        // Create tables again
        db.execSQL(CREATE_DCFAILURELIST_TABLE);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    public void addFailureDcList(FailureDcList failuredclist) {
        try {
            db = this.getWritableDatabase();
        } catch (SQLException s) {
            new Exception("Error with DB Open");
        }

        ContentValues values = new ContentValues();
        values.put(KEY_TOTALSTRING, failuredclist.getTotalString());

        // Inserting Row
        db.insert(TABLE_FAILUREDCLIST, null, values);
        db.close(); // Closing database connection
    }


    public List<FailureDcList> getAllFailuredclists() {
        List<FailureDcList> failureDcListArrayList = new ArrayList<FailureDcList>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_FAILUREDCLIST;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                FailureDcList failuredclist = new FailureDcList();
                failuredclist.setID(Integer.parseInt(cursor.getString(0)));
                failuredclist.setTotalString(cursor.getString(1));
                failureDcListArrayList.add(failuredclist);
            } while (cursor.moveToNext());
        }

        return failureDcListArrayList;
    }

    public void clearTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FAILUREDCLIST, null, null);
    }


}

