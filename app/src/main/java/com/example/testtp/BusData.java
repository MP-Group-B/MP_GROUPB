package com.example.testtp;

import android.os.Parcel;
import android.os.Parcelable;

public class BusData implements Parcelable {
    private String busNm, busId, region;    // 버스 명, 버스 ID, 해당 버스 관리 지역
    private String arrvmsg1, arrvmsg2;  // 정류장에 버스가 도착할 예정 시간
    private String stOrder, base;    // 사용한 api가 경기, 서울 인지를 나타 내는 변수

    public BusData(){
        busNm = busId = region = arrvmsg1 = arrvmsg2 = stOrder = base = null;
    }
    public void setBusNm(String data){
        this.busNm = data;
    }
    public void setBusId(String data){
        this.busId = data;
    }
    public void setRegion(String data){
        this.region = data;
    }
    public void setBase(String data){this.base = data;}
    public void setArrvmsg1(String arrvmsg1) { this.arrvmsg1 = arrvmsg1;}
    public void setArrvmsg2(String arrvmsg2) {this.arrvmsg2 = arrvmsg2; }
    public void setStOrder(String stOrder) { this.stOrder = stOrder; }
    public String getBusNm(){return this.busNm;}
    public String getBusId(){ return this.busId; }
    public String getBusRegion(){return this.region;}
    public String getBase(){return this.base;}
    public String getArrvmsg1() { return arrvmsg1; }
    public String getArrvmsg2() { return arrvmsg2; }
    public String getStOrder() { return stOrder; }
    protected BusData(Parcel in){
        busNm = in.readString();
        busId = in.readString();
        region = in.readString();
        arrvmsg1 = in.readString();
        arrvmsg2 = in.readString();
        stOrder = in.readString();
        base = in.readString();
    }

    public static final Creator<BusData> CREATOR = new Creator<BusData>(){
        @Override
        public BusData createFromParcel(Parcel in){
            return new BusData(in);
        }

        @Override
        public BusData[] newArray(int size){
            return new BusData[size];
        }
    };

    @Override
    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(busNm);
        parcel.writeString(busId);
        parcel.writeString(region);
        parcel.writeString(arrvmsg1);
        parcel.writeString(arrvmsg2);
        parcel.writeString(stOrder);
        parcel.writeString(base);
    }
}
