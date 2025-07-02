package com.tinubu.insurance.application.commands;

import com.tinubu.insurance.domain.policy.entity.PolicyId;
import com.tinubu.insurance.domain.policy.entity.PolicyStatus;
import java.time.LocalDate;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

public record UpdatePolicyCommand(
    @TargetAggregateIdentifier PolicyId policyId,
    String name,
    PolicyStatus status,
    LocalDate startDate,
    LocalDate endDate)
    implements PolicyCommand {
  @Override
  public PolicyId aggregateId() {
    return policyId;
  }
}
