package com.tinusj.ultima.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

@Slf4j
@Configuration
public class FirebaseConfig {

    @PostConstruct
    public void init() {
        log.info("Firebase initialized.");
    }

    @Bean
    public FirebaseAuth firebaseAuth() throws IOException {
        if (FirebaseApp.getApps().isEmpty()) {
            GoogleCredentials googleCredentials = GoogleCredentials
                    .fromStream(new ClassPathResource("firebase-service-account.json").getInputStream());

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(googleCredentials)
                    .build();

            FirebaseApp.initializeApp(options);
        }

        return FirebaseAuth.getInstance();
    }
}