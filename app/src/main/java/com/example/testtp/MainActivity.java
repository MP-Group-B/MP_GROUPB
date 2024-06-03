package com.example.testtp;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private ImageView trafficBtn, weatherBtn, homeBtn, calendarBtn, hotplaceBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        init();

        trafficBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TrafficActivity.class);
                startActivity(intent);
            }
        });

        weatherBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this, WeatherActivity.class);
                startActivity(intent);
            }
        });

        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        calendarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CalendarActivity.class);
                startActivity(intent);
            }
        });

        hotplaceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HotplaceActivity.class);
                startActivity(intent);
            }
        });
    }

    public void init(){
        trafficBtn = (ImageView) findViewById(R.id.trafficBtn);
        weatherBtn = (ImageView) findViewById(R.id.weatherBtn);
        homeBtn = (ImageView) findViewById(R.id.homeBtn);
        calendarBtn = (ImageView) findViewById(R.id.calendarBtn);
        hotplaceBtn = (ImageView) findViewById(R.id.hotplaceBtn);
    }
}
