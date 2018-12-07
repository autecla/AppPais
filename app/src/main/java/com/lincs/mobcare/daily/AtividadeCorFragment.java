package com.lincs.mobcare.daily;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Message;
import android.os.ParcelUuid;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lincs.mobcare.MainActivity;
import com.lincs.mobcare.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class AtividadeCorFragment extends Fragment {

    private final static int REQUEST_ENABLE_BT = 1;
    private final static int MY_PERMISSIONS_COARSE_LOCATION = 1;
    private final static String TAG = "Atividade Cor === ";
    private final static UUID MY_UUID_UNSECURE = UUID.fromString("a8dd6c1b-0908-42c8-b2ae-32133f068a0b ");
    private TextView message;
    private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private ArrayAdapter<String> adapterList;
    private ArrayList<String> scannedDevicesNames = new ArrayList<String>();
    private TextView bluetoothStatus;
    private ListView lv;

    private Button vermelho, amarelo, verde, azul, scanB;

    //BroadcastReceiver mReceiver;

    public AtividadeCorFragment() {
        // Required empty public constructor
    }

    private void FindAllViews(View view){

        vermelho = view.findViewById(R.id.vermelho);
        amarelo = view.findViewById(R.id.amarelo);
        verde = view.findViewById(R.id.verde);
        azul = view.findViewById(R.id.azul);
        scanB = view.findViewById(R.id.scanButton);

        message = view.findViewById(R.id.textMessage);
        bluetoothStatus = view.findViewById(R.id.titleDispPareados);
        lv = (ListView) view.findViewById(R.id.listViewPaired);

        return;
    }

    private void SetUpColorButtons(){

        vermelho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                message.setText("VERMELHO");
            }
        });

        amarelo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                message.setText("AMARELO");
            }
        });

        verde.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                message.setText("VERDE");
            }
        });

        azul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                message.setText("AZUL");
            }
        });

        return;
    }

    private void ListPairedDevices(ListView lv) {
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        ArrayList<String> btList = new ArrayList<>();

        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
               btList.add(device.getName() + "\n");
            }
        }

        adapterList = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, btList);
        lv.setAdapter(adapterList);
    }


    private void ScanDevices(String deviceName) {
        scannedDevicesNames.add(deviceName);
        adapterList = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, scannedDevicesNames);
        lv.setAdapter(adapterList);
    }

    private final BroadcastReceiver bReciever = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d(TAG, "ON HERE");
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if(device.getName() != null) {
                    Log.d(TAG, device.getName());
                    ScanDevices(device.getName() + ": " + device.getAddress());
                }
                else {
                    Log.d(TAG, "no name to show");
                }
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();

        // Don't forget to unregister the ACTION_FOUND receiver.
        getActivity().unregisterReceiver(bReciever);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_medical, container, false);
        FindAllViews(view);

        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                MY_PERMISSIONS_COARSE_LOCATION);


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Object obj = parent.getItemAtPosition(position);
                String name = obj.toString();
                message.setText(name);
                mBluetoothAdapter.cancelDiscovery();


            }
        });

        SetUpColorButtons();


        scanB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scannedDevicesNames.clear();
                mBluetoothAdapter.startDiscovery();

                IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                getActivity().registerReceiver(bReciever, filter);

                bluetoothStatus.setText("Started Discovery");
                Log.d(TAG, "PRESSED BUTTON SCAN\n");

                if(mBluetoothAdapter.isDiscovering()){

                    Log.d(TAG, "IS DISCOVERING...");
                }
            }
        });


        if (mBluetoothAdapter != null) {
            message.setText("Os resultados da atividade aparecerão aqui\n");
        }
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);

            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(discoverableIntent);

        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {


    }




}
