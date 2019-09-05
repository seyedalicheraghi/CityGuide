package org.smartcityguide.cityguide;

import android.content.Context;
import android.media.AudioManager;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Locale;

public class SpeakOut {
    Context ctx;
    private String speakString;
    private TextToSpeech textToSpeech;
    private String TAG = "MainActivity";

    public SpeakOut(Context ctx) {
        this.ctx = ctx;
    }

    public void start(final String string) {
        speakString = string;
        textToSpeech = new TextToSpeech(ctx, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {

                if (status == TextToSpeech.SUCCESS) {
                    int result = textToSpeech.setLanguage(Locale.US);

                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.d(TAG, "Text to Speech not Supported!");
                    } else {
                        HashMap<String, String> myHashAlarm = new HashMap<>();
                        myHashAlarm.put(TextToSpeech.Engine.KEY_PARAM_STREAM, String.valueOf(AudioManager.STREAM_ALARM));
                        myHashAlarm.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "SOME MESSAGE");
                        textToSpeech.speak(string, TextToSpeech.QUEUE_FLUSH, myHashAlarm);
                    }
                } else {
                    Toast.makeText(ctx, "Text to Speech Initialization Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        textToSpeech.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String utteranceId) {
            }

            @Override
            public void onDone(String utteranceId) {

            }

            @Override
            public void onError(String utteranceId) {
                Log.d(TAG, "SpeakOutError");
            }

            @Override
            public void onStop(String utteranceId, boolean interrupted) {
                super.onStop(utteranceId, interrupted);
                start(speakString);
            }
        });

    }
}
