package com.streamspace.platform.shared.infrastructure.storage.azure;

import com.azure.storage.blob.*;
import com.azure.storage.blob.models.BlobHttpHeaders;
import org.springframework.stereotype.Service;
import com.azure.storage.blob.sas.BlobSasPermission;
import com.azure.storage.blob.sas.BlobServiceSasSignatureValues;

import java.time.OffsetDateTime;

import java.io.InputStream;

@Service
public class AzureBlobStorageService {

    private final BlobContainerClient containerClient;
    private final AzureStorageProps props;

    public AzureBlobStorageService(AzureStorageProps props) {
        this.props = props;

        BlobServiceClient serviceClient = new BlobServiceClientBuilder()
                .connectionString(props.connectionString())
                .buildClient();

        this.containerClient = serviceClient.getBlobContainerClient(props.containerName());

        if (!this.containerClient.exists()) {
            this.containerClient.create();
        }
    }

    public String upload(String objectName, InputStream data, long length, String contentType) {
        BlobClient blobClient = containerClient.getBlobClient(objectName);
        blobClient.upload(data, length, true);

        if (contentType != null && !contentType.isBlank()) {
            blobClient.setHttpHeaders(new BlobHttpHeaders().setContentType(contentType));
        }

        return props.publicBaseUrl() + "/" + props.containerName() + "/" + objectName;
    }

    public String generateReadUrl(String objectName, int minutes) {
        var blobClient = containerClient.getBlobClient(objectName);

        var permission = new BlobSasPermission().setReadPermission(true);

        var values = new BlobServiceSasSignatureValues(
                OffsetDateTime.now().plusMinutes(minutes),
                permission
        );

        String sas = blobClient.generateSas(values);
        return blobClient.getBlobUrl() + "?" + sas;
    }

    public void delete(String objectName) {
        var blobClient = containerClient.getBlobClient(objectName);
        if (blobClient.exists()) {
            blobClient.delete();
        }
    }
}
