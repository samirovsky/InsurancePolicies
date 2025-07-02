package com.tinubu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableJpaRepositories
@EnableScheduling
@EnableTransactionManagement
@ComponentScan(
    basePackages = {
      "com.tinubu.insurance.application", // Application layer
      "com.tinubu.insurance.infrastructure", // Infrastructure layer
      "com.tinubu.insurance.presentation", // Queries/Commands
      "com.tinubu"
    })
public class InsurancePoliciesApplication {
  public static void main(String[] args) {
    SpringApplication.run(InsurancePoliciesApplication.class, args);
  }
}
