package com.lincs.mobcare.daily;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.lincs.mobcare.R;

public class AtividadeCorActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical);

        // Assume thisActivity is the current activity
        int permissionCheck = ContextCompat.checkSelfPermission(AtividadeCorActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION);

        ActivityCompat.requestPermissions(AtividadeCorActivity.this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                MY_PERMISSIONS_REQUEST_READ_CONTACTS);

    }
}
