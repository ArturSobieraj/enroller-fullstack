package com.company.enroller;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class EnrollerConf {

    @Bean("BCryptEncoder")
    public PasswordEncoder getEncoder() {
        return new BCryptPasswordEncoder();
    }
}
