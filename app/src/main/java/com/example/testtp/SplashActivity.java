package com.example.testtp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView logo = (ImageView) findViewById(R.id.logo);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable(){
            @Override
            public void run(){
                Intent main = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(main);
                finish();
            }
        }, 3000);   //3초 후 (3000ms) 스플래시 화면을 닫는다. (보통 사용하는 시간)
    }
}
