package edu.msudenver.tsp.services.scoring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
//@PropertySource("classpath:development.properties")
public class ScoringConfig {

    @Bean
    public TheoremScoringService theoremScoringService() {
        return new TheoremScoringService();
    }
}
