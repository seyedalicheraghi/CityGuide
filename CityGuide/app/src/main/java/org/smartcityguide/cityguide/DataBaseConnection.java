package org.smartcityguide.cityguide;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class DataBaseConnection extends AsyncTask<String, Void, String> {

    private String typeOfAction = "";
    private File subFolder;

    @SuppressLint("StaticFieldLeak")
    private
    Context ctx;
    private AsyncResponse delegate = null;
    DataBaseConnection(Context ctx){
        this.ctx=ctx;
        subFolder = new File(ctx.getApplicationContext().getFilesDir().getAbsolutePath() + File.separator + "BeaconData");
    }

    @Override
    protected String doInBackground(String... params) {

        typeOfAction = params[0];
        String username = params[1];
        String password = params[2];
        String namespace = params[3];
        String locationName  = params[4];


        switch (typeOfAction){
            case "login":
                try {//"http://smartcityguide.org/mysql/login.php"
                    URL url = new URL("https://smartcitygude.000webhostapp.com/login.php");

                    String post_data = URLEncoder.encode("username", "UTF-8")+"="
                            + URLEncoder.encode(username.trim(), "UTF-8") + "&"
                            + URLEncoder.encode("password", "UTF-8")+"="+URLEncoder.encode(password.trim(), "UTF-8");

                    return serverConnector(url,post_data);

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "beacons":
                try {//"http://smartcityguide.org/mysql/beacons.php"
                    URL url = new URL("https://smartcitygude.000webhostapp.com/beacons.php");

                    String post_data = URLEncoder.encode("username", "UTF-8")+"=" + URLEncoder.encode(username.trim(), "UTF-8") + "&"
                            + URLEncoder.encode("password", "UTF-8")+"="+URLEncoder.encode(password.trim(), "UTF-8")+"&"
                            + URLEncoder.encode("namespace", "UTF-8")+"="+URLEncoder.encode(namespace.trim(), "UTF-8");

                    return serverConnector(url,post_data);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "buildingInquiry":
//                Log.d("MyErrorDetector", "jsonConverter: *******"+locationName);
                try {//"http://smartcityguide.org/mysql/location.php"
                    URL url = new URL("https://smartcitygude.000webhostapp.com/location.php");
                    String post_data = URLEncoder.encode("username", "UTF-8")+"=" + URLEncoder.encode(username, "UTF-8") + "&"
                            + URLEncoder.encode("password", "UTF-8")+"="+URLEncoder.encode(password, "UTF-8")+"&"
                            + URLEncoder.encode("locationName", "UTF-8")+"="+URLEncoder.encode(locationName, "UTF-8");
                    return serverConnector(url,post_data);

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "outdoorInquiry":

                try {//"http://smartcityguide.org/mysql/outdoor.php"
                    URL url = new URL("https://smartcitygude.000webhostapp.com/outdoor.php");
//                    Log.d("MyErrorDetector", "doInBackground: "+username+", "+password+", "+locationName);

                    String post_data = URLEncoder.encode("username", "UTF-8")+"=" + URLEncoder.encode(username.trim(), "UTF-8") + "&"
                            + URLEncoder.encode("password", "UTF-8")+"="+URLEncoder.encode(password.trim(), "UTF-8")+"&"
                            + URLEncoder.encode("locationName", "UTF-8")+"="+URLEncoder.encode(locationName.trim(), "UTF-8");

                    return serverConnector(url,post_data);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }

        return null;
    }

    private String serverConnector(URL url,String post_data){
            try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));

            bufferedWriter.write(post_data);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
            StringBuilder result = new StringBuilder();
            String line;
            while((line = bufferedReader.readLine())!=null){
                result.append(line);
            }
            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();
            return result.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        delegate = (AsyncResponse) ctx;
    }

    @Override
    protected void onPostExecute(String result) {

//        Log.d("MyErrorDetector", "onPostExecute: "+result);
        if (result!=null)
            delegate.serverInquiry(jsonConverter(result));
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    private ArrayList<HashMap<String,String>> jsonConverter(String jsonStr) {
//        Log.d("MyErrorDetector", "jsonConverter: sa"+jsonStr+typeOfAction);
        ArrayList<HashMap<String,String>> informationList = new ArrayList<>();
        HashMap<String, String> information;
        try {


            JSONArray jsonArray = new JSONArray(jsonStr);

            switch(typeOfAction){
                case "buildingInquiry":
                    for(int i = 0; i < jsonArray.length(); i++)
                    {

                        information = new HashMap<>();
                        information.put("inquiryMode", "buildingInquiry");
                        information.put("id", String.valueOf(jsonArray.getJSONObject(i).getInt("id")));
                        information.put("node", String.valueOf(jsonArray.getJSONObject(i).getInt("node")));
                        information.put("numsens", String.valueOf(jsonArray.getJSONObject(i).getInt("numsens")));
                        information.put("locname", String.valueOf(jsonArray.getJSONObject(i).getString("locname")));
                        information.put("threshold", String.valueOf(jsonArray.getJSONObject(i).getInt("threshold")));
                        information.put("direction", String.valueOf(jsonArray.getJSONObject(i).getInt("direction")));
                        information.put("other", String.valueOf(jsonArray.getJSONObject(i).getString("other")));
                        information.put("level", String.valueOf(jsonArray.getJSONObject(i).getInt("level")));
                        information.put("bnorth", String.valueOf(jsonArray.getJSONObject(i).getInt("bnorth")));
                        information.put("ndist", String.valueOf(jsonArray.getJSONObject(i).getInt("ndist")));
                        information.put("bsouth", String.valueOf(jsonArray.getJSONObject(i).getInt("bsouth")));
                        information.put("sdist", String.valueOf(jsonArray.getJSONObject(i).getInt("sdist")));
                        information.put("beast", String.valueOf(jsonArray.getJSONObject(i).getInt("beast")));
                        information.put("edist", String.valueOf(jsonArray.getJSONObject(i).getInt("edist")));
                        information.put("bwest", String.valueOf(jsonArray.getJSONObject(i).getInt("bwest")));
                        information.put("wdist", String.valueOf(jsonArray.getJSONObject(i).getInt("wdist")));
                        information.put("bneast", String.valueOf(jsonArray.getJSONObject(i).getInt("bneast")));
                        information.put("neastdist", String.valueOf(jsonArray.getJSONObject(i).getInt("neastdist")));
                        information.put("bnwest", String.valueOf(jsonArray.getJSONObject(i).getInt("bnwest")));
                        information.put("nwestdist", String.valueOf(jsonArray.getJSONObject(i).getInt("nwestdist")));
                        information.put("bseast", String.valueOf(jsonArray.getJSONObject(i).getInt("bseast")));
                        information.put("seastdist", String.valueOf(jsonArray.getJSONObject(i).getInt("seastdist")));
                        information.put("bswest", String.valueOf(jsonArray.getJSONObject(i).getInt("bswest")));
                        information.put("swestdist", String.valueOf(jsonArray.getJSONObject(i).getInt("swestdist")));
                        information.put("emgmode", String.valueOf(jsonArray.getJSONObject(i).getInt("emgmode")));
                        information.put("emgtype", String.valueOf(jsonArray.getJSONObject(i).getInt("emgtype")));
                        information.put("safenode", String.valueOf(jsonArray.getJSONObject(i).getInt("safenode")));

                        informationList.add(information);

                    }
                break;
                case "beacons":

                    // To read and write in a file the following lines must be followed
                    StringBuilder stringBuilder;
                    FileOutputStream outputStream;
                    File file = new File(subFolder, "beacons");

                    if (!subFolder.exists())
                        subFolder.mkdirs();

                    if(!file.exists())
                        file.createNewFile();

                    outputStream = new FileOutputStream(file);

                    SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                    String formattedDate = df.format(Calendar.getInstance().getTime());
                    stringBuilder = new StringBuilder(String.valueOf(0)+", "+formattedDate);
                    outputStream.write(stringBuilder.toString().getBytes());
                    outputStream.write('\n');

                    for(int i = 0; i < jsonArray.length(); i++)
                    {
                        stringBuilder = new StringBuilder(String.valueOf(jsonArray.getJSONObject(i).getInt("id"))+", "+jsonArray.getJSONObject(i).getString("location"));
                        // write in file that was already created;
                        try {
                            outputStream.write(stringBuilder.toString().getBytes());
                            outputStream.write('\n');
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                    outputStream.close();
                    break;
                case "login":
                    for(int i = 0; i < jsonArray.length(); i++)
                    {
                        information = new HashMap<>();
                        information.put("inquiryMode", "login");
                        information.put("id", String.valueOf(jsonArray.getJSONObject(i).getInt("id")));
                        information.put("name", jsonArray.getJSONObject(i).getString("name"));
                        informationList.add(information);
                    }
                    break;
                case "outdoorInquiry":

                    for(int i = 0; i < jsonArray.length(); i++)
                    {
//                        Log.d("MyErrorDetector", "jsonConverter: "+String.valueOf(jsonArray.getJSONObject(i).getInt("id"))+", "+String.valueOf(jsonArray.getJSONObject(i).getDouble("latitude"))+
//                        ", "+String.valueOf(jsonArray.getJSONObject(i).getDouble("longitude"))+", "+String.valueOf(jsonArray.getJSONObject(i).getString("other"))+", "+
//                                String.valueOf(jsonArray.getJSONObject(i).getInt("direction")));
                        information = new HashMap<>();
                        information.put("inquiryMode", "outdoorInquiry");
                        information.put("id", String.valueOf(jsonArray.getJSONObject(i).getInt("id")));
                        information.put("latitude", String.valueOf(jsonArray.getJSONObject(i).getDouble("latitude")));
                        information.put("longitude", String.valueOf(jsonArray.getJSONObject(i).getDouble("longitude")));
                        information.put("other", String.valueOf(jsonArray.getJSONObject(i).getString("other")));
                        information.put("direction", String.valueOf(jsonArray.getJSONObject(i).getInt("direction")));

                        informationList.add(information);
                    }

                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

//        for(int i=0;i<informationList.size();i++)
//            Log.d("MyErrorDetector", "jsonConverter: "+informationList.get(i));
        return informationList;

    }


}
