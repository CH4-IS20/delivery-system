package com.sparta.ch4.delivery.policy.controller;

import com.sparta.ch4.delivery.policy.service.PolicyService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PolicyController {

    private final PolicyService policyService;

    public PolicyController(PolicyService policyService) {
        this.policyService = policyService;
    }

    @GetMapping("/policies/update")
    public void updatePolicy() {
        policyService.initPolicies();
    }

}
