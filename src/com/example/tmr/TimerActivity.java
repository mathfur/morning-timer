package com.example.tmr;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;

public class TimerActivity extends Activity implements OnClickListener {
    static MediaPlayer mp = new MediaPlayer();
    static Ringtone mRingtone;

    static State state = State.BEFORE_START;

    final static String TAG = "TimerActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        Button button1 = (Button) findViewById(R.id.button1);
        Button button2 = (Button) findViewById(R.id.button2);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);

        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        mRingtone = RingtoneManager.getRingtone(getApplicationContext(), uri);
    }

    public void onClick(View v){
        Log.d(TAG, "onClick()");
        switch(v.getId()){
        case R.id.button1:
            state = State.BEFORE_RING;
            // 通常はラジオボタンで選択した秒数待つ
            startTimer(0);
            break;
        case R.id.button2:
            if(state != State.BEFORE_RING){
                Log.d(TAG, "The state must be BEFORE_RING when button2 is clicked.");
                break;
            }
            if(checkCodeIsOK()){
                stopTimer();
                state = State.SHOWER;
                startTimer(20*60*1000);
                // TODO: 「間違えたパスです」消す
            }else{
                // TODO: 「間違えたパスです」表示
            }
            break;
        case R.id.button3:
            // to complete
            if(state != State.SHOWER){
                Log.d(TAG, "The state must be SHOWER when button3 is clicked.");
                break;
            }
            if(state != State.TIMEOUT_COMPLETE){
                state = State.COMPLETE;
            }
            break;
        case R.id.button4:
            // temporary stop.
            if(state != State.AFTER_RING){
                Log.d(TAG, "The state must be AFTER_RING when button3 is clicked.");
                break;
            }
            state = State.BEFORE_RING;
            stopTimer();
            startTimer(15*60*1000);
            break;
        default:
        }
    }

    // If wait_time is 0, then radio button value is used.
    protected void startTimer(int wait_time){
        Log.d(TAG, "startTimer()");

        if(wait_time == 0){
            RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup1);
            switch(radioGroup.getCheckedRadioButtonId()){
            case R.id.radioButton1:
                Log.d(TAG, "radioButton1 is selected.");
                // alerm_start_time = 5*60*60*1000;
                wait_time = 2000;
                break;
            case R.id.radioButton2:
                Log.d(TAG, "radioButton2 is selected.");
                // 5:30セット
                wait_time = (5*60 + 30)*60*1000;
                break;
            case R.id.radioButton3:
                Log.d(TAG, "radioButton3 is selected.");
                // 6:00セット
                wait_time = 6*60*60*1000;
                break;
            }
        }

        Context context = getBaseContext();
        Intent intent = new Intent(context, MyService3.class);
        PendingIntent pendingIntent = PendingIntent.getService(context, -1, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarmManager  = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + wait_time, 5*60*1000, pendingIntent);
        Log.d(TAG,"Timer is set.");
    }

    protected void stopTimer(){
        // どうやって止める?
        Log.d(TAG,"stopTimer()");
        mRingtone.stop();


    }

    // コードが正か確認する
    protected boolean checkCodeIsOK(){
        Log.d(TAG,"readCode()");
        EditText text = (EditText) findViewById(R.id.editText1);

        // とりあえず固定文字列で対応
        if(text.getText().toString() == "123"){
            return true;
        }else{
            return false;
        }
    }
}
