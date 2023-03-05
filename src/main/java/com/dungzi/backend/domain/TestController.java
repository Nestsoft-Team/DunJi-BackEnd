package com.dungzi.backend.domain;

import com.dungzi.backend.domain.user.application.AuthService;
import com.dungzi.backend.domain.user.domain.User;
import com.dungzi.backend.global.common.CommonCode;
import com.dungzi.backend.global.common.CommonResponse;
import com.dungzi.backend.global.config.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/test")
public class TestController {
    private final JwtTokenProvider jwtTokenProvider;

    private final AuthService authService;

    @GetMapping("/hello")
    public CommonResponse hello() {
        log.info("[API] test/hello");
        return CommonResponse.toResponse(CommonCode.OK, "hello");
    }

    @GetMapping("/redirect")
    public void testRedirect(HttpServletResponse response) throws IOException {
//        String redirect_uri="http://www.google.com"; //http://localhost:3000/login/kakao
        String redirect_uri="http://localhost:3000/chat";
        response.sendRedirect(redirect_uri);
    }


    @GetMapping("/checkToken/{tokenType}")
//    public CommonResponse checkAccessToken(HttpServletRequest httpServletRequest) { //@CookieValue("x-access-token") String accessToken
    public CommonResponse checkToken(@PathVariable String tokenType, HttpServletRequest httpServletRequest) {
        log.info("[API] test/checkToken");

        String headerName = "";
        if(tokenType.equals("access")){
            headerName = jwtTokenProvider.ACCESS_TOKEN_HEADER_NAME;
        }else if(tokenType.equals("refresh")){
            headerName = jwtTokenProvider.REFRESH_TOKEN_HEADER_NAME;
        }

        String message;
        User user;
        //이 요청 시 액세스 토큰이 만료되어서 필터에서 새 액세스 토큰을 저장했어도 이 serveletReqest 에는 구토큰이 저장되어있음
        if(jwtTokenProvider.isTokenValidByServlet(httpServletRequest, headerName)){
            String uuid = jwtTokenProvider.getUserPKByServlet(httpServletRequest, headerName);
            user = authService.getUserByUuid(uuid);
            message = tokenType+" token 사용자 닉네임 : "+user.getNickname()+", 이메일 : "+user.getEmail();
        }else{
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            user = (User) userDetails;
            message = "요청 시 보내신 access 토큰은 만료되었습니다. 새로 발급된 access token 사용자 닉네임 : "+user.getNickname()+", 이메일 : "+user.getEmail();
        }

        return CommonResponse.toResponse(CommonCode.OK, message);
    }
}
