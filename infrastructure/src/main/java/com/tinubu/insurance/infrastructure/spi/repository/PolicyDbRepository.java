package com.tinubu.insurance.infrastructure.spi.repository;

import com.tinubu.insurance.domain.policy.entity.Policy;
import com.tinubu.insurance.domain.policy.entity.PolicyStatus;
import com.tinubu.insurance.domain.policy.port.PolicyRepository;
import com.tinubu.insurance.infrastructure.spi.mappers.Mapper;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PolicyDbRepository implements PolicyRepository<Policy, UUID> {

  private final PolicyDataRepository policyDataRepository;

  @Override
  public Optional<Policy> findByPolicyId(UUID policyId) {
    return policyDataRepository.findByPolicyId(policyId).map(Mapper::toPolicy);
  }

  @Override
  public List<Policy> findAll() {
    return policyDataRepository.findAll().stream().map(Mapper::toPolicy).toList();
  }

  @Override
  public List<Policy> findPoliciesNeedingStatusUpdate() {
    return policyDataRepository.findPoliciesNeedingStatusUpdate().stream()
        .map(Mapper::toPolicy)
        .toList();
  }

  @Override
  public List<Policy> findByStatus(PolicyStatus status) {
    return policyDataRepository.findByStatus(status).stream().map(Mapper::toPolicy).toList();
  }

  @Override
  public List<Policy> findExpiredPolicies() {
    return policyDataRepository.findByIsExpiredTrue().stream().map(Mapper::toPolicy).toList();
  }

  @Override
  public List<Policy> findPoliciesExpiringBetween(LocalDate startDate, LocalDate endDate) {
    return policyDataRepository.findPoliciesExpiringBetween(startDate, endDate).stream()
        .map(Mapper::toPolicy)
        .toList();
  }

  @Override
  public long countByStatus(PolicyStatus status) {
    return policyDataRepository.countByStatus(status);
  }

  @Override
  public void save(Policy policy) {
    policyDataRepository.save(Mapper.toPolicyEntity(policy));
  }
}
