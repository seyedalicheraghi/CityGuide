package org.smartcityguide.cityguide;

import android.annotation.SuppressLint;
import android.util.Log;

class HttpsLinkMaker {
    @SuppressLint("LongLogTag")
    public String nearbyPlaces(String nearbyPlace, double [][] coordinates, double PROXIMITY_RADIUS, String google_maps_key){
        StringBuilder googlePlaceUrl = new StringBuilder("https://maps.googleapis.com/maps/api/");
        switch (nearbyPlace) {
            default:
                googlePlaceUrl.append("place/textsearch/json?");
                googlePlaceUrl.append("location=" + coordinates[0][0] + "," + coordinates[0][1]);
                googlePlaceUrl.append("&radius=" + PROXIMITY_RADIUS);
                googlePlaceUrl.append("&query=" + nearbyPlace);
                googlePlaceUrl.append("&key=" + google_maps_key+"D");
                Log.d("ProceduralProcess Default link maker", googlePlaceUrl.toString());
                break;
            case "atm":
                googlePlaceUrl.append("place/nearbysearch/json?");
                googlePlaceUrl.append("location=" + coordinates[0][0] + "," + coordinates[0][1]);
                googlePlaceUrl.append("&radius=" + PROXIMITY_RADIUS);
                googlePlaceUrl.append("&type=" + "atm");
                googlePlaceUrl.append("&sensor=true");
                googlePlaceUrl.append("&key=" + google_maps_key+"O");
                break;
            case "bar":
                googlePlaceUrl.append("place/nearbysearch/json?");
                googlePlaceUrl.append("location=" + coordinates[0][0] + "," + coordinates[0][1]);
                googlePlaceUrl.append("&radius=" + PROXIMITY_RADIUS);
                googlePlaceUrl.append("&type=" + "bar");
                googlePlaceUrl.append("&sensor=true");
                googlePlaceUrl.append("&key=" + google_maps_key+"O");
                break;
            case "bank":
                googlePlaceUrl.append("place/nearbysearch/json?");
                googlePlaceUrl.append("location=" + coordinates[0][0] + "," + coordinates[0][1]);
                googlePlaceUrl.append("&radius=" + PROXIMITY_RADIUS);
                googlePlaceUrl.append("&type=" + "bank");
                googlePlaceUrl.append("&sensor=true");
                googlePlaceUrl.append("&key=" + google_maps_key+"O");
                break;
            case "bus_station":
                googlePlaceUrl.append("place/nearbysearch/json?");
                googlePlaceUrl.append("location=" + coordinates[0][0] + "," + coordinates[0][1]);
                googlePlaceUrl.append("&radius=" + PROXIMITY_RADIUS);
                googlePlaceUrl.append("&type=" + "bus_station");
                googlePlaceUrl.append("&sensor=true");
                googlePlaceUrl.append("&key=" + google_maps_key+"O");
                break;
            case "cafe":
                googlePlaceUrl.append("place/nearbysearch/json?");
                googlePlaceUrl.append("location=" + coordinates[0][0] + "," + coordinates[0][1]);
                googlePlaceUrl.append("&radius=" + PROXIMITY_RADIUS);
                googlePlaceUrl.append("&type=" + "cafe");
                googlePlaceUrl.append("&sensor=true");
                googlePlaceUrl.append("&key=" + google_maps_key+"O");
                break;
            case "church":
                googlePlaceUrl.append("place/nearbysearch/json?");
                googlePlaceUrl.append("location=" + coordinates[0][0] + "," + coordinates[0][1]);
                googlePlaceUrl.append("&radius=" + PROXIMITY_RADIUS);
                googlePlaceUrl.append("&type=" + "church");
                googlePlaceUrl.append("&sensor=true");
                googlePlaceUrl.append("&key=" + google_maps_key+"O");
                break;
            case "department_store":
                googlePlaceUrl.append("place/nearbysearch/json?");
                googlePlaceUrl.append("location=" + coordinates[0][0] + "," + coordinates[0][1]);
                googlePlaceUrl.append("&radius=" + PROXIMITY_RADIUS);
                googlePlaceUrl.append("&type=" + "department_store");
                googlePlaceUrl.append("&sensor=true");
                googlePlaceUrl.append("&key=" + google_maps_key+"O");
                break;
            case "doctor":
                googlePlaceUrl.append("place/nearbysearch/json?");
                googlePlaceUrl.append("location=" + coordinates[0][0] + "," + coordinates[0][1]);
                googlePlaceUrl.append("&radius=" + PROXIMITY_RADIUS);
                googlePlaceUrl.append("&type=" + "doctor");
                googlePlaceUrl.append("&sensor=true");
                googlePlaceUrl.append("&key=" + google_maps_key+"O");
                break;
            case "gym":
                googlePlaceUrl.append("place/nearbysearch/json?");
                googlePlaceUrl.append("location=" + coordinates[0][0] + "," + coordinates[0][1]);
                googlePlaceUrl.append("&radius=" + PROXIMITY_RADIUS);
                googlePlaceUrl.append("&type=" + "gym");
                googlePlaceUrl.append("&sensor=true");
                googlePlaceUrl.append("&key=" + google_maps_key+"O");
                break;
            case "hospital":
                googlePlaceUrl.append("place/nearbysearch/json?");
                googlePlaceUrl.append("location=" + coordinates[0][0] + "," + coordinates[0][1]);
                googlePlaceUrl.append("&radius=" + PROXIMITY_RADIUS);
                googlePlaceUrl.append("&type=" + "hospital");
                googlePlaceUrl.append("&sensor=true");
                googlePlaceUrl.append("&key=" + google_maps_key+"O");
                break;
            case "liquor_store":
                googlePlaceUrl.append("place/nearbysearch/json?");
                googlePlaceUrl.append("location=" + coordinates[0][0] + "," + coordinates[0][1]);
                googlePlaceUrl.append("&radius=" + PROXIMITY_RADIUS);
                googlePlaceUrl.append("&type=" + "liquor_store");
                googlePlaceUrl.append("&sensor=true");
                googlePlaceUrl.append("&key=" + google_maps_key+"O");
                break;
            case "pharmacy":
                googlePlaceUrl.append("place/nearbysearch/json?");
                googlePlaceUrl.append("location=" + coordinates[0][0] + "," + coordinates[0][1]);
                googlePlaceUrl.append("&radius=" + PROXIMITY_RADIUS);
                googlePlaceUrl.append("&type=" + "pharmacy");
                googlePlaceUrl.append("&sensor=true");
                googlePlaceUrl.append("&key=" + google_maps_key+"O");
                break;
            case "police":
                googlePlaceUrl.append("place/nearbysearch/json?");
                googlePlaceUrl.append("location=" + coordinates[0][0] + "," + coordinates[0][1]);
                googlePlaceUrl.append("&radius=" + PROXIMITY_RADIUS);
                googlePlaceUrl.append("&type=" + "police");
                googlePlaceUrl.append("&sensor=true");
                googlePlaceUrl.append("&key=" + google_maps_key+"O");
                break;
            case "post_office":
                googlePlaceUrl.append("place/nearbysearch/json?");
                googlePlaceUrl.append("location=" + coordinates[0][0] + "," + coordinates[0][1]);
                googlePlaceUrl.append("&radius=" + PROXIMITY_RADIUS);
                googlePlaceUrl.append("&type=" + "post_office");
                googlePlaceUrl.append("&sensor=true");
                googlePlaceUrl.append("&key=" + google_maps_key+"O");
                break;
            case "restaurant":
                googlePlaceUrl.append("place/nearbysearch/json?");
                googlePlaceUrl.append("location=" + coordinates[0][0] + "," + coordinates[0][1]);
                googlePlaceUrl.append("&radius=" + PROXIMITY_RADIUS);
                googlePlaceUrl.append("&type=" + "restaurant");
                googlePlaceUrl.append("&sensor=true");
                googlePlaceUrl.append("&key=" + google_maps_key+"O");
                break;
            case "school":
                googlePlaceUrl.append("place/nearbysearch/json?");
                googlePlaceUrl.append("location=" + coordinates[0][0] + "," + coordinates[0][1]);
                googlePlaceUrl.append("&radius=" + PROXIMITY_RADIUS);
                googlePlaceUrl.append("&type=" + "school");
                googlePlaceUrl.append("&sensor=true");
                googlePlaceUrl.append("&key=" + google_maps_key+"O");
                break;
            case "shopping_mall":
                googlePlaceUrl.append("place/nearbysearch/json?");
                googlePlaceUrl.append("location=" + coordinates[0][0] + "," + coordinates[0][1]);
                googlePlaceUrl.append("&radius=" + PROXIMITY_RADIUS);
                googlePlaceUrl.append("&type=" + "shopping_mall");
                googlePlaceUrl.append("&sensor=true");
                googlePlaceUrl.append("&key=" + google_maps_key+"O");
                break;
            case "subway_station":
                googlePlaceUrl.append("place/nearbysearch/json?");
                googlePlaceUrl.append("location=" + coordinates[0][0] + "," + coordinates[0][1]);
                googlePlaceUrl.append("&radius=" + PROXIMITY_RADIUS);
                googlePlaceUrl.append("&type=" + "subway_station");
                googlePlaceUrl.append("&sensor=true");
                googlePlaceUrl.append("&key=" + google_maps_key+"O");
                break;
            case "university":
                googlePlaceUrl.append("place/nearbysearch/json?");
                googlePlaceUrl.append("location=" + coordinates[0][0] + "," + coordinates[0][1]);
                googlePlaceUrl.append("&radius=" + PROXIMITY_RADIUS);
                googlePlaceUrl.append("&type=" + "university");
                googlePlaceUrl.append("&sensor=true");
                googlePlaceUrl.append("&key=" + google_maps_key+"O");
                break;

            case "distanceMeasurement":
                googlePlaceUrl.append("directions/json?");
                googlePlaceUrl.append("origin="+coordinates[0][0] + "," + coordinates[0][1]);
                googlePlaceUrl.append("&destination="+coordinates[1][0] + "," + coordinates[1][1]);
                googlePlaceUrl.append("&mode=walking");
                googlePlaceUrl.append("&key=" + google_maps_key);
        }
        return googlePlaceUrl.toString();
    }
}