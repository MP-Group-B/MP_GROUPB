package com.example.testtp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BusstopSearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BusstopSearchFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private static final String TAG = "BusstopSearchFragment";
    private EditText search;
    private String busStopNm;
    RecyclerView recyclerView;
    BusStopDataAdapter adapter;
    /*private Handler mainHandler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            // 메시지를 처리하는 코드
            Log.d("HandlerCallback", "Message received: " + msg.what);
            return true; // 메시지가 처리되었음을 나타냄
        }
    });*/
    private Handler mainHandler = new Handler(Looper.getMainLooper());

    public BusstopSearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BusstopSearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BusstopSearchFragment newInstance(String param1, String param2) {
        BusstopSearchFragment fragment = new BusstopSearchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_busstop_search, container, false);

        List<BusStopData> busStopList = new ArrayList<BusStopData>();
        search = view.findViewById(R.id.busStopSearch);
        Button button1 = view.findViewById(R.id.button2);

        SharedViewModel sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        recyclerView = view.findViewById(R.id.BSSSearch);
        adapter = new BusStopDataAdapter(busStopList, new BusStopDataAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BusStopData busStopData) {
                Intent intent = new Intent(getActivity(), BusStopInfoActivity.class);
                intent.putExtra("busStopData", busStopData);
                startActivity(intent);
            }
        }, new BusStopDataAdapter.OnFavoriteClickListener() {
            @Override
            public void onFavoriteClick(BusStopData busStopData) {
                sharedViewModel.selectBusStopData(busStopData);
            }
        });

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                busStopList.clear();
                busStopNm = search.getText().toString();
                Log.d("BusstopSearchFragment", "Search text :" + busStopNm);
                if (busStopNm == null || busStopNm.isEmpty()) {
                    Log.d("BusstopSearchFragment", "Search text is null or empty"); // 디버그 로그 추가
                    return; // busNm이 null이거나 빈 문자열이면 스레드를 실행하지 않음
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Log.d("BusstopSearchFragment", "Thread started");
                            BusstopSearch busstopSearch = new BusstopSearch();
                            busStopList.addAll(busstopSearch.getBusStopId(busStopNm));
                            Log.d("BusstopSearchFragment", "Bus search completed. Results found: " + busStopList.size());

                            mainHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Log.d("BusstopSearchFragment", "Updating UI on main thread");
                                    adapter.updateBusStopList(busStopList);
                                }
                            });
                        }
                        catch (Exception e) {
                            Log.e(TAG, "Exception occurred", e);
                            mainHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Log.d("BusstopSearchFragment", "Exception occurred on main thread", e);
                                    Toast.makeText(getActivity(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }).start();
            }
        });

        return view;
    }
}