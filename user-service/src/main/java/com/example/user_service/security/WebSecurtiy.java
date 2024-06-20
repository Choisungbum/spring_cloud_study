package com.example.user_service.security;

import com.example.user_service.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.IpAddressMatcher;

import java.util.function.Supplier;

@Configuration
// EnableWebSecurity 에노테이션은 기본적으로 debug가
// 비활성화되어있습니다. debug값을 true로 주고 실행하면 다음과 같이 콘솔에 출력
@EnableWebSecurity
public class WebSecurtiy {

    private UserService userService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private Environment env;
    // 해당 클래스를 통해 authenticationManager 파라미터 전달
    private final AuthenticationConfiguration authenticationConfiguration;

    public static final String ALLOWED_IP_ADDRESS = "127.0.0.1";
    public static final String SUBNET = "/32";
    public static final IpAddressMatcher ALLOWED_IP_ADDRESS_MATCHER = new IpAddressMatcher(ALLOWED_IP_ADDRESS + SUBNET);

    public WebSecurtiy(Environment env, UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder, AuthenticationConfiguration authenticationConfiguration) {
        this.env = env;
        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.authenticationConfiguration = authenticationConfiguration;
    }

    @Bean
    protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
//         Configure AuthenticationManagerBuilder
//        AuthenticationManagerBuilder authenticationManagerBuilder =
//                http.getSharedObject(AuthenticationManagerBuilder.class);
//        authenticationManagerBuilder.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder);
//
//        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();

//        http.csrf(AbstractHttpConfigurer::disable);

        http.authorizeHttpRequests((authz) ->
                authz.anyRequest().permitAll())
                .csrf(csrf -> csrf.disable()) // CSRF 보호 비활성화 (옵션)
                .formLogin(form -> form.disable()) // 기본 로그인 화면 비활성화
                .httpBasic(httpBasic -> httpBasic.disable()) // HTTP Basic 인증 비활성화
                .logout(logout -> logout.disable()) // 로그아웃 비활성화
                .addFilter(getAuthenticationFilter(authenticationConfiguration.getAuthenticationManager()))
        ;

        http.headers((headers) -> headers.frameOptions((frameOptions) -> frameOptions.sameOrigin()));


//        http.authorizeHttpRequests((authz) -> authz
//                                .requestMatchers(new AntPathRequestMatcher("/actuator/**")).permitAll()
//                                .requestMatchers(new AntPathRequestMatcher("/h2-console/**")).permitAll()
//                                .requestMatchers(new AntPathRequestMatcher("/users", "POST")).permitAll()
//                        .requestMatchers("/**").access(this::hasIpAddress)
//                        .anyRequest().authenticated()
//                        .requestMatchers("/**").access(
//                                new WebExpressionAuthorizationManager("hasIpAddress('127.0.0.1') or hasIpAddress('121.162.233.183')"))
//                );
//        http.addFilter(getAuthenticationFilter(authenticationManager));
//        http.headers((headers) -> headers.frameOptions((frameOptions) -> frameOptions.sameOrigin()));

        return http.getOrBuild();
    }

    private AuthorizationDecision hasIpAddress(Supplier<Authentication> authentication, RequestAuthorizationContext object) {
        return new AuthorizationDecision(ALLOWED_IP_ADDRESS_MATCHER.matches(object.getRequest()));
    }

    private AuthenticationFilter getAuthenticationFilter(AuthenticationManager authenticationManager) throws Exception {
        return new AuthenticationFilter(authenticationManager, userService, env);
    }
}
