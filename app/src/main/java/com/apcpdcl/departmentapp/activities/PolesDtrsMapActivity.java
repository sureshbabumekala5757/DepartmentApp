/*
package com.apspdcl.departmentapp.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.apspdcl.departmentapp.R;
import com.apspdcl.departmentapp.services.NetworkReceiver;
import com.apspdcl.departmentapp.utils.Utility;
import com.apspdcl.departmentapp.utils.Utils;
import com.esri.arcgisruntime.ArcGISRuntimeException;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.layers.ArcGISTiledLayer;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.view.DefaultMapViewOnTouchListener;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.LocationDisplay;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol;
import com.google.gson.JsonArray;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PolesDtrsMapActivity extends AppCompatActivity {

    private MapView mPolesMapView, mDtrsMapView;
    private LocationDisplay mLocationDisplay;
    GraphicsOverlay pgraphicsOverlay = new GraphicsOverlay();
    GraphicsOverlay dgraphicsOverlay = new GraphicsOverlay();
    String str_poles, str_dtrs, I_POSID, I_AUFNR, LV_KOSTL;
    int selectedpoles = 0, maxpoles, minpoles, selecteddtrs = 0, maxdtrs, mindtrs,poles,dtrs;

    Map<String, String> poleDetails = new HashMap<>();

    Map<String, String> dtrDetails = new HashMap<>();

   JSONArray wholeJsonArray = new JSONArray();

    Graphic pinitialGraphic, dinitialGraphic;
    Button submitMapbtn;
    NetworkReceiver objNetworkReceiver = new NetworkReceiver();
    ProgressDialog pDialog;
    StringEntity entity;
    TextView poles_count,dtrs_count;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.poleslinesmap_layout);

        mPolesMapView = (MapView) findViewById(R.id.polesmapView);
        mDtrsMapView = (MapView) findViewById(R.id.dtrsmapView);
        setupMap(mPolesMapView);
        setupMap(mDtrsMapView);
        setupLocationDisplay(mPolesMapView);
        setupLocationDisplay(mDtrsMapView);

        submitMapbtn = (Button) findViewById(R.id.submitmapbtn);
        poles_count = (TextView) findViewById(R.id.poles_count);
        dtrs_count = (TextView) findViewById(R.id.dtrs_count);
        pDialog = new ProgressDialog(this);
        */
/*str_dtrs="0";
        str_poles="0";*//*


        Intent intent = getIntent();
        Bundle bd = intent.getExtras();
        if (bd != null) {

            str_poles = (String) bd.get("poles");
            str_dtrs = (String) bd.get("dtrs");
            I_POSID = (String) bd.get("I_POSID");
            I_AUFNR = (String) bd.get("I_AUFNR");
            LV_KOSTL = (String) bd.get("LV_KOSTL");
        }

        if (!Utils.IsNullOrBlank(str_poles)) {
            poles=Integer.parseInt(str_poles);
        }else{
            poles=0;
        }
        if (!Utils.IsNullOrBlank(str_dtrs)) {
            dtrs=Integer.parseInt(str_dtrs);
        }else{
            dtrs=0;
        }

        maxpoles =poles + 3;
        maxdtrs = dtrs + 2;


        if (poles <= 5) {
            minpoles =poles;
        } else if (dtrs <= 5) {
            mindtrs = dtrs;
        } else {
            minpoles = poles - 2;
            mindtrs = dtrs - 1;
        }


        mPolesMapView.getGraphicsOverlays().add(pgraphicsOverlay);
        SimpleMarkerSymbol symbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, Color.RED,
                12);

        //add a new graphic with a new point geometry
        Point graphicPoint = new Point(0, 0, SpatialReferences.getWebMercator());
        pinitialGraphic = new Graphic(graphicPoint, symbol);
        pgraphicsOverlay.getGraphics().add(pinitialGraphic);


        mPolesMapView.setOnTouchListener(new ShowCoordinatesMapPolesTouchListener(this, mPolesMapView));


        mDtrsMapView.getGraphicsOverlays().add(dgraphicsOverlay);
        SimpleMarkerSymbol dtrsymbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, Color.BLUE,
                12);

        //add a new graphic with a new point geometry
        Point dtrgraphicPoint = new Point(0, 0, SpatialReferences.getWebMercator());
        dinitialGraphic = new Graphic(dtrgraphicPoint, dtrsymbol);
        dgraphicsOverlay.getGraphics().add(dinitialGraphic);


        mDtrsMapView.setOnTouchListener(new ShowCoordinatesMapDtrsTouchListener(this, mDtrsMapView));





        submitMapbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (objNetworkReceiver.hasInternetConnection(getApplicationContext())) {
                        if (selectedpoles == 0 && selecteddtrs == 0) {
                           */
