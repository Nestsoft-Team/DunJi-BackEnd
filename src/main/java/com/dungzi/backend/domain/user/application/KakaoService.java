package com.dungzi.backend.domain.user.application;

import com.dungzi.backend.domain.user.dao.UserDao;
import com.dungzi.backend.domain.user.domain.User;
import com.dungzi.backend.global.common.error.AuthErrorCode;
import com.dungzi.backend.global.common.error.AuthException;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class KakaoService {

    private final String TOKEN_REQUEST_URL = "https://kauth.kakao.com/oauth/token";
    private final String INFO_REQUEST_URL = "https://kapi.kakao.com/v2/user/me";
    private final String TOKEN_GRANT_TYPE = "authorization_code";
    private final String API_KEY = "00c48270395b6a27deb3c5a044c1407f";
    private final String ACCESS_TOKEN = "access_token";
    private final String REFRESH_TOKEN = "refresh_token";

    @Value("${KAKAO_TOKEN_REDIRECT_URI}")
    private String tokenRedirectUri; //받은 인가코드로 요청한 토큰이 리다이렉트되는 url

    //access_token 발급
    public HashMap<String, String> getKakaoAccessToken(String code) {
        log.info("[SERVICE] getKakaoAccessToken");

        String access_Token = "";
        String refresh_Token = "";

        try {
            URL url = new URL(TOKEN_REQUEST_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            //  URL연결은 입출력에 사용 될 수 있고, POST 혹은 PUT 요청을 하려면 setDoOutput을 true로 설정해야함.
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            //TODO : url 문자열 코드 부분 가독성을 위해 StringBuilder 보다는 MultiValueMap 등 다른 객체 사용 고려해보기
            //POST 요청에 필요로 요구하는 파라미터 스트림을 통해 전송
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            StringBuilder sb = new StringBuilder();
            sb.append("grant_type=" + TOKEN_GRANT_TYPE);
            sb.append("&client_id=" + API_KEY); // REST_API_KEY 입력
//            sb.append("&redirect_uri=http://3.39.129.136:8090/DungziProject/login/kakao"); // To-Do 인가코드 받은 redirect_uri 입력
            sb.append("&redirect_uri=" + tokenRedirectUri); // To-Do 인가코드 받은 redirect_uri 입력
            sb.append("&code=" + code);
            bw.write(sb.toString());
            bw.flush();

            //결과 코드가 200이라면 성공
            int responseCode = conn.getResponseCode();
            if(responseCode != 200){
                log.info("kakao connection failed : {} {}", conn.getResponseCode(), conn.getResponseMessage());
            }

            //요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }

            //Gson 라이브러리에 포함된 클래스로 JSON파싱 객체 생성
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);

            access_Token = element.getAsJsonObject().get(ACCESS_TOKEN).getAsString();
            refresh_Token = element.getAsJsonObject().get(REFRESH_TOKEN).getAsString();

            log.info("kakao {} : {}", ACCESS_TOKEN, access_Token);
            log.info("kakao {} : {}", REFRESH_TOKEN, refresh_Token);

            br.close();
            bw.close();
        } catch (Exception e) {
            log.warn("getKakaoAccessToken failed : {}", e.getMessage());
//            e.printStackTrace();
            throw new AuthException(AuthErrorCode.KAKAO_FAILED);
        }

        HashMap<String, String> userInfo = new HashMap<String, String>();
        userInfo.put(ACCESS_TOKEN, access_Token);
        userInfo.put(REFRESH_TOKEN, refresh_Token);

        return userInfo;
    }

    // access_token을 이용하여 사용자 정보 조회
    public User getKakaoUserInfo(String token) {
        log.info("[SERVICE] getKakaoUserInfo");

        User user = new User();

        try {
            URL url = new URL(INFO_REQUEST_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Authorization", "Bearer " + token); //전송할 header 작성, access_token전송

            //결과 코드가 200이라면 성공
            int responseCode = conn.getResponseCode();
            if(responseCode != 200){
                log.warn("kakao connection failed : {} {}", conn.getResponseCode(), conn.getResponseMessage());
            }

            //요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }

            //TODO : 카카오에서 가져올 값들 목록 정리, 확정 후 코드 정리
            //Gson 라이브러리로 JSON파싱
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);

            log.info("kakao json element : {}", element);

//            JsonObject properties = element.getAsJsonObject().get("properties").getAsJsonObject();
            JsonObject kakao_account = element.getAsJsonObject().get("kakao_account").getAsJsonObject();
            JsonObject profile = kakao_account.getAsJsonObject().get("profile").getAsJsonObject();

//            String id = element.getAsJsonObject().get("id").getAsString();

            // email, ci, nickname 필수 값으로 변경시 수정 필요
            boolean hasEmail = kakao_account.getAsJsonObject().get("has_email").getAsBoolean();
            String email = "";
            if (hasEmail) {
                email = kakao_account.getAsJsonObject().get("email").getAsString();
            }

//            boolean hasNickname = properties.getAsJsonObject().get("has_nickname").getAsBoolean();
//            String nickname = "";
//            if (hasNickname) {
//                nickname = profile.getAsJsonObject().get("nickname").getAsString();
//            }
//
//            boolean hasCi = kakao_account.getAsJsonObject().get("has_ci").getAsBoolean();
//            String ci = "";
//            if (hasCi) {
//                ci = kakao_account.getAsJsonObject().get("ci").getAsString();
//            }

            String nickname = "";
            if (profile.getAsJsonObject().get("nickname") != null) {
                nickname = profile.getAsJsonObject().get("nickname").getAsString();
            }

            String profileImage = "";
            if (profile.getAsJsonObject().get("profile_image_url") != null) {
                profileImage = profile.getAsJsonObject().get("profile_image_url").getAsString();
            }

            String ci = "";
            if (kakao_account.getAsJsonObject().get("ci") != null) {
                ci = kakao_account.getAsJsonObject().get("ci").getAsString();
            }

//            log.info("id : {}", id);
            log.info("email : {}", email);
            log.info("nickname : {}", nickname);
            log.info("profileImage : {}", profileImage);
            log.info("ci : {}", ci);

            //TODO : 회원가입 시 user에 저장할 데이터 다시 확인
            user = User.builder()
                    .email(email)
                    .nickname(nickname)
                    .profileImg(profileImage)
                    .ci(ci)
                    .isActivated(true)
                    .build();

            br.close();

        } catch (Exception e) {
            log.warn("getKakaoUserInfo failed : {}", e.getMessage());
//            e.printStackTrace();
            throw new AuthException(AuthErrorCode.KAKAO_FAILED);
        }

        return user;

    }
}
