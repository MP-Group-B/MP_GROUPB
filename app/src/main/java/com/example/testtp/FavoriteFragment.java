package com.example.testtp;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FavoriteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavoriteFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private RecyclerView recyclerView, recyclerView2;
    private List<BusData> favoriteBusList = new ArrayList<>();
    private List<BusStopData> favoriteBusStopList = new ArrayList<>();
    private BusDataAdapter adapter;
    private BusStopDataAdapter sTadapter;
    SharedViewModel sharedViewModel;

    public FavoriteFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FavoriteFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FavoriteFragment newInstance(String param1, String param2) {
        FavoriteFragment fragment = new FavoriteFragment();
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
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);

        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        recyclerView = view.findViewById(R.id.recyclerBus);
        adapter = new BusDataAdapter(favoriteBusList, new BusDataAdapter.OnItemClickListener(){
            @Override
            public void onItemClick(BusData busData){
                Intent intent = new Intent(getActivity(), BusInfoActivity.class);
                intent.putExtra("busData", busData);
                startActivity(intent);
            }
        }, new BusDataAdapter.OnFavoriteClickListener(){
            @Override
            public void onFavoriteClick(BusData busData){
                removeFavorite(busData);
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerView2 = view.findViewById(R.id.recyclerBusstop);
        sTadapter = new BusStopDataAdapter(favoriteBusStopList, new BusStopDataAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BusStopData busStopData) {
                Intent intent = new Intent(getActivity(), BusStopInfoActivity.class);
                intent.putExtra("busStopData", busStopData);
                startActivity(intent);
            }
        }, new BusStopDataAdapter.OnFavoriteClickListener() {
            @Override
            public void onFavoriteClick(BusStopData busStopData) {
                removeFavoriteSt(busStopData);
            }
        });
        recyclerView2.setAdapter(sTadapter);
        recyclerView2.setLayoutManager(new LinearLayoutManager(getContext()));

        sharedViewModel.getSelectedBusData().observe(getViewLifecycleOwner(), new Observer<BusData>(){
            @Override
            public void onChanged(BusData busData){
                addFavorite(busData);
            }
        });

        sharedViewModel.getSelectedBusStopData().observe(getViewLifecycleOwner(), new Observer<BusStopData>(){
            @Override
            public void onChanged(BusStopData busStopData){
                addFavoriteSt(busStopData);
            }
        });

        return view;
    }

    private void addFavorite(BusData busData) {
        favoriteBusList.add(busData);
        removeDuplicates(favoriteBusList);
        adapter.updateBusList(favoriteBusList);
    }

    private void addFavoriteSt(BusStopData busStopData) {
        favoriteBusStopList.add(busStopData);
        removeDuplicateSts(favoriteBusStopList);
        sTadapter.updateBusStopList(favoriteBusStopList);
    }

    private void removeFavorite(BusData busData){
        favoriteBusList.remove(busData);
        adapter.updateBusList(favoriteBusList);
    }

    private void removeFavoriteSt(BusStopData busStopData){
        favoriteBusStopList.remove(busStopData);
        sTadapter.updateBusStopList(favoriteBusStopList);
    }

    public static void removeDuplicates(List<BusData> busList) {
        Set<String> busIdSet = new HashSet<>();
        Iterator<BusData> iterator = busList.iterator();

        while (iterator.hasNext()) {
            BusData busData = iterator.next();
            if (!busIdSet.add(busData.getBusId())) {
                iterator.remove();
            }
        }
    }

    /**
     * busStopList 안에 있는 중복된 항목들을 stationID를 이용해 제거하는 method
     * @param busStopList - 여러 버스 정류장의 정보들을 담은 arrayList
     */
    public static void removeDuplicateSts(List<BusStopData> busStopList) {
        Set<String> busStopIdSet = new HashSet<>();
        Iterator<BusStopData> iterator = busStopList.iterator();

        while (iterator.hasNext()) {
            BusStopData busStopData = iterator.next();
            if (!busStopIdSet.add(busStopData.getStation())) {
                iterator.remove();
            }
        }
    }
}