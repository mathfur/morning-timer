package com.example.tmr;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;


public class TimerActivity extends Activity implements OnClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        
        Button button = (Button) findViewById(R.id.button1);
        button.setOnClickListener(this);
    }
  
    public void onClick(View v){
    	Log.d("DEBUG", ">>frt");
        if(v.getId() == R.id.button1){
            scheduleService();
        }
    }
    
    protected void scheduleService(){
        Log.d("DEBUG", "scheduleService()");
        Context context = getBaseContext();
        Intent intent = new Intent(context, MyService3.class);
        PendingIntent pendingIntent = PendingIntent.getService(
            context, -1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager  = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        alarmManager.setInexactRepeating(
                AlarmManager.RTC, System.currentTimeMillis(), 5000, pendingIntent);
          }
      }
