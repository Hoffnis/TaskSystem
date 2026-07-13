package com.taks.demo.config;

import com.taks.demo.entity.User;
import com.taks.demo.enums.Role;
import com.taks.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final UserRepository repository;
    private final PasswordEncoder encoder;

    @Bean
    CommandLineRunner init() {

        return args -> {

            if(repository.count() == 0){

                User user = User.builder()
                        .username("admin")
                        .password(encoder.encode("123456"))
                        .role(Role.ADMIN)
                        .build();

                repository.save(user);

            }

        };

    }

}
