package com.tinubu.insurance.application.service;

import com.tinubu.insurance.application.queries.FindAllPoliciesQuery;
import com.tinubu.insurance.application.queries.FindPoliciesNeedingStatusUpdateQuery;
import com.tinubu.insurance.application.queries.FindPolicyByIdQuery;
import com.tinubu.insurance.domain.policy.entity.Policy;
import com.tinubu.insurance.domain.policy.entity.PolicyId;
import com.tinubu.insurance.domain.policy.port.PolicyRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.axonframework.queryhandling.QueryHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PolicyQueryService {

  private static final Logger logger = LoggerFactory.getLogger(PolicyQueryService.class);

  private final Map<PolicyId, Policy> policyMap = new HashMap<>();
  private final PolicyRepository<Policy, UUID> policyRepository;

  @QueryHandler
  public Policy handle(FindPolicyByIdQuery query) {
    try {
      return policyRepository.findByPolicyId(query.policyId().toUUID()).orElse(null);
    } catch (Exception e) {
      logger.error("Failed to handle FindPolicyByIdQuery for policy: {}", query.policyId(), e);
      throw e;
    }
  }

  @QueryHandler
  public List<Policy> handle(FindAllPoliciesQuery query) {
    try {
      return policyRepository.findAll();
    } catch (Exception e) {
      logger.error("Failed to handle FindAllPoliciesQuery", e);
      throw e;
    }
  }

  @QueryHandler
  public List<Policy> handle(FindPoliciesNeedingStatusUpdateQuery query) {
    try {
      return policyRepository.findPoliciesNeedingStatusUpdate();
    } catch (Exception e) {
      logger.error("Failed to handle FindPoliciesNeedingStatusUpdateQuery", e);
      throw e;
    }
  }
}
