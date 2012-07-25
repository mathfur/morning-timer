package com.example.tmr;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class TimerActivity extends Activity implements OnClickListener {
    static MediaPlayer mp = new MediaPlayer();
    static Ringtone mRingtone;

    static State state = State.BEFORE_START;

    final static String TAG = "TimerActivity";

    Button startButton;
    IntentFilter intentFilter;
    MyBroadcastReceiver receiver;

    Vibrator vibrator;
    long[] vibrator_pattern = {2000, 500};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        Button button1 = (Button) findViewById(R.id.button1);
        Button button2 = (Button) findViewById(R.id.button2);
        Button button3 = (Button) findViewById(R.id.button3);
        Button button4 = (Button) findViewById(R.id.button4);

        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);

        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        mRingtone = RingtoneManager.getRingtone(getApplicationContext(), uri);

        receiver = new MyBroadcastReceiver();
        intentFilter = new IntentFilter();
        intentFilter.addAction("MY_ACTION");
        registerReceiver(receiver,  intentFilter);

        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
    }

    public void onClick(View v){
        Log.d(TAG, "onClick()");
        switch(v.getId()){
        case R.id.button1:
            changeState(State.BEFORE_RING);
            // 通常はラジオボタンで選択した秒数待つ
            startTimer(0);
            break;
        case R.id.button2:
            if(state != State.AFTER_RING){
                Log.d(TAG, "The state must be AFTER_RING when button2 is clicked.");
                break;
            }

            EditText text = (EditText) findViewById(R.id.editText1);

            if(checkCodeIsOK()){
                Log.d(TAG, "code is valid.");
                stopTimer();
                changeState(State.SHOWER);
                startTimer(20*60*1000);

                text.setText("OK");
            }else{
                Log.d(TAG,"code is not valid.");
                text.setText("NG");
            }
            break;
        case R.id.button3:
            // to complete
            if(state != State.SHOWER){
                Log.d(TAG, "The state must be SHOWER when button3 is clicked.");
                break;
            }
            if(state != State.TIMEOUT_COMPLETE){
                changeState(State.COMPLETE);
            }
            Log.d(TAG,"State is changed to COMPLETE.");
            break;
        case R.id.button4:
            // temporary stop.
            if(state != State.AFTER_RING){
                Log.d(TAG, "The state must be AFTER_RING when button4 is clicked.");
                break;
            }
            stopTimer();
            startTimer(10*1000);
            Log.d(TAG,"Timer is stopped temporarily.");
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
                wait_time = 5*60*60*1000;
                // wait_time = 7000;
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

    protected void expireTimer(){
        Log.d(TAG,"expireTimer()");

        if(state == State.BEFORE_RING || state == State.AFTER_RING){
             mRingtone.play();
             changeState(State.AFTER_RING);
             vibrator.vibrate(vibrator_pattern,0);
        }else if(TimerActivity.state ==  State.SHOWER){
             TimerActivity.state = State.TIMEOUT_COMPLETE;
        }
    }

    protected void stopTimer(){
        Log.d(TAG,"stopTimer()");
        mRingtone.stop();
        vibrator.cancel();
    }

    // コードが正か確認する
    protected boolean checkCodeIsOK(){
        Log.d(TAG,"readCode()");
        EditText text = (EditText) findViewById(R.id.editText1);

        Log.d(TAG, "getText() is " + text.getText().toString());

        // とりあえず固定文字列で対応
        if(text.getText().toString().equals("0jj2nbdcw")){
            return true;
        }else{
            return false;
        }
    }

    public void changeState(State s){
        TextView text = (TextView) findViewById(R.id.textView1);
        text.setText(s.toString());
        state = s;
    }

    public class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context,  Intent intent) {

            Bundle bundle = intent.getExtras();
            String message = bundle.getString("message");

            if(message.equals("expire timer")){
                expireTimer();
            }
        }
    }
}
