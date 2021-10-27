package ru.ifmo.egalkin.vought.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Created by egalkin
 * Date: 12.10.2021
 */
@Configuration
public class BeanConfig {

    @Bean
    @Scope("singleton")
    PasswordEncoder getEncoder() {
        return new BCryptPasswordEncoder();
    }

}
