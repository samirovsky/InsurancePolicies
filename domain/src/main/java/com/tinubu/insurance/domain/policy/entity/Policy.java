package com.tinubu.insurance.domain.policy.entity;

import static com.tinubu.insurance.domain.validation.Validator.requireCoherentDatesPair;
import static com.tinubu.insurance.domain.validation.Validator.requireNonNull;
import static com.tinubu.insurance.domain.validation.Validator.requireNonNullAndNonBlank;

import com.tinubu.insurance.domain.policy.events.PolicyCreatedEvent;
import com.tinubu.insurance.domain.policy.events.PolicyUpdatedEvent;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public record Policy(
    PolicyId id,
    String name,
    PolicyStatus status,
    LocalDate startDate,
    LocalDate endDate,
    OffsetDateTime createdAt,
    OffsetDateTime updatedAt) {

  // Compact constructor for validation
  public Policy {
    requireNonNull(id, "id");
    requireNonNullAndNonBlank(name, "name");
    requireNonNull(status, "status");
    requireNonNull(startDate, "startDate");
    requireNonNull(endDate, "endDate");
    requireCoherentDatesPair(startDate, "startDate", endDate, "endDate");
    requireNonNull(createdAt, "createdAt");
    requireNonNull(updatedAt, "updatedAt");
  }

  // Static factory method for creating new policies
  public static Policy create(
      PolicyId id, String name, PolicyStatus status, LocalDate startDate, LocalDate endDate) {
    OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
    return new Policy(id, name, status, startDate, endDate, now, now);
  }

  // Static validation method for updates
  public static void validateUpdate(
      String name, PolicyStatus status, LocalDate startDate, LocalDate endDate) {
    requireNonNullAndNonBlank(name, "name");
    requireNonNull(status, "status");
    requireNonNull(startDate, "startDate");
    requireNonNull(endDate, "endDate");
    requireCoherentDatesPair(startDate, "startDate", endDate, "endDate");
  }

  // Immutable update - returns new Policy instance
  public Policy update(String name, PolicyStatus status, LocalDate startDate, LocalDate endDate) {
    validateUpdate(name, status, startDate, endDate);

    return new Policy(
        this.id,
        name,
        status,
        startDate,
        endDate,
        this.createdAt,
        OffsetDateTime.now(ZoneOffset.UTC));
  }

  // Immutable status update - returns new Policy instance
  public Policy updateStatus(PolicyStatus newStatus) {
    requireNonNull(newStatus, "newStatus");

    if (this.status.equals(newStatus)) {
      return this; // No change needed
    }

    return new Policy(
        this.id,
        this.name,
        newStatus,
        this.startDate,
        this.endDate,
        this.createdAt,
        OffsetDateTime.now(ZoneOffset.UTC));
  }

  // Business logic methods for status calculation
  public PolicyStatus calculateStatusBasedOnDates() {
    return calculateStatusBasedOnDates(LocalDate.now());
  }

  public PolicyStatus calculateStatusBasedOnDates(LocalDate currentDate) {
    requireNonNull(currentDate, "currentDate");

    if (currentDate.isBefore(startDate)) {
      return PolicyStatus.INACTIVE; // Policy not yet started
    } else if (currentDate.isAfter(endDate)) {
      return PolicyStatus.INACTIVE; // Policy expired
    } else {
      return PolicyStatus.ACTIVE; // Policy is currently active
    }
  }

  public boolean needsStatusUpdate() {
    return needsStatusUpdate(LocalDate.now());
  }

  public boolean needsStatusUpdate(LocalDate currentDate) {
    PolicyStatus calculatedStatus = calculateStatusBasedOnDates(currentDate);
    return !this.status.equals(calculatedStatus);
  }

  // Check if policy is currently active
  public boolean isActive() {
    return status == PolicyStatus.ACTIVE;
  }

  // Check if policy is within its coverage period
  public boolean isWithinCoveragePeriod() {
    return isWithinCoveragePeriod(LocalDate.now());
  }

  public boolean isWithinCoveragePeriod(LocalDate currentDate) {
    return !currentDate.isBefore(startDate) && !currentDate.isAfter(endDate);
  }

  // Check if policy has expired
  public boolean hasExpired() {
    return hasExpired(LocalDate.now());
  }

  public boolean hasExpired(LocalDate currentDate) {
    return currentDate.isAfter(endDate);
  }

  // Check if policy hasn't started yet
  public boolean hasNotStarted() {
    return hasNotStarted(LocalDate.now());
  }

  public boolean hasNotStarted(LocalDate currentDate) {
    return currentDate.isBefore(startDate);
  }

  // Event application methods - return new instances
  public static Policy fromCreatedEvent(PolicyCreatedEvent event) {
    requireNonNull(event, "PolicyCreatedEvent");

    return new Policy(
        event.policyId(),
        event.name(),
        event.status(),
        event.startDate(),
        event.endDate(),
        event.createdAt(),
        event.updatedAt());
  }

  public Policy applyUpdatedEvent(PolicyUpdatedEvent event) {
    requireNonNull(event, "PolicyUpdatedEvent");

    return new Policy(
        this.id,
        event.name(),
        event.status(),
        event.startDate(),
        event.endDate(),
        this.createdAt,
        event.updatedAt());
  }

  public Policy applyStatusUpdatedEvent(PolicyStatus newStatus, OffsetDateTime updatedAt) {
    requireNonNull(newStatus, "newStatus");
    requireNonNull(updatedAt, "updatedAt");

    return new Policy(
        this.id, this.name, newStatus, this.startDate, this.endDate, this.createdAt, updatedAt);
  }
}
