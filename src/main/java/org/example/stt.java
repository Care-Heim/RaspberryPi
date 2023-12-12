package org.example;

import com.google.api.gax.rpc.ClientStream;
import com.google.api.gax.rpc.ResponseObserver;
import com.google.api.gax.rpc.StreamController;
import com.google.cloud.speech.v1.*;
import com.google.cloud.texttospeech.v1.*;
import com.google.protobuf.ByteString;
import javazoom.jl.player.Player;

import javax.sound.sampled.*;
import javax.sound.sampled.DataLine.Info;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class stt {
    public static OutputStream out;
    public static String name;
    public static ByteString synthesizeText(String text, String fileName) throws Exception {
        // Instantiates a client
        try (TextToSpeechClient textToSpeechClient = TextToSpeechClient.create()) {
            // Set the text input to be synthesized
            SynthesisInput input = SynthesisInput.newBuilder().setText(text).build();

            // Build the voice request
            VoiceSelectionParams voice =
                    VoiceSelectionParams.newBuilder()
                            .setLanguageCode("ko-KR") // languageCode = "en_us"
                            .setSsmlGender(SsmlVoiceGender.FEMALE) // ssmlVoiceGender = SsmlVoiceGender.FEMALE
                            .build();

            // Select the type of audio file you want returned
            AudioConfig audioConfig =
                    AudioConfig.newBuilder()
                            .setAudioEncoding(AudioEncoding.MP3) // MP3 audio.
                            .build();

            // Perform the text-to-speech request
            SynthesizeSpeechResponse response =
                    textToSpeechClient.synthesizeSpeech(input, voice, audioConfig);

            // Get the audio contents from the response
            ByteString audioContents = response.getAudioContent();

            // Write the response to the output file.
            name = imgName(fileName).toString();
            try (OutputStream out = new FileOutputStream(fileName)) {
                out.write(audioContents.toByteArray());
                //System.out.println("Audio content written to file \"output.mp3\"");
                return audioContents;

            }
        }

    }
/*
    public static void ttsSound(String fN) {
        try {
            File f = new File(fN);
            MP3Player mp3Player = new MP3Player(f);
            mp3Player.play();

            while (!mp3Player.isStopped()) {
                Thread.sleep(5000);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
*/
    public static void ttsSound(String fN) {
        try {
            FileInputStream fis = new FileInputStream(fN);
            Player playMp3 = new Player(fis);
            playMp3.play();

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Demonstrates using the Text to Speech client to list the client's supported voices.
     *
     * @throws Exception on TextToSpeechClient Errors.
     */
    public static List<Voice> listAllSupportedVoices() throws Exception {
        // Instantiates a client
        try (TextToSpeechClient textToSpeechClient = TextToSpeechClient.create()) {
            // Builds the text to speech list voices request
            ListVoicesRequest request = ListVoicesRequest.getDefaultInstance();

            // Performs the list voices request
            ListVoicesResponse response = textToSpeechClient.listVoices(request);
            List<Voice> voices = response.getVoicesList();

            for (Voice voice : voices) {
                // Display the voice's name. Example: tpc-vocoded
                System.out.format("Name: %s\n", voice.getName());

                // Display the supported language codes for this voice. Example: "en-us"
                List<ByteString> languageCodes = voice.getLanguageCodesList().asByteStringList();
                for (ByteString languageCode : languageCodes) {
                    System.out.format("Supported Language: %s\n", languageCode.toStringUtf8());
                }

                // Display the SSML Voice Gender
                System.out.format("SSML Voice Gender: %s\n", voice.getSsmlGender());

                // Display the natural sample rate hertz for this voice. Example: 24000
                System.out.format("Natural Sample Rate Hertz: %s\n\n", voice.getNaturalSampleRateHertz());
            }
            return voices;
        }
    }


    // img name
    static File file;
    public static File imgName(String baseFileName) {
        //String baseFileName = "file";
        String fileExtension = ".mp3";

        int fileNumber = 1;

        do {
            String fileName = baseFileName + fileNumber + fileExtension;
            file = new File(fileName);
            fileNumber++;
        } while (file.exists());

        try {
            // 파일 생성 및 처리
            boolean created = file.createNewFile();
            if (created) {
                System.out.println("mp3파일이 생성되었습니다: " + file.getName());
                // 여기에서 파일에 대한 추가 작업을 수행할 수 있습니다.
            } else {
                System.out.println("mp3파일을 생성할 수 없습니다.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }


    public static String sen;
    public static ResponseObserver<StreamingRecognizeResponse> responseObserver;
    public static void sttTest() {
        ResponseObserver<StreamingRecognizeResponse> responseObserver = null;
        try (SpeechClient client = SpeechClient.create()) {

            responseObserver = new ResponseObserver<StreamingRecognizeResponse>() {
                ArrayList<StreamingRecognizeResponse> responses = new ArrayList<>();

                public void onStart(StreamController controller) {
                }

                public void onResponse(StreamingRecognizeResponse response) {
                    responses.add(response);
                }

                public void onComplete() {
                    String record = "tts\\outpu1.mp3";

                    for (StreamingRecognizeResponse response : responses) {
                        StreamingRecognitionResult result = response.getResultsList().get(0);
                        SpeechRecognitionAlternative alternative = result.getAlternativesList().get(0);
                        record += alternative.getTranscript();
                        sen =  alternative.getTranscript();
                        System.out.printf("Transcript : %s\n", sen);
                    }
                }

                public void onError(Throwable t) {
                    System.out.println(t);
                }
            };

            ClientStream<StreamingRecognizeRequest> clientStream = client.streamingRecognizeCallable()
                    .splitCall(responseObserver);

            RecognitionConfig recognitionConfig = RecognitionConfig.newBuilder()
                    .setEncoding(RecognitionConfig.AudioEncoding.LINEAR16).setLanguageCode("ko-KR")// 한국어 설정
                    .setSampleRateHertz(16000).build();
            StreamingRecognitionConfig streamingRecognitionConfig = StreamingRecognitionConfig.newBuilder()
                    .setConfig(recognitionConfig).build();

            StreamingRecognizeRequest request = StreamingRecognizeRequest.newBuilder()
                    .setStreamingConfig(streamingRecognitionConfig).build(); // The first request in a streaming call
            // has to be a config

            clientStream.send(request);
            // SampleRate:16000Hz, SampleSizeInBits: 16, Number of channels: 1, Signed:
            // true,
            // bigEndian: false
            AudioFormat audioFormat = new AudioFormat(16000, 16, 1, true, false);
            DataLine.Info targetInfo = new Info(TargetDataLine.class, audioFormat); // Set the system information to
            // read from the microphone audio
            // stream

            if (!AudioSystem.isLineSupported(targetInfo)) {
                System.out.println("Microphone not supported");
                System.exit(0);
            }
            // Target data line captures the audio stream the microphone produces.
            TargetDataLine targetDataLine = (TargetDataLine) AudioSystem.getLine(targetInfo);
            targetDataLine.open(audioFormat);
            targetDataLine.start();
            System.out.println("Start speaking");
            long startTime = System.currentTimeMillis();
            // Audio Input Stream
            AudioInputStream audio = new AudioInputStream(targetDataLine);
            while (true) {
                long estimatedTime = System.currentTimeMillis() - startTime;
                byte[] data = new byte[9000];
                audio.read(data);
                if (estimatedTime > 6000) { // 60 seconds 스트리밍 하는 시간설정(기본 60초)
                    System.out.println("Stop speaking.");
                    targetDataLine.stop();
                    targetDataLine.close();
                    break;
                }
                request = StreamingRecognizeRequest.newBuilder().setAudioContent(ByteString.copyFrom(data)).build();
                clientStream.send(request);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        responseObserver.onComplete(); //완료 이벤트 발생
    }
}