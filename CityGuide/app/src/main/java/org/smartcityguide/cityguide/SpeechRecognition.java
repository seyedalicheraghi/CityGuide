package org.smartcityguide.cityguide;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.HashMap;

public class SpeechRecognition implements RecognitionListener {

    private ProgressBar progressBar;
    private Context ctx;
    private SpeechRecognizer speech = null;
    private String TAG = "MainActivity";
    public RequestResponse delegate = null;

    public SpeechRecognition(Context ctx, ProgressBar progressBar){
        this.ctx = ctx;
        this.progressBar = progressBar;
    }

    public void onStart(Intent recognizerIntent){
        speech = SpeechRecognizer.createSpeechRecognizer(ctx);
        speech.setRecognitionListener(this);
        speech.startListening(recognizerIntent);
    }

    @Override
    public void onReadyForSpeech(Bundle bundle) {

    }

    @Override
    public void onBeginningOfSpeech() {
        progressBar.setIndeterminate(false);
        progressBar.setMax(10);
    }

    @Override
    public void onRmsChanged(float v) {
        progressBar.setProgress((int) v);
    }

    @Override
    public void onBufferReceived(byte[] bytes) {

    }

    @Override
    public void onEndOfSpeech() {
        progressBar.setIndeterminate(true);
    }

    @Override
    public void onError(int errorCode) {
        String errorMessage = getErrorText(errorCode);
        Log.d(TAG, "FAILED " + errorMessage);
    }

    @Override
    public void onResults(Bundle bundle) {
        ArrayList<HashMap<String, String>> dataList = new ArrayList<>();

        ArrayList<String> matches = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        for(int i=0;i<matches.size();i++){
            HashMap<String, String> resultHash = new HashMap<>();
            resultHash.put("description",matches.get(i));
            dataList.add(resultHash);
        }
        delegate.searchedFinish(dataList);
        progressBar.setIndeterminate(false);
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onPartialResults(Bundle bundle) {

    }

    @Override
    public void onEvent(int i, Bundle bundle) {

    }
    public static String getErrorText(int errorCode) {
        String message;
        switch (errorCode) {
            case SpeechRecognizer.ERROR_AUDIO:
                message = "Audio recording error";
                break;
            case SpeechRecognizer.ERROR_CLIENT:
                message = "Client side error";
                break;
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                message = "Insufficient permissions";
                break;
            case SpeechRecognizer.ERROR_NETWORK:
                message = "Network error";
                break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                message = "Network timeout";
                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                message = "No match";
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                message = "RecognitionService busy";
                break;
            case SpeechRecognizer.ERROR_SERVER:
                message = "error from server";
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                message = "No speech input";
                break;
            default:
                message = "Didn't understand, please try again.";
                break;
        }
        return message;
    }
}
