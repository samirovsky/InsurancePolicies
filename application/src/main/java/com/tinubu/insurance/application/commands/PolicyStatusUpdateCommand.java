package com.tinubu.insurance.application.commands;

import com.tinubu.insurance.domain.policy.entity.PolicyId;
import com.tinubu.insurance.domain.policy.entity.PolicyStatus;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

public record PolicyStatusUpdateCommand(
    @TargetAggregateIdentifier PolicyId policyId, PolicyStatus newStatus, String reason)
    implements PolicyCommand {
  @Override
  public PolicyId aggregateId() {
    return policyId;
  }
}
