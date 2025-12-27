package com.streamspace.platform.shared.interfaces.rest;

import com.streamspace.platform.shared.infrastructure.storage.azure.AzureBlobStorageService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/test-storage")
public class StorageTestController {

    private final AzureBlobStorageService storage;

    public StorageTestController(AzureBlobStorageService storage) {
        this.storage = storage;
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Map<String, String> upload(@RequestPart("file") MultipartFile file) throws Exception {
        String objectName = "test/" + UUID.randomUUID() + "-" + file.getOriginalFilename();
        String url = storage.upload(objectName, file.getInputStream(), file.getSize(), file.getContentType());
        return Map.of("url", url, "objectName", objectName);
    }

    @GetMapping("/signed-url")
    public Map<String, String> signedUrl(@RequestParam String objectName) {
        String url = storage.generateReadUrl(objectName, 15);
        return Map.of("url", url);
    }

}
