package com.example.user_service.security;

import com.example.user_service.service.UserService;
import com.example.user_service.vo.RequestLogin;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.sql.Array;
import java.util.ArrayList;

@Slf4j
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private UserService userService;
    private Environment environment;

    public AuthenticationFilter(AuthenticationManager authenticationManager,
                                   UserService userService, Environment environment) {
        super(authenticationManager);
        this.userService = userService;
        this.environment = environment;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request
                                              , HttpServletResponse response) throws AuthenticationException {
        try {
            // ObjectMapper().readValue()로 전달되어진 inputStream에 더떤 값이 들어가 있을 때 그 값을 
            // 우리가 원하는 Java클래스 타입으로 변경
            RequestLogin creds = new ObjectMapper().readValue(request.getInputStream(), RequestLogin.class);

            // UsernamePasswordAuthenticationToken : 사용자가 입력한 아이디와 비밀번호 값을 Spring Security 에서
            // 사용할 수 있는 형태의 값으로 변환
            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            creds.getEmail(),
                            creds.getPassword(),
                             new ArrayList<>()
                    )
            );


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        super.successfulAuthentication(request, response, chain, authResult);
    }


    //
//    private UserService userService;
//    private Environment environment;
//
//    public AuthenticationFilterNew(AuthenticationManager authenticationManager,
//                                   UserService userService, Environment environment) {
//        super(authenticationManager);
//        this.userService = userService;
//        this.environment = environment;
//    }
//
//    @Override
//    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res)
//            throws AuthenticationException {
//        try {
//
//            RequestLogin creds = new ObjectMapper().readValue(req.getInputStream(), RequestLogin.class);
//
//            return getAuthenticationManager().authenticate(
//                    new UsernamePasswordAuthenticationToken(creds.getEmail(), creds.getPassword(), new ArrayList<>()));
//
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    @Override
//    protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain,
//                                            Authentication auth) throws IOException, ServletException {
//
//        String userName = ((User) auth.getPrincipal()).getUsername();
//        UserDto userDetails = userService.getUserDetailsByEmail(userName);
//
//        byte[] secretKeyBytes = Base64.getEncoder().encode(environment.getProperty("token.secret").getBytes());
//
//        SecretKey secretKey = Keys.hmacShaKeyFor(secretKeyBytes);
//
//        Instant now = Instant.now();
//
//        String token = Jwts.builder()
//                .subject(userDetails.getUserId())
//                .expiration(Date.from(now.plusMillis(Long.parseLong(environment.getProperty("token.expiration_time")))))
//                .issuedAt(Date.from(now))
//                .signWith(secretKey)
//                .compact();
//
//        res.addHeader("token", token);
//        res.addHeader("userId", userDetails.getUserId());
//    }
}
