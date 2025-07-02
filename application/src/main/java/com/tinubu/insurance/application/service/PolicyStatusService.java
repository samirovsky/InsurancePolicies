package com.tinubu.insurance.application.service;

import com.tinubu.insurance.domain.policy.entity.Policy;
import com.tinubu.insurance.domain.policy.entity.PolicyStatus;
import java.time.LocalDate;
import org.springframework.stereotype.Service;

@Service
public class PolicyStatusService {

  /** Calculate what the policy status should be based on dates */
  public PolicyStatus calculateCorrectStatus(Policy policy) {
    return calculateCorrectStatus(policy, LocalDate.now());
  }

  public PolicyStatus calculateCorrectStatus(Policy policy, LocalDate currentDate) {
    if (currentDate.isBefore(policy.startDate())) {
      return PolicyStatus.INACTIVE; // Policy hasn't started yet
    } else if (currentDate.isAfter(policy.endDate())) {
      return PolicyStatus.INACTIVE; // Policy has expired
    } else {
      return PolicyStatus.ACTIVE; // Policy is currently active
    }
  }

  /** Check if policy needs status update */
  public boolean needsStatusUpdate(Policy policy) {
    return needsStatusUpdate(policy, LocalDate.now());
  }

  public boolean needsStatusUpdate(Policy policy, LocalDate currentDate) {
    PolicyStatus correctStatus = calculateCorrectStatus(policy, currentDate);
    return !policy.status().equals(correctStatus);
  }

  /** Create update reason message */
  public String createUpdateReason(Policy policy, PolicyStatus newStatus, LocalDate currentDate) {
    if (newStatus == PolicyStatus.ACTIVE && currentDate.isEqual(policy.startDate())) {
      return "Policy activated - coverage period started";
    } else if (newStatus == PolicyStatus.ACTIVE) {
      return "Policy status corrected - within coverage period";
    } else if (currentDate.isBefore(policy.startDate())) {
      return "Policy not yet active - before start date (" + policy.startDate() + ")";
    } else if (currentDate.isAfter(policy.endDate())) {
      return "Policy expired - after end date (" + policy.endDate() + ")";
    }
    return "Status updated based on date evaluation";
  }
}
