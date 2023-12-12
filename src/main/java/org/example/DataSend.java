package org.example;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.UUID;

public class DataSend {
    public static boolean isSuccess;
    public static int code;
    public static String message;
    public static JSONArray result;

    public static int type;
    public static int ptn;
    public static JSONArray colors;
    public static String color = "";
    public static JSONArray features;
    public static String feature = "";

    public static String nickName = "";
    /*	"canDetectStain" : true,
"hasStain" : true or false, // only when 'canDetectStain' is true
"status" : 1,
"careInfos" : ["손세탁", "표백 금지"] // only when 'status' is true*/

    public static String nickname;
    public static boolean canDetectStain;
    public static boolean hasStain;
    public static int status;
    public static JSONArray careInfos;
    public static String careInfo = "";

    public static void clothExtract(byte[] image) {
        // 요청을 보낼 uri 작성
        String uri = "http://119.192.42.243:10002/clothes/extract";

        // 응답을 주고 받을 template 생성
        RestTemplate restTemplate = new RestTemplate();

        // Header 설정
        HttpHeaders headers = new HttpHeaders();

        // Content-type 설정
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        // RequestPart를 담을 map 생성
        MultiValueMap<String, Object> multipartBody = new LinkedMultiValueMap<>();

        // image Part 추가
        multipartBody.add("image", new ByteArrayResource(image) {
            @Override
            public String getFilename() {
                return String.valueOf(UUID.randomUUID()); // 이미지 파일명 설정
            }
        });

        // 요청 json object 생성 / 값 넣기 - json.simple lib 사
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Content-Type", "multipart");

        // multipartBody에 json 추가
        multipartBody.add("form-data", jsonObject.toJSONString());

        // HttpEntity 설정
        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(multipartBody, headers);

        try {
            // Http 요청 - 응답을 String으로 받을 것 명시
            ResponseEntity<String> response = restTemplate.postForEntity(uri, entity, String.class);
            String resultGet = response.getBody(); // response의 body를 가져옴

            System.out.println(resultGet);

            /* 의류 정보 등록_Get */
            JSONParser parser = new JSONParser();
            // JSON 파싱
            JSONObject getjsonObject = (JSONObject) parser.parse(resultGet); //**

            /* 의류 정보 등록_Get */
            isSuccess = (boolean) getjsonObject.get("isSuccess");
            code = ((Long) getjsonObject.get("code")).intValue();
            message = (String) getjsonObject.get("message");
            result = (JSONArray) getjsonObject.get("result");
            for(int i=0;i<result.size();i++){
                JSONObject tmp = (JSONObject) result.get(i);//인덱스 번호로 접근해서 가져온다.

                type = ((Long) tmp.get("type")).intValue();
                ptn = ((Long) tmp.get("ptn")).intValue();
                colors = (JSONArray) tmp.get("colors");
                color = "";
                feature = "";
                for(int j=0; j<colors.size(); j++)
                    color += (String) colors.get(j);

                features = (JSONArray) tmp.get("features");
                if(features != null){
                    for(int j=0; j<features.size(); j++)
                        feature += (String)features.get(j);
                }
            }
        } catch (Exception e) {
            // Exception 처리 내용
        }
    }



    /* 의류 정보 추출 요청_Request */
    public static void clothEnroll(JSONObject jsonData) {
        // 웹 서버 URL 설정
        String serverUrl = "http://119.192.42.243:10002/clothes/enroll";
        try {
            System.out.println("request:"+jsonData);

            // HTTP 클라이언트 생성
            HttpClient client = HttpClient.newHttpClient();

            // HTTP 요청 생성
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(serverUrl))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonData.toString()))
                    .build();

            // HTTP 요청 보내기
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String resultGet = response.body(); // response의 body를 가져옴

            System.out.println("result :" + resultGet);
            /* 의류 정보 등록_Get */
            JSONParser parser = new JSONParser();
            // JSON 파싱
            JSONObject obj = (JSONObject) parser.parse(resultGet); //**
            /* 의류 정보 등록_Get */
            isSuccess = (boolean) obj.get("isSuccess");
            code = ((Long) obj.get("code")).intValue();
            message = (String) obj.get("message");

        } catch (IOException | InterruptedException | ParseException e) {
            e.printStackTrace();
        }
    }

    // image? : 읽어 온 이미지 파일의 byte array
    public static void ClothInfo(byte[] image) {
        // 요청을 보낼 uri 작성
        String uri = "http://119.192.42.243:10002/clothes/careInfos";

        // 응답을 주고 받을 template 생성
        RestTemplate restTemplate = new RestTemplate();

        // Header 설정
        HttpHeaders headers = new HttpHeaders();

        // Content-type 설정
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        // RequestPart를 담을 map 생성
        MultiValueMap<String, Object> multipartBody = new LinkedMultiValueMap<>();

        // image Part 추가
        multipartBody.add("image", new ByteArrayResource(image) {
            @Override
            public String getFilename() {
                return String.valueOf(UUID.randomUUID()); // 이미지 파일명 설정
            }
        });

        // 요청 json object 생성 / 값 넣기 - json.simple lib 사
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Content-Type", "multipart");

        // multipartBody에 json 추가
        multipartBody.add("form-data", jsonObject.toJSONString());

        // HttpEntity 설정
        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(multipartBody, headers);

        try {
            // Http 요청 - 응답을 String으로 받을 것 명시
            ResponseEntity<String> response = restTemplate.postForEntity(uri, entity, String.class);
            String resultGet = response.getBody(); // response의 body를 가져옴
            System.out.println("resultGet: "+resultGet);

            //String json = "{\"isSuccess\":true,\"code\":200,\"message\":\"요청에 성공하였습니다.\",\"result\":[{\"type\":1,\"ptn\":7,\"colors\":[\"연한 주황색\"],\"canDetectStain\":true,\"hasStain\":true,\"status\":1,\"careInfos\":[\"다림질 가능\",\"표백 불가능\",\"손세탁 가능\"]}]}";
            /* 의류 정보 등록_Get */
            JSONParser parser = new JSONParser();
            // JSON 파싱
            JSONObject getjsonObject = (JSONObject) parser.parse(resultGet); //**
            System.out.println("getjsonObject: "+getjsonObject);
            /* 의류 정보 등록_Get */
            isSuccess = (boolean) getjsonObject.get("isSuccess");
            code = ((Long) getjsonObject.get("code")).intValue();
            message = (String) getjsonObject.get("message");
            result = (JSONArray) getjsonObject.get("result");
            color = "";
            feature = "";
            for(int i=0;i<result.size();i++){
                JSONObject tmp = (JSONObject) result.get(i);//인덱스 번호로 접근해서 가져온다.

                type = ((Long) tmp.get("type")).intValue();
                ptn = ((Long) tmp.get("ptn")).intValue();
                colors = (JSONArray) tmp.get("colors");
                for(int j=0; j<colors.size(); j++)
                    color += (String) colors.get(j);

                features = (JSONArray) tmp.get("features");
                if(features != null){
                    for(int j=0; j<features.size(); j++)
                        feature += (String)features.get(j);
                }

                canDetectStain = (boolean) tmp.get("canDetectStain");
                hasStain = (boolean) tmp.get("hasStain");
                status = ((Long) tmp.get("status")).intValue();
                careInfos = (JSONArray) tmp.get("careInfos");
                if(careInfos != null){
                    for(int j=0; j<careInfos.size(); j++)
                        careInfo = careInfo + careInfos.get(j)+ " ";
                }
            }
        } catch (Exception e) {
            // Exception 처리 내용
        }
    }
}