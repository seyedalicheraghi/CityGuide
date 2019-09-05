package org.smartcityguide.cityguide;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity implements AsyncResponse, RequestResponse, SensorEventListener, BLEResponse {

    private static final String TAG = "MyErrorDetector";
    private static final int REQUEST_ENABLE_BT = 903;
    private boolean firstTimeFlag = false;
    private boolean rerouting = false;
    private int FIRST_THRESHOLD;
    private int  STEP_SIZE,DISTANCE_UNITS;
    private ImageView img_compass;
    private CompassService dirService;
    private Timer directionTimer;
    private String where[] = {"N", "0"};
    private TextView txt_compass;
    private int[] directions = new int[10];
    private int directionCounter = 0;
    private GPSService gpsService;
    private Timer GPSTimer;
    private Location newLocation = null;
    private ProgressBar progressBar;
    private Intent recognizerIntent;
    private SpeechRecognition speech;
    private long GPS_ACCURACY;
    private double PROXIMITY_RADIUS;
    private int beacon_RSSI = -10000;
    private StringBuilder beacon_id = new StringBuilder();
    private StringBuilder beacon_namespace = new StringBuilder("");
    private SpeakOut talk;
    private boolean speakFlag;
    private StringBuilder[][] buildings_info;
    private int[][] connections;
    private ArrayList<HashMap<Integer, StringBuilder>> beacons_array = new ArrayList<>();
    private StringBuilder beacon_location = new StringBuilder();
    private boolean beacon_download_flag = false;
    private RoutFinding rf = new RoutFinding();
    private String routeSrcDst;
    private TextToSpeech textToSpeech=null;
    private int beaconNumber;
    private double[][][] mapDetails;
    private long lastReceivedTimeStamp = 0, lastSavedTimeBuffer = 0;
    private String[][] buildingSensorsMap;
    private int[][] nodeDeciderArray;
    private boolean ROUTE_PREVIEW, MIDPOINTS_LOCATIONS;
    private int DISTANCE_OPTION, DIRECTION_OPTION, USER_CATEGORY, WAITING_TIME, current = -1;
    private boolean inquiry_flag = false;
    private double[] weightedAverage;
    private StringBuilder[] instructionVoice;
    private ArrayList<HashMap<String, String>> instruction;
    private String unit;
    private boolean reachFlag = false;
    private int WMA_OPTION;
    private boolean explorationFlag = true, explorationFlagInitialization = false;
    private int exploredNode = -10;
    private String loop_number = "-1000";
    private int desNumber;
    private int number_Of_Sensors=0;
    private int finalDesNumber;
    private int next;
    private int bufferRouCounter;
    private int[] bufferRou;
    private boolean exceptionFlag = false;
    private InstructionGenerator indoorInstructionGenerator;
    private boolean indoorInitialization=false;
    private ProgressBar progressDialog;
    private RelativeLayout.LayoutParams params;
    private ConstraintLayout layout;
    private ArrayList<ArrayList<HashMap<String, String>>> instructionListData = new ArrayList<>();
    private ArrayList<HashMap<String, String>> resultsToshowExploration = new ArrayList<>();
    private boolean indoorWayfindingFlag = false;
    private boolean progressDialogFlag=false;
    private SimpleAdapter simpleAdapter;
    private float stepCounter = 0, originalStepCounter = 0;
    private Long startTime = 0l , endTime = 0l;
    private boolean activityRunning = true, originalStepFlag = true;
    private boolean EVACUATION_FLAG = false;
    private int frgno = 2;
    private FrameLayout myFrame;
    private int xy[][] = new int[40][2];
    private FragmentTransaction fragmentBlindTransaction;
    private FragmentTransaction fragmentSightedTransaction;
    private FragmentManager fragmentManager  = getSupportFragmentManager();
    private SightedFragment fragmentSighted = new SightedFragment();
    private BlindFragment fragmentBlind = new BlindFragment();
    private ServiceConnection directionConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            CompassService.DirectionBinder binder = (CompassService.DirectionBinder) iBinder;
            dirService = binder.getService();
            directionTimer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    where = dirService.currentDirection();
                    runOnUiThread(new Runnable() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void run() {
                            img_compass.setRotation(-Integer.parseInt(where[1]));
                            txt_compass.setText(Integer.parseInt(where[1]) + "° " + where[0]);
                            directions[directionCounter] = Integer.parseInt(where[1]);
                            directionCounter++;
                            if (directionCounter == 10)
                                directionCounter = 0;
                            fragmentSighted.beamAngle(where);
                        }
                    });
                }
            }, 10, 200);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };
    private DisplayMetrics displayMetrics;
    private ArrayList<HashMap<String, String>> outdoorLocationsToGo;
    private ArrayList<HashMap<String, String>> indoorLocationsToGo;
    private void isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        if(!(activeNetworkInfo != null && activeNetworkInfo.isConnected())){
            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
            alertDialog.setTitle("Alert");
            alertDialog.setMessage("Please Connect to Internet and Restart the App!");
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }



    }

    private ServiceConnection gpsConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            GPSService.LocationBinder binder = (GPSService.LocationBinder) iBinder;
            gpsService = binder.getService();
            GPSTimer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    if (newLocation == null)
                        newLocation = gpsService.gpsCoordinates();
                    else if (gpsService.gpsCoordinates().getTime() != newLocation.getTime()) {
                        newLocation = gpsService.gpsCoordinates();
                    }
                }
            }, 10, 1000);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {        }
    };

    public static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor sensor = sensorEvent.sensor;
        if (sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            if (originalStepFlag) {
                originalStepCounter = sensorEvent.values[0];
                originalStepFlag = false;
            }
            if (activityRunning) {
                stepCounter = (int) sensorEvent.values[0];
            }
        }
    }


    @Override
    public void userSelection(String option) {
        explorationFlag = false;
        searchingTime(new StringBuilder(option.trim().toLowerCase()));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        directionConnection = null;
        gpsConnection = null;
        dirService = null;
        int id= android.os.Process.myPid();
        android.os.Process.killProcess(id);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogCatApp logCatApp = new LogCatApp();
        logCatApp.onCreate();
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        myFrame = findViewById(R.id.frame);
        settingParameters();
        displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        setRouteSrcDst("");

        fragmentSightedTransaction = fragmentManager.beginTransaction();
        fragmentSightedTransaction.replace(R.id.frame, fragmentSighted);
        fragmentSightedTransaction.commit();
        fragmentSighted.xyToDraw(5, xy, "",getRouteSrcDst());

        pixelsInitialization();

        directionTimer = new Timer();
        txt_compass = findViewById(R.id.txt_azimuth);
        img_compass = findViewById(R.id.img_compass);

        bindService(new Intent(this, CompassService.class), directionConnection, Context.BIND_AUTO_CREATE);

        GPSTimer = new Timer();
        bindService(new Intent(this, GPSService.class), gpsConnection, Context.BIND_AUTO_CREATE);// Start GPS service

        int Permission_All = 1;
        final String[] Permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.RECORD_AUDIO, Manifest.permission.ACCESS_COARSE_LOCATION,};
        if (!hasPermissions(this, Permissions)) {
            ActivityCompat.requestPermissions(this, Permissions, Permission_All);
        }

        settingParameters();

        talk = new SpeakOut(getApplicationContext());
        EditText targetEditText = findViewById(R.id.search_view);

        progressBar = findViewById(R.id.progressBar1);
        progressBar.setVisibility(View.INVISIBLE);
        layout = findViewById(R.id.drawer_layout);
        UserTextEditorSearch onEditorListener = new UserTextEditorSearch(targetEditText);
        onEditorListener.delegate = this;

        speech = new SpeechRecognition(this.getApplicationContext(), progressBar);
        speech.delegate = this;
        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "en");
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);

        ImageButton recordBtn = findViewById(R.id.recordBtn);
        recordBtn.setOnClickListener(new View.OnClickListener() {                                   //----->0
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setIndeterminate(true);
                explorationFlag = false;
                speech.onStart(recognizerIntent);

            }
        });

        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        assert sensorManager != null;
        Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        Sensor stepCounter = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        sensorManager.registerListener(MainActivity.this, accelerometer, SensorManager.SENSOR_STATUS_ACCURACY_HIGH);
        sensorManager.registerListener(MainActivity.this, stepCounter, SensorManager.SENSOR_STATUS_ACCURACY_HIGH);

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
                BluetoothLowEnergyActivity bluetoothLowEnergyActivity = new BluetoothLowEnergyActivity(mBluetoothAdapter);
                bluetoothLowEnergyActivity.delegate = this;
                bluetoothLowEnergyActivity.startListening();
            }
        }
        instructionVoice = new StringBuilder[2];

        simpleAdapter = new SimpleAdapter(MainActivity.this,
                resultsToshowExploration, android.R.layout.simple_list_item_2, new String[]{"description"}, new int[]{android.R.id.text1});

        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = textToSpeech.setLanguage(Locale.US);

                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Toast.makeText(getApplicationContext(), "Text to Speech not Supported!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        isNetworkAvailable();
        startTime = System.currentTimeMillis();


    }

    @Override
    public void searchedFinish(ArrayList<HashMap<String, String>> result) {
        explorationFlag = false;
        listViewToShow(result,1,1);
//        HashMap<String, String> FR = result.get(0);
//        result = new ArrayList<>();
//        result.add(FR);
//        resultsToshowExploration.clear();
//        resultsToshowExploration.addAll(result);
//        simpleAdapter.notifyDataSetChanged();
//        listView.setAdapter(simpleAdapter);
//        final ArrayList<HashMap<String, String>> finalResult = result;
//        talk.start("Please either confirm the following option or say your destination again!");
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                searchingTime(new StringBuilder(finalResult.get(position).get("description").trim().toLowerCase()));
//            }
//        });
    }

    private void searchingTime(final StringBuilder user_input) {

        final Timer searchTimer = new Timer();
        if(!progressDialogFlag)
            showProgressDialog();
        searchTimer.scheduleAtFixedRate(new TimerTask() {

            int searchWaitTimer = 0;
            //            Search Controller
            boolean[] searchingStatus = new boolean[]{false, false, false, false, false};
            /*
            Whenever a location is found in outdoor the searchingStatus[1] becomes true
            If it checks the location outdoor environment it trigger the searchingStatus[3] to true
            */
            ArrayList<HashMap<String, String>> outdoorLocations = new ArrayList<>();
            ArrayList<HashMap<String, String>> indoorLocations = new ArrayList<>();

            @Override
            public void run() {
                /*GPS Search Section, 5 is the GPS accuracy
                 * neaLocation is related to GPS coordinates
                 * */
                if (newLocation != null && newLocation.getLatitude() != 0 && newLocation.getLongitude() != 0 && newLocation.getAccuracy() < GPS_ACCURACY && !searchingStatus[1]) {
                    String[] choice = new String[]{"locationInquiry", new HttpsLinkMaker().nearbyPlaces
                            (user_input.toString(), new double[][]{{newLocation.getLatitude(), newLocation.getLongitude()}, {0, 0}}, (int) PROXIMITY_RADIUS, getString(R.string.google_maps_key))};
                    try {
                        outdoorLocations = new LocationInquiry().execute(choice).get();
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                    if (outdoorLocations.size() > 0) {
                        searchingStatus[1] = true;
                    }
                    searchingStatus[3] = true;
                }
                boolean firstFlag = false, secondFlag = false;
                ArrayList<Integer> tempArray = new ArrayList<>();
                if (beacon_RSSI > -100 && !searchingStatus[2] && buildings_info != null) {

                    if (buildings_info.length > 0) {
                        for (int i = 0; i < buildings_info.length; i++) {
                            if (buildings_info[i][1].toString().toLowerCase().trim().equals(user_input.toString().trim().toLowerCase()) ||
                                    buildings_info[i][2].toString().toLowerCase().trim().equals(user_input.toString().trim().toLowerCase())) {
                                HashMap<String, String> data = new HashMap<>();
                                data.put("description", buildings_info[i][1].toString().trim().toLowerCase());
                                data.put("loop_number", String.valueOf(i));
                                indoorLocations.add(data);
                                firstFlag = true;
                                tempArray.add(i);
                            }
                        }
                        if (!firstFlag) {
                            for (int i = 0; i < buildings_info.length; i++) {
                                if ((buildings_info[i][1].toString().toLowerCase().trim().contains(user_input.toString().trim().toLowerCase()) ||
                                        buildings_info[i][2].toString().toLowerCase().trim().contains(user_input.toString().trim().toLowerCase()))) {
                                    HashMap<String, String> data = new HashMap<>();
                                    data.put("description", buildings_info[i][1].toString().trim().toLowerCase());
                                    data.put("loop_number", String.valueOf(i));
                                    indoorLocations.add(data);
                                    secondFlag = true;
                                    tempArray.add(i);
                                }
                            }
                        }
                        if (!secondFlag && !firstFlag) {
                            String strTemp[] = user_input.toString().trim().toLowerCase().replace("#", "").split(" ");
                            for (int i = 0; i < buildings_info.length; i++) {
                                for (int j = 0; j < strTemp.length; j++) {
                                    if ((buildings_info[i][1].toString().toLowerCase().trim().contains(strTemp[j]) ||
                                            buildings_info[i][2].toString().toLowerCase().trim().contains(strTemp[j]))) {
                                        HashMap<String, String> data = new HashMap<>();
                                        data.put("description", buildings_info[i][1].toString().trim().toLowerCase());
                                        data.put("loop_number", String.valueOf(i));
                                        indoorLocations.add(data);
                                        tempArray.add(i);
                                    }
                                }
                            }
                        }
                        if (indoorLocations.size() > 0) {
                            searchingStatus[2] = true;
                        }
                        searchingStatus[4] = true;
                    }
                }
                searchWaitTimer++;
                if (searchingStatus[2] && searchingStatus[1]) {
                    searchTimer.cancel();
                    searchTimer.purge();
                    setIndoorLocationsToGo(indoorLocations);
                    setOutdoorLocationsToGo(outdoorLocations);
                    searchResultAnnouncement(searchingStatus);
                } else if (searchWaitTimer > WAITING_TIME) {
                    if (!searchingStatus[3] && !searchingStatus[4]) {
                        searchTimer.cancel();
                        searchTimer.purge();
                        dialogBoxCreator("No signal! Do you want to wait for another " + WAITING_TIME + " seconds?", user_input);

                    } else {
                        setIndoorLocationsToGo(indoorLocations);
                        setOutdoorLocationsToGo(outdoorLocations);
                        searchTimer.cancel();
                        searchTimer.purge();
                        searchResultAnnouncement(searchingStatus);
                    }
                }
            }
        }, 10, 1000);
    }

    private void searchResultAnnouncement(final boolean[] searchingStatus) {
        if(progressDialogFlag)
            hideProgressDialog();

        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (!searchingStatus[1] && !searchingStatus[2]) {
                    talk.start("Nothing was found!");
                } else if (searchingStatus[2] && !searchingStatus[1]) {
                    talk.start("Please choose one of the options from indoor locations!");
                    listViewToShow(getIndoorLocationsToGo(),2, 1);
                } else if (!searchingStatus[2] && searchingStatus[1]) {
                    talk.start("Please choose one of the options from outdoor locations!");
                    listViewToShow(getOutdoorLocationsToGo(),3, 1);

                } else {
                    final ArrayList<HashMap<String, String>> finalResult = new ArrayList<>();
                    talk.start("Please choose between indoor, outdoor or all locations.");

                    HashMap<String, String> information = new HashMap<>();
                    information.put("description", "Indoor");
                    finalResult.add(information);

                    information = new HashMap<>();
                    information.put("description", "Outdoor");
                    finalResult.add(information);

                    information = new HashMap<>();
                    information.put("description", "All");
                    finalResult.add(information);
                    listViewToShow(finalResult,4, finalResult.size());
                }
            }
        });
    }

    void dialogBoxCreator(final String string, final StringBuilder user_input) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                alertDialog.setTitle("Alert");
                alertDialog.setMessage(string);
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                searchingTime(user_input);
                            }
                        });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                if(progressDialogFlag){
                                    hideProgressDialog();
                                }
                            }
                        });
                alertDialog.show();
                if(buildings_info == null || buildings_info.length == 0)
                    Toast.makeText(getApplicationContext(),"DownloadingError",Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void beaconSighting(StringBuilder beaconID, StringBuilder beaconNameSpace, int BLE_RSSI) {
        beacon_RSSI = BLE_RSSI;
        beacon_id = beaconID;
        beacon_namespace = beaconNameSpace;
        if (!beacon_download_flag)
            loginFunc("beacons", new StringBuilder("admin"), new StringBuilder("W50d9Pqn5a"), beacon_namespace, "");
        else if (beacon_location.toString().compareTo("outdoor") == 0 && inquiry_flag) {
            loginFunc("outdoorInquiry", new StringBuilder("admin"), new StringBuilder("W50d9Pqn5a"), beacon_namespace, beacon_namespace.toString().toLowerCase().trim());
            inquiry_flag = false;
        } else if (inquiry_flag && beacon_location.toString().compareTo("outdoor") != 0) {
            loginFunc("buildingInquiry", new StringBuilder("admin"), new StringBuilder("W50d9Pqn5a"), beacon_namespace, beacon_location.toString());
            inquiry_flag = false;
        }
        lastReceivedTimeStamp = System.currentTimeMillis();

            if (buildings_info != null) {
                if (buildings_info.length > 0) {
                    for (int i = 0; i < buildings_info.length; i++) {
                        if (Integer.valueOf(buildings_info[i][0].toString()).equals(Integer.valueOf(beacon_id.toString()))) {
                            beaconNumber = connections[i][0];
                            if (explorationFlag && !inquiry_flag && beacon_download_flag && !EVACUATION_FLAG) {
                                explorationMode();
                            }
                        }
                    }
                }
            }
            if(indoorWayfindingFlag){
                indoorWayfinding();
            }
        if(EVACUATION_FLAG){
            evacuationMode();
        }
    }


    private void evacuationMode() {
        if(speakFlag){
            int referenceDistanceEvacuationElev=1000,referenceDistanceEvacuationStair=1000, shortestPath, bestNodeElev=0,bestNodeStair=0;
            if (DISTANCE_UNITS != 1){
                referenceDistanceEvacuationElev = meterToFeet(referenceDistanceEvacuationElev);
                referenceDistanceEvacuationStair = meterToFeet(referenceDistanceEvacuationStair);
            }
            ArrayList<Integer> elevator = new ArrayList<>();
            ArrayList<Integer> stairs = new ArrayList<>();

            for(int i=0;i<buildings_info.length;i++){
                if(Integer.valueOf(buildings_info[i][5].toString())==1){
                    elevator.add(i);
                }else if(Integer.valueOf(buildings_info[i][5].toString())==2){
                    stairs.add(i);
                }
            }

            for(int i=0;i<stairs.size();i++){
                shortestPath = 0;
                String[] routePath = Routing.routing(beaconNumber, stairs.get(i), Separation.seperation(connections, buildings_info.length, STEP_SIZE, DISTANCE_UNITS), buildings_info.length).split(",");
                for(int j=0;j<routePath.length-1;j++){
                    for(int k=4;k<20;k=k+2){
                        if(connections[Integer.valueOf(routePath[j])][k]==Integer.valueOf(routePath[j+1])){
                            shortestPath = shortestPath+connections[Integer.valueOf(routePath[j])][k+1];
                        }
                    }
                }
                if(shortestPath<referenceDistanceEvacuationStair) {
                    referenceDistanceEvacuationStair = shortestPath;
                    bestNodeStair = stairs.get(i);
                }
            }

            for(int i=0;i<elevator.size();i++){
                shortestPath = 0;
                String[] routePath = Routing.routing(beaconNumber, elevator.get(i), Separation.seperation(connections, buildings_info.length, STEP_SIZE, DISTANCE_UNITS), buildings_info.length).split(",");
                for(int j=0;j<routePath.length-1;j++){
                    for(int k=4;k<20;k=k+2){
                        if(connections[Integer.valueOf(routePath[j])][k]==Integer.valueOf(routePath[j+1])){
                            shortestPath = shortestPath+connections[Integer.valueOf(routePath[j])][k+1];
                        }
                    }
                }
                if(shortestPath<referenceDistanceEvacuationElev) {
                    referenceDistanceEvacuationElev = shortestPath;
                    bestNodeElev = elevator.get(i);
                }
            }


            SendLocationToServer sendLocationToServer = new SendLocationToServer();
            switch (USER_CATEGORY){
                case 1:
                    sendLocationToServer.execute(String.valueOf(xy[beaconNumber][0]),String.valueOf(xy[beaconNumber][1]),"blind");
                    switch (Integer.valueOf(buildings_info[beaconNumber][4].toString())){
                        case 1:
//                            speaking(new StringBuilder[]{new StringBuilder("Fire Alarm!")});
                            loop_number = String.valueOf(bestNodeStair);
                            break;
                        case 2:
//                            speaking(new StringBuilder[]{new StringBuilder("Tornado Alarm!")});
                            loop_number = String.valueOf(bestNodeStair);
                            break;
                        case 3:
//                            speaking(new StringBuilder[]{new StringBuilder("Shooting Alarm!")});
                            loop_number = String.valueOf(bestNodeStair);
                            break;
                    }
                    break;
                case 2:
                    switch (Integer.valueOf(buildings_info[beaconNumber][4].toString())){
                        case 1:
//                            speaking(new StringBuilder[]{new StringBuilder("Fire Alarm!")});
                            loop_number = String.valueOf(bestNodeStair);
                            break;
                        case 2:
//                            speaking(new StringBuilder[]{new StringBuilder("Tornado Alarm!")});
                            loop_number = String.valueOf(bestNodeStair);
                            break;
                        case 3:
//                            speaking(new StringBuilder[]{new StringBuilder("Shooting Alarm!")});
                            loop_number = String.valueOf(bestNodeStair);
                            break;
                    }
                    break;
                case 3:
                    sendLocationToServer.execute(String.valueOf(xy[beaconNumber][0]),String.valueOf(xy[beaconNumber][1]),"wheelchair");
                    switch (Integer.valueOf(buildings_info[beaconNumber][4].toString())){
                        case 2:
//                            speaking(new StringBuilder[]{new StringBuilder("Tornado Alarm!")});
                            loop_number = String.valueOf(bestNodeElev);
                            break;
                        case 3:
//                            speaking(new StringBuilder[]{new StringBuilder("Shooting Alarm!")});
                            if(referenceDistanceEvacuationElev<referenceDistanceEvacuationStair)
                                loop_number = String.valueOf(bestNodeElev);
                            break;
                    }
                    break;
            }
            speakFlag = false;
        }

        if(!loop_number.equals("-1000")){
            SendLocationToServer sendLocationToServer = new SendLocationToServer();
            switch (USER_CATEGORY){
                case 1:
                    sendLocationToServer.execute(String.valueOf(xy[beaconNumber][0]),String.valueOf(xy[beaconNumber][1]),"blind");
                    break;
                case 3:
                    sendLocationToServer.execute(String.valueOf(xy[beaconNumber][0]),String.valueOf(xy[beaconNumber][1]),"wheelchair");
                    break;
            }

            indoorWayfinding();
        }

    }

    private void explorationMode() {

        if (!explorationFlagInitialization) {
            nodeDeciderArray = new int[Integer.valueOf(buildings_info[Integer.valueOf(beaconNumber)][3].toString())][WMA_OPTION + 1];
            weightedAverage = new double[Integer.valueOf(buildings_info[Integer.valueOf(beaconNumber)][3].toString())];
            explorationFlagInitialization = true;
        } else if (nodeDeciderArray.length > 0 && explorationFlagInitialization) {
            if (nodeDeciderArray[beaconNumber][WMA_OPTION] < WMA_OPTION) {
                nodeDeciderArray[beaconNumber][nodeDeciderArray[beaconNumber][WMA_OPTION]] = beacon_RSSI;
                if (WMA_OPTION == 5) {
                    weightedAverage[beaconNumber] = (nodeDeciderArray[beaconNumber][0] * 0.067 +
                            nodeDeciderArray[beaconNumber][1] * 0.13 + nodeDeciderArray[beaconNumber][2] * 0.2 +
                            nodeDeciderArray[beaconNumber][3] * 0.268 + nodeDeciderArray[beaconNumber][4] * 0.335);
                } else if (WMA_OPTION == 4) {
                    weightedAverage[beaconNumber] = (nodeDeciderArray[beaconNumber][0] * 0.1 +
                            nodeDeciderArray[beaconNumber][1] * 0.2 + nodeDeciderArray[beaconNumber][2] * 0.3 + nodeDeciderArray[beaconNumber][3] * 0.4);
                } else {
                    weightedAverage[beaconNumber] = (nodeDeciderArray[beaconNumber][0] * 0.25 + nodeDeciderArray[beaconNumber][1] * 0.35 + nodeDeciderArray[beaconNumber][2] * 0.4);
                }
                nodeDeciderArray[beaconNumber][WMA_OPTION]++;
            }
            if (nodeDeciderArray[beaconNumber][WMA_OPTION] == WMA_OPTION) {
                int triggerCounter = 0;
                for (int i = 0; i < WMA_OPTION; i++) {
                    if (nodeDeciderArray[beaconNumber][i] > connections[beaconNumber][1])
                        triggerCounter++;
                }
                System.arraycopy(nodeDeciderArray[beaconNumber], 1, nodeDeciderArray[beaconNumber], 0, WMA_OPTION - 1);
                nodeDeciderArray[beaconNumber][WMA_OPTION]--;
                if (triggerCounter > 2 && (int) weightedAverage[beaconNumber] >= connections[beaconNumber][1] &&
                        (int) weightedAverage[beaconNumber] != 0 && triggerCounter > 2) {
                    if (exploredNode != beaconNumber) {
                        exploredNode = beaconNumber;
                        speakOut("Very close to " + buildingSensorsMap[exploredNode][0]);
                        fragmentSighted.xyToDraw(beaconNumber, xy, buildings_info[exploredNode][2].toString(),getRouteSrcDst());
                    }
                }
            }
        }
    }

    @SuppressLint("UseSparseArrays")
    private void readFile() {
        HashMap<Integer, StringBuilder> beacons_array_Hash;
        File file = new File(this.getFilesDir().getAbsolutePath() + File.separator + "BeaconData", "beacons");
        String lineReader;
        BufferedReader br;

        try {
            br = new BufferedReader(new FileReader(file));

            while ((lineReader = br.readLine()) != null) {

                String[] splitter = lineReader.split(",");
                beacons_array_Hash = new HashMap<>();
                beacons_array_Hash.put(Integer.valueOf(splitter[0]), new StringBuilder(splitter[1]));
                beacons_array.add(beacons_array_Hash);

            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < beacons_array.size(); i++) {
            beacons_array_Hash = beacons_array.get(i);
            if (beacons_array_Hash.get(Integer.valueOf(beacon_id.toString())) != null)
                beacon_location = beacons_array_Hash.get(Integer.valueOf(beacon_id.toString()));

        }

        beacon_location = new StringBuilder(beacon_location.toString().trim().toLowerCase());
//        Log.d(TAG, "readFile: " + beacon_location);

        beacon_download_flag = true;
        inquiry_flag = true;

    }

    private void indoorWayfinding() {

        if (!indoorInitialization && number_Of_Sensors==0){
            number_Of_Sensors = Integer.valueOf(buildings_info[Integer.valueOf(loop_number)][3].toString());
            if(number_Of_Sensors!=0){
                nodeDeciderArray = new int[number_Of_Sensors][WMA_OPTION + 1];
                weightedAverage = new double[number_Of_Sensors];
                firstTimeFlag = false;
                reachFlag = false;
                desNumber = connections[Integer.valueOf(loop_number)][0];
                finalDesNumber = desNumber;
                next = -1;
                bufferRouCounter = 0;
                bufferRou = new int[number_Of_Sensors];
                exceptionFlag = false;
                indoorInstructionGenerator = new InstructionGenerator();
                indoorInitialization=true;
            }
        }

        if (beacon_RSSI > FIRST_THRESHOLD && lastSavedTimeBuffer != lastReceivedTimeStamp && indoorInitialization) {

            lastSavedTimeBuffer = lastReceivedTimeStamp;
            if (nodeDeciderArray[beaconNumber][WMA_OPTION] < WMA_OPTION) {
                nodeDeciderArray[beaconNumber][nodeDeciderArray[beaconNumber][WMA_OPTION]] = beacon_RSSI;
                if (WMA_OPTION == 5) {
                    weightedAverage[beaconNumber] = (nodeDeciderArray[beaconNumber][0] * 0.067 +
                            nodeDeciderArray[beaconNumber][1] * 0.13 + nodeDeciderArray[beaconNumber][2] * 0.2 +
                            nodeDeciderArray[beaconNumber][3] * 0.268 + nodeDeciderArray[beaconNumber][4] * 0.335);
                } else if (WMA_OPTION == 4) {
                    weightedAverage[beaconNumber] = (nodeDeciderArray[beaconNumber][0] * 0.1 +
                            nodeDeciderArray[beaconNumber][1] * 0.2 + nodeDeciderArray[beaconNumber][2] * 0.3 +
                            nodeDeciderArray[beaconNumber][3] * 0.4);
                } else {
                    weightedAverage[beaconNumber] = (nodeDeciderArray[beaconNumber][0] * 0.25 +
                            nodeDeciderArray[beaconNumber][1] * 0.35 + nodeDeciderArray[beaconNumber][2] * 0.4);
                }
                nodeDeciderArray[beaconNumber][WMA_OPTION]++;


                if (nodeDeciderArray[beaconNumber][WMA_OPTION] == WMA_OPTION) {
                    if(progressDialogFlag)
                        hideProgressDialog();
                    int triggerCounter = 0;
                    for (int i = 0; i < WMA_OPTION; i++) {
                        if (nodeDeciderArray[beaconNumber][i] > connections[beaconNumber][1])
                            triggerCounter++;
                    }
                    System.arraycopy(nodeDeciderArray[beaconNumber], 1, nodeDeciderArray[beaconNumber], 0, WMA_OPTION - 1);
                    nodeDeciderArray[beaconNumber][WMA_OPTION]--;
                    try {
                        setMapDetails(Separation.seperation(connections, number_Of_Sensors, STEP_SIZE, DISTANCE_UNITS));

                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "setMapDetails Error Indoor", Toast.LENGTH_LONG);
                    }

                    if (!exceptionFlag && current != beaconNumber && (int) weightedAverage[beaconNumber] >= connections[beaconNumber][1] &&
                            (int) weightedAverage[beaconNumber] != 0 && triggerCounter > 2 && finalDesNumber != -1 && !getMapDetails().equals("")) {
                        if ((beaconNumber == finalDesNumber) || (beaconNumber == finalDesNumber && next == -1)) {
                            current = beaconNumber;
                            exploredNode = current;

                            HashMap<String, String> dataToShow = new HashMap<>();
                            ArrayList<HashMap<String,String>> tempList = new ArrayList<>();
                            try {
                                setRouteSrcDst(Routing.routing(beaconNumber, finalDesNumber, getMapDetails(), number_Of_Sensors));
                                next = rf.routfinding(beaconNumber, getRouteSrcDst(), number_Of_Sensors, next);
                            } catch (Exception e) {
                                Toast.makeText(getApplicationContext(), "setRouteSrcDst next Error Indoor", Toast.LENGTH_LONG);
                            }
                            if (!reachFlag) {
                                StringBuilder[] instructionVoice = new StringBuilder[2];
                                OverallGuidance og = new OverallGuidance();
                                try {
                                    StringBuilder[] stepBySteptemp = og.directionGuide(connections[beaconNumber][2], Float.parseFloat(where[1]), true, DIRECTION_OPTION);
                                    instructionVoice[0] = new StringBuilder("You are at destination proximity! destination is somewhere " + stepBySteptemp[0]);
                                    dataToShow.put("description", instructionVoice[0].toString());
                                    tempList.add(dataToShow);

                                    instructionListData.add(tempList);
                                    getResult(instructionListData.get(0).get(0).get("description"), 0, 12);
                                    reachFlag = true;
                                    speakFlag = true;
                                    speaking(instructionVoice);
                                } catch (Exception e) {
                                    Log.e(TAG,"Error to find destination!");
                                }
                            }
                        }

                        if (!reachFlag) {
                            if (!firstTimeFlag) {
                                try {
                                    current = beaconNumber;
                                    setRouteSrcDst(Routing.routing(beaconNumber, finalDesNumber, getMapDetails(), number_Of_Sensors));
                                    next = rf.routfinding(beaconNumber, getRouteSrcDst(), number_Of_Sensors, next);
                                    speakFlag = true;
                                    instructionListData = indoorInstructionGenerator.completePath(getRouteSrcDst(), Float.parseFloat(where[1]), connections,
                                                    DIRECTION_OPTION, getMapDetails(), MIDPOINTS_LOCATIONS, DISTANCE_OPTION, buildingSensorsMap, reachFlag,
                                                    explorationFlag, unit, ROUTE_PREVIEW, firstTimeFlag);

                                    instruction = instructionListData.get(1);

                                    instructionVoice[0] = new StringBuilder(instructionListData.get(2).get(0).get("instructionVoice[0]"));
                                    instructionVoice[1] = new StringBuilder(instructionListData.get(2).get(1).get("instructionVoice[1]"));
                                    explorationFlag = Boolean.valueOf(instructionListData.get(2).get(2).get("explorationFlag"));
                                    reachFlag = Boolean.valueOf(instructionListData.get(2).get(3).get("reachFlag"));

                                    getResult(instructionListData.get(0).get(0).get("description"), 0, 12);
                                    speaking(instructionVoice);
                                    firstTimeFlag = true;
                                } catch (Exception e) {
                                    Toast.makeText(getApplicationContext(), "!firstTimeFlag Error Indoor", Toast.LENGTH_LONG);
                                }
                            } else {
                                if (beaconNumber == next) {
                                    try {
                                        setRouteSrcDst(Routing.routing(beaconNumber, finalDesNumber, getMapDetails(), number_Of_Sensors));
                                        next = rf.routfinding(beaconNumber, getRouteSrcDst(), number_Of_Sensors, next);
                                        speakFlag = true;
                                        current = beaconNumber;
                                        setRouteSrcDst(Routing.routing(beaconNumber, finalDesNumber, getMapDetails(), number_Of_Sensors));
                                        next = rf.routfinding(beaconNumber, getRouteSrcDst(), number_Of_Sensors, next);
                                    } catch (Exception e) {
                                        Toast.makeText(getApplicationContext(), "!firstTimeFlag Error Indoor", Toast.LENGTH_LONG);
                                    }
                                    instructionListData = indoorInstructionGenerator.completePath(getRouteSrcDst(), Float.parseFloat(where[1]), connections,
                                            DIRECTION_OPTION, getMapDetails(), MIDPOINTS_LOCATIONS, DISTANCE_OPTION, buildingSensorsMap, reachFlag,
                                            explorationFlag, unit, false, false);

                                    instruction = instructionListData.get(0);

                                    instructionVoice[0] = new StringBuilder(instructionListData.get(2).get(0).get("instructionVoice[0]"));
                                    instructionVoice[1] = new StringBuilder(instructionListData.get(2).get(1).get("instructionVoice[1]"));
                                    explorationFlag = Boolean.valueOf(instructionListData.get(2).get(2).get("explorationFlag"));
                                    reachFlag = Boolean.valueOf(instructionListData.get(2).get(3).get("reachFlag"));

                                    final ArrayList<HashMap<String, String>> resultsToshow = new ArrayList<>();
                                    resultsToshow.addAll(instruction);
                                    getResult(instructionListData.get(0).get(0).get("description"), 0, 12);
                                    speaking(instructionVoice);
                                } else {
                                    bufferRou = new int[number_Of_Sensors];
                                    bufferRouCounter = 0;
                                    String[] sString = getRouteSrcDst().split(",");
                                    for (int i = 0; i < sString.length; i++) {
                                        bufferRou[bufferRouCounter] = Integer.parseInt(sString[bufferRouCounter]);
                                        bufferRouCounter++;
                                    }
                                    exceptionFlag = true;
                                }
                            }

                        }
                    } else if (exceptionFlag && current != beaconNumber && (int) weightedAverage[beaconNumber] >= connections[beaconNumber][1] &&
                            (int) weightedAverage[beaconNumber] != 0 && triggerCounter > 2 && finalDesNumber != -1) {

                        for (int p = 0; p < bufferRou.length; p++) {
                            if (beaconNumber == bufferRou[p]) {
                                speakFlag = true;
                                current = beaconNumber;
                                setRouteSrcDst(Routing.routing(beaconNumber, finalDesNumber, getMapDetails(), number_Of_Sensors));
                                next = rf.routfinding(beaconNumber, getRouteSrcDst(), number_Of_Sensors, next);
                                instructionListData =
                                        indoorInstructionGenerator.completePath(getRouteSrcDst(), Float.parseFloat(where[1]), connections,
                                                DIRECTION_OPTION, getMapDetails(), MIDPOINTS_LOCATIONS,
                                                DISTANCE_OPTION, buildingSensorsMap, reachFlag,
                                                explorationFlag, unit, ROUTE_PREVIEW, firstTimeFlag);

                                instruction = instructionListData.get(1);

                                instructionVoice[0] = new StringBuilder(instructionListData.get(2).get(0).get("instructionVoice[0]"));
                                instructionVoice[1] = new StringBuilder(instructionListData.get(2).get(1).get("instructionVoice[1]"));
                                explorationFlag = Boolean.valueOf(instructionListData.get(2).get(2).get("explorationFlag"));
                                reachFlag = Boolean.valueOf(instructionListData.get(2).get(3).get("reachFlag"));
                                getResult(instructionListData.get(0).get(0).get("description"), 0, 12);
                                speaking(instructionVoice);
                                exceptionFlag = false;
                            }
                        }
                        if (exceptionFlag) {
                            nodeDeciderArray = new int[number_Of_Sensors][WMA_OPTION + 1];
                            weightedAverage = new double[number_Of_Sensors];
                            speakFlag = true;
                            rerouting = true;
                            current = beaconNumber;
                            setRouteSrcDst(Routing.routing(beaconNumber, finalDesNumber, getMapDetails(), number_Of_Sensors));
                            next = rf.routfinding(beaconNumber, getRouteSrcDst(), number_Of_Sensors, next);
                            instructionListData =
                                    indoorInstructionGenerator.completePath(getRouteSrcDst(), Float.parseFloat(where[1]), connections,
                                            DIRECTION_OPTION, getMapDetails(), MIDPOINTS_LOCATIONS,
                                            DISTANCE_OPTION, buildingSensorsMap, reachFlag,
                                            explorationFlag, unit, ROUTE_PREVIEW, firstTimeFlag);

                            instruction = instructionListData.get(1);

                            instructionVoice[0] = new StringBuilder(instructionListData.get(2).get(0).get("instructionVoice[0]"));
                            instructionVoice[1] = new StringBuilder(instructionListData.get(2).get(1).get("instructionVoice[1]"));
                            explorationFlag = Boolean.valueOf(instructionListData.get(2).get(2).get("explorationFlag"));
                            reachFlag = Boolean.valueOf(instructionListData.get(2).get(3).get("reachFlag"));

                            getResult(instructionListData.get(0).get(0).get("description"), 0, 12);
                            speaking(instructionVoice);
                            exceptionFlag = false;
                        }
                    }
                }
            }
        }

        if (reachFlag) {
            indoorWayfindingFlag=false;
            indoorInitialization=false;
        }
    }

    void speaking(final StringBuilder[] instructionVoice) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (speakFlag) {
                    try {
                        if (rerouting) {
                            speakOut("Re Routing" + ", " + instructionVoice[0].toString());
                            rerouting=false;
                        }else
                            speakOut(instructionVoice[0].toString());
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Speaking error", Toast.LENGTH_LONG);
                    }
                    speakFlag = false;
                }
            }
        });
    }

    private void speakOut(final String text) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        } else {
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    ArrayList <Integer> blockNodes = new ArrayList<>();
    ArrayList <Integer> blockNodesDistance = new ArrayList<>();
    int blockZone = 6;

    @Override
    public void serverInquiry(ArrayList<HashMap<String, String>> serverInquiryResult) {
        int num;
        if (serverInquiryResult.size() > 0) {
            switch (serverInquiryResult.get(0).get("inquiryMode")) {
                case "outdoorInquiry":
                    break;
                case "buildingInquiry":
                    buildingSensorsMap = new String[Integer.parseInt(serverInquiryResult.get(0).get("numsens"))][2];
                    connections = new int[Integer.parseInt(serverInquiryResult.get(0).get("numsens"))][24];
                    buildings_info = new StringBuilder[Integer.parseInt(serverInquiryResult.get(0).get("numsens"))][6];
                    num = Integer.parseInt(serverInquiryResult.get(0).get("numsens"));
                    for (int i = 0; i < serverInquiryResult.size(); i++) {
                        buildings_info[Integer.valueOf(serverInquiryResult.get(i).get("node"))][0] = new StringBuilder(serverInquiryResult.get(i).get("id"));
                        buildings_info[Integer.valueOf(serverInquiryResult.get(i).get("node"))][1] = new StringBuilder(serverInquiryResult.get(i).get("locname"));
                        buildings_info[Integer.valueOf(serverInquiryResult.get(i).get("node"))][2] = new StringBuilder(serverInquiryResult.get(i).get("other"));
                        buildings_info[Integer.valueOf(serverInquiryResult.get(i).get("node"))][3] = new StringBuilder(serverInquiryResult.get(i).get("numsens"));
                        buildings_info[Integer.valueOf(serverInquiryResult.get(i).get("node"))][4] = new StringBuilder(serverInquiryResult.get(i).get("emgmode"));
                        buildings_info[Integer.valueOf(serverInquiryResult.get(i).get("node"))][5] = new StringBuilder(serverInquiryResult.get(i).get("emgtype"));

                        if(Integer.valueOf(serverInquiryResult.get(i).get("safenode"))!=1) {
                            blockNodes.add(Integer.valueOf(serverInquiryResult.get(i).get("node")));
                            if (DISTANCE_UNITS == 1)
                                blockNodesDistance.add(blockZone);
                            else
                                blockNodesDistance.add(meterToFeet(blockZone));
                        }

                        connections[Integer.valueOf(serverInquiryResult.get(i).get("node"))][0] = Integer.valueOf(serverInquiryResult.get(i).get("node"));
                        connections[Integer.valueOf(serverInquiryResult.get(i).get("node"))][1] = Integer.valueOf(serverInquiryResult.get(i).get("threshold"));
                        connections[Integer.valueOf(serverInquiryResult.get(i).get("node"))][2] = Integer.valueOf(serverInquiryResult.get(i).get("direction"));
                        connections[Integer.valueOf(serverInquiryResult.get(i).get("node"))][3] = Integer.valueOf(serverInquiryResult.get(i).get("level"));
                        //North
                        connections[Integer.valueOf(serverInquiryResult.get(i).get("node"))][4] = Integer.valueOf(serverInquiryResult.get(i).get("bnorth"));
                        if (DISTANCE_UNITS == 1)
                            connections[Integer.valueOf(serverInquiryResult.get(i).get("node"))][5] = Integer.valueOf(serverInquiryResult.get(i).get("ndist"));
                        else
                            connections[Integer.valueOf(serverInquiryResult.get(i).get("node"))][5] = meterToFeet(Integer.valueOf(serverInquiryResult.get(i).get("ndist")));
                        //South
                        connections[Integer.valueOf(serverInquiryResult.get(i).get("node"))][6] = Integer.valueOf(serverInquiryResult.get(i).get("bsouth"));
                        if (DISTANCE_UNITS == 1)
                            connections[Integer.valueOf(serverInquiryResult.get(i).get("node"))][7] = Integer.valueOf(serverInquiryResult.get(i).get("sdist"));
                        else
                            connections[Integer.valueOf(serverInquiryResult.get(i).get("node"))][7] = meterToFeet(Integer.valueOf(serverInquiryResult.get(i).get("sdist")));
                        //East
                        connections[Integer.valueOf(serverInquiryResult.get(i).get("node"))][8] = Integer.valueOf(serverInquiryResult.get(i).get("beast"));
                        if (DISTANCE_UNITS == 1)
                            connections[Integer.valueOf(serverInquiryResult.get(i).get("node"))][9] = Integer.valueOf(serverInquiryResult.get(i).get("edist"));
                        else
                            connections[Integer.valueOf(serverInquiryResult.get(i).get("node"))][9] = meterToFeet(Integer.valueOf(serverInquiryResult.get(i).get("edist")));
                        //West
                        connections[Integer.valueOf(serverInquiryResult.get(i).get("node"))][10] = Integer.valueOf(serverInquiryResult.get(i).get("bwest"));
                        if (DISTANCE_UNITS == 1)
                            connections[Integer.valueOf(serverInquiryResult.get(i).get("node"))][11] = Integer.valueOf(serverInquiryResult.get(i).get("wdist"));
                        else
                            connections[Integer.valueOf(serverInquiryResult.get(i).get("node"))][11] = meterToFeet(Integer.valueOf(serverInquiryResult.get(i).get("wdist")));
                        //North East
                        connections[Integer.valueOf(serverInquiryResult.get(i).get("node"))][12] = Integer.valueOf(serverInquiryResult.get(i).get("bneast"));
                        if (DISTANCE_UNITS == 1)
                            connections[Integer.valueOf(serverInquiryResult.get(i).get("node"))][13] = Integer.valueOf(serverInquiryResult.get(i).get("neastdist"));
                        else
                            connections[Integer.valueOf(serverInquiryResult.get(i).get("node"))][13] = meterToFeet(Integer.valueOf(serverInquiryResult.get(i).get("neastdist")));
                        //North West
                        connections[Integer.valueOf(serverInquiryResult.get(i).get("node"))][14] = Integer.valueOf(serverInquiryResult.get(i).get("bnwest"));
                        if (DISTANCE_UNITS == 1)
                            connections[Integer.valueOf(serverInquiryResult.get(i).get("node"))][15] = Integer.valueOf(serverInquiryResult.get(i).get("nwestdist"));
                        else
                            connections[Integer.valueOf(serverInquiryResult.get(i).get("node"))][15] = meterToFeet(Integer.valueOf(serverInquiryResult.get(i).get("nwestdist")));
                        //South East
                        connections[Integer.valueOf(serverInquiryResult.get(i).get("node"))][16] = Integer.valueOf(serverInquiryResult.get(i).get("bseast"));
                        if (DISTANCE_UNITS == 1)
                            connections[Integer.valueOf(serverInquiryResult.get(i).get("node"))][17] = Integer.valueOf(serverInquiryResult.get(i).get("seastdist"));
                        else
                            connections[Integer.valueOf(serverInquiryResult.get(i).get("node"))][17] = meterToFeet(Integer.valueOf(serverInquiryResult.get(i).get("seastdist")));
                        //South West
                        connections[Integer.valueOf(serverInquiryResult.get(i).get("node"))][18] = Integer.valueOf(serverInquiryResult.get(i).get("bswest"));
                        if (DISTANCE_UNITS == 1)
                            connections[Integer.valueOf(serverInquiryResult.get(i).get("node"))][19] = Integer.valueOf(serverInquiryResult.get(i).get("swestdist"));
                        else
                            connections[Integer.valueOf(serverInquiryResult.get(i).get("node"))][19] = meterToFeet(Integer.valueOf(serverInquiryResult.get(i).get("swestdist")));

                        buildingSensorsMap[Integer.valueOf(serverInquiryResult.get(i).get("node"))][0] = buildings_info[Integer.valueOf(serverInquiryResult.get(i).get("node"))][1].toString();
                        buildingSensorsMap[Integer.valueOf(serverInquiryResult.get(i).get("node"))][1] = String.valueOf(connections[Integer.valueOf(serverInquiryResult.get(i).get("node"))][0]);
//                        Log.d(TAG, Integer.valueOf(serverInquiryResult.get(i).get("node"))+"->"+connections[Integer.valueOf(serverInquiryResult.get(i).get("node"))][4]+" = "+connections[Integer.valueOf(serverInquiryResult.get(i).get("node"))][5]+", "+
//                                connections[Integer.valueOf(serverInquiryResult.get(i).get("node"))][6]+" = "+connections[Integer.valueOf(serverInquiryResult.get(i).get("node"))][7]+", "+
//                                connections[Integer.valueOf(serverInquiryResult.get(i).get("node"))][8]+" = "+connections[Integer.valueOf(serverInquiryResult.get(i).get("node"))][9]+", "+
//                                connections[Integer.valueOf(serverInquiryResult.get(i).get("node"))][10]+" = "+connections[Integer.valueOf(serverInquiryResult.get(i).get("node"))][11]+", "+
//                                connections[Integer.valueOf(serverInquiryResult.get(i).get("node"))][12]+" = "+connections[Integer.valueOf(serverInquiryResult.get(i).get("node"))][13]+", "+
//                                connections[Integer.valueOf(serverInquiryResult.get(i).get("node"))][14]+" = "+connections[Integer.valueOf(serverInquiryResult.get(i).get("node"))][15]+", "+
//                                connections[Integer.valueOf(serverInquiryResult.get(i).get("node"))][16]+" = "+connections[Integer.valueOf(serverInquiryResult.get(i).get("node"))][17]+", "+
//                                connections[Integer.valueOf(serverInquiryResult.get(i).get("node"))][18]+" = "+connections[Integer.valueOf(serverInquiryResult.get(i).get("node"))][19]);
                    }

//                    StringBuilder mystrnodesrc = new StringBuilder("s = [");
//                    StringBuilder mystrnodedes = new StringBuilder("t = [");
//                    StringBuilder mystrweight = new StringBuilder("weights = [");
//                    StringBuilder mystrname = new StringBuilder("names = {");
//                    for(int k=0;k<num;k++){
//                        for(int j=4;j<20;j=j+2){
//                            if(connections[k][j]!=-10 && (connections[k][j]+1)>(k+1)){
//                                mystrnodesrc.append(k+1).append(" ");
//                                mystrnodedes.append(connections[k][j]+1).append(" ");
//                                mystrweight.append(connections[k][j+1]).append(" ");
//
//                            }
//                        }
//                    }
//                    for(int k=0;k<num-1;k++) {
//                        mystrname.append('\'').append(k).append('\'').append(" ");
//                    }
//                    mystrnodesrc.append("];");
//                    mystrnodedes.append("];");
//                    mystrweight.append("];");
//                    mystrname.append("};");
//                    Log.d(TAG, "serverInquiry: "+mystrnodesrc+mystrnodedes+mystrweight+mystrname+"G = graph(s,t,weights,names);"+"plot(G,'EdgeLabel',G.Edges.Weight);");
//                    Log.d(TAG, "serverInquiry: "+mystrnodedes);
//                    Log.d(TAG, "serverInquiry: "+mystrweight);
//                    Log.d(TAG, "serverInquiry: "+mystrname);
//                    Log.d(TAG, "serverInquiry: "+"G = graph(s,t,weights,names)");
//                    Log.d(TAG, "serverInquiry: "+"plot(G,'EdgeLabel',G.Edges.Weight)");

                    StringBuilder mystrweight = new StringBuilder("weights = [");
                    StringBuilder nodeName = new StringBuilder("nodes = [");
                    StringBuilder xSiteSRC = new StringBuilder("xs = [");
                    StringBuilder ySiteSRC = new StringBuilder("ys = [");
                    StringBuilder xSiteDES = new StringBuilder("xd = [");
                    StringBuilder ySiteDES = new StringBuilder("yd = [");
                    for(int k=0;k<num;k++){
                        for(int j=4;j<20;j=j+2){
                            if(connections[k][j]!=-10){
                                xSiteSRC.append(xy[k][0]).append(", ");
                                ySiteSRC.append(xy[k][1]).append(", ");
                                xSiteDES.append(xy[connections[k][j]][0]).append(", ");
                                ySiteDES.append(xy[connections[k][j]][1]).append(", ");


                                if(connections[k][1]==200 || connections[k][j+1]==0) {
                                    nodeName.append("-10").append(", ");
                                    mystrweight.append("-10").append(", ");
                                }else {
                                    nodeName.append(k).append(", ");
                                    mystrweight.append(connections[k][j+1]).append(", ");
                                }
                            }
                        }
                    }
                    Log.d(TAG, " "+xSiteSRC.append("];"));
                    Log.d(TAG, " "+ySiteSRC.append("];"));
                    Log.d(TAG, " "+xSiteDES.append("];"));
                    Log.d(TAG, " "+ySiteDES.append("];"));
                    Log.d(TAG, " "+nodeName.append("];"));
                    Log.d(TAG, " "+mystrweight.append("];"));

                    if(Integer.valueOf(buildings_info[beaconNumber][4].toString())>0){
                        EVACUATION_FLAG=true;
                        speakFlag = true;
                        int nodeToChange, updateDistance;
                        for(int i=0;i<blockNodes.size();i++) {
                            nodeToChange = blockNodes.get(i);
                            updateDistance = blockNodesDistance.get(i);
                            blockNodes.remove(i);
                            blockNodesDistance.remove(i);
                            for(int k=0;k<num;k++){
                                for(int j=4;j<20;j=j+2){
                                    if(connections[k][j]==nodeToChange){
                                        if(updateDistance-connections[k][j+1]>0){
                                            blockNodesDistance.add(updateDistance-connections[k][j+1]);
                                            blockNodes.add(k);
                                        }
                                        connections[k][j+1]=connections[k][j+1]+500;
                                        i=-1;
                                    }
                                    if(k==nodeToChange){
                                        if(connections[k][j]!=-10){
                                            connections[k][j+1]=connections[k][j+1]+500;
                                        }
                                    }
                                }
                            }

                        }

                        Log.d(TAG, "***********************************************************************");
//                        for(int k=0;k<num;k++){
//                                Log.d(TAG, Integer.valueOf(k)+"->"+connections[k][4]+" = "+connections[k][5]+", "+
//                                        connections[k][6]+" = "+connections[k][7]+", "+connections[k][8]+" = "+connections[k][9]+", "+
//                                        connections[k][10]+" = "+connections[k][11]+", "+connections[k][12]+" = "+connections[k][13]+", "+
//                                        connections[k][14]+" = "+connections[k][15]+", "+connections[k][16]+" = "+connections[k][17]+", "+
//                                        connections[k][18]+" = "+connections[k][19]);
//                        }
                        mystrweight = new StringBuilder("weights = [");
                        nodeName = new StringBuilder("nodes = [");
                        xSiteSRC = new StringBuilder("xs = [");
                        ySiteSRC = new StringBuilder("ys = [");
                        xSiteDES = new StringBuilder("xd = [");
                        ySiteDES = new StringBuilder("yd = [");
                        for(int k=0;k<num;k++){
                            for(int j=4;j<20;j=j+2){
                                if(connections[k][j]!=-10){
                                    xSiteSRC.append(xy[k][0]).append(", ");
                                    ySiteSRC.append(xy[k][1]).append(", ");
                                    xSiteDES.append(xy[connections[k][j]][0]).append(", ");
                                    ySiteDES.append(xy[connections[k][j]][1]).append(", ");


                                    if(connections[k][1]==200 || connections[k][j+1]==0) {
                                        nodeName.append("-10").append(", ");
                                        mystrweight.append("-10").append(", ");
                                    }else {
                                        nodeName.append(k).append(", ");
                                        mystrweight.append(connections[k][j+1]).append(", ");
                                    }
                                }
                            }
                        }
                        Log.d(TAG, " "+xSiteSRC.append("];"));
                        Log.d(TAG, " "+ySiteSRC.append("];"));
                        Log.d(TAG, " "+xSiteDES.append("];"));
                        Log.d(TAG, " "+ySiteDES.append("];"));
                        Log.d(TAG, " "+nodeName.append("];"));
                        Log.d(TAG, " "+mystrweight.append("];"));

                    }



                    break;
                case "login":
                    break;
            }
        } else if (!beacon_download_flag) {
            readFile();
        }
    }

    private void loginFunc(String typeOfAction, StringBuilder uName, StringBuilder pass,
                           StringBuilder namespace, String locationName) {

        DataBaseConnection dataBaseConnection = new DataBaseConnection(this);
        dataBaseConnection.execute(typeOfAction, uName.toString(), pass.toString(), namespace.toString().toLowerCase(), locationName);
    }

    private void sightedActivity(){

        myFrame.removeAllViews();
        FragmentManager fragmentSightedManager  = getSupportFragmentManager();
        SightedFragment fragmentSighted  = new SightedFragment();
        FragmentTransaction fragmentSightedTransaction;
        fragmentSightedTransaction = fragmentSightedManager.beginTransaction();
        fragmentSightedTransaction.replace(R.id.frame, fragmentSighted);
        fragmentSightedTransaction.commit();

        fragmentSighted.xyToDraw(5, xy, "",getRouteSrcDst());

    }

    private void pixelsInitialization() {
        xy[0][0] = 737;xy[0][1] = 110;
        xy[1][0] = 737;xy[1][1] = 277;
        xy[2][0] = 212;xy[2][1] = 277;
        xy[3][0] = 354;xy[3][1] = 277;
        xy[4][0] = 34;xy[4][1] = 549;
        xy[5][0] = 116;xy[5][1] = 617;
        xy[6][0] = 155;xy[6][1] = 277;
        xy[7][0] = 34;xy[7][1] = 110;
        xy[8][0] = 259;xy[8][1] = 617;
        xy[9][0] = 570;xy[9][1] = 277;
        xy[10][0] = 493;xy[10][1] = 277;
        xy[11][0] = 152;xy[11][1] = 110;
        xy[12][0] = 228;xy[12][1] = 110;
        xy[13][0] = 34;xy[13][1] = 311;
        xy[14][0] = 34;xy[14][1] = 245;
        xy[15][0] = 400;xy[15][1] = 110;
        xy[16][0] = 276;xy[16][1] = 198;
        xy[17][0] = 508;xy[17][1] = 110;
        xy[18][0] = 712;xy[18][1] = 110;
        xy[19][0] = 307;xy[19][1] = 110;
        xy[20][0] = 737;xy[20][1] = 277;
        xy[21][0] = 508;xy[21][1] = 110;
        xy[22][0] = 570;xy[22][1] = 277;
        xy[23][0] = 508;xy[23][1] = 110;
        xy[24][0] = 34;xy[24][1] = 549;
        xy[25][0] = 400;xy[25][1] = 110;
        xy[26][0] = 677;xy[26][1] = 277;
        xy[27][0] = 354;xy[27][1] = 380;
        xy[28][0] = 354;xy[28][1] = 542;
        xy[29][0] = 590;xy[29][1] = 110;
        xy[30][0] = 259;xy[30][1] = 607;
        xy[31][0] = 116;xy[31][1] = 677;
        xy[32][0] = 354;xy[32][1] = 542;
        xy[33][0] = 354;xy[33][1] = 542;
        xy[34][0] = 354;xy[34][1] = 617;
        xy[35][0] = 34;xy[35][1] = 617;
        xy[36][0] = 34;xy[36][1] = 277;
        xy[37][0] = 803;xy[37][1] = 110;
        xy[38][0] = 152;xy[38][1] = 110;
        xy[39][0] = 34;xy[39][1] = 677;

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            settingParameters();
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    sightedActivity();
                    frgno = 2;
                    return true;
                case R.id.Setting_tab:
                    myFrame.removeAllViews();
                    FragmentManager fragmentSettingManager= getSupportFragmentManager();
                    SettingsActivity fragmentSetting = new SettingsActivity();
                    FragmentTransaction fragmentSettingTransaction;
                    fragmentSettingTransaction = fragmentSettingManager.beginTransaction();
                    fragmentSettingTransaction.replace(R.id.frame, fragmentSetting);
                    fragmentSettingTransaction.commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onPostResume() {
        super.onPostResume();
        settingParameters();
    }

    private void settingParameters() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        GPS_ACCURACY = Integer.valueOf(prefs.getString("accuracy_int", "5000"));
        PROXIMITY_RADIUS = Integer.valueOf(prefs.getString("coverage_int", "500"));
        WAITING_TIME = Integer.valueOf(prefs.getString("timer", "5"));
        WMA_OPTION = Integer.valueOf(prefs.getString("wma_int", "5"));
        FIRST_THRESHOLD = Integer.valueOf(prefs.getString("first_threshold_int", "-80"));
        USER_CATEGORY = Integer.valueOf(prefs.getString("example_list", "1"));
        ROUTE_PREVIEW = prefs.getBoolean("switch_preference", false);
        STEP_SIZE = (int)Math.round(Double.valueOf(prefs.getString("step_size_int", "1.25"))*30.48);
        DISTANCE_UNITS = Integer.valueOf(prefs.getString("distance_units_list", "2"));//Refers to meter or feet
        DISTANCE_OPTION = Integer.valueOf(prefs.getString("distance_pref_option", "1"));
        DIRECTION_OPTION = Integer.valueOf(prefs.getString("direction_pref_option", "1"));
        MIDPOINTS_LOCATIONS = prefs.getBoolean("midpoints_preference", true);

        if (FIRST_THRESHOLD > 0)
            FIRST_THRESHOLD = FIRST_THRESHOLD * -1;
        if (DISTANCE_UNITS == 1) {
            unit = " meters";
        }else {
            unit = " feet";
        }
        explorationFlagInitialization = false;
//        Log.d(TAG, "GPS_ACCURACY: "+GPS_ACCURACY+" PROXIMITY_RADIUS: "+PROXIMITY_RADIUS+" WAITING_TIME: "+WAITING_TIME+" WMA_OPTION: "+WMA_OPTION
//                +" FIRST_THRESHOLD: "+FIRST_THRESHOLD+" USER_CATEGORY: "+USER_CATEGORY+" ROUTE_PREVIEW: "+ROUTE_PREVIEW+" STEP_SIZE: "+STEP_SIZE+
//                " DISTANCE_UNITS: "+unit+" DISTANCE_OPTION: "+DISTANCE_OPTION+" DIRECTION_OPTION: "+DIRECTION_OPTION+" MIDPOINTS_LOCATIONS: "+MIDPOINTS_LOCATIONS);

    }

    private void showProgressDialog() {
        talk.start("Start Searching!");
        progressDialogFlag=true;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                progressDialog = new ProgressBar(MainActivity.this, null, android.R.attr.progressBarStyleLarge);
                params = new RelativeLayout.LayoutParams(500, 500);
                params.addRule(RelativeLayout.CENTER_IN_PARENT);
                layout.addView(progressDialog, params);
                progressDialog.setVisibility(View.VISIBLE);  //To show ProgressBar
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);// To disable the user interaction
            }
        });

    }

    private void hideProgressDialog() {
        progressDialogFlag=false;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // To Hide ProgressBar
                 progressDialog.setVisibility(View.GONE);
                 //To get user interaction back
                 getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }
        });

    }

    private void outdoorWayfinding(final double lat, final double lng) {
//        showProgressDialog();
//        Log.d(TAG, "outdoorWayfinding: "+lat+", "+lng);
//        final Timer GPSTimer = new Timer();
//        GPSTimer.scheduleAtFixedRate(new TimerTask() {
//
//            ArrayList<HashMap<String, String>> informationList = new ArrayList<>();
//            boolean startFlag=false,preciseEnq=false;
//            int lastNumberOfSteps=numberOfStepsBySensor;
//            Location estimatedLocation = new Location("");
//            int counterTalk=0;
//            long milisecGim=0;
//            String bName="";
//            double tempTime;
//            boolean annFlag=false;
//            boolean reachedDestination=false;
//
//            @Override
//            public void run() {
//                if(!startFlag) {
//                    estimatedLocation.setLatitude(0);
//                    estimatedLocation.setLongitude(0);
//                    estimatedLocation.setAccuracy(100000);
//                    estimatedLocation.setTime(0);
//                    startFlag=true;
//                }
//
//                if(lastReceivedTimeStamp!=milisecGim && beaconNumber == -10 && beaconRSSI>-80 && (!bName.equals(beaconName.toString()))) { // || lastReceivedTimeStamp-milisecGim>One_MINUTES)){
////                    Log.d(TAG, "run: "+locationName.toString().replaceAll("\\s+","").toLowerCase()+" , "+"gps"+searchedItem.toString().replaceAll("\\s+","").toLowerCase());
//                    if(locationName.toString().replaceAll("\\s+","").toLowerCase().equals("gps"+searchedItem.toString().replaceAll("\\s+","").toLowerCase())){
//                        reachedDestination=true;
//                        talk.start("Destination is somewhere around you!");
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {

//                            }
//                        });
//
//                    }
//                    if(reachedDestination==false) {
//                        Log.d(TAG, "run Time:" + (lastReceivedTimeStamp - milisecGim) + ", " + One_MINUTES);
//                        tempTime = lastReceivedTimeStamp - milisecGim;
//                        milisecGim = lastReceivedTimeStamp;
//                        bName = beaconName.toString();
//
//                        String[] coordinatesGimbal = bName.split("\\*");
//
//                        coordinatesGimbal[0] = new StringBuffer(coordinatesGimbal[0]).insert(2, ".").toString();
//                        coordinatesGimbal[1] = new StringBuffer(coordinatesGimbal[1]).insert(3, ".").toString();
//
//                        double gimbLat = Double.parseDouble(coordinatesGimbal[0]);
//                        double gimbLng = Double.parseDouble(coordinatesGimbal[1]);
//
//                        Log.d(TAG + " Gimbal outdoor", coordinatesGimbal[0] + ", " + coordinatesGimbal[1]);
//
//                        if (!preciseEnq) {
//                            preciseEnq = true;
//                            informationList = showGPSList(lat, lng, gimbLat, gimbLng);
//                        } else {
////                        informationList = updateGPSList(informationList,gimbLat,gimbLng,tempTime);
//                            informationList = showGPSList(lat, lng, gimbLat, gimbLng);
//                        }
//                        estimatedLocation.setTime(lastReceivedTimeStamp);
//                        estimatedLocation.setAccuracy(5);
//                        estimatedLocation.setLatitude(gimbLat);
//                        estimatedLocation.setLongitude(gimbLng);
//
//                        lastNumberOfSteps = numberOfStepsBySensor;
//                    }
//                } else if(estimatedLocation.getTime()!= newLocation.getTime() && !reachedDestination){
//
//                    double newAcc = newLocation.getAccuracy();
//
//                    if(newAcc<=GPS_MIN_ACCURACY){
//
//                        double newLat = newLocation.getLatitude();
//                        double newLng = newLocation.getLongitude();
//
//                        if(!preciseEnq) {
//                            preciseEnq=true;
//                            informationList = showGPSList(lat, lng, newLat , newLng);
//                        }else{
//                            informationList = updateGPSList(informationList,newLat,newLng,tempTime);
//                        }
//
//                        estimatedLocation = newLocation;
//                        lastNumberOfSteps=numberOfStepsBySensor;
//                        Log.d(TAG, " GPS <= "+GPS_MIN_ACCURACY+" newLatLng: "+newLat +" , "+newLng);
//
//                    }else if(preciseEnq && newAcc>GPS_MIN_ACCURACY){
//
//                        int aveDir=0;
//                        for(int i=0;i<10;i++)
//                            aveDir=aveDir+directions[i];
//                        aveDir = aveDir/10;
//
//                        double disTraveled = (numberOfStepsBySensor-lastNumberOfSteps)*0.0006;
//                        double prevEstLat = estimatedLocation.getLatitude();
//                        double prevEstLng = estimatedLocation.getLongitude();
//                        double newEstLatLng[]=DestinationLatLng(prevEstLat,prevEstLng,aveDir, disTraveled);
//
////                        Log.d(TAG, " GPS > "+GPS_MIN_ACCURACY+" newLatLng: "+newEstLatLng[0]+", "+newEstLatLng[1]+", "+
////                                " PrevLoc: "+prevEstLat+", "+prevEstLng+", AD: "+aveDir+", DT By step counting: "+disTraveled);
//                        Log.d(TAG, " GPS > "+"Des coor: "+lat+" , "+lng+" Cur coor: "+estimatedLocation.getLatitude()+" , "+estimatedLocation.getLongitude());
////                        for(int i=0;i<informationList.size();i++)
////                            Log.d(TAG, informationList.get(i).get("lat")+" , "+informationList.get(i).get("lng"));
//
//                        estimatedLocation.setLatitude(newEstLatLng[0]);
//                        estimatedLocation.setLongitude(newEstLatLng[1]);
//                        estimatedLocation.setTime(newLocation.getTime());
//                        estimatedLocation.setAccuracy((float) (disTraveled*0.00006));
//                        lastNumberOfSteps=numberOfStepsBySensor;
//                        informationList = updateGPSList(informationList,newEstLatLng[0],newEstLatLng[1],tempTime);
//                    }else{
//                        if(counterTalk==0)
//                            talk.start("Try to get trustable GPS location");
//                        else if(counterTalk==200)
//                            counterTalk=-1;
//                        counterTalk++;
//                    }
//                }
//
//                if(informationList.size()==0 && !annFlag && preciseEnq) {
//                    talk.start("Destination is somewhere around you!");
//                    annFlag=true;
//                    reachedDestination=true;
//                }
//                if (reachedDestination){
//                    GPSTimer.cancel();
//                    GPSTimer.purge();
//                }
//            }
//        },10,100);

    }

    public void getResult(String description, int position, int NextToDo) {
        switch (NextToDo){
            case 1:
                searchingTime(new StringBuilder(description));
                break;
            case 2:
                if(!progressDialogFlag)
                    showProgressDialog();
                loop_number = getIndoorLocationsToGo().get(position).get("loop_number");
                indoorWayfindingFlag = true;
                break;
            case 3:
                if(!progressDialogFlag)
                    showProgressDialog();
                outdoorWayfinding(Double.valueOf(getOutdoorLocationsToGo().get(position).get("lat")), Double.valueOf(getOutdoorLocationsToGo().get(position).get("lng")));
                break;
            case 4:
                talk.start("Please confirm one of the following options!");
                if(description.equals("indoor")){
                    listViewToShow(getIndoorLocationsToGo(),2, 1);
                }else{
                    listViewToShow(getOutdoorLocationsToGo(),3, getOutdoorLocationsToGo().size());
                }
                break;
            case 12:
                Log.d(TAG, "indoorWayfinding: "+loop_number+", "+getRouteSrcDst()+", "+instructionListData.get(0).get(0).get("description")+", "+frgno+", "+USER_CATEGORY);
                if(USER_CATEGORY==1 && !EVACUATION_FLAG){
                    listViewToShow(instructionListData.get(0),12, 1);
                }else if(USER_CATEGORY==2 || USER_CATEGORY==3 || (USER_CATEGORY==1 && EVACUATION_FLAG)){
                    if(frgno != 2){
                        myFrame.removeAllViews();
                        fragmentSightedTransaction = fragmentManager.beginTransaction();
                        fragmentSightedTransaction.replace(R.id.frame, fragmentSighted);
                        fragmentSightedTransaction.commit();
                        fragmentSighted.xyToDraw(beaconNumber,xy,"",getRouteSrcDst());
                        frgno = 2;
                    }else{
                        fragmentSighted.xyToDraw(beaconNumber,xy,"",getRouteSrcDst());
                    }
                }
                break;
        }
    }

    private void listViewToShow(ArrayList<HashMap<String, String>> result, int nextStep, int noResultToShow) {

        Bundle bundle = new Bundle();
        ArrayList<String> myListExample = new ArrayList<>();
        for(int i=0;i<noResultToShow;i++)
            myListExample.add(result.get(i).get("description"));

        myListExample.add(String.valueOf(nextStep));

        bundle.putStringArrayList("dataFromMainActivity",myListExample);
        if(frgno == 1) {
            fragmentBlind = new BlindFragment();
            fragmentBlindTransaction = fragmentManager.beginTransaction();
            fragmentBlind.setArguments(bundle);
            fragmentBlindTransaction.replace(R.id.frame, fragmentBlind);
            fragmentBlindTransaction.commit();
        }else if(frgno != 1){
            myFrame.removeAllViews();
            fragmentBlindTransaction = fragmentManager.beginTransaction();
            fragmentBlind.setArguments(bundle);
            fragmentBlindTransaction.replace(R.id.frame, fragmentBlind);
            fragmentBlindTransaction.commit();
            frgno = 1;
        }
    }


    public void setIndoorLocationsToGo(ArrayList<HashMap<String, String>> indoorLocationsToGo) {        this.indoorLocationsToGo = indoorLocationsToGo;    }

    public ArrayList<HashMap<String, String>> getOutdoorLocationsToGo() {        return outdoorLocationsToGo;    }

    public void setOutdoorLocationsToGo(ArrayList<HashMap<String, String>> outdoorLocationsToGo) {        this.outdoorLocationsToGo = outdoorLocationsToGo;    }

    public ArrayList<HashMap<String, String>> getIndoorLocationsToGo() {        return indoorLocationsToGo;    }

    private int meterToFeet(int value) {        return (int) Math.round(value / 0.3048);    }

    public double[][][] getMapDetails() {        return mapDetails;    }

    public void setMapDetails(double[][][] mapDetails) {        this.mapDetails = mapDetails;    }

    public String getRouteSrcDst() {        return routeSrcDst;    }

    public void setRouteSrcDst(String routeSrcDst) {        this.routeSrcDst = routeSrcDst;    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {    }

    @Override
    public void downloadLocationsIndoors(String result) {    }
}