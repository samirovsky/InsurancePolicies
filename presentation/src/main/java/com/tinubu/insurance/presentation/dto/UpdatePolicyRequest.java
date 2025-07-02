package com.tinubu.insurance.presentation.dto;

import com.tinubu.insurance.domain.policy.entity.PolicyStatus;
import com.tinubu.insurance.presentation.validator.ValidPolicyDateRange;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import java.time.LocalDate;

@ValidPolicyDateRange
public record UpdatePolicyRequest(
    @NotEmpty(message = "Policy name must not be empty") String name,
    @NotNull(message = "Policy status must not be null") PolicyStatus status,
    @NotNull(message = "Start date must not be null")
        @PastOrPresent(message = "Start date cannot be in the future")
        LocalDate startDate,
    @NotNull(message = "End date must not be null")
        @FutureOrPresent(message = "End date must be today or in the future")
        LocalDate endDate) {}
