package com.example.testtp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.database.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class TrafficActivity extends AppCompatActivity {
    private ImageView trafficBtn, weatherBtn, homeBtn, calendarBtn, hotplaceBtn;

    private TabLayout mTabLayout;
    private ViewPager2 mViewPager;
    private PagerAdapter pagerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traffic);
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        mViewPager = (ViewPager2) findViewById(R.id.pager);

        pagerAdapter = new PagerAdapter(this);
        mViewPager.setAdapter(pagerAdapter);
        setViewPager();
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

    public void setViewPager(){
        final List<String> tabElement = Arrays.asList("즐겨찾기", "버스 검색", "정류장 검색");

        new TabLayoutMediator(mTabLayout, mViewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull @NotNull TabLayout.Tab tab, int position) {
                TextView textView = new TextView(TrafficActivity.this);
                textView.setText(tabElement.get(position));
                textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                tab.setCustomView(textView);
            }
        }).attach();

    }
}
