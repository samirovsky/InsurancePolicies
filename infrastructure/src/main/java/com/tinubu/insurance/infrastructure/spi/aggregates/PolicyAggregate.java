package com.tinubu.insurance.infrastructure.spi.aggregates;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

import com.tinubu.insurance.application.commands.CreatePolicyCommand;
import com.tinubu.insurance.application.commands.PolicyStatusUpdateCommand;
import com.tinubu.insurance.application.commands.UpdatePolicyCommand;
import com.tinubu.insurance.domain.policy.entity.Policy;
import com.tinubu.insurance.domain.policy.entity.PolicyId;
import com.tinubu.insurance.domain.policy.entity.PolicyStatus;
import com.tinubu.insurance.domain.policy.events.PolicyCreatedEvent;
import com.tinubu.insurance.domain.policy.events.PolicyStatusUpdatedEvent;
import com.tinubu.insurance.domain.policy.events.PolicyUpdatedEvent;
import java.time.OffsetDateTime;
import lombok.Getter;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;

@Aggregate
public class PolicyAggregate {

  @AggregateIdentifier private PolicyId policyId;

  // Immutable Policy record - replaced entirely on each event
  @Getter private Policy policy;

  protected PolicyAggregate() {
    // Required by Axon
  }

  @CommandHandler
  public PolicyAggregate(CreatePolicyCommand command) {

    OffsetDateTime now = OffsetDateTime.now();

    // Apply the creation event
    apply(
        new PolicyCreatedEvent(
            command.policyId(),
            command.name(),
            command.status(),
            command.startDate(),
            command.endDate(),
            now,
            now));
  }

  @CommandHandler
  public void handle(UpdatePolicyCommand command) {
    if (this.policy == null) {
      throw new IllegalStateException("Policy not found for ID: " + command.policyId());
    }

    // Validate using domain Policy static method
    Policy.validateUpdate(command.name(), command.status(), command.startDate(), command.endDate());

    // Apply the update event
    apply(
        new PolicyUpdatedEvent(
            command.policyId(),
            command.name(),
            command.status(),
            command.startDate(),
            command.endDate(),
            OffsetDateTime.now()));
  }

  @CommandHandler
  public void handle(PolicyStatusUpdateCommand command) {
    if (this.policy == null) {
      throw new IllegalStateException("Policy not found for ID: " + command.policyId());
    }

    PolicyStatus currentStatus = this.policy.status();
    PolicyStatus newStatus = command.newStatus();

    // Only apply event if status actually changes
    if (!currentStatus.equals(newStatus)) {
      apply(
          new PolicyStatusUpdatedEvent(
              command.policyId(),
              currentStatus,
              newStatus,
              command.reason(),
              OffsetDateTime.now()));
    }
  }

  @EventSourcingHandler
  public void on(PolicyCreatedEvent event) {
    this.policyId = event.policyId();
    // Create new immutable Policy from event
    this.policy = Policy.fromCreatedEvent(event);
  }

  @EventSourcingHandler
  public void on(PolicyUpdatedEvent event) {
    if (this.policy != null) {
      // Create new immutable Policy by applying the update
      this.policy = this.policy.applyUpdatedEvent(event);
    }
  }

  @EventSourcingHandler
  public void on(PolicyStatusUpdatedEvent event) {
    if (this.policy != null) {
      // Create new immutable Policy with updated status
      this.policy = this.policy.applyStatusUpdatedEvent(event.newStatus(), event.updatedAt());
    }
  }
}
