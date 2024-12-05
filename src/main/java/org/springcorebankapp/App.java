package org.springcorebankapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * The main class of the application, which serves as the entry point for the Spring Boot application.
 * <p>
 * This class is annotated with {@link SpringBootApplication} to enable Spring Boot auto-configuration,
 * component scanning, and other necessary configurations. The {@link EnableCaching} annotation is also
 * used to enable caching functionality in the application.
 * </p>
 *
 * @author Mukhammed Lolo
 * @version 1.0.0
 */
@EnableCaching
@SpringBootApplication
public class App {

    /**
     * The main method, which is the entry point of the Spring Boot application.
     * It triggers the startup of the Spring Boot application by calling {@link SpringApplication#run}.
     *
     * @param args command-line arguments passed to the application
     */
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
