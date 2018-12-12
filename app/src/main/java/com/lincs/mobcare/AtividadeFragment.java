package com.lincs.mobcare;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.github.ivbaranov.rxbluetooth.RxBluetooth;
import com.lincs.mobcare.R;
import com.lincs.mobcare.utils.BluetoothChat;
import com.lincs.mobcare.utils.BluetoothConnectionService;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class AtividadeFragment extends Fragment {
        private Button btnCores;

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

            View view = inflater.inflate(R.layout.bt_test_layout, container, false);
            btnCores = (Button) view.findViewById(R.id.btnCores);

            return view;
        }


        @Override
        public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {

            btnCores.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), BluetoothChat.class);
                    startActivity(intent);
                }
            });


        }




    }




