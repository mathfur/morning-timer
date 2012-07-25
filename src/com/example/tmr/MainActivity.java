package com.example.tmr;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;

public class MainActivity extends Activity implements OnClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = (Button) findViewById(R.id.button1);
        button.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    public void onClick(View v){
        CheckBox c1 = (CheckBox) findViewById(R.id.checkBox1);
        CheckBox c2 = (CheckBox) findViewById(R.id.checkBox2);
        CheckBox c3 = (CheckBox) findViewById(R.id.checkBox3);
        CheckBox c4 = (CheckBox) findViewById(R.id.checkBox4);

        if(v.getId() == R.id.button1){
            if(c1.isChecked() && c2.isChecked() && c3.isChecked() && c4.isChecked()){
                Intent intent = new Intent(MainActivity.this,TimerActivity.class);
                startActivity(intent);
            }
        }
    }
}
