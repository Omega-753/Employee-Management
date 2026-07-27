package com.securedevops.employeemanagement.config;

import com.securedevops.employeemanagement.model.User;
import com.securedevops.employeemanagement.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initializeUsers(UserRepository userRepository,
                                      BCryptPasswordEncoder passwordEncoder) {

        return args -> {

            if (userRepository.findByUsername("abhijeet").isEmpty()) {

                User admin = new User();

                admin.setUsername("abhijeet");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setRole("ADMIN");
                admin.setEnabled(true);

                userRepository.save(admin);

                System.out.println("------------------------------------");
                System.out.println("Default ADMIN user created.");
                System.out.println("Username : abhijeet");
                System.out.println("Password : admin123");
                System.out.println("------------------------------------");
            }
        };
    }
}