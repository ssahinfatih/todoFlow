package com.todoflow.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing//Spring Data JPA auditing altyapısını aktif ediyor.
public class JpaConfig {
}