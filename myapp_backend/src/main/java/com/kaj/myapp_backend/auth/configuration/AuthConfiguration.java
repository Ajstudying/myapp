package com.kaj.myapp_backend.auth.configuration;

import com.kaj.myapp_backend.auth.util.HashUtil;
import com.kaj.myapp_backend.auth.util.JwtUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthConfiguration {
    @Bean
    public HashUtil hashUtil(){
        return new HashUtil();
    }
    @Bean
    public JwtUtil jwtUtil(){
        return new JwtUtil();
    }

}