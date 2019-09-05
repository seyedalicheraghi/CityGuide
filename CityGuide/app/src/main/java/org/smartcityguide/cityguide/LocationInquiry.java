package org.smartcityguide.cityguide;


import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.google.maps.android.PolyUtil.decode;

public class LocationInquiry extends AsyncTask<String[], Void, ArrayList<HashMap<String,String> > > {



    private String TAG = "MyErrorDetector";


    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    @Override
    protected ArrayList<HashMap<String, String>> doInBackground(String[]... urlChoice) {
        HttpURLConnection conn;
        String[] result = urlChoice[0];
        // tmp hash map for single contact
        HashMap<String, String> information;
        ArrayList<HashMap<String,String>> informationList = new ArrayList<>();
        switch(result[0]) {
            case "locationInquiry":
                try {
                    String str = result[1];
                    char ch = str.charAt(str.length()-1);
                    str = str.substring(0,str.length()-1);
                    URL url = new URL(str.replaceAll("\\s+","_"));// Replace spaces with _
                    Log.d(TAG+"1", String.valueOf(url));
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    // read the response
                    InputStream in = new BufferedInputStream(conn.getInputStream());
                    String jsonStr = convertStreamToString(in);
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONObject jsonObject;
                    // Getting JSON Array node
                    JSONArray locInf = jsonObj.getJSONArray("results");

                    for (int i = 0; i < locInf.length(); i++) {
                        information = new HashMap<>();
                        jsonObject = locInf.getJSONObject(i).getJSONObject("geometry").getJSONObject("location");
                        if (ch == 'D') {
                            information.put("description", locInf.getJSONObject(i).getString("formatted_address"));
                        }else if (ch == 'O'){
                            information.put("description", locInf.getJSONObject(i).getString("name") + " at " + locInf.getJSONObject(i).getString("vicinity"));
                        }
                        information.put("lat", jsonObject.getString("lat"));
                        information.put("lng", jsonObject.getString("lng"));
                        informationList.add(information);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case "distanceInquiry":
                try {
                    URL url = new URL(result[1]);
                    Log.d(TAG+"2", String.valueOf(url));
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    // read the response
                    InputStream in = new BufferedInputStream(conn.getInputStream());
                    String jsonStr = convertStreamToString(in);
                    information = new HashMap<>();
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONObject jsonObject;
                    // Getting JSON Array node
                    JSONArray locInf = jsonObj.getJSONArray("routes").getJSONObject(0).getJSONArray("legs");
                    information.put("description", "It is "+locInf.getJSONObject(0).getJSONObject("distance").getString("text")
                            +" (around "+locInf.getJSONObject(0).getJSONObject("duration").getString("text")+")"
                            //+" from "+locInf.getJSONObject(0).getString("start_address")
                            +" to "+locInf.getJSONObject(0).getString("end_address"));
                    informationList.add(information);
                    JSONArray locInf1 = locInf.getJSONObject(0).getJSONArray("steps");

                    LatLng latLng;
                    double plat=0,plng=0;

                    for (int i = 0; i < locInf1.length(); i++) {

                        jsonObject = locInf1.getJSONObject(i);
                        List tempList = decode(jsonObject.getJSONObject("polyline").getString("points"));

                        for(int kk=0;kk<tempList.size();kk++) {

                            latLng = (LatLng) tempList.get(kk);
                            if(latLng.latitude!=plat || latLng.longitude!=plng) {
                                information = new HashMap<>();
                                information.put("lat", String.valueOf(roundDecimalsToDecNum(latLng.latitude, 6)));
                                information.put("lng", String.valueOf(roundDecimalsToDecNum(latLng.longitude, 6)));
                                informationList.add(information);
                                plat=latLng.latitude;
                                plng=latLng.longitude;
                            }
                        }
                    }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;
        }

        return informationList;
    }
    String directionGenerator(double mAzimuth){

        if (mAzimuth <= 23 || mAzimuth > 338)
            return  "North";
        else if (mAzimuth <= 338 && mAzimuth > 293)
            return  "North West";
        else if (mAzimuth <= 293 && mAzimuth > 248)
            return  "West";
        else if (mAzimuth <= 248 && mAzimuth > 203)
            return  "South West";
        else if (mAzimuth <= 203 && mAzimuth > 158)
            return  "South";
        else if (mAzimuth <= 158 && mAzimuth > 113)
            return  "South East";
        else if (mAzimuth <= 113 && mAzimuth > 68)
            return "East";
        else if (mAzimuth <= 68 && mAzimuth > 23)
            return "North East";
        else
            return "";
    }

    double roundDecimalsToDecNum(double d, int decNum){
        StringBuilder str = new StringBuilder("#.");
        for(int i=0;i<decNum;i++)
            str.append("#");

        DecimalFormat twoDForm = new DecimalFormat(str.toString());
        return Double.valueOf(twoDForm.format(d));
    }
}
