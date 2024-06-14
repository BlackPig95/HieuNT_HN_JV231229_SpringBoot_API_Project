package com.ra.hieunt_hn_jv231229_project_module4_api.security.config;

import com.ra.hieunt_hn_jv231229_project_module4_api.model.constants.RoleName;
import com.ra.hieunt_hn_jv231229_project_module4_api.security.exception.AccessDenied;
import com.ra.hieunt_hn_jv231229_project_module4_api.security.exception.JwtEntryPoint;
import com.ra.hieunt_hn_jv231229_project_module4_api.security.jwt.JwtTokenFilter;
import com.ra.hieunt_hn_jv231229_project_module4_api.security.principal.UserDetailServiceCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig
{
    private final UserDetailServiceCustom userDetailServiceCustom;
    private final JwtTokenFilter jwtTokenFilter;
    private final JwtEntryPoint jwtEntryPoint;
    private final AccessDenied accessDenied;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
    {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(url -> url.requestMatchers("/api.myservice.com/v1/admin/**").hasAuthority(RoleName.ROLE_ADMIN.name())
                        .requestMatchers("/api.myservice.com/v1/user/**").hasAuthority(RoleName.ROLE_USER.name())
                        .anyRequest().permitAll())
                .authenticationProvider(authProvider())
                .exceptionHandling(ex ->
                        ex.authenticationEntryPoint(jwtEntryPoint)
                                .accessDeniedHandler(accessDenied))
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authProvider()
    {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userDetailServiceCustom);
        return daoAuthenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception
    {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
