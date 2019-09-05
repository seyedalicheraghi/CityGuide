package org.smartcityguide.cityguide;

import java.util.ArrayList;
import java.util.HashMap;

public interface RequestResponse {

    void downloadLocationsIndoors(String result);

    void searchedFinish(ArrayList<HashMap<String, String>> result);

    void userSelection(String option);

//    void outdoorWayfiningActivity(String locationName, double lat, double lng);
//
//    void indoorWayfiningActivity(String buildingName, int beaconNumber);
}
