package com.example.testtp;

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

public class BusstopSearch {
    private final String TAG = "myTag";

    // API에 접속할 때 사용하는 인증키 - 지금은 드러나 있지만 나중에 보이지 않게 숨길 필요 있음
    private final String key = "arTA9sww86xXXS5bpcn8D22VvU8gP8BaoududbcQxILXg4D2ZuJHrultQ9D8yyNscBxy6JizMIS38urqGFZOcQ%3D%3D";
    private final String baseURL = "http://ws.bus.go.kr/api/rest";
    private URL url;
    private InputStream is;
    private XmlPullParserFactory factory;
    private XmlPullParser xpp;
    private String tag;
    private int eventType;

    /**
     * parameter를 이름에 포함하는 정류장을 (서울) API를 이용하여 검색하는 method
     * @param busstopNm - 버스 정류장 명
     * @return  - busstopNm을 포함하는 정류장 명을 가진 정류장 list들
     * @throws UnsupportedEncodingException
     */
    public List<BusStopData> getBusStopId(String busstopNm)throws UnsupportedEncodingException {
        String bssnEncode = URLEncoder.encode(busstopNm);
        String busStopIDUrl = baseURL + "/stationinfo/getStationByName?serviceKey="
                + key + "&stSrch=" +bssnEncode;
        Log.d(TAG,"정류장 명 -> 정류장 ID: " + busStopIDUrl);
        List<BusStopData> busStopList = new ArrayList<BusStopData>();

        try{
            setUrlNParser(busStopIDUrl);
            BusStopData busStopData = null;

            while(eventType != XmlPullParser.END_DOCUMENT){
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
                                busStopData.setBase("Seoul");
                            }else if(busStopData != null){
                                switch (tag){
                                    case "arsId":
                                        xpp.next();
                                        busStopData.setStationNo(xpp.getText());
                                        break;
                                    case "stId":
                                        xpp.next();
                                        busStopData.setStation(xpp.getText());
                                        break;
                                    case "stNm":
                                        xpp.next();
                                        busStopData.setStationNm(xpp.getText());
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
                        if(tag.equals("itemList")){
                            busStopList.add(busStopData);
                            busStopData = null;
                        }
                        break;
                }
                eventType = xpp.next();
            }
        }catch(Exception e){e.printStackTrace();}
        List<BusStopData> busStopList2 = getBusStopId2(busstopNm);
        List<BusStopData> mergeList = new ArrayList<>();
        mergeList.addAll(busStopList2);
        mergeList.addAll(busStopList);

        removeDuplicates(mergeList);
        mergeList = removeBypassingStops(mergeList);
        return mergeList;
    }

