package org.smartcityguide.cityguide;


import android.util.Log;

public class AudioGuidance {



    public String guidance(int[][] door,boolean reachFlag,int next,int degree,StringBuilder beaconName,boolean firstTimeFlag,int currentBeaconNumber) {

        int direction=0;
        int options;

        for(int i=4;i<19;i=i+2){
            if(door [currentBeaconNumber][i]==next) {
                direction = i;
            }
        }
        switch (direction){
            case 4:
                options=1;
                break;
            case 6:
                options=2;
                break;
            case 8:
                options=4;
                break;
            case 10:
                options=3;
                break;
            case 12:
                options=1;
                break;
            case 14:
                options=2;
                break;
            case 16:
                options=4;
                break;
            case 18:
                options=3;
                break;
                default:
                    options=direction;
                    break;
        }

        String pD = "";


//        if (reachFlag) {
//            options=door[destination][2];
//        }else{
//            if (String.valueOf(path[beaconNumber][next][1]).length() > 3) {
//                options = 0;
//            }else{
//                options = Integer.parseInt(String.valueOf(String.valueOf(path[beaconNumber][next][1]).charAt(0)));
//            }
//        }
        switch (options) {
            case 1:
                if ((degree >= 0 && degree <= 30) || (degree >= 330 && degree <= 360)) {//..........North
                    if (reachFlag)
                        pD = "You are in destination proximity. Destination should be somewhere in front of you.";
                    else
                        pD = "go straight";

                }else if ((degree > 30 && degree < 60)) {//..........................................North East
                    if (reachFlag)
                        pD = "You are in destination proximity. Destination should be somewhere in your left.";
                    else
                        pD = "turn a little left and then go straight";

                }else if ((degree >= 60 && degree <= 120)) {//.......................................East
                    if (reachFlag)
                        pD = "You are in destination proximity. Destination should be somewhere in your left.";
                    else
                        pD = "turn left and then go straight";

                }else if ((degree > 120 && degree <= 150)) {//.......................................South East
                    if (reachFlag)
                        pD = "You are in destination proximity. Destination should be somewhere in your left.";
                    else
                        pD = "turn left and then go straight";

                }else if ((degree > 150 && degree < 210)) {//........................................South
                    if (reachFlag)
                        pD = "You are in destination proximity. Destination should be somewhere behind you.";
                    else
                        pD = "turn around and then go straight";

                }else if ((degree > 210 && degree < 240)) {//........................................South West
                    if (reachFlag)
                        pD = "You are in destination proximity. Destination should be somewhere in your right.";
                    else
                        pD = "turn right and then go straight";

                }else if ((degree >= 240 && degree <= 300)) {//......................................West
                    if (reachFlag)
                        pD = "You are in destination proximity. Destination should be somewhere in your right.";
                    else
                        pD = "turn right and then go straight";

                }else if ((degree > 300 && degree < 330)) {//........................................North West
                    if (reachFlag)
                        pD = "You are in destination proximity. Destination should be somewhere in your right.";
                    else
                        pD = "turn a little right and then go straight";

                }
                break;
            case 2:
                if ((degree >= 0 && degree <= 30) || (degree >= 330 && degree <= 360)) {//..........North
                    if (reachFlag)
                        pD = "You are in destination proximity. Destination should be somewhere behind you.";
                    else
                        pD = "turn around and then go straight";

                }else if ((degree > 30 && degree < 60)) {//..........................................North East
                    if (reachFlag)
                        pD = "You are in destination proximity. Destination should be somewhere in your right.";
                    else
                        pD = "turn right and then go straight";

                }else if ((degree >= 60 && degree <= 120)) {//.......................................East
                    if (reachFlag)
                        pD = "You are in destination proximity. Destination should be somewhere in your right.";
                    else
                        pD = "turn Right and then go straight";

                }else if ((degree > 120 && degree <= 150)) {//.......................................South East
                    if (reachFlag)
                        pD = "You are in destination proximity. Destination should be somewhere in your right.";
                    else
                        pD = "turn a little right and then go straight";

                }else if ((degree > 150 && degree < 210)) {//........................................South
                    if (reachFlag)
                        pD = "You are in destination proximity. Destination should be somewhere in front of you.";
                    else
                        pD = "go straight";

                }else if ((degree > 210 && degree < 240)) {//........................................South West
                    if (reachFlag)
                        pD = "You are in destination proximity. Destination should be somewhere in your left.";
                    else
                        pD = "turn a little left and then go straight";

                }else if ((degree >= 240 && degree <= 300)) {//......................................West
                    if (reachFlag)
                        pD = "You are in destination proximity. Destination should be somewhere in your left.";
                    else
                        pD = "turn left and then go straight";

                }else if ((degree > 300 && degree < 330)) {//........................................North West
                    if (reachFlag)
                        pD = "You are in destination proximity. Destination should be somewhere in your left.";
                    else
                        pD = "turn left and then go straight";

                }
                break;
            case 3:
                if ((degree >= 0 && degree <= 30) || (degree >= 330 && degree <= 360)) {//..........North
                    if (reachFlag)
                        pD = "You are in destination proximity. Destination should be somewhere in your right.";
                    else
                        pD = "turn Right and then go straight";

                }else if ((degree > 30 && degree < 60)) {//..........................................North East
                    if (reachFlag)
                        pD = "You are in destination proximity. Destination should be somewhere in your right.";
                    else
                        pD = "turn a little Right and then go straight";

                }else if ((degree >= 60 && degree <= 120)) {//.......................................East
                    if (reachFlag)
                        pD = "You are in destination proximity. Destination should be somewhere in front of you.";
                    else
                        pD = "go straight";

                }else if ((degree > 120 && degree <= 150)) {//.......................................South East
                    if (reachFlag)
                        pD = "You are in destination proximity. Destination should be somewhere in your left.";
                    else
                        pD = "turn a little left and then go straight";

                }else if ((degree > 150 && degree < 210)) {//........................................South
                    if (reachFlag)
                        pD = "You are in destination proximity. Destination should be somewhere in your left.";
                    else
                        pD = "turn left and then go straight";

                }else if ((degree > 210 && degree < 240)) {//........................................South West
                    if (reachFlag)
                        pD = "You are in destination proximity. Destination should be somewhere in your left.";
                    else
                        pD = "turn left and then go straight";

                }else if ((degree >= 240 && degree <= 300)) {//......................................West
                    if (reachFlag)
                        pD = "You are in destination proximity. Destination should be somewhere behind you.";
                    else
                        pD = "turn around and then go straight";

                }else if ((degree > 300 && degree < 330)) {//........................................North West
                    if (reachFlag)
                        pD = "You are in destination proximity. Destination should be somewhere in your right.";
                    else
                        pD = "turn Right and then go straight";

                }
                break;
            case 4:
                if ((degree >= 0 && degree <= 30) || (degree >= 330 && degree <= 360)) {//..........North
                    if (reachFlag)
                        pD = "You are in destination proximity. Destination should be somewhere in your left.";
                    else
                        pD = "turn left and then go straight";

                }else if ((degree > 30 && degree < 60)) {//..........................................North East
                    if (reachFlag)
                        pD = "You are in destination proximity. Destination should be somewhere in your left.";
                    else
                        pD = "turn left and then go straight";

                }else if ((degree >= 60 && degree <= 120)) {//.......................................East
                    if (reachFlag)
                        pD = "You are in destination proximity. Destination should be somewhere behind you.";
                    else
                        pD = "turn around and then go straight";

                }else if ((degree > 120 && degree <= 150)) {//.......................................South East
                    if (reachFlag)
                        pD = "You are in destination proximity. Destination should be somewhere in your right.";
                    else
                        pD = "turn Right and then go straight";

                }else if ((degree > 150 && degree < 210)) {//........................................South
                    if (reachFlag)
                        pD = "You are in destination proximity. Destination should be somewhere in your right.";
                    else
                        pD = "turn Right and then go straight";

                }else if ((degree > 210 && degree < 240)) {//........................................South West
                    if (reachFlag)
                        pD = "You are in destination proximity. Destination should be somewhere in your right.";
                    else
                        pD = "turn a little right and then go straight";

                }else if ((degree >= 240 && degree <= 300)) {//......................................West
                    if (reachFlag)
                        pD = "You are in destination proximity. Destination should be somewhere in front of you.";
                    else
                        pD = "go straight";

                }else if ((degree > 300 && degree < 330)) {//........................................North West
                    if (reachFlag)
                        pD = "You are in destination proximity. Destination should be somewhere in front of you.";
                    else
                        pD = "turn a little left and then go straight";

                }
                break;
            case 0:
//                switch (Integer.parseInt(String.valueOf(String.valueOf(path[beaconNumber][next][1]).charAt(0)))){
//                    case 1:
//                }
//                pD = " use the stairs to go from floor " +
//                        Integer.parseInt(String.valueOf(String.valueOf(path[beaconNumber][next][1]).charAt(0)))  + " to floor " +
//                        Integer.parseInt(String.valueOf(String.valueOf(path[beaconNumber][next][1]).charAt(1)));
                Log.d("emad",pD);

                break;

        }

        if (!reachFlag) {
            if(firstTimeFlag)
                return  "You are approaching " + beaconName.toString() + ". You should " + pD + "%p%" + options;
            else
                return  "You are at " + beaconName.toString() + ". You should " + pD + "%p%" + options;
        } else {
            return  pD + "%p%" + options;
        }
    }
}
