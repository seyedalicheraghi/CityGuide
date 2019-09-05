package org.smartcityguide.cityguide;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.neovisionaries.bluetooth.ble.advertising.ADPayloadParser;
import com.neovisionaries.bluetooth.ble.advertising.ADStructure;
import com.neovisionaries.bluetooth.ble.advertising.EddystoneUID;

import java.util.List;

public class BluetoothLowEnergyActivity {

    private BluetoothAdapter mBluetoothAdapter;
    BLEResponse delegate = null;

    BluetoothLowEnergyActivity(BluetoothAdapter mBluetoothAdapter){
        this.mBluetoothAdapter = mBluetoothAdapter;

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void startListening(){
        mBluetoothAdapter.startLeScan(mLeScanCallback);
    }

    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
            // Parse the payload of the advertisement packet as a list of AD structures.
            List<ADStructure> structures = ADPayloadParser.getInstance().parse(scanRecord);


            // For each AD structure contained in the advertisement packet.
            for (ADStructure structure : structures)
            {
//                If the AD structure represents Eddystone UID.
                if (structure instanceof EddystoneUID)
                {
                    EddystoneUID es = (EddystoneUID)structure;
                    delegate.beaconSighting(new StringBuilder(es.getInstanceIdAsString()),new StringBuilder(es.getNamespaceIdAsString()),rssi);
                }
            }
        }
    };
}


/*
      Copy this into mainActivity
    //    Check if user's device support Bluetooth etc
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void BLEMonitoring() {
        // Use this check to determine whether BLE is supported on the device. Then you can selectively disable BLE-related features.
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Snackbar.make(this.getWindow().getDecorView().findViewById(android.R.id.content), "BLE Not Supported on Your Device!", Snackbar.LENGTH_LONG).show();
            finish();
        } else {
            // Initializes Bluetooth adapter.
            final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            assert bluetoothManager != null;
            BluetoothAdapter mBluetoothAdapter = bluetoothManager.getAdapter();
            // Ensures Bluetooth is available on the device and it is enabled. If not, displays a dialog requesting user permission to enable Bluetooth.
            if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                assert mBluetoothAdapter != null;
                mBluetoothAdapter.startLeScan(mLeScanCallback);
            }
        }
    }
private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
    @Override
    public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
        // Parse the payload of the advertisement packet as a list of AD structures.
        List<ADStructure> structures = ADPayloadParser.getInstance().parse(scanRecord);

        beaconRSSI = rssi;
            // For each AD structure contained in the advertisement packet.
            for (ADStructure structure : structures)
            {
                // If the AD structure represents Eddystone UID.
                if (structure instanceof EddystoneUID)
                {
                    EddystoneUID es = (EddystoneUID)structure;

                    // Populate beacons_array with beacons information
                    if(namespace.toString().toLowerCase().compareTo("") == 0 && !beacon_ready_flag) {
                        namespace = new StringBuilder(es.getNamespaceIdAsString());
                        loginFunc("beacons", user_name, user_password, namespace, beacon_location);
                    }
                    //Try to get building information
                    if(beacon_ready_flag && !building_information_flag){

                        beacon_location = beacons_array.get(Integer.valueOf(es.getInstanceIdAsString())).trim().toLowerCase();

                        if(beacon_location.compareTo("outdoor")==0){
                            loginFunc("outdoorInquiry",user_name, user_password, namespace, namespace.toString().toLowerCase().trim());
                        }else{
                            loginFunc("buildingInquiry",user_name,user_password,namespace, beacon_location);
                        }

                        building_information_flag=true;
                    }

                }
            }
    }
};
*/
