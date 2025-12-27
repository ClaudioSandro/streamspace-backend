package com.streamspace.platform.spaces.domain.model.commands;

import java.io.InputStream;

public record UploadProductionSpaceImageCommand(
        Long spaceId,
        String originalFilename,
        String contentType,
        long size,
        InputStream inputStream
) {
}

