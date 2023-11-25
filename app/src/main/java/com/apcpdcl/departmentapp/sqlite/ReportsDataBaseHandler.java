package com.apcpdcl.departmentapp.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import com.apcpdcl.departmentapp.activities.DailyReports;
import com.apcpdcl.departmentapp.models.ReportsList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 17-02-2018.
 */

public class ReportsDataBaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "reportsListManager";

    private static final String TABLE_REPORTSLIST = "ReportsList";

    private static final String KEY_ID = "id";
    private static final String KEY_SCNO = "scno";
    private static final String KEY_STATUS = "status";
    private static final String KEY_DATE = "date";
    SQLiteDatabase db;
    private String CREATE_REPORTSLIST_TABLE ="";


    public ReportsDataBaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        CREATE_REPORTSLIST_TABLE = "CREATE TABLE " + TABLE_REPORTSLIST + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_SCNO + " TEXT,"
                + KEY_STATUS + " TEXT," + KEY_DATE + " TEXT"
                + ")";
        db.execSQL(CREATE_REPORTSLIST_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REPORTSLIST);

        // Create tables again
        db.execSQL(CREATE_REPORTSLIST_TABLE);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    public void addReportsList(ReportsList reportsList) {
        try {
            db = this.getWritableDatabase();

        } catch (SQLException s) {
            new Exception("Error with DB Open");
        }
        ContentValues values = new ContentValues();
        values.put(KEY_SCNO, reportsList.getCmuscno());
        values.put(KEY_STATUS, reportsList.getStatus());
        values.put(KEY_DATE, reportsList.getDate());


        // Inserting Row
        db.insert(TABLE_REPORTSLIST, null, values);
        db.close(); // Closing database connection
    }


    public List<ReportsList> getAllreportslists() {
        List<ReportsList> reportArrayList = new ArrayList<ReportsList>();
        // Select All Query
        String selectQuery = "SELECT * FROM" + " " + TABLE_REPORTSLIST + " " + "WHERE" + " " + "SUBSTR" + "(" + KEY_DATE + ",4,3)" + " " + "=" + " " + "'" + DailyReports.month_string + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ReportsList reportlist = new ReportsList();
                reportlist.setID(Integer.parseInt(cursor.getString(0)));
                reportlist.setCmuscno(cursor.getString(1));
                reportlist.setStatus(cursor.getString(2));
                reportlist.setDate(cursor.getString(3));
                reportArrayList.add(reportlist);
            } while (cursor.moveToNext());
        }

        return reportArrayList;
    }

    public int getDeleteCount() {
        String selectQuery = "DELETE FROM" + " " + TABLE_REPORTSLIST + " " + "WHERE" + " " + "SUBSTR" + "(" + KEY_DATE + ",4,3)" + " " + "<>" + " " + "'" + DailyReports.month_string + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public List<ReportsList> getAllServiceNum() {
        List<ReportsList> sNumList = new ArrayList<ReportsList>();
        // Select All Query
        String selectQuery = "SELECT  DISTINCT" + " " + KEY_SCNO + " " + "FROM " + TABLE_REPORTSLIST;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ReportsList sNumreportlist = new ReportsList();
                sNumreportlist.setCmuscno(cursor.getString(0));
                sNumList.add(sNumreportlist);
            } while (cursor.moveToNext());
        }

        return sNumList;
    }
}



