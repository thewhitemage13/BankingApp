package org.springcorebankapp;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.Scanner;

@Configuration
@PropertySource("classpath:application.properties")
public class ApplicationConfiguration {

    @Bean
    public Scanner scanner(){
        return new Scanner(System.in);
    }
}
