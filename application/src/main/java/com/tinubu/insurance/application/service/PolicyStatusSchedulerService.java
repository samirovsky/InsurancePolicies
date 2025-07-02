package com.tinubu.insurance.application.service;

import com.tinubu.insurance.application.queries.FindPoliciesNeedingStatusUpdateQuery;
import com.tinubu.insurance.domain.policy.entity.Policy;
import com.tinubu.insurance.domain.policy.entity.PolicyStatus;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class PolicyStatusSchedulerService {

  private static final Logger logger = LoggerFactory.getLogger(PolicyStatusSchedulerService.class);

  private final PolicyCommandService commandService;
  private final PolicyQueryService queryService;
  private final PolicyStatusService statusService;

  public PolicyStatusSchedulerService(
      PolicyCommandService commandService,
      PolicyQueryService queryService,
      PolicyStatusService statusService) {
    this.commandService = commandService;
    this.queryService = queryService;
    this.statusService = statusService;
  }

  // Run every day at 2:00 AM
  @Scheduled(cron = "0 0 2 * * ?")
  public void updatePolicyStatuses() {
    logger.info("Starting automated policy status update process");

    try {
      List<Policy> allPolicies = queryService.handle(new FindPoliciesNeedingStatusUpdateQuery());
      LocalDate today = LocalDate.now();
      int updatedCount = 0;

      for (Policy policy : allPolicies) {
        if (statusService.needsStatusUpdate(policy, today)) {
          PolicyStatus newStatus = statusService.calculateCorrectStatus(policy, today);
          String reason = statusService.createUpdateReason(policy, newStatus, today);

          CompletableFuture<Void> updateFuture =
              commandService.updatePolicyStatus(policy.id(), newStatus, reason);

          updateFuture
              .thenRun(
                  () -> {
                    logger.info(
                        "Successfully updated policy {} from {} to {} - Reason: {}",
                        policy.id(),
                        policy.status(),
                        newStatus,
                        reason);
                  })
              .exceptionally(
                  ex -> {
                    logger.error("Failed to update policy status for {}", policy.id(), ex);
                    return null;
                  });

          updatedCount++;
        }
      }

      logger.info(
          "Policy status update process completed. Triggered updates for {} policies",
          updatedCount);

    } catch (Exception e) {
      logger.error("Error during automated policy status update", e);
    }
  }

  // Manual trigger for testing
  public void triggerManualUpdate() {
    logger.info("Manual policy status update triggered");
    updatePolicyStatuses();
  }
}
