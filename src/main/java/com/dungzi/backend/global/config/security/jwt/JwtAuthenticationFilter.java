package com.dungzi.backend.global.config.security.jwt;

import com.dungzi.backend.domain.user.application.AuthService;
import com.dungzi.backend.domain.user.domain.User;
import com.dungzi.backend.global.common.Code;
import com.dungzi.backend.global.common.error.AuthException;
import com.dungzi.backend.global.common.error.AuthErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
//@WebFilter(urlPatterns = {"/api/v1/users/login/kakao", "/api/v1/users/email-auth", "/api/v1/chat/*"}) //필터 등록, 필터링 url 설정
//@WebFilter(urlPatterns = {"/api/v1/chat/*"}) //필터 등록, 필터링 url 설정
@WebFilter(urlPatterns = {"/api/v1/users/*", "/api/v1/chat/*", "/api/v1/rooms/*"}) //필터 등록, 필터링 url 설정
public class JwtAuthenticationFilter implements Filter {
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthService authService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("[FILTER] jwtAuthenticationFilter : init");
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException, AuthException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;

        //TODO : servlet 에서 getToken을 두번식 하는 게 됐는데 토큰을 getToken을 public 으로 바꿔서 여기서 꺼낼까..그럼 예외처리는..
        boolean isAccessTokenValid = jwtTokenProvider.isTokenValidByServlet(httpServletRequest, jwtTokenProvider.ACCESS_TOKEN_HEADER_NAME);
        
        //access token 유효
        if(isAccessTokenValid) {
            try{
                Authentication authentication = jwtTokenProvider.getAuthenticationByServlet(httpServletRequest, jwtTokenProvider.ACCESS_TOKEN_HEADER_NAME);
                log.info("[FILTER] jwtAuthenticationFilter : access token is valid");

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }catch (AuthException authException){
                //token은 유효기간이 남았는데 탈퇴 등을 해서 DB에는 이 userPK에 해당하는 user가 없음 -> 모든 토큰이 유효하지 않은 상황과 동일 취급
                log.info("[FILTER] jwtAuthenticationFilter : access token is same as invalid. No user exist with this userPK");
            }
        }
        //access token 유효하지 않음
        else {
            log.info("[FILTER] jwtAuthenticationFilter : access token is invalid");

            //refresh token 유효, access token 재발급
            //RTR 적용 : refresh token 도 새로 발급
            if(jwtTokenProvider.isTokenValidByServlet(httpServletRequest, jwtTokenProvider.REFRESH_TOKEN_HEADER_NAME)) {
                log.info("[FILTER] jwtAuthenticationFilter : refresh token is valid. Recreate tokens");
                String uuid = jwtTokenProvider.getUserPKByServlet(httpServletRequest, jwtTokenProvider.REFRESH_TOKEN_HEADER_NAME);

                try {
                    User user = authService.getUserByUuid(uuid);

                    log.info("[FILTER] jwtAuthenticationFilter : Set new access token & refresh token in cookie");
                    authService.setTokenCookieAndSecurityByUser((HttpServletResponse)response, user);

                }catch (AuthException authException) {
                    Code code = authException.getCode();
                    if(code == AuthErrorCode.NOT_EXIST_USER){
                        log.info("[FILTER] jwtAuthenticationFilter AuthException : "+code.name()+" - "+code.getMessage());
                    }
                }
                catch (Exception e) {
                    log.info("[FILTER] jwtAuthenticationFilter Exception : ");
                    e.printStackTrace();
                    throw e;
                }

            }
            //refresh token 유효하지 않음
            else {
                log.info("[FILTER] jwtAuthenticationFilter : refresh token is invalid. Guest user");
            }
        }
        chain.doFilter(request, response);
    }
}
