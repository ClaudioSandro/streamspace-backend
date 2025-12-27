package com.streamspace.platform.spaces.application.internal.commandservices;

import com.streamspace.platform.spaces.application.internal.outboundservices.acl.ExternalUserService;
import com.streamspace.platform.spaces.domain.model.aggregates.ProductionSpace;
import com.streamspace.platform.spaces.domain.model.commands.*;
import com.streamspace.platform.spaces.domain.model.valueobjects.Address;
import com.streamspace.platform.spaces.domain.model.valueobjects.Money;
import com.streamspace.platform.spaces.domain.model.valueobjects.SpaceType;
import com.streamspace.platform.spaces.domain.services.ProductionSpaceCommandService;
import com.streamspace.platform.spaces.infrastructure.persistence.jpa.repositories.ProductionSpaceRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductionSpaceCommandServiceImpl implements ProductionSpaceCommandService {

    private final ProductionSpaceRepository productionSpaceRepository;
    private final ExternalUserService externalUserService;

    public ProductionSpaceCommandServiceImpl(ProductionSpaceRepository productionSpaceRepository,
                                             ExternalUserService externalUserService) {
        this.productionSpaceRepository = productionSpaceRepository;
        this.externalUserService = externalUserService;
    }

    @Override
    public Optional<ProductionSpace> handle(CreateProductionSpaceCommand command) {
        if (!externalUserService.existsUserById(command.ownerId())) {
            throw new IllegalArgumentException("User with id " + command.ownerId() + " does not exist");
        }

        if (productionSpaceRepository.existsByOwnerIdAndName(command.ownerId(), command.name())) {
            throw new IllegalArgumentException("Production space with name " + command.name() + " already exists for this owner");
        }

        var address = new Address(command.city(), command.district(), command.addressLine());
        var hourlyRate = new Money(command.hourlyRateAmount(), command.hourlyRateCurrency());
        var spaceType = SpaceType.valueOf(command.type().toUpperCase());

        var productionSpace = new ProductionSpace(
                command.ownerId(),
                command.name(),
                command.description(),
                spaceType,
                address,
                hourlyRate,
                command.maxPeople(),
                command.rules()
        );

        productionSpaceRepository.save(productionSpace);
        return Optional.of(productionSpace);
    }

    @Override
    public Optional<ProductionSpace> handle(UpdateProductionSpaceCommand command) {
        var spaceId = command.spaceId();
        var result = productionSpaceRepository.findById(spaceId);

        if (result.isEmpty()) {
            throw new IllegalArgumentException("Production space with id " + spaceId + " does not exist");
        }

        var productionSpace = result.get();
        var spaceType = SpaceType.valueOf(command.type().toUpperCase());
        var address = new Address(command.city(), command.district(), command.addressLine());
        var hourlyRate = new Money(command.hourlyRateAmount(), command.hourlyRateCurrency());

        productionSpace.updateDetails(
                command.name(),
                command.description(),
                spaceType,
                address,
                command.maxPeople(),
                command.rules()
        );
        productionSpace.changeHourlyRate(hourlyRate);

        var updatedSpace = productionSpaceRepository.save(productionSpace);
        return Optional.of(updatedSpace);
    }

    @Override
    public void handle(DeleteProductionSpaceCommand command) {
        var spaceId = command.spaceId();
        if (!productionSpaceRepository.existsById(spaceId)) {
            throw new IllegalArgumentException("Production space with id " + spaceId + " does not exist");
        }
        productionSpaceRepository.deleteById(spaceId);
    }

    @Override
    public Optional<ProductionSpace> handle(ActivateProductionSpaceCommand command) {
        var spaceId = command.spaceId();
        var result = productionSpaceRepository.findById(spaceId);

        if (result.isEmpty()) {
            throw new IllegalArgumentException("Production space with id " + spaceId + " does not exist");
        }

        var productionSpace = result.get();
        productionSpace.activate();
        var updatedSpace = productionSpaceRepository.save(productionSpace);
        return Optional.of(updatedSpace);
    }

    @Override
    public Optional<ProductionSpace> handle(DeactivateProductionSpaceCommand command) {
        var spaceId = command.spaceId();
        var result = productionSpaceRepository.findById(spaceId);

        if (result.isEmpty()) {
            throw new IllegalArgumentException("Production space with id " + spaceId + " does not exist");
        }

        var productionSpace = result.get();
        productionSpace.deactivate();
        var updatedSpace = productionSpaceRepository.save(productionSpace);
        return Optional.of(updatedSpace);
    }
}
