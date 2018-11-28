package com.lincs.mobcare.daily;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Message;
import android.os.ParcelUuid;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lincs.mobcare.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class AtividadeCorFragment extends Fragment {

    private final static int REQUEST_ENABLE_BT = 1;
    private TextView status;
    private ListView pairedDevicesList;
    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

     public AtividadeCorFragment() {
        // Required empty public constructor
    }

    private void getPairedDevices() {
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        ArrayList<String> btList = new ArrayList<>();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                status.append(device.getName() + "\n");
            }
        }


    }

    private void fillList() {
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        ArrayList<String> btList = new ArrayList<>();
        for(int i=0; i<12; i++){
            btList.add(Integer.toString(i));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1, btList);
        pairedDevicesList.setAdapter(adapter);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_medical, container, false);



        Button vermelho, amarelo, verde, azul;

        status = view.findViewById(R.id.textMessage);

        String[] menuItems = {"Do something", "Why is not working\n"};
        ListView lv = (ListView) view.findViewById(R.id.listViewPaired);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, menuItems);
        lv.setAdapter(adapter);


        adapter.notifyDataSetChanged();

/*
        vermelho = view.findViewById(R.id.vermelho);
        amarelo = view.findViewById(R.id.amarelo);
        verde = view.findViewById(R.id.verde);
        azul = view.findViewById(R.id.azul);


        vermelho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                status.setText("VERMELHO");
            }
        });
        amarelo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                status.setText("AMARELO TOAST!");
                if(getActivity().getApplicationContext() != null){
                    status.setText("Attached to activity\n");
                    Toast.makeText(getActivity(), "YEHAWWWW", Toast.LENGTH_LONG).show();

                }

            }
        });
        verde.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                status.setText("VERDE");
            }
        });
        azul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                status.setText("AZUL");
            }
        });



        if (mBluetoothAdapter != null) {
            status.setText("Os resultados da atividade aparecerão aqui\n");
        }
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);

            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(discoverableIntent);
        }

        getPairedDevices(); */

        return view;
    }


    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {



    }


}
