package com.mario.alumni.app.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()
                .formLogin()
                .loginPage("/login") // S
                .loginProcessingUrl("/perform_login") // Specify the URL to process the login
                .defaultSuccessUrl("/", true) // Set the default success URL after login
                .failureUrl("/login?error=true") // Set the URL for login failures
                .and()
                .logout()
                .logoutSuccessUrl("/")
                .and()
                .authorizeHttpRequests(
                        (authz) -> authz
                                .requestMatchers(req -> req.getServletPath().equals("/login")).permitAll()
                                .anyRequest().authenticated()
                );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
