package com.tinubu.insurance.domain.policy.port;

import com.tinubu.insurance.domain.policy.entity.PolicyStatus;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PolicyRepository<Policy, UUID> {

  Optional<Policy> findByPolicyId(UUID policyId);

  List<Policy> findAll();

  List<Policy> findPoliciesNeedingStatusUpdate();

  List<Policy> findByStatus(PolicyStatus status);

  List<Policy> findExpiredPolicies();

  List<Policy> findPoliciesExpiringBetween(LocalDate startDate, LocalDate endDate);

  long countByStatus(PolicyStatus status);

  void save(Policy policy);
}
