package com.tinubu.insurance.application.commands;

import com.tinubu.insurance.domain.policy.entity.PolicyId;

public sealed interface PolicyCommand extends Command<PolicyId>
    permits CreatePolicyCommand, PolicyStatusUpdateCommand, UpdatePolicyCommand {}
