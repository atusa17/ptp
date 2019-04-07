package edu.msudenver.tsp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
public class ServicesTestConfig {

    @Bean
    @Autowired
    public UserService userService(final RestService restService) {
        return new UserService(restService);
    }

    @Bean
    @Autowired
    public DefinitionService definitionService(final RestService restService) {
        return new DefinitionService(restService);
    }
}
