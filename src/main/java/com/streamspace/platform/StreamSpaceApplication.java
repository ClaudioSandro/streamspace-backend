package com.streamspace.platform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class StreamSpaceApplication {

    public static void main(String[] args) {
        SpringApplication.run(StreamSpaceApplication.class, args);
    }

}
