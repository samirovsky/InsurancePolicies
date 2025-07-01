package com.tinubu.insurance.events;

import com.tinubu.insurance.entity.PolicyId;
import com.tinubu.insurance.entity.PolicyStatus;

import java.time.LocalDate;
import java.time.OffsetDateTime;

public record PolicyUpdatedEvent(
    PolicyId policyId,
    String name,
    PolicyStatus status,
    LocalDate startDate,
    LocalDate endDate,
    OffsetDateTime updatedAt
) implements PolicyEvent {}
