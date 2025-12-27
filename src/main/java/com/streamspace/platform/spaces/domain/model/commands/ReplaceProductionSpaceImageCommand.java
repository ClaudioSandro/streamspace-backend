package com.streamspace.platform.spaces.domain.model.commands;

import java.io.InputStream;

public record ReplaceProductionSpaceImageCommand(
        Long spaceId,
        String originalFilename,
        String contentType,
        long size,
        InputStream inputStream
) {
}
