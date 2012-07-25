package com.example.tmr;

import android.app.IntentService;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;
import android.util.Log;

public class MyService3 extends IntentService {

    final static String TAG = "MyService3";

    public MyService3() {
        super(TAG);
        Log.d(TAG,"MyService3()");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            Log.d(TAG, "timer was expired.");

            Intent broadcastIntent = new Intent();
            broadcastIntent.putExtra("message", "expire timer");
            broadcastIntent.setAction("MY_ACTION");
            getBaseContext().sendBroadcast(broadcastIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
