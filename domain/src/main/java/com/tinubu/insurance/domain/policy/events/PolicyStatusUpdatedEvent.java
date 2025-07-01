package com.tinubu.insurance.domain.policy.events;

import com.tinubu.insurance.domain.policy.entity.PolicyId;
import com.tinubu.insurance.domain.policy.entity.PolicyStatus;
import java.time.OffsetDateTime;

public record PolicyStatusUpdatedEvent(
    PolicyId policyId,
    PolicyStatus oldStatus,
    PolicyStatus newStatus,
    String reason,
    OffsetDateTime updatedAt)
    implements PolicyEvent {}
