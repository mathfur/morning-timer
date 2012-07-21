package com.example.tmr;

import android.app.IntentService;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

public class MyService3 extends IntentService {

    final static String TAG = "MyService3";

    public MyService3() {
        super(TAG);
        Log.d(TAG,"MyService3()");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
      Log.d(TAG, "timer was expired.");
      if(TimerActivity.state == State.BEFORE_RING){
          TimerActivity.mRingtone.play();
          TimerActivity.state = State.AFTER_RING;
      }else if(TimerActivity.state ==  State.SHOWER){
          TimerActivity.state = State.TIMEOUT_COMPLETE;
          // TODO: ���Ԑ؂���������b�Z�[�W��\��
      }
    }
  }
