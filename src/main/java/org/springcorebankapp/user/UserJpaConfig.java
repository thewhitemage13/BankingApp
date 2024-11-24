package org.springcorebankapp.user;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(
        basePackages = "org.springcorebankapp.user",
        repositoryImplementationPostfix = "JpaRepository"
)
public class UserJpaConfig {
}
