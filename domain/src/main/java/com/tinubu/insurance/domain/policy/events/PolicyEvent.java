package com.tinubu.insurance.domain.policy.events;

import com.tinubu.insurance.domain.policy.entity.PolicyId;

public sealed interface PolicyEvent
    permits PolicyCreatedEvent, PolicyStatusUpdatedEvent, PolicyUpdatedEvent {

  PolicyId policyId();
}
