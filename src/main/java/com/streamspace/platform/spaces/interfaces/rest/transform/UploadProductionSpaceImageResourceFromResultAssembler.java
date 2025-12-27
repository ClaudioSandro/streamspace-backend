package com.streamspace.platform.spaces.interfaces.rest.transform;

import com.streamspace.platform.spaces.application.internal.commandservices.UploadProductionSpaceImageResult;
import com.streamspace.platform.spaces.interfaces.rest.resources.UploadProductionSpaceImageResource;

public class UploadProductionSpaceImageResourceFromResultAssembler {

    public static UploadProductionSpaceImageResource toResourceFromResult(UploadProductionSpaceImageResult result) {
        return new UploadProductionSpaceImageResource(
                result.imageObjectName(),
                result.imageUrl()
        );
    }
}