    /**
     * parameter를 이름에 포함하는 정류장을 (경기) API를 이용하여 검색하는 method
     * @param busstopNm - 버스 정류장 명
     * @return  - busstopNm을 포함하는 정류장 명을 가진 정류장 list들
     * @throws UnsupportedEncodingException
     */
    private List<BusStopData> getBusStopId2(String busstopNm)throws UnsupportedEncodingException{
        String bssnEncode = URLEncoder.encode(busstopNm);
        String busStopIDUrl = "http://apis.data.go.kr/6410000/busstationservice/getBusStationList?serviceKey="
                + key + "&keyword=" + bssnEncode;
        Log.d(TAG,"버스명 -> 버스 ID : " + busStopIDUrl);
        List<BusStopData> busStopList = new ArrayList<>();

        try{
            setUrlNParser(busStopIDUrl);
            BusStopData busStopData = null;
            while(eventType != XmlPullParser.END_DOCUMENT){
                switch (eventType){
                    case XmlPullParser.START_DOCUMENT:
                        Log.d(TAG, "XML parsing started");
                        break;
                    case XmlPullParser.START_TAG:
                        Log.d(TAG, "START_TAG: " + tag);
                        tag = xpp.getName();
                        if(tag.equals("busStationList")){
                            Log.d(TAG, "Found busStationList tag");
                            busStopData = new BusStopData();
                            busStopData.setBase("Gyeonggi");
                        }else if(busStopData != null){
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
                                default:
                                    break;
                            }
                        }
                        break;
                    case XmlPullParser.TEXT:
                        break;
                    case XmlPullParser.END_TAG:
                        tag = xpp.getName();
                        if(tag.equals("busStationList")){
                            busStopList.add(busStopData);
                            busStopData = null;
                        }
                        break;
                }
                eventType = xpp.next();
            }
        }catch (Exception e){e.printStackTrace();}
        return busStopList;
    }

    /**
     * 버스 정류장에 정차하는 버스들의 list 목록을 검색하는 method(서울 API)
     * @param StNo - 버스 정류장의 No.(arsId)
     * @return  - 해당 정류장에 정류하는 버스들의 list
     * @throws UnsupportedEncodingException
     */
    public List<BusData> getStationUid(String StNo) throws UnsupportedEncodingException{
        String bssnEncode = URLEncoder.encode(StNo);
        String stUidUrl = baseURL + "/stationinfo/getStationByUid?serviceKey="
                + key + "&arsId=" + bssnEncode;
        Log.d(TAG,"정류장 No.(ArsId) -> 경유 노선 Uid: " + stUidUrl);
        List<BusData> busList = new ArrayList<>();

        try{
            setUrlNParser(stUidUrl);
            BusData busData = null;

            while(eventType != XmlPullParser.END_DOCUMENT){
                switch(eventType){
                    case XmlPullParser.START_DOCUMENT:
                        Log.d(TAG, "XML parsing started");
                        break;
                    case XmlPullParser.START_TAG:
                        tag = xpp.getName();
                        Log.d(TAG, "START_TAG: " + tag);
                        if(tag.equals("itemList")){
                            Log.d(TAG, "Found itemList tag");
                            busData = new BusData();
                        }else if(busData != null){
                            switch(tag){
                                case "arrmsg1":
                                    xpp.next();
                                    busData.setArrvmsg1(xpp.getText());
                                    break;
                                case "arrmsg2":
                                    xpp.next();
                                    busData.setArrvmsg2(xpp.getText());
                                    break;
                                case "busRouteAbrv":
                                    xpp.next();
                                    busData.setBusNm(xpp.getText());
                                    break;
                                case "busRouteId":
                                    xpp.next();
                                    busData.setBusId(xpp.getText());
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
                        if(tag.equals("itemList")){
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
     * 버스 정류장에 정차하는 버스들의 list를 검색하는 method (경기 API)
     * @param stId - 버스 정류장의 ID (stationId)
     * @return  - 정류장 ID로 검색한 버스들의 arayList
     */
    public List<BusData> getBusStViaRouteList(String stId){
        String bssnEncode = URLEncoder.encode(stId);
        String stBusUidUrl = "http://apis.data.go.kr/6410000/busstationservice/getBusStationViaRouteList?serviceKey="
                + key + "&stationId=" + bssnEncode;
        Log.d(TAG,"정류장 ID.(stationId) -> 경유 노선 Uid: " + stBusUidUrl);
        List<BusData> busList = new ArrayList<>();

        try{
            setUrlNParser(stBusUidUrl);
            BusData busData = null;

            while(eventType != XmlPullParser.END_DOCUMENT){
                switch (eventType){
                    case XmlPullParser.START_DOCUMENT:
                        Log.d(TAG, "XML parsing started");
                        break;
                    case XmlPullParser.START_TAG:
                        tag = xpp.getName();
                        Log.d(TAG, "START_TAG: " + tag);
                        if(tag.equals("busRouteList")){
                            Log.d(TAG, "Found busRouteList tag");
                            busData = new BusData();
                        }else if(busData != null){
                            switch(tag){
                                case "routeId":
                                    xpp.next();
                                    busData.setBusId(xpp.getText());
                                    break;
                                case "routeName":
                                    xpp.next();
                                    busData.setBusNm(xpp.getText());
                                    break;
                                case "staOrder":
                                    xpp.next();
                                    busData.setStOrder(xpp.getText());
                                    break;
                            }
                            break;
                        }
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
        }catch(Exception e){ e.printStackTrace();}
        for (BusData busData : busList)
            getBusArrItem(busData, stId);
        return busList;
    }

    /**
     * 버스 정류장과 버스 노선 ID, 해당 노선 정류장의 순서를 통해 버스의 정류장 도착 예정 시간을 받는 method (경기 API)
     * @param busData - 정류장과 함께 검색할 버스 data
     * @param stId  - 정류장 ID
     */
    private void getBusArrItem(BusData busData, String stId){
        String stIdEncode = URLEncoder.encode(stId);
        String routeIdEncode = URLEncoder.encode(busData.getBusId());
        String stOrderEncode = URLEncoder.encode(busData.getStOrder());
        String busArrItmUrl = "http://apis.data.go.kr/6410000/busarrivalservice/getBusArrivalItem?serviceKey="
                + key + "&stationId=" + stIdEncode
                + "&routeId=" + routeIdEncode
                + "&staOrder=" + stOrderEncode;
        Log.d(TAG,"정류장 ID.(stationId) -> 경유 노선 Uid: " + busArrItmUrl);

        try{
            setUrlNParser(busArrItmUrl);

            while(eventType!=XmlPullParser.END_DOCUMENT){
                switch (eventType){
                    case XmlPullParser.START_DOCUMENT:
                        Log.d(TAG, "XML parsing started");
                        break;
                    case XmlPullParser.START_TAG:
                        tag = xpp.getName();
                        Log.d(TAG, "START_TAG: " + tag);
                        if(tag.equals("busArrivalItem"))
                            Log.d(TAG, "Found burArrivalItem tag");
                            switch(tag){
                                case "predictTime1":
                                    xpp.next();
                                    if(xpp.getText() != null && !xpp.getText().isEmpty())
                                        busData.setArrvmsg1(xpp.getText() +"분 후");
                                    break;
                                case "predictTime2":
                                    xpp.next();
                                    if(xpp.getText() != null && !xpp.getText().isEmpty())
                                        busData.setArrvmsg2(xpp.getText() +"분 후");
                                    break;
                            }
                            break;
                    case XmlPullParser.TEXT:
                        break;
                    case XmlPullParser.END_TAG:
                        tag =xpp.getName();
                        break;
                }
                eventType = xpp.next();
            }
        }catch(Exception e){e.printStackTrace();}
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
     * busStopList 안에 있는 중복된 항목들을 stationID를 이용해 제거하는 method
     * @param busStopList - 여러 버스 정류장의 정보들을 담은 arrayList
     */
    public static void removeDuplicates(List<BusStopData> busStopList) {
        Set<String> busStopIdSet = new HashSet<>();
        Iterator<BusStopData> iterator = busStopList.iterator();

        while (iterator.hasNext()) {
            BusStopData busStopData = iterator.next();
            if (!busStopIdSet.add(busStopData.getStation())) {
                iterator.remove();
            }
        }
    }

    /**
     * "(경유)"라는 단어를 포함하는 정류장을 list에서 제거하는 method
     * @param busStopList - 버스 정류장들을 항목으로 가지는 arraylist
     * @return  - parameter로 받은 busStopList를 그대로 보냄
     */
    public List<BusStopData> removeBypassingStops(List<BusStopData> busStopList) {
        List<BusStopData> filteredList = new ArrayList<>();
        for (BusStopData busStopData : busStopList) {
            if (!busStopData.getStationNm().contains("(경유)")) {
                filteredList.add(busStopData);
            }
        }
        return filteredList;
    }
}
