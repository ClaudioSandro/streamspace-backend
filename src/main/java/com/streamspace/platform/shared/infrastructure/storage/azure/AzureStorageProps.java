package com.streamspace.platform.shared.infrastructure.storage.azure;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "azure.storage")
public record AzureStorageProps(
        String connectionString,
        String containerName,
        String publicBaseUrl
) {}
