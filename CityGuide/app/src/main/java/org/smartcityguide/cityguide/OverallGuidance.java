package org.smartcityguide.cityguide;

import android.util.Log;

class OverallGuidance {

    StringBuilder[] directionGuide(int options, float degree, boolean flag, int DIRECTION_OPTION) {

        StringBuilder[] data = new StringBuilder[2];
        
        switch (options) {
            //---------------------------------------North---------------------------------------
            case 1:
                if (DIRECTION_OPTION == 1) {
                    if ((degree >= 0 && degree <= 45) || (degree > 315 && degree <= 360)) { //..........North
                        data[0] = straight(flag);
                    } else if ((degree > 45 && degree <= 135)) {//.......................................East
                        data[0] = leftStraight(flag);
                    } else if ((degree > 135 && degree <= 225)) {//........................................South
                        data[0] = aroundStraight(flag);
                    } else if ((degree > 225 && degree <= 315)) {//........................................West
                        data[0] = rightStraight(flag);
                    }
                } else if (DIRECTION_OPTION == 2) {
                    if ((degree >= 0 && degree <= 15) || (degree > 345 && degree <= 360)) {//..........12
                        data[0] = twelveClock(flag);
                    } else if ((degree > 15 && degree <= 45)) {//.......................................1
                        data[0] = elevenClock(flag);
                    } else if ((degree > 45 && degree <= 75)) {//........................................2
                        data[0] = tenClock(flag);
                    } else if ((degree > 75 && degree <= 105)) {//........................................3
                        data[0] = nineClock(flag);
                    } else if ((degree > 105 && degree <= 135)) {//........................................4
                        data[0] = eightClock(flag);
                    } else if ((degree > 135 && degree <= 165)) {//........................................5
                        data[0] = sevenClock(flag);
                    } else if ((degree > 165 && degree <= 195)) {//........................................6
                        data[0] = sixClock(flag);
                    } else if ((degree > 195 && degree <= 225)) {//........................................7
                        data[0] = fiveClock(flag);
                    } else if ((degree > 225 && degree <= 255)) {//........................................8
                        data[0] = fourClock(flag);
                    } else if ((degree > 255 && degree <= 285)) {//........................................9
                        data[0] = threeClock(flag);
                    } else if ((degree > 285 && degree <= 315)) {//........................................10
                        data[0] = twoClock(flag);
                    } else if ((degree > 315 && degree <= 345)) {//........................................11
                        data[0] = oneClock(flag);
                    }
                }
                data[1] =  new StringBuilder("0");
                break;
            //---------------------------------------South---------------------------------------
            case 2:
                if (DIRECTION_OPTION == 1) {
                    if ((degree >= 0 && degree <= 45) || (degree > 315 && degree <= 360)) {//..........North
                        data[0] = aroundStraight(flag);
                    } else if ((degree > 45 && degree <= 135)) {//.......................................East
                        data[0] = rightStraight(flag);
                    } else if ((degree > 135 && degree <= 225)) {//........................................South
                        data[0] = straight(flag);
                    } else if ((degree > 225 && degree <= 315)) {//........................................West
                        data[0] = leftStraight(flag);
                    }
                } else if (DIRECTION_OPTION == 2) {
                    if ((degree >= 0 && degree <= 15) || (degree > 345 && degree <= 360)) {//..........12
                        data[0] = sixClock(flag);
                    } else if ((degree > 15 && degree <= 45)) {//.......................................1
                        data[0] = fiveClock(flag);
                    } else if ((degree > 45 && degree <= 75)) {//........................................2
                        data[0] = fourClock(flag);
                    } else if ((degree > 75 && degree <= 105)) {//........................................3
                        data[0] = threeClock(flag);
                    } else if ((degree > 105 && degree <= 135)) {//........................................4
                        data[0] = twoClock(flag);
                    } else if ((degree > 135 && degree <= 165)) {//........................................5
                        data[0] = oneClock(flag);
                    } else if ((degree > 165 && degree <= 195)) {//........................................6
                        data[0] = twelveClock(flag);
                    } else if ((degree > 195 && degree <= 225)) {//........................................7
                        data[0] = elevenClock(flag);
                    } else if ((degree > 225 && degree <= 255)) {//........................................8
                        data[0] = tenClock(flag);
                    } else if ((degree > 255 && degree <= 285)) {//........................................9
                        data[0] = nineClock(flag);
                    } else if ((degree > 285 && degree <= 315)) {//........................................10
                        data[0] = eightClock(flag);
                    } else if ((degree > 315 && degree <= 345)) {//........................................11
                        data[0] = sevenClock(flag);
                    }
                }
                data[1] =  new StringBuilder("180");
                break;
            //---------------------------------------East---------------------------------------
            case 3:
                if (DIRECTION_OPTION == 1) {
                    if ((degree >= 0 && degree <= 45) || (degree > 315 && degree <= 360)) {//..........North
                        data[0] = rightStraight(flag);
                    } else if ((degree > 45 && degree <= 135)) {//.......................................East
                        data[0] = straight(flag);
                    } else if ((degree > 135 && degree <= 225)) {//........................................South
                        data[0] = leftStraight(flag);
                    } else if ((degree > 225 && degree <= 315)) {//........................................West
                        data[0] = aroundStraight(flag);
                    }
                } else if (DIRECTION_OPTION == 2) {
                    if ((degree >= 0 && degree <= 15) || (degree > 345 && degree <= 360)) {//..........12
                        data[0] = threeClock(flag);
                    } else if ((degree > 15 && degree <= 45)) {//.......................................1
                        data[0] = twoClock(flag);
                    } else if ((degree > 45 && degree <= 75)) {//........................................2
                        data[0] = oneClock(flag);
                    } else if ((degree > 75 && degree <= 105)) {//........................................3
                        data[0] = twelveClock(flag);
                    } else if ((degree > 105 && degree <= 135)) {//........................................4
                        data[0] = elevenClock(flag);
                    } else if ((degree > 135 && degree <= 165)) {//........................................5
                        data[0] = tenClock(flag);
                    } else if ((degree > 165 && degree <= 195)) {//........................................6
                        data[0] = nineClock(flag);
                    } else if ((degree > 195 && degree <= 225)) {//........................................7
                        data[0] = eightClock(flag);
                    } else if ((degree > 225 && degree <= 255)) {//........................................8
                        data[0] = sevenClock(flag);
                    } else if ((degree > 255 && degree <= 285)) {//........................................9
                        data[0] = sixClock(flag);
                    } else if ((degree > 285 && degree <= 315)) {//........................................10
                        data[0] = fiveClock(flag);
                    } else if ((degree > 315 && degree <= 345)) {//........................................11
                        data[0] = fourClock(flag);
                    }
                }
                data[1] =  new StringBuilder("90");
                break;
            //---------------------------------------West---------------------------------------
            case 4:
                if (DIRECTION_OPTION == 1) {
                    if ((degree >= 0 && degree <= 45) || (degree > 315 && degree <= 360)) {//..........North
                        data[0] = leftStraight(flag);
                    } else if ((degree > 45 && degree <= 135)) {//.......................................East
                        data[0] = aroundStraight(flag);
                    } else if ((degree > 135 && degree <= 225)) {//........................................South
                        data[0] = rightStraight(flag);
                    } else if ((degree > 225 && degree <= 315)) {//........................................West
                        data[0] = straight(flag);
                    }
                } else if (DIRECTION_OPTION == 2) {
                    if ((degree >= 0 && degree <= 15) || (degree > 345 && degree <= 360)) {//..........12
                        data[0] = nineClock(flag);
                    } else if ((degree > 15 && degree <= 45)) {//.......................................1
                        data[0] = eightClock(flag);
                    } else if ((degree > 45 && degree <= 75)) {//........................................2
                        data[0] = sevenClock(flag);
                    } else if ((degree > 75 && degree <= 105)) {//........................................3
                        data[0] = sixClock(flag);
                    } else if ((degree > 105 && degree <= 135)) {//........................................4
                        data[0] = fiveClock(flag);
                    } else if ((degree > 135 && degree <= 165)) {//........................................5
                        data[0] = fourClock(flag);
                    } else if ((degree > 165 && degree <= 195)) {//........................................6
                        data[0] = threeClock(flag);
                    } else if ((degree > 195 && degree <= 225)) {//........................................7
                        data[0] = twoClock(flag);
                    } else if ((degree > 225 && degree <= 255)) {//........................................8
                        data[0] = oneClock(flag);
                    } else if ((degree > 255 && degree <= 285)) {//........................................9
                        data[0] = twelveClock(flag);
                    } else if ((degree > 285 && degree <= 315)) {//........................................10
                        data[0] = elevenClock(flag);
                    } else if ((degree > 315 && degree <= 345)) {//........................................11
                        data[0] = tenClock(flag);
                    }
                }
                data[1] =  new StringBuilder("270");
                break;
            //---------------------------------------North East---------------------------------------
            case 5:
                if (DIRECTION_OPTION == 1) {
                    if ((degree >= 0 && degree <= 45) || (degree > 315 && degree <= 360)) {//..........North
                        data[0] = turnALittleRightStraight(flag);
                    } else if ((degree > 45 && degree <= 135)) {//.......................................East
                        data[0] = turnALittleLeftStraight(flag);
                    } else if ((degree > 135 && degree <= 225)) {//........................................South
                        data[0] = aroundStraight(flag);
                    } else if ((degree > 225 && degree <= 315)) {//........................................West
                        data[0] = aroundStraight(flag);
                    }
                } else if (DIRECTION_OPTION == 2) {
                    if ((degree >= 0 && degree <= 15) || (degree > 345 && degree <= 360)) {//..........12
                        data[0] = twoClock(flag);
                    } else if ((degree > 15 && degree <= 45)) {//.......................................1
                        data[0] = twelveClock(flag);
                    } else if ((degree > 45 && degree <= 75)) {//........................................2
                        data[0] = twelveClock(flag);
                    } else if ((degree > 75 && degree <= 105)) {//........................................3
                        data[0] = tenClock(flag);
                    } else if ((degree > 105 && degree <= 135)) {//........................................4
                        data[0] = nineClock(flag);
                    } else if ((degree > 135 && degree <= 165)) {//........................................5
                        data[0] = eightClock(flag);
                    } else if ((degree > 165 && degree <= 195)) {//........................................6
                        data[0] = sevenClock(flag);
                    } else if ((degree > 195 && degree <= 225)) {//........................................7
                        data[0] = sixClock(flag);
                    } else if ((degree > 225 && degree <= 255)) {//........................................8
                        data[0] = fiveClock(flag);
                    } else if ((degree > 255 && degree <= 285)) {//........................................9
                        data[0] = fourClock(flag);
                    } else if ((degree > 285 && degree <= 315)) {//........................................10
                        data[0] = threeClock(flag);
                    } else if ((degree > 315 && degree <= 345)) {//........................................11
                        data[0] = twoClock(flag);
                    }
                }
                data[1] =  new StringBuilder("45");
                break;
            //---------------------------------------North West---------------------------------------
            case 6:
                if (DIRECTION_OPTION == 1) {
                    if ((degree >= 0 && degree <= 45) || (degree > 315 && degree <= 360)) {//..........North
                        data[0] = turnALittleLeftStraight(flag);
                    } else if ((degree > 45 && degree <= 135)) {//.......................................East
                        data[0] = aroundStraight(flag);
                    } else if ((degree > 135 && degree <= 225)) {//........................................South
                        data[0] = aroundStraight(flag);
                    } else if ((degree > 225 && degree <= 315)) {//........................................West
                        data[0] = turnALittleRightStraight(flag);
                    }
                } else if (DIRECTION_OPTION == 2) {
                    if ((degree >= 0 && degree <= 15) || (degree > 345 && degree <= 360)) {//..........12
                        data[0] = tenClock(flag);
                    } else if ((degree > 15 && degree <= 45)) {//.......................................1
                        data[0] = nineClock(flag);
                    } else if ((degree > 45 && degree <= 75)) {//........................................2
                        data[0] = eightClock(flag);
                    } else if ((degree > 75 && degree <= 105)) {//........................................3
                        data[0] = sevenClock(flag);
                    } else if ((degree > 105 && degree <= 135)) {//........................................4
                        data[0] = sixClock(flag);
                    } else if ((degree > 135 && degree <= 165)) {//........................................5
                        data[0] = fiveClock(flag);
                    } else if ((degree > 165 && degree <= 195)) {//........................................6
                        data[0] = fourClock(flag);
                    } else if ((degree > 195 && degree <= 225)) {//........................................7
                        data[0] = threeClock(flag);
                    } else if ((degree > 225 && degree <= 255)) {//........................................8
                        data[0] = twoClock(flag);
                    } else if ((degree > 255 && degree <= 285)) {//........................................9
                        data[0] = oneClock(flag);
                    } else if ((degree > 285 && degree <= 315)) {//........................................10
                        data[0] = twelveClock(flag);
                    } else if ((degree > 315 && degree <= 345)) {//........................................11
                        data[0] = twelveClock(flag);
                    }
                }
                data[1] =  new StringBuilder("315");
                break;
            //---------------------------------------South East---------------------------------------
            case 7:
                if (DIRECTION_OPTION == 1) {
                    if ((degree >= 0 && degree <= 45) || (degree > 315 && degree <= 360)) {//..........North
                        data[0] = aroundStraight(flag);
                    } else if ((degree > 45 && degree <= 135)) {//.......................................East
                        data[0] = turnALittleRightStraight(flag);
                    } else if ((degree > 135 && degree <= 225)) {//........................................South
                        data[0] = turnALittleLeftStraight(flag);
                    } else if ((degree > 225 && degree <= 315)) {//........................................West
                        data[0] = aroundStraight(flag);
                    }
                } else if (DIRECTION_OPTION == 2) {
                    if ((degree >= 0 && degree <= 15) || (degree > 345 && degree <= 360)) {//..........12
                        data[0] = fiveClock(flag);
                    } else if ((degree > 15 && degree <= 45)) {//.......................................1
                        data[0] = fourClock(flag);
                    } else if ((degree > 45 && degree <= 75)) {//........................................2
                        data[0] = threeClock(flag);
                    } else if ((degree > 75 && degree <= 105)) {//........................................3
                        data[0] = twoClock(flag);
                    } else if ((degree > 105 && degree <= 135)) {//........................................4
                        data[0] = twelveClock(flag);
                    } else if ((degree > 135 && degree <= 165)) {//........................................5
                        data[0] = twelveClock(flag);
                    } else if ((degree > 165 && degree <= 195)) {//........................................6
                        data[0] = elevenClock(flag);
                    } else if ((degree > 195 && degree <= 225)) {//........................................7
                        data[0] = tenClock(flag);
                    } else if ((degree > 225 && degree <= 255)) {//........................................8
                        data[0] = nineClock(flag);
                    } else if ((degree > 255 && degree <= 285)) {//........................................9
                        data[0] = eightClock(flag);
                    } else if ((degree > 285 && degree <= 315)) {//........................................10
                        data[0] = sixClock(flag);
                    } else if ((degree > 315 && degree <= 345)) {//........................................11
                        data[0] = sixClock(flag);
                    }
                }
                data[1] =  new StringBuilder("135");
                break;
            //---------------------------------------South West---------------------------------------
            case 8:
                if (DIRECTION_OPTION == 1) {
                    if ((degree >= 0 && degree <= 45) || (degree > 315 && degree <= 360)) {//..........North
                        data[0] = aroundStraight(flag);
                    } else if ((degree > 45 && degree <= 135)) {//.......................................East
                        data[0] = aroundStraight(flag);
                    } else if ((degree > 135 && degree <= 225)) {//........................................South
                        data[0] = turnALittleRightStraight(flag);
                    } else if ((degree > 225 && degree <= 315)) {//........................................West
                        data[0] = turnALittleLeftStraight(flag);
                    }
                } else if (DIRECTION_OPTION == 2) {
                    if ((degree >= 0 && degree <= 15) || (degree > 345 && degree <= 360)) {//..........12
                        data[0] =sevenClock(flag);
                    } else if ((degree > 15 && degree <= 45)) {//.......................................1
                        data[0] =sixClock(flag);
                    } else if ((degree > 45 && degree <= 75)) {//........................................2
                        data[0] =sixClock(flag);
                    } else if ((degree > 75 && degree <= 105)) {//........................................3
                        data[0] =fourClock(flag);
                    } else if ((degree > 105 && degree <= 135)) {//........................................4
                        data[0] =threeClock(flag);
                    } else if ((degree > 135 && degree <= 165)) {//........................................5
                        data[0] =twoClock(flag);
                    } else if ((degree > 165 && degree <= 195)) {//........................................6
                        data[0] =twoClock(flag);
                    } else if ((degree > 195 && degree <= 225)) {//........................................7
                        data[0] =oneClock(flag);
                    } else if ((degree > 225 && degree <= 255)) {//........................................8
                        data[0] =elevenClock(flag);
                    } else if ((degree > 255 && degree <= 285)) {//........................................9
                        data[0] =elevenClock(flag);
                    } else if ((degree > 285 && degree <= 315)) {//........................................10
                        data[0] =tenClock(flag);
                    } else if ((degree > 315 && degree <= 345)) {//........................................11
                        data[0] =nineClock(flag);
                    }
                }
                data[1] =  new StringBuilder("225");
                break;
        }
        return data;
    }

