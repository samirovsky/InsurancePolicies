package com.tinubu.insurance.infrastructure.spi.repository;

import com.tinubu.insurance.domain.policy.entity.PolicyStatus;
import com.tinubu.insurance.infrastructure.spi.entities.PolicyEntity;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PolicyDataRepository extends JpaRepository<PolicyEntity, UUID> {

  Optional<PolicyEntity> findByPolicyId(UUID policyId);

  List<PolicyEntity> findByStatus(PolicyStatus status);

  List<PolicyEntity> findByIsExpiredTrue();

  @Query("SELECT p FROM PolicyEntity p WHERE p.endDate BETWEEN :startDate AND :endDate")
  List<PolicyEntity> findPoliciesExpiringBetween(
      @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

  @Query(
      "SELECT p FROM PolicyEntity p WHERE p.status != "
          + "CASE WHEN CURRENT_DATE < p.startDate OR CURRENT_DATE > p.endDate "
          + "THEN 'INACTIVE' ELSE 'ACTIVE' END")
  List<PolicyEntity> findPoliciesNeedingStatusUpdate();

  @Query("SELECT COUNT(p) FROM PolicyEntity p WHERE p.status = :status")
  long countByStatus(@Param("status") PolicyStatus status);
}
