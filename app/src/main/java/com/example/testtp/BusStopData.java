package com.example.testtp;

import android.os.Parcel;
import android.os.Parcelable;

public class BusStopData implements Parcelable {
    private String direction, stationNm;
    private String station, stationNo;
    private String transYn, base;

    public BusStopData(){
        direction = stationNm = station = stationNo = transYn = null;
    }

    public String getDirection() { return direction; }
    public String getStationNm() { return stationNm; }
    public String getStation() { return station; }
    public String getStationNo() {return stationNo;}
    public String getTransYn() {return transYn;}
    public String getBase() {return base;}
    public void setDirection(String direction) { this.direction = direction; }
    public void setStationNm(String stationNm) { this.stationNm = stationNm; }
    public void setStation(String station) { this.station = station; }
    public void setStationNo(String stationNo) { this.stationNo = stationNo; }
    public void setTransYn(String transYn) { this.transYn = transYn; }
    public void setBase(String base) { this.base = base; }

    protected BusStopData(Parcel in){
        direction = in.readString();
        stationNm = in.readString();
        station = in.readString();
        stationNo = in.readString();
        transYn = in.readString();
        base = in.readString();
    }

    public static final Creator<BusStopData> CREATOR = new Creator<BusStopData>() {
        @Override
        public BusStopData createFromParcel(Parcel in) {
            return new BusStopData(in);
        }

        @Override
        public BusStopData[] newArray(int size) {
            return new BusStopData[size];
        }
    };

    @Override
    public int describeContents(){return 0;}

    @Override
    public void writeToParcel(Parcel parcel, int i){
        parcel.writeString(direction);
        parcel.writeString(stationNm);
        parcel.writeString(station);
        parcel.writeString(stationNo);
        parcel.writeString(transYn);
        parcel.writeString(base);
    }
}
