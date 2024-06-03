package com.example.testtp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.MapView;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.NaverMapSdk;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.util.FusedLocationSource;

public class HotplaceActivity extends AppCompatActivity implements OnMapReadyCallback {
    private ImageView trafficBtn, weatherBtn, homeBtn, calendarBtn, hotplaceBtn;
    private MapView mapView;
    private NaverMap naverMap;
    private FusedLocationSource locationSource;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotplace);

        init();
        trafficBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HotplaceActivity.this, TrafficActivity.class);
                startActivity(intent);
                finish();
            }
        });

        weatherBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(HotplaceActivity.this, WeatherActivity.class);
                startActivity(intent);
                finish();
            }
        });

        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HotplaceActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        calendarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HotplaceActivity.this, CalendarActivity.class);
                startActivity(intent);
                finish();
            }
        });

        hotplaceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HotplaceActivity.this, HotplaceActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // NaverMap SDK 초기화
        NaverMapSdk.getInstance(this).setClient(
                new NaverMapSdk.NaverCloudPlatformClient("4b6gi1t9ym")
        );

        // MapFragment 초기화 및 설정
        MapFragment mapFragment = (MapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_fragment);
        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.map_fragment, mapFragment)
                    .commit();
        }

        mapFragment.getMapAsync(this);

        // 위치 소스 초기화
        locationSource = new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        naverMap.setLocationSource(locationSource);
        naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);
    }
    public void init(){
        trafficBtn = (ImageView) findViewById(R.id.trafficBtn);
        weatherBtn = (ImageView) findViewById(R.id.weatherBtn);
        homeBtn = (ImageView) findViewById(R.id.homeBtn);
        calendarBtn = (ImageView) findViewById(R.id.calendarBtn);
        hotplaceBtn = (ImageView) findViewById(R.id.hotplaceBtn);
    }
}
