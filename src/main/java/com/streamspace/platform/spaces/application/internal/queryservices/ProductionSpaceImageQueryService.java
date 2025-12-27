package com.streamspace.platform.spaces.application.internal.queryservices;

import com.streamspace.platform.shared.infrastructure.storage.azure.AzureBlobStorageService;
import org.springframework.stereotype.Service;

@Service
public class ProductionSpaceImageQueryService {

    private static final int SAS_URL_EXPIRATION_MINUTES = 15;

    private final AzureBlobStorageService azureBlobStorageService;

    public ProductionSpaceImageQueryService(AzureBlobStorageService azureBlobStorageService) {
        this.azureBlobStorageService = azureBlobStorageService;
    }

    /**
     * Generates a signed URL (SAS) for reading an image.
     * Returns null if imageObjectName is null or blank.
     */
    public String generateImageUrl(String imageObjectName) {
        if (imageObjectName == null || imageObjectName.isBlank()) {
            return null;
        }
        return azureBlobStorageService.generateReadUrl(imageObjectName, SAS_URL_EXPIRATION_MINUTES);
    }
}

