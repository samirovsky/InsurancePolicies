package com.tinubu.insurance.application.queries;

import com.tinubu.insurance.domain.policy.entity.PolicyId;
import com.tinubu.insurance.domain.policy.entity.PolicyStatus;
import java.time.LocalDate;
import java.time.OffsetDateTime;

public record PolicySummary(
    PolicyId policyId,
    String name,
    PolicyStatus status,
    LocalDate startDate,
    LocalDate endDate,
    OffsetDateTime createdAt,
    OffsetDateTime updatedAt) {}
