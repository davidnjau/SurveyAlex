package com.dave.survey;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class Home extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_CODE = 123;
    private MapView map;
    private Float StartLatitude, StartLongitude;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSION_STORAGE = {Manifest.permission.WRITE_EXTERNAL_STORAGE};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        map = findViewById(R.id.map);
        Formatter formatter = new Formatter();
        formatter.customBottomNavigation(this);

    }

    @Override
    protected void onStart() {
        super.onStart();

        checkPermissions();
        StartLatitude = Float.valueOf("-1.292066");
        StartLongitude = Float.valueOf("36.821945");

        initMap();

    }

    private void checkPermissions() {

        if(
                ContextCompat.checkSelfPermission(Home.this,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        +
                        ContextCompat.checkSelfPermission(Home.this,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED){

            // Do something, when permissions not granted
            if(
                    ActivityCompat.shouldShowRequestPermissionRationale(
                            Home.this,Manifest.permission.ACCESS_FINE_LOCATION)
                            ||
                            ActivityCompat.shouldShowRequestPermissionRationale(
                                    Home.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                // If we should give explanation of requested permissions

                // Show an alert dialog here with request explanation
                AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
                builder.setMessage("Location and Write External" +
                        " Storage permissions are required for the app.");
                builder.setTitle("Please grant these permissions");
                builder.setPositiveButton("OK", (dialogInterface, i) -> ActivityCompat.requestPermissions(
                        Home.this,
                        new String[]{
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                        },
                        MY_PERMISSIONS_REQUEST_CODE
                ));
                builder.setNeutralButton("Cancel", (dialog, which) -> {

                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }else{
                // Directly request for required permissions, without explanation
                ActivityCompat.requestPermissions(
                        Home.this,
                        new String[]{
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                        },
                        MY_PERMISSIONS_REQUEST_CODE
                );
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        switch (requestCode){
            case MY_PERMISSIONS_REQUEST_CODE:{
                // When request is cancelled, the results array are empty
                if(
                        (grantResults.length >0) &&
                                (grantResults[0]
                                        + grantResults[1]
                                        == PackageManager.PERMISSION_GRANTED
                                )
                ){
                    // Permissions are granted
                    Toast.makeText(Home.this, "Permissions granted.", Toast.LENGTH_SHORT).show();

                }else {
                    // Permissions are denied
                    Toast.makeText(Home.this, "Permissions denied.", Toast.LENGTH_SHORT).show();

                }
            }
        }
    }


    private void initMap() {

//        getCurrentLocation();

        final GeoPoint geoPoint = new GeoPoint(StartLatitude, StartLongitude);

        map.setTileSource(TileSourceFactory.OpenTopo);
        map.setMultiTouchControls(true);
        map.setBuiltInZoomControls(true);

        map.getController().setZoom(4.0f);
        map.setMaxZoomLevel(null);

        MapController mapController = (MapController) map.getController();
//        mapController.animateTo(geoPoint, 7.5, 4000L);
        mapController.setZoom(10.5);
        mapController.setCenter(geoPoint);


    }
}