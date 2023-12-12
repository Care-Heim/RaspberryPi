package org.example;

import org.json.simple.JSONArray;

public class ClothData{
    // 서버에서 전송한 데이터 저장할 변수
    static String success;

    static JSONArray statuses;

    //body
//    static Long type;
//    static Long pattern; // 패턴
//
//    static String cType;
//    static String cPattern;
//
//    static JSONArray colors; // 색
//    static JSONArray features; // 특징 : 추가 정보
//
//    static JSONArray careInfos; // 케어라벨 정보
//    static boolean hasStain; // 오염 여부
//    static boolean canDetectStain; // 오염 탐지 가능 여부


    public static String TYPE(int t) {
//        int t = Integer.parseInt(type2);
        String type = null;

        if(t==0)
            type="반소매 상의";
        else if(t==1)
            type="긴소매 상의";
        else if(t==2)
            type="반소매 외투";
        else if(t==3)
            type="긴소매 외투";
        else if(t==4)
            type="조끼";
        else if(t==5)
            type="민소매";
        else if(t==6)
            type="반바지";
        else if(t==7)
            type="긴바지";
        else if(t==8)
            type="치마";
        else if(t==9)
            type="반소매 원피스";
        else if(t==10)
            type="긴소매 원피스";
        else if(t==11)
            type="민소매 원피스";

        return type;
    }

    public static String PATTERN(int p) {
        //int p = Integer.parseInt(a);
        String pattern = null;

        if(p==0)
            pattern="동물 얼룩 무늬";
        else if(p==1)
            pattern="체크 무늬";
        else if(p==2)
            pattern="지그재그 무늬";
        else if(p==3)
            pattern="마름모 무늬";
        else if(p==4)
            pattern="꽃무늬";
        else if(p==5)
            pattern="그림이 그려져 있는 무늬";
        else if(p==6)
            pattern="글씨가 쓰여 있는 무늬";
        else if(p==7)
            pattern="민무늬";
        else if(p==8)
            pattern="땡땡이 무늬";
        else if(p==9)
            pattern="줄무늬";
        else if(p==10)
            pattern="인식할 수 없는";

        return pattern;
    }
}