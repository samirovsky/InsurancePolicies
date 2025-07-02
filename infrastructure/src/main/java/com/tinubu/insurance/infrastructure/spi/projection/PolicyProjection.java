package com.tinubu.insurance.infrastructure.spi.projection;

import com.tinubu.insurance.application.queries.FindAllPoliciesQuery;
import com.tinubu.insurance.application.queries.FindPoliciesNeedingStatusUpdateQuery;
import com.tinubu.insurance.application.queries.FindPolicyByIdQuery;
import com.tinubu.insurance.domain.policy.entity.Policy;
import com.tinubu.insurance.domain.policy.events.PolicyCreatedEvent;
import com.tinubu.insurance.domain.policy.events.PolicyStatusUpdatedEvent;
import com.tinubu.insurance.domain.policy.events.PolicyUpdatedEvent;
import com.tinubu.insurance.domain.policy.port.PolicyRepository;
import com.tinubu.insurance.infrastructure.spi.exception.PolicyNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PolicyProjection {

  private static final Logger logger = LoggerFactory.getLogger(PolicyProjection.class);

  private final PolicyRepository<Policy, UUID> repository;

  @EventHandler
  public void on(PolicyCreatedEvent event) {
    try {
      Policy entity =
          new Policy(
              event.policyId(),
              event.name(),
              event.status(),
              event.startDate(),
              event.endDate(),
              event.createdAt(),
              event.updatedAt());

      repository.save(entity);
      logger.info("Policy projection created: {}", event.policyId());

    } catch (Exception e) {
      logger.error("Failed to handle PolicyCreatedEvent for policy: {}", event.policyId(), e);
      throw e; // Re-throw to ensure event processing fails and can be retried
    }
  }

  @EventHandler
  public void on(PolicyUpdatedEvent event) {
    try {
      Optional<Policy> existingEntity = repository.findByPolicyId(event.policyId().toUUID());

      if (existingEntity.isPresent()) {
        Policy entity = existingEntity.get();

        // Update all fields except createdAt
        Policy updatedPolicy =
            entity.update(event.name(), event.status(), event.startDate(), event.endDate());

        repository.save(updatedPolicy);
        logger.debug("Policy projection updated: {}", event.policyId());

      } else {
        logger.warn("Policy not found for update event: {}", event.policyId());
        // Could create new entity here if needed, or throw exception
      }

    } catch (Exception e) {
      logger.error("Failed to handle PolicyUpdatedEvent for policy: {}", event.policyId(), e);
      throw e;
    }
  }

  @EventHandler
  public void on(PolicyStatusUpdatedEvent event) {
    try {
      Optional<Policy> existingEntity = repository.findByPolicyId(event.policyId().toUUID());

      if (existingEntity.isPresent()) {
        Policy entity = existingEntity.get();
        Policy updatedPolicy = entity.updateStatus(event.newStatus());

        repository.save(updatedPolicy);
        logger.info(
            "Policy status updated in projection: {} -> {} for policy: {}",
            event.oldStatus(),
            event.newStatus(),
            event.policyId());

      } else {
        logger.warn("Policy not found for status update event: {}", event.policyId());
      }

    } catch (Exception e) {
      logger.error("Failed to handle PolicyStatusUpdatedEvent for policy: {}", event.policyId(), e);
      throw e;
    }
  }

  @QueryHandler
  public Policy handle(FindPolicyByIdQuery query) throws PolicyNotFoundException {
    try {
      Optional<Policy> policy = repository.findByPolicyId(query.policyId().toUUID());

      return policy.orElseThrow(PolicyNotFoundException::new);

    } catch (Exception e) {
      logger.error("Failed to handle FindPolicyByIdQuery for policy: {}", query.policyId(), e);
      throw e;
    }
  }

  @QueryHandler
  public List<Policy> handle(FindAllPoliciesQuery query) {
    try {
      return repository.findAll().stream().toList();

    } catch (Exception e) {
      logger.error("Failed to handle FindAllPoliciesQuery", e);
      throw e;
    }
  }

  @QueryHandler
  public List<Policy> handle(FindPoliciesNeedingStatusUpdateQuery query) {
    try {
      return repository.findPoliciesNeedingStatusUpdate().stream().toList();

    } catch (Exception e) {
      logger.error("Failed to handle FindAllPoliciesQuery", e);
      throw e;
    }
  }
}
