package org.springcorebankapp.account;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "org.springcorebankapp.account", repositoryImplementationPostfix = "JpaRepository")
public class AccountJpaConfig {
}
