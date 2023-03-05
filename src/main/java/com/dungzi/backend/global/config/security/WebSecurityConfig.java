package com.dungzi.backend.global.config.security;

import com.dungzi.backend.domain.user.application.AuthService;
import com.dungzi.backend.global.config.security.jwt.JwtAuthenticationFilter;
import com.dungzi.backend.global.config.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthService authService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable() //rest api 만을 고려하여 기본 설정을 해제함
                .csrf().disable() //csrf 보안 토큰 disable // TODO : ??
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                // STATELESS : 세션이 아닌 토큰 기반 인증을 사용하므로 세션을 사용하지 않음
                .and()
                .authorizeRequests() //요청에 대한 사용권한 체크
//                .antMatchers("/admin/**").hasRole("ROLE_ADMIN")
//                .antMatchers("api/users/**").hasRole("ROLE_USER")
//                .antMatchers("/api/v1/login").permitAll() //그 외 요청들은 누구나 접근 허용
//                .antMatchers("/api/v1/login/**").hasRole("USER")
                .antMatchers("/", "/**").permitAll() //그 외 요청들은 누구나 접근 허용
//                .antMatchers("/api/test/hello").permitAll()
//                .and()
//                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider, authService),
//                        UsernamePasswordAuthenticationFilter.class)
        //JwtAuthenticationFilter 를 UsernamePasswordAuthenticationFilter 전에 넣음
        ;
    }
}
