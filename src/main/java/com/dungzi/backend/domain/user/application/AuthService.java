package com.dungzi.backend.domain.user.application;

import com.dungzi.backend.domain.user.domain.User;
import com.dungzi.backend.global.common.response.exception.AuthException;
import com.dungzi.backend.global.common.response.code.AuthErrorCode;
import com.dungzi.backend.global.common.response.code.ValidErrorCode;
import com.dungzi.backend.global.common.response.exception.ValidException;
import com.dungzi.backend.global.config.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.dungzi.backend.domain.user.dao.UserDao;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@RequiredArgsConstructor
@Service
@Slf4j
public class AuthService {
    private final UserDao userDao;
    private final JwtTokenProvider jwtTokenProvider;

    private final String ROLE_USER = "ROLE_USER"; //TODO : 추후 다른 권한 이름들 정리해서 추가 (공인중개사 계정 등)
    private final String NICKNAME_RULE_REGEX = "^[0-9A-Za-zㄱ-ㅎㅏ-ㅣ가-힣][0-9A-Za-zㄱ-ㅎㅏ-ㅣ가-힣\\.\\/\\_\\[\\]]*[0-9A-Za-zㄱ-ㅎㅏ-ㅣ가-힣]$";


    public User getUserFromSecurity() {
        log.info("[SERVICE] getUserFromSecurity");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication instanceof AnonymousAuthenticationToken) {
            log.info("User in spring security is anonymous");
            throw new AuthException(AuthErrorCode.GUEST_USER);
        }
        else{
            return (User) authentication.getPrincipal();
        }

    }

    public User getUserByUuid(String uuid) throws AuthException {
        log.info("[SERVICE] getUserByUuid");
        return userDao.findByUserId(UUID.fromString(uuid))
                .orElseThrow(() -> new AuthException(AuthErrorCode.NOT_EXIST_USER));
    }

    // TODO : 설계가 바뀌면서 더러워졌음. 한 메서드는 한 가지 기능만 하도록 토큰관련 코드 개선할 것
    public void setTokenCookieAndSecurityByUser(HttpServletResponse httpServletResponse, User user) {
        log.info("[SERVICE] setTokenCookieAndSecurityByUser");
        Map<String, Cookie> cookieMap = setCookieTokenInResponse(httpServletResponse, user);
        Authentication authentication = jwtTokenProvider.getAuthenticationByToken(cookieMap.get("access").getValue());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private Map<String, Cookie> createCookieTokenByUser(User user) {
        String accessToken = jwtTokenProvider.createToken(user.getUserId().toString(), user.getRoles(), jwtTokenProvider.ACCESS_TOKEN_HEADER_NAME);
        String refreshToken = jwtTokenProvider.createToken(user.getUserId().toString(), user.getRoles(), jwtTokenProvider.REFRESH_TOKEN_HEADER_NAME);

        Map<String, Cookie> cookieMap = new HashMap();
        cookieMap.put("access", new Cookie(jwtTokenProvider.ACCESS_TOKEN_HEADER_NAME, accessToken));
        cookieMap.get("access").setMaxAge(jwtTokenProvider.ACCESS_COOKIE_MAX_AGE);

        cookieMap.put("refresh", new Cookie(jwtTokenProvider.REFRESH_TOKEN_HEADER_NAME, refreshToken));
        cookieMap.get("refresh").setMaxAge(jwtTokenProvider.REFRESH_COOKIE_MAX_AGE);

        cookieMap.forEach( (key, cookie) -> {
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            cookie.setSecure(true); //https 상에서만 동작
        });

        return cookieMap;
    }

    public Map<String, Cookie> setCookieTokenInResponse(HttpServletResponse httpServletResponse, User user){
        log.info("[SERVICE] setCookieTokenInResponse");
        Map<String, Cookie> cookieMap = createCookieTokenByUser(user);
        cookieMap.forEach( (key, cookie) -> {
            httpServletResponse.addCookie(cookie);
        });
        return cookieMap;
    }

    public void removeCookieToken(HttpServletResponse servletResponse) {
        log.info("[SERVICE] removeCookieToken");

        List<Cookie> cookieList = new ArrayList<>();
        cookieList.add(new Cookie(jwtTokenProvider.ACCESS_TOKEN_HEADER_NAME, null));
        cookieList.add(new Cookie(jwtTokenProvider.REFRESH_TOKEN_HEADER_NAME, null));

        for(Cookie cookie : cookieList) {
            cookie.setMaxAge(0);
            cookie.setPath("/");
            servletResponse.addCookie(cookie);
        }
    }


    public User login(User user) throws AuthException {
        log.info("[SERVICE] login");
        Optional<User> userOptional = isExistUser(user);

        if(userOptional.isPresent()){
            log.info("login : Existing user. Login");
            return userOptional.get();
        }else{
            log.info("login : User not exist.");
            throw new AuthException(AuthErrorCode.NOT_EXIST_USER);
        }
    }

    private Optional<User> isExistUser(User user) {
        return userDao.findByEmail(user.getEmail());
    }


    public User signUpByKakao(User kakaoUser, String nickname) {
        log.info("[SERVICE] signUpByKakao");
        Optional<User> userOptional = isExistUser(kakaoUser);

        if(userOptional.isPresent()){
            throw new AuthException(AuthErrorCode.ALREADY_EXIST_USER);
        }else{
            kakaoUser.updateNickname(nickname);
            kakaoUser.updateRoles(Collections.singletonList(ROLE_USER));
            return userDao.save(kakaoUser);
        }
    }

    public void checkNicknameUnique(String nickname) {
        log.info("[SERVICE] isNicknameExist");
        userDao.findByNickname(nickname)
                .ifPresent(n -> {
                    throw new ValidException(ValidErrorCode.NICKNAME_ALREADY_EXIST);
                });
    }

    public void validateNickname(String nickname) {
        if(nickname.length() < 2 || nickname.length() > 12){
            log.info("validateNickname : too long or too short");
            throw new ValidException(ValidErrorCode.NICKNAME_FORBIDDEN);
        }

        if(! nickname.matches(NICKNAME_RULE_REGEX)) {
            log.info("validateNickname : do not fit with nickname rule");
            throw new ValidException(ValidErrorCode.NICKNAME_FORBIDDEN);
        }

        //TODO : isNicknameExist() 메서드를 재사용 하고 싶었으나 그러면 내부 호출된 함수라 @Transactional 적용이 안된다고 함..
        if(userDao.findByNickname(nickname).isPresent()) {
            log.info("validateNickname : NICKNAME_ALREADY_EXIST");
            throw new ValidException(ValidErrorCode.NICKNAME_ALREADY_EXIST);
        }
    }


    //TODO  추후 제거
    @Transactional
    public User userLoginWithSignUp(User user) {
        log.info("[SERVICE] userLoginWithSignUp");

        Optional<User> userOptional = userDao.findByEmail(user.getEmail());

        if(userOptional.isEmpty()){
            log.info("userLoginWithSignUp : User not exist. Sign up and login");
            user.updateRoles(Collections.singletonList(ROLE_USER));
            return userDao.save(user);
        }else{
            log.info("userLoginWithSignUp : Existing user. Login");
            return userOptional.get();
        }
    }

}