    private StringBuilder turnALittleLeftStraight(boolean flag) {
        if(!flag)
            return new StringBuilder("turn a little left and then go straight");
        else
            return new StringBuilder("in your device 11 o'clock");
    }

    private StringBuilder turnALittleRightStraight(boolean flag) {
        if(!flag)
            return new StringBuilder("turn a little right and then go straight");
        else
            return new StringBuilder("in your device 1 o'clock");
    }

    private StringBuilder aroundStraight(boolean flag) {
        if(!flag)
            return new StringBuilder("turn around and then go straight");
        else
            return new StringBuilder("in your device 6 o'clock");
    }

    private StringBuilder leftStraight(boolean flag) {
        if(!flag)
            return new StringBuilder("turn left and then go straight");
        else
            return new StringBuilder("on your device left");
    }

    private StringBuilder straight(boolean flag) {
        if(!flag)
            return new StringBuilder("go straight");
        else
            return new StringBuilder("in front of you");
    }

    private StringBuilder rightStraight(boolean flag) {
        if(!flag)
            return new StringBuilder("turn right and then go straight");
        else
            return  new StringBuilder("on your device right");
    }

    private StringBuilder tenClock(boolean flag) {
        if(!flag)
            return  new StringBuilder("turn to your device 10 o'clock and go straight");
        else
            return  new StringBuilder("in your device 10 o'clock");
    }

