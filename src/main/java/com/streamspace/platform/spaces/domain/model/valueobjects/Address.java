package com.streamspace.platform.spaces.domain.model.valueobjects;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Embeddable
public record Address(
        @NotBlank(message = "City is required")
        @Size(max = 100, message = "City must be at most 100 characters")
        String city,

        @NotBlank(message = "District is required")
        @Size(max = 100, message = "District must be at most 100 characters")
        String district,

        @NotBlank(message = "Address line is required")
        @Size(max = 255, message = "Address line must be at most 255 characters")
        String addressLine
) {
    public Address {
        if (city == null || city.isBlank()) throw new IllegalArgumentException("city is required");
        if (district == null || district.isBlank()) throw new IllegalArgumentException("district is required");
        if (addressLine == null || addressLine.isBlank()) throw new IllegalArgumentException("addressLine is required");
        city = city.trim();
        district = district.trim();
        addressLine = addressLine.trim();
    }

    public String format() {
        return addressLine + ", " + district + ", " + city;
    }
}
