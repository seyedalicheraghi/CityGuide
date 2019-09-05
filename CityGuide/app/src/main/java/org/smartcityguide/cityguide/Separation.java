package org.smartcityguide.cityguide;


public class Separation {


    public static double [][][] seperation(int [][] connections, int sensors, int stepSize, int DISTANCE_UNITS){
        double [][][] path=new double[sensors][sensors][3];
        int [] doors=new int[sensors];

        for(int i=0;i<sensors;i++) {
            doors[connections[i][0]] = connections[i][2];
            for(int j=4;j<19;j=j+2){
                if(connections[i][j]!=-10){
                    path[connections[i][0]][connections[i][j]][0] = connections[i][j+1];// Here we define Weight Between src to des
                    //We define directions
                    switch (j){
                        case 4:
                            path[connections[i][0]][connections[i][j]][1] = 1; // North
                            break;
                        case 6:
                            path[connections[i][0]][connections[i][j]][1] = 2; // South
                            break;
                        case 8:
                            path[connections[i][0]][connections[i][j]][1] = 3; // East
                            break;
                        case 10:
                            path[connections[i][0]][connections[i][j]][1] = 4; // West
                            break;
                        case 12:
                            path[connections[i][0]][connections[i][j]][1] = 5; // NorthEast
                            break;
                        case 14:
                            path[connections[i][0]][connections[i][j]][1] = 6; // NorthWest
                            break;
                        case 16:
                            path[connections[i][0]][connections[i][j]][1] = 7; // SouthEast
                            break;
                        case 18:
                            path[connections[i][0]][connections[i][j]][1] = 8; // SouthWest
                            break;
                    }
                    if (DISTANCE_UNITS==1)
                        path[connections[i][0]][connections[i][j]][2] = (connections[i][j+1]*100)/stepSize;// Here we define number of steps based on step size
                    else
                        path[connections[i][0]][connections[i][j]][2] = (connections[i][j+1]*12)/stepSize;// Here we define number of steps based on step size

                }
            }
        }


        return path;
    }
}

