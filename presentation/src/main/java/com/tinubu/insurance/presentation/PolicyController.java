package com.tinubu.insurance.presentation;

import com.tinubu.insurance.application.queries.FindAllPoliciesQuery;
import com.tinubu.insurance.application.queries.FindPolicyByIdQuery;
import com.tinubu.insurance.application.service.PolicyCommandService;
import com.tinubu.insurance.application.service.PolicyQueryService;
import com.tinubu.insurance.application.service.PolicyStatusSchedulerService;
import com.tinubu.insurance.domain.policy.entity.Policy;
import com.tinubu.insurance.domain.policy.entity.PolicyId;
import com.tinubu.insurance.presentation.dto.CreatePolicyRequest;
import com.tinubu.insurance.presentation.dto.UpdatePolicyRequest;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/policies")
@CrossOrigin
public class PolicyController {

  private final PolicyCommandService commandService;
  private final PolicyQueryService queryService;
  private final PolicyStatusSchedulerService schedulerService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public void createPolicy(@Valid @RequestBody CreatePolicyRequest request) {
    commandService.createPolicy(
        request.name(), request.status(), request.startDate(), request.endDate());
  }

  @PutMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public void updatePolicy(@PathVariable UUID id, @Valid @RequestBody UpdatePolicyRequest request) {
    commandService.updatePolicy(
        PolicyId.fromUUID(id),
        request.name(),
        request.status(),
        request.startDate(),
        request.endDate());
  }

  @GetMapping("/{id}")
  public ResponseEntity<Policy> getPolicyById(@PathVariable UUID id) {
    Policy policy = queryService.handle(new FindPolicyByIdQuery(PolicyId.fromUUID(id)));
    return policy != null ? ResponseEntity.ok(policy) : ResponseEntity.notFound().build();
  }

  @GetMapping
  public ResponseEntity<List<Policy>> getAllPolicies() {
    List<Policy> policies = queryService.handle(new FindAllPoliciesQuery());
    return ResponseEntity.ok(policies);
  }
}
