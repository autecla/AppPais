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

public class BTFragment extends Fragment {

    private static final String TAG = "BluetoothChat";
    private static final int MY_PERMISSIONS_COARSE_LOCATION = 1;


    BluetoothAdapter mBluetoothAdapter;
    Button btnEnableDisable_Discoverable;
    Button btnDiscover;

    BluetoothConnectionService mBluetoothConnection;

    Button btnStartConnection;
    Button btnSend;
    Button btnRefresh;

    EditText etSend;
    TextView recMessage;

    private static final UUID MY_UUID_INSECURE =
            UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");

    BluetoothDevice mBTDevice;

    public ArrayList<BluetoothDevice> mBTDevices = new ArrayList<>();

    private ArrayAdapter<String> adapterList;
    private ArrayList<String> scannedDevicesNames = new ArrayList<String>();



    ListView lvNewDevices;

        public BTFragment() {
            // Required empty public constructor
        }


    // Create a BroadcastReceiver for ACTION_FOUND
    private final BroadcastReceiver mBroadcastReceiver1 = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            if (action.equals(mBluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, mBluetoothAdapter.ERROR);

                switch(state){
                    case BluetoothAdapter.STATE_OFF:
                        Log.d(TAG, "onReceive: STATE OFF");
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        Log.d(TAG, "mBroadcastReceiver1: STATE TURNING OFF");
                        break;
                    case BluetoothAdapter.STATE_ON:
                        Log.d(TAG, "mBroadcastReceiver1: STATE ON");
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        Log.d(TAG, "mBroadcastReceiver1: STATE TURNING ON");
                        break;
                }
            }
        }
    };

    /**
     * Broadcast Receiver for changes made to bluetooth states such as:
     * 1) Discoverability mode on/off or expire.
     */
    private final BroadcastReceiver mBroadcastReceiver2 = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (action.equals(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED)) {

                int mode = intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE, BluetoothAdapter.ERROR);

                switch (mode) {
                    //Device is in Discoverable Mode
                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE:
                        Log.d(TAG, "mBroadcastReceiver2: Discoverability Enabled.");
                        break;
                    //Device not in discoverable mode
                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE:
                        Log.d(TAG, "mBroadcastReceiver2: Discoverability Disabled. Able to receive connections.");
                        break;
                    case BluetoothAdapter.SCAN_MODE_NONE:
                        Log.d(TAG, "mBroadcastReceiver2: Discoverability Disabled. Not able to receive connections.");
                        break;
                    case BluetoothAdapter.STATE_CONNECTING:
                        Log.d(TAG, "mBroadcastReceiver2: Connecting....");
                        break;
                    case BluetoothAdapter.STATE_CONNECTED:
                        Log.d(TAG, "mBroadcastReceiver2: Connected.");
                        break;
                }

            }
        }
    };




    /**
     * Broadcast Receiver for listing devices that are not yet paired
     * -Executed by btnDiscover() method.
     */
    private BroadcastReceiver mBroadcastReceiver3 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            Log.d(TAG, "onReceive: ACTION FOUND.");

            if (action.equals(BluetoothDevice.ACTION_FOUND)){
                BluetoothDevice device = intent.getParcelableExtra (BluetoothDevice.EXTRA_DEVICE);
                mBTDevices.add(device);
                String info = new String();
                if(device.getName() != null) {
                    info += device.getName();
                }
                info += ": " + device.getAddress();
                scannedDevicesNames.add(info);
                Log.d(TAG, "onReceive: " + device.getName() + ": " + device.getAddress());
                adapterList = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, scannedDevicesNames);
                lvNewDevices.setAdapter(adapterList);
            }
        }
    };

    private void ListPairedDevices(ListView lv) {
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                mBTDevices.add(device);
                String info = new String();
                if(device.getName() != null) {
                    info += device.getName();
                }
                info += ": " + device.getAddress();
                scannedDevicesNames.add(info);
                Log.d(TAG, "onPaired: " + device.getName() + ": " + device.getAddress());
                adapterList = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, scannedDevicesNames);
                lvNewDevices.setAdapter(adapterList);
            }
        }

    }

    /**
     * Broadcast Receiver that detects bond state changes (Pairing status changes)
     */
    private final BroadcastReceiver mBroadcastReceiver4 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if(action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)){
                BluetoothDevice mDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                //3 cases:
                //case1: bonded already
                if (mDevice.getBondState() == BluetoothDevice.BOND_BONDED){
                    Log.d(TAG, "BroadcastReceiver: BOND_BONDED.");
                    //inside BroadcastReceiver4
                    mBTDevice = mDevice;
                }
                //case2: creating a bone
                if (mDevice.getBondState() == BluetoothDevice.BOND_BONDING) {
                    Log.d(TAG, "BroadcastReceiver: BOND_BONDING.");
                }
                //case3: breaking a bond
                if (mDevice.getBondState() == BluetoothDevice.BOND_NONE) {
                    Log.d(TAG, "BroadcastReceiver: BOND_NONE.");
                }
            }
        }
    };



    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: called.");
        super.onDestroy();
        getActivity().unregisterReceiver(mBroadcastReceiver1);
        getActivity().unregisterReceiver(mBroadcastReceiver2);
        getActivity().unregisterReceiver(mBroadcastReceiver3);
        getActivity().unregisterReceiver(mBroadcastReceiver4);
        //mBluetoothAdapter.cancelDiscovery();
    }


        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

            View view = inflater.inflate(R.layout.bt_test_layout, container, false);
          /*  Button btnONOFF = (Button) view.findViewById(R.id.btnONOFF);
            recMessage = (TextView) view.findViewById(R.id.bt_message) ;
            btnEnableDisable_Discoverable = (Button) view.findViewById(R.id.btnDiscoverable_on_off);
            btnDiscover = (Button) view.findViewById(R.id.btnFindUnpairedDevices);
            lvNewDevices = (ListView) view.findViewById(R.id.lvNewDevices);
            mBTDevices = new ArrayList<>();

            btnStartConnection = (Button) view.findViewById(R.id.btnStartConnection);
            btnSend = (Button) view.findViewById(R.id.btnSend);
            btnRefresh = (Button) view.findViewById(R.id.btnRefresh);
            etSend = (EditText) view.findViewById(R.id.editText);

            scannedDevicesNames.clear();
            //Broadcasts when bond state changes (ie:pairing)
            IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
            getActivity().registerReceiver(mBroadcastReceiver4, filter);

            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

            lvNewDevices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int i,
                                        long id) {

                    //first cancel discovery because its very memory intensive.
                    mBluetoothAdapter.cancelDiscovery();

                    Log.d(TAG, "onItemClick: You Clicked on a device.");
                    String deviceName = mBTDevices.get(i).getName();
                    String deviceAddress = mBTDevices.get(i).getAddress();

                    Log.d(TAG, "onItemClick: deviceName = " + deviceName);
                    Log.d(TAG, "onItemClick: deviceAddress = " + deviceAddress);

                    //create the bond.
                    //NOTE: Requires API 17+? I think this is JellyBean
                    if(Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2){
                        Log.d(TAG, "Trying to pair with " + deviceName);
                        mBTDevices.get(i).createBond();
                        mBTDevice = mBTDevices.get(i);
                        mBluetoothConnection = new BluetoothConnectionService(getActivity());

                    }


                }
            });


            btnONOFF.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(TAG, "onClick: enabling/disabling bluetooth.");
                    enableDisableBT();
                }
            });

            btnStartConnection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startConnection();
                }
            });

            btnSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    byte[] bytes = etSend.getText().toString().getBytes(Charset.defaultCharset());
                    etSend.setText("Sent: " + etSend.getText());
                    mBluetoothConnection.write(bytes);
                }
            });

            btnRefresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String msg;
                    msg = mBluetoothConnection.read();
                    recMessage.setText(msg);
                }
            });




            btnEnableDisable_Discoverable.setOnClickListener(new View.OnClickListener() {
                    @Override
                public void onClick(View view) {
                    Log.d(TAG, "btnEnableDisable_Discoverable: Making device discoverable for 300 seconds.");

                    Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                    discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
                    startActivity(discoverableIntent);

                    IntentFilter intentFilter = new IntentFilter(mBluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
                    getActivity().registerReceiver(mBroadcastReceiver2,intentFilter);
                }
            });




            btnDiscover.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(TAG, "btnDiscover: Looking for unpaired devices.");
                    ListPairedDevices(lvNewDevices);

                    if(mBluetoothAdapter.isDiscovering()){
                        mBluetoothAdapter.cancelDiscovery();
                        Log.d(TAG, "btnDiscover: Canceling discovery.");

                        //check BT permissions in manifest
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            checkBTPermissions();
                        }

                        mBluetoothAdapter.startDiscovery();
                        IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                        getActivity().registerReceiver(mBroadcastReceiver3, discoverDevicesIntent);
                    }
                    if(!mBluetoothAdapter.isDiscovering()){

                        //check BT permissions in manifest
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            checkBTPermissions();
                        }

                        mBluetoothAdapter.startDiscovery();
                        IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                        getActivity().registerReceiver(mBroadcastReceiver3, discoverDevicesIntent);
                    }
                }
            });
*/
            return view;
        }

    //create method for starting connection
