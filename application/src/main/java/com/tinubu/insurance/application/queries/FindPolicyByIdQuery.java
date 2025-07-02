package com.tinubu.insurance.application.queries;

import com.tinubu.insurance.domain.policy.entity.PolicyId;

public record FindPolicyByIdQuery(PolicyId policyId) {}
