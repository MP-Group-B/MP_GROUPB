package com.example.testtp;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {
    private final MutableLiveData<BusData> selectedBusData = new MutableLiveData<BusData>();
    private final MutableLiveData<BusStopData> selectedBusStopData = new MutableLiveData<BusStopData>();
    public void selectBusData(BusData busData){
        selectedBusData.setValue(busData);
    }

    public void selectBusStopData(BusStopData busStopData){
        selectedBusStopData.setValue(busStopData);
    }

    public LiveData<BusData> getSelectedBusData(){
        return selectedBusData;
    }

    public LiveData<BusStopData> getSelectedBusStopData(){
        return selectedBusStopData;
    }


}