/* Utility.showCustomOKOnlyDialog(PolesDtrsMapActivity.this, "Please Select Poles and Dtrs");*//*

                            Toast.makeText(getApplicationContext(), "Please Select Poles and Dtrs", Toast.LENGTH_LONG).show();
                        } else if (selectedpoles < minpoles) {
                          */
/*  Utility.showCustomOKOnlyDialog(PolesDtrsMapActivity.this, "Selected Poles are less than Minimum Poles.");*//*

                            Toast.makeText(getApplicationContext(),  "Selected Poles are less than Minimum Poles.", Toast.LENGTH_LONG).show();
                        } else if (selecteddtrs < mindtrs) {
                           */
/* Utility.showCustomOKOnlyDialog(PolesDtrsMapActivity.this, "Selected Dtrs are less than Minimum Dtrs.");*//*

                            Toast.makeText(getApplicationContext(),  "Selected Dtrs are less than Minimum Dtrs.", Toast.LENGTH_LONG).show();
                        } else if (selectedpoles - 2 <= 0 || selecteddtrs - 1 <= 0) {
                           */
/* Utility.showCustomOKOnlyDialog(PolesDtrsMapActivity.this, "Selected Poles are less than Minimum Poles (or) " +
                                    "Selected Dtrs are less than Minimum Dtrs.");*//*

                            Toast.makeText(getApplicationContext(),   "Selected Poles are less than Minimum Poles (or) " +
                                    "Selected Dtrs are less than Minimum Dtrs.", Toast.LENGTH_LONG).show();
                        } else if (selectedpoles - 2 > 0 || selecteddtrs - 1 > 0) {
                            invokeDataPushing();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Please check your Internet connection", Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });


    }

    private void invokeDataPushing() {
        JSONObject json = new JSONObject();
        try {
            json.put("i_POSID", I_POSID);
            json.put("i_AUFNR", I_AUFNR);
            json.put("LV_KOSTL", LV_KOSTL);
            json.put("coordnates",wholeJsonArray);
           */
/* entity = new StringEntity(json.toString().replaceAll("\\\\",""));*//*

            entity = new StringEntity(json.toString());
        } catch (JSONException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        pDialog.show();
        pDialog.setMessage("Please wait...");

        AsyncHttpClient client = new AsyncHttpClient();

        client.post(getApplicationContext(), "http://122.252.251.175:2020/SapReport/JavaCodeGeeks/SapCoordnateService/MobileDataPushing", entity,
                "application/json", new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(String response) {
                        Utility.showLog("response", response);
                        if (pDialog != null && pDialog.isShowing()) {
                            pDialog.dismiss();
                        }
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String str_status=jsonObject.getString("status");
                            if(str_status.equalsIgnoreCase("sucessfull")) {
                                Toast.makeText(getApplicationContext(), str_status, Toast.LENGTH_LONG).show();
                                Intent in = new Intent(PolesDtrsMapActivity.this, PolesDtrsActivity.class);
                                startActivity(in);
                            }else{
                                Toast.makeText(getApplicationContext(), str_status, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Throwable error, String content) {
                        if (pDialog != null && pDialog.isShowing()) {
                            pDialog.dismiss();
                        }
                        switch (statusCode) {
                            case 404:
                                Utility.showCustomOKOnlyDialog(PolesDtrsMapActivity.this, "Unable to Connect Server");
                                break;
                            case 500:
                                Utility.showCustomOKOnlyDialog(PolesDtrsMapActivity.this, "Something went wrong at server end");
                                break;
                            default:
                                Utility.showCustomOKOnlyDialog(PolesDtrsMapActivity.this, "Check Your Internet Connection and Try Again");
                                break;
                        }
                    }
                });
    }

    private void setupMap(MapView mMapView) {
        if (mMapView != null) {
           */
