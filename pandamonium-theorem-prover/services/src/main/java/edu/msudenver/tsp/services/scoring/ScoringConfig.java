package edu.msudenver.tsp.services.scoring;

//@Configuration
//@ComponentScan
//@PropertySource("classpath:development.properties")
public class ScoringConfig {

//    @Bean
    public TheoremScoringService theoremScoringService() {
        return new TheoremScoringService();
    }
}
