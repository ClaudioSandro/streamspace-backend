package com.streamspace.platform.spaces.domain.model.entities;

import com.streamspace.platform.shared.domain.model.entities.AuditableModel;
import com.streamspace.platform.spaces.domain.model.aggregates.ProductionSpace;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
@Entity
@Table(name = "equipment")
public class Equipment extends AuditableModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Equipment name is required")
    @Size(max = 100, message = "Equipment name must be at most 100 characters")
    @Column(nullable = false)
    private String name;

    @Min(value = 0, message = "Quantity must be >= 0")
    @Column(nullable = false)
    private int quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "production_space_id", nullable = false)
    private ProductionSpace productionSpace;

    protected Equipment() {
    }

    public Equipment(String name, int quantity) {
        rename(name);
        changeQuantity(quantity);
    }

    public Equipment(ProductionSpace productionSpace, String name, int quantity) {
        this(name, quantity);
        this.productionSpace = productionSpace;
    }

    public void rename(String newName) {
        if (newName == null || newName.isBlank()) {
            throw new IllegalArgumentException("Equipment.name is required");
        }
        this.name = newName.trim();
    }

    public void changeQuantity(int newQty) {
        if (newQty < 0) {
            throw new IllegalArgumentException("Equipment.quantity must be >= 0");
        }
        this.quantity = newQty;
    }

    public void setProductionSpace(ProductionSpace productionSpace) {
        this.productionSpace = productionSpace;
    }
}

