package com.example.getloc;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class MainActivity extends AppCompatActivity {

    TextView longi,lat;
    Switch share;

    // google play service api
    FusedLocationProviderClient fusedLocationProviderClient;
    // location request service
    LocationRequest locationRequest;
    private static final int sETINTERVAL=30;
    private static final int fASTESTINTERVAL=5;
    public static final int rEQUESTCODE=1;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        longi = findViewById(R.id.longi);
        lat   = findViewById(R.id.lat);
        share = findViewById(R.id.share);

        locationRequest = LocationRequest.create();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        locationRequest.setInterval(1000 * sETINTERVAL);
        locationRequest.setFastestInterval(1000 * fASTESTINTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        share.setOnClickListener(v -> {
            if(share.isChecked()){
                share.setText("ON");
                updateGPS();
            }
            else{
                share.setText("OFF");
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case rEQUESTCODE:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    updateGPS();
                }
                else{
                    Toast.makeText(this, "Requires location permission", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + requestCode);
        };
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void updateGPS() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MainActivity.this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    updateUI(location);
                }
            });
        } else {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, rEQUESTCODE);
        }
    }

    private void updateUI(Location location){
        lat.setText(String.valueOf(location.getLatitude()));
        longi.setText(String.valueOf(location.getLongitude()));
    }
}