/* ArcGISTiledLayer basemapLayer = new ArcGISTiledLayer(getString(R.string.basemap_url));
            Basemap wgs84Basemap = new Basemap(basemapLayer);
            ArcGISMap map = new ArcGISMap(wgs84Basemap);*//*

            Basemap.Type basemapType = Basemap.Type.IMAGERY;
            double latitude = 34.09042;
            double longitude = -118.71511;
            int levelOfDetail = 11;
            ArcGISMap map = new ArcGISMap(basemapType, latitude, longitude, levelOfDetail);
            mMapView.setMap(map);
        }
    }

    private void setupLocationDisplay(MapView mMapView) {
        mLocationDisplay = mMapView.getLocationDisplay();
        mLocationDisplay.addDataSourceStatusChangedListener(dataSourceStatusChangedEvent -> {

            // If LocationDisplay started OK or no error is reported, then continue.
            if (dataSourceStatusChangedEvent.isStarted() || dataSourceStatusChangedEvent.getError() == null) {
                return;
            }

            int requestPermissionsCode = 2;
            String[] requestPermissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

            // If an error is found, handle the failure to start.
            // Check permissions to see if failure may be due to lack of permissions.
            if (!(ContextCompat.checkSelfPermission(PolesDtrsMapActivity.this, requestPermissions[0]) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(PolesDtrsMapActivity.this, requestPermissions[1]) == PackageManager.PERMISSION_GRANTED)) {

                // If permissions are not already granted, request permission from the user.
                ActivityCompat.requestPermissions(PolesDtrsMapActivity.this, requestPermissions, requestPermissionsCode);
            } else {

                // Report other unknown failure types to the user - for example, location services may not
                // be enabled on the device.
                String message = String.format("Error in DataSourceStatusChangedListener: %s", dataSourceStatusChangedEvent
                        .getSource().getLocationDataSource().getError().getMessage());
                Toast.makeText(PolesDtrsMapActivity.this, message, Toast.LENGTH_LONG).show();
            }
        });
        mLocationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.COMPASS_NAVIGATION);
        mLocationDisplay.startAsync();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        // If request is cancelled, the result arrays are empty.
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            // Location permission was granted. This would have been triggered in response to failing to start the
            // LocationDisplay, so try starting this again.
            mLocationDisplay.startAsync();
        } else {

            // If permission was denied, show toast to inform user what was chosen. If LocationDisplay is started again,
            // request permission UX will be shown again, option should be shown to allow never showing the UX again.
            // Alternative would be to disable functionality so request is not shown again.
            Toast.makeText(PolesDtrsMapActivity.this, getResources().getString(R.string.location_permission_denied), Toast.LENGTH_SHORT).show();
        }
    }

    private class ShowCoordinatesMapPolesTouchListener extends DefaultMapViewOnTouchListener {

        public ShowCoordinatesMapPolesTouchListener(Context context, MapView mapView) {
            super(context, mapView);
        }

        */
