package com.streamspace.platform;

import com.streamspace.platform.shared.infrastructure.storage.azure.AzureStorageProps;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@EnableConfigurationProperties(AzureStorageProps.class)
public class StreamSpaceApplication {

    public static void main(String[] args) {
        SpringApplication.run(StreamSpaceApplication.class, args);
    }

}
