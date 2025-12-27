package com.streamspace.platform.spaces.interfaces.rest;

import com.streamspace.platform.spaces.application.internal.commandservices.ProductionSpaceImageCommandService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice(assignableTypes = ProductionSpacesController.class)
public class ProductionSpaceExceptionHandler {

    @ExceptionHandler(ProductionSpaceImageCommandService.ProductionSpaceNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleProductionSpaceNotFound(
            ProductionSpaceImageCommandService.ProductionSpaceNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(ProductionSpaceImageCommandService.InvalidImageTypeException.class)
    public ResponseEntity<Map<String, String>> handleInvalidImageType(
            ProductionSpaceImageCommandService.InvalidImageTypeException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(ProductionSpaceImageCommandService.ImageUploadException.class)
    public ResponseEntity<Map<String, String>> handleImageUploadException(
            ProductionSpaceImageCommandService.ImageUploadException ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", ex.getMessage()));
    }
}

