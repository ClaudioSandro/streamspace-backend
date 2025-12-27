package com.streamspace.platform.spaces.domain.model.aggregates;

import com.streamspace.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import com.streamspace.platform.spaces.domain.model.entities.Equipment;
import com.streamspace.platform.spaces.domain.model.valueobjects.Address;
import com.streamspace.platform.spaces.domain.model.valueobjects.Money;
import com.streamspace.platform.spaces.domain.model.valueobjects.SpaceStatus;
import com.streamspace.platform.spaces.domain.model.valueobjects.SpaceType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Getter
@Entity
@Table(name = "production_spaces")
public class ProductionSpace extends AuditableAbstractAggregateRoot<ProductionSpace> {

    @NotNull(message = "Owner ID is required")
    @Column(name = "owner_id", nullable = false)
    private Long ownerId;

    @NotBlank(message = "Name is required")
    @Size(max = 120, message = "Name must be at most 120 characters")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "Description is required")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @NotNull(message = "Type is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SpaceType type;

    @NotNull(message = "Status is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SpaceStatus status;

    @NotNull(message = "Address is required")
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "city", column = @Column(name = "address_city", nullable = false)),
            @AttributeOverride(name = "district", column = @Column(name = "address_district", nullable = false)),
            @AttributeOverride(name = "addressLine", column = @Column(name = "address_line", nullable = false))
    })
    private Address address;

    @NotNull(message = "Hourly rate is required")
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "hourly_rate_amount", nullable = false)),
            @AttributeOverride(name = "currency", column = @Column(name = "hourly_rate_currency", nullable = false, length = 3))
    })
    private Money hourlyRate;

    @Min(value = 1, message = "Max people must be > 0")
    @Column(name = "max_people", nullable = false)
    private int maxPeople;

    @Column(columnDefinition = "TEXT")
    private String rules;

    @Column(name = "image_object_name")
    private String imageObjectName;

    @OneToMany(mappedBy = "productionSpace", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Equipment> equipment = new ArrayList<>();

    protected ProductionSpace() {
    }

    public ProductionSpace(Long ownerId,
                           String name,
                           String description,
                           SpaceType type,
                           Address address,
                           Money hourlyRate,
                           int maxPeople,
                           String rules) {
        this.ownerId = ownerId;
        setName(name);
        setDescription(description);
        this.type = type;
        this.address = address;
        this.hourlyRate = hourlyRate;
        setMaxPeople(maxPeople);
        this.rules = (rules == null) ? "" : rules.trim();
        this.status = SpaceStatus.ACTIVE;
    }

    // ====== Getters para colecciones inmutables ======
    public List<Equipment> getEquipment() {
        return Collections.unmodifiableList(equipment);
    }

    // ====== Behavior ======
    public void activate() {
        this.status = SpaceStatus.ACTIVE;
    }

    public void deactivate() {
        this.status = SpaceStatus.INACTIVE;
    }

    public boolean isActive() {
        return this.status == SpaceStatus.ACTIVE;
    }

    public boolean belongsTo(Long userId) {
        return userId != null && userId.equals(this.ownerId);
    }

    public void updateDetails(String name,
                              String description,
                              SpaceType type,
                              Address address,
                              int maxPeople,
                              String rules) {
        setName(name);
        setDescription(description);
        this.type = type;
        this.address = address;
        setMaxPeople(maxPeople);
        this.rules = (rules == null) ? "" : rules.trim();
    }

    public void changeHourlyRate(Money newRate) {
        if (newRate == null) {
            throw new IllegalArgumentException("newRate is required");
        }
        this.hourlyRate = newRate;
    }

    public Equipment addEquipment(String name, int quantity) {
        var item = new Equipment(this, name, quantity);
        this.equipment.add(item);
        return item;
    }

    public void updateEquipment(Long equipmentId, String newName, int newQty) {
        var item = findEquipmentOrThrow(equipmentId);
        item.rename(newName);
        item.changeQuantity(newQty);
    }

    public void removeEquipment(Long equipmentId) {
        var item = findEquipmentOrThrow(equipmentId);
        this.equipment.remove(item);
    }

    private Equipment findEquipmentOrThrow(Long equipmentId) {
        if (equipmentId == null) {
            throw new IllegalArgumentException("equipmentId is required");
        }
        return this.equipment.stream()
                .filter(e -> equipmentId.equals(e.getId()))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Equipment not found: " + equipmentId));
    }

    private void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name is required");
        }
        if (name.trim().length() > 120) {
            throw new IllegalArgumentException("name max length is 120");
        }
        this.name = name.trim();
    }

    private void setDescription(String description) {
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("description is required");
        }
        this.description = description.trim();
    }

    private void setMaxPeople(int maxPeople) {
        if (maxPeople <= 0) {
            throw new IllegalArgumentException("maxPeople must be > 0");
        }
        this.maxPeople = maxPeople;
    }

    // ====== Image Management ======
    public void changeImageObjectName(String imageObjectName) {
        if (imageObjectName == null || imageObjectName.isBlank()) {
            throw new IllegalArgumentException("imageObjectName is required");
        }
        this.imageObjectName = imageObjectName.trim();
    }

    public void removeImageObjectName() {
        this.imageObjectName = null;
    }

    public Optional<String> getImageObjectName() {
        return Optional.ofNullable(imageObjectName);
    }
}

