package com.tinubu.insurance.events;

import com.tinubu.insurance.entity.PolicyId;
import com.tinubu.insurance.entity.PolicyStatus;

import java.time.LocalDate;
import java.time.OffsetDateTime;

public record PolicyCreatedEvent(
    PolicyId policyId,
    String name,
    PolicyStatus status,
    LocalDate startDate,
    LocalDate endDate,
    OffsetDateTime createdAt,
    OffsetDateTime updatedAt
) implements PolicyEvent {}
