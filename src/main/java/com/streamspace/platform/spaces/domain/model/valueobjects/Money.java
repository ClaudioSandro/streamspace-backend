package com.streamspace.platform.spaces.domain.model.valueobjects;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

@Embeddable
public record Money(
        @NotNull(message = "Amount is required")
        @DecimalMin(value = "0.0", inclusive = true, message = "Amount must be >= 0")
        BigDecimal amount,

        @NotBlank(message = "Currency is required")
        @Size(min = 3, max = 3, message = "Currency must be 3 characters (ISO 4217)")
        String currency
) {
    public Money {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Money.amount must be >= 0");
        }
        if (currency == null || currency.isBlank()) {
            throw new IllegalArgumentException("Money.currency is required");
        }
        currency = currency.trim().toUpperCase();
    }

    public Money multiply(BigDecimal factor) {
        if (factor == null || factor.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("factor must be >= 0");
        }
        return new Money(this.amount.multiply(factor), this.currency);
    }


    @Override
    public String toString() {
        return currency + " " + amount;
    }
}