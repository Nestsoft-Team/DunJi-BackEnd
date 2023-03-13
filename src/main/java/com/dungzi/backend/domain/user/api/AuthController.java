package com.dungzi.backend.domain.user.api;

import com.dungzi.backend.domain.univ.application.UnivAuthService;
import com.dungzi.backend.domain.univ.application.UnivService;
import com.dungzi.backend.domain.univ.domain.Univ;
import com.dungzi.backend.domain.user.application.AuthService;
import com.dungzi.backend.domain.user.application.EmailService;
import com.dungzi.backend.domain.user.application.KakaoService;
import com.dungzi.backend.domain.user.domain.User;
import com.dungzi.backend.domain.user.dto.UserRequestDto;
import com.dungzi.backend.domain.user.dto.UserAuthResponseDto;
import com.dungzi.backend.global.common.response.code.CommonCode;
import com.dungzi.backend.global.common.response.CommonResponse;
import com.dungzi.backend.global.common.response.exception.AuthException;
import com.dungzi.backend.global.common.response.code.AuthErrorCode;
import com.dungzi.backend.global.common.response.code.ValidErrorCode;
import com.dungzi.backend.global.common.response.exception.ValidException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.util.HashMap;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth") // cookie 에서 사용자 정보를 확인하지 않는 api 들
public class AuthController {
    private final String LOGIN_SUCCESS_REDIRECT_DOMAIN_URL = "https://dankan.co.kr";
    private final String LOGIN_SUCCESS_REDIRECT_LOCAL_URL = "http://localhost:3000";
    private final String LOGIN_FAIL_REDIRECT_DOMAIN_URL = "https://dankan.co.kr/signup/policy";
    private final String LOGIN_FAIL_REDIRECT_LOCAL_URL = "http://localhost:3000/signup/policy";
    private final String LOGIN_REDIRECT_STATE_RELEASE = "release";
    private final String LOGIN_REDIRECT_STATE_DEVELOP = "develop";
    private final String LOGIN_FAIL_KAKAO_TOKEN_HEADER = "kakao-access-token";

    private final KakaoService kakaoService;
    private final AuthService authService;
    private final EmailService emailService;
    private final UnivService univService;
    private final UnivAuthService univAuthService;

    private final String ACCESS_TOKEN = "access_token";
//    private final String REFRESH_TOKEN = "refresh_token";

