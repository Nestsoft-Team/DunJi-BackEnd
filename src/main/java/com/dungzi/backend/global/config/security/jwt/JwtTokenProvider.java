package com.dungzi.backend.global.config.security.jwt;

import com.dungzi.backend.global.common.error.AuthException;
import com.dungzi.backend.global.common.error.AuthErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtTokenProvider {
    @Value("${JWT_TOKEN_SECRET_KEY}") //yml 파일에 설정한 변수명
    private String secretKey;

    public final static String ACCESS_TOKEN_HEADER_NAME = "x-access-token";
    public final static  String REFRESH_TOKEN_HEADER_NAME = "x-refresh-token";

    // access, refresh 토큰 유효기간 : 각 1시간, 30일
    public final int ACCESS_COOKIE_MAX_AGE = 60*60; // 1시간
    public final int REFRESH_COOKIE_MAX_AGE = 30*24*60*60; // 30일
    private final long ACCESS_TOKEN_VALID_TIME = ACCESS_COOKIE_MAX_AGE * 1000L; //나노초 long
    private final long REFRESH_TOKEN_VALID_TIME = REFRESH_COOKIE_MAX_AGE * 1000L;

    private final UserDetailsService userDetailsService;


    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String createToken(String userPK, List<String> roles, String tokenType) {
        log.info("jwtTokenProvider createToken");

        long validTime = getValidTime(tokenType);

        Claims claims = Jwts.claims().setSubject(userPK);
        claims.put("roles", roles);
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now) //토큰 발행 시간 저장
                .setExpiration(new Date(now.getTime() + validTime)) //토큰 만료 시각 설정
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String getUserPKByServlet(HttpServletRequest httpServletRequest, String tokenType) throws AuthException {
        String token = "";
        try {
            token = getToken(httpServletRequest, tokenType);
//            log.info("jwtTokenProvider getUserPKByServlet token : "+token+", tokenType : "+tokenType);
        }
        catch (AuthException authException){
            if(authException.getCode() == AuthErrorCode.GUEST_USER){
                //비회원 사용자 처리 : UserPK값을 null로 반환
                return null;
            } else {
                //다른 에러일 시 throw
                throw authException;
            }
        }
        return getUserPKByToken(token);

    }

    //토큰에서 회원 정보 추출
    private String getUserPKByToken(String token) {
        String userPK;
        try {
            // TODO : 만료된 토큰을 주면 왜 여기서 에러를 던질까
            userPK = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject(); //setSubject했던 값 가져오기
        } catch (Exception e) {
            log.info("jwtTokenProvider getUserPKByToken : {}", e.getMessage());
            e.printStackTrace();
            throw new AuthException(AuthErrorCode.INVALID_TOKEN); //토큰에서 회원 정보를 확인할 수 없을 때 throw
        }

        log.info("jwtTokenProvider getUserPKByToken userPK : {}", userPK);
        return userPK;
    }

    public boolean isTokenValidByServlet(HttpServletRequest httpServletRequest, String tokenType) throws AuthException {
        String token = "";
        try {
            token = getToken(httpServletRequest, tokenType);
        }
        catch (AuthException authException){
            if(authException.getCode() == AuthErrorCode.GUEST_USER){
                //비회원 사용자 처리
                return false;
            } else {
                throw authException;
            }
        }
        return isTokenValid(token);
    }

    //사용자 인증정보 조회
    public Authentication getAuthenticationByServlet(HttpServletRequest httpServletRequest, String tokenType) {
        String token = getToken(httpServletRequest, tokenType);
        return getAuthenticationByToken(token);
    }

    public Authentication getAuthenticationByToken(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserPKByToken(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }


    //////-- private method --//////

    //Header 에서 토큰 가져옴
    private String getToken(HttpServletRequest httpServletRequest, String tokenType) {
//        String token = httpServletRequest.getHeader(tokenType);
        String token = "";
        Cookie[] cookieList = httpServletRequest.getCookies();

        if(cookieList == null || cookieList.length == 0){
            //가진 쿠키가 없으면 비회원 사용으로 간주함
            log.info("jwtTokenProvider getToken : cookieList is empty");
            throw new AuthException(AuthErrorCode.GUEST_USER);
        }

        for(Cookie cookie : cookieList) {
            if(cookie.getName().equals(tokenType)) {
                token = cookie.getValue();
            }
        }

        if(token == null || token.isEmpty()){
            //토큰이 비어있으면 비회원 사용으로 간주함
            log.info("jwtTokenProvider getToken : token is empty");
            throw new AuthException(AuthErrorCode.GUEST_USER);
        }
        else{
            log.info("jwtTokenProvider getToken {} : {}", tokenType, token);
        }
        return token;
    }

    //토큰 만료일자로 유효 여부 확인
    private boolean isTokenValid(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e){
            return false; //유효하지 않음 (setSigningKey 에서 파싱 시 나는 에러 캐치)
        }
    }

    private long getValidTime(String tokenType){
        switch (tokenType) {
            case REFRESH_TOKEN_HEADER_NAME:
                return REFRESH_TOKEN_VALID_TIME;
            case ACCESS_TOKEN_HEADER_NAME:
                return ACCESS_TOKEN_VALID_TIME;
            default :
                return 0;
        }
    }

}
