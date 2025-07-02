package com.tinubu.insurance.application.service;

import com.tinubu.insurance.application.commands.CreatePolicyCommand;
import com.tinubu.insurance.application.commands.PolicyStatusUpdateCommand;
import com.tinubu.insurance.application.commands.UpdatePolicyCommand;
import com.tinubu.insurance.application.exception.InvalidInputException;
import com.tinubu.insurance.domain.policy.entity.PolicyId;
import com.tinubu.insurance.domain.policy.entity.PolicyStatus;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PolicyCommandService {
  private static final Logger logger = LoggerFactory.getLogger(PolicyCommandService.class);
  private final CommandGateway commandGateway;

  /**
   * Asynchronously handles the creation of a new policy. Generates a UUID for the policy id and
   * sends the creation command.
   *
   * @param name the name of the policy
   * @param status the status of the policy (e.g., ACTIVE/INACTIVE)
   * @param startDate the start date of the policy
   * @param endDate the end date of the policy
   * @return a CompletableFuture indicating the result of the command
   */
  public CompletableFuture<Void> createPolicy(
      String name, PolicyStatus status, LocalDate startDate, LocalDate endDate) {
    PolicyId policyId = PolicyId.fromUUID(UUID.randomUUID()); // Generate a unique ID for the policy
    CreatePolicyCommand command =
        new CreatePolicyCommand(policyId, name, status, startDate, endDate);
    logger.info("Sending command to create policy with ID: {}", policyId);
    // Send the command asynchronously using CommandGateway and return the CompletableFuture
    return commandGateway
        .send(command)
        .thenAccept(
            result -> {
              logger.info("Policy creation command executed successfully for ID: {}", policyId);
            })
        .exceptionally(
            ex -> {
              String errorMsg =
                  "Error while creating policy with ID: %s. Cause: %s"
                      .formatted(policyId, ex.getMessage());
              logger.error(errorMsg, ex);
              throw new InvalidInputException(errorMsg);
            });
  }

  /**
   * Asynchronously handles the update of an existing policy.
   *
   * @param policyId the UUID of the policy to update
   * @param name the updated name of the policy
   * @param status the updated status of the policy
   * @param startDate the updated start date of the policy
   * @param endDate the updated end date of the policy
   * @return a CompletableFuture indicating the result of the command
   */
  public CompletableFuture<Void> updatePolicy(
      PolicyId policyId, String name, PolicyStatus status, LocalDate startDate, LocalDate endDate) {
    // Build the command object (PolicyUpdatedEvent in this case)
    OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
    UpdatePolicyCommand command =
        new UpdatePolicyCommand(policyId, name, status, startDate, endDate);
    logger.info("Sending command to update policy with ID: {}", policyId);
    // Send the command asynchronously using CommandGateway and return the CompletableFuture
    return commandGateway
        .send(command)
        .thenAccept(
            result -> {
              logger.info("Policy update command executed successfully for ID: {}", policyId);
            })
        .exceptionally(
            ex -> {
              String errorMsg =
                  "Error while updating policy with ID: %s. Cause: %s"
                      .formatted(policyId, ex.getMessage());
              logger.error(errorMsg, ex);
              throw new InvalidInputException(errorMsg);
            });
  }

  /**
   * Asynchronously handles the status update of an existing policy.
   *
   * @param policyId
   * @param newStatus
   * @param reason
   * @return a CompletableFuture indicating the result of the command
   */
  public CompletableFuture<Void> updatePolicyStatus(
      PolicyId policyId, PolicyStatus newStatus, String reason) {
    PolicyStatusUpdateCommand command = new PolicyStatusUpdateCommand(policyId, newStatus, reason);
    logger.info("Updating policy status for {}: {} - {}", policyId, newStatus, reason);
    return commandGateway
        .send(command)
        .thenAccept(result -> logger.info("Policy status updated successfully: {}", policyId))
        .exceptionally(
            ex -> {
              String errorMsg =
                  "Error while updating policy status with ID: %s. Cause: %s"
                      .formatted(policyId, ex.getMessage());
              logger.error(errorMsg, ex);
              throw new InvalidInputException(errorMsg);
            });
  }
}
