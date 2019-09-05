package org.smartcityguide.cityguide;

public interface BLEResponse {

    void beaconSighting(StringBuilder beaconID, StringBuilder beaconNameSpace, int beaconRSSI);

}
