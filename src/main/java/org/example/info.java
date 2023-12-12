package org.example;

import java.io.File;
import java.nio.file.Files;

public class info {
    public static String user; // 사용자 답변
    public static String replay; //다시 듣기

    public static DataSend http;

    public void care() {

        String d = ("세탁 정보 안내를 시작합니다.");
        System.out.println(d);
        //stt.synthesizeText(d);
        //stt.ttsSound("info2.mp3");
        stt.ttsSound("src/main/resources/mp3/info_0.mp3");

        photo();
        //stt.ttsSound("info5.mp3");
    }

    static String st = "";
    static void careLabelInfo() throws Exception { // 세탁정보 안내
        st += s;

        if(http.careInfos != null) {
            st += "해당 의류의 세탁 방법은 " + http.careInfo + "입니다.";
            System.out.println(st);
            stt.synthesizeText(st, "src/main/resources/info_care.mp3");
            stt.ttsSound("src/main/resources/info_care.mp3");
            //stt.ttsSound("mp3/info text 2.mp3");

        }
        else if (http.careInfos == null){
            System.out.println("해당 의류는 세탁정보가 등록되지 않은 의류이므로 세탁 정보를 출력할 수 없습니다. " +
                    "핸드폰 어플리케이션을 통해 케어라벨을 등록한 의류에 한해 세탁정보 안내 서비스를 제공하고 있으니 양해 바랍니다");
            stt.ttsSound("src/main/resources/mp3/info_not_enroll.mp3");
        }
        stainCheck();

        System.out.println("세탁 정보 안내 프로세스를 모두 마쳤습니다. 이용해주셔서 감사합니다.");
        //stt.ttsSound("info5.mp3");
        stt.ttsSound("src/main/resources/mp3/info_end.mp3");
    }

    static void stainCheck() {
        if(http.canDetectStain == true) { // 얼룩 탐지 가능 의류라면
            if(http.hasStain== true) {
                System.out.println("해당 방향의 의류 사진에서 얼룩을 탐지하였습니다.");
                //stt.ttsSound("info6.mp3");
                stt.ttsSound("src/main/resources/mp3/info_stain.mp3");
            }
            else {
                System.out.println("해당 방향의 의류 사진에서 얼룩이 탐지되지 않았습니다.");
                //stt.ttsSound("info7.mp3");
                stt.ttsSound("src/main/resources/mp3/info_no_stain.mp3");
            }
        }
    	else {
    		System.out.println("얼룩을 탐지할 수 없는 의류입니다. 세탁정보 안내 서비스 중 얼룩 탐지 기능은 "
						+ " 단색이나 단순한 패턴의 디자인에서만 얼룩을 판별할 수 있습니다.");
    		stt.ttsSound("info8.mp3");
    	}
    }

    static String s;
    public static void photo() {
        s = "";
        try {
            System.out.println("촬영을 시작하겠습니다.");
//            stt.ttsSound("0.mp3");
//            stt.ttsSound("photo321.mp3");
            stt.ttsSound("src/main/resources/mp3/photo_321.mp3");

            Con.conShell();
            //conShell.cntC ++;

            System.out.println("촬영이 끝났습니다. 정보를 추출하기까지 "
                    + "시간이 소요되니 잠시만 기다려주세요.");
            //stt.ttsSound("photo1.mp3");
            stt.ttsSound("src/main/resources/mp3/photo_warn.mp3");

            File file = new File("cloth.jpg"); // 촬영 cloth.jpg httpSend
            //File file = new File("test.jpg");
            byte[] imageBytes = Files.readAllBytes(file.toPath());

            http.ClothInfo(imageBytes);


            if(http.code == 200) {
                String i = "인식된 의류는 "+http.result.size()+"개 입니다.";
                System.out.println(i);
                //stt.synthesizeText(i, "src/main/resources/info_num.mp3");
                //stt.ttsSound("src/main/resources/info_num.mp3");
                s += i;


                for(int k=0; k<http.result.size(); k++){
                    s += http.color + " 을 가진 ";
                    s += ClothData.PATTERN(http.ptn) + "패턴의 ";
                    if(http.features != null)
                        s += http.feature + " 특징을 가진 ";
                    s += ClothData.TYPE(http.type) + " 의 세탁정보를 안내합니다.";
                    //stt.synthesizeText(s, "cloth.mp3");
                    //stt.ttsSound("cloth.mp3");
                    System.out.println(s);

                    //stt.synthesizeText(s, "src/main/resources/infoC.mp3");
                    //stt.ttsSound("src/main/resources/infoC.mp3");
                    //stt.ttsSound("mp3/info text 0.mp3");
                    careLabelInfo();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}