package com.streamspace.platform.spaces.application.internal.commandservices;

import com.streamspace.platform.shared.infrastructure.storage.azure.AzureBlobStorageService;
import com.streamspace.platform.spaces.domain.model.commands.DeleteProductionSpaceImageCommand;
import com.streamspace.platform.spaces.domain.model.commands.ReplaceProductionSpaceImageCommand;
import com.streamspace.platform.spaces.domain.model.commands.UploadProductionSpaceImageCommand;
import com.streamspace.platform.spaces.infrastructure.persistence.jpa.repositories.ProductionSpaceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
public class ProductionSpaceImageCommandService {

    private static final Set<String> ALLOWED_MIME_TYPES = Set.of(
            "image/jpeg",
            "image/png",
            "image/webp"
    );

    private static final int SAS_URL_EXPIRATION_MINUTES = 15;

    private final ProductionSpaceRepository productionSpaceRepository;
    private final AzureBlobStorageService azureBlobStorageService;

    public ProductionSpaceImageCommandService(ProductionSpaceRepository productionSpaceRepository,
                                               AzureBlobStorageService azureBlobStorageService) {
        this.productionSpaceRepository = productionSpaceRepository;
        this.azureBlobStorageService = azureBlobStorageService;
    }

    @Transactional
    public UploadProductionSpaceImageResult uploadImage(UploadProductionSpaceImageCommand command) {
        var productionSpace = productionSpaceRepository.findById(command.spaceId())
                .orElseThrow(() -> new ProductionSpaceNotFoundException(
                        "Production space with id " + command.spaceId() + " does not exist"));

        if (!ALLOWED_MIME_TYPES.contains(command.contentType())) {
            throw new InvalidImageTypeException(
                    "Content type '" + command.contentType() + "' is not allowed. " +
                    "Allowed types: " + ALLOWED_MIME_TYPES);
        }

        String extension = getExtensionFromContentType(command.contentType());
        String objectName = String.format("spaces/%d/cover%s", command.spaceId(), extension);

        try {
            azureBlobStorageService.upload(
                    objectName,
                    command.inputStream(),
                    command.size(),
                    command.contentType()
            );
        } catch (Exception e) {
            throw new ImageUploadException("Failed to upload image: " + e.getMessage(), e);
        }

        productionSpace.changeImageObjectName(objectName);

        productionSpaceRepository.save(productionSpace);

        String imageUrl = azureBlobStorageService.generateReadUrl(objectName, SAS_URL_EXPIRATION_MINUTES);

        return new UploadProductionSpaceImageResult(objectName, imageUrl);
    }

    @Transactional
    public UploadProductionSpaceImageResult replaceImage(ReplaceProductionSpaceImageCommand command) {
        var productionSpace = productionSpaceRepository.findById(command.spaceId())
                .orElseThrow(() -> new ProductionSpaceNotFoundException(
                        "Production space with id " + command.spaceId() + " does not exist"));

        if (!ALLOWED_MIME_TYPES.contains(command.contentType())) {
            throw new InvalidImageTypeException(
                    "Content type '" + command.contentType() + "' is not allowed. " +
                    "Allowed types: " + ALLOWED_MIME_TYPES);
        }

        String extension = getExtensionFromContentType(command.contentType());
        String objectName = String.format("spaces/%d/cover%s", command.spaceId(), extension);

        try {
            azureBlobStorageService.upload(
                    objectName,
                    command.inputStream(),
                    command.size(),
                    command.contentType()
            );
        } catch (Exception e) {
            throw new ImageUploadException("Failed to replace image: " + e.getMessage(), e);
        }

        productionSpace.changeImageObjectName(objectName);

        productionSpaceRepository.save(productionSpace);

        String imageUrl = azureBlobStorageService.generateReadUrl(objectName, SAS_URL_EXPIRATION_MINUTES);

        return new UploadProductionSpaceImageResult(objectName, imageUrl);
    }

    @Transactional
    public void deleteImage(DeleteProductionSpaceImageCommand command) {
        var productionSpace = productionSpaceRepository.findById(command.spaceId())
                .orElseThrow(() -> new ProductionSpaceNotFoundException(
                        "Production space with id " + command.spaceId() + " does not exist"));

        productionSpace.getImageObjectName().ifPresent(objectName -> {
            try {
                azureBlobStorageService.delete(objectName);
            } catch (Exception e) {
            }
            productionSpace.removeImageObjectName();
            productionSpaceRepository.save(productionSpace);
        });
    }

    private String getExtensionFromContentType(String contentType) {
        return switch (contentType) {
            case "image/jpeg" -> ".jpg";
            case "image/png" -> ".png";
            case "image/webp" -> ".webp";
            default -> "";
        };
    }

    public static class ProductionSpaceNotFoundException extends RuntimeException {
        public ProductionSpaceNotFoundException(String message) {
            super(message);
        }
    }

    public static class InvalidImageTypeException extends RuntimeException {
        public InvalidImageTypeException(String message) {
            super(message);
        }
    }

    public static class ImageUploadException extends RuntimeException {
        public ImageUploadException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}

