package com.tinubu.insurance.infrastructure.spi.mappers;

import com.tinubu.insurance.domain.policy.entity.Policy;
import com.tinubu.insurance.domain.policy.entity.PolicyId;
import com.tinubu.insurance.domain.policy.entity.PolicyStatus;
import com.tinubu.insurance.infrastructure.spi.entities.PolicyEntity;

public class Mapper {

  private Mapper() {}

  public static Policy toPolicy(PolicyEntity entity) {
    return new Policy(
        PolicyId.fromUUID(entity.getPolicyId()),
        entity.getName(),
        entity.getStatus(),
        entity.getStartDate(),
        entity.getEndDate(),
        entity.getCreatedAt(),
        entity.getUpdatedAt());
  }

  public static PolicyEntity toPolicyEntity(Policy policy) {
    return new PolicyEntity(
        policy.id().toUUID(),
        policy.name(),
        policy.status(),
        policy.startDate(),
        policy.endDate(),
        policy.createdAt(),
        policy.updatedAt(),
        PolicyStatus.ACTIVE.equals(policy.status()),
        false);
  }
}
