package com.example.testtp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class BusInfoActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private BusStopDataAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_info);

        TextView busNmTv = (TextView) findViewById(R.id.busNm);
        TextView busRegionTv = (TextView) findViewById(R.id.busRegion);

        Intent intent = getIntent();
        BusData busData = intent.getParcelableExtra("busData");

        if (busData != null) {
            Log.d("BusInfoActivity", "Received busData: " + busData.getBusNm() + ", " + busData.getBusRegion());
        } else {
            Log.e("BusInfoActivity", "busData is null");
        }
        List<BusStopData> busStopList = new ArrayList<BusStopData>();
        recyclerView = (RecyclerView) findViewById(R.id.busVisit);
        if(busData != null){
            busNmTv.setText(busData.getBusNm());
            busRegionTv.setText(busData.getBusRegion());

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        Log.d("BusInfoActivity", "Thread started");
                        BusSearch busSearch = new BusSearch();

                        String base = busData.getBase();
                        Log.d("BusInfoActivity", "Base: " + base);

                        if(busData.getBase().equals("Seoul"))
                            busStopList.addAll(busSearch.getStationsByRouteList(busData.getBusId()));
                        else if(busData.getBase().equals("Gyeonggi"))
                            busStopList.addAll(busSearch.getStationByRouteList2(busData.getBusId()));
                        Log.d("BusInfoActivity", "BusStop search completed. Results found: " + busStopList.size());

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.d("BusSearchFragment", "Updating UI on main thread");
                                adapter = new BusStopDataAdapter(busStopList, null, null);
                                recyclerView.setAdapter(adapter);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            }
                        });
                    }catch(Exception e){
                        Log.e("BusInfoActivity", "Exception in thread", e);
                        e.printStackTrace();
                    }
                }
            }).start();
        }else {
            Log.e("BusInfoActivity", "busData is null, not starting thread");
        }
    }
}