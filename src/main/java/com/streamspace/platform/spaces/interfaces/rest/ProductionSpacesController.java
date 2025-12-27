package com.streamspace.platform.spaces.interfaces.rest;

import com.streamspace.platform.spaces.application.internal.commandservices.ProductionSpaceImageCommandService;
import com.streamspace.platform.spaces.application.internal.queryservices.ProductionSpaceImageQueryService;
import com.streamspace.platform.spaces.domain.model.commands.ActivateProductionSpaceCommand;
import com.streamspace.platform.spaces.domain.model.commands.DeactivateProductionSpaceCommand;
import com.streamspace.platform.spaces.domain.model.commands.DeleteProductionSpaceCommand;
import com.streamspace.platform.spaces.domain.model.commands.RemoveEquipmentCommand;
import com.streamspace.platform.spaces.domain.model.queries.*;
import com.streamspace.platform.spaces.domain.services.EquipmentCommandService;
import com.streamspace.platform.spaces.domain.services.EquipmentQueryService;
import com.streamspace.platform.spaces.domain.services.ProductionSpaceCommandService;
import com.streamspace.platform.spaces.domain.services.ProductionSpaceQueryService;
import com.streamspace.platform.spaces.interfaces.rest.resources.*;
import com.streamspace.platform.spaces.interfaces.rest.transform.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/production-spaces", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Production Spaces", description = "Production Spaces Management Endpoints")
public class ProductionSpacesController {

    private final ProductionSpaceCommandService productionSpaceCommandService;
    private final ProductionSpaceQueryService productionSpaceQueryService;
    private final EquipmentCommandService equipmentCommandService;
    private final EquipmentQueryService equipmentQueryService;
    private final ProductionSpaceImageCommandService productionSpaceImageCommandService;
    private final ProductionSpaceImageQueryService productionSpaceImageQueryService;

    public ProductionSpacesController(ProductionSpaceCommandService productionSpaceCommandService,
                                      ProductionSpaceQueryService productionSpaceQueryService,
                                      EquipmentCommandService equipmentCommandService,
                                      EquipmentQueryService equipmentQueryService,
                                      ProductionSpaceImageCommandService productionSpaceImageCommandService,
                                      ProductionSpaceImageQueryService productionSpaceImageQueryService) {
        this.productionSpaceCommandService = productionSpaceCommandService;
        this.productionSpaceQueryService = productionSpaceQueryService;
        this.equipmentCommandService = equipmentCommandService;
        this.equipmentQueryService = equipmentQueryService;
        this.productionSpaceImageCommandService = productionSpaceImageCommandService;
        this.productionSpaceImageQueryService = productionSpaceImageQueryService;
    }

