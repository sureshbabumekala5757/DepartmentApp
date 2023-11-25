package com.apcpdcl.departmentapp.utils;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.CalendarContract;
import androidx.core.app.ActivityCompat;

import java.util.Calendar;
import java.util.TimeZone;

public class AddEventToCalender {
    private Context context;

    public AddEventToCalender(Context context) {
        this.context = context;
    }

    public void addEvent(boolean isNonSlab) {
        Calendar calStart = Calendar.getInstance();
        Calendar calEnd = Calendar.getInstance();
        calEnd.add(Calendar.HOUR_OF_DAY, 2);

        ContentResolver cr = context.getContentResolver();
        ContentValues values = new ContentValues();
        TimeZone timeZone = TimeZone.getDefault();
        values.put(CalendarContract.Events.DTSTART, calStart.getTimeInMillis());
        values.put(CalendarContract.Events.DTEND, calEnd.getTimeInMillis());
        values.put(CalendarContract.Events.EVENT_TIMEZONE, timeZone.getID());
        values.put(CalendarContract.Events.TITLE, "Event Title");
        values.put(CalendarContract.Events.DESCRIPTION, "Event Description");
        values.put(CalendarContract.Events.CALENDAR_ID, 1);
        values.put("rrule", "FREQ=MONTHLY");


        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Uri uri1 = cr.insert(CalendarContract.Events.CONTENT_URI, values);
        String eventID = uri1.getLastPathSegment();
        Utility.showLog("Event Id", eventID);
    }


}
