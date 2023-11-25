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

import com.apcpdcl.departmentapp.activities.MatsDCListDashBoardActivity;
import com.apcpdcl.departmentapp.models.MatsConsumerModel;
import com.apcpdcl.departmentapp.utils.Utility;

import java.util.ArrayList;
import java.util.List;

public class MatsDatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 2;

    // Database Name
    private static final String DATABASE_NAME = "matsDcList";

    public static final String TABLE_MATS_DC_LIST = "MATSDCLIST";

    private static final String KEY_ID = "id";
    private static final String KEY_STYPE = "stype";
    private static final String KEY_CMCNAME = "cmcname";
    private static final String KEY_CMUSCNO = "cmuscno";
    private static final String KEY_TOTAL = "total";
    private static final String KEY_CMCAT = "cmcat";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_LAT = "lat";
    private static final String KEY_LONG = "long";
    private static final String KEY_LMCODE = "lmcode";
    private static final String KEY_POLENO = "poleno";
    private static final String KEY_EXCESS_LOAD = "excessload";
    private static final String KEY_CASENO = "caseno";
    private SQLiteDatabase db;
    private String CREATE_CONSUMERS_TABLE = "";

    public MatsDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        CREATE_CONSUMERS_TABLE = "CREATE TABLE " + TABLE_MATS_DC_LIST + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_CMCNAME + " TEXT," + KEY_CMUSCNO + " TEXT,"
                + KEY_TOTAL + " INTEGER," + KEY_CMCAT + " TEXT," + KEY_ADDRESS + " TEXT,"
                + KEY_LAT + " TEXT," + KEY_LONG + " TEXT," + KEY_EXCESS_LOAD + " INTEGER,"
                + KEY_CASENO + " TEXT," + KEY_LMCODE + " TEXT," + KEY_POLENO + " TEXT" + ")";
        db.execSQL(CREATE_CONSUMERS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_MATS_DC_LIST);
            db.execSQL(CREATE_CONSUMERS_TABLE);
        }
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */
    public void addConsumer(Context context, MatsConsumerModel consumer) {
        try {
            db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_CMCNAME, consumer.getCMCNAME());
            values.put(KEY_CMUSCNO, consumer.getCMUSCNO());
            values.put(KEY_TOTAL, consumer.getTOT());
            values.put(KEY_CMCAT, consumer.getCMCAT());
            values.put(KEY_ADDRESS, consumer.getCMADDRESS());
            values.put(KEY_LAT, consumer.getLAT());
            values.put(KEY_LONG, consumer.getLONG());
            values.put(KEY_EXCESS_LOAD, consumer.getEXECSSLOAD());
            values.put(KEY_CASENO, consumer.getCASENO());
            values.put(KEY_LMCODE, consumer.getLMCODE());
            values.put(KEY_POLENO, consumer.getPOLENO());
            // Inserting Row
            db.insert(TABLE_MATS_DC_LIST, null, values);
            db.close(); // Closing database connection
        } catch (SQLException s) {
            s.printStackTrace();
            Utility.showToastMessage(context, s.getMessage());
        }
    }

    public List<MatsConsumerModel> getConsumers() {
        List<MatsConsumerModel> allconsumerList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_MATS_DC_LIST + " " + "WHERE" + " " + KEY_LMCODE + " " + "=" + " " + "'" + MatsDCListDashBoardActivity.lmcode + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                MatsConsumerModel consumer = new MatsConsumerModel();
                consumer.setId(Integer.parseInt(cursor.getString(0)));
                consumer.setCMCNAME(cursor.getString(1));
                consumer.setCMUSCNO(cursor.getString(2));
                consumer.setTOT(cursor.getInt(3));
                consumer.setCMCAT(cursor.getString(4));
                consumer.setCMADDRESS(cursor.getString(5));
                consumer.setLAT(cursor.getString(6));
                consumer.setLONG(cursor.getString(7));
                consumer.setEXECSSLOAD(cursor.getInt(8));
                consumer.setCASENO(cursor.getString(9));
                consumer.setLMCODE(cursor.getString(10));
                consumer.setPOLENO(cursor.getString(11));

                allconsumerList.add(consumer);
            } while (cursor.moveToNext());
        }

        return allconsumerList;
    }

    public void clearTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MATS_DC_LIST, null, null);
    }


    public List<MatsConsumerModel> getAllPoles() {
        List<MatsConsumerModel> dtrCodeList = new ArrayList<MatsConsumerModel>();
        // Select All Query
        String selectQuery = "SELECT  DISTINCT" + " " + KEY_POLENO + " " + "FROM " + TABLE_MATS_DC_LIST + " " + "WHERE" + " " + KEY_LMCODE + " " + "=" + " " + "'" + MatsDCListDashBoardActivity.lmcode + "'" + " " + "ORDER BY" + " " + KEY_POLENO;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                MatsConsumerModel dtrCodeConsumer = new MatsConsumerModel();
                dtrCodeConsumer.setPOLENO(cursor.getString(0));
                dtrCodeList.add(dtrCodeConsumer);
            } while (cursor.moveToNext());
        }

        return dtrCodeList;
    }


    public void updateDCList(String CMUSCNO, String MLAT, String MLONG) {
        // Select All Query
        try {
            db = this.getWritableDatabase();
        } catch (SQLException s) {
            s.printStackTrace();
        }
        String selectQuery = "UPDATE" + " " + TABLE_MATS_DC_LIST + " " + "SET" + " " + KEY_LAT + " " + "=" + " " + "'" + MLAT + "'" + " ," + " " + KEY_LONG + " " + "=" + " " + "'" + MLONG + "'" + " " + "WHERE" + " " + KEY_CMUSCNO + " " + "=" + " " + "'" + CMUSCNO + "'";
        db.execSQL(selectQuery);
        db.close();
    }
}