    @Operation(summary = "Create a new production space")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Production space created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    public ResponseEntity<ProductionSpaceResource> createProductionSpace(@RequestBody CreateProductionSpaceResource resource) {
        var command = CreateProductionSpaceCommandFromResourceAssembler.toCommandFromResource(resource);
        var productionSpace = productionSpaceCommandService.handle(command);
        if (productionSpace.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        var entity = productionSpace.get();
        var imageUrl = entity.getImageObjectName()
                .map(productionSpaceImageQueryService::generateImageUrl)
                .orElse(null);
        var productionSpaceResource = ProductionSpaceResourceFromEntityAssembler.toResourceFromEntity(entity, imageUrl);
        return new ResponseEntity<>(productionSpaceResource, HttpStatus.CREATED);
    }

    @Operation(summary = "Get all production spaces")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Production spaces retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<List<ProductionSpaceResource>> getAllProductionSpaces() {
        var query = new GetAllProductionSpacesQuery();
        var productionSpaces = productionSpaceQueryService.handle(query);
        var productionSpaceResources = productionSpaces.stream()
                .map(entity -> {
                    var imageUrl = entity.getImageObjectName()
                            .map(productionSpaceImageQueryService::generateImageUrl)
                            .orElse(null);
                    return ProductionSpaceResourceFromEntityAssembler.toResourceFromEntity(entity, imageUrl);
                })
                .toList();
        return ResponseEntity.ok(productionSpaceResources);
    }

    @Operation(summary = "Get production space by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Production space retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Production space not found")
    })
    @GetMapping("/{spaceId}")
    public ResponseEntity<ProductionSpaceResource> getProductionSpaceById(@PathVariable Long spaceId) {
        var query = new GetProductionSpaceByIdQuery(spaceId);
        var productionSpace = productionSpaceQueryService.handle(query);
        if (productionSpace.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var entity = productionSpace.get();
        var imageUrl = entity.getImageObjectName()
                .map(productionSpaceImageQueryService::generateImageUrl)
                .orElse(null);
        var productionSpaceResource = ProductionSpaceResourceFromEntityAssembler.toResourceFromEntity(entity, imageUrl);
        return ResponseEntity.ok(productionSpaceResource);
    }

    @Operation(summary = "Get production spaces by owner ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Production spaces retrieved successfully")
    })
    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<ProductionSpaceResource>> getProductionSpacesByOwnerId(@PathVariable Long ownerId) {
        var query = new GetProductionSpacesByOwnerIdQuery(ownerId);
        var productionSpaces = productionSpaceQueryService.handle(query);
        var productionSpaceResources = productionSpaces.stream()
                .map(entity -> {
                    var imageUrl = entity.getImageObjectName()
                            .map(productionSpaceImageQueryService::generateImageUrl)
                            .orElse(null);
                    return ProductionSpaceResourceFromEntityAssembler.toResourceFromEntity(entity, imageUrl);
                })
                .toList();
        return ResponseEntity.ok(productionSpaceResources);
    }

    @Operation(summary = "Get production spaces by city")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Production spaces retrieved successfully")
    })
    @GetMapping("/city/{city}")
    public ResponseEntity<List<ProductionSpaceResource>> getProductionSpacesByCity(@PathVariable String city) {
        var query = new GetProductionSpacesByCityQuery(city);
        var productionSpaces = productionSpaceQueryService.handle(query);
        var productionSpaceResources = productionSpaces.stream()
                .map(entity -> {
                    var imageUrl = entity.getImageObjectName()
                            .map(productionSpaceImageQueryService::generateImageUrl)
                            .orElse(null);
                    return ProductionSpaceResourceFromEntityAssembler.toResourceFromEntity(entity, imageUrl);
                })
                .toList();
        return ResponseEntity.ok(productionSpaceResources);
    }

    @Operation(summary = "Update a production space")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Production space updated successfully"),
            @ApiResponse(responseCode = "404", description = "Production space not found")
    })
    @PutMapping("/{spaceId}")
    public ResponseEntity<ProductionSpaceResource> updateProductionSpace(@PathVariable Long spaceId,
                                                                         @RequestBody UpdateProductionSpaceResource resource) {
        var command = UpdateProductionSpaceCommandFromResourceAssembler.toCommandFromResource(spaceId, resource);
        var productionSpace = productionSpaceCommandService.handle(command);
        if (productionSpace.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var entity = productionSpace.get();
        var imageUrl = entity.getImageObjectName()
                .map(productionSpaceImageQueryService::generateImageUrl)
                .orElse(null);
        var productionSpaceResource = ProductionSpaceResourceFromEntityAssembler.toResourceFromEntity(entity, imageUrl);
        return ResponseEntity.ok(productionSpaceResource);
    }

    @Operation(summary = "Delete a production space")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Production space deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Production space not found")
    })
    @DeleteMapping("/{spaceId}")
    public ResponseEntity<Void> deleteProductionSpace(@PathVariable Long spaceId) {
        var command = new DeleteProductionSpaceCommand(spaceId);
        productionSpaceCommandService.handle(command);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Activate a production space")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Production space activated successfully"),
            @ApiResponse(responseCode = "404", description = "Production space not found")
    })
    @PatchMapping("/{spaceId}/activate")
    public ResponseEntity<ProductionSpaceResource> activateProductionSpace(@PathVariable Long spaceId) {
        var command = new ActivateProductionSpaceCommand(spaceId);
        var productionSpace = productionSpaceCommandService.handle(command);
        if (productionSpace.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var entity = productionSpace.get();
        var imageUrl = entity.getImageObjectName()
                .map(productionSpaceImageQueryService::generateImageUrl)
                .orElse(null);
        var productionSpaceResource = ProductionSpaceResourceFromEntityAssembler.toResourceFromEntity(entity, imageUrl);
        return ResponseEntity.ok(productionSpaceResource);
    }

    @Operation(summary = "Deactivate a production space")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Production space deactivated successfully"),
            @ApiResponse(responseCode = "404", description = "Production space not found")
    })
    @PatchMapping("/{spaceId}/deactivate")
    public ResponseEntity<ProductionSpaceResource> deactivateProductionSpace(@PathVariable Long spaceId) {
        var command = new DeactivateProductionSpaceCommand(spaceId);
        var productionSpace = productionSpaceCommandService.handle(command);
        if (productionSpace.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var entity = productionSpace.get();
        var imageUrl = entity.getImageObjectName()
                .map(productionSpaceImageQueryService::generateImageUrl)
                .orElse(null);
        var productionSpaceResource = ProductionSpaceResourceFromEntityAssembler.toResourceFromEntity(entity, imageUrl);
        return ResponseEntity.ok(productionSpaceResource);
    }

    // ====== Image Upload Endpoint ======

    @Operation(summary = "Upload cover image for a production space")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Image uploaded successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid image type"),
            @ApiResponse(responseCode = "404", description = "Production space not found")
    })
    @PostMapping(value = "/{spaceId}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UploadProductionSpaceImageResource> uploadImage(
            @PathVariable Long spaceId,
            @RequestParam("file") MultipartFile file) {
        var command = UploadProductionSpaceImageCommandFromResourceAssembler.toCommandFromResource(spaceId, file);
        var result = productionSpaceImageCommandService.uploadImage(command);
        var resource = UploadProductionSpaceImageResourceFromResultAssembler.toResourceFromResult(result);
        return ResponseEntity.ok(resource);
    }

    @Operation(summary = "Replace cover image for a production space")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Image replaced successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid image type"),
            @ApiResponse(responseCode = "404", description = "Production space not found")
    })
    @PutMapping(value = "/{spaceId}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UploadProductionSpaceImageResource> replaceImage(
            @PathVariable Long spaceId,
            @RequestParam("file") MultipartFile file) {
        var command = ReplaceProductionSpaceImageCommandFromResourceAssembler.toCommandFromResource(spaceId, file);
        var result = productionSpaceImageCommandService.replaceImage(command);
        var resource = UploadProductionSpaceImageResourceFromResultAssembler.toResourceFromResult(result);
        return ResponseEntity.ok(resource);
    }

    @Operation(summary = "Delete cover image from a production space")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Image deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Production space not found")
    })
    @DeleteMapping("/{spaceId}/image")
    public ResponseEntity<Void> deleteImage(@PathVariable Long spaceId) {
        var command = new com.streamspace.platform.spaces.domain.model.commands.DeleteProductionSpaceImageCommand(spaceId);
        productionSpaceImageCommandService.deleteImage(command);
        return ResponseEntity.noContent().build();
    }

    // ====== Equipment Endpoints ======

    @Operation(summary = "Get all equipment for a production space")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Equipment retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Production space not found")
    })
    @GetMapping("/{spaceId}/equipment")
    public ResponseEntity<List<EquipmentResource>> getEquipmentBySpaceId(@PathVariable Long spaceId) {
        var query = new GetEquipmentBySpaceIdQuery(spaceId);
        var equipment = equipmentQueryService.handle(query);
        var equipmentResources = equipment.stream()
                .map(EquipmentResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(equipmentResources);
    }

    @Operation(summary = "Add equipment to a production space")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Equipment added successfully"),
            @ApiResponse(responseCode = "404", description = "Production space not found")
    })
    @PostMapping("/{spaceId}/equipment")
    public ResponseEntity<EquipmentResource> addEquipment(@PathVariable Long spaceId,
                                                          @RequestBody AddEquipmentResource resource) {
        var command = AddEquipmentCommandFromResourceAssembler.toCommandFromResource(spaceId, resource);
        var equipment = equipmentCommandService.handle(command);
        if (equipment.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var equipmentResource = EquipmentResourceFromEntityAssembler.toResourceFromEntity(equipment.get());
        return new ResponseEntity<>(equipmentResource, HttpStatus.CREATED);
    }

    @Operation(summary = "Update equipment in a production space")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Equipment updated successfully"),
            @ApiResponse(responseCode = "404", description = "Equipment or production space not found")
    })
    @PutMapping("/{spaceId}/equipment/{equipmentId}")
    public ResponseEntity<EquipmentResource> updateEquipment(@PathVariable Long spaceId,
                                                             @PathVariable Long equipmentId,
                                                             @RequestBody UpdateEquipmentResource resource) {
        var command = UpdateEquipmentCommandFromResourceAssembler.toCommandFromResource(spaceId, equipmentId, resource);
        var equipment = equipmentCommandService.handle(command);
        if (equipment.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var equipmentResource = EquipmentResourceFromEntityAssembler.toResourceFromEntity(equipment.get());
        return ResponseEntity.ok(equipmentResource);
    }

    @Operation(summary = "Remove equipment from a production space")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Equipment removed successfully"),
            @ApiResponse(responseCode = "404", description = "Equipment or production space not found")
    })
    @DeleteMapping("/{spaceId}/equipment/{equipmentId}")
    public ResponseEntity<Void> removeEquipment(@PathVariable Long spaceId,
                                                @PathVariable Long equipmentId) {
        var command = new RemoveEquipmentCommand(spaceId, equipmentId);
        equipmentCommandService.handle(command);
        return ResponseEntity.noContent().build();
    }
}

