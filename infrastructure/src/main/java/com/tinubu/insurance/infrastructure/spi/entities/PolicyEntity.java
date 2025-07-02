package com.tinubu.insurance.infrastructure.spi.entities;

import com.tinubu.insurance.domain.policy.entity.PolicyStatus;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "policy_projection")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PolicyEntity {

  @Id
  @Column(name = "policy_id")
  private UUID policyId;

  @Column(name = "name", nullable = false)
  private String name;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false)
  private PolicyStatus status;

  @Column(name = "start_date", nullable = false)
  private LocalDate startDate;

  @Column(name = "end_date", nullable = false)
  private LocalDate endDate;

  @Column(name = "created_at", nullable = false)
  private OffsetDateTime createdAt;

  @Column(name = "updated_at", nullable = false)
  private OffsetDateTime updatedAt;

  // Additional indexes for common queries
  @Column(name = "is_active")
  private Boolean isActive;

  @Column(name = "is_expired")
  private Boolean isExpired;

  @PrePersist
  @PreUpdate
  private void updateDerivedFields() {
    LocalDate today = LocalDate.now();
    this.isActive = (status == PolicyStatus.ACTIVE);
    this.isExpired = today.isAfter(endDate);
  }
}
