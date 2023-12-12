package org.example;

import org.json.simple.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class enroll{
    public static String user;
    public static String replay; //다시 듣기
    public static DataSend http;
    public static JSONObject jsonData = new JSONObject();
    public void enrollInfo() { // 의류 등록
        try {
            System.out.println("의류 등록을 시작합니다.");
            //stt.ttsSound("enroll0.mp3");
            //stt.synthesizeText("의류 등록을 시작합니다.");
            stt.ttsSound("src/main/resources/mp3/enroll_0.mp3");
        } catch (Exception e2) {
            // TODO Auto-generated catch block
            e2.printStackTrace();
        }
        photo();
    }
    public static void photo() {
        try {
            System.out.println("촬영을 시작하겠습니다.");
//            stt.ttsSound("0.mp3");
//            stt.ttsSound("photo321.mp3");
            stt.ttsSound("src/main/resources/mp3/photo_321.mp3");

            Con.conShell();
            //conShell.conShell();
            //conShell.cntC ++;

            System.out.println("촬영이 끝났습니다. 정보를 추출하기까지 "
                    + "시간이 소요되니 잠시만 기다려주세요.");
            //stt.ttsSound("photo1.mp3");
            stt.ttsSound("src/main/resources/mp3/photo_warn.mp3");

            File file = new File("cloth.jpg"); // 촬영 cloth.jpg httpSend
            //File file = new File("test.jpg");
            try {
                byte[] imageBytes = Files.readAllBytes(file.toPath());
                /* RP takePhoto() 구현*/
                http.clothExtract(imageBytes);

                String s = "";
                if (http.code == 200) {
                    if (http.result.size() != 1) {
                        System.out.println("인식된 의류가 2개입니다. 특징을 말씀드리겠습니다. 해당하는 의류를 골라주세요.");
                        for (int k = 0; k < http.result.size(); k++) {
                            s += http.color + "을 가진 ";
                            s += ClothData.PATTERN(http.ptn) + " 패턴의 ";
                            if (http.features != null)
                                s += http.feature + " 특징을 가진 ";
                            s += ClothData.TYPE(http.type) + " 의류를 등록하시겠습니까?";
                            //stt.synthesizeText(s, "cloth.mp3");
                            //stt.ttsSound("cloth.mp3");

                            System.out.println(s);
                            try{
                                stt.sttTest();

                                user = stt.sen;
                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }

                           // user = "네";

                            http.nickname = "";
                            if (user.equals("네")) {
                                jsonData.put("type", http.type);
                                jsonData.put("ptn", http.ptn);
                                jsonData.put("colors", http.colors);
                                jsonData.put("features", http.features);
                                jsonData.put("nickname", http.nickname);
                                http.clothEnroll(jsonData);
                            } else if (k == http.result.size() - 1) {
                                System.out.println("등록하고자 하는 의류가 없다면, 촬영을 다시 진행해주시기 바랍니다. 촬영을 다시 하시겠습니까?");
                                try{
                                    stt.sttTest();

                                    user = stt.sen;
                                } catch (Exception e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                                if (user.equals("네"))
                                    photo();
                                else {
                                    System.out.println("시스템을 종료합니다. 이용해주셔서 감사합니다.");
                                    System.exit(0);
                                }
                            }
                        }
                    } else {
                        s += "인식된 의류는 ";
                        s += http.color + "을 가진 ";
                        s += ClothData.PATTERN(http.ptn) + " 패턴의 ";
                        if (http.features != null)
                            s += http.feature + " 특징을 가진 ";
                        s += ClothData.TYPE(http.type) + " 의류입니다. 등록하시겠습니까?";

                        System.out.println(s);
                        stt.synthesizeText(s, "src/main/resources/cloth.mp3");
                        stt.ttsSound("src/main/resources/cloth.mp3");
                        //stt.ttsSound("mp3/enroll text 0.mp3");

                        try{
                            stt.sttTest();

                            user = stt.sen;
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                        http.nickname = null;
                        if (user.equals("네")) {

                            jsonData.put("type", http.type);
                            jsonData.put("ptn", http.ptn);
                            jsonData.put("colors", http.colors);
                            jsonData.put("features", http.features);
                            //jsonData.put("nickname", http.nickname);

                            http.clothEnroll(jsonData);

                            if (http.code == 201) {
                                System.out.println(http.message);
                                // user.equals("다음") 이라면
                                enrollGo();
                            }
                            // 중복 의류있다면 무조건
                            else if (http.code == 2002) {
                                replay = "해당 의류와 특징이 유사한 의류가 등록되어 있어, 추가적인 의류 정보를 필수적으로 받습니다."
                                        + "추가하고자 하는 특징을 단어로 끊어서 말씀해주세요. "
                                        + "추가할 수 있는 특징으로는 장미꽃, 스판 등과 같이 카메라가 인식하지 못한 정보나 "
                                        + "사용자가 원하는 별칭, 재질 정보 등이 있습니다."; // enroll2, 3.mp3
                                System.out.println(replay);
                            }
                        } else {
                            System.out.println("등록하고자 하는 의류가 없다면, 촬영을 다시 진행해주시기 바랍니다. 촬영을 다시 하시겠습니까?");

                            try{
                                stt.sttTest();

                                user = stt.sen;
                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }

                            if (user.equals("네"))
                                photo();
                            else {
                                System.out.println("시스템을 종료합니다. 이용해주셔서 감사합니다.");
                                System.exit(0);
                            }

                        }
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void enrollGo() { // 의류 등록 완료 (서버로 전송확인까지 마친 후)
        replay = "등록이 완료되었습니다. 의류 등록이 완료되었으니 핸드폰 어플을 "
                + "실행하여 해당 의류의 케어라벨을 등록해주세요. 감사합니다.";

        System.out.println(replay);
        //stt.ttsSound("photo2.mp3");
        stt.ttsSound("src/main/resources/mp3/enroll_end.mp3");
        System.out.print('\n');
    }
}
