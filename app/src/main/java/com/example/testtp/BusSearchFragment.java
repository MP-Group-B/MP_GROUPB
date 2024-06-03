package com.example.testtp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;


import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BusSearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BusSearchFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "BusSearchFragment";
    private Handler mainHandler = new Handler(Looper.getMainLooper());
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private EditText search;
    private String busNm;
    private RecyclerView recyclerView;
    private BusDataAdapter adapter;

    public BusSearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BusSearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BusSearchFragment newInstance(String param1, String param2) {
        BusSearchFragment fragment = new BusSearchFragment();
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
        View view = inflater.inflate(R.layout.fragment_bus_search, container, false);

        List<BusData> busList = new ArrayList<BusData>();
        search = view.findViewById(R.id.busSearch);
        Button button = view.findViewById(R.id.button1);

        SharedViewModel sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        recyclerView = view.findViewById(R.id.BSearch);
        adapter = new BusDataAdapter(busList, new BusDataAdapter.OnItemClickListener(){
            @Override
            public void onItemClick(BusData busData){
                Intent intent = new Intent(getActivity(), BusInfoActivity.class);
                intent.putExtra("busData", busData);
                startActivity(intent);
            }
        }, new BusDataAdapter.OnFavoriteClickListener(){
            @Override
            public void onFavoriteClick(BusData busData){
                sharedViewModel.selectBusData(busData);
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        if (search == null) {
            Log.e("BusSearchFragment", "EditText search is null");
        } else {
            Log.d("BusSearchFragment", "EditText search found");
        }

        if (button == null) {
            Log.e("BusSearchFragment", "Button button1 is null");
        } else {
            Log.d("BusSearchFragment", "Button button1 found");
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                busList.clear();
                busNm = search.getText().toString();
                Log.d("BusSearchFragment", "Search text :" + busNm);
                if (busNm == null || busNm.isEmpty()) {
                    Log.d("BusSearchFragment", "Search text is null or empty"); // 디버그 로그 추가
                    return; // busNm이 null이거나 빈 문자열이면 스레드를 실행하지 않음
                }
                new Thread(new Runnable(){
                    @Override
                    public void run(){
                        try{
                            Log.d("BusSearchFragment", "Thread started");
                            BusSearch busSearch = new BusSearch();
                            busList.addAll(busSearch.getBusID(busNm));
                            Log.d("BusSearchFragment", "Bus search completed. Results found: " + busList.size());

                        mainHandler.post(new Runnable(){
                           @Override
                           public void run(){
                               Log.d("BusSearchFragment", "Updating UI on main thread");
                               adapter.updateBusList(busList);
                           }
                        });
                        }catch(UnsupportedEncodingException e){
                            Log.e("BusSearchFragment", "Encoding exception occurred", e);
                            mainHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getActivity(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }catch (Exception e) {
                            Log.e(TAG, "Exception occurred", e);
                            mainHandler.post(new Runnable() {
                                @Override
                                public void run() {
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