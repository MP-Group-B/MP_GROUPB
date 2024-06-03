package com.example.testtp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class TrafficActivity extends AppCompatActivity {
    private ImageView trafficBtn, weatherBtn, homeBtn, calendarBtn, hotplaceBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traffic);

        init();
        trafficBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TrafficActivity.this, TrafficActivity.class);
                startActivity(intent);
                finish();
            }
        });

        weatherBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(TrafficActivity.this, WeatherActivity.class);
                startActivity(intent);
                finish();
            }
        });

        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TrafficActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        calendarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TrafficActivity.this, CalendarActivity.class);
                startActivity(intent);
                finish();
            }
        });

        hotplaceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TrafficActivity.this, HotplaceActivity.class);
                startActivity(intent);
                finish();
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
