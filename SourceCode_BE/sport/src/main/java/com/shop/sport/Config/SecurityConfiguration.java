package com.shop.sport.Config;

import com.shop.sport.auth.JwtAuthenticationEntryPoint;
import io.jsonwebtoken.Jwt;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    private final JwtAnthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .headers((headers) ->
                        headers
                                .defaultsDisabled()
                                .frameOptions(withDefaults())
                );

        http
                .csrf(csrf -> csrf.disable());
        http
                .authorizeRequests()
                .requestMatchers("/api/v1/auth/*","/v3/api-docs","/api/v1/user/update/**","/api/v1/product/update/*"
                        ,"/api/v1/user/forget-password/*", "/api/v1/product/*","/api/v1/product/delete/*")
                .permitAll()
                .anyRequest()
                .authenticated();
        http
                .exceptionHandling(exception->exception.authenticationEntryPoint(jwtAuthenticationEntryPoint))
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider( authenticationProvider);
        http
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);


                return http.build();
    }
}
