package edu.msudenver.tsp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.TestPropertySource;

@Configuration
@ComponentScan
@EnableAutoConfiguration(exclude = DataSourceAutoConfiguration.class)
@TestPropertySource("classpath:test.properties")
public class ServiceTestConfig {

    @Bean
    @Autowired public DefinitionService definitionService(final RestService restService) {
        return new DefinitionService(restService);
    }
}
