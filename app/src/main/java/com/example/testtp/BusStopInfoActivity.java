package com.example.testtp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class BusStopInfoActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    StBusAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_stop_info);

        TextView stNm = (TextView) findViewById(R.id.StNm);
        TextView stNo = (TextView) findViewById(R.id.StNo);
        Intent intent = getIntent();
        BusStopData busStopData = intent.getParcelableExtra("busStopData");
        Button button1 = (Button) findViewById(R.id.research);

        if (busStopData != null) {
            Log.d("BusStopInfoActivity", "Received busStopData: " + busStopData.getStationNm() + ", " + busStopData.getStationNo());
        } else {
            Log.e("BusStopInfoActivity", "busStopData is null");
        }

        List<BusData> busList = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.StAtBus);

        if(busStopData != null){
            stNm.setText(busStopData.getStationNm());
            stNo.setText(busStopData.getStationNo());

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        Log.d("BusStopInfoActivity", "Thread started");
                        BusstopSearch busstopSearch = new BusstopSearch();
                        Log.d("BusStopInfoActivity", "Base: " + busStopData.getBase());
                        if(busStopData.getBase().equals("Seoul"))
                            busList.addAll(busstopSearch.getStationUid(busStopData.getStationNo()));
                        else if(busStopData.getBase().equals("Gyeonggi"))
                            busList.addAll(busstopSearch.getBusStViaRouteList(busStopData.getStation()));
                        Log.d("BusStopInfoActivity", "BusStop search completed. Results found: " + busList.size());

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.d("BusstopSearchFragment", "Updating UI on main thread");
                                adapter = new StBusAdapter(busList);
                                recyclerView.setAdapter(adapter);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            }
                        });
                    }catch(Exception e){
                        Log.e("BusStopInfoActivity", "Exception in thread", e);
                        e.printStackTrace();
                    }
                }
            }).start();
        }

        button1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(busStopData != null) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Log.d("BusStopInfoActivity", "After clicking button, Thread started");
                                BusstopSearch busstopSearch = new BusstopSearch();
                                busList.clear();
                                if (busStopData.getBase().equals("Seoul"))
                                    busList.addAll(busstopSearch.getStationUid(busStopData.getStationNo()));
                                else if (busStopData.getBase().equals("Gyeonggi"))
                                    busList.addAll(busstopSearch.getBusStViaRouteList(busStopData.getStation()));
                                Log.d("BusStopInfoActivity", "BusStop research completed. Results found: " + busList.size());

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Log.d("BusStopInfoActivity", "Updating UI on main thread");
                                        adapter.updateBusList(busList);
                                    }
                                });
                            }catch(Exception e){e.printStackTrace();}
                        }
                    }).start();
                }
            }
        });
    }
}