/**
         * Overrides the onSingleTapConfirmed gesture on the MapView, showing formatted coordinates of the tapped location.
         *
         * @param e the motion event
         * @return true if the listener has consumed the event; false otherwise
         *//*

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            // convert the screen location where user tapped into a map point
            if (selectedpoles < maxpoles) {
                Point tapPoint = mMapView.screenToLocation(new android.graphics.Point((int) e.getX(), (int) e.getY()));
                SimpleMarkerSymbol simpleMarkerSymbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, Color.RED, 18);
                Graphic graphic = new Graphic(tapPoint, simpleMarkerSymbol);
                pgraphicsOverlay.getGraphics().add(graphic);
                poleDetails.put("lat", String.valueOf(tapPoint.getX()));
                poleDetails.put("lon", String.valueOf(tapPoint.getY()));
                poleDetails.put("poledesc", "Testing");
                poleDetails.put("poleid", "pole" + selectedpoles);
                poleDetails.put("type", "pole");
                JSONObject pjson = new JSONObject(poleDetails);
                wholeJsonArray.put(pjson);

                selectedpoles = selectedpoles + 1;
                toCoordinateNotationFromPoint(tapPoint, pinitialGraphic, mPolesMapView);
                poles_count.setText("Selected Poles Count :"+" "+ selectedpoles);

            }
            return true;
        }
        @Override
        public boolean onRotate(MotionEvent event, double rotationAngle) {
            return false;
        }

    }

    private class ShowCoordinatesMapDtrsTouchListener extends DefaultMapViewOnTouchListener {

        public ShowCoordinatesMapDtrsTouchListener(Context context, MapView mapView) {
            super(context, mapView);
        }

        */
/**
         * Overrides the onSingleTapConfirmed gesture on the MapView, showing formatted coordinates of the tapped location.
         *
         * @param e the motion event
         * @return true if the listener has consumed the event; false otherwise
         *//*

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            // convert the screen location where user tapped into a map point
            if (selecteddtrs < maxdtrs) {
                Point tapPoint = mMapView.screenToLocation(new android.graphics.Point((int) e.getX(), (int) e.getY()));
                SimpleMarkerSymbol simpleMarkerSymbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, Color.BLUE, 18);
                Graphic graphic = new Graphic(tapPoint, simpleMarkerSymbol);
                dgraphicsOverlay.getGraphics().add(graphic);
                dtrDetails.put("lat", String.valueOf(tapPoint.getX()));
                dtrDetails.put("lon", String.valueOf(tapPoint.getY()));
                dtrDetails.put("poledesc", "Testing");
                dtrDetails.put("poleid", "dtr" + selecteddtrs);
                dtrDetails.put("type", "DTR");
                JSONObject djson = new JSONObject(dtrDetails);
                wholeJsonArray.put(djson);
                selecteddtrs = selecteddtrs + 1;
                toCoordinateNotationFromPoint(tapPoint, dinitialGraphic, mDtrsMapView);
                dtrs_count.setText("Selected DTRS Count:"+" "+selecteddtrs);

            }
            return true;
        }
        @Override
        public boolean onRotate(MotionEvent event, double rotationAngle) {
            return false;
        }

    }


    @Override
    protected void onPause() {
        super.onPause();
        // pause MapView
        mPolesMapView.pause();
        mDtrsMapView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // resume MapView
        mPolesMapView.resume();
        mDtrsMapView.resume();
        setupMap(mPolesMapView);
        setupMap(mDtrsMapView);
        setupLocationDisplay(mPolesMapView);
        setupLocationDisplay(mDtrsMapView);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // dispose MapView
        mPolesMapView.dispose();
        mDtrsMapView.dispose();
    }

    private void toCoordinateNotationFromPoint(Point newLocation, Graphic initialGraphic, MapView mMapView) {
        if ((newLocation != null) && (!newLocation.isEmpty())) {
            initialGraphic.setGeometry(newLocation);
            try {

               */
/* Toast.makeText(getApplicationContext(), CoordinateFormatter.toLatitudeLongitude(newLocation,
                        CoordinateFormatter.LatitudeLongitudeFormat.DECIMAL_DEGREES, 10), Toast.LENGTH_LONG).show();*//*


            } catch (ArcGISRuntimeException convertException) {
                String message = String.format("%s Point at '%s'\n%s", getString(R.string.failed_convert),
                        newLocation.toString(), convertException.getMessage());
                */
/* Snackbar.make(mMapView, message, Snackbar.LENGTH_SHORT).show();*//*

            }
        }
    }

}*/
