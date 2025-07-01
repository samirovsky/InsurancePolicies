package com.tinubu.insurance.aggregate;

import com.tinubu.insurance.entity.PolicyId;
import com.tinubu.insurance.entity.PolicyStatus;
import com.tinubu.insurance.events.PolicyCreatedEvent;
import com.tinubu.insurance.events.PolicyUpdatedEvent;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static com.tinubu.validation.Validator.requireCoherentDatesPair;
import static com.tinubu.validation.Validator.requireNonNull;
import static com.tinubu.validation.Validator.requireNonNullAndNonBlank;

public class PolicyAggregate {

    private PolicyId id;
    private String name;
    private PolicyStatus status;
    private LocalDate startDate;
    private LocalDate endDate;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    // Parameterized constructor with validation
    public PolicyAggregate(PolicyId id, String name, PolicyStatus status,
                           LocalDate startDate, LocalDate endDate,
                           OffsetDateTime createdAt, OffsetDateTime updatedAt) {
        requireNonNull(id, "id");
        requireNonNullAndNonBlank(name, "name");
        requireNonNull(status, "status");
        requireNonNull(startDate, "startDate");
        requireNonNull(endDate, "endDate");
        requireCoherentDatesPair(startDate, "startDate", endDate, "endDate");
        requireNonNull(createdAt, "createdAt");
        requireNonNull(updatedAt, "updatedAt");

        this.id = id;
        this.name = name;
        this.status = status;
        this.startDate = startDate;
        this.endDate = endDate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Default no-args constructor
    public PolicyAggregate() {}

    // Static factory method to create a new policy
    public static PolicyAggregate create(PolicyId id, String name, PolicyStatus status,
                                         LocalDate startDate, LocalDate endDate) {
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        return new PolicyAggregate(id, name, status, startDate, endDate, now, now);
    }

    // Method to update an existing Policy
    public void update(String name, PolicyStatus status,
                       LocalDate startDate, LocalDate endDate) {
        requireNonNullAndNonBlank(name, "name");
        requireNonNull(status, "status");
        requireNonNull(startDate, "startDate");
        requireNonNull(endDate, "endDate");
        requireCoherentDatesPair(startDate, "startDate", endDate, "endDate");

        this.name = name;
        this.status = status;
        this.startDate = startDate;
        this.endDate = endDate;
        this.updatedAt = OffsetDateTime.now(ZoneOffset.UTC);
    }

    // Applies a PolicyCreatedEvent to the Policy
    public void applyPolicyCreated(PolicyCreatedEvent event) {
        requireNonNull(event, "PolicyCreatedEvent");

        this.id = event.policyId();
        this.name = event.name();
        this.status = event.status();
        this.startDate = event.startDate();
        this.endDate = event.endDate();
        this.createdAt = event.createdAt();
        this.updatedAt = event.createdAt();
    }

    // Applies a PolicyUpdatedEvent to the Policy
    public void applyPolicyUpdated(PolicyUpdatedEvent event) {
        requireNonNull(event, "PolicyUpdatedEvent");

        this.name = event.name();
        this.status = event.status();
        this.startDate = event.startDate();
        this.endDate = event.endDate();
        this.updatedAt = event.updatedAt();
    }

    // Getters and setters
    public PolicyId getId() {
        return id;
    }

    public void setId(PolicyId id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        requireNonNullAndNonBlank(name, "name");
        this.name = name;
    }

    public PolicyStatus getStatus() {
        return status;
    }

    public void setStatus(PolicyStatus status) {
        requireNonNull(status, "status");
        this.status = status;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        requireNonNull(startDate, "startDate");
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        requireNonNull(endDate, "endDate");
        this.endDate = endDate;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        requireNonNull(createdAt, "createdAt");
        this.createdAt = createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(OffsetDateTime updatedAt) {
        requireNonNull(updatedAt, "updatedAt");
        this.updatedAt = updatedAt;
    }

    // Overriding toString for better representation
    @Override
    public String toString() {
        return "Policy{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", status=" + status +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