    @Operation(summary = "이메일 인증코드 전송 api", description = "이메일 인증코드 전송 요청 api")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "이메일 인증코드 전송 성공"),
                    @ApiResponse(responseCode = "400", description = "이메일 주소 형식 오류"),
                    @ApiResponse(responseCode = "400", description = "이메일 값 필수 오류"),
                    @ApiResponse(responseCode = "400", description = "대학코드 값 필수 오류"),
                    @ApiResponse(responseCode = "409", description = "카카오 관련 오류 (카카오 토큰값 확인 권장)")
            }
    )
    @GetMapping("/code")
    public ResponseEntity<CommonResponse> getAuthCodeEmail(@RequestParam @NotBlank @Email String email, @RequestParam(value = "univ") @NotBlank String univId) throws Exception {
        log.info("[API] auth/code");
        univService.checkUnivDomain(email, univId);
        String code = emailService.sendSimpleMessage(email);
        log.info("이메일 전송 완료. 인증코드 : {}", code);
        UserAuthResponseDto.SendEmailAuth response = UserAuthResponseDto.SendEmailAuth.builder()
                .email(email)
                .authCode(code)
                .build();
        return ResponseEntity.ok(CommonResponse.toResponse(CommonCode.OK, response));
    }

    @Operation(summary = "카카오 회원가입 api", description = "카카오 계정 연동 회원가입")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201", description = "회원 가입 성공"),
                    @ApiResponse(responseCode = "400", description = "request body 값 관련 오류"),
                    @ApiResponse(responseCode = "409", description = "해당 email 로 가입된 사용자가 이미 존재함"),
                    @ApiResponse(responseCode = "409", description = "해당 nickname 은 사용할 수 없는 형식임"),
                    @ApiResponse(responseCode = "409", description = "해당 nickname 으로 가입된 사용자가 이미 존재함"),
                    @ApiResponse(responseCode = "409", description = "카카오 관련 오류 (카카오 토큰값 확인 권장)")
            }
    )
    @Transactional
    @PostMapping("/kakao")
    public ResponseEntity<CommonResponse> signUpByKakao(@RequestBody @Valid UserRequestDto.SignUpByKakao requestDto) {
        log.info("[API] auth/kakao");

        //isUnivAuth == true 일 경우 univId, univEmail 필수값 체크
        if(requestDto.getIsUnivAuth()) { validateUnivAuthFields(requestDto); }

        authService.validateNickname(requestDto.getNickname());

        User kakaoUser = kakaoService.getKakaoUserInfo(requestDto.getKakaoAccessToken());

        //회원가입
        User newUser = authService.signUpByKakao(kakaoUser, requestDto.getNickname());

        //이메일 인증 정보 저장
        if(requestDto.getIsUnivAuth()){
            Univ univ = univService.getUniv(requestDto.getUnivId());
            univAuthService.createUnivAuth(newUser, univ, requestDto.getUnivEmail(), true);
        }

        return ResponseEntity.ok(CommonResponse.toResponse(CommonCode.CREATED, UserAuthResponseDto.SignUpByKakao.toDto(newUser)));
    }

    @Operation(summary = "닉네임 중복 검사 api", description = "이미 존재하는 닉네임인지 확인하는 api")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "확인 성공"),
                    @ApiResponse(responseCode = "400", description = "request body 값 관련 오류"),
                    @ApiResponse(responseCode = "409", description = "이미 존재하는 nickname 임")
            }
    )
    @PostMapping("/nickname")
    public ResponseEntity<CommonResponse> checkNicknameUnique(@RequestBody @Valid UserRequestDto.NicknameOnly requestDto) {
        log.info("[API] auth/nickname");
        authService.checkNicknameUnique(requestDto.getNickname());
        return ResponseEntity.ok(CommonResponse.toResponse(CommonCode.OK,
                UserAuthResponseDto.CheckNicknameExist.toDto(requestDto.getNickname(), true)));
    }

    @Operation(summary = "로그아웃 api", description = "토큰이 저장된 쿠키를 삭제하여 로그아웃")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "로그아웃 성공")
            }
    )
    @GetMapping("/logout")
    public ResponseEntity<CommonResponse> logout(HttpServletResponse servletResponse) {
        log.info("[API] auth/logout");
        authService.removeCookieToken(servletResponse);
        return ResponseEntity.ok(CommonResponse.toResponse(CommonCode.OK, null));
    }


    //카카오 로그인 콜백
    @Operation(summary = "카카오 로그인 콜백 api", description = "카카오 계정 연동 로그인에 사용되는 콜백 api")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "회원 가입된 유저. 로그인 성공"),
                    @ApiResponse(responseCode = "200", description = "가입되지 않은 유저. 카카오 액세스 토큰 전달"),
                    @ApiResponse(responseCode = "409", description = "카카오 관련 오류 (카카오 토큰값 확인 권장)")
            }
    )
    @GetMapping("/kakao")
    public void kakaoCallback(@RequestParam @NotBlank String state, @RequestParam @NotBlank String code, HttpServletResponse httpServletResponse) throws IOException {
        log.info("[API] auth/kakao");
        log.info("state : {}", state);

        HashMap<String, String> token = kakaoService.getKakaoAccessToken(code);
        String kakao_access_token = token.get(ACCESS_TOKEN);

        User kakaoUser = kakaoService.getKakaoUserInfo(kakao_access_token);

        try {
            User loginUser = authService.login(kakaoUser);
            authService.setCookieTokenInResponse(httpServletResponse, loginUser); //위로 합치기
            setLoginRedirect(httpServletResponse, state, true);
            log.info("kakaoCallback : success login");
        }
        catch(AuthException authException) {
            if(authException.getCode() == AuthErrorCode.NOT_EXIST_USER){
                httpServletResponse.addHeader(LOGIN_FAIL_KAKAO_TOKEN_HEADER, kakao_access_token);
                setLoginRedirect(httpServletResponse, state, false);
                log.info("kakaoCallback : fail login");
            }
        }

    }

    private void setLoginRedirect(HttpServletResponse httpServletResponse, String state, Boolean isLoginSuccess) throws IOException {
        String redirectUrl = "";
        if(isLoginSuccess){
            if(state.equals(LOGIN_REDIRECT_STATE_RELEASE)){
                redirectUrl=LOGIN_SUCCESS_REDIRECT_DOMAIN_URL;
            }
            else if(state.equals(LOGIN_REDIRECT_STATE_DEVELOP)){
                redirectUrl=LOGIN_SUCCESS_REDIRECT_LOCAL_URL;
            }
        }
        else {
            if(state.equals(LOGIN_REDIRECT_STATE_RELEASE)){
                redirectUrl=LOGIN_FAIL_REDIRECT_DOMAIN_URL;
            }
            else if(state.equals(LOGIN_REDIRECT_STATE_DEVELOP)){
                redirectUrl=LOGIN_FAIL_REDIRECT_LOCAL_URL;
            }
        }
        log.info("redirectUrl : {}", redirectUrl);
        httpServletResponse.sendRedirect(redirectUrl);
    }


    private void validateUnivAuthFields(UserRequestDto.SignUpByKakao requestDto) {
        if(requestDto.getUnivEmail() == null || requestDto.getUnivId() == null){
            throw new ValidException(ValidErrorCode.REQUIRED_VALUE);
        }
        else{
            if(requestDto.getUnivEmail().isBlank() || requestDto.getUnivId().isBlank()){
                throw new ValidException(ValidErrorCode.REQUIRED_VALUE);
            }
        }
    }

}
