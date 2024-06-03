package com.example.testtp;

import android.os.Bundle;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class BusSearch {
    private final String TAG = "myTag";

    // API에 접속할 때 사용하는 인증키 - 지금은 드러나 있지만 나중에 보이지 않게 숨길 필요 있음
    private final String key = "arTA9sww86xXXS5bpcn8D22VvU8gP8BaoududbcQxILXg4D2ZuJHrultQ9D8yyNscBxy6JizMIS38urqGFZOcQ%3D%3D";
    private final String baseURL = "http://ws.bus.go.kr/api/rest";

    // url parsing 할 때 쓸 전역 변수들
    private URL url;
    private InputStream is;
    private XmlPullParserFactory factory;
    private XmlPullParser xpp;
    private String tag;
    private int eventType;

    public BusSearch(){}

    /**
     * 검색하고 싶은 버스(서울)명을 parameter로 받아서 busId를 받아오는 method
     * @param busNm - Bus name that you want to search
     * @return
     * @throws UnsupportedEncodingException
     */
    public List<BusData> getBusID(String busNm) throws UnsupportedEncodingException{
        String bssnEncode = URLEncoder.encode(busNm);
        String busIDURL = baseURL + "/busRouteInfo/getBusRouteList?serviceKey=" +
                key + "&strSrch=" + bssnEncode;
        Log.d(TAG,"버스명 -> 버스 ID : " + busIDURL);
        List<BusData> busList = new ArrayList<BusData>();

        try{
            setUrlNParser(busIDURL);
            BusData busData = null;
            while(eventType!=XmlPullParser.END_DOCUMENT){
                switch(eventType){
                    case XmlPullParser.START_DOCUMENT:
                        Log.d(TAG, "XML parsing started");
                        break;
                    case XmlPullParser.START_TAG:
                        tag = xpp.getName();
                        Log.d(TAG, "START_TAG: " + tag);  // 현재 태그를 로그로 출력
                        if (tag.equals("itemList")) {
                            Log.d(TAG, "Found itemList tag");
                            busData = new BusData();
                            busData.setBase("Seoul");
                        }else if(busData != null){
                            switch(tag) {
                                case "busRouteAbrv":
                                    xpp.next();
                                    busData.setBusNm(xpp.getText());
                                    break;
                                case "busRouteId":
                                    xpp.next();
                                    busData.setBusId(xpp.getText());
                                    break;
                                case "corpNm":
                                    xpp.next();
                                    busData.setRegion(xpp.getText());
                                    break;
                                default:
                                    break;
                            }
                        }
                        break;
                    case XmlPullParser.TEXT:
                        break;
                    case XmlPullParser.END_TAG:
                        tag = xpp.getName();
                        if(tag.equals("itemList")) {
                            busList.add(busData);
                            busData = null;
                        }
                        break;
                }
                eventType = xpp.next();
            }
        }catch(Exception e){e.printStackTrace();}
        List<BusData> busList2 = getBusID2(busNm);
        List<BusData> mergeList = new ArrayList<BusData>();
        mergeList.addAll(busList2);
        mergeList.addAll(busList);
        removeDuplicates(mergeList);
        return mergeList;
    }

    /**
     * 검색하고 싶은 버스(경기)명을 parameter로 받아서 busId를 받아오는 method
     * @param busNm - Bus name that you want to search
     * @return 검색어를 포함한 버스들의 data를 arraylist로 만들어 return
     * @throws UnsupportedEncodingException
     */
    private List<BusData> getBusID2(String busNm) throws UnsupportedEncodingException{
        String bssnEncode = URLEncoder.encode(busNm);
        String busIDURL = "http://apis.data.go.kr/6410000/busrouteservice/getBusRouteList?serviceKey=" +
                key + "&keyword=" + bssnEncode;
        Log.d(TAG,"버스명 -> 버스 ID : " + busIDURL);
        List<BusData> busList = new ArrayList<>();

        try{
            setUrlNParser(busIDURL);
            BusData busData = null;
            while(eventType!=XmlPullParser.END_DOCUMENT){
                switch(eventType){
                    case XmlPullParser.START_DOCUMENT:
                        Log.d(TAG, "XML parsing started");
                        break;
                    case XmlPullParser.START_TAG:
                        Log.d(TAG, "START_TAG: " + tag);
                        tag = xpp.getName();

                        if(tag.equals("busRouteList")){
                            Log.d(TAG, "Found busRouteList tag");
                            busData = new BusData();
                            busData.setBase("Gyeonggi");
                        }else if(busData != null) {
                            switch (tag) {
                                case "regionName":
                                    xpp.next();
                                    busData.setRegion(xpp.getText());
                                    break;
                                case "routeId":
                                    xpp.next();
                                    busData.setBusId(xpp.getText());
                                    break;
                                case "routeName" :
                                    xpp.next();
                                    busData.setBusNm(xpp.getText());
                                    break;
                                default:
                                    break;
                            }
                        }
                        break;
                    case XmlPullParser.TEXT:
                        break;
                    case XmlPullParser.END_TAG:
                        tag = xpp.getName();
                        if(tag.equals("busRouteList")){
                            busList.add(busData);
                            busData = null;
                        }
                        break;
                }
                eventType = xpp.next();
            }
        }catch(Exception e){e.printStackTrace();}
        return busList;
    }

    /**
     * param으로 받은 busID(서울 API)를 이용하여 해당 노선이 경유하는 정류장 목록들을 검색해 return 하는 method
     * @param busId - bus가 가진 고유 ID
     * @return 버스 ID를 통해 검색한 버스가 경유하는 정류장들을 arrayList로 만들어 return
     * @throws UnsupportedEncodingException
     */
    public List<BusStopData> getStationsByRouteList(String busId) throws UnsupportedEncodingException{
        String bssnEncode = URLEncoder.encode(busId);
        String busStopURL = baseURL + "/busRouteInfo/getStaionByRoute?ServiceKey="
                + key
                + "&busRouteId=" + bssnEncode;
        Log.d(TAG,"버스 ID -> 버스 정류장 : " + busStopURL);
        List<BusStopData> busStopList = new ArrayList<BusStopData>();
        try{
            setUrlNParser(busStopURL);
            BusStopData busStopData = null;
            while(eventType!=XmlPullParser.END_DOCUMENT){
                switch(eventType){
                    case XmlPullParser.START_DOCUMENT:
                        Log.d(TAG, "XML parsing started");
                        break;
                    case XmlPullParser.START_TAG:
                        tag = xpp.getName();
                        Log.d(TAG, "START_TAG: " + tag);
                        if(tag.equals("itemList")){
                            Log.d(TAG, "Found itemList tag");
                            busStopData = new BusStopData();
                        }else if(busStopData != null){
                            switch (tag){
                                case "direction":
                                    xpp.next();
                                    busStopData.setDirection(xpp.getText());
                                    break;
                                case "station":
                                    xpp.next();
                                    busStopData.setStation(xpp.getText());
                                    break;
                                case "stationNm":
                                    xpp.next();
                                    busStopData.setStationNm(xpp.getText());
                                    break;
                                case "stationNo":
                                    xpp.next();
                                    busStopData.setStationNo(xpp.getText());
                                    break;
                                case "transYn":
                                    xpp.next();
                                    busStopData.setTransYn(xpp.getText());
                                    break;
                                default:
                                    break;
                            }
                        }
                        break;
                    case XmlPullParser.TEXT:
                        break;
                    case XmlPullParser.END_TAG:
                        tag = xpp.getName();
                        if(tag.equals("itemList")) {
                            busStopList.add(busStopData);
                            busStopData = null;
                        }
                        break;
                }
                eventType = xpp.next();
            }
        }catch(Exception e){e.printStackTrace();}
        return busStopList;
    }

    /**
     * 버스 고유 ID를 통해 해당 버스가 경유하는 정류장들을 탐색하는 method
     * @param busId - 버스 고유 ID
     * @return 버스가 경유하는 버스 정류장 목록
     * @throws UnsupportedEncodingException
     */
    public List<BusStopData> getStationByRouteList2(String busId) throws UnsupportedEncodingException{
        String bssnEncode = URLEncoder.encode(busId);
        String busStopURL = "http://apis.data.go.kr/6410000/busrouteservice/getBusRouteStationList?servicekey="
                + key + "&routeId=" + bssnEncode;
        Log.d(TAG,"버스 ID -> 버스 정류장 : " + busStopURL);
        List<BusStopData> busStopList = new ArrayList<BusStopData>();
        try {
            setUrlNParser(busStopURL);
            BusStopData busStopData = null;
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        Log.d(TAG, "XML parsing started");
                        break;
                    case XmlPullParser.START_TAG:
                        tag = xpp.getName();
                        Log.d(TAG, "START_TAG: " + tag);
                        if (tag.equals("busRouteStationList")) {
                            Log.d(TAG, "Found busRouteStationList tag");
                            busStopData = new BusStopData();
                        } else if (busStopData != null) {
                                switch(tag){
                                    case "mobileNo":
                                        xpp.next();
                                        busStopData.setStationNo(xpp.getText());
                                        break;
                                    case "stationId":
                                        xpp.next();
                                        busStopData.setStation(xpp.getText());
                                        break;
                                    case "stationName":
                                        xpp.next();
                                        busStopData.setStationNm(xpp.getText());
                                        break;
                                    case "turnYn":
                                        xpp.next();
                                        busStopData.setTransYn(xpp.getText());
                                        break;
                                    default:
                                        break;
                                }
                                break;
                        }
                    case XmlPullParser.TEXT:
                        break;
                    case XmlPullParser.END_TAG:
                        tag = xpp.getName();
                        if(tag.equals("busRouteStationList")) {
                            busStopList.add(busStopData);
                            busStopData = null;
                        }
                        break;
                }
                eventType = xpp.next();
            }
        }catch(Exception e){e.printStackTrace();}
        return busStopList;
    }

    /**
     * 인자로 받아온 URLquery를 통해 API에 접속하는 method
     * @param URLquery - URL
     */
    private void setUrlNParser(String URLquery) {
        try{
            url = new URL(URLquery);
            is = url.openStream();

            factory = XmlPullParserFactory.newInstance();
            xpp = factory.newPullParser();
            xpp.setInput(new InputStreamReader(is, "UTF-8"));

            xpp.next();
            eventType = xpp.getEventType();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * busList 안에 있는 중복된 항목들을 busId를 이용해 제거하는 method
     * @param busList - 여러 버스의 정보들을 담은 arrayList
     */
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
}
