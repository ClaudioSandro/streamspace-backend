package com.streamspace.platform.spaces.interfaces.rest.transform;

import com.streamspace.platform.spaces.domain.model.commands.ReplaceProductionSpaceImageCommand;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public class ReplaceProductionSpaceImageCommandFromResourceAssembler {

    public static ReplaceProductionSpaceImageCommand toCommandFromResource(Long spaceId, MultipartFile file) {
        try {
            return new ReplaceProductionSpaceImageCommand(
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

