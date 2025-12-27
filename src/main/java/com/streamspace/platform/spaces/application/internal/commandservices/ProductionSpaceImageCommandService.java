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
        // 1) Buscar ProductionSpace por id - si no existe → 404
        var productionSpace = productionSpaceRepository.findById(command.spaceId())
                .orElseThrow(() -> new ProductionSpaceNotFoundException(
                        "Production space with id " + command.spaceId() + " does not exist"));

        // 2) Validar MIME type - si no es imagen permitida → 400
        if (!ALLOWED_MIME_TYPES.contains(command.contentType())) {
            throw new InvalidImageTypeException(
                    "Content type '" + command.contentType() + "' is not allowed. " +
                    "Allowed types: " + ALLOWED_MIME_TYPES);
        }

        // 3) Construir objectName: "spaces/{spaceId}/cover" + extensión
        String extension = getExtensionFromContentType(command.contentType());
        String objectName = String.format("spaces/%d/cover%s", command.spaceId(), extension);

        // 4) Llamar a AzureBlobStorageService.upload
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

        // 5) Llamar en el aggregate: productionSpace.changeImageObjectName(objectName)
        productionSpace.changeImageObjectName(objectName);

        // 6) Persistir cambios
        productionSpaceRepository.save(productionSpace);

        // 7) Generar URL firmada: storage.generateReadUrl(objectName, 15)
        String imageUrl = azureBlobStorageService.generateReadUrl(objectName, SAS_URL_EXPIRATION_MINUTES);

        // 8) Retornar resultado con imageObjectName e imageUrl (SAS)
        return new UploadProductionSpaceImageResult(objectName, imageUrl);
    }

    @Transactional
    public UploadProductionSpaceImageResult replaceImage(ReplaceProductionSpaceImageCommand command) {
        // 1) Buscar ProductionSpace por id - si no existe → 404
        var productionSpace = productionSpaceRepository.findById(command.spaceId())
                .orElseThrow(() -> new ProductionSpaceNotFoundException(
                        "Production space with id " + command.spaceId() + " does not exist"));

        // 2) Validar MIME type - si no es imagen permitida → 400
        if (!ALLOWED_MIME_TYPES.contains(command.contentType())) {
            throw new InvalidImageTypeException(
                    "Content type '" + command.contentType() + "' is not allowed. " +
                    "Allowed types: " + ALLOWED_MIME_TYPES);
        }

        // 3) Construir objectName: "spaces/{spaceId}/cover" + extensión
        String extension = getExtensionFromContentType(command.contentType());
        String objectName = String.format("spaces/%d/cover%s", command.spaceId(), extension);

        // 4) Llamar a AzureBlobStorageService.upload (sobrescribe)
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

        // 5) Llamar en el aggregate: productionSpace.changeImageObjectName(objectName)
        productionSpace.changeImageObjectName(objectName);

        // 6) Persistir cambios
        productionSpaceRepository.save(productionSpace);

        // 7) Generar URL firmada: storage.generateReadUrl(objectName, 15)
        String imageUrl = azureBlobStorageService.generateReadUrl(objectName, SAS_URL_EXPIRATION_MINUTES);

        // 8) Retornar resultado con imageObjectName e imageUrl (SAS)
        return new UploadProductionSpaceImageResult(objectName, imageUrl);
    }

    @Transactional
    public void deleteImage(DeleteProductionSpaceImageCommand command) {
        // 1) Buscar ProductionSpace por id - si no existe → 404
        var productionSpace = productionSpaceRepository.findById(command.spaceId())
                .orElseThrow(() -> new ProductionSpaceNotFoundException(
                        "Production space with id " + command.spaceId() + " does not exist"));

        // 2) Si tiene imageObjectName -> eliminar del storage y del aggregate
        productionSpace.getImageObjectName().ifPresent(objectName -> {
            try {
                azureBlobStorageService.delete(objectName);
            } catch (Exception e) {
                // Log pero no lanzar excepción - operación idempotente
            }
            productionSpace.removeImageObjectName();
            productionSpaceRepository.save(productionSpace);
        });
        // Si no tiene imageObjectName, no hacer nada (idempotente)
    }

    private String getExtensionFromContentType(String contentType) {
        return switch (contentType) {
            case "image/jpeg" -> ".jpg";
            case "image/png" -> ".png";
            case "image/webp" -> ".webp";
            default -> "";
        };
    }

    // Custom exceptions
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

