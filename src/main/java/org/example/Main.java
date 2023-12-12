package org.example;


// main

// 음성 인터페이스 빠르기 조절 40%
public class Main {

    public static boolean logData;
    public static String user;
    public static String useTip =
            "기기 사용 시에는 창이 없는 밀폐되고 밝은 곳에서 촬영하는 것을 권장합니다."; //start4.mp3
    public static void main(String args[]) throws Exception {

        info in = new info();
        enroll en = new enroll();


//		if(logData==false) {
//			try {
//				String a = (
//						"케어하임은 소지하고 있는 의류의 세탁정보를 저장하여 "
//						+ "외출 후 얼룩 여부와 세탁 정보를 들을 수 있는 시각장애인 "
//						+ "전용 의류 케어 서비스입니다.");
//				String b=(
//						 "케어하임은 종료 프로세스가 존재하지 않아 사용자가 언제든 편하게 이용 가능한 서비스로,"
//						+ "사용자의 정보를 무단 수집하지 않아, 프로세스를 계속 실행시켜 두셔도 괜찮습니다.");
//				String c = (
//						"하지만, 사생활과 보안이 걱정되신다면 해당 기기의 전원을 꺼주시기 바랍니다. "
//						+ "기기의 전원을 다시 켜시면 케어하임 서비스가 자동 실행됩니다.") ;
//				System.out.println(a+b+c);
//				stt.synthesizeText(a);
//				stt.synthesizeText(b);
//				stt.synthesizeText(c);
//
//				//stt.ttsSound("start.mp3");
//
//
//				//stt.ttsSound("start2.mp3");
//				//stt.ttsSound("start3.mp3");
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			logData = true;
//		}

        //stt.synthesizeText(useTip);
        //System.out.println(useTip);
        //stt.ttsSound("start4.mp3");

        //while(true){
        // 최초 실행
//			String b = (
//					"케어라벨을 저장할 옷을 "
//					+ "등록하시려면 ‘의류 등록’을, 현재 착용 중인 옷의 "
//					+ "세탁 정보를 들으시려면 ‘세탁 정보 안내’를 말씀해주세요. "
//					+ "다시 들으시려면 ‘다시듣기’를 말씀해주세요."); /* 작업수행 질의문으로 변경 */
//			System.out.println(b);
//			stt.synthesizeText(b);
//			stt.ttsSound("start4+5.mp3");

        //user = "의류 등록";
        //user = "세탁 정보 안내";
        System.out.println("케어하임 서비스를 시작합니다.");
        //stt.tts("mp3/careHeim start 0.mp3");
        stt.ttsSound("src/main/resources/mp3/careHeim_start_0.mp3");
        //stt.ttsSound("start0.mp3");

        String change = ("수행할 작업을 말씀해주세요"); /* 작업수행 질의문으로 변경 */
        System.out.println(change);
        //stt.ttsSound("change.mp3");

        stt.ttsSound("src/main/resources/mp3/careHeim_start_1.mp3");

        while(true){
            try{
                stt.sttTest();

                user = stt.sen;
                //user = "세탁 정보 안내";

                if(user.contains("의류 등록"))
                    en.enrollInfo();
                else if(user.contains("세탁 정보 안내"))
                    in.care();
                else if(user.contains("그만")){
                    stt.ttsSound("src/main/resources/mp3/exit.mp3");
                    System.exit(0);
                }
                else {
                    System.out.println("음성이 제대로 인식되지 않았습니다. 다시 한번 말씀해주세요");
                    stt.ttsSound("src/main/resources/mp3/noUser.mp3");
                }

                user = "";
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

        //}
    }
}