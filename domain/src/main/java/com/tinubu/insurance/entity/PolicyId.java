package com.tinubu.insurance.entity;

import java.util.UUID;

import static com.tinubu.validation.Validator.requireNonNullAndNonBlank;

public record PolicyId(String value) {
    public PolicyId {
        requireNonNullAndNonBlank(value, "insuranceIdValue");
    }

    public static PolicyId fromUUID(UUID uuid) {
        return new PolicyId(uuid.toString());
    }

    public UUID toUUID() {
        return UUID.fromString(value);
    }
}
