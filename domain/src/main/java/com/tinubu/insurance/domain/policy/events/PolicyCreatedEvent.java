package com.tinubu.insurance.domain.policy.events;

import com.tinubu.insurance.domain.policy.entity.PolicyId;
import com.tinubu.insurance.domain.policy.entity.PolicyStatus;
import java.time.LocalDate;
import java.time.OffsetDateTime;

public record PolicyCreatedEvent(
    PolicyId policyId,
    String name,
    PolicyStatus status,
    LocalDate startDate,
    LocalDate endDate,
    OffsetDateTime createdAt,
    OffsetDateTime updatedAt)
    implements PolicyEvent {}
