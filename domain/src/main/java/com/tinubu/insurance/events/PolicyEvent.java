package com.tinubu.insurance.events;

import com.tinubu.insurance.entity.PolicyId;

public sealed interface PolicyEvent
    permits PolicyCreatedEvent, PolicyUpdatedEvent {
    
    PolicyId policyId();
}