    private StringBuilder nineClock(boolean flag) {
        if(!flag)
            return  new StringBuilder("turn to your device 9 o'clock and go straight");
        else
            return  new StringBuilder("in your device 9 o'clock");
    }

    private StringBuilder eightClock(boolean flag) {
        if(!flag)
            return  new StringBuilder("turn to your device 8 o'clock and go straight");
        else
            return  new StringBuilder("in your device 8 o'clock");
    }

    private StringBuilder sevenClock(boolean flag) {
        if(!flag)
            return  new StringBuilder("turn to your device 7 o'clock and go straight");
        else
            return  new StringBuilder("in your device 7 o'clock");
    }

    private StringBuilder sixClock(boolean flag) {
        if(!flag)
            return  new StringBuilder("turn to your device 6 o'clock and go straight");
        else
            return  new StringBuilder("in your device 6 o'clock");
    }

    private StringBuilder fiveClock(boolean flag) {
        if(!flag)
            return  new StringBuilder("turn to your device 5 o'clock and go straight");
        else
            return  new StringBuilder("in your device 5 o'clock");
    }

    private StringBuilder fourClock(boolean flag) {
        if(!flag)
            return  new StringBuilder("turn to your device 4 o'clock and go straight");
        else
            return  new StringBuilder("in your device 4 o'clock");
    }

    private StringBuilder threeClock(boolean flag) {
        if(!flag)
            return  new StringBuilder("turn to your device 3 o'clock and go straight");
        else
            return  new StringBuilder("in your device 3 o'clock");
    }

    private StringBuilder twoClock(boolean flag) {
        if(!flag)
            return  new StringBuilder("turn to your device 2 o'clock and go straight");
        else
            return  new StringBuilder("in your device 2 o'clock");
    }

    private StringBuilder oneClock(boolean flag) {
        if(!flag)
            return  new StringBuilder("turn to your device 1 o'clock and go straight");
        else
            return  new StringBuilder("in your device 1 o'clock");
    }

    private StringBuilder twelveClock(boolean flag) {
        if(!flag)
            return new StringBuilder("go straight on your device 12 o'clock");
        else
            return new StringBuilder("in your device 12 o'clock");
    }

    private StringBuilder elevenClock(boolean flag) {
        if(!flag)
            return new StringBuilder("turn to your device 11 o'clock and go straight");
        else
            return new StringBuilder("in your device 11 o'clock");
    }
}