//***remember the conncction will fail and app will crash if you haven't paired first
    public void startConnection(){
        Log.d(TAG, "startConnection: " + mBTDevice.getName() + " -> " + mBTDevice.getAddress());
        startBTConnection(mBTDevice,MY_UUID_INSECURE);
    }

    /**
     * starting chat service method
     */
    public void startBTConnection(BluetoothDevice device, UUID uuid){
        Log.d(TAG, "startBTConnection: Initializing RFCOM Bluetooth Connection.");

        mBluetoothConnection.startClient(device,uuid);
    }



    public void enableDisableBT(){
        if(mBluetoothAdapter == null){
            Log.d(TAG, "enableDisableBT: Does not have BT capabilities.");
        }
        if(!mBluetoothAdapter.isEnabled()){
            Log.d(TAG, "enableDisableBT: enabling BT.");
            Intent enableBTIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enableBTIntent);

            IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            getActivity().registerReceiver(mBroadcastReceiver1, BTIntent);
        }
        if(mBluetoothAdapter.isEnabled()){
            Log.d(TAG, "enableDisableBT: disabling BT.");
            mBluetoothAdapter.disable();

            IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            getActivity().registerReceiver(mBroadcastReceiver1, BTIntent);
        }

    }


    public void btnEnableDisable_Discoverable(View view) {


    }

    public void btnDiscover(View view) {

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkBTPermissions() {
        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                MY_PERMISSIONS_COARSE_LOCATION);
    }
        @Override
        public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {

            Intent intent = new Intent(view.getContext(), BluetoothChat.class);
            startActivity(intent);
        }




    }




