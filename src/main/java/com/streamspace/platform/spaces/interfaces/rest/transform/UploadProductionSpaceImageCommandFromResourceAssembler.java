package com.streamspace.platform.spaces.interfaces.rest.transform;

import com.streamspace.platform.spaces.domain.model.commands.UploadProductionSpaceImageCommand;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public class UploadProductionSpaceImageCommandFromResourceAssembler {

    public static UploadProductionSpaceImageCommand toCommandFromResource(Long spaceId, MultipartFile file) {
        try {
            return new UploadProductionSpaceImageCommand(
                    spaceId,
                    file.getOriginalFilename(),
                    file.getContentType(),
                    file.getSize(),
                    file.getInputStream()
            );
        } catch (IOException e) {
            throw new RuntimeException("Failed to read file input stream", e);
        }
    }
}

