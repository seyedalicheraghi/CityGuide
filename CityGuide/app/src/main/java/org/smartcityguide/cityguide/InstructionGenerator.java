package org.smartcityguide.cityguide;

import android.util.Log;
import java.util.ArrayList;
import java.util.HashMap;

class InstructionGenerator {
    ArrayList<ArrayList<HashMap<String, String>>> completePath(String getRouteSrcDst,float nextPointDirection, int[][] connections,
                                 int DIRECTION_OPTION, double[][][] pathSteps,boolean MIDPOINTS_LOCATIONS,
                                 int DISTANCE_OPTION, String[][] buildingSensorsMap, boolean reachFlag,
                                 boolean explorationFlag, String unit,  boolean ROUTE_PREVIEW, boolean firstTimeFlag) {

        

        ArrayList<HashMap<String, String>> instruction = new ArrayList<>();
        ArrayList<HashMap<String, String>> dataList = new ArrayList<>();
        ArrayList<ArrayList<HashMap<String, String>>> resultData = new ArrayList<>();
        StringBuilder tempBuffer;
        StringBuilder[] instructionVoice = new StringBuilder[4];

        OverallGuidance og = new OverallGuidance();
        String[] sString = getRouteSrcDst.split(",");
        //Try to get direction
        StringBuilder[] stepBySteptemp;
        StringBuilder[] stepByStep = new StringBuilder[sString.length - 1];
        StringBuilder[] stepByStepBuf = new StringBuilder[sString.length - 1];
        float nextPointDirectionBuffer=nextPointDirection;
        for (int i = 0; i < (sString.length - 1); i++) {
            for (int j = 4; j < 19; j = j + 2) {
                if (connections[Integer.parseInt(sString[i])][j] == Integer.valueOf(sString[i + 1])) {
                    stepBySteptemp = og.directionGuide(((j / 2) - 1), nextPointDirection, false, DIRECTION_OPTION);
                    stepByStep[i] = stepBySteptemp[0];
                    nextPointDirection = Float.parseFloat(stepBySteptemp[1].toString());
                    stepByStepBuf[i] = stepBySteptemp[1];
                }
            }
        }
        Log.d("alicheraghi", getRouteSrcDst);
        HashMap<String, String> data = new HashMap<>();
        if (MIDPOINTS_LOCATIONS) {
            if (DISTANCE_OPTION == 1 && sString.length > 1) {
                if (connections[Integer.parseInt(sString[1])][1] < 0) {
                    instructionVoice[0] = new StringBuilder("You are at " + buildingSensorsMap[Integer.parseInt(sString[0])][0] + ". You should " + stepByStep[0] + " for " + (int) (pathSteps[Integer.parseInt(sString[0])][Integer.parseInt(sString[1])][0]) + unit + " to reach " + buildingSensorsMap[Integer.parseInt(sString[1])][0] + ". ");
                } else if (connections[Integer.parseInt(sString[1])][1] == 100) {
                    instructionVoice[0] = new StringBuilder("You are at " + buildingSensorsMap[Integer.parseInt(sString[0])][0] + ". You should " + stepByStep[0] + " for " + (int) (pathSteps[Integer.parseInt(sString[0])][Integer.parseInt(sString[1])][0]) + unit +
                            " and then " + stepByStep[1] + " for " + (int) (pathSteps[Integer.parseInt(sString[1])][Integer.parseInt(sString[2])][0]) + unit+ " to reach " + buildingSensorsMap[Integer.parseInt(sString[2])][0] + ". ");
                } else if (connections[Integer.parseInt(sString[1])][1] == 200) {
                    StringBuilder[] stepByStepbuf = og.directionGuide(connections[Integer.parseInt(sString[1])][2], nextPointDirectionBuffer, true, DIRECTION_OPTION);
                    reachFlag = true;
                    explorationFlag=false;
                    Log.d("alicheraghi", "completePath: "+stepByStepbuf[0]);
                    instructionVoice[0] = new StringBuilder("You are at destination proximity! destination is somewhere " + stepByStepbuf[0]);

                }
            } else {
                if (sString.length > 1 && connections[Integer.parseInt(sString[1])][1] < 0) {
                    instructionVoice[0] = new StringBuilder("You are at " + buildingSensorsMap[Integer.parseInt(sString[0])][0] + ". You should " + stepByStep[0] + " for " + (int) (pathSteps[Integer.parseInt(sString[0])][Integer.parseInt(sString[1])][2]) + " steps to reach " + buildingSensorsMap[Integer.parseInt(sString[1])][0] + ". ");
                } else if (sString.length > 1 && connections[Integer.parseInt(sString[1])][1] == 100) {
                    instructionVoice[0] = new StringBuilder("You are at " + buildingSensorsMap[Integer.parseInt(sString[0])][0] + ". You should " + stepByStep[0] + " for " + (int) (pathSteps[Integer.parseInt(sString[0])][Integer.parseInt(sString[1])][2]) + " steps. " +
                            " and then " + stepByStep[1] + " for " + (int) (pathSteps[Integer.parseInt(sString[1])][Integer.parseInt(sString[2])][0]) + " steps to reach " + buildingSensorsMap[Integer.parseInt(sString[2])][0] + ". ");
                } else if (sString.length > 1 && connections[Integer.parseInt(sString[1])][1] == 200) {
                    StringBuilder[] stepByStepbuf = og.directionGuide(connections[Integer.parseInt(sString[1])][2], nextPointDirectionBuffer, true, DIRECTION_OPTION);
                    reachFlag = true;
                    explorationFlag=false;
                    instructionVoice[0] = new StringBuilder("You are at destination proximity! destination is somewhere " + stepByStepbuf[0]);
                }
            }
        } else {
            if (DISTANCE_OPTION == 1) {
                if (sString.length > 1 && connections[Integer.parseInt(sString[1])][1] < 0) {
                    instructionVoice[0] = new StringBuilder("You should " + stepByStep[0] + " for " + (int) (pathSteps[Integer.parseInt(sString[0])][Integer.parseInt(sString[1])][0]) + unit + ". ");
                } else if (sString.length > 1 && connections[Integer.parseInt(sString[1])][1] == 100) {
                    instructionVoice[0] = new StringBuilder("You should " + stepByStep[0] + " for " + (int) (pathSteps[Integer.parseInt(sString[0])][Integer.parseInt(sString[1])][0]) + unit +
                            " and then " + stepByStep[1] + " for " + (int) (pathSteps[Integer.parseInt(sString[1])][Integer.parseInt(sString[2])][0]) + unit + ". ");
                } else if (sString.length > 1 && connections[Integer.parseInt(sString[1])][1] == 200) {
                    StringBuilder[] stepByStepbuf = og.directionGuide(connections[Integer.parseInt(sString[1])][2], nextPointDirectionBuffer, true, DIRECTION_OPTION);
                    reachFlag = true;
                    explorationFlag=false;
                    instructionVoice[0] = new StringBuilder("You are at destination proximity! destination is somewhere " + stepByStepbuf[0]);
                }
            } else {
                if (sString.length > 1 && connections[Integer.parseInt(sString[1])][1] < 0) {
                    instructionVoice[0] = new StringBuilder("You should " + stepByStep[0] + " for " + (int) (pathSteps[Integer.parseInt(sString[0])][Integer.parseInt(sString[1])][2]) + " steps. ");
                } else if (sString.length > 1 && connections[Integer.parseInt(sString[1])][1] == 100) {
                    instructionVoice[0] = new StringBuilder("You should " + stepByStep[0] + " for " + (int) (pathSteps[Integer.parseInt(sString[0])][Integer.parseInt(sString[1])][2]) + " steps and then "+
                            stepByStep[1] + " for " + (int) (pathSteps[Integer.parseInt(sString[1])][Integer.parseInt(sString[2])][0]) + " steps. ");
                } else if (sString.length > 1 && connections[Integer.parseInt(sString[1])][1] == 200) {
                    StringBuilder[] stepByStepbuf = og.directionGuide(connections[Integer.parseInt(sString[1])][2], nextPointDirectionBuffer, true, DIRECTION_OPTION);
                    reachFlag = true;
                    explorationFlag=false;
                    instructionVoice[0] = new StringBuilder("You are at destination proximity! destination is somewhere " + stepByStepbuf[0]);
                }
            }
        }
        data.put("description", instructionVoice[0].toString());
        dataList.add(data);

        HashMap<String, String> instructionHash;
        if (sString.length == 3) {

            if (connections[Integer.parseInt(sString[2])][1] != 200) {
                data = new HashMap<>();
                instructionHash = new HashMap<>();
                if (DISTANCE_OPTION == 2) {
                    Log.d("alicheraghi", "1");
                    tempBuffer = new StringBuilder("Then To get to your destination at " + buildingSensorsMap[Integer.parseInt(sString[2])][0] + " you need to " + stepByStep[1] + " for " + (int) (pathSteps[Integer.valueOf(sString[1])][Integer.valueOf(sString[2])][2]) + " steps. ");
                    instructionHash.put("description", "You are at " + buildingSensorsMap[Integer.parseInt(sString[1])][0] + ". To get to your destination, you need to " + stepByStep[1] + " for " + (int) (pathSteps[Integer.valueOf(sString[1])][Integer.valueOf(sString[2])][2]) + " steps. ");
                } else {
                    Log.d("alicheraghi", "2");
                    tempBuffer = new StringBuilder("Then To get to your destination at " + buildingSensorsMap[Integer.parseInt(sString[2])][0] + " you need to " + stepByStep[1] + " for " + (int) (pathSteps[Integer.valueOf(sString[1])][Integer.valueOf(sString[2])][0]) + unit + ".");
                    instructionHash.put("description", "You are at " + buildingSensorsMap[Integer.parseInt(sString[1])][0] + ". To get to your destination, you need to " + stepByStep[1] + " for " + (int) (pathSteps[Integer.valueOf(sString[1])][Integer.valueOf(sString[2])][0]) + unit + ".");
                }

                instruction.add(instructionHash);
                data.put("description", tempBuffer.toString());
                dataList.add(data);
                if (ROUTE_PREVIEW && !firstTimeFlag)
                    instructionVoice[0].append(tempBuffer);
            } else {
                data = new HashMap<>();
                instructionHash = new HashMap<>();
                StringBuilder[] stepByStepbuf = og.directionGuide(connections[Integer.parseInt(sString[2])][2], Float.valueOf(String.valueOf(stepByStepBuf[0])), true, DIRECTION_OPTION);

                if (DISTANCE_OPTION == 2) {
                    tempBuffer = new StringBuilder("You are at destination proximity! destination is somewhere " + stepByStepbuf[0]);
                    instructionHash.put("description", "You are at destination proximity! destination is somewhere " + stepByStepbuf[0]);
                } else {
                    tempBuffer = new StringBuilder("You are at destination proximity! destination is somewhere " + stepByStepbuf[0]);
                    instructionHash.put("description", "You are at destination proximity! destination is somewhere " + stepByStepbuf[0]);
                }

                instruction.add(instructionHash);
                data.put("description", tempBuffer.toString());
                dataList.add(data);
            }
        } else if (sString.length > 3) {

            for (int i = 1; i < sString.length - 1; i++) {
                if (MIDPOINTS_LOCATIONS) {
                    data = new HashMap<>();
                    instructionHash = new HashMap<>();
                    if (DISTANCE_OPTION == 2) {
                        if (connections[Integer.parseInt(sString[i + 1].trim())][1] < 0) {
                            data.put("description", "You should " + stepByStep[i] + " for " + (int)(pathSteps[Integer.valueOf(sString[i])][Integer.valueOf(sString[i + 1])][2]) + " steps to reach " + buildingSensorsMap[Integer.parseInt(sString[i + 1].trim())][0] + ". ");
                            instructionHash.put("description", "You are at " + buildingSensorsMap[Integer.parseInt(sString[i])][0] + ". You should " + stepByStep[i] + " for " + (int)(pathSteps[Integer.valueOf(sString[i])][Integer.valueOf(sString[i + 1])][2]) + " steps to reach " + buildingSensorsMap[Integer.parseInt(sString[i + 1].trim())][0] + ". ");
                            dataList.add(data);
                            Log.d("alicheraghi", "3");
                        } else if (connections[Integer.parseInt(sString[i + 1].trim())][1] == 100) {
                            data.put("description", "You should " + stepByStep[i] + " for " + (int)(pathSteps[Integer.valueOf(sString[i])][Integer.valueOf(sString[i + 1])][2]) + " steps. ");
                            instructionHash.put("description", "You are at " + buildingSensorsMap[Integer.parseInt(sString[i])][0] + ". You should " + stepByStep[i] + " for " + (int)(pathSteps[Integer.valueOf(sString[i])][Integer.valueOf(sString[i + 1])][2]) + " steps. ");
                            dataList.add(data);
                            Log.d("alicheraghi", "4");
                        } else if (connections[Integer.parseInt(sString[i + 1].trim())][1] == 200) {
                            StringBuilder[] stepByStepbuf = og.directionGuide(connections[Integer.parseInt(sString[i + 1].trim())][2], Float.valueOf(String.valueOf(stepByStepBuf[i - 1])), true, DIRECTION_OPTION);
                            data.put("description", "You are at destination proximity! destination is somewhere " + stepByStepbuf[0]);
                            dataList.add(data);
                            stepByStepbuf = og.directionGuide(connections[Integer.parseInt(sString[i].trim())][2], Float.valueOf(String.valueOf(stepByStepBuf[i - 1])), true, DIRECTION_OPTION);
                            instructionHash.put("description", "You are at destination proximity! destination is somewhere " + stepByStepbuf[0].toString());
                            Log.d("alicheraghi", "5");
                        }
                    } else {

                        if (connections[Integer.parseInt(sString[i + 1].trim())][1] < 0 && connections[Integer.parseInt(sString[i].trim())][1]!=100) {
                            data.put("description", "You should " + stepByStep[i] + " for " + (int)(pathSteps[Integer.valueOf(sString[i])][Integer.valueOf(sString[i + 1])][0]) + unit + " to reach " + buildingSensorsMap[Integer.parseInt(sString[i + 1].trim())][0] + ". ");
                            dataList.add(data);
                            instructionHash.put("description", "You are at " + buildingSensorsMap[Integer.parseInt(sString[i])][0] + ". You should " + stepByStep[i] + " for " + (int) (pathSteps[Integer.valueOf(sString[i])][Integer.valueOf(sString[i + 1])][0]) + unit + " to reach " + buildingSensorsMap[Integer.parseInt(sString[i + 1].trim())][0] + ". ");
                            Log.d("alicheraghi", "6");
                        } else if (connections[Integer.parseInt(sString[i + 1].trim())][1] == 100) {
                            data.put("description", "You should " + stepByStep[i] + " for " + (int)(pathSteps[Integer.valueOf(sString[i])][Integer.valueOf(sString[i + 1])][0]) + unit + " and then "+
                                    stepByStep[i + 1] + " for " + (int)(pathSteps[Integer.valueOf(sString[i + 1])][Integer.valueOf(sString[i + 2])][0]) + unit + " to reach " + buildingSensorsMap[Integer.parseInt(sString[i + 2].trim())][0] + ". ");
                            dataList.add(data);
                            Log.d("alicheraghi","7");
                            instructionHash.put("description", "You are at " + buildingSensorsMap[Integer.parseInt(sString[i])][0] + ". You should " + stepByStep[i] + " for " + (int) (pathSteps[Integer.valueOf(sString[i])][Integer.valueOf(sString[i + 1])][0]) + unit + " and then " +
                                    stepByStep[i+1] + " for " + (int) (pathSteps[Integer.valueOf(sString[i+1])][Integer.valueOf(sString[i + 2])][0]) + unit + " to reach " + buildingSensorsMap[Integer.parseInt(sString[i + 2].trim())][0] + ". ");
                        } else if (connections[Integer.parseInt(sString[i + 1].trim())][1] == 200) {
                            StringBuilder[] stepByStepbuf = og.directionGuide(connections[Integer.parseInt(sString[i + 1].trim())][2], Float.valueOf(String.valueOf(stepByStepBuf[i - 1])), true, DIRECTION_OPTION);
                            data.put("description", "You are at destination proximity! destination is somewhere " + stepByStepbuf[0]);
                            dataList.add(data);
                            stepByStepbuf = og.directionGuide(connections[Integer.parseInt(sString[i].trim())][2], Float.valueOf(String.valueOf(stepByStepBuf[i - 1])), true, DIRECTION_OPTION);
                            instructionHash.put("description", "You are at destination proximity! destination is somewhere " + stepByStepbuf[0].toString());
                            Log.d("alicheraghi", "8");
                        }
                    }
                    instruction.add(instructionHash);


                    if (ROUTE_PREVIEW && DISTANCE_OPTION == 2 && !firstTimeFlag) {
                        if (connections[Integer.parseInt(sString[i + 1].trim())][1] < 0) {
                            instructionVoice[0].append("Then you should ").append(stepByStep[i]).append(" for ").append((int) (pathSteps[Integer.valueOf(sString[i])][Integer.valueOf(sString[i + 1])][2])).append(" steps to reach ").append(buildingSensorsMap[Integer.parseInt(sString[i + 1].trim())][0]).append(". ");
                        } else if (connections[Integer.parseInt(sString[i + 1].trim())][1] == 100) {
                            instructionVoice[0].append("Then you should ").append(stepByStep[i]).append(" for ").append((int) (pathSteps[Integer.valueOf(sString[i])][Integer.valueOf(sString[i + 1])][2])).append(" steps.");
                        }
                    } else if (ROUTE_PREVIEW && DISTANCE_OPTION == 1 && !firstTimeFlag) {
                        if (connections[Integer.parseInt(sString[i + 1].trim())][1] < 0) {
                            instructionVoice[0].append("Then you should ").append(stepByStep[i]).append(" for ").append((int) (pathSteps[Integer.valueOf(sString[i])][Integer.valueOf(sString[i + 1])][0])).append(unit).append(" to reach ").append(buildingSensorsMap[Integer.parseInt(sString[i + 1].trim())][0]).append(". ");
                        } else if (connections[Integer.parseInt(sString[i + 1].trim())][1] == 100) {
                            instructionVoice[0].append("Then you should ").append(stepByStep[i]).append(" for ").append((int) (pathSteps[Integer.valueOf(sString[i])][Integer.valueOf(sString[i + 1])][0])).append(unit).append(". ");
                        }
                    }
                } else {
                    data = new HashMap<>();
                    instructionHash = new HashMap<>();
                    if (DISTANCE_OPTION == 2) {
                        if (connections[Integer.parseInt(sString[i + 1].trim())][1] < 0) {
                            data.put("description", "You should " + stepByStep[i] + " for " + (int) (pathSteps[Integer.valueOf(sString[i])][Integer.valueOf(sString[i + 1])][2]) + " steps.");
                            instructionHash.put("description", "You should " + stepByStep[i] + " for " + (int) (pathSteps[Integer.valueOf(sString[i])][Integer.valueOf(sString[i + 1])][2]) + " steps.");
                            Log.d("alicheraghi", "15");
                        } else if (connections[Integer.parseInt(sString[i + 1].trim())][1] == 100) {
                            data.put("description", "You should " + stepByStep[i] + " for " + (int) (pathSteps[Integer.valueOf(sString[i])][Integer.valueOf(sString[i + 1])][2]) + " steps. ");
                            instructionHash.put("description", "You should " + stepByStep[i] + " for " + (int) (pathSteps[Integer.valueOf(sString[i])][Integer.valueOf(sString[i + 1])][2]) + " steps. ");
                            Log.d("alicheraghi", "16");
                        } else if (connections[Integer.parseInt(sString[i + 1].trim())][1] == 200) {
                            StringBuilder[] stepByStepbuf = og.directionGuide(connections[Integer.parseInt(sString[i + 1].trim())][2], Float.valueOf(String.valueOf(stepByStepBuf[i - 1])), true, DIRECTION_OPTION);
                            data.put("description", "You are at destination proximity! destination is somewhere " + stepByStepbuf[0]);
                            stepByStepbuf = og.directionGuide(connections[Integer.parseInt(sString[i].trim())][2], Float.valueOf(String.valueOf(stepByStepBuf[i - 1])), true, DIRECTION_OPTION);
                            instructionHash.put("description", "You are at destination proximity! destination is somewhere " + stepByStepbuf[0].toString());
                            Log.d("alicheraghi", "17");
                        }
                    } else {
                        if (connections[Integer.parseInt(sString[i + 1].trim())][1] < 0) {
                            data.put("description", "You should " + stepByStep[i] + " for " + (int) (pathSteps[Integer.valueOf(sString[i])][Integer.valueOf(sString[i + 1])][0]) + unit + ". ");
                            instructionHash.put("description", "You should " + stepByStep[i] + " for " + (int) (pathSteps[Integer.valueOf(sString[i])][Integer.valueOf(sString[i + 1])][0]) + unit + ". ");
                            Log.d("alicheraghi", "18");
                        } else if (connections[Integer.parseInt(sString[i + 1].trim())][1] == 100) {
                            data.put("description", "You should " + stepByStep[i] + " for " + (int) (pathSteps[Integer.valueOf(sString[i])][Integer.valueOf(sString[i + 1])][0]) + unit + ". ");
                            instructionHash.put("description", "You should " + stepByStep[i] + " for " + (int) (pathSteps[Integer.valueOf(sString[i])][Integer.valueOf(sString[i + 1])][0]) + unit + ". ");
                            Log.d("alicheraghi", "19");
                        } else if (connections[Integer.parseInt(sString[i + 1].trim())][1] == 200) {
                            StringBuilder[] stepByStepbuf = og.directionGuide(connections[Integer.parseInt(sString[i + 1].trim())][2], Float.valueOf(String.valueOf(stepByStepBuf[i - 1])), true, DIRECTION_OPTION);
                            data.put("description", "You are at destination proximity! destination is somewhere " + stepByStepbuf[0]);
                            stepByStepbuf = og.directionGuide(connections[Integer.parseInt(sString[i].trim())][2], Float.valueOf(String.valueOf(stepByStepBuf[i - 1])), true, DIRECTION_OPTION);
                            instructionHash.put("description", "You are at destination proximity! destination is somewhere " + stepByStepbuf[0].toString());
                            Log.d("alicheraghi", "20");
                        }
                    }

                    instruction.add(instructionHash);
                    dataList.add(data);
                    if (ROUTE_PREVIEW && DISTANCE_OPTION == 2 && !firstTimeFlag) {
                        if (connections[Integer.parseInt(sString[i + 1].trim())][1] < 0) {
                            instructionVoice[0].append("Then you should ").append(stepByStep[i]).append(" for ").append((int) (pathSteps[Integer.valueOf(sString[i])][Integer.valueOf(sString[i + 1])][2])).append(" steps.");
                        } else if (connections[Integer.parseInt(sString[i + 1].trim())][1] == 100) {
                            instructionVoice[0].append("Then you should ").append(stepByStep[i]).append(" for ").append((int) (pathSteps[Integer.valueOf(sString[i])][Integer.valueOf(sString[i + 1])][2])).append(" steps.");
                        }
                    } else if (ROUTE_PREVIEW && DISTANCE_OPTION == 1 && !firstTimeFlag) {
                        if (connections[Integer.parseInt(sString[i + 1].trim())][1] < 0) {
                            instructionVoice[0].append("Then you should ").append(stepByStep[i]).append(" for ").append((int) (pathSteps[Integer.valueOf(sString[i])][Integer.valueOf(sString[i + 1])][0])).append(unit).append(". ");
                        } else if (connections[Integer.parseInt(sString[i + 1].trim())][1] == 100) {
                            instructionVoice[0].append("Then you should ").append(stepByStep[i]).append(" for ").append((int) (pathSteps[Integer.valueOf(sString[i])][Integer.valueOf(sString[i + 1])][0])).append(unit).append(". ");
                        }
                    }
                }
            }

        }

        ArrayList<HashMap<String, String>> others = new ArrayList<>();

        data = new HashMap<>();
        data.put("instructionVoice[0]", String.valueOf(instructionVoice[0]));
        others.add(data);

        data = new HashMap<>();
        data.put("instructionVoice[1]", String.valueOf((int) (pathSteps[Integer.parseInt(sString[0])][Integer.parseInt(sString[1])][2])));
        others.add(data);

        data = new HashMap<>();
        data.put("explorationFlag", String.valueOf(explorationFlag));
        others.add(data);

        data = new HashMap<>();
        data.put("reachFlag", String.valueOf(reachFlag));
        others.add(data);

        resultData.add(dataList);
        resultData.add(instruction);
        resultData.add(others);

        return resultData;
    }
}
