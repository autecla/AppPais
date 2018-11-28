package com.lincs.mobcare.daily;

import android.bluetooth.BluetoothAdapter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.lincs.mobcare.R;

public class AtividadeCorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical);

        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

            Toast.makeText(getApplicationContext(), "Medical Activity", Toast.LENGTH_SHORT).show();


    }
}
