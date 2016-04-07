package com.example.radhason.newinsertphp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends Activity {
    private static int SPLASH_TIME_OUT = 4000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Thread splashThread = new Thread(){
            @Override
            public void run() {
                super.run();
                try{
                    sleep(SPLASH_TIME_OUT);
                    Intent intent = new Intent(MainActivity.this,WainActivity.class);
                    startActivity(intent);
                    finish();
                }
                catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        };
        splashThread.start();
    